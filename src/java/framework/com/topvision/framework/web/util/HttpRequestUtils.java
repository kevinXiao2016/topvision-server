/**
 * 
 */
package com.topvision.framework.web.util;

import javax.servlet.http.HttpServletRequest;

/**
 * @author niejun
 * 
 */
public class HttpRequestUtils {
    public boolean getBoolean(HttpServletRequest request, String key) {
        String v = request.getParameter(key);
        return "true".equalsIgnoreCase(v);
    }

    public int getInt(HttpServletRequest request, String key, int defaultValue) {
        String v = request.getParameter(key);
        return v == null ? defaultValue : Integer.parseInt(v);
    }

    public String getInt(HttpServletRequest request, String key, String defaultValue) {
        String v = request.getParameter(key);
        return v == null ? defaultValue : v;
    }

    public String getString(HttpServletRequest request, String key) {
        return request.getParameter(key);
    }
}
