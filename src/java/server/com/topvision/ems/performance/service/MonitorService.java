package com.topvision.ems.performance.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.network.domain.EntitySnap;
import com.topvision.ems.network.domain.Link;
import com.topvision.ems.network.domain.Port;
import com.topvision.ems.network.domain.PortPerf;
import com.topvision.ems.performance.domain.Monitor;
import com.topvision.ems.performance.domain.MonitorValue;
import com.topvision.ems.performance.domain.Threshold;
import com.topvision.exception.service.NetworkException;
import com.topvision.framework.service.Service;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.domain.SystemLog;

public interface MonitorService extends Service {
    /**
     * 添加一个设备监视器到工作队列.
     * 
     * @param dm
     * @return
     */
    Boolean addMonitor(Monitor dm);

    /**
     * 实时通知设备的性能.
     * 
     * @param entityId
     * @param cpu
     * @param mem
     * @param vmem
     * @param disk
     * @param sysupTime
     */
    void fireEntityPerformance(Long entityId, Double cpu, Double mem, Double vmem, Double disk, String sysupTime);

    /**
     * 实时通知延时时间.
     * 
     * @param entityId
     * @param delay
     */
    void fireEntityState(Long entityId, Integer delay);

    /**
     * 实时通知流量.
     * 
     * @param linkId
     * @param flow
     * @param rate
     * @param usage
     */
    void fireLinkFlow(Long linkId, Double flow, Double rate, Double usage);

    /**
     * 获取设备监视器.
     * 
     * @param map
     * @return
     */
    Monitor getMonitor(Map<String, String> map);

    /**
     * 获取最后得到的设备监视器信息.
     * 
     * @param monitorId
     * @return
     */
    List<MonitorValue> getLastValuesByMonitor(Long monitorId);

    /**
     * 获取指定设备ID及端口ID的连接.
     * 
     * @param entityId
     * @param ifIndex
     * @return
     */
    List<Link> getLinkByPort(Long entityId, Long ifIndex);

    /**
     * 获取指定设备所有端口状态列表.
     * 
     * @param entityId
     * @return
     */
    List<Port> getPortStatusByEntityId(Long entityId);

    /**
     * 获取SNMP参数.
     * 
     * @param entityId
     * @return
     */
    SnmpParam getSnmpParamByEntity(Long entityId);

    /**
     * 获取阈值.
     * 
     * @param monitorId
     * @param itemName
     * @return
     */
    List<Threshold> getThreshold(Long monitorId, String itemName);

    /**
     * 将设备监视器信息插入连通表.
     * 
     * @param systemLog
     * @param value
     */
    void insertConnectivity(SystemLog systemLog, MonitorValue value);

    /**
     * 将设备监视器信息批量插入连通表.
     * 
     * @param systemLog
     * @param values
     */
    void insertConnectivity(SystemLog systemLog, List<MonitorValue> values);

    /**
     * 插入端口信息.
     * 
     * @param systemLog
     * @param perfs
     */
    void insertPortPerf(SystemLog systemLog, List<PortPerf> perfs);

    /**
     * 插入设备监视器信息.
     * 
     * @param systemLog
     * @param value
     */
    void insertValue(SystemLog systemLog, MonitorValue value);

    /**
     * 批量插入设备监视器信息.
     * 
     * @param systemLog
     * @param values
     */
    void insertValue(SystemLog systemLog, List<MonitorValue> values);

    /**
     * 暂停一个设备监视器.
     * 
     * @param dm
     * @return
     * @throws NetworkException
     */
    Boolean pauseMonitor(Monitor dm) throws NetworkException;

    /**
     * 启动一个设备监视器.
     * 
     * @param dm
     * @return
     * @throws NetworkException
     */
    Boolean startMonitor(Monitor dm) throws NetworkException;

    /**
     * 停止一个设备监视器.
     * 
     * @param dm
     * @return
     * @throws NetworkException
     */
    Boolean stopMonitor(Monitor dm) throws NetworkException;

    /**
     * 更新设备信息.
     * 
     * @param monitor
     */
    void updateHealthy(Monitor monitor);

    /**
     * 更新指定设备所有端口状态列表.
     * 
     * @param ports
     */
    void updatePortStatus(List<Port> ports);

    /**
     * 更新开关信息.
     * 
     * @param ip
     * @param snap
     */
    void updateSnapPerf(String ip, EntitySnap snap);

    /**
     * 更新开关状态.
     * 
     * @param snap
     */
    void updateSnapState(EntitySnap snap);
}
