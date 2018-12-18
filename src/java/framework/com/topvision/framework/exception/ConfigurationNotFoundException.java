/**
 * 
 */
package com.topvision.framework.exception;

/**
 * @author niejun
 * 
 */
public class ConfigurationNotFoundException extends ConfigurationException {
    private static final long serialVersionUID = 3137065887234191615L;

    public ConfigurationNotFoundException(String item) {
        super("Configuration (" + item + ") Not Found Exception.");
    }
}
