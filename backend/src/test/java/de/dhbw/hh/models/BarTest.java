package de.dhbw.hh.models;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

/**
 * Diese Klasse testet die Datenklasse Bar
 * 
 * @author Tobias Häußermann
 */
public class BarTest {

	private static String id 			= "abcdefghijklmnopqrstuvwxyz";
	private static String name 		= "Name123";
	private static float rating		= 2f;
	private static int costs			= 3;
	private static String description	= "Eine Bar";
	private static String imageUrl		= null;
	private static Location location	= new Location(0.1f, 0.45f);
	private static String address		= "Teststraße 12";
	private static ArrayList<JSONOpeningTimes> openingTimes	= new ArrayList<JSONOpeningTimes>();
	private static ArrayList<JSONHappyHour> happyHours			= new ArrayList<JSONHappyHour>();
	
	// Das Bar Objekt zum Testen
	private static Bar bar;
	
	/**
     * Diese Funktion wird vor jedem Testrun aufgerufen und setzt das
     * Barobjekt zurück
     */
	@Before
	public void setUp() {
		bar = new Bar();
		bar.setId(id);
		bar.setName(name);
		bar.setRating(rating);
		bar.setCosts(costs);
		bar.setDescription(description);
		bar.setImageUrl(imageUrl);
		bar.setLocation(location);
		bar.setAdress(address);
		bar.setOpeningTimes(openingTimes);
		bar.setHappyHours(happyHours);
	}
	
	/**
	 * Testet die Getter der Klasse Bar
	 */
	@Test
	public void testGetter() {
		assertEquals(bar.getId(), id);
		assertEquals(bar.getName(), name);
		assertEquals(bar.getRating(), rating, 0.01);
		assertEquals(bar.getCosts(), costs);
		assertEquals(bar.getDescription(), description);
		assertEquals(bar.getImageUrl(), imageUrl);
		assertEquals(bar.getLocation(), location);
		assertEquals(bar.getAdress(), address);
		assertEquals(bar.getOpeningTimes(), openingTimes);
		assertEquals(bar.getHappyHours(), happyHours);
	}
	
	/**
	 * Testet die toString()-Methode der Klasse Bar
	 */
	@Test
	public void testToString() {
		String result = "ID: "+id+"\nName: "+name+"\nRating: "+rating+"\nCosts: "+costs+"\nDescription: "+description+
				"\nimageURL: "+imageUrl+"\nLongitude: "+location.longitude+"\nLatitude: "+location.latitude+"\nAdresse: "+
				address+"\n-----------------";
		assertEquals(bar.toString(), result);
	}

}
