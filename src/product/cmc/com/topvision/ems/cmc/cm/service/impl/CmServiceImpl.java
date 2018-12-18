/***********************************************************************
 * $Id: CmServiceImpl.java,v1.0 2011-12-8 上午10:57:57 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.cm.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.ems.cmc.cm.dao.CmDao;
import com.topvision.ems.cmc.cm.dao.CmRefreshDao;
import com.topvision.ems.cmc.cm.domain.Cm3Signal;
import com.topvision.ems.cmc.cm.domain.CmServiceType;
import com.topvision.ems.cmc.cm.facade.CmFacade;
import com.topvision.ems.cmc.cm.service.CmService;
import com.topvision.ems.cmc.cm.service.CmServiceTypeService;
import com.topvision.ems.cmc.cpe.domain.CmIfTable;
import com.topvision.ems.cmc.cpe.domain.CmStatusTable;
import com.topvision.ems.cmc.cpe.domain.CmSystemInfoExt;
import com.topvision.ems.cmc.cpe.service.CpeService;
import com.topvision.ems.cmc.domain.CmTopologyInfo;
import com.topvision.ems.cmc.downchannel.dao.CmcDownChannelDao;
import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.cmc.facade.domain.CmCpe;
import com.topvision.ems.cmc.facade.domain.CmStaticIp;
import com.topvision.ems.cmc.facade.domain.CmStatus;
import com.topvision.ems.cmc.facade.domain.CpeAttribute;
import com.topvision.ems.cmc.facade.domain.DocsIf3CmtsCmUsStatus;
import com.topvision.ems.cmc.facade.domain.DocsIfDownstreamChannel;
import com.topvision.ems.cmc.facade.domain.DocsIfSignalQuality;
import com.topvision.ems.cmc.facade.domain.DocsIfUpstreamChannel;
import com.topvision.ems.cmc.performance.domain.CmAct;
import com.topvision.ems.cmc.performance.domain.CmNum;
import com.topvision.ems.cmc.performance.domain.CpeAct;
import com.topvision.ems.cmc.qos.domain.CmcQosServiceFlowInfo;
import com.topvision.ems.cmc.remotequerycm.service.CmRemoteQueryService;
import com.topvision.ems.cmc.service.impl.CmcBaseCommonService;
import com.topvision.ems.cmc.topology.dao.CmcDiscoveryDao;
import com.topvision.ems.cmc.upchannel.dao.CmcUpChannelDao;
import com.topvision.ems.cmc.util.CmcConstants;
import com.topvision.ems.devicesupport.version.service.DeviceVersionService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.IfTable;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.common.DateUtils;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.exception.engine.SnmpException;
import com.topvision.framework.exception.engine.SnmpSetException;
import com.topvision.framework.ping.PingExecutorService;
import com.topvision.framework.ping.PingResult;
import com.topvision.framework.ping.PingWorker;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.utils.CmcIndexUtils;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.service.SystemPreferencesService;

/**
 * CM相关功能
 * 
 * @author loyal
 * @created @2011-12-8-上午10:57:57
 * 
 */
@Service("cmService")
public class CmServiceImpl extends CmcBaseCommonService implements CmService {
    @Resource(name = "cmDao")
    private CmDao cmDao;
    @Resource(name = "cmRefreshDao")
    private CmRefreshDao cmRefreshDao;
    @Resource(name = "upChannelDao")
    private CmcUpChannelDao cmcUpChannelDao;
    @Resource(name = "cmcDownChannelDao")
    private CmcDownChannelDao cmcDownChannelDao;
    private SnmpParam snmpParam;
    @Resource(name = "cmcDiscoveryDao")
    private CmcDiscoveryDao cmcDiscoveryDao;
    @Resource(name = "pingExecutorService")
    private PingExecutorService pingExecutorService;
    @Resource(name = "entityService")
    private EntityService entityService;
    @Resource(name = "cpeService")
    private CpeService cpeService;
    @Autowired
    private EntityTypeService entityTypeService;
    @Resource(name = "cmcService")
    private CmcService cmcService;
    @Value("${cmReadCommunity}")
    private String cmReadCommunity;
    @Value("${cmWriteCommunity}")
    private String cmWriteCommunity;
    @Resource(name = "systemPreferencesService")
    private SystemPreferencesService systemPreferencesService;
    @Resource(name = "cmRemoteQueryService")
    private CmRemoteQueryService cmRemoteQueryService;
    @Resource(name = "cmServiceTypeService")
    private CmServiceTypeService cmServiceTypeService;
    @Resource(name = "deviceVersionService")
    private DeviceVersionService deviceVersionService;

    @PreDestroy
    public void destroy() {
        super.destroy();
    }

    @PostConstruct
    public void initialize() {
        super.initialize();
    }

