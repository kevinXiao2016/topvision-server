/***********************************************************************
 * $Id: CmtsChannelServiceImpl.java,v1.0 2013-8-8 上午11:56:24 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmts.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.ccmts.dao.CmcChannelDao;
import com.topvision.ems.cmc.ccmts.domain.ChannelPerfInfo;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcPort;
import com.topvision.ems.cmc.domain.CmcDownChannelBaseShowInfo;
import com.topvision.ems.cmc.downchannel.dao.CmcDownChannelDao;
import com.topvision.ems.cmc.exception.RefreshDataException;
import com.topvision.ems.cmc.exception.SetValueFailException;
import com.topvision.ems.cmc.facade.CmcFacade;
import com.topvision.ems.cmc.facade.domain.CmcDownChannelBaseInfo;
import com.topvision.ems.cmc.service.impl.CmcBaseCommonService;
import com.topvision.ems.cmc.topology.dao.CmcDiscoveryDao;
import com.topvision.ems.cmc.upchannel.dao.CmcUpChannelDao;
import com.topvision.ems.cmc.upchannel.domain.CmcUpChannelBaseShowInfo;
import com.topvision.ems.cmc.upchannel.facade.domain.CmcUpChannelBaseInfo;
import com.topvision.ems.cmc.upchannel.facade.domain.CmcUpChannelSignalQualityInfo;
import com.topvision.ems.cmts.dao.CmtsChannelDao;
import com.topvision.ems.cmts.domain.CmtsUpLinkPort;
import com.topvision.ems.cmts.service.CmtsChannelService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.template.service.EntityTypeService;

/**
 * @author loyal
 * @created @2013-8-8-上午11:56:24
 * 
 */
@Service("cmtsChannelService")
public class CmtsChannelServiceImpl extends CmcBaseCommonService implements CmtsChannelService {
    @Resource(name = "cmtsChannelDao")
    private CmtsChannelDao cmtsChannelDao;
    @Autowired
    private CmcChannelDao cmcChannelDao;
    @Autowired
    private EntityTypeService entityTypeService;
    @Autowired
    private CmcUpChannelDao cmcUpChannelDao;
    @Autowired
    private CmcDiscoveryDao cmcDiscoveryDao;
    @Autowired
    private CmcDownChannelDao cmcDownChannelDao;
    @Resource(name = "entityService")
    private EntityService entityService;

