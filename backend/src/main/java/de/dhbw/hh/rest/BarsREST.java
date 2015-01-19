package de.dhbw.hh.rest;

import static spark.Spark.get;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import de.dhbw.hh.dao.DAOFactory;
import de.dhbw.hh.dao.HappyHourDAO;
import de.dhbw.hh.dao.h2.H2FoursquareDAO;
import de.dhbw.hh.models.Bar;
import de.dhbw.hh.models.RESTResponse;

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
		
		H2FoursquareDAO h2FoursquareDAO = (H2FoursquareDAO) daoFactory.getFoursquareDAO();
		HappyHourDAO happyHourDAO = daoFactory.getHappyHourDAO();
		
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
			
			float lng = Float.parseFloat(request.queryParams("longitude"));
			float lat = Float.parseFloat(request.queryParams("latitude"));
			float rad = Float.parseFloat(request.queryParams("radius"));	//			int rad = 2000;
			/*
			 * Folgender Parameter wird nur dann benötigt, wenn die Suchergebnisse über die Methode filterBars()
			 * auf vorhandene Happy-Hours geprüft werden müssen. 
			 * 
			 * int day = Integer.parseInt(request.queryParams("weekday"));
			 */
			
			ArrayList<Bar> rawBars = new ArrayList<Bar>();
			// Überprüfung ob die Abfrage oder eine Ähnliche schon einmal abgesetzt wurde
			boolean alreadyCached = isAlreadyInCache(lng, lat, rad);
			
			if(alreadyCached){
				// Nichts unternehmen
			}
			else{
				rawBars = h2FoursquareDAO.getBarsInArea(lng, lat, rad);
				int count = rawBars.size();
				for(int i=0;i<count;i++){
					String id = rawBars.get(i).getId();
					ArrayList<de.dhbw.hh.models.HappyHour> hh = (ArrayList<de.dhbw.hh.models.HappyHour>) happyHourDAO.findHappyHour(id);
					// Transformiert die Happy-Hours der Datenbank in Happy-Hour-Objekte, die den Schnittstellendefinitionen
					// zwischen Frontend und Backend entsprechen
					ArrayList<HappyHour> happyHours = transformHappyHours(hh);
					// Fügt den gefundenen Bars ihre Happy-Hours aus der Datenbank hinzu. 
					rawBars.get(i).setHappyHours(happyHours);
				}
				/*
				 * Der folgende Methoden-Aufruf filtert diejenigen Bars aus den Suchergebnissen aus, die am 
				 * verlangten Tag oder generell keine Happy-Hours anbieten. Zur Zeit wird die Funktionalität
				 * nicht benötigt, ist aber für zukünftige Anforderungen bereits vorbereitet. 
				 * 
				 * rawbars = filterBars(rawBars, day);
				 */
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
	@SuppressWarnings("unused")
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
				for(int k=0;k<hh.days.length;k++){
					if(hh.days[k] == day){
						b = true;
						continue;
					}
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
	private boolean isAlreadyInCache(float lng, float lat, float rad){
		// Wird zu einem späteren Zeitpunkt implementiert
		return false;
	}
	
	/**
	 * Dokumentation folg
	 *
	 */
	@SuppressWarnings("deprecation")
	private ArrayList<HappyHour> transformHappyHours(ArrayList<de.dhbw.hh.models.HappyHour> hh){
		ArrayList<HappyHour> result = new ArrayList<HappyHour>();
		
		for(int i=0;i<hh.size();i++){
			HappyHour hour = new HappyHour();
			de.dhbw.hh.models.HappyHour item = hh.get(i);
			hour.setBarID(item.getBarID());
			int sh = item.getStart().getHours();
			int sm = item.getStart().getMinutes();
			int eh = item.getEnd().getHours();
			int em = item.getEnd().getMinutes();
			String shs = ""+sh;
			String sms = ""+sm;
			String ehs = ""+eh;
			String ems = ""+em;
			if(shs.length()<2)
				shs = "0"+shs;
			if(sms.length()<2)
				sms = "0"+sms;
			if(ehs.length()<2)
				ehs = "0"+ehs;
			if(ems.length()<2)
				ems = "0"+ems;
			
			hour.setStartTime(shs+":"+sms);
			hour.setEndTime(ehs+":"+ems);
			
			ArrayList<Integer> al = new ArrayList<Integer>();
			if(item.isMonday() == true)
				al.add(1);
			if(item.isTuesday() == true)
				al.add(2);
			if(item.isWednesday() == true)
				al.add(3);
			if(item.isThursday() == true)
				al.add(4);
			if(item.isFriday() == true)
				al.add(5);
			if(item.isSaturday() == true)
				al.add(6);
			if(item.isSunday() == true)
				al.add(7);
			int[] days = new int[al.size()];
			for(int j=0;j<al.size();j++){
				days[j] = al.get(j);
			}
			
			hour.setDays(days);
			result.add(hour);
		}
		
		return result;
	}
	
	/**
	 * Dokumentation folgt
	 * @author Tobias Häußermann
	 *
	 */
	public class HappyHour{
		
		private String 	barID;
		private String 	startTime;
		private String 	endTime;
		private int[] 	days;
		
		public String getBarID() {
			return barID;
		}
		
		public void setBarID(String barID) {
			this.barID = barID;
		}
		
		public String getStartTime() {
			return startTime;
		}

		public void setStartTime(String startTime) {
			this.startTime = startTime;
		}

		public String getEndTime() {
			return endTime;
		}

		public void setEndTime(String endTime) {
			this.endTime = endTime;
		}

		public int[] getDays() {
			return days;
		}

		public void setDays(int[] days){
			this.days = days;
		}
		
		public String toString(){
			String result = "Öffnungszeiten:{\nBarID: "+barID+"\nStart: "+startTime+"\nEnde: "+endTime+"\nTage:";
			for(int i=0;i<days.length;i++){
				result += "\n"+days[i];
			}
			return result;
		}
	}
}
