package de.dhbw.hh.dao.h2;

import de.dhbw.hh.dao.DAOFactory;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.*;

/**
 * 
 * @author Robert
 *
 */
public class H2DAOFactoryTest {

    private static Properties properties;

    /**
     * Setzte die benötigten Eigenschaften
     *
     * @throws Exception
     */
    @BeforeClass
    public static void setUpClass() throws Exception {
        //choose the database
        properties = new Properties();
        properties.setProperty("db.type", "h2");

        //h2 database settings
        properties.setProperty("db.h2.url", "jdbc:h2:mem:test");
        properties.setProperty("db.h2.user", "root");
        properties.setProperty("db.h2.password", "toor");
        properties.setProperty("db.h2.maxStatements", "200");
        properties.setProperty("db.h2.maxPoolSize", "10");
    }

    /**
     * Teste ob ein Objekt erzeugt wird.
     *
     * @throws Exception
     */
    @Test
    public void testGetTestrunDAO() throws Exception {
        assertNotNull(H2DAOFactory.getDaoFactory(DAOFactory.H2, properties).getBarReportDAO());
    }
}