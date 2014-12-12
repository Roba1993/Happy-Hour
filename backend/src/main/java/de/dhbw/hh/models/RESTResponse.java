package de.dhbw.hh.models;

import java.sql.Timestamp;
import java.util.Collection;

/**
 * Datenklasse für REST Antwort auf HTTP Anfragen.
 * @author Jonas, Tobias
 *
 */

public class RESTResponse {

	private String name = "";
	private String description = "";
	private Timestamp timestamp = null;
	private String status = "";
<<<<<<< HEAD
	private Collection<Object> data = null;
=======
	private Object[] data = null;
>>>>>>> f436062f8b38ddeadf7a35c40912ec537f74fdfa

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

<<<<<<< HEAD
	public Collection<Object> getData() {
		return data;
	}

	public void setData(Collection<Object> data) {
=======
	public Object[] getData() {
		return data;
	}

	public void setData(Object[] data) {
>>>>>>> f436062f8b38ddeadf7a35c40912ec537f74fdfa
		this.data = data;
	}

}
