/**
 * 
 */
package com.topvision.framework.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.topvision.framework.exception.ConfigurationException;
import com.topvision.framework.exception.ConfigurationNotFoundException;

/**
 * @author niejun
 * 
 */
public class PropertiesFileConfiguration extends FileConfiguration {
    private Properties properties = null;
    private String fileName = null;

    /**
     * 
     * @param is
     * @throws ConfigurationException
     */
    public PropertiesFileConfiguration(InputStream is) throws ConfigurationException {
        this(is, false);
    }

    /**
     * 
     * @param is
     * @param xml
     * @throws ConfigurationException
     */
    public PropertiesFileConfiguration(InputStream is, boolean xml) throws ConfigurationException {
        try {
            properties = new Properties();
            if (xml) {
                properties.loadFromXML(is);
            } else {
                properties.load(is);
            }
        } catch (IOException ioe) {
            throw new ConfigurationException("Load Properties File Configuration:", ioe);
        } finally {
            FileUtils.closeQuitely(is);
        }
    }

    /**
     * 
     * @param fileName
     * @throws ConfigurationException
     */
    public PropertiesFileConfiguration(String fileName) throws ConfigurationException, FileNotFoundException {
        this(fileName, false);
    }

    /**
     * 
     * @param fileName
     * @param xml
     * @throws ConfigurationException
     */
    public PropertiesFileConfiguration(String fileName, boolean xml) throws ConfigurationException,
            FileNotFoundException {
        this(new FileInputStream(fileName), xml);
        this.fileName = fileName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.common.FileConfiguration#getString(java.lang.String)
     */
    @Override
    public String getString(String key) throws ConfigurationNotFoundException {
        return properties.getProperty(key);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.common.FileConfiguration#getString(java.lang.String,
     * java.lang.String)
     */
    @Override
    public String getString(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.common.FileConfiguration#put(java.lang.String, java.lang.String)
     */
    @Override
    public void put(String key, String value) {
        properties.setProperty(key, value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.common.FileConfiguration#save()
     */
    @Override
    public void save() throws ConfigurationException {
        if (fileName == null) {
            throw new ConfigurationException("File name not Null when Store Properties File Configuration.");
        }
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(fileName);
            properties.store(os, "");
        } catch (IOException ioe) {
            throw new ConfigurationException("Store Properties File Configuration:", ioe);
        } finally {
            FileUtils.closeQuitely(os);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.common.FileConfiguration#save(java.lang.String,
     * java.lang.String)
     */
    @Override
    public void save(String fileName, String comments) throws ConfigurationException {
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(fileName);
            properties.store(os, comments);
        } catch (IOException ioe) {
            throw new ConfigurationException("Store Properties File Configuration:", ioe);
        } finally {
            FileUtils.closeQuitely(os);
        }
    }

    @Override
    public String toString() {
        return properties.toString();
    }
}
