package com.topvision.ems.performance.service.impl;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.sql.Timestamp;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.topvision.ems.billboard.domain.Notice;
import com.topvision.ems.billboard.service.BillBoardService;
import com.topvision.ems.performance.domain.ServerPerformanceInfo;
import com.topvision.ems.performance.job.ServerDiskJob;
import com.topvision.ems.performance.service.ServerPerformanceService;
import com.topvision.framework.common.DateUtils;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.service.SchedulerService;

/**
 * 服务器性能监控,主要是关注服务器(engine服务器、数据库服务器)的磁盘利用情况
 * 
 * @author xiaoyue
 * @created @2018年5月21日-下午4:57:28
 *
 */
@Service("serverPerformanceService")
public class ServerPerformanceServiceImpl extends BaseService implements ServerPerformanceService {

    @Autowired
    private BillBoardService billBoardService;
    @Autowired
    private SchedulerService schedulerService;
    @Resource(name = "facadeFactory")
    private FacadeFactory facadeFactory;

    @Value("${diskCheck.intervalTime:30}")
    private int intervalTime; // 循环周期，分钟

    @Value("${diskCheck.diskRatio:95}")
    private int diskRatio;
    @Value("${diskCheck.diskSize:20}")
    private int diskSize;

    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private BlockingQueue<Notice> noticeQueue = new ArrayBlockingQueue<>(10);

    @Override
    public void initialize() {
        super.initialize();

        try {
            JobDetail job = newJob(ServerDiskJob.class).withIdentity("CheckDisk", "ServerPerf").build();
            JobDataMap $jobDataMap = job.getJobDataMap();
            $jobDataMap.put("ServerPerformanceService", this);
            $jobDataMap.put("FacadeFactory", facadeFactory);

            SimpleTrigger trigger = newTrigger().withIdentity(job.getKey().getName(), job.getKey().getGroup())
                    .withSchedule(simpleSchedule().withIntervalInMinutes(intervalTime).repeatForever()).build();

            schedulerService.scheduleJob(job, trigger);

            executorService.execute(new Runnable() {

                @Override
                public void run() {

                    while (true) {
                        try {
                            Notice notice = noticeQueue.take();
                            billBoardService.createNotice(notice);
                        } catch (Exception e) {
                            logger.error("", e);
                        }

                    }
                }
            });

        } catch (SchedulerException e) {
            logger.error("Server Disk Job Error", e);
        }
    }

    public void addDiskInfo(ServerPerformanceInfo diskInfo) {
        if (validateDiskInfo(diskInfo)) {
            Notice notice = new Notice();
            // 直接使用admin
            notice.setUserId(2L);
            String content = ResourceManager.getResourceManager("com.topvision.platform.resources").getNotNullString(
                    "Billboard.diskRatio", diskInfo.getServerIp(), String.valueOf(diskInfo.getFreeDiskSize() / 1024),
                    String.valueOf(diskInfo.getDiskRatio()));
            notice.setContent(content);
            // 有效时间：30分钟
            long deadline = System.currentTimeMillis() + DateUtils.MINUTE_MILLS * intervalTime;
            notice.setDeadline(new Timestamp(deadline));
            notice.setType(Notice.SERVER_INFO);
            noticeQueue.add(notice);
        }
    }

    /**
     * 验证是否可以公告
     * 
     * @param diskInfo
     * @return
     */
    private boolean validateDiskInfo(ServerPerformanceInfo diskInfo) {
        if (diskInfo == null) {
            return false;
        }

        // 默认：利用率高于95%，或者让剩余可用小于20G，则产生通知
        if (diskInfo.getDiskRatio() >= diskRatio || diskInfo.getFreeDiskSize() < diskSize * 1024) {
            return true;
        }
        return false;

    }

}
