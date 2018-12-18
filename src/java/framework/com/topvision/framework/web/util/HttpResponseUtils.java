/*
 * @(#)HttpResponseUtils.java
 *
 * Copyright 2011 Topoview All rights reserved.
 */

package com.topvision.framework.web.util;

import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

/**
 * @author niejun
 * 
 */
public class HttpResponseUtils {
    /**
     * UTF-8编码.
     */
    public static final String UTF_8 = "UTF-8";

    public static String buildAttachmentName(String name, String desc, String suffux) throws Exception {
        return URLEncoder.encode(name, "UTF-8") + desc + "." + suffux;
    }

    public static void setHtmlResponse(HttpServletResponse response) {
        setHtmlResponse(response, UTF_8);
    }

    /**
     * 按给定的编码格式设置HTML输出响应.
     * 
     * @param response
     * @param encoding
     */
    public static void setHtmlResponse(HttpServletResponse response, String encoding) {
        response.setContentType("text/html;charset=" + encoding);
        setNoCacheResponse(response);
    }

    public static void setJSONResponse(HttpServletResponse response) {
        setJSONResponse(response, UTF_8);
    }

    /**
     * 设置JSON格式响应.
     * 
     * @param response
     * @param encoding
     */
    public static void setJSONResponse(HttpServletResponse response, String encoding) {
        response.setContentType("applicatioin/json;charset=" + encoding);
        setNoCacheResponse(response);

    }

    public static void setNoCacheResponse(HttpServletResponse response) {
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Expires", "0");
    }

    public static void setResponseHeader(HttpServletResponse response, String contentType, Map<String, String> header) {
        response.setContentType(contentType);
        for (Iterator<Map.Entry<String, String>> iter = header.entrySet().iterator(); iter.hasNext();) {
            Map.Entry<String, String> entry = iter.next();
            response.setHeader(entry.getKey(), entry.getValue());
        }
    }

    public static void setResponseHeader(HttpServletResponse response, String contentType, String disposition) {
        response.setContentType(contentType);
        response.setHeader("Content-disposition", disposition);
    }

    /**
     * 设置XML输出响应, 默认UTF-8编码.
     * 
     * @param response
     */
    public static void setXMLResponse(HttpServletResponse response) {
        setXMLResponse(response, UTF_8);
    }

    /**
     * 
     * @param response
     * @param encoding
     */
    public static void setXMLResponse(HttpServletResponse response, String encoding) {
        response.setContentType("text/xml;charset=" + encoding);
        setNoCacheResponse(response);
    }
}
