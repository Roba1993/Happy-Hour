package de.dhbw.hh.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

/**
 * Diese Klasse testet die Datenklasse RESTRespone
 * 
 * @author Jonas
 *
 */
public class RESTResponseTest {

	// RESTResponse Objekt zum Wiederverwenden
    private static RESTResponse restResponse;
	
    /**
     * Diese Funktion wird vor jedem Testrun aufgerufen und setzt das
     * RESTResponse Objekt zurück
     */
    @Before
    public void setUp() {
        // Befüllt das RESTResponse Objekt zum Wiederverwenden
        restResponse = new RESTResponse();
        restResponse.setName("HTTP-GET Antwort");
        restResponse.setSuccess();
        restResponse.setDescription("Die Daten wurden erfolgreich gespeichert");
        restResponse.setTimestamp(new Timestamp(4500));
        restResponse.setData(null);
    }
    
    /**
     * Diese Funktion testet alle Getter von RESTResponse
     */
	@Test
	public void testGetter() {
		assertEquals("HTTP-GET Antwort", restResponse.getName());
		assertEquals("success", restResponse.getStatus());
		assertEquals("Die Daten wurden erfolgreich gespeichert", restResponse.getDescription());
		assertEquals("1970-01-01 01:00:04.5", restResponse.getTimestamp().toString());
		assertEquals(null, restResponse.getData());
	}
	
	/**
	 * Diese Funktion testet die toString Funktion von RESTResponse
	 */
	@Test
	public void testToString() {
		assertEquals("Name: HTTP-GET Antwort; Description: Die Daten wurden erfolgreich gespeichert; Timestamp: "
				+ "1970-01-01 01:00:04.5; Status: success; Data null", restResponse.toString());
	}
	
}
