package de.dhbw.hh.rest;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;
import spark.*; 
import de.dhbw.hh.utils.ProtectCookie;
import de.dhbw.hh.rest.LoginREST;

/**
 * Diese Klasse filtert die Cookies
 * @author Maren
 *
 */
public class CookieFILTER {

	public CookieFILTER() {
		
		/**
		 * unsecure the cookies
		 */
		before((request, response) -> {
		    // Erstellt ein neues Objekt
			ProtectCookie uC = new ProtectCookie();
			
			// Aufruf der unsecureCookie Funktion mit Übergabe des ausgelesen Cookies
			uC.unsecureCookies(request.cookies());
		});
		
		
		/**
		 * secure the cookies
		 */
		after((request, response) -> {
		    // Neues Objekt wird erstellt
			ProtectCookie sC = new ProtectCookie();
			
			// Aufruf der secureCookies Funktion mit Übergabe des zurückgegeben Cookies
			sC.secureCookies(response.cookie());
			
		});
	}
}
