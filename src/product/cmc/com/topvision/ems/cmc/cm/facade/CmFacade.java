/***********************************************************************
 * $Id: CmFacade.java,v1.0 2012-2-1 下午05:58:09 $
 * 
 * @author: Administrator
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.cm.facade;

import java.util.List;

import com.topvision.ems.cmc.cm.domain.RealtimeCm;
import com.topvision.ems.cmc.cm.domain.RealtimeCpe;
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
import com.topvision.ems.facade.Facade;
import com.topvision.ems.facade.domain.IfTable;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author Administrator
 * @created @2012-2-1-下午05:58:09
 * 
 */
@EngineFacade(serviceName = "CmFacade", beanName = "cmFacade")
public interface CmFacade extends Facade {
    /**
     * 实时查询CM的状态信息
     * 
     * @param snmpParam
     *            SnmpParam
     * @return CmStatus
     */
    CmStatus showCmStatus(SnmpParam snmpParam);

    /**
     * CM重启
     * 
     * @param snmpParam
     *            SnmpParam
     */
    void resetCm(SnmpParam snmpParam);

    /**
     * 获取指定索引的CM信息
     * 
     * @param snmpParam
     *            SnmpParam
     * @param cmIndex
     *            Long
     * @param cmMac
     *            String
     * @return CmAttribute
     */
    CmAttribute getCmAttributeOnDol(SnmpParam snmpParam, Long cmIndex, String cmMac);

    /**
     * 获取指定范围的CM信息
     *
     * @param snmpParam
     *            SnmpParam
     *            @param startIndex
     *            @param endIndex
     * @return List<CmAttribute>
     */
    List<CmAttribute> getCmAttributeInfos(SnmpParam snmpParam, Long startIndex, Long endIndex);

    /**
     * 获取OLT上的CM信息
     *
     * @param snmpParam
     *            SnmpParam
     * @return List<CmAttribute>
     */
    List<CmAttribute> getCmAttributeInfos(SnmpParam snmpParam);

    /**
     * 获取cm上行射频信息
     * 
     * @param snmpParam
     * @return
     */
    List<DocsIfUpstreamChannel> getDocsIfUpstreamChannelList(SnmpParam snmpParam);

    /**
     * 获取cm下行射频信息
     * 
     * @param snmpParam
     * @return
     */
    List<DocsIfDownstreamChannel> getDocsIfDownstreamChannel(SnmpParam snmpParam);

    /**
     * 获取cm下行信号质量信息
     * 
     * @param snmpParam
     * @return
     */
    List<DocsIfSignalQuality> getDocsIfSignalQuality(SnmpParam snmpParam);

    /**
     * 获取3.0上行质量信号信息
     * 
     * @param cms
     * @param snmpParam
     * @return
     */
    List<DocsIf3CmtsCmUsStatus> getCmUsStatus(List<CmAttribute> cms, SnmpParam snmpParam);

    /**
     * 通过ifindex获取信道带宽
     * 
     * @param snmpParam
     * @param ifIndex
     * @return
     */
    Long getIfSpeed(SnmpParam snmpParam, Long ifIndex);

    void clearCpe(SnmpParam snmpParam, String cpeMac);

    /**
     * 获取大客户IP信息
     * 
     * @param snmpParam
     * @return
     */
    List<CmStaticIp> getCmStaticIp(SnmpParam snmpParam);

    /**
     * 获取CMCPE信息
     * 
     * @param snmpParam
     * @return
     */
    List<CmCpe> getCmCpe(SnmpParam snmpParam);

    /**
     * 获取3.0CM上行信道信息
     * 
     * @param snmpParam
     * @return
     */
    List<DocsIf3CmtsCmUsStatus> getDocsIf3CmtsCmUsStatusList(SnmpParam snmpParam);

