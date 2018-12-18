/***********************************************************************
 * $Id: CmRemoteQueryFacade.java,v1.0 2014-1-27 上午9:26:04 $
 * 
 * @author: YangYi
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.remotequerycm.facade;

import java.util.List;

import com.topvision.ems.cmc.cpe.domain.*;
import com.topvision.ems.cmc.remotequerycm.facade.domain.Cm3DsRemoteQuery;
import com.topvision.ems.cmc.remotequerycm.facade.domain.Cm3UsRemoteQuery;
import com.topvision.ems.cmc.remotequerycm.facade.domain.Cm2RemoteQuery;
import com.topvision.ems.facade.Facade;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author YangYi
 * @created @2014-1-27-上午9:26:04
 *
 */
@EngineFacade(serviceName = "CmRemoteQueryFacade", beanName = "cmRemoteQueryFacade")
public interface CmRemoteQueryFacade extends Facade {

    /**
     * 使用Remote Query方式查询CM 2.0实时信号
     * 
     * @param snmpParam
     * @param cmRemoteQuery
     * @return
     */
    Cm2RemoteQuery getCm2Signal(SnmpParam snmpParam, Cm2RemoteQuery cmRemoteQuery);

    /**
     * 用Remote Query方式查询CM 3.0 下行信道实时信号
     * 
     * @param snmpParam
     * @param cm3DsRemoteQuery
     * @return
     */
    List<Cm3DsRemoteQuery> getCm3DsSignal(SnmpParam snmpParam, Cm3DsRemoteQuery cm3DsRemoteQuery);

    /**
     * 用Remote Query方式查询CM 3.0 上行信道实时信号
     * 
     * @param snmpParam
     * @param cm3DsRemoteQuery
     * @return
     */
    List<Cm3UsRemoteQuery> getCm3UsSignal(SnmpParam snmpParam, Cm3UsRemoteQuery cm3UsRemoteQuery);

    /**
     * 用RemoteQuery获取CmSystemInfo
     * @param snmpParam
     * @param cmSystemInfo
     * @return
     */
    CmSystemInfo getCmSystemInfo(SnmpParam snmpParam, CmSystemInfo cmSystemInfo);

    /**
     * 用RemoteQuery获取CmIfTable
     * @param snmpParam
     * @param cmIfTable
     * @return
     */
    List<CmIfTable> getCmIfTable(SnmpParam snmpParam, CmIfTable cmIfTable);

    /**
     * 用RemoteQuery获取CmUSInfo
     * @param snmpParam
     * @param cmUSInfo
     * @return
     */
    List<CmUSInfo> getCmUSInfo(SnmpParam snmpParam, CmUSInfo cmUSInfo);

    /**
     * 用RemoteQuery获取CmDSInfo
     * @param snmpParam
     * @param cmDSInfo
     * @return
     */
    List<CmDSInfo> getCmDSInfo(SnmpParam snmpParam, CmDSInfo cmDSInfo);

    /**
     * 用RemoteQuery获取CmUSInfoExt
     * @param entitySnmpParam
     * @param cmUSInfoExt
     * @return
     */
    List<CmUSInfoExt> getCmUSInfoExt(SnmpParam entitySnmpParam, CmUSInfoExt cmUSInfoExt);
    /**
     * 用Remote Query方式查询所有CM 3.0 上行信道实时信号
     * 
     * @param snmpParam
     * @param cm3DsRemoteQuery
     * @return
     */
    List<Cm3UsRemoteQuery> getCm3UsSignalAll(SnmpParam snmpParam);
    
    /**
     * 用Remote Query方式查询所有CM 3.0 下行信道实时信号
     * 
     * @param snmpParam
     * @param cm3DsRemoteQuery
     * @return
     */
    List<Cm3DsRemoteQuery> getCm3DsSignalAll(SnmpParam snmpParam);
    
    /**
     * @param
     * @return List<Cm3DsRemoteQuery>
     */
    List<Cm3DsRemoteQuery> getCm3DsSignalAllOfSingleCC(SnmpParam snmpParam,Cm3DsRemoteQuery Cm3DsRemoteQuery);
    /**
     * @param
     * @return List<Cm3DsRemoteQuery>
     */
    List<Cm3UsRemoteQuery> getCm3UsSignalAllOfSingleCC(SnmpParam snmpParam,Cm3UsRemoteQuery Cm3UsRemoteQuery);
    
}
