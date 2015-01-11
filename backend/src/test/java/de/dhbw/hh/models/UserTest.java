package de.dhbw.hh.models;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Dies Klasse testet die Datenklasse User
 *
 * @author Maren
 */
public class UserTest {

	@Test
	public void test() {
		User u = new User("Max Mustermann");
		
		 assertEquals("Max Mustermann", u.getName());
	}

}
