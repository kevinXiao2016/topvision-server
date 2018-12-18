/***********************************************************************
 * $Id: CheckFacadeImpl.java,v 1.1 Jul 27, 2009 12:17:54 AM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.ems.engine.executor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;

import org.jruby.embed.ScriptingContainer;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

import com.alibaba.dubbo.config.InvokeGetProxy;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.sun.management.OperatingSystemMXBean;
import com.topvision.ems.engine.BaseEngine;
import com.topvision.ems.engine.BaseEngineIf;
import com.topvision.ems.engine.common.EngineThreadPool;
import com.topvision.ems.engine.dao.HsqlManager;
import com.topvision.ems.engine.launcher.CollectorContext;
import com.topvision.ems.engine.launcher.FacadeAutoAware;
import com.topvision.ems.engine.performance.CacheCount;
import com.topvision.ems.engine.performance.PerfExecutorService;
import com.topvision.ems.engine.performance.PerfScheduledFuture;
import com.topvision.ems.engine.util.EngineBeanDefinitionRegistry;
import com.topvision.ems.engine.util.EngineStatusUtil;
import com.topvision.ems.facade.CheckFacade;
import com.topvision.ems.facade.domain.EngineServerParam;
import com.topvision.ems.facade.domain.EngineServerStatus;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.ScheduleMessage;
import com.topvision.framework.annotation.Facade;
import com.topvision.framework.common.DateUtils;
import com.topvision.framework.common.NumberUtils;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.domain.ThreadPoolMonitor;
import com.topvision.framework.dubbo.ReferenceConfigCache;
import com.topvision.framework.ping.PingExecutorService;
import com.topvision.framework.ping.PingThreadPoolExecutor;
import com.topvision.framework.snmp.PerfSnmpExecutorService;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpThreadPoolExecutor;
import com.topvision.framework.snmp.SnmpTrapManager;
import com.topvision.platform.SystemConstants;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;

/**
 * @Create Date Jul 27, 2009 12:17:54 AM
 * 
 * @author kelers
 * 
 */
@Facade("checkFacade")
public class CheckFacadeImpl extends EmsFacade implements CheckFacade {
    private SqlSessionFactoryBean sqlSessionFactory;
    private SqlSessionTemplate sqlSessionTemplate;
    @Value("classpath*:com/topvision/**/mybatis/engine/*.xml")
    private Resource[] locations;
    @Autowired
    private EngineBeanDefinitionRegistry beanRegistry;
    private EngineServerParam init;
    @Autowired
    private FacadeAutoAware facadeAutoAware;
    @Autowired
    private PerfExecutorService perfExecutorService;
    @Autowired
    private SnmpExecutorService snmpExecutorService;
    @Autowired
    private PerfSnmpExecutorService perfSnmpExecutorService;
    @Autowired
    private PingExecutorService pingExecutorService;
    @Autowired
    private EngineThreadPool engineThreadPool;
    @Autowired
    private HsqlManager hsqlManager;
    @Autowired
    private SnmpTrapManager snmpTrapManager;
    private LoggerContext loggerContext;

    @PostConstruct
    public void init() {
        loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
    }

    public boolean check(String name) {
        logger.info("Engine[{}] has bean checked!!!", name);
        if (init == null) {
            return false;
        }
        return true;
    }

