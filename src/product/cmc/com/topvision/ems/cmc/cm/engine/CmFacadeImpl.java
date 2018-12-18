/***********************************************************************
 * $Id: CmFacadeImpl.java,v1.0 2012-2-1 下午06:04:15 $
 *
 * @author: Administrator
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.cm.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.topvision.ems.cmc.cm.domain.CMMacToIndex;
import com.topvision.ems.cmc.cm.domain.RealtimeCm;
import com.topvision.ems.cmc.cm.domain.RealtimeCpe;
import com.topvision.ems.cmc.cm.facade.CmFacade;
import com.topvision.ems.cmc.cpe.domain.CmFdbTable;
import com.topvision.ems.cmc.cpe.domain.CmFdbTableRemoteQuery;
import com.topvision.ems.cmc.cpe.domain.CmServiceFlow;
import com.topvision.ems.cmc.cpe.domain.CmStatusTable;
import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.cmc.facade.domain.CmCpe;
import com.topvision.ems.cmc.facade.domain.CmStaticIp;
import com.topvision.ems.cmc.facade.domain.CmStatus;
import com.topvision.ems.cmc.facade.domain.DocsIf3CmtsCmUsStatus;
import com.topvision.ems.cmc.facade.domain.DocsIfDownstreamChannel;
import com.topvision.ems.cmc.facade.domain.DocsIfSignalQuality;
import com.topvision.ems.cmc.facade.domain.DocsIfUpstreamChannel;
import com.topvision.ems.cmc.performance.domain.TopCmControl;
import com.topvision.ems.cmc.remotequerycm.facade.domain.Cm3DsRemoteQuery;
import com.topvision.ems.cmc.remotequerycm.facade.domain.Cm3UsRemoteQuery;
import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.ems.facade.domain.IfTable;
import com.topvision.framework.annotation.Facade;
import com.topvision.framework.domain.PhysAddress;
import com.topvision.framework.snmp.RowStatus;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.utils.CmdDiskSpace;

/**
 * @author Administrator
 * @created @2012-2-1-下午06:04:15
 */
@Facade("cmFacade")
public class CmFacadeImpl extends EmsFacade implements CmFacade {
    @Resource(name = "snmpExecutorService")
    private SnmpExecutorService snmpExecutorService;

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.facade.CmFacade#showCmStatus(java.lang.String)
     */
    @Override
    public CmStatus showCmStatus(SnmpParam snmpParam) {
        return snmpExecutorService.getData(snmpParam, CmStatus.class);
    }

    public SnmpExecutorService getSnmpExecutorService() {
        return snmpExecutorService;
    }

    public void setSnmpExecutorService(SnmpExecutorService snmpExecutorService) {
        this.snmpExecutorService = snmpExecutorService;
    }

    @Override
    public void resetCm(SnmpParam snmpParam) {
        String oid = "1.3.6.1.2.1.69.1.1.3.0";// docsDevResetNow--DOCS-CABLE-DEVICE-MIB
        snmpExecutorService.set(snmpParam, oid, "1");
    }

    @Override
    public CmAttribute getCmAttributeOnDol(SnmpParam snmpParam, Long cmIndex, String cmMac) {
        CmAttribute cmAttribute = new CmAttribute();
        cmAttribute.setStatusIndex(cmIndex);
        try {
            snmpExecutorService.getTableLine(snmpParam, cmAttribute);
        } catch (Exception e1) {
            logger.error("", e1);
            return null;
        }
        if (cmAttribute.getStatusMacAddress() != null && cmAttribute.getStatusMacAddress().equals(cmMac)) {
            return cmAttribute;
        } else {
            for (int i = 1; i < 500; i++) {
                try {
                    // 根据cm所在ccmts的index，按定义的索引中CM ID递增顺序取出cm的信息
                    cmAttribute.setStatusIndex((cmIndex & 0xFFFF0000) | (i << 6));
                    snmpExecutorService.getTableLine(snmpParam, cmAttribute);
                } catch (Exception e) {
                    // 如果在CCMTS上已经不存在指定索引的CM信息，则跳出循环
                    logger.error("", e);
                    break;
                }
                if (cmAttribute.getStatusMacAddress().equals(cmMac)) {
                    return cmAttribute;
                }
            }
        }
        return null;
    }

    @Override
    public List<CmAttribute> getCmAttributeInfos(SnmpParam snmpParam, Long startIndex, Long endIndex) {
        return snmpExecutorService.getTableRangeLine(snmpParam, new CmAttribute(), startIndex, endIndex);
    }

