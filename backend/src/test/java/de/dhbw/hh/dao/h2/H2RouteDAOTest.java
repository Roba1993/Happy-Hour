package de.dhbw.hh.dao.h2;

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

/**
 * Diese Klasse testet die Anfrage der Routen
 * 
 * @author Maren
 */
public class H2RouteDAOTest {

	// Der Connectionpool für die Tests
	private static ComboPooledDataSource cpds;

	// Die zu Testende Klasse
	private static H2RouteDAO h2RouteDAO;

	// Route Klasse zum Testen
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

		// Erstellung der Connection zur Datenbank
		try (Connection connection = cpds.getConnection()) {
			// Initialisiert die Datenbank durch das angegeben SQL-File
			RunScript.execute(connection, new FileReader("db-h2-create.sql"));
		}

		// Erzeugt ein Objekt der zu testenden Klasse
		h2RouteDAO = new H2RouteDAO(cpds);
	}

	/**
	 * Diese Funktion wird vor jedem Testrun aufgerufen und setzt die vorherigen
	 * Änderungen an der Datenbank zurück.
	 *
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		// Befüllt das Route Objekt zum Testen
		route = new Route();
		route.setHash("kjasifhuidjfelosamnb");
		route.setData("Ich bin ein Json String Objekt");
		route.setTop(false);

		// Löscht alle Einträge aus der Route Tabelle
		try (Connection connection = cpds.getConnection()) {
			try (PreparedStatement preparedStatement = connection
					.prepareStatement("DELETE FROM route")) {
				preparedStatement.execute();
			}

			// Fügt das neue Route Objekt in die Datenbank ein
			try (PreparedStatement preparedStatement = connection
					.prepareStatement("INSERT INTO route (hash, data, top) "
							+ "VALUES (?,?,?)")) {
				preparedStatement.setString(1, route.getHash());
				preparedStatement.setString(2, route.getData());
				preparedStatement.setBoolean(3, route.isTop());
				preparedStatement.execute();
			}
		}
	}

	/**
	 * Test, ob die Route gefunden werden konnte
	 *
	 * @author Tabea
	 */
	@Test
	public void testFindRoute() throws Exception {
		// Abfrage der Routen nach einem Hash-Wert
		Route r = h2RouteDAO.findRoute("kjasifhuidjfelosamnb");
		// Test: Hash und Data müssen übereinstimmen, der Boolean topRoute muss
		// false sein
		assertEquals("kjasifhuidjfelosamnb", r.getHash());
		assertEquals(false, r.isTop());
		assertEquals("Ich bin ein Json String Objekt", r.getData());

	}

	/**
	 * Test, ob die TopRouten gefunden werden können
	 *
	 * @author Maren
	 */
	@Test
	public void testFindTopRoutes() throws Exception {
		// Zweiter Test-Datensatz wird erstellt
		Route route2 = new Route();
		route2.setHash("jhkjahdeuwfewsjmchs");
		route2.setData("Ich bin ein Json String Objekt");
		route2.setTop(true);

		// Dritter Test-Datensatz wird erstellt
		Route route3 = new Route();
		route3.setHash("sdweuhdrjnkistzhdjwb");
		route3.setData("Ich bin ein Json String Objekt");
		route3.setTop(true);

		// Holt eine Connection zur Datenbank aus dem Connectionpool
		try (Connection connection = cpds.getConnection()) {
			// Fügt den zweiten und dritten Datensatz in die Datenbank ein
			try (PreparedStatement preparedStatement = connection
					.prepareStatement("INSERT INTO route (hash, data, top) "
							+ "VALUES (?,?,?), (?,?,?)")) {
				preparedStatement.setString(1, route2.getHash());
				preparedStatement.setString(2, route2.getData());
				preparedStatement.setBoolean(3, route2.isTop());

				preparedStatement.setString(4, route3.getHash());
				preparedStatement.setString(5, route3.getData());
				preparedStatement.setBoolean(6, route3.isTop());
				preparedStatement.execute();
			}
		}

		// Abfrage der Top Routen
		Collection<Route> routes = h2RouteDAO.findTopRoutes();

		// Erstellt einen Irator zum Testen
		Iterator<Route> iterator = routes.iterator();

		// Test: Es müssen zwei Datensätze vorhanden sein
		assertEquals(2, routes.size());

		// Test: Beide Datensätze müssen eine Top Route sein
		assertEquals(true, iterator.next().isTop());
		assertEquals(true, iterator.next().isTop());
	}

	/**
	 * 
	 * @author Michael
	 */
	@Test
	public void testInsertRoute() throws Exception {
		// Löscht alle Einträge aus der Route Tabelle
		try (Connection connection = cpds.getConnection()) {
			try (PreparedStatement preparedStatement = connection
					.prepareStatement("DELETE FROM route")) {
				preparedStatement.execute();
			}
		}

		// Neue route definieren
		Route route = new Route();
		route.setHash("07e0c07a6c711c4eee99e03e753d4ace");
		route.setData("Ich bin ein Json String Objekt");
		route.setTop(false);
		// route in Datenbank einfügen
		boolean routes = h2RouteDAO.insertRoute(route);

		// Methode muss true zurückgegen, dann war das einfügen erfolgreich
		assertTrue(routes);

		// Frage den eingefügte Route aus Datenbank ab
		ResultSet resultSet;
		try (Connection connection = cpds.getConnection()) {
			try (PreparedStatement preparedStatement = connection
					.prepareStatement("SELECT * FROM route")) {
				resultSet = preparedStatement.executeQuery();

				// Prüfe ob genau eine Route ausgelesen wurde
				resultSet.last();
				assertEquals(1, resultSet.getRow());

			}
		}

	}

}
