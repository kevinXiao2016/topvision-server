/***********************************************************************
 * $Id: Bean2StringUtil.java,v 1.1 May 11, 2009 9:39:14 PM kelers Exp $
 *
 * @author: kelers
 *
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.platform.util;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;

/**
 * @author kelers
 * @Create Date May 11, 2009 9:39:14 PM
 */
public class Bean2StringUtil {
    /**
     * 把一个java bean所有变量及值打印出来.
     * 
     * @param bean
     * @return java bean的所有field及其值list
     */
    public static String toString(Object bean) {
        StringBuffer results = new StringBuffer();
        Class<?> clazz = bean.getClass();

        results.append(clazz + "{\n");

        while (clazz != null && clazz != Object.class) {
            Field[] fields = clazz.getDeclaredFields();

            try {
                AccessibleObject.setAccessible(fields, true);
                for (Field field : fields) {
                    results.append("\t" + field.getName() + "=" + field.get(bean) + "\n");
                }
            } catch (Exception e) {
                // System.out.print("faild");
            }
            clazz = clazz.getSuperclass();
        }
        results.append("}");
        return results.toString();
    }
}
