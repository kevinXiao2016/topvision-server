package com.topvision.ems.epon.ofa.facade;

import java.util.List;

import com.topvision.ems.epon.ofa.facade.domain.OfaBasicInfo;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

@EngineFacade(serviceName = "OfaBasicInfoFacade", beanName = "ofaBasicInfoFacade")
public interface OfaBasicInfoFacade {

    /**
     * 修改基本信息
     * 
     * @param snmpParam
     * @param topOFABasicInfoEntry
     * @return
     */
	OfaBasicInfo modifyOfaBasicInfo(SnmpParam snmpParam, OfaBasicInfo ofaBasicInfo);

    /**
     * 获取基本信息
     * 
     * @param snmpParam
     * @return
     */
	List<OfaBasicInfo> getOfaBasicInfo(SnmpParam snmpParam);

}