    @Override
    public void modifyUpChannelBaseShowInfo(CmcUpChannelBaseShowInfo cmcUpChannelBaseShowInfo) {
        snmpParam = getSnmpParamByCmcId(cmcUpChannelBaseShowInfo.getCmcId());
        Entity entity = entityService.getEntity(cmcUpChannelBaseShowInfo.getCmcId());
        long ifIndex = cmcUpChannelBaseShowInfo.getChannelIndex();
        int ifAdminStatus = cmcUpChannelBaseShowInfo.getIfAdminStatus();

        CmcPort cmcPort = new CmcPort();
        CmcUpChannelBaseInfo cmcUpChannelBaseInfo = new CmcUpChannelBaseInfo();
        CmcUpChannelSignalQualityInfo channelSignalQualityInfo = new CmcUpChannelSignalQualityInfo();

        cmcPort.setCmcId(cmcUpChannelBaseShowInfo.getCmcId());
        cmcPort.setIfIndex(cmcUpChannelBaseShowInfo.getChannelIndex());
        cmcPort.setIfAdminStatus(ifAdminStatus);
        cmcPort.setIfName(cmcUpChannelBaseShowInfo.getIfName());
        cmcPort.setCmcPortId(cmcUpChannelBaseShowInfo.getCmcPortId());

        cmcUpChannelBaseInfo.setCmcId(cmcUpChannelBaseShowInfo.getCmcId());
        cmcUpChannelBaseInfo.setCmcPortId(cmcUpChannelBaseShowInfo.getCmcPortId());
        cmcUpChannelBaseInfo.setChannelIndex(cmcUpChannelBaseShowInfo.getChannelIndex());
        cmcUpChannelBaseInfo.setChannelFrequency(cmcUpChannelBaseShowInfo.getChannelFrequency());
        cmcUpChannelBaseInfo.setChannelWidth(cmcUpChannelBaseShowInfo.getChannelWidth());
        cmcUpChannelBaseInfo.setChannelModulationProfile(cmcUpChannelBaseShowInfo.getChannelModulationProfile());
        cmcUpChannelBaseInfo.setChannelType(cmcUpChannelBaseShowInfo.getChannelType());

        channelSignalQualityInfo.setCmcId(cmcUpChannelBaseShowInfo.getCmcId());
        channelSignalQualityInfo.setCmcPortId(cmcUpChannelBaseShowInfo.getCmcPortId());
        channelSignalQualityInfo.setChannelIndex(cmcUpChannelBaseShowInfo.getChannelIndex());
        channelSignalQualityInfo.setDocsIf3SignalPower(cmcUpChannelBaseShowInfo.getDocsIf3SignalPower());
        channelSignalQualityInfo.setIfName(cmcUpChannelBaseShowInfo.getIfName());

        try {
            if (ifAdminStatus == 1) {
                modifyCmcUpChannelBaseInfo(cmcUpChannelBaseInfo);
                modifyCmcUpChannelSignalQualityInfo(channelSignalQualityInfo, entity);
                modifyCmcPort(cmcPort, ifIndex);
            } else {
                modifyCmcPort(cmcPort, ifIndex);
                modifyCmcUpChannelBaseInfo(cmcUpChannelBaseInfo);
                modifyCmcUpChannelSignalQualityInfo(channelSignalQualityInfo, entity);
            }
            refreshUpChannelBaseInfo(cmcUpChannelBaseShowInfo.getCmcId());
        } catch (Exception e) {
            logger.info("modifyUpChannelBaseShowInfo Error ", e);
            throw new RefreshDataException("modifyUpChannelBaseShowInfo Failed.", e);
        }

    }

    private void modifyCmcUpChannelSignalQualityInfo(CmcUpChannelSignalQualityInfo cmcUpChannelSignalQualityInfo,
            Entity entity) {
        if (entityTypeService.isBsrCmts(entity.getTypeId())) {
            Integer power = getCmcFacade(snmpParam.getIpAddress()).getBsrUpChannelRxPower(snmpParam,
                    cmcUpChannelSignalQualityInfo.getChannelIndex().intValue());
            if (power != null && !power.equals(cmcUpChannelSignalQualityInfo.getDocsIf3SignalPower())) {
                getCmcFacade(snmpParam.getIpAddress()).modifyBsrUpChannelRxPower(snmpParam,
                        cmcUpChannelSignalQualityInfo.getChannelIndex().intValue(),
                        cmcUpChannelSignalQualityInfo.getDocsIf3SignalPower());
            }
        } else {
            Integer power = getCmcFacade(snmpParam.getIpAddress()).getCmcUpChannelRxPower(snmpParam,
                    cmcUpChannelSignalQualityInfo.getChannelIndex().intValue());
            if (power != null && !power.equals(cmcUpChannelSignalQualityInfo.getDocsIf3SignalPower())) {
                getCmcFacade(snmpParam.getIpAddress()).modifyCmcUpChannelRxPower(snmpParam,
                        cmcUpChannelSignalQualityInfo.getChannelIndex().intValue(),
                        cmcUpChannelSignalQualityInfo.getDocsIf3SignalPower());
            }
        }
        cmcUpChannelDao.updateUpChannelSignalQuality(cmcUpChannelSignalQualityInfo);
    }

