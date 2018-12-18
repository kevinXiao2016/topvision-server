package com.topvision.ems.cmc.downchannel.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.downchannel.domain.*;
import com.topvision.framework.snmp.SnmpParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.ccmts.dao.CmcChannelDao;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcPort;
import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.ems.cmc.domain.CmcDownChannelBaseShowInfo;
import com.topvision.ems.cmc.downchannel.dao.CmcDownChannelDao;
import com.topvision.ems.cmc.downchannel.facade.CmcIpqamCollectFacade;
import com.topvision.ems.cmc.downchannel.service.CmcDownChannelService;
import com.topvision.ems.cmc.exception.SetValueFailException;
import com.topvision.ems.cmc.facade.CmcFacade;
import com.topvision.ems.cmc.facade.domain.CmcDownChannelBaseInfo;
import com.topvision.ems.cmc.facade.domain.CmcDownChannelStaticInfo;
import com.topvision.ems.cmc.service.impl.CmcBaseCommonService;
import com.topvision.ems.cmc.topology.dao.CmcDiscoveryDao;
import com.topvision.ems.cmc.util.CmcConstants;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.http.HttpParam;
import com.topvision.platform.message.event.CmcSynchronizedEvent;
import com.topvision.platform.message.event.CmcSynchronizedListener;
import com.topvision.platform.message.service.MessageService;

