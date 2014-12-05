package de.dhbw.hh.models;

public class ReportsMessage {

	private String barID = "";
	private String description = "";
	
	public ReportsMessage(String barID, String description) {
		this.barID = barID;
		this.description = description;
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
