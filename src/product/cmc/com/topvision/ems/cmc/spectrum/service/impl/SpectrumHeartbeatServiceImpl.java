/***********************************************************************
 * $ SpectrumHeartbeatServiceImpl.java,v1.0 2014-1-4 17:06:52 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.spectrum.service.impl;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.repeatSecondlyForever;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import com.topvision.ems.cmc.spectrum.domain.SpectrumVideo;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.ems.cmc.service.impl.CmcBaseCommonService;
import com.topvision.ems.cmc.spectrum.domain.Heartbeat;
import com.topvision.ems.cmc.spectrum.exception.CmtsSwitchOffException;
import com.topvision.ems.cmc.spectrum.exception.OltSwitchOffException;
import com.topvision.ems.cmc.spectrum.facade.SpectrumFacade;
import com.topvision.ems.cmc.spectrum.job.HeartBeatJob;
import com.topvision.ems.cmc.spectrum.service.SpectrumCallback;
import com.topvision.ems.cmc.spectrum.service.SpectrumCallbackService1S;
import com.topvision.ems.cmc.spectrum.service.SpectrumConfigService;
import com.topvision.ems.cmc.spectrum.service.SpectrumHeartbeatService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.service.SchedulerService;

/**
 * @author jay
 * @created @2014-1-4-17:06:52
 */
@Service("spectrumHeartbeatService")
public class SpectrumHeartbeatServiceImpl extends CmcBaseCommonService implements SpectrumHeartbeatService,BeanFactoryAware {
    @Value("${Spectrum.heartbeatTimeout}")
    private Integer heartbeatTimeout;
    @Resource(name = "spectrumCallbackService1S")
    private SpectrumCallbackService1S spectrumCallbackService1S;
    @Resource(name = "spectrumConfigService")
    private SpectrumConfigService spectrumConfigService;
    @Resource(name = "facadeFactory")
    private FacadeFactory facadeFactory;
    @Resource(name = "cmcService")
    private CmcService cmcService;
    @Autowired
    private SchedulerService schedulerService;
    @Autowired
    private EntityTypeService entityTypeService;
    JobDetail job;
    private List<Heartbeat> heartbeatList = new ArrayList<Heartbeat>();
    private BeanFactory beanFactory;

    @Override
    @PostConstruct
    public void initialize() {
    }

    @Override
    public void start() {
        job = newJob(HeartBeatJob.class).withIdentity("HeartBeatJob", "Default").build();
        job.getJobDataMap().put("heartbeatTimeout", heartbeatTimeout);// Integer 超时30秒无心跳
        job.getJobDataMap().put("spectrumCallbackService1S", spectrumCallbackService1S);
        job.getJobDataMap().put("spectrumHeartbeatService", this);
        job.getJobDataMap().put("spectrumConfigService", spectrumConfigService);
        TriggerBuilder<SimpleTrigger> builder = newTrigger().withIdentity(job.getKey().getName(),
                job.getKey().getGroup()).withSchedule(repeatSecondlyForever(10));
        try {
            schedulerService.scheduleJob(job, builder.build());
        } catch (SchedulerException e) {
            logger.error("", e);
        }
    }

    @Override
    public Long addHeartbeat(Long cmcId, SpectrumCallback callback) throws OltSwitchOffException,
            CmtsSwitchOffException {
        Boolean switchStatus = getSwitchStatus(cmcId);
        Long callbackId = null;
        if (switchStatus) {
            if (spectrumCallbackService1S.isSameCallbackExist(callback)) {
                logger.debug("Spectrum addCallback, callback exist, addCallback failed. cmcId = " + callback.getCmcId()
                        + " && callback type = " + callback.getClass());
            } else if (spectrumCallbackService1S.isOnlyHisVideoCallbackExist(cmcId)) {// 只有一个CALLBACK工作并且为历史录像
                Long hisCallbackId = takeHisCallbackId(cmcId);
                delHeartbeat(hisCallbackId);
                Long cId = addHeartbeat(cmcId, callback);
                if (cId != -1) {
                    HistoryCallback newHistoryCallback = (HistoryCallback) beanFactory.getBean("historyCallback");
                    newHistoryCallback.setCmcId(callback.getCmcId());
                    addHeartbeat(cmcId,newHistoryCallback);
                }
                return cId;
            }
            callbackId = spectrumCallbackService1S.addCallback(cmcId, callback);
            // 维护心跳队列
            if (callbackId != -1) {
                Long now = System.currentTimeMillis();
                Heartbeat heartbeat = new Heartbeat();
                heartbeat.setCallbackId(callbackId);
                heartbeat.setSessionId(null);
                heartbeat.setCmcId(cmcId);
                heartbeat.setHeartbeatTime(now);
                heartbeat.setCreateTime(now);
                if (callback instanceof WebCallback) {
                    heartbeat.setType(Heartbeat.WEB);
                } else if (callback instanceof RealtimeCallback) {
                    heartbeat.setType(Heartbeat.REALTIME);
                } else if (callback instanceof HistoryCallback) {
                    heartbeat.setType(Heartbeat.HISTORY);
                } else if (callback instanceof MobileCallback) {
                    heartbeat.setType(Heartbeat.MOBILE);
                }
                heartbeatList.add(heartbeat);
                logger.debug("Spectrum addServiceHeartbeat, cmcId = " + cmcId + " && callback class = "
                        + callback.getClass().getSimpleName() + " && callbackId = " + callbackId);
            } else {
                callbackId = -1L;
                logger.error("Spectrum addHeartbeat failed, callbackId = -1, cmcId = " + cmcId
                        + " && callback class = " + callback.getClass().getSimpleName());
            }
        } else {
            callbackId = -1L;
            logger.error("Spectrum addServiceHeartbeat failed, switchStatus is false, cmcId = " + cmcId
                    + " && callback class = " + callback.getClass().getSimpleName());
        }
        if (callback != null) {
            callback.setCallbackId(callbackId);
        }
        return callbackId;
    }

