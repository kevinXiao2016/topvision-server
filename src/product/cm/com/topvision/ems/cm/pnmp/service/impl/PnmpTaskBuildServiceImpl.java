/***********************************************************************
 * PnmpTaskBuildServiceImpl.java,v1.0 17-8-8 上午8:40 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.pnmp.service.impl;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.repeatSecondlyForever;
import static org.quartz.TriggerBuilder.newTrigger;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

import com.topvision.platform.domain.SystemPreferences;
import com.topvision.platform.service.SystemPreferencesService;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.topvision.ems.cm.pnmp.facade.PnmpPollFacade;
import com.topvision.ems.cm.pnmp.facade.domain.CmtsCm;
import com.topvision.ems.cm.pnmp.facade.domain.PnmpCmData;
import com.topvision.ems.cm.pnmp.facade.domain.PnmpPollHighSpeedEndTask;
import com.topvision.ems.cm.pnmp.facade.domain.PnmpPollHighSpeedTask;
import com.topvision.ems.cm.pnmp.facade.domain.PnmpPollLowSpeedEndTask;
import com.topvision.ems.cm.pnmp.facade.domain.PnmpPollLowSpeedTask;
import com.topvision.ems.cm.pnmp.facade.domain.PnmpPollMiddleSpeedEndTask;
import com.topvision.ems.cm.pnmp.facade.domain.PnmpPollMiddleSpeedTask;
import com.topvision.ems.cm.pnmp.facade.domain.PnmpPollTask;
import com.topvision.ems.cm.pnmp.job.HighSpeedTaskBuildJob;
import com.topvision.ems.cm.pnmp.job.LowSpeedTaskBuildJob;
import com.topvision.ems.cm.pnmp.job.MiddleSpeedTaskBuildJob;
import com.topvision.ems.cm.pnmp.job.TaskBuildJob;
import com.topvision.ems.cm.pnmp.service.PnmpMonitorCmService;
import com.topvision.ems.cm.pnmp.service.PnmpSchedulerService;
import com.topvision.ems.cm.pnmp.service.PnmpTaskBuildService;
import com.topvision.ems.cmc.service.impl.CmcBaseCommonService;
import com.topvision.ems.network.dao.EntityDao;
import com.topvision.framework.common.CollectTimeUtil;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.service.SchedulerService;

/**
 * @author jay
 * @created 17-8-8 上午8:40
 */
@Service("pnmpTaskBuildService")
public class PnmpTaskBuildServiceImpl extends CmcBaseCommonService implements PnmpTaskBuildService {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private BeanFactory beanFactory;
    @Autowired
    private SchedulerService schedulerService;
    @Autowired
    private PnmpSchedulerService pnmpSchedulerService;
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private PnmpMonitorCmService pnmpMonitorCmService;
    @Autowired
    private SystemPreferencesService systemPreferencesService;
    // 默认低频率采集周期32小时一次
    private Long intervalLow;
    // 默认普通频率采集周期4小时一次
    private Long intervalMiddle;
    // 默认高频率采集周期10分钟一次
    private Long intervalHigh;
    private String lowSpeedStartTime;
    private String middleSpeedStartTime;
    private JobDetail jobLow;
    private JobDetail jobMiddle;
    private JobDetail jobHigh;
    private PnmpTaskAppendThread lowSpeedThread;
    private PnmpTaskAppendThread middleSpeedThread;
    private PnmpTaskAppendThread highSpeedThread;

    @Autowired
    private EntityDao entityDao;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private DecimalFormat df = new DecimalFormat("00");

