package de.dhbw.hh.dao.h2;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import com.google.common.primitives.Ints;
import de.dhbw.hh.dao.DAOFactory;
import de.dhbw.hh.models.*;
import de.dhbw.hh.models.HappyHour;
import de.dhbw.hh.utils.HashConverter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import de.dhbw.hh.dao.FoursquareDAO;
import de.dhbw.hh.utils.Settings;

/**
 * Diese Klasse kann dazu verwendet werden, alle Bars innerhalb eines bestimmten 
 * Radius von Foursquare abzufragen. Alle Suchergebnisse werden als Liste von Bars 
 * zurückgegeben.
 *
 * @author Tobias Häußermann
 * @author2 Robert Schütte
 */
public class H2FoursquareDAO implements FoursquareDAO{

	// Initialisiert einen Logger für die Fehlerausgabe
	static final Logger LOG = LoggerFactory.getLogger(H2FoursquareDAO.class);
	// Deklaration des 4SquareCaches
	@SuppressWarnings("rawtypes")
	private static Cache otFoursquareCache;
	private static Cache rqFoursquareCache;
	// Zugriff auf die allgemeinen Settings
	private Settings settings;
	private DAOFactory daoFactory;

	// Zugriff auf die allgemeinen Settings wird erstellt
	public H2FoursquareDAO(Settings settings, DAOFactory daoFactory){
		this.settings = settings;
		this.daoFactory = daoFactory;
	}

	// Initialisierung des 4SquareCaches
	public static void initCache(Settings settings){
		otFoursquareCache = CacheBuilder.newBuilder()
				.maximumSize(10000)
				.expireAfterWrite(Long.parseLong((String)settings.get("foursquare.otCacheTimer")), TimeUnit.MINUTES)
				.build();
		rqFoursquareCache = CacheBuilder.newBuilder()
				.maximumSize(10000)
				.expireAfterWrite(Long.parseLong((String)settings.get("foursquare.rqCacheTimer")), TimeUnit.MINUTES)
				.build();
	}

