/***********************************************************************
 * $Id: CmcSystemHandle.java,v1.0 2015年6月23日 下午5:02:21 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.handle;

import java.sql.Timestamp;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.ccmts.dao.CmcDao;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.perf.dao.CmcPerfDao;
import com.topvision.ems.cmc.performance.domain.SystemPerfResult;
import com.topvision.ems.cmc.util.CmcConstants;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.performance.domain.PerfThresholdEventParams;
import com.topvision.ems.performance.handle.PerformanceHandle;
import com.topvision.platform.message.event.EntityValueEvent;
import com.topvision.platform.message.event.EntityValueListener;
import com.topvision.platform.message.service.MessageService;

/**
 * @author Victor
 * @created @2015年6月23日-下午5:02:21
 *
 */
@Service("cmcSystemHandle")
public class CmcSystemHandle extends PerformanceHandle {
    public static String HANDLE_ID = "CCMTS_SYSTEM";
    @Autowired
    private MessageService messageService;
    @Autowired
    private EntityService entityService;
    @Autowired
    private CmcDao cmcDao;
    @Autowired
    private CmcPerfDao cmcPerfDao;

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
        SystemPerfResult sysPerf = (SystemPerfResult) data.getPerfData();
        long cmcId = sysPerf.getCmcId();
        CmcAttribute cmc = sysPerf.getSystemPerf();
        int cmcStatusOld = cmcDao.getCmcAttributeByCmcId(cmcId).getTopCcmtsSysStatus();//上次CC的状态
        if (cmc == null) {
            return;
        }
        Entity entity = entityService.getEntity(sysPerf.getEntityId());
        Long ccWithoutAgentType = entityTypeService.getCcmtswithoutagentType();
        Long entityId;
        if (entityTypeService.isOlt(entity.getTypeId())) {
            entityId = cmcPerfDao.getTopoEntityIdByCmcId(cmcId, ccWithoutAgentType);
        } else {
            entityId = sysPerf.getEntityId();
        }
        // 构造消息
        // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        EntityValueEvent event = new EntityValueEvent(sysPerf.getEntityId());
        event.setEntityId(sysPerf.getEntityId());
        // event.setSysUpTime(sdf.format(new Date(System.currentTimeMillis())));
        event.setSysUpTime(cmc.getTopCcmtsSysUpTime().toString());
        event.setActionName("performanceChanged");
        event.setListener(EntityValueListener.class);
        if (CmcConstants.TOPCCMTSSYSSTATUS_ONLINE.equals(sysPerf.getSystemPerf().getTopCcmtsSysStatus())) {
            event.setCpu(cmc.getTopCcmtsSysCPURatio().doubleValue() / 100);
            event.setMem(cmc.getTopCcmtsSysRAMRatio().doubleValue() / 100);
            event.setDisk(cmc.getTopCcmtsSysFlashRatio().doubleValue() / 100);
            event.setState(true);
        } else {
            // TODO olt ping不通的情况是不处理这个逻辑的 因为ping不通不代表真就下线了，维持最后一次的状态不变
            // Cmc下线的情况下向快照模块发送下线的消息，并将下挂的所有cm状态修改为offline
            event.setCpu(0d);
            event.setMem(0d);
            event.setDisk(0d);
            event.setState(false);
            cmcPerfDao.changeCmStatusOffine(cmcId);
            //记录CC由在线变为离线的时间 Added by huangdongsheng
            if (cmcStatusOld == CmcConstants.TOPCCMTSSYSSTATUS_ONLINE) {
                Timestamp statuschangeTime = new Timestamp(sysPerf.getDt());
                cmcDao.updateStatusChangeTime(cmcId, statuschangeTime);
            }
        }
        // 将cmc系统信息保存到数据库中
        cmcPerfDao.recordCcmtsSystemInfo(cmc);
        if (entityId != null) {
            messageService.addMessage(event);
        }
    }

}
