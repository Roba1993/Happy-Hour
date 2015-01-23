package de.dhbw.hh.dao;

import java.util.Collection;

import de.dhbw.hh.models.BarReport;

/**
 * Dieses Interface definiert, welche Methoden die jeweiligen DAOs implementieren müssen.
 * @author Jonas
 *
 */
public interface BarReportDAO {
	
	/**
     * Diese Funktion fügt ein BarReport in die Datenbank ein.
     *
     * @param barReport Der einzufügende BarReport.
     * @return True bei Erfolg, andernfalls false.
     */
    public boolean insertBarReport(BarReport barReport);

    /**
     * Diese Funktion löscht alle BarReports zu einer zugehörigen Bar anhand der BarID
     *
     * @param barID Die BarID anhand der die BarReports zugeordnet werden.
     * @return True bei Erfolg, andernfalls false.
     */
    public boolean deleteBarReport(String barID);
    
    /**
     * Diese Funktion löscht einen BarReport aus der Datenbank anhand seiner ID.
     *
     * @param id Die id anhand der BarReport zugeordnet wird.
     * @return True bei Erfolg, andernfalls false.
     */
    public boolean deleteSpecificBarReport(int id);

    /**
     * Diese Funktion findet einen bestimmten BarReport anhand seiner BarID.
     *
     * @param barID Die BarID nach der gesucht werden soll.
     * @return Gibt bei Erfolg das gefundene BarReport-Objekt zurück. Wenn nichts
     * gefunden wird, enthält die Rückgabe den Wert null.
     */
    public Collection<BarReport> findBarReport(String barID);

    /**
     * Diese Funktion sucht alle BarReports in der Datenbank mit dem angegebenen Reported-Wahrheitswert.
     *
     * @param reported Der Reported-Wahrheitswert nach dem gesucht werden soll.
     * @return BarReport-Array mit den gefundenen BarReports.
     */
    public Collection<BarReport> findAllBarReports();

}
