package de.dhbw.hh.models;

import static org.junit.Assert.*;

import java.sql.Time;

import org.junit.Test;

/**
 * Diese Klasse testet die Datenklasse HappyHour
 * 
 * @author Marcus
 */
public class HappyHourTest {
	
	/**
	 * Diese Funktion testet den erweitereten Konstruktor der Klasse Route
	 */
	@SuppressWarnings("deprecation")
	@Test
	public void testConstructor() {
		// Erstellt ein HappyHour Objekt
		HappyHour happyHour = new HappyHour();
		happyHour.setID(4);
		happyHour.setBarID("kasd32mma87h9n");
		happyHour.setDescription("Geile Happy-Hour mit Tour-de-sauf");
		happyHour.setStart(new Time(19, 00, 00));
		happyHour.setEnd(new Time(21, 00, 00));
		happyHour.setMonday(false);
		happyHour.setTuesday(false);
		happyHour.setWednesday(true);
		happyHour.setThursday(true);
		happyHour.setFriday(false);
		happyHour.setSaturday(true);
		happyHour.setSunday(false);
		
		// Testet, ob die Werte übereinstimmen
		assertEquals(4, happyHour.getID());
		assertEquals("kasd32mma87h9n", happyHour.getBarID());
		assertEquals("Geile Happy-Hour mit Tour-de-sauf", happyHour.getDescription());
		assertEquals(new Time(19, 00, 00), happyHour.getStart());
		assertEquals(new Time(21, 00, 00), happyHour.getEnd());
		assertEquals(false, happyHour.isMonday());
		assertEquals(false, happyHour.isTuesday());
		assertEquals(true, happyHour.isWednesday());
		assertEquals(true, happyHour.isThursday());
		assertEquals(false, happyHour.isFriday());
		assertEquals(true, happyHour.isSaturday());
		assertEquals(false, happyHour.isSunday());
		
		
		// Testet die korrekte Ausgabe der Daten der Funktion toString()
		assertEquals("ID:4 barID: kasd32mma87h9n description: Geile Happy-Hour mit Tour-de-sauf start: 19:00:00 end: 21:00:00 Monday true/false?: false Tuesday true/false?: false"
				+ " Wednesday true/false?: true Thursday true/false?: true Friday true/false?: false Saturday true/false?: true Sunday true/false?: false",
				happyHour.toString());
	}
}
