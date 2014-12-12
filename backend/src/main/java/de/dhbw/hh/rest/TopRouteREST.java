package de.dhbw.hh.rest;

import java.sql.Timestamp;

import java.util.Date;

import com.google.gson.Gson;




import de.dhbw.hh.dao.DAOFactory;
import de.dhbw.hh.models.RESTResponse;
import de.dhbw.hh.models.ReportsMessage;
import de.dhbw.hh.models.Route;
import de.dhbw.hh.models.Testrun;
import static spark.Spark.*;


/**Diese Klasse stellt die Methoden für die REST-Schnittstelle 
 * für den Pfad /toproutes bereit
 * 
 * @author: Maren
 */

public class TopRouteREST {
	
	// Gson Objekt zum umwandeln in json
	
	private Gson gson = new Gson();

	public TopRouteREST (DAOFactory daoFactory){
		
		/**
		 * Gibt die TopRouten über die REST Schnittstelle zurück 
		 */
		get("/toproutes", "application/json", (request, response) -> {
			Route[] toproutes = daoFactory.getRouteDAO().findTopRoutes();
			
			RESTResponse r = new RESTResponse();
			r.setSuccess();
			r.setData(toproutes);
			r.setDescription("Rückgabe der toproutes als Array");
			r.setName("/toproutes");
			r.setTimestamp(new Timestamp(new Date().getTime()));
			
			
			
			return gson.toJson(r);
		});
		
	}

	
}
