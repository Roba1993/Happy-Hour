package de.dhbw.hh.dao.h2;

import static org.junit.Assert.assertEquals;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.Iterator;

import org.h2.tools.RunScript;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import de.dhbw.hh.models.BarReport;

/**
 * Diese Klasse testet die Anfrage der gemeldeten Bars
 * @author Jonas
 *
 */

public class H2BarReportDAOTest {
	
	// Der Connectionpool für die Tests
    private static ComboPooledDataSource cpds;

    // Die zu testende Klasse
    private static H2BarReportDAO h2BarReportDAO;

    // BarReport Klasse zum Wiederverwenden
    private static BarReport barReport;

    
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
		// Befüllt das Route Objekt zum Wiederverwenden 
		barReport = new BarReport();
		barReport.setBarID("uuaz972sdkinasd");
		barReport.setDescription("Die Bar ist mir zu langweilig");
		
		
		// Löscht alle Einträge
		try (Connection connection = cpds.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM barReport")) {
                preparedStatement.execute();
            }
		
		 // Fügt das Route Objekt in die Datenbank ein
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO barReport (barID, description) " +
                        "VALUES (?,?)")) {
            preparedStatement.setString(1, barReport.getBarID());
            preparedStatement.setString(2, barReport.getDescription());
            preparedStatement.execute();
        }
		}
	}
	
	/**
	 * Test, ob die BarReports gefunden werden können
	 */

	@Test
	public void testFindAllRoutes() throws Exception {
		// Zweiten und Dritten Datensatz werden erstellt
		BarReport barReport2 = new BarReport();
		barReport2.setBarID("uuaz972sdkinasd");
		barReport2.setDescription("Die Bar gibt es nicht mehr");
		
		BarReport barReport3 = new BarReport();
		barReport3.setBarID("98a8sdlilsaic8nj");
		barReport3.setDescription("Alles korrekt");
		
		// Fügt den zweiten und dritten Datensatz in die Datenbank ein
        try (Connection connection = cpds.getConnection()) {
            
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO barReport (barID, description) " +
                            "VALUES (?,?), (?,?)")) {
                preparedStatement.setString(1, barReport2.getBarID());
                preparedStatement.setString(2, barReport2.getDescription());

                preparedStatement.setString(3, barReport3.getBarID());
                preparedStatement.setString(4, barReport3.getDescription());
                preparedStatement.execute();
                
            }
		
        }
		
        // Alle gefundenen BarReports werden in eine Collection geschrieben
        Collection<BarReport> barReports = h2BarReportDAO.findAllBarReports();
        Iterator<BarReport> iterator = barReports.iterator();
        assertEquals(3, barReports.size());
        
        
       
	}
	
	@Test
	public void testFindRoute() throws Exception {
		
		BarReport barReport = new BarReport();
		barReport.setBarID("liasdoinas09kas");
		barReport.setDescription("Dies ist der vierte Report");
		
		// Fügt den vierten Datensatz in die Datenbank ein
        try (Connection connection = cpds.getConnection()) {
            
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO barReport (barID, description) " +
                            "VALUES (?,?)")) {

                preparedStatement.setString(1, barReport.getBarID());
                preparedStatement.setString(2, barReport.getDescription());
                preparedStatement.execute();
            }
		
        }
        
		Collection<BarReport> barReports =  h2BarReportDAO.findBarReport("liasdoinas09kas");
        Iterator<BarReport> iteratorNew = barReports.iterator();
        
        assertEquals(1, barReports.size());
        
        if (iteratorNew.hasNext()) {
            BarReport neuBarReport = iteratorNew.next();
            assertEquals(barReport.getBarID(), neuBarReport.getBarID());
            assertEquals(barReport.getDescription(), neuBarReport.getDescription());
        }
        
	}

	 /**
     * Diese Methode gibt auf der Konsole die aktuelle Tabelle aus.
     * Zum schreiben der Teste und der Fehlersuche kann diese Funktion sehr
     * hilfrei sein.
     *
     * @throws Exception
     */
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
