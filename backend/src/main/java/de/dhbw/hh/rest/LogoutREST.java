package de.dhbw.hh.rest;

import de.dhbw.hh.dao.DAOFactory;
import static spark.Spark.*;

/**
 * Diese Klasse logt den User wieder aus und löscht den Cookie
 * @author Maren
 *
 */
public class LogoutREST {
	
	/**
	 * Diese Funktion ist ein Kronstruktor für den Pfad /logout über die REST Schnittstelle
	 * @param daoFactory
	 */
	public LogoutREST(DAOFactory daoFactory){
		
		// Diese Funktion meldet den Administrator wieder ab		
		get("/logout", "application/json", (request, response) -> {
			// Cookie wird entfernt
			response.removeCookie("username");
			return true;        
		});
	}
	
}
