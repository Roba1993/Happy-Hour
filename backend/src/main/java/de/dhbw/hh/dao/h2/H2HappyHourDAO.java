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

import de.dhbw.hh.dao.HappyHourDAO;
import de.dhbw.hh.models.HappyHour;

/**
 * Diese Klasse kommuniziert direkt mit der H2-Datenbank
 * und stellt die in dem Interface HappyHourDAO definierten
 * Funktionen bereit.
 *
 * @author Marcus
 */
public class H2HappyHourDAO implements HappyHourDAO {
	
	// Initialisiert einen Logger für die Fehlerausgabe
    static final Logger LOG = LoggerFactory.getLogger(H2HappyHourDAO.class);
	
	 // Der Connectionpool
    ComboPooledDataSource cpds;

    /**
     * Diese Konstrukor-Funktion erstellt ein Objekt der Klasse H2HappyHourDAO.
     *
     * @param cpds Der Connectionpool
     */
    public H2HappyHourDAO(ComboPooledDataSource cpds) {
        this.cpds = cpds;
    }

    /**
     * Diese Funktion fügt eine HappyHour in die Datenbank hinzu
     *
     * @param HappyHour Das Happy Hour Objekt wird übergeben
     * @return True bei Erfolg, andernfalls false.
     */
	@Override
	public boolean insertHappyHour(HappyHour happyHour) {
		// SQL-String, um Happy Hours in die H2 Datenbank einzutragen
		String sql = "INSERT INTO happyHour "
				+ "(barID, description, start, end, monday, tuesday, wednesday, thursday, friday, saturday, sunday)"
				+ " VALUES (?,?,?,?,?,?,?,?,?,?,?)";

		// Holt eine Connection zur Datenbank aus dem Connectionpool 
        try (Connection connection = cpds.getConnection()) {
        	// verbietet den automatischen Commit zur Datenbank
            connection.setAutoCommit(false);

            // Erstellt das Prepared Statement für die Datenbankabfrage
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, happyHour.getBarID());
                preparedStatement.setString(2, happyHour.getDescription());
                preparedStatement.setTime(3, happyHour.getStart());
                preparedStatement.setTime(4, happyHour.getEnd());
                preparedStatement.setBoolean(5, happyHour.isMonday());
                preparedStatement.setBoolean(6, happyHour.isTuesday());
                preparedStatement.setBoolean(7, happyHour.isWednesday());
                preparedStatement.setBoolean(8, happyHour.isThursday());
                preparedStatement.setBoolean(9, happyHour.isFriday());
                preparedStatement.setBoolean(10, happyHour.isSaturday());
                preparedStatement.setBoolean(11, happyHour.isSunday());

                // Füge das Statement der Ausführungsschlange hinzu
                preparedStatement.addBatch();

                // Führe alle Statements aus
                preparedStatement.executeBatch();
            }

            // Schreibe die Änderungen in die Datenbank
            connection.commit();
            return true;
        } catch (SQLException e) {
        	LOG.error(e.getMessage());
        }
        return false;
	}

	/**
     * Diese Funktion löscht eine HappyHour aus der Datenbank anhand seiner ID.
     *
     * @param id Die BarID anhand die HappyHour zugeordnet wird.
     * @return True bei Erfolg, andernfalls false.
     */
	@Override
	public boolean deleteHappyHour(int id) {
		// SQL-String, um die Happy Hours aus der Datenbank zu löschen
		String sql = "DELETE FROM happyHour WHERE id=?";

		// Holt eine Connection zur Datenbank aus dem Connectionpool 
        try (Connection connection = cpds.getConnection()) {
        	// verbietet den automatischen Commit zur Datenbank
            connection.setAutoCommit(false);

            // Erstellt das Prepared Statement für die Datenbankabfrage
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, id);

                // Fügt das Statement der Ausführungsschlange hinzu
                preparedStatement.addBatch();

                // Führt alle Statements aus
                preparedStatement.executeBatch();
            }

            // Schreibt die Änderungen in die Datenbank
            connection.commit();
            return true;
        } catch (SQLException e) {
        	LOG.error(e.getMessage());
        }        
        return false;
	}

	/**
     * Diese Funktion findet eine HappyHour in der Datenbank anhand seiner ID.
     *
     * @param id Die BarID anhand die HappyHour zugeordnet wird.
     * @return happyHourArray bei Erfolg, andernfalls eine leere ArrayList.
     */
	@Override
	public Collection<HappyHour> findHappyHour(String barID) {
		// Abfrage-String der Happy Hours aus der H2 Datenbank
		String sql = "SELECT * FROM happyHour WHERE barID=?";

		// Holt eine Connection zur Datenbank aus dem Connectionpool
        try (Connection connection = cpds.getConnection()) {
        	// verbietet den automatischen Commit zur Datenbank
            connection.setAutoCommit(false);

            // Erstellt das Prepared Statement für die Datenbankabfrage
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, barID);

                // Holt die Daten von der Datenbank
                ResultSet resultSet = preparedStatement.executeQuery();
                
                // Erstellt eine Collection für die Datenrückgabe
                Collection<HappyHour> happyHourArray = new ArrayList<HappyHour>();

                /// Schreibt die Daten aus der Datenbank in die Collection
                while(resultSet.next()) {
                    HappyHour happyHour = new HappyHour();

                    happyHour.setBarID(resultSet.getString("barID"));
                    happyHour.setDescription(resultSet.getString("description"));
                    happyHour.setStart(resultSet.getTime("start"));
                    happyHour.setEnd(resultSet.getTime("end"));
                    happyHour.setMonday(resultSet.getBoolean("monday"));
                    happyHour.setTuesday(resultSet.getBoolean("tuesday"));
                    happyHour.setWednesday(resultSet.getBoolean("wednesday"));
                    happyHour.setThursday(resultSet.getBoolean("thursday"));
                    happyHour.setSaturday(resultSet.getBoolean("saturday"));
                    happyHour.setSunday(resultSet.getBoolean("sunday"));
                    happyHourArray.add(happyHour);
                }

                // Gibt die Collection
                return happyHourArray;
            }
        } catch (SQLException e) {
        	LOG.error(e.getMessage());
        }
        // Gibt eine leere Collection zurück
        return new ArrayList<HappyHour>();
	}

}