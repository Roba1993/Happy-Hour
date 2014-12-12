package de.dhbw.hh.models;

/*Diese Datenklasse stellt die Daten zu den sieben Toprouten bereit
 * 
 * @author: Maren
 */

public class Routes {
	
	private String hash = "";
	
	private String data = "";
	
	private boolean top = false;
	
	public Routes (String hash, String data, Boolean top){
		this.setHash(hash);
		this.setData(data);
		this.setTop(top);
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
	
}
