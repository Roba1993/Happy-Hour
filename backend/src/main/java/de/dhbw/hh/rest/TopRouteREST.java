package de.dhbw.hh.rest;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;

import com.google.gson.Gson;

import de.dhbw.hh.dao.DAOFactory;
import de.dhbw.hh.models.RESTResponse;
import de.dhbw.hh.models.Route;
import static spark.Spark.*;


/**Diese Klasse stellt die Methoden für die REST Schnittstelle 
 * für den Pfad /toproutes bereit
 * 
 * @author: Maren
 */
public class TopRouteREST {
	
	// Gson Objekt zum umwandeln in json	
	private Gson gson = new Gson();
	
	/**
	 * Diese Funktion ist ein Konstruktor, um REST Schnittstelle für die Top Routen zu definieren
	 * @param daoFactory
	 */
	public TopRouteREST (DAOFactory daoFactory){
		
		/**
		 * Gibt die TopRouten über die REST Schnittstelle zurück 
		 */
		get("/toproutes", "application/json", (request, response) -> {
			// Holt die Top Routen aus der Datenbank
			Collection<Route> toproutes = daoFactory.getRouteDAO().findTopRoutes();
			
			// Es wird ein Rückgabe Objekt erstellt
			RESTResponse r = new RESTResponse();
			// Setzt den Pfad der Abfrage
			r.setName("/toproutes");
			// Setzt die aktuelle Zeit der Rückgabe
			r.setTimestamp(new Timestamp(new Date().getTime()));
			
			// Überprüfung, ob die Top Routen abgerufen werden können
			if (toproutes.isEmpty() ){
				// Es wurden keine Top Routen gefunden
				r.setError();
				r.setDescription("Es konnten keine Top Routen aus der Datenbank abgerufen werden");
				r.setData(null);
			} 
			else {
				// Die Top Routen konnten gefunden werden
				r.setSuccess();
				r.setDescription("Rückgabe der top Routes als Array");
				r.setData(toproutes);
			}	
					
			// Übergibt das REST Objekt als Json String zur Anfrage zurück 
			return gson.toJson(r);
		});
		
	}

	
}
