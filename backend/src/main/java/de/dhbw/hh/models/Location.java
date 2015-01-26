package de.dhbw.hh.models;

/**
 *  Datenklasse für die Daten der Location
 *
 * @author Tobias Häußermann
 */
public class Location {

	// Erstellen der Variablen
	public float longitude, latitude;
	
	/**
	 * Ein leerer Konstruktur zur Erstellung eines Location Objekts 
	 */
	public Location(){
		
	}
	
	/**
	 * Der Location Konstruktur zur Erstellung des Location Objekts
	 */
	public Location(float longitude, float latitude){
		this.longitude = longitude;
		this.latitude = latitude;
	}
	
	// Nun folgen alle Getter und Setter Methoden
	public void setX(float longitude){
		this.longitude = longitude;
	}
	
	public void setY(float latitude){
		this.latitude = latitude;
	}
	
	public float getLongitude(){
		return longitude;
	}
	
	public float getLatitude(){
		return latitude;
	}
	
	/**
	 * Wandelt alle Datensätze in einen String um
	 */
	@Override
	public String toString(){
		return ("Longitude: " + longitude + " Latitude: " + latitude);
	}
	
}
