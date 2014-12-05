package de.dhbw.hh.rest;

import static spark.Spark.get;

import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import de.dhbw.hh.dao.DAOFactory;
import de.dhbw.hh.models.RESTResponse;

/**
 * Diese Klasse stellt die Methoden für die REST Schnittstelle für eine GET
 * Anfrage auf den Pfad /bars/reports bereit.
 * 
 * @author Jonas
 */
public class ReportsGet {

	static final Logger LOG = LoggerFactory.getLogger(ReportsGet.class);

	private Gson gson = new Gson();

	public ReportsGet(DAOFactory daoFactory) {
		get("/bars/reports", "application/json", (request, response) -> {
			RESTResponse restResponse = new RESTResponse();
			restResponse.setName("/bars/reports");
			restResponse.setDescription("Dies ist die Beschreibung");
			restResponse.setTimestamp(new Timestamp(0));

			return gson.toJson(restResponse);
		});

	}

}
