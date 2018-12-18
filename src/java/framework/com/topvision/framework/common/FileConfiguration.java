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
public abstract class FileConfiguration implements Configuration {
    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.common.Configuration#getBoolean(java.lang.String)
     */
    @Override
    public boolean getBoolean(String key) throws ConfigurationNotFoundException {
        return "true".equalsIgnoreCase(getString(key));
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.common.Configuration#getBoolean(java.lang.String, boolean)
     */
    @Override
    public boolean getBoolean(String key, boolean defaultValue) {

        String value = null;
        try {
            value = getString(key);
        } catch (Exception ex) {
        }
        return (value == null || "".equals(value.trim())) ? defaultValue : "true".equalsIgnoreCase(value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.common.Configuration#getInt(java.lang.String)
     */
    @Override
    public int getInt(String key) throws ConfigurationNotFoundException {

        return Integer.parseInt(getString(key));
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.common.Configuration#getInt(java.lang.String, int)
     */
    @Override
    public int getInt(String key, int defaultValue) {
        String value = null;
        try {
            value = getString(key);
        } catch (Exception ex) {
        }
        return (value == null || "".equals(value.trim())) ? defaultValue : Integer.parseInt(value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.common.Configuration#getLong(java.lang.String)
     */
    @Override
    public long getLong(String key) throws ConfigurationNotFoundException {

        return Integer.parseInt(getString(key));
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.common.Configuration#getLong(java.lang.String, long)
     */
    @Override
    public long getLong(String key, long defaultValue) {
        String value = null;
        try {
            value = getString(key);
        } catch (Exception ex) {
        }
        return (value == null || "".equals(value.trim())) ? defaultValue : Long.parseLong(value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.common.Configuration#getString(java.lang.String)
     */
    @Override
    public abstract String getString(String key) throws ConfigurationNotFoundException;

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.common.Configuration#getString(java.lang.String,
     * java.lang.String)
     */
    @Override
    public abstract String getString(String key, String defaultValue);

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.common.Configuration#put(java.lang.String, java.lang.String)
     */
    @Override
    public abstract void put(String key, String value);

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.common.Configuration#save()
     */
    @Override
    public abstract void save() throws ConfigurationException;

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.common.Configuration#save(java.lang.String, java.lang.String)
     */
    @Override
    public abstract void save(String fileName, String comments) throws ConfigurationException;
}
