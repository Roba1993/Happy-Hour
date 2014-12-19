package de.dhbw.hh.rest;

import static spark.Spark.post;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.security.*;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.google.gson.Gson;


import de.dhbw.hh.dao.DAOFactory;
import de.dhbw.hh.dao.h2.H2RouteDAO;
import de.dhbw.hh.models.BarReport;
import de.dhbw.hh.models.RESTResponse;
import de.dhbw.hh.models.Route;

/**
 * Diese Klasse stellt eine POST-Methode zur Verfügung, mit der eine Route in
 * die Datenbank eingepflegt werden kann
 * 
 * @author Michael
 */
public class RoutesREST {

	static final Logger LOG = LoggerFactory.getLogger(RoutesREST.class);

	private Gson gson = new Gson();

	public RoutesREST(DAOFactory daoFactory) {
		post("/routes",
				"application/json",
				(request, response) -> {

					LOG.debug("HTTP-POST Anfrage eingetroffen: " + request.queryString());

					Route route = new Route();

					String temp = request.queryParams("route");
					route.setData(temp);

					// Hashwert bilden

					String hash = getHashfromString(temp);

					route.setHash(hash);

					route.setTop(false);

					Collection<Object> data = new ArrayList<Object>();
					data.add(hash);

					RESTResponse restResponse = new RESTResponse();
					restResponse.setName("/routes");
					
					restResponse.setTimestamp(new Timestamp(Calendar
							.getInstance().getTime().getTime()));
					restResponse.setData(data);
					
					
					boolean successfull = daoFactory.getRouteDAO().insertRoute(route);
					
					if(successfull){
						restResponse.setDescription("Route erfolgreich hinzugefügt");
						restResponse.setSuccess();
					}else{
						restResponse.setDescription("Fehler beim Hinzufügen der Route");
						restResponse.setError();
					}
					
					restResponse.setData(null);

					return gson.toJson(restResponse);

				});

	}

	public String getHashfromString(String s) {
		StringBuffer sb = null;

		try {
			String original = s;
			MessageDigest md;

			md = MessageDigest.getInstance("MD5");

			md.update(original.getBytes());
			byte[] digest = md.digest();
			sb = new StringBuffer();
			for (byte b : digest) {
				sb.append(String.format("%02x", b & 0xff));
			}

			// System.out.println("original:" + original);
			// System.out.println("digested(hex):" + sb.toString());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();
	}

}
