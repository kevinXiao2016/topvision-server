/***********************************************************************
 * $Id: CmPollTaskBuildJob.java,v1.0 2015年3月6日 下午2:29:11 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.cmpoll.taskbuild;

import java.util.*;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;

import com.topvision.ems.cm.cmpoll.config.service.CmPollConfigService;
import com.topvision.ems.cm.cmpoll.facade.CmPollFacade;
import com.topvision.ems.cm.cmpoll.facade.domain.CmPollEndTask;
import com.topvision.ems.cm.cmpoll.facade.domain.CmPollNormalTask;
import com.topvision.ems.cm.cmpoll.facade.domain.CmPollRemoteQueryTask;
import com.topvision.ems.cm.cmpoll.facade.domain.CmPollTask;
import com.topvision.ems.cm.cmpoll.taskbuild.dao.CmPollTaskBuildDao;
import com.topvision.ems.cm.cmpoll.taskbuild.domain.CmPollAttribute;
import com.topvision.ems.cm.cmpoll.taskbuild.service.CmPollTaskBuildService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.common.CollectTimeUtil;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.platform.facade.FacadeFactory;

/**
 * @author loyal
 * @created @2015年3月6日-下午2:29:11
 * 
 */
public class CmPollTaskBuildJob implements Job {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    private Long collectTime;
    private CmPollTaskBuildService cmPollTaskBuildService;
    private EntityTypeService entityTypeService;
    private EntityService entityService;
    private CmPollTaskBuildDao cmPollTaskBuildDao;
    private CmPollConfigService cmPollConfigService;
    private FacadeFactory facadeFactory;
    private CmPollFacade cmPollFacade;
    private Boolean remoteQuery = false;
    private List<Long> entityIdList = new ArrayList<Long>();

    @Override
    public void execute(JobExecutionContext ctx) throws JobExecutionException {
        logger.info("CmPollTaskBuildJob.execute");
        // 获取变量及bean
        getBeanAndValue(ctx);
        // 判断上轮轮询是否结束
        if (cmPollTaskBuildService.isLastPollEnd()) {
            logger.trace("CmPoll Task Start" + new Date().toString());
            // 计算采集时间
            setCollectTime();
            Long cmOnLine = cmPollTaskBuildDao.getCmOnLineNum();
            //判断是否为指定CM轮询，如果是的则读取指定CM列表
            List<String> specifiedCmList = new ArrayList<>();
            if (cmPollConfigService.isSpecifiedCmPoll()) {
                specifiedCmList = cmPollConfigService.getSpecifiedCmList();
            }
            // 通知轮询开始
            cmPollTaskBuildService.fireRoundStartMessage(collectTime, cmOnLine);
            List<Long> cmcEntityIdList = cmPollTaskBuildDao.selectEntityIdWithIp(entityTypeService
                    .getCcmtswithagentType());
            List<Long> oltEntityIdList = cmPollTaskBuildDao.selectEntityIdWithIp(entityTypeService.getOltType());
            if (cmcEntityIdList != null) {
                entityIdList.addAll(cmcEntityIdList);
            }
            if (oltEntityIdList != null) {
                entityIdList.addAll(oltEntityIdList);
            }
            logger.info("CmPollTaskBuildJob.entityIdList:" + entityIdList);
            remoteQuery = cmPollConfigService.isCmPollRemoteQuery();
            CmCmtsCollectThread.initThreadCount();
            for (Long entityId : entityIdList) {
                CmCmtsCollectThread cmCmtsCollectThread = new CmCmtsCollectThread();
                cmCmtsCollectThread.setEntityService(entityService);
                cmCmtsCollectThread.setCmPollTaskBuildDao(cmPollTaskBuildDao);
                cmCmtsCollectThread.setCmPollConfigService(cmPollConfigService);
                cmCmtsCollectThread.setCmPollFacade(cmPollFacade);
                cmCmtsCollectThread.setCmPollTaskBuildService(cmPollTaskBuildService);
                cmCmtsCollectThread.setEntityId(entityId);
                cmCmtsCollectThread.setSpecifiedCmList(specifiedCmList);
                cmCmtsCollectThread.setRemoteQuery(remoteQuery);
                cmCmtsCollectThread.setCollectTime(collectTime);
                cmCmtsCollectThread.start();
            }

            CmCmtsCollectThread.waitFinish();
            // 构造结束任务
            CmPollTask cmPollTask = createCmPollTask(new CmPollAttribute(), collectTime, false, true);
            cmPollTaskBuildService.putTaskToQueue(cmPollTask);
            cmPollTaskBuildService.fireRoundEndMessage(collectTime);
            logger.trace("CmPoll Task End" + new Date().toString());
        } else {
            logger.info("Last CmPoll Build Is Running, CmPoll Task End" + new Date().toString());
        }

        logger.info("CmPollTaskBuildJob.execute end");
    }


