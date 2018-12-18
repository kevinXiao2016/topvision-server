/***********************************************************************
 * $Id: NbiConnectionService.java,v1.0 2016年3月17日 上午9:48:49 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.nbi.service.impl;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.concurrent.atomic.AtomicInteger;

import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.topvision.ems.nbi.domain.NbiBaseConfig;
import com.topvision.ems.nbi.job.NbiCheckJob;
import com.topvision.ems.nbi.rmi.NbiRmiService;
import com.topvision.ems.nbi.service.NbiConnectionService;
import com.topvision.ems.nbi.service.NbiService;
import com.topvision.framework.EnvironmentConstants;
import com.topvision.framework.service.BaseService;
import com.topvision.performance.nbi.api.NbiFtpConfig;
import com.topvision.performance.nbi.api.Server2NbiInvoke;
import com.topvision.platform.domain.EngineServer;
import com.topvision.platform.message.event.EngineServerEvent;
import com.topvision.platform.message.event.EngineServerListener;
import com.topvision.platform.message.service.MessageService;
import com.topvision.platform.service.EngineServerService;
import com.topvision.platform.service.SchedulerService;

/**
 * EngineServerListener必须等待NBI连接上后才能被注册到MessageSerivce中
 * 
 * @author Bravin
 * @created @2016年3月17日-上午9:48:49
 *
 */
@Service
public class NbiConnectionServiceImpl extends BaseService implements NbiConnectionService, EngineServerListener {
    @Autowired
    private SchedulerService schedulerService;
    @Value("${nbi.checkPerfNbiInterval:5}")
    private int checkNbiInterval;
    @Value("${server.rmi.ip:0.0.0.0}")
    private String serverIp;
    @Value("${rmi.port:3002}")
    private int serverPort;
    @Autowired
    private EngineServerService engineServerService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private NbiService nbiService;
    @Autowired
    private NbiRmiService nbiRmiService;
    private AtomicInteger notifyEngineCounter = new AtomicInteger();
    private String nbiIpAddress;
    private Integer nbiPort;
    private static final int MAX_ALLOW_NBI_DISCONNECTED_TIMES = 3;
    private static final int NBI_DISCONNECTED = -1;
    /** 每过3个周期就重复通知ENGINE一次,防止NBI中途断开后SERVER并没有感知到,致使ENGINE一直无法重复开启转发数据 */
    private static final int RE_NOTIFY_INTERVAL = 3;
    private int nbiDisconnectedTimes = 0;

    @Override
    public void start() {
        if ("0.0.0.0".equals(serverIp)) {
            /** 对NBI暴露本机的IP */
            serverIp = EnvironmentConstants.getHostAddress();
            logger.info("Server RMI address is {}:{}", serverIp, serverPort);
            retryCheckNbi();
        }
    }

