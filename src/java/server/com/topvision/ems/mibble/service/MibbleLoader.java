/***********************************************************************
 * $Id: MibbleLoader.java,v1.0 2012-11-28 下午12:48:10 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.mibble.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.swing.JTree;

import com.topvision.framework.service.Service;

import net.percederberg.mibble.Mib;
import net.percederberg.mibble.MibLoaderException;
import net.percederberg.mibble.browser.MibNode;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

/**
 * @author Bravin
 * @created @2012-11-28-下午12:48:10
 * 
 */
public interface MibbleLoader extends Service {

    /**
     * 解析一个MIB对象成一个JSON对象
     * 
     * @param root
     * @param json
     * @throws JSONException
     * @throws UnsupportedEncodingException
     */
    void parseMib(MibNode root, JSONObject json) throws JSONException, UnsupportedEncodingException;

    /**
     * Loads MIB file or URL.
     * 
     * @param src
     * @return
     * @throws IOException
     * @throws MibLoaderException
     */
    Mib loadMib(String src) throws IOException, MibLoaderException;

    /**
     * parse a MIB to the MIB tree json.
     * 
     * @param mib
     * @return
     */
    JTree parse(Mib mib);
}
