package de.dhbw.hh.models;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


/**
 * Diese Klasse testet die Datenklasse BarReport
 * 
 * @author Jonas
 *
 */
public class BarReportTest {

	// BarReport Objekt zum Wiederverwenden
    private static BarReport barReport;
	
    /**
     * Diese Funktion wird vor jedem Testrun aufgerufen und setzt das
     * BarReport Objekt zurück
     */
    @Before
    public void setUp() {
        // Befüllt die BarReport Objekt zum Wiederverwenden
        barReport = new BarReport();
        barReport.setID(4);
        barReport.setBarID("uuaz972sdkinasd");
        barReport.setDescription("Die Bar ist mir zu langweilig");
    }
    
    /**
     * Diese Funktion testet alle Getter von BarReport
     */
	@Test
	public void testGetter() {
		assertEquals(4, barReport.getID());
		assertEquals("uuaz972sdkinasd", barReport.getBarID());
		assertEquals("Die Bar ist mir zu langweilig", barReport.getDescription());
	}
	
	/**
	 * Diese Funktion testet die toString Funktion von BarReport
	 */
	@Test
	public void testToString() {
		assertEquals("ID: 4; BarID: uuaz972sdkinasd; Description: Die Bar ist mir zu langweilig", barReport.toString());
	}
	
}
