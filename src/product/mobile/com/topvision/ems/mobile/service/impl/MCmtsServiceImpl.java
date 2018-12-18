package com.topvision.ems.mobile.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.Resource;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.ccmts.domain.QualityRange;
import com.topvision.ems.cmc.ccmts.domain.QualityRangeResult;
import com.topvision.ems.cmc.ccmts.facade.CmcRealtimeInfoFacade;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcCmNum;
import com.topvision.ems.cmc.ccmts.facade.domain.CmtsCmQuality;
import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.cmc.cm.dao.CmListDao;
import com.topvision.ems.cmc.downchannel.service.CmcDownChannelService;
import com.topvision.ems.cmc.remotequerycm.facade.domain.Cm2RemoteQuery;
import com.topvision.ems.cmc.service.impl.CmcBaseCommonService;
import com.topvision.ems.cmc.spectrum.domain.UpChannelSpectrum;
import com.topvision.ems.cmc.upchannel.service.CmcUpChannelService;
import com.topvision.ems.devicesupport.version.service.DeviceVersionService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.mobile.dao.MCmtsDao;
import com.topvision.ems.mobile.domain.BaiduMapInfo;
import com.topvision.ems.mobile.domain.CmtsDownChannel;
import com.topvision.ems.mobile.domain.CmtsDownChannelWithPortId;
import com.topvision.ems.mobile.domain.CmtsInCmtsList;
import com.topvision.ems.mobile.domain.CmtsInfo;
import com.topvision.ems.mobile.domain.CmtsUpChannel;
import com.topvision.ems.mobile.domain.CmtsUpChannelWithPortId;
import com.topvision.ems.mobile.service.MCmtsService;
import com.topvision.ems.network.domain.TelnetLogin;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.network.service.TelnetLoginService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.ems.upgrade.telnet.CcmtsTelnetUtil;
import com.topvision.ems.upgrade.telnet.OltTelnetUtil;
import com.topvision.ems.upgrade.telnet.TelnetUtil;
import com.topvision.ems.upgrade.telnet.TelnetUtilFactory;
import com.topvision.framework.common.IpUtils;
import com.topvision.framework.utils.CmcIndexUtils;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.zetaframework.var.UnitConfigConstant;

@Service("mCmtsService")
public class MCmtsServiceImpl extends CmcBaseCommonService implements MCmtsService {
    @Resource(name = "mCmtsDao")
    private MCmtsDao mCmtsDao;
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private TelnetLoginService telnetLoginService;
    @Autowired
    private EntityTypeService entityTypeService;
    @Autowired
    private EntityService entityService;
    @Autowired
    private CmcService cmcService;
    @Resource(name = "cmListDao")
    private CmListDao cmListDao;
    @Autowired
    private TelnetUtilFactory telnetUtilFactory;
    @Autowired
    private CmcUpChannelService cmcUpChannelService;
    @Resource(name = "deviceVersionService")
    private DeviceVersionService deviceVersionService;
    @Autowired
    private CmcDownChannelService cmcDownChannelService;

    @Override
    public CmtsInfo getCmtsInfoById(Long cmtsId) {
        return mCmtsDao.getCmtsInfoById(cmtsId);
    }

    @Override
    public List<CmtsDownChannel> getDownChannelsById(Long cmtsId) {
        return mCmtsDao.getDownChannelsById(cmtsId);
    }

    @Override
    public List<CmtsUpChannel> getUpChannelsById(Long cmtsId) {
        Entity entity = entityService.getEntity(cmtsId);
        //cmcUpChannelService.refreshUpChannelSignalQualityInfo(cmtsId, (int) entity.getTypeId());
        return mCmtsDao.getUpChannelsById(cmtsId);
    }

