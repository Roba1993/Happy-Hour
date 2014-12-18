package de.dhbw.hh.dao.h2;

/**
 * Diese Klasse testet die Anfrage der Top Routen
 * 
 * @author Maren
 */

import static org.junit.Assert.*;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.Iterator;

import org.h2.tools.RunScript;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import de.dhbw.hh.models.Route;

public class H2RouteDAOTest {
	
	   // Der Connectionpool für die Tests
    private static ComboPooledDataSource cpds;

    // Die zu Testende Klasse
    private static H2RouteDAO h2RouteDAO;

    // Route Klasse zum Wiederverwenden
    private static Route route;

    
    /**
     * Diese Klasse wird als erste aufgerufen und dient zur Testkonfiguration
     * 
     * @throws Exception
     */

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

        // Eine temporäre Inmemory-Datenbank wird erstellt
        cpds = new ComboPooledDataSource();
        cpds.setDriverClass("org.h2.Driver");
        cpds.setJdbcUrl("jdbc:h2:mem:UTRouteTestDAO");
        cpds.setUser("root");
        cpds.setPassword("toor");
        cpds.setMaxStatements(10);
        cpds.setMaxPoolSize(1);
        cpds.setAutoCommitOnClose(true);

        // Erstellt die Datenbank
        try (Connection connection = cpds.getConnection()) {
            RunScript.execute(connection, new FileReader("db-h2-create.sql"));
        }

        // Erzeugt ein Objekt der zu Testenden Klasse
        h2RouteDAO = new H2RouteDAO(cpds);
	}

	
	 /**
     * Diese Funktion wird vor jedem Testrun aufgerufen und setzt die
     * vorherigen Änderungen an der Datenbank zurück.
     *
     * @throws Exception
     */
	
	@Before
	public void setUp() throws Exception {
		// Befüllt das Route Objekt zum Wiederverwenden 
		route = new Route();
		route.setHash("kjasifhuidjfelosamnb");
		route.setData("Ich bin ein Json String Objekt");
		route.setTop(true);
		
		
		// Löscht alle Einträge
		try (Connection connection = cpds.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM route")) {
                preparedStatement.execute();
            }
		
		 // Fügt das Route Objekt in die Datenbank ein
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO route (hash, data, top) " +
                        "VALUES (?,?,?)")) {
            preparedStatement.setString(1, route.getHash());
            preparedStatement.setString(2, route.getData());
            preparedStatement.setBoolean(3, route.isTop());
            preparedStatement.execute();
        }
		}
	}
	
	/**
	 * Test, ob die TopRouten gefunden werden können
	 */

	@Test
	public void testFindTopRoutes() throws Exception {
		// Zweiten und Dritten Datensatz werden erstellt
		Route route2 = new Route();
		route2.setHash("jhkjahdeuwfewsjmchs");
		route2.setData("Ich bin ein Json String Objekt");
		route2.setTop(true);
		
		Route route3 = new Route();
		route3.setHash("sdweuhdrjnkistzhdjwb");
		route3.setData("Ich bin ein Json String Objekt");
		route3.setTop(false);
		
		// Fügt den zweiten und dritten Datensatz in die Datenbank ein
        try (Connection connection = cpds.getConnection()) {
            
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO route (hash, data, top) " +
                            "VALUES (?,?,?), (?,?,?)")) {
                preparedStatement.setString(1, route2.getHash());
                preparedStatement.setString(2, route2.getData());
                preparedStatement.setBoolean(3, route2.isTop());

                preparedStatement.setString(4, route3.getHash());
                preparedStatement.setString(5, route3.getData());
                preparedStatement.setBoolean(6, route3.isTop());
                preparedStatement.execute();
                
            }
		
        }
		
        // Die gefundenen Top Routen werden in ein Array geschrieben
        
        Collection<Route> routes = h2RouteDAO.findTopRoutes();
        
        Iterator<Route> iterator = routes.iterator();
        
        assertEquals(2, routes.size());
        assertEquals(true, iterator.next().isTop());
        assertEquals(true, iterator.next().isTop());
        
       
	}

	 /**
     * Diese Methode gibt auf der Konsole die aktuelle Tabelle aus.
     * Zum schreiben der Teste und der fehlersuche kann diese Funktion sehr
     * hilfrei sein.
     *
     * @throws Exception
     */
    private void showRows() throws Exception {
        try (Connection connection = cpds.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM route")) {
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
                        System.out.print(resultSet.getString(i + 1) + " | ");
                    }
                    System.out.println();
                }
            }
        }
    }
	
}
