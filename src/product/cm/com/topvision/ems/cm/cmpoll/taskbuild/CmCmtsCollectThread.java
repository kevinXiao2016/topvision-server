package com.topvision.ems.cm.cmpoll.taskbuild;

import com.topvision.ems.cm.cmpoll.config.service.CmPollConfigService;
import com.topvision.ems.cm.cmpoll.facade.CmPollFacade;
import com.topvision.ems.cm.cmpoll.facade.domain.*;
import com.topvision.ems.cm.cmpoll.taskbuild.dao.CmPollTaskBuildDao;
import com.topvision.ems.cm.cmpoll.taskbuild.domain.CmPollAttribute;
import com.topvision.ems.cm.cmpoll.taskbuild.service.CmPollTaskBuildService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.snmp.SnmpParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by jay on 17-9-27.
 */
public class CmCmtsCollectThread extends Thread {
    public static int threadCount;
    public static Object syObj = new Object();
    private static Logger logger = LoggerFactory.getLogger(CmCmtsCollectThread.class);
    private EntityService entityService;
    private CmPollTaskBuildDao cmPollTaskBuildDao;
    private CmPollConfigService cmPollConfigService;
    private CmPollFacade cmPollFacade;
    private CmPollTaskBuildService cmPollTaskBuildService;
    private Long entityId;
    private List<String> specifiedCmList = new ArrayList<>();
    private Boolean remoteQuery = false;
    private Long collectTime;
    @Override
    public void run() {
        synchronized (syObj) {
            threadCount++;
            setName("CmCmtsCollectThread-" + threadCount);
            logger.debug("CmCmtsCollectThread entityId:" + entityId + " start");
        }
        try {
            List<UpstreamChannelInfo> upstreamChannelList = null;
            List<DownstreamChannelInfo> downstreamChannelList = null;
            SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
            logger.info("CmPollTaskBuildJob.entityId:" + entityId);
            List<CmPollAttribute> cmList = cmPollTaskBuildDao.selectCmListByEntityId(entityId);
            if (cmPollConfigService.isSpecifiedCmPoll()) {
                for (Iterator<CmPollAttribute> iterator = cmList.iterator(); iterator.hasNext(); ) {
                    CmPollAttribute cmPollAttribute = iterator.next();
                    List<DocsIf3CmPollUsStatus> docsIf3CmPollUsStatuses = null;
                    String cmMac = MacUtils.convertToMaohaoFormat(cmPollAttribute.getStatusMacAddress());
                    if (!specifiedCmList.contains(cmMac)) {
                        if (logger.isTraceEnabled()) {
                            logger.trace("Skip this cm[" + cmMac + "]");
                        }
                        iterator.remove();
                        continue;
                    }
                    try {
                        docsIf3CmPollUsStatuses = cmPollFacade.getDocsIf3CmPollUsStatusByCmIndex(snmpParam, cmPollAttribute.getStatusIndex());
                    } catch (Exception e) {
                        logger.debug("CmPollTaskBuildJob.execute docsIf3CmPollUsStatuses error" + e);
                    }
                    if (docsIf3CmPollUsStatuses == null) {
                        docsIf3CmPollUsStatuses = new ArrayList<>();
                    }
                    try {
                        logger.info("CmPollTaskBuildJob.getUpstreamChannelInfo start");
                        if (upstreamChannelList == null) {
                            upstreamChannelList = cmPollFacade.getUpstreamChannelInfo(snmpParam);
                        }
                        logger.info("CmPollTaskBuildJob.getDownstreamChannelInfo start");
                        if (downstreamChannelList == null) {
                            downstreamChannelList = cmPollFacade.getDownstreamChannelInfo(snmpParam);
                        }
                        logger.info("CmPollTaskBuildJob.getDownstreamChannelInfo end");
                    } catch (Exception ex) {
                        logger.debug("CmPollTaskBuildJob.execute getCmtsCmInfo error" + ex);
                    }
                    if (upstreamChannelList == null) {
                        upstreamChannelList = new ArrayList<>();
                    }
                    if (downstreamChannelList == null) {
                        downstreamChannelList = new ArrayList<>();
                    }
                    cmPollAttribute.setDocsIf3CmPollUsStatusList(docsIf3CmPollUsStatuses);
                    cmPollAttribute.setUpstreamChannelInfo(upstreamChannelList);
                    cmPollAttribute.setDownstreamChannelInfo(downstreamChannelList);
                    if (remoteQuery) {
                        CmPollCmRemoteQuery cmPollCmRemoteQuery = null;
                        List<CmPoll3UsRemoteQuery> cmPoll3UsRemoteQueries = null;
                        List<CmPoll3DsRemoteQuery> cmPoll3DsRemoteQueries = null;
                        try {
                            cmPollCmRemoteQuery = cmPollFacade.getCmPollCmRemoteQueryByCmIndex(snmpParam, cmPollAttribute.getStatusIndex());
                            cmPoll3UsRemoteQueries = cmPollFacade.getCm3UsSignalByCmIndex(snmpParam, cmPollAttribute.getStatusIndex());
                            cmPoll3DsRemoteQueries = cmPollFacade.getCm3DsSignalByCmIndex(snmpParam, cmPollAttribute.getStatusIndex());
                        } catch (Exception e) {
                            logger.debug("CmPollTaskBuildJob.execute remoteQuery error" + e);
                        }
                        if (cmPollCmRemoteQuery == null) {
                            cmPollCmRemoteQuery = new CmPollCmRemoteQuery();
                            cmPollCmRemoteQuery.setCmIndex(cmPollAttribute.getStatusIndex());
                            cmPollCmRemoteQuery.setCmRemoteQueryStatus(2);
                        }
                        if (cmPoll3UsRemoteQueries == null) {
                            cmPoll3UsRemoteQueries = new ArrayList<>();
                        }
                        if (cmPoll3DsRemoteQueries == null) {
                            cmPoll3DsRemoteQueries = new ArrayList<>();
                        }
                        // 设置remoteQuery信息
                        cmPollAttribute.setCmRemoteQueryStatus(cmPollCmRemoteQuery.getCmRemoteQueryStatus());
                        cmPollAttribute.setCmPoll3UsRemoteQueryList(cmPoll3UsRemoteQueries);
                        cmPollAttribute.setCmPoll3DsRemoteQueryList(cmPoll3DsRemoteQueries);
                        CmPollTask cmPollTask = CmPollTaskBuildJob.createCmPollTask(cmPollAttribute, collectTime, true, false);
                        cmPollTaskBuildService.putTaskToQueue(cmPollTask);
                    } else {
                        CmPollTask cmPollTask = CmPollTaskBuildJob.createCmPollTask(cmPollAttribute, collectTime, false, false);
                        cmPollTaskBuildService.putTaskToQueue(cmPollTask);
                    }
                }
            } else {
                List<DocsIf3CmPollUsStatus> docsIf3CmPollUsStatusList = new ArrayList<DocsIf3CmPollUsStatus>();

                Map<Long, CmPollCmRemoteQuery> cmRemoteQueryMap = null;
                Map<Long, List<CmPoll3UsRemoteQuery>> cmUsRemoteQueryMap = null;
                Map<Long, List<CmPoll3DsRemoteQuery>> cmDsRemoteQueryMap = null;
                Map<Long, List<DocsIf3CmPollUsStatus>> docsIf3CmUsStatusMap = null;
                // 获取DocsIf3CmPollUsStatus信息
                try {
                    logger.info("CmPollTaskBuildJob.getDocsIf3CmPollUsStatus start");
                    docsIf3CmPollUsStatusList = cmPollFacade.getDocsIf3CmPollUsStatus(snmpParam);
                    logger.info("CmPollTaskBuildJob.getUpstreamChannelInfo start");
                    upstreamChannelList = cmPollFacade.getUpstreamChannelInfo(snmpParam);
                    logger.info("CmPollTaskBuildJob.getDownstreamChannelInfo start");
                    downstreamChannelList = cmPollFacade.getDownstreamChannelInfo(snmpParam);
                    logger.info("CmPollTaskBuildJob.getDownstreamChannelInfo end");
                } catch (Exception ex) {
                    logger.debug("CmPollTaskBuildJob.execute getCmtsCmInfo error" + ex);
                }
                // 将DocsIf3CmPollUsStatus信息放在map中，方便后面获取.map中主键为cmId，值为cm
                docsIf3CmUsStatusMap = buildCmDocs3UsStatusMap(docsIf3CmPollUsStatusList);
                if (upstreamChannelList == null) {
                    upstreamChannelList = new ArrayList<>();
                }
                if (downstreamChannelList == null) {
                    downstreamChannelList = new ArrayList<>();
                }
                if (remoteQuery) {
                    // 获取remoteQuery信息
                    List<CmPollCmRemoteQuery> cmPollCmRemoteQueryList = new ArrayList<CmPollCmRemoteQuery>();
                    List<CmPoll3UsRemoteQuery> cmPoll3UsRemoteQueryList = new ArrayList<CmPoll3UsRemoteQuery>();
                    List<CmPoll3DsRemoteQuery> cmPoll3DsRemoteQueryList = new ArrayList<CmPoll3DsRemoteQuery>();
                    try {
                        logger.info("CmPollTaskBuildJob.getCmPollCmRemoteQueryList start");
                        cmPollCmRemoteQueryList = cmPollFacade.getCmPollCmRemoteQueryList(snmpParam);
                        logger.info("CmPollTaskBuildJob.getCm3UsSignal start");
                        cmPoll3UsRemoteQueryList = cmPollFacade.getCm3UsSignal(snmpParam);
                        logger.info("CmPollTaskBuildJob.getCm3DsSignal start");
                        cmPoll3DsRemoteQueryList = cmPollFacade.getCm3DsSignal(snmpParam);
                        logger.info("CmPollTaskBuildJob.getCm3DsSignal end");
                    } catch (Exception ex) {
                        logger.debug("CmPollTaskBuildJob.execute getRemoteQueryInfo error" + ex);
                    }
                    // 将remoteQuery信息放在map中，方便后面获取.map中主键为cmIndex，值为cm remoteQuery信息列表
                    cmRemoteQueryMap = buildCmRemoteQueryMap(cmPollCmRemoteQueryList);
                    cmUsRemoteQueryMap = buildCmUsRemoteQueryMap(cmPoll3UsRemoteQueryList);
                    cmDsRemoteQueryMap = buildCmDsRemoteQueryMap(cmPoll3DsRemoteQueryList);
                    if (logger.isTraceEnabled()) {
                        logger.trace("{}",cmRemoteQueryMap);
                        logger.trace("{}",cmUsRemoteQueryMap);
                        logger.trace("{}",cmDsRemoteQueryMap);
                    }
                }
                for (CmPollAttribute cmPollAttribute : cmList) {
                    try {
                        cmPollAttribute.setDocsIf3CmPollUsStatusList(docsIf3CmUsStatusMap.get(cmPollAttribute
                                .getStatusIndex()));
                        cmPollAttribute.setUpstreamChannelInfo(upstreamChannelList);
                        cmPollAttribute.setDownstreamChannelInfo(downstreamChannelList);
                        if (remoteQuery) {
                            // 设置remoteQuery信息
                            cmPollAttribute.setCmRemoteQueryStatus(cmRemoteQueryMap.get(
                                    cmPollAttribute.getStatusIndex()).getCmRemoteQueryStatus());
                            cmPollAttribute.setCmPoll3UsRemoteQueryList(cmUsRemoteQueryMap.get(cmPollAttribute
                                    .getStatusIndex()));
                            cmPollAttribute.setCmPoll3DsRemoteQueryList(cmDsRemoteQueryMap.get(cmPollAttribute
                                    .getStatusIndex()));
                            CmPollTask cmPollTask = CmPollTaskBuildJob.createCmPollTask(cmPollAttribute, collectTime, true, false);
                            cmPollTaskBuildService.putTaskToQueue(cmPollTask);
                        } else {
                            CmPollTask cmPollTask = CmPollTaskBuildJob.createCmPollTask(cmPollAttribute, collectTime, false, false);
                            cmPollTaskBuildService.putTaskToQueue(cmPollTask);
                        }
                    } catch (Exception e) {
                        logger.error("CmPollTaskBuildJob.data format error", e);
                        if (logger.isTraceEnabled()) {
                            logger.trace("{}",cmPollAttribute);
                        }
                    }
                }
            }
        }catch (Exception e) {
            logger.error("CmPollTaskBuildJob error", e);
        } finally {
            synchronized (syObj) {
                threadCount--;
                logger.debug("CmCmtsCollectThread entityId:" + entityId + " end");
            }
        }
    }

