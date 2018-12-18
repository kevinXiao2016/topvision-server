/***********************************************************************
 * $Id: OltPerfFacade.java,v1.0 2013-10-25 下午3:50:00 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.facade;

import java.util.List;
import java.util.Map;

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
import com.topvision.ems.facade.Facade;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author flack
 * @created @2013-10-25-下午3:50:00
 *
 */
@EngineFacade(serviceName = "OltPerfFacade", beanName = "oltPerfFacade")
public interface OltPerfFacade extends Facade {

    /**
     * 
     * @param snmpParam
     * @return
     */
    OltAttribute getOltAttribute(SnmpParam snmpParam);

    /**
     * 获取所有ONU的注册时长
     * 
     * @param snmpParam
     * @return
     */
    Map<String, String> getOnuRegister(SnmpParam snmpParam);

    /**
     * 获取所有ONU的Mac地址
     * 
     * @param snmpParam
     * @return
     */
    Map<String, String> getOnuMacAddress(SnmpParam snmpParam);

    /**
     * 获取OLT的在线时长
     * 
     * @param snmpParam
     * @return
     */
    String getOltSysUpTime(SnmpParam snmpParam);

    /**
     * PN8600-V1.10.0.5 获取OLT在线时长的方式
     * 
     * @param snmpParam
     * @return
     */
    String getTopSysOltUptime(SnmpParam snmpParam);

    List<OltSlotStatus> getOltSlotStatus(SnmpParam snmpParam);

    /**
     * 修改端口性能阈值
     * 
     * @param snmpParam
     * @param perfThresholdPort
     *            修改值
     */
    void modifyPerfThreshold(SnmpParam snmpParam, PerfThresholdPort perfThresholdPort);

    /**
     * 修改温度阈值
     * 
     * @param snmpParam
     * @param perfThresholdTemperature
     *            修改值
     */
    void modifyPerfThreshold(SnmpParam snmpParam, PerfThresholdTemperature perfThresholdTemperature);

    /**
     * 获取某个接口的实时性能数据
     * 
     * @param snmpParam
     * @param perfCurStatsTable
     *            设置了index的采集对象 @return PerfCurStatsTable
     */
    PerfCurStatsTable getCurPerfRecord(SnmpParam snmpParam, PerfCurStatsTable perfCurStatsTable);

    /**
     * 获取轮询间隔设置
     * 
     * @return PerfStatCycle
     * @param snmpParam
     */
    PerfStatCycle getPerfStatCycle(SnmpParam snmpParam);

    /**
     * 获取历史数据记录数配置
     * 
     * @return PerfStatsGlobalSet
     * @param snmpParam
     */
    PerfStatsGlobalSet getPerfStatsGlobalSet(SnmpParam snmpParam);

    /**
     * 获取端口阈值配置列表
     * 
     * @return List<PerfThresholdPort>
     * @param snmpParam
     */
    List<PerfThresholdPort> getPerfThresholdPortList(SnmpParam snmpParam);

    /**
     * 获取温度阈值配置列表
     * 
     * @return
     * @param snmpParam
     */
    List<PerfThresholdTemperature> getPerfThresholdTemperatureList(SnmpParam snmpParam);

    /**
     * 采集某一个端口某个时间之后的15分钟历史性能记录
     * 
     * @param snmpParam
     * @param perfStats15Table
     *            端口
     * @param lastTime
     *            获取该时间以后的数据 @return
     */
    List<PerfStats15Table> getPerfStats15Table(SnmpParam snmpParam, PerfStats15Table perfStats15Table, Long lastTime);

    /**
     * 全设备刷新15分钟历史性能记录
     * 
     * @return
     * @param snmpParam
     */
    List<PerfStats15Table> getPerfStats15Table(SnmpParam snmpParam);

    /**
     * 采集某一个端口某个时间之后的24小时历史性能记录
     * 
     * @param snmpParam
     * @param perfStats24Table
     *            端口
     * @param lastTime
     *            获取该时间以后的数据 @return
     */
    List<PerfStats24Table> getPerfStats24Table(SnmpParam snmpParam, PerfStats24Table perfStats24Table, Long lastTime);

    /**
     * 全设备刷新24小时历史性能记录
     * 
     * @param snmpParam
     * @return
     */
    List<PerfStats24Table> getPerfStats24Table(SnmpParam snmpParam);

    /**
     * 保存轮询周期设置
     * 
     * @param snmpParam
     * @param perfStatCycle
     *            要设置的参数
     * @throws SavePerfStatCycleException
     */
    void savePerfStatCycle(SnmpParam snmpParam, PerfStatCycle perfStatCycle);

    /**
     * 保存记录数配置参数
     * 
     * @param snmpParam
     * @param perfStatsGlobalSet
     *            要设置的参数
     */
    void savePerfStatsGlobalSet(SnmpParam snmpParam, PerfStatsGlobalSet perfStatsGlobalSet);
}
