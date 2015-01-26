package de.dhbw.hh.models;

/**
 * Location Datenklasse
 *
 * @author Tobias Häußermann
 */
public class Location {

	public float longitude, latitude;
	
	public Location(){}
	
	public Location(float longitude, float latitude){
		this.longitude = longitude;
		this.latitude = latitude;
	}
	
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
