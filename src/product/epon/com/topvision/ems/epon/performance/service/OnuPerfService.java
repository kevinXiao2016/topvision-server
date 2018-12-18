/***********************************************************************
 * $Id: OnuPerfService.java,v1.0 2015-4-22 上午10:43:47 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.epon.performance.domain.OnuLinkCollectInfo;
import com.topvision.ems.epon.performance.domain.PerfOnuQualityHistory;
import com.topvision.framework.annotation.DynamicDB;
import com.topvision.framework.service.Service;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.util.highcharts.domain.Point;

/**
 * @author flack
 * @created @2015-4-22-上午10:43:47
 *
 */
public interface OnuPerfService extends Service {

    /**
     * 开启ONU在线状性能采集
     * @param entityId OLT设备ID
     * @param onuId
     * @param onuIndex
     * @param snmpParam
     */
    void startOnuOnlineQuality(Long entityId, Long onuId, Long onuIndex, SnmpParam snmpParam, Long typeId);

    /**
     * 开启ONU链路质量性能采集
     * @param entityId OLT设备ID
     * @param onuId
     * @param onuIndex
     * @param snmpParam
     */
    void startOnuLinkQuality(Long entityId, Long onuId, Long onuIndex, SnmpParam snmpParam, Long typeId);

    /**
     * 开启ONU速率性能采集
     * @param entityId
     * @param onuId
     * @param onuIndex
     * @param snmpParam
     * @param typeId
     */
    void startOnuFlowQuality(Long entityId, Long onuId, Long onuIndex, SnmpParam snmpParam, Long typeId);

    /**
     * 关闭ONU在线状性能采集
     * @param onuId
     * @param snmpParam
     */
    void stopOnuOnlineQuality(Long onuId, SnmpParam snmpParam);

    /**
     * 关闭ONU链路质量性能采集
     * @param onuId
     * @param snmpParam
     */
    void stopOnuLinkQuality(Long onuId, SnmpParam snmpParam);

    /**
     * 关闭ONU速率采集
     * @param onuId
     * @param snmpParam
     */
    void stopOnuFlowQuality(Long onuId, SnmpParam snmpParam);

    /**
     * 是否存在对应的性能采集器
     * @param onuId
     * @param category
     * @return
     */
    boolean hasOnuPerfMonitor(Long onuId, String category);

    /**
     * 获取对应的性能采集器
     * @param onuId
     * @param category
     * @return
     */
    List<Integer> getOnuPerfMonitor(Long onuId, String category);

    /**
     * 查询ONU光链路信息采集数据
     * @param onuId
     * @param perfTarget
     * @param startTime
     * @param endTime
     * @return
     */
    @DynamicDB
    List<Point> queryOnuOptLinkPerfPoints(Long onuId, String perfTarget, String startTime, String endTime);

    /**
     * 获取Onu的Pon端口列表
     * @param onuId
     * @return
     */
    List<Long> getOnuPonIndexList(Long onuId);

    /**
     * 获取Onu的UNI端口列表
     * @param onuId
     * @return
     */
    List<Long> getOnuUniIndexList(Long onuId);

    /**
     * 查询ONU速率性能数据
     * @param paramMap
     * @return
     */
    @DynamicDB
    List<Point> getOnuFlowPerfPoints(Map<String, Object> paramMap);

    /**
     * ONU开启性能(From OnuDiscoveryService)
     * 
     * @param entityId
     * @param onuId
     * @param onuIndex
     */
    void startOnuPerfCollect(Long entityId, Long onuId, Long onuIndex);

    /**
     * ONU关闭性能(From OnuDiscoveryService)
     * 
     * @param onuId
     * @param snmpParam
     */
    void stopOnuPerfCollect(Long onuId, SnmpParam snmpParam);

    /**
     * 开启ONU CATV性能采集
     * @param entityId
     * @param onuId
     * @param onuIndex
     * @param snmpParam
     * @param typeId
     */
    void startOnuCatvQuality(Long entityId, Long onuId, Long onuIndex, SnmpParam snmpParam, Long typeId);

    /**
     * 关闭ONU CATV性能采集
     * @param onuId
     * @param snmpParam
     */
    void stopOnuCatvQuality(Long onuId, SnmpParam snmpParam);
    
    /**
     * 查询24h内Onu历史最低光功率
     * @param onuLinkCollectInfo
     * @return
     */
    PerfOnuQualityHistory queryMinPonRevPower(OnuLinkCollectInfo onuLinkCollectInfo);
    
    /**
     * 查询一个月内Onu历史最好光接收功率
     * @param onuLinkCollectInfo
     * @return
     */
    PerfOnuQualityHistory queryMaxPonRevPower(OnuLinkCollectInfo onuLinkCollectInfo);
    
    /**
     * 查询24h内CATV历史最低光接收功率
     * @param onuLinkCollectInfo
     * @return
     */
    PerfOnuQualityHistory queryMinCATVRevPower(OnuLinkCollectInfo onuLinkCollectInfo);
    
    /**
     * 查询一个月内CATV历史最好光接收功率
     * @param onuLinkCollectInfo
     * @return
     */
    PerfOnuQualityHistory queryMaxCATVRevPower(OnuLinkCollectInfo onuLinkCollectInfo);
    
    /**
     * 将汇总的24h最低收光功率插入数据库中
     * 
     * @param perfOnuQualityHistory
     */
    void insertOrUpdateMinReceivedPower(PerfOnuQualityHistory perfOnuQualityHistory);
}