	/**
	 * Diese Methode liefert gegen eine Liste von Parametern einen Array aus Bar-Objekten
	 * zurück. Die Methode nutzt ihrerseits die private Methode {@code explore(String query)}
	 * um Ergebnisse zu bekommen. Die Suchergebnisse können durch folgende Parameter 
	 * eingegrenzt werden:
	 *
	 * @param longitude GPS-Location Longitude als float
	 * @param latitude GPS-Location Latitude als float
	 * @param radius Radius in Metern
	 * @param weekday Wochentag im Bereich von 1-7
	 * @return ArrayList mit (unvollständigen) Bar-Objekten gefüllt
	 *
	 * @author Tobias Häußermann
	 * @author2 Robert Schütte
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<Bar> getBarsInArea(float longitude, float latitude, float radius, int weekday){
		// Erstelle einen Hash aus den übergebenen Werten
		String hashKey = HashConverter.md5(latitude+","+longitude+","+radius+","+weekday);

		// Überprüfe ob für diesen Hash bereits Bars im Cache enhalten ist
		if(rqFoursquareCache.asMap().containsKey(hashKey)){
			// Falls Bars zu diesem Hash existieren, gebe diese zurück
			return (ArrayList<Bar>) rqFoursquareCache.asMap().get(hashKey);
		}

		// Foursquare benötigt die Radius angabe in Metern
		int rad =  (int) (radius * 1000);

		// Hole alle Bars von Foursqare für die angegebene Position und Radius
		ArrayList<Bar> bars = getBarsForPositions(latitude, longitude, rad);

		// Überarbeite alle Bars
		for(Bar bar : bars) {
			// Die Bar mit Öffnungszeiten befüllen
			bar.setOpeningTimes(getOpeningTimesByID(bar.getId()));

			// Die Bar mit Happy Hour Werten befüllen
			bar.setHappyHours(transformHappyHours((ArrayList<HappyHour>) daoFactory.getHappyHourDAO().findHappyHour(bar.getId())));
		}

		// Füge die Rückgabe den Cache hinzu
		rqFoursquareCache.put(hashKey, bars);
		return bars;
	}

	/**
	 * Diese private Methode kümmert sich um die Foursquare-Abfrage und interpretiert/ konvertiert 
	 * anschließend das Ergebnis in eine ArrayList aus Bar-Objekten.
	 *
	 * @param latitude Latitude der Position
	 * @param longitude Longitude der Position
	 * @param radius Umkreis der Abgefragten Position
	 * @return ArrayList mit (unvollständigen) Bar-Objekten gefüllt
	 *
	 * @author Tobias Häußermann
	 * @author2 Robert Schütte
	 */
	private ArrayList<Bar> getBarsForPositions(float latitude, float longitude, int radius){
		// Rückgabe Variable
		ArrayList<Bar> bars = new ArrayList<Bar>();

		try{
			// Query-String für die Foursquare-Abfrage
			String query = "https://api.foursquare.com/v2/venues/explore"+
					"?client_id="		+ settings.getProperty("foursquare.clientID") +
					"&client_secret="	+ settings.getProperty("foursquare.clientSecret") +
					"&v="				+ new SimpleDateFormat("yyyyMMdd").format(new Date()) +
					"&ll="				+latitude+","+longitude+
					"&query="			+ "bar" +
					"&radius="			+radius;

			// Absenden eines Html-Requests mittels der Java-Funktionalität HttpURLConnection
			URL url = new URL(query);
			HttpURLConnection connection = null;

			// Bis zu 3 Versuche um die Daten von Foursquare zu bekommen
			int counter = 0;
			while(connection == null || connection.getResponseCode() != 200){
				connection	= (HttpURLConnection) url.openConnection();
				counter++;
				if(counter > 3)
					continue;
			}

			// Lese den String aus der Connection
			InputStream is = connection.getInputStream();
			@SuppressWarnings("resource")
			String response = new Scanner(is,"UTF-8").useDelimiter("\\A").next();
			is.close();
			connection.disconnect();

			// Wandel den Rückgabe String in ein JSON Objekt um
			JSONObject jObject = (JSONObject) new JSONParser().parse(response);

			// Hole die benötigten Daten aus dem Array
			JSONObject jResponse = (JSONObject) jObject.get( "response" );
			JSONArray  jGroups = (JSONArray) jResponse.get("groups");
			JSONObject jGroup0 = (JSONObject) jGroups.get(0);
			JSONArray  jItems = (JSONArray) jGroup0.get("items");

			for(Object i : jItems){
				// Abstrakhiere das JSON Bar Objekt
				JSONObject jBar = (JSONObject) ((JSONObject) i).get("venue");

				// Erstellen eines neuen Java Bar-Objektes
				Bar bar = new Bar();
				bar.setId((String) jBar.get("id"));
				bar.setName((String) jBar.get("name"));
				bar.setAdress((String) ((JSONObject) jBar.get("location")).get("address"));
				// setzte die Bewertung der Bar
				if(jBar.get("rating") != null) {
					bar.setRating((float) ((double) jBar.get("rating")));
				}
				else {
					bar.setRating(-1);
				}
				// setze den Preis der Bar
				if(jBar.get("price") != null) {
					bar.setCosts((int) (long) ((JSONObject) jBar.get("price")).get("tier"));
				}
				else {
					bar.setCosts(-1);
				}
				// setzte die Katregorie der Bar
				if(jBar.get("categories") != null && ((JSONArray)jBar.get("categories")).get(0) != null) {
					bar.setDescription((String) ((JSONObject) ((JSONArray) jBar.get("categories")).get(0)).get("name"));
				}
				// setzte die Bar Position
				float lng = (float) (double) ((JSONObject) jBar.get("location")).get("lng");
				float lat = (float) (double) ((JSONObject) jBar.get("location")).get("lat");
				bar.setLocation(new Location(lng, lat));

				// Füge die Bar dem Array hinzu
				bars.add(bar);
			}
		}catch(Exception e){
			LOG.error(e.getMessage());
		}

		// Gebe die gefunden Bars zurück
		return bars;
	}

