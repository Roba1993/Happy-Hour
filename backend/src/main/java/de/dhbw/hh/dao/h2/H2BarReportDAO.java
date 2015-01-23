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

import de.dhbw.hh.dao.BarReportDAO;
import de.dhbw.hh.models.BarReport;

/**
 * Diese Klasse kommuniziert direkt mit der H2-Datenbank
 * und stellt die in dem Interface BarReportDAO definierten
 * Funktionen bereit.
 *
 * @author Jonas
 */
public class H2BarReportDAO implements BarReportDAO {
	
	// Initialisiert einen Logger für die Fehlerausgabe
    static final Logger LOG = LoggerFactory.getLogger(H2BarReportDAO.class);
	
	 // Der Connectionpool
    ComboPooledDataSource cpds;

    /**
     * Diese Funktion erstellt ein Objekt der Klasse H2BarReportDAO.
     *
     * @param cpds Der Connectionpool
     */
    public H2BarReportDAO(ComboPooledDataSource cpds) {
        this.cpds = cpds;
    }

    /**
     * Diese Funktion fügt ein BarReport in die Datenbank ein.
     *
     * @param barReport Der einzufügende BarReport.
     * @return True bei Erfolg, andernfalls false.
     */
	@Override
	public boolean insertBarReport(BarReport barReport) {
		String sql = "INSERT INTO barReport (barID, description) VALUES (?,?)";

        try (Connection connection = cpds.getConnection()) {
            // Immer ohne Autocommits arbeiten
            connection.setAutoCommit(false);

            // Immer mit PreparedStatements arbeiten
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, barReport.getBarID());
                preparedStatement.setString(2, barReport.getDescription());

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
     * Diese Funktion löscht alle BarReports zu einer zugehörigen Bar anhand der BarID
     *
     * @param barID Die BarID anhand der die BarReports zugeordnet werden.
     * @return True bei Erfolg, andernfalls false.
     */
	@Override
	public boolean deleteBarReport(String barID) {
		 String sql = "DELETE FROM barReport WHERE barID=?";
		 
	        try (Connection connection = cpds.getConnection()) {
	            // Immer ohne Autocommits arbeiten
	            connection.setAutoCommit(false);

	            // Immer mit PreparedStatements arbeiten
	            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
	                preparedStatement.setString(1, barID);

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
     * Diese Funktion löscht einen BarReport aus der Datenbank anhand seiner ID.
     *
     * @param id Die id anhand der BarReport zugeordnet wird.
     * @return True bei Erfolg, andernfalls false.
     */
	@Override
	public boolean deleteSpecificBarReport(int id) {
		 String sql = "DELETE FROM barReport WHERE id=?";

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

	/**
     * Diese Funktion findet einen bestimmten BarReport anhand seiner BarID.
     *
     * @param barID Die BarID nach der gesucht werden soll.
     * @return Gibt bei Erfolg das gefundene BarReport-Objekt zurück. Wenn nichts
     * gefunden wird, enthält die Rückgabe den Wert null.
     */
	@Override
	public Collection<BarReport> findBarReport(String barID) {
		String sql = "SELECT id, barID, description FROM barReport WHERE barID=?";

        try (Connection connection = cpds.getConnection()) {
            // Immer ohne Autocommits arbeiten
            connection.setAutoCommit(false);

            // Immer mit PreparedStatements arbeiten
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, barID);

                // Hole die Daten von der Datenbank
                ResultSet resultSet = preparedStatement.executeQuery();
                
                Collection<BarReport> runs = new ArrayList<BarReport>();

                // Schreibe die Daten ins BarReport Objekt
                while(resultSet.next()) {
                    BarReport barReport = new BarReport();
                    barReport.setID(resultSet.getInt("id"));
                    barReport.setBarID(resultSet.getString("barID"));
                    barReport.setDescription(resultSet.getString("description"));
                    runs.add(barReport);
                }

                // Gebe alle Datenobjekte als Array zurück
                return runs;
            }
        } catch (SQLException e) {
        	LOG.error(e.getMessage());
        }

        return new ArrayList<BarReport>();
	}

	/**
     * Diese Funktion sucht alle BarReports in der Datenbank mit dem angegebenen Reported-Wahrheitswert.
     *
     * @param reported Der Reported-Wahrheitswer nach dem gesucht werden soll.
     * @return BarReport-Array mit den gefundenen BarReports.
     */
	@Override
	public Collection<BarReport> findAllBarReports() {
		String sql = "SELECT id, barID, description FROM barReport";

        try (Connection connection = cpds.getConnection()) {
            // Immer ohne Autocommits arbeiten
            connection.setAutoCommit(false);

            // Immer mit PreparedStatements arbeiten
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

                // Hole die Daten von der Datenbank
                ResultSet resultSet = preparedStatement.executeQuery();

                Collection<BarReport> runs = new ArrayList<BarReport>();

                // Schreibe die Daten ins BarReport Objekt
                while(resultSet.next()) {
                    BarReport barReport = new BarReport();
                    barReport.setID(resultSet.getInt("id"));
                    barReport.setBarID(resultSet.getString("barID"));
                    barReport.setDescription(resultSet.getString("description"));
                    runs.add(barReport);
                }

                // Gebe alle Datenobjekte als Array zurück
                return runs;
            }
        } catch (SQLException e) {
        	LOG.error(e.getMessage());
        }

        return new ArrayList<BarReport>();
	}

}
