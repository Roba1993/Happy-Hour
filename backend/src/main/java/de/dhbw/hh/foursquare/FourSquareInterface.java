package de.dhbw.hh.foursquare;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import de.dhbw.hh.models.Bar;
import de.dhbw.hh.models.Location;

public class FourSquareInterface {
	
	/**
	 * Method to get all bars in an area with the defined radius. Data is found 
	 * through the Foursquare-functionality "Explore"
	 * @param longitude GPS-Location Longitude as float
	 * @param latitude GPS-Location Latitude as float
	 * @param radius radius in meters
	 * @author Tobias Häußermann
	 */
	public ArrayList<Bar> getBarsInArea(float longitude, float latitude, int radius){
		
		String CLIENT_ID			= "";
		String CLIENT_SECRET		= "";
		String VERSION				= "";
		float LONGITUDE=0,LATITUDE	= 0;
		String CATEGORY				= "";
		int RADIUS					= 0;
		
		VERSION = "20141217";		// e.g. 23122014
		CATEGORY = "bar";
		LONGITUDE = 48.949034f;
		LATITUDE = 9.431656f;
		CLIENT_ID = "ZNZQPW20YC1N1VERBVBAVWMN1YZX4Z0OW0IEUYBSOYO5HXTV";
		CLIENT_SECRET = "E5IUW33BRPBBVWO1JP4FVJ2Z4DBPLVTZVPX22QEOLNE3ZTFX";
		RADIUS = 100;
		
		LONGITUDE = longitude;
		LATITUDE = latitude;
		if(radius >= RADIUS)
			RADIUS = radius;
		
		String query = "https://api.foursquare.com/v2/venues/explore"+
				"?client_id="+CLIENT_ID+
				"&client_secret="+CLIENT_SECRET+
				"&v="+VERSION+
				"&ll="+LONGITUDE+","+LATITUDE+
				"&query="+CATEGORY+
				"&radius="+RADIUS;
	
		return explore(query);
	}
	
	/**
	 * This method manages the service-call to Foursquare and handles/converts the response. 
	 * @param query The complete Html-request
	 * @return An Array with all found bars.
	 * @author Tobias Häußermann
	 */
	private ArrayList<Bar> explore(String query){
		ArrayList<Bar> bars = new ArrayList<Bar>();
		
		try{
			URL foursquare 		= new URL(query);
			HttpURLConnection connection	= (HttpURLConnection) foursquare.openConnection();
			InputStream is 		= connection.getInputStream();
			@SuppressWarnings("resource")
			String response 	= new Scanner(is,"UTF-8").useDelimiter("\\A").next();
			
			final JSONParser jsonPrs = new JSONParser();
			final JSONObject jsonObj = (JSONObject) jsonPrs.parse( response );
			
			JSONObject jsonResp = (JSONObject) jsonObj.get( "response" );
			JSONArray jsonTmp0 = (JSONArray) jsonResp.get("groups");
			JSONObject jsonGrps = (JSONObject) jsonTmp0.get(0);
			JSONArray jsonTmp1 = (JSONArray) jsonGrps.get("items");
			int size = jsonTmp1.size();
			
			for(int i=0;i<size;i++){
				JSONObject venue = ((JSONObject)((JSONObject) jsonTmp1.get(i)).get("venue"));
				
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
	 * Not implemented yet for there is no need for this functionality.
	 */
	public void getBarByID(){
		
	}	
}
