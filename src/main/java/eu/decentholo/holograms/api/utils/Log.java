package eu.decentholo.holograms.api.utils;

import lombok.experimental.UtilityClass;

import java.util.logging.Level;
import java.util.logging.Logger;

import eu.decentholo.holograms.api.DecentHologramsAPI;

/**
 * Utility class for logging.
 *
 * @author d0by
 * @since 2.8.9
 */
@UtilityClass
public final class Log {

    private static final Logger LOGGER = DecentHologramsAPI.get().getLogger();

    public static void info(String message) {
        LOGGER.info(message);
    }

    public static void info(String message, Object... args) {
        LOGGER.info(() -> String.format(message, args));
    }

    public static void info(String message, Throwable throwable) {
        LOGGER.log(Level.INFO, message, throwable);
    }

    public static void info(String message, Throwable throwable, Object... args) {
        LOGGER.log(Level.INFO, String.format(message, args), throwable);
    }

    public static void warn(String message) {
        LOGGER.warning(message);
    }

    public static void warn(String message, Object... args) {
        LOGGER.warning(() -> String.format(message, args));
    }

    public static void warn(String message, Throwable throwable) {
        LOGGER.log(Level.WARNING, message, throwable);
    }

    public static void warn(String message, Throwable throwable, Object... args) {
        LOGGER.log(Level.WARNING, String.format(message, args), throwable);
    }

    public static void error(String message) {
        LOGGER.severe(message);
    }

    public static void error(String message, Object... args) {
        LOGGER.severe(() -> String.format(message, args));
    }

    public static void error(String message, Throwable throwable) {
        LOGGER.log(Level.SEVERE, message, throwable);
    }

    public static void error(String message, Throwable throwable, Object... args) {
        LOGGER.log(Level.SEVERE, String.format(message, args), throwable);
    }

}