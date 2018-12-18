/**
 * 
 */
package com.topvision.ems.realtime.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.facade.SnmpFacade;
import com.topvision.ems.facade.domain.FormulaBinding;
import com.topvision.ems.facade.domain.SnmpMonitorParam;
import com.topvision.ems.network.dao.EntityDao;
import com.topvision.ems.network.dao.PortDao;
import com.topvision.ems.network.domain.Port;
import com.topvision.ems.network.domain.PortPerf;
import com.topvision.ems.network.domain.PortStatistics;
import com.topvision.ems.network.util.FlowUtil;
import com.topvision.ems.realtime.domain.PortThresholdInfo;
import com.topvision.ems.realtime.service.PortPerfCallback;
import com.topvision.ems.realtime.service.PortRtStateService;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.facade.FacadeFactory;

/**
 * @author niejun
 * 
 */
@Service("portRtStateService")
public class PortRtStateServiceImpl extends BaseService implements PortRtStateService {
    @Autowired
    private EntityDao entityDao;
    @Autowired
    private PortDao portDao;
    @Autowired
    private FacadeFactory facadeFactory;

    private class PortRtCollector extends Thread {
        private final long entityId;
        private boolean completed;
        private int count;

        public PortRtCollector(long entityId) {
            this.entityId = entityId;
        }

        public boolean isCompleted() {
            return completed;
        }

        @Override
        public void run() {
            SnmpMonitorParam smp = new SnmpMonitorParam();
            try {
                smp.addBinding("ifTableStatistics", ifTableStatistics, FormulaBinding.TYPE_COLUMN, null);
                SnmpParam snmpParam = entityDao.getSnmpParamByEntityId(entityId);
                smp.setSnmpParam(snmpParam);
            } catch (Exception ex) {
                getLogger().debug(ex.getMessage(), ex);
            }
            Map<Long, PortStatistics> previousPs = new HashMap<Long, PortStatistics>();
            while (!completed) {
                List<PortPerf> perfs = new ArrayList<PortPerf>();
                try {
                    smp = getSnmpFacade(smp.getSnmpParam().getIpAddress()).getSnmpMonitor(smp);
                    if (smp.getError() != null) {
                        Thread.sleep(10000);
                        continue;
                    }
                    FormulaBinding binding = smp.getBinding("ifTableStatistics");
                    Timestamp collectTime = new Timestamp(System.currentTimeMillis());
                    for (int i = 0; binding != null && binding.getTable() != null && i < binding.getTable().length; i++) {
                        String[] statistics = binding.getTable()[i];
                        if (statistics == null || statistics.length == 0 || statistics[0].equalsIgnoreCase("null")) {
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
                            ps.setCollectTime(collectTime);
                            PortStatistics prevPs = previousPs.get(ps.getIfIndex());
                            previousPs.put(ps.getIfIndex(), ps);
                            if (prevPs == null || prevPs.getCollectTime() == null) {
                                continue;
                            }
                            PortPerf perf = FlowUtil.getPortPerf(ps, prevPs, entityId);
                            perfs.add(perf);
                        } catch (Exception ex) {
                            getLogger().debug(ex.getMessage(), ex);
                        }
                    }
                } catch (Exception ex) {
                    getLogger().debug(ex.getMessage(), ex);
                }
                count = firePortPerf(entityId, perfs);
                if (count == 0) {
                    completed = true;
                    collectors.remove(entityId);
                    break;
                }
                try {
                    sleep(10000);
                } catch (Exception ex) {
                }
            }
        }

        public void setCompleted(boolean completed) {
            this.completed = completed;
        }
    }

