package de.dhbw.hh.models;

/**
 * Datenklasse für gemeldete Bars
 * @author Jonas
 *
 */

public class BarReport {

	private int id = 0;
	private String barID = "";
	private String description = "";

	public int getID() {
		return id;
	}
	
	public void setID(int id) {
		this.id = id;
	}
	
	public String getBarID() {
		return barID;
	}

	public void setBarID(String barID) {
		this.barID = barID;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