    @Override
    public QualityRangeResult getSignalQuality(Long cmtsId) {
        Long cmcIndex = cmcService.getCmcIndexByCmcId(cmtsId);
        snmpParam = getSnmpParamByCmcId(cmtsId);
        Map<String, CmtsCmQuality> cmtsCmQualityMaps = new HashMap<>();
        Map<String, Cm2RemoteQuery> cm2RemoteQueryMaps = new HashMap<>();
        List<CmtsCmQuality> cmtsCmQualitys = new ArrayList<CmtsCmQuality>();
        List<Cm2RemoteQuery> cm2RemoteQuerys = new ArrayList<Cm2RemoteQuery>();
        CmcCmNum cmcCmNum = new CmcCmNum();
        try {
            cmtsCmQualitys = getCmcRealtimeInfoFacade(snmpParam.getIpAddress()).getCmtsCmQualitys(snmpParam);
            for (CmtsCmQuality cmtsCmQuality : cmtsCmQualitys) {
                if (CmcIndexUtils.getCmcIndexFromCmIndex(cmtsCmQuality.getStatusIndex()).equals(cmcIndex)) {
                    cmtsCmQualityMaps.put(cmtsCmQuality.getStatusIndex().toString(), cmtsCmQuality);
                }
            }
        } catch (Exception ex) {
            logger.debug("getCmtsCmQualitys", ex);
        }
        try {
            cm2RemoteQuerys = getCmcRealtimeInfoFacade(snmpParam.getIpAddress()).getCm2RemoteQuerys(snmpParam);
            for (Cm2RemoteQuery cm2RemoteQuery : cm2RemoteQuerys) {
                if (CmcIndexUtils.getCmcIndexFromCmIndex(cm2RemoteQuery.getCmIndex()).equals(cmcIndex)) {
                    cm2RemoteQueryMaps.put(cm2RemoteQuery.getCmIndex().toString(), cm2RemoteQuery);
                }
            }
        } catch (Exception ex) {
            logger.debug("getCm2RemoteQuerys", ex);
        }
        try {
            cmcCmNum = getCmcRealtimeInfoFacade(snmpParam.getIpAddress()).getCmcCmNum(snmpParam);
        } catch (Exception ex) {
            logger.debug("getCmcCmNum", ex);
        }
        QualityRangeResult qualityRangeResult = new QualityRangeResult();
        if (cmtsCmQualityMaps != null && !cmtsCmQualityMaps.isEmpty()) {
            boolean remoteQueryStarted = false;
            if (cm2RemoteQueryMaps != null && !cm2RemoteQueryMaps.isEmpty()) {
                for (Cm2RemoteQuery cm2RemoteQuery : cm2RemoteQueryMaps.values()) {
                    if (cm2RemoteQuery.getStatus() == 1) {
                        remoteQueryStarted = true;
                        break;
                    }
                }
            }
            makeQualityRangeResult(qualityRangeResult, cmtsCmQualityMaps, cm2RemoteQueryMaps, cmcCmNum,
                    remoteQueryStarted);
        }
        return qualityRangeResult;
    }

    private void makeQualityRangeResult(QualityRangeResult re, Map<String, CmtsCmQuality> cmtsCmQualitys,
            Map<String, Cm2RemoteQuery> cm2RemoteQuerys, CmcCmNum cmcCmNum, boolean remoteQueryStarted) {
        for (CmtsCmQuality cmtsCmQuality : cmtsCmQualitys.values()) {
            for (QualityRange qualityRange : re.getUsSnrQualityRange()) {
                Float value = null;
                if (CmAttribute.isCmOnline(cmtsCmQuality.getStatusValue())) {
                    value = cmtsCmQuality.getStatusSignalNoise().floatValue() / 10;
                }
                if (qualityRange.isInRange(value)) {
                    if (qualityRange.isException()) {
                        cmcCmNum.addSnrException(cmtsCmQuality.getStatusIndex());
                    }
                    qualityRange.addCount(cmtsCmQuality.getStatusIndex());
                }
            }
        }
        for (Cm2RemoteQuery cm2RemoteQuery : cm2RemoteQuerys.values()) {
            for (QualityRange qualityRange : re.getUsTxPowerQualityRange()) {
                Float value = null;
                if (cm2RemoteQuery.getStatus() == 1) {
                    // value = cm2RemoteQuery.getCmTxPower().floatValue() / 10;
                    value = (float) UnitConfigConstant
                            .parsePowerValue(cm2RemoteQuery.getCmTxPower().doubleValue() / 10);
                }
                if (qualityRange.isInRange(value)) {
                    qualityRange.addCount(cm2RemoteQuery.getCmIndex());
                }
            }
            for (QualityRange qualityRange : re.getDsRxPowerQualityRange()) {
                Float value = null;
                if (cm2RemoteQuery.getStatus() == 1) {
                    // value = cm2RemoteQuery.getCmRxPower().floatValue() / 10;
                    value = (float) UnitConfigConstant
                            .parsePowerValue(cm2RemoteQuery.getCmRxPower().doubleValue() / 10);
                }
                if (qualityRange.isInRange(value)) {
                    if (qualityRange.isException()) {
                        cmcCmNum.addPowerException(cm2RemoteQuery.getCmIndex());
                    }
                    qualityRange.addCount(cm2RemoteQuery.getCmIndex());
                }
            }
            for (QualityRange qualityRange : re.getDsSnrQualityRange()) {
                Float value = null;
                if (cm2RemoteQuery.getStatus() == 1) {
                    value = cm2RemoteQuery.getCmSignalNoise().floatValue() / 10;
                }
                if (qualityRange.isInRange(value)) {
                    qualityRange.addCount(cm2RemoteQuery.getCmIndex());
                }
            }
        }
    }

