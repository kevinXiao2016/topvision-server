/***********************************************************************
 * $Id: CmRemoteQueryServiceImpl.java,v1.0 2014-1-27 上午10:50:03 $
 * 
 * @author: YangYi
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.remotequerycm.service.impl;

import java.util.*;

import javax.annotation.Resource;

import com.topvision.ems.cmc.cpe.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.ems.cmc.cm.dao.CmDao;
import com.topvision.ems.cmc.cm.dao.CmListDao;
import com.topvision.ems.cmc.cm.domain.CmSignal;
import com.topvision.ems.cmc.cm.service.CmService;
import com.topvision.ems.cmc.domain.CmTopologyInfo;
import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.cmc.facade.domain.CmStatus;
import com.topvision.ems.cmc.facade.domain.CmcDownChannelBaseInfo;
import com.topvision.ems.cmc.facade.domain.DocsIf3CmtsCmUsStatus;
import com.topvision.ems.cmc.facade.domain.DocsIfDownstreamChannel;
import com.topvision.ems.cmc.facade.domain.DocsIfSignalQuality;
import com.topvision.ems.cmc.facade.domain.DocsIfUpstreamChannel;
import com.topvision.ems.cmc.remotequerycm.dao.CmRemoteQueryDao;
import com.topvision.ems.cmc.remotequerycm.facade.domain.Cm2RemoteQuery;
import com.topvision.ems.cmc.remotequerycm.facade.domain.Cm3DsRemoteQuery;
import com.topvision.ems.cmc.remotequerycm.facade.domain.Cm3UsRemoteQuery;
import com.topvision.ems.cmc.remotequerycm.service.CmRemoteQueryService;
import com.topvision.ems.cmc.service.impl.CmcBaseCommonService;
import com.topvision.ems.cmc.upchannel.facade.domain.CmcUpChannelBaseInfo;
import com.topvision.ems.performance.dao.PerfThresholdDao;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author YangYi
 * @created @2014-1-27-上午10:50:03
 * 
 */