    private void modifyCmcUpChannelBaseInfo(CmcUpChannelBaseInfo cmcUpChannelBaseInfo) {
        int ifIndex = cmcUpChannelBaseInfo.getChannelIndex().intValue();
        CmcUpChannelBaseInfo current = getCmcFacade(snmpParam.getIpAddress()).getCmcUpChannelBaseInfo(snmpParam,
                ifIndex);
        long channelFrequency = cmcUpChannelBaseInfo.getChannelFrequency();
        CmcUpChannelBaseInfo setting = new CmcUpChannelBaseInfo();
        setting.setChannelIndex((long) ifIndex);
        StringBuilder sBuilder = new StringBuilder();
        // Frequency
        Long frequency = current.getChannelFrequency();
        if (frequency != null && current.getChannelFrequency().longValue() != channelFrequency) {
            setting.setChannelFrequency(channelFrequency);
        }
        // Width
        Long channelWidth = cmcUpChannelBaseInfo.getChannelWidth();
        if (channelWidth != null && current.getChannelWidth().longValue() != channelWidth) {
            setting.setChannelWidth(channelWidth);
        }
        // ModulationProfile
        Long modulProfile = cmcUpChannelBaseInfo.getChannelModulationProfile();
        if (modulProfile != null && current.getChannelModulationProfile().longValue() != modulProfile) {
            setting.setChannelModulationProfile(modulProfile);
        }
        CmcUpChannelBaseInfo cmcUpChannelBaseInfoAfterModified = getCmcFacade(snmpParam.getIpAddress())
                .modifyUpChannelBaseInfo(snmpParam, setting);
        // Frequency
        if (cmcUpChannelBaseInfoAfterModified.getChannelFrequency() != null
                && cmcUpChannelBaseInfoAfterModified.getChannelFrequency().longValue() != cmcUpChannelBaseInfo
                        .getChannelFrequency().longValue()) {
            sBuilder.append(getString("cmc.message.cmc.setDocsIfUpChannelFrequency"));
        }
        // Width
        if (cmcUpChannelBaseInfoAfterModified.getChannelWidth() != null
                && cmcUpChannelBaseInfoAfterModified.getChannelWidth().longValue() != cmcUpChannelBaseInfo
                        .getChannelWidth().longValue()) {
            sBuilder.append(getString("cmc.message.cmc.setDocsIfUpChannelWidth"));
        }

        // ModulationProfile
        if (cmcUpChannelBaseInfoAfterModified.getChannelModulationProfile() != null
                && cmcUpChannelBaseInfoAfterModified.getChannelModulationProfile().longValue() != cmcUpChannelBaseInfo
                        .getChannelModulationProfile().longValue()) {
            sBuilder.append(getString("cmc.message.cmc.setDocsIfUpChannelWidth"));
        }
        if (sBuilder.length() > 0) {
            throw new SetValueFailException(sBuilder.toString());
        }
        cmcUpChannelBaseInfo.setCmtsChannelModulationProfile(cmcUpChannelBaseInfo.getChannelModulationProfile());
        Long modType = getCmcFacade(snmpParam.getIpAddress()).getCmtsUpChannelModType(snmpParam,
                cmcUpChannelBaseInfo.getChannelModulationProfile());
        if (modType != null) {
            cmcUpChannelBaseInfo.setChannelModulationProfile(modType);
        }
        cmcUpChannelDao.updateUpChannelBaseInfo(cmcUpChannelBaseInfo);
    }

    private void refreshUpChannelBaseInfo(Long cmcId) {
        snmpParam = getSnmpParamByCmcId(cmcId);
        CmcFacade cmcFacade = getCmcFacade(snmpParam.getIpAddress());
        List<CmcUpChannelBaseInfo> cmcUpChannelBaseInfos = cmcFacade.getCC8800BUpChannelBaseInfo(snmpParam);
        for (CmcUpChannelBaseInfo cmcUpChannelBaseInfo : cmcUpChannelBaseInfos) {
            try {
                cmcUpChannelBaseInfo
                        .setCmtsChannelModulationProfile(cmcUpChannelBaseInfo.getChannelModulationProfile());
                Long modType = cmcFacade.getCmtsUpChannelModType(snmpParam,
                        cmcUpChannelBaseInfo.getChannelModulationProfile());
                if (modType != null) {
                    cmcUpChannelBaseInfo.setChannelModulationProfile(modType);
                }
            } catch (Exception e) {
                logger.debug("", e);
            }
        }
        List<CmcPort> cmcPorts = cmcFacade.getCmc8800BPorts(snmpParam);
        cmcDiscoveryDao.batchInsertCmcUpChannelBaseInfo(cmcUpChannelBaseInfos, cmcId);
        Entity entity = entityService.getEntity(cmcId);
        if (entityTypeService.isUbrCmts(entity.getTypeId())) {
            // UBR7225上行信道类型为129
            cmcDiscoveryDao.batchInsertCmcPortInfo(cmcPorts, cmcId);
            cmcDiscoveryDao.batchInsertCmtsPorts(cmcId, cmcPorts);// 将iftable所有信息插入端口表（port）
        } else {
            cmcDiscoveryDao.batchInsertCmcChannelInfo(cmcPorts, cmcId);
            cmcDiscoveryDao.batchInsertCmtsPorts(cmcId, cmcPorts);// 将iftable所有信息插入端口表（port）
        }
    }

