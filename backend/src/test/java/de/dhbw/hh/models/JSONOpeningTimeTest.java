package de.dhbw.hh.models;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * Diese Klasse testet die Klasse JSONOpeningTimes
 *  
 * @author Tobias Häußermann
 */
public class JSONOpeningTimeTest {

	private static String barID 		= "abcdefghijklmnopqrstuvwxyz";
	private static String startTime 	= "18:00";
	private static String endTime 		= "23:00";
	private static int[] days 			= {1, 3, 4, 5};
	
	// Das Öffnungszeitenobjekt zum Testen
	private static JSONOpeningTime ot;
	
	/**
     * Diese Funktion wird vor jedem Testrun aufgerufen und setzt das
     * Öffnungszeitenobjekt zurück
     */
	@Before
	public void setUp() {	
		ot = new JSONOpeningTime();
		ot.setBarID(barID);
		System.out.println("BarID: "+barID);
		ot.setStartTime(startTime);
		ot.setEndTime(endTime);
		ot.setDays(days);	
	}
	
	/**
	 * Testet alle Getter der Klasse JSONOpeningTimes
	 */
	@Test
	public void testGetter(){
		assertEquals(ot.getBarID(), barID);
		assertEquals(ot.getStartTime(), startTime);
		assertEquals(ot.getEndTime(), endTime);
		assertEquals(ot.getDays(), days);
	}
	
	/**
	 * Testet die toString-Methode der Klasse JSONOpeningTimes
	 */
	@Test
	public void testToString(){
		
		String result = "Öffnungszeiten:{\nBarID: "+barID+"\nStart: "+startTime+"\nEnde: "+endTime+"\nTage:";
		for(int i=0;i<days.length;i++){
			result += "\n"+days[i];
		}
		assertEquals(ot.toString(), result);
	}

}
