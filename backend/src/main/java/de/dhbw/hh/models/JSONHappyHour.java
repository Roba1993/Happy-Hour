package de.dhbw.hh.models;

public class JSONHappyHour{
	
	private String 	barID;
	private String 	startTime;
	private String 	endTime;
	private String description;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String toString(){
		String result = "Öffnungszeiten:{\nBarID: "+barID+"\nStart: "+startTime+"\nEnde: "+endTime+"\nTage:";
		for(int i=0;i<days.length;i++){
			result += "\n"+days[i];
		}
		return result;
	}
}