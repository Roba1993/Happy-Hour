package de.dhbw.hh.models;

/**
 * Diese Klasse bildet die Öffnungszeiten eines Bar-Objekts ab. Ein Bar-Objekt kann 
 * mehrere verschiedene Öffnungszeiten haben. 
 * @author Tobias Häußermann
 */
public class JSONOpeningTimes {

	private String 	barID;
	private String 	startTime;
	private String 	endTime;
	private int[] 	days;
	
	public String getBarID() {
		return barID;
	}
	
	public void setBarID(String barID) {
		this.barID = barID;
	}
	
	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public int[] getDays() {
		return days;
	}

	public void setDays(int[] days){
		this.days = days;
	}
	
	public String toString(){
		String result = "Öffnungszeiten:{\nBarID: "+barID+"\nStart: "+startTime+"\nEnde: "+endTime+"\nTage:";
		for(int i=0;i<days.length;i++){
			result += "\n"+days[i];
		}
		return result;
	}
}
