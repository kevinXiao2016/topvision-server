/***********************************************************************
 * $Id: NbiSupportEngineSaver.java,v1.0 2016年3月14日 上午10:55:15 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.engine.nbi;

import java.lang.reflect.Field;
import java.rmi.RemoteException;

import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.engine.BaseEngine;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceResult;
import com.topvision.ems.facade.nbi.NbiSnmpProperty;
import com.topvision.framework.annotation.SnmpProperty;

/**
 * @author Bravin
 * @created @2016年3月14日-上午10:55:15
 *
 */
public abstract class NbiSupportEngineSaver extends BaseEngine {
    @Autowired
    private NbiSupportService nbiSupportService;

    protected <T> void redirctPerformanceData(T data, String ip, Long entityId, Long index) {
        Class<? extends Object> clazz = data.getClass();
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            String oid = null;
            NbiSnmpProperty nbiSnmpProperty = field.getAnnotation(NbiSnmpProperty.class);
            if (nbiSnmpProperty != null) {
                oid = nbiSnmpProperty.oid();
            } else {
                SnmpProperty snmpProperty = field.getAnnotation(SnmpProperty.class);
                if (snmpProperty != null) {
                    oid = snmpProperty.oid();
                    //index数据不传
                    if (snmpProperty.index()) {
                        continue;
                    }
                }
            }
            if (oid != null) {
                field.setAccessible(true);
                Object value;
                try {
                    value = field.get(data);
                    nbiSupportService.redirect(oid, value, ip, entityId, index);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    logger.error("", e);
                } catch (RemoteException e) {
                    logger.error("", e);
                }
            }
        }
    }
    
    protected void redirctPerformanceData(String oid, Object value, PerformanceResult<OperClass> result, Long entityId,
            Long index) {
        try {
            nbiSupportService.redirect(oid, value, result.getDomain().getIpAddress(), entityId, index);
        } catch (RemoteException e) {
            logger.error("", e);
        }
    }

    /**
     * @param oltFlowQuality
     * @param result
     * @param entityId
     * @param ifIndex
     */
    protected <T> void redirctPerformanceData(T data, PerformanceResult<OperClass> result, Long entityId, Long index) {
        redirctPerformanceData(data, result.getDomain().getIpAddress(), entityId, index);
    }
}
