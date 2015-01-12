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
		// Erstellt ein User Objekt
		User u = new User("Max Mustermann");
		
		// Testet, ob der eingegebene Wert übereinstimmt
		assertEquals("Max Mustermann", u.getName());
		
		// Testet die korrekte Ausgabe der Daten der Funktion toString()
		assertEquals("Name: Max Mustermann", u.toString());
	}

}
