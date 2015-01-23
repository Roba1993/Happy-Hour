package de.dhbw.hh.dao.h2;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import de.dhbw.hh.dao.FoursquareDAO;
import de.dhbw.hh.models.Bar;
import de.dhbw.hh.models.JSONOpeningTimes;
import de.dhbw.hh.models.Location;
import de.dhbw.hh.utils.Settings;

/**
 * Diese Klasse kann dazu verwendet werden, alle Bars innerhalb eines bestimmten 
 * Radius von Foursquare abzufragen. Alle Suchergebnisse werden als Liste von Bars 
 * zurückgegeben.
 *
 * @author Tobias Häußermann
 * @version 0.9
 */
public class H2FoursquareDAO implements FoursquareDAO{
	
	// Zugriff auf die allgemeinen Settings
	private Settings settings;
	
	// Initialisiert einen Logger für die Fehlerausgabe
    static final Logger LOG = LoggerFactory.getLogger(H2FoursquareDAO.class);
    
    // Deklaration des 4SquareCaches
    @SuppressWarnings("rawtypes")
	private static Cache otFoursquareCache;
    private static Cache rqFoursquareCache;
    
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
    
    // Zugriff auf die allgemeinen Settings wird erstellt
    public H2FoursquareDAO(){
    	settings = new Settings();
    	settings.loadDefault();
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
	 * @return ArrayList mit (unvollständigen) Bar-Objekten gefüllt
	 *
	 * @author Tobias Häußermann
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<Bar> getBarsInArea(float longitude, float latitude, float radius){
		//TODO Kommentar
		String key = latitude+","+longitude+","+radius;
		if(rqFoursquareCache.asMap().containsKey(key)){
			return (ArrayList<Bar>) rqFoursquareCache.asMap().get(key);
		}
		
		// Zu finden auf GitHub (zur Authentifizierung)
		String CLIENT_ID			= settings.getProperty("foursquare.clientID");
		String CLIENT_SECRET		= settings.getProperty("foursquare.clientSecret");		
		// Datumsangabe, da Foursquare nur aktuelle Anfragen entgegennimmt
		String VERSION				= new SimpleDateFormat("yyyyMMdd").format(new Date());
		// GPS-Location
		float LONGITUDE=0, LATITUDE	= 0;			
		// Kategorie wie "bars", "food"
		String CATEGORY				= "bar";
		// Radius in Metern
		int RADIUS					= 100;			//Radius in Metern
		
		// Füllen der Parameter Longitude, Latitude und Radius mit den Übergabewerten
		LONGITUDE = longitude;
		LATITUDE = latitude;
		if((int)(radius*1000) >= RADIUS)			//Minimalwert von 100 Metern!
			RADIUS = (int)(radius*1000);
		
		// Fertiger Query-String für eine Foursquare-Abfrage
		String query = "https://api.foursquare.com/v2/venues/explore"+
				"?client_id="		+CLIENT_ID+
				"&client_secret="	+CLIENT_SECRET+
				"&v="				+VERSION+
				"&ll="				+LATITUDE+","+LONGITUDE+
				"&query="			+CATEGORY+
				"&radius="			+RADIUS;
	
		ArrayList<Bar> bars = explore(query);
		
		for(int i=0;i<bars.size();i++){
			bars.get(i).setOpeningTimes(getOpeningTimesByID(bars.get(i).getId()));
		}
		
		rqFoursquareCache.put(key, bars);
		
		return bars;
	}
	
	/**
	 * Diese private Methode kümmert sich um die Foursquare-Abfrage und interpretiert/ konvertiert 
	 * anschließend das Ergebnis in eine ArrayList aus Bar-Objekten.
	 * 
	 * @param query Der fertige Html-Request als String
	 * @return ArrayList mit (unvollständigen) Bar-Objekten gefüllt
	 *
	 * @author Tobias Häußermann
	 */
	private ArrayList<Bar> explore(String query){
		ArrayList<Bar> bars = new ArrayList<Bar>();
		
		try{
			// Absenden eines Html-Requests mittels der Java-Funktionalität HttpURLConnection
			URL foursquare = new URL(query);
			HttpURLConnection connection = null;
			int counter = 0;
			while(connection == null || connection.getResponseCode() >= 500){
				connection	= (HttpURLConnection) foursquare.openConnection();
				counter++;
				if(counter > 3)
					continue;
			}
			
			InputStream is = connection.getInputStream();
			
			// Konvertierung der Html-Response in einen String
			@SuppressWarnings("resource")
			String response = new Scanner(is,"UTF-8").useDelimiter("\\A").next();
			
			// Interpretation des Antwort Strings durch einen JSON-Parser
			final JSONParser jsonPrs = new JSONParser();
			final JSONObject jsonObj = (JSONObject) jsonPrs.parse( response );
			
			JSONObject jsonResp = (JSONObject) jsonObj.get( "response" );
			JSONArray  jsonTmp0 = (JSONArray)  jsonResp.get("groups");
			JSONObject jsonGrps = (JSONObject) jsonTmp0.get(0);
			JSONArray  jsonTmp1 = (JSONArray)  jsonGrps.get("items");
			int size 			= jsonTmp1.size();
			
			for(int i=0;i<size;i++){
				JSONObject venue = ((JSONObject)((JSONObject) jsonTmp1.get(i)).get("venue"));
				
				// Erzeugung & Befüllung eines neuen Bar-Objekts
				Bar bar = new Bar();
				bar.setId((String) venue.get("id"));
				bar.setName((String) venue.get("name"));
				if(venue.get("rating") != null)
					bar.setRating((float)((double)venue.get("rating")));
				else 
					bar.setRating(-1f);
				if(venue.get("price") != null)
					bar.setCosts((int) (long) ((JSONObject) venue.get("price")).get("tier"));
				if(venue.get("categories") != null && ((JSONArray)venue.get("categories")).get(0) != null)
					bar.setDescription((String) ((JSONObject)((JSONArray) venue.get("categories")).get(0)).get("name"));
				bar.setImageUrl("");
				
				float lng = (float) (double) ((JSONObject) venue.get("location")).get("lng");
				float lat = (float) (double) ((JSONObject) venue.get("location")).get("lat");
				
				bar.setLocation(new Location(lng, lat));
				bar.setAdress((String) ((JSONObject) venue.get("location")).get("address"));
								
				bars.add(bar);
			}
			is.close();
			connection.disconnect();
		}catch(Exception e){
			LOG.error(e.getMessage());
		}
		
		return bars;
	}
	
	/**
	 * Bisher noch nicht implementiert, da keine Notwendigkeit für diese Funktionalität
	 * besteht.
	 *
	 * @return Diese Methode gibt nichts zurück.
	 */
	@Override
	public Bar getBarByID(String id){
		return null;
	}
	
	/**
	 * Dokumentation folgt
	 * @param id
	 * @param version
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<JSONOpeningTimes> getOpeningTimesByID(String id){
		//TODO Kommentieren
		if(otFoursquareCache.asMap().containsKey(id)){
			return (ArrayList<JSONOpeningTimes>) otFoursquareCache.asMap().get(id);
		}
		
		// Zu finden auf GitHub (zur Authentifizierung)
		String CLIENT_ID			= settings.getProperty("foursquare.clientID");
		String CLIENT_SECRET		= settings.getProperty("foursquare.clientSecret");		
		// Datumsangabe, da Foursquare nur aktuelle Anfragen entgegennimmt
		String VERSION				= new SimpleDateFormat("yyyyMMdd").format(new Date());
		
		String query = "https://api.foursquare.com/v2/venues/"+id+"/"+
				"hours"+
				"?client_id="+CLIENT_ID+
				"&client_secret="+CLIENT_SECRET+
				"&v="+VERSION;
		
		ArrayList<JSONOpeningTimes> openingTimes = new ArrayList<JSONOpeningTimes>();
		
		try{
			URL foursquare = new URL(query);
			HttpURLConnection connection = null;
			int counter = 0;
			while(connection == null || connection.getResponseCode() >= 500){
				connection	= (HttpURLConnection) foursquare.openConnection();
				counter++;
				if(counter > 3)
					continue;
			}
			
			InputStream is = connection.getInputStream();
			
			// Konvertierung der Html-Response in einen String
			@SuppressWarnings("resource")
			String response = new Scanner(is,"UTF-8").useDelimiter("\\A").next();
			
			// Interpretation des Antwort Strings durch einen JSON-Parser
			final JSONParser jsonPrs = new JSONParser();
			final JSONObject jsonObj = (JSONObject) jsonPrs.parse( response );
			
			JSONObject jsonResp = (JSONObject) jsonObj.get( "response" );
			JSONObject jsonPopu = (JSONObject)  jsonResp.get("popular");
			JSONArray  jsonTime = (JSONArray) jsonPopu.get("timeframes");
			
			if(jsonTime!=null)
				for(int i=0;i<jsonTime.size();i++){
					JSONObject element = (JSONObject) jsonTime.get(i);
					JSONArray daysArr = (JSONArray) element.get("days");
					int size = daysArr.size();
					int[] days = new int[size];
					for(int j=0;j<size;j++)
						days[j] = (int) (long) daysArr.get(j);
					
					JSONArray open = (JSONArray) element.get("open");
					if(open.size()<1)
						continue;
					String startString = (String) ((JSONObject) open.get(0)).get("start");
					String endString = (String) ((JSONObject) open.get(0)).get("end");
					endString = endString.substring(endString.indexOf("+")+1);
					
					int sH = Integer.parseInt(startString.substring(0,2));
					int sM = Integer.parseInt(startString.substring(2,4));
					int eH = Integer.parseInt(endString.substring(0,2));
					int eM = Integer.parseInt(endString.substring(2,4));
					
					String shs = ""+sH;
					String sms = ""+sM;
					String ehs = ""+eH;
					String ems = ""+eM;
					if(shs.length()<2)
						shs = "0"+shs;
					if(sms.length()<2)
						sms = "0"+sms;
					if(ehs.length()<2)
						ehs = "0"+ehs;
					if(ems.length()<2)
						ems = "0"+ems;
					
					JSONOpeningTimes ot = new JSONOpeningTimes();
					ot.setBarID(id);
					ot.setStartTime(shs+":"+sms);
					ot.setEndTime(ehs+":"+ems);
					ot.setDays(days);
					
					openingTimes.add(ot);
				}
			is.close();
			connection.disconnect();
		}catch(Exception e){
			LOG.error(e.getMessage());
		}
		
		// Füge die Öffnungszeiten zum Cache hinzu
		otFoursquareCache.put(id, openingTimes);
		
		return openingTimes;
	}
}
