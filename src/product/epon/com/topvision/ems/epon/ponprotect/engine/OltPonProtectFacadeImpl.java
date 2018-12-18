/***********************************************************************
 * $Id: OltPonProtectFacadeImpl.java,v1.0 2013-10-25 下午3:34:37 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.ponprotect.engine;

import java.util.List;

import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.ems.epon.ponprotect.domain.OltPonProtectConfig;
import com.topvision.ems.epon.ponprotect.facade.OltPonProtectFacade;
import com.topvision.framework.snmp.RowStatus;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author flack
 * @created @2013-10-25-下午3:34:37
 *
 */
public class OltPonProtectFacadeImpl extends EmsFacade implements OltPonProtectFacade {
    private SnmpExecutorService snmpExecutorService;

    @Override
    public OltPonProtectConfig addPonProtect(SnmpParam param, OltPonProtectConfig ponprotect) {
        ponprotect.setTopPonPsRowstatus(RowStatus.CREATE_AND_GO);
        snmpExecutorService.setData(param, ponprotect);
        return snmpExecutorService.getTableLine(param, ponprotect);
    }

    @Override
    public OltPonProtectConfig modifyOltPonProtect(SnmpParam param, OltPonProtectConfig ponprotect) {
        // ponprotect.setTopPonPsRowstatus(RowStatus.CREATE_AND_GO); 修改端口的时候才需要 CREATE_AND_GO
        snmpExecutorService.setData(param, ponprotect);
        // TODO 如果set成功，但是get失败，这种情况是否会出现，如果出现了应该怎么处理？
        return snmpExecutorService.getTableLine(param, ponprotect);
    }

    @Override
    public void deletePonProtect(SnmpParam param, OltPonProtectConfig ponprotect) {
        ponprotect.setTopPonPsRowstatus(RowStatus.DESTORY);
        snmpExecutorService.setData(param, ponprotect);
    }

    @Override
    public List<OltPonProtectConfig> refreshOltPonProtect(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, OltPonProtectConfig.class);
    }

    /**
     * @return the snmpExecutorService
     */
    public SnmpExecutorService getSnmpExecutorService() {
        return snmpExecutorService;
    }

    /**
     * @param snmpExecutorService
     *            the snmpExecutorService to set
     */
    public void setSnmpExecutorService(SnmpExecutorService snmpExecutorService) {
        this.snmpExecutorService = snmpExecutorService;
    }
}
