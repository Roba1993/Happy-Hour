package de.dhbw.hh.dao.h2;

import static org.junit.Assert.*;

import java.io.FileReader;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;

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
		String hashPw = md5("Passwort");
										
		// Löscht alle Einträge aus der User Tabelle
		try (Connection connection = cpds.getConnection()) {
			try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM user")) {
				preparedStatement.execute();
			}
				
			// Fügt das neue User Objekt in die Datenbank ein
			try (PreparedStatement preparedStatement = connection.prepareStatement(
					"INSERT INTO user (name, hashPw) " +
			                        "VALUES (?,?)")) {
				preparedStatement.setString(1, user.getName());
				preparedStatement.setString(2, hashPw);
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
		// Übergabe des Namens und des Passworts an die checkLogin Funktion
		boolean user = h2UserDAO.checkLogin("Admin", "Passwort");
					
		// Methode muss true zurückgegen, dann war die Überprüfung des Passworts erfolgreich 
		assertTrue(user);
	}
	
	private static String md5(String input) {
		// Erzeugt einen hash-String, in den der Hash für die Rückgabe eingefügt wird
        String hash = null;
        
        // Wenn der übergebene Wert null ist, wird auch null zurückgegeben
        if(null == input) return null;
         
        try {  
        // Erstellt ein Message Digest Objekt für MD5
        MessageDigest digest = MessageDigest.getInstance("MD5");
         
        // Updatet den Input-String in das Message Digest Objekt
        digest.update(input.getBytes(), 0, input.length());
 
        // Konvertiert Message Digest Wert in einen Basis 16 (hex) Hash
        hash = new BigInteger(1, digest.digest()).toString(16);
 
        } catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
        // Rückgabe des MD5 Hash-Werts
        return hash;
	}
		
}


