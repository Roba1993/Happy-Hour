package de.dhbw.hh.dao;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import de.dhbw.hh.models.Bar;
import de.dhbw.hh.models.Location;

/**
 * Diese Klasse kann dazu verwendet werden, alle Bars innerhalb eines bestimmten 
 * Radius von Foursquare abzufragen. Alle Suchergebnisse werden als Liste von Bars 
 * zurückgegeben.
 *
 * @author Tobias Häußermann
 * @version 0.9
 */
public class FourSquareConnection {
	
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
	public ArrayList<Bar> getBarsInArea(float longitude, float latitude, int radius){
		
		String CLIENT_ID			= "";			//Zu finden auf GitHub (zur Authentifizierung)
		String CLIENT_SECRET		= "";			//Zu finden auf GitHub (zur Authentifizierung)
		String VERSION				= "";			//Datumsangabe, da Foursquare nur aktuelle Anfragen entgegennimmt
		float LONGITUDE=0, LATITUDE	= 0;			//GPS-Location
		String CATEGORY				= "";			//Kategorie wie "bars", "food"
		int RADIUS					= 0;			//Radius in Metern
		
		// Füllen der oben deklarierten Parameter für den Html-Request mit Standartwerten.
		CLIENT_ID 		= "ZNZQPW20YC1N1VERBVBAVWMN1YZX4Z0OW0IEUYBSOYO5HXTV";
		CLIENT_SECRET 	= "E5IUW33BRPBBVWO1JP4FVJ2Z4DBPLVTZVPX22QEOLNE3ZTFX";
		VERSION = new SimpleDateFormat("yyyyMMdd").format(new Date());	
		LONGITUDE 	= 48.949034f;
		LATITUDE 	= 9.431656f;
		CATEGORY 	= "bar";
		RADIUS 		= 100;
		
//		https://api.foursquare.com/v2/venues/explore?client_id=ZNZQPW20YC1N1VERBVBAVWMN1YZX4Z0OW0IEUYBSOYO5HXTV
//		&client_secret=E5IUW33BRPBBVWO1JP4FVJ2Z4DBPLVTZVPX22QEOLNE3ZTFX&v=20150107&ll=48.949034,9.431656&query=bar&radius=150
		
		LONGITUDE = longitude;
		LATITUDE = latitude;
		if(radius >= RADIUS)			//Minimalwert von 100 Metern!
			RADIUS = radius;
		
		//Fertiger Query-String für eine Foursquare-Abfrage
		String query = "https://api.foursquare.com/v2/venues/explore"+
				"?client_id="		+CLIENT_ID+
				"&client_secret="	+CLIENT_SECRET+
				"&v="				+VERSION+
				"&ll="				+LONGITUDE+","+LATITUDE+
				"&query="			+CATEGORY+
				"&radius="			+RADIUS;
	
		return explore(query);
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
			HttpURLConnection connection	= (HttpURLConnection) foursquare.openConnection();
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
				bar.setRating(-1);	//TODO
				bar.setCosts((int) (long) ((JSONObject) venue.get("price")).get("tier"));
				bar.setDescription((String) ((JSONObject)((JSONArray) venue.get("categories")).get(0)).get("name"));
				bar.setImageUrl("");	//TODO
				
				float lng = (float) (double) ((JSONObject) venue.get("location")).get("lng");
				float lat = (float) (double) ((JSONObject) venue.get("location")).get("lat");
				
				bar.setLocation(new Location(lng, lat));
				bar.setAdress((String) ((JSONObject) venue.get("location")).get("address"));
								
				bars.add(bar);
			}
			is.close();
			connection.disconnect();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return bars;
	}
	
	/**
	 * Bisher noch nicht implementiert, da keine Notwendigkeit für diese Funktionalität
	 * besteht.
	 *
	 * @return Diese Methode gibt nichts zurück.
	 */
	public Bar getBarByID(String id){
		return null;
	}	
}
