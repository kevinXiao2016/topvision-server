/***********************************************************************
 * $Id: FlowJob.java,v 1.1 Apr 6, 2009 4:11:09 PM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.job;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.topvision.ems.facade.SnmpFacade;
import com.topvision.ems.facade.domain.FormulaBinding;
import com.topvision.ems.facade.domain.SnmpMonitorParam;
import com.topvision.ems.fault.domain.Event;
import com.topvision.ems.fault.service.EventSender;
import com.topvision.ems.network.domain.Link;
import com.topvision.ems.network.domain.Port;
import com.topvision.ems.network.domain.PortPerf;
import com.topvision.ems.network.domain.PortStatistics;
import com.topvision.ems.network.util.FlowUtil;
import com.topvision.ems.performance.domain.Threshold;
import com.topvision.framework.exception.engine.SnmpException;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.domain.SystemLog;

/**
 * @Create Date Apr 6, 2009 4:11:09 PM
 * 
 * @author kelers
 * 
 */
public class FlowJob extends MonitorJob {
    public static final String[] itemName = { "ifInOctetsRate", "ifOutOctetsRate", "ifInOctets", "ifOutOctets",
            "ifInErrorsRate", "ifOutErrorsRate", "ifInDiscardsRate", "ifOutDiscardsRate" };
    public static final String[] ifTableStatistics = { "1.3.6.1.2.1.2.2.1.1", "1.3.6.1.2.1.2.2.1.7",
            "1.3.6.1.2.1.2.2.1.8", "1.3.6.1.2.1.2.2.1.9", "1.3.6.1.2.1.2.2.1.10", "1.3.6.1.2.1.2.2.1.11",
            "1.3.6.1.2.1.2.2.1.12", "1.3.6.1.2.1.2.2.1.13", "1.3.6.1.2.1.2.2.1.14", "1.3.6.1.2.1.2.2.1.15",
            "1.3.6.1.2.1.2.2.1.16", "1.3.6.1.2.1.2.2.1.17", "1.3.6.1.2.1.2.2.1.18", "1.3.6.1.2.1.2.2.1.19",
            "1.3.6.1.2.1.2.2.1.20", "1.3.6.1.2.1.2.2.1.21" };

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.monitor.MonitorJob#doJob(org.quartz.JobExecutionContext)
     */
    @SuppressWarnings("unchecked")
    @Override
    protected void doJob(JobExecutionContext context) throws JobExecutionException {
        SnmpMonitorParam smp = new SnmpMonitorParam();
        try {
            smp.addBinding("ifTableStatistics", ifTableStatistics, FormulaBinding.TYPE_COLUMN, null, null);
            SnmpParam snmpParam = monitorService.getSnmpParamByEntity(monitor.getEntityId());
            snmpParam.setIpAddress(monitor.getIp());
            smp.setSnmpParam(snmpParam);
            smp = facadeFactory.getFacade(monitor.getIp(), SnmpFacade.class).getSnmpMonitor(smp);
            monitor.setLastCollectTime(new Timestamp(System.currentTimeMillis()));
        } catch (SnmpException ex) {
            getLogger().debug(ex.getMessage(), ex);
        }
        if (smp.getError() != null) {
            monitor.setAvailable(false);
            monitor.setHealthy(false);
            monitor.setReason(smp.getError());
            return;
        }
        List<Port> ports = new ArrayList<Port>();
        try {
            Map<Long, PortStatistics> previousPs = (Map<Long, PortStatistics>) jobDataMap.get("previousPs");
            if (previousPs == null) {
                List<Port> lists = monitorService.getPortStatusByEntityId(monitor.getEntityId());
                if (lists != null && !lists.isEmpty()) {
                    previousPs = new HashMap<Long, PortStatistics>(lists.size());
                    for (Port port : lists) {
                        PortStatistics ps = new PortStatistics();
                        ps.setIfIndex(port.getIfIndex());
                        ps.setIfName(port.getIfName());
                        ps.setIfAdminStatus(port.getIfAdminStatus());
                        ps.setIfOperStatus(port.getIfOperStatus());
                        previousPs.put(ps.getIfIndex(), ps);
                    }
                } else {
                    previousPs = new HashMap<Long, PortStatistics>();
                }
                jobDataMap.put("previousPs", previousPs);
            }
            List<PortPerf> perfs = new ArrayList<PortPerf>(previousPs.size());
            FormulaBinding binding = smp.getBinding("ifTableStatistics");
            for (int i = 0; binding != null && binding.getTable() != null && i < binding.getTable().length; i++) {
                String[] statistics = binding.getTable()[i];
                if (statistics == null || statistics.length == 0 || statistics[0] == null
                        || statistics[0].equalsIgnoreCase("null")) {
                    continue;
                }
                try {
                    PortStatistics ps = new PortStatistics();
                    ps.setIfIndex(Long.parseLong(statistics[0]));
                    ps.setIfAdminStatus(Byte.parseByte(statistics[1]));
                    ps.setIfOperStatus(Byte.parseByte(statistics[2]));
                    ps.setIfLastChange(statistics[3]);
                    ps.setIfInOctets(Long.parseLong(statistics[4]));
                    ps.setIfInUcastPkts(Long.parseLong(statistics[5]));
                    ps.setIfInNUcastPkts(Long.parseLong(statistics[6]));
                    ps.setIfInDiscards(Long.parseLong(statistics[7]));
                    ps.setIfInErrors(Long.parseLong(statistics[8]));
                    ps.setIfInUnknownProtos(Long.parseLong(statistics[9]));
                    ps.setIfOutOctets(Long.parseLong(statistics[10]));
                    ps.setIfOutUcastPkts(Long.parseLong(statistics[11]));
                    ps.setIfOutNUcastPkts(Long.parseLong(statistics[12]));
                    ps.setIfOutDiscards(Long.parseLong(statistics[13]));
                    ps.setIfOutErrors(Long.parseLong(statistics[14]));
                    ps.setIfOutQLen(Long.parseLong(statistics[15]));
                    ps.setCollectTime(monitor.getLastCollectTime());

                    PortStatistics prevPs = previousPs.get(ps.getIfIndex());
                    if (prevPs.getIfName() != null) {
                        ps.setIfName(prevPs.getIfName());
                    } else {
                        ps.setIfName("Port " + ps.getIfIndex());
                    }
                    previousPs.put(ps.getIfIndex(), ps);
                    if (prevPs == null || prevPs.getCollectTime() == null) {
                        continue;
                    }

                    PortPerf perf = FlowUtil.getPortPerf(ps, prevPs, monitor.getEntityId());
                    perfs.add(perf);
                    // 回调拓扑图client
                    // TODO 是否需要将【PortPerf】中的基本数据类型全部转换成类
                    List<Link> links = monitorService.getLinkByPort(perf.getEntityId(), perf.getIfIndex());
                    for (int id = 0; links != null && id < links.size(); id++) {
                        Link link = links.get(id);
                        double usage = Math.max(perf.getIfInOctetsRate(), perf.getIfOutOctetsRate())
                                / Math.max(1, link.getIfSpeed());
                        if (usage == Double.NaN || usage >= 1 || usage < 0) {
                            usage = 0;
                        }
                        monitorService.fireLinkFlow(link.getLinkId(), perf.getIfOctets(), perf.getIfInOctetsRate()
                                + perf.getIfOutOctetsRate(), usage);
                    }
                    Properties props = new Properties();
                    props.put("source", perf.getIfName());
                    List<Threshold> thresholds = null;
                    // 端口总字节速率
                    thresholds = monitorService.getThreshold(monitor.getMonitorId(), "pbr");
                    props.put("type", getString("type.IfOctetsRate"));
                    props.put("value", perf.getIfOctetsRate());
                    props.put("strValue", perf.getIfOctetsRateString());
                    numberThreshold(thresholds, props, perf.getIfOctetsRate(), 30011);
                    // 端口发送字节速率
                    thresholds = monitorService.getThreshold(monitor.getMonitorId(), "spbr");
                    props.put("type", getString("type.IfOutOctetsRate"));
                    props.put("value", perf.getIfOutOctetsRate());
                    props.put("strValue", perf.getIfOutOctetsRateString());
                    numberThreshold(thresholds, props, perf.getIfOutOctetsRate(), 30012);
                    // 端口接收字节速率
                    thresholds = monitorService.getThreshold(monitor.getMonitorId(), "rpbr");
                    props.put("type", getString("type.IfInOctetsRate"));
                    props.put("value", perf.getIfInOctetsRate());
                    props.put("strValue", perf.getIfInOctetsRateString());
                    numberThreshold(thresholds, props, perf.getIfInOctetsRate(), 30013);
                    // 端口总包流量
                    thresholds = monitorService.getThreshold(monitor.getMonitorId(), "ppf");
                    props.put("type", getString("type.IfOctets"));
                    props.put("value", perf.getIfOctets());
                    props.put("strValue", perf.getIfOctetsString());
                    numberThreshold(thresholds, props, perf.getIfOctets(), 30021);
                    // 端口发送包流量
                    thresholds = monitorService.getThreshold(monitor.getMonitorId(), "sppf");
                    props.put("type", getString("type.IfOutOctets"));
                    props.put("value", perf.getIfOutOctets());
                    props.put("strValue", perf.getIfOutOctetsString());
                    numberThreshold(thresholds, props, perf.getIfOutOctets(), 30022);
                    // 端口接收包流量
                    thresholds = monitorService.getThreshold(monitor.getMonitorId(), "rppf");
                    props.put("type", getString("type.IfInOctets"));
                    props.put("value", perf.getIfInOctets());
                    props.put("strValue", perf.getIfInOctetsString());
                    numberThreshold(thresholds, props, perf.getIfInOctets(), 30023);
                    // 端口发送丢包率
                    thresholds = monitorService.getThreshold(monitor.getMonitorId(), "slp");
                    props.put("type", getString("type.IfOutDiscardsRate"));
                    props.put("value", perf.getIfOutDiscardsRate());
                    props.put("strValue", perf.getIfOutDiscardsRateString());
                    numberThreshold(thresholds, props, perf.getIfOutDiscardsRate(), 30017);
                    // 端口接收丢包率
                    thresholds = monitorService.getThreshold(monitor.getMonitorId(), "rlp");
                    props.put("type", getString("type.IfInDiscardsRate"));
                    props.put("value", perf.getIfInDiscardsRate());
                    props.put("strValue", perf.getIfInDiscardsRateString());
                    numberThreshold(thresholds, props, perf.getIfInDiscardsRate(), 30018);
                    // 端口发送错包率
                    thresholds = monitorService.getThreshold(monitor.getMonitorId(), "sep");
                    props.put("type", getString("type.IfOutErrorsRate"));
                    props.put("value", perf.getIfOutErrorsRate());
                    props.put("strValue", perf.getIfOutErrorsRateString());
                    numberThreshold(thresholds, props, perf.getIfOutErrorsRate(), 30019);
                    // 端口接收错包率
                    thresholds = monitorService.getThreshold(monitor.getMonitorId(), "rep");
                    props.put("type", getString("type.IfInErrorsRate"));
                    props.put("value", perf.getIfInErrorsRate());
                    props.put("strValue", perf.getIfInErrorsRateString());
                    numberThreshold(thresholds, props, perf.getIfInErrorsRate(), 30020);
                    if (ps.getIfAdminStatus() != prevPs.getIfAdminStatus()
                            || ps.getIfOperStatus() != prevPs.getIfOperStatus()) {
                        Port port = new Port();
                        port.setEntityId(monitor.getEntityId());
                        port.setIfIndex(ps.getIfIndex());
                        port.setIfAdminStatus(ps.getIfAdminStatus());
                        port.setIfOperStatus(ps.getIfOperStatus());
                        ports.add(port);
                    }
                    if (ps.getIfAdminStatus() != prevPs.getIfAdminStatus()) {
                        Event event = EventSender.getInstance().createEvent(10010, monitor.getIp(), perf.getIfName());
                        event.setMonitorId(monitor.getMonitorId());
                        switch (ps.getIfAdminStatus()) {
                        case 1:
                            event.setClear(true);
                            event.setMessage(getString("port.admin.up.alert", monitor.getIp(), perf.getIfName()));
                            break;
                        case 2:
                            event.setMessage(getString("port.admin.down.alert", monitor.getIp(), perf.getIfName()));
                            break;
                        case 3:
                            event.setMessage(getString("port.testing.alert", monitor.getIp(), perf.getIfName()));
                            break;
                        }
                        EventSender.getInstance().send(event);
                    }
                    if (ps.getIfOperStatus() != prevPs.getIfOperStatus()) {
                        Event event = EventSender.getInstance().createEvent(10010, monitor.getIp(), perf.getIfName());
                        event.setMonitorId(monitor.getMonitorId());
                        switch (ps.getIfOperStatus()) {
                        case 1:
                            event.setClear(true);
                            event.setMessage(getString("port.up.alert", monitor.getIp(), perf.getIfName()));
                            break;
                        case 2:
                            event.setMessage(getString("port.down.alert", monitor.getIp(), perf.getIfName()));
                            break;
                        case 3:
                            event.setMessage(getString("port.testing.alert", monitor.getIp(), perf.getIfName()));
                            break;
                        case 4:
                            event.setMessage(getString("port.unknown.alert", monitor.getIp(), perf.getIfName()));
                            break;
                        case 5:
                            event.setMessage(getString("port.dormant.alert", monitor.getIp(), perf.getIfName()));
                            break;
                        case 6:
                            event.setMessage(getString("port.notPresent.alert", monitor.getIp(), perf.getIfName()));
                            break;
                        }
                        EventSender.getInstance().send(event);
                    }
                } catch (Exception ex) {
                    getLogger().debug(ex.getMessage(), ex);
                }
            }
            monitor.setReason(smp.getError());
            monitorService.insertPortPerf(new SystemLog(), perfs);
        } catch (Exception ex) {
            getLogger().debug(ex.getMessage(), ex);
            monitor.setAvailable(false);
            monitor.setHealthy(false);
            monitor.setReason(ex.toString());
        } finally {
            try {
                if (!ports.isEmpty()) {
                    monitorService.updatePortStatus(ports);
                }
            } catch (Exception e) {
                getLogger().debug(e.getMessage(), e);
            }
        }
        if (getLogger().isDebugEnabled()) {
            getLogger().debug("********************Result:" + smp + ",next monitor time:" + context.getNextFireTime());
        }
    }
}
