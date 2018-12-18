package com.topvision.ems.cmc.upchannel.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.ccmts.dao.CmcChannelDao;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcPort;
import com.topvision.ems.cmc.exception.RefreshDataException;
import com.topvision.ems.cmc.exception.SetValueFailException;
import com.topvision.ems.cmc.facade.CmcFacade;
import com.topvision.ems.cmc.frequencyhopping.domain.CcmtsSpectrumGpChnl;
import com.topvision.ems.cmc.perf.dao.CmcPerfDao;
import com.topvision.ems.cmc.performance.domain.SingleNoise;
import com.topvision.ems.cmc.service.impl.CmcBaseCommonService;
import com.topvision.ems.cmc.topology.dao.CmcDiscoveryDao;
import com.topvision.ems.cmc.upchannel.dao.CmcUpChannelDao;
import com.topvision.ems.cmc.upchannel.domain.CmcUpChannelBaseShowInfo;
import com.topvision.ems.cmc.upchannel.domain.CmcUsSignalQualityInfo;
import com.topvision.ems.cmc.upchannel.facade.domain.CmcUpChannelBaseInfo;
import com.topvision.ems.cmc.upchannel.facade.domain.CmcUpChannelCounterInfo;
import com.topvision.ems.cmc.upchannel.facade.domain.CmcUpChannelRanging;
import com.topvision.ems.cmc.upchannel.facade.domain.CmcUpChannelSignalQualityInfo;
import com.topvision.ems.cmc.upchannel.facade.domain.CmtsModulationEntry;
import com.topvision.ems.cmc.upchannel.service.CmcUpChannelService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.snmp.SnmpParam;

@Service("cmcUpChannelService")
public class CmcUpChannelServiceImpl extends CmcBaseCommonService implements CmcUpChannelService {
    @Autowired
    private CmcChannelDao cmcChannelDao;
    @Autowired
    private CmcUpChannelDao cmcUpChannelDao;
    @Autowired
    private CmcPerfDao cmcPerfDao = null;
    @Autowired
    private CmcDiscoveryDao cmcDiscoveryDao = null;
    @Autowired
    private EntityTypeService entityTypeService;
    // private CcmtsSpectrumGpService ccmtsSpectrumGpService;
    @Resource(name = "entityService")
    private EntityService entityService;

    @Override
    public List<CmcUpChannelBaseShowInfo> getUpChannelBaseShowInfoList(Long cmcId) {
        // 通过cmcId，调用Dao方法，返回上行信道基本信息列表
        List<CmcUpChannelBaseShowInfo> allChannel = cmcUpChannelDao.getUpChannelBaseShowInfoList(cmcId);
        return allChannel;
    }

    @Override
    public CmcUpChannelBaseShowInfo getUpChannelBaseShowInfo(Long portId) {
        // 通过portId，调用Dao方法，返回上行信道基本信息
        return cmcUpChannelDao.getUpChannelBaseShowInfo(portId);
    }

