package de.dhbw.hh.models;

import java.sql.Timestamp;

/**
 * Dies ist die Testrun-Datenklasse.
 */
public class Testrun {

    private long id;

    private String name;

    private long rounds;

    private Timestamp date;

    // Folgend alle Getter & Setter Methoden

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getRounds() {
        return rounds;
    }

    public void setRounds(long rounds) {
        this.rounds = rounds;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }
}
