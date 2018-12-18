/***********************************************************************
 * $Id: CmcRealtimeInfoServiceImpl.java,v1.0 2014年5月11日 上午10:09:06 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ccmts.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.alert.service.CmcAlertService;
import com.topvision.ems.cmc.ccmts.domain.CmcOpticalInfo;
import com.topvision.ems.cmc.ccmts.domain.CmcRealtimeInfo;
import com.topvision.ems.cmc.ccmts.domain.QualityRange;
import com.topvision.ems.cmc.ccmts.domain.QualityRangeResult;
import com.topvision.ems.cmc.ccmts.facade.CmcRealtimeInfoFacade;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcCmNum;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcCpeTypeNum;
import com.topvision.ems.cmc.ccmts.facade.domain.CmtsCmQuality;
import com.topvision.ems.cmc.ccmts.service.CmcRealtimeInfoService;
import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.cmc.remotequerycm.facade.domain.Cm2RemoteQuery;
import com.topvision.ems.cmc.service.impl.CmcBaseCommonService;
import com.topvision.ems.network.domain.TelnetLogin;
import com.topvision.ems.network.service.CommonConfigService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.network.service.TelnetLoginService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.ems.upgrade.telnet.TelnetUtil;
import com.topvision.ems.upgrade.telnet.TelnetUtilFactory;
import com.topvision.framework.common.IpUtils;
import com.topvision.framework.ping.PingExecutorService;
import com.topvision.framework.ping.PingResult;
import com.topvision.framework.ping.PingWorker;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.telnet.TelnetVty;
import com.topvision.framework.utils.CmcIndexUtils;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.zetaframework.var.UnitConfigConstant;

/**
 * @author loyal
 * @created @2014年5月11日-上午10:09:06
 * 
 */
@Service("cmcRealtimeInfoService")
public class CmcRealtimeInfoServiceImpl extends CmcBaseCommonService implements CmcRealtimeInfoService {
    private Logger logger = LoggerFactory.getLogger(CmcRealtimeInfoServiceImpl.class);
    @Autowired
    private CmcAlertService cmcAlertService;
    @Autowired
    private CmcService cmcService;
    @Autowired
    private PingExecutorService pingExecutorService;
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private TelnetLoginService telnetLoginService;
    @Autowired
    private CommonConfigService commonConfigService;
    @Autowired
    private EntityService entityService;
    @Autowired
    private EntityTypeService entityTypeService;
    @Autowired
    private TelnetUtilFactory telnetUtilFactory;

    @Override
    public Map<String, Object> getCmcRealTimeData(Long cmcId) {
        Map<String, Object> re = new HashMap<>();
        try {
            CmcRealtimeInfo cmcRealtimeInfo = getCmcRealTimeInfo(cmcId);
            re.put("cmcRealtimeInfo", cmcRealtimeInfo);
            Long typeId = entityService.getEntity(cmcId).getTypeId();
            try {
                if (entityTypeService.isCcmtsWithAgent(typeId)) {
                    CmcOpticalInfo cmcOpticalInfo = getCmcOpticalInfo(cmcId);
                    re.put("cmcOpticalInfo", cmcOpticalInfo);
                }
                re.putAll(pickCmAllData(cmcId));
                try {
                    QualityRangeResult qualityRangeResult = getQualityRangeResult(cmcId, re);
                    re.put("qualityRangeResult", qualityRangeResult);
                } catch (Exception e) {
                    logger.error("getQualityRangeResult error", e);
                }
            } catch (Exception e) {
                logger.error("pickCmAllData error", e);
            }
        } catch (Exception e) {
            logger.error("getCmcRealTimeInfo error", e);
            CmcRealtimeInfo cmcRealtimeInfo = new CmcRealtimeInfo();
            cmcRealtimeInfo.setPingCheck(false);
            cmcRealtimeInfo.setSnmpCheck(false);
            cmcRealtimeInfo.setCmcId(cmcId);
            cmcRealtimeInfo.setAlertNum(0);
            cmcRealtimeInfo.setAlias("");
            cmcRealtimeInfo.setDisplayName("");
            re.put("cmcRealtimeInfo", cmcRealtimeInfo);

            QualityRangeResult qualityRangeResult = new QualityRangeResult();
            re.put("qualityRangeResult", qualityRangeResult);

            Map<Long, CmtsCmQuality> cmtsCmQualityMaps = new HashMap<>();
            re.put("cmtsCmQualityMaps", cmtsCmQualityMaps);

            Map<Long, Cm2RemoteQuery> cm2RemoteQueryMaps = new HashMap<>();
            re.put("cm2RemoteQueryMaps", cm2RemoteQueryMaps);

            CmcCpeTypeNum cmcCpeTypeNum = new CmcCpeTypeNum();
            cmcCpeTypeNum.setHostNum(0L);
            cmcCpeTypeNum.setMtaNum(0L);
            cmcCpeTypeNum.setStbNum(0L);
            cmcCpeTypeNum.setExtenDevNum(0L);
            re.put("cmcCpeTypeNum", cmcCpeTypeNum);

            CmcCmNum cmcCmNum = new CmcCmNum();
            cmcCmNum.setTotalCmNum(0L);
            cmcCmNum.setOnlineCmNum(0L);
            cmcCmNum.setOfflineCmNum(0L);
            cmcCmNum.setDocsis3Num(0L);
            cmcCmNum.setSnrExceptionNum(0L);
            cmcCmNum.setPowerExceptionNum(0L);
            re.put("cmcCmNum", cmcCmNum);
        }
        return re;
    }

