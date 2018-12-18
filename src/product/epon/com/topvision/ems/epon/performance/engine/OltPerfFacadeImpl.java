/***********************************************************************
 * $Id: OltPerfFacadeImpl.java,v1.0 2013-10-25 下午3:52:07 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.ems.epon.exception.SavePerfStatCycleException;
import com.topvision.ems.epon.olt.domain.OltAttribute;
import com.topvision.ems.epon.olt.domain.OltSlotStatus;
import com.topvision.ems.epon.performance.domain.PerfCurStatsTable;
import com.topvision.ems.epon.performance.domain.PerfStatCycle;
import com.topvision.ems.epon.performance.domain.PerfStats15Table;
import com.topvision.ems.epon.performance.domain.PerfStats24Table;
import com.topvision.ems.epon.performance.domain.PerfStatsGlobalSet;
import com.topvision.ems.epon.performance.domain.PerfThresholdPort;
import com.topvision.ems.epon.performance.domain.PerfThresholdTemperature;
import com.topvision.ems.epon.performance.facade.OltPerfFacade;
import com.topvision.framework.exception.engine.SnmpException;
import com.topvision.framework.exception.engine.SnmpNoSuchInstanceException;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.snmp.SnmpWorker;

/**
 * @author flack
 * @created @2013-10-25-下午3:52:07
 *
 */
public class OltPerfFacadeImpl extends EmsFacade implements OltPerfFacade {
    private SnmpExecutorService snmpExecutorService;

    @Override
    public OltAttribute getOltAttribute(SnmpParam snmpParam) {
        OltAttribute oltAttribute = snmpExecutorService.getData(snmpParam, OltAttribute.class);
        // 把启动时长换算为服务器的时刻，以便界面显示启动时长
        oltAttribute.setOltDeviceUpTime(System.currentTimeMillis() - oltAttribute.getOltDeviceUpTime() * 10);
        return oltAttribute;
    }