    @Override
    public void initialize() {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void start() {
        logger.debug("PnmpTaskBuildServiceImpl.start");
        startPnmp();
        lowSpeedThread = new PnmpTaskAppendThread("LOW");
        lowSpeedThread.start();
        middleSpeedThread = new PnmpTaskAppendThread("MIDDLE");
        middleSpeedThread.start();
        highSpeedThread = new PnmpTaskAppendThread("HIGH");
        highSpeedThread.start();
    }

    @Override
    public void startPnmp() {
        logger.debug("PnmpTaskBuildServiceImpl.startPnmp");
        boolean debug = getValueByName("PNMP.isDebug",true);
        intervalLow = getValueByName("PNMP.intervalLow",115200L);
        intervalMiddle = getValueByName("PNMP.intervalMiddle",14400L);
        intervalHigh = getValueByName("PNMP.intervalHigh",600L);
        lowSpeedStartTime = getValueByName("PNMP.lowSpeedStartTime","8,16,24");
        middleSpeedStartTime = getValueByName("PNMP.middleSpeedStartTime","2,6,10,14,18,22");
        if (debug) {
            //debug版本立即启动
            startTaskBuildJob(jobLow, intervalLow, new LowSpeedTaskBuildJob(), null);
            startTaskBuildJob(jobMiddle, intervalMiddle, new MiddleSpeedTaskBuildJob(), null);
        } else {
            //低频率采集只能在整点8 16 24点启动
            // 低频率采集只能在整点8 16 24点启动
            Date startLowSpeedTaskBuildAt = getLowSpeedTaskBuildStartAt();
            startTaskBuildJob(jobLow, intervalLow, new LowSpeedTaskBuildJob(), startLowSpeedTaskBuildAt);
            // 中等频率采集只能在整点2 6 10 14 18 22点启动
            Date startMiddleSpeedTaskBuildAt = getMiddleSpeedTaskBuildStartAt();
            startTaskBuildJob(jobMiddle, intervalMiddle, new MiddleSpeedTaskBuildJob(), startMiddleSpeedTaskBuildAt);
        }
        CollectTimeUtil.createCollectTimeUtil("PnmpLow", System.currentTimeMillis(), intervalLow);
        CollectTimeUtil.createCollectTimeUtil("PnmpMiddle", System.currentTimeMillis(), intervalMiddle);
        // 高频率采集立即启动
        startTaskBuildJob(jobHigh, intervalHigh, new HighSpeedTaskBuildJob(), null);
        CollectTimeUtil.createCollectTimeUtil("PnmpHigh", System.currentTimeMillis(), intervalHigh);
    }

    @Override
    public void stopPnmp() {
        logger.debug("PnmpTaskBuildServiceImpl.stopPnmp");
        stopTaskBuildJob(jobLow);
        stopTaskBuildJob(jobMiddle);
        stopTaskBuildJob(jobHigh);
    }

    /**
     * 任务分配线程类
     *
     * @author loyal
     * @created @2015年3月11日-下午2:38:54
     *
     */
    class PnmpTaskAppendThread extends Thread {
        private Integer status = 0;// 0:上轮轮询已结束，1：上轮轮询未结束
        private Long cmOnLine = 0L; // 在线CM总数，提供计算预计完成时间使用
        private Long collectTime;

        public PnmpTaskAppendThread(String name) {
            this.setName("PnmpTaskAppendThread_" + name);
        }

        private ArrayBlockingQueue<PnmpPollTask> pnmpPollTaskList = new ArrayBlockingQueue<PnmpPollTask>(100);

        public ArrayBlockingQueue<PnmpPollTask> getPnmpPollTaskList() {
            return pnmpPollTaskList;
        }

        public void setPnmpPollTaskList(ArrayBlockingQueue<PnmpPollTask> pnmpPollTaskList) {
            this.pnmpPollTaskList = pnmpPollTaskList;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public Long getCmOnLine() {
            return cmOnLine;
        }

        public void setCmOnLine(Long cmOnLine) {
            this.cmOnLine = cmOnLine;
        }

        public Long getCollectTime() {
            return collectTime;
        }

        public void setCollectTime(Long collectTime) {
            this.collectTime = collectTime;
        }

        @Override
        public void run() {
            logger.debug("PnmpTaskAppendThread.run start");
            while (true) {
                try {
                    Integer engineId = pnmpSchedulerService.isAnyIdle();
                    List<PnmpPollTask> appendPnmpPollTaskList = new ArrayList<PnmpPollTask>();
                    Integer taskCount = pnmpPollTaskList.size();
                    if (logger.isDebugEnabled()) {
                        logger.debug("TaskAppendThread.run taskCount:" + taskCount);
                    }

                    if (engineId < 0 || pnmpPollTaskList.isEmpty()) {
                        // 如果没有空闲采集器，等待五秒
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                        }
                        continue;
                    }
                    Integer appendCmCount = pnmpSchedulerService.idleTaskCount(engineId);
                    int count = 0;
                    if (appendCmCount >= taskCount) {
                        count = taskCount;
                    } else {
                        count = appendCmCount;
                    }
                    for (int i = 0; i < count; i++) {
                        try {
                            appendPnmpPollTaskList.add(pnmpPollTaskList.take());
                        } catch (InterruptedException e) {
                            if (logger.isDebugEnabled()) {
                                logger.debug("", e);
                            }
                        }
                    }
                    if (appendPnmpPollTaskList.size() > 0) {
                        pnmpSchedulerService.appendTask(engineId, collectTime, appendPnmpPollTaskList);
                    }
                } catch (Exception e) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("TaskAppendThread while", e);
                    }
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                }
            }
        }
    }

    @Override
    public void putTaskToQueue(PnmpPollTask pnmpPollTask) {
        PnmpTaskAppendThread pnmpTaskAppendThread = switchThread(pnmpPollTask);
        if (pnmpTaskAppendThread != null) {
            try {
                pnmpTaskAppendThread.getPnmpPollTaskList().put(pnmpPollTask);
            } catch (InterruptedException e) {
                if (logger.isErrorEnabled()) {
                    logger.error("", e);
                }
            }
        }
    }

    @Override
    public Map<String, Long> getCmOnLine() {
        Map<String, Long> map = new HashMap<>();
        map.put("Low", lowSpeedThread.getCmOnLine());
        map.put("Middle", middleSpeedThread.getCmOnLine());
        map.put("High", highSpeedThread.getCmOnLine());
        return map;
    }

    @Override
    public void fireRoundStartMessage(PnmpPollTask pnmpPollTask, Long collectTime, Long cmOnLine) {
        PnmpTaskAppendThread pnmpTaskAppendThread = switchThread(pnmpPollTask);
        if (pnmpTaskAppendThread != null) {
            pnmpTaskAppendThread.setCollectTime(collectTime);
            pnmpTaskAppendThread.setCmOnLine(cmOnLine);
            pnmpSchedulerService.roundStart(collectTime, pnmpPollTask);
            pnmpTaskAppendThread.setStatus(1);
        }
    }

    @Override
    public void fireRoundEndMessage(PnmpPollTask pnmpPollTask, Long collectTime) {
        PnmpTaskAppendThread pnmpTaskAppendThread = switchThread(pnmpPollTask);
        if (pnmpTaskAppendThread != null) {
            pnmpSchedulerService.roundFinished(collectTime, pnmpPollTask);
            pnmpTaskAppendThread.setStatus(0);
        }
    }

    @Override
    public Boolean isLastPollEnd(PnmpPollTask pnmpPollTask) {
        PnmpTaskAppendThread pnmpTaskAppendThread = switchThread(pnmpPollTask);
        return pnmpTaskAppendThread != null && pnmpTaskAppendThread.getStatus() == 0;
    }

    @Override
    public List<CmtsCm> walkCmtsCm(Long entityId) {
        try {
            SnmpParam snmpParam = getSnmpParamByEntityId(entityId);
            PnmpPollFacade pnmpPollFacade = getPnmpPollFacade(snmpParam.getIpAddress());
            return pnmpPollFacade.getCmtsCmList(snmpParam);
        } catch (Exception e) {
            logger.debug("", e);
            return new ArrayList<CmtsCm>();
        }
    }

    @Override
    public Long getCmOnLineNum(List<Long> entityIdList) {
        Long cmNum = 0L;
        for (Long entityId : entityIdList) {
            cmNum = cmNum + entityDao.getEntityRelaOnlineCm(entityId);
        }
        return cmNum;
    }

    @Override
    public Long getMiddleCmNum() {
        return Long.valueOf(pnmpMonitorCmService.getAllMiddleMonitorCmListNum());
    }

    @Override
    public List<CmtsCm> getMiddleCmList() {
        List<PnmpCmData> cmList = pnmpMonitorCmService.getAllMiddleMonitorCmList();

        List<CmtsCm> cms = new ArrayList<CmtsCm>();
        if (cmList == null) {
            return cms;
        }

        for (PnmpCmData cm : cmList) {
            if (cm.getCmIp() != null) {
                CmtsCm cmtsCm = new CmtsCm();
                cmtsCm.setEntityId(cm.getEntityId());
                cmtsCm.setCmcId(cm.getCmcId());
                cmtsCm.setStatusMacAddress(cm.getCmMac());
                cmtsCm.setStatusIpAddress(cm.getCmIp());
                SnmpParam snmpParam = entityDao.getSnmpParamByEntityId(cm.getEntityId());
                cmtsCm.setSnmpParam(snmpParam);
                cms.add(cmtsCm);
            }
        }
        return cms;
    }

    @Override
    public Long getHighCmNum() {
        return Long.valueOf(pnmpMonitorCmService.getAllHighMonitorCmListNum());
    }

    @Override
    public List<CmtsCm> getHighCmList() {
        List<PnmpCmData> cmList = pnmpMonitorCmService.getAllHighMonitorCmList();

        List<CmtsCm> cms = new ArrayList<CmtsCm>();
        if (cmList == null) {
            return cms;
        }

        for (PnmpCmData cm : cmList) {
            if (cm.getCmIp() != null) {
                CmtsCm cmtsCm = new CmtsCm();
                cmtsCm.setEntityId(cm.getEntityId());
                cmtsCm.setCmcId(cm.getCmcId());
                cmtsCm.setStatusMacAddress(cm.getCmMac());
                cmtsCm.setStatusIpAddress(cm.getCmIp());
                SnmpParam snmpParam = entityDao.getSnmpParamByEntityId(cm.getEntityId());
                cmtsCm.setSnmpParam(snmpParam);
                cms.add(cmtsCm);
            }
        }
        return cms;
    }

    private void startTaskBuildJob(JobDetail job, Long interval, TaskBuildJob taskBuildJob, Date startAt) {
        TriggerBuilder<Trigger> builder;
        if (job != null) {
            stopPnmp();
        }
        try {
            logger.info("startTaskBuildJob class[" + taskBuildJob.getClass().getName() + "] interval[" + interval + "]"
                    + " startAt[" + (startAt == null ? "now" : sdf.format(startAt)) + "]");
            job = newJob(taskBuildJob.getClass()).withIdentity(taskBuildJob.getClass().getName()).build();
            job.getJobDataMap().put("beanFactory", beanFactory);
            job.getJobDataMap().put("interval", interval);
            builder = newTrigger().withIdentity(job.getKey().getName(), job.getKey().getGroup());
            if (startAt == null) {
                builder.startNow();
            } else {
                builder.startAt(startAt);
            }
            builder.withSchedule(repeatSecondlyForever(interval.intValue()));
            schedulerService.scheduleJob(job, builder.build());
        } catch (SchedulerException e) {
            if (logger.isErrorEnabled()) {
                logger.error("PnmpTaskBuildServiceImpl----SchedulerException");
            }
        }
    }

    private void stopTaskBuildJob(JobDetail job) {
        try {
            if (job != null) {
                schedulerService.deleteJob(job.getKey());
            }
            job = null;
        } catch (SchedulerException e) {
            if (logger.isErrorEnabled()) {
                logger.error("PnmpTaskBuildServiceImpl.stopPnmpFail job:" + job.getDescription(), e);
            }
        }
    }

    private Date getMiddleSpeedTaskBuildStartAt() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        String[] middleSpeedStartTimes = middleSpeedStartTime.split(",");
        for (String startTime : middleSpeedStartTimes) {
            try {
                int time = Integer.parseInt(startTime);
                if (time > hour) {
                    return makeStartTime(year, month, day, time);
                }
            } catch (NumberFormatException e) {
                logger.debug("", e);
            } catch (ParseException e) {
                logger.debug("", e);
            }
        }
        try {
            int time = Integer.parseInt(middleSpeedStartTimes[0]);
            return makeStartTime(year, month, day, time);
        } catch (NumberFormatException e) {
            logger.debug("", e);
        } catch (ParseException e) {
            logger.debug("", e);
        }
        return new Date();
    }

    private Date getLowSpeedTaskBuildStartAt() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        String[] lowSpeedStartTimes = lowSpeedStartTime.split(",");
        for (String startTime : lowSpeedStartTimes) {
            try {
                int time = Integer.parseInt(startTime);
                if (time > hour) {
                    return makeStartTime(year, month, day, time);
                }
            } catch (NumberFormatException e) {
                logger.debug("", e);
            } catch (ParseException e) {
                logger.debug("", e);
            }
        }
        try {
            int time = Integer.parseInt(lowSpeedStartTimes[0]);
            return makeStartTime(year, month, day, time);
        } catch (NumberFormatException e) {
            logger.debug("", e);
        } catch (ParseException e) {
            logger.debug("", e);
        }
        return new Date();
    }

    private Date makeStartTime(int year, int month, int day, int hour) throws ParseException {
        String tString = "" + year + "-" + df.format(month) + "-" + df.format(day) + " " + df.format(hour) + ":00:00";
        logger.info("getMiddleSpeedTaskBuildStartAt start at:" + tString);
        return sdf.parse(tString);
    }

    private PnmpTaskAppendThread switchThread(PnmpPollTask pnmpPollTask) {
        if (pnmpPollTask instanceof PnmpPollLowSpeedTask || pnmpPollTask instanceof PnmpPollLowSpeedEndTask) {
            return lowSpeedThread;
        }
        if (pnmpPollTask instanceof PnmpPollMiddleSpeedTask || pnmpPollTask instanceof PnmpPollMiddleSpeedEndTask) {
            return middleSpeedThread;
        }
        if (pnmpPollTask instanceof PnmpPollHighSpeedTask || pnmpPollTask instanceof PnmpPollHighSpeedEndTask) {
            return highSpeedThread;
        }
        return null;
    }

    private PnmpPollFacade getPnmpPollFacade(String ip) {
        return facadeFactory.getFacade(ip, PnmpPollFacade.class);
    }

    private String getValueByName(String name, String defalut) {
        SystemPreferences systemPreferences = systemPreferencesService.getSystemPreference(name);
        if (systemPreferences != null) {
            return systemPreferences.getValue();
        } else {
            return defalut;
        }
    }

    private Boolean getValueByName(String name, boolean defalut) {
        SystemPreferences systemPreferences = systemPreferencesService.getSystemPreference(name);
        if (systemPreferences != null) {
            return "true".equalsIgnoreCase(systemPreferences.getValue());
        } else {
            return defalut;
        }
    }

    private Long getValueByName(String name, Long defalut) {
        SystemPreferences systemPreferences = systemPreferencesService.getSystemPreference(name);
        try {
            if (systemPreferences != null) {
                return Long.parseLong(systemPreferences.getValue());
            } else {
                return defalut;
            }
        } catch (NumberFormatException e) {
            return defalut;
        }
    }
}
