package de.dhbw.hh.dao.h2;

import static org.junit.Assert.*;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.h2.tools.RunScript;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import de.dhbw.hh.models.User;

/**
 * Diese Klasse testet die Anfrage der User
 * 
 * @author Maren
 */
public class H2UserDAOTest {
	
	// Der Connectionpool für die Tests
    private static ComboPooledDataSource cpds;

    // Die zu Testende Klasse
    private static H2UserDAO h2UserDAO;

    // Route Klasse zum Testen
    private static User user;

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
        cpds.setJdbcUrl("jdbc:h2:mem:UTUserTestDAO");
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
        h2UserDAO = new H2UserDAO(cpds);
	}

	 /**
     * Diese Funktion wird vor jedem Testrun aufgerufen und setzt die
     * vorherigen Änderungen an der Datenbank zurück.
     *
     * @throws Exception
     */	
	@Before
	public void setUp() throws Exception {
		// Befüllt das User Objekt zum Testen 
				user = new User();
				user.setName("Admin");
				// user.setHashPw("password");
				
				// Löscht alle Einträge aus der User Tabelle
				try (Connection connection = cpds.getConnection()) {
		            try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM user")) {
		                preparedStatement.execute();
		            }
				
					// Fügt das neue User Objekt in die Datenbank ein
			        try (PreparedStatement preparedStatement = connection.prepareStatement(
			                "INSERT INTO user (name) " +
			                        "VALUES (?)")) {
			            preparedStatement.setString(1, user.getName());
			            // preparedStatement.setString(2, user.getHashPw());
			            preparedStatement.execute();
			        }
				}
	}

	/**
	 * Test, ob eine Übereinstimmung der Passwörter gefunden werden kann
     *
     * @author Maren
	 */
	@Test
	public void testCheckLogin() {
		try (Connection connection = cpds.getConnection()) {
			
			try (PreparedStatement preparedStatement = connection.prepareStatement
					("SELECT name FROM user WHERE name=? and hashPw=?")) {
				
					// Führt die SQL-Abfrage durch und speichert die zurückgegebenen Daten in eine Collection
					ResultSet resultSet = preparedStatement.executeQuery();

					// Überprüft die Rückgabe der SQL-Abfrage
					if(resultSet.wasNull()) {
						// Gibt false zurück, wenn die Rückgabe-Collection aus der Datenbank leer ist
						System.out.println("Das eingegebene Passwort stimmt NICHT mit dem aus der Datenbank überein.");
					} else {
						// Wenn ein Wert vorhanden ist, wird true zurückgegegben
						System.out.println("Das eingegebene Passwort stimmt mit dem aus der Datenbank überein.");
					}
			} catch (Exception e){
					e.printStackTrace();
					}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
		
}


