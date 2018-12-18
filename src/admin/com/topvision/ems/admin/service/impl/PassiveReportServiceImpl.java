/***********************************************************************
 * $Id: PassiveReportServiceImpl.java,v1.0 Sep 20, 2016 10:01:05 AM $
 * 
 * @author: Victorli
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.admin.service.impl;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.repeatHourlyForever;
import static org.quartz.SimpleScheduleBuilder.repeatSecondlyForever;
import static org.quartz.TriggerBuilder.newTrigger;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jruby.embed.ScriptingContainer;
import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter;
import org.springframework.stereotype.Service;

import com.sun.management.OperatingSystemMXBean;
import com.topvision.ems.admin.dao.SystemMonitorDao;
import com.topvision.ems.admin.domain.EmsInfo;
import com.topvision.ems.admin.domain.SystemMonitor;
import com.topvision.ems.admin.job.ProactiveReportJob;
import com.topvision.ems.admin.job.SystemMonitorJob;
import com.topvision.ems.admin.service.PassiveReportService;
import com.topvision.ems.facade.CheckFacade;
import com.topvision.ems.facade.domain.EntityType;
import com.topvision.ems.network.dao.EntityDao;
import com.topvision.ems.template.dao.EntityTypeDao;
import com.topvision.framework.domain.ThreadPoolMonitor;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.version.dao.VersionDao;
import com.topvision.framework.version.domain.QueryResult;
import com.topvision.license.parser.LicenseIf;
import com.topvision.platform.SystemConstants;
import com.topvision.platform.SystemVersion;
import com.topvision.platform.debug.BeanService;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.service.SchedulerService;
import com.topvision.platform.service.SystemPreferencesService;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;

/**
 * @author Victorli
 * @created @Sep 20, 2016-10:01:05 AM
 *
 */
@Service("passiveReportService")
public class PassiveReportServiceImpl extends BaseService implements PassiveReportService {
    private static Logger logger = LoggerFactory.getLogger(PassiveReportServiceImpl.class);
    private Set<String> ability;
    private HttpInvokerServiceExporter exporter;
    @Autowired
    private SystemMonitorDao systemMonitorDao;
    @Autowired
    private LicenseIf licenseIf;
    @Autowired
    private SchedulerService schedulerService;
    @Autowired
    private SystemPreferencesService systemPreferencesService;
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private EntityDao entityDao;
    @Autowired
    private EntityTypeDao entityTypeDao;

