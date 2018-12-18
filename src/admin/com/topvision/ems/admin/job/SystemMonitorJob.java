/***********************************************************************
 * $Id: SystemMonitorJob.java,v1.0 2014-6-23 下午4:32:53 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.admin.job;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.util.Date;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

import com.sun.management.OperatingSystemMXBean;
import com.topvision.ems.admin.dao.SystemMonitorDao;
import com.topvision.ems.admin.domain.SystemMonitor;
import com.topvision.ems.admin.service.PassiveReportService;
import com.topvision.ems.admin.service.ProactiveReportService;
import com.topvision.framework.scheduler.AbstractJob;
import com.topvision.license.parser.LicenseIf;
import com.topvision.platform.SystemConstants;

/**
 * @author Victor
 * @created @2014-6-23-下午4:32:53
 *
 */
@PersistJobDataAfterExecution
public class SystemMonitorJob extends AbstractJob {
    @Override
    public void doJob(JobExecutionContext ctx) throws JobExecutionException {
        SystemMonitorDao systemMonitorDao = getService(SystemMonitorDao.class);
        SystemMonitor monitor = new SystemMonitor();
        MemoryMXBean memorymbean = ManagementFactory.getMemoryMXBean();
        OperatingSystemMXBean osmb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        File file = new File("conf/config.properties");
        file = file.getAbsoluteFile();
        while (file.getParentFile() != null) {
            file = file.getParentFile();
        }
        ThreadGroup systemThreadGroup = Thread.currentThread().getThreadGroup();
        while (systemThreadGroup.getParent() != null) {
            systemThreadGroup = systemThreadGroup.getParent();
        }
        monitor.setCollectTime(new Date());
        monitor.setCpu(osmb.getSystemCpuLoad());
        monitor.setDisk(file.getFreeSpace());
        monitor.setHeapMemory(memorymbean.getHeapMemoryUsage().getUsed());
        monitor.setNonHeapMemory(memorymbean.getNonHeapMemoryUsage().getUsed());
        monitor.setThreadCount(systemThreadGroup.activeCount());

        if (!SystemConstants.getInstance().getBooleanParam("DISK.FORBIDDEN", false)) {
            // TODO 读取磁盘性能
        }
        systemMonitorDao.insertEntity(monitor);
        // 汇报
        if (SystemConstants.getInstance().getBooleanParam("REPORT.FORBIDDEN", false)) {
            return;
        }
        try {
            LicenseIf licenseIf = getService(LicenseIf.class);
            String hid = licenseIf.getHid();
            PassiveReportService passiveReportService = getService(PassiveReportService.class);
            ProactiveReportService proactiveReportService = getFacade(ProactiveReportService.class);
            proactiveReportService.report(hid);
            proactiveReportService.reportCpuAndMemory(hid, monitor);
            proactiveReportService.reportThreadPoolMonitor(hid, passiveReportService.getThreadPoolMonitor());
        } catch (Exception e) {
            logger.warn("远程汇报出错:{}", e.getMessage());
            logger.debug("远程汇报出错", e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getFacade(Class<T> clazz) {
        StringBuilder serviceUrl = new StringBuilder("http://");
        serviceUrl.append(SystemConstants.getInstance().getStringParam("http.invoke.host", "ems.top-vision.cn"))
                .append(":").append(SystemConstants.getInstance().getIntParam("http.invoke.port", 8107));
        serviceUrl.append("/remoting/").append(clazz.getSimpleName());

        HttpInvokerProxyFactoryBean proxy = new HttpInvokerProxyFactoryBean();
        proxy.setServiceUrl(serviceUrl.toString());
        proxy.setServiceInterface(clazz);
        proxy.afterPropertiesSet();
        return (T) proxy.getObject();
    }
}