    @Override
    public void modifyUpChannelBaseShowInfo(CmcUpChannelBaseShowInfo cmcUpChannelBaseShowInfo, Integer productType,
            CcmtsSpectrumGpChnl chnlGroup) {
        // Modified by huangdongsheng
        // @date 2013-8-10
        snmpParam = getSnmpParamByCmcId(cmcUpChannelBaseShowInfo.getCmcId());
        long ifIndex = cmcUpChannelBaseShowInfo.getChannelIndex();
        int ifAdminStatus = cmcUpChannelBaseShowInfo.getIfAdminStatus();
        CmcPort cmcPort = new CmcPort();
        CmcUpChannelBaseInfo cmcUpChannelBaseInfo = new CmcUpChannelBaseInfo();
        CmcUpChannelSignalQualityInfo channelSignalQualityInfo = new CmcUpChannelSignalQualityInfo();
        CmcUpChannelRanging cmcUpChannelRanging = new CmcUpChannelRanging();
        // cmcPort SET
        cmcPort.setCmcPortId(cmcUpChannelBaseShowInfo.getCmcPortId());
        cmcPort.setCmcId(cmcUpChannelBaseShowInfo.getCmcId());
        cmcPort.setIfIndex(cmcUpChannelBaseShowInfo.getChannelIndex());
        cmcPort.setIfAdminStatus(ifAdminStatus);
        // cmcUpChannelBaseInfo SET
        cmcUpChannelBaseInfo.setCmcId(cmcUpChannelBaseShowInfo.getCmcId());
        cmcUpChannelBaseInfo.setCmcPortId(cmcUpChannelBaseShowInfo.getCmcPortId());
        cmcUpChannelBaseInfo.setChannelIndex(cmcUpChannelBaseShowInfo.getChannelIndex());
        cmcUpChannelBaseInfo.setChannelFrequency(cmcUpChannelBaseShowInfo.getChannelFrequency());
        cmcUpChannelBaseInfo.setChannelWidth(cmcUpChannelBaseShowInfo.getChannelWidth());
        cmcUpChannelBaseInfo.setChannelModulationProfile(cmcUpChannelBaseShowInfo.getChannelModulationProfile());
        cmcUpChannelBaseInfo.setChannelExtMode(cmcUpChannelBaseShowInfo.getChannelExtMode());
        // CmcUpChannelRanging SET
        cmcUpChannelRanging.setCmcId(cmcUpChannelBaseShowInfo.getCmcId());
        cmcUpChannelRanging.setCmcPortId(cmcUpChannelBaseShowInfo.getCmcPortId());
        cmcUpChannelRanging.setChannelIndex(cmcUpChannelBaseShowInfo.getChannelIndex());
        cmcUpChannelRanging.setChannelRangingBackoffStart(cmcUpChannelBaseShowInfo.getChannelRangingBackoffStart());
        cmcUpChannelRanging.setChannelRangingBackoffEnd(cmcUpChannelBaseShowInfo.getChannelRangingBackoffEnd());
        cmcUpChannelRanging.setChannelTxBackoffStart(cmcUpChannelBaseShowInfo.getChannelTxBackoffStart());
        cmcUpChannelRanging.setChannelTxBackoffEnd(cmcUpChannelBaseShowInfo.getChannelTxBackoffEnd());
        // channelSignalQualityInfo SET
        channelSignalQualityInfo.setCmcId(cmcUpChannelBaseShowInfo.getCmcId());
        channelSignalQualityInfo.setCmcPortId(cmcUpChannelBaseShowInfo.getCmcPortId());
        channelSignalQualityInfo.setChannelIndex(cmcUpChannelBaseShowInfo.getChannelIndex());
        channelSignalQualityInfo.setDocsIf3SignalPower(cmcUpChannelBaseShowInfo.getDocsIf3SignalPower());
        if (ifAdminStatus == 1) {
            // 修改UPCHANNELBASE
            modifyCmcUpChannelBaseInfo(cmcUpChannelBaseInfo);
            // 修改测距信息
            modifyCmcUpChannelRanging(cmcUpChannelRanging);
            // 修改上行电平
            modifyCmcUpChannelSignalQualityInfo(channelSignalQualityInfo);
            // 如果设置之后的通道状态为开启状态，则后修改通道状态
            // 修改CMC PORT
            modifyCmcPort(cmcPort, ifIndex);
        } else {
            // 如果设置之后的通道状态为关闭状态，则先修改通道状态
            // 修改CMC PORT
            modifyCmcPort(cmcPort, ifIndex);
            // 修改UPCHANNELBASE
            modifyCmcUpChannelBaseInfo(cmcUpChannelBaseInfo);
            // 修改测距信息
            modifyCmcUpChannelRanging(cmcUpChannelRanging);
            // 修改上行电平
            modifyCmcUpChannelSignalQualityInfo(channelSignalQualityInfo);
        }
        // 当刷新信道基本信息时发生异常，则不认为设置失败，仅记录log
        try {
            // TODO 在此次版本中屏蔽自动跳频功能
            // ccmtsSpectrumGpService.modifyChnlGroupInfo(chnlGroup);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            // 当刷新信道基本信息时发生异常，则不认为设置失败，仅记录log
            try {
                refreshUpChannelBaseInfo(cmcUpChannelBaseShowInfo.getCmcId(), productType, chnlGroup);
            } catch (Exception e) {
                logger.info("Refresh UpChannelBaseInfo Error after Setting", e);
                throw new RefreshDataException("Refresh UpChannel Info Failed.", e);
            }
        }
    }

