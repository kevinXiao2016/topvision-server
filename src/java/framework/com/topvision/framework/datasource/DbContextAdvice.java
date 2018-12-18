/***********************************************************************
 * $Id: DbContextAdvice.java,v1.0 2015-3-28 上午10:28:41 $
 * 
 * @author: Administrator
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.datasource;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.framework.annotation.DynamicDB;

/**
 * @author Rod John
 * @created @2015-3-28-上午10:28:41
 *
 */
public class DbContextAdvice {
    private static Logger logger = LoggerFactory.getLogger(DbContextAdvice.class);

    public Object dbContextAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        DynamicDB dbAnnotation = null;
        try {
            //拦截的实体类
           /* Object target = proceedingJoinPoint.getTarget();
            //拦截的方法名称
            String methodName = proceedingJoinPoint.getSignature().getName();
            //拦截的放参数类型
            Class[] parameterTypes1 = ((MethodSignature) proceedingJoinPoint.getSignature()).getMethod()
                    .getParameterTypes();
            try {
                Class[] parameterTypes = (Class[]) proceedingJoinPoint.getArgs();
            } catch (Exception e) {
                // TODO: handle exception
            }
            //通过反射获得拦截的method
            Method method = target.getClass().getMethod(methodName, parameterTypes1);*/

            MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
            Method method = signature.getMethod();

            dbAnnotation = method.getAnnotation(DynamicDB.class);
            if (dbAnnotation != null) {
                String dbKey = dbAnnotation.dbKey();
                DbContextHolder.setJdbcType(dbKey);
            }
            Object result = proceedingJoinPoint.proceed();
            return result;
        } catch (Throwable e) {
            throw e;
        } finally {
            if (dbAnnotation != null) {
                DbContextHolder.cleanJdbcType();
            }
        }
    }
}