@Service("cmRemoteQueryService")
public class CmRemoteQueryServiceImpl extends CmcBaseCommonService implements CmRemoteQueryService {
    @Resource(name = "cmListDao")
    private CmListDao cmListDao;
    @Resource(name = "cmDao")
    private CmDao cmDao;
    @Resource(name = "perfThresholdDao")
    private PerfThresholdDao perfThresholdDao;
    @Resource(name = "cmRemoteQueryDao")
    private CmRemoteQueryDao cmRemoteQueryDao;
    @Autowired
    private EntityTypeService entityTypeService;
    @Resource(name = "cmcService")
    private CmcService cmcService;
    @Resource(name = "cmService")
    private CmService cmService;
    private Integer cmType = 2; // 2.0和3.0 CM标识

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.remotequerycm.service.CmRemoteQueryService#
     * remoteQueryCmChanInfos(java .lang.String, java.lang.Long)
     */
    @Override
    public CmStatus remoteQueryCmChanInfos(String cmIp, Long cmId) {
        CmAttribute cmAttribute = this.cmDao.getCmAttributeByCmId(cmId);
        Long entityId = getEntityIdByCmcId(cmAttribute.getCmcId());
        SnmpParam entitySnmpParam = getSnmpParamByEntityId(entityId);
        cmAttribute = getCmcFacade(entitySnmpParam.getIpAddress()).getCmAttribute(entitySnmpParam,cmAttribute);
        entitySnmpParam.setMibs("TOPVISION-CCMTS-MIB");
        // TODO 此处需要判断该CM是2.0还是3.0
        CmStatus cmStatus;
        if (cmType == 2) { // CM 2.0
            cmStatus = remoteQueryCm2ChanInfos(cmAttribute, entitySnmpParam);
        } else if (cmType == 3) {// CM 3.0
            cmStatus = remoteQueryCm3ChanInfos(cmAttribute, entitySnmpParam);
        } else {
            cmStatus = new CmStatus();
        }
        cmStatus.setCmId(cmId);
        cmStatus.setStatusValue(cmAttribute.getStatusValue());
        if (!cmStatus.getRemoteQueryState()) {
            return cmStatus;
        }
        try {
            CmSystemInfo cmSystemInfo = new CmSystemInfo();
            cmSystemInfo.setCmIndex(cmAttribute.getStatusIndex());
            cmSystemInfo = getCmRemoteQueryFacade(entitySnmpParam.getIpAddress()).getCmSystemInfo(entitySnmpParam, cmSystemInfo);
            cmStatus.setSysDescr(cmSystemInfo.getSysDesc());
            cmStatus.setDocsIfCmStatusResets(cmSystemInfo.getResets());
            cmStatus.setSysUpTime(cmSystemInfo.getUpTime());
        } catch (Exception e) {
            logger.debug("",e);
        }

        try {
            CmIfTable cmIfTable = new CmIfTable();
            cmIfTable.setCmIndex(cmAttribute.getStatusIndex());
            List<CmIfTable> ifTables = getCmRemoteQueryFacade(entitySnmpParam.getIpAddress()).getCmIfTable(entitySnmpParam,cmIfTable);
            cmStatus.setCmIfTables(ifTables);
        } catch (Exception e) {
            logger.debug("",e);
        }
        try {
            CmSystemInfoExt cmSystemInfoExt = getCmcFacade(entitySnmpParam.getIpAddress()).
                    getCmSystemInfoExtByCmIndex(entitySnmpParam, cmAttribute.getStatusIndex());
            cmStatus.setCmHardWare(cmSystemInfoExt.getCmHardWare());
            cmStatus.setCmSoftWare(cmSystemInfoExt.getCmSoftWare());
        } catch (Exception e) {
            logger.debug("", e);
        }
        try {
            CmUSInfo cmUSInfo = new CmUSInfo();
            cmUSInfo.setCmIndex(cmAttribute.getStatusIndex());
            List<CmUSInfo> cmUSInfos = getCmRemoteQueryFacade(entitySnmpParam.getIpAddress()).getCmUSInfo(entitySnmpParam, cmUSInfo);
            Map<Integer,CmUSInfo>  cmUSInfoMap = new HashMap<>();
            for (CmUSInfo usInfo : cmUSInfos) {
                cmUSInfoMap.put(usInfo.getDocsIfUpChannelId().intValue(),usInfo);
            }
            CmUSInfoExt cmUSInfoExt = new CmUSInfoExt();
            cmUSInfoExt.setCmIndex(cmAttribute.getStatusIndex());
            List<CmUSInfoExt> cmUSInfoExts = getCmRemoteQueryFacade(entitySnmpParam.getIpAddress()).getCmUSInfoExt(entitySnmpParam, cmUSInfoExt);

            Map<Integer,CmUSInfoExt> cmUSInfoExtMap = new HashMap<>();
            for (CmUSInfoExt usInfoExt : cmUSInfoExts) {
                cmUSInfoExtMap.put(usInfoExt.getDocsIfUpChannelId().intValue(),usInfoExt);
            }
            List<DocsIfUpstreamChannel> upstreamChannels = cmStatus.getDocsIfUpstreamChannelList();
            for (DocsIfUpstreamChannel upstreamChannel :upstreamChannels) {
                CmUSInfo usInfo = cmUSInfoMap.get(upstreamChannel.getDocsIfUpChannelId().intValue());
                if (usInfo == null) {
                    upstreamChannel.setUsBps(-1L);
                } else {
                    upstreamChannel.setUsBps(usInfo.getUsBps());
                }
                CmUSInfoExt usInfoExt = cmUSInfoExtMap.get(upstreamChannel.getDocsIfUpChannelId().intValue());
                if (usInfoExt == null) {
                    upstreamChannel.setUsErrorRation(-1L);
                } else {
                    upstreamChannel.setUsErrorRation(usInfoExt.getUsErrorRation());
                }
            }
        } catch (Exception e) {
            logger.debug("", e);
        }
        try {
            CmDSInfo cmDSInfo = new CmDSInfo();
            cmDSInfo.setCmIndex(cmAttribute.getStatusIndex());
            List<CmDSInfo> cmDSInfos = getCmRemoteQueryFacade(entitySnmpParam.getIpAddress()).getCmDSInfo(entitySnmpParam, cmDSInfo);
            Map<Integer,CmDSInfo> map = new HashMap<>();
            for (CmDSInfo dsInfo : cmDSInfos) {
                map.put(dsInfo.getDocsIfDownChannelId().intValue(),dsInfo);
            }
            List<DocsIfDownstreamChannel> downstreamChannels = cmStatus.getDocsIfDownstreamChannelList();
            for (DocsIfDownstreamChannel downstreamChannel :downstreamChannels) {
                CmDSInfo dsInfo = map.get(downstreamChannel.getDocsIfDownChannelId().intValue());
                if (dsInfo == null) {
                    downstreamChannel.setDsBps(-1L);
                    downstreamChannel.setDsErrorRation(-1L);
                } else {
                    downstreamChannel.setDsBps(dsInfo.getDsBps());
                    downstreamChannel.setDsErrorRation(dsInfo.getDsErrorRation());
                }
            }
        } catch (Exception e) {
            logger.debug("", e);
        }

        return cmStatus;
    }

