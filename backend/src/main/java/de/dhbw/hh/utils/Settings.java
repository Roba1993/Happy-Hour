package de.dhbw.hh.utils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Diese Klasse stellt alle Funktionen zur verwaltung der Eigenschaften bereit.
 */
public class Settings extends Properties {

    /**
     * Erstelle ein Settings Objekt mit dein Default Einstellungen.
     */
    public Settings() {
        loadDefault();
    }

    /**
     * Erstelle ein Settings Objekt mit den Default Einstellungen.
     * Wenn die angegebene Datei gefunden wird, überschreibe die
     * Default Einstellungen mit denen aus der Datei.
     *
     * @param filename Datei mit den Einstellungen.
     */
    public Settings(String filename) {
        loadDefault();
        load(filename);
    }

    /**
     * Überschreibe die aktuellen Einstellungen mit Standart Werten.
     */
    public void loadDefault() {
        clear();

        // log settings
        setProperty("log.level", "Info");

        // choose the database
        setProperty("db.type", "h2");

        // h2 database settings
        setProperty("db.h2.url", "jdbc:h2:mem:test");
        setProperty("db.h2.user", "root");
        setProperty("db.h2.password", "toor");
        setProperty("db.h2.maxStatements", "200");
        setProperty("db.h2.maxPoolSize", "10");
        setProperty("db.h2.startFile", "db-h2-create.sql");
        setProperty("db.h2.dataFile", "db-h2-data.sql");

        // server settings
        setProperty("server.port", "8080");
        setProperty("server.web", "../frontend/app");

    }

    /**
     * Lade die Eigenschaften vom gegebenen File
     *
     * @param filename Der Name und Pfad der Eigenschaften Datei.
     */
    public void load(String filename) {
        // Wenn keine Datei angegeben wurde gebe die Defaultwerte zurück
        if (filename == null) {
            return;
        }

        // Versuche das Eigenschten File zu lesen und die Eigenschaften aufzunehmen
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(filename))) {
            load(bis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gebe eine Eigenschaft für einen Key zurück. Es existieren keine Case-Sensitve
     * Keys. Somit spielt die Groß- und Kleinschreibung bei den Keys keine Rolle.
     *
     * @param key
     * @return
     */
    @Override
    public String getProperty(String key) {
        return super.getProperty(key);
    }
}
