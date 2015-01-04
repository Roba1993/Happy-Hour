package de.dhbw.hh.utils;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.jasypt.util.text.BasicTextEncryptor;

/**
 * Diese Klasse verschlüsselt und entschlüsselt die Cookies
 * @author Robert
 *
 */
public class ProtectCookie {

	private static final BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
    private static BasicTextEncryptor textEncryptor;
    private static String privateKey;

    /**
     * Diese Funktion sichert eine Map von Cookies gegen Modifikation
     *
     * @param cookies Die ungesicherten Cookies
     * @return gesicherter Cookie
     */
    public Map<String, String> secureCookies(Map<String, String> cookies) {
        // ein private key wird erstellt
        checkPrivateKey();

        // Abfrage nach Cookies
        if (cookies == null || cookies.isEmpty()) {
            return Collections.emptyMap();
        }
        
        // Rückgabe der map
        HashMap<String, String> out = new HashMap<String, String>();

        // loop alle Cookies
        for (Map.Entry<String, String> entry : cookies.entrySet()) {
            // sichert jedes Cookie
            out.put(entry.getKey(), generateProtectedCookie(entry.getKey(), entry.getValue()));
        }

        // gibt alle Cookies zurück
        return out;
    }

    /**
     * Diese Funktion überprüft, ob die Cookies modifiziert sind oder nicht. Wenn ein cookie
     * modifiziert ist, wird sein Wert null gesetzt.
     *
     * @param cookies
     * @return
     */
    public Map<String, String> unsecureCookies(Map<String, String> cookies) {
        // erstellen eines private keys
        checkPrivateKey();

        // Return map
        HashMap<String, String> out = new HashMap<String, String>();

        // cookies in der map?
        if (cookies == null || cookies.isEmpty()) {
            return out;
        }

        // loop alle cookies
        for (Map.Entry<String, String> entry : cookies.entrySet()) {
            // entschlüsselt jeden cookie
            out.put(entry.getKey(), generateUnprotectedCookie(entry.getKey(), entry.getValue()));
        }

        // gibt alle cookies zurück
        return out;
    }

    /**
     * Diese Funktion verschlüsselt einen Cookie. Der Rückgabewert ist ein neuer verschlüsselter Wert.
     * Der gegebene Schlüssel ist ebenfalls verschlüsselt und kann hinterher nicht mehr geändert werden
     *      
     * @param key cookie key
     * @param value cookie value
     * @return secured cookie value
     */
    public String secureCookie(String key, String value) {
        // ein private key wird benötigt
        checkPrivateKey();
        return generateProtectedCookie(key, value);
    }

    /**
     * Diese Funktion entschlüsselt einen Cookie. Der Rückgabwert ist ein neuer entschlüsselter Wert.
     * Wenn eine Änderung im Schlüssel oder im Wert stattgefunden hat, ist der Rückgabewert null.
     *
     * @param key cookie key
     * @param value verschlüsselter cookie Wert
     * @return entschlüsselter  cookie Wert
     */
    public String unsecureCookie(String key, String value) {
        // ein private key wird benötigt
        checkPrivateKey();
        return generateUnprotectedCookie(key, value);
    }

    /**
     * Diese Funktion setzt den private key für diese Anwendung. Der privateKey
     * wird benutzt, um die Cookies zu verschlüsseln und sie gegen Modifikation zu schützen.
     *
     * @param privateKey
     */
    public void setPrivateKey(String privateKey) {
        ProtectCookie.privateKey = privateKey;
        textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPassword(privateKey);
    }

    // Private Funktionen
    /**
     * Diese Funktionen überprüfen, ob ein privateKey existiert. Wenn nicht wird ein neuer
     * random private key erstellt.
     */
    private void checkPrivateKey() {
        // generiert einen neuen random key, wenn keiner existiert
        if  (privateKey == null) {

            // available key symbols
            String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            int len = 32;

            // SecureRandom protectd against vulnerable to timing attacks
            SecureRandom rnd = new SecureRandom();

            // generiert einen neuen random key
            StringBuilder sb = new StringBuilder(len);
            for (int i = 0; i < len; i++) {
                sb.append(AB.charAt(rnd.nextInt(AB.length())));
            }

            // setzt den Schlüssel und erstellt einen textEncryptor
            setPrivateKey(sb.toString());
        }
    }

    /**
     * Diese Funktion sichert einen einzelnen cookie Wert.
     *
     * @param name
     * @param value
     * @return
     */
    private String generateProtectedCookie(String name, String value) {
        // keine null Werte erlaubt
        if (name == null || value == null) {
            return null;
        }

        // unmask all +
        value = value.replaceAll("\\+", "&#43;");

        // der Name und Wert werden gehasht, um zu überprüfen, ob jemand eine Änderunge gemacht hat
        String hash = passwordEncryptor.encryptPassword(name + "+" + value);

        // verschlüsseln den Hash, um ihn gegen Änderungen zu schützen
        String crypt = textEncryptor.encrypt(hash);

        // Rückgabe von name + value + gesicherten hash
        return value + "+" + crypt;
    }

    /**
     * Diese Funktion entschlüsselt einen einzelnen Cookie Wert.
     *
     * @param name
     * @param value
     * @return
     */
    private String generateUnprotectedCookie(String name, String value) {
        if (name == null || value == null) {
            return null;
        }

        // splittet den Wert zu Hash und Wert
        String split[] = value.split("\\+", 2);

        // wir brauchen den Wert und den Hash
        if (split.length != 2) {
            return null;
        }

        value = split[0];
        String hash = split[1];

        // holt den alten Hash mit decryption
        try {
        hash = textEncryptor.decrypt(hash);
        } catch(EncryptionOperationNotPossibleException ex){
            return null;
        }

        // wenn die Hashs gleich sind, hat sie keiner modifiziert und wir können sie benutzen
        if (passwordEncryptor.checkPassword(name + "+" + value, hash)) {
            return value;
        }

        return null;
    }
}

