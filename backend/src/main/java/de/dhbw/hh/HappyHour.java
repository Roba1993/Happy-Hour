package de.dhbw.hh;

import de.dhbw.hh.dao.DAOFactory;
import de.dhbw.hh.models.Testrun;
import de.dhbw.hh.utils.LoggingConfiguration;
import de.dhbw.hh.utils.Settings;
import de.dhbw.hh.utils.Spark;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

/**
 * Dies ist die Java-Hauptklasse und der Einstiegspunkt
 * für die Backend-Programmierung.
 *
 */
public class HappyHour {

    // Die Logger Klasse
    static final Logger LOG = LoggerFactory.getLogger(HappyHour.class);

    // Die Eigenschaften für das Projekt
    public static Settings settings;
    public static DAOFactory daoFactory;
    public static Spark spark;

    /**
     * Die main Funktion in der die Ausführung gestartet wird.
     *
     * @param args Es werden keine Argumente benötigt
     */
    public static void main( String[] args )
    {
        LOG.info("Happy-Hour startet...");

        // Lade die Standart-Einstellungen
        settings = new Settings();
        settings.loadDefault();

        // Setze Logging Einstellungen
        LoggingConfiguration.setLoggingConfiguration(settings);

        LOG.info("Verbinde zur Datenbank...");

        // Erstelle die DAOFactory
        daoFactory = DAOFactory.getDaoFactory(DAOFactory.H2, settings);

        LOG.info("Starte den Server auf Port "+settings.getProperty("server.port"));

        // Starte den Spark-Server
        spark = new Spark(settings, daoFactory);

        LOG.info("Happy-Hour ist bereit für Anfragen...");
    }
}
