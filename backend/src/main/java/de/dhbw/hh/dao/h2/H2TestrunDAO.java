package de.dhbw.hh.dao.h2;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import de.dhbw.hh.dao.TestrunDAO;
import de.dhbw.hh.models.Testrun;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Diese Klasse stellt kommuniziert direkt mit der H2-DAtenbank
 * und stellt die in dem Interface TestDAO definierten
 * Funktionen bereit.
 */
public class H2TestrunDAO implements TestrunDAO {

    // Der Connectionpool
    private ComboPooledDataSource cpds;

    /**
     * Diese Funktion erstellt ein Objekt der Klasse H2TestDAO.
     *
     * @param cpds Der Connectionpool
     */
    public H2TestrunDAO(ComboPooledDataSource cpds) {
        this.cpds = cpds;
    }


    @Override
    public boolean insertTestrun(Testrun testrun) {
        String sql = "INSERT INTO testrun (name, date, rounds) VALUES (?,?,?)";

        try (Connection connection = cpds.getConnection()) {
            // Immer ohne Autocommits arbeiten
            connection.setAutoCommit(false);

            // Immer mit PreparedStatements arbeiten
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, testrun.getName());
                preparedStatement.setTimestamp(2, testrun.getDate());
                preparedStatement.setLong(3, testrun.getRounds());

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
    public boolean updateTestrun(Testrun testrun) {
        String sql = "UPDATE testrun SET name=?, date=?, rounds=? WHERE id=?";

        try (Connection connection = cpds.getConnection()) {
            // Immer ohne Autocommits arbeiten
            connection.setAutoCommit(false);

            // Immer mit PreparedStatements arbeiten
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, testrun.getName());
                preparedStatement.setTimestamp(2, testrun.getDate());
                preparedStatement.setLong(3, testrun.getRounds());
                preparedStatement.setLong(4, testrun.getId());

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
    public boolean deleteTestrun(long id) {
        String sql = "DELETE FROM testrun WHERE id=?";

        try (Connection connection = cpds.getConnection()) {
            // Immer ohne Autocommits arbeiten
            connection.setAutoCommit(false);

            // Immer mit PreparedStatements arbeiten
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setLong(1, id);

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
    public Testrun findTestrun(long id) {
        String sql = "SELECT id, name, date, rounds FROM testrun WHERE id=?";

        try (Connection connection = cpds.getConnection()) {
            // Immer ohne Autocommits arbeiten
            connection.setAutoCommit(false);

            // Immer mit PreparedStatements arbeiten
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setLong(1, id);

                // Hole die Daten von der Datenbank
                ResultSet resultSet = preparedStatement.executeQuery();

                // Schreibe die Daten ins Testrun Objekt
                if(resultSet.next()) {
                    Testrun testrun = new Testrun();
                    testrun.setId(resultSet.getLong("id"));
                    testrun.setName(resultSet.getString("name"));
                    testrun.setDate(resultSet.getTimestamp("date"));
                    testrun.setRounds(resultSet.getLong("rounds"));
                    return testrun;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Collection<Testrun> findTestrunsByName(String name) {
        String sql = "SELECT id, name, date, rounds FROM testrun WHERE name=?";

        try (Connection connection = cpds.getConnection()) {
            // Immer ohne Autocommits arbeiten
            connection.setAutoCommit(false);

            // Immer mit PreparedStatements arbeiten
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, name);

                // Hole die Daten von der Datenbank
                ResultSet resultSet = preparedStatement.executeQuery();

                Collection<Testrun> runs = new ArrayList<Testrun>();

                // Schreibe die Daten ins Testrun Objekt
                while(resultSet.next()) {
                    Testrun testrun = new Testrun();
                    testrun.setId(resultSet.getLong("id"));
                    testrun.setName(resultSet.getString("name"));
                    testrun.setDate(resultSet.getTimestamp("date"));
                    testrun.setRounds(resultSet.getLong("rounds"));
                    runs.add(testrun);
                }

                // Gebe alle Datenobjekte als Array zurück
                return runs;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<Testrun>();
    }

}
