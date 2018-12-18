/**
 * 
 */
package com.topvision.framework.exception;

/**
 * @author niejun
 * 
 */
public class ResourceNotFoundException extends TopvisionRuntimeException {
    private static final long serialVersionUID = -218696217826037011L;

    public ResourceNotFoundException(String key) {
        super("Resource (" + key + ") Not Found Exception.");
    }
}
