package de.dhbw.hh.dao;

/**
 * Interface für die User Methoden
 * 
 * @author Maren
 *
 */
public interface UserDAO {
	
	/**
	 * Diese Funktion findet einen bestimmten User anhand seines Namens
	 * 
	 * @param name Name nach dem das Password gesucht wird
	 * @return gesamte Route-Objekt bei Erfolg. Wenn nichts gefunden wird null
	 */	
	public boolean checkLogin(String name, String password);
	
}
