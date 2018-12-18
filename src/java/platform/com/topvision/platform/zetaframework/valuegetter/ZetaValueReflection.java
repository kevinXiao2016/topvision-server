/***********************************************************************
 * $Id: ValueReflection.java,v1.0 2017年1月4日 下午3:47:07 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.zetaframework.valuegetter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.framework.common.ClassAware;
import com.topvision.platform.zetaframework.var.ZetaValueGetter;

/**
 * @author lizongtian
 * @created @2017年1月4日-下午3:47:07
 *
 */
public class ZetaValueReflection {
    private static final Logger logger = LoggerFactory.getLogger(ZetaValueReflection.class);
    private static Map<String, Class<?>> mapper = new HashMap<>();
    static {
        try {
            ClassAware $aware = new ClassAware();
            Set<Class<?>> $set = $aware.scanAnnotation("com.topvision", ZetaValueGetter.class);
            for (Class<?> $clazz : $set) {
                ZetaValueGetter $anno = $clazz.getAnnotation(ZetaValueGetter.class);
                String $value = $anno.value();
                if ($value == null || "".equals($value)) {
                    $value = $clazz.getSimpleName();
                }
                mapper.put($value, $clazz);
            }
        } catch (Exception e) {
            logger.info("", e);
        }
    }

    public static final Object getValue(String expression) {
        String[] params = expression.split("\\.");
        if (params.length != 2) {
            logger.debug("two parameters or more is not support!");
        }
        String first = params[0];

        Class<?> $clazz = mapper.get(first);
        if ($clazz == null) {
            logger.error("con not found :" + first);
            return null;
        }
        Object $value = null;
        try {
            Field $field = $clazz.getDeclaredField(params[1]);
            $field.setAccessible(true);
            $value = $field.get($clazz);
        } catch (Exception e) {
            Method $method;
            try {
                $method = $clazz.getMethod("get", String.class);
                $value = $method.invoke($clazz, params[1]);
            } catch (Exception e1) {
                logger.error("{}" + e1);
            }
        }
        return $value;
    }
}
