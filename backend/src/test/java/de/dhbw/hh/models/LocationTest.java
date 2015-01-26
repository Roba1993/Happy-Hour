package de.dhbw.hh.models;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * Diese Klasse testet die Klasse Location
 * 
 * @author Tobias Häußermann
 */
public class LocationTest {

	private static float latitude  = 0.456f;
	private static float longitude = 0.123f;
	
	// Das Location Objekt zum Testen
	private static Location loc;
	
	/**
     * Diese Funktion wird vor jedem Testrun aufgerufen und setzt das
     * Location zurück
     */
	@Before
	public void setUp() {
		loc = new Location();
		loc.setX(longitude);
		loc.setY(latitude);
	}
	
	/**
	 * Testet die Getter der Klasse Location
	 */
	@Test
	public void testGetter() {
		assertEquals(loc.getLatitude(), latitude, 0.01f);
		assertEquals(loc.getLongitude(), longitude, 0.01f);
	}
	
	/**
	 * Testet den erweiterten Konstruktor der Klasse Location
	 */
	@Test
	public void testConstructor() {
		loc = new Location(longitude, latitude);
		assertEquals(loc.getLatitude(), latitude, 0.01f);
		assertEquals(loc.getLongitude(), longitude, 0.01f);
	}
	
	/**
	 * Testet die toString-Methode der Klasse Location
	 */
	@Test
	public void testToString() {
		String result = "Longitude: " + longitude + " Latitude: " + latitude;
		assertEquals(loc.toString(), result);		
	}

}
