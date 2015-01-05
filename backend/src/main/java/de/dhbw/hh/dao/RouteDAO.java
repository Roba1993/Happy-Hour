package de.dhbw.hh.dao;

import java.util.Collection;

import de.dhbw.hh.models.Route;

/**
 * Interface für die Route Methoden
 * 
 * @author Maren
 *
 */
public interface RouteDAO {

	/**
	 * Diese Funktion führt eine Route in die Datenbank ein
	 * 
	 * @param route die einzufügende Route
	 * @return true wenn erfolgreich, ansonsten false
	 */
	public boolean insertRoute(Route route);
	
	/**
	 * Diese Funktion findet eine bestimmte Route anhand ihres Hashwerts
	 * 
	 * @param hash Wert nach dem die Route gesucht wird
	 * @return gesamte Route-Onjekt bei Erfolg. Wenn nichts gefunden wird null
	 */	
	public Route findRoute(String hash);
	
	
	/**Diese Funktion gibt alle Top Routen zurück 
	 * 
	 * @param es wird nach allen TopRouten gesucht
	 * @return alle Top Routen bei Erfolg, andernfalls wird null zurückgegeben
	 */	
	public Collection<Route> findTopRoutes();
		
}
