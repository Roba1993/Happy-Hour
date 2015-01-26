package de.dhbw.hh.models;

/**
 * Datenklasse für gemeldete Bars
 *
 * @author Jonas
 */
public class BarReport {
	// Erstellen der Variablen
	private int id = 0;
	private String barID = "";
	private String description = "";

	// Folgend alle Getter und Setter
	
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
	
	/**
	 * Wandelt alle Datensätze in einen String um
	 */
	@Override
	public String toString() {
		return "ID: " + getID() + "; BarID: " + getBarID() + "; Description: " + getDescription();
	}

}
