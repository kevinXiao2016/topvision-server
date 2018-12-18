/***********************************************************************
 * $Id: CmtsSystemHandle.java,v1.0 2015年6月23日 下午4:02:07 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmts.performance.handle;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmts.performance.domain.CmtsSystemResult;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.performance.domain.PerfThresholdEventParams;
import com.topvision.ems.performance.handle.PerformanceHandle;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author Victor
 * @created @2015年6月23日-下午4:02:07
 *
 */
@Service("cmtsSystemHandle")
public class CmtsSystemHandle extends PerformanceHandle {
    public static String HANDLE_ID = "CMTS_SYSTEM";
    @Autowired
    private EntityService entityService;

    @Override
    @PostConstruct
    public void initialize() {
        performanceStatisticsCenter.registerPerformanceHandler(HANDLE_ID, this);
    }

    @Override
    @PreDestroy
    public void destroy() {
        performanceStatisticsCenter.unregisterPerformanceHandler(HANDLE_ID);
    }

    @Override
    public String getTypeCode(PerformanceData data) {
        return HANDLE_ID;
    }

    @Override
    public PerfThresholdEventParams getEventParams(PerformanceData data) {
        return null;
    }

    @Override
    public void handle(PerformanceData data) {
        CmtsSystemResult sysPerf = (CmtsSystemResult) data.getPerfData();
        Long entityId = sysPerf.getSystemAttribute().getEntityId();
        try {
            // 将cmc系统信息保存到数据库中
            Entity entity = entityService.getEntity(entityId);
            SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
            entity.setParam(snmpParam);
            entity.setSysDescr(sysPerf.getSystemAttribute().getSysDescr());
            entity.setSysContact(sysPerf.getSystemAttribute().getSysContact());
            entity.setSysLocation(sysPerf.getSystemAttribute().getSysLocation());
            entity.setSysName(sysPerf.getSystemAttribute().getSysName());
            entity.setSysUpTime(sysPerf.getSystemAttribute().getSysUpTime());
            entityDao.updateEntity(entity);
        } catch (Exception e) {
            logger.error("Update Cmts[{}] info failed: {}", entityId, e);
        }
    }

}
