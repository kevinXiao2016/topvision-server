/***********************************************************************
 * $Id: CssStyleLoader.java,v1.0 2013-5-8 下午2:09:42 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.zetaframework.stylesheet;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Bravin
 * @created @2013-5-8-下午2:09:42
 *
 */
public class CssStyleLoader {
    public static final Logger logger = LoggerFactory.getLogger(CssStyleLoader.class);
    private static final String CSS_SUFFIX = ".css";
    protected static final String STYLE_TEMPLATE = "<link rel=\"stylesheet\" type=\"text/css\" href=\"%s\" />";
    public static final String PATH_SEPAROTR = "/";

    /**
     *  引入层叠样式表
     *  TODO 这部分不完整,对于配置中的加载和javascript加载是完全不一样的
     * @param writer
     * @param stylesheet
     */
    public static void load(JspWriter writer, String stylesheet) {
        try {
            if (stylesheet.indexOf(PATH_SEPAROTR) == -1) {
                stylesheet = stylesheet.replaceAll("\\.", PATH_SEPAROTR);
            }
            writer.println(String.format(STYLE_TEMPLATE, "/" + stylesheet + CSS_SUFFIX));
        } catch (IOException e) {
            logger.warn("", e);
        }
    }

}
