package com.topvision.ems.epon.ofa.engine;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.ems.epon.ofa.facade.CommonOptiTransFacade;
import com.topvision.ems.epon.ofa.facade.domain.CommonOptiTrans;
import com.topvision.framework.annotation.Facade;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;

@Facade("commonOptiTransFacade")
public class CommonOptiTransFacadeImpl extends EmsFacade implements CommonOptiTransFacade {

    @Autowired
    private SnmpExecutorService snmpExecutorService;

    @Override
    public CommonOptiTrans modifyCommonOptiTrans(SnmpParam snmpParam,
            CommonOptiTrans commonOptiTrans) {
        return snmpExecutorService.setData(snmpParam, commonOptiTrans);
    }

    @Override
    public List<CommonOptiTrans> getCommonOptiTrans(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, CommonOptiTrans.class);
    }

}
