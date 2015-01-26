package de.dhbw.hh.dao;
import java.util.Collection;
import de.dhbw.hh.models.HappyHour;

/**
 * Dieses Interface definiert, welche Methoden die jeweiligen DAOs implementieren müssen.
 *
 * @author Marcus
 */
public interface HappyHourDAO {
	
	/**
     * Diese Funktion fügt eine HappyHour in die Datenbank ein.
     *
     * @param happyHour Der einzufügende HappyHour.
     * @return True bei Erfolg, andernfalls false.
     * 
     */
	public boolean insertHappyHour(HappyHour happyHour);
	
	/**
     * Diese Funktion löscht eine HappyHour aus der Datenbank anhand seiner ID.
     *
     * @param id Die BarID anhand die HappyHour zugeordnet wird.
     * @return True bei Erfolg, andernfalls false.
     */
	public boolean deleteHappyHour(int id);
	  /**
     * Diese Funktion findet einen bestimmte HappyHour anhand ihrer BarID.
     *
     * @param id Die BarID nach der gesucht werden soll.
     * @return Gibt bei Erfolg das gefundene HappyHour-Objekt zurück. Wenn nichts
     * gefunden wird, enthält die Rückgabe den Wert null.
     */
    public Collection<HappyHour> findHappyHour(String id);
	
}
