/***********************************************************************
 * $Id: CmRemoteQueryFacadeImpl.java,v1.0 2014-1-27 上午9:28:06 $
 * 
 * @author: YangYi
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.remotequerycm.engine;

import java.util.List;

import javax.annotation.Resource;

import com.topvision.ems.cmc.cpe.domain.*;
import com.topvision.ems.cmc.remotequerycm.facade.CmRemoteQueryFacade;
import com.topvision.ems.cmc.remotequerycm.facade.domain.Cm2RemoteQuery;
import com.topvision.ems.cmc.remotequerycm.facade.domain.Cm3DsRemoteQuery;
import com.topvision.ems.cmc.remotequerycm.facade.domain.Cm3UsRemoteQuery;
import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.framework.annotation.Facade;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author YangYi
 * @created @2014-1-27-上午9:28:06
 * 
 */
@Facade("cmRemoteQueryFacade")
public class CmRemoteQueryFacadeImpl extends EmsFacade implements CmRemoteQueryFacade {
    @Resource(name = "snmpExecutorService")
    private SnmpExecutorService snmpExecutorService;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.remotequerycm.facade.CmRemoteQueryFacade#getCmSignal(com.topvision.
     * framework.snmp.SnmpParam, com.topvision.ems.cmc.remotequerycm.facade.domain.CmRemoteQuery)
     */
    @Override
    public Cm2RemoteQuery getCm2Signal(SnmpParam snmpParam, Cm2RemoteQuery cm2RemoteQuery) {
        return snmpExecutorService.getTableLine(snmpParam, cm2RemoteQuery);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.remotequerycm.facade.CmRemoteQueryFacade#getCm3DsSignal(com.topvision
     * .framework.snmp.SnmpParam,
     * com.topvision.ems.cmc.remotequerycm.facade.domain.Cm3DsRemoteQuery)
     */

    @Override
    public List<Cm3DsRemoteQuery> getCm3DsSignal(SnmpParam snmpParam, Cm3DsRemoteQuery cm3DsRemoteQuery) {
        return snmpExecutorService.getTableLines(snmpParam, cm3DsRemoteQuery, 1, Integer.MAX_VALUE);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.cmc.remotequerycm.facade.CmRemoteQueryFacade#getCm3DsSignalAll(com.topvision.framework.snmp.SnmpParam)
     */
    @Override
    public List<Cm3DsRemoteQuery> getCm3DsSignalAll(SnmpParam snmpParam) {    	
        return snmpExecutorService.getTable(snmpParam, Cm3DsRemoteQuery.class);
    }
    
    @Override
    public List<Cm3DsRemoteQuery> getCm3DsSignalAllOfSingleCC(SnmpParam snmpParam,Cm3DsRemoteQuery Cm3DsRemoteQuery) {    	
        return snmpExecutorService.getTableRangeLine(snmpParam, Cm3DsRemoteQuery, 1, 16);
    }
    
    @Override
    public List<Cm3UsRemoteQuery> getCm3UsSignalAllOfSingleCC(SnmpParam snmpParam,Cm3UsRemoteQuery Cm3UsRemoteQuery) {    	
        return snmpExecutorService.getTableRangeLine(snmpParam, Cm3UsRemoteQuery, 1, 4);
    }

   /* (non-Javadoc)
 * @see com.topvision.ems.cmc.remotequerycm.facade.CmRemoteQueryFacade#getCm3UsSignalAll(com.topvision.framework.snmp.SnmpParam)
 */
@Override
    public List<Cm3UsRemoteQuery> getCm3UsSignalAll(SnmpParam snmpParam) {    	
        return snmpExecutorService.getTable(snmpParam, Cm3UsRemoteQuery.class);
    }
    

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.remotequerycm.facade.CmRemoteQueryFacade#getCm3UsSignal(com.topvision
     * .framework.snmp.SnmpParam,
     * com.topvision.ems.cmc.remotequerycm.facade.domain.Cm3UsRemoteQuery)
     */
    @Override
    public List<Cm3UsRemoteQuery> getCm3UsSignal(SnmpParam snmpParam, Cm3UsRemoteQuery cm3UsRemoteQuery) {
        return snmpExecutorService.getTableLines(snmpParam, cm3UsRemoteQuery, 1, Integer.MAX_VALUE);
    }

    @Override
    public CmSystemInfo getCmSystemInfo(SnmpParam snmpParam, CmSystemInfo cmSystemInfo) {
        return snmpExecutorService.getTableLine(snmpParam, cmSystemInfo);
    }

    @Override
    public List<CmIfTable> getCmIfTable(SnmpParam snmpParam, CmIfTable cmIfTable) {
        return snmpExecutorService.getTableLines(snmpParam, cmIfTable, 1, Integer.MAX_VALUE);
    }

    @Override
    public List<CmUSInfo> getCmUSInfo(SnmpParam snmpParam, CmUSInfo cmUSInfo) {
        return snmpExecutorService.getTableLines(snmpParam, cmUSInfo, 1, Integer.MAX_VALUE);
    }

    @Override
    public List<CmDSInfo> getCmDSInfo(SnmpParam snmpParam, CmDSInfo cmDSInfo) {
        return snmpExecutorService.getTableLines(snmpParam, cmDSInfo, 1, Integer.MAX_VALUE);
    }

    @Override
    public List<CmUSInfoExt> getCmUSInfoExt(SnmpParam snmpParam, CmUSInfoExt cmUSInfoExt) {
        return snmpExecutorService.getTableLines(snmpParam, cmUSInfoExt, 1, Integer.MAX_VALUE);
    }

}
