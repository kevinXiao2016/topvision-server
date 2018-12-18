/***********************************************************************
 * $Id: MonitorJob.java,v 1.1 Apr 6, 2009 2:50:36 PM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.job;

import static org.quartz.SimpleScheduleBuilder.repeatSecondlyForever;
import static org.quartz.TriggerBuilder.newTrigger;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.commons.collections.CollectionUtils;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.topvision.ems.facade.domain.Formula;
import com.topvision.ems.fault.domain.Event;
import com.topvision.ems.fault.service.EventSender;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.network.service.OnlineService;
import com.topvision.ems.performance.domain.Monitor;
import com.topvision.ems.performance.domain.Threshold;
import com.topvision.ems.performance.message.MonitorEvent;
import com.topvision.ems.performance.message.MonitorListener;
import com.topvision.ems.performance.service.MonitorService;
import com.topvision.exception.service.MonitorCreateException;
import com.topvision.exception.service.NotSuchServiceException;
import com.topvision.framework.exception.ResourceNotFoundException;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.message.service.MessageService;

/**
 * @Create Date Apr 6, 2009 2:50:36 PM
 * 
 * @author kelers
 * 
 * @modify by Rod
 */
@PersistJobDataAfterExecution
public abstract class MonitorJob implements Job {
    protected static Logger logger = LoggerFactory.getLogger(MonitorJob.class);
    protected FacadeFactory facadeFactory = null;
    protected Monitor monitor = null;
    protected OnlineService onlineService = null;
    protected MonitorService monitorService = null;
    protected JobDataMap jobDataMap = null;
    protected boolean previousAvailable;
    protected boolean previousHealthy;
    protected MessageService messageService;
    protected EntityService entityService;

    /**
     * 字符型
     * 
     * @param ts
     * @param source
     * @param value
     * @param alertType
     * @return
     */
    protected boolean charThreshold(List<Threshold> ts, Properties props, int alertType) {
        if (CollectionUtils.isEmpty(ts)) {
            return false;
        }
        boolean result = false;
        for (Threshold t : ts) {
            if (t == null || t.getOperator1() < 6 || t.getOperator1() >= 11) {
                continue;
            }
            switch (t.getOperator1()) {
            // 6:包含
            case 6:
                if (props.getProperty("value").indexOf(t.getThreshold1()) != -1) {
                    result = sendEvent(alertType, props.getProperty("source"), t, props) || result;
                }
                break;
            // 7:不包含
            case 7:
                if (props.getProperty("value").indexOf(t.getThreshold1()) == -1) {
                    result = sendEvent(alertType, props.getProperty("source"), t, props) || result;
                }
                break;
            // 8:等于
            case 8:
                if (props.getProperty("value").equalsIgnoreCase(t.getThreshold1())) {
                    result = sendEvent(alertType, props.getProperty("source"), t, props) || result;
                }
                break;
            // 9:不等于
            case 9:
                if (!props.getProperty("value").equalsIgnoreCase(t.getThreshold1())) {
                    result = sendEvent(alertType, props.getProperty("source"), t, props) || result;
                }
                break;
            // 10:起始于
            case 10:
                if (props.getProperty("value").startsWith(t.getThreshold1())) {
                    result = sendEvent(alertType, props.getProperty("source"), t, props) || result;
                }
                break;
            // 11:终止于
            case 11:
                if (props.getProperty("value").endsWith(t.getThreshold1())) {
                    result = sendEvent(alertType, props.getProperty("source"), t, props) || result;
                }
                break;
            }
        }
        String key = new StringBuilder().append(monitor.getEntityId()).append(props.getProperty("source")).toString();
        if (result) {
            jobDataMap.put(key, false);
        } else {
            if (jobDataMap.get(key) == null || !jobDataMap.getBoolean(key)) {
                clearAlert(alertType, props.getProperty("source"), "nor");
                jobDataMap.put(key, true);
            }
        }
        return result;
    }

