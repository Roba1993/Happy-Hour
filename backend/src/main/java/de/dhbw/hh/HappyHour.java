package de.dhbw.hh;

import de.dhbw.hh.dao.DAOFactory;
import de.dhbw.hh.models.Testrun;
import de.dhbw.hh.utils.Settings;

/**
 * Dies ist die Java-Hauptklasse und der Einstiegspunkt
 * für die Backend-Programmierung.
 *
 */
public class HappyHour {

    // Die Eigenschaften für das Projekt
    public static Settings settings;
    public static DAOFactory daoFactory;

    /**
     * Die main Funktion in der die Ausführung gestartet wird.
     *
     * @param args Es werden keine Argumente benötigt
     */
    public static void main( String[] args )
    {
        // Lade die Standart-Einstellungen
        settings = new Settings();
        settings.loadDefault();

        // Erstelle die DAOFactory
        daoFactory = DAOFactory.getDaoFactory(DAOFactory.H2, settings);


        // Hole die Daten das Admins aus der Datenbank
        Testrun[] runs = daoFactory.getTestrunDAO().findTestrunsByName("Admin");

        // Wenn Daten existieren gebe diese aus
        System.out.println( "Hello Admin deine Runden:" );

        for(Testrun run: runs) {
            System.out.println("Am " + run.getDate() + " fuhrst du " + run.getRounds() + " Runden");
        }

        // Schließe die Factory und alle damit verbundenen Connections
        daoFactory.close();
    }
}