    @Override
    public List<OltSlotStatus> getOltSlotStatus(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, OltSlotStatus.class);
    }

    /**
     * @param snmpExecutorService
     *            the snmpExecutorService to set
     */
    public void setSnmpExecutorService(SnmpExecutorService snmpExecutorService) {
        this.snmpExecutorService = snmpExecutorService;
    }

    /**
     * 获取某个接口的实时性能数据
     * 
     * @param snmpParam
     * @param perfCurStatsTable
     *            设置了index的采集对象 @return PerfCurStatsTable
     */
    public PerfCurStatsTable getCurPerfRecord(SnmpParam snmpParam, PerfCurStatsTable perfCurStatsTable) {
        try {
            perfCurStatsTable = snmpExecutorService.getTableLine(snmpParam, perfCurStatsTable);
        } catch (SnmpException e) {
            // 出现noSuchInstance情况后，表示该ONUPON不存在，继续获得下一个
            if (e instanceof SnmpNoSuchInstanceException) {
                return null;
            } else {
                throw e;
            }
        }
        return perfCurStatsTable;
    }

    /**
     * 修改端口性能阈值
     * 
     * @param snmpParam
     * @param perfThresholdPort
     */
    public void modifyPerfThreshold(SnmpParam snmpParam, PerfThresholdPort perfThresholdPort) {
        snmpExecutorService.setData(snmpParam, perfThresholdPort);
    }

    /**
     * 修改温度阈值
     * 
     * @param snmpParam
     * @param perfThresholdTemperature
     */
    public void modifyPerfThreshold(SnmpParam snmpParam, PerfThresholdTemperature perfThresholdTemperature) {
        snmpExecutorService.setData(snmpParam, perfThresholdTemperature);
    }

    /**
     * 获取轮询间隔设置
     * 
     * @param snmpParam
     * @return PerfStatCycle
     */
    public PerfStatCycle getPerfStatCycle(SnmpParam snmpParam) {
        return snmpExecutorService.getData(snmpParam, PerfStatCycle.class);
    }

    /**
     * 全设备刷新15分钟历史性能记录
     * 
     * @param snmpParam
     * @return
     */
    public List<PerfStats15Table> getPerfStats15Table(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, PerfStats15Table.class);
    }

    /**
     * 采集某一个端口某个时间之后的15分钟历史性能记录
     * 
     * @param snmpParam
     * @param perfStats15Table
     *            端口
     * @param lastTime
     *            获取该时间以后的数据 @return
     */
    public List<PerfStats15Table> getPerfStats15Table(SnmpParam snmpParam, PerfStats15Table perfStats15Table,
            Long lastTime) {
        List<PerfStats15Table> re = new ArrayList<PerfStats15Table>();
        PerfStatsGlobalSet perfStatsGlobalSet = getPerfStatsGlobalSet(snmpParam);
        int max = perfStatsGlobalSet.getPerfStats15MinMaxRecord();
        // 每次刷新5条记录
        for (int start = 1; start <= max; start++) {
            // List<PerfStats24Table> perfStats24Tables =
            // snmpExecutorService.getTableLines(snmpParam, perfStats24Table,
            // start, length);
            PerfStats15Table table = new PerfStats15Table();
            table.setDeviceIndex(perfStats15Table.getDeviceIndex());
            table.setEntityId(perfStats15Table.getEntityId());
            table.setPortIndex(perfStats15Table.getPortIndex());
            table.setStats15Index((long) start);
            try {
                table = snmpExecutorService.getTableLine(snmpParam, table);
                re.add(table);
            } catch (SnmpException e) {
                // 出现noSuchInstance情况后，表示该ONUPON不存在，继续获得下一个
                if (e instanceof SnmpNoSuchInstanceException) {
                    break;
                } else {
                    throw e;
                }
            }
        }
        return re;
    }

    /**
     * 全设备刷新24小时历史性能记录
     * 
     * @param snmpParam
     * @return
     */
    public List<PerfStats24Table> getPerfStats24Table(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, PerfStats24Table.class);
    }

    /**
     * 采集某一个端口某个时间之后的24小时历史性能记录
     * 
     * @param snmpParam
     * @param perfStats24Table
     *            端口
     * @param lastTime
     *            获取该时间以后的数据 @return
     */
    public List<PerfStats24Table> getPerfStats24Table(SnmpParam snmpParam, PerfStats24Table perfStats24Table,
            Long lastTime) {
        List<PerfStats24Table> re = new ArrayList<PerfStats24Table>();
        PerfStatsGlobalSet perfStatsGlobalSet = getPerfStatsGlobalSet(snmpParam);
        int max = perfStatsGlobalSet.getPerfStats24HourMaxRecord();
        // 每次刷新1条记录
        for (int start = 1; start <= max; start++) {
            PerfStats24Table table = new PerfStats24Table();
            table.setDeviceIndex(perfStats24Table.getDeviceIndex());
            table.setEntityId(perfStats24Table.getEntityId());
            table.setPortIndex(perfStats24Table.getPortIndex());
            table.setStats24Index((long) start);
            try {
                table = snmpExecutorService.getTableLine(snmpParam, table);
                re.add(table);
            } catch (SnmpException e) {
                // 出现noSuchInstance情况后，表示该ONUPON不存在，继续获得下一个
                if (e instanceof SnmpNoSuchInstanceException) {
                    break;
                } else {
                    throw e;
                }
            }
        }
        return re;
    }

    /**
     * 获取历史数据记录数配置
     * 
     * @param snmpParam
     * @return PerfStatsGlobalSet
     */
    public PerfStatsGlobalSet getPerfStatsGlobalSet(SnmpParam snmpParam) {
        return snmpExecutorService.getData(snmpParam, PerfStatsGlobalSet.class);
    }

    /**
     * 获取端口阈值配置列表
     * 
     * @param snmpParam
     * @return List<PerfThresholdPort>
     */
    public List<PerfThresholdPort> getPerfThresholdPortList(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, PerfThresholdPort.class);
    }

    /**
     * 获取温度阈值配置列表
     * 
     * @param snmpParam
     * @return
     */
    public List<PerfThresholdTemperature> getPerfThresholdTemperatureList(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, PerfThresholdTemperature.class);
    }

    /**
     * 保存轮询周期设置
     * 
     * @param snmpParam
     * @param perfStatCycle
     *            要设置的参数
     * @throws SavePerfStatCycleException
     */
    public void savePerfStatCycle(SnmpParam snmpParam, PerfStatCycle perfStatCycle) {
        snmpExecutorService.setData(snmpParam, perfStatCycle);
    }

    /**
     * 保存记录数配置参数
     * 
     * @param snmpParam
     * @param perfStatsGlobalSet
     *            要设置的参数
     */
    public void savePerfStatsGlobalSet(SnmpParam snmpParam, PerfStatsGlobalSet perfStatsGlobalSet) {
        snmpExecutorService.setData(snmpParam, perfStatsGlobalSet);
    }

    @Override
    public Map<String, String> getOnuRegister(SnmpParam snmpParam) {
        return snmpExecutorService.execute(new SnmpWorker<Map<String, String>>(snmpParam) {
            private static final long serialVersionUID = -727320896339548070L;

            @Override
            protected void exec() {
                snmpUtil.reset(snmpParam);
                result = snmpUtil.getListWithOid("1.3.6.1.4.1.17409.2.3.4.1.1.18");
            }
        }, null);
    }

    @Override
    public Map<String, String> getOnuMacAddress(SnmpParam snmpParam) {
        return snmpExecutorService.execute(new SnmpWorker<Map<String, String>>(snmpParam) {
            private static final long serialVersionUID = -727320896339548070L;

            @Override
            protected void exec() {
                snmpUtil.reset(snmpParam);
                result = snmpUtil.getListWithOid("1.3.6.1.4.1.17409.2.3.4.1.1.7");
            }
        }, null);
    }

    @Override
    public String getOltSysUpTime(SnmpParam snmpParam) {
        return snmpExecutorService.get(snmpParam, "1.3.6.1.2.1.1.3.0");
    }

    @Override
    public String getTopSysOltUptime(SnmpParam snmpParam) {
        return snmpExecutorService.get(snmpParam, "1.3.6.1.4.1.32285.11.2.3.1.2.9.0");
    }

}