    /**
     * 将一个entity下的DocsIf3CmPollUsStatus信息放入map中，map主键为cmId，值为此cm DocsIf3CmPollUsStatus信息列表
     *
     * @return
     */
    private Map<Long, List<DocsIf3CmPollUsStatus>> buildCmDocs3UsStatusMap(
            List<DocsIf3CmPollUsStatus> docsIf3CmPollUsStatusList) {
        Map<Long, List<DocsIf3CmPollUsStatus>> docsIf3CmUsStatusMap = new HashMap<Long, List<DocsIf3CmPollUsStatus>>();
        List<DocsIf3CmPollUsStatus> docsIf3CmUsStatusTemp;
        for (DocsIf3CmPollUsStatus docsIf3CmPollUsStatus : docsIf3CmPollUsStatusList) {
            docsIf3CmUsStatusTemp = (List<DocsIf3CmPollUsStatus>) docsIf3CmUsStatusMap.get(docsIf3CmPollUsStatus
                    .getCmRegStatusId());
            if (docsIf3CmUsStatusTemp == null) {
                docsIf3CmUsStatusTemp = new ArrayList<DocsIf3CmPollUsStatus>();
            }
            docsIf3CmUsStatusTemp.add(docsIf3CmPollUsStatus);
            docsIf3CmUsStatusMap.put(docsIf3CmPollUsStatus.getCmRegStatusId(), docsIf3CmUsStatusTemp);
        }
        return docsIf3CmUsStatusMap;
    }

