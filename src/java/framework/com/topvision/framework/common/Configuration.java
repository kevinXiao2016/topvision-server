/**
 * 
 */
package com.topvision.framework.common;

import com.topvision.framework.exception.ConfigurationException;
import com.topvision.framework.exception.ConfigurationNotFoundException;

/**
 * @author niejun
 * 
 */
public interface Configuration {

    /**
     * 
     * @param key
     * @return
     * @throws ConfigurationNotFoundException
     */
    boolean getBoolean(String key) throws ConfigurationNotFoundException;

    /**
     * 
     * @param key
     * @param defaultValue
     * @return
     * @throws ConfigurationNotFoundException
     */
    boolean getBoolean(String key, boolean defaultValue);

    /**
     * 
     * @param key
     * @return
     * @throws ConfigurationNotFoundException
     */
    int getInt(String key) throws ConfigurationNotFoundException;

    /**
     * 
     * @param key
     * @param defaultValue
     * @return
     * @throws ConfigurationNotFoundException
     */
    int getInt(String key, int defaultValue);

    /**
     * 
     * @param key
     * @return
     * @throws ConfigurationNotFoundException
     */
    long getLong(String key) throws ConfigurationNotFoundException;

    /**
     * 
     * @param key
     * @param defaultValue
     * @return
     * @throws ConfigurationNotFoundException
     */
    long getLong(String key, long defaultValue);

    /**
     * 
     * @param key
     * @return
     * @throws ConfigurationNotFoundException
     */
    String getString(String key) throws ConfigurationNotFoundException;

    /**
     * 
     * @param key
     * @param defaultValue
     * @return
     * @throws ConfigurationNotFoundException
     */
    String getString(String key, String defaultValue);

    void put(String key, String value);

    void save() throws ConfigurationException;

    void save(String fileName, String comments) throws ConfigurationException;
}
