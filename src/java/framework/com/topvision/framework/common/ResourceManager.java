/*
 * ResourceManager.java
 *
 * Created on 2007年10月11日, 下午2:58
 */
package com.topvision.framework.common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * @author niejun
 * 
 */
public class ResourceManager {
    public static final ResourceClassLoader RESOURCE_LOADER = new ResourceClassLoader(
            ResourceClassLoader.EMPTY_URL_ARRAY, ResourceManager.class.getClassLoader());
    private ResourceBundle resourceBundle = null;
    /**
     * Ordered list of the inserted resource bundles.
     */
    protected static Map<String, ResourceBundle> bundles = new HashMap<String, ResourceBundle>();

    public static void init(String name) {
    }

    public static ResourceManager getResourceManager(String baseName, String lang) {
        ResourceBundle resourceBundle;
        if (lang == null) {
            resourceBundle = ResourceBundle.getBundle(baseName);
        } else {
            // Modify by
            // Victor@20131022解决传入语言解析问题，直接使用zh_CN构造Locale会返回zh_cn而不是期望的zh_CN，在区分大小写的操作系统中会出错
            Locale locale = null;
            String[] ss = lang.split("_");
            if (ss != null && ss.length == 2) {
                locale = new Locale(ss[0], ss[1]);
            } else {
                locale = new Locale(lang);
            }
            resourceBundle = ResourceBundle.getBundle(baseName, locale);
        }
        ResourceManager rm = new ResourceManager();
        rm.setResourceBundle(resourceBundle);
        return rm;
    }

    public String getNotNullString(String key) {
        String v = ((PropertyResourceBundle) resourceBundle).getString(key);
        if (v == null) {
            return key;
        } else {
            return v;
        }
    }

    /**
     * Adds a resource bundle.
     * 
     * @param basename
     *            The basename of the resource bundle to add.
     */
    public static void addBundle(String basename) {
        if (!bundles.containsKey(basename)) {
            bundles.put(basename, ResourceBundle.getBundle(basename));
        }
    }

    /**
     * Adds an array of resource bundles using {@link #addBundle(String)}.
     * 
     * @param basenames
     *            The array of basenames to add.
     */
    public static void addBundles(String[] basenames) {
        if (basenames != null) {
            for (int i = 0; i < basenames.length; i++) {
                addBundle(basenames[i]);
            }
        }
    }

    /**
     * Returns the specified file as an image or <code>null</code> if there was an exception.
     * Exceptions are silently ignored by this method. This implementation first tries to load the
     * specified filename from the classpath. If the file cannot be found, it tries loading it as
     * external file or URL.
     * 
     * @param uri
     *            The URI to load the image from.
     * @return Returns the image for <code>filename</code>.
     * 
     * @see ImageIO#read(java.net.URL)
     * @see Class#getResource(java.lang.String)
     */
    public static ImageIcon getImage(String uri) {
        try {
            return new ImageIcon(ImageIO.read(ResourceManager.class.getResource(uri)));
        } catch (Exception e) {
            try {
                return new ImageIcon(ImageIO.read(new BufferedInputStream(ResourceManager.isURL(uri) ? new URL(uri)
                        .openStream() : new FileInputStream(uri))));
            } catch (Exception e1) {
                // ignore
            }
        }
        return null;
    }

    /**
     * Returns the specified file as a buffered input stream or <code>null</code> if there was an
     * exception. Exceptions are silently ignored by this method. This implementation first tries to
     * load the specified filename from the classpath. If the file cannot be found, it tries loading
     * it as external file or URL.
     * 
     * @param uri
     *            The URI to return the input stream for.
     * @throws IOException
     *             If the URI can not be read.
     * @throws FileNotFoundException
     *             If the URI can not be found.
     * @throws MalformedURLException
     *             If the URI is an invalid URL.
     * @return Returns the input stream for <code>filename</code>.
     * 
     * @see Class#getResource(java.lang.String)
     * @see URL#openStream()
     * @see BufferedInputStream
     */
    public static InputStream getInputStream(String uri) throws MalformedURLException, FileNotFoundException,
            IOException {
        URL url = ResourceManager.class.getResource(uri);
        try {
            return new BufferedInputStream(url.openStream());
        } catch (Exception e) {
            return new BufferedInputStream(ResourceManager.isURL(uri) ? new URL(uri).openStream()
                    : new FileInputStream(uri));
        }
    }

    /**
     * Returns a buffered output stream for the specified URI or null if there was an exception.
     * 
     * @param uri
     *            The URI to return the output stream for.
     * @return Returns an output stream for the specified URI.
     * 
     * @throws FileNotFoundException
     *             If the specified URI can not be found.
     */
    public static OutputStream getOutputStream(String uri) throws FileNotFoundException {
        OutputStream out = null;
        if (ResourceManager.isURL(uri))
            out = new ByteArrayOutputStream();
        else
            out = new BufferedOutputStream(new FileOutputStream(uri));
        return out;
    }

