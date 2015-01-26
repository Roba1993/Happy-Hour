package de.dhbw.hh.rest;

import com.google.gson.Gson;
import de.dhbw.hh.dao.DAOFactory;
import de.dhbw.hh.models.Bar;
import de.dhbw.hh.models.RESTResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

import static spark.Spark.get;

/**
 * Diese Klasse stellt die Methoden für die REST Schnittstelle für
 * eine GET Anfrage auf den Pfad /bars bereit.
 *
 * @author Tobias Häußermann
 * @author2 Robert Schütte
 */
public class BarsREST {

    static final Logger LOG = LoggerFactory.getLogger(ReportsREST.class);

    private Gson gson = new Gson();

    public BarsREST(DAOFactory daoFactory) {

        /**
         * Gibt alle Bars zurück, die zu den Suchoptionen passen.
         * Die folgenden Parameter werden zwingend benötigt. Bei
         * Fehlen eines Parameters werden keine Bars zurückgegeben.
         * X-----------------X----------------------------------X
         * |    longitude    |    GPS-Koordinate als double     |
         * |    latitude     |    GPS-Koordinate als double     |
         * |    radius       |    Radius als integer            |
         * |    weekday      |    Wochentag als Zahl von 1-7    |
         * X-----------------X----------------------------------X
         */
        get("/bars", "application/json", (request, response) -> {
            // Erstellt Log-Eintrag bei Zugriff auf den Pfad /bars
            LOG.debug("HTTP-GET Anfrage eingetroffen: " + request.queryString());

            // umformen der übergeben Daten
            float lng = Float.parseFloat(request.queryParams("longitude"));
            float lat = Float.parseFloat(request.queryParams("latitude"));
            float rad = Float.parseFloat(request.queryParams("radius"));
            int weekday = Integer.parseInt(request.queryParams("weekday"));

            // Hole die Barinformationen aus der Datenbank & von Foursquare
            ArrayList<Bar> bars = daoFactory.getFoursquareDAO().getBarsInArea(lng, lat, rad, weekday);

            // REST-Response erstellen
            RESTResponse restResponse = new RESTResponse();
            restResponse.setName(request.queryString());
            restResponse.setTimestamp(new Timestamp(Calendar.getInstance().getTime().getTime()));
            restResponse.setDescription("Alles ok. Noch ergänzen");
            restResponse.setSuccess();
            restResponse.setData(bars);

            // Log-Eintrag bei Rückgabe
            LOG.debug(restResponse.getStatus() + restResponse.getDescription());

            // In diesem Schritt wird die vorgefertigte REST-Response in das JSON-Format umgewandelt
            // und zurückgegeben.
            return gson.toJson(restResponse);
        });

    }
}