    private String getStatus(Byte ifOperStatus) {
        switch (ifOperStatus) {
            case 1:
                return "UP";
            case 2:
                return "DOWN";
            case 3:
                return "Testing";
            case 4:
                return "Unknown";
            case 5:
                return "Dormant";
            case 6:
                return "NotPresent";
            case 7:
                return "LowerLayerDown";
            default:
                return "Unknown";
        }
    }
    /**
     * CM 2.0获取CM频率、带宽、信噪比
     *
     * @return
     */
    private CmStatus remoteQueryCm2ChanInfos(CmAttribute cmAttribute, SnmpParam entitySnmpParam) {
        CmStatus cmStatus = new CmStatus();
        cmAttribute = this.getCm20Signal(entitySnmpParam, cmAttribute);
        if (cmAttribute != null) {
            cmStatus.setRemoteQueryState(cmAttribute.getRemoteQueryState());
            /*----上行信道射频状态处理------*/
            Long upChanIndex = cmAttribute.getStatusUpChannelIfIndex();// 上行信道Index
            CmcUpChannelBaseInfo upChanInfo = new CmcUpChannelBaseInfo();
            upChanInfo = cmRemoteQueryDao.getUpChanInfo(cmAttribute.getCmcId(), upChanIndex);
            DocsIfUpstreamChannel docsIfUpstreamChannel = new DocsIfUpstreamChannel();
            docsIfUpstreamChannel.setDocsIfUpChannelId(upChanInfo.getChannelId());// Channel ID
            docsIfUpstreamChannel.setDocsIfUpChannelFrequency(upChanInfo.getChannelFrequency());// 频率
            docsIfUpstreamChannel.setDocsIfUpChannelWidth(upChanInfo.getChannelWidth());// 频宽
            docsIfUpstreamChannel.setDocsIfUpChannelModulationProfile(upChanInfo.getChannelModulationProfile());// 调制方式
            // 设置经过转换后的发送电平值
            docsIfUpstreamChannel.setDocsIfCmStatusTxPowerForUnit(String.valueOf(cmAttribute.getUpChannelTransPower()));// 发射电平
            List<DocsIfUpstreamChannel> docsIfUpstreamChannelList = new ArrayList<DocsIfUpstreamChannel>();
            docsIfUpstreamChannelList.add(docsIfUpstreamChannel);
            cmStatus.setDocsIfUpstreamChannelList(docsIfUpstreamChannelList);
            /*----上行行信道信号质量处理------*/
            // 参考CmServiceImpl.java 322行
            List<DocsIf3CmtsCmUsStatus> docsIf3CmtsCmUsStatusList = new ArrayList<DocsIf3CmtsCmUsStatus>();
            DocsIf3CmtsCmUsStatus docsIf3CmtsCmUsStatus = new DocsIf3CmtsCmUsStatus();
            docsIf3CmtsCmUsStatus.setCmUsStatusSignalNoise(cmAttribute.getStatusSignalNoise());// 信噪比
            CmTopologyInfo cmTopologyInfo = cmService.getTopologyInfo(cmAttribute.getCmId());
            docsIf3CmtsCmUsStatus.setUpChannelId(cmTopologyInfo.getDocsIfUpChannelId());
            if (entityTypeService.isCmts(cmTopologyInfo.getDeviceType())) {
                // CMTS下CM ADD BY loyal
                docsIf3CmtsCmUsStatus.setUpChannelIdString(cmTopologyInfo.getUpDesrc());
            } else {
                docsIf3CmtsCmUsStatus.setUpChannelIdString(String.valueOf(cmTopologyInfo.getDocsIfUpChannelId()));
            }
            docsIf3CmtsCmUsStatusList.add(docsIf3CmtsCmUsStatus);
            cmStatus.setDocsIf3CmtsCmUsStatusList(docsIf3CmtsCmUsStatusList);
            /*----下行信道射频状态处理------*/
            Long downChanIndex = cmAttribute.getStatusDownChannelIfIndex();// 下行信道Index
            CmcDownChannelBaseInfo downChanInfo = new CmcDownChannelBaseInfo();
            downChanInfo = cmRemoteQueryDao.getDownChanInfo(cmAttribute.getCmcId(), downChanIndex);
            DocsIfDownstreamChannel docsIfDownstreamChannel = new DocsIfDownstreamChannel();
            docsIfDownstreamChannel.setDocsIfDownChannelId(Long.valueOf(downChanInfo.getDocsIfDownChannelId()));// ChanID
            docsIfDownstreamChannel.setDocsIfDownChannelFrequency(downChanInfo.getDocsIfDownChannelFrequency());// 频率
            docsIfDownstreamChannel.setDocsIfDownChannelWidth(Long.valueOf(downChanInfo.getDocsIfDownChannelWidth()));// 带宽
            docsIfDownstreamChannel.setDocsIfDownChannelModulation(downChanInfo.getDocsIfDownChannelModulation());// 调制方式
            // 设置经过转换后的接收电平值
            docsIfDownstreamChannel.setDocsIfDownChannelPowerForUnit(String.valueOf(cmAttribute
                    .getDownChannelRecvPower()));// 接收电平
            List<DocsIfDownstreamChannel> docsIfDownstreamChannelList = new ArrayList<DocsIfDownstreamChannel>();
            docsIfDownstreamChannelList.add(docsIfDownstreamChannel);
            cmStatus.setDocsIfDownstreamChannelList(docsIfDownstreamChannelList);
            /*----下行信道信号质量处理------*/
            DocsIfSignalQuality docsIfSignalQuality = new DocsIfSignalQuality();
            docsIfSignalQuality.setDownChanelId(Long.valueOf(downChanInfo.getDocsIfDownChannelId()));
            docsIfSignalQuality.setDocsIfSigQSignalNoiseForUnit(cmAttribute.getDownChannelSnr());// 下行信噪比
            List<DocsIfSignalQuality> docsIfSignalQualityList = new ArrayList<DocsIfSignalQuality>();
            docsIfSignalQualityList.add(docsIfSignalQuality);
            cmStatus.setDocsIfSignalQualityList(docsIfSignalQualityList);
        }
        return cmStatus;
    }

