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
     * Diese Funktion erstellt ein Objekt der Klasse H2TestDAO.
     *
     * @param cpds Der Connectionpool
     */
    public H2HappyHourDAO(ComboPooledDataSource cpds) {
        this.cpds = cpds;
    }

	@Override
	public boolean insertHappyHour(HappyHour happyHour) {
		String sql = "INSERT INTO happyHour "
				+ "(barID, description, start, end, monday, tuesday, wednesday, thursday, friday, saturday, sunday)"
				+ " VALUES (?,?,?,?,?,?,?,?,?,?,?)";

        try (Connection connection = cpds.getConnection()) {
            // Immer ohne Autocommits arbeiten
            connection.setAutoCommit(false);

            // Immer mit PreparedStatements arbeiten
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

	@Override
	public boolean deleteHappyHour(int id) {
		String sql = "DELETE FROM happyHour WHERE id=?";

        try (Connection connection = cpds.getConnection()) {
            // Immer ohne Autocommits arbeiten
            connection.setAutoCommit(false);

            // Immer mit PreparedStatements arbeiten
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, id);

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

	@Override
	public Collection<HappyHour> findHappyHour(String barID) {
		String sql = "SELECT * FROM happyHour WHERE barID=?";

        try (Connection connection = cpds.getConnection()) {
            // Immer ohne Autocommits arbeiten
            connection.setAutoCommit(false);

            // Immer mit PreparedStatements arbeiten
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, barID);

                // Hole die Daten von der Datenbank
                ResultSet resultSet = preparedStatement.executeQuery();
                
                Collection<HappyHour> happyHourArray = new ArrayList<HappyHour>();

                // Schreibe die Daten ins Testrun Objekt
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

                // Gebe alle Datenobjekte als Array zurück
                return happyHourArray;
            }
        } catch (SQLException e) {
        	LOG.error(e.getMessage());
        }

        return new ArrayList<HappyHour>();
	}

}