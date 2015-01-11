package de.dhbw.hh.dao.h2;

import static org.junit.Assert.*;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.util.Collection;

import org.h2.tools.RunScript;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import de.dhbw.hh.models.HappyHour;

/**
 * Diese Klasse testet alle Methoden der h2HappyHourDAO-Klasse
 *
 * @author Jonas
 */
public class H2HappyHourDAOTest {

	// Der Connectionpool für die Tests
    private static ComboPooledDataSource cpds;

    // Die zu testende Klasse
    private static H2HappyHourDAO h2HappyHourDAO;

    // HappyHour Klassen zum Wiederverwenden
    private static HappyHour happyHour1;
    private static HappyHour happyHour2;
    private static HappyHour happyHour3;
    
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
        cpds.setJdbcUrl("jdbc:h2:mem:UTHappyHourDAO");
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
        h2HappyHourDAO = new H2HappyHourDAO(cpds);
	}
	
	 /**
     * Diese Funktion wird vor jedem Testrun aufgerufen und setzt die
     * vorherigen Änderungen an der Datenbank zurück.
	  *
	 * @throws Exception
     */
	@Before
	public void setUp() throws Exception {
		// Befüllt die HappyHour Objekte zum Wiederverwenden 
		happyHour1 = new HappyHour();
		happyHour1.setID(1);
		happyHour1.setBarID("98ujanbsdmb23");
		happyHour1.setDescription("Alle Longdrinks für 5€");
		happyHour1.setStart(new Time(1239));
		happyHour1.setEnd(new Time(1299));
		happyHour1.setMonday(true);
		happyHour1.setTuesday(true);
		happyHour1.setWednesday(false);
		happyHour1.setThursday(false);
		happyHour1.setFriday(true);
		happyHour1.setSaturday(true);
		happyHour1.setSunday(false);
		
		happyHour2 = new HappyHour();
		happyHour2.setID(2);
		happyHour2.setBarID("98ujanbsdmb23");
		happyHour2.setDescription("Alle Shots für 2€");
		happyHour2.setStart(new Time(1239));
		happyHour2.setEnd(new Time(1299));
		happyHour2.setMonday(true);
		happyHour2.setTuesday(true);
		happyHour2.setWednesday(false);
		happyHour2.setThursday(false);
		happyHour2.setFriday(true);
		happyHour2.setSaturday(true);
		happyHour2.setSunday(false);
		
		happyHour3 = new HappyHour();
		happyHour3.setID(3);
		happyHour3.setBarID("ja8s8nas83mm");
		happyHour3.setDescription("Alle Longdrinks für 50€");
		happyHour3.setStart(new Time(1239));
		happyHour3.setEnd(new Time(1299));
		happyHour3.setMonday(false);
		happyHour3.setTuesday(false);
		happyHour3.setWednesday(false);
		happyHour3.setThursday(false);
		happyHour3.setFriday(true);
		happyHour3.setSaturday(true);
		happyHour3.setSunday(true);
		
		// Öffnet die Verbindung zur Datenbank
		try (Connection connection = cpds.getConnection()) {
			
			// Löscht alle Einträge
            try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM happyHour")) {
                preparedStatement.execute();
            }
		
            // Fügt das BarReport Objekt in die Datenbank ein
		    try (PreparedStatement preparedStatement = connection.prepareStatement(
		    		"INSERT INTO happyHour "
							+ "(id, barID, description, start, end, monday, tuesday, wednesday, thursday, friday, saturday, sunday)"
							+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?), (?,?,?,?,?,?,?,?,?,?,?,?), (?,?,?,?,?,?,?,?,?,?,?,?)")) {
		    	preparedStatement.setInt(1, happyHour1.getID());
		    	preparedStatement.setString(2, happyHour1.getBarID());
		    	preparedStatement.setString(3, happyHour1.getDescription());
		    	preparedStatement.setTime(4, happyHour1.getStart());
		    	preparedStatement.setTime(5, happyHour1.getEnd());
		    	preparedStatement.setBoolean(6, happyHour1.isMonday());
		    	preparedStatement.setBoolean(7, happyHour1.isTuesday());
		    	preparedStatement.setBoolean(8, happyHour1.isWednesday());
		    	preparedStatement.setBoolean(9, happyHour1.isThursday());
		    	preparedStatement.setBoolean(10, happyHour1.isFriday());
		    	preparedStatement.setBoolean(11, happyHour1.isSaturday());
		    	preparedStatement.setBoolean(12, happyHour1.isSunday());
		    	
		    	preparedStatement.setInt(13, happyHour2.getID());
		    	preparedStatement.setString(14, happyHour2.getBarID());
		    	preparedStatement.setString(15, happyHour2.getDescription());
		    	preparedStatement.setTime(16, happyHour2.getStart());
		    	preparedStatement.setTime(17, happyHour2.getEnd());
		    	preparedStatement.setBoolean(18, happyHour2.isMonday());
		    	preparedStatement.setBoolean(19, happyHour2.isTuesday());
		    	preparedStatement.setBoolean(20, happyHour2.isWednesday());
		    	preparedStatement.setBoolean(21, happyHour2.isThursday());
		    	preparedStatement.setBoolean(22, happyHour2.isFriday());
		    	preparedStatement.setBoolean(23, happyHour2.isSaturday());
		    	preparedStatement.setBoolean(24, happyHour2.isSunday());
		    	
		    	preparedStatement.setInt(25, happyHour3.getID());
		    	preparedStatement.setString(26, happyHour3.getBarID());
		    	preparedStatement.setString(27, happyHour3.getDescription());
		    	preparedStatement.setTime(28, happyHour3.getStart());
		    	preparedStatement.setTime(29, happyHour3.getEnd());
		    	preparedStatement.setBoolean(30, happyHour3.isMonday());
		    	preparedStatement.setBoolean(31, happyHour3.isTuesday());
		    	preparedStatement.setBoolean(32, happyHour3.isWednesday());
		    	preparedStatement.setBoolean(33, happyHour3.isThursday());
		    	preparedStatement.setBoolean(34, happyHour3.isFriday());
		    	preparedStatement.setBoolean(35, happyHour3.isSaturday());
		    	preparedStatement.setBoolean(36, happyHour3.isSunday());
		        
		        preparedStatement.execute();
		    }
		}
	}
	
	/**
	 * Teste, ob eine HappyHours anhand seiner ID aus der Datenbank gelöscht wird
	 *
	 * @throws Exception 
	 */
	@Test
	public void testDeleteHappyHour() throws Exception {
		// Führe zu testende Methode zum Löschen einer HappyHour aus
		h2HappyHourDAO.deleteHappyHour(happyHour2.getID());
		
		// Frage alle Einträge aus Datenbank ab
		ResultSet resultSet;
		try (Connection connection = cpds.getConnection()) {
			try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM happyHour")) {
				resultSet = preparedStatement.executeQuery();
				
				// Prüfe ob zwei HappyHours in Datenbank gespeichert sind
				resultSet.last();
				assertEquals(2, resultSet.getRow());
            }
        }
	}
	
	
	/**
	 * Teste, ob HappyHour in Datenbank eingefügt werden kann
	 *
	 * @throws Exception
	 *
	 * @author Marcus
	 */
	@Test
	public void testInsertHappyHour() throws Exception {
		// Erzeuge neue HappyHour
		HappyHour happyHour = new HappyHour();
		happyHour.setID(4);
		happyHour.setBarID("kasd32mma87h9n");
		happyHour.setDescription("Geile Happy-Hour mit Tour-de-sauf");
		happyHour.setStart(new Time(1900));;
		happyHour.setEnd(new Time(2100));
		happyHour.setMonday(false);
		happyHour.setTuesday(false);
		happyHour.setWednesday(true);
		happyHour.setThursday(true);
		happyHour.setFriday(false);
		happyHour.setSaturday(true);
		happyHour.setSunday(false);
		
		// Führe zu testende Methode zum Einfügen einer Happy Hour aus
		h2HappyHourDAO.insertHappyHour(happyHour);
		
		// Frage die eingefügte HappyHour aus der Datenbank ab
		ResultSet resultSet;
		try (Connection connection = cpds.getConnection()) {
			try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM happyHour WHERE id=?")) {
				preparedStatement.setInt(1, happyHour.getID());
				resultSet = preparedStatement.executeQuery();
				
				// Prüfe ob genau eine HappyHour ausgelesen wurde
				resultSet.last();
				assertEquals(1, resultSet.getRow());
				
				// Prüfe ob die einzelnen Variablen der HappyHour richtig gespeichert wurden
				assertEquals(happyHour.getID(), resultSet.getInt("id"));
				assertEquals(happyHour.getBarID(), resultSet.getString("barID"));
				assertEquals(happyHour.getDescription(), resultSet.getString("description"));
				assertEquals(happyHour.getStart(), resultSet.getTime("start"));
				assertEquals(happyHour.getEnd(), resultSet.getTime("end"));
				assertEquals(happyHour.isMonday(), resultSet.getBoolean("monday"));
				assertEquals(happyHour.isTuesday(), resultSet.getBoolean("tuesday"));
				assertEquals(happyHour.isWednesday(), resultSet.getBoolean("wednesday"));
				assertEquals(happyHour.isThursday(), resultSet.getBoolean("thursday"));
				assertEquals(happyHour.isFriday(), resultSet.getBoolean("friday"));
				assertEquals(happyHour.isSaturday(), resultSet.getBoolean("saturday"));
				assertEquals(happyHour.isSunday(), resultSet.getBoolean("sunday"));
				
            }
        }
	}

	/**
	 * Teste ob alle HappyHour zu einer zugehörigen BarID gefunden werden
	 *
	 * @throws Exception
	 *
	 * @author Marcus
	 */
	@Test
	public void testFindHappyHour() {
        // Führe zu testende Methode zum Suchen der BarReports aus
		Collection<HappyHour> happyHours =  h2HappyHourDAO.findHappyHour(happyHour1.getBarID());
        
        // Prüft ob zwei HappyHours gefunden wurden
        assertEquals(2, happyHours.size());        
	}
	
}
