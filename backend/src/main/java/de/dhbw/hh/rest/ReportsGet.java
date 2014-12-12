package de.dhbw.hh.rest;

import static spark.Spark.get;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import de.dhbw.hh.dao.DAOFactory;
import de.dhbw.hh.models.RESTResponse;

/**
 * Diese Klasse stellt die Methoden für eine HTTP-GET Anfrage auf den Pfad
 * "/bars/reports" bereit.
 * 
 * @author Jonas
 *
 */
public class ReportsGet {

	static final Logger LOG = LoggerFactory.getLogger(ReportsGet.class);

	private Gson gson = new Gson();

	public ReportsGet(DAOFactory daoFactory) {
		
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

	}

}
