/***********************************************************************
 * $ DocUtil.java,v1.0 2013-3-18 10:47:00 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * @author jay
 * @created @2013-3-18-10:47:00
 */
public class DocUtil {
    private static final long serialVersionUID = 7342180206202447869L;

    public static String filePath2Doc(String filePath) throws IOException {
        if (filePath == null) {
            return null;
        }
        File file = new File(filePath);
        return file2Doc(file);
    }

    public static String file2Doc(File file) throws IOException {
        if (file == null) {
            return null;
        }
        URL url = file.toURI().toURL();
        return url2Doc(url);
    }
    
    public static String url2Doc(URL url) throws IOException {
        if (url == null) {
            return null;
        }
        InputStream doc = url.openStream();
        try {
            byte[] bs = new byte[doc.available()];
            int index = doc.read(bs, 0, bs.length);
            while (index < bs.length && index != -1) {
                index += doc.read(bs, index, bs.length - index);
            }
            return new String(bs);
        } finally {
            doc.close();
        }
    }
}
