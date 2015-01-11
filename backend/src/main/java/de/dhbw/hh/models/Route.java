package de.dhbw.hh.models;

/**
 * Diese Datenklasse stellt die Daten zu den sieben Top Routen bereit
 * 
 * @author Maren
 */
public class Route {
	// Erstellen der Variablen
	private String hash = "";
	
	private String data = "";
	
	private boolean top = false;
	
	/**
	 * Der Route Konstruktur zur Erstellung des Routes Objekts
	 */
	public Route (String hash, String data, Boolean top){
		this.setHash(hash);
		this.setData(data);
		this.setTop(top);	
	}
	
	/**
	 * Ein leerer Konstruktur zur Erstellung eines Routes Objekts 
	 */
	public Route (){
		
	}
	
	// Nun folgen alle Getter und Setter Methoden
	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public boolean isTop() {
		return top;
	}

	public void setTop(boolean top) {
		this.top = top;
	}

	public boolean isHashEmpty() {
		// TODO Auto-generated method stub
		return hash.isEmpty();
	}
	
	/**
	 * Wandelt alle Datensätze in einen String um
	 */
	@Override
	public String toString(){
		return ("hash: " + hash + " Data: " + data + " TopRoute true/false?: " + top);
	}
	
}
