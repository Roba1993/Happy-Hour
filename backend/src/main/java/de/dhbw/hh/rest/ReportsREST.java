package de.dhbw.hh.rest;

import static spark.Spark.get;
import static spark.Spark.post;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import de.dhbw.hh.dao.DAOFactory;
import de.dhbw.hh.models.BarReport;
import de.dhbw.hh.models.RESTResponse;

/**
 * Diese Klasse stellt die Methoden für die REST Schnittstelle
 * zum hinzufügen und anschauen von reporteten Bars bereit 
 * @author Jonas
 *
 */
public class ReportsREST {

	static final Logger LOG = LoggerFactory.getLogger(ReportsREST.class);

	private Gson gson = new Gson();

	public ReportsREST(DAOFactory daoFactory) {
		
		/**
		 * Gebe alle gemeldeten Bars zurück
		 */
		get("/bars/reports", "application/json", (request, response) -> {
			LOG.debug("HTTP-GET Anfrage eingetroffen: " + request.queryString());

			// Holt alle reporteten Bars aus der Datenbank zurück
			Collection<BarReport> data = daoFactory.getBarReportDAO().findAllBarReports();

			// Das Rückgabeobjekt wird erstellt
			RESTResponse restResponse = new RESTResponse();

			// Das Rückgabeobjekt wird befüllt
			restResponse.setName(request.queryString());
			restResponse.setTimestamp(new Timestamp(Calendar.getInstance().getTime().getTime()));
			
			if (data.isEmpty()) {
				// Es wurden keine reporteten Bars gefunden
				restResponse.setDescription("Keine reporteten Bars gefunden");
				restResponse.setError();
				restResponse.setData(null);
			} else {
				// Es wurden reportete Bars gefunden
				restResponse.setDescription("Folgende reporteten Bars wurden gefunden");
				restResponse.setSuccess();
				// Alle reporteten Bars werden zurück gegeben
				restResponse.setData(data);
			}
			
			// Übergibt das REST Objekt als Json String zur Anfrage zurück
			return gson.toJson(restResponse);
		});
		
		/**
		 * Trage neuen BarReport in DB ein
		 */
		post("/bars/:barID/reports", "application/json", (request, response) -> {
			LOG.debug("HTTP-POST Anfrage eingetroffen: " + request.queryString());

			// Schreibe Anfrageparameter in neues BarReport Objekt
			BarReport barReport = new BarReport();
			barReport.setBarID(request.params("barID"));
			barReport.setDescription(request.params("description"));

			// Schreibe neuen BarReport in DB
			boolean successfull = daoFactory.getBarReportDAO().insertBarReport(barReport);

			// Das Rückgabeobjekt wird erstellt
			RESTResponse restResponse = new RESTResponse();

			// Das Rückgabeobjekt wird befüllt
			restResponse.setName(request.queryString());
			restResponse.setTimestamp(new Timestamp(Calendar.getInstance().getTime().getTime()));
			if (successfull) {
				restResponse.setDescription("BarReport erfolgreich in DB geschrieben");
				restResponse.setSuccess();
			} else {
				restResponse.setDescription("Es gab einen Fehler beim Schreiben des BarRepors in die DB");
				restResponse.setError();
			}
			restResponse.setData(null);

			return gson.toJson(restResponse);
		});

	}
	
}
