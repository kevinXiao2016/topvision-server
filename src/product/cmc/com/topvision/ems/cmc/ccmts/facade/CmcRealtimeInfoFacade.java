/***********************************************************************
 * $ com.topvision.ems.cmc.ccmts.facade.CmcRealtimeInfoFacade,v1.0 14-5-11 下午2:55 $
 *
 * @author: root
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ccmts.facade;

import java.util.List;

import com.topvision.ems.cmc.ccmts.facade.domain.CmcCmNum;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcCpeTypeNum;
import com.topvision.ems.cmc.ccmts.facade.domain.CmtsCmQuality;
import com.topvision.ems.cmc.ccmts.facade.domain.DownChannelTxPower;
import com.topvision.ems.cmc.remotequerycm.facade.domain.Cm2RemoteQuery;
import com.topvision.ems.facade.Facade;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author root
 * @created @14-5-11-下午2:55
 */
@EngineFacade(serviceName = "cmcRealtimeInfoFacade", beanName = "cmcRealtimeInfoFacadeImpl")
public interface CmcRealtimeInfoFacade extends Facade {

    List<CmtsCmQuality> getCmtsCmQualitys(SnmpParam snmpParam);

    List<Cm2RemoteQuery> getCm2RemoteQuerys(SnmpParam snmpParam);

    List<DownChannelTxPower> getDownChannelTxPowers(SnmpParam snmpParam);

    CmcCpeTypeNum getCmcCpeTypeNum(SnmpParam snmpParam, Long cmcIndex);

    CmcCmNum getCmcCmNum(SnmpParam snmpParam);

    String[] getCmcSnr(SnmpParam snmpParam, String[] oids);

    /**
     * 实时刷新时, 刷新前改成实时模式 再去刷新 完成后再改成轮询模式
     * 
     * @param snmpParam
     * @param state
     */
    void setRealTimeSnmpDataStatus(SnmpParam snmpParam, String state);
}