    /**
     * 修改上行基本信息 by huangdongsheng
     * 
     * @date 2013-8-10
     * @param cmcUpChannelBaseInfo
     */
    private void modifyCmcUpChannelRanging(CmcUpChannelRanging cmcUpChannelRanging) {
        int ifIndex = cmcUpChannelRanging.getChannelIndex().intValue();
        CmcUpChannelRanging current = getCmcFacade(snmpParam.getIpAddress()).getCmcUpChannelRanging(snmpParam, ifIndex);
        CmcUpChannelRanging setting = new CmcUpChannelRanging();
        setting.setChannelIndex((long) ifIndex);
        StringBuilder sBuilder = new StringBuilder();
        // RagingBackoffStart
        Integer channelRangingStart = cmcUpChannelRanging.getChannelRangingBackoffStart();
        if (channelRangingStart != null && current.getChannelRangingBackoffStart().intValue() != channelRangingStart) {
            setting.setChannelRangingBackoffStart(channelRangingStart);
        }
        // RangingBackoffEnd
        Integer channelRangingEnd = cmcUpChannelRanging.getChannelRangingBackoffEnd();
        if (channelRangingEnd != null && current.getChannelRangingBackoffEnd().intValue() != channelRangingEnd) {
            setting.setChannelRangingBackoffEnd(channelRangingEnd);
        }
        // TxBackoffStart
        Integer channelTxStart = cmcUpChannelRanging.getChannelTxBackoffStart();
        if (channelTxStart != null && current.getChannelTxBackoffStart().intValue() != channelTxStart) {
            setting.setChannelTxBackoffStart(channelTxStart);
        }
        // TxBackoffEnd
        Integer channelTxEnd = cmcUpChannelRanging.getChannelTxBackoffEnd();
        if (channelTxEnd != null && current.getChannelTxBackoffEnd().intValue() != channelTxEnd) {
            setting.setChannelTxBackoffEnd(channelTxEnd);
        }
        CmcUpChannelRanging cmcUpChannelBaseInfoAfterModified = getCmcFacade(snmpParam.getIpAddress())
                .modifyCmcUpChannelRanging(snmpParam, setting);
        // RangingStart
        if (cmcUpChannelBaseInfoAfterModified.getChannelRangingBackoffStart() != null
                && cmcUpChannelBaseInfoAfterModified.getChannelRangingBackoffStart().intValue() != cmcUpChannelRanging
                        .getChannelRangingBackoffStart().intValue()) {
            sBuilder.append(getString("cmc.message.cmc.setChannelRangingBackoffStart"));
        }
        // RangingEnd
        if (cmcUpChannelBaseInfoAfterModified.getChannelRangingBackoffEnd() != null
                && cmcUpChannelBaseInfoAfterModified.getChannelRangingBackoffEnd().intValue() != cmcUpChannelRanging
                        .getChannelRangingBackoffEnd().intValue()) {
            sBuilder.append(getString("cmc.message.cmc.setChannelRangingBackoffEnd"));
        }
        // TxStart
        if (cmcUpChannelBaseInfoAfterModified.getChannelTxBackoffStart() != null
                && cmcUpChannelBaseInfoAfterModified.getChannelTxBackoffStart().intValue() != cmcUpChannelRanging
                        .getChannelTxBackoffStart().intValue()) {
            sBuilder.append(getString("cmc.message.cmc.setChannelTxBackoffStart"));
        }
        // TxEnd
        if (cmcUpChannelBaseInfoAfterModified.getChannelTxBackoffEnd() != null
                && cmcUpChannelBaseInfoAfterModified.getChannelTxBackoffEnd().intValue() != cmcUpChannelRanging
                        .getChannelTxBackoffEnd().intValue()) {
            sBuilder.append(getString("cmc.message.cmc.setChannelTxBackoffEnd"));
        }
        if (sBuilder.length() > 0) {
            throw new SetValueFailException(sBuilder.toString());
        }
        cmcUpChannelDao.updateUpChannelRanging(cmcUpChannelRanging);
    }

    /**
     * 修改上行基本信息 by huangdongsheng
     * 
     * @date 2013-8-10
     * @param cmcUpChannelBaseInfo
     */
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
        // Mode
        Integer channelMode = cmcUpChannelBaseInfo.getChannelExtMode();
        if (channelMode != null && current.getChannelExtMode() != null
                && current.getChannelExtMode().intValue() != channelMode
                && current.getChannelExtMode().intValue() != 127) {
            setting.setChannelExtMode(channelMode);
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
        // MODE
        if (cmcUpChannelBaseInfoAfterModified.getChannelExtMode() != null
                && cmcUpChannelBaseInfoAfterModified.getChannelExtMode().intValue() != cmcUpChannelBaseInfo
                        .getChannelExtMode().intValue()) {
            sBuilder.append(getString("cmc.message.cmc.setChannelExtMode"));
        }
        if (sBuilder.length() > 0) {
            throw new SetValueFailException(sBuilder.toString());
        }
        cmcUpChannelDao.updateUpChannelBaseInfo(cmcUpChannelBaseInfo);
    }

