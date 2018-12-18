/***********************************************************************
 * $Id: CmStatusHandle.java,v1.0 2015年6月23日 下午8:21:21 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.handle;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.cm.dao.CmRefreshDao;
import com.topvision.ems.cmc.cm.service.CmListService;
import com.topvision.ems.cmc.constants.CpeAnalyseConstants;
import com.topvision.ems.cmc.cpe.service.CpeAnalyseService;
import com.topvision.ems.cmc.cpe.service.CpeService;
import com.topvision.ems.cmc.domain.CmCollectConfig;
import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.cmc.facade.domain.CmPartialSvcState;
import com.topvision.ems.cmc.facade.domain.DocsIf3CmtsCmUsStatus;
import com.topvision.ems.cmc.performance.domain.CmNum;
import com.topvision.ems.cmc.performance.domain.CmStatusPerfResult;
import com.topvision.ems.cmc.topology.dao.CmcDiscoveryDao;
import com.topvision.framework.utils.CmcIndexUtils;
import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.ems.facade.domain.ScheduleMessage;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.performance.dao.PerformanceDao;
import com.topvision.ems.performance.domain.PerfThresholdEventParams;
import com.topvision.ems.performance.handle.PerformanceHandle;
import com.topvision.framework.common.CollectTimeRange;
import com.topvision.framework.common.CollectTimeUtil;

/**
 * @author Victor
 * @created @2015年6月23日-下午8:21:21
 *
 */
@Service("cmStatusHandle")
public class CmStatusHandle extends PerformanceHandle {
    public static String HANDLE_ID = "CM_STATUS";
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Autowired
    private CmcDiscoveryDao cmcDiscoveryDao;
    @Autowired
    private CmRefreshDao cmRefreshDao;
    @Autowired
    private CpeAnalyseService cpeAnalyseService;
    @Autowired
    private CpeService cpeService;
    @Autowired
    private PerformanceDao performanceDao;
    @Autowired
    private EntityService entityService;
    @Autowired
    private CmListService cmListService;

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
        CmStatusPerfResult cmStatusPerfResult = (CmStatusPerfResult) data.getPerfData();
        Long entityId = cmStatusPerfResult.getEntityId();

