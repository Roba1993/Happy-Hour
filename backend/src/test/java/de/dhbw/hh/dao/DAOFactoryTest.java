package de.dhbw.hh.dao;

import de.dhbw.hh.dao.h2.H2DAOFactory;
import de.dhbw.hh.utils.Settings;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test der DaoFactory Klasse
 *
 * @author Robert
 */
public class DAOFactoryTest {

    // Die benötigten Eigenschaften
    private static Settings properties;

    /**
     * Setzte die benötigten Eigenschaften
     *
     * @throws Exception
     */
    @BeforeClass
    public static void setUpClass() throws Exception {
        //choose the database
        properties = new Settings();
        properties.setProperty("db.type", "h2");

        //h2 database settings
        properties.setProperty("db.h2.url", "jdbc:h2:mem:test");
        properties.setProperty("db.h2.user", "root");
        properties.setProperty("db.h2.password", "toor");
        properties.setProperty("db.h2.maxStatements", "200");
        properties.setProperty("db.h2.maxPoolSize", "10");
    }

    /**
     * Teste ob eine factory erzeugt wird.
     *
     * @throws Exception
     */
    @Test
    public void testGetDaoFactory() throws Exception {
        assertNotNull(H2DAOFactory.getDaoFactory(DAOFactory.H2, properties));
    }
    
}