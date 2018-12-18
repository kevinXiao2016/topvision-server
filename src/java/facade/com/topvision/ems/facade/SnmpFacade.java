/***********************************************************************
 * $Id: SnmpFacade.java,v 1.1 Oct 25, 2008 5:04:22 PM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.ems.facade;

import java.util.List;

import com.topvision.ems.facade.domain.SnmpData;
import com.topvision.ems.facade.domain.SnmpMonitorParam;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.exception.engine.SnmpException;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @Create Date Oct 25, 2008 5:04:22 PM
 * 
 * @author kelers
 * 
 */
@EngineFacade(serviceName = "SnmpFacade", beanName = "snmpFacade")
public interface SnmpFacade extends Facade {
    <T> T get(SnmpParam param, Class<T> clazz) throws SnmpException;

    SnmpMonitorParam getSnmpMonitor(SnmpMonitorParam monitorParam) throws SnmpException;

    <T> List<T> getTable(SnmpParam param, Class<T> clazz) throws SnmpException;

    SnmpData set(SnmpData data) throws SnmpException;

    <T> T set(SnmpParam param, T data) throws SnmpException;
}
