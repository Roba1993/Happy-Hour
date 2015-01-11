package de.dhbw.hh.dao.h2;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import de.dhbw.hh.dao.UserDAO;

/**
 * Mit dieser Klasse können die User auf der H2 Datenbank verwaltet werden
 *
 * @author Maren
 */
public class H2UserDAO implements UserDAO {
		
	// Initialisiert einen Logger für die Fehlerausgabe
    static final Logger log = LoggerFactory.getLogger(H2UserDAO.class);

	// Eine Variable zum Connectionpool der Datenbank wird erstellt
    private ComboPooledDataSource cpds;
    
    /**
     * Diese Konstruktur Funktion erstellt ein Objekt der Klasse H2RouteDAO.
     *
     * @param cpds Der Connectionpool
     */    
    public H2UserDAO(ComboPooledDataSource cpds) {
        this.cpds = cpds;
    }
		
	/**
	 * Überprüft ob das gehashte Passwort mit dem in der Datenbank übereinstimmt
	 * @return Boolean
	 */		
	@Override
	public boolean checkLogin(String name, String password) {
		// Abfrage-String der Daten aus der H2-Datenbank mit Bedingung
		String sql = "SELECT name FROM user WHERE name=? and hashPw=?";
		
		// eingegebenes passwort in hash umwandeln
		String hashPw = md5(password);

		// Holt eine Connection zur Datenbank aus dem Connectionpool
		try (Connection connection = cpds.getConnection()) {
			// verbietet den automatischen Commit zur Datenbank
			connection.setAutoCommit(false);

			// Erstellt das Prepared Statement für die Datenbankabfrage
			try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
				// Fügt den Name in die SQL-Abfrage ein
				preparedStatement.setString(1, name);
				// Fügt das gehashte Passwort in die SQL-Abfrage ein
				preparedStatement.setString(2, hashPw);

				// Führt die SQL-Abfrage durch und speichert die zurückgegebenen Daten in eine Collection
				ResultSet resultSet = preparedStatement.executeQuery();

				// Überprüft die Rückgabe der SQL-Abfrage
				if(resultSet.wasNull()) {
					// Gibt false zurück, wenn die Rückgabe-Collection aus der Datenbank leer ist
					return false;
				} else {
					// Wenn ein Wert vorhanden ist, wird true zurückgegegben
					return true;
				}
			}catch(Exception e){
				e.printStackTrace();
	            }
		} catch (SQLException e) {
			log.error(e.getMessage());
		}
		// Gibt false zurück, wenn Passwort nicht gleich
		return false;
	}
	
	/**
	 * Diese Methode wandelt einen String, der übergeben wird, in einen MD5 Hash um
	 * @param input
	 * @return hash
	 */
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