    /**
     * 构建任务
     *
     * @param collectTime
     * @param remoteQuery
     * @param end
     * @return
     */
    public static CmPollTask createCmPollTask(CmPollAttribute cmPollAttribute, Long collectTime, Boolean remoteQuery,
                                        Boolean end) {
        if (end) {
            return new CmPollEndTask();
        } else if (remoteQuery) {
            CmPollRemoteQueryTask cmPollTask = new CmPollRemoteQueryTask();
            cmPollTask.setCmRemoteQueryStatus(cmPollAttribute.getCmRemoteQueryStatus());
            cmPollTask.setCm3DsRemoteQueryList(cmPollAttribute.getCmPoll3DsRemoteQueryList());
            cmPollTask.setCm3UsRemoteQueryList(cmPollAttribute.getCmPoll3UsRemoteQueryList());
            cmPollTask.setDocsIf3CmPollUsStatusList(cmPollAttribute.getDocsIf3CmPollUsStatusList());
            cmPollTask.setUpstreamChannelInfo(cmPollAttribute.getUpstreamChannelInfo());
            cmPollTask.setDownstreamChannelList(cmPollAttribute.getDownstreamChannelInfo());
            setBaseTaskInfo(cmPollTask, cmPollAttribute);
            return cmPollTask;
        } else {
            CmPollNormalTask cmPollTask = new CmPollNormalTask();
            cmPollTask.setDocsIf3CmPollUsStatusList(cmPollAttribute.getDocsIf3CmPollUsStatusList());
            cmPollTask.setUpstreamChannelInfo(cmPollAttribute.getUpstreamChannelInfo());
            cmPollTask.setDownstreamChannelList(cmPollAttribute.getDownstreamChannelInfo());
            setBaseTaskInfo(cmPollTask, cmPollAttribute);
            return cmPollTask;
        }
    }

    /**
     * 将cm的基本信息设置到task中
     *
     * @param cmPollTask
     * @param cmPollAttribute
     * @return
     */
    private static CmPollTask setBaseTaskInfo(CmPollTask cmPollTask, CmPollAttribute cmPollAttribute) {
        Long time = System.currentTimeMillis();
        cmPollTask.setCollectTime(time);
        cmPollTask.setEntityId(cmPollAttribute.getEntityId());
        cmPollTask.setCmcId(cmPollAttribute.getCmcId());
        cmPollTask.setCmcIndex(cmPollAttribute.getCmcIndex());
        cmPollTask.setCmIndex(cmPollAttribute.getStatusIndex());
        cmPollTask.setCmMac(cmPollAttribute.getStatusMacAddress());
        cmPollTask.setCmIp(cmPollAttribute.getStatusIpAddress());
        cmPollTask.setStatusValue(cmPollAttribute.getStatusValue());
        cmPollTask.setCmId(cmPollAttribute.getCmId());
        return cmPollTask;

    }

    /**
     * 获取bean及变量
     * 
     * @param ctx
     */
    private void getBeanAndValue(JobExecutionContext ctx) {
        BeanFactory beanFactory = (BeanFactory) ctx.getJobDetail().getJobDataMap().get("beanFactory");
        cmPollTaskBuildDao = (CmPollTaskBuildDao) beanFactory.getBean("cmPollTaskBuildDao");
        cmPollTaskBuildService = (CmPollTaskBuildService) beanFactory.getBean("cmPollTaskBuildService");
        entityTypeService = (EntityTypeService) beanFactory.getBean("entityTypeService");
        facadeFactory = (FacadeFactory) beanFactory.getBean("facadeFactory");
        entityService = (EntityService) beanFactory.getBean("entityService");
        cmPollConfigService = (CmPollConfigService) beanFactory.getBean("cmPollConfigService");
        cmPollFacade = (CmPollFacade) beanFactory.getBean("cmPollFacade");
    }

    /**
     * 设置采集时间
     */
    private void setCollectTime() {
        collectTime = CollectTimeUtil.getCollectTimeUtil("cmPoll").getCollectTime();
    }
}