    private final String[] ifTableStatistics = { "1.3.6.1.2.1.2.2.1.1", "1.3.6.1.2.1.2.2.1.7", "1.3.6.1.2.1.2.2.1.8",
            "1.3.6.1.2.1.2.2.1.9", "1.3.6.1.2.1.2.2.1.10", "1.3.6.1.2.1.2.2.1.11", "1.3.6.1.2.1.2.2.1.12",
            "1.3.6.1.2.1.2.2.1.13", "1.3.6.1.2.1.2.2.1.14", "1.3.6.1.2.1.2.2.1.15", "1.3.6.1.2.1.2.2.1.16",
            "1.3.6.1.2.1.2.2.1.17", "1.3.6.1.2.1.2.2.1.18", "1.3.6.1.2.1.2.2.1.19", "1.3.6.1.2.1.2.2.1.20",
            "1.3.6.1.2.1.2.2.1.21" };
    private final Map<Long, List<PortPerfCallback>> portPerfCallbackMapping = new HashMap<Long, List<PortPerfCallback>>();
    private final Map<Long, PortRtCollector> collectors = new Hashtable<Long, PortRtCollector>();

    public int firePortPerf(long entityId, List<PortPerf> perfs) {
        List<PortPerfCallback> callbacks = portPerfCallbackMapping.get(entityId);
        PortPerfCallback callback = null;
        for (int i = callbacks.size() - 1; i >= 0; i--) {
            callback = callbacks.get(i);
            try {
                callback.sendPortPerf(perfs);
            } catch (Exception ex) {
                callbacks.remove(callback);
            }
        }
        return callbacks.size();
    }

    @Override
    public List<Port> getPortByEntity(long entityId) throws Exception {
        return portDao.getPortsByEntityId(entityId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.realtime.service.PortRtStateService#getPortThresholdInfo (long)
     */
    @Override
    public PortThresholdInfo getPortThresholdInfo(long entityId) throws Exception {
        return null;
    }

    /**
     * @return the snmpService
     */
    public SnmpFacade getSnmpFacade(String ip) {
        return facadeFactory.getFacade(ip, SnmpFacade.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.realtime.service.PortRtStateService# registerPortPerfCallback
     * (com.topvision.ems.realtime.service .PortPerfCallback, long)
     */
    @Override
    public void registerPortPerfCallback(PortPerfCallback callback, long entityId) {

        List<PortPerfCallback> list = portPerfCallbackMapping.get(entityId);
        if (list == null) {
            list = new Vector<PortPerfCallback>();
            portPerfCallbackMapping.put(entityId, list);
        }
        list.add(callback);

        boolean flag = collectors.containsKey(entityId);
        if (flag) {
            if (collectors.get(entityId).isCompleted()) {
                // 删除已经处于完成状态的采集器
                collectors.remove(entityId);
            } else {
                return;
            }
        }

        // 启动该entiyId的实时采集, 同一个设备只允许一个采集器
        PortRtCollector thread = new PortRtCollector(entityId);
        collectors.put(entityId, thread);
        thread.start();
        if (getLogger().isDebugEnabled()) {
            getLogger().debug("Start device port perf collector:" + entityId);
        }
    }

    /**
     * @param entityDao
     *            the entityDao to set
     */
    public void setEntityDao(EntityDao entityDao) {
        this.entityDao = entityDao;
    }

    /**
     * @param portDao
     *            the portDao to set
     */
    public void setPortDao(PortDao portDao) {
        this.portDao = portDao;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.realtime.service.PortRtStateService#
     * unregisterPortPerfCallback(com.topvision.ems.realtime.service .PortPerfCallback, long)
     */
    @Override
    public void unregisterPortPerfCallback(PortPerfCallback callback, long entityId) {
        List<PortPerfCallback> list = portPerfCallbackMapping.get(entityId);
        list.remove(callback);
        getLogger().debug("unregisterCpuAndMemCallback:" + callback + "   " + entityId);
        if (list.size() == 0) {
            // 停止该entiyId的实时采集
            PortRtCollector t = collectors.remove(entityId);
            if (t != null) {
                t.setCompleted(true);
                t = null;
            }
            if (getLogger().isDebugEnabled()) {
                getLogger().debug("Stop and destroy device CPU and MEM collector:" + entityId);
            }
        }
    }

    /**
     * @param facadeFactory
     *            the facadeFactory to set
     */
    public void setFacadeFactory(FacadeFactory facadeFactory) {
        this.facadeFactory = facadeFactory;
    }
}