    private void modifyCmcUpChannelSignalQualityInfo(CmcUpChannelSignalQualityInfo cmcUpChannelSignalQualityInfo) {
        CmcUpChannelSignalQualityInfo current = getCmcFacade(snmpParam.getIpAddress())
                .getCmcUpChannelSignalqualityInfo(snmpParam, cmcUpChannelSignalQualityInfo.getChannelIndex().intValue());
        Integer power = current.getDocsIf3SignalPower();
        if (power != null && !power.equals(cmcUpChannelSignalQualityInfo.getDocsIf3SignalPower())) {
            getCmcFacade(snmpParam.getIpAddress()).modifyUpChannelSignalQualityInfo(snmpParam,
                    cmcUpChannelSignalQualityInfo);
        }
        // Merge by Victor@20130813NM3000分支合并时取消
        // cmcUpChannelDao.updateUpChannelBaseInfo(cmcUpChannelBaseInfoAfterModified);
        // refreshUpChannelBaseInfo(cmcUpChannelBaseShowInfo.getCmcId(),
        // productType);
        // //
        // refreshUpChannelSignalQualityInfo(cmcUpChannelBaseShowInfo.getCmcId(),
        // productType);
        // /*
        // * // 如果数据库数据不是新的，需要刷新下行通道 if (diffFlag) {
        // *
        // refreshUpChannelSignalQualityInfo(cmcUpChannelBaseShowInfo.getCmcId(),
        // productType); }
        // */
        //
        // //更新信道跳频组相关信息
        // this.modifyChnlGroupInfo(chnlGroup);
    }

    @Override
    public void refreshUpChannelBaseInfo(Long cmcId, Integer productType, CcmtsSpectrumGpChnl chnlGroup) {
        Long entityId;
        if (entityTypeService.isCcmtsWithoutAgent(productType.longValue())) {
            entityId = getEntityIdByCmcId(cmcId);
            refresh8800AUpChannelBaseInfo(cmcId, entityId, productType);
        } else if (entityTypeService.isCcmtsWithAgent(productType.longValue())) {
            refresh8800BUpChannelBaseInfo(cmcId, productType);
        } else if (entityTypeService.isCmts(productType.longValue())) {
            refreshCmtsUpChannelBaseInfo(cmcId, productType);
        }
        // TODO 在此次版本中屏蔽自动跳频功能
        // ccmtsSpectrumGpService.refreshChnlGroupFromDevice(cmcId, chnlGroup);
    }

    private void refresh8800AUpChannelBaseInfo(Long cmcId, Long entityId, Integer productType) {
        snmpParam = getSnmpParamByEntityId(entityId);
        CmcFacade cmcFacade = getCmcFacade(snmpParam.getIpAddress());
        Long cmcIndex = cmcDao.getCmcIndexByCmcId(cmcId);
        List<CmcUpChannelBaseInfo> cmcUpChannelBaseInfos = cmcFacade.getUpChannelBaseInfo(snmpParam, cmcIndex);
        List<CmcPort> cmcPorts = cmcFacade.getCmcPorts(snmpParam, cmcIndex);
        cmcDiscoveryDao.batchInsertCmcPortInfo(cmcPorts, cmcId);
        cmcDiscoveryDao.batchInsertCmcUpChannelBaseInfo(cmcUpChannelBaseInfos, cmcId);
        // 刷新基本信息页面上的信号质量信息
        refreshUpChannelSignalQualityInfo(cmcId, productType);
    }

    private void refresh8800BUpChannelBaseInfo(Long cmcId, Integer productType) {
        snmpParam = getSnmpParamByCmcId(cmcId);
        CmcFacade cmcFacade = getCmcFacade(snmpParam.getIpAddress());
        List<CmcUpChannelBaseInfo> cmcUpChannelBaseInfos = cmcFacade.getCC8800BUpChannelBaseInfo(snmpParam);
        List<CmcPort> cmcPorts = cmcFacade.getCmc8800BPorts(snmpParam);
        cmcDiscoveryDao.batchInsertCmcUpChannelBaseInfo(cmcUpChannelBaseInfos, cmcId);
        cmcDiscoveryDao.batchInsertCmcPortInfo(cmcPorts, cmcId);
        // 刷新基本信息页面上的信号质量信息
        refreshUpChannelSignalQualityInfo(cmcId, productType);
    }

    private void refreshCmtsUpChannelBaseInfo(Long cmcId, Integer productType) {
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
        snmpParam.setTimeout(2000L);
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

        // 刷新基本信息页面上的信号质量信息
        refreshUpChannelSignalQualityInfo(cmcId, productType);
    }

    @Override
    public void refreshUpChannelSignalQualityInfo(Long cmcId, Integer productType) {
        Long entityId = getEntityIdByCmcId(cmcId);
        try {
            // 实时采集数据前,先改成实时模式 再去刷新
            setRealTimeSnmpDataStatus(entityId, "1");
            if (entityTypeService.isCcmtsWithoutAgent(productType.longValue())) {
                refreshCC8800AUpChannelSignalQualityInfo(cmcId, entityId);
            } else if (entityTypeService.isCcmtsWithAgent(productType.longValue())) {
                refreshCC8800BUpChannelSignalQualityInfo(cmcId);
            } else if (entityTypeService.isCmts(productType.longValue())) {
                Entity entity = entityService.getEntity(cmcId);
                if (entityTypeService.isBsrCmts(entity.getTypeId())) {
                    refreshBsr2000UpChannelSignalQualityInfo(cmcId);
                } else {
                    refreshCC8800BUpChannelSignalQualityInfo(cmcId);
                }
            }
        } finally {
            // 实时采集数据完成后,再改成轮询模式
            setRealTimeSnmpDataStatus(entityId, "2");
        }
    }

    private void refreshCC8800AUpChannelSignalQualityInfo(Long cmcId, Long entityId) {
        snmpParam = getSnmpParamByEntityId(entityId);
        Long cmcIndex = cmcDao.getCmcIndexByCmcId(cmcId);
        List<CmcUpChannelSignalQualityInfo> signalQualityInfos = getCmcFacade(snmpParam.getIpAddress())
                .getUpChannelSignalQualityInfo(snmpParam, cmcIndex);
        cmcDiscoveryDao.batchInsertCmcUpChannelSignalQualityInfo(signalQualityInfos, cmcId);
        for (CmcUpChannelSignalQualityInfo cmcUpChannelSignalQualityInfo : signalQualityInfos) {
            SingleNoise singleNoise = new SingleNoise();
            singleNoise.setEntityId(entityId);
            singleNoise.setCmcId(cmcUpChannelSignalQualityInfo.getCmcId());
            singleNoise.setIfIndex(cmcUpChannelSignalQualityInfo.getChannelIndex());
            singleNoise.setNoise(cmcUpChannelSignalQualityInfo.getDocsIfSigQSignalNoise().intValue());
            singleNoise.setDt(new Timestamp(System.currentTimeMillis()));
            cmcPerfDao.recordNoise(singleNoise);
        }
    }

    private void refreshBsr2000UpChannelSignalQualityInfo(Long cmcId) {
        snmpParam = getSnmpParamByCmcId(cmcId);
        snmpParam.setTimeout(10000L);
        List<CmcUpChannelSignalQualityInfo> signalQualityInfos = getCmcFacade(snmpParam.getIpAddress())
                .getBsr2000UpChannelSignalQualityInfo(snmpParam);
        cmcDiscoveryDao.batchInsertCmcUpChannelSignalQualityInfo(signalQualityInfos, cmcId);
        for (CmcUpChannelSignalQualityInfo cmcUpChannelSignalQualityInfo : signalQualityInfos) {
            SingleNoise singleNoise = new SingleNoise();
            singleNoise.setEntityId(snmpParam.getEntityId());
            singleNoise.setCmcId(cmcUpChannelSignalQualityInfo.getCmcId());
            singleNoise.setIfIndex(cmcUpChannelSignalQualityInfo.getChannelIndex());
            singleNoise.setNoise(cmcUpChannelSignalQualityInfo.getDocsIfSigQSignalNoise().intValue());
            singleNoise.setDt(new Timestamp(System.currentTimeMillis()));
            cmcPerfDao.recordNoise(singleNoise);
        }
        snmpParam.setTimeout(2000L);
    }

