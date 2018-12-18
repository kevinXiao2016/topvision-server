/***********************************************************************
 * $Id: OltConfigInfoService.java,v1.0 2013-10-26 上午9:40:22 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.congfigbackup.service.impl;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.congfigbackup.dao.ConfigAndBackupDao;
import com.topvision.ems.congfigbackup.job.FileAutoSaveJob;
import com.topvision.ems.congfigbackup.service.ConfigBackupService;
import com.topvision.ems.congfigbackup.service.FileAutoSaveService;
import com.topvision.framework.common.FileUtils;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.SystemConstants;
import com.topvision.platform.service.SchedulerService;
import com.topvision.platform.service.SystemPreferencesService;

/**
 * @author flack
 * 
 * 设备配置文件每日自动保存, 保存时读取管理中的设备清单,对每一个设备依次下发保存配置命令，并让设备将文件上传到FTP服务器
 * 从FTP服务器中将配置文件按目录分类到META-INF/startconfig文件下 保存时对文件进行对比，如果当前文件与最近的一个文件相比时没有改变则不改变
 * 
 * @created @2013-10-26-上午9:40:22
 *
 */
@Service("fileAutoSaveService")
public class FileAutoSaveServiceImpl extends BaseService implements FileAutoSaveService {
    private static final Logger logger = LoggerFactory.getLogger(FileAutoSaveServiceImpl.class);
    @Autowired
    private SchedulerService schedulerService;
    @Autowired
    private SystemPreferencesService systemPreferencesService;
    @Autowired
    private ConfigBackupService configBackupService;
    @Autowired
    private ConfigAndBackupDao configAndBackupDao;
    //@FIXME 需要把这里改成注入 
    //@Value("${file.autoWriteTime}")
    private String autoWriteTime;

    /**
     * 初始化,启动时执行
     */
    @PostConstruct
    @Override
    public void initialize() {
        Properties properties = systemPreferencesService.getModulePreferences("platform");
        //格式为 00:00_1,2,3,4,5,6,7
        autoWriteTime = properties.getProperty("file.autoWriteTime");
        String cronExpression = bulidCronExpression(autoWriteTime);
        //run
        JobDetail job = newJob(FileAutoSaveJob.class).withIdentity("daily", "fileAutoSave").build();
        JobDataMap $jobDataMap = job.getJobDataMap();
        $jobDataMap.put("configBackupService", configBackupService);
        $jobDataMap.put("systemPreferencesService", systemPreferencesService);
        try {
            Trigger trg = newTrigger().withIdentity(job.getKey().getName(), job.getKey().getGroup())
                    .withSchedule(cronSchedule(cronExpression)).build();
            schedulerService.scheduleJob(job, trg);
        } catch (SchedulerException e) {
            logger.debug("", e);
        }
    }

    /*
     * (non-Javadoc)
     * @see com.topvision.ems.epon.batchConfig.FileAutoSave#reConfigureTrigger(java.lang.String)
     */
    @Override
    public void reConfigureTrigger(String _autoWriteTime) throws SchedulerException, ParseException {
        if (!autoWriteTime.equalsIgnoreCase(_autoWriteTime)) {
            TriggerKey triggerKey = new TriggerKey("daily", "fileAutoSave");
            CronTriggerImpl trigger = (CronTriggerImpl) schedulerService.getTrigger(triggerKey);
            String cronExpression = bulidCronExpression(_autoWriteTime);
            trigger.setCronExpression(cronExpression);
            schedulerService.modifyJobTrigger(trigger);
            //change autoWriteTime
            autoWriteTime = _autoWriteTime;
        }
    }

    /**
     * 构建任务执行的表达式
     * @param autoWriteTime
     * @return
     */
    public String bulidCronExpression(String autoWriteTime) {
        String[] split = autoWriteTime.split("_");
        String datetime = split[0];
        String weekdays = split[1];
        String[] time = datetime.split(":");
        String hour = time[0];
        String min = time[1];
        StringBuilder sb = new StringBuilder();
        sb.append("0 ").append(min).append(" ").append(hour).append(" ? * ").append(weekdays);
        return sb.toString();
    }

