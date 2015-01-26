package de.dhbw.hh.models;

/**
 * Datenklasse für die Happy Hour Klasse,
 * durch die die Daten anschließend ins JSON Format umgewandelt werden können
 * @author Tobi
 *
 */
public class JSONHappyHour{
	// Erstellen der Variablen
	private String 	barID;
	private String 	startTime;
	private String 	endTime;
	private String description;
	private int[] 	days;
	
	// Folgend alle Getter und Setter
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

	/**
	 * Wandelt alle Datensätze in einen String um
	 */
	@Override
	public String toString(){
		String result = "Öffnungszeiten:{\nBarID: "+barID+"\nStart: "+startTime+"\nEnde: "+endTime+"\nTage:";
		for(int i=0;i<days.length;i++){
			result += "\n"+days[i];
		}
		return result;
	}
	
}