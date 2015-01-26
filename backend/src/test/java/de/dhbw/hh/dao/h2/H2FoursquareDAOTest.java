package de.dhbw.hh.dao.h2;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import de.dhbw.hh.dao.DAOFactory;
import de.dhbw.hh.utils.Settings;
import org.junit.BeforeClass;
import org.junit.Test;

import de.dhbw.hh.models.Bar;
import de.dhbw.hh.models.Location;

/**
 * Diese Klasse testet alle relevanten Methoden der H2FoursquareDAO-Klasse
 * @author Tobias Häußermann
 * 
 * guava
 */
public class H2FoursquareDAOTest {

	// Die zu testende Klasse
	private static H2FoursquareDAO fsc;
	
	/**
	 * Diese Funktion initialisiert die Klasse H2FoursquareDAO für 
	 * ausführliche Tests.
	 * @throws Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		fsc = new H2FoursquareDAO(new Settings(), DAOFactory.getDaoFactory(DAOFactory.H2, new Settings()));
	}
	
	/**
	 * Diese Funktion setzt eine Anfrage mit definierten Parametern an Foursquare
	 * ab und überprüft die Ergebnis-Werte auf Korrektheit
	 * @throws Exception
	 */
	//@Test
	public void testGetBarsInArea() throws Exception {
		// Übergabeparameter für Test definieren und festlegen.
		float latitude 		= 48.949034f;
		float longitude 	= 9.431656f;
		float radius		= 0.15f;
		int weekday 		= 1;
		
		// Setze die Anfrage an Foursquare ab und speichere die Ergebnisse in 'results'
		ArrayList<Bar> results = fsc.getBarsInArea(longitude, latitude, radius, weekday);
		
		// Lege Vergleichs-Bar-Objekt an und befülle es mit Daten, die aus manuellen
		// Tests entnommen wurden
		Bar cb = new Bar();
		cb.setId("51377264e4b0e335fd32afe8");
		cb.setAdress(null);
		cb.setCosts(1);
		cb.setDescription("Pub");
		cb.setHappyHours(null);	
		cb.setImageUrl("");
		cb.setLocation(new Location(9.433431520350682f, 48.94866611443367f));
		cb.setName("Waldhorn");
		cb.setOpeningTimes(null);
		cb.setRating(-1f);
		
		int totalCount = 2;
		
		// Prüfe ob zwei Ergebnisse von Foursquare zurückgegeben wurden
		assertEquals(totalCount, results.size());
		
		// Prüfe, ob das erste Barobjekt aus der Ergebnis-Liste 'results' dem 
		// manuell angelegten Vergleichs-Objekt gleicht
		Bar resultB = results.get(0);
		assertEquals(cb.getAdress(), resultB.getAdress());
		assertEquals(cb.getCosts(), resultB.getCosts());
		assertEquals(cb.getDescription(), resultB.getDescription());
//		assertEquals(cb.getHappyHours(), resultB.getHappyHours());
		assertEquals(cb.getId(), resultB.getId());
		assertEquals(cb.getImageUrl(), resultB.getImageUrl());
		assertEquals(cb.getLocation().latitude, resultB.getLocation().latitude, 0.001);
		assertEquals(cb.getLocation().longitude, resultB.getLocation().longitude, 0.001);
		assertEquals(cb.getName(), resultB.getName());
//		assertEquals(cb.getOpeningTimes(), resultB.getOpeningTimes());
		assertEquals(cb.getRating(), resultB.getRating(), 0.1);	
	}
	
}