    @Override
    public void retryCheckNbi() {
        NbiBaseConfig nbiBaseConfig = nbiService.getNbiBaseConfig();
        /**
         * 如果本次配置的NBI地址为空, 1.并且上个周期的地址也为空,则不处理, 2.如果上个周期不为空,则删除job 如果本次配置的NBI地址不为空，
         * 1.如果上个周期的地址也不为空,则不处理 2.如果上个周期的地址为空,则新建JOB
         */
        if (NbiBaseConfig.NBI_SWITCH_ON == nbiBaseConfig.getNbiSwitch()) {
            if (nbiIpAddress == null) {
                try {
                    JobDetail job = newJob(NbiCheckJob.class).withIdentity("check", "nbi.performance").build();
                    JobDataMap $jobDataMap = job.getJobDataMap();
                    $jobDataMap.put("nbiConnectionService", this);
                    $jobDataMap.put("nbiRmiService", nbiRmiService);
                    Trigger trg = newTrigger().withIdentity(job.getKey().getName(), job.getKey().getGroup())
                            .withSchedule(SimpleScheduleBuilder.repeatMinutelyForever(checkNbiInterval)).build();
                    schedulerService.scheduleJob(job, trg);
                } catch (SchedulerException e) {
                    logger.debug("", e);
                }
            }
            nbiIpAddress = nbiBaseConfig.getNbiAddr();
        } else {
            if (nbiIpAddress != null) {
                try {
                    stopEngineRedirect();
                    schedulerService.deleteJob(new JobKey("check", "nbi.performance"));
                } catch (SchedulerException e) {
                    logger.error("", e);
                }
            }
            nbiIpAddress = null;
        }
        nbiPort = nbiBaseConfig.getNbiPort();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.nbi.service.NbiConnectionService#notifyEngineConnectNbi()
     */
    @Override
    public void notifyEngineConnectNbi() {
        int count = notifyEngineCounter.getAndIncrement();
        if (count == NBI_DISCONNECTED || count % RE_NOTIFY_INTERVAL == 0) {
            nbiDisconnectedTimes = 0;
            messageService.removeListener(EngineServerListener.class, this);
            messageService.addListener(EngineServerListener.class, this);
            engineServerService.notifyEngineConnectNbi(nbiIpAddress, nbiPort, true);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.message.event.EngineServerListener#statusChanged(com.topvision.
     * platform .message.event.EngineServerEvent)
     */
    @Override
    public void statusChanged(EngineServerEvent event) {
        byte status = event.getStatus();
        EngineServer engineServer = event.getEngineServer();
        if (EngineServerEvent.STATUS_CONNECTED == status) {
            engineServerService.notifyEngineConnectNbi(engineServer, nbiIpAddress, nbiPort, true);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.nbi.service.NbiConnectionService#incrementAndHandleDIsconnected()
     */
    @Override
    public void incrementAndHandleDisconnected() {
        nbiDisconnectedTimes++;
        /* 此处不能是 > ,否则每次超时后都需要执行下面的操作* */
        if (nbiDisconnectedTimes == MAX_ALLOW_NBI_DISCONNECTED_TIMES) {
            stopEngineRedirect();
        }
    }

    /**
     * 通知Engine停止转发数据
     */
    private void stopEngineRedirect() {
        messageService.removeListener(EngineServerListener.class, this);
        engineServerService.notifyEngineConnectNbi(nbiIpAddress, nbiPort, false);
        notifyEngineCounter.set(NBI_DISCONNECTED);
    }

    @Override
    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    @Override
    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    @Override
    public String getNbiIpAddress() {
        return nbiIpAddress;
    }

    public void setNbiIpAddress(String nbiIpAddress) {
        this.nbiIpAddress = nbiIpAddress;
    }

    @Override
    public Integer getNbiPort() {
        return nbiPort;
    }

    public void setNbiPort(Integer nbiPort) {
        this.nbiPort = nbiPort;
    }

    @Override
    public int getCheckNbiInterval() {
        return checkNbiInterval;
    }

    public void setCheckNbiInterval(int checkNbiInterval) {
        this.checkNbiInterval = checkNbiInterval;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.nbi.service.NbiConnectionService#dynamicUpdateGroupIndexs()
     */
    @Override
    public void dynamicUpdateGroupIndexs() {
        if (isNbiConnected()) {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    Server2NbiInvoke server2NbiInvoke = nbiRmiService.getNbiService(Server2NbiInvoke.class,
                            nbiIpAddress, nbiPort);
                    server2NbiInvoke.dynamicUpdateGroupIndexs();
                }
            }).start();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.nbi.service.NbiConnectionService#dynamicUpdateGroupPeroid()
     */
    @Override
    public void dynamicUpdateGroupPeroid() {
        if (isNbiConnected()) {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    Server2NbiInvoke server2NbiInvoke = nbiRmiService.getNbiService(Server2NbiInvoke.class,
                            nbiIpAddress, nbiPort);
                    server2NbiInvoke.dynamicUpdateGroupPeroid();
                }
            }).start();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.nbi.service.NbiConnectionService#dynamicUpdateFtpConfig(com.topvision.
     * performance.nbi.api.NbiFtpConfig)
     */
    @Override
    public void dynamicUpdateFtpConfig(final NbiFtpConfig config) {
        if (isNbiConnected()) {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    Server2NbiInvoke server2NbiInvoke = nbiRmiService.getNbiService(Server2NbiInvoke.class,
                            nbiIpAddress, nbiPort);
                    server2NbiInvoke.dynamicUpdateFtpConfig(config);
                }
            }).start();
        }
    }

    /**
     * 判断NBI是否已连接上
     * 
     * @return
     */
    @Override
    public boolean isNbiConnected() {
        return notifyEngineCounter.get() > 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.nbi.service.NbiConnectionService#dynamicRemoveEntity(long)
     */
    @Override
    public void dynamicRemoveEntity(long entityId) {
        Server2NbiInvoke server2NbiInvoke = nbiRmiService.getNbiService(Server2NbiInvoke.class, nbiIpAddress, nbiPort);
        server2NbiInvoke.dynamicRemoveEntity(entityId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.nbi.service.NbiConnectionService#dynamicRemoveEntity(java.lang.String)
     */
    @Override
    public void dynamicRemoveEntity(String ip) {
        Server2NbiInvoke server2NbiInvoke = nbiRmiService.getNbiService(Server2NbiInvoke.class, nbiIpAddress, nbiPort);
        server2NbiInvoke.dynamicRemoveEntity(ip);
    }

}
