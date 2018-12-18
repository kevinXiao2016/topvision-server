/***********************************************************************
 * $Id: OnuSlaFacadeImpl.java,v1.0 2013年10月25日 下午5:54:43 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.qos.engine;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.ems.epon.qos.domain.SlaTable;
import com.topvision.ems.epon.qos.facade.OnuSlaFacade;
import com.topvision.framework.annotation.Facade;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author Bravin
 * @created @2013年10月25日-下午5:54:43
 *
 */
@Facade("onuSlaFacade")
public class OnuSlaFacadeImpl extends EmsFacade implements OnuSlaFacade {
    @Autowired
    private SnmpExecutorService snmpExecutorService;

    @Override
    public List<SlaTable> getOnuSla(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, SlaTable.class);
    }

    @Override
    public SlaTable getOnuSla(SnmpParam snmpParam, Long onuIndex) {
        SlaTable slaTalbe = new SlaTable();
        slaTalbe.setOnuIndex(onuIndex);
        return snmpExecutorService.getTableLine(snmpParam, slaTalbe);
    }

}
