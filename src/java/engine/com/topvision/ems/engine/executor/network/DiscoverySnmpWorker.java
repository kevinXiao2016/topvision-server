/***********************************************************************
 * $Id: DiscoverySnmpWorker.java,v1.0 2011-6-28 下午08:35:04 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.engine.executor.network;

import com.topvision.ems.facade.domain.DeviceBaseInfo;
import com.topvision.ems.facade.domain.DiscoveryData;
import com.topvision.framework.exception.engine.SnmpException;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.snmp.SnmpWorker;

/**
 * @author Victor
 * @created @2011-6-28-下午08:35:04
 * 
 */
public class DiscoverySnmpWorker<T extends DiscoveryData> extends SnmpWorker<T> {
    private static final long serialVersionUID = -6006646583596836944L;

    /**
     * @param snmpParam
     * @param data
     */
    public DiscoverySnmpWorker(SnmpParam snmpParam) {
        super(snmpParam);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.snmp.SnmpWorker#exec()
     */
    @Override
    protected void exec() {
        if (logger.isDebugEnabled()) {
            logger.debug("Begin to discover:" + snmpParam.getIpAddress());
        }
        snmpUtil.reset(snmpParam);
        result.setDiscoveryTime(System.currentTimeMillis());
        result.setEntityId(snmpParam.getEntityId());
        result.setIp(snmpParam.getIpAddress());
        if (!snmpUtil.verifySnmpVersion()) {
            result.setStackTrace(new SnmpException("Community Error"));
            return;
        }
        snmpParam.setVersion(snmpUtil.getVersion());
        result.setSnmpParam(snmpParam);
        try {
            result.setSystem(snmpUtil.get(DeviceBaseInfo.class));
        } catch (Exception ex) {
            // TODO Exception
        }
        try {
            // result.setInterfaces(snmpUtil.getTable(PortEntity.class, true));
        } catch (Exception ex) {
            // TODO Exception
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Discover ended:" + result);
        }
    }
}
