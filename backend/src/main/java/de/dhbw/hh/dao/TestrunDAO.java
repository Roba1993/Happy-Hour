package de.dhbw.hh.dao;

import de.dhbw.hh.models.Testrun;

/**
 * Dieses Interface definiert, welche Funktionen die
 * jeweiligen DAO implementieren müssen.
 */
public interface TestrunDAO {

    /**
     * Diese Funktion fügt einen Testrun in die Datenbank ein.
     *
     * @param testrun Der einzufügende Testrun.
     * @return True bei Erfolg, andernfalls false.
     */
    public boolean insertTestrun(Testrun testrun);

    /**
     * Diese Funktion aktualiesiert einen Testrun in der Datenbank.
     * Der Testrun wird dabei über die ID zugeordnet.
     *
     * @param testrun Der Testrun mit den neuen Werten.
     * @return True bei Erfolg, andernfalls false.
     */
    public boolean updateTestrun(Testrun testrun);

    /**
     * Diese Funktion löscht einen Testrun aus der Datenbank anhand seiner ID.
     *
     * @param id Die ID anhand der Testrun zugeordnet wird.
     * @return True bei Erfolg, andernfalls false.
     */
    public boolean deleteTestrun(long id);

    /**
     * Diese Funktion findet einen bestimmten Testrun anhand seiner ID.
     *
     * @param id Die ID nach der gesucht werden soll.
     * @return Gibt bei Erfolg das gefunden Testrun-Objekt zurück. Wenn nichts
     * gefunden wird, enthält die Rückgabe den Wert null.
     */
    public Testrun findTestrun(long id);

    /**
     * Diese Funktion sucht alle Tests in der Datenbank mit den gegebenen Namen.
     *
     * @param name Der Name nach dem gesucht werden soll.
     * @return Testrun-Array mit den gefunenen Tests.
     */
    public Testrun[] findTestrunsByName(String name);
}
