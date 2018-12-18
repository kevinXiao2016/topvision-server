/**
 * 
 */
package com.topvision.framework.exception;

/**
 * @author niejun
 * 
 */
public class ConfigurationException extends TopvisionRuntimeException {
    private static final long serialVersionUID = -1970283771544923419L;

    public ConfigurationException(String msg) {
        super(msg);
    }

    public ConfigurationException(String msg, Throwable th) {
        super(msg, th);
    }
}