    @Override
    public void initEngine(EngineServerParam param) {
        logger.info("Engine init:{}", param);
        init = param;
        try {
            // 数据库连接池初始化
            ComboPooledDataSource dataSource = new ComboPooledDataSource();
            dataSource.setDriverClass(param.getJdbcDriverClassName());
            dataSource.setJdbcUrl(param.getJdbcUrl());
            dataSource.setUser(param.getJdbcUsername());
            dataSource.setPassword(param.getJdbcPassword());

            dataSource.setAutoCommitOnClose(true);
            dataSource.setCheckoutTimeout(param.getCheckoutTimeout());
            dataSource.setInitialPoolSize(param.getInitialPoolSize());
            dataSource.setMinPoolSize(param.getMinPoolSize());
            dataSource.setMaxPoolSize(param.getMaxPoolSize());
            dataSource.setMaxIdleTime(param.getMaxIdleTime());
            dataSource.setAcquireIncrement(param.getAcquireIncrement());
            dataSource.setMaxIdleTimeExcessConnections(param.getMaxIdleTimeExcessConnections());

            sqlSessionFactory = new SqlSessionFactoryBean();
            sqlSessionFactory.setDataSource(dataSource);
            sqlSessionFactory.setMapperLocations(locations);
            sqlSessionFactory.setTypeAliasesPackage("com.topvision");
            sqlSessionFactory.setTypeAliasesSuperType(AliasesSuperType.class);

            sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory.getObject());
            beanRegistry.registerSingleton("engineSqlSessionTemplate", sqlSessionTemplate);
            CollectorContext.getInstance().setSqlSessionTemplate(sqlSessionTemplate);
            logger.info("Engine database init success.");

            connectEngine(param);
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    private void connectEngine(EngineServerParam param) {
        facadeAutoAware.facadeAutoAware(param);
        // 调用所有实现了BaseEngine的conntected方法
        logger.info("Invoke the engine bean's connected method.");
        List<BaseEngineIf> list = beanRegistry.getBeans(BaseEngineIf.class);
        for (BaseEngineIf be : list) {
            try{
                be.setServiceIp(param.getServiceIp());
                be.setServicePort(param.getServicePort());
                be.setId(param.getId());
                be.setIp(param.getIp());
                be.setPort(param.getPort());
                be.connected();                
            } catch(Exception e) {
                logger.error("connectEngine connected error: " + e);
            }
        }
        logger.info("Engine connect successful.");
    }

    private void disconnect() {
        // 调用所有实现了BaseEngine的disconntected方法
        List<BaseEngine> list = beanRegistry.getBeans(BaseEngine.class);
        for (BaseEngine be : list) {
            be.setServiceIp(null);
            be.setServicePort(null);
            be.setId(null);
            be.setIp(null);
            be.setPort(null);
            be.disconnected();
        }
        facadeAutoAware.destroy();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.facade.CheckFacade#syncEngine(com.topvision.ems.facade.domain.
     * EngineServerParam)
     */
    @Override
    public void syncEngine(EngineServerParam param) {
        facadeAutoAware.facadeChangeAware(param);
    }

    @Override
    public EngineServerStatus getEngineServerStatus() {
        EngineServerStatus engineServerStatus = new EngineServerStatus();
        // 获取内存使用率
        engineServerStatus.setMemUsage(EngineStatusUtil.getMemUsage());
        // 获取运行时间
        engineServerStatus.setRunTime(EngineStatusUtil.getRunTime());
        // 获取线程数
        engineServerStatus.setThreadNumber(EngineStatusUtil.getThreadNumber());
        return engineServerStatus;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.facade.CheckFacade#shutDown()
     */
    @Override
    public void shutDown() {
        logger.info("start to shutdown");
        try {
            this.disconnect();
        } catch (Exception e) {
            logger.error("", e);
        }
        try {
            System.exit(0);
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    @Override
    public String getAdminStatus() {
        StringBuilder msg = new StringBuilder();
        try {
            MemoryMXBean memorymbean = ManagementFactory.getMemoryMXBean();
            RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
            final ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
            OperatingSystemMXBean osmb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
            File file = new File("conf/quartz.properties");

            msg.append("<ul><li><a href='#threadCount'>线程统计</a></li>");
            msg.append("<li><a href='#dubbo'>Dubbo接口</a></li>");
            msg.append("<li><a href='#taskCount'>任务数统计</a></li>");
            msg.append("<li><a href='#pool'>线程池监测</a></li>");
            msg.append("<li><a href='#task'>性能任务</a></li>");
            msg.append("<li><a href='#threadDetail'>线程查看</a></li></ul>");

            msg.append("<p><table class='datagrid'><caption>基本状态信息</caption><tr><th>Name</th><th>Value</th></tr>");
            msg.append("<tr><td>系统启动时间:</td><td>")
                    .append(DateUtils.FULL_S_FORMAT.format(new Date(runtimeMXBean.getStartTime()))).append("(")
                    .append(DateUtils.getTimeDesInObscure(runtimeMXBean.getUptime())).append(")").append("</td></tr>");
            msg.append("<tr><td>物理内存(Free/Total):</td><td>")
                    .append(NumberUtils.getByteLength(osmb.getFreePhysicalMemorySize())).append("/")
                    .append(NumberUtils.getByteLength(osmb.getTotalPhysicalMemorySize())).append("</td></tr>");
            msg.append("<tr><td>虚拟内存:</td><td>").append(NumberUtils.getByteLength(osmb.getCommittedVirtualMemorySize()))
                    .append("</td></tr>");
            msg.append("<tr><td>磁盘(Free/Total):</td><td>").append(NumberUtils.getByteLength(file.getFreeSpace()))
                    .append("/").append(NumberUtils.getByteLength(file.getTotalSpace())).append("</td></tr>");
            msg.append("<tr><td>CPU:</td><td>")
                    .append(NumberUtils.PERCENT_FORMAT_ONEDOT.format(osmb.getSystemCpuLoad())).append("</td></tr>");
            msg.append("<tr><td>CPU个数:</td><td>").append(osmb.getAvailableProcessors()).append("</td></tr>");
            msg.append("<tr><td>Heap Memory Init:</td><td>")
                    .append(NumberUtils.getByteLength(memorymbean.getHeapMemoryUsage().getInit())).append("</td></tr>");
            msg.append("<tr><td>Heap Memory Used:</td><td>")
                    .append(NumberUtils.getByteLength(memorymbean.getHeapMemoryUsage().getUsed())).append("</td></tr>");
            msg.append("<tr><td>Heap Memory Committed:</td><td>")
                    .append(NumberUtils.getByteLength(memorymbean.getHeapMemoryUsage().getCommitted()))
                    .append("</td></tr>");
            msg.append("<tr><td>Heap Memory Max:</td><td>")
                    .append(NumberUtils.getByteLength(memorymbean.getHeapMemoryUsage().getMax())).append("</td></tr>");
            msg.append("<tr><td>Non-Heap Memory Init:</td><td>")
                    .append(NumberUtils.getByteLength(memorymbean.getNonHeapMemoryUsage().getInit()))
                    .append("</td></tr>");
            msg.append("<tr><td>Non-Heap Memory Used:</td><td>")
                    .append(NumberUtils.getByteLength(memorymbean.getNonHeapMemoryUsage().getUsed()))
                    .append("</td></tr>");
            msg.append("<tr><td>Non-Heap Memory Committed:</td><td>")
                    .append(NumberUtils.getByteLength(memorymbean.getNonHeapMemoryUsage().getCommitted()))
                    .append("</td></tr>");
            msg.append("<tr><td>Non-Heap Memory Max:</td><td>")
                    .append(NumberUtils.getByteLength(memorymbean.getNonHeapMemoryUsage().getMax()))
                    .append("</td></tr>");
            msg.append("</table>");
            msg.append("</p>");

            ThreadGroup systemThreadGroup = Thread.currentThread().getThreadGroup();
            while (systemThreadGroup.getParent() != null) {
                systemThreadGroup = systemThreadGroup.getParent();
            }
            msg.append("<a name='threadCount' href='#home'>Home</a>");
            msg.append(
                    "<p><table class='datagrid'><caption>线程统计信息</caption><tr><th>name</th><th>name</th><th>name</th><th>线程数</th><th>下级线程组数</th></tr>");
            doThreadGroup(systemThreadGroup, msg, 0);
            List<String> knownThreads = new ArrayList<String>();
            knownThreads.add("Finalizer");
            knownThreads.add("NM3000QuartzScheduler_QuartzSchedulerThread");
            knownThreads.add("PerfResultSend");
            knownThreads.add("SnmpTrapManager");
            knownThreads.add("SyslogManager");

            msg.append("<p>关键线程:").append(knownThreads).append("</p>");
            Map<Thread, StackTraceElement[]> threads = Thread.getAllStackTraces();
            List<Thread> threadList = new ArrayList<Thread>();
            for (Thread thread : threads.keySet()) {
                threadList.add(thread);
                if (knownThreads.contains(thread.getName())) {
                    knownThreads.remove(thread.getName());
                }
            }
            msg.append("<p>出现异常的关键线程").append(knownThreads).append("</p>");
            msg.append("<tr><td>总计</td><td></td><td></td><td><a href='/admin/systemStatus.tv'>")
                    .append(systemThreadGroup.activeCount()).append("</a></td><td>")
                    .append(systemThreadGroup.activeGroupCount()).append("</td></tr>");
            msg.append("</table>");
            try {
                Collections.sort(threadList, new Comparator<Thread>() {
                    @Override
                    public int compare(Thread o1, Thread o2) {
                        return (int) (threadMXBean.getThreadCpuTime(o2.getId())
                                - threadMXBean.getThreadCpuTime(o1.getId()));
                        // if (o1.getThreadGroup().equals(o2.getThreadGroup())) {
                        // return o1.getName().compareTo(o2.getName());
                        // } else {
                        // return
                        // o1.getThreadGroup().getName().compareTo(o2.getThreadGroup().getName());
                        // }
                    }
                });
            } catch (Exception e1) {
            }
            msg.append("</p>");

            ConcurrentMap<String, ReferenceConfigCache> cacheHolder = ReferenceConfigCache.getCacheHolders();
            msg.append("<a name='dubbo' href='#home'>Home</a>");
            msg.append("<p><table class='datagrid'><caption>Dubbo接口缓存(").append(cacheHolder.size())
                    .append(")</caption><tr><th>Name</th><th>Class</th></tr>");
            for (String name : cacheHolder.keySet()) {
                ReferenceConfigCache cache = cacheHolder.get(name);
                ConcurrentMap<String, ReferenceConfig<?>> rcs = cache.getCaches();
                msg.append("<tr><td colspan='2'>").append(name).append("(").append(rcs.size()).append(")</td></tr>");
                for (String key : rcs.keySet()) {
                    msg.append("<tr><td>").append(key).append("</td><td>")
                            .append(InvokeGetProxy.getInvoker(rcs.get(key))).append("</td></tr>");
                }
            }
            msg.append("</table></p>");

            Map<String, AtomicInteger> counts = new HashMap<String, AtomicInteger>();
            ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = perfExecutorService
                    .getScheduledThreadPoolExecutor();
            ScheduledThreadPoolExecutor realtimeScheduledThreadPoolExecutor = perfExecutorService
                    .getRealtimeScheduledThreadPoolExecutor();
            ConcurrentHashMap<ScheduleMessage<OperClass>, PerfScheduledFuture> data = perfExecutorService
                    .getScheduledFutureConcurrentHashMaps();

            List<ScheduleMessage<OperClass>> smList = new ArrayList<ScheduleMessage<OperClass>>();
            for (ScheduleMessage<OperClass> scheduleMessage : data.keySet()) {
                smList.add(scheduleMessage);
                AtomicInteger count = counts.get(scheduleMessage.getCategory());
                if (count == null) {
                    count = new AtomicInteger();
                    counts.put(scheduleMessage.getCategory(), count);
                }
                count.getAndIncrement();
            }
            try {
                Collections.sort(smList, new Comparator<ScheduleMessage<?>>() {

                    @Override
                    public int compare(ScheduleMessage<?> o1, ScheduleMessage<?> o2) {
                        int c = o1.getCategory().compareTo(o2.getCategory());
                        if (c == 0) {
                            return o1.getIdentifyKey() > o2.getIdentifyKey() ? 1
                                    : o1.getIdentifyKey() == o2.getIdentifyKey() ? 0 : -1;
                        } else {
                            return c;
                        }
                    }
                });
            } catch (Exception e) {
            }

            List<String> names = new ArrayList<String>();
            names.addAll(counts.keySet());
            Collections.sort(names);

            msg.append("<a name='taskCount' href='#home'>Home</a>");
            msg.append("<p><table class='datagrid'><caption>任务数统计</caption><tr><th>任务类型</th><th>后台任务数</th></tr>");
            for (String name : names) {
                msg.append("<tr><td>").append(name).append("</td><td>")
                        .append(counts.get(name) == null ? "-" : counts.get(name).get()).append("</td></tr>");
            }
            msg.append("</table>");
            msg.append("</p>");

            msg.append("<a name='pool' href='#home'>Home</a>");
            msg.append("<p>Snmp Pool:").append(snmpExecutorService.getQueueSize()).append(",ActiveCount/PoolSize:")
                    .append(snmpExecutorService.getService().getActiveCount()).append("/")
                    .append(snmpExecutorService.getService().getPoolSize()).append(",Queue Size:")
                    .append(snmpExecutorService.getService().getQueue().size());
            msg.append("</p>");

            msg.append("<p>Perf Snmp Pool:").append(perfSnmpExecutorService.getQueueSize())
                    .append(",ActiveCount/PoolSize:").append(perfSnmpExecutorService.getService().getActiveCount())
                    .append("/").append(perfSnmpExecutorService.getService().getPoolSize()).append(",Queue Size:")
                    .append(perfSnmpExecutorService.getService().getQueue().size());
            msg.append("</p>");

            msg.append("<p>Ping Pool:").append(pingExecutorService.getQueueSize()).append(",ActiveCount/PoolSize:")
                    .append(pingExecutorService.getService().getActiveCount()).append("/")
                    .append(pingExecutorService.getService().getPoolSize()).append(",Queue Size:")
                    .append(pingExecutorService.getService().getQueue().size());
            msg.append("</p>");

            msg.append("<p>Thread Pool:").append(engineThreadPool.getQueueSize()).append(",ActiveCount/PoolSize:")
                    .append(engineThreadPool.getActiveCount()).append("/").append(engineThreadPool.getPoolSize())
                    .append(",Queue Size:").append(engineThreadPool.getQueue().size());
            msg.append("</p>");

            ThreadPoolExecutor threadPoolExecutor = snmpTrapManager.getExecutorService();
            msg.append("<p>Snmp Trap Manager ActiveCount/PoolSize:").append(threadPoolExecutor.getActiveCount())
                    .append("/").append(threadPoolExecutor.getPoolSize()).append(",Queue Size:")
                    .append(threadPoolExecutor.getQueue().size()).append(",Cache counts:")
                    .append(snmpTrapManager.getTrapCounts());
            msg.append("</p>");

            msg.append("<p>Performance local file count:<br>");
            for (CacheCount count : perfExecutorService.getCacheCount().values()) {
                msg.append(count).append("<br>");
            }
            msg.append("</p>");

            msg.append("<a name='task' href='#home'>Home</a>");
            msg.append("<p><table class='datagrid'><caption>性能采集任务数:").append(data.size())
                    .append(",ActiveCount/PoolSize:").append(scheduledThreadPoolExecutor.getActiveCount()).append("/")
                    .append(perfExecutorService.getPoolSize()).append(",Queue Size:")
                    .append(scheduledThreadPoolExecutor.getQueue().size()).append("<br>实时性能采集:ActiveCount/PoolSize:")
                    .append(realtimeScheduledThreadPoolExecutor.getActiveCount()).append("/")
                    .append(perfExecutorService.getRealtimePoolSize()).append(",Queue Size:")
                    .append(realtimeScheduledThreadPoolExecutor.getQueue().size())
                    .append("</caption><tr><th>Category</th><th>Scheduler</th><th>IdentifyKey</th><th>PreviousFireTime</th><th>NextFireTime</th></tr>");
            for (ScheduleMessage<OperClass> sm : smList) {
                String previousFireTime = "-";
                String nextFireTime = "-";
                if (sm.getDomain().getPreviousFireTime() != null) {
                    previousFireTime = DateUtils.FULL_FORMAT.format(new Date(sm.getDomain().getPreviousFireTime()))
                            + "-" + DateUtils.getTimeDesInObscure(
                                    System.currentTimeMillis() - sm.getDomain().getPreviousFireTime(), "zh_CN");
                    nextFireTime = DateUtils.FULL_FORMAT
                            .format(new Date(sm.getDomain().getPreviousFireTime() + sm.getPeriod()))
                            + "-"
                            + DateUtils.getTimeDesInObscure(
                                    System.currentTimeMillis() - sm.getDomain().getPreviousFireTime() - sm.getPeriod(),
                                    "zh_CN");
                }
                msg.append("<tr><td>").append(sm.getCategory()).append("</td><td>").append(sm.getDomain().getEntityId())
                        .append("</td><td>").append(sm.getSnmpParam().getIpAddress()).append("</td><td>")
                        .append(previousFireTime).append("</td><td>").append(nextFireTime).append("</td></tr>");
            }
            msg.append("</table>");
            msg.append("</p>");

            msg.append("<a name='threadDetail' href='#home'>Home</a>");
            msg.append("<p>总线程数:").append(threads.size()).append("<br>");
            for (Thread thread : threadList) {
                long id = thread.getId();
                ThreadInfo info = threadMXBean.getThreadInfo(id);
                msg.append(thread.toString()).append("--").append(info.getThreadState()).append("<br>");
                msg.append("ThreadCpuTime=").append(threadMXBean.getThreadCpuTime(id) / 1000000000);
                msg.append(";ThreadUserTime=").append(threadMXBean.getThreadUserTime(id) / 1000000000).append("<br>");
                if (SystemConstants.isDevelopment) {
                    StackTraceElement[] stacks = threads.get(thread);
                    for (StackTraceElement stack : stacks) {
                        msg.append("&nbsp;&nbsp;&nbsp;&nbsp;").append(stack).append("<br>");
                    }
                }
                msg.append(info).append("<br>");
                msg.append("<br>");
            }
            msg.append("</p>");
        } catch (Exception e) {
            msg.append("<br>").append(e.getMessage());
            for (StackTraceElement ste : e.getStackTrace()) {
                msg.append("<br>----").append(ste.toString());
            }
        }
        return msg.toString();
    }

    private void doThreadGroup(ThreadGroup group, StringBuilder msg, int index) {
        ThreadGroup[] subs = new ThreadGroup[group.activeGroupCount()];
        group.enumerate(subs, false);
        msg.append("<tr>");
        for (int i = 0; i < index; i++) {
            msg.append("<td></td>");
        }
        msg.append("<td>").append(group.getName()).append("</td>");
        for (int i = 2; i > index; i--) {
            msg.append("<td></td>");
        }
        int activeCount = group.activeCount();
        for (ThreadGroup sub : subs) {
            if (sub != null) {
                activeCount -= sub.activeCount();
            }
        }
        msg.append("<td><a href='/admin/systemStatus.tv?m=").append(group.getName()).append("'>").append(activeCount)
                .append("</a></td><td>").append(group.activeGroupCount()).append("</td></tr>");
        for (ThreadGroup sub : subs) {
            if (sub != null) {
                doThreadGroup(sub, msg, index + 1);
            }
        }
    }

    @Override
    public boolean resetLogger() {
        InputStream stream = null;
        try {
            File config = new File("conf/logback-test.xml");
            if (!config.exists()) {
                config = new File("conf/logback.xml");
            }
            if (!config.exists()) {
                return false;
            }
            stream = new FileInputStream(config);
            JoranConfigurator configurator = new JoranConfigurator();
            loggerContext.reset();
            configurator.setContext(loggerContext);
            configurator.doConfigure(stream);
            logger.info("Reset the logger config:{}", config);
        } catch (JoranException e) {
            logger.error("", e);
            return Boolean.FALSE;
        } catch (Exception el) {
            logger.error("", el);
            return Boolean.FALSE;
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    logger.error("", e);
                }
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public String runScripts(String scripts) {
        ScriptingContainer container = new ScriptingContainer();
        Object o = container.runScriptlet(scripts);
        if (logger.isDebugEnabled()) {
            logger.debug("admin.debug:{}", o);
        }
        return o.toString();
    }

    @Override
    public String executeHsql(String action, String sql) {
        if ("execute".equals(action)) {
            return executeHsql(sql);
        } else if ("query".equals(action)) {
            return queryHsql(sql);
        }
        return sql;
    }

    private String executeHsql(String sql) {
        Statement st = hsqlManager.createStatement();
        if (logger.isDebugEnabled()) {
            logger.debug("HSQL execute:{}", sql);
        }
        try {
            StringTokenizer token = new StringTokenizer(sql, ";");
            while (token.hasMoreTokens()) {
                String s = token.nextToken();
                if (s == null || s.trim().length() == 0) {
                    continue;
                }
                st.addBatch(s);
            }
            st.executeBatch();
            return "Success!";
        } catch (SQLException e) {
            logger.warn("", e);
            return e.getMessage();
        } finally {
            try {
                st.close();
            } catch (SQLException e) {
            }
        }
    }

    private String queryHsql(String sql) {
        Statement st = hsqlManager.createStatement();
        try {
            ResultSet rs = st.executeQuery(sql);
            StringBuilder result = new StringBuilder();
            result.append("<div>").append(sql).append("</div>");
            result.append("<table class='datagrid'>");
            result.append("<tr>");
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                result.append("<th>").append(metaData.getColumnName(i)).append("</th>");
            }
            result.append("</tr>");
            while (rs.next()) {
                result.append("<tr>");
                for (int i = 1; i <= columnCount; i++) {
                    result.append("<td>").append(rs.getString(i)).append("</td>");
                }
                result.append("</tr>");
            }
            rs.close();
            result.append("</table>");
            return result.toString();
        } catch (SQLException e) {
            logger.warn("", e);
            return e.getMessage();
        } finally {
            try {
                st.close();
            } catch (SQLException e) {
            }
        }
    }

    @Override
    public List<ThreadPoolMonitor> getThreadPoolMonitor() {
        List<ThreadPoolMonitor> list = new ArrayList<ThreadPoolMonitor>();
        ThreadPoolExecutor executor = perfExecutorService.getRealtimeScheduledThreadPoolExecutor();
        ThreadPoolMonitor tpm = new ThreadPoolMonitor();
        tpm.setName("perfRealtimeExecutorService");
        tpm.setEngineId(init.getId());
        tpm.setActiveCount(executor.getActiveCount());
        tpm.setCollectTime(System.currentTimeMillis());
        tpm.setCompletedTaskCount(executor.getCompletedTaskCount());
        tpm.setCorePoolSize(executor.getCorePoolSize());
        tpm.setLargestPoolSize(executor.getLargestPoolSize());
        tpm.setMaximumPoolSize(executor.getMaximumPoolSize());
        tpm.setPoolSize(executor.getPoolSize());
        tpm.setQueueSize(executor.getQueue().size());
        tpm.setTaskCount(executor.getTaskCount());
        list.add(tpm);

        executor = perfExecutorService.getScheduledThreadPoolExecutor();
        tpm = new ThreadPoolMonitor();
        tpm.setName("perfExecutorService");
        tpm.setEngineId(init.getId());
        tpm.setActiveCount(executor.getActiveCount());
        tpm.setCollectTime(System.currentTimeMillis());
        tpm.setCompletedTaskCount(executor.getCompletedTaskCount());
        tpm.setCorePoolSize(executor.getCorePoolSize());
        tpm.setLargestPoolSize(executor.getLargestPoolSize());
        tpm.setMaximumPoolSize(executor.getMaximumPoolSize());
        tpm.setPoolSize(executor.getPoolSize());
        tpm.setQueueSize(executor.getQueue().size());
        tpm.setTaskCount(executor.getTaskCount());
        list.add(tpm);

        SnmpThreadPoolExecutor snmp = snmpExecutorService.getService();
        tpm = new ThreadPoolMonitor();
        tpm.setName("snmpExecutorService");
        tpm.setEngineId(init.getId());
        tpm.setActiveCount(snmp.getActiveCount());
        tpm.setCollectTime(System.currentTimeMillis());
        tpm.setCompletedTaskCount(snmp.getCompletedTaskCount());
        tpm.setCorePoolSize(snmp.getCorePoolSize());
        tpm.setLargestPoolSize(snmp.getLargestPoolSize());
        tpm.setMaximumPoolSize(snmp.getMaximumPoolSize());
        tpm.setPoolSize(snmp.getPoolSize());
        tpm.setQueueSize(snmp.getQueue().size());
        tpm.setTaskCount(snmp.getTaskCount());
        list.add(tpm);

        snmp = perfSnmpExecutorService.getService();
        tpm = new ThreadPoolMonitor();
        tpm.setName("perfSnmpExecutorService");
        tpm.setEngineId(init.getId());
        tpm.setActiveCount(snmp.getActiveCount());
        tpm.setCollectTime(System.currentTimeMillis());
        tpm.setCompletedTaskCount(snmp.getCompletedTaskCount());
        tpm.setCorePoolSize(snmp.getCorePoolSize());
        tpm.setLargestPoolSize(snmp.getLargestPoolSize());
        tpm.setMaximumPoolSize(snmp.getMaximumPoolSize());
        tpm.setPoolSize(snmp.getPoolSize());
        tpm.setQueueSize(snmp.getQueue().size());
        tpm.setTaskCount(snmp.getTaskCount());
        list.add(tpm);

        PingThreadPoolExecutor ping = pingExecutorService.getService();
        tpm = new ThreadPoolMonitor();
        tpm.setName("pingExecutorService");
        tpm.setEngineId(init.getId());
        tpm.setActiveCount(ping.getActiveCount());
        tpm.setCollectTime(System.currentTimeMillis());
        tpm.setCompletedTaskCount(ping.getCompletedTaskCount());
        tpm.setCorePoolSize(ping.getCorePoolSize());
        tpm.setLargestPoolSize(ping.getLargestPoolSize());
        tpm.setMaximumPoolSize(ping.getMaximumPoolSize());
        tpm.setPoolSize(ping.getPoolSize());
        tpm.setQueueSize(ping.getQueue().size());
        tpm.setTaskCount(ping.getTaskCount());
        list.add(tpm);

        tpm = new ThreadPoolMonitor();
        tpm.setName("engineThreadPool");
        tpm.setEngineId(init.getId());
        tpm.setActiveCount(engineThreadPool.getActiveCount());
        tpm.setCollectTime(System.currentTimeMillis());
        tpm.setCompletedTaskCount(engineThreadPool.getCompletedTaskCount());
        tpm.setCorePoolSize(engineThreadPool.getCorePoolSize());
        tpm.setLargestPoolSize(engineThreadPool.getLargestPoolSize());
        tpm.setMaximumPoolSize(engineThreadPool.getMaximumPoolSize());
        tpm.setPoolSize(engineThreadPool.getPoolSize());
        tpm.setQueueSize(engineThreadPool.getQueue().size());
        tpm.setTaskCount(engineThreadPool.getTaskCount());
        list.add(tpm);
        return list;
    }

    @Override
    public Object dynamicInvoke(String jrubyScript) {
        ScriptingContainer container = new ScriptingContainer();
        return container.runScriptlet(jrubyScript);
    }
}
