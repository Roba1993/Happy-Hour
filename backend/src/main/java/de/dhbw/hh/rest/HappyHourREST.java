package de.dhbw.hh.rest;

import com.google.gson.Gson;

import de.dhbw.hh.dao.DAOFactory;
import de.dhbw.hh.models.HappyHour;
import de.dhbw.hh.models.RESTResponse;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static spark.Spark.*;


/**
 * Diese Klasse stellt die Methoden für die REST Schnittstelle
 * zum hinzufügen und ändern von Happy Hour Zeiten bereit
 *
 * @author Marcus
 */
public class HappyHourREST {
	
	static final Logger LOG  = LoggerFactory.getLogger(HappyHourREST.class);
	// Gson Objekt zum umwandeln in json
    private Gson gson = new Gson();

    public HappyHourREST(DAOFactory daoFactory)
	    {
    	/**
		 * Trage neuen HappyHour in DB ein
		 *
		 * @author Marcus
		 */
		post("/bars/:barID/hour",
				"application/json",
				(request, response) -> {
					LOG.debug("HTTP-POST Anfrage eingetroffen: " + request.body());

					// Schreibe Anfrageparameter in neues HappyHour Objekt
					HappyHour HappyHour = new HappyHour();
					HappyHour.setBarID(request.params("barID"));
					HappyHour.setDescription(request.body());
					
					//Konvertieren des String mit Format "hh:mm:ss" in ein Java-Time-Objekt
					String start = request.queryParams("start");
					HappyHour.setStart(Time.valueOf(start));
					
					//Konvertieren des String mit Format "hh:mm:ss" in ein Java-Time-Objekt
					String end = request.queryParams("end");
					HappyHour.setEnd(Time.valueOf(end));
				
					//Abholen des Tages-Array als String
					String days = request.queryParams("days");
					
					//Prüfen ob Montag im Tages-Array vorhanden ist
					if (days.indexOf(1) > -1){
						HappyHour.setMonday(true);
					}
					
					//Prüfen ob Dienstag im Tages-Array vorhanden ist
					if (days.indexOf(2) > -1){
						HappyHour.setTuesday(true);
					}
					
					
					//Prüfen ob Mittwoch im Tages-Array vorhanden ist
					if (days.indexOf(3) > -1){
						HappyHour.setWednesday(true);
					}
					
					//Prüfen ob Donnerstag im Tages-Array vorhanden ist
					if (days.indexOf(4) > -1){
						HappyHour.setThursday(true);
					}
					
					//Prüfen ob Freitag im Tages-Array vorhanden ist
					if (days.indexOf(5) > -1){
						HappyHour.setFriday(true);
					}
					
					//Prüfen ob Samstag im Tages-Array vorhanden ist
					if (days.indexOf(6) > -1){
						HappyHour.setSaturday(true);
					}
					
					//Prüfen ob Sonntag im Tages-Array vorhanden ist
					if (days.indexOf(7) > -1){
						HappyHour.setSunday(true);
					}
					
					
					// Schreibe neue HappyHour in DB
					boolean successfull = daoFactory.getHappyHourDAO().insertHappyHour(HappyHour);

					// Das Rückgabeobjekt wird erstellt
					RESTResponse restResponse = new RESTResponse();

					// Das Rückgabeobjekt wird befüllt
					restResponse.setName(request.queryString());
					restResponse.setTimestamp(new Timestamp(Calendar.getInstance().getTime().getTime()));
					if (successfull) {
						restResponse.setDescription("HapyHour erfolgreich in DB geschrieben");
						restResponse.setSuccess();
					} else {
						restResponse.setDescription("Es gab einen Fehler beim Schreiben der HappyHour in die DB");
						restResponse.setError();
					}
					restResponse.setData(null);

					response.type("application/json");
					return gson.toJson(restResponse);
				});
		
		
	    	/**
	    	 * Lösche Happy Hour anhand Happy Hour ID aus der Datenbank
			 *
	    	 * @author Jonas
	    	 */
			delete("/delHour/:hourID", "application/json", (request, response) -> {
				LOG.debug("HTTP-DELETE Anfrage eingetroffen: " + request.queryString());

				
				// Lösche HappyHour aus DB
				int hourID = Integer.parseInt(request.params("hourID"));
				boolean successfull = daoFactory.getHappyHourDAO().deleteHappyHour(hourID);

				// Das Rückgabeobjekt wird erstellt
				RESTResponse restResponse = new RESTResponse();

				// Das Rückgabeobjekt wird befüllt
				restResponse.setName(request.queryString());
				restResponse.setTimestamp(new Timestamp(Calendar.getInstance().getTime().getTime()));
				if (successfull) {
					restResponse.setDescription("HapyHour erfolgreich aus DB gelöscht");
					restResponse.setSuccess();
				} else {
					restResponse.setDescription("Es gab einen Fehler beim Löschen der HappyHour aus der DB");
					restResponse.setError();
				}
				restResponse.setData(null);

				LOG.debug(restResponse.getStatus() + restResponse.getDescription() + restResponse.getData());
				response.type("application/json");
				return gson.toJson(restResponse);
				
			});
	    }

}