    /**
     * Returns the value for <code>key</code> by searching the resource bundles in inverse order or
     * <code>null</code> if no value can be found for <code>key</code>.
     * 
     * @param key
     *            The key to be searched for.
     * @return Returns the value for <code>key</code> or <code>null</code>.
     * 
     * @see ResourceBundle#getString(java.lang.String)
     */
    public static String getString(String key) {
        Iterator<ResourceBundle> it = bundles.values().iterator();
        while (it.hasNext()) {
            try {
                return ((PropertyResourceBundle) it.next()).getString(key);
            } catch (MissingResourceException mrex) {
                // continue
            }
        }
        return null;
    }

    /**
     * Returns the value for <code>key</code> replacing every occurrence of <code>{0}</code> with
     * <code>param</code>. This is a shortcut method for values with only one placeholder.
     * 
     * @return Returns the parametrized value for <code>key</code>.
     * 
     * @see #getString(String, Object[])
     */
    public static String getString(String key, Object param) {
        return getString(key, new Object[] { param });
    }

    /**
     * Returns the value for <code>key</code> replacing every occurrence of <code>{i}</code> with
     * <code>params[i]</code> where i is an integer (i = 0, 1, ..., n).
     * 
     * @return Returns the parametrized value for <code>key</code>.
     */
    public static String getString(String key, Object[] params) {
        String base = getString(key);
        if (base != null && params != null) {
            // Allocates space for the result string
            int len = base.length();
            for (int i = 0; i < params.length; i++)
                len += String.valueOf(params[i]).length();
            StringBuffer ret = new StringBuffer(len);

            // Parses the resource string and replaces placeholders
            StringBuffer indexString = null;
            for (int i = 0; i < base.length(); i++) {
                char c = base.charAt(i);

                // Starts reading the value index
                if (c == '{') {

                    // Assumes an average index length of 1
                    indexString = new StringBuffer(1);

                } else if (indexString != null && c == '}') {

                    // Finishes reading and appends the value
                    // Issues a warning if the index is wrong
                    // and warnings are switched on.
                    int index = Integer.parseInt(indexString.toString());
                    if (index >= 0 && index < params.length)
                        ret.append(params[index]);
                    indexString = null;

                } else if (indexString != null) {

                    // Reads the value index
                    indexString.append(c);

                } else {
                    ret.append(c);
                }
            }
            return ret.toString();
        }
        return base;
    }

    public static boolean isURL(Object value) {
        return (value != null && (value.toString().startsWith("http://") || value.toString().startsWith("mailto:")
                || value.toString().startsWith("ftp://") || value.toString().startsWith("file:")
                || value.toString().startsWith("https://") || value.toString().startsWith("webdav://") || value
                .toString().startsWith("webdavs://")));
    }

    /**
     * @return the resourceBundle
     */
    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    /**
     * @param resourceBundle
     *            the resourceBundle to set
     */
    public void setResourceBundle(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    //
    // /**
    // *
    // * @param fileName
    // * @return
    // */
    // public Image getImage(String fileName) {
    // return getImageIcon(fileName).getImage();
    // }
    //
    // /**
    // *
    // * @param fileName
    // * @return
    // */
    // public ImageIcon getImageIcon(String fileName) {
    // ImageIcon icon = images.get(fileName);
    // if (icon == null) {
    // File file = new File(rootPath + File.separator + fileName);
    // if (file.exists()) {
    // icon = new ImageIcon(fileName);
    // images.put(fileName, icon);
    // } else {
    // String path = baseImgPath + fileName;
    // URL url = classLoader.getResource(path);
    // if (url != null) {
    // icon = new ImageIcon(url);
    // images.put(fileName, icon);
    // }
    // }
    // }
    // if (icon == null && parentLocator != null) {
    // icon = parentLocator.getImageIcon(fileName);
    // }
    // return icon;
    // }
    //
    // /**
    // *
    // * @param key
    // * @return
    // */
    // public String getString(String key) {
    // String value = null;
    // try {
    // value = resourceBundle.getString(key);
    // } catch (Exception ex) {
    // logger.debug("Not found resource by key:" + key, ex);
    // }
    // if (value == null && parentLocator != null) {
    // value = parentLocator.getString(key);
    // }
    // return value;
    // }
    //
    // /**
    // *
    // * @param key
    // * @param params
    // * @return
    // */
    // public String getString(String key, Object... params) {
    // String value = getString(key);
    // if (value == null) {
    // return null;
    // }
    // for (int i = 0; i < params.length; i++) {
    // value = value.replace("{" + i + "}", params[i].toString());
    // }
    // return value;
    // }
}
