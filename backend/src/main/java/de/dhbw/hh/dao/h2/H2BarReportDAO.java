package de.dhbw.hh.dao.h2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import de.dhbw.hh.dao.BarReportDAO;
import de.dhbw.hh.models.BarReport;

/**
 * Diese Klasse kommuniziert direkt mit der H2-Datenbank
 * und stellt die in dem Interface BarReportDAO definierten
 * Funktionen bereit.
 * @author Jonas
 *
 */

public class H2BarReportDAO implements BarReportDAO {
	
	 // Der Connectionpool
    ComboPooledDataSource cpds;

    /**
     * Diese Funktion erstellt ein Objekt der Klasse H2TestDAO.
     *
     * @param cpds Der Connectionpool
     */
    public H2BarReportDAO(ComboPooledDataSource cpds) {
        this.cpds = cpds;
    }

	@Override
	public boolean insertBarReport(BarReport barReport) {
		String sql = "INSERT INTO barReport (barID, description, reported) VALUES (?,?,?)";

        try (Connection connection = cpds.getConnection()) {
            // Immer ohne Autocommits arbeiten
            connection.setAutoCommit(false);

            // Immer mit PreparedStatements arbeiten
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, barReport.getBarID());
                preparedStatement.setString(2, barReport.getDescription());
                preparedStatement.setBoolean(3, barReport.isReported());

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
	public boolean updateBarReport(BarReport barReport) {
		String sql = "UPDATE barReport SET description=?, reported=? WHERE barID=?";

        try (Connection connection = cpds.getConnection()) {
            // Immer ohne Autocommits arbeiten
            connection.setAutoCommit(false);

            // Immer mit PreparedStatements arbeiten
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, barReport.getDescription());
                preparedStatement.setBoolean(2, barReport.isReported());
                preparedStatement.setString(3, barReport.getBarID());

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
	            e.printStackTrace();
	        }
	        
	        return false;
	}

	@Override
	public BarReport findBarReport(String barID) {
		String sql = "SELECT barID, description, reported FROM barReport WHERE barID=?";

        try (Connection connection = cpds.getConnection()) {
            // Immer ohne Autocommits arbeiten
            connection.setAutoCommit(false);

            // Immer mit PreparedStatements arbeiten
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, barID);

                // Hole die Daten von der Datenbank
                ResultSet resultSet = preparedStatement.executeQuery();

                // Schreibe die Daten ins Testrun Objekt
                if(resultSet.next()) {
                    BarReport barReport = new BarReport();
                    barReport.setBarID(resultSet.getString("barID"));
                    barReport.setDescription(resultSet.getString("description"));
                    barReport.setReported(resultSet.getBoolean("reported"));
                    return barReport;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
	}

	@Override
	public Collection<BarReport> findBarReportsByReported(boolean reported) {
		String sql = "SELECT barID, description, reported FROM barReport WHERE reported=?";

        try (Connection connection = cpds.getConnection()) {
            // Immer ohne Autocommits arbeiten
            connection.setAutoCommit(false);

            // Immer mit PreparedStatements arbeiten
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setBoolean(1, reported);

                // Hole die Daten von der Datenbank
                ResultSet resultSet = preparedStatement.executeQuery();

                Collection<BarReport> runs = new ArrayList<BarReport>();

                // Schreibe die Daten ins Testrun Objekt
                while(resultSet.next()) {
                    BarReport barReport = new BarReport();
                    barReport.setBarID(resultSet.getString("barID"));
                    barReport.setDescription(resultSet.getString("description"));
                    barReport.setReported(resultSet.getBoolean("reported"));
                    runs.add(barReport);
                }

                // Gebe alle Datenobjekte als Array zurück
                return runs;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<BarReport>();
	}

}