    /**
     * 通过CMMac实时获取CmIndex
     * 
     * @param snmpParam
     * @return
     */
    Long getCmIndexByCmMac(SnmpParam snmpParam, String cmMac);

    /**
     * 获取实时CM列表
     * 
     * @param snmpParam
     * @return
     */
    List<RealtimeCm> getRealtimeCm(SnmpParam snmpParam);
    
    /**
     * 获取单个cc实时CM列表
     * 
     * @param snmpParam
     * @return
     */
    List<RealtimeCm> getRealtimeCmOfSingleCC(SnmpParam snmpParam,Long cmcIndex,Long cmcNextIndex);
    
    /**
     * 
     * @param
     * @return List<DocsIf3CmtsCmUsStatus>
     */
    List<DocsIf3CmtsCmUsStatus> getDocsIf3CmtsCmUsStatusListOfCC(SnmpParam snmpParam,DocsIf3CmtsCmUsStatus DocsIf3CmtsCmUsStatus);

    /**
     * @param
     * @return List<Cm3DsRemoteQuery>
     */
    List<Cm3DsRemoteQuery> getCm3DsSignalOfCm(SnmpParam snmpParam,Cm3DsRemoteQuery Cm3DsRemoteQuery);
    /**
     * @param
     * @return List<Cm3UsRemoteQuery>
     */
    List<Cm3UsRemoteQuery> getCm3UsSignalOfCm(SnmpParam snmpParam,Cm3UsRemoteQuery Cm3UsRemoteQuery);
    /**
     * 获取实时CPE列表
     * 
     * @param snmpParam
     * @return
     */
    List<RealtimeCpe> getCpeListByCmIndex(SnmpParam snmpParam, Long cmIndex);

    /**
     * @param snmpParam
     * @param cmMac
     * @return
     */
    CmCpe getCpeByCpeMac(SnmpParam snmpParam, String cmMac);

    /**
     * 查询CM的定位信息
     * @param snmpParam
     * @param oid
     * @return
     */
    String getCmLocate(SnmpParam snmpParam, String oid);

    /**
     * 查询CM的IP
     * @param snmpParam
     * @param oid
     * @return
     */
    String getCmIpAddress(SnmpParam snmpParam, String oid);

    /**
     * 通过cmc重启某个CM
     * @param snmpParam
     * @param cmIndex
     */
    void resetCmFromCmc(SnmpParam snmpParam, Long cmIndex);

    /**
     * 判断CM是否在线
     * @param snmpParam
     * @param result
     * @return
     */
    boolean isCmOnline(SnmpParam snmpParam, String result);

    /**
     * 获取CM离线前状态
     * @param snmpParam
     * @param cmcIndex
     * @param cmIndex
     * @return
     */
    TopCmControl getCmPreStatusOnCmts(SnmpParam snmpParam, Long cmcIndex, Long cmIndex);

    /**
     * 在CC上通过CMINdex获取cm下的服务流信息
     * @param snmpParam
     * @param cmIndex
     * @return
     */
    List<CmServiceFlow> getCmServiceFlowByCmIndex(SnmpParam snmpParam, Long cmIndex);

    /**
     * 获取CM的接口信息
     * @param snmpParam
     * @return
     */
    List<IfTable> getIfTable(SnmpParam snmpParam);

    /**
     * 获取CM的CmStatusTable
     * @param snmpParam
     * @param cmStatusTable
     * @return
     */
    CmStatusTable getCmStatusTable(SnmpParam snmpParam, CmStatusTable cmStatusTable);

    List<CmFdbTableRemoteQuery> getCmFdbAddressByCmIndex(SnmpParam snmpParam, Long cmIndex);

    List<CmFdbTable> getFdbTable(SnmpParam snmpParam);

    void checkCmSnmp(SnmpParam snmpParam);
    
    /**
     * 清除单个CM
     * @param
     * @return void
     */
    void clearSingleCmFromCmc(SnmpParam snmpParam, Long cmIndex);
}