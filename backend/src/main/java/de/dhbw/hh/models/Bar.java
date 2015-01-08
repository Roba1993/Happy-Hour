package de.dhbw.hh.models;

import java.util.ArrayList;

import de.dhbw.hh.models.HappyHour;

/**
 * Diese Klasse repräsentiert eine Bar mit folgenden Eigenschaften:
 * Bar-ID, 
 * Bar-Name, 
 * Rating, 
 * Kosten, 
 * Beschreibung, 
 * URL für eine Bar-Bild, 
 * Geo-Position, 
 * Adresse, 
 * Öffnungszeiten, 
 * Happy-Hour-Zeiten
 * 
 * @author Tobias Häußermann
 * @version 1.0
 */
public class Bar {

	private String 	id;
	private String 	name;
	private int 	rating;
	private int 	costs;
	private String 	description;
	private String 	imageUrl;
	private Location location;
	private String 	adress;
	private Object[] openingTimes;
	private ArrayList<HappyHour> happyHours;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getRating() {
		return rating;
	}
	
	public void setRating(int rating) {
		this.rating = rating;
	}
	
	public int getCosts() {
		return costs;
	}
	
	public void setCosts(int costs) {
		this.costs = costs;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getImageUrl() {
		return imageUrl;
	}
	
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	public Location getLocation() {
		return location;
	}
	
	public void setLocation(Location location) {
		this.location = location;
	}
	
	public String getAdress() {
		return adress;
	}
	
	public void setAdress(String adress) {
		this.adress = adress;
	}
	
	public Object[] getOpeningTimes() {
		return openingTimes;
	}
	
	public void setOpeningTimes(Object[] openingTimes) {
		this.openingTimes = openingTimes;
	}
	
	public ArrayList<HappyHour> getHappyHours() {
		return happyHours;
	}
	
	public void setHappyHours(ArrayList<HappyHour> happyHours) {
		this.happyHours = happyHours;
	}
	
	@Override
	public String toString(){
		return ("ID: "+id+"\nName: "+name+"\nRating: "+rating+"\nCosts: "+costs+"\nDescription: "+description+
			"\nimageURL: "+imageUrl+"\nLongitude: "+location.longitude+"\nLatitude: "+location.latitude+"\nAdresse: "+
			adress+"\n-----------------");
	}
}