    @Override
    public boolean openRemoteQuery(Long cmcId) {
        snmpParam = getSnmpParamByCmcId(cmcId);
        String ip = snmpParam.getIpAddress();
        TelnetLogin telnetLogin = telnetLoginService.getTelnetLoginConfigByIp(new IpUtils(ip).longValue());
        if (telnetLogin == null) {
            telnetLogin = telnetLoginService.getGlobalTelnetLogin();
        }
        Long typeId = entityService.getEntity(cmcId).getTypeId();
        Long cmcIndex = cmcService.getCmcAttributeByCmcId(cmcId).getCmcIndex();
        String cmcIndexString = CmcIndexUtils.getSlotNo(cmcIndex) + "/" + CmcIndexUtils.getPonNo(cmcIndex) + "/"
                + CmcIndexUtils.getCmcId(cmcIndex);
        TelnetUtil telnetUtil = null;
        try {
            if (!checkPing(snmpParam.getIpAddress())) {
                return false;
            }
            if (entityTypeService.isCcmtsWithAgent(typeId)) {
                try {
                    telnetUtil = telnetUtilFactory.getCcmtsTelnetUtil(snmpParam.getIpAddress());
                    telnetUtil.setTimeout(15000L);
                    telnetUtil.connect(ip);
                    if (!telnetUtil.login(telnetLogin.getUserName(), telnetLogin.getPassword(),
                            telnetLogin.getEnablePassword(), telnetLogin.getIsAAA())) {
                        telnetUtilFactory.releaseTelnetUtil(telnetUtil);
                        return false;
                    }
                } catch (Exception e) {
                    logger.error("", e);
                }
            } else {
                try {
                    telnetUtil = telnetUtilFactory.getOltTelnetUtil(snmpParam.getIpAddress());
                    telnetUtil.setTimeout(15000L);
                    telnetUtil.connect(ip);
                    if (!telnetUtil.login(telnetLogin.getUserName(), telnetLogin.getPassword(),
                            telnetLogin.getEnablePassword(), telnetLogin.getIsAAA())) {
                        telnetUtilFactory.releaseTelnetUtil(telnetUtil);
                        return false;
                    }
                } catch (Exception e) {
                    logger.error("", e);
                }
            }
            telnetUtil.enterConfig();
            telnetUtil.execCmd("cable modem remote-query community-string public");
            telnetUtil.execCmd("cable modem remote-query interval 60");
            telnetUtil.execCmd("interface ccmts " + cmcIndexString);
            telnetUtil.execCmd("cable modem remote-query");
            telnetUtil.execCmd("exit");
            telnetUtil.execCmd("show cable modem remote-query config");
            String result = telnetUtil.execCmd("show cable modem remote-query config");
            telnetUtilFactory.releaseTelnetUtil(telnetUtil);
            // 命令总长度为22，enable长度为6，空格长度=总长度-enable长度-（interface长度+1）
            int space = 22 - 6 - cmcIndexString.length() - 1;
            String command = "C" + cmcIndexString;
            for (int i = 0; i < space; i++) {
                command = command + " ";
            }
            command = command + "enable";
            return result.contains(command);
        } catch (Exception e) {
            logger.error("", e);
            telnetUtilFactory.releaseTelnetUtil(telnetUtil);
            return false;
        }
    }

