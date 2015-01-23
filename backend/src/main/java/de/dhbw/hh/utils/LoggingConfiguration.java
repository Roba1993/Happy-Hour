package de.dhbw.hh.utils;

import ch.qos.logback.classic.Level;

import java.util.Properties;

/**
 * In dieser Klasse werden Logging Einstellungen vorgenommen
 *
 * @author Robert
 */
public class LoggingConfiguration {

    /**
     * Sets the logging settings which are defined in the given properties
     *
     * @param props The defined logging properties
     */
    public static void setLoggingConfiguration(Properties props) {
        setLoggingLevel(props.getProperty("log.level"));
    }

    /**
     * Set the logging level
     *
     * @param logLevel Slf4j logging level
     */
    public static void setLoggingLevel(String logLevel) {
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        root.setLevel(Level.toLevel(logLevel, Level.INFO));
    }
    
}
