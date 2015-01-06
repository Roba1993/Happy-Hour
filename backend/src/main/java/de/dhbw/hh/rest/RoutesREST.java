package de.dhbw.hh.rest;

import static spark.Spark.post;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
import static spark.Spark.get;

/**
 * Diese Klasse stellt eine POST-Methode zur Verfügung, mit der eine Route in
 * die Datenbank eingepflegt werden kann, sowie eine GET-Methode, mit der eine Route mit dem Hash-Wert aus der Datenbank 
 * abgerufen werden kann
 */
public class RoutesREST {

/**
 * @author Michael	
 */
	
	static final Logger LOG = LoggerFactory.getLogger(RoutesREST.class);

	private Gson gson = new Gson();

	public RoutesREST(DAOFactory daoFactory) {

		post("/routes", "application/json", (request, response) -> {

			LOG.debug("HTTP-POST Anfrage eingetroffen: " + request.queryString());

			Route route = new Route();

			String temp = request.queryParams("route");
			route.setData(temp);

			// Hashwert bilden
			String hash = getHashfromString(temp);

			route.setHash(hash);

			route.setTop(false);

			Collection<Object> data = new ArrayList<Object>();
			data.add(hash);

			RESTResponse restResponse = new RESTResponse();
			restResponse.setName("/routes");
			
			restResponse.setTimestamp(new Timestamp(Calendar
					.getInstance().getTime().getTime()));
			restResponse.setData(data);
			
			boolean successfull = daoFactory.getRouteDAO().insertRoute(route);
			
			if(successfull){
				restResponse.setDescription("Route erfolgreich hinzugefügt");
				restResponse.setSuccess();
			}else{
				restResponse.setDescription("Fehler beim Hinzufügen der Route");
				restResponse.setError();
			}
			
			restResponse.setData(null);
			response.type("application/json");
			return gson.toJson(restResponse);

		});

/**
 * Gibt die Route über die REST Schnitstelle zurück
 * 
 * @author Tabea		
 */
		
		get("/routes/:hash", "application/json", (request, response) -> {
			// Routen aus der Datenbank holen
			Route HashRoute = daoFactory.getRouteDAO().findRoute(request.queryString());
			
			// Es wird ein Rückgabe Objekt erstellt
			RESTResponse r = new RESTResponse();
			// Das Rückgabeobjekt wird befüllt
			r.setName(request.queryString());
			r.setTimestamp(new Timestamp(new Date().getTime()));
			
			//Überprüfen, ob die Route abgerufen werden kann
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
			// Übergibt das REST Objekt als Json String zur Anfrage zurück
			response.type("application/json");
			return gson.toJson(r);
		});

	}

	public String getHashfromString(String s) {
		StringBuffer sb = null;

		try {
			String original = s;
			MessageDigest md;

			md = MessageDigest.getInstance("MD5");

			md.update(original.getBytes());
			byte[] digest = md.digest();
			sb = new StringBuffer();
			for (byte b : digest) {
				sb.append(String.format("%02x", b & 0xff));
			}

			// System.out.println("original:" + original);
			// System.out.println("digested(hex):" + sb.toString());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();
	}

}
