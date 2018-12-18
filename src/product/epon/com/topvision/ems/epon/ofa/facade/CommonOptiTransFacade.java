package com.topvision.ems.epon.ofa.facade;

import java.util.List;

import com.topvision.ems.epon.ofa.facade.domain.CommonOptiTrans;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

@EngineFacade(serviceName = "CommonOptiTransFacade", beanName = "commonOptiTransFacade")
public interface CommonOptiTransFacade {
	
	/**
     * 修改基本信息
     * 
     * @return
     */
	CommonOptiTrans modifyCommonOptiTrans(SnmpParam snmpParam,
    		CommonOptiTrans commonOptiTrans);

    /**
     * 获取基本信息
     * 
     * @param snmpParam
     * @return
     */
	List<CommonOptiTrans> getCommonOptiTrans(SnmpParam snmpParam);
}
