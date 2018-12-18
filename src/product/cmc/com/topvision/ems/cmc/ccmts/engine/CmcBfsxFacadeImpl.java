/***********************************************************************
 * $Id: CmcBfsxFacadeImpl.java,v1.0 2014年9月23日 下午3:39:52 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ccmts.engine;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.cmc.ccmts.facade.CmcBfsxFacade;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcBfsxSnapInfo;
import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.framework.annotation.Facade;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author Bravin
 * @created @2014年9月23日-下午3:39:52
 *
 */
@Facade("cmcBfsxFacade")
public class CmcBfsxFacadeImpl extends EmsFacade implements CmcBfsxFacade {
    @Autowired
    private SnmpExecutorService snmpExecutorService;

    @Override
    public List<CmcBfsxSnapInfo> getCmcBfsxSnapInfo(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, CmcBfsxSnapInfo.class);
    }

    @Override
    public CmcBfsxSnapInfo getCC8800ABaseInfo(SnmpParam snmpParam, CmcBfsxSnapInfo baseInfo) {
        return snmpExecutorService.getTableLine(snmpParam, baseInfo);
    }

    @Override
    public String getCmcSysUpTime(SnmpParam snmpParam) {
        return snmpExecutorService.get(snmpParam, "1.3.6.1.2.1.1.3.0");
    }

}
