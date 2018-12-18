/**
 * 
 */
package com.topvision.framework.common;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author niejun
 * 
 */
public class ResourceClassLoader extends URLClassLoader {
    public static final URL[] EMPTY_URL_ARRAY = {};

    public ResourceClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    public void addClassLib(String libpath) {
        try {
            File files = new File(libpath);
            URL homeURL = files.toURI().toURL();
            String filename[] = files.list();
            if (filename == null) {
                return;
            }
            for (int i = 0; i < filename.length; i++) {
                addURL(new URL(homeURL, filename[i]));
            }
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
        }
    }
}
