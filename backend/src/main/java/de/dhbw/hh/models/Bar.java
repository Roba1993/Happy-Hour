package de.dhbw.hh.models;

import java.util.ArrayList;

import de.dhbw.hh.dao.h2.H2FoursquareDAO.OpeningTimes;
import de.dhbw.hh.rest.BarsREST;

/**
 * Diese Klasse repräsentiert eine Bar mit folgenden Eigenschaften:
 * <li>Bar-ID</li>
 * <li>Bar-Name</li>
 * <li>Rating</li>
 * <li>Kosten</li>
 * <li>Beschreibung</li>
 * <li>URL für eine Bar-Bild</li>
 * <li>Geo-Position</li>
 * <li>Adresse</li>
 * <li>Öffnungszeiten</li>
 * <li>Happy-Hour-Zeiten</li>
 * 
 * @author Tobias Häußermann
 * @version 1.0
 */
public class Bar {

	private String 		id;
	private String 		name;
	private float 		rating;
	private int 		costs;
	private String 		description;
	private String 		imageUrl;
	private Location	location;
	private String 		address;
	private ArrayList<OpeningTimes> openingTimes;
	private ArrayList<BarsREST.HappyHour> 	happyHours;
	
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
	
	public float getRating() {
		return rating;
	}
	
	public void setRating(float rating) {
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
		return address;
	}
	
	public void setAdress(String adress) {
		this.address = adress;
	}
	
	public ArrayList<OpeningTimes> getOpeningTimes() {
		return openingTimes;
	}
	
	public void setOpeningTimes(ArrayList<OpeningTimes> openingTimes) {
		this.openingTimes = openingTimes;
	}
	
	public ArrayList<BarsREST.HappyHour> getHappyHours() {
		return happyHours;
	}
	
	public void setHappyHours(ArrayList<BarsREST.HappyHour> happyHours) {
		this.happyHours = happyHours;
	}
	
	@Override
	public String toString(){
		return ("ID: "+id+"\nName: "+name+"\nRating: "+rating+"\nCosts: "+costs+"\nDescription: "+description+
			"\nimageURL: "+imageUrl+"\nLongitude: "+location.longitude+"\nLatitude: "+location.latitude+"\nAdresse: "+
			address+"\n-----------------");
	}
}
