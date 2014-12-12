package de.dhbw.hh.rest;

import com.google.gson.Gson;
import de.dhbw.hh.dao.DAOFactory;
import de.dhbw.hh.models.Testrun;

import java.sql.Timestamp;
import java.util.Date;

import static spark.Spark.*;

/**
 * Diese Klasse stellt die Methoden für die REST Schnittstelle
 * für den Pfad /testrun bereit
 */
public class TestrunREST {

    // Gson Objekt zum umwandeln in json
    private Gson gson = new Gson();

    public TestrunREST(DAOFactory daoFactory) {

        /**
         * Gibt alle Testrunden des angegbenen Namen zurück.
         */
        get("/testrun/:name", "application/json", (request, response) -> {
            Testrun[] runs = daoFactory.getTestrunDAO().findTestrunsByName(request.params(":name"));

            return gson.toJson(runs[0]);
        });

        /**
         * Erstellt eine neue Testrunde
         */
        put("testrun", "application/json", (request, response) -> {
            // Hole die Daten aus den Parametern
            String name = request.params("name");
            int rounds = Integer.valueOf(request.params("rounds"));

            // Erstelle ein neues Datenobjekt
            Testrun testrun = new Testrun();
            testrun.setName(name);
            testrun.setRounds(rounds);
            testrun.setDate(new Timestamp(new Date().getTime()));

            // Schreibe die Daten in die Datenbank
            daoFactory.getTestrunDAO().insertTestrun(testrun);

            // Return a result
            return new Object(){String result="succesfull";};
        });
    }
}
