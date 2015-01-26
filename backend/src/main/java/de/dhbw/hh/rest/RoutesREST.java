package de.dhbw.hh.rest;

import static spark.Spark.post;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import de.dhbw.hh.dao.DAOFactory;
import de.dhbw.hh.models.RESTResponse;
import de.dhbw.hh.models.Route;
import de.dhbw.hh.utils.HashConverter;
import static spark.Spark.get;

/**
 * Diese Klasse stellt eine POST-Methode zur Verfügung, mit der eine Route in
 * die Datenbank eingepflegt werden kann, sowie eine GET-Methode,
 * mit der eine Route mit dem Hash-Wert aus der Datenbank 
 * abgerufen werden kann
 * @author: Tabea
 */
public class RoutesREST {
	
	// Initialisiert einen Logger für die Fehlerausgabe
	static final Logger LOG = LoggerFactory.getLogger(RoutesREST.class);

	// Erstellt ein Gson-Objekt für die Rückgabe
	private Gson gson = new Gson();

	/**
	 * Diese Funktion ist ein Konstruktor, um REST Schnittstellen für die Routen zu definieren
	 * @param daoFactory
	 */
	public RoutesREST(DAOFactory daoFactory) {

		/**
		 * @author Michael
		 */
		post("/routes", "application/json", (request, response) -> {
			// Erstellt Log-Eintrag bei Zugriff auf den Pfad /routes
			LOG.debug("HTTP-POST Anfrage eingetroffen: " + request.queryString());
			// Neues Route Objekt erstellen
			Route route = new Route();

			// Route mit Daten aus POST Anfrage befüllen
			String temp = request.body();
			route.setData(temp);

			// Hashwert bilden und der Route zuweisen
			String hash = HashConverter.md5(temp);
			route.setHash(hash);

			// Route ist keine Top Route
			route.setTop(false);

			// RESTRespone mit Rückgabedaten befüllen
			RESTResponse restResponse = new RESTResponse();
			restResponse.setName("/routes");
			restResponse.setTimestamp(new Timestamp(Calendar.getInstance().getTime().getTime()));
			restResponse.setData(hash);
			
			// Route in Datenbank speichern
			boolean successfull = daoFactory.getRouteDAO().insertRoute(route);
			
			if(successfull){
				// Wenn erfolgreich gespeichert
				restResponse.setDescription("Route erfolgreich hinzugefügt");
				restResponse.setSuccess();
			}else{
				// Wenn nicht erfolgreich gespeichert
				restResponse.setDescription("Fehler beim Hinzufügen der Route");
				restResponse.setError();
			}
			// Log-Eintrag bei Rückgabe
			LOG.debug(restResponse.getStatus() + restResponse.getDescription());

			// Übergibt das REST Objekt als Json String zur Anfrage zurück 
			response.type("application/json");
			return gson.toJson(restResponse);
		});

		/**
 		* Gibt die Route über die REST Schnitstelle zurück
 		*
 		* @author Tabea
 		*/
		get("/routes/:hash", "application/json", (request, response) -> {
			// Erstellt Log-Eintrag bei Zugriff auf den Pfad /routes/:hash
			LOG.debug("HTTP-GET Anfrage eingetroffen: " + request.queryString());			
			
			// Routen aus der Datenbank holen
			Route hashRoute = daoFactory.getRouteDAO().findRoute(request.params("hash"));
			
			// Es wird ein Rückgabe Objekt erstellt
			RESTResponse r = new RESTResponse();
			// Das Rückgabeobjekt wird befüllt
			r.setName(request.queryString());
			r.setTimestamp(new Timestamp(new Date().getTime()));

			String out;
			
			//Überprüfen, ob die Route abgerufen werden kann
			if(hashRoute == null) {
				//Es wurde keine Route gefunden
				r.setDescription("Keine Route gefunden");
				r.setError();
				r.setData(null);

				out = gson.toJson(r);
			} else {
				//Route wurde gefunden
				r.setDescription("Folgende Route wurde gefunden");
				r.setSuccess();

				// change to gson string to return the route data in the right format
				out = gson.toJson(r);
				out = out.substring(0, out.length()-1);
				out += ", \"data\": " + hashRoute.getData() + "}";
			}
			
			// Log-Eintrag bei Rückgabe
			LOG.debug(r.getStatus() + r.getDescription());
			
			// Übergibt das REST Objekt als Json String zur Anfrage zurück
			response.type("application/json");
			return out;
		});
	}
	
}
