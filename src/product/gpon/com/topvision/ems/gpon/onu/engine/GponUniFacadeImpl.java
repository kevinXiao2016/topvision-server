/***********************************************************************
 * $Id: GponUniFacadeImpl.java,v1.0 2016年12月25日 上午10:43:51 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.onu.engine;

import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.ems.gpon.onu.domain.GponOnuUniPvid;
import com.topvision.ems.gpon.onu.domain.GponUniAttribute;
import com.topvision.ems.gpon.onu.facade.GponUniFacade;
import com.topvision.framework.annotation.Facade;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author lizongtian
 * @created @2016年12月25日-上午10:43:51
 *
 */
@Facade("gponUniFacade")
public class GponUniFacadeImpl extends EmsFacade implements GponUniFacade {
    @Autowired
    private SnmpExecutorService snmpExecutorService;

    @Override
    public void setUni15minEnable(SnmpParam snmpParam, Long uniIndex, Integer uni15minEnable) {
        GponUniAttribute gponUniAttribute = new GponUniAttribute();
        gponUniAttribute.setUniIndex(uniIndex);
        gponUniAttribute.setEthPerfStats15minuteEnable(uni15minEnable);
        snmpExecutorService.setData(snmpParam, gponUniAttribute);
    }

    @Override
    public void setUniAdminStatus(SnmpParam snmpParam, Long uniIndex, Integer uniAdminStatus) {
        GponUniAttribute gponUniAttribute = new GponUniAttribute();
        gponUniAttribute.setUniIndex(uniIndex);
        gponUniAttribute.setEthAdminStatus(uniAdminStatus);
        snmpExecutorService.setData(snmpParam, gponUniAttribute);
    }

    @Override
    public void setUniVlanConfig(SnmpParam snmpParam, Long uniIndex, Integer uniPri, Integer uniPvid) {
        GponOnuUniPvid gponOnuUniPvid = new GponOnuUniPvid();
        gponOnuUniPvid.setUniIndex(uniIndex);
        gponOnuUniPvid.setGponOnuUniPvid(uniPvid);
        gponOnuUniPvid.setGponOnuUniPri(uniPri);
        snmpExecutorService.setData(snmpParam, gponOnuUniPvid);
    }
}