    private Boolean checkPing(String ip) {
        PingResult r = new PingResult();
        PingWorker worker = new PingWorker(ip, r);
        try {
            Future<PingResult> f = pingExecutorService.submit(worker, r);
            return f.get().available();
        } catch (InterruptedException e) {
            logger.debug("checkPingReachable" + ip, e);
        } catch (ExecutionException e) {
            logger.debug("checkPingReachable" + ip, e);
        }
        return false;
    }

    @Override
    public String sendCommonConfig(Long cmcId) {
        snmpParam = getSnmpParamByCmcId(cmcId);
        List<String> configs = commonConfigService.getConfigs(entityTypeService.getCcmtsType());
        TelnetLogin telnetLogin;
        TelnetVty telnetVty = new TelnetVty();
        telnetLogin = telnetLoginService.getTelnetLoginConfigByIp(new IpUtils(snmpParam.getIpAddress()).longValue());
        if (telnetLogin == null) {
            telnetLogin = telnetLoginService.getGlobalTelnetLogin();
        }
        String re = "";
        try {
            telnetVty.connect(snmpParam.getIpAddress());
            re = re + telnetVty.sendLine(telnetLogin.getUserName());
            re = re + telnetVty.sendLine(telnetLogin.getPassword());
            re = re + telnetVty.sendLine("enable");
            re = re + telnetVty.sendLine("configure terminal");
            for (String config : configs) {
                re = re + telnetVty.sendLine(config);
            }
            telnetVty.disconnect();
        } catch (IOException e) {
            logger.error("", e);
        }
        return re;
    }

    private Map<String, Object> pickCmAllData(Long cmcId) {
        snmpParam = getSnmpParamByCmcId(cmcId);
        Map<String, Object> re = new HashMap<>();
        Long cmcIndex = cmcService.getCmcIndexByCmcId(cmcId);

        Map<String, CmtsCmQuality> cmtsCmQualityMaps = new HashMap<>();
        Map<String, Cm2RemoteQuery> cm2RemoteQueryMaps = new HashMap<>();
        CmcCpeTypeNum cmcCpeTypeNum = new CmcCpeTypeNum();
        CmcCmNum cmcCmNum = new CmcCmNum();
        List<CmtsCmQuality> cmtsCmQualitys = new ArrayList<CmtsCmQuality>();
        try {
            long total = 0;
            long online = 0;
            long offline = 0;
            cmtsCmQualitys = getCmcRealtimeInfoFacade(snmpParam.getIpAddress()).getCmtsCmQualitys(snmpParam);
            for (Iterator<CmtsCmQuality> iter = cmtsCmQualitys.iterator(); iter.hasNext();) {
                CmtsCmQuality cmtsCmQuality = iter.next();
                if (!CmcIndexUtils.getCmcIndexFromCmIndex(cmtsCmQuality.getStatusIndex()).equals(cmcIndex)) {
                    iter.remove();
                } else {
                    total++;
                    if (cmtsCmQuality.getStatusValue().equals(6)) {
                        online++;
                    } else {
                        offline++;
                    }
                }
            }
            cmcCmNum.setTotalCmNum(total);
            cmcCmNum.setOnlineCmNum(online);
            cmcCmNum.setOfflineCmNum(offline);
        } catch (Exception ex) {
            logger.debug("getCmtsCmQualitys", ex);
        }

        if (cmtsCmQualitys.size() > 0) {
            for (CmtsCmQuality cmtsCmQuality : cmtsCmQualitys) {
                cmtsCmQualityMaps.put(cmtsCmQuality.getStatusIndex().toString(), cmtsCmQuality);
            }
        }
        re.put("cmtsCmQualityMaps", cmtsCmQualityMaps);
        List<Cm2RemoteQuery> cm2RemoteQuerys = new ArrayList<Cm2RemoteQuery>();
        try {
            cm2RemoteQuerys = getCmcRealtimeInfoFacade(snmpParam.getIpAddress()).getCm2RemoteQuerys(snmpParam);
            for (Iterator<Cm2RemoteQuery> iter = cm2RemoteQuerys.iterator(); iter.hasNext();) {
                Cm2RemoteQuery cm2RemoteQuery = iter.next();
                if (!CmcIndexUtils.getCmcIndexFromCmIndex(cm2RemoteQuery.getCmIndex()).equals(cmcIndex)) {
                    iter.remove();
                }
            }
        } catch (Exception ex) {
            logger.debug("getCm2RemoteQuerys", ex);
        }
        if (cm2RemoteQuerys.size() > 0) {
            for (Cm2RemoteQuery cm2RemoteQuery : cm2RemoteQuerys) {
                cm2RemoteQueryMaps.put(cm2RemoteQuery.getCmIndex().toString(), cm2RemoteQuery);
            }
        }
        re.put("cm2RemoteQueryMaps", cm2RemoteQueryMaps);
        // try {
        // cmcCmNum = getCmcRealtimeInfoFacade(snmpParam.getIpAddress()).getCmcCmNum(snmpParam);
        // } catch (Exception ex) {
        // logger.debug("getCmcCmNum", ex);
        // }

        re.put("cmcCmNum", cmcCmNum);

        try {
            cmcCpeTypeNum = getCmcRealtimeInfoFacade(snmpParam.getIpAddress()).getCmcCpeTypeNum(snmpParam, cmcIndex);
        } catch (Exception ex) {
            logger.debug("getCmcCpeTypeNum", ex);
        }
        re.put("cmcCpeTypeNum", cmcCpeTypeNum);

        return re;
    }

