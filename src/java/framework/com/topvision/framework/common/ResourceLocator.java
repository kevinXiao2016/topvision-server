/**
 * 
 */
package com.topvision.framework.common;

import java.awt.Image;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author niejun
 * 
 */
public class ResourceLocator {
    private static final String BASE_BUNDLE_PATH = "META-INF/";
    private static final String BASE_IMAGES_PATH = "META-INF/images/";
    private static final Logger logger = LoggerFactory.getLogger(ResourceLocator.class);
    private ResourceBundle resourceBundle = null;
    private ResourceLocator parentLocator;
    private ClassLoader classLoader = null;
    private String baseBundlePath = null;
    private String baseImgPath = null;
    private String rootPath = null;
    private Map<String, ImageIcon> images = new HashMap<String, ImageIcon>();

    public ResourceLocator(String bundle) {
        this(BASE_BUNDLE_PATH + bundle, BASE_IMAGES_PATH);
    }

    public ResourceLocator(String bundlePath, String imagePath) {
        this(bundlePath, imagePath, null);
    }

    public ResourceLocator(String bundlePath, String imagePath, ResourceLocator parent) {
        this(bundlePath, imagePath, parent, ResourceManager.RESOURCE_LOADER);
    }

    public ResourceLocator(String bundlePath, String imagePath, ResourceLocator parent, ClassLoader loader) {
        baseBundlePath = bundlePath;
        resourceBundle = ResourceBundle.getBundle(bundlePath, Locale.getDefault(), loader);
        baseImgPath = imagePath;
        this.parentLocator = parent;
        this.classLoader = loader;
        this.rootPath = System.getProperty("user.dir", "./");
    }

    public String getBaseBundlePath() {
        return baseBundlePath;
    }

    public String getBaseImgPath() {
        return baseImgPath;
    }

    /**
     * 
     * @param fileName
     * @return
     */
    public Image getImage(String fileName) {
        return getImageIcon(fileName).getImage();
    }

    /**
     * 
     * @param fileName
     * @return
     */
    public ImageIcon getImageIcon(String fileName) {
        ImageIcon icon = images.get(fileName);
        if (icon == null) {
            File file = new File(rootPath + File.separator + fileName);
            if (file.exists()) {
                icon = new ImageIcon(fileName);
                images.put(fileName, icon);
            } else {
                String path = baseImgPath + fileName;
                URL url = classLoader.getResource(path);
                if (url != null) {
                    icon = new ImageIcon(url);
                    images.put(fileName, icon);
                }
            }
        }
        if (icon == null && parentLocator != null) {
            icon = parentLocator.getImageIcon(fileName);
        }
        return icon;
    }

    /**
     * 
     * @param key
     * @return
     */
    public String getString(String key) {
        String value = null;
        try {
            value = resourceBundle.getString(key);
        } catch (Exception ex) {
            logger.debug("Not found resource by key:" + key, ex);
        }
        if (value == null && parentLocator != null) {
            value = parentLocator.getString(key);
        }
        return value;
    }

    /**
     * 
     * @param key
     * @param params
     * @return
     */
    public String getString(String key, Object... params) {
        String value = getString(key);
        if (value == null) {
            return null;
        }
        for (int i = 0; i < params.length; i++) {
            value = value.replace("{" + i + "}", params[i].toString());
        }
        return value;
    }
}