    /**
     * 清除告警
     * 
     * @param alertType
     * @param source
     * @return
     */
    protected boolean clearAlert(int alertType, String source, String msg) {
        Event event = EventSender.getInstance().createEvent(alertType, monitor.getIp(), source);
        event.setMonitorId(monitor.getMonitorId());
        event.setEntityId(monitor.getEntityId());
        if (msg.indexOf(getString("greater.or.eqaul")) != -1) {
            msg = msg.replace(getString("greater.or.eqaul"), getString("less"));
        } else if (msg.indexOf(getString("less.or.equal")) != -1) {
            msg = msg.replace(getString("less.or.equal"), getString("greater"));
        } else if (msg.indexOf(getString("less")) != -1) {
            msg = msg.replace(getString("less"), getString("greater.or.eqaul"));
        } else if (msg.indexOf(getString("greater")) != -1) {
            msg = msg.replace(getString("greater"), getString("less.or.equal"));
        } else if (msg.indexOf(getString("not.equal")) != -1) {
            msg = msg.replace(getString("not.equal"), getString("equals"));
        } else if (msg.indexOf(getString("equals")) != -1) {
            msg = msg.replace(getString("equals"), getString("not.equal"));
        }
        event.setMessage(msg);
        event.setClear(true);
        EventSender.getInstance().send(event);
        return true;
    }

    /**
     * @param monitorService
     * @param prop
     * @return
     * @throws MonitorCreateException
     */
    public Monitor createMonitor(MonitorService monitorService, Properties prop) throws MonitorCreateException {
        this.monitorService = monitorService;
        return createMonitor(prop);
    }

    /**
     * 
     * @param prop
     * @return
     * @throws MonitorCreateException
     */
    public Monitor createMonitor(Properties prop) throws MonitorCreateException {
        Monitor monitor = new Monitor();
        monitor.setIp(prop.getProperty("ip", "127.0.0.1"));
        monitor.setEntityId(Long.parseLong(prop.getProperty("entityId", "-1")));
        monitor.setIntervalOfNormal(Long.parseLong(prop.getProperty("intervalOfNormal", "900000")));
        // monitor.setGroupId(Long.parseLong(prop.getProperty("groupId",
        // "-1")));
        if (prop.containsKey("categoryId")) {
            // monitor.setCategoryId(prop.getProperty("categoryId"));
        }
        if (prop.containsKey("name")) {
            monitor.setName(prop.getProperty("name"));
        }
        return monitor;
    }

    /**
     * 
     * @param m
     * @return
     */
    protected Trigger createTrigger(Monitor m, JobDetail jd, boolean isNormal) {
        TriggerBuilder<SimpleTrigger> builder = newTrigger()
                .withIdentity(jd.getKey().getName(), jd.getKey().getGroup()).usingJobData(jd.getJobDataMap())
                .withSchedule(repeatSecondlyForever((int) (m.getIntervalOfNormal() / 1000)));
        if (isNormal) {
            builder.startAt(new Date(System.currentTimeMillis() + m.getIntervalOfNormal()));
        } else {
            builder.startAt(new Date(System.currentTimeMillis() + m.getIntervalAfterError()));
        }
        return builder.build();
    }

    /**
     * @param context
     */
    protected abstract void doJob(JobExecutionContext context) throws JobExecutionException;

