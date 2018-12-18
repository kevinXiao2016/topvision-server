/***********************************************************************
 * $Id: EponLinkQualityPerfSaver.java,v1.0 2013-8-6 下午04:32:35 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.dbsaver;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.engine.dao.EngineDaoFactory;
import com.topvision.ems.engine.nbi.NbiSupportEngineSaver;
import com.topvision.ems.engine.performance.PerfEngineSaver;
import com.topvision.ems.epon.performance.domain.EponLinkQualityData;
import com.topvision.ems.epon.performance.domain.EponLinkQualityPerfResult;
import com.topvision.ems.epon.performance.engine.OltPerfDao;
import com.topvision.ems.epon.performance.handle.OltPortOptCurrentHandle;
import com.topvision.ems.epon.performance.handle.OltPortOptRePowerHandle;
import com.topvision.ems.epon.performance.handle.OltPortOptTempHandle;
import com.topvision.ems.epon.performance.handle.OltPortOptTxPowerHandle;
import com.topvision.ems.epon.performance.handle.OltPortOptVoltageHandle;
import com.topvision.ems.facade.callback.PerformanceCallback;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.ems.performance.domain.PerfTargetConstants;
import com.topvision.framework.annotation.Engine;
import com.topvision.framework.utils.EponConstants;

/**
 * @author Rod John
 * @created @2013-8-6-下午04:32:35
 * 
 */
@Engine("eponLinkQualityPerfSaver")
public class EponLinkQualityPerfSaver extends NbiSupportEngineSaver implements
        PerfEngineSaver<EponLinkQualityPerfResult, OperClass> {
    @Autowired
    private EngineDaoFactory engineDaoFactory;

    @Override
    public void save(EponLinkQualityPerfResult result) {
        OltPerfDao perfDao = engineDaoFactory.getEngineDao(OltPerfDao.class);
        Long entityId = result.getEntityId();
        List<EponLinkQualityData> perf = new ArrayList<EponLinkQualityData>();
        List<PerformanceData> pList = new ArrayList<PerformanceData>();
        for (EponLinkQualityData data : result.getLinkQualityDatas()) {
            data.setEntityId(entityId);
            if (data.getOptTxPower() >= 1 && data.getOptTxPower() != 65535
                    && !data.getOptTxPower().equals(EponConstants.TX_POWER)) {
                Float txPower = (float) (10 * Math.log10(data.getOptTxPower()) - 40);
                data.setTransPower(txPower);
                PerformanceData optTxPowerData = new PerformanceData(entityId,
                        OltPortOptTxPowerHandle.OLT_OPTTXPOWER_FLAG, data);
                pList.add(optTxPowerData);
            } else {
                // data.setTransPower(0d);
                data.setTransPower(EponConstants.INVALID_VALUE);
            }
            if (data.getOptRePower() >= 1 && data.getOptRePower() != 65535
                    && !data.getOptRePower().equals(EponConstants.RE_POWER)) {
                Float rePower = (float) (10 * Math.log10(data.getOptRePower()) - 40);
                data.setRecvPower(rePower);
                PerformanceData optRePowerData = new PerformanceData(entityId,
                        OltPortOptRePowerHandle.OLT_OPTREPOWER_FLAG, data);
                /*
                 * midify by lzt PON口的接收功率没有意义故不处理 SNI口做处理
                 */
                if ("SNI".equals(data.getPortType())) {
                    pList.add(optRePowerData);
                }
            } else {
                // data.setRecvPower(0d);
                data.setRecvPower(EponConstants.INVALID_VALUE);
            }
            if (!data.getOptTemp().equals(EponConstants.OPT_TEMP)) {
                data.setWorkingTemp(data.getOptTemp() / 256F);
                PerformanceData optTempData = new PerformanceData(entityId, OltPortOptTempHandle.OLT_OPTTEMP_FLAG, data);
                pList.add(optTempData);
            } else {
                data.setWorkingTemp(EponConstants.INVALID_VALUE);
            }
            if (!data.getOptCurrent().equals(EponConstants.OPT_CURRENT)) {
                data.setBiasCurrent(data.getOptCurrent() / 500F);
                PerformanceData optCurrentData = new PerformanceData(entityId,
                        OltPortOptCurrentHandle.OLT_OPTCURRENT_FLAG, data);
                pList.add(optCurrentData);
            } else {
                data.setBiasCurrent(EponConstants.INVALID_VALUE);
            }
            if (!data.getOptVoltage().equals(EponConstants.OPT_VOLTAGE)) {
                // 电压使用毫V
                data.setWorkingVoltage(data.getOptVoltage() / 10F);
                PerformanceData optVolageData = new PerformanceData(entityId, OltPortOptVoltageHandle.OLT_OPTVOL_FLAG,
                        data);
                pList.add(optVolageData);
            } else {
                data.setWorkingVoltage(EponConstants.INVALID_VALUE);
            }
            perf.add(data);
            // 性能北向接口数据处理
            try {
                redirctPerformanceData(data, result, entityId, data.getPortIndex());
            } catch (Exception e) {
                logger.info("EponLinkQualityPerf redirctPerformanceData info : " + result);
                logger.info("EponLinkQualityPerf redirctPerformanceData error", e);
            }
        }
        perfDao.insertEponLinkQuality(entityId, PerfTargetConstants.OLT_OPTLINK, perf);
        getCallback(PerformanceCallback.class).sendPerfomaceResult(pList);
    }
}
