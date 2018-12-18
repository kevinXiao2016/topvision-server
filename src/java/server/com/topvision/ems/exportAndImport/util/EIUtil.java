/***********************************************************************
 * $Id: EIUtil.java,v1.0 2015-7-10 下午2:49:55 $
 * 
 * @author: Fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.exportAndImport.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * @author fanzidong
 * @created @2015-7-10-下午2:49:55
 * 
 */
public class EIUtil {

    /**
     * 根据不同的sheet对应的domain，返回对应的列数组
     * 
     * @param clazz
     * @return
     */
    public static List<String> getColumnList(Class<?> clazz) {
        List<String> columns = new ArrayList<String>();
        Field[] fields = clazz.getDeclaredFields();
        //遍历所有属性
        for(int i=0; i<fields.length; i++){
            String descriptor=Modifier.toString(fields[i].getModifiers());
            //TODO static 
            if(!descriptor.contains("static")){
                columns.add(fields[i].getName());
            }
        }
        return columns;
    }
}