    private void modifyCmcPort(CmcPort cmcPort, Long ifIndex) {
        CmcPort currentCmcPort = new CmcPort();
        currentCmcPort.setIfIndex(ifIndex);
        currentCmcPort = getCmcFacade(snmpParam.getIpAddress()).getCmcPortByIfIndex(snmpParam, currentCmcPort);
        cmcChannelDao.updateCmcPortIfName(cmcPort.getIfName(), cmcPort.getCmcPortId());
        cmcChannelDao.updatePortIfName(cmcPort.getIfName(), cmcPort.getCmcId(), ifIndex);
        if (!currentCmcPort.getIfAdminStatus().equals(cmcPort.getIfAdminStatus())) {
            StringBuilder sBuilder = new StringBuilder();
            CmcPort r = getCmcFacade(snmpParam.getIpAddress()).modifyCmcPortInfo(snmpParam, cmcPort);
            if (!r.getIfAdminStatus().equals(cmcPort.getIfAdminStatus())) {
                sBuilder.append(getString("cmc.message.cmc.setIfAdminStatus"));
            }
            if (sBuilder.length() > 0) {
                throw new SetValueFailException(sBuilder.toString());
            }
            cmcChannelDao.updateCmcPort(cmcPort);
        }
    }

    @Override
    public void modifyDownChannelBaseShowInfo(CmcDownChannelBaseShowInfo cmcDownChannelBaseShowInfo) {
        Long cmcId = cmcDownChannelBaseShowInfo.getCmcId();
        Long ifIndex = cmcDownChannelBaseShowInfo.getChannelIndex();
        Long cmcPortId = cmcDownChannelBaseShowInfo.getCmcPortId();
        Integer ifAdminStatus = cmcDownChannelBaseShowInfo.getIfAdminStatus();
        snmpParam = getSnmpParamByCmcId(cmcId);
        // ifTable
        CmcPort cmcPort = new CmcPort();
        cmcPort.setCmcPortId(cmcPortId);
        cmcPort.setCmcId(cmcId);
        cmcPort.setIfIndex(ifIndex);
        cmcPort.setIfAdminStatus(ifAdminStatus);
        cmcPort.setIfName(cmcDownChannelBaseShowInfo.getIfName());
        // 下行
        CmcDownChannelBaseInfo cmcDownChannelBaseInfo = new CmcDownChannelBaseInfo();
        cmcDownChannelBaseInfo.setCmcPortId(cmcDownChannelBaseShowInfo.getCmcPortId());
        cmcDownChannelBaseInfo.setCmcId(cmcDownChannelBaseShowInfo.getCmcId());
        cmcDownChannelBaseInfo.setChannelIndex(cmcDownChannelBaseShowInfo.getChannelIndex());
        cmcDownChannelBaseInfo
                .setDocsIfDownChannelFrequency(cmcDownChannelBaseShowInfo.getDocsIfDownChannelFrequency());
        cmcDownChannelBaseInfo.setDocsIfDownChannelPower(cmcDownChannelBaseShowInfo.getDocsIfDownChannelPower());
        cmcDownChannelBaseInfo.setDocsIfDownChannelModulation(cmcDownChannelBaseShowInfo
                .getDocsIfDownChannelModulation());
        if (ifAdminStatus == 1) {
            // 管理状态为开启->开启,关闭->开启状态时，先设置信道基本配置
            modifyCmcDownChannelBaseInfo(cmcDownChannelBaseInfo);
            modifyCmcPort(cmcPort, ifIndex);
        } else {
            // 管理状态为关闭->关闭,开启->关闭状态时，先将信道关闭
            modifyCmcPort(cmcPort, ifIndex);
            modifyCmcDownChannelBaseInfo(cmcDownChannelBaseInfo);
        }
        // 当刷新信道基本信息时发生异常，则不认为设置失败，仅记录log
        try {
            refreshCmtsDownChannelBaseInfo(cmcDownChannelBaseShowInfo.getCmcId());
        } catch (Exception e) {
            logger.info("Refresh DonwchannelBaseInfo ", e);
        }
    }

