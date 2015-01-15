package de.dhbw.hh.rest;

import static spark.Spark.post;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
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
 */
public class RoutesREST {
	
	// Initialisiert einen Logger für die Fehlerausgabe
	static final Logger LOG = LoggerFactory.getLogger(RoutesREST.class);

	// Erstellt ein Gson-Objekt für die Rückgabe
	private Gson gson = new Gson();

	public RoutesREST(DAOFactory daoFactory) {

		/**
		 * @author Michael
		 */
		post("/routes", "application/json", (request, response) -> {
			LOG.debug("HTTP-POST Anfrage eingetroffen: " + request.queryString());
			// Neue Route erstellen
			Route route = new Route();

			// Route mit Daten aus POST Anfrage befüllen
			String temp = request.queryParams("route");
			route.setData(temp);

			// Hashwert bilden
			String hash = HashConverter.md5(temp);
			route.setHash(hash);

			// Route ist keine Top Route
			route.setTop(false);

			// Hash in Rückgabeobjekt einfügen
			Collection<Object> data = new ArrayList<Object>();
			data.add(hash);

			// RESTRespone mit Rückgabedaten befüllen
			RESTResponse restResponse = new RESTResponse();
			restResponse.setName("/routes");
			restResponse.setTimestamp(new Timestamp(Calendar
					.getInstance().getTime().getTime()));
			restResponse.setData(data);
			
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
			
			response.type("application/json");
			return gson.toJson(restResponse);

		});

		/**
 		* Gibt die Route über die REST Schnitstelle zurück
 		*
 		* @author Tabea
 		*/
		get("/routes/:hash", "application/json", (request, response) -> {
			LOG.debug("HTTP-GET Anfrage eingetroffen: " + request.queryString());			
			
			// Routen aus der Datenbank holen
			Route HashRoute = daoFactory.getRouteDAO().findRoute(request.params("hash"));
			
			// Es wird ein Rückgabe Objekt erstellt
			RESTResponse r = new RESTResponse();
			// Das Rückgabeobjekt wird befüllt
			r.setName(request.queryString());
			r.setTimestamp(new Timestamp(new Date().getTime()));
			
			//Überprüfen, ob die Route abgerufen werden kann
			System.out.println(HashRoute.getData());
			if(HashRoute.isHashEmpty()) {
				//Es wurde keine Route gefunden
				r.setDescription("Keine Route gefunden");
				r.setError();
				r.setData(null);				
			} else {
				//Route wurde gefunden
				r.setDescription("Folgende Route wurde gefunden");
				r.setSuccess();
				
				//Collection erstellen und HashRoute hinzufügen um Methode setData() verwenden zu können
				Collection<Object> data = new ArrayList<Object>();
				data.add(HashRoute);
				
				r.setData(data);
			}
			
			// Log-Eintrag bei Rückgabe
			LOG.debug(r.getStatus() + r.getDescription() + r.getData());
			
			// Übergibt das REST Objekt als Json String zur Anfrage zurück
			response.type("application/json");
			return gson.toJson(r);
		});
	}
	
}
