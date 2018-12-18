/***********************************************************************
 * $Id: AdminAction.java,v1.0 2012-12-20 下午5:46:38 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.admin.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.dom4j.Element;
import org.dom4j.tree.DefaultElement;
import org.jruby.embed.ScriptingContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.alibaba.dubbo.config.InvokeGetProxy;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.sun.management.OperatingSystemMXBean;
import com.topvision.ems.admin.domain.DataSourceInfo;
import com.topvision.ems.admin.domain.LoggerTreeEntry;
import com.topvision.ems.admin.domain.SystemMonitor;
import com.topvision.ems.admin.service.C3p0PoolService;
import com.topvision.ems.admin.service.CpuAndMemoryService;
import com.topvision.ems.engine.performance.PerfExecutorService;
import com.topvision.ems.engine.performance.PerfScheduledFuture;
import com.topvision.ems.epon.fault.domain.OltEventSource;
import com.topvision.ems.facade.CheckFacade;
import com.topvision.ems.facade.domain.EntityType;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.ScheduleMessage;
import com.topvision.ems.fault.domain.Alert;
import com.topvision.ems.facade.performance.PerformanceFacade;
import com.topvision.ems.fault.service.EventSender;
import com.topvision.ems.fault.service.TrapService;
import com.topvision.ems.message.Message;
import com.topvision.ems.message.MessagePusher;
import com.topvision.ems.network.service.AutoRefreshService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.network.service.PortService;
import com.topvision.ems.performance.dao.PerformanceDao;
import com.topvision.ems.performance.domain.ExecutorThreadSnap;
import com.topvision.ems.performance.handle.PerformanceHandle;
import com.topvision.ems.performance.service.PerfThresholdService;
import com.topvision.ems.performance.service.PerformanceService;
import com.topvision.ems.performance.service.PerformanceStatistics;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.common.DateUtils;
import com.topvision.framework.common.FileUtils;
import com.topvision.framework.common.NumberUtils;
import com.topvision.framework.common.RunCmd;
import com.topvision.framework.dubbo.ReferenceConfigCache;
import com.topvision.framework.snmp.Trap;
import com.topvision.framework.version.dao.VersionDao;
import com.topvision.framework.version.domain.QueryResult;
import com.topvision.framework.web.dhtmlx.DefaultDhtmlxHandler;
import com.topvision.framework.web.dhtmlx.DhtmlxTreeOutputter;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.framework.web.util.HttpResponseUtils;
import com.topvision.license.parser.WR;
import com.topvision.platform.EmsHttpSessionListener;
import com.topvision.platform.SystemConstants;
import com.topvision.platform.SystemVersion;
import com.topvision.platform.debug.BeanService;
import com.topvision.platform.debug.DebugService;
import com.topvision.platform.domain.EngineServer;
import com.topvision.platform.domain.SystemPreferences;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.message.event.EmsEventObject;
import com.topvision.platform.message.event.EventListenerAggregate;
import com.topvision.platform.message.service.MessageService;
import com.topvision.platform.service.EngineServerService;
import com.topvision.platform.service.FrontEndLogService;
import com.topvision.platform.service.SchedulerService;
import com.topvision.platform.service.SystemPreferencesService;
import com.topvision.platform.service.ThreadPoolService;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author Victor
 * @created @2012-12-20-下午5:46:38
 * 
 */