        // TODO Victor临时解决办法
        Long entityType = entityService.getEntity(entityId).getTypeId();
        boolean isOlt = entityTypeService.isOlt(entityType);
        boolean isCmts = entityTypeService.isCmts(entityType);
        try {
            List<CmAttribute> cmAttributes = cmStatusPerfResult.getCmAttributes();
            if (cmAttributes != null) {
                Map<Long, Long> cmcIds = new HashMap<Long, Long>();
                Map<Long, List<CmAttribute>> cmcAttributeMap = new HashMap<Long, List<CmAttribute>>();
                if (isOlt) {
                    if (!cmStatusPerfResult.isRealtimeRefresh()) {
                        List<Long> cmcIdList = cmcDiscoveryDao.getCmcIdByOlt(entityId);
                        for (Long cmcId : cmcIdList) {
                            List<CmAttribute> cas = new ArrayList<CmAttribute>();
                            cmcAttributeMap.put(cmcId, cas);
                        }
                    }
                    for (CmAttribute cmAttribute : cmAttributes) {
                        // TODO 这里的index需要检查一下
                        Long cmcIndex = CmcIndexUtils.getCmcIndexFromCmIndex(cmAttribute.getStatusIndex());
                        Long cmcId;
                        if (!cmcIds.containsKey(cmcIndex)) {
                            Map<String, Long> map = new HashMap<String, Long>();
                            map.put("entityId", entityId);
                            map.put("cmcIndex", cmcIndex);
                            cmcId = cmcDiscoveryDao.getCmcIdByCmcIndexAndEntityId(map);
                            if (cmcId == null) {
                                logger.debug("entityId [" + entityId + "] cmcIndex [" + cmcIndex + "] cmcId [" + cmcId
                                        + "]");
                                continue;
                            } else {
                                cmcIds.put(cmcIndex, cmcId);
                            }
                        } else {
                            cmcId = cmcIds.get(cmcIndex);
                        }
                        List<CmAttribute> attrs;
                        if (cmcAttributeMap.containsKey(cmcId)) {
                            attrs = cmcAttributeMap.get(cmcId);
                        } else {
                            attrs = new ArrayList<CmAttribute>();
                            cmcAttributeMap.put(cmcId, attrs);
                        }
                        attrs.add(cmAttribute);
                    }
                } else {
                    Long cmcId = entityId;
                    if (isCmts) {
                        for (CmAttribute cmAttribute : cmAttributes) {
                            cmAttribute.setStatusUpChannelIfIndex(null);
                        }
                    }
                    cmcAttributeMap.put(cmcId, cmAttributes);
                }
                CollectTimeUtil ctu = CollectTimeUtil.getCollectTimeUtil(CollectTimeUtil.CmStatus);
                CollectTimeRange ctr = ctu.getCollectTimeRange(cmStatusPerfResult.getDt());
                Map<Long, Map<String, Map<Long, CmNum>>> entityMap = new HashMap<>();
                for (Long cmcId : cmcAttributeMap.keySet()) {
                    List<CmAttribute> attrs = cmcAttributeMap.get(cmcId);
                    if (cmcId != null) {
                        cmRefreshDao.batchRefreshCmAttribute(entityId, cmcId, attrs);
                        if (cmStatusPerfResult.isRealtimeRefresh()) {
                            for (CmAttribute attr : attrs) {
                                addCmNum(entityMap, attr, ctr, cmStatusPerfResult.getDt());
                            }
                        }
                    }
                }
                if (cmStatusPerfResult.isRealtimeRefresh()) {
                    cpeAnalyseService.updateDeviceAllCmNumLast(entityId, entityMap, cmStatusPerfResult.isAllDevice());
                }
                // 做CPE管理为了不影响之前的功能故留下了上面的插入，以下的处理都是CPE管理加入的
                if (logger.isDebugEnabled()) {
                    logger.debug("#############now[" + sdf.format(new Date(System.currentTimeMillis())) + "] dt ["
                            + sdf.format(new Date(cmStatusPerfResult.getDt())) + "]");
                }
                if (cmStatusPerfResult.isCmArrayEmpty()) {
                    CmNum cmNum = new CmNum();
                    cmNum.setEntityId(entityId);
                    cmNum.setTime(new Timestamp(System.currentTimeMillis()));
                    cmNum.setRealTime(new Timestamp(System.currentTimeMillis()));
                    cpeAnalyseService.updateDeviceCmNumLast(cmNum);
                    cpeAnalyseService.updatePonCmNumLast(cmNum);
                    cpeAnalyseService.updateCmtsCmNumLast(cmNum);
                    cpeAnalyseService.updatePortCmNumLast(cmNum);
                }
                if (!cmStatusPerfResult.isRealtimeRefresh()) {
                    try {
                        /**
                         * Added by huangdognsheng 增加上行信道信息、Flap信息插入数据库
                         */
                        List<DocsIf3CmtsCmUsStatus> docsIf3CmtsCmUsStatus = cmStatusPerfResult
                                .getDocsIf3CmtsCmUsStatus();
                        if (docsIf3CmtsCmUsStatus != null) {
                            cmcDiscoveryDao.batchInsertOrUpdateDocsIf3CmtsCmUsStatus(docsIf3CmtsCmUsStatus, entityId);
                        }
                    } catch (Exception e) {
                        logger.debug("", e);
                    }
                    cmcDiscoveryDao.batchInsertInitialDataCmAction(cmStatusPerfResult.getDt(), cmAttributes,
                            cmStatusPerfResult.getEntityId());
                }
            }
        } catch (Exception e) {
            logger.error("CmStatusDBSaver error", e);
        }
        // getCallback(PerformanceCallback.class).sendPerfomaceResult(performanceResult);

