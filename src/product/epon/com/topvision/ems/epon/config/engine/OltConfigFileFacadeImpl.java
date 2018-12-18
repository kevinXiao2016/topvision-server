/***********************************************************************
 * $Id: OltConfigInfoFacadeImpl.java,v1.0 2013-10-26 上午10:08:52 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.config.engine;

import java.util.concurrent.atomic.AtomicInteger;

import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.ems.epon.config.facade.OltConfigFileFacade;
import com.topvision.framework.exception.engine.SnmpSetException;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.snmp.SnmpWorker;

/**
 * @author flack
 * @created @2013-10-26-上午10:08:52
 *
 */
public class OltConfigFileFacadeImpl extends EmsFacade implements OltConfigFileFacade {
    private SnmpExecutorService snmpExecutorService;

    @Override
    public void saveOltConfig(SnmpParam snmpParam) {
        snmpExecutorService.setSnmpSetTimeout(300000);
        String oid = "1.3.6.1.4.1.32285.11.2.3.1.2.3.0";
        snmpExecutorService.set(snmpParam, oid, "1");
    }

    @Override
    public Integer getOltSaveStatus(SnmpParam snmpParam) {
        AtomicInteger status = new AtomicInteger(-1);
        try {
            status = snmpExecutorService.execute(new SnmpWorker<AtomicInteger>(snmpParam) {
                private static final long serialVersionUID = -727320896339548070L;

                @Override
                protected void exec() {
                    snmpParam.setTimeout(30000L);
                    snmpUtil.reset(snmpParam);
                    result.set(Integer.parseInt(snmpUtil.get("1.3.6.1.4.1.32285.11.2.3.1.2.4.0")));
                }
            }, status);
        } catch (Exception e) {
            logger.debug("", e);
            throw new SnmpSetException(e);
        }
        return status.intValue();
    }

    public SnmpExecutorService getSnmpExecutorService() {
        return snmpExecutorService;
    }

    public void setSnmpExecutorService(SnmpExecutorService snmpExecutorService) {
        this.snmpExecutorService = snmpExecutorService;
    }

}
