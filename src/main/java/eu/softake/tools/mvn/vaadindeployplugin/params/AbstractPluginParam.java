package eu.softake.tools.mvn.vaadindeployplugin.params;

import java.util.Locale;

/**
 * Abstract base class for plugin parameter objects.
 * <p>
 * This class provides utility methods to manipulate and clean up string values
 * that are used in the plugin parameters, such as trimming, lowercasing, and
 * decorating paths.
 * </p>
 */
public abstract class AbstractPluginParam {

    /**
     * Trims the leading and trailing whitespace from the given value.
     *
     * @param value the string to trim
     * @return the trimmed string, or null if the input was null
     */
    protected String trim(String value) {
        if (value != null) {
            value = value.trim();
        }
        return value;
    }

    /**
     * Converts the given value to lowercase and trims the leading and trailing whitespace.
     *
     * @param value the string to process
     * @return the lowercase and trimmed string, or null if the input was null
     */
    protected String lowercaseAndTrim(String value) {
        if (value != null) {
            value = value.toLowerCase(Locale.ROOT).trim();
        }
        return value;
    }

    /**
     * Ensures the given path starts with a "/" and does not end with one.
     *
     * @param path the path to decorate
     * @return the decorated path, or null if the input was null
     */
    protected String decoratePath(String path) {
        if (path != null) {
            // Ensure it starts with a "/" and ends without one
            path = "/" + path.trim().replaceAll("^/|/$", "");
        }
        return path;
    }
}