/***********************************************************************
 * $Id: CmtsSystemPerfDBSaver.java,v1.0 2012-7-11 下午01:56:46 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmts.performance.dbsaver;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.cmc.performance.facade.CmcServiceQuality;
import com.topvision.ems.cmc.performance.handle.CmcCpuUsedHandle;
import com.topvision.ems.cmc.performance.handle.CmcMemUsedHandle;
import com.topvision.ems.cmts.performance.domain.CmtsServiceQualityResult;
import com.topvision.ems.cmts.performance.domain.CmtsSystemPerf;
import com.topvision.ems.cmts.performance.domain.CmtsSystemResult;
import com.topvision.ems.cmts.performance.engine.CmtsPerfDao;
import com.topvision.ems.cmts.performance.handle.CmtsSystemHandle;
import com.topvision.ems.engine.BaseEngine;
import com.topvision.ems.engine.dao.EngineDaoFactory;
import com.topvision.ems.engine.performance.PerfEngineSaver;
import com.topvision.ems.facade.callback.PerformanceCallback;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.framework.annotation.Engine;
import com.topvision.platform.message.event.EntityValueEvent;
import com.topvision.platform.message.event.EntityValueListener;

/**
 * @author jay
 * @created @2012-7-11-下午01:56:46
 */
@Engine("cmtsSystemPerfDBSaver")
public class CmtsSystemPerfDBSaver extends BaseEngine implements PerfEngineSaver<CmtsSystemResult, OperClass> {
    private Logger logger = LoggerFactory.getLogger(CmtsSystemPerfDBSaver.class);
    @Autowired
    private EngineDaoFactory engineDaoFactory;

    // 将采集的数据保存到数据库中
    public void save(CmtsSystemResult sysPerf) {
        if (logger.isDebugEnabled()) {
            logger.debug("CmtsSystemPerfDBSaver identifyKey[" + sysPerf.getDomain().getIdentifyKey() + "] exec start.");
        }
        CmtsPerfDao cmtsPerfDao = engineDaoFactory.getEngineDao(CmtsPerfDao.class);
        Long entityId = sysPerf.getSystemAttribute().getEntityId();
        //Modify by Victor@20150623把原来以下代码移到handle进行处理
        PerformanceData data = new PerformanceData(sysPerf.getEntityId(), CmtsSystemHandle.HANDLE_ID, sysPerf);
        getCallback(PerformanceCallback.class).sendPerfomaceResult(data);

        //CMTS的CPU和内存利用率进行性能展示和阈值告警处理
        List<PerformanceData> perfDataList = new ArrayList<PerformanceData>();
        CmtsSystemPerf servicePerf = (CmtsSystemPerf) sysPerf.getDomain();
        CmtsServiceQualityResult serviceResult = sysPerf.getServiceResult();
        double cpuUsed = serviceResult.getCpuUtilization();
        double memUsed = serviceResult.getMemUtilization();
        //保存CPU和内存性能数据以及进行阈值处理
        if (servicePerf.getIsCpuPerf()) {
            if (cpuUsed >= 0 && cpuUsed < 1D) {
                cmtsPerfDao.insertCmtsCpuQuality(serviceResult);
                //进行阈值处理
                CmcServiceQuality result = new CmcServiceQuality();
                result.setCmcId(entityId);
                result.setCpuUsed((float) cpuUsed * 100);
                PerformanceData cmtsCpuUsedData = new PerformanceData(entityId, CmcCpuUsedHandle.CC_CPUUSED_FLAG,
                        result);
                perfDataList.add(cmtsCpuUsedData);
            }
        }
        if (servicePerf.getIsMemPerf()) {
            if (memUsed >= 0 && memUsed < 1D) {
                cmtsPerfDao.insertCmtsMemQuality(serviceResult);
                //进行阈值处理
                CmcServiceQuality result = new CmcServiceQuality();
                result.setCmcId(entityId);
                result.setMemUsed((float) memUsed * 100);
                PerformanceData cmcMemUsedData = new PerformanceData(entityId, CmcMemUsedHandle.CC_MEMUSED_FLAG, result);
                perfDataList.add(cmcMemUsedData);
            }
        }
        //阈值告警处理
        getCallback(PerformanceCallback.class).sendPerfomaceResult(perfDataList);

        // YangYi修改 2013-10-22 修正CMTS刷新之后状态错误
        EntityValueEvent event = new EntityValueEvent(entityId);
        event.setEntityId(entityId);
        event.setState(true);
        event.setSysUpTime(sysPerf.getSystemAttribute().getSysUpTime());
        if (cpuUsed >= 0 && cpuUsed < 1F) {
            event.setCpu(cpuUsed);
        }
        if (memUsed >= 0 && memUsed < 1F) {
            event.setMem(memUsed);
        }
        event.setActionName("performanceChanged");
        event.setListener(EntityValueListener.class);
        getCallback(PerformanceCallback.class).addServerMessage(event);
    }
}