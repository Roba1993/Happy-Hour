package de.dhbw.hh.rest;

import static spark.Spark.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.dhbw.hh.dao.DAOFactory;
import de.dhbw.hh.dao.h2.H2UserDAO; 

/**
 * Diese Klasse filtert die Zugriffe auf die Funktionen
 *
 * @author Maren
 */
public class LoginFILTER {

	// Initialisiert einen Logger für die Fehlerausgabe
    static final Logger log = LoggerFactory.getLogger(H2UserDAO.class);
    
	/**
	 * Diese Funktion ist ein Konstruktor, um Anfragen über die REST Schnittstelle zu filtern
	 *
	 * @param daoFactory Ein object des Types daoFactory wird benötigt
	 */
	public LoginFILTER(DAOFactory daoFactory) {	
		
		/**
		 * Diese Funktion lässt den Zugriff auf bestimmte Pfade nur zu, 
		 * wenn der Anfragesteller als Admin angemeldet ist
		 */
		before( (request, response) -> {
		   	// Überprüfung, ob auf einen Pfad zugegriffen wird, der eine Admin-Anmeldung benötigt
			if ("/bars/reports".equals(request.pathInfo()) || "/bars/:barID/report".equals(request.pathInfo()) || "/report/:id".equals(request.pathInfo())){
				
				// Frägt den eingegebenen Namen ab
				String name = request.queryParams("name");
				// Frägt das eingegebene Passwort ab
				String password = request.queryParams("password");
				        
				// Überprüft das eingegebene Passwort mit dem aus der H2-Datenbank
				boolean successful = daoFactory.getUserDAO().checkLogin(name, password);
					     							     			
				// Überprüfung, ob ein Ergebnis zurückgegeben wird
				if (successful){
					// Log-Eintrag bei erfolgreichen Zugriff auf Pfade, die eine Adminberechtigung benötigen
					log.debug("Admin: " + name + " - " + request.requestMethod() + ":" + request.pathInfo());
				} 
				else {
					// Stimmt das eingegebene Passwort nicht überein, wird die Anfrage abgebrochen
					halt(401, "Du bist nicht berechtigt auf diese Funktion zuzugreifen");
				}
			}				
		});
	}
		
}