    @Override
    public SnmpParam getCmSnmpParam() {
        String cmReadCommunity = systemPreferencesService.getModulePreferences("cmcTerminal").getProperty("cmReadCommunity", "public");
        String cmWriteCommunity = systemPreferencesService.getModulePreferences("cmcTerminal").getProperty("cmWriteCommunity", "private");
        String retry = systemPreferencesService.getModulePreferences("Snmp").getProperty("Snmp.retries", "0");
        String timeout = systemPreferencesService.getModulePreferences("Snmp").getProperty("Snmp.timeout", "10000");
        
        SnmpParam snmpParam = new SnmpParam();
        snmpParam.setCommunity(cmReadCommunity);
        snmpParam.setWriteCommunity(cmWriteCommunity);
        snmpParam.setRetry(Byte.parseByte(retry));
        snmpParam.setTimeout(Long.parseLong(timeout));
        return snmpParam;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.cm.service.CmService#getCmAttributeByCmcId(java.lang.Long)
     */
    public List<CmAttribute> getCmAttributeByCmcId(Long cmcId) {
        List<CmAttribute> cmAttributeList = cmDao.selectCmAttributeByCmcId(cmcId);
        List<DocsIf3CmtsCmUsStatus> cmUsStatusList = cmDao.selectCmUsStatusByCmcId(cmcId);
        for (CmAttribute aCmAttributeList : cmAttributeList) {
            List<DocsIf3CmtsCmUsStatus> cmUsStatus = new ArrayList<>();
            for (DocsIf3CmtsCmUsStatus aCmUsStatusList : cmUsStatusList) {
                if (aCmAttributeList.getStatusIndex().equals(aCmUsStatusList.getCmRegStatusId())) {
                    cmUsStatus.add(aCmUsStatusList);
                }
            }
            aCmAttributeList.setDocsIf3CmtsCmUsStatusList(cmUsStatus);
        }
        return cmAttributeList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.cm.service.CmService#getRealtimeCmAttributeByCmcId(java.lang.Long)
     */
    public List<CmAttribute> getRealtimeCmAttributeByCmcId(Long cmcId) {
        Long entityId = cmcService.getEntityIdByCmcId(cmcId);
        Long typeId = entityService.getEntity(cmcId).getTypeId();
        SnmpParam snmpParam = getSnmpParamByEntityId(entityId);
        List<CmAttribute> cmAttributeList = getCmFacade(snmpParam.getIpAddress()).getCmAttributeInfos(snmpParam);
        List<DocsIf3CmtsCmUsStatus> docsIf3CmtsCmUsStatusList = getCmFacade(snmpParam.getIpAddress())
                .getCmUsStatus(cmAttributeList, snmpParam);

        // add by haojie 根据不同的cmcId获取不通的cmAttribute
        Map<Long, Long> cmcIds = new HashMap<Long, Long>();
        Map<Long, List<CmAttribute>> cmcAttributeMap = new HashMap<Long, List<CmAttribute>>();
        for (CmAttribute cmAttribute : cmAttributeList) {
            Long cmcIndex = CmcIndexUtils.getCmcIndexFromCmIndex(cmAttribute.getStatusIndex());
            Long cmcIdCol;
            if (!cmcIds.containsKey(cmcIndex)) {
                Map<String, Long> map = new HashMap<String, Long>();
                map.put("entityId", entityId);
                map.put("cmcIndex", cmcIndex);
                cmcIdCol = cmcDiscoveryDao.getCmcIdByCmcIndexAndEntityId(map);
                if (cmcIdCol == null) {
                    logger.debug("entityId [" + entityId + "] cmcIndex [" + cmcIndex + "] cmcId [" + cmcIdCol + "]");
                    continue;
                } else {
                    cmcIds.put(cmcIndex, cmcIdCol);
                }
            } else {
                cmcIdCol = cmcIds.get(cmcIndex);
            }
            List<CmAttribute> attrs;
            if (cmcAttributeMap.containsKey(cmcIdCol)) {
                attrs = cmcAttributeMap.get(cmcIdCol);
            } else {
                attrs = new ArrayList<CmAttribute>();
                cmcAttributeMap.put(cmcIdCol, attrs);
            }
            attrs.add(cmAttribute);
        }
        List<CmAttribute> attrs = new ArrayList<CmAttribute>();
        if (cmcAttributeMap.get(cmcId) != null) {
            attrs = cmcAttributeMap.get(cmcId);
        }
        cmRefreshDao.batchRefreshCmAttribute(entityId, cmcId, attrs);

        if (entityTypeService.isCcmtsWithoutAgent(typeId)) {
            cmcDiscoveryDao.batchInsertOrUpdateDocsIf3CmtsCmUsStatusFor8800A(docsIf3CmtsCmUsStatusList, cmcId);
        } else {
            cmcDiscoveryDao.batchInsertOrUpdateDocsIf3CmtsCmUsStatus(docsIf3CmtsCmUsStatusList, cmcId);
        }
        return getCmAttributeByCmcId(cmcId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.cm.service.CmService#getChannelCmNum(java.lang.Long,
     * java.lang.Long)
     */
    public CmNum getChannelCmNum(Long cmcId, Long channelIndex) {
        Long entityId = cmcService.getEntityIdByCmcId(cmcId);
        return cmDao.selectChannelCmNum(entityId, channelIndex);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.cm.service.CmService#getRealtimeChannelCmNum(java.lang.Long,
     * java.lang.Long)
     */
    public CmNum getRealtimeChannelCmNum(Long cmcId, Long channelIndex) {
        Long entityId = cmcService.getEntityIdByCmcId(cmcId);
        SnmpParam snmpParam = getSnmpParamByEntityId(entityId);
        List<CmAttribute> cmAttributeList = getCmFacade(snmpParam.getIpAddress()).getCmAttributeInfos(snmpParam);
        CmNum cmNum = new CmNum();
        cmNum.setEntityId(entityId);
        cmNum.setCmcId(cmcId);
        cmNum.setPortIfIndex(channelIndex);
        int size = cmAttributeList.size();
        int onlineNum = 0;
        int offlineNum = 0;
        int otherNum = 0;
        for (int i = 0; i < size; i++) {
            if (channelIndex.equals(cmAttributeList.get(i).getStatusUpChannelIfIndex())
                    || channelIndex.equals(cmAttributeList.get(i).getStatusDownChannelIfIndex())) {
                if (cmAttributeList.get(i).isCmOnline()) {
                    // 上线
                    onlineNum += 1;
                } else if (cmAttributeList.get(i).isCmOffline()) {
                    // 下线
                    offlineNum += 1;
                } else {
                    // 上线中
                    otherNum += 1;
                }
            }
        }
        cmNum.setAllNum(onlineNum + offlineNum + otherNum);
        cmNum.setOnlineNum(onlineNum);
        cmNum.setOfflineNum(offlineNum);
        cmNum.setOtherNum(otherNum);
        return cmNum;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.cm.service.CmService#getChannelCmNumHis(java.lang.Long,
     * java.lang.Long, java.lang.String, java.lang.String)
     */
    public List<CmNum> getChannelCmNumHis(Long cmcId, Long channelIndex, String startTime, String endTime) {
        Long entityId = cmcService.getEntityIdByCmcId(cmcId);
        return cmDao.selectChannelCmNumHis(entityId, channelIndex, startTime, endTime);
    }

    @Override
    public CmStatus showCmStatus(String cmIp, Long cmId) {
        Integer queryMode = this.getCmQueryMode();
        CmStatus cmStatus;
        if (CmcConstants.CM_IMMEDIATELY_QUERY.equals(queryMode)) { // 原有方法查询
            cmStatus = this.showCmStatusImmediately(cmIp, cmId);
        } else {
            cmStatus = cmRemoteQueryService.remoteQueryCmChanInfos(cmIp, cmId); // 从CC上取CM上下行信道信息
        }
        Long typeId = cmDao.getDeviceTypeByCmId(cmId);
        Boolean otherCmts = entityTypeService.isCmts(typeId);
        cmStatus.setOtherCmts(otherCmts);
        Long cmcId = cmDao.getCmcIdByCmId(cmId);
        if (cmcId != null) {
            CmcAttribute cmcAttribute = cmcService.getCmcAttributeByCmcId(cmcId);
            if (cmcAttribute != null) {
                boolean b = deviceVersionService.isFunctionSupported(cmcId, "remoteQueryII");
                cmStatus.setVersionSupport(b);
            } else {
                cmStatus.setVersionSupport(false);
            }
        } else {
            cmStatus.setVersionSupport(false);
        }
        return cmStatus;
    }

    @Override
    public Integer getCmQueryMode() {
        Properties properties = systemPreferencesService.getModulePreferences("RemoteQuery");
        String mode = properties.getProperty("RemoteQueryCmMode");
        Integer collectMode = 1;
        if (mode != null && mode.equals("2")) {
            collectMode = CmcConstants.CM_REMOTE_QUERY;
        } else {
            collectMode = CmcConstants.CM_IMMEDIATELY_QUERY;
        }
        return collectMode;
    }

    /**
     * 原有方法，查询CmStatus，上下行信道信息
     * 
     * @param cmIp
     * @param cmId
     * @return
     */
    private CmStatus showCmStatusImmediately(String cmIp, Long cmId) {
        // CM snmpParam
        SnmpParam snmpParam = new SnmpParam();
        // modify by loyal cm共同体名存在数据库中，可配
        cmReadCommunity = cpeService.getCmReadCommunity();
        cmWriteCommunity = cpeService.getCmWriteCommunity();
        snmpParam.setCommunity(cmReadCommunity);
        snmpParam.setWriteCommunity(cmWriteCommunity);
        snmpParam.setIpAddress(cmIp);
        String cmcmib = "RFC1213-MIB,IF-MIB,DOCS-IF-MIB";
        snmpParam.setMibs(cmcmib);

        // cm基本属性信息
        CmStatus cmStatus = getCmFacade(cmIp).showCmStatus(snmpParam);
        if (cmStatus == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("showCmStatusImmediately cmStatus is not exist.[" + cmIp + "]");
            }
            return new CmStatus();
        }

        // 通过CM获取上联设备的相关信息
        CmAttribute cmAttribute = cmDao.getCmAttributeByCmId(cmId);
        if (cmAttribute == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("showCmStatusImmediately cmAttribute is not exist.[" + cmId + "]");
            }
            return cmStatus;
        }
        Entity entity = entityService.getEntity(cmAttribute.getEntityId());
        if (entity == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("showCmStatusImmediately entity is not exist.[" + cmAttribute.getEntityId() + "]");
            }
            return cmStatus;
        }
        SnmpParam entitySnmpParam = getSnmpParamByEntityId(entity.getEntityId());
        cmAttribute = getCmcFacade(entitySnmpParam.getIpAddress()).getCmAttribute(entitySnmpParam, cmAttribute);

        try {
            List<IfTable> ifTables = getCmFacade(cmIp).getIfTable(snmpParam);
            List<CmIfTable> cmIfTables = new ArrayList<>();
            Integer macDomainIndex = null;
            for (IfTable ifTable : ifTables) {
                if (ifTable.getIfType().equals(127)) {
                    macDomainIndex = ifTable.getIfIndex();
                }
                CmIfTable cmIfTable = new CmIfTable();
                cmIfTable.setCmIndex(cmAttribute.getStatusIndex());
                cmIfTable.setIfIndex(ifTable.getIfIndex().longValue());
                cmIfTable.setPortDesc(ifTable.getIfDescr());
                cmIfTable.setPortType(ifTable.getIfType());
                cmIfTable.setPortStatus(ifTable.getIfOperStatus());
                cmIfTables.add(cmIfTable);
            }
            cmStatus.setCmIfTables(cmIfTables);
            if (macDomainIndex != null) {
                try {
                    CmStatusTable cmStatusTable = new CmStatusTable();
                    cmStatusTable.setIfIndex(macDomainIndex);
                    cmStatusTable = getCmFacade(cmIp).getCmStatusTable(snmpParam, cmStatusTable);
                    cmStatus.setDocsIfCmStatusResets(cmStatusTable.getDocsIfCmStatusResets());
                } catch (Exception e) {
                    logger.debug("", e);
                }
            }
        } catch (Exception e) {
            logger.debug("", e);
        }

        try {
            CmSystemInfoExt cmSystemInfoExt = getCmcFacade(entity.getIp()).getCmSystemInfoExtByCmIndex(entitySnmpParam,
                    cmAttribute.getStatusIndex());
            cmStatus.setCmHardWare(cmSystemInfoExt.getCmHardWare());
            cmStatus.setCmSoftWare(cmSystemInfoExt.getCmSoftWare());
        } catch (Exception e) {
            logger.debug("", e);
        }

        try {
            // 获取CM的业务类型
            if (cmStatus != null && cmStatus.getDocsDevServerConfigFile() != null) {
                // 查看cmcpe信息的时候，将刷新的配置文件名更新到cmattribute表中
                cmDao.updateCmConfigFile(cmId, cmStatus.getDocsDevServerConfigFile());
                // 替换configfile的格式，采用格式 配置文件名（业务类型）
                CmServiceType cmServiceType = cmServiceTypeService
                        .getCmServiceTypeById(cmStatus.getDocsDevServerConfigFile());
                if (cmServiceType != null && cmServiceType.getServiceType() != null) {
                    cmStatus.setDocsDevServerConfigFile(
                            cmStatus.getDocsDevServerConfigFile() + "(" + cmServiceType.getServiceType() + ")");
                }
            }
        } catch (Exception e) {
            logger.debug("get cmConfigFileName error!", e);
        }

        // cm上行射频信息
        List<DocsIfUpstreamChannel> docsIfUpstreamChannelList = getCmFacade(cmIp)
                .getDocsIfUpstreamChannelList(snmpParam);
        // 处理上行射频信息中对于2.0和3.0cm的电平区分
        switch (cmStatus.getDocsIfDocsisBaseCapability()) {
        case 1:
        case 2:
        case 3:// 2.0cm
            for (int i = 0; i < docsIfUpstreamChannelList.size(); i++) {
                docsIfUpstreamChannelList.get(i).setTxPower(docsIfUpstreamChannelList.get(i).getUpChannelPower());
            }
            break;
        case 4:// 3.0cm
            if (cmStatus.getDocsIf3CmCapabilitiesRsp() != null) {
                if (getCmDocsisRegVersion(cmStatus.getDocsIf3CmCapabilitiesRsp()) == 3) {
                    for (DocsIfUpstreamChannel aDocsIfUpstreamChannelList : docsIfUpstreamChannelList) {
                        aDocsIfUpstreamChannelList.setTxPower(aDocsIfUpstreamChannelList.getCm3UpChannelPower());
                    }
                } else {
                    for (DocsIfUpstreamChannel aDocsIfUpstreamChannelList : docsIfUpstreamChannelList) {
                        aDocsIfUpstreamChannelList.setTxPower(aDocsIfUpstreamChannelList.getUpChannelPower());
                    }
                }
            } else {
                for (DocsIfUpstreamChannel aDocsIfUpstreamChannelList : docsIfUpstreamChannelList) {
                    aDocsIfUpstreamChannelList.setTxPower(aDocsIfUpstreamChannelList.getUpChannelPower());
                }
            }
            break;
        default:
            for (DocsIfUpstreamChannel aDocsIfUpstreamChannelList : docsIfUpstreamChannelList) {
                aDocsIfUpstreamChannelList.setTxPower(aDocsIfUpstreamChannelList.getUpChannelPower());
            }
            break;
        }
        // 映射带宽显示
        for (DocsIfUpstreamChannel aDocsIfUpstreamChannelList : docsIfUpstreamChannelList) {
            Long ifindex = aDocsIfUpstreamChannelList.getIfIndex();
            Long upIfSpeed = getCmFacade(cmIp).getIfSpeed(snmpParam, ifindex);
            aDocsIfUpstreamChannelList.setUpIfSpeed(upIfSpeed);
        }
        cmStatus.setDocsIfUpstreamChannelList(docsIfUpstreamChannelList);

        // cm下行射频信息
        List<DocsIfDownstreamChannel> docsIfDownstreamChannelList = getCmFacade(cmIp)
                .getDocsIfDownstreamChannel(snmpParam);
        // 映射带宽显示
        for (DocsIfDownstreamChannel aDocsIfDownstreamChannelList : docsIfDownstreamChannelList) {
            Long ifIndex = aDocsIfDownstreamChannelList.getIfIndex();
            Long downIfSpeed = getCmFacade(cmIp).getIfSpeed(snmpParam, ifIndex);
            aDocsIfDownstreamChannelList.setDownIfSpeed(downIfSpeed);
        }
        cmStatus.setDocsIfDownstreamChannelList(docsIfDownstreamChannelList);

        // cm下行信号质量信息
        List<DocsIfSignalQuality> docsIfSignalQualityList = getCmFacade(cmIp).getDocsIfSignalQuality(snmpParam);
        // 映射信道Id和频率
        for (DocsIfSignalQuality aDocsIfSignalQualityList : docsIfSignalQualityList) {
            for (DocsIfDownstreamChannel aDocsIfDownstreamChannelList : docsIfDownstreamChannelList) {
                if (aDocsIfSignalQualityList.getIfIndex().equals(aDocsIfDownstreamChannelList.getIfIndex())) {
                    aDocsIfSignalQualityList.setDownChanelId(aDocsIfDownstreamChannelList.getDocsIfDownChannelId());
                    aDocsIfSignalQualityList.setDocsIfDownChannelFrequencyForUnit(
                            aDocsIfDownstreamChannelList.getDocsIfDownChannelFrequencyForUnit());
                }
            }
        }
        cmStatus.setDocsIfSignalQualityList(docsIfSignalQualityList);

        // 为了解决广州和内蒙版本不支持3.0mib而做的兼容措施，当从3.0mib里取不到上行信号质量数据时，改用2.0mib里的取值
        List<DocsIf3CmtsCmUsStatus> docsIf3CmtsCmUsStatusList = new ArrayList<DocsIf3CmtsCmUsStatus>();
        // 2.0CM的处理
        DocsIf3CmtsCmUsStatus docsIf3CmtsCmUsStatus = new DocsIf3CmtsCmUsStatus();
        docsIf3CmtsCmUsStatus.setCmUsStatusSignalNoise(cmAttribute.getStatusSignalNoise());
        docsIf3CmtsCmUsStatus.setCmUsStatusUncorrectables(cmAttribute.getStatusExtUncorrectables());
        docsIf3CmtsCmUsStatus.setCmUsStatusCorrecteds(cmAttribute.getStatusExtCorrecteds());
        docsIf3CmtsCmUsStatus.setCmUsStatusUnerroreds(cmAttribute.getStatusExtUnerroreds());
        CmTopologyInfo cmTopologyInfo = getTopologyInfo(cmId);
        docsIf3CmtsCmUsStatus.setUpChannelId(cmTopologyInfo.getDocsIfUpChannelId());
        if (entityTypeService.isCmts(cmTopologyInfo.getDeviceType())) {
            // cmts下cm add by loyal
            docsIf3CmtsCmUsStatus.setUpChannelIdString(cmTopologyInfo.getUpDesrc());
        } else {
            docsIf3CmtsCmUsStatus.setUpChannelIdString(cmTopologyInfo.getDocsIfUpChannelId().toString());
        }

        switch (cmStatus.getDocsIfDocsisBaseCapability()) {
        case 1:
        case 2:
        case 3:// 2.0cm
            docsIf3CmtsCmUsStatusList.add(docsIf3CmtsCmUsStatus);
            break;
        case 4:// 3.0cm
            if (cmStatus.getDocsIf3CmCapabilitiesRsp() != null) {
                if (getCmDocsisRegVersion(cmStatus.getDocsIf3CmCapabilitiesRsp()) == 3) {// 注册模式3.0
                    // cm上行信号质量信息
                    docsIf3CmtsCmUsStatusList = getCmcFacade(entitySnmpParam.getIpAddress())
                            .getDocsIf3CmtsCmUsStatusByCmIndex(entitySnmpParam, cmAttribute.getStatusIndex());
                    // 映射信道频率
                    for (DocsIf3CmtsCmUsStatus aDocsIf3CmtsCmUsStatusList : docsIf3CmtsCmUsStatusList) {
                        for (DocsIfUpstreamChannel aDocsIfUpstreamChannelList : docsIfUpstreamChannelList) {
                            if (aDocsIf3CmtsCmUsStatusList.getUpChannelId().intValue() == aDocsIfUpstreamChannelList
                                    .getDocsIfUpChannelId().intValue()) {
                                aDocsIf3CmtsCmUsStatusList.setDocsIfUpChannelFrequencyForUnit(
                                        aDocsIfUpstreamChannelList.getDocsIfUpChannelFrequencyForUnit());
                            }
                        }
                    }
                } else {// 注册模式2.0
                    docsIf3CmtsCmUsStatusList.add(docsIf3CmtsCmUsStatus);
                }
            } else {
                docsIf3CmtsCmUsStatusList.add(docsIf3CmtsCmUsStatus);
            }
            break;
        default:
            docsIf3CmtsCmUsStatusList.add(docsIf3CmtsCmUsStatus);
            break;
        }
        cmStatus.setDocsIf3CmtsCmUsStatusList(docsIf3CmtsCmUsStatusList);
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

    @Override
    public List<CmAttribute> getCmListByOlt(Long cmId, Integer start, Integer limit) {
        // 调用Dao中方法获取所在OLT下的CM列表
        return cmDao.getCmListByOlt(cmId, start, limit);
    }

    @Override
    public Long getCmListByOltCount(Long cmId) {
        // 调用Dao中方法获取所在OLT下的CM列表数量
        return cmDao.getCmListByOltCount(cmId);
    }

    @Override
    public List<CmAttribute> getCmListByPon(Long cmId, Integer start, Integer limit) {
        // 调用Dao中方法获取所在Pon口下的CM列表
        return cmDao.getCmListByPon(cmId, start, limit);
    }

    @Override
    public Long getCmListByPonCount(Long cmId) {
        // 调用Dao中方法获取所在PON口下CM列表数量
        return cmDao.getCmListByPonCount(cmId);
    }

    @Override
    public List<CmAttribute> getCmListByCmc(Long cmId, Integer start, Integer limit) {
        // 调用Dao中方法获取所在CMC下的CM列表
        return cmDao.getCmListByCmc(cmId, start, limit);
    }

    @Override
    public Long getCmListByCmcCount(Long cmId) {
        // 调用Dao中方法获取所在CMC下的CM列表数量
        return cmDao.getCmListByCmcCount(cmId);
    }

    @Override
    public List<CmAttribute> getCmListByUpPortId(Long cmId, Integer start, Integer limit) {
        // 调用Dao中方法获取所在上行通道下的CM列表
        return cmDao.getCmListByUpPortId(cmId, start, limit);
    }

    @Override
    public Long getCmListByUpPortIdCount(Long cmId) {
        // 调用Dao中方法获取所在上行通道下的CM列表数量
        return cmDao.getCmListByUpPortIdCount(cmId);
    }

    @Override
    public List<CmAttribute> getCmListByDownPortId(Long cmId, Integer start, Integer limit) {
        // 调用Dao中方法获取所在下行通道上的CM列表
        return cmDao.getCmListByDownPortId(cmId, start, limit);
    }

    @Override
    public Long getCmListByDownPortIdCount(Long cmId) {
        // 调用Dao中方法获取所在下行通道上的CM列表数量
        return cmDao.getCmListByDownPortIdCount(cmId);
    }

    @Override
    public List<CmAttribute> getCmListByCmcId(Long cmcId, Integer start, Integer limit, UserContext uc) {
        List<CmAttribute> cmList = cmDao.getCmListByCmcId(cmcId, start, limit);
        for (CmAttribute cmAttribute : cmList) {
            List<DocsIf3CmtsCmUsStatus> docsIf3CmtsCmUsStatusList = cmDao
                    .queryDocsIf3CmtsCmUsStatusList(cmAttribute.getCmId());
            cmAttribute.setDocsIf3CmtsCmUsStatusList(docsIf3CmtsCmUsStatusList);
            // 处理CM实时信号最后刷新时间，YangYi 2013-11-02
            if (cmAttribute.getCollectTime() != null && uc != null) {
                cmAttribute.setLastRefreshTime(DateUtils.getTimeDesInObscure(
                        System.currentTimeMillis() - cmAttribute.getCollectTime().getTime(),
                        uc.getUser().getLanguage()));
            }
        }
        return cmList;
    }

    @Override
    public CmTopologyInfo getTopologyInfo(Long cmId) {
        // 获取CM的拓扑信息
        Long typeId = cmDao.getDeviceTypeByCmId(cmId);
        CmTopologyInfo cmtopologyInfo = null;
        if (entityTypeService.isCcmtsWithAgent(typeId)) {
            cmtopologyInfo = cmDao.get8800BTopologyInfo(cmId);
            // modify by loyal 添加cmts支持
        } else if (entityTypeService.isCmts(typeId)) {
            cmtopologyInfo = cmDao.getCmtsTopologyInfo(cmId);
            cmtopologyInfo.setDeviceType(typeId);
            return cmtopologyInfo;
        } else {
            cmtopologyInfo = cmDao.getTopologyInfo(cmId);
        }
        cmtopologyInfo.setDeviceType(typeId);
        // 之前获取上行信道ID的方法，现在要兼容3.0cm，修改获取方式
        cmtopologyInfo.setDocsIfUpChannelId(cmDao.getUpChannelId(cmId));
        List<Long> uList = cmDao.getUpChannelIdList(cmId);
        cmtopologyInfo.setUpChannelIdList(uList);
        cmtopologyInfo.setDocsIfDownChannelId(cmDao.getDownChannelId(cmId));
        return cmtopologyInfo;
    }

    @Override
    public List<CmcQosServiceFlowInfo> getCmServiceFlowListInfo(String cmMac) {
        // 调用Dao中方法获取 CM关联的服务流信息
        return cmDao.getCmServiceFlowListInfo(cmMac);
    }

    @Override
    public Integer resetCm(Long cmId) {
        // TODO 将数据库中对应Id的数据删除--防止有的CM没上线但是在页面展示
        CmAttribute cm = cmDao.getCmAttributeByCmId(cmId);
        long cmcId = cm.getCmcId();
        long cmIndex = cm.getStatusIndex();
        CmAttribute cmAttribute = refreshCmInfo(cmcId, cmIndex, cm.getStatusMacAddress());
        if (cmAttribute == null || !cmAttribute.isCmOnline()) {
            return 1;// CM已经不在其连接的CC上
        }
        try {
            SnmpParam snmpParam = getSnmpParamByCmcId(cmcId);
            CmFacade cmFacade = getCmFacade(snmpParam.getIpAddress());
            cmFacade.resetCmFromCmc(snmpParam, cmIndex);
            // add by fanzidong,重启CM，需要将数据库中对应的CM置为下线
            cmDao.updateStatusValue(cmId, CmAttribute.OFFLINESTATUS);
            return 0;
        } catch (Exception e) {
            logger.error("", e);
            return 3;
        }
    }

    @Override
    public Integer clearSingleCM(Long cmId) {
        CmAttribute cm = cmDao.getCmAttributeByCmId(cmId);
        long cmcId = cm.getCmcId();
        long cmIndex = cm.getStatusIndex();
        try {
            SnmpParam snmpParam = getSnmpParamByCmcId(cmcId);
            CmFacade cmFacade = getCmFacade(snmpParam.getIpAddress());
            cmFacade.clearSingleCmFromCmc(snmpParam, cmIndex);
            return 0;
        } catch (SnmpSetException snmpSetException) {
            logger.error("", snmpSetException);
            return 1;
        } catch (SnmpException snmpException) {
            logger.error("", snmpException);
            return 2;
        } catch (Exception e) {
            logger.error("", e);
            return 3;
        }
    }

    @Override
    public Long getCmNumByStatus(Map<String, Object> map) {
        return cmDao.getCmNumByStatus(map);
    }

    @Override
    public Long getCmtsCmNumByStatus(Map<String, Object> map) {
        return cmDao.getCmtsCmNumByStatus(map);
    }

    @Override
    public List<CpeAttribute> getCpeListByCmId(Long cmId) {
        return cmDao.getCpeListByCmId(cmId);
    }

    @Override
    public String getRealIpNum(Long cmId) {
        Integer realCpes = cmDao.getRealIpNum(cmId);
        return realCpes.toString();
    }

    @Override
    public String getCpeMaxIpNum(Long cmId) {
        Integer maxCpes = cmDao.getCpeMaxIpNum(cmId);
        return maxCpes.toString();
    }

    @Override
    public CmAttribute refreshCmInfo(Long cmId) {
        CmAttribute cmAttribute = cmDao.getCmAttributeByCmId(cmId);
        return refreshCmInfo(cmAttribute.getCmcId(), cmAttribute.getStatusIndex(), cmAttribute.getStatusMacAddress());
    }

    /**
     * 
     * @param cmcId
     *            Long
     * @param cmIndex
     *            Long
     * @param cmMac
     *            String
     * @return CmAttribute
     */
    private CmAttribute refreshCmInfo(Long cmcId, Long cmIndex, String cmMac) {
        SnmpParam snmpParam = getSnmpParamByCmcId(cmcId);
        CmAttribute cmAttribute = getCmFacade(snmpParam.getIpAddress()).getCmAttributeOnDol(snmpParam, cmIndex, cmMac);
        if (cmAttribute != null) {
            cmAttribute.setCmcId(cmcId);
            cmDao.updateCmAttribute(cmAttribute);
        }
        return cmAttribute;
    }

    @Override
    public void refreshCmOnCcmtsInfo(Long cmcId) {
        CmcAttribute cmcAttribute = cmcDao.getCmcAttributeByCmcId(cmcId);
        long entityId = cmcDao.getEntityIdByCmcId(cmcId);
        if (entityTypeService.isCcmtsWithoutAgent(cmcAttribute.getCmcDeviceStyle())) {
            snmpParam = getSnmpParamByEntityId(entityId);
        } else if (entityTypeService.isCcmtsWithAgent(cmcAttribute.getCmcDeviceStyle())) {
            snmpParam = getSnmpParamByCmcId(cmcId);
        }
        List<CmAttribute> cmAttributes = getCmFacade(snmpParam.getIpAddress()).getCmAttributeInfos(snmpParam);
        cmRefreshDao.batchRefreshCmAttribute(entityId, cmcId, cmAttributes);
    }

    @Override
    public Long getEntityIdByCmcId(Long cmcId) {
        return cmDao.getEntityIdByCmcId(cmcId);
    }

    public List<CmAttribute> refreshCmtsContactedCmList(Long cmcId) {
        long entityId = cmcDao.getEntityIdByCmcId(cmcId);
        List<CmAttribute> cmAttributeList = new ArrayList<CmAttribute>();
        snmpParam = getSnmpParamByCmcId(entityId);
        snmpParam.setMibs("RFC1213-MIB,DOCS-IF-MIB;DOCS-QOS-MIB");
        cmAttributeList = getCmFacade(snmpParam.getIpAddress()).getCmAttributeInfos(snmpParam);
        cmcDiscoveryDao.batchInsertCmAttribute8800b(cmAttributeList, cmcId, entityId);
        refreshCmUsStatus(cmAttributeList, snmpParam);
        return cmAttributeList;
    }

    private void refreshCmUsStatus(List<CmAttribute> cms, SnmpParam snmpParam) {
        List<DocsIf3CmtsCmUsStatus> cmUses = getCmFacade(snmpParam.getIpAddress()).getCmUsStatus(cms, snmpParam);
        cmcDiscoveryDao.batchInsertOrUpdateDocsIf3CmtsCmUsStatus(cmUses, snmpParam.getEntityId());
    }

    @Override
    public CmAttribute getCmAttributeByCmId(Long cmId) {
        return cmDao.getCmAttributeByCmId(cmId);
    }

    @Override
    public List<CmAttribute> queryCmListByCmMacs(List<Long> macs, Integer start, Integer limit) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < macs.size(); i++) {
            // modify by fanzidong
            String formattedMac = new MacUtils(macs.get(i)).toString(MacUtils.MAOHAO).toUpperCase();
            formattedMac = MacUtils.convertToMaohaoFormat(formattedMac);
            sb.append("'").append(formattedMac).append("'").append(",");
            /*
             * sb.append("'").append(new
             * MacUtils(macs.get(i)).toString(MacUtils.MAOHAO).toUpperCase()).append("'")
             * .append(",");
             */
        }
        String macsString = sb.toString();
        macsString = sb.toString().substring(0, sb.toString().length() - 1);
        return cmDao.selectCmListByCmMacs(macsString, start, limit);
    }

    @Override
    public boolean checkCmReachable(String ip) {
        // modify by loyal 添加非空判断
        if (!"".equals(ip) && ip != null) {
            PingResult r = new PingResult();
            PingWorker worker = new PingWorker(ip, r);
            Future<PingResult> f = pingExecutorService.submit(worker, r);
            try {
                return f.get().available();
            } catch (InterruptedException e) {
                logger.debug(e.toString(), e);
            } catch (ExecutionException e) {
                logger.debug(e.toString(), e);
            }
        }
        return false;
    }

    @Override
    public boolean checkCmSnmp(String cmIp, String readCommunity, String writeCommunity) {
        SnmpParam snmpParam = new SnmpParam();
        snmpParam.setIpAddress(cmIp);
        snmpParam.setCommunity(readCommunity);
        snmpParam.setWriteCommunity(writeCommunity);
        snmpParam.setTimeout(5000);

        try {
            getCmFacade(cmIp).checkCmSnmp(snmpParam);
            return true;
        } catch (Exception e) {
            logger.debug("", e);
        }
        return false;
    }

    @Override
    public List<CmCpe> getCmCpeList(Long cmId) {
        return cmDao.queryCmCpeList(cmId);
    }

    @Override
    public List<CmStaticIp> getCmStaticIpList(Long cmId) {
        return cmDao.queryCmStaticIpList(cmId);
    }

    @Override
    public List<CmAct> getCmActionInfo(Map<String, Object> map) {
        return cmDao.selectCmActionInfo(map);
    }

    @Override
    public List<CpeAct> getCpeActionInfoByCmMac(Map<String, Object> map) {
        return cmDao.selectCpeActionInfoByCmMac(map);
    }

    @Override
    public void clearCpeInfo(Long cmId, String cpeMac) {
        long cmcId = cmDao.getCmAttributeByCmId(cmId).getCmcId();
        snmpParam = this.getSnmpParamByCmcId(cmcId);
        getCmFacade(snmpParam.getIpAddress()).clearCpe(snmpParam, cpeMac);
        cmDao.deleteCmCpe(cmId, cpeMac);

    }

    /**
     * 解析TLV
     * 
     * @param tlv
     *            以冒号隔开的十六进制数“01:01:01”
     * @return Sting[0]:类型，String[1]值
     */
    public List<String[]> getValueFromTlv(String tlv) {
        List<String[]> list = new ArrayList<String[]>();
        String[] tlvArry = tlv.split(":");
        for (int i = 0; i < tlvArry.length;) {
            String[] s = new String[2];
            s[0] = tlvArry[i];
            int length = Integer.parseInt(tlvArry[i + 1], 16);
            StringBuilder sb = new StringBuilder();
            for (int j = i + 2; j < i + 2 + length; j++) {
                sb.append(tlvArry[j]).append(":");
            }
            s[1] = sb.substring(0, sb.length() - 1);
            list.add(s);
            i = i + 2 + length;
        }
        return list;
    }

    /**
     * 返回3.0CM的注册模式
     * 
     * @param tlv
     * @return -1：未找到注册模式的TLV，0：DOCSIS 1.0， 1：DOCSIS1.1 2：DOCSIS 2.0， 3：DOCSIS 3.0，4-255保留位
     */
    public int getCmDocsisRegVersion(String tlv) {
        List<String[]> list = getValueFromTlv(tlv);
        // 如果最外层不是只有一个TLV，并且该类型不是‘05’则返回异常数据
        if (list.size() != 1 || !list.get(0)[0].equals("05")) {
            return -1;
        }
        List<String[]> tlvList = getValueFromTlv(list.get(0)[1]);
        // 查找DOCSIS 注册模式type = '02'
        for (int i = 0; i < tlvList.size(); i++) {
            if (tlvList.get(i)[0].equals("02")) {
                return Integer.parseInt(tlvList.get(i)[1]);
            }
        }
        return -1;
    }

    public Long getCmcIdByCmId(Long cmId) {
        return cmDao.getCmcIdByCmId(cmId);
    }

    public void setSnmpParam(SnmpParam snmpParam) {
        this.snmpParam = snmpParam;
    }

    @SuppressWarnings("unused")
    private void refreshCmStaticIp(Long entityId) {
        snmpParam = getSnmpParamByEntityId(entityId);
        List<CmStaticIp> cmStaticIpList = getCmFacade(snmpParam.getIpAddress()).getCmStaticIp(snmpParam);
        if (cmStaticIpList != null) {
            cmcDiscoveryDao.batchInsertOrUpdateCmStaticIp(cmStaticIpList, entityId);
        }
    }

    @SuppressWarnings("unused")
    private void refreshCmCpe(Long entityId) {
        snmpParam = getSnmpParamByEntityId(entityId);
        List<CmCpe> CmCpeList = getCmFacade(snmpParam.getIpAddress()).getCmCpe(snmpParam);
        if (CmCpeList != null) {
            cmcDiscoveryDao.batchInsertOrUpdateCmCpe(CmCpeList, entityId);
        }
    }

    @SuppressWarnings("unused")
    private void refreshDocsIf3CmtsCmUsStatus(Long entityId) {
        snmpParam = getSnmpParamByEntityId(entityId);
        List<DocsIf3CmtsCmUsStatus> docsIf3CmtsCmUsStatusList = getCmFacade(snmpParam.getIpAddress())
                .getDocsIf3CmtsCmUsStatusList(snmpParam);
        if (docsIf3CmtsCmUsStatusList != null) {
            cmcDiscoveryDao.batchInsertOrUpdateDocsIf3CmtsCmUsStatus(docsIf3CmtsCmUsStatusList, entityId);
        }
    }

    @Override
    public void saveCmSignal(CmStatus cmStatus) {
        cmDao.insertOrUpdateCmSignal(cmStatus);
    }

    @Override
    public void refreshSingleCmAttribute(CmAttribute cmAttribute) {
        // 更新cmattribute表
        cmDao.updateCmAttribute(cmAttribute);
        // 从cmcupchannelbaseinfo和cmcdownchannelbaseinfo中获取upportId和downPortId
        Long upPortId = cmcUpChannelDao.getPortId(cmAttribute.getCmcId(), cmAttribute.getStatusUpChannelIfIndex());
        Long downPortId = cmcDownChannelDao.getPortId(cmAttribute.getCmcId(),
                cmAttribute.getStatusDownChannelIfIndex());
        // 更新cmccmrelation表中的upportId和downPortId
        cmRefreshDao.refreshCmPortId(upPortId, downPortId,
                MacUtils.convertToMaohaoFormat(cmAttribute.getStatusMacAddress()));
    }

    public void updateCmAttribute(Long entityId, String cmmac, Integer status) {
        Long cmcId = cmDao.selectcmcIdByEntityIdAndCmmac(entityId, cmmac);
        cmDao.updateCmStatusValue(cmcId, new MacUtils(cmmac).toString(MacUtils.MAOHAO), status);

    }

    @Override
    public CmAttribute getPreviousStateById(Long cmId) {
        return cmDao.getPreviousStateById(cmId);
    }

    @Override
    public List<Cm3Signal> getUpChannelSignalByCmId(Long cmId) {
        return cmDao.getUpChannelSignalByCmId(cmId);
    }

    @Override
    public List<Cm3Signal> getDownChannelSignalByCmId(Long cmId) {
        return cmDao.getDownChannelSignalByCmId(cmId);
    }

    @Override
    public void deleteCmCleared(List<Long> cmId) {
        cmDao.deleteCmCleared(cmId);
    }

    public void deleteCmClearedOne(Long cmId) {
        cmDao.deleteCmClearedOne(cmId);
    }

}