    private void refreshCC8800BUpChannelSignalQualityInfo(Long cmcId) {
        snmpParam = getSnmpParamByCmcId(cmcId);
        List<CmcUpChannelSignalQualityInfo> signalQualityInfos = getCmcFacade(snmpParam.getIpAddress())
                .getCC8800BUpChannelSignalQualityInfo(snmpParam);
        cmcDiscoveryDao.batchInsertCmcUpChannelSignalQualityInfo(signalQualityInfos, cmcId);
        for (CmcUpChannelSignalQualityInfo cmcUpChannelSignalQualityInfo : signalQualityInfos) {
            SingleNoise singleNoise = new SingleNoise();
            singleNoise.setEntityId(snmpParam.getEntityId());
            singleNoise.setCmcId(cmcUpChannelSignalQualityInfo.getCmcId());
            singleNoise.setIfIndex(cmcUpChannelSignalQualityInfo.getChannelIndex());
            singleNoise.setNoise(cmcUpChannelSignalQualityInfo.getDocsIfSigQSignalNoise().intValue());
            singleNoise.setDt(new Timestamp(System.currentTimeMillis()));
            cmcPerfDao.recordNoise(singleNoise);
        }
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
            cmcChannelDao.updateCmcPort(cmcPort);
        }
    }

    @Override
    public List<CmcUpChannelCounterInfo> getUpChannelStaticInfoList(Long cmcId) {
        // 调用Dao方法，返回cmcId指定的上行通道统计信息
        return cmcUpChannelDao.getUpChannelStaticInfoList(cmcId);
    }

    @Override
    public List<CmcUpChannelSignalQualityInfo> getUpChannelSignalQualityInfoList(Long cmcId) {
        return cmcUpChannelDao.getUpChannelSignalQualityInfoList(cmcId);
    }

    @Override
    public List<CmcUpChannelBaseShowInfo> getUpChannelListByPortId(Long cmcPortId) {
        return cmcUpChannelDao.getUpChannelListByPortId(cmcPortId);
    }

    @Override
    public List<CmcUsSignalQualityInfo> getUsSignalQualityInfoList(Long cmcId) {
        return cmcUpChannelDao.getUsSignalQualityInfoList(cmcId);
    }

    @Override
    public List<CmcUpChannelBaseShowInfo> getUpChannelFrequencyListByCmcId(Long cmcId) {
        return cmcUpChannelDao.getUpChannelOnListByCmcId(cmcId);
    }

    @Override
    public Long getChannleIndex(Long cmcId, Integer channleId) {
        return cmcUpChannelDao.getChannleIndex(cmcId, channleId);
    }

    @Override
    public void modifyUpChannelForSpe(CmcUpChannelBaseInfo chl, Integer productType) {
        snmpParam = getSnmpParamByCmcId(chl.getCmcId());
        getCmcFacade(snmpParam.getIpAddress()).modifyUpChannelForSpe(snmpParam, chl);
        refreshUpChannelBaseInfo(chl.getCmcId(), productType, new CcmtsSpectrumGpChnl());
    }

    @Override
    public List<CmtsModulationEntry> getDocsIfCmtsModTypeList(Long cmcId) {
        snmpParam = getSnmpParamByCmcId(cmcId);
        List<CmtsModulationEntry> list = getCmcFacade(snmpParam.getIpAddress()).getCmtsModulationEntryList(snmpParam);
        List<CmtsModulationEntry> returnList = new ArrayList<CmtsModulationEntry>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getDocsIfCmtsModIntervalUsageCode().equals(4L)) {
                returnList.add(list.get(i + 1));
            }
        }
        return returnList;
    }

    @Override
    public CmcUpChannelBaseShowInfo getUpChannelBaseInfo(Long cmcId, Long upChannleIndex) {
        return cmcUpChannelDao.selectUpChannelBaseInfo(cmcId, upChannleIndex);
    }

    /**
     * 实时刷新时, 刷新前改成实时模式 再去刷新 完成后再改成轮询模式
     * 
     * @param entityId
     * @param state
     */
    private void setRealTimeSnmpDataStatus(Long entityId, String state) {
        SnmpParam snmpParam = entityDao.getSnmpParamByEntityId(entityId);
        CmcFacade cmcFacade = getCmcFacade(snmpParam.getIpAddress());
        cmcFacade.setRealTimeSnmpDataStatus(snmpParam, state);
    }

}
