package com.topvision.ems.network.util;

/***********************************************************************
 * $Id: XmlOperationUtil.java,v1.0 2011-9-21 下午01:55:58 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lizongtian
 * @created @2011-9-21-下午01:55:58
 * 
 */
public class XmlOperationUtil {
    private static Logger logger = LoggerFactory.getLogger(XmlOperationUtil.class);
    public static Object readObject(File file) {
        Object re = null;
        try {
            XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(file)));
            re = decoder.readObject();
            decoder.close();
        } catch (FileNotFoundException e) {
            logger.info("xml file not found");
        }
        return re;
    }

    public static void writeObject(final String path, final Object o) {
        try {
            XMLEncoder e = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(path)));
            e.writeObject(o);
            e.close();
        } catch (FileNotFoundException e) {
            logger.info("xml file not found");
        }
    }
}
