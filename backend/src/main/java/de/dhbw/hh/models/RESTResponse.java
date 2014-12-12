package de.dhbw.hh.models;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Standardantwortklasse für REST Anfragen.
 * @author Jonas, Tobias
 *
 */

public class RESTResponse {

	private String name = "";
	private String description = "";
	private Timestamp timestamp = null;
	private String status = "";
	private Object[] data = null;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public String getStatus() {
		return status;
	}

	public void setSuccess() {
		this.status = "success";
	}
	
	public void setError() {
		this.status = "error";
	}

	public Object[] getData() {
		return data;
	}

	public void setData(Object[] data) {
		this.data = data;
	}

}
