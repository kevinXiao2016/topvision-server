/***********************************************************************
 * $Id: JavascriptLoader.java,v1.0 2013-9-9 上午9:19:32 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.zetaframework.javascript;

import javax.servlet.jsp.JspWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.platform.zetaframework.stylesheet.CssStyleLoader;

/**
 * @author Bravin
 * @created @2013-9-9-上午9:19:32
 *
 */
public class JavascriptLoader {
    public static final Logger logger = LoggerFactory.getLogger(JavascriptLoader.class);
    public static final String PATH_SEPAROTR = "/";
    public static final String BLANK_ROW_STRING = "";
    public static final String JAVASCRIPT_SUFFIX = ".js";
    protected static final String SCRIPT_TEMPLATE = "<script type=\"text/javascript\" src=\"%s\"></script>";
    protected static final String STYLE_TEMPLATE = "<link rel=\"stylesheet\" type=\"text/css\" href=\"%s\" />";
    
    /**
     * 加载单个javascript文件
     * @param javascript
     * @param writer
     */
    public static void load(String javascript, JspWriter writer) {
        try {
            writer.println(String.format(SCRIPT_TEMPLATE, javascript));
        } catch (Exception e) {
            logger.warn("load javascript: {} faild", javascript);
        }
    }
    
    
    /**
    *  加载javascript列表
    * @param javascripts
    * @param writer
    */
    public static void load(String[] javascripts, JspWriter writer) {
        try {
            for (String javascript : javascripts) {
                writer.println(String.format(STYLE_TEMPLATE, "/css/ext-all.css"));
                writer.println(String.format(SCRIPT_TEMPLATE, javascript));
            }
        } catch (Exception e) {
            logger.warn("load javascripts: {} faild", javascripts.toString());
        }
    }

    /**
     *  加载javascript列表,styoesheet列表
     * @param javascripts
     * @param stylesheets
     * @param writer
     */
    public static void load(String[] javascripts, String[] stylesheets, JspWriter writer) {
        try {
            for (String stylesheet : stylesheets) {
                CssStyleLoader.load(writer, stylesheet);
            }
            for (String javascript : javascripts) {
                writer.println(String.format(SCRIPT_TEMPLATE, javascript));
            }
        } catch (Exception e) {
            logger.warn("load resource: {}{} faild", javascripts.toString(), stylesheets.toString());
        }
    }
}
