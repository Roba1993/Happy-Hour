package de.dhbw.hh.dao.h2;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import de.dhbw.hh.models.Testrun;
import junit.framework.TestCase;
import org.h2.tools.RunScript;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.*;

public class H2TestrunDAOTest {

    // Der Connectionpool für die Tests
    private static ComboPooledDataSource cpds;

    // Die zu Testende Klasse
    private static H2TestrunDAO h2TestrunDAO;

    // Testrun Klassen zur wiederverwenden
    private static Testrun testrun;

    /**
     * Diese Funktion wird aller erstes aufgerufen und dient zur
     * Testkonfiguration.
     *
     * @throws Exception
     */
    @BeforeClass
    public static void setUpClass() throws Exception {
        // Wir erstellen eine temporäre Inmemory-Datenbank
        cpds = new ComboPooledDataSource();
        cpds.setDriverClass("org.h2.Driver");
        cpds.setJdbcUrl("jdbc:h2:mem:UTTestDAO");
        cpds.setUser("root");
        cpds.setPassword("toor");
        cpds.setMaxStatements(10);
        cpds.setMaxPoolSize(1);
        cpds.setAutoCommitOnClose(true);

        // Erstelle die Datenbank
        try (Connection connection = cpds.getConnection()) {
            RunScript.execute(connection, new FileReader("db-h2-create.sql"));
        }

        // Etzeuge ein Objekt der zu Testenden Klasse
        h2TestrunDAO = new H2TestrunDAO(cpds);
    }

    /**
     * Diese Funktion wird vor jedem Testrun aufgerufen und setzt die
     * vorherigen Änderungen an der Datenbank zurück.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        // Befülle das Testrun Objekt zum wiederverwenden
        testrun = new Testrun();
        testrun.setName("Max");
        testrun.setDate(new Timestamp(new Date().getTime()));
        testrun.setRounds(12);

        // Lösche alle Einträge aus der Tabelle testrun
        try (Connection connection = cpds.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM testrun")) {
                preparedStatement.execute();
            }

            // Füge das Testrun Objekt in die Datenbank ein
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO testrun (name, date, rounds) " +
                            "VALUES (?,?,?)")) {
                preparedStatement.setString(1, testrun.getName());
                preparedStatement.setTimestamp(2, testrun.getDate());
                preparedStatement.setLong(3, testrun.getRounds());
                preparedStatement.execute();
            }

            // Frage die ID des eben erstellten Datensatzes ab
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT id FROM testrun WHERE name=?")) {
                preparedStatement.setString(1, "Max");
                ResultSet resultSet = preparedStatement.executeQuery();

                // Setze die ID in unser Testobjekt
                if (resultSet.next()) {
                    testrun.setId(resultSet.getLong("id"));
                }
            }
        }
    }

    /**
     * Teste ob ein Objekt in die Datenbank eingefügt werden kann.
     *
     * @throws Exception
     */
    @Test
    public void testInsertTestrun() throws Exception {
        // Verändere das Testrun Objekt, da die Default Werte schon in der Datenbank sind
        testrun = new Testrun();
        testrun.setName("Olga");
        testrun.setDate(new Timestamp(1000));
        testrun.setRounds(222);

        // Versuche den Datensatz in die Datenbank zu schreiben
        assertTrue(h2TestrunDAO.insertTestrun(testrun));

        // Die Daten müssen in die Datenbank geschrieben worden sein
        try (Connection connection = cpds.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM testrun WHERE name=?")) {
                preparedStatement.setString(1, testrun.getName());
                ResultSet resultSet = preparedStatement.executeQuery();

                // Es müssend die Daten richtig vorhanden sein
                if (resultSet.next()) {
                    assertEquals(testrun.getDate(), resultSet.getTimestamp("date"));
                    assertEquals(testrun.getRounds(), resultSet.getLong("rounds"));
                } else {
                    assertFalse(true);
                }
            }
        }
    }

    /**
     * Teste ob ein Objekt in der Datenabnk aktualiesiert werden kann.
     *
     * @throws Exception
     */
    @Test
    public void testUpdateTestrun() throws Exception {
        // Verändere die Werte des Testrun Objektes
        testrun.setName("Olga");
        testrun.setDate(new Timestamp(200));
        testrun.setRounds(1);

        // Versuche das Testobjekt auf der Datenbank zu aktualiesieren
        assertTrue(h2TestrunDAO.updateTestrun(testrun));

        // Überprüfe ob der Datensatz aktualiesiert wurde
        try (Connection connection = cpds.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM testrun WHERE id=?")) {
                preparedStatement.setLong(1, testrun.getId());
                ResultSet resultSet = preparedStatement.executeQuery();

                // Überprüfe die Ergebnisse
                if (resultSet.next()) {
                    assertEquals(testrun.getName(), resultSet.getString("name"));
                    assertEquals(testrun.getDate(), resultSet.getTimestamp("date"));
                    assertEquals(testrun.getRounds(), resultSet.getLong("rounds"));
                } else {
                    assertFalse(true);
                }
            }
        }
    }

    /**
     * Teste ob ein Objekt von der Datenbank gelöscht werden kann.
     *
     * @throws Exception
     */
    @Test
    public void testDeleteTestrun() throws Exception {
        // Versuche den Datensatz zu löschen
        assertTrue(h2TestrunDAO.deleteTestrun(testrun.getId()));

        // Überprüfe ob der Datensatz gelöscht wurde
        try (Connection connection = cpds.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM testrun WHERE id=?")) {
                preparedStatement.setLong(1, testrun.getId());
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    assertFalse(true);
                } else {
                    assertFalse(false);
                }
            }
        }
    }

    /**
     * Teste ob ein Objekt nach seiner ID gefunden werden kann.
     *
     * @throws Exception
     */
    @Test
    public void testFindTestrun() throws Exception {
        // Versuche den Testrun zu finden
        Testrun tr = h2TestrunDAO.findTestrun(testrun.getId());

        // Überprüfe ob der gefundene Testrun mit dem gegebenen übereinstimmt
        assertEquals(testrun.getId(), tr.getId());
        assertEquals(testrun.getRounds(), tr.getRounds());
        assertEquals(testrun.getDate(), tr.getDate());
        assertEquals(testrun.getName(), tr.getName());
    }

    /**
     * Teste ob Objekte nach ihren Namen gefunden werden können.
     *
     * @throws Exception
     */
    @Test
    public void testFindTestrunsByName() throws Exception {
        // Erstelle einen zweiten Datensatz
        Testrun testrun2 = new Testrun();
        testrun2.setDate(new Timestamp(10000));
        testrun2.setRounds(10);
        testrun2.setName(testrun.getName());

        // Füge den zweiten Datensatz in die Datenbank ein
        try (Connection connection = cpds.getConnection()) {
            // Füge das Testrun Objekt in die Datenbank ein
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO testrun (name, date, rounds) " +
                            "VALUES (?,?,?)")) {
                preparedStatement.setString(1, testrun2.getName());
                preparedStatement.setTimestamp(2, testrun2.getDate());
                preparedStatement.setLong(3, testrun2.getRounds());
                preparedStatement.execute();
            }
        }

        // Versuche die beiden Datensaätze zu finden
        Testrun[] runs = h2TestrunDAO.findTestrunsByName(testrun.getName());

        // als Ergebniss erwaten wir 2 Datensätze
        assertEquals(2, runs.length);
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
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM testrun")) {
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