        // 增加对partial service的存储
        List<CmPartialSvcState> states = cmStatusPerfResult.getPartialStates();
        try {
            if(states!=null && states.size() != 0) {
                cmListService.saveCmPartialSvcState(entityId, states);
            }
        } catch (Exception e) {
            logger.error("partial service saver error", e);
        }

        try {
            CmCollectConfig cmCollectConfig = cpeService.getCmCollectConfig();
            if (CmCollectConfig.START.intValue() == cmCollectConfig.getCmActionStatisticStatus().intValue()) {
                cpeAnalyseService.append(cmStatusPerfResult);
            }
        } catch (Exception e) {
            logger.error("CmStatusDBSaver error", e);
        }

        try {
            if (!cmStatusPerfResult.isRealtimeRefresh()) {
                ScheduleMessage<?> scheduleMessage = performanceDao
                        .selectByIdentifyAndCategory(cmStatusPerfResult.getEntityId(), "CC_CMSTATUS");
                performanceDao.updateScheduleCollectTime(scheduleMessage.getMonitorId(), cmStatusPerfResult.getDt());
            }
        } catch (Exception e) {
            logger.error("CmStatusDBSaver error", e);
        }

    }

    private void addCmNum(Map<Long, Map<String, Map<Long, CmNum>>> cmtsListMap, CmAttribute cmAttribute,
            CollectTimeRange ctr, long dt) {
        Map<String, Map<Long, CmNum>> cmtsMap;
        Long cmcIndex = CmcIndexUtils.getCmcIndexFromCmIndex(cmAttribute.getStatusIndex());
        if (cmtsListMap.containsKey(cmcIndex)) {
            cmtsMap = cmtsListMap.get(cmcIndex);
        } else {
            cmtsMap = new HashMap<>();
            cmtsListMap.put(cmcIndex, cmtsMap);
        }

        Map<Long, CmNum> upMap;
        if (cmtsMap.containsKey(CpeAnalyseConstants.UPSTREAM)) {
            upMap = cmtsMap.get(CpeAnalyseConstants.UPSTREAM);
        } else {
            upMap = new HashMap<>();
            cmtsMap.put(CpeAnalyseConstants.UPSTREAM, upMap);
        }
        CmNum upCmNum;
        if (upMap.containsKey(cmAttribute.getStatusUpChannelIfIndex())) {
            upCmNum = upMap.get(cmAttribute.getStatusUpChannelIfIndex());
        } else {
            upCmNum = new CmNum();
            upCmNum.setRealTimeLong(dt);
            upCmNum.setTimeLong(ctr.getEndTimeLong());
            upMap.put(cmAttribute.getStatusUpChannelIfIndex(), upCmNum);
        }
        makeCmNum(upCmNum, cmAttribute);
        Map<Long, CmNum> downMap;
        if (cmtsMap.containsKey(CpeAnalyseConstants.DOWNSTREAM)) {
            downMap = cmtsMap.get(CpeAnalyseConstants.DOWNSTREAM);
        } else {
            downMap = new HashMap<>();
            cmtsMap.put(CpeAnalyseConstants.DOWNSTREAM, downMap);
        }
        CmNum downCmNum;
        if (downMap.containsKey(cmAttribute.getStatusDownChannelIfIndex())) {
            downCmNum = downMap.get(cmAttribute.getStatusDownChannelIfIndex());
        } else {
            downCmNum = new CmNum();
            downCmNum.setRealTimeLong(dt);
            downCmNum.setTimeLong(ctr.getEndTimeLong());
            downMap.put(cmAttribute.getStatusDownChannelIfIndex(), downCmNum);
        }
        makeCmNum(downCmNum, cmAttribute);
    }

    private void makeCmNum(CmNum cmNum, CmAttribute cmAttribute) {
        if (cmAttribute.isCmOnline()) {
            cmNum.addOnline();
        } else if (cmAttribute.isCmOffline()) {
            cmNum.addOffline();
        } else {
            cmNum.addOther();
        }
    }
}
