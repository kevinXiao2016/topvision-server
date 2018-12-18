/**
 * 
 */
package com.topvision.framework.common;

import java.io.FileNotFoundException;

import com.topvision.framework.exception.ConfigurationException;

/**
 * @author niejun
 * 
 */
public class ConfigurationFactory {

    public static Configuration buildClasspathPropertiesFileConfiguration(String path) throws ConfigurationException,
            FileNotFoundException {
        return new PropertiesFileConfiguration(ConfigurationFactory.class.getClass().getResourceAsStream(path));
    }

    public static Configuration buildPropertiesFileConfiguration(String path) throws ConfigurationException,
            FileNotFoundException {

        return new PropertiesFileConfiguration(path);
    }

}