    @Override
    public List<CmAttribute> getCmAttributeInfos(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, CmAttribute.class);
    }

    @Override
    public List<RealtimeCm> getRealtimeCm(SnmpParam snmpParam) {
        List<RealtimeCm> list = snmpExecutorService.getTable(snmpParam, RealtimeCm.class);
        return list;
    }
    
    @Override
    public List<RealtimeCm> getRealtimeCmOfSingleCC(SnmpParam snmpParam,Long cmcIndex,Long cmcNextIndex) {
        List<RealtimeCm> list = snmpExecutorService.getTableRangeLine(snmpParam, new RealtimeCm() ,cmcIndex,cmcNextIndex);
        return list;
    }
    
    public List<DocsIf3CmtsCmUsStatus> getDocsIf3CmtsCmUsStatusListOfCC(SnmpParam snmpParam,DocsIf3CmtsCmUsStatus DocsIf3CmtsCmUsStatus){
    	return snmpExecutorService.getTableRangeLine(snmpParam, DocsIf3CmtsCmUsStatus,1,Long.MAX_VALUE);
    }
    
    public List<Cm3DsRemoteQuery> getCm3DsSignalOfCm(SnmpParam snmpParam,Cm3DsRemoteQuery Cm3DsRemoteQuery){
    	return snmpExecutorService.getTableRangeLine(snmpParam, Cm3DsRemoteQuery, 1, 16);
    }
    
    public List<Cm3UsRemoteQuery> getCm3UsSignalOfCm(SnmpParam snmpParam,Cm3UsRemoteQuery Cm3UsRemoteQuery){
    	return snmpExecutorService.getTableRangeLine(snmpParam, Cm3UsRemoteQuery, 1, 4);
    }

    @Override
    public List<DocsIfUpstreamChannel> getDocsIfUpstreamChannelList(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, DocsIfUpstreamChannel.class);
    }

    @Override
    public List<DocsIfDownstreamChannel> getDocsIfDownstreamChannel(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, DocsIfDownstreamChannel.class);
    }

    @Override
    public List<DocsIfSignalQuality> getDocsIfSignalQuality(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, DocsIfSignalQuality.class);
    }

    @Override
    public List<DocsIf3CmtsCmUsStatus> getCmUsStatus(List<CmAttribute> cms, SnmpParam snmpParam) {
        List<DocsIf3CmtsCmUsStatus> cmUses = new ArrayList<DocsIf3CmtsCmUsStatus>();
        List<DocsIf3CmtsCmUsStatus> collecteds = snmpExecutorService.getTable(snmpParam, DocsIf3CmtsCmUsStatus.class);
        for (CmAttribute cm : cms) {
            for (DocsIf3CmtsCmUsStatus collected : collecteds) {
                if (cm.getStatusIndex().equals(collected.getCmRegStatusId())) {
                    cmUses.add(collected);
                }
            }

        }
        return cmUses;
    }

    @Override
    public Long getIfSpeed(SnmpParam snmpParam, Long ifIndex) {
        return Long.parseLong(snmpExecutorService.get(snmpParam, "1.3.6.1.2.1.2.2.1.5." + ifIndex.toString()));
    }

    @Override
    public void clearCpe(SnmpParam snmpParam, String cpeMac) {
        CmCpe cpe = new CmCpe();
        PhysAddress phy = new PhysAddress(cpeMac);
        cpe.setTopCmCpeMacAddress(phy);
        cpe.setTopCmCpeStatus(RowStatus.DESTORY);
        snmpExecutorService.setData(snmpParam, cpe);
    }

    @Override
    public List<CmStaticIp> getCmStaticIp(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, CmStaticIp.class);
    }

    @Override
    public List<CmCpe> getCmCpe(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, CmCpe.class);
    }

    @Override
    public List<DocsIf3CmtsCmUsStatus> getDocsIf3CmtsCmUsStatusList(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, DocsIf3CmtsCmUsStatus.class);
    }

    @Override
    public Long getCmIndexByCmMac(SnmpParam snmpParam, String cmMac) {
        CMMacToIndex cmMacToIndex = new CMMacToIndex();
        cmMacToIndex.setMac(new PhysAddress(cmMac));
        snmpExecutorService.getTableLine(snmpParam, cmMacToIndex);
        return cmMacToIndex.getCmIndex();
    }

    @Override
    public List<RealtimeCpe> getCpeListByCmIndex(SnmpParam snmpParam, Long cmIndex) {
        RealtimeCpe realtimeCpe = new RealtimeCpe();
        realtimeCpe.setTopCmIndex(cmIndex);
        List<RealtimeCpe> realtimeCpes = snmpExecutorService
                .getTableLines(snmpParam, realtimeCpe, 1, Integer.MAX_VALUE);
        return realtimeCpes;
    }

    @Override
    public CmCpe getCpeByCpeMac(SnmpParam snmpParam, String cmMac) {
        CmCpe cmCpe = new CmCpe();
        PhysAddress physAddress = new PhysAddress(cmMac);
        cmCpe.setTopCmCpeMacAddress(physAddress);
        snmpExecutorService.getTableLine(snmpParam, cmCpe);
        return cmCpe;
    }

    @Override
    public String getCmLocate(SnmpParam snmpParam, String oid) {
        String $oid = "1.3.6.1.2.1.10.127.1.3.7.1.2";
        return snmpExecutorService.get(snmpParam, $oid + oid);
    }

    @Override
    public String getCmIpAddress(SnmpParam snmpParam, String oid) {
        String $oid = "1.3.6.1.2.1.10.127.1.3.3.1.20.";
        if ("1".equals(snmpExecutorService.get(snmpParam, $oid + oid))) {//IPV4
            $oid = "1.3.6.1.2.1.10.127.1.3.3.1.3.";
        } else {//IPV6
            $oid = "1.3.6.1.2.1.10.127.1.3.3.1.21.";
        }
        return snmpExecutorService.get(snmpParam, $oid + oid);
    }

    @Override
    public void resetCmFromCmc(SnmpParam snmpParam, Long cmIndex) {
        logger.info("resetCmFromCmc snmpParam:" + snmpParam + " cmIndex:" + cmIndex);
        String oid = "1.3.6.1.4.1.32285.11.1.1.2.2.5.1.1." + cmIndex;
        snmpExecutorService.set(snmpParam, oid, "1");
    }
    
    @Override
    public void clearSingleCmFromCmc(SnmpParam snmpParam, Long cmIndex) {
        logger.info("clearSingleCmFromCmc snmpParam:" + snmpParam + " cmIndex:" + cmIndex);
        String oid = "1.3.6.1.4.1.32285.11.1.1.2.2.5.1.6." + cmIndex;
        snmpExecutorService.set(snmpParam, oid, "1");
    }
    /* (non-Javadoc)
     * @see com.topvision.ems.cmc.cm.facade.CmFacade#isCmOnline(com.topvision.framework.snmp.SnmpParam, java.lang.String)
     */
    @Override
    public boolean isCmOnline(SnmpParam snmpParam, String cmIndex) {
        String oid = "1.3.6.1.2.1.10.127.1.3.3.1.9." + cmIndex;
        String registerAndCompelte = "6";
        return registerAndCompelte.equals(snmpExecutorService.get(snmpParam, oid));
    }

    @Override
    public TopCmControl getCmPreStatusOnCmts(SnmpParam snmpParam, Long cmcIndex, Long cmIndex) {
        TopCmControl topCmControl = new TopCmControl();
        topCmControl.setStatusIndex(cmIndex);
        return snmpExecutorService.getTableLine(snmpParam, topCmControl);
    }

    @Override
    public List<CmServiceFlow> getCmServiceFlowByCmIndex(SnmpParam snmpParam, Long cmIndex) {
        CmServiceFlow cmServiceFlow = new CmServiceFlow();
        cmServiceFlow.setCmIndex(cmIndex);
        List<CmServiceFlow> cmsf = snmpExecutorService
                .getTableLines(snmpParam, cmServiceFlow, 1, Integer.MAX_VALUE);
        return cmsf;
    }

    @Override
    public List<IfTable> getIfTable(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam,IfTable.class);
    }

    @Override
    public CmStatusTable getCmStatusTable(SnmpParam snmpParam, CmStatusTable cmStatusTable) {
        return snmpExecutorService.getTableLine(snmpParam,cmStatusTable);
    }

    @Override
    public List<CmFdbTableRemoteQuery> getCmFdbAddressByCmIndex(SnmpParam snmpParam, Long cmIndex) {
        CmFdbTableRemoteQuery cmFdbAddress = new CmFdbTableRemoteQuery();
        cmFdbAddress.setCmIndex(cmIndex);
        List<CmFdbTableRemoteQuery> cmFdbAddressList = snmpExecutorService
                .getTableLines(snmpParam, cmFdbAddress, 0, Integer.MAX_VALUE);
        return cmFdbAddressList;
    }

    @Override
    public List<CmFdbTable> getFdbTable(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam,CmFdbTable.class);
    }

    @Override
    public void checkCmSnmp(SnmpParam snmpParam) {
        snmpExecutorService.get(snmpParam,"1.3.6.1.2.1.1.1.0");
    }

}