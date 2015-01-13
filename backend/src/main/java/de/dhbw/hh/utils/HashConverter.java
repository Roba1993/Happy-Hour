package de.dhbw.hh.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Diese Klasse konvertiert einen übergebenen String in einen MD5 Hash
 * @author Maren
 *
 */
public class HashConverter {
	// Initialisiert einen Logger für die Fehlerausgabe
    static final Logger LOG = LoggerFactory.getLogger(HashConverter.class);

    /**
	 * Diese Methode wandelt einen String, der übergeben wird, in einen MD5 Hash um
	 * @param input
	 * @return hash
	 */
	public static String md5(String input) {
		// Erzeugt einen hash-String, in den der Hash für die Rückgabe eingefügt wird
		String hash = null;
	        
		// Wenn der übergebene Wert null ist, wird auch null zurückgegeben
		if(null == input) return null;
	         
		try {  
			// Erstellt ein Message Digest Objekt für MD5
			MessageDigest digest = MessageDigest.getInstance("MD5");
	         
			// Updatet den Input-String in das Message Digest Objekt
			digest.update(input.getBytes(), 0, input.length());
	 
			// Konvertiert Message Digest Wert in einen Basis 16 (hex) Hash
			hash = new BigInteger(1, digest.digest()).toString(16);
	 
		}catch (NoSuchAlgorithmException e) {
			LOG.error(e.getMessage());
			}
		// Rückgabe des MD5 Hash-Werts
		return hash;
		}
	}

