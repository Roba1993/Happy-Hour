package de.dhbw.hh.rest;

import static spark.Spark.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.dhbw.hh.dao.DAOFactory;
import de.dhbw.hh.dao.h2.H2UserDAO;
import spark.*; 

/**
 * Diese Klasse filtert die Zugriffe auf die Funktionen
 * @author Maren
 *
 */
public class LoginFILTER {

	// Initialisiert einen Logger für die Fehlerausgabe
    static final Logger log = LoggerFactory.getLogger(H2UserDAO.class);
	
	public LoginFILTER() {
		
		before( (request, response) -> {
		   	// Überprüfung, ob ein Pfad zugegriffen wird, der eine Admin-Anmeldung benötigt
			if ("/admin/a".equals(request.pathInfo()) || "/admin/b".equals(request.pathInfo())){
		    
				// Überprüfung ob ein "username"-Cookie existiert
				if (request.cookie("username") == null || "".equals(request.cookie("username").trim())){
					halt(401, "Du bist nicht berechtigt darauf zuzugreifen");
				}
		    }

			
		});

	}
}