    /**
     * 将一个entity下的Cm3UsRemoteQuery信息放入map中，map主键为cmIndex，值为此cm Cm3UsRemoteQuery信息列表
     *
     * @return
     */
    private Map<Long, List<CmPoll3DsRemoteQuery>> buildCmDsRemoteQueryMap(
            List<CmPoll3DsRemoteQuery> cmPoll3DsRemoteQueryList) {
        Map<Long, List<CmPoll3DsRemoteQuery>> cmPoll3DsRemoteQueryMap = new HashMap<Long, List<CmPoll3DsRemoteQuery>>();
        List<CmPoll3DsRemoteQuery> cmPoll3DsRemoteQueryTemp;
        for (CmPoll3DsRemoteQuery cmPoll3DsRemoteQuery : cmPoll3DsRemoteQueryList) {
            cmPoll3DsRemoteQueryTemp = (List<CmPoll3DsRemoteQuery>) cmPoll3DsRemoteQueryMap.get(cmPoll3DsRemoteQuery
                    .getCmIndex());
            if (cmPoll3DsRemoteQueryTemp == null) {
                cmPoll3DsRemoteQueryTemp = new ArrayList<CmPoll3DsRemoteQuery>();
            }
            cmPoll3DsRemoteQueryTemp.add(cmPoll3DsRemoteQuery);
            cmPoll3DsRemoteQueryMap.put(cmPoll3DsRemoteQuery.getCmIndex(), cmPoll3DsRemoteQueryTemp);
        }
        return cmPoll3DsRemoteQueryMap;
    }