    /**
     * 维护设备的文件目录
     * 
     * @param entityId
     * @return
     */
    public static String getDirectory(long entityId) {
        // Calendar calendar = Calendar.getInstance();
        DateFormat directoryFormat = new SimpleDateFormat("yyyyMM");
        String directory = directoryFormat.format(new Date()).toString();
        // String directory = calendar.get(Calendar.YEAR) + "" +
        // (calendar.get(Calendar.MONTH)+1);
        File thisDirectory = new File(SystemConstants.ROOT_REAL_PATH + "META-INF" + File.separator + "startConfig"
                + File.separator + entityId + File.separator + directory);
        // 检查设备的目录存不存在
        File thisDevice = new File(SystemConstants.ROOT_REAL_PATH + "META-INF" + File.separator + "startConfig"
                + File.separator + entityId);
        if (!thisDevice.exists() || !thisDevice.isDirectory()) {
            thisDevice.mkdir();
        }
        // 如果当前目录存在并是一个目录,则直接往该目录内写文件，如果没有该目录，则新建一个目录，并写文件
        if (!thisDirectory.exists() || !thisDirectory.isDirectory()) {
            thisDirectory.mkdir();
        }
        // 计算该文件的文件名称
        DateFormat formate = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        String fileName = formate.format(new Date(System.currentTimeMillis()));
        return thisDirectory.getPath() + File.separator + fileName;
    }

    /**
     * 设置文件为最新的 首先需要清空最新文件的内容，再将当前内容写到最新文件夹中 最新文件 -- 一个文件名，放在entity目录下,名字都叫 startcon.cfg
     * 
     * @param file
     * @param entityId
     * @throws IOException 
     */
    public void setFileNewest(String file, long entityId) throws IOException {
        File newestFile = new File(SystemConstants.ROOT_REAL_PATH + "META-INF" + File.separator + "startConfig"
                + File.separator + entityId + File.separator + "startcon.cfg");
        FileUtils.copy(new File(file), newestFile);
    }

    /**
     * 对比当前文件是最新的
     * 
     * @param originFile
     * @param targetFile
     * @return
     * @throws IOException
     */
    public boolean checkFileIsDifferent(String target, long entityId) throws IOException {
        File startCfgFile = new File(SystemConstants.ROOT_REAL_PATH + "META-INF" + File.separator + "startConfig");
        // 如果 startCfg文件夹不存在或者不是一个一个夹，则创建这个文件夹
        if (!startCfgFile.exists() || !startCfgFile.isDirectory()) {
            startCfgFile.mkdir();
        }

        File file = new File(SystemConstants.ROOT_REAL_PATH + "META-INF" + File.separator + "startConfig"
                + File.separator + entityId + File.separator + "startcon.cfg");
        if (!file.exists()) {
            return true;
        }
        FileReader latestFile = new FileReader(SystemConstants.ROOT_REAL_PATH + "META-INF" + File.separator
                + "startConfig" + File.separator + entityId + File.separator + "startcon.cfg");
        FileReader targetFile = new FileReader(target);
        BufferedReader latestReader = new BufferedReader(latestFile);
        BufferedReader newestReader = new BufferedReader(targetFile);
        StringBuffer latestText = new StringBuffer();
        StringBuffer newestText = new StringBuffer();
        String cursor;
        try {
            while ((cursor = latestReader.readLine()) != null) {
                if (cursor.indexOf("! system datetime:") == -1) {
                    latestText.append(cursor + "\n");
                }
            }
            while ((cursor = newestReader.readLine()) != null) {
                if (cursor.indexOf("! system datetime:") == -1) {
                    newestText.append(cursor + "\n");
                }
            }
            String text1 = latestText.toString();
            String text2 = newestText.toString();
            if (text1.equals(text2)) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            return false;
        } finally {
            latestReader.close();
            newestReader.close();
        }
        // diff_match_patch diff = new diff_match_patch();
    }

    public String getAutoWriteTime() {
        return autoWriteTime;
    }

    public void setAutoWriteTime(String autoWriteTime) {
        this.autoWriteTime = autoWriteTime;
    }

}