    private CmcRealtimeInfo getCmcRealTimeInfo(Long cmcId) {
        snmpParam = getSnmpParamByCmcId(cmcId);
        CmcRealtimeInfo cmcRealtimeInfo = new CmcRealtimeInfo();
        Long cmcIndex = cmcService.getCmcIndexByCmcId(cmcId);

        String ip = snmpParam.getIpAddress();
        boolean pingCheck = false;
        try {
            if (new IpUtils(ip).longValue() != 0 && !"".equals(ip) && ip != null) {
                pingCheck = checkPingReachable(ip);
            }
        } catch (Exception ipe) {
            logger.debug("Check Ping" + ip, ipe);
            pingCheck = false;
        }

        boolean snmpCheck = false;
        if (pingCheck) {
            snmpCheck = checkCmcSnmpReachable(snmpParam);
        }

        if (pingCheck && snmpCheck) {
            try {
                cmcRealtimeInfo = getCmcFacade(snmpParam.getIpAddress()).getCmcRealTimeInfo(snmpParam, cmcIndex);
            } catch (Exception ex) {
                logger.debug("CmcRealtimeInfoServiceImpl getCmcRealTimeInfo", ex);
            }
        }

        cmcRealtimeInfo.setPingCheck(pingCheck);
        cmcRealtimeInfo.setSnmpCheck(snmpCheck);
        cmcRealtimeInfo.setCmcId(cmcId);
        cmcRealtimeInfo.setIp(snmpParam.getIpAddress());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cmcId", cmcId);
        Integer alertNum = cmcAlertService.getCmcAlertListNum(map);
        CmcAttribute cmcAttribute = cmcService.getCmcAttributeByCmcId(cmcId);
        cmcRealtimeInfo.setAlertNum(alertNum);
        cmcRealtimeInfo.setAlias(cmcAttribute.getNmName());
        cmcRealtimeInfo.setDisplayName(cmcAttribute.getCmcDeviceStyleString());

        return cmcRealtimeInfo;
    }

    private CmcOpticalInfo getCmcOpticalInfo(Long cmcId) {
        snmpParam = getSnmpParamByCmcId(cmcId);
        CmcOpticalInfo cmcOpticalInfo = new CmcOpticalInfo();

        String ip = snmpParam.getIpAddress();
        boolean pingCheck = false;
        try {
            if (new IpUtils(ip).longValue() != 0 && !"".equals(ip) && ip != null) {
                pingCheck = checkPingReachable(ip);
            }
        } catch (Exception ipe) {
            logger.debug("Check Ping" + ip, ipe);
            pingCheck = false;
        }

        boolean snmpCheck = false;
        if (pingCheck) {
            snmpCheck = checkCmcSnmpReachable(snmpParam);
        }

        if (pingCheck && snmpCheck) {
            try {
                cmcOpticalInfo = getCmcFacade(snmpParam.getIpAddress()).getCmcOpticalInfo(snmpParam);
            } catch (Exception ex) {
                logger.debug("CmcRealtimeInfoServiceImpl getCmcOpticalInfo", ex);
            }
        }
        cmcOpticalInfo.setPingCheck(pingCheck);
        cmcOpticalInfo.setSnmpCheck(snmpCheck);
        cmcOpticalInfo.setCmcId(cmcId);
        cmcOpticalInfo.setIp(snmpParam.getIpAddress());
        if (cmcOpticalInfo.getTemperature() != null) {
            // 转换温度单位展示
            cmcOpticalInfo.setCmcOpticalTemp(UnitConfigConstant.translateTemperature(cmcOpticalInfo.getTemperature()));
        }
        return cmcOpticalInfo;
    }

