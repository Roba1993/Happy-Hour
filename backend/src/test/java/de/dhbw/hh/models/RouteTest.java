package de.dhbw.hh.models;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Diese Klasse testet die Datenklasse Route
 * 
 * @author Maren
 */
public class RouteTest {
	
	/**
	 * Diese Funktion testet den erweitereten Konstruktor der Klasse Route
	 */
	@Test
	public void testConstructor() {
		// Erstellt ein Route Objekt
		Route r = new Route("Hashwerttest", "Dies ist ein Json Test Objekt", true);
		
		// Testet, ob die Werte übereinstimmen
		assertEquals("Hashwerttest", r.getHash());
		assertEquals("Dies ist ein Json Test Objekt", r.getData());
		assertEquals(true, r.isTop());
		
		// Testet die korrekte Ausgabe der Daten der Funktion toString()
		assertEquals("hash: Hashwerttest Data: Dies ist ein Json Test Objekt TopRoute true/false?: true", r.toString());	
	}

}
