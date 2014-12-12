package de.dhbw.hh.dao.h2;

/**
 * Diese Klasse kommuniziert direkt mit der H2-Datenbank
 * und stellt die in dem Interface RouteDAO definierten Funktionen bereit
 * 
 * @author Maren
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import de.dhbw.hh.dao.RouteDAO;
import de.dhbw.hh.models.Route;
import de.dhbw.hh.models.Testrun;

public class H2RouteDAO implements RouteDAO {

	// Der Connectionpool 
    private ComboPooledDataSource cpds;

    /**
     * Diese Funktion erstellt ein Objekt der Klasse H2RouteDAO.
     *
     * @param cpds Der Connectionpool
     */
    
    public H2RouteDAO(ComboPooledDataSource cpds) {
        this.cpds = cpds;
    }

	
	
	@Override
	public boolean insertRoute(Route route) {
		// TODO Auto-generated method stub
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
	 * Diese Funktion frägt alle Top Routen aus der Datenbank ab und gibt sie in einem Array zurück
	 * 
	 * @author Maren
	 */

	@Override
	public Route[] findTopRoutes() {
		
		//Abfrage aus der Datenbank
		String sql = "SELECT hash, data, top FROM route WHERE top=true";

        try (Connection connection = cpds.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            	// Daten aus der Datenbank holen
                ResultSet resultSet = preparedStatement.executeQuery();

                Collection<Route> routes = new ArrayList<Route>();

                // Schreibe die Daten ins Route Objekt
                while(resultSet.next()) {
                    Route route = new Route();
                    route.setHash(resultSet.getString("hash"));
                    route.setData(resultSet.getString("data"));
                    route.setTop(resultSet.getBoolean("top"));
                    routes.add(route);
                }

                // Gebe alle Datenobjekte als Array zurück
                return (Route[]) routes.toArray(new Route[routes.size()]);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new Route[0];
    }
            	
            }
        