    private CmcRealtimeInfoFacade getCmcRealtimeInfoFacade(String ip) {
        return facadeFactory.getFacade(ip, CmcRealtimeInfoFacade.class);
    }

    @Override
    public List<UpChannelSpectrum> getCmtsSpectrum(Long cmtsId, String spectrumWidth) {

        boolean isOldSpectrumTelnet = deviceVersionService.isFunctionSupported(cmtsId, "oldSpectrumTelnet");

        TelnetUtil telnetUtil = null;
        try {
            CmtsInfo cmtsInfo = getCmtsInfoById(cmtsId);
            String ip = cmtsInfo.getIp();
            Long cmcIndex = cmtsInfo.getCmcIndex();
            TelnetLogin telnetLogin = new TelnetLogin();
            telnetLogin = telnetLoginService.getTelnetLoginConfigByIp(new IpUtils(ip).longValue());
            if (telnetLogin == null) {
                telnetLogin = telnetLoginService.getGlobalTelnetLogin();
            }
            if (entityTypeService.isCcmtsWithAgent(cmtsInfo.getTypeId())) {
                telnetUtil = (CcmtsTelnetUtil) telnetUtilFactory.getCcmtsTelnetUtil(ip);
                telnetUtil.connect(ip);
                telnetUtil.login(telnetLogin.getUserName(), telnetLogin.getPassword(), telnetLogin.getEnablePassword(),telnetLogin.getIsAAA());
                telnetUtil.enterConfig();
                telnetUtil.execCmd("interface ccmts 1/1/1");
                String result = "";
                /*if (isOldSpectrumTelnet) {
                    result = telnetUtil.execCmd("show cable upstream-spectrum " + spectrumWidth);
                } else {
                    result = telnetUtil.execCmd("show cable upstream-spectrum channel-width-" + spectrumWidth);
                }*/
                result = telnetUtil.execCmd("show cable upstream-spectrum " + spectrumWidth);
                if (result.contains("Unknown command")) {
                    result = telnetUtil.execCmd("show cable upstream-spectrum channel-width-" + spectrumWidth);
                }
                
                return parseSpectrum(result);
            } else if (entityTypeService.isCcmtsWithoutAgent(cmtsInfo.getTypeId())) {
                telnetUtil = (OltTelnetUtil) telnetUtilFactory.getOltTelnetUtil(ip);
                telnetUtil.connect(ip);
                telnetUtil.login(telnetLogin.getUserName(), telnetLogin.getPassword(), telnetLogin.getEnablePassword(),telnetLogin.getIsAAA());
                telnetUtil.enterConfig();
                telnetUtil.execCmd("interface ccmts " + CmcIndexUtils.getSlotNo(cmcIndex) + "/"
                        + CmcIndexUtils.getPonNo(cmcIndex) + "/" + CmcIndexUtils.getCmcId(cmcIndex));
                String result = "";
                /*if (isOldSpectrumTelnet) {
                    result = telnetUtil.execCmd("show cable upstream-spectrum " + spectrumWidth);
                } else {
                    result = telnetUtil.execCmd("show cable upstream-spectrum channel-width-" + spectrumWidth);
                }*/
                result = telnetUtil.execCmd("show cable upstream-spectrum " + spectrumWidth);
                if (result.contains("Unknown command")) {
                    result = telnetUtil.execCmd("show cable upstream-spectrum channel-width-" + spectrumWidth);
                }
                return parseSpectrum(result);
            }
        } catch (Exception e) {
        	logger.info("",e);
        } finally {
            telnetUtilFactory.releaseTelnetUtil(telnetUtil);
        }
        return new ArrayList<UpChannelSpectrum>();
    }

