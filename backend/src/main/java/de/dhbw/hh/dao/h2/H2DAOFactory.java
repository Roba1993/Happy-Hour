package de.dhbw.hh.dao.h2;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import de.dhbw.hh.dao.*;

import org.h2.tools.RunScript;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyVetoException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * H2Factory zum erstellen der H2DAO Objekte.
 *
 * @author Robert
 */
public class H2DAOFactory extends DAOFactory {
	
	// Initialisiert einen Logger für die Fehlerausgabe
    static final Logger LOG = LoggerFactory.getLogger(H2RouteDAO.class);

    // Der Connectionpool
    private ComboPooledDataSource cpds;

    /**
     * Constructor Funktion zum erstellen des Objektes.
     * Es erstellt eine Verbindung zur Datenbank über
     * einen Connectionpool her. Zur Konfiguration werden
     * die entsprechenden Einstellungen in den Properties
     * benötigt.
     *
     * @param properties Die Einstellungen zum erstellen der
     *                   Datenbank connection.
     */
    public H2DAOFactory(Properties properties) {
        cpds = new ComboPooledDataSource();

        // Versuche den Datenbank Treiber zu laden
        try {
            cpds.setDriverClass("org.h2.Driver");
        } catch (PropertyVetoException e) {
            LOG.error(e.getMessage());
        }

        // Setzte die Datenbank Einstellungen
        cpds.setJdbcUrl(properties.getProperty("db.h2.url"));
        cpds.setUser(properties.getProperty("db.h2.user"));
        cpds.setPassword(properties.getProperty("db.h2.password"));
        cpds.setMaxStatements(Integer.parseInt(properties.getProperty("db.h2.maxStatements")));
        cpds.setMaxPoolSize(Integer.parseInt(properties.getProperty("db.h2.maxPoolSize")));

        // Wenn ein Datenbank Start File angegeben wurde, führe es aus
        if(properties.containsKey("db.h2.startFile") && properties.getProperty("db.h2.startFile") != null) {
            try (Connection connection = cpds.getConnection()) {
                RunScript.execute(connection, new FileReader(properties.getProperty("db.h2.startFile")));
            } catch (SQLException e) {
            	LOG.error(e.getMessage());
            } catch (FileNotFoundException e) {
            	LOG.error(e.getMessage());
            }
        }
        
        // Wenn Daten in die Datenbank eingegeben werden, führe es aus
        if(properties.containsKey("db.h2.dataFile") && properties.getProperty("db.h2.dataFile") != null) {
            try (Connection connection = cpds.getConnection()) {
                RunScript.execute(connection, new FileReader(properties.getProperty("db.h2.dataFile")));
            } catch (SQLException e) {
            	LOG.error(e.getMessage());
            } catch (FileNotFoundException e) {
            	LOG.error(e.getMessage());
            }
        }

    }

    //******* Folgend die Zugriffsfunktionen auf die DAO Klassen
    @Override
	public BarReportDAO getBarReportDAO() {
		return new H2BarReportDAO(cpds);
	}

    @Override
    public void close() {	
        cpds.close();
    }

	@Override
	public RouteDAO getRouteDAO() {
		return new H2RouteDAO(cpds);
	}
	
	@Override
	public HappyHourDAO getHappyHourDAO() {
		return new H2HappyHourDAO(cpds);
	}

	@Override
	public FoursquareDAO getFoursquareDAO() {
		return new H2FoursquareDAO();
	}
	
	@Override
	public UserDAO getUserDAO() {
		return new H2UserDAO(cpds);
	}
	
}
