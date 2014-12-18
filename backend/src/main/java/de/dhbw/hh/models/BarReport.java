package de.dhbw.hh.models;

/**
 * Datenklasse für gemeldete Bars
 * @author Jonas
 *
 */

public class BarReport {

	private String barID = "";
	private String description = "";
	private boolean reported = false;

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
	
	public boolean isReported() {
		return reported;
	}
	
	public void setReported(boolean reported) {
		this.reported = reported;
	}

}
