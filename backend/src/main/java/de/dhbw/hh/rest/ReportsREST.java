package de.dhbw.hh.rest;

import static spark.Spark.get;
import static spark.Spark.post;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import de.dhbw.hh.dao.DAOFactory;
import de.dhbw.hh.models.BarReport;
import de.dhbw.hh.models.RESTResponse;

public class ReportsREST {

	static final Logger LOG = LoggerFactory.getLogger(ReportsREST.class);

	private Gson gson = new Gson();

	public ReportsREST(DAOFactory daoFactory) {
		
		/**
		 * Gebe alle gemeldeten Bars zurück
		 */
		get("/bars/reports", "application/json", (request, response) -> {
			LOG.info("HTTP-GET Anfrage eingetroffen: " + request.queryString());

			// Finde alle gemeldeten Bars in DB
			Collection<Object> data = new ArrayList<Object>();
			data.addAll(daoFactory.getBarReportDAO().findBarReportsByReported(true));

			// Gebe RESTResponse mit gemeldeten Bars zurück
			RESTResponse restResponse = new RESTResponse();
			restResponse.setName(request.queryString());
			restResponse.setDescription("Dies sind die gemeldeten Bars");
			restResponse.setTimestamp(new Timestamp(Calendar.getInstance().getTime().getTime()));
			restResponse.setSuccess();
			restResponse.setData(data);

			return gson.toJson(restResponse);
		});
		
		/**
		 * Trage neuen BarReport in DB ein
		 */
		post("/bars/:barID/reports",
				"application/json",
				(request, response) -> {
					LOG.info("HTTP-POST Anfrage eingetroffen: " + request.queryString());

					// Schreibe Anfrageparameter in neues BarReport Objekt
					BarReport barReport = new BarReport();
					barReport.setBarID(request.params("barID"));
					barReport.setDescription(request.params("description"));
					barReport.setReported(true);

					// Prüfe ob BarReport bereits vorhanden und füge neuen BarReport hinzu oder überschreibe alten BarReport
					if (daoFactory.getBarReportDAO().findBarReport(barReport.getBarID()) == null) {
						LOG.info("Neuer BarReport wird geschrieben: " + barReport.toString());
						daoFactory.getBarReportDAO().insertBarReport(barReport);
					} else {
						LOG.info("Alter BarReport wird durch neuen überschrieben: " + barReport.toString());
						daoFactory.getBarReportDAO().updateBarReport(barReport);
					}

					// Gebe RESTResponse mit leeren Dateninhalt zurück
					RESTResponse restResponse = new RESTResponse();
					restResponse.setName(request.queryString());
					restResponse.setDescription("Antwort auf Meldung von Bar");
					restResponse.setTimestamp(new Timestamp(Calendar.getInstance().getTime().getTime()));
					restResponse.setSuccess();
					restResponse.setData(null);

					return gson.toJson(restResponse);
				});

	}
	
}
