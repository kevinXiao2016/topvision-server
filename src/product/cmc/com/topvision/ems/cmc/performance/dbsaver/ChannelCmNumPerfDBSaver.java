/***********************************************************************
 * $Id: DsUserPerfDBSaver.java,v1.0 2012-7-11 下午01:46:06 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.dbsaver;

import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.cmc.facade.domain.ChannelCmNumStatic;
import com.topvision.ems.cmc.perf.engine.CmcPerfDao;
import com.topvision.ems.cmc.performance.domain.ChannelCmNum;
import com.topvision.ems.cmc.performance.domain.ChannelCmNumPerfResult;
import com.topvision.framework.utils.CmcIndexUtils;
import com.topvision.ems.engine.dao.EngineDaoFactory;
import com.topvision.ems.engine.nbi.NbiSupportEngineSaver;
import com.topvision.ems.engine.performance.PerfEngineSaver;
import com.topvision.ems.facade.callback.PerformanceCallback;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.framework.annotation.Engine;

/**
 * @author loyal
 * @created @2012-7-11-下午01:46:06
 * 
 * @modify by Rod 性能采集优化与重构
 * 
 */
@Engine("channelCmNumPerfDBSaver")
public class ChannelCmNumPerfDBSaver extends NbiSupportEngineSaver implements
        PerfEngineSaver<ChannelCmNumPerfResult, OperClass> {
    private Logger logger = LoggerFactory.getLogger(ChannelCmNumPerfDBSaver.class);
    @Autowired
    private EngineDaoFactory engineDaoFactory;

    @Override
    public void save(ChannelCmNumPerfResult channelCmNumPerfResult) {
        if (logger.isDebugEnabled()) {
            logger.debug("ChannelCmNumPerfDBSaver identifyKey[" + channelCmNumPerfResult.getDomain().getIdentifyKey()
                    + "] exec start.");
        }
        CmcPerfDao cmcPerfDao = engineDaoFactory.getEngineDao(CmcPerfDao.class);
        getCallback(PerformanceCallback.class).updateScheduleModifyTime(channelCmNumPerfResult.getCmcId(),
                channelCmNumPerfResult.getDomain().getCategory());
        List<ChannelCmNumStatic> channelCmNumStatics = channelCmNumPerfResult.getChannelCmNumStatics();
        if (channelCmNumStatics != null) {
            for (ChannelCmNumStatic channelCmNumStatic : channelCmNumStatics) {
                ChannelCmNum channelCmNum = new ChannelCmNum();
                channelCmNum.setEntityId(channelCmNumPerfResult.getEntityId());
                channelCmNum.setCmcId(channelCmNumPerfResult.getCmcId());
                channelCmNum.setDt(new Timestamp(channelCmNumPerfResult.getDt()));
                channelCmNum.setChannelIndex(channelCmNumStatic.getChannelIndex());
                channelCmNum.setChannelType(CmcIndexUtils.getChannelType(channelCmNumStatic.getChannelIndex())
                        .intValue());
                channelCmNum.setCmNumActive(channelCmNumStatic.getCmNumActive());
                channelCmNum.setCmNumOffline(channelCmNumStatic.getCmNumOffline());
                channelCmNum.setCmNumOnline(channelCmNumStatic.getCmNumOnline());
                channelCmNum.setCmNumRregistered(channelCmNumStatic.getCmNumRregistered());
                channelCmNum.setCmNumTotal(channelCmNumStatic.getCmNumTotal());
                channelCmNum.setCmNumUnregistered(channelCmNumStatic.getCmNumUnregistered());
                cmcPerfDao.recordChannelCmNum(channelCmNum);
                redirctPerformanceData(channelCmNumStatic, channelCmNumPerfResult,
                        channelCmNumPerfResult.getEntityId(), channelCmNumStatic.getChannelIndex());
            }
        }
        // Add by Rod 放在此处执行有两个原因：
        // 第一必须保证数据存储正确后进行阈值判断，第二由于引用同一个result对象，阈值操作处理器和存储线程都可能改变对象
        // getCallback(PerformanceCallback.class).sendPerfomaceResult(performanceResult);
    }
}