    public void start() {
        ability = new HashSet<String>();
        try {
            ch.qos.logback.classic.LoggerContext loggerContext = (ch.qos.logback.classic.LoggerContext) LoggerFactory
                    .getILoggerFactory();
            List<ch.qos.logback.classic.Logger> logs = loggerContext.getLoggerList();

            Appender<ILoggingEvent> mybatis = null;
            for (ch.qos.logback.classic.Logger l : logs) {
                for (Iterator<Appender<ILoggingEvent>> itr = l.iteratorForAppenders(); itr.hasNext();) {
                    Appender<ILoggingEvent> appender = itr.next();
                    if ("mybatis".endsWith(appender.getName())) {
                        mybatis = appender;
                        loggerContext = l.getLoggerContext();
                        break;
                    }
                }
                if (mybatis != null) {
                    break;
                }
            }
            for (ch.qos.logback.classic.Logger l : logs) {
                if (l.getName().endsWith("domain") && l.getAppender("mybatis") == null) {
                    if (mybatis != null) {
                        l.addAppender(mybatis);
                        l.setAdditive(false);
                    } else {
                        l.setLevel(Level.INFO);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("", e);
        }
        try {
            exporter = new HttpInvokerServiceExporter();
            exporter.setServiceInterface(PassiveReportService.class);
            exporter.setService(this);
            exporter.prepare();
        } catch (Exception e) {
            logger.warn("", e);
        }
        // Add by Victor@20161009增加主动汇报开关，在conf目录下增加report.forbid文件
        File forbid = new File("conf/report.forbidden");
        // conf/report.forbid存在即不主动汇报
        SystemConstants.getInstance().putParam("REPORT.FORBIDDEN", String.valueOf(forbid.exists()));
        File allow = new File("conf/disk.allow");
        // conf/disk.allow存在即开启磁盘采集/默认不开启采集
        SystemConstants.getInstance().putParam("DISK.FORBIDDEN", String.valueOf(!allow.exists()));
        try {
            JobDetail job = newJob(SystemMonitorJob.class).withIdentity("systemMonitor", "Admin").build();
            job.getJobDataMap().put(SystemMonitorDao.class.getSimpleName(), systemMonitorDao);
            job.getJobDataMap().put(LicenseIf.class.getSimpleName(), licenseIf);
            job.getJobDataMap().put(PassiveReportService.class.getSimpleName(), this);

            TriggerBuilder<SimpleTrigger> builder = newTrigger()
                    .withIdentity(job.getKey().getName(), job.getKey().getGroup())
                    .withSchedule(repeatSecondlyForever(Integer.parseInt(systemPreferencesService
                            .getModulePreferences("Admin").getProperty("SystemMonitor.interval", "900"))));
            schedulerService.scheduleJob(job, builder.build());

            if (!forbid.exists()) {
                job = newJob(ProactiveReportJob.class).withIdentity("proactiveReport", "Admin").build();
                job.getJobDataMap().put(LicenseIf.class.getSimpleName(), licenseIf);
                job.getJobDataMap().put(PassiveReportService.class.getSimpleName(), this);

                builder = newTrigger().withIdentity(job.getKey().getName(), job.getKey().getGroup())
                        .withSchedule(repeatHourlyForever(Integer.parseInt(systemPreferencesService
                                .getModulePreferences("Admin").getProperty("ProactiveReport.interval", "24"))));
                schedulerService.scheduleJob(job, builder.build());
            }
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public void stop() {
        ability.clear();
        ability = null;
        exporter = null;
    }

    @Override
    public String getHid() {
        if (!ability.contains(ABILITY_PASSIVE)) {
            ability.add(ABILITY_PASSIVE);
        }
        return licenseIf.getHid();
    }

    @Override
    public EmsInfo getEmsInfo() {
        EmsInfo info = new EmsInfo();
        MemoryMXBean memorymbean = ManagementFactory.getMemoryMXBean();
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        OperatingSystemMXBean osmb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

        VersionDao versionDao = (VersionDao) BeanService.getInstance().getBean("versionDao");
        QueryResult r = versionDao.query("show global status like 'uptime'");
        long mysqlUptime = -1L;
        if (r != null && r.getDatas() != null && r.getDatas().size() == 1
                && r.getDatas().get(0).get("VARIABLE_VALUE") != null) {
            mysqlUptime = Long.parseLong(r.getDatas().get(0).get("VARIABLE_VALUE")) * 1000;
        }
        File file = new File("conf/config.properties");
        info.setHid(licenseIf.getHid());
        info.setVersion(new SystemVersion().toString());
        info.setEmsCurrentTime(new Date());
        info.setEmsSysUptime(new Date(runtimeMXBean.getStartTime()));
        info.setMysqlUptime(new Date(System.currentTimeMillis() - mysqlUptime));
        info.setTotalDisk(file.getTotalSpace());
        info.setTotalMem(osmb.getTotalPhysicalMemorySize());
        info.setTotalVmem(osmb.getCommittedVirtualMemorySize());
        info.setInitMem(memorymbean.getHeapMemoryUsage().getInit());
        info.setCommittedMem(memorymbean.getHeapMemoryUsage().getCommitted());
        info.setMaxMem(memorymbean.getHeapMemoryUsage().getMax());
        return info;
    }

    @Override
    public Set<String> getMethodNames() {
        Set<String> methods = new HashSet<String>();
        for (Method m : PassiveReportService.class.getMethods()) {
            methods.add(m.getName());
        }
        return methods;
    }

    @Override
    public Set<String> getAbility() {
        return ability;
    }

    @Override
    public List<SystemMonitor> getCpuAndMemory(Map<String, Object> map) {
        return systemMonitorDao.loadCpuAndMemory(map);
    }

    @Override
    public List<ThreadPoolMonitor> getThreadPoolMonitor() {
        List<ThreadPoolMonitor> list = new ArrayList<ThreadPoolMonitor>();
        List<CheckFacade> facades = facadeFactory.getAllFacade(CheckFacade.class);
        for (CheckFacade facade : facades) {
            list.addAll(facade.getThreadPoolMonitor());
        }
        return list;
    }

    @Override
    public List<String> getHostIPs() {
        List<String> res = new ArrayList<String>();
        Enumeration<NetworkInterface> netInterfaces;
        try {
            netInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
                Enumeration<InetAddress> nii = ni.getInetAddresses();
                while (nii.hasMoreElements()) {
                    ip = (InetAddress) nii.nextElement();
                    if (ip.getHostAddress().indexOf(":") == -1) {
                        res.add(ip.getHostAddress());
                        System.out.println("本机的ip=" + ip.getHostAddress());
                    }
                }
            }
        } catch (SocketException e) {
            logger.warn("", e);
        }
        return res;
    }

    @Override
    public Map<String, Integer> getDeviceCount() {
        Map<String, Integer> map = new HashMap<String, Integer>();
        List<EntityType> types = entityTypeDao.getEntityCategories();
        for (EntityType type : types) {
            map.put(type.getName(), entityDao.getEntityByType(type.getTypeId()).size());
        }
        return map;
    }

    @Override
    public Object dynamicInvoke(String jrubyScript) {
        ScriptingContainer container = new ScriptingContainer();
        return container.runScriptlet(jrubyScript);
    }
}