    /**
     * CM 3.0获取CM频率、带宽、信噪比
     *
     * @return
     */
    private CmStatus remoteQueryCm3ChanInfos(CmAttribute cmAttribute, SnmpParam entitySnmpParam) {
        CmStatus cmStatus = new CmStatus();
        cmAttribute = this.getCm20Signal(entitySnmpParam, cmAttribute);
        if (cmAttribute == null) {
            return cmStatus;
        }
        cmStatus.setRemoteQueryState(cmAttribute.getRemoteQueryState());
        /*----上行信道射频状态处理------*/
        Cm3UsRemoteQuery cm3UsRemoteQuery = new Cm3UsRemoteQuery();
        cm3UsRemoteQuery.setCmIndex(cmAttribute.getStatusIndex());
        List<Cm3UsRemoteQuery> upChannels = getCmRemoteQueryFacade(entitySnmpParam.getIpAddress()).getCm3UsSignal(
                entitySnmpParam, cm3UsRemoteQuery);
        List<DocsIfUpstreamChannel> docsIfUpstreamChannelList = new ArrayList<DocsIfUpstreamChannel>();
        Collections.sort(upChannels); // 按照信道ID排序
        for (Cm3UsRemoteQuery up : upChannels) {
            CmcUpChannelBaseInfo upChanInfo = cmRemoteQueryDao.getUpChanInfoByChanId(cmAttribute.getCmcId(),
                    up.getCmUsChanId());
            DocsIfUpstreamChannel docsIfUpstreamChannel = new DocsIfUpstreamChannel();
            docsIfUpstreamChannel.setDocsIfUpChannelId(upChanInfo.getChannelId());// Channel ID
            docsIfUpstreamChannel.setDocsIfUpChannelFrequency(upChanInfo.getChannelFrequency());// 频率
            docsIfUpstreamChannel.setDocsIfUpChannelWidth(upChanInfo.getChannelWidth());// 频宽
            docsIfUpstreamChannel.setDocsIfUpChannelModulationProfile(upChanInfo.getChannelModulationProfile());// 调制方式
            docsIfUpstreamChannel.setDocsIfCmStatusTxPowerForUnit(up.getCmUsTxPowerString());// 发射电平
            docsIfUpstreamChannelList.add(docsIfUpstreamChannel);
        }
        /*----上行信道信号质量处理------*/
        // CM上行信号质量信息
        List<DocsIf3CmtsCmUsStatus> docsIf3CmtsCmUsStatusList = cmDao.queryDocsIf3CmtsCmUsStatusList(cmAttribute
                .getCmId());
        // 映射信道频率
        for (DocsIf3CmtsCmUsStatus aDocsIf3CmtsCmUsStatusList : docsIf3CmtsCmUsStatusList) {
            for (DocsIfUpstreamChannel aDocsIfUpstreamChannelList : docsIfUpstreamChannelList) {
                if (aDocsIf3CmtsCmUsStatusList.getUpChannelId().intValue() == aDocsIfUpstreamChannelList
                        .getDocsIfUpChannelId().intValue()) {
                    aDocsIf3CmtsCmUsStatusList.setDocsIfUpChannelFrequencyForUnit(
                            aDocsIfUpstreamChannelList.getDocsIfUpChannelFrequencyForUnit());// 信噪比
                    aDocsIf3CmtsCmUsStatusList
                            .setUpChannelId(Long.valueOf(aDocsIfUpstreamChannelList.getDocsIfUpChannelId()));// ChanID
                }
            }
        }
        cmStatus.setDocsIf3CmtsCmUsStatusList(docsIf3CmtsCmUsStatusList);
        cmStatus.setDocsIfUpstreamChannelList(docsIfUpstreamChannelList);
        /*----下行信道处理------*/
        Cm3DsRemoteQuery cm3DsRemoteQuery = new Cm3DsRemoteQuery();
        cm3DsRemoteQuery.setCmIndex(cmAttribute.getStatusIndex());
        List<Cm3DsRemoteQuery> downChannels = getCmRemoteQueryFacade(entitySnmpParam.getIpAddress()).getCm3DsSignal(
                entitySnmpParam, cm3DsRemoteQuery);
        List<DocsIfDownstreamChannel> docsIfDownstreamChannelList = new ArrayList<DocsIfDownstreamChannel>();
        List<DocsIfSignalQuality> docsIfSignalQualityList = new ArrayList<DocsIfSignalQuality>();
        Collections.sort(downChannels);// 按照信道ID排序
        for (Cm3DsRemoteQuery down : downChannels) {
            CmcDownChannelBaseInfo downChanInfo = cmRemoteQueryDao.getDownChanInfoByChanId(cmAttribute.getCmcId(),
                    down.getCmDsChanId());
            DocsIfDownstreamChannel docsIfDownstreamChannel = new DocsIfDownstreamChannel();
            docsIfDownstreamChannel.setDocsIfDownChannelId(Long.valueOf(downChanInfo.getDocsIfDownChannelId()));// ChanID
            docsIfDownstreamChannel.setDocsIfDownChannelFrequency(downChanInfo.getDocsIfDownChannelFrequency());// 频率
            docsIfDownstreamChannel.setDocsIfDownChannelWidth(Long.valueOf(downChanInfo.getDocsIfDownChannelWidth()));// 带宽
            docsIfDownstreamChannel.setDocsIfDownChannelModulation(downChanInfo.getDocsIfDownChannelModulation());// 调制方式
            docsIfDownstreamChannel.setDocsIfDownChannelPowerForUnit(down.getCmDsRxPowerString());// 接收电平
            docsIfDownstreamChannelList.add(docsIfDownstreamChannel);
            DocsIfSignalQuality docsIfSignalQuality = new DocsIfSignalQuality();
            docsIfSignalQuality.setDownChanelId(Long.valueOf(downChanInfo.getDocsIfDownChannelId()));// ChanID
            docsIfSignalQuality.setDocsIfSigQSignalNoiseForUnit(down.getCmDsSignalNoiseString());// 下行信噪比
            docsIfSignalQualityList.add(docsIfSignalQuality);
        }
        cmStatus.setDocsIfDownstreamChannelList(docsIfDownstreamChannelList);
        cmStatus.setDocsIfSignalQualityList(docsIfSignalQualityList);
        return cmStatus;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.remotequerycm.service.CmRemoteQueryService#
     * remoteQueryCmSignal(java .lang.Long)
     */
    @Override
    public CmAttribute remoteQueryCmSignal(CmAttribute cmAttribute) {
        if (cmAttribute != null && cmAttribute.isCmOnline()) { // 判断CM是否在线
            Long entityId = getEntityIdByCmcId(cmAttribute.getCmcId());
            SnmpParam entitySnmpParam = getSnmpParamByEntityId(entityId);
            entitySnmpParam.setMibs("TOPVISION-CCMTS-MIB");
            // TODO 此处需要判断该CM是2.0还是3.0
            if (cmType == 2) {
                return this.getCm20Signal(entitySnmpParam, cmAttribute);
            } else if (cmType == 3) {
                return this.getCm30Signal(entitySnmpParam, cmAttribute);
            }
        }
        return cmAttribute;
    }

    /**
     * CM 2.0 获取实时信号--上行电平、下行电平、信噪比
     *
     * @param snmpParam
     * @param cmAttribute
     * @return
     */
    private CmAttribute getCm20Signal(SnmpParam snmpParam, CmAttribute cmAttribute) {
        Cm2RemoteQuery cm2RemoteQuery = new Cm2RemoteQuery();
        cm2RemoteQuery.setCmIndex(cmAttribute.getStatusIndex());
        cm2RemoteQuery = getCmRemoteQueryFacade(snmpParam.getIpAddress()).getCm2Signal(snmpParam, cm2RemoteQuery);
        cmAttribute.setDownChannelTx(cm2RemoteQuery.getCmRxPowerString());
        cmAttribute.setUpChannelTx(cm2RemoteQuery.getCmTxPowerString());
        cmAttribute.setDownChannelSnr(cm2RemoteQuery.getCmSignalNoiseString());
        cmAttribute.setRemoteQueryState(cm2RemoteQuery.getStatus() == 1);
        return cmAttribute;
    }

    /**
     * CM 3.0 获取实时信号--上行电平、下行电平、信噪比
     *
     * @param snmpParam
     * @param cmAttribute
     * @return
     */
    private CmAttribute getCm30Signal(SnmpParam snmpParam, CmAttribute cmAttribute) {
        Cm2RemoteQuery cm2RemoteQuery = new Cm2RemoteQuery();
        cm2RemoteQuery.setCmIndex(cmAttribute.getStatusIndex());
        cm2RemoteQuery = getCmRemoteQueryFacade(snmpParam.getIpAddress()).getCm2Signal(snmpParam, cm2RemoteQuery);
        cmAttribute.setRemoteQueryState(cm2RemoteQuery.getStatus() == 1);
        Cm3DsRemoteQuery cm3DsRemoteQuery = new Cm3DsRemoteQuery();
        cm3DsRemoteQuery.setCmIndex(cmAttribute.getStatusIndex());
        List<Cm3DsRemoteQuery> downChannels = getCmRemoteQueryFacade(snmpParam.getIpAddress()).getCm3DsSignal(
                snmpParam, cm3DsRemoteQuery);
        if (downChannels.size() > 0) {
            Cm3DsRemoteQuery d = downChannels.get(0);
            cmAttribute.setDownChannelTx(d.getCmDsRxPowerString());
            cmAttribute.setDownChannelSnr(d.getCmDsSignalNoiseString());
        }
        Cm3UsRemoteQuery cm3UsRemoteQuery = new Cm3UsRemoteQuery();
        cm3UsRemoteQuery.setCmIndex(cmAttribute.getStatusIndex());
        List<Cm3UsRemoteQuery> upChannels = getCmRemoteQueryFacade(snmpParam.getIpAddress()).getCm3UsSignal(snmpParam,
                cm3UsRemoteQuery);
        if (upChannels.size() > 0) {
            Cm3UsRemoteQuery u = upChannels.get(0);
            cmAttribute.setUpChannelTx(u.getCmUsTxPowerString());
        }
        return cmAttribute;
    }
    
    
    public Cm2RemoteQuery getCmSignal(SnmpParam snmpParam, CmAttribute cmAttribute) {
        Cm2RemoteQuery cm2RemoteQuery = new Cm2RemoteQuery();
        cm2RemoteQuery.setCmIndex(cmAttribute.getStatusIndex());
        cm2RemoteQuery = getCmRemoteQueryFacade(snmpParam.getIpAddress()).getCm2Signal(snmpParam, cm2RemoteQuery);
        return cm2RemoteQuery;
    }
    
    public  List<Cm3DsRemoteQuery> getCm3DsRemoteQuery(SnmpParam snmpParam, CmAttribute cmAttribute) {
        Cm3DsRemoteQuery cm3DsRemoteQuery = new Cm3DsRemoteQuery();
        cm3DsRemoteQuery.setCmIndex(cmAttribute.getStatusIndex());
        List<Cm3DsRemoteQuery> downChannels = getCmRemoteQueryFacade(snmpParam.getIpAddress()).getCm3DsSignal(
                snmpParam, cm3DsRemoteQuery);
        return downChannels;
    }
      
    public List<Cm3UsRemoteQuery> getCm3UsRemoteQuery(SnmpParam snmpParam, CmAttribute cmAttribute) {
        Cm3UsRemoteQuery cm3UsRemoteQuery = new Cm3UsRemoteQuery();
        cm3UsRemoteQuery.setCmIndex(cmAttribute.getStatusIndex());
        List<Cm3UsRemoteQuery> upChannels = getCmRemoteQueryFacade(snmpParam.getIpAddress()).getCm3UsSignal(snmpParam,
                cm3UsRemoteQuery);
        return upChannels;
    }
 

}
