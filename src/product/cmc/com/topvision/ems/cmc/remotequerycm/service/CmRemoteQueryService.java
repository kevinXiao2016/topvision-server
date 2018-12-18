/***********************************************************************
 * $Id: CmRemoteQueryFacade.java,v1.0 2014-1-27 上午9:26:04 $
 * 
 * @author: YangYi
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.remotequerycm.service;

import java.util.List;

import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.cmc.facade.domain.CmStatus;
import com.topvision.ems.cmc.remotequerycm.facade.domain.Cm2RemoteQuery;
import com.topvision.ems.cmc.remotequerycm.facade.domain.Cm3DsRemoteQuery;
import com.topvision.ems.cmc.remotequerycm.facade.domain.Cm3UsRemoteQuery;
import com.topvision.framework.service.Service;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author YangYi
 * @created @2014-1-27-上午9:26:04
 *
 */
public interface CmRemoteQueryService extends Service {

    /**
     * 使用RemoteQuery的方式查询CM实时信号
     * 
     * @param cmId
     * @return
     */
    CmAttribute remoteQueryCmSignal(CmAttribute cmAttribute);

    /**
     * 使用RemoteQuery的方式查询CM上下行信道信息
     * 
     * @param cmId
     * @return
     */
    CmStatus remoteQueryCmChanInfos(String cmIp, Long cmId);

    Cm2RemoteQuery getCmSignal(SnmpParam snmpParam, CmAttribute cmAttribute);

    List<Cm3DsRemoteQuery> getCm3DsRemoteQuery(SnmpParam snmpParam, CmAttribute cmAttribute);

    List<Cm3UsRemoteQuery> getCm3UsRemoteQuery(SnmpParam snmpParam, CmAttribute cmAttribute);

}