    @SuppressWarnings("unchecked")
    private QualityRangeResult getQualityRangeResult(Long cmcId, Map<String, Object> allData) {
        Map<Long, CmtsCmQuality> cmtsCmQualityMaps = (Map<Long, CmtsCmQuality>) allData.get("cmtsCmQualityMaps");
        Map<Long, Cm2RemoteQuery> cm2RemoteQueryMaps = (Map<Long, Cm2RemoteQuery>) allData.get("cm2RemoteQueryMaps");
        CmcCmNum cmcCmNum = (CmcCmNum) allData.get("cmcCmNum");
        QualityRangeResult re = new QualityRangeResult();
        snmpParam = getSnmpParamByCmcId(cmcId);
        if (cmtsCmQualityMaps != null && !cmtsCmQualityMaps.isEmpty()) {
            boolean remoteQueryStarted = false;
            if (cm2RemoteQueryMaps != null && !cm2RemoteQueryMaps.isEmpty()) {
                // 先不计算衰减
                // List<DownChannelTxPower> downChannelTxPowers =
                // getCmcRealtimeInfoFacade(snmpParam.getIpAddress()).getDownChannelTxPowers(snmpParam);
                for (Cm2RemoteQuery cm2RemoteQuery : cm2RemoteQueryMaps.values()) {
                    if (cm2RemoteQuery.getStatus() == 1) {
                        remoteQueryStarted = true;
                        break;
                    }
                }
            }
            makeQualityRangeResult(re, cmtsCmQualityMaps, cm2RemoteQueryMaps, cmcCmNum, remoteQueryStarted);
        }
        return re;
    }

    private void makeQualityRangeResult(QualityRangeResult re, Map<Long, CmtsCmQuality> cmtsCmQualitys,
            Map<Long, Cm2RemoteQuery> cm2RemoteQuerys, CmcCmNum cmcCmNum, boolean remoteQueryStarted) {
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
        if (remoteQueryStarted) {
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
        } else {
            QualityRange utpqr = re.getUsTxPowerQualityRange().get(0);
            utpqr.setCount(cmtsCmQualitys.size());
            QualityRange drpqr = re.getDsRxPowerQualityRange().get(0);
            drpqr.setCount(cmtsCmQualitys.size());
            QualityRange dsqr = re.getDsSnrQualityRange().get(0);
            dsqr.setCount(cmtsCmQualitys.size());
        }
    }

    private CmcRealtimeInfoFacade getCmcRealtimeInfoFacade(String ip) {
        return facadeFactory.getFacade(ip, CmcRealtimeInfoFacade.class);
    }

    private boolean checkPingReachable(String ip) {
        PingResult r = new PingResult();
        PingWorker worker = new PingWorker(ip, r);
        try {
            Future<PingResult> f = pingExecutorService.submit(worker, r);
            return f.get().available();
        } catch (InterruptedException e) {
            logger.debug("checkPingReachable" + ip, e);
        } catch (ExecutionException e) {
            logger.debug("checkPingReachable" + ip, e);
        }
        return false;
    }

    private boolean checkCmcSnmpReachable(SnmpParam snmpParam) {
        try {
            getCmcFacade(snmpParam.getIpAddress()).getCmcAttributeForSnmpTest(snmpParam);
            return true;
        } catch (Exception e) {
            logger.debug("checkCmcSnmpReachable" + snmpParam.getIpAddress(), e);
        }
        return false;
    }

    @Override
    public String[] getCmcSnr(Long cmcId, String[] channelIndex) {
        SnmpParam snmpParam = getSnmpParamByCmcId(cmcId);
        String[] oids = new String[20];
        for (int i = 0; i < channelIndex.length; i++) {
            oids[i] = "1.3.6.1.2.1.10.127.1.1.4.1.5." + channelIndex[i];
        }
        CmcRealtimeInfoFacade facade = getCmcRealtimeInfoFacade(snmpParam.getIpAddress());
        String[] snrs = null;
        try {
            facade.setRealTimeSnmpDataStatus(snmpParam, "1");
            snrs = facade.getCmcSnr(snmpParam, oids);
        } finally {
            facade.setRealTimeSnmpDataStatus(snmpParam, "2");
        }
        return snrs;
    }
}
