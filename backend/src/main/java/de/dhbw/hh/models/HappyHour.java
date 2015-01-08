package de.dhbw.hh.models;

import java.sql.Time;

/**
 * Datenklasse für Happy Hour Zeiten
 * @author Marcus
 *
 */

public class HappyHour {
	
	private int id = 0;
	private String barID = "";
	private String description = "";
	private Time start = new Time(0);
	private Time end = new Time(0);
	private boolean monday = false;				
	private boolean tuesday = false;				
	private boolean wednesday = false;			
	private boolean thursday = false;				
	private boolean friday = false;					
	private boolean saturday = false;				
	private boolean sunday = false;					
		
	// Getters and Setters
	
	public int getID() {
		return id;
	}
	
	public void setID(int id) {
		this.id = id;
	}
		
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isMonday() {
		return monday;
	}

	public void setMonday(boolean monday) {
		this.monday = monday;
	}

	public boolean isTuesday() {
		return tuesday;
	}

	public void setTuesday(boolean tuesday) {
		this.tuesday = tuesday;
	}

	public boolean isWednesday() {
		return wednesday;
	}

	public void setWednesday(boolean wednesday) {
		this.wednesday = wednesday;
	}

	public boolean isThursday() {
		return thursday;
	}

	public void setThursday(boolean thursday) {
		this.thursday = thursday;
	}

	public boolean isFriday() {
		return friday;
	}

	public void setFriday(boolean friday) {
		this.friday = friday;
	}

	public boolean isSaturday() {
		return saturday;
	}

	public void setSaturday(boolean saturday) {
		this.saturday = saturday;
	}

	public boolean isSunday() {
		return sunday;
	}

	public void setSunday(boolean sunday) {
		this.sunday = sunday;
	}

	public String getBarID() {
		return barID;
	}

	public void setBarID(String barID) {
		this.barID = barID;
	}

	public Time getStart() {
		return start;
	}

	public void setStart(Time start) {
		this.start = start;
	}

	public Time getEnd() {
		return end;
	}

	public void setEnd(Time end) {
		this.end = end;
	}

}
