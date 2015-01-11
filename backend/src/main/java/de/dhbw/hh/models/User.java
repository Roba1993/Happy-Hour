package de.dhbw.hh.models;

/**
 * Dies ist die Datenklasse für die User Daten der Administratoren
 *
 * @author Maren
 */
public class User {
	
	private String name = "";
	
	/**
	 * Der User Konstruktur zur Erstellung der User Objekts
	 */
	public User (String name){
		this.setName(name);
		}
	
	/**
	 * Leerer Konstrukter für das User Objekt
	 */
	public User (){
	}
	
	// Folgend die Getter und Setter
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