    private Map<Long, CmPollCmRemoteQuery> buildCmRemoteQueryMap(List<CmPollCmRemoteQuery> cmPollCmRemoteQueryList) {
        Map<Long, CmPollCmRemoteQuery> cmRemoteQueryMap = new HashMap<Long, CmPollCmRemoteQuery>();
        for (CmPollCmRemoteQuery cmPollCmRemoteQuery : cmPollCmRemoteQueryList) {
            cmRemoteQueryMap.put(cmPollCmRemoteQuery.getCmIndex(), cmPollCmRemoteQuery);
        }
        return cmRemoteQueryMap;
    }

    /**
     * 将一个entity下的Cm3UsRemoteQuery信息放入map中，map主键为cmIndex，值为此cm Cm3UsRemoteQuery信息列表
     *
     * @return
     */
    private Map<Long, List<CmPoll3UsRemoteQuery>> buildCmUsRemoteQueryMap(
            List<CmPoll3UsRemoteQuery> cmPoll3UsRemoteQueryList) {
        Map<Long, List<CmPoll3UsRemoteQuery>> cmPoll3UsRemoteQueryMap = new HashMap<Long, List<CmPoll3UsRemoteQuery>>();
        List<CmPoll3UsRemoteQuery> cmPoll3UsRemoteQueryTemp;
        for (CmPoll3UsRemoteQuery cmPoll3UsRemoteQuery : cmPoll3UsRemoteQueryList) {
            cmPoll3UsRemoteQueryTemp = (List<CmPoll3UsRemoteQuery>) cmPoll3UsRemoteQueryMap.get(cmPoll3UsRemoteQuery
                    .getCmIndex());
            if (cmPoll3UsRemoteQueryTemp == null) {
                cmPoll3UsRemoteQueryTemp = new ArrayList<CmPoll3UsRemoteQuery>();
            }
            cmPoll3UsRemoteQueryTemp.add(cmPoll3UsRemoteQuery);
            cmPoll3UsRemoteQueryMap.put(cmPoll3UsRemoteQuery.getCmIndex(), cmPoll3UsRemoteQueryTemp);
        }
        return cmPoll3UsRemoteQueryMap;
    }

