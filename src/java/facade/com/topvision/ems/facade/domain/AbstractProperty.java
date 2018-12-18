/***********************************************************************
 * $Id: AbstractProperty.java,v 1.1 Mar 20, 2009 10:53:44 PM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.ems.facade.domain;

import java.io.Serializable;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Create Date Mar 20, 2009 10:53:44 PM
 * 
 * @author kelers
 * 
 */
public class AbstractProperty implements Serializable {
    private static final long serialVersionUID = -459403156992610322L;
    protected transient Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder results = new StringBuilder();
        Class<?> clazz = getClass();

        results.append(clazz + "\n");

        while (clazz != Object.class) {
            Field[] fields = clazz.getDeclaredFields();
            try {
                AccessibleObject.setAccessible(fields, true);
                for (Field field : fields) {
                    results.append("\t" + field.getName() + "=" + field.get(this) + "\n");
                }
            } catch (Exception e) {
                logger.debug(e.getMessage(), e);
            }
            clazz = clazz.getSuperclass();
        }

        return results.toString();
    }
}
