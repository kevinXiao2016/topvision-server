/***********************************************************************
 * $Id: XssHttpServletRequestWrapper.java,v1.0 2015-9-9 下午2:39:52 $
 * 
 * @author: Fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.web.filter;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.struts2.dispatcher.StrutsRequestWrapper;

/**
 * @author fanzidong
 * @created @2015-9-9-下午2:39:52
 *
 */
public class XssHttpServletRequestWrapper extends StrutsRequestWrapper{

    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }
    
    @Override  
    public String getHeader(String name) {  
        return StringEscapeUtils.escapeHtml4(super.getHeader(name));  
    }  
  
    @Override  
    public String getQueryString() {  
        return StringEscapeUtils.escapeHtml4(super.getQueryString());  
    }  
  
    @Override  
    public String getParameter(String name) {  
        return StringEscapeUtils.escapeHtml4(super.getParameter(name));  
    }  
  
    @Override  
    public String[] getParameterValues(String name) {  
        String[] values = super.getParameterValues(name);  
        if(values != null) {  
            int length = values.length;  
            String[] escapseValues = new String[length];  
            for(int i = 0; i < length; i++){  
                escapseValues[i] = StringEscapeUtils.escapeHtml4(values[i]);  
            }  
            return escapseValues;  
        }  
        return super.getParameterValues(name);  
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        @SuppressWarnings("unchecked")
        Map<String, String[]> paramMap = super.getParameterMap();
        Set<String> keySet = paramMap.keySet();
        for (Iterator<String> iterator = keySet.iterator(); iterator.hasNext();) {
            String key = (String) iterator.next();
            String[] str = paramMap.get(key);
            for(int i=0; i<str.length; i++) {
                str[i] = StringEscapeUtils.escapeHtml4(str[i]);  
            }
        }
        return paramMap ;
    }  
    
    

}
