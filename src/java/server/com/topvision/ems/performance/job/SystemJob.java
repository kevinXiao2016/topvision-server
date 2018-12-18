/***********************************************************************
 * $Id: SystemJob.java,v 1.1 Apr 6, 2009 4:11:02 PM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.job;

import java.sql.Timestamp;
import java.text.NumberFormat;
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
import com.topvision.ems.network.domain.EntitySnap;
import com.topvision.ems.performance.domain.MonitorValue;
import com.topvision.ems.performance.domain.Threshold;
import com.topvision.framework.exception.engine.SnmpException;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @Create Date Apr 6, 2009 4:11:02 PM
 * 
 * @author kelers
 * 
 */
public class SystemJob extends MonitorJob {
    public final static int CPU_TYPE = 20002;// CPU事件类别
    public final static int MEM_TYPE = 20003;// MEM事件类别
    public final static int DISK_TYPE = 20004;// DISK事件类别
    private final static NumberFormat format = NumberFormat.getPercentInstance();
    static {
        format.setMinimumFractionDigits(2);
    }

    /**
     * @param smp
     * @param snap
     */
    private void doHostSystem(SnmpMonitorParam smp, EntitySnap snap) {
        monitor.setLastCollectTime(new Timestamp(System.currentTimeMillis()));
        try {
            List<MonitorValue> values = new ArrayList<MonitorValue>();
            FormulaBinding binding = smp.getBinding("sysUpTime");
            snap.setSysUpTime(binding.getSingle());
            binding = smp.getBinding("hrProcessorTable");
            double count = 0;
            double v;
            MonitorValue value = null;
            for (int i = 0; i < binding.getTable().length; i++) {
                value = new MonitorValue();
                value.setCollectTime(snap.getSnapTime());
                value.setEntityId(monitor.getEntityId());
                value.setItemIndex(i + 1);
                value.setItemName("cpu" + value.getItemIndex());
                v = Integer.parseInt(binding.getTable()[i][0]) / 100.0;
                value.setItemValue(v == Double.NaN ? 0 : v);
                count += value.getItemValue();
                values.add(value);
            }
            value = new MonitorValue();
            value.setCollectTime(snap.getSnapTime());
            value.setEntityId(monitor.getEntityId());
            value.setItemIndex(0);
            value.setItemName("cpu");
            v = count / binding.getTable().length;
            value.setItemValue(v == Double.NaN ? 0 : v);
            snap.setCpu(value.getItemValue());
            values.add(value);
            List<Threshold> thresholds = monitorService.getThreshold(monitor.getMonitorId(), "cpu");
            Properties props = new Properties();
            props.put("source", monitor.getIp());
            props.put("type", getString("type.Cpu"));
            props.put("value", value.getItemValue());
            props.put("strValue", format.format(value.getItemValue()));
            numberThreshold(thresholds, props, value.getItemValue(), CPU_TYPE);
            binding = smp.getBinding("hrStorageTable");
            int diskIndex = 1;
            long memUsed = 0;
            long memCap = 0;
            long diskUsed = 0;
            long diskCap = 0;
            for (String[] col : binding.getTable()) {
                if (col == null || col.length == 0 || col[1] == null) {
                    continue;
                }
                if (col[1].equals("1.3.6.1.2.1.25.2.1.2")) {
                    value = new MonitorValue();
                    value.setCollectTime(snap.getSnapTime());
                    value.setEntityId(monitor.getEntityId());
                    value.setItemIndex(0);
                    value.setItemName("pmem");
                    memUsed += (Long.parseLong(col[5]) * Long.parseLong(col[3]) / 8.0);
                    memCap += (Long.parseLong(col[4]) * Long.parseLong(col[3]) / 8.0);
                    v = 1.0 * Long.parseLong(col[5]) / Long.parseLong(col[4]);
                    value.setItemValue(v == Double.NaN ? 0 : v);
                    value.setExtValue(Double.parseDouble(col[4]));
                    value.setNote(col[2]);
                    values.add(value);
                } else if (col[1].equals("1.3.6.1.2.1.25.2.1.3")) {
                    value = new MonitorValue();
                    value.setCollectTime(snap.getSnapTime());
                    value.setEntityId(monitor.getEntityId());
                    value.setItemIndex(0);
                    value.setItemName("vmem");
                    v = 1.0 * Long.parseLong(col[5]) / Long.parseLong(col[4]);
                    value.setItemValue(v == Double.NaN ? 0 : v);
                    value.setExtValue(Double.parseDouble(col[4]));
                    memUsed += (Long.parseLong(col[5]) * Long.parseLong(col[3]) / 8.0);
                    memCap += (Long.parseLong(col[4]) * Long.parseLong(col[3]) / 8.0);
                    value.setNote(col[2]);
                    values.add(value);
                    snap.setVmem(value.getItemValue());
                } else if (col[1].equals("1.3.6.1.2.1.25.2.1.4")) {
                    value = new MonitorValue();
                    value.setCollectTime(snap.getSnapTime());
                    value.setEntityId(monitor.getEntityId());
                    value.setItemName("disk" + diskIndex);
                    value.setItemIndex(diskIndex++);
                    diskUsed += (Long.parseLong(col[5]) * Long.parseLong(col[3]) / 8.0);
                    diskCap += (Long.parseLong(col[4]) * Long.parseLong(col[3]) / 8.0);
                    v = 1.0 * Long.parseLong(col[5]) / Long.parseLong(col[4]);
                    value.setItemValue(v == Double.NaN ? 0 : v);
                    value.setExtValue(Double.parseDouble(col[4]));
                    value.setNote(col[2]);
                    values.add(value);
                }
            }
            value = new MonitorValue();
            value.setCollectTime(snap.getSnapTime());
            value.setEntityId(monitor.getEntityId());
            value.setItemIndex(0);
            value.setItemName("mem");
            v = 1.0 * memUsed / memCap;
            value.setItemValue(v == Double.NaN ? 0 : v);
            value.setExtValue(Double.valueOf(memCap));
            value.setNote(String.valueOf(memCap));
            values.add(value);
            thresholds = monitorService.getThreshold(monitor.getMonitorId(), "mem");
            props = new Properties();
            props.put("source", monitor.getIp());
            props.put("type", getString("type.Mem"));
            props.put("value", value.getItemValue());
            props.put("strValue", format.format(value.getItemValue()));
            numberThreshold(thresholds, props, value.getItemValue(), MEM_TYPE);
            snap.setMem(value.getItemValue());
            value = new MonitorValue();
            value.setCollectTime(snap.getSnapTime());
            value.setEntityId(monitor.getEntityId());
            value.setItemName("disk");
            value.setItemIndex(0);
            v = 1.0 * diskUsed / diskCap;
            value.setItemValue(v == Double.NaN ? 0 : v);
            value.setExtValue(Double.valueOf(diskCap));
            value.setNote(String.valueOf(diskCap));
            values.add(value);
            snap.setDisk(value.getItemValue());
            // TODO阈值
            thresholds = monitorService.getThreshold(monitor.getMonitorId(), "disk");
            props = new Properties();
            props.put("source", monitor.getIp());
            props.put("type", getString("type.Disk"));
            props.put("value", value.getItemValue());
            props.put("strValue", format.format(value.getItemValue()));
            numberThreshold(thresholds, props, value.getItemValue(), DISK_TYPE);
            monitor.setReason(smp.getError());
            monitorService.insertValue(null, values);
            monitorService.updateSnapPerf(monitor.getIp(), snap);
            monitorService.fireEntityPerformance(monitor.getEntityId(), snap.getCpu(), snap.getMem(), snap.getVmem(),
                    snap.getDisk(), snap.getSysUpTime());
        } catch (Exception ex) {
            getLogger().debug(ex.getMessage(), ex);
            monitor.setAvailable(false);
            monitor.setHealthy(false);
            monitor.setReason(ex.toString());
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.monitor.MonitorJob#doJob(org.quartz.JobExecutionContext)
     */
    @Override
    protected void doJob(JobExecutionContext context) throws JobExecutionException {
        SnmpMonitorParam smp = (SnmpMonitorParam) monitor.getObjectContent();
        if (smp == null) {
            getLogger().debug("error system monitor of " + monitor.getIp());
            return;
        }
        try {
            SnmpParam snmpParam = monitorService.getSnmpParamByEntity(monitor.getEntityId());
            snmpParam.setIpAddress(monitor.getIp());
            smp.setSnmpParam(snmpParam);
            if (smp.getBinding("sysUpTime") == null) {
                smp.addBinding("sysUpTime", "1.3.6.1.2.1.25.1.1.0", FormulaBinding.TYPE_LEAF, null, null);
            }
            smp = facadeFactory.getFacade(monitor.getIp(), SnmpFacade.class).getSnmpMonitor(smp);
        } catch (SnmpException ex) {
            getLogger().debug(ex.getMessage(), ex);
        }
        if (smp.getError() != null) {
            monitor.setAvailable(false);
            monitor.setHealthy(false);
            monitor.setReason(smp.getError());
            return;
        }
        EntitySnap snap = new EntitySnap();
        snap.setSnapTime(monitor.getLastCollectTime());
        if (smp.containBindings("hrProcessorTable")) {
            doHostSystem(smp, snap);
            getLogger().debug("********************Result:" + smp + ",next monitor time:" + context.getNextFireTime());
            return;
        }
        Map<String, FormulaBinding> bindings = smp.getBindings();
        Map<String, MonitorValue> values = new HashMap<String, MonitorValue>();
        MonitorValue value = null;
        Properties props = new Properties();
        props.put("source", monitor.getIp());
        for (String name : bindings.keySet()) {
            if (name.indexOf("cpu") == -1 && name.indexOf("mem") == -1 && name.indexOf("disk") == -1
                    && name.indexOf("temperature") == -1 && name.indexOf("stack") == -1 && name.indexOf("heap") == -1) {
                continue;
            }
            double v = bindings.get(name).getFormulaValue();
            if (v == Double.NaN) {
                continue;
            }
            value = new MonitorValue();
            value.setCollectTime(monitor.getLastCollectTime());
            value.setEntityId(monitor.getEntityId());
            value.setItemIndex(0);
            value.setItemName(name);
            value.setItemValue(v / 100.0);
            value.setNote(bindings.get(name).getDesc());
            values.put(name, value);
        }
        value = values.get("mem");
        if (value != null) {
            List<Threshold> thresholds = monitorService.getThreshold(monitor.getMonitorId(), "mem");
            snap.setMem(value.getItemValue());
            props.put("type", getString("type.Mem"));
            props.put("value", value.getItemValue());
            props.put("strValue", format.format(value.getItemValue()));
            numberThreshold(thresholds, props, value.getItemValue(), MEM_TYPE);
        }
        String[] types = { "cpu", "cpu10m", "cpu5m", "cpu2m", "cpu1m", "cpu30s", "cpu10s", "cpu5s", "cpu1s" };
        for (String t : types) {
            value = values.get(t);
            if (value != null) {
                break;
            }
        }
        if (value != null) {
            List<Threshold> thresholds = monitorService.getThreshold(monitor.getMonitorId(), "cpu");
            snap.setCpu(value.getItemValue());
            props.put("type", getString("type.Cpu"));
            props.put("value", value.getItemValue());
            props.put("strValue", format.format(value.getItemValue()));
            numberThreshold(thresholds, props, value.getItemValue(), CPU_TYPE);
        }
        FormulaBinding binding = smp.getBinding("sysUpTime");
        if (binding != null) {
            snap.setSysUpTime(binding.getSingle());
        }
        monitorService.updateSnapPerf(monitor.getIp(), snap);

        // TODO 是否需要将【Monitor】中全部的基本数据类型转换成类
        monitorService.fireEntityPerformance(Long.valueOf(monitor.getEntityId()), Double.valueOf(snap.getCpu()),
                Double.valueOf(snap.getMem()), Double.valueOf(0), Double.valueOf(0), snap.getSysUpTime());
        List<MonitorValue> list = new ArrayList<MonitorValue>(values.size());
        list.addAll(values.values());
        monitorService.insertValue(null, list);
    }
}
