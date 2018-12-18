/***********************************************************************
 * $Id: OltTrunkFacadeImpl.java,v1.0 2013-10-25 下午3:22:53 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.trunk.engine;

import java.util.List;

import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.ems.epon.trunk.domain.OltSniTrunkConfig;
import com.topvision.ems.epon.trunk.facade.OltTrunkFacade;
import com.topvision.framework.snmp.RowStatus;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author flack
 * @created @2013-10-25-下午3:22:53
 *
 */
public class OltTrunkFacadeImpl extends EmsFacade implements OltTrunkFacade {
    private SnmpExecutorService snmpExecutorService;

    @Override
    public List<OltSniTrunkConfig> refreshSniTrunkConfig(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, OltSniTrunkConfig.class);
    }

    /**
     * 修改一个TrunkGroup
     * 
     * @param snmpParam
     *            对应设备的snmpParam
     * @param oltSniTrunkConfig
     */
    @Override
    public OltSniTrunkConfig modifySniTrunkConfig(SnmpParam snmpParam, OltSniTrunkConfig oltSniTrunkConfig) {
        return snmpExecutorService.setData(snmpParam, oltSniTrunkConfig);
    }

    @Override
    public OltSniTrunkConfig addSniTrunkConfig(SnmpParam snmpParam, OltSniTrunkConfig sniTrunkConfig) {
        sniTrunkConfig.setSniTrunkGroupConfigRowstatus(RowStatus.CREATE_AND_GO);
        snmpExecutorService.setData(snmpParam, sniTrunkConfig);
        OltSniTrunkConfig oltSniTrunkConfig = new OltSniTrunkConfig();
        oltSniTrunkConfig.setEntityId(sniTrunkConfig.getEntityId());
        oltSniTrunkConfig.setSniTrunkGroupConfigIndex(sniTrunkConfig.getSniTrunkGroupConfigIndex());
        oltSniTrunkConfig.setSniTrunkGroupIndex(sniTrunkConfig.getSniTrunkGroupConfigIndex());
        return snmpExecutorService.getTableLine(snmpParam, oltSniTrunkConfig);
    }

    /**
     * 删除一个TrunkGroup
     * 
     * @param snmpParam
     *            对应设备的snmpParam
     * @param oltSniTrunkConfig
     */
    @Override
    public void deleteSniTrunkConfig(SnmpParam snmpParam, OltSniTrunkConfig oltSniTrunkConfig) {
        oltSniTrunkConfig.setSniTrunkGroupConfigRowstatus(RowStatus.DESTORY);
        snmpExecutorService.setData(snmpParam, oltSniTrunkConfig);
    }

    public SnmpExecutorService getSnmpExecutorService() {
        return snmpExecutorService;
    }

    public void setSnmpExecutorService(SnmpExecutorService snmpExecutorService) {
        this.snmpExecutorService = snmpExecutorService;
    }

}