@Controller("adminAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AdminAction extends BaseAction {
    private static final long serialVersionUID = 4584830182590917814L;
    private final Logger logger = LoggerFactory.getLogger(AdminAction.class);
    private String m;
    private Map<String, String> d;
    private List<Map<String, String>> ds;
    @Autowired
    private DebugService debugService;
    @Autowired
    private SchedulerService schedulerService;
    @Autowired
    private ThreadPoolService threadPoolService;
    @Autowired
    private C3p0PoolService c3p0PoolService;
    @Autowired
    private CpuAndMemoryService cpuAndMemoryService;
    @Autowired
    private PerformanceDao performanceDao;
    @Autowired
    private MessageService messageService;
    @Autowired
    private FrontEndLogService frontEndLogService;
    @Autowired
    protected PerformanceStatistics performanceStatistics;
    @Autowired
    private SystemPreferencesService systemPreferencesService;
    @Autowired
    private EntityTypeService entityTypeService;
    @Autowired
    private PerfThresholdService perfThresholdService;
    @Autowired
    private EntityService entityService;
    @Autowired
    private PerformanceService<?> performanceService;
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private AutoRefreshService autoRefreshService;
    @Resource(name = "messagePusher")
    private MessagePusher messagePusher;

    @Autowired
    private PortService portService;
    @Autowired
    private EngineServerService engineServerService;

    private JSONObject serverDataSource = new JSONObject();
    private JSONObject engineDataSource = new JSONObject();
    private JSONObject dataSourceInfo = new JSONObject();
    private List<Map<String, String>> datas;
    private Timestamp st;
    private Timestamp et;

    private String sampleInterval;
    private String flowCollectType;
    private List<EngineServer> engines;
    private File upload;
    private Integer engineId;
    private Integer periodCount;

    private String ip;
    private Integer typeId;
    private Long entityId;
    private Long parentId;
    private Long slotNo;
    private Long portNo;
    private Long onuNo;
    private Long onuPortNo;
    private String message;
    private Boolean frontEndLogSwitch;

    public String showSendMessage() {
        return SUCCESS;
    }

    public String sendMessage() {
        Message msg = new Message(Message.ALERT_EVENT);

        OltEventSource sourceObject = new OltEventSource(entityId);
        sourceObject.setSlotNo(slotNo);
        sourceObject.setPortNo(portNo);
        sourceObject.setOnuNo(onuNo);
        sourceObject.setOnuPortNo(onuPortNo);

        Alert alert = new Alert();
        alert.setHost(ip);
        alert.setTypeId(typeId);
        alert.setEntityId(entityId);
        alert.setParentId(parentId);
        alert.setSourceObject(sourceObject);
        alert.setSource(sourceObject.formatSource());
        alert.setMessage(message);
        msg.setData(alert);
        if (alert != null && alert.getTypeId() != null) {
            msg.setId(alert.getTypeId().toString());
        }
        messagePusher.sendMessage(msg);
        return NONE;
    }

    /**
     * 展示CMTS速率采样配置页面
     * 
     * @return
     */
    public String showCmtsFlowSampling() {
        if (!hasPower()) {
            return ERROR;
        }
        return SUCCESS;
    }

    /**
     * 加载CMTS速率采样配置信息
     * 
     * @return
     * @throws IOException
     */
    public String loadCmtsFlowSampling() throws IOException {
        JSONObject json = new JSONObject();
        Properties cmtsFlowCollect = systemPreferencesService.getModulePreferences("cmtsFlowCollect");
        if ("-1".equalsIgnoreCase(cmtsFlowCollect.getProperty("sampleInterval"))) {
            json.put("samplingSwitch", 0);
        } else {
            json.put("samplingSwitch", 1);
        }
        json.put("sampleInterval", cmtsFlowCollect.getProperty("sampleInterval"));
        json.put("flowCollectType", cmtsFlowCollect.getProperty("flowCollectType"));
        // 获取所有CMTS的子类型
        List<EntityType> cmtsTypes = entityTypeService.loadSubType(40000L);
        json.put("cmtsTypes", cmtsTypes);
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 修改CMTS速率采样配置信息
     * 
     * @return
     */
    public String modifyCmtsFlowSampling() {
        List<SystemPreferences> preferences = new ArrayList<SystemPreferences>();
        SystemPreferences intervalPreference = new SystemPreferences();
        intervalPreference = new SystemPreferences();
        intervalPreference.setName("sampleInterval");
        intervalPreference.setValue(sampleInterval);
        intervalPreference.setModule("cmtsFlowCollect");
        preferences.add(intervalPreference);
        SystemPreferences typePreference = new SystemPreferences();
        typePreference = new SystemPreferences();
        typePreference.setName("flowCollectType");
        typePreference.setValue(flowCollectType);
        typePreference.setModule("cmtsFlowCollect");
        preferences.add(typePreference);
        systemPreferencesService.savePreferences(preferences);
        // cmtsPerfService.modifyCmtsFlowSampleColect(flowCollectType,
        // Integer.parseInt(sampleInterval));
        return NONE;
    }

    public String showDataSource() throws SQLException {
        if (!hasPower()) {
            return ERROR;
        }
        List<String> ret = c3p0PoolService.databasePool();
        JSONArray server_json = new JSONArray();
        JSONObject server_ds = new JSONObject();
        server_ds.put("Type", "Server连接池");
        server_ds.put("ServerDataSource_URL", ret.get(0));
        server_ds.put("ServerDataSource_All_Number", ret.get(1));
        server_ds.put("ServerDataSource_Use_Number", ret.get(2));
        server_ds.put("ServerDataSource_Idle_Number", ret.get(3));
        server_json.add(server_ds);
        serverDataSource.put("server_ds", server_json);

        JSONArray engine_json = new JSONArray();
        JSONObject engine_ds = new JSONObject();
        engine_ds.put("Type", "Engine连接池");
        engine_ds.put("EngineDataSource_URL", ret.get(4));
        engine_ds.put("EngineDataSource_All_Number", ret.get(5));
        engine_ds.put("EngineDataSource_Use_Number", ret.get(6));
        engine_ds.put("EngineDataSource_Idle_Number", ret.get(7));
        engine_json.add(engine_ds);
        engineDataSource.put("engine_ds", engine_json);
        List<DataSourceInfo> dataSourceInfos = c3p0PoolService.getDataSourceInfos();
        JSONArray jsonArray2 = new JSONArray();
        for (DataSourceInfo tmp : dataSourceInfos) {
            JSONObject object = new JSONObject();
            object.put("id", tmp.getId());
            object.put("command", tmp.getCommand());
            object.put("db", tmp.getDb());
            object.put("host", tmp.getHost());
            object.put("info", tmp.getInfo());
            object.put("state", tmp.getState());
            object.put("time", tmp.getTime());
            object.put("user", tmp.getUser());
            jsonArray2.add(object);
        }
        dataSourceInfo.put("dataSourceInfo", jsonArray2);
        return SUCCESS;
    }

    /**
     * 展示CPU和内存统计
     * 
     * @return
     */
    public String showCpuAndMemory() {
        if (!hasPower()) {
            return ERROR;
        }
        return SUCCESS;
    }

    /**
     * 加载CPU和内存的数据
     * 
     * @return
     * @throws IOException
     */
    public String loadCpuAndMemory() throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("st", st);
        map.put("et", et);
        List<SystemMonitor> list = cpuAndMemoryService.loadCpuAndMemory(map);
        int size = list.size();
        long[][] cpuList = new long[size][2];
        long[][] heapList = new long[size][2];
        long[][] nonheapList = new long[size][2];
        long[][] diskList = new long[size][2];
        long[][] diskReadsList = new long[size][2];
        long[][] diskWritesList = new long[size][2];
        long[][] threadCountList = new long[size][2];
        int index = 0;
        long M_SIZE = 1024 * 1024l;
        for (SystemMonitor sm : list) {
            long x = sm.getCollectTime().getTime();
            long heapMem = sm.getHeapMemory();
            long nonheapMem = sm.getNonHeapMemory();
            long disk = sm.getDisk();

            long[] cpu = new long[] { x, (long) (sm.getCpu() * 100) };
            long[] heap = new long[] { x, heapMem / M_SIZE };
            long[] nonheap = new long[] { x, nonheapMem / M_SIZE };
            long[] diskUse = new long[] { x, disk / M_SIZE / 1024 };
            long[] diskReadsUse = new long[] { x, sm.getDiskReads() };
            long[] diskWritesUse = new long[] { x, sm.getDiskWrites() };
            long[] threadCountUse = new long[] { x, sm.getThreadCount() };

            cpuList[index] = cpu;
            heapList[index] = heap;
            nonheapList[index] = nonheap;
            diskList[index] = diskUse;
            diskReadsList[index] = diskReadsUse;
            diskWritesList[index] = diskWritesUse;
            threadCountList[index] = threadCountUse;
            index++;
        }

        JSONObject json = new JSONObject();
        json.put("cpu", cpuList);
        json.put("heap", heapList);
        json.put("nonheap", nonheapList);
        json.put("disk", diskList);
        json.put("diskReads", diskReadsList);
        json.put("diskWrites", diskWritesList);
        json.put("threadcount", threadCountList);
        json.put("st", st);
        json.put("et", et);
        json.write(response.getWriter());
        if (logger.isDebugEnabled()) {
            logger.debug(json.toString());
        }
        return NONE;
    }

    /**
     * 展示设备重启图表
     * 
     * @return
     */
    public String showRestartAnalyzer() {
        if (!hasPower()) {
            return ERROR;
        }
        return SUCCESS;
    }

    /**
     * 展示设备重启统计
     * 
     * @return
     */
    public String showRestartStatistic() {
        if (!hasPower()) {
            return ERROR;
        }
        return SUCCESS;
    }
    
    public String showFrontEndLogger() {
        // 获取当前客户端的前端日志开启状态
        frontEndLogSwitch = frontEndLogService.getFrontEndLogStatus(request.getSession().getId());
        return SUCCESS;
    }
    
    public String openFrontEndLog() {
        frontEndLogService.openFrontEndLog(request.getSession().getId());
        return NONE;
    }
    
    public String closeFrontEndLog() {
        frontEndLogService.closeFrontEndLog(request.getSession().getId());
        return NONE;
    }

    /**
     * 展示实时日志
     * 
     * @return
     */
    public String showLogger() {
        if (!hasPower()) {
            return ERROR;
        }
        return SUCCESS;
    }

    public String dd() throws Exception {
        if (!hasPower()) {
            return ERROR;
        }
        c3p0PoolService.databasePool();
        logger.info("view admin information");
        return SUCCESS;
    }

    public String systemStatus() throws Exception {
        if (!hasPower()) {
            return ERROR;
        }
        StringBuilder msg = new StringBuilder();
        try {
            msg.append("<ul><li><a href='#threadCount'>线程统计</a></li>");
            msg.append("<li><a href='#dubbo'>Dubbo接口</a></li>");
            msg.append("<li><a href='#handler'>Handle接口</a></li>");
            msg.append("<li><a href='#cache'>缓存查看</a></li>");
            msg.append("<li><a href='#listener'>消息接口</a></li>");
            msg.append("<li><a href='#threadDetail'>线程查看</a></li></ul>");
            MemoryMXBean memorymbean = ManagementFactory.getMemoryMXBean();
            RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
            final ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
            OperatingSystemMXBean osmb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

            VersionDao versionDao = (VersionDao) BeanService.getInstance().getBean("versionDao");
            QueryResult r = versionDao.query("show global status like 'uptime'");
            long mysqlUptime = -1L;
            if (r != null && r.getDatas() != null && r.getDatas().size() == 1
                    && r.getDatas().get(0).get("VARIABLE_VALUE") != null) {
                mysqlUptime = Long.parseLong(r.getDatas().get(0).get("VARIABLE_VALUE")) * 1000;
            }
            File file = new File("conf/config.properties");
            logger.debug("query result:\n{}", m);
            // 重启功能
            if (SystemConstants.isSuperMode) {
                msg.append("<div><form name='update' action='/admin/update.tv' method='post'>");
                msg.append("<input type='button' value='升级/重启' onclick='restart();' />");
                msg.append("</form></div>");
                // 模式切换
                msg.append("<div><form action='/admin/change2Development.tv' method='post'>");
                msg.append("运行模式：");
                if (SystemConstants.isDevelopment) {
                    msg.append("开发模式");
                } else {
                    msg.append("发布模式");
                }
                msg.append("<input class='button' type='submit' value='更改' />");
                msg.append("</form></div>");
            }

            msg.append("<p><table class='datagrid'><caption>基本状态信息</caption><tr><th>Name</th><th>Value</th></tr>");
            msg.append("<tr><td>版本号:</td><td>").append(new SystemVersion()).append("</td></tr>");
            msg.append("<tr><td>进程信息:</td><td>").append(runtimeMXBean.getName()).append("</td></tr>");
            msg.append("<tr><td>系统启动时间:</td><td>")
                    .append(DateUtils.FULL_S_FORMAT.format(new Date(runtimeMXBean.getStartTime()))).append("(")
                    .append(DateUtils.getTimeDesInObscure(runtimeMXBean.getUptime())).append(")").append("</td></tr>");
            msg.append("<tr><td>Mysql启动时间:</td><td>")
                    .append(DateUtils.FULL_S_FORMAT.format(new Date(System.currentTimeMillis() - mysqlUptime)))
                    .append("(").append(DateUtils.getTimeDesInObscure(mysqlUptime)).append(")").append("</td></tr>");
            msg.append("<tr><td>运行模式:</td><td>").append(SystemConstants.isDevelopment ? "开发模式" : "发布模式")
                    .append("</td></tr>");
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
            msg.append("<tr><td>Event.Queue.Size:</td><td>").append(EventSender.getInstance().getQueue().size())
                    .append("</td></tr>");
            msg.append("</table>");
            msg.append("</p>");

            ThreadGroup systemThreadGroup = Thread.currentThread().getThreadGroup();
            while (systemThreadGroup.getParent() != null) {
                systemThreadGroup = systemThreadGroup.getParent();
            }
            msg.append(
                    "<p><table class='datagrid'><caption>线程统计信息</caption><tr><th>name</th><th>name</th><th>name</th><th>线程数</th><th>下级线程组数</th></tr>");
            doThreadGroup(systemThreadGroup, msg, 0);

            msg.append("<a name='cache' href='#home'>Home</a>");
            msg.append("<form action='/admin/clearPerformanceQueue.tv' method='post'>");
            msg.append("<p>PerformanceStatisticsCenter.getPerformanceQueue:")
                    .append(performanceStatistics.getPerformanceQueue().size()).append("<br>");
            msg.append("PerformanceStatisticsCenter.getRealtimePerformanceQueue:")
                    .append(performanceStatistics.getRealtimePerformanceQueue().size()).append("<br>");
            msg.append("<input class='button' type='submit' value='清空' /></p>");
            msg.append("</form>");

            msg.append("<p>");
            msg.append("autoRefreshService.getPoolSize [").append(autoRefreshService.getAutoRefreshPoolSize())
                    .append("]").append("<br>");
            msg.append("autoRefreshService [").append(autoRefreshService.getScheduledThreadPoolExecutor()).append("]")
                    .append("<br>");
            msg.append("</p>");

            msg.append("<p>");
            msg.append("threadPoolService.getPoolSize [").append(threadPoolService.getPoolSize()).append("]")
                    .append("<br>");
            msg.append("threadPoolService.getActiveCount [").append(threadPoolService.getActiveCount()).append("]")
                    .append("<br>");
            msg.append("threadPoolService.getCompletedTaskCount [").append(threadPoolService.getCompletedTaskCount())
                    .append("]").append("<br>");
            msg.append("threadPoolService.getTaskCount [").append(threadPoolService.getTaskCount()).append("]")
                    .append("<br>");
            msg.append("threadPoolService.getQueue [").append(threadPoolService.getQueue().size()).append("]")
                    .append("<br>");
            msg.append("</p>");
            msg.append("<p>");
            msg.append("<form action='/admin/clearPerfThresholdCaches.tv' method='post'>");
            msg.append("perfThresholdService.getThresholdCache.size = ")
                    .append(perfThresholdService.getEntityTemplateCache().size()).append("<br>");
            msg.append("perfThresholdService.getRuleCache.size = ")
                    .append(perfThresholdService.getTemplateTargetRuleCache().size()).append("<br>");
            msg.append("<input class='button' type='submit' value='清空' /></p>");
            msg.append("</form>");

            msg.append("<form action='/admin/clearPortCaches.tv' method='post'>");
            msg.append("portService.getPortCaches.size = ").append(portService.getPortCaches().size()).append("<br>");
            msg.append("<input class='button' type='submit' value='清空' /></p>");
            msg.append("</form>");

            msg.append("<form action='/admin/clearEntityCaches.tv' method='post'>");
            msg.append("entityService.getEntityCaches.size = ").append(entityService.getEntityCaches().size())
                    .append("<br>");
            msg.append("<input class='button' type='submit' value='清空' /></p>");
            msg.append("</form>");

            msg.append("<form action='/admin/clearSnmpParamCaches.tv' method='post'>");
            msg.append("entityService.getSnmpParamCaches.size = ").append(entityService.getSnmpParamCaches().size())
                    .append("<br>");
            msg.append("<input class='button' type='submit' value='清空' /></p>");
            msg.append("</form>");
            
            msg.append("<form action='/admin/clearSystemPreferencesCaches.tv' method='post'>");
            msg.append("systemPreferencesService.getCaches.size = ").append(systemPreferencesService.getCacheSize())
                    .append("<br>");
            msg.append("<input class='button' type='submit' value='清空' /></p>");
            msg.append("</form>");
            
            msg.append("</p>");

            List<String> knownThreads = new ArrayList<String>();
            knownThreads.add("AutoDiscovery");
            knownThreads.add("BoardTempParser");
            knownThreads.add("CmActionWorker");
            knownThreads.add("CpeActionWorker");
            knownThreads.add("DateCache");
            knownThreads.add("DefaultEventParser");
            knownThreads.add("EventReceiver");
            knownThreads.add("EventSender");
            knownThreads.add("FanRemoveEventParser");
            knownThreads.add("Finalizer");
            knownThreads.add("IgmpCallCrdEventParser");
            knownThreads.add("JConsoleServiceListener");
            knownThreads.add("LoggerPusher");
            knownThreads.add("MessagePusherThread");
            knownThreads.add("NM3000QuartzScheduler_QuartzSchedulerThread");
            knownThreads.add("OnlineService.checkThread");
            knownThreads.add("OnuRegisterFailEventParser");
            knownThreads.add("PerfResultSend");
            knownThreads.add("PerfThread");
            knownThreads.add("PerformanceStatisticsCenter");
            knownThreads.add("PingEventParser");
            knownThreads.add("PortLinkedEventParser");
            knownThreads.add("PowerRemoveEventParser");
            knownThreads.add("SlotRemoveEventParser");
            knownThreads.add("SlotResetEventParser");
            knownThreads.add("SnmpTrapManager");
            knownThreads.add("SyslogListener");
            knownThreads.add("SyslogManager");
            knownThreads.add("TFTPServerSocket");
            knownThreads.add("main");

            List<String> knownThreadGroups = new ArrayList<String>();
            knownThreadGroups.addAll(knownThreads);
            knownThreadGroups.add("QuartzThreadGroup");
            knownThreadGroups.add("QuartzScheduler:NM3000QuartzScheduler");
            knownThreadGroups.add("ThreadPool");
            knownThreadGroups.add("PingThreadGroup");
            knownThreadGroups.add("SnmpThreadGroup");
            knownThreadGroups.add("SnmpUDPTransportMapping");
            knownThreadGroups.add("EngineThreadPool");
            knownThreadGroups.add("PerfExecutorService");
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

            msg.append("<a name='threadCount' href='#home'>Home</a>");
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
            } catch (Exception e) {
            }
            msg.append("</p>");

            Map<Class<?>, EventListenerAggregate> regs = messageService.getRegListeners();
            msg.append("<a name='listener' href='#home'>Home</a>");
            msg.append("<p><table class='datagrid'><caption>消息监听器(").append(regs.size())
                    .append(")</caption><tr><th>Class</th><th>Value</th></tr>");
            for (Iterator<Class<?>> itr = regs.keySet().iterator(); itr.hasNext();) {
                Class<?> clazz = itr.next();
                EventListenerAggregate aggr = regs.get(clazz);
                msg.append("<tr><td>").append(clazz.getName()).append("</td><td>");
                for (EventListener l : aggr.getListenersInternal()) {
                    msg.append(l.getClass().getName()).append("<br/>");
                }
                msg.append("</td></tr>");
            }
            msg.append("</table></p>");
            ArrayBlockingQueue<EmsEventObject<?>> queue = messageService.getEventQueue();
            msg.append("<p><table class='datagrid'><caption>消息监听器的内容(").append(queue.size())
                    .append(")</caption><tr><th>Class</th><th>Value</th></tr>");
            for (EmsEventObject<?> event : queue) {
                msg.append("<tr><td>").append(event.getListener()).append("</td><td>").append(event)
                        .append("</td></tr>");
            }
            msg.append("</table></p>");

            Map<String, PerformanceHandle> handles = performanceStatistics.getPerformanceHandle();
            msg.append("<a name='handler' href='#home'>Home</a>");
            msg.append("<p><table class='datagrid'><caption>性能处理Handles(").append(handles.size())
                    .append(")</caption><tr><th>Name</th><th>Class</th></tr>");
            for (String name : handles.keySet()) {
                PerformanceHandle handle = handles.get(name);
                msg.append("<tr><td>").append(name).append("</td><td>").append(handle.getClass().getName())
                        .append("</td></tr>");
            }
            msg.append("</table></p>");

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

            msg.append("<a name='threadDetail' href='#home'>Home</a>");
            if (m != null) {
                msg.append("<p>当前线程组:").append(m).append("<br>");
            } else {
                msg.append("<p>总线程数:").append(threads.size()).append("<br>");
            }
            for (Thread thread : threadList) {
                if (m != null && !thread.getThreadGroup().getName().equals(m)) {
                    continue;
                }
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

        } catch (

        Exception e) {
            msg.append("<br>").append(e.getMessage());
            for (StackTraceElement ste : e.getStackTrace()) {
                msg.append("<br>----").append(ste.toString());
            }
        }
        m = msg.toString();
        return SUCCESS;
    }

    public String logonInfo() throws Exception {
        if (!hasPower()) {
            return ERROR;
        }
        StringBuilder msg = new StringBuilder();
        List<UserContext> logonUsers = new ArrayList<UserContext>();
        msg.append(
                "<p><table class='datagrid'><caption>Session信息</caption><tr><th>ID</th><th>创建时间</th><th>最新时间</th><th>超时时间</th><th>IP</th><th>用户名</th></tr>");
        List<HttpSession> sessions = EmsHttpSessionListener.getSessions();
        UserContext userDetail = null;
        Collections.sort(sessions, new Comparator<HttpSession>() {
            @Override
            public int compare(HttpSession o1, HttpSession o2) {
                return (int) (o2.getLastAccessedTime() - o1.getLastAccessedTime());
            }
        });
        for (HttpSession session : sessions) {
            msg.append("<tr><td>").append(session.getId()).append("</td><td>")
                    .append(DateUtils.format(session.getCreationTime())).append("</td><td>")
                    .append(DateUtils.getTimeDesInObscure(System.currentTimeMillis() - session.getLastAccessedTime()));
            msg.append("</td><td>").append(session.getMaxInactiveInterval());
            msg.append("</td><td>").append(session.getAttribute("ip"));
            msg.append("</td><td>");
            Object obj = session.getAttribute("UserContext");
            if (obj != null) {
                UserContext userContext = (UserContext) obj;
                if (session.getId().equals(m)) {
                    userDetail = userContext;
                }
                msg.append("<a href='/admin/logonInfo.tv?m=").append(session.getId()).append("'>")
                        .append(userContext.getUser().getFamilyName()).append("(")
                        .append(userContext.getUser().getUserName()).append(")</a>");
            } else {
                msg.append("未登录或者退出");
            }
            msg.append("</td></tr>");
            for (@SuppressWarnings("unchecked")
            Enumeration<String> en = session.getAttributeNames(); en.hasMoreElements();) {
                String name = en.nextElement();
                obj = session.getAttribute(name);
                if ("UserContext".equals(name)) {
                    UserContext userContext = (UserContext) obj;
                    if (!logonUsers.contains(userContext)) {
                        logonUsers.add(userContext);
                    }
                    continue;
                }
            }
        }
        msg.append("</table>");
        msg.append("</p>");
        msg.append("<p>");
        if (userDetail != null) {
            msg.append(userDetail).append("<br>");
        }
        msg.append("</p>");
        m = msg.toString();
        return SUCCESS;
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

    /**
     * 按engine查看性能统计
     * 
     * @return
     * @throws Exception
     */
    public String viewMonitors() throws Exception {
        if (!hasPower()) {
            return ERROR;
        }
        datas = new ArrayList<>();
        engines = engineServerService.getEngineServerList();
        Map<Integer, Set<Long>> messageIdCache = performanceService.getMessageIdCache();
        for (EngineServer es : engines) {
            try {
                Map<String, String> data = new HashMap<>();
                datas.add(data);
                data.put("engineId", es.getEngineId());
                data.put("status", es.getLinkStatusStr());
                data.put("monitors", messageIdCache.containsKey(es.getId())
                        ? String.valueOf(messageIdCache.get(es.getId()).size()) : "0");
                PerformanceFacade facade = facadeFactory.getFacade(es, PerformanceFacade.class);
                data.putAll(facade.getStatus());
            } catch (Exception e) {
                logger.debug("", e);
            }
        }
        return SUCCESS;
    }

    /**
     * 删除所有性能采集器中性能采集
     * 
     * @return
     * @throws Exception
     */
    public String clearMonitors() throws Exception {
        if (!hasPower()) {
            return ERROR;
        }
        logger.info("clearMonitors");
        engines = engineServerService.getEngineServerList();
        for (EngineServer es : engines) {
            if (es.getAdminStatus() != EngineServer.START) {
                // 停用的不管
                continue;
            }
            if (!es.getType().contains(EngineServer.TYPE_PERFORMANCE) && !es.getType().equals(EngineServer.TYPE_ALL)) {
                // 不是性能采集器不管
                continue;
            }
            PerformanceFacade facade = facadeFactory.getFacade(es, PerformanceFacade.class);
            facade.clear();
        }
        performanceService.destroy();
        performanceService.initialize();
        return NONE;
    }

    /**
     * 把性能全部重新
     * 
     * @return
     * @throws Exception
     */
    public String startMonitors() throws Exception {
        if (!hasPower()) {
            return ERROR;
        }
        logger.info("startMonitors");
        performanceService.start();
        return NONE;
    }

    public String engineInfo() throws Exception {
        if (!hasPower()) {
            return ERROR;
        }
        StringBuilder msg = new StringBuilder();

        engines = engineServerService.getEngineServerList();
        Map<Integer, Set<Long>> messageIdCache = performanceService.getMessageIdCache();
        for (EngineServer es : engines) {
            if (!es.getName().equals(m)) {
                continue;
            }
            try {
                String esDesc = es.getName() + "(" + es.getIp() + ":" + es.getPort() + ")";
                msg.append("<p><table class='datagrid'><caption>").append(esDesc).append(
                        "</caption><tr><th>EntityId</th><th>Category</th><th>Period</th><th>CreateTime</th><th>LastCollectTime</th></tr>");
                Set<Long> engineMessage = messageIdCache.get(es.getId());
                if (engineMessage != null) {
                    for (Long monitorId : engineMessage) {
                        ScheduleMessage<?> scheduleMessage = performanceDao.getScheduleMessageForAdminAction(monitorId);
                        if (scheduleMessage != null) {
                            msg.append("<tr><td>").append(scheduleMessage.getIdentifyKey()).append("</td><td>")
                                    .append(scheduleMessage.getCategory()).append("</td><td>")
                                    .append(scheduleMessage.getPeriod());
                            msg.append("</td><td>").append(scheduleMessage.getCreateTime());
                            if (scheduleMessage.getLastCollectTime() == null) {
                                msg.append("</td><td>").append("未执行");
                            } else {
                                msg.append("</td><td>").append(scheduleMessage.getLastCollectTime());
                            }
                            msg.append("</td></tr>");
                        }
                    }
                }
                msg.append("</p><p>");
                CheckFacade check = facadeFactory.getCheckFacade(es);
                if (check != null) {
                    msg.append(check.getAdminStatus());
                } else {
                    msg.append("disconnected");
                }
            } catch (Exception e) {
                logger.debug("", e);
                msg.append("error:").append(e.getMessage());
            }
            msg.append("</p>");
        }
        m = msg.toString();
        return SUCCESS;
    }

    public String loadEngineHealthInfo() {
        JSONArray list = new JSONArray();

        // 获取engine信息
        engines = engineServerService.getEngineServerList();
        Map<Integer, EngineServer> engineMap = new HashMap<Integer, EngineServer>();
        for (EngineServer engine : engines) {
            engineMap.put(engine.getId(), engine);
        }
        // 获取超过periodCount周期未执行的任务，将其归纳为不同采集器上
        List<ScheduleMessage<?>> perfMonitors = performanceDao.loadDelayedPerfMonitors(periodCount);
        Map<Integer, Integer> delayedMap = new HashMap<Integer, Integer>();
        Map<Integer, Set<Long>> messageIdCache = performanceService.getMessageIdCache();
        for (ScheduleMessage<?> monitor : perfMonitors) {
            for (Integer engineId : messageIdCache.keySet()) {
                if (messageIdCache.containsKey(engineId)
                        && messageIdCache.get(engineId).contains(monitor.getMonitorId())) {
                    int number = 0;
                    if (delayedMap.containsKey(engineId)) {
                        number = delayedMap.get(engineId);
                    }
                    delayedMap.put(engineId, ++number);
                    break;
                }
            }
        }

        for (EngineServer es : engines) {
            if(es.hasType(EngineServer.TYPE_PERFORMANCE)) {
                try {
                    Map<String, String> data = new HashMap<>();
                    data.put("engineId", es.getEngineId());
                    data.put("engineName", es.getName());
                    data.put("status", es.getLinkStatusStr());
                    data.put("monitors", messageIdCache.containsKey(es.getId())
                            ? String.valueOf(messageIdCache.get(es.getId()).size()) : "0");
                    if (delayedMap.containsKey(es.getId())) {
                        data.put("delayedMonitors", delayedMap.get(es.getId()).toString());
                        Double delayedPercent = Double.valueOf(data.get("delayedMonitors"))
                                / Double.valueOf(data.get("monitors"));
                        data.put("delayedPercent", String.format("%.2f", delayedPercent * 100) + "%");
                    } else {
                        data.put("delayedMonitors", "0");
                        data.put("delayedPercent", "0%");
                    }
                    list.add(data);
                } catch (Exception e) {
                    logger.debug("", e);
                }
            }
        }
        JSONObject json = new JSONObject();
        json.put("data", list);
        json.put("rowCount", list.size());
        writeDataToAjax(json);
        return NONE;
    }

    public String systemInfo() throws Exception {
        if (!hasPower()) {
            return ERROR;
        }
        StringBuilder msg = new StringBuilder();

        Map<String, String> env = System.getenv();
        msg.append("</p><p><table class='datagrid'><caption>System Environment</caption>");
        msg.append("<tr><th>Key</th><th>Value</th></tr>");
        for (String key : env.keySet()) {
            msg.append("<tr><td>").append(key).append("</td><td>").append(env.get(key)).append("</td></tr>");
        }
        msg.append("</table>");

        Properties props = System.getProperties();
        msg.append("</p><p><table class='datagrid'><caption>System Properties</caption>");
        msg.append("<tr><th>Key</th><th>Value</th></tr>");
        for (Object key : props.keySet()) {
            msg.append("<tr><td>").append(key).append("</td><td>").append(props.get(key)).append("</td></tr>");
            // d.put(key.toString(), props.get(key).toString());
        }
        msg.append("</table>");
        msg.append("</p>");
        m = msg.toString();
        return SUCCESS;
    }

    /**
     * 主要是开发调试时使用，可以功过beanFactory获取任意bean进行值查看打印。
     * 
     * @return
     * @throws Exception
     */
    public String debug() throws Exception {
        if (!hasPower()) {
            return ERROR;
        }
        return SUCCESS;
    }

    public String beanMgr() throws Exception {
        if (!hasPower()) {
            return ERROR;
        }
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) BeanService.getInstance()
                .getBeanFactory();
        StringBuilder msg = new StringBuilder();
        List<String> beans = new ArrayList<String>();
        String[] names = beanFactory.getBeanDefinitionNames();
        for (String name : names) {
            beans.add(name);
        }
        msg.append("<p><table class='datagrid'><caption>Bean(").append(beans.size()).append(")</caption>");
        msg.append("<tr><th>BeanName</th><th>Class</th><th>Object</th></tr>");
        Collections.sort(beans);
        int index = -1;
        for (String name : beans) {
            Object obj = beanFactory.getBean(name);
            index = name.lastIndexOf('.') + 1;
            msg.append("<tr><td title='").append(name).append("'><a href='/admin/beanInfo.tv?m=").append(name)
                    .append("'>").append(name.substring(index)).append("</a></td><td>").append(obj.getClass().getName())
                    .append("</td><td>").append(obj).append("</td></tr>");
        }
        msg.append("</table></p>");
        m = msg.toString();
        return SUCCESS;
    }

    public String beanInfo() throws Exception {
        if (!hasPower()) {
            return ERROR;
        }
        boolean isAccessible;
        Object obj = BeanService.getInstance().getBeanFactory().getBean(m);
        Class<?> clazz = obj.getClass();
        boolean isProxy = AopUtils.isAopProxy(obj);
        StringBuilder msg = new StringBuilder();
        msg.append("<p><table class='datagrid'><caption>").append(clazz);
        if (isProxy) {
            msg.append("(").append(AopUtils.getTargetClass(obj).getName()).append(")");
        }
        msg.append("</caption>");
        msg.append("<tr><th colspan=2>Feilds</th></tr>");
        if (isProxy) {
            clazz = AopUtils.getTargetClass(obj);
        }
        Field[] fields = clazz.getDeclaredFields();
        for (Field f : fields) {
            isAccessible = f.isAccessible();
            f.setAccessible(true);
            if (isProxy) {
                msg.append("<tr><td>").append(f.getName()).append("</td><td>").append(f).append("</td></tr>");
            } else {
                msg.append("<tr><td>").append(f.getName()).append("</td><td>").append(f.get(obj)).append("</td></tr>");
            }
            f.setAccessible(isAccessible);
        }
        Method[] methods = clazz.getDeclaredMethods();
        msg.append("<tr><th colspan=2>Methods</th></tr>");
        for (Method m : methods) {
            if (m.getParameterTypes().length == 0 && !m.getReturnType().equals(Void.TYPE)) {
                isAccessible = m.isAccessible();
                m.setAccessible(true);
                msg.append("<tr><td>").append(m.getName()).append("</td><td>");
                if (isProxy) {
                    try {
                        Object r = AopUtils.invokeJoinpointUsingReflection(obj, m, new Object[0]);
                        if (r.getClass().isArray()) {
                            msg.append("[");
                            for (int i = 0; i < Array.getLength(r); i++) {
                                msg.append(Array.get(r, i)).append(",");
                            }
                            msg.append("]");
                        } else {
                            msg.append(obj);
                        }
                    } catch (Throwable e) {
                        logger.error("", e);
                    }
                } else {
                    Object r = m.invoke(obj);
                    if (r.getClass().isArray()) {
                        msg.append("[");
                        for (int i = 0; i < Array.getLength(r); i++) {
                            msg.append(Array.get(r, i)).append(",");
                        }
                        msg.append("]");
                    } else {
                        msg.append(obj);
                    }
                }
                msg.append("</td></tr>");
                m.setAccessible(isAccessible);
            }
        }
        for (Method m : methods) {
            if (m.getParameterTypes().length > 0 || m.getReturnType().equals(Void.TYPE)) {
                msg.append("<tr><td>").append(m.getName()).append("</td><td>");
                msg.append(m);
                msg.append("</td></tr>");
            }
        }
        msg.append("</table></p>");
        m = msg.toString();
        return SUCCESS;
    }

    public String runScriptlet() throws Exception {
        if (!hasPower()) {
            return ERROR;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("admin.debug invoked,beanFactory:{}", BeanService.getInstance().getBeanFactory());
        }
        if (m != null && m.trim().length() > 0) {
            if (m.startsWith("#ENGINE")) {
                engines = engineServerService.getEngineServerList();
                for (EngineServer es : engines) {
                    if (!m.startsWith("#ENGINE " + es.getId() + ";")) {
                        continue;
                    }
                    CheckFacade check = facadeFactory.getCheckFacade(es);
                    m = check.runScripts(m);
                    break;
                }
            } else {
                ScriptingContainer container = new ScriptingContainer();
                Object o = container.runScriptlet(m);
                m = o.toString();
            }
        } else {
            m = debugService.debug(BeanService.getInstance().getBeanFactory());
            m = m.replaceAll("\n", "<br\\>");
        }
        if (logger.isDebugEnabled()) {
            logger.debug("admin.debug:{}", m);
        }
        writeDataToAjax(m);
        return NONE;
    }

    public String viewLogs() throws Exception {
        if (!hasPower()) {
            return ERROR;
        }
        File root = new File("./logs");
        ds = new ArrayList<Map<String, String>>();
        for (File file : root.listFiles()) {
            Map<String, String> f = new HashMap<String, String>();
            f.put("name", file.getName());
            f.put("path", file.getPath());
            f.put("length", getFileSize(file.length()));
            f.put("l", String.valueOf(file.length()));
            ds.add(f);
        }
        try {
            Collections.sort(ds, new Comparator<Map<String, String>>() {
                @Override
                public int compare(Map<String, String> o1, Map<String, String> o2) {
                    return o1.get("name").toLowerCase().compareTo(o2.get("name").toLowerCase());
                }
            });
        } catch (Exception e1) {
            logger.debug("sort", e1);
        }
        try {
            StringBuilder data = new StringBuilder();
            if (m != null) {
                BufferedReader reader = new BufferedReader(new FileReader(m));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    line = line.replaceAll("\t", "&nbsp;&nbsp;&nbsp;&nbsp;");
                    line = line.replaceAll(" ", "&nbsp;");
                    data.append(line).append("<br>");
                }
                reader.close();
            }
            m = data.toString();
        } catch (Exception e) {
            logger.error("", e);
        }
        return SUCCESS;
    }

    public String deleteLog() throws Exception {
        if (!hasPower()) {
            return ERROR;
        }
        if (m != null) {
            File file = new File(m);
            file.delete();
            m = null;
        }
        return viewLogs();
    }

    public String downloadLog() throws Exception {
        if (!hasPower()) {
            return ERROR;
        }
        HttpServletResponse response = ServletActionContext.getResponse();
        try {
            if (m != null) {
                File file = new File(m);
                // 获得读取本地文件的输入流
                FileInputStream fin = new FileInputStream(file);
                // 设置响应的MIMI类型
                response.setContentType("application/force-download");
                response.addHeader("Content-Disposition", "attachment;filename=" + file.getName());
                // 流的方式输出文件
                byte[] buf = new byte[1024];
                int readSize = fin.read(buf);
                OutputStream os = response.getOutputStream();

                while (readSize != -1) {
                    os.write(buf, 0, readSize);
                    readSize = fin.read(buf);
                }
                os.flush();
                os.close();
                os = null;
                fin.close();
                fin = null;
                response.flushBuffer();
            }
        } catch (Exception e) {
            logger.error("", e);
        }
        return NONE;
    }

    public String listFiles() throws Exception {
        if (!hasPower()) {
            return ERROR;
        }
        if (m == null || m.trim().length() == 0) {
            m = "./";
        }
        File root = new File(m);
        if (root.isDirectory()) {
            ds = new ArrayList<Map<String, String>>();
            for (File file : root.listFiles()) {
                Map<String, String> f = new HashMap<String, String>();
                f.put("name", file.getName());
                f.put("path", file.getPath());
                f.put("length", getFileSize(file.length()));
                f.put("l", String.valueOf(file.length()));
                f.put("createDate", DateUtils.format(file.lastModified()));
                f.put("createTime", String.valueOf(file.lastModified()));
                ds.add(f);
            }
            try {
                Collections.sort(ds, new Comparator<Map<String, String>>() {
                    @Override
                    public int compare(Map<String, String> o1, Map<String, String> o2) {
                        return o2.get("createTime").compareTo(o1.get("createTime"));
                    }
                });
            } catch (Exception e1) {
                logger.debug("sort", e1);
            }
        } else {
            try {
                StringBuilder data = new StringBuilder();
                BufferedReader reader = new BufferedReader(new FileReader(root));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    line = line.replaceAll("\t", "&nbsp;&nbsp;&nbsp;&nbsp;");
                    line = line.replaceAll(" ", "&nbsp;");
                    data.append(line).append("<br>");
                }
                reader.close();
                m = data.toString();
            } catch (Exception e) {
                logger.error("", e);
            }
        }
        return SUCCESS;
    }

    public String postUpload() throws Exception {
        if (!hasPower()) {
            return ERROR;
        }
        if (m == null || m.trim().length() == 0) {
            m = "./";
        }
        if (logger.isDebugEnabled()) {
            logger.debug(m);
            logger.debug(upload.getAbsolutePath());
            logger.debug("size={}", upload.getUsableSpace());
        }
        File file = new File(m);
        if (file.exists()) {
            file.delete();
        }
        upload.renameTo(file);
        logger.debug(upload.getAbsolutePath());
        upload.delete();
        long l = file.length();
        if (l > 1048576) {
            logger.info("上传文件{}成功[{}MB({}bytes)]", file, l / 1048576, l);
        } else if (l > 1024) {
            logger.info("上传文件{}成功[{}KB({}bytes)]", file, l / 1024, l);
        } else {
            logger.info("上传文件{}成功[{}bytes]", file, l);
        }
        logger.info(file.getAbsolutePath());
        m = file.getParent();
        logger.info(m);
        return listFiles();
    }

    public String deleteFile() throws Exception {
        if (!hasPower()) {
            return ERROR;
        }
        if (m != null) {
            File file = new File(m);
            file.delete();
            m = null;
        }
        return listFiles();
    }

    public String downloadFile() throws Exception {
        if (!hasPower()) {
            return ERROR;
        }
        HttpServletResponse response = ServletActionContext.getResponse();
        try {
            if (m != null) {
                File file = new File(m);
                // 获得读取本地文件的输入流
                FileInputStream fin = new FileInputStream(file);
                // 设置响应的MIMI类型
                response.setContentType("application/force-download");
                response.addHeader("Content-Disposition", "attachment;filename=" + file.getName());
                // 流的方式输出文件
                byte[] buf = new byte[1024];
                int readSize = fin.read(buf);
                OutputStream os = response.getOutputStream();

                while (readSize != -1) {
                    os.write(buf, 0, readSize);
                    readSize = fin.read(buf);
                }
                os.flush();
                os.close();
                os = null;
                fin.close();
                fin = null;
                response.flushBuffer();
            }
        } catch (Exception e) {
            logger.error("", e);
        }
        return NONE;
    }

    public String cmd() throws Exception {
        if (!hasPower()) {
            return ERROR;
        }
        if (m != null && m.trim().length() > 0) {
            RunCmd cmd = new RunCmd();
            if (!cmd.runCommand(m)) {
                m = String.format("CMD(%s)运行错误", m);
                logger.debug("Output:" + cmd.getStdout());
                logger.debug("Error put:" + cmd.getStderr());
                return SUCCESS;
            }
            m = cmd.getStdout();
            logger.info("Admin 运行CMD结果:{}", m);
            m = m.replaceAll("\n", "<br/>");
            m = m.replaceAll(" ", "&nbsp;");
            m = m.replaceAll("\t", "&nbsp;&nbsp;&nbsp;&nbsp;");
        }
        return SUCCESS;
    }

    /**
     * Add by Victor@20160715 生产线程和内存信息
     * 
     * @return
     * @throws Exception
     */
    public String jstack() throws Exception {
        if (!hasPower()) {
            return ERROR;
        }
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        String pid = runtimeMXBean.getName().substring(0, runtimeMXBean.getName().indexOf('@'));
        File file = new File(getJreExe("jstack"));
        if (file.exists()) {
            RunCmd cmd = new RunCmd();
            cmd.runCommand(getJreExe("jstack"), pid);
            file = new File("logs/stack." + DateUtils.FULL_FILENAME_FORMAT.format(new Date()) + ".txt");
            FileUtils.output(cmd.getStdout(), file);
        }
        return viewLogs();
    }

    public String jmap() throws Exception {
        if (!hasPower()) {
            return ERROR;
        }
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        String pid = runtimeMXBean.getName().substring(0, runtimeMXBean.getName().indexOf('@'));
        File file = new File(getJreExe("jmap"));
        if (file.exists()) {
            RunCmd cmd = new RunCmd();
            cmd.runCommand(getJreExe("jmap"),
                    "-dump:format=b,file=logs/heap." + DateUtils.FULL_FILENAME_FORMAT.format(new Date()) + ".bin ",
                    pid);
        }
        return viewLogs();
    }

    public String update() throws Exception {
        if (!hasPower() || !SystemConstants.isSuperMode) {
            return ERROR;
        }
        logger.info("restart...");

        if (SystemConstants.osname.startsWith("Windows")) {
            Runtime.getRuntime().exec("cmd /k bin\\update.bat");
        } else if (SystemConstants.osname.startsWith("Linux")) {
            Runtime.getRuntime().exec("./bin/update.sh");
        } else {
            // 不支持
        }
        logger.info("run restart");
        return NONE;
    }

    public String logConfig() throws Exception {
        if (!hasPower()) {
            return ERROR;
        }
        StringBuilder data = new StringBuilder();
        ch.qos.logback.classic.LoggerContext loggerContext = (ch.qos.logback.classic.LoggerContext) LoggerFactory
                .getILoggerFactory();
        List<ch.qos.logback.classic.Logger> logs = loggerContext.getLoggerList();
        data.append("logs.size=").append(logs.size()).append("<br>");

        for (ch.qos.logback.classic.Logger l : logs) {
            for (Iterator<Appender<ILoggingEvent>> itr = l.iteratorForAppenders(); itr.hasNext();) {
                Appender<ILoggingEvent> appender = itr.next();
                // data.append("====[").append(l.getEffectiveLevel()).append("]").append(l.getName()).append(">>")
                // .append(appender.getName()).append("<br>");
                print(appender);
            }
        }
        Appender<ILoggingEvent> mybatis = getAppender("mybatis");
        for (ch.qos.logback.classic.Logger l : logs) {
            if (l.getName().endsWith("domain") && l.getAppender("mybatis") == null) {
                l.addAppender(mybatis);
                l.setAdditive(false);
                logger.error(l.getName());
            }
            // data.append("[").append(l.getEffectiveLevel()).append("]").append(l.getName()).append("-")
            // .append(l.isAdditive()).append("<br>");
        }
        m = data.toString();
        return SUCCESS;
    }

    public String showLog() throws Exception {
        if (!hasPower()) {
            return ERROR;
        }
        if (m == null) {
            return ERROR;
        }
        ch.qos.logback.classic.LoggerContext loggerContext = (ch.qos.logback.classic.LoggerContext) LoggerFactory
                .getILoggerFactory();
        List<ch.qos.logback.classic.Logger> logs = loggerContext.getLoggerList();
        List<Appender<ILoggingEvent>> appenders = new ArrayList<Appender<ILoggingEvent>>();
        for (ch.qos.logback.classic.Logger l : logs) {
            for (Iterator<Appender<ILoggingEvent>> itr = l.iteratorForAppenders(); itr.hasNext();) {
                Appender<ILoggingEvent> appender = itr.next();
                if (!appenders.contains(appender)) {
                    appenders.add(appender);
                }
            }
        }
        StringBuilder data = new StringBuilder();
        ch.qos.logback.classic.Logger l = loggerContext.getLogger(m);
        data.append(l.getName()).append("<br>");
        data.append(l.getEffectiveLevel()).append("<br>");
        for (Iterator<Appender<ILoggingEvent>> itr = l.iteratorForAppenders(); itr.hasNext();) {
            Appender<ILoggingEvent> appender = itr.next();
            data.append(">>").append(appender.getName()).append("<br>");
            appenders.remove(appender);
        }
        for (Appender<ILoggingEvent> appender : appenders) {
            data.append("==============").append(appender.getName()).append("<br>");
        }
        m = data.toString();
        return SUCCESS;
    }

    public String loadAllLogs() throws Exception {
        if (!hasPower()) {
            return NONE;
        }
        HttpResponseUtils.setXMLResponse(ServletActionContext.getResponse());
        ch.qos.logback.classic.LoggerContext loggerContext = (ch.qos.logback.classic.LoggerContext) LoggerFactory
                .getILoggerFactory();
        List<ch.qos.logback.classic.Logger> logs = loggerContext.getLoggerList();
        List<LoggerTreeEntry> entry = new ArrayList<LoggerTreeEntry>();
        for (ch.qos.logback.classic.Logger l : logs) {
            entry.add(new LoggerTreeEntry(l));
        }
        DefaultDhtmlxHandler handler = new DefaultDhtmlxHandler(entry) {
            @Override
            public Element buildElement(Object obj) {
                LoggerTreeEntry item = (LoggerTreeEntry) obj;
                Element el = new DefaultElement("item");
                el.addAttribute("id", item.getId());
                el.addAttribute("text", item.getText());
                el.addAttribute("tooltip", item.getText());
                el.addAttribute("open", "0");
                // el.addAttribute("im0", "role.gif");
                // el.addAttribute("im1", "role.gif");
                // el.addAttribute("im2", "role.gif");
                return el;
            }
        };
        DhtmlxTreeOutputter.output(handler, ServletActionContext.getResponse().getOutputStream());
        return NONE;
    }

    /**
     * @param appender
     */
    private void print(Appender<ILoggingEvent> appender) {
        if (appender instanceof RollingFileAppender) {
            StringBuilder data = new StringBuilder();
            RollingFileAppender<ILoggingEvent> rolling = (RollingFileAppender<ILoggingEvent>) appender;
            data.append("\nname:").append(rolling.getName()).append("\n");
            data.append("RollingFileAppender:").append(rolling).append("\n");
            data.append("getFile:").append(rolling.getFile()).append("\n");
            data.append("isAppend:").append(rolling.isAppend()).append("\n");
            data.append("isPrudent:").append(rolling.isPrudent()).append("\n");
            data.append("isStarted:").append(rolling.isStarted()).append("\n");
            data.append("rawFileProperty:").append(rolling.rawFileProperty()).append("\n");
            data.append("getContext:").append(rolling.getContext()).append("\n");
            data.append("getCopyOfAttachedFiltersList:").append(rolling.getCopyOfAttachedFiltersList()).append("\n");
            data.append("getEncoder:").append(rolling.getEncoder()).append("\n");
            data.append("getRollingPolicy:").append(rolling.getRollingPolicy()).append("\n");
            data.append("getStatusManager:").append(rolling.getStatusManager()).append("\n");
            data.append("getTriggeringPolicy:").append(rolling.getTriggeringPolicy()).append("\n");
            if (rolling.getRollingPolicy() instanceof TimeBasedRollingPolicy) {
                @SuppressWarnings("unchecked")
                TimeBasedRollingPolicy<ILoggingEvent> policy = (TimeBasedRollingPolicy<ILoggingEvent>) rolling
                        .getRollingPolicy();
                data.append("getRollingPolicy:").append("policy:").append(policy).append("\n");
                data.append("getRollingPolicy.").append("getActiveFileName:").append(policy.getActiveFileName())
                        .append("\n");
                data.append("getRollingPolicy.").append("getFileNamePattern:").append(policy.getFileNamePattern())
                        .append("\n");
                data.append("getRollingPolicy.").append("getMaxHistory:").append(policy.getMaxHistory()).append("\n");
                data.append("getRollingPolicy.").append("getParentsRawFileProperty:")
                        .append(policy.getParentsRawFileProperty()).append("\n");
                data.append("getRollingPolicy.").append("isCleanHistoryOnStart:").append(policy.isCleanHistoryOnStart())
                        .append("\n");
                data.append("getRollingPolicy.").append("isParentPrudent:").append(policy.isParentPrudent())
                        .append("\n");
                data.append("getRollingPolicy.").append("isStarted:").append(policy.isStarted()).append("\n");
                data.append("getRollingPolicy.").append("getCompressionMode:").append(policy.getCompressionMode())
                        .append("\n");
                data.append("getRollingPolicy.").append("getContext:").append(policy.getContext()).append("\n");
                data.append("getRollingPolicy.").append("getStatusManager:").append(policy.getStatusManager())
                        .append("\n");
                data.append("getRollingPolicy.").append("getTimeBasedFileNamingAndTriggeringPolicy:")
                        .append(policy.getTimeBasedFileNamingAndTriggeringPolicy()).append("\n");
                if (policy.getTimeBasedFileNamingAndTriggeringPolicy() instanceof SizeAndTimeBasedFNATP) {
                    SizeAndTimeBasedFNATP<ILoggingEvent> sizeAndTime = (SizeAndTimeBasedFNATP<ILoggingEvent>) policy
                            .getTimeBasedFileNamingAndTriggeringPolicy();
                    rolling.setTriggeringPolicy(policy.getTimeBasedFileNamingAndTriggeringPolicy());
                    data.append("getTriggeringPolicy.").append("sizeAndTime:").append(sizeAndTime).append("\n");
                    data.append("getTriggeringPolicy.").append("getCurrentPeriodsFileNameWithoutCompressionSuffix:")
                            .append(sizeAndTime.getCurrentPeriodsFileNameWithoutCompressionSuffix()).append("\n");
                    data.append("getTriggeringPolicy.").append("getCurrentTime:").append(sizeAndTime.getCurrentTime())
                            .append("\n");
                    data.append("getTriggeringPolicy.").append("getElapsedPeriodsFileName:")
                            .append(sizeAndTime.getElapsedPeriodsFileName()).append("\n");
                    data.append("getTriggeringPolicy.").append("getMaxFileSize:").append(sizeAndTime.getMaxFileSize())
                            .append("\n");
                    data.append("getTriggeringPolicy.").append("isStarted:").append(sizeAndTime.isStarted())
                            .append("\n");
                    data.append("getTriggeringPolicy.").append("getArchiveRemover:")
                            .append(sizeAndTime.getArchiveRemover()).append("\n");
                    data.append("getTriggeringPolicy.").append("getContext:").append(sizeAndTime.getContext())
                            .append("\n");
                    data.append("getTriggeringPolicy.").append("getStatusManager:")
                            .append(sizeAndTime.getStatusManager()).append("\n");
                }
            }
            logger.debug(data.toString());
        }
    }

    public String showSql() throws Exception {
        if (!hasPower()) {
            return ERROR;
        }
        return SUCCESS;
    }

    public String executeSql() throws Exception {
        if (!hasPower()) {
            throw new Exception("没有权限执行");
        }
        VersionDao versionDao = (VersionDao) BeanService.getInstance().getBean("versionDao");
        if (m == null || m.trim().length() == 0) {
            m = "无SQL语句执行。";
            writeDataToAjax(m);
            return NONE;
        }
        versionDao.execute(m);
        m += "更新成功。";
        writeDataToAjax(m);
        return NONE;
    }

    public String querySql() throws Exception {
        if (!hasPower()) {
            throw new Exception("没有权限执行");
        }
        VersionDao versionDao = (VersionDao) BeanService.getInstance().getBean("versionDao");
        if (m == null || m.trim().length() == 0) {
            m = "无SQL语句查询。";
            writeDataToAjax(m);
            return NONE;
        }
        QueryResult r = versionDao.query(m);
        logger.debug("query result:\n{}", m);
        StringBuilder result = new StringBuilder();
        result.append("<div>").append(r.getQuerySql()).append("</div>");
        result.append("<table class='datagrid'>");
        result.append("<tr>");
        for (int i = 0; i < r.getColumnCount(); i++) {
            result.append("<th>").append(r.getColumnNames().get(i)).append("</th>");
        }
        result.append("</tr>");
        for (int c = 0; c < r.getDatas().size(); c++) {
            Map<String, String> data = r.getDatas().get(c);
            result.append("<tr>");
            for (int i = 0; i < r.getColumnCount(); i++) {
                result.append("<td>").append(data.get(r.getColumnNames().get(i))).append("</td>");
            }
            result.append("</tr>");
        }
        result.append("</table>");
        m = result.toString();
        writeDataToAjax(m);
        return NONE;
    }

    public String executeHSql() throws Exception {
        return hsql("execute", m);
    }

    public String queryHSql() throws Exception {
        return hsql("query", m);
    }

    private String hsql(String action, String sql) throws Exception {
        if (!hasPower()) {
            throw new Exception("没有权限执行");
        }
        if (sql != null && sql.trim().length() > 0) {
            if (sql.startsWith("#ENGINE")) {
                engines = engineServerService.getEngineServerList();
                for (EngineServer es : engines) {
                    if (!sql.startsWith("#ENGINE " + es.getId() + ";")) {
                        continue;
                    }
                    CheckFacade check = facadeFactory.getCheckFacade(es);
                    m = check.executeHsql(action, sql.substring(11));
                    break;
                }
            }
        } else {
            return SUCCESS;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("admin.hsql.{}:{}", action, sql);
        }
        writeDataToAjax(m);
        return NONE;
    }

    public String debugLogs() {
        File src = new File("./lib/dl");
        File des = new File("./lib/logback-test.xml");
        src.renameTo(des);
        return NONE;
    }

    public String clearDebugLogs() {
        File src = new File("./lib/logback-test.xml");
        File des = new File("./lib/dl");
        src.renameTo(des);
        return NONE;
    }

    @SuppressWarnings({ "rawtypes" })
    public String viewJobs() throws Exception {
        if (!hasPower()) {
            return ERROR;
        }
        Map<String, AtomicInteger> counts = new HashMap<String, AtomicInteger>();
        List<Map<String, String>> jobs = schedulerService.getJobs();
        StringBuilder msg = new StringBuilder();
        for (Map<String, String> job : jobs) {
            AtomicInteger count = counts.get(job.get("group"));
            if (count == null) {
                count = new AtomicInteger();
                counts.put(job.get("group"), count);
            }
            count.getAndIncrement();
        }

        PerfExecutorService service = BeanService.getInstance().getBeanFactory().getBean(PerfExecutorService.class);
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = service.getScheduledThreadPoolExecutor();
        ConcurrentHashMap<ScheduleMessage<OperClass>, PerfScheduledFuture> data = service
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
        Collections.sort(smList, new Comparator<ScheduleMessage>() {
            @Override
            public int compare(ScheduleMessage o1, ScheduleMessage o2) {
                int c = o1.getCategory().compareTo(o2.getCategory());
                if (c == 0) {
                    return o1.getIdentifyKey() > o2.getIdentifyKey() ? 1
                            : o1.getIdentifyKey() == o2.getIdentifyKey() ? 0 : -1;
                } else {
                    return c;
                }
            }
        });

        Map<String, Map<String, String>> dbCounts = performanceDao.getCategoryCounts();
        List<String> names = new ArrayList<String>();
        names.addAll(counts.keySet());
        for (String name : dbCounts.keySet()) {
            if (!names.contains(name)) {
                names.add(name);
            }
        }
        Collections.sort(names);
        msg.append(
                "<p><table class='datagrid'><caption>任务数统计</caption><tr><th>任务类型</th><th>后台任务数</th><th>数据库任务数</th></tr>");
        for (String name : names) {
            msg.append("<tr><td>").append(name).append("</td><td>")
                    .append(counts.get(name) == null ? "-" : counts.get(name).get()).append("</td><td>")
                    .append(dbCounts.get(name) == null ? '-' : dbCounts.get(name).get("counts")).append("</td></tr>");
        }
        msg.append("</table>");
        msg.append("</p>");

        msg.append("<p><table class='datagrid'><caption>Quartz任务数:").append(jobs.size()).append(
                "</caption><tr><th>name</th><th>group</th><th>PreviousFireTime</th><th>NextFireTime</th><th>Job</th></tr>");
        for (Map<String, String> job : jobs) {
            msg.append("<tr><td>").append(job.get("name")).append("</td><td>").append(job.get("group"))
                    .append("</td><td>").append(job.get("PreviousFireTime")).append("</td><td>")
                    .append(job.get("NextFireTime")).append("</td><td>").append(job.get("job")).append("</td></tr>");
        }
        msg.append("</table></p>");

        msg.append("<p><table class='datagrid'><caption>性能采集任务数:").append(data.size()).append(",ActiveCount:")
                .append(scheduledThreadPoolExecutor.getActiveCount())
                .append("</caption><tr><th>Category</th><th>Scheduler</th><th>IdentifyKey</th><th>PreviousFireTime</th><th>NextFireTime</th></tr>");
        for (ScheduleMessage<OperClass> sm : smList) {
            String previousFireTime = "-";
            String nextFireTime = "-";
            if (sm.getDomain().getPreviousFireTime() != null) {
                previousFireTime = DateUtils.FULL_FORMAT.format(new Date(sm.getDomain().getPreviousFireTime())) + "-"
                        + DateUtils
                                .getTimeDesInObscure(System.currentTimeMillis() - sm.getDomain().getPreviousFireTime());
                nextFireTime = DateUtils.FULL_FORMAT
                        .format(new Date(sm.getDomain().getPreviousFireTime() + sm.getPeriod())) + "-"
                        + DateUtils.getTimeDesInObscure(
                                System.currentTimeMillis() - sm.getDomain().getPreviousFireTime() - sm.getPeriod());
            }
            msg.append("<tr><td>").append(sm.getCategory()).append("</td><td>").append(sm.getDomain().getPerfService())
                    .append("</td><td>").append(sm.getSnmpParam().getIpAddress()).append("</td><td>")
                    .append(previousFireTime).append("</td><td>").append(nextFireTime).append("</td></tr>");
        }
        msg.append("</table>");
        msg.append("</p>");
        m = msg.toString();
        return SUCCESS;
    }

    public String getInstallInfo() throws Exception {
        if (!hasPower()) {
            return ERROR;
        }
        if (!System.getProperty("os.name").startsWith("Windows")) {
            m = "<p>只支持Windows安装.</p>";
            return SUCCESS;
        }
        StringBuilder result = new StringBuilder();
        result.append("<table class='datagrid'>");
        result.append("<caption>安装信息</caption>");
        result.append("<tr><th>Name</th><th>Value</th></tr>");
        Map<String, String> datas = WR.readStringValues(WR.HKEY_CURRENT_USER, "Software\\TopVision\\NM3000");
        if (datas != null && !datas.isEmpty()) {
            for (String name : datas.keySet()) {
                result.append("<tr><td>").append(name).append("</td><td>").append(datas.get(name)).append("</td></tr>");
            }
        }
        result.append("</table>");

        List<String> keys = WR.readStringSubKeys(WR.HKEY_CURRENT_USER, "Software\\TopVision\\NM3000");
        if (keys != null) {
            for (String key : keys) {
                result.append("<table class='datagrid'>");
                result.append("<caption>").append(key).append("更新信息</caption>");
                result.append("<tr><th>Name</th><th>Value</th></tr>");
                datas = WR.readStringValues(WR.HKEY_CURRENT_USER, "Software\\TopVision\\NM3000\\" + key);
                if (datas != null && !datas.isEmpty()) {
                    for (String name : datas.keySet()) {
                        result.append("<tr><td>").append(name).append("</td><td>").append(datas.get(name))
                                .append("</td></tr>");
                    }
                }
                result.append("</table>");
            }
        }
        m = result.toString();
        return SUCCESS;
    }

    public String change2Development() throws Exception {
        if (!hasPower()) {
            return ERROR;
        }
        SystemConstants.isDevelopment = !SystemConstants.isDevelopment;
        return systemStatus();
    }

    public String clearPerformanceQueue() throws Exception {
        if (!hasPower()) {
            return ERROR;
        }
        performanceStatistics.getPerformanceQueue().clear();
        performanceStatistics.getRealtimePerformanceQueue().clear();
        return systemStatus();
    }

    public String showReportManagement() {
        return SUCCESS;
    }

    public String sendTrap() throws Exception {
        if (!hasPower()) {
            return ERROR;
        }
        TrapService trapService = BeanService.getInstance().getBeanFactory().getBean(TrapService.class);
        Trap trap = new Trap();
        trap.setAddress("127.0.0.1");
        trap.setPort(162);
        trap.addVariableBinding("1.1", "Victor");
        trapService.sendTrap(trap);
        return SUCCESS;
    }

    private boolean hasPower() {
        if (SystemConstants.isDevelopment) {
            return true;
        }
        UserContext uc = (UserContext) getSession().get(UserContext.KEY);
        return uc != null && uc.getUser().getUserName().equals("superadmin");
    }

    private String getFileSize(long size) {
        if (size < 1024) {
            return String.format("%dB", size);
        } else if (size < 1048576) {
            return String.format("%dKB", size / 1024);
        } else if (size < 1073741824) {
            return String.format("%dMB", size / 1048576);
        } else {
            return String.format("%dGB", size / 1073741824);
        }
    }

    public String loadEngineList() {
        engines = engineServerService.getEngineServerList();
        writeDataToAjax(engines);
        return NONE;
    }

    public String queryEngineExecutorThread() {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("engineId", engineId);
        queryMap.put("startTime", st);
        queryMap.put("endTime", et);
        List<ExecutorThreadSnap> snaps = performanceDao.queryEngineThreadStatistic(queryMap);
        // 转成activeCount、poolSize、completeedTaskCount等几条线
        int size = snaps.size();
        long[][] activeCountList = new long[size][2];
        long[][] poolSizeList = new long[size][2];
        long[][] completeedTaskCountList = new long[size][2];
        int index = 0;
        for (ExecutorThreadSnap snap : snaps) {
            long time = snap.getCollectTime().getTime();

            long[] activeCount = new long[] { time, (long) (snap.getActiveCount()) };
            long[] poolSize = new long[] { time, (long) (snap.getPoolSize()) };
            long[] completeedTaskCount = new long[] { time, (long) (snap.getCompletedTaskCount()) };

            activeCountList[index] = activeCount;
            poolSizeList[index] = poolSize;
            completeedTaskCountList[index] = completeedTaskCount;
            index++;
        }

        JSONObject json = new JSONObject();
        json.put("activeCount", activeCountList);
        json.put("poolSize", poolSizeList);
        json.put("completeedTaskCount", completeedTaskCountList);
        json.put("st", st);
        json.put("et", et);
        writeDataToAjax(json);
        return NONE;
    }

    private Appender<ILoggingEvent> getAppender(String name) {
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        RollingFileAppender<ILoggingEvent> rolling = new RollingFileAppender<ILoggingEvent>();
        rolling.setContext(lc);
        rolling.setAppend(true);
        rolling.setName(name);
        rolling.setFile(String.format("logs/%s.logback", name));
        TimeBasedRollingPolicy<ILoggingEvent> policy = new TimeBasedRollingPolicy<ILoggingEvent>();
        policy.setFileNamePattern(String.format("logs/%s-%s.logback", name, "%d{yyyy-MM-dd}.%i"));
        policy.setMaxHistory(10);
        policy.setContext(lc);
        policy.setParent(rolling);
        policy.start();
        SizeAndTimeBasedFNATP<ILoggingEvent> timeBasedTriggering = new SizeAndTimeBasedFNATP<ILoggingEvent>();
        timeBasedTriggering.setMaxFileSize("10MB");
        timeBasedTriggering.setContext(lc);
        timeBasedTriggering.setTimeBasedRollingPolicy(policy);
        timeBasedTriggering.start();
        policy.setTimeBasedFileNamingAndTriggeringPolicy(timeBasedTriggering);
        rolling.setRollingPolicy(policy);
        rolling.setTriggeringPolicy(timeBasedTriggering);
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(lc);
        encoder.setImmediateFlush(true);
        encoder.setPattern("%date %level [%thread] %logger{10} [%file:%line] %msg%n");
        encoder.start();
        rolling.setEncoder(encoder);
        rolling.start();
        return rolling;
    }

    public String showExecutorThreadSnap() {
        return SUCCESS;
    }

    public String showDelayedPerfMonitors() {
        return SUCCESS;
    }

    public String loadDelayedPerfMonitors() {
        List<ScheduleMessage<?>> perfMonitors = performanceDao.loadDelayedPerfMonitors(periodCount);
        Map<Integer, Set<Long>> messageIdCache = performanceService.getMessageIdCache();
        if (engineId != null) {
            // 找到分配到对应engine的monitorId
            Set<Long> monitorIds = messageIdCache.get(engineId);
            if (monitorIds != null && monitorIds.size() != 0) {
                for (int i = perfMonitors.size() - 1; i >= 0; i--) {
                    if (!monitorIds.contains(perfMonitors.get(i).getMonitorId())) {
                        perfMonitors.remove(i);
                    }
                }
            }
        }

        // 需要知道perfmonitor是在哪个engine下
        engines = engineServerService.getEngineServerList();
        Map<Integer, EngineServer> engineMap = new HashMap<Integer, EngineServer>();
        for (EngineServer engine : engines) {
            engineMap.put(engine.getId(), engine);
        }
        for (ScheduleMessage<?> monitor : perfMonitors) {
            for (Integer engineId : messageIdCache.keySet()) {
                if (messageIdCache.get(engineId).contains(monitor.getMonitorId())) {
                    monitor.setEngineId(engineId);
                    monitor.setEngineName(engineMap.get(engineId).getName());
                    break;
                }
            }
        }
        // 如果只需要某一个engine的，进行过滤
        JSONObject json = new JSONObject();
        json.put("data", perfMonitors);
        json.put("rowCount", perfMonitors.size());
        writeDataToAjax(json);
        return NONE;
    }

    public String showEngineHealth() {
        return SUCCESS;
    }

    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }

    public Map<String, String> getD() {
        return d;
    }

    public void setD(Map<String, String> d) {
        this.d = d;
    }

    public List<Map<String, String>> getDs() {
        return ds;
    }

    public void setDs(List<Map<String, String>> ds) {
        this.ds = ds;
    }

    public DebugService getDebugService() {
        return debugService;
    }

    public void setDebugService(DebugService debugService) {
        this.debugService = debugService;
    }

    /**
     * @return the datas
     */
    public List<Map<String, String>> getDatas() {
        return datas;
    }

    /**
     * @param datas
     *            the datas to set
     */
    public void setDatas(List<Map<String, String>> datas) {
        this.datas = datas;
    }

    /**
     * @return the serverDataSource
     */
    public JSONObject getServerDataSource() {
        return serverDataSource;
    }

    /**
     * @param serverDataSource
     *            the serverDataSource to set
     */
    public void setServerDataSource(JSONObject serverDataSource) {
        this.serverDataSource = serverDataSource;
    }

    /**
     * @return the engineDataSource
     */
    public JSONObject getEngineDataSource() {
        return engineDataSource;
    }

    /**
     * @param engineDataSource
     *            the engineDataSource to set
     */
    public void setEngineDataSource(JSONObject engineDataSource) {
        this.engineDataSource = engineDataSource;
    }

    /**
     * @return the dataSourceInfo
     */
    public JSONObject getDataSourceInfo() {
        return dataSourceInfo;
    }

    /**
     * @param dataSourceInfo
     *            the dataSourceInfo to set
     */
    public void setDataSourceInfo(JSONObject dataSourceInfo) {
        this.dataSourceInfo = dataSourceInfo;
    }

    public Timestamp getSt() {
        return st;
    }

    public void setSt(Timestamp st) {
        this.st = st;
    }

    public Timestamp getEt() {
        return et;
    }

    public void setEt(Timestamp et) {
        this.et = et;
    }

    public String getSampleInterval() {
        return sampleInterval;
    }

    public void setSampleInterval(String sampleInterval) {
        this.sampleInterval = sampleInterval;
    }

    public String getFlowCollectType() {
        return flowCollectType;
    }

    public void setFlowCollectType(String flowCollectType) {
        this.flowCollectType = flowCollectType;
    }

    public List<EngineServer> getEngines() {
        return engines;
    }

    public void setEngines(List<EngineServer> engines) {
        this.engines = engines;
    }

    /**
     * 开启性能采集
     * 
     * @return
     */
    public String showOpenPerfCollect() {
        return SUCCESS;
    }

    public String clearPortCaches() throws Exception {
        if (!hasPower()) {
            return ERROR;
        }
        portService.getPortCaches().clear();
        return systemStatus();
    }

    public String clearEntityCaches() throws Exception {
        if (!hasPower()) {
            return ERROR;
        }
        entityService.getEntityCaches().clear();
        return systemStatus();
    }

    public String clearSnmpParamCaches() throws Exception {
        if (!hasPower()) {
            return ERROR;
        }
        entityService.getSnmpParamCaches().clear();
        return systemStatus();
    }
    
    public String clearSystemPreferencesCaches() throws Exception{
        if (!hasPower()) {
            return ERROR;
        }
        systemPreferencesService.clearCache();
        return systemStatus();
    }

    public String clearPerfThresholdCaches() throws Exception {
        if (!hasPower()) {
            return ERROR;
        }
        perfThresholdService.getTemplateTargetRuleCache().clear();
        perfThresholdService.getEntityTemplateCache().clear();
        return systemStatus();
    }

    private String getJreExe(String name) {
        StringBuilder path = new StringBuilder();
        if (SystemConstants.isDevelopment) {
            path.append("..").append(File.separator).append("topvision-installer").append(File.separator)
                    .append("jre_amd64");
        } else {
            path.append("jre");
        }
        path.append(File.separator).append("bin").append(File.separator).append(name);
        if (System.getProperty("os.name").startsWith("Windows")) {
            path.append(".exe");
        }
        return path.toString();
    }

    /**
     * @return the upload
     */
    public File getUpload() {
        return upload;
    }

    /**
     * @param upload
     *            the upload to set
     */
    public void setUpload(File upload) {
        this.upload = upload;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Long getSlotNo() {
        return slotNo;
    }

    public void setSlotNo(Long slotNo) {
        this.slotNo = slotNo;
    }

    public Long getPortNo() {
        return portNo;
    }

    public void setPortNo(Long portNo) {
        this.portNo = portNo;
    }

    public Long getOnuNo() {
        return onuNo;
    }

    public void setOnuNo(Long onuNo) {
        this.onuNo = onuNo;
    }

    public Long getOnuPortNo() {
        return onuPortNo;
    }

    public void setOnuPortNo(Long onuPortNo) {
        this.onuPortNo = onuPortNo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Boolean getFrontEndLogSwitch() {
        return frontEndLogSwitch;
    }

    public void setFrontEndLogSwitch(Boolean frontEndLogSwitch) {
        this.frontEndLogSwitch = frontEndLogSwitch;
    }


    public Integer getEngineId() {
        return engineId;
    }

    public void setEngineId(Integer engineId) {
        this.engineId = engineId;
    }

    public Integer getPeriodCount() {
        return periodCount;
    }

    public void setPeriodCount(Integer periodCount) {
        this.periodCount = periodCount;
    }

}