	/**
	 * Funktion um alle Öffnungszeiten einer einzelnen Bar über ihre ID von Foursquare abzurufen
	 *
	 * @param barId Eindeutige ID als Grundlage für die Suche
	 * @return Liste an Öffnungszeiten für eine Bar
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<JSONOpeningTime> getOpeningTimesByID(String barId){
		// Überprüfe ob die Öffnungszeit für die Angefragte Bar bereits im Cache ist
		if(otFoursquareCache.asMap().containsKey(barId)){
			// Wenn die Zeit gecacht ist, gebe diese zurück
			return (ArrayList<JSONOpeningTime>) otFoursquareCache.asMap().get(barId);
		}

		// Erstelle die ArrayList zur Rückgabe der Öffnungszeiten
		ArrayList<JSONOpeningTime> openingTimes = new ArrayList<JSONOpeningTime>();

		try{
			// Erstelle den Foursquare abfrage String
			String query = "https://api.foursquare.com/v2/venues/"+ barId + "/" +
					"hours" +
					"?client_id="+ settings.getProperty("foursquare.clientID") +
					"&client_secret=" + settings.getProperty("foursquare.clientSecret") +
					"&v=" + new SimpleDateFormat("yyyyMMdd").format(new Date());

			// Hole die Daten von Foursquare
			URL foursquare = new URL(query);
			HttpURLConnection connection = null;
			int counter = 0;
			// 3 Versuche die Daten abzufragen
			while(connection == null || connection.getResponseCode() != 200){
				connection	= (HttpURLConnection) foursquare.openConnection();
				counter++;
				if(counter > 3)
					continue;
			}

			// Konvertierung der Html-Response in einen String
			InputStream is = connection.getInputStream();
			@SuppressWarnings("resource")
			String response = new Scanner(is,"UTF-8").useDelimiter("\\A").next();
			is.close();
			connection.disconnect();

			// Interpretation des Antwort Strings durch einen JSON-Parser
			final JSONObject jsonObj = (JSONObject) new JSONParser().parse(response);

			// Extrahiere das benötigte JSON Objekt
			JSONObject jsonResp = (JSONObject) jsonObj.get( "response" );
			JSONObject jsonPopu = (JSONObject)  jsonResp.get("popular");
			JSONArray  jsonTimes = (JSONArray) jsonPopu.get("timeframes");

			// Es sollte ein Time Object enhaten sein
			if(jsonTimes!=null)
				// Jedes Time Objekt beseitzt mehrere Öffnungszeiten
				for(int i=0;i<jsonTimes.size();i++){
					// Extrahiere die Öffnungszeit für den Tag
					JSONObject jsonTime = (JSONObject) jsonTimes.get(i);
					JSONArray daysArr = (JSONArray) jsonTime.get("days");

					// Lade die Tage in ein Array
					int[] days = new int[daysArr.size()];
					for(int j=0;j<daysArr.size();j++) {
						days[j] = (int) (long) daysArr.get(j);
					}

					// Extrahiere die Öffnungs- und Schließzeiten
					JSONArray open = (JSONArray) jsonTime.get("open");
					if(open.size() < 1) {
						continue;
					}
					String startString = (String) ((JSONObject) open.get(0)).get("start");
					String endString = (String) ((JSONObject) open.get(0)).get("end");

					// Entferne das vorstehende + Zeichen
					endString = endString.substring(endString.indexOf("+")+1);

					// Forme die Zeiten um
					startString = new StringBuffer(startString).insert(2, ":").toString();
					endString = new StringBuffer(endString).insert(2, ":").toString();

					// Erstelle eine neues Öffnungszeiten Objekt
					JSONOpeningTime ot = new JSONOpeningTime();
					ot.setBarID(barId);
					ot.setStartTime(startString);
					ot.setEndTime(endString);
					ot.setDays(days);

					// Füge die Öffnungszeiten dem Rückgabe Array hinzu
					openingTimes.add(ot);
				}

		}catch(Exception e){
			LOG.error(e.getMessage());
		}

		// Füge die Öffnungszeiten zum Cache hinzu
		otFoursquareCache.put(barId, openingTimes);

		return openingTimes;
	}

	/**
	 * Diese Funktion wandelt ein Liste von Happy Hour
	 * Objekten in eine Liste mit JSONHappyHoure Objekten
	 * um. Diese funktionalität wird für die richtige
	 * Rest-Rückgabe benötigt.
	 *
	 * @param happyHours ArrayList mit den Happy Hours
	 * @return ArrayList mit den JSON Happy Hours
	 */
	@SuppressWarnings("deprecation")
	private ArrayList<JSONHappyHour> transformHappyHours(ArrayList<HappyHour> happyHours) {
		ArrayList<JSONHappyHour> result = new ArrayList<JSONHappyHour>();

		// Forme alle Happy Hour Zeiten um
		for(HappyHour hh : happyHours) {
			// Übertrage die Standart Werte
			JSONHappyHour jsonHH = new JSONHappyHour();
			jsonHH.setBarID(hh.getBarID());
			jsonHH.setDescription(hh.getDescription());
			jsonHH.setStartTime(new SimpleDateFormat("hh:mm").format(hh.getStart()));
			jsonHH.setEndTime(new SimpleDateFormat( "hh:mm" ).format(hh.getEnd()));

			// Überprüfe für welche Tage die Happy Hour gilt
			ArrayList<Integer> al = new ArrayList<Integer>();
			if (hh.isMonday())
				al.add(1);
			if (hh.isTuesday())
				al.add(2);
			if (hh.isWednesday())
				al.add(3);
			if (hh.isThursday())
				al.add(4);
			if (hh.isFriday())
				al.add(5);
			if (hh.isSaturday())
				al.add(6);
			if (hh.isSunday())
				al.add(7);

			// Wandel die ArrayList in ein Array um
			int[] days = Ints.toArray(al);
			jsonHH.setDays(days);

			// Füge die Happy Hour Zeit der Rückgabe Liste hinzu
			result.add(jsonHH);
		}

		// Gebe die Happy Hour Zeiten zurück
		return result;
	}

}