    public EntityService getEntityService() {
        return entityService;
    }

    public void setEntityService(EntityService entityService) {
        this.entityService = entityService;
    }

    public CmPollTaskBuildDao getCmPollTaskBuildDao() {
        return cmPollTaskBuildDao;
    }

    public void setCmPollTaskBuildDao(CmPollTaskBuildDao cmPollTaskBuildDao) {
        this.cmPollTaskBuildDao = cmPollTaskBuildDao;
    }

    public CmPollConfigService getCmPollConfigService() {
        return cmPollConfigService;
    }

    public void setCmPollConfigService(CmPollConfigService cmPollConfigService) {
        this.cmPollConfigService = cmPollConfigService;
    }

    public CmPollFacade getCmPollFacade() {
        return cmPollFacade;
    }

    public void setCmPollFacade(CmPollFacade cmPollFacade) {
        this.cmPollFacade = cmPollFacade;
    }

    public CmPollTaskBuildService getCmPollTaskBuildService() {
        return cmPollTaskBuildService;
    }

    public void setCmPollTaskBuildService(CmPollTaskBuildService cmPollTaskBuildService) {
        this.cmPollTaskBuildService = cmPollTaskBuildService;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public List<String> getSpecifiedCmList() {
        return specifiedCmList;
    }

    public void setSpecifiedCmList(List<String> specifiedCmList) {
        this.specifiedCmList = specifiedCmList;
    }

    public Boolean getRemoteQuery() {
        return remoteQuery;
    }

    public void setRemoteQuery(Boolean remoteQuery) {
        this.remoteQuery = remoteQuery;
    }

    public Long getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Long collectTime) {
        this.collectTime = collectTime;
    }

    public static void initThreadCount() {
        waitFinish();
        synchronized (syObj) {
            threadCount = 0;
        }
    }

    public static void waitFinish() {
        while (threadCount > 0) {
            logger.trace("CmCmtsCollectThread waitFinish " + threadCount);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {

            }
        }
    }
}
