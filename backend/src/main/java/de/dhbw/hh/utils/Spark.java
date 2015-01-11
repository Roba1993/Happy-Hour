package de.dhbw.hh.utils;

import de.dhbw.hh.dao.DAOFactory;
import de.dhbw.hh.rest.BarsREST;
import de.dhbw.hh.rest.LoginFILTER;
import de.dhbw.hh.rest.HappyHourREST;
import de.dhbw.hh.rest.ReportsREST;
import de.dhbw.hh.rest.RoutesREST;
import de.dhbw.hh.rest.TopRouteREST;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static spark.Spark.*;

/**
 * Diese Klasse startet den Spark REST Server.
 * 
 * @author Robert
 */
public class Spark {

	static final Logger LOG = LoggerFactory.getLogger(Spark.class);

	/**
	 * Diese Funktion startet den Spark-Server und initialisiert die Routes im
	 * rest Package.
	 */
	public Spark(Properties properties, DAOFactory daoFactory) {
		// Setzte den Port wenn die Angabe existiert und eine Zahl ist
		if (properties.containsKey("server.port")
				&& properties.getProperty("server.port") != null
				&& properties.getProperty("server.port").matches("-?\\d+")) {
			setPort(Integer.valueOf(properties.getProperty("server.port")));
		}

		// Hoste den frontend/app Ordner
		if (properties.containsKey("server.web")
				&& properties.getProperty("server.web") != null) {
			externalStaticFileLocation(properties.getProperty("server.web"));
		}

		// Filter einbinden
		new LoginFILTER(daoFactory);		
		
		// Binde die Rest-Module ein
		new ReportsREST(daoFactory);
		new TopRouteREST(daoFactory);
		new RoutesREST(daoFactory);
		new BarsREST(daoFactory);
		new HappyHourREST(daoFactory);
	}

	/**
	 * Funktion zum stoppen des Spark-Servers
	 */
	public void close() {
		stop();
		LOG.info("Spark wurde beendet");
	}
}