    private List<UpChannelSpectrum> parseSpectrum(String result) {
        Pattern pattern = Pattern.compile("^(\\d+)(.*)");
        List<String> cmtsSpectrum = new ArrayList<String>();
        String[] resultArray = result.split("\n");
        List<UpChannelSpectrum> upChannelSpectrums = new ArrayList<UpChannelSpectrum>();
        for (int i = 0; i < resultArray.length; i++) {
            if (pattern.matcher(resultArray[i].toString().trim()).matches()) {// 以数字开头
                cmtsSpectrum.add(resultArray[i]);// 解析槽位号
            }
        }
        for (int i = 0; i < cmtsSpectrum.size(); i++) {
            try {
                UpChannelSpectrum upChannelSpectrum = new UpChannelSpectrum();
                String[] temp = cmtsSpectrum.get(i).replaceAll(" +", " ").split(" ");
                upChannelSpectrum.setCenterFreq(temp[0]);
                upChannelSpectrum.setAvg(temp[1]);
                upChannelSpectrum.setMin(temp[2]);
                upChannelSpectrum.setMax(temp[3]);
                upChannelSpectrums.add(upChannelSpectrum);
            } catch (Exception e) {
                logger.info("",e);
            }
        }
        return upChannelSpectrums;
    }

    @Override
    public Long getErrorRateInterval(Long entityId) {
        return mCmtsDao.getErrorRateInterval(entityId);
    }

    @Override
    public List<CmtsInCmtsList> getCmtsList(Map<String, Object> map) {
        return mCmtsDao.getCmtsList(map);
    }

    @Override
    public Long getCmtsListCount(Map<String, Object> map) {
        return mCmtsDao.getCmtsListCount(map);
    }

    @Override
    public List<CmtsUpChannelWithPortId> getUpChannelsInfoById(Long cmtsId) {
        Entity entity = entityService.getEntity(cmtsId);
        //cmcUpChannelService.refreshUpChannelSignalQualityInfo(cmtsId, (int) entity.getTypeId());
        //cmcUpChannelService.refreshUpChannelBaseInfo(cmtsId, (int) entity.getTypeId(), null);
        return mCmtsDao.getUpChannelsInfoById(cmtsId);
    }

    @Override
    public List<CmtsDownChannelWithPortId> getDownChannelsInfoById(Long cmtsId) {
        Entity entity = entityService.getEntity(cmtsId);
        //cmcDownChannelService.refreshDownChannelBaseInfo(cmtsId, (int) entity.getTypeId());
        return mCmtsDao.getDownChannelsInfoById(cmtsId);
    }

    @Override
    public void saveMapDataToDB(Map<String, Object> map) {
        mCmtsDao.saveMapDataToDB(map);
    }

    @Override
    public List<CmtsInCmtsList> getCmtsListWithRegion(Map<String, Object> map) {
        return mCmtsDao.getCmtsListWithRegion(map);
    }

    @Override
    public Long getCmtsListCountWithRegion(Map<String, Object> map) {
        return mCmtsDao.getCmtsListCountWithRegion(map);
    }

    @Override
    public void modifyCmcLocation(Map<String, Object> map) {
        mCmtsDao.modifyCmtsLocation(map);
    }

    @Override
    public BaiduMapInfo getBaiduMapInfo(Long cmtsId) {
        return mCmtsDao.getBaiduMapInfo(cmtsId);
    }

    @Override
    public Double getCmcOptRecPower(Long cmtsId) {
        Long oltEntityId = cmListDao.selectOltIdByCmcId(cmtsId);
        Long portIndex = cmListDao.selectOnuIndex(oltEntityId, cmtsId);
        return mCmtsDao.selectCmtsOptRecPower(cmtsId,portIndex);
    }
    
    @Override
    public void refreshUpChannel(Long cmtsId) {
        Entity entity = entityService.getEntity(cmtsId);
        cmcUpChannelService.refreshUpChannelBaseInfo(cmtsId, (int) entity.getTypeId(), null);
    }

    @Override
    public void refreshDownChannel(Long cmtsId) {
        Entity entity = entityService.getEntity(cmtsId);
        cmcDownChannelService.refreshDownChannelBaseInfo(cmtsId, (int) entity.getTypeId());
    }

}