@Service("cmcDownChannelService")
public class CmcDownChannelServiceImpl extends CmcBaseCommonService
        implements CmcDownChannelService, CmcSynchronizedListener {
    @Autowired
    private CmcDownChannelDao cmcDownChannelDao;
    @Autowired
    private CmcChannelDao cmcChannelDao;
    @Autowired
    private CmcDiscoveryDao cmcDiscoveryDao;
    @Autowired
    private CmcService cmcService;
    @Autowired
    private EntityTypeService entityTypeService;
    @Autowired
    private EntityService entityService;
    @Autowired
    private MessageService messageService;

    @PreDestroy
    public void destroy() {
        super.destroy();
        messageService.removeListener(CmcSynchronizedListener.class, this);
    }

    @PostConstruct
    public void initialize() {
        super.initialize();
        messageService.addListener(CmcSynchronizedListener.class, this);
    }

    @Override
    public List<TxPowerLimit> getDownChannelTxPowerLimt(Long cmcId, Long cmcIndex) {
        return cmcDownChannelDao.getTxPowerLimit(cmcId, cmcIndex);
    }

    @Override
    public void refreshDownChannelBaseInfo(Long cmcId, Integer productType) {
        Long entityId;
        if (entityTypeService.isCcmtsWithoutAgent(productType.longValue())) {
            entityId = getEntityIdByCmcId(cmcId);
            refreshCC8800ADownChannelBaseInfo(cmcId, entityId);
        } else if (entityTypeService.isCcmtsWithAgent(productType.longValue())) {
            refreshCC8800BDownChannelBaseInfo(cmcId);
            refreshCC8800BIPQAMBaseInfo(cmcId);
        } else if (entityTypeService.isCmts(productType.longValue())) {
            refreshCmtsDownChannelBaseInfo(cmcId);
        }
    }

    private void refreshCC8800ADownChannelBaseInfo(Long cmcId, Long entityId) {
        snmpParam = getSnmpParamByEntityId(entityId);
        Long cmcIndex = cmcDao.getCmcIndexByCmcId(cmcId);
        List<CmcDownChannelBaseInfo> cmcDownChannelBaseInfos = getCmcFacade(snmpParam.getIpAddress())
                .getDownChannelBaseInfo(snmpParam, cmcIndex);
        List<CmcPort> cmcPorts = getCmcFacade(snmpParam.getIpAddress()).getCmcPorts(snmpParam, cmcIndex);
        cmcDiscoveryDao.batchInsertCmcDownChannelBaseInfo(cmcDownChannelBaseInfos, cmcId);
        cmcDiscoveryDao.batchInsertCmcPortInfo(cmcPorts, cmcId);
    }

    private void refreshCC8800BDownChannelBaseInfo(Long cmcId) {
        snmpParam = getSnmpParamByCmcId(cmcId);
        CmcFacade cmcFacade = getCmcFacade(snmpParam.getIpAddress());
        List<CmcDownChannelBaseInfo> cmcDownChannelBaseInfos = cmcFacade.getCC8800BDownChannelBaseInfo(snmpParam);
        List<CmcPort> cmcPorts = cmcFacade.getCmc8800BPorts(snmpParam);
        cmcDiscoveryDao.batchInsertCmcDownChannelBaseInfo(cmcDownChannelBaseInfos, cmcId);
        cmcDiscoveryDao.batchInsertCmcPortInfo(cmcPorts, cmcId);
    }

    private void refreshCC8800BIPQAMBaseInfo(Long cmcId) {
        snmpParam = getSnmpParamByCmcId(cmcId);
        HttpParam httpParam = new HttpParam();
        httpParam.setIpAddress(snmpParam.getIpAddress());
        CmcIpqamCollectFacade httpFacade = getFacade(snmpParam.getIpAddress(), CmcIpqamCollectFacade.class);
        List<CmcDSIpqamBaseInfo> ipqamBaseList = httpFacade.getDSChannelIpqamBaseList(httpParam);
        cmcDownChannelDao.batchInsertCC8800BDSIPQAMBaseList(ipqamBaseList, cmcId);
    }

    private void refreshCmtsDownChannelBaseInfo(Long cmcId) {
        snmpParam = getSnmpParamByCmcId(cmcId);
        CmcFacade cmcFacade = getCmcFacade(snmpParam.getIpAddress());

        List<CmcDownChannelBaseInfo> cmcDownChannelBaseInfos = cmcFacade.getCC8800BDownChannelBaseInfo(snmpParam);
        List<com.topvision.ems.cmc.ccmts.facade.domain.CmcPort> cmcPorts = cmcFacade.getCmc8800BPorts(snmpParam);
        cmcDiscoveryDao.batchInsertCmcDownChannelBaseInfo(cmcDownChannelBaseInfos, cmcId);
        Entity entity = entityService.getEntity(cmcId);
        if (entityTypeService.isUbrCmts(entity.getTypeId())) {
            // UBR7225上行信道类型为129
            cmcDiscoveryDao.batchInsertCmcPortInfo(cmcPorts, cmcId);
        } else {
            cmcDiscoveryDao.batchInsertCmcChannelInfo(cmcPorts, cmcId);
        }
        cmcDiscoveryDao.batchInsertCmtsPorts(cmcId, cmcPorts);// 将iftable所有信息插入端口表（port）
    }

    @Override
    public void refreshDownChannelStaticInfo(Long cmcId, Integer productType) {
        Long entityId;
        if (entityTypeService.isCcmtsWithoutAgent(productType.longValue())) {
            entityId = getEntityIdByCmcId(cmcId);
            refreshCC8800ADownChannelStaticInfo(cmcId, entityId);
        } else if (entityTypeService.isCcmtsWithAgent(productType.longValue())) {
            refreshCC8800BDownChannelStaticInfo(cmcId);
        } else if (entityTypeService.isCmts(productType.longValue())) {
            refreshCC8800BDownChannelStaticInfo(cmcId);
        }
    }

    private void refreshCC8800ADownChannelStaticInfo(Long cmcId, Long entityId) {
        snmpParam = getSnmpParamByEntityId(entityId);
        Long cmcIndex = cmcDao.getCmcIndexByCmcId(cmcId);
        List<CmcDownChannelStaticInfo> cmcDownChannleStaticInfos = getCmcFacade(snmpParam.getIpAddress())
                .getCmcDownChannelStaticInfo(snmpParam, cmcIndex);
        cmcDownChannelDao.batchInsertCmcDownChannelStaticInfo(cmcDownChannleStaticInfos, cmcId);
    }

    private void refreshCC8800BDownChannelStaticInfo(Long cmcId) {
        snmpParam = getSnmpParamByCmcId(cmcId);
        List<CmcDownChannelStaticInfo> cmcDownChannleStaticInfos = getCmcFacade(snmpParam.getIpAddress())
                .getCC8800BCmcDownChannelStaticInfo(snmpParam);
        cmcDownChannelDao.batchInsertCmcDownChannelStaticInfo(cmcDownChannleStaticInfos, cmcId);
        snmpParam.setTimeout(2000L);
    }

    @Override
    public List<CmcDownChannelBaseShowInfo> getDownChannelBaseShowInfoList(Long cmcId) {
        List<CmcDownChannelBaseShowInfo> allChannel = cmcDownChannelDao.getDownChannelBaseShowInfoList(cmcId);
        return allChannel;

    }

    @Override
    public CmcDownChannelBaseShowInfo getDownChannelBaseShowInfo(CmcAttribute cmcAttribute, Long cmcPortId) {
        CmcDownChannelBaseShowInfo cmcDownChannelBaseShowInfo = cmcDownChannelDao.getDownChannelBaseShowInfo(cmcPortId);
        if (cmcAttribute != null) {
            try {
                List<CmcDownChannelBaseShowInfo> cmcDownChannelBaseShowInfoList = cmcDownChannelDao
                        .getDownChannelBaseShowInfoList(cmcAttribute.getCmcId());
                Integer num = 0;
                for (CmcDownChannelBaseShowInfo downChannelBaseShowInfo : cmcDownChannelBaseShowInfoList) {
                    if (CmcConstants.IFOPERSTATUS_UP.equals(downChannelBaseShowInfo.getIfAdminStatus())) {
                        num++;
                    }
                }
                List<TxPowerLimit> txPowerLimits = cmcDownChannelDao.getTxPowerLimit(cmcAttribute.getEntityId(),
                        cmcAttribute.getCmcIndex());
                for (TxPowerLimit txPowerLimit : txPowerLimits) {
                    if (num.equals(txPowerLimit.getChannelNum())) {
                        cmcDownChannelBaseShowInfo.setTxPowerLimit(txPowerLimit);
                    }
                }
            } catch (Exception e) {
                logger.debug("", e);
            }
        }
        return cmcDownChannelBaseShowInfo;
    }

    @Override
    public void modifyDownChannelBaseShowInfo(CmcDownChannelBaseShowInfo cmcDownChannelBaseShowInfo,
            Integer productType) {
        // modified by huangdongsheng
        // @date 2013-8-10
        Long cmcId = cmcDownChannelBaseShowInfo.getCmcId();
        Long ifIndex = cmcDownChannelBaseShowInfo.getChannelIndex();
        Long portId = cmcDownChannelBaseShowInfo.getCmcPortId();
        Integer ifAdminStatus = cmcDownChannelBaseShowInfo.getIfAdminStatus();
        snmpParam = getSnmpParamByCmcId(cmcId);
        // ifTable
        CmcPort cmcPort = new CmcPort();
        cmcPort.setCmcPortId(portId);
        cmcPort.setCmcId(cmcId);
        cmcPort.setIfIndex(ifIndex);
        cmcPort.setIfAdminStatus(ifAdminStatus);
        // 下行
        CmcDownChannelBaseInfo cmcDownChannelBaseInfo = new CmcDownChannelBaseInfo();
        cmcDownChannelBaseInfo.setCmcPortId(cmcDownChannelBaseShowInfo.getCmcPortId());
        cmcDownChannelBaseInfo.setCmcId(cmcDownChannelBaseShowInfo.getCmcId());
        cmcDownChannelBaseInfo.setChannelIndex(cmcDownChannelBaseShowInfo.getChannelIndex());
        cmcDownChannelBaseInfo
                .setDocsIfDownChannelFrequency(cmcDownChannelBaseShowInfo.getDocsIfDownChannelFrequency());
        cmcDownChannelBaseInfo.setDocsIfDownChannelPower(cmcDownChannelBaseShowInfo.getDocsIfDownChannelPower());
        cmcDownChannelBaseInfo
                .setDocsIfDownChannelModulation(cmcDownChannelBaseShowInfo.getDocsIfDownChannelModulation());
        cmcDownChannelBaseInfo.setDocsIfDownChannelWidth(cmcDownChannelBaseShowInfo.getDocsIfDownChannelWidth());
        cmcDownChannelBaseInfo
                .setDocsIfDownChannelInterleave(cmcDownChannelBaseShowInfo.getDocsIfDownChannelInterleave());
        /*
         * 由于Annex不可更改，仅作更新数据库使用。 要求：必须在传入数据前将Annex变更,或取消设置，由类对象进行转换
         * 
         * @added by bryan 2013年12月6日10:43:45
         */
        cmcDownChannelBaseInfo.setDocsIfDownChannelAnnex(cmcDownChannelBaseShowInfo.getDocsIfDownChannelAnnex());
        // 去掉管理状态的设置 避免管理状态变化导致电平可配置范围出现变化 modify by jay
        modifyCmcDownChannelBaseInfo(cmcDownChannelBaseInfo);

        // 当刷新信道基本信息时发生异常，则不认为设置失败，仅记录log
        try {
            refreshDownChannelBaseInfo(cmcDownChannelBaseShowInfo.getCmcId(), productType);
        } catch (Exception e) {
            logger.info("Refresh DonwchannelBaseInfo ", e);
        }
    }

    /**
     * 修改下行信道基本信息 added by huangdongsheng
     * 
     * @date 2013-8-10
     * @param cmcDownChannelBaseInfo
     */
    private void modifyCmcDownChannelBaseInfo(CmcDownChannelBaseInfo cmcDownChannelBaseInfo) {
        int ifIndex = cmcDownChannelBaseInfo.getChannelIndex().intValue();
        StringBuilder sBuilder = new StringBuilder();
        CmcDownChannelBaseInfo setting = new CmcDownChannelBaseInfo();
        setting.setChannelIndex((long) ifIndex);
        CmcDownChannelBaseInfo current = getCmcFacade(snmpParam.getIpAddress()).getCmcDownChannelBaseInfo(snmpParam,
                ifIndex);
        // Frequency
        Long frequency = cmcDownChannelBaseInfo.getDocsIfDownChannelFrequency();
        if (frequency != null && current.getDocsIfDownChannelFrequency() != frequency.longValue()) {
            setting.setDocsIfDownChannelFrequency(frequency);
        }
        // Power
        Long power = cmcDownChannelBaseInfo.getDocsIfDownChannelPower();
        if (power != null && current.getDocsIfDownChannelPower() != power.longValue()) {
            setting.setDocsIfDownChannelPower(power);
        }
        // Modulation
        Integer modulation = cmcDownChannelBaseInfo.getDocsIfDownChannelModulation();
        if (modulation != null && current.getDocsIfDownChannelModulation() != modulation.intValue()) {
            setting.setDocsIfDownChannelModulation(modulation);
        }
        // Width
        Integer downChannelWidth = cmcDownChannelBaseInfo.getDocsIfDownChannelWidth();
        if (downChannelWidth != null && downChannelWidth != null
                && current.getDocsIfDownChannelWidth() != downChannelWidth.intValue()) {
            setting.setDocsIfDownChannelWidth(downChannelWidth);
        }
        // InterLeave
        Integer interLeave = cmcDownChannelBaseInfo.getDocsIfDownChannelInterleave();
        if (interLeave != null && interLeave.intValue() != current.getDocsIfDownChannelInterleave()) {
            // 当频宽为8时，交织深度必须设置成null才能下发成功，当频宽为6时，可以设置交织深度的值下发，故此处做特殊处理
            if (downChannelWidth == CmcConstants.ANNEX_A.intValue()) {
                setting.setDocsIfDownChannelInterleave(null);
            } else {
                setting.setDocsIfDownChannelInterleave(interLeave);

            }
        }
        CmcDownChannelBaseInfo cmcDownChannelBaseInfoAfterModified = getCmcFacade(snmpParam.getIpAddress())
                .modifyDownChannelBaseInfo(snmpParam, setting);
        if (cmcDownChannelBaseInfoAfterModified.getDocsIfDownChannelFrequency() != null
                && !cmcDownChannelBaseInfoAfterModified.getDocsIfDownChannelFrequency()
                        .equals(cmcDownChannelBaseInfo.getDocsIfDownChannelFrequency())) {
            sBuilder.append(getString("cmc.message.cmc.setDocsIfDownChannelFrequency"));
        }

        if (cmcDownChannelBaseInfoAfterModified.getDocsIfDownChannelModulation() != null
                && !cmcDownChannelBaseInfoAfterModified.getDocsIfDownChannelModulation()
                        .equals(cmcDownChannelBaseInfo.getDocsIfDownChannelModulation())) {
            sBuilder.append(getString("cmc.message.cmc.setDocsIfDownChannelModulation"));
        }
        if (cmcDownChannelBaseInfoAfterModified.getDocsIfDownChannelPower() != null
                && !cmcDownChannelBaseInfoAfterModified.getDocsIfDownChannelPower()
                        .equals(cmcDownChannelBaseInfo.getDocsIfDownChannelPower())) {
            sBuilder.append(getString("cmc.message.cmc.setDocsIfDownChannelPower"));
        }
        if (cmcDownChannelBaseInfoAfterModified.getDocsIfDownChannelWidth() != null
                && !cmcDownChannelBaseInfoAfterModified.getDocsIfDownChannelWidth()
                        .equals(cmcDownChannelBaseInfo.getDocsIfDownChannelWidth())) {
            sBuilder.append(getString("cmc.message.cmc.setDocsIfDownChannelWidth"));
        }
        if (cmcDownChannelBaseInfoAfterModified.getDocsIfDownChannelInterleave() != null
                && !cmcDownChannelBaseInfoAfterModified.getDocsIfDownChannelInterleave()
                        .equals(cmcDownChannelBaseInfo.getDocsIfDownChannelInterleave())) {
            sBuilder.append(getString("cmc.message.cmc.setDocsIfDownChannelWidth"));
        }
        if (sBuilder.length() > 0) {
            throw new SetValueFailException(sBuilder.toString());
        }
        cmcDownChannelDao.updateDownChannelBaseInfo(cmcDownChannelBaseInfo);
    }

    /**
     * 修改端口管理状态 added by huangdongsheng
     * 
     * @date 2013-8-10
     * @param cmcPort
     *            设置参数
     * @param ifIndex
     *            端口ifIndex
     */
    private void modifyCmcPort(CmcPort cmcPort, Long ifIndex) {
        CmcPort currentCmcPort = new CmcPort();
        currentCmcPort.setIfIndex(ifIndex);
        currentCmcPort = getCmcFacade(snmpParam.getIpAddress()).getCmcPortByIfIndex(snmpParam, currentCmcPort);
        if (!currentCmcPort.getIfAdminStatus().equals(cmcPort.getIfAdminStatus())) {
            StringBuilder sBuilder = new StringBuilder();
            CmcPort r = getCmcFacade(snmpParam.getIpAddress()).modifyCmcPortInfo(snmpParam, cmcPort);
            if (!r.getIfAdminStatus().equals(cmcPort.getIfAdminStatus())) {
                sBuilder.append(getString("cmc.message.cmc.setIfAdminStatus"));
            }
            if (sBuilder.length() > 0) {
                throw new SetValueFailException(sBuilder.toString());
            }
            // TODO 修改CmcChannelDao，重构
            cmcChannelDao.updateCmcPort(cmcPort);
        }
    }

    @Override
    public List<CmcDownChannelStaticInfo> getDownChannelStaticInfoList(Long cmcId) {
        return cmcDownChannelDao.getDownChannelStaticInfoList(cmcId);
    }

    @Override
    public List<CmcDownChannelBaseShowInfo> getDownChannelListByPortId(Long cmcPortId) {
        return cmcDownChannelDao.getDownChannelListByPortId(cmcPortId);
    }

    @Override
    public Long getDownChannelAdminStatusUpNum(Long cmcId) {
        return cmcDownChannelDao.getDownChannelAdminStatusUpNum(cmcId);
    }

    @Override
    public List<CmcDSIpqamBaseInfo> getDownChannelIPQAMInfoList(Long cmcId) {
        return cmcDownChannelDao.getDownChannelIPQAMInfoList(cmcId);
    }

    @Override
    public CmcDSIpqamBaseInfo getDownChannelIPQAMInfo(Long cmcPortId) {
        return cmcDownChannelDao.getDownChannelIPQAMInfo(cmcPortId);
    }

    @Override
    public Integer getDownChannelIPQAMListSize(Long cmcId) {
        return cmcDownChannelDao.getDownChannelIPQAMListSize(cmcId);
    }

    @Override
    public String setChannelsAdminStatus(Long cmcId, Integer channelIds, Integer status) {
        snmpParam = getSnmpParamByCmcId(cmcId);
        String ip = snmpParam.getIpAddress();
        CmcIpqamCollectFacade httpFacade = getFacade(ip, CmcIpqamCollectFacade.class);
        String result = "ERROR";
        if (status == 3) {// IPQAM信道
            HttpParam httpParam = new HttpParam();
            httpParam.setIpAddress(ip);
            result = httpFacade.modifyChannelsAdminStatus(httpParam, channelIds, status);
            List<CmcDSIpqamBaseInfo> ipqamBaseList = httpFacade.getDSChannelIpqamBaseList(httpParam);
            cmcDownChannelDao.batchInsertCC8800BDSIPQAMBaseList(ipqamBaseList, cmcId);
        }
        return result;
    }

    @Override
    public String modifyChannelIpqamBaseInfo(Long cmcId, CmcDSIpqamBaseInfo ipqam) {
        snmpParam = getSnmpParamByCmcId(cmcId);
        String ip = snmpParam.getIpAddress();
        HttpParam httpParam = new HttpParam();
        httpParam.setIpAddress(ip);
        CmcIpqamCollectFacade httpFacade = getFacade(ip, CmcIpqamCollectFacade.class);
        String result = "ERROR";
        result = httpFacade.modifyDSChannelIpqamBaseList(httpParam, ipqam);
        List<CmcDSIpqamBaseInfo> ipqamBaseList = httpFacade.getDSChannelIpqamBaseList(httpParam);
        cmcDownChannelDao.batchInsertCC8800BDSIPQAMBaseList(ipqamBaseList, cmcId);
        return result;
    }

    @Override
    public List<CmcDSIpqamStatusInfo> queryCCIpqamOutPutStatusList(Long cmcId) {

        return cmcDownChannelDao.queryCCIpqamOutPutStatusList(cmcId);
    }

    @Override
    public List<CmcDSIpqamMappings> queryIpqamStreamMappingsList(Long cmcId) {

        return cmcDownChannelDao.queryIpqamStreamMappingsList(cmcId);
    }

    @Override
    public CmcDSIpqamMappings queryIpqamStreamMappingsById(Long mappingId) {

        return cmcDownChannelDao.queryIpqamStreamMappingsById(mappingId);
    }

    @Override
    public void refreshIpQamMappingsStatus(Long cmcId) {
        snmpParam = getSnmpParamByCmcId(cmcId);
        String ip = snmpParam.getIpAddress();
        HttpParam httpParam = new HttpParam();
        httpParam.setIpAddress(ip);
        CmcIpqamCollectFacade httpFacade = getFacade(ip, CmcIpqamCollectFacade.class);
        List<CmcDSIpqamStatusInfo> statusList = httpFacade.fetchIpqamStatusInfoList(httpParam);
        List<CmcDSIpqamMappings> ipqamMappingsList = httpFacade.fetchIpqamMappingsInfoList(httpParam);
        CmcIpqamInfo cmcIpqamInfo = httpFacade.getCmcIpQamIpInfo(httpParam);
        cmcDownChannelDao.batchInsertCC8800BDSIPQAMStatusList(statusList, cmcId);
        cmcDownChannelDao.batchInsertCC8800BDSIPQAMMappingsList(ipqamMappingsList, cmcId);
        cmcDownChannelDao.insertorUpdateCC8800BIPQAMIpInfo(cmcIpqamInfo, cmcId);
    }

    @Override
    public void modifyIpqamMappingsList(Long cmcId, List<CmcDSIpqamMappings> mappingsList, Integer action) {
        snmpParam = getSnmpParamByCmcId(cmcId);
        String ip = snmpParam.getIpAddress();
        HttpParam httpParam = new HttpParam();
        httpParam.setIpAddress(ip);
        CmcIpqamCollectFacade httpFacade = getFacade(ip, CmcIpqamCollectFacade.class);
        httpFacade.modifyIpqamMappings(httpParam, mappingsList, action);
        List<CmcDSIpqamMappings> refreshList = httpFacade.fetchIpqamMappingsInfoList(httpParam);
        List<CmcDSIpqamStatusInfo> statusList = httpFacade.fetchIpqamStatusInfoList(httpParam);
        cmcDownChannelDao.batchInsertCC8800BDSIPQAMMappingsList(refreshList, cmcId);
        cmcDownChannelDao.batchInsertCC8800BDSIPQAMStatusList(statusList, cmcId);
    }

    @Override
    public List<CmcDSIpqamISInfo> showIpqamInputStreamInfoList(Long cmcId) {

        return cmcDownChannelDao.showIpqamInputStreamInfoList(cmcId);
    }

    @Override
    public List<CmcDSIpqamOSInfo> showIpqamOutputStreamInfoList(Long cmcId) {
        return cmcDownChannelDao.showIpqamOutputStreamInfoList(cmcId);
    }

    @Override
    public void refreshIpqamStreamInfoList(Long cmcId) {
        snmpParam = getSnmpParamByCmcId(cmcId);
        String ip = snmpParam.getIpAddress();
        HttpParam httpParam = new HttpParam();
        httpParam.setIpAddress(ip);
        CmcIpqamCollectFacade httpFacade = getFacade(ip, CmcIpqamCollectFacade.class);
        List<CmcDSIpqamISInfo> isList = httpFacade.fetchIpqamInputStreamInfoList(httpParam);
        List<CmcDSIpqamOSInfo> osList = httpFacade.fetchIpqamOutputStreamInfoList(httpParam);
        cmcDownChannelDao.batchInsertCC8800BDSIPQAMISInfoList(isList, cmcId);
        cmcDownChannelDao.batchInsertCC8800BDSIPQAMOsInfoList(osList, cmcId);
    }

    @Override
    public List<CmcDownChannelBaseShowInfo> getDownChannelFrequencyListByCmcId(Long cmcId) {
        return cmcDownChannelDao.getDownChannelOnListByCmcId(cmcId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.downchannel.service.CmcDownChannelService#queryCCIpqamIpInfo(java.lang
     * .Long)
     */
    @Override
    public CmcIpqamInfo queryCCIpqamIpInfo(Long cmcId) {
        return cmcDownChannelDao.queryforCC8800BIPQAMIpInfo(cmcId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.downchannel.service.CmcDownChannelService#modifyCCIpqamIpInfo(com.
     * topvision .ems.cmc.downchannel.domain.CmcIpqamInfo)
     */
    @Override
    public String modifyCCIpqamIpInfo(CmcIpqamInfo cmcIpqamInfo, Long cmcId) {
        snmpParam = getSnmpParamByCmcId(cmcId);
        String ip = snmpParam.getIpAddress();
        CmcIpqamCollectFacade httpFacade = getFacade(ip, CmcIpqamCollectFacade.class);
        String result = "ERROR";
        HttpParam httpParam = new HttpParam();
        httpParam.setIpAddress(ip);
        result = httpFacade.modifyCmcIpqamIPInfo(httpParam, cmcIpqamInfo);

        CmcIpqamInfo ipqamInfo = httpFacade.getCmcIpQamIpInfo(httpParam);
        cmcDownChannelDao.insertorUpdateCC8800BIPQAMIpInfo(ipqamInfo, cmcId);

        return result;
    }

    @Override
    public void insertEntityStates(CmcSynchronizedEvent event) {
        long entityId = event.getEntityId();
        SnmpParam sp = getSnmpParamByEntityId(entityId);
        try {
            List<TxPowerLimit> list = getFacadeFactory().getFacade(sp.getIpAddress(), CmcFacade.class)
                    .getTxPowerLimit(sp);
            for (Long cmcIndex : event.getCmcIndexList()) {
                List<TxPowerLimit> tmp = new ArrayList<>();
                for (TxPowerLimit txPowerLimit : list) {
                    if (txPowerLimit.getCmcIndex().equals(cmcIndex)) {
                        tmp.add(txPowerLimit);
                    }
                }
                cmcDownChannelDao.batchRefreshCmcTxPowerLimit(tmp, entityId, cmcIndex);
            }
            logger.info("batchRefreshCmcTxPowerLimit finish");
        } catch (Exception e) {
            logger.error("batchRefreshCmcTxPowerLimit wrong", e);
        }
        // TODO ipqam待重构，沿用之前的刷新方法
        if (event.getEntityType().equals(entityTypeService.getCcmtswithagentType())) {

            try {
                HttpParam httpParam = new HttpParam();
                httpParam.setIpAddress(event.getIpAddress().get(0));
                // CmcFpgaSpecification fpga = cmcService.getFpgaSpecificationById(entityId);
                // if (fpga != null && fpga.getIpqamChannelCount() > 0) {
                CmcDiscoveryIpqamDataB ccIpqamDataB = getFacadeFactory()
                        .getFacade(httpParam.getIpAddress(), CmcIpqamCollectFacade.class)
                        .refreshCC8800BIpqambyHttp(httpParam);
                if (ccIpqamDataB != null) {
                    // 下行信道IPQAM基本信息
                    if (ccIpqamDataB.getCmcDSIpqamBaseInfos() != null) {
                        cmcDownChannelDao.batchInsertCC8800BDSIPQAMBaseList(ccIpqamDataB.getCmcDSIpqamBaseInfos(),
                                entityId);
                    }
                    // 下行信道ipqam信道状态信息
                    if (ccIpqamDataB.getCmcDSIpqamStatusInfos() != null) {
                        cmcDownChannelDao.batchInsertCC8800BDSIPQAMStatusList(ccIpqamDataB.getCmcDSIpqamStatusInfos(),
                                entityId);
                    }
                    // 下行ipqam信道映射信息
                    if (ccIpqamDataB.getCmcDSIpqamMappings() != null) {
                        cmcDownChannelDao.batchInsertCC8800BDSIPQAMMappingsList(ccIpqamDataB.getCmcDSIpqamMappings(),
                                entityId);
                    }
                    // 下行ipqam输入流信息
                    if (ccIpqamDataB.getCmcDSIpqamISInfos() != null) {
                        cmcDownChannelDao.batchInsertCC8800BDSIPQAMISInfoList(ccIpqamDataB.getCmcDSIpqamISInfos(),
                                entityId);
                    }
                    // 下行ipqam输出流信息
                    if (ccIpqamDataB.getCmcDSIpqamOSInfos() != null) {
                        cmcDownChannelDao.batchInsertCC8800BDSIPQAMOsInfoList(ccIpqamDataB.getCmcDSIpqamOSInfos(),
                                entityId);
                    }
                    // IPQAM的IP类信息
                    if (ccIpqamDataB.getCmcIpqamInfo() != null) {
                        cmcDownChannelDao.insertorUpdateCC8800BIPQAMIpInfo(ccIpqamDataB.getCmcIpqamInfo(), entityId);
                    }
                }
                // }
                logger.info("Refresh CmcIpQam Info finish.");
            } catch (Exception e) {
                logger.error("Refresh CmcIpQam Info Wrong.", e);
            }
        }
    }

}
