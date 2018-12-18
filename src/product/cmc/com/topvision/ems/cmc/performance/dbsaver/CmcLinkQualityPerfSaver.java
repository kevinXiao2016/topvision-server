/***********************************************************************
 * $Id: CmcLinkQualityPerfSaver.java,v1.0 2013-8-12 上午09:47:53 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.dbsaver;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.cmc.perf.engine.CmcPerfDao;
import com.topvision.ems.cmc.performance.domain.CmcLinkQualityData;
import com.topvision.ems.cmc.performance.domain.CmcLinkQualityPerfResult;
import com.topvision.ems.cmc.performance.handle.CmcPortCurrentHandle;
import com.topvision.ems.cmc.performance.handle.CmcPortOptRePowerHandle;
import com.topvision.ems.cmc.performance.handle.CmcPortOptRtPowerHandle;
import com.topvision.ems.cmc.performance.handle.CmcPortOptTempHandle;
import com.topvision.ems.cmc.performance.handle.CmcPortVoltageHandle;
import com.topvision.ems.engine.dao.EngineDaoFactory;
import com.topvision.ems.engine.nbi.NbiSupportEngineSaver;
import com.topvision.ems.engine.performance.PerfEngineSaver;
import com.topvision.ems.facade.callback.PerformanceCallback;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.framework.annotation.Engine;
import com.topvision.framework.utils.EponConstants;

/**
 * @author Rod John
 * @created @2013-8-12-上午09:47:53
 * 
 */
@Engine("cmcLinkQualityPerfSaver")
public class CmcLinkQualityPerfSaver extends NbiSupportEngineSaver implements
        PerfEngineSaver<CmcLinkQualityPerfResult, OperClass> {
    private Logger logger = LoggerFactory.getLogger(CmcLinkQualityPerfSaver.class);
    @Autowired
    private EngineDaoFactory engineDaoFactory;

    @Override
    public void save(CmcLinkQualityPerfResult result) {
        if (logger.isDebugEnabled()) {
            logger.debug("CmcLinkQualityPerfSaver identifyKey[" + result.getDomain().getIdentifyKey() + "] exec start.");
        }
        CmcPerfDao cmcPerfDao = engineDaoFactory.getEngineDao(CmcPerfDao.class);
        // CMC的光模块性能统一进行配置
        Long cmcId = result.getEntityId();
        List<CmcLinkQualityData> cmcLink = new ArrayList<CmcLinkQualityData>();
        List<PerformanceData> perfDataList = new ArrayList<PerformanceData>();
        if (result.getCmcLinkQualityDatas() != null) {
            for (CmcLinkQualityData data : result.getCmcLinkQualityDatas()) {
                // 对光口信息的无效值进行处理，无效值全部统一转换
                if (data.getOptTxPower() == null
                        || (data.getOptTxPower() != null && data.getOptTxPower().equals(EponConstants.TX_POWER))) {
                    data.setOptTxPowerFloat(EponConstants.INVALID_VALUE);
                } else {
                    data.setOptTxPowerFloat(data.getOptTxPower() / 100F);
                    // 对之前的版本进行兼容在之前的版本对采集回来为0的值不进行阈值处理
                    if (data.getOptTxPower() != 0) {
                        PerformanceData optTxPowerData = new PerformanceData(cmcId,
                                CmcPortOptRtPowerHandle.CC_PON_RT_POWER_FLAG, data);
                        perfDataList.add(optTxPowerData);
                    }
                }
                // 接收光功率
                if (data.getOptRePower() == null
                        || (data.getOptRePower() != null && data.getOptRePower().equals(EponConstants.RE_POWER))) {
                    data.setOptRePowerFloat(EponConstants.INVALID_VALUE);
                } else {
                    data.setOptRePowerFloat(data.getOptRePower() / 100F);
                    // 对之前的版本进行兼容在之前的版本对采集回来为0的值不进行阈值处理
                    if (data.getOptRePower() != 0) {
                        PerformanceData optRePowerData = new PerformanceData(cmcId,
                                CmcPortOptRePowerHandle.CC_PON_RE_POWER_FLAG, data);
                        perfDataList.add(optRePowerData);
                    }
                }
                // 温度
                if (data.getOptTemp() == null
                        || (data.getOptTemp() != null && data.getOptTemp().equals(EponConstants.OPT_TEMP))) {
                    data.setOptTempFloat(EponConstants.INVALID_VALUE);
                } else {
                    data.setOptTempFloat(data.getOptTemp() / 100F);
                    PerformanceData optTempData = new PerformanceData(cmcId, CmcPortOptTempHandle.CC_PON_TEMP_FLAG,
                            data);
                    perfDataList.add(optTempData);
                }
                // 电流
                if (data.getOptCurrent() == null
                        || (data.getOptCurrent() != null && data.getOptCurrent().equals(EponConstants.OPT_CURRENT))) {
                    data.setOptCurrentFloat(EponConstants.INVALID_VALUE);
                } else {
                    data.setOptCurrentFloat(data.getOptCurrent() / 100F);
                    PerformanceData optCurrentData = new PerformanceData(cmcId,
                            CmcPortCurrentHandle.CC_PON_CURRENT_FLAG, data);
                    perfDataList.add(optCurrentData);
                }
                // 电压
                if (data.getOptVoltage() == null
                        || (data.getOptVoltage() != null && data.getOptVoltage().equals(EponConstants.OPT_VOLTAGE))) {
                    data.setOptVoltageFloat(EponConstants.INVALID_VALUE);
                } else {
                    data.setOptVoltageFloat(data.getOptVoltage() / 100 / 1000F);
                    PerformanceData optVoltageData = new PerformanceData(cmcId,
                            CmcPortVoltageHandle.CC_PON_VOLTAGE_FLAG, data);
                    perfDataList.add(optVoltageData);
                }
                cmcLink.add(data);
                try {
                    redirctPerformanceData(data, result, cmcId, data.getPortIndex());
                } catch (Exception e) {
                    logger.info("cmcLinkquality redirctPerformanceData info : " + result);
                    logger.info("cmcLinkquality redirctPerformanceData error ", e);
                }
            }
            cmcPerfDao.insertCmcLinkQuality(cmcLink);
            getCallback(PerformanceCallback.class).sendPerfomaceResult(perfDataList);
        }
        // Sync OltOnuPonAttribute For Optical
        if (result.getCmcLinkQualityFor8800A() != null) {
            cmcPerfDao.syncOnuPonOptical(result.getCmcLinkQualityFor8800A());
        }
    }
}
