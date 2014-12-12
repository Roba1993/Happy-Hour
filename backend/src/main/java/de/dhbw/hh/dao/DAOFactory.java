package de.dhbw.hh.dao;

import de.dhbw.hh.dao.h2.H2DAOFactory;

import java.util.Properties;

/**
 * Mit dieser Klasse kann eine Datenbank-Factory erstellt werden.
 */
public abstract class DAOFactory {
    // Liste der unterstützen Datenbanken
    public static final int H2 = 1;

    // Funktionen um die DAO-Klassen zu bekommen
    public abstract TestrunDAO getTestrunDAO();
    
    public abstract BarReportDAO getBarReportDAO();

    /**
     * Gibt eine Datenbank-Factory zurück, mit welcher auf
     * eine Datenbank zugegriffen werden kann.
     *
     * @param database   Die zu verwendene Datenbank als Integer.
     * @param properties Einstellungen welche von der Factory benötigt werden.
     * @return Gibt eine Datenbank-Factory zurück-
     */
    public static DAOFactory getDaoFactory(int database, Properties properties) {
        switch (database) {
            case H2:
                return new H2DAOFactory(properties);
            default:
                return null;
        }
    }

    /**
     * Schließt eine Factory und deren Connections.
     */
    public abstract void close();
}
