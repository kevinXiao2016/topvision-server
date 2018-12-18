/***********************************************************************
 * $Id: OnuCpeFacadeImpl.java,v1.0 2016年7月6日 下午3:06:46 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onucpe.engine;

import com.topvision.ems.epon.performance.domain.GponOnuUniCpeList;
import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.ems.epon.cpelocation.domain.OnuCpeIpLocation;
import com.topvision.ems.epon.cpelocation.domain.OnuCpeLocation;
import com.topvision.ems.epon.onucpe.facade.OnuCpeFacade;
import com.topvision.ems.epon.performance.domain.OnuUniCpeList;
import com.topvision.framework.annotation.Facade;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author Bravin
 * @created @2016年7月6日-下午3:06:46
 *
 */
@Facade("onuCpeFacade")
public class OnuCpeFacadeImpl extends EmsFacade implements OnuCpeFacade {
    @Autowired
    private SnmpExecutorService snmpExecutorService;

    @Override
    public OnuUniCpeList refreshEponOnuUniCpe(SnmpParam snmpParam, Long uniIndex) {
        OnuUniCpeList cpe = new OnuUniCpeList();
        cpe.setPortIndex(uniIndex);
        OnuUniCpeList tableLine = snmpExecutorService.getTableLine(snmpParam, cpe);
        return tableLine;
    }

    @Override
    public GponOnuUniCpeList refreshGponOnuUniCpe(SnmpParam snmpParam, Long uniIndex) {
        GponOnuUniCpeList cpe = new GponOnuUniCpeList();
        cpe.setPortIndex(uniIndex);
        GponOnuUniCpeList tableLine = snmpExecutorService.getTableLine(snmpParam, cpe);
        return tableLine;
    }

    @Override
    public String fetchOnuCpeLocationList(SnmpParam snmpParam, OnuCpeLocation cpeLocation) {
        String oid = "1.3.6.1.4.1.32285.11.2.3.1.12.2.1.4.";
        oid += cpeLocation.getSlotLocation() + ".";
        oid += cpeLocation.getPortLocation() + ".";
        oid += cpeLocation.getOnuLocation();
        return snmpExecutorService.get(snmpParam, oid);
    }

    @Override
    public OnuCpeIpLocation fetchOnuCpeLocationByIP(SnmpParam snmpParam, String ip) {
        OnuCpeIpLocation location = new OnuCpeIpLocation();
        location.setIpAddress(ip);
        return snmpExecutorService.getTableLine(snmpParam, location);
    }

    @Override
    public String fetchOnuCpeLocationList(SnmpParam snmpParam, String index) {
        String oid = "1.3.6.1.4.1.32285.11.2.3.1.12.2.1.4.";
        return snmpExecutorService.get(snmpParam, oid + index);
    }
}
