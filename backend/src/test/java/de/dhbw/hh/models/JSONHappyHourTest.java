package de.dhbw.hh.models;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * Diese Klasse testet die Klasse JSONHappyHour
 * @author Tobias Häußermann
 */
public class JSONHappyHourTest {

	private static String barID 		= "abcdefghijklmnopqrstuvwxyz";
	private static String startTime 	= "18:00";
	private static String endTime 		= "23:00";
	private static int[] days 			= {1, 3, 4, 5};
	
	// Das HappyHour Objekt zum Testen
	private static JSONHappyHour hh;
	
	/**
     * Diese Funktion wird vor jedem Testrun aufgerufen und setzt das
     * HappyHourobjekt zurück
     */
	@Before
	public void setUp() {
		hh = new JSONHappyHour();
		hh.setBarID(barID);
		hh.setStartTime(startTime);
		hh.setEndTime(endTime);
		hh.setDays(days);
	}

	/**
	 * Testet die Getter der Klasse JSONHappyHour
	 */
	@Test
	public void testGetter(){
		assertEquals(hh.getBarID(), barID);
		assertEquals(hh.getStartTime(), startTime);
		assertEquals(hh.getEndTime(), endTime);
		assertEquals(hh.getDays(), days);
	}
	
	/**
	 * Testet die toString-Methode der Klasse JSONHappyHour
	 */
	@Test
	public void testToString(){
		String result = "Öffnungszeiten:{\nBarID: "+barID+"\nStart: "+startTime+"\nEnde: "+endTime+"\nTage:";
		for(int i=0;i<days.length;i++){
			result += "\n"+days[i];
		}
		assertEquals(hh.toString(), result);
	}
}