    /**
     * (non-Javadoc)
     * 
     * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        if (logger.isDebugEnabled()) {
            StringBuilder jobInfo = new StringBuilder();
            jobInfo.append("\n=====================MonitorJob==========================");
            jobInfo.append("\nJob:").append(context.getJobDetail().getKey());
            jobInfo.append("\ngetJobRunTime:").append(context.getJobRunTime());
            jobInfo.append("\ngetRefireCount:").append(context.getRefireCount());
            jobInfo.append("\ngetFireTime:").append(context.getFireTime());
            jobInfo.append("\ngetNextFireTime:").append(context.getNextFireTime());
            jobInfo.append("\ngetPreviousFireTime:").append(context.getPreviousFireTime());
            jobInfo.append("\ngetScheduledFireTime:").append(context.getScheduledFireTime());
            jobInfo.append("\n=====================MonitorJob==========================");
            logger.debug(jobInfo.toString());
        }
        try {
            jobDataMap = context.getJobDetail().getJobDataMap();
            facadeFactory = (FacadeFactory) jobDataMap.get("facadeFactory");
            monitor = (Monitor) jobDataMap.get("monitor");
            onlineService = (OnlineService) jobDataMap.get("onlineService");
            monitorService = (MonitorService) jobDataMap.get("monitorService");
            messageService = (MessageService) jobDataMap.get("messageService");
            entityService = (EntityService) jobDataMap.get("entityService");
            previousAvailable = monitor.isAvailable();
            previousHealthy = monitor.isHealthy();
            monitor.setAvailable(true);
            // monitor.setHealthy(true);
            monitor.setLastCollectTime(new Timestamp(System.currentTimeMillis()));
            doJob(context);
            // 取消Monitor状态变化后采集时间改变的功能
            /*
             * if (!previousHealthy == monitor.isHealthy()) { sendMonitorChangedMessage(); }
             */
            if (logger.isDebugEnabled()) {
                logger.debug("Available:{}, Healthy:{}", monitor.isAvailable(), monitor.isHealthy());
            }
        } catch (Exception ex) {
            getLogger().debug(ex.getMessage(), ex);
            throw new JobExecutionException(ex);
        } finally {
            try {
                monitorService.updateHealthy(monitor);
            } catch (Exception e) {
                getLogger().debug(e.getMessage(), e);
            }
        }
    }

    /**
     * @param mfs
     * @param string
     * @return
     */
    protected Formula getByType(List<Formula> mfs, String type) {
        for (Formula mf : mfs) {
            if (mf.getType().equalsIgnoreCase(type)) {
                return mf;
            }
        }
        return null;
    }

    /**
     * @return the logger
     */
    protected Logger getLogger() {
        return logger;
    }

    private String getMessage(Threshold t, Properties props) {
        String msg = t.getAlertDesc1();
        if (msg.indexOf("${type}") != -1) {
            msg = msg.replaceAll("\\$\\{type\\}", props.getProperty("type"));
        }
        if (msg.indexOf("${value}") != -1) {
            msg = msg.replaceAll("\\$\\{value\\}", props.getProperty("strValue"));
        }
        if (msg.indexOf("${threshold}") != -1) {
            msg = msg.replaceAll("\\$\\{threshold\\}", t.getThreshold1());
        }
        if (msg.indexOf("${source}") != -1) {
            msg = msg.replaceAll("\\$\\{source\\}", props.getProperty("source"));
        }
        if (msg.indexOf("${host}") != -1) {
            msg = msg.replaceAll("\\$\\{host\\}", monitor.getIp());
        }
        if (msg.indexOf("${date}") != -1) {
            msg = msg.replaceAll("\\$\\{date\\}", DateFormat.getDateInstance().format(new Date()));
        }
        if (msg.indexOf("${datetime}") != -1) {
            msg = msg.replaceAll("\\$\\{datetime\\}", DateFormat.getDateTimeInstance().format(new Date()));
        }
        return msg;
    }

    /**
     * 
     * @param formula
     * @param condition
     * @return
     */
    protected String[] getOidFromFormula(String formula, String condition) {
        List<String> oids = new ArrayList<String>();
        if (formula != null && formula.length() > 0) {
            StringTokenizer token = new StringTokenizer(formula, "/()*+- ", false);
            while (token.hasMoreTokens()) {
                String oid = token.nextToken();
                if (oid.indexOf("1.3.6") != -1) {
                    oids.add(oid);
                }
            }
        }
        if (condition != null && condition.indexOf('=') != -1) {
            oids.add(condition.substring(0, condition.indexOf('=')));
        }
        return oids.toArray(new String[oids.size()]);
    }

    /**
     * 获取某个监视器自己的service，根据service类名获取
     * 
     * @param name
     * @return
     */
    protected Object getServiceByName(String className) {
        try {
            return null;
            // return monitorService.getService(className);
        } catch (NotSuchServiceException e) {
            return null;
        }
    }

    /**
     * 国际化
     * 
     * @param key
     * @param strings
     * @return
     */
    protected String getString(String key, String... strings) {
        try {
            return ResourceManager.getResourceManager("com.topvision.ems.network.resources").getString(key, strings);
        } catch (ResourceNotFoundException e) {
            for (int i = 0; i < strings.length; i++) {
                key = key.replaceAll("\\{" + i + "\\}", strings[i]);
            }
            return key;
        }
    }

    /**
     * 数字型
     * 
     * @param ts
     * @param source
     * @param value
     * @param sValue
     * @param alertType
     * @return
     */
    protected boolean numberThreshold(List<Threshold> ts, Properties props, double value, int alertType) {
        if (CollectionUtils.isEmpty(ts)) {
            return false;
        }
        boolean result = false;
        for (Threshold t : ts) {
            if (t == null || t.getOperator1() < 0 || t.getOperator1() >= 6) {
                continue;
            }
            switch (t.getOperator1()) {
            // 0:>
            case 0:
                if (value > Double.parseDouble(t.getThreshold1())) {
                    result = sendEvent(alertType, props.getProperty("source"), t, props) || result;
                }
                break;
            // 1:>=
            case 1:
                if (value >= Double.parseDouble(t.getThreshold1())) {
                    result = sendEvent(alertType, props.getProperty("source"), t, props) || result;
                }
                break;
            // 2:=
            case 2:
                if (value == Double.parseDouble(t.getThreshold1())) {
                    result = sendEvent(alertType, props.getProperty("source"), t, props) || result;
                }
                break;
            // 3:!=
            case 3:
                if (value != Double.parseDouble(t.getThreshold1())) {
                    result = sendEvent(alertType, props.getProperty("source"), t, props) || result;
                }
                break;
            // 4:<
            case 4:
                if (value < Double.parseDouble(t.getThreshold1())) {
                    result = sendEvent(alertType, props.getProperty("source"), t, props) || result;
                }
                break;
            // 5:<=
            case 5:
                if (value <= Double.parseDouble(t.getThreshold1())) {
                    result = sendEvent(alertType, props.getProperty("source"), t, props) || result;
                }
                break;
            }
        }
        String key = new StringBuilder().append(monitor.getEntityId()).append(props.getProperty("source"))
                .append(props.get("type")).toString();
        if (result) {
            jobDataMap.put(key, false);
        } else {
            if (jobDataMap.get(key) == null || !jobDataMap.getBoolean(key)) {
                clearAlert(alertType, props.getProperty("source"), getMessage(ts.get(0), props));
                jobDataMap.put(key, true);
            }
        }
        return result;
    }

    /**
     * 
     * @param nodeList
     * @return
     */
    protected List<Formula> parseMonitorNode(NodeList nodeList) {
        List<Formula> mfs = new ArrayList<Formula>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            Formula mf = new Formula();
            for (int j = 0; j < node.getChildNodes().getLength(); j++) {
                Node prop = node.getChildNodes().item(j);
                if (prop.getNodeName().equals("name")) {
                    mf.setName(prop.getFirstChild().getNodeValue());
                } else if (prop.getNodeName().equals("type")) {
                    mf.setType(prop.getFirstChild().getNodeValue());
                } else if (prop.getNodeName().equals("formula")) {
                    mf.setFormula(prop.getFirstChild().getNodeValue());
                } else if (prop.getNodeName().equals("condition")) {
                    mf.setCondition(prop.getFirstChild().getNodeValue());
                } else if (prop.getNodeName().equals("desc")) {
                    mf.setDesc(prop.getFirstChild().getNodeValue());
                } else if (prop.getNodeName().equals("mibs")) {
                    mf.setMibs(prop.getFirstChild().getNodeValue());
                }
            }
            mfs.add(mf);
        }
        return mfs;
    }

    /**
     * 发送告警信息
     * 
     * @param type
     * @param source
     * @param msg
     */
    protected void sendEvent(int type, String source, String msg) {
        Event event = EventSender.getInstance().createEvent(type, monitor.getIp(), source);
        event.setMonitorId(monitor.getMonitorId());
        event.setEntityId(monitor.getEntityId());
        event.setMessage(msg);
        EventSender.getInstance().send(event);
    }

    /**
     * 发送阈值告警信息
     * 
     * @param alertType
     * @param source
     * @param t
     * @param props
     * @return
     */
    protected boolean sendEvent(int alertType, String source, Threshold t, Properties props) {
        Event event = EventSender.getInstance().createEvent(alertType, monitor.getIp(), source);
        event.setMonitorId(monitor.getMonitorId());
        event.setEntityId(monitor.getEntityId());
        event.setValue(String.valueOf(t.getAlertLevel1()));
        event.setMessage(getMessage(t, props));
        EventSender.getInstance().send(event);
        monitor.setHealthy(false);
        monitor.setReason(event.getMessage());
        return true;
    }

    /**
     * 发送改变monitor的消息
     * 
     */
    @Deprecated
    protected void sendMonitorChangedMessage() {
        MonitorEvent evt = new MonitorEvent(monitor);
        evt.setActionName("monitorChanged");
        evt.setListener(MonitorListener.class);
        evt.setMonitor(monitor);
        messageService.fireMessage(evt);
    }
}
