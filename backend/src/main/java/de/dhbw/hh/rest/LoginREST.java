package de.dhbw.hh.rest;

import java.sql.Timestamp;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.dhbw.hh.dao.DAOFactory;
import de.dhbw.hh.models.RESTResponse;
import static spark.Spark.*;

/**
 * Diese Klasse überprüft das eingegebene Password des Users mit dem aus der Datenbank.
 * Wenn die Eingabe korrekt ist, wird ein Cookie erstellt.
 * 
 * @author Maren
 *
 */
public class LoginREST {
	
	static final Logger LOG = LoggerFactory.getLogger(ReportsREST.class);
	
	/**
	 * Diese Funktion ist ein Konstruktor, um die REST Schnittstelle zu den User Daten zu definieren
	 * @param daoFactory
	 */
	public LoginREST(DAOFactory daoFactory){

		// Überprüft das eingegebene Passwort mit dem aus der Datenbank
		get("/login/:name", "application/json", (request, response) -> {
			LOG.debug("HTTP-GET Anfrage eingetroffen: " + request.queryString());
				
			// Frägt den eingegebenen Namen ab
			String name = request.params("name");
			// Frägt das eingegebene Passwort ab
			String password = request.queryParams("password");
		        
		    // Überprüft das eingegebene Passwort mit dem aus der H2-Datenbank
		    boolean successful = daoFactory.getUserDAO().checkLogin(name, password);
		     			
		    // Es wird ein Rückgabe Objekt erstellt
		    RESTResponse r = new RESTResponse();
		    // Setzt den Pfad der Abfrage
		    r.setName("/login/"+name);
		    // Setzt die aktuelle Zeit der Rückgabe
		    r.setTimestamp(new Timestamp(new Date().getTime()));
		     			
		    // Überprüfung, ob ein Ergebnis zurückgegeben wird
		    if (successful){
		    	// wenn true zurückgegeben wird, wird folgendes in das Rückgabe Objekt gespeichert:
		    	r.setDescription("Das eingegebene Passwort stimmt überein");
		    	r.setSuccess();
		    	// setzt Cookie bei korrekter Passwort Eingabe
		    	response.cookie("username", name);
		    } 
		    else {
		    	// wenn false zurückgegeben wird:
		    	r.setDescription("Das Passwort ist nicht korrekt");
		    	r.setError();
		    	// Entfernt das Cookie
		    	response.removeCookie("username");
		    }
			return response;	
		});
	}
	
}
