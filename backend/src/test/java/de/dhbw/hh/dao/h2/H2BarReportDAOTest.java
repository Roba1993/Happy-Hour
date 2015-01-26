package de.dhbw.hh.dao.h2;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import de.dhbw.hh.models.BarReport;
import org.h2.tools.RunScript;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

/**
 * Diese Klasse testet alle Methoden der h2BarReportDAO-Klasse
 *
 * @author Jonas
 */
public class H2BarReportDAOTest {

    // Der Connectionpool für die Tests
    private static ComboPooledDataSource cpds;

    // Die zu testende Klasse
    private static H2BarReportDAO h2BarReportDAO;

    // BarReport Klassen zum Wiederverwenden
    private static BarReport barReport1;
    private static BarReport barReport2;
    private static BarReport barReport3;

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
        cpds.setJdbcUrl("jdbc:h2:mem:UTBarReportDAO");
        cpds.setUser("root");
        cpds.setPassword("toor");
        cpds.setMaxStatements(10);
        cpds.setMaxPoolSize(1);
        cpds.setAutoCommitOnClose(true);

        // Erstellt die Datenbank
        try (Connection connection = cpds.getConnection()) {
            RunScript.execute(connection, new FileReader("db-h2-create.sql"));
        }

        // Erzeugt ein Objekt der zu Testenden Klasse
        h2BarReportDAO = new H2BarReportDAO(cpds);
    }

    /**
     * Diese Funktion wird vor jedem Testrun aufgerufen und setzt die
     * vorherigen Änderungen an der Datenbank zurück.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        // Befüllt die BarReport Objekt zum Wiederverwenden
        barReport1 = new BarReport();
        barReport1.setID(1);
        barReport1.setBarID("uuaz972sdkinasd");
        barReport1.setDescription("Die Bar ist mir zu langweilig");

        barReport2 = new BarReport();
        barReport2.setID(2);
        barReport2.setBarID("uuaz972sdkinasd");
        barReport2.setDescription("Die Bar gibt es nicht mehr");

        barReport3 = new BarReport();
        barReport3.setID(3);
        barReport3.setBarID("98a8sdlilsaic8nj");
        barReport3.setDescription("Alles korrekt");

        // Öffnet die Verbindung zur Datenbank
        try (Connection connection = cpds.getConnection()) {

            // Löscht alle Einträge
            try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM barReport")) {
                preparedStatement.execute();
            }

            // Fügt das BarReport Objekt in die Datenbank ein
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO barReport (id, barID, description) " +
                            "VALUES (?,?,?), (?,?,?), (?,?,?)")) {
                preparedStatement.setInt(1, barReport1.getID());
                preparedStatement.setString(2, barReport1.getBarID());
                preparedStatement.setString(3, barReport1.getDescription());

                preparedStatement.setInt(4, barReport2.getID());
                preparedStatement.setString(5, barReport2.getBarID());
                preparedStatement.setString(6, barReport2.getDescription());

                preparedStatement.setInt(7, barReport3.getID());
                preparedStatement.setString(8, barReport3.getBarID());
                preparedStatement.setString(9, barReport3.getDescription());
                preparedStatement.execute();
            }
        }
    }

    /**
     * Teste, ob BarReport in Datenbank eingefügt werden kann
     *
     * @throws Exception
     */
    @Test
    public void testInsertBarReport() throws Exception {
        // Erzeuge neuen BarReport
        BarReport barReport = new BarReport();
        barReport.setID(4);
        barReport.setBarID("kasd32mma87h9n");
        barReport.setDescription("Der Bar hat den Besitzer gewechselt");

        // Führe zu testende Methode zum Einfügen eines BarReports aus
        h2BarReportDAO.insertBarReport(barReport);

        // Frage den eingefügten BarReport aus Datenbank ab
        ResultSet resultSet;
        try (Connection connection = cpds.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM barReport WHERE id=?")) {
                preparedStatement.setInt(1, barReport.getID());
                resultSet = preparedStatement.executeQuery();

                // Prüfe ob genau ein BarReport ausgelesen wurde
                resultSet.last();
                assertEquals(1, resultSet.getRow());

                // Prüfe ob die einzelnen Variablen des BarReports richtig gespeichert wurden
                assertEquals(barReport.getID(), resultSet.getInt("id"));
                assertEquals(barReport.getBarID(), resultSet.getString("barID"));
                assertEquals(barReport.getDescription(), resultSet.getString("description"));
            }
        }
    }

    /**
     * Teste, ob alle BarReports zu einer zugehörigen Bar aus der Datenbank gelöscht wird
     *
     * @throws Exception
     */
    @Test
    public void testDeleteBarReport() throws Exception {
        // Führe zu testende Methode zum Löschen eines BarReports aus
        h2BarReportDAO.deleteBarReport(barReport1.getBarID());

        // Frage alle Einträge aus Datenbank ab
        ResultSet resultSet;
        try (Connection connection = cpds.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM barReport")) {
                resultSet = preparedStatement.executeQuery();

                // Prüfe ob ein BarReport in Datenbank gespeichert ist
                resultSet.last();
                assertEquals(1, resultSet.getRow());
            }
        }
    }

    /**
     * Teste, ob ein BarReport anhand seiner ID aus der Datenbank gelöscht wird
     *
     * @throws Exception
     */
    @Test
    public void testDeleteSpecificBarReport() throws Exception {
        // Führe zu testende Methode zum Löschen eines BarReports aus
        h2BarReportDAO.deleteSpecificBarReport(barReport2.getID());

        // Frage alle Einträge aus Datenbank ab
        ResultSet resultSet;
        try (Connection connection = cpds.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM barReport")) {
                resultSet = preparedStatement.executeQuery();

                // Prüfe ob zwei BarReports in Datenbank gespeichert sind
                resultSet.last();
                assertEquals(2, resultSet.getRow());
            }
        }
    }

    /**
     * Teste ob alle BarReports zu einer zugehörigen BarID gefunden werden
     *
     * @throws Exception
     */
    @Test
    public void testFindReport() {
        // Führe zu testende Methode zum Suchen der BarReports aus
        Collection<BarReport> barReports = h2BarReportDAO.findBarReport(barReport1.getBarID());

        // Prüft ob zwei BarReports gefunden wurden
        assertEquals(2, barReports.size());
    }

    /**
     * Test, ob alle BarReports gefunden werden können
     */
    @Test
    public void testFindAllReports() {
        // Führe zu testende Methode zum Suchen aller BarReports aus
        Collection<BarReport> barReports = h2BarReportDAO.findAllBarReports();

        // Prüfe ob drei BarReports gefunden wurden
        assertEquals(3, barReports.size());
    }

    /**
     * Diese Methode gibt auf der Konsole die aktuelle Tabelle aus.
     * Zum schreiben der Teste und der Fehlersuche kann diese Funktion sehr
     * hilfrei sein.
     *
     * @throws Exception
     */
    @SuppressWarnings("unused")
    private void showRows() throws Exception {
        try (Connection connection = cpds.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM barReport")) {
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
