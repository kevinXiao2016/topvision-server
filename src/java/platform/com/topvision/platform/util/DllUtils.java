/***********************************************************************
 * $Id: DllUtils.java,v 1.1 Sep 27, 2009 5:54:08 PM kelers Exp $
 *
 * @author: kelers
 *
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.platform.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Vector;

/**
 * @author kelers
 * @Create Date Sep 27, 2009 5:54:08 PM
 */
public class DllUtils {

    /**
     * 释放动态链接库
     * 
     * @return
     */
    public Boolean freeLibrary() {
        boolean result = true;
        try {
            unloadNativeLibs();
        } catch (Throwable e) {
            result = false;
        }
        return result;
    }

    @SuppressWarnings("rawtypes")
    private void unloadNativeLibs() throws Throwable {
        ClassLoader classLoader = this.getClass().getClassLoader();
        Field field = ClassLoader.class.getDeclaredField("nativeLibraries");
        field.setAccessible(true);
        Vector libs = (Vector) field.get(classLoader);
        Iterator it = libs.iterator();
        Object o;
        while (it.hasNext()) {
            o = it.next();
            Method finalize = o.getClass().getDeclaredMethod("finalize", new Class[0]);
            finalize.setAccessible(true);
            finalize.invoke(o, new Object[0]);

        }
    }

}
