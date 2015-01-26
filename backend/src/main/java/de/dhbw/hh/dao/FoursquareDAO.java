package de.dhbw.hh.dao;

import java.util.ArrayList;

import de.dhbw.hh.models.Bar;
import de.dhbw.hh.models.JSONOpeningTime;

/**
 * Interface für die Foursquare-Anbindung
 * 
 * @author Tobias Häußermann
 * 
 */
public interface FoursquareDAO {
	
	/**
	 * Diese Funktion ruft alle Bars in der Nähe vom Online-Dienst Foursquare ab
	 * 
	 * @param longitude GPS-Koordinate als Voraussetzung für die Suche
	 * @param latitude GPS-Koordinate als Voraussetzung für die Suche
	 * @param radius Umkreis in Metern
	 * @param weekday Wochentag im Bereich von 1 bis 7
	 * @return Eine Liste aller gefundener Bars, wenn nichts gefunden wurde, einen leeren Array
	 */
	public ArrayList<Bar> getBarsInArea(float longitude, float latitude, float radius, int weekday);
	
	/**
	 * Funktion um alle Öffnungszeiten einer einzelnen Bar über ihre ID von Foursquare abzurufen
	 * 
	 * @param id Eindeutige ID als Grundlage für die Suche
	 * @return Liste an Öffnungszeiten für eine Bar
	 */
	public ArrayList<JSONOpeningTime> getOpeningTimesByID(String id);
	
}
