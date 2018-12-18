/***********************************************************************
 * $Id: JsonUtil.java,v1.0 2013-6-19 下午6:28:19 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.json.JSONObject;

/**
 * @author Bravin
 * @created @2013-6-19-下午6:28:19
 * 
 */
public class JSONUtil {
    /**
     * 把json对象串转换成map对象 由于所有的JSON对象本质上就是一个Map，所以 deserialize就是把一个JS对象转换为Map对象即可
     * 
     * @param jsonString
     *            e.g. {'name':'get','int':1,'double',1.1,'null':null}
     * @return Map
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static Map deserialize(String jsonString) {
        JSONObject jsonObject = JSONObject.fromObject(jsonString);
        Map map = new HashMap();
        for (Iterator iter = jsonObject.keys(); iter.hasNext();) {
            String key = (String) iter.next();
            map.put(key, jsonObject.get(key));
        }
        return map;
    }
}
