package de.dhbw.hh.dao.h2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import de.dhbw.hh.dao.RouteDAO;
import de.dhbw.hh.models.Route;

/**
 * Diese Klasse kommuniziert direkt mit der H2-Datenbank
 * und stellt die in dem Interface RouteDAO definierten Funktionen bereit
 * 
 * @author Maren
 */
public class H2RouteDAO implements RouteDAO {
	// Initialisiert einen Logger für die Fehlerausgabe
    static final Logger log = LoggerFactory.getLogger(H2RouteDAO.class);

	// Eine Variable zum Connectionpool der Datenbank wird erstellt
    private ComboPooledDataSource cpds;
    
    /**
     * Diese Konstruktur Funktion erstellt ein Objekt der Klasse H2RouteDAO.
     *
     * @param cpds Der Connectionpool
     */    
    public H2RouteDAO(ComboPooledDataSource cpds) {
        this.cpds = cpds;
    }

	// Im Folgenden kommen die einzelnen Datenbankabfragen für die Route Klasse
    
	@Override
	public boolean insertRoute(Route route) {
		String sql = "INSERT INTO route (hash, data, top) VALUES (?,?,?)";

        try (Connection connection = cpds.getConnection()) {
            // Immer ohne Autocommits arbeiten
            connection.setAutoCommit(false);

            // Immer mit PreparedStatements arbeiten
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, route.getHash());
                preparedStatement.setString(2, route.getData());
                preparedStatement.setBoolean(3, route.isTop());

                // Füge das Statement der Ausführungsschlange hinzu
                preparedStatement.addBatch();

                // Führe alle Statements aus
                preparedStatement.executeBatch();
            }

            // Schreibe die Änderungen in die Datenbank
            connection.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
		return false;
	}

	@Override
	public boolean deleteRoute(String hash) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Route findRoute(String hash) {
		// TODO Auto-generated method stub
		return null;
	}
		
	/**
	 * Diese Funktion frägt alle Top Routen aus der H2 Datenbank ab 
	 * und gibt sie in einer Collection zurück.
	 * 
	 * @author Maren
	 */
	@Override
	public Collection<Route> findTopRoutes() {
		// Abfrage-String der Top Routen aus der H2 Datenbank
		String sql = "SELECT hash, data, top FROM route WHERE top=true";

		 // Erstellt eine Collection für die Datenrückgabe
        Collection<Route> routes = new ArrayList<Route>();
		
		// Holt eine Connection zur Datenbank aus dem Connectionpool 
        try (Connection connection = cpds.getConnection()) {
            // verbietet den automatischen Commit zur Datenbank
        	connection.setAutoCommit(false);
            
        	// Erstellt das Prepared Statement für die Datenbankabfrage
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            	// Daten aus der Datenbank holen
                ResultSet resultSet = preparedStatement.executeQuery();

                // Schreibt die Daten aus der Datenbank in die Collection
                while(resultSet.next()) {
                    Route route = new Route();
                    route.setHash(resultSet.getString("hash"));
                    route.setData(resultSet.getString("data"));
                    route.setTop(resultSet.getBoolean("top"));
                    routes.add(route);
                }

                // Gibt die Collection zurück
                return routes;
            }  
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        
        // Gibt die leere Collection zurück
        return routes;
    }
          	
}