    private void refreshCmtsDownChannelBaseInfo(Long cmcId) {
        snmpParam = getSnmpParamByCmcId(cmcId);
        CmcFacade cmcFacade = getCmcFacade(snmpParam.getIpAddress());
        List<CmcDownChannelBaseInfo> cmcDownChannelBaseInfos = cmcFacade.getCC8800BDownChannelBaseInfo(snmpParam);
        List<CmcPort> cmcPorts = cmcFacade.getCmc8800BPorts(snmpParam);
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

        CmcDownChannelBaseInfo cmcDownChannelBaseInfoAfterModified = getCmcFacade(snmpParam.getIpAddress())
                .modifyDownChannelBaseInfo(snmpParam, setting);
        if (cmcDownChannelBaseInfoAfterModified.getDocsIfDownChannelFrequency() != null
                && !cmcDownChannelBaseInfoAfterModified.getDocsIfDownChannelFrequency().equals(
                        cmcDownChannelBaseInfo.getDocsIfDownChannelFrequency())) {
            sBuilder.append(getString("cmc.message.cmc.setDocsIfDownChannelFrequency"));
        }

        if (cmcDownChannelBaseInfoAfterModified.getDocsIfDownChannelModulation() != null
                && !cmcDownChannelBaseInfoAfterModified.getDocsIfDownChannelModulation().equals(
                        cmcDownChannelBaseInfo.getDocsIfDownChannelModulation())) {
            sBuilder.append(getString("cmc.message.cmc.setDocsIfDownChannelModulation"));
        }
        if (cmcDownChannelBaseInfoAfterModified.getDocsIfDownChannelPower() != null
                && !cmcDownChannelBaseInfoAfterModified.getDocsIfDownChannelPower().equals(
                        cmcDownChannelBaseInfo.getDocsIfDownChannelPower())) {
            sBuilder.append(getString("cmc.message.cmc.setDocsIfDownChannelPower"));
        }
        if (sBuilder.length() > 0) {
            throw new SetValueFailException(sBuilder.toString());
        }
        cmcDownChannelDao.updateDownChannelBaseInfo(cmcDownChannelBaseInfo);
    }

    public List<CmtsUpLinkPort> getUpLinkPortList(Long cmcId) {
        return cmtsChannelDao.selectUpLinkPortList(cmcId);
    }

    public List<ChannelPerfInfo> getCmtsChannelPerfInfoList(Long cmcId) {
        return cmtsChannelDao.selectCmtsChannelPerfInfoList(cmcId);
    }

    @Override
    public List<CmcUpChannelBaseShowInfo> getUpChannelBaseShowInfoList(Long cmcId) {
        return cmtsChannelDao.selectUpChannelBaseShowInfoList(cmcId);
    }

    public CmtsChannelDao getCmtsChannelDao() {
        return cmtsChannelDao;
    }

    public void setCmtsChannelDao(CmtsChannelDao cmtsChannelDao) {
        this.cmtsChannelDao = cmtsChannelDao;
    }
}
