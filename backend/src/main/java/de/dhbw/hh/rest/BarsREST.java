package de.dhbw.hh.rest;

import static spark.Spark.get;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.gson.Gson;
import de.dhbw.hh.dao.DAOFactory;
import de.dhbw.hh.dao.FourSquareConnection;
import de.dhbw.hh.dao.HappyHourDAO;
import de.dhbw.hh.models.Bar;
import de.dhbw.hh.models.HappyHour;import de.dhbw.hh.models.RESTResponse;

/**
 * Diese Klasse stellt die Methoden für die REST Schnittstelle für 
 * eine GET Anfrage auf den Pfad /bars bereit. 
 * 
 * @author Tobias Häußermann
 * @version 0.8
 */
public class BarsREST {
	
	static final Logger LOG = LoggerFactory.getLogger(ReportsREST.class);
	
	private Gson gson = new Gson();
	
	public BarsREST(DAOFactory daoFactory) {
		
		FourSquareConnection foursquareConnection = new FourSquareConnection();
		HappyHourDAO happyHours = daoFactory.getHappyHourDAO();
		
		/**
		 * Gibt alle Bars zurück, die zu den Suchoptionen passen.
		 * Die folgenden Parameter werden zwingend benötigt. Bei 
		 * Fehlen eines Parameters werden keine Bars zurückgegeben.
		 * X-----------------X----------------------------------X
		 * |    longitude    |    GPS-Koordinate als double     |
		 * |    latitude     |    GPS-Koordinate als double     |
		 * |    radius       |    Radius als integer            |
		 * |    weekday      |    Wochentag als Zahl von 1-7    |
		 * X-----------------X----------------------------------X
		 */
		get("/bars", "application/json", (request, response) -> {
			LOG.debug("HTTP-GET Anfrage eingetroffen: "+request.queryString());
			
			float lng = Float.parseFloat(request.queryParams("longitude"));	//			float lng = 48.957848f;
			float lat = Float.parseFloat(request.queryParams("latitude"));	//			float lat = 9.422454f;
			int rad = Integer.parseInt(request.queryParams("radius"));		//			int rad = 2000;
			int day = Integer.parseInt(request.queryParams("weekday"));
			
			ArrayList<Bar> rawBars = new ArrayList<Bar>();
			// Überprüfung ob die Abfrage oder eine Ähnliche schon einmal abgesetzt wurde
			boolean alreadyCached = isAlreadyInCache(lng, lat, rad);
			
			if(alreadyCached){
				// do nothing
			}
			else{
				rawBars = foursquareConnection.getBarsInArea(lng, lat, rad);
				int count = rawBars.size();
				for(int i=0;i<count;i++){
					String id = rawBars.get(i).getId();
					ArrayList<HappyHour> hh = (ArrayList<HappyHour>) happyHours.findHappyHour(id);
					
					// Fügt den gefundenen Bars ihre Happy-Hours aus der Datenbank hinzu. 
					rawBars.get(i).setHappyHours(hh);
				}
				//TODO activate filter as soon as happy-hour data is available
				// rawBars = filterBars(rawBars, day);
			}
			
			// REST-Response erstellen
			RESTResponse restResponse = new RESTResponse();
			restResponse.setName(request.queryString());
			restResponse.setTimestamp(new Timestamp(Calendar.getInstance().getTime().getTime()));
			restResponse.setDescription("Alles ok. Noch ergänzen");
			restResponse.setSuccess();
			restResponse.setData(rawBars);
			
			response.type("application/json");
			
			// In diesem Schritt wird die vorgefertigte REST-Response in das JSON-Format umgewandelt
			// und zurückgegeben.
			return gson.toJson(restResponse);
		});
		
	}
	
	/**
	 * Sortiert alle Bars aus, die am verlangten Tag keine Happy-Hour haben.
	 * @param rawBars
	 * @param day
	 * @return Gibt die Liste in aktualisierter Form zurück
	 */
	private ArrayList<Bar> filterBars(ArrayList<Bar> rawBars, int day) {
		
		// Schleife durch alle Bars in der übergebenen Liste
		for(int i=0;i<rawBars.size();i++){
			ArrayList<HappyHour> hhList = rawBars.get(i).getHappyHours();
			int size = hhList.size();
			boolean b = false;
			
			//Schleife durch alle Einträge in der Happy-Hour-Liste
			for(int j=0;j<size;j++){
				HappyHour hh = hhList.get(j);
				// Abgleich des aktuellen Wochentags mit den Einträgen aus der Happy-Hour-Liste
				switch(day){
					case 1: if(hh.isMonday()) b = true; break;
					case 2: if(hh.isTuesday()) b = true; break;
					case 3: if(hh.isWednesday()) b = true; break;
					case 4: if(hh.isThursday()) b = true; break;
					case 5: if(hh.isFriday()) b = true; break;
					case 6: if(hh.isSaturday()) b = true; break;
					case 7: if(hh.isSunday()) b = true; break;
				}
				if(b)
					continue;
			}
			if(!b){		
				// Falls kein Happy-Hour-Eintrag mit dem aktuellen Wochentag übereingestimmt
				// hat, so wird die Bar aus der Liste entfernt. 
				rawBars.remove(i);
				i--;
			}
		}
		return rawBars;
	}

	/**
	 * Prüfung ob die gestellte Anfrage bereits im Cache vorhanden ist.  
	 * @param lng Longitude (wird auf 100 Meter gerundet)
	 * @param lat Latitude (wird auf 100 Meter gerundet)
	 * @param rad Radius in Metern
	 * @return
	 */
	private boolean isAlreadyInCache(float lng, float lat, int rad){
		// Wird zu einem späteren Zeitpunkt umgesetzt
		return false;
	}
	
	@SuppressWarnings("unused")
	private class OpeningTimes{
		String startTime;
		String endTime;
		int[] days;
	}
}
