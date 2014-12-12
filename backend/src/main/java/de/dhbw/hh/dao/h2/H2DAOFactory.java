package de.dhbw.hh.dao.h2;

/**
 * 
 * @author Robert
 */

import com.mchange.v2.c3p0.ComboPooledDataSource;

<<<<<<< HEAD
import de.dhbw.hh.dao.BarReportDAO;
=======
>>>>>>> f436062f8b38ddeadf7a35c40912ec537f74fdfa
import de.dhbw.hh.dao.DAOFactory;
import de.dhbw.hh.dao.RouteDAO;
import de.dhbw.hh.dao.TestrunDAO;

import org.h2.tools.RunScript;

import java.beans.PropertyVetoException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * H2Factory zum erstellen der H2DAO Objekte.
 */
public class H2DAOFactory extends DAOFactory {

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
            e.printStackTrace();
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
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        
        // Wenn Daten in die Datenbank eingegeben werden, führe es aus
        if(properties.containsKey("db.h2.dataFile") && properties.getProperty("db.h2.dataFile") != null) {
            try (Connection connection = cpds.getConnection()) {
                RunScript.execute(connection, new FileReader(properties.getProperty("db.h2.dataFile")));
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        
    }

    @Override
    public TestrunDAO getTestrunDAO() {
        return new H2TestrunDAO(cpds);
    }
    
    @Override
	public BarReportDAO getBarReportDAO() {
		return new H2BarReportDAO(cpds);
	}

    @Override
    public void close() {	
        cpds.close();
    }

<<<<<<< HEAD
=======
	@Override
	public RouteDAO getRouteDAO() {
		return new H2RouteDAO(cpds);
	}
>>>>>>> f436062f8b38ddeadf7a35c40912ec537f74fdfa
}
