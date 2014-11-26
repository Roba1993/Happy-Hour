package de.dhbw.hh;

import de.dhbw.hh.dao.DAOFactory;
import de.dhbw.hh.models.Testrun;
import de.dhbw.hh.utils.LoggingConfiguration;
import de.dhbw.hh.utils.Settings;
import de.dhbw.hh.utils.Spark;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
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
    public static CmdLineParser parser;
    
    // Boolean mit Flag für Testrun
    @Option(name="-testrun",usage="Flag for doing testrun")
    public static boolean testrun;

    /**
     * Die main Funktion in der die Ausführung gestartet wird.
     *
     * @param args Es werden keine Argumente benötigt
     * @return 
     */
    public static void main(String[] args) {
        new HappyHour(args);
    }
    
    public HappyHour(String[] args) {
	
	// Parse Argumente aus Kommandozeile
	CmdLineParser parser = new CmdLineParser(this);
	try {
	    parser.parseArgument(args);
	} catch (CmdLineException e) {
	    LOG.error(e.getMessage());
	}
	
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
        
        // Beende Server wenn Testrun Argument übergeben wurde
        if (testrun) {
            LOG.info("Happy-Hour läuft im Testrun Modus");
            try {
		Thread.sleep(3000);
	    } catch (InterruptedException e) {
		LOG.error(e.getMessage());
	    }
            
            spark.close();
            daoFactory.close();
            
            try {
 		Thread.sleep(3000);
 	    } catch (InterruptedException e) {
 		LOG.error(e.getMessage());
 	    }
            
            System.exit(0);
        }
        
    }
}