    @Override
    public SpectrumVideo delHeartbeat(Long callbackId) {
        SpectrumVideo spectrumVideo;
        for (Iterator<Heartbeat> iterator = heartbeatList.iterator(); iterator.hasNext(); ) {
            Heartbeat heartbeat = iterator.next();
            if (heartbeat.getCallbackId().equals(callbackId)) {
                try {
                    logger.debug("Spectrum delHeartbeat, callbackId = " + callbackId);
                    spectrumVideo = spectrumCallbackService1S.delCallback(heartbeat.getCallbackId(), heartbeat.getCmcId());
                    iterator.remove();
                    return spectrumVideo;
                } catch (Exception e) {
                    logger.debug("",e);
                }
            }
        }
        logger.error("Spectrum delHeartbeat error, heartbeat not found, callbackId = " + callbackId);
        return null;
    }

    @Override
    public Boolean heartbeat(Long callbackId) {
        Heartbeat heartbeat = null;
        for (Heartbeat aHeartbeatList : heartbeatList) {
            if (aHeartbeatList.getCallbackId().equals(callbackId)) {
                heartbeat = aHeartbeatList;
            }
        }
        if (heartbeat != null) {
            heartbeat.setHeartbeatTime(System.currentTimeMillis());
            return true;
        } else {
            // 如果超时会找不到heartbeat，抛出异常
            logger.error("Spectrum heartbeat error, heartbeat not found, callbackId = " + callbackId);
            return false;
        }
    }

    public SpectrumFacade getSpectrumFacade(String ip) {
        return facadeFactory.getFacade(ip, SpectrumFacade.class);
    }

    private Boolean getSwitchStatus(Long cmcId) throws OltSwitchOffException, CmtsSwitchOffException {
        CmcAttribute cmc = cmcService.getCmcAttributeByCmcId(cmcId);
        if (entityTypeService.isCcmtsWithoutAgent(cmc.getCmcDeviceStyle())) {
            if (!spectrumConfigService.getOltSwitchStatus(cmcId)) {
                throw new OltSwitchOffException();
            }
        }
        if (!spectrumConfigService.getCmcSwitchStatus(cmcId)) {
            // throw new CmtsSwitchOffException();
            List<Long> cmcIds = new ArrayList<Long>();
            cmcIds.add(cmcId);
            spectrumConfigService.startSpectrumSwitchCmts(cmcIds);
        }
        return true;
    }

    @SuppressWarnings("unused")
    private Boolean getDeviceSwitchStatus(Long cmcId) {
        CmcAttribute cmc = cmcService.getCmcAttributeByCmcId(cmcId);
        snmpParam = getSnmpParamByCmcId(cmcId);
        SpectrumFacade cmcSpectrumFacade = getSpectrumFacade(snmpParam.getIpAddress());
        if (entityTypeService.isCcmtsWithoutAgent(cmc.getCmcDeviceStyle())) {
            try {
                // 从olt实时获取开关状态
                boolean oltSwitch = cmcSpectrumFacade.getOltSwitchStatus(snmpParam);
                if (!oltSwitch) {
                    return false;
                }
            } catch (Exception e) {
                logger.error("", e);
            }
        }
        try {
            // 从网管侧获取CC开关状态
            // return true;
            return cmcSpectrumFacade.getCmcSwitchStatus(snmpParam, cmc.getTopCcmtsSysMacAddr());
        } catch (Exception e) {
            logger.error("", e);
        }
        return false;
    }

    @Override
    public List<Heartbeat> getHeartbeatList() {
        return heartbeatList;
    }

    @Override
    public Long takeHisCallbackId(Long cmcId) {
        return spectrumCallbackService1S.takeHisCallbackId(cmcId);
    }

    @Override
    public Long takeRealtimeCallbackIdByWebCallbackId(Long callbackId) {
        Heartbeat heartbeat = null;
        for (Heartbeat aHeartbeatList : heartbeatList) {
            if (aHeartbeatList.getCallbackId().equals(callbackId)) {
                heartbeat = aHeartbeatList;
            }
        }
        if (heartbeat != null) {
            return spectrumCallbackService1S.takeRealtimeCallbackIdByWebCallbackId(callbackId,heartbeat.getCmcId());
        } else {
            return null;
        }
    }

    @Override
    public void delWebHeartBeat(Long callbackId) {
        //停止实时查看相关联的实时录像
        Long realtimeCallbackId = takeRealtimeCallbackIdByWebCallbackId(callbackId);
        if (realtimeCallbackId != null) {
            delHeartbeat(realtimeCallbackId);
        }
        //停止
        delHeartbeat(callbackId);
    }

    public void setHeartbeatList(List<Heartbeat> heartbeatList) {
        this.heartbeatList = heartbeatList;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
