/***********************************************************************
 * $Id: CmcPerService.java,v1.0 2012-5-8 上午10:53:22 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.perf.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.topvision.ems.cmc.ccmts.domain.Cmc;
import com.topvision.ems.cmc.domain.CmcChanelName;
import com.topvision.ems.cmc.domain.CmcCmNumStatic;
import com.topvision.ems.cmc.domain.PortalChannelUtilizationShow;
import com.topvision.ems.cmc.perf.domain.CmcOpticalTemp;
import com.topvision.ems.cmc.performance.domain.ChannelCmNum;
import com.topvision.ems.cmc.performance.domain.ChannelUtilization;
import com.topvision.ems.cmc.performance.domain.CmcLinkQualityData;
import com.topvision.ems.cmc.performance.domain.SingleNoise;
import com.topvision.ems.cmc.performance.domain.UsBitErrorRate;
import com.topvision.ems.cmc.performance.facade.CmFlap;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author loyal
 * @created @2012-5-8-上午10:53:22
 * 
 */
public interface CmcPerfService {

    /**
     * 获取CCMTS设备的上下行通道用户数
     * 
     * @param direction
     *            String
     * @return List<ChannelCmNum>
     */
    List<ChannelCmNum> getNetworkCcmtsDeviceUsersLoadingTop(Map<String, Object> map);

    /**
     * 获取CCMTS设备的上下行通道用户数
     * 
     * @param direction
     *            String
     * @return List<ChannelCmNum>
     */
    List<ChannelCmNum> getCcmtsDeviceUsersList(Map<String, Object> map);

    /**
     * 获取CCMTS设备的上下行通道用户数记录数
     * 
     * @param direction
     *            String
     * @return List<ChannelCmNum>
     */
    Integer getCcmtsDeviceUsersCount(Map<String, Object> map);

    /**
     * 获取CCMTS设备的负载排行.
     * 
     * @param item
     *            String
     * @return List<Cmc>
     */
    List<Cmc> getNetworkCcmtsDeviceLoadingTop(String item);

    /**
     * 开启SNR性能采集
     * 
     * @param cmcId
     *            Long
     * @param period
     *            Long
     * @param snmpParam
     *            SnmpParam
     */
    public void startSNRMonitor(Long cmcId, Long period, SnmpParam snmpParam);

    /**
     * 设置SNR性能采集周期
     * 
     * @param cmcId
     *            Long
     * @param period
     *            Long
     * @param snmpParam
     *            SnmpParam
     */
    public void resetSNRMonitor(Long cmcId, Long period, SnmpParam snmpParam);

    /**
     * 关闭SNR性能采集
     * 
     * @param cmcId
     *            Long
     * @param snmpParam
     *            SnmpParam
     */
    public void stopSNRMonitor(Long cmcId, SnmpParam snmpParam);

    /**
     * 判断是否存在SNR监视器
     * 
     * @param cmcId
     *            Long
     * @return boolean
     */
    public boolean hasSNRMonitor(Long cmcId);

    /**
     * 根据CMC ID 与通道类型获取通道的ifIndex
     * 
     * @param cmcId
     *            Long CMC ID
     * @param type
     *            通道类型:128:下行通道，129：上行通道
     * @return List<Integer>
     */
    public List<Integer> getIfIndexByCmcId(Long cmcId, Integer type);

    /**
     * 获取某个通道timeStart，timeEnd之间的数据
     * 
     * @param map
     *            查询条件
     * @param timeStart
     *            起始时间
     * @param timeEnd
     *            终止时间
     * @return List<SingleNoise>
     */
    public List<SingleNoise> getSnrData(Map<String, Object> map, Timestamp timeStart, Timestamp timeEnd);

    /**
     * 获取某个通道最近采集的size指定条数的数据
     * 
     * @param map
     *            查询条件
     * @param size
     *            条数
     * @return List<SingleNoise>
     */
    public List<SingleNoise> getSnrData(Map<String, Object> map, Integer size);

    /**
     * 获取某个通道timeStart，timeEnd之间的数据
     * 
     * @param map
     *            查询条件
     * @param timeStart
     *            起始时间
     * @param timeEnd
     *            终止时间
     * @return List<ChannelUtilization>
     */
    public List<ChannelUtilization> getUtilizationData(Map<String, Object> map, Timestamp timeStart, Timestamp timeEnd);

    /**
     * 获取某个通道最近采集的size指定条数的数据
     * 
     * @param map
     *            查询条件
     * @param size
     *            条数
     * @return List<ChannelUtilization>
     */
    public List<ChannelUtilization> getUtilizationData(Map<String, Object> map, Integer size);

    /**
     * 获取SNR性能采集周期
     * 
     * @param cmcId
     *            Long
     * @return Integer
     */
    public Integer getSnrPeriod(Long cmcId);

    /**
     * 判断是否存在信道CM数监视器
     * 
     * @param cmcId
     *            Long
     * @return boolean
     */
    public boolean hasChannelCmNumMonitor(Long cmcId);

    /**
     * 开启信道CM数采集
     * 
     * @param cmcId
     *            Long
     * @param period
     *            Long
     * @param snmpParam
     *            SnmpParam
     */
    public void startChannelCmMonitor(Long cmcId, Long period, SnmpParam snmpParam);

    /**
     * 设置信道CM数采集周期
     * 
     * @param cmcId
     *            Long
     * @param period
     *            Long
     * @param snmpParam
     *            SnmpParam
     */
    public void resetChannelCmMonitor(Long cmcId, Long period, SnmpParam snmpParam);

    /**
     * 关闭信道CM数采集
     * 
     * @param cmcId
     *            Long
     * @param snmpParam
     *            SnmpParam
     */
    public void stopChannelCmMonitor(Long cmcId, SnmpParam snmpParam);

    /**
     * 获取信道CM数采集周期
     * 
     * @param cmcId
     *            Long
     * @return Integer
     */
    public Integer getChannelCmPeriod(Long cmcId);

    /**
     * 判断是否存在信道误码率监视器
     * 
     * @param cmcId
     *            Long
     * @return boolean
     */
    public boolean hasUsBitErrorRateMonitor(Long cmcId);

    /**
     * 开启信道误码率采集
     * 
     * @param cmcId
     *            Long
     * @param period
     *            Long
     * @param snmpParam
     *            SnmpParam
     */
    public void startUsBitErrorRateMonitor(Long cmcId, Long period, SnmpParam snmpParam);

    /**
     * 设置信道误码率采集周期
     * 
     * @param cmcId
     *            Long
     * @param period
     *            Long
     * @param snmpParam
     *            SnmpParam
     */
    public void resetUsBitErrorRateMonitor(Long cmcId, Long period, SnmpParam snmpParam);

    /**
     * 关闭信道误码率采集
     * 
     * @param cmcId
     *            Long
     * @param snmpParam
     *            SnmpParam
     */
    public void stopUsBitErrorRateMonitor(Long cmcId, SnmpParam snmpParam);

    /**
     * 获取信道误码率采集周期
     * 
     * @param cmcId
     *            Long
     * @return Integer
     */
    public Integer getUsBitErrorRatePeriod(Long cmcId);

    /**
     * 判断是否存在信道速率监视器
     * 
     * @param cmcId
     *            Long
     * @return boolean
     */
    public boolean hasChannelSpeedStaticMonitor(Long cmcId);

    /**
     * 开启信道速率采集
     * 
     * @param cmcId
     *            Long
     * @param period
     *            Long
     * @param snmpParam
     *            SnmpParam
     */
    public void startChannelSpeedStaticMonitor(Long cmcId, Long period, SnmpParam snmpParam);

    /**
     * 设置信道速率采集周期
     * 
     * @param cmcId
     *            Long
     * @param period
     *            Long
     * @param snmpParam
     *            SnmpParam
     */
    public void resetChannelSpeedStaticMonitor(Long cmcId, Long period, SnmpParam snmpParam);

    /**
     * 关闭信道速率采集
     * 
     * @param cmcId
     *            Long
     * @param snmpParam
     *            SnmpParam
     */
    public void stopChannelSpeedStaticMonitor(Long cmcId, SnmpParam snmpParam);

    /**
     * 获取信道速率采集周期
     * 
     * @param cmcId
     *            Long
     * @return Integer
     */
    public Integer getChannelSpeedStaticPeriod(Long cmcId);

    /**
     * 获取通道利用率TOP10
     * 
     * @return List<PortalChannelUtilizationShow>
     */
    public List<PortalChannelUtilizationShow> getNetworkCcmtsDeviceLoadingTop(Map<String, Object> map);

    /**
     * 开启0LT/CCMTS内存利用率、CPU利用率性能采集
     * 
     * @param cmcId
     *            Long
     * @param period
     *            Long 性能采集周期，单位：秒
     * @param snmpParam
     *            SnmpParam
     */
    public void startSystemPerfMonitor(Long cmcId, Long period, SnmpParam snmpParam);

    /**
     * 开启CCMTS-8800B内存利用率、CPU利用率性能采集
     * 
     * @param cmcId
     *            Long
     * @param period
     *            Long 性能采集周期，单位：秒
     * @param snmpParam
     *            SnmpParam
     */
    public void startSystem8800BPerfMonitor(Long cmcId, Long period, SnmpParam snmpParam);

    /**
     * 重新设置CCMTS内存利用率、CPU利用率性能采集
     * 
     * @param cmcId
     *            Long
     * @param period
     *            Long 性能采集周期，单位：秒
     * @param snmpParam
     *            SnmpParam
     */
    public void resetCcmtsSystemMonitor(Long cmcId, Long period, SnmpParam snmpParam);

    /**
     * 停止CCMTS内存利用率、CPU利用率性能采集 会将性能采集Monitor在数据库中删除
     * 
     * @param cmcId
     *            Long
     * @param snmpParam
     *            SnmpParam
     */
    public void stopCcmtsSystemMonitor(Long cmcId, SnmpParam snmpParam);

    /**
     * 查询指定CCMTS是否存在内存利用率、CPU利用率性能采集
     * 
     * @param cmcId
     *            Long
     * @return 存在返回true，不存在返回false
     */
    public boolean hasCcmtsSystemMonitor(Long cmcId);

    /**
     * 查询指定CCMTS内存利用率、CPU利用率性能采集周期
     * 
     * @param cmcId
     *            Long
     * @return 返回指定CCMTS性能采集周期
     */
    public Integer getCcmtsSystemPeriod(Long cmcId);

    /**
     * 开启cm采集
     * 
     * @param entityId
     *            Long
     * @param period
     *            Long
     * @param snmpParam
     *            SnmpParam
     */
    public void startCmStatusMonitor(Long entityId, Long period, SnmpParam snmpParam);

    /**
     * 关闭cm采集
     * 
     * @param cmcId
     *            Long
     * @param snmpParam
     *            SnmpParam
     */
    public void stopCmStatusMonitor(Long cmcId, SnmpParam snmpParam);

    /**
     * 判断是否有cmStatus采集
     * 
     * @param entityId
     *            Long
     * @return boolean
     */
    public boolean hasCmStatusMonitor(Long entityId);

    /**
     * 获取CC设备误码率
     * 
     * @return List<UsBitErrorRate>
     */
    List<UsBitErrorRate> getTopPortletErrorCodesLoading(Map<String, Object> paramMap);

    /**
     * 查询CCMTS的信道的误码率
     * 
     * @param start
     * @return
     */
    List<UsBitErrorRate> getChannelBerRate(Map<String, Object> paramMap);

    Integer getChannelBerRateCount(Map<String, Object> paramMap);

    /**
     * 获取CC设备CM FLAP ins异常次数
     * 
     * @return List<UsBitErrorRate>
     */
    List<CmFlap> getTopPortletFlapInsGrowthLoading(Map<String, Object> paramMap);

    List<CmFlap> loadCmFlapIns(Map<String, Object> paramMap);

    Integer getCmFlapInsCount(Map<String, Object> paramMap);

    /**
     * 获取CC设备误码率
     * 
     * @return UsBitErrorRate
     */
    UsBitErrorRate getErrorCodesByPortId(Long cmcId, Long portId, String targetName);

    /**
     * 获取CC信噪比
     * 
     * @return List<SingleNoise>
     */
    List<SingleNoise> getTopLowNoiseLoading(Map<String, Object> paramMap);

    /**
     * 通过cmcId获取信道列表
     * 
     * @param cmcId
     *            Long
     * @return List<CmcChanelName>
     */
    public List<CmcChanelName> getCmcChanelNames(Long cmcId);

    /**
     * 通过cmcId获取cc用户数
     * 
     * @param cmcId
     *            Long
     * @return CmcCmNumStatic
     */
    public CmcCmNumStatic getCmcCmNumStatic(Long cmcId);

    /**
     * 通过cmcId查询是否存在该cmc
     * 
     * @param cmcId
     *            cmcId
     * @return true 存在 false 不存在
     */
    public boolean isExistCmc(Long cmcId);

    /**
     * 查询CCMTS的上行信道的信噪比
     * 
     * @param sort
     *            排序属性
     * @param dir
     *            排序方向
     * @param start
     * @param limit
     * @return
     */
    List<SingleNoise> getNoiseRate(String name, Long deviceType, String sort, String dir, int start, int limit);

    /**
     * 查询CCMTS的上行信道的信噪比记录数
     * 
     * @return
     */
    Long getNoiseRateCount(String name, Long deviceType);

    /**
     * 查询CCMTS的信道的利用率
     * 
     * @param sort
     *            排序属性
     * @param dir
     *            排序方向
     * @param start
     * @param limit
     * @return
     */
    List<PortalChannelUtilizationShow> getChannelUsed(String name, Long deviceType, String sort, String dir, int start,
            int limit);

    /**
     * 查询CCMTS的信道的利用率记录数
     * 
     * @return
     */
    Long getChannelUsedCount(String name, Long deviceType);

    /**
     * 启动CPE监视器
     * 
     * @param cmcId
     *            CMCID
     * @param period
     *            时间间隔
     * @param snmpParam
     *            采集共同体名
     */
    void startCpeStatusMonitor(Long cmcId, long period, SnmpParam snmpParam);

    /**
     * 判断是否有cpeStatus采集
     * 
     * @param entityId
     *            Long
     * @return boolean
     */
    public boolean hasCpeStatusMonitor(Long entityId);

    /**
     * 关闭cm采集
     * 
     * @param cmcId
     *            Long
     * @param snmpParam
     *            SnmpParam
     */
    public void stopCpeStatusMonitor(Long cmcId, SnmpParam snmpParam);

    /**
     * 开启CMC在线状态性能采集(适用于独立IP)
     * 
     * @param cmcId
     * @param cmcIndex
     * @param snmpParam
     */
    void startCmcOnlineQuality(Long cmcId, Long cmcIndex, SnmpParam snmpParam, Long entityId, Long typeId);

    /**
     * 关闭CMC在线状态性能采集(适用于独立IP)
     * 
     * @param cmcId
     * @param snmpParam
     */
    void stopCmcOnlineQuality(Long cmcId, SnmpParam snmpParam);

    /**
     * 开启CMC服务质量性能采集
     * 
     * @param cmcId
     * @param cmcIndex
     * @param snmpParam
     * 
     */
    void startCmcServiceQuality(Long cmcId, Long cmcIndex, SnmpParam snmpParam, Long entityId, Long typeId);

    /**
     * 关闭CMC服务质量性能采集
     * 
     * @param cmcId
     * @param snmpParam
     */
    void stopCmcServiceQuality(Long cmcId, SnmpParam snmpParam);

    /**
     * 开启CMC链路质量性能采集
     * 
     * @param cmcId
     * @param cmcIndex
     * @param snmpParam
     */
    void startCmcLinkQuality(Long cmcId, Long cmcIndex, SnmpParam snmpParam, Long entityId, Long typeId);

    /**
     * 关闭CMC链路质量性能采集
     * 
     * @param cmcId
     * @param snmpParam
     */
    void stopCmcLinkQuality(Long cmcId, SnmpParam snmpParam);

    /**
     * 开启CMC链路质量性能采集
     * 
     * @param cmcId
     * @param cmcIndex
     * @param onuIndex
     * @param snmpParam
     */
    void startCmcLinkQualityFor8800A(Long cmcId, Long cmcIndex, Long onuIndex, SnmpParam snmpParam, Long entityId,
            Long typeId);

    /**
     * 开启CMC信号质量性能采集
     * 
     * @param cmcId
     * @param cmcIndex
     * @param snmpParam
     */
    void startCmcSignalQuality(Long cmcId, Long cmcIndex, SnmpParam snmpParam, Long entityId, Long typeId);

    /**
     * 关闭CMC信号质量性能采集
     * 
     * @param cmcId
     * @param snmpParam
     */
    void stopCmcSignalQuality(Long cmcId, SnmpParam snmpParam);

    /**
     * 开启CMC流量性能采集
     * 
     * @param cmcId
     * @param cmcIndex
     * @param snmpParam
     */
    void startCmcFlowQuality(Long cmcId, Long cmcIndex, SnmpParam snmpParam, Long entityId, Long typeId);

    /**
     * 关闭CMC流量性能采集
     * 
     * @param cmcId
     * @param snmpParam
     */
    void stopCmcFlowQuality(Long cmcId, SnmpParam snmpParam);

    /**
     * 开启CMC温度性能采集
     * 
     * @param cmcId
     * @param cmcIndex
     * @param snmpParam
     */
    void startCmcTempQuality(Long cmcId, Long cmcIndex, SnmpParam snmpParam, Long entityId, Long typeId);

    public boolean hasCmcMonitor(Long cmcId, String category);

    /**
     * 关闭CMC温度性能采集
     * 
     * @param cmcId
     * @param snmpParam
     */
    void stopCmcTempQuality(Long cmcId, SnmpParam snmpParam);

    /**
     * 开启CC上CM FLAP性能采集
     * 
     * @param cmcId
     * @param cmcIndex
     * @param snmpParam
     */
    void startCCCmFlapQuality(Long cmcId, Long cmcIndex, SnmpParam snmpParam, Long entityId, Long typeId);

    /**
     * 关闭CC上CM FLAP性能采集
     * 
     * @param cmcId
     * @param snmpParam
     */
    void stopCCCmFlapQuality(Long cmcId, SnmpParam snmpParam);

    /**
     * 获得CMC的指标索引
     * 
     * @param entityId
     * @param targetName
     */
    public List<Long> getModifyCmcTargetIndexs(Long entityId, String targetName);

    /**
     * 开启光机接收功率性能采集 added by huangdongsheng 2013-12-17
     * 
     * @param cmcId
     * @param cmcIndex
     * @param snmpParam
     */
    public void startOpticalReceiverMonitor(Long cmcId, Long cmcIndex, SnmpParam snmpParam, Long entityId, Long typeId);

    /**
     * 重置光机接收功率性能采集 未有其他地方使用
     * 
     * @param cmcId
     * @param period
     * @param snmpParam
     */
    public void resetOpticalReceiverMonitor(Long cmcId, Long period, SnmpParam snmpParam);

    /**
     * 关闭光机接收功能性能采集 added by huangdongsheng 2013-12-17
     * 
     * @param cmcId
     * @param snmpParam
     */
    public void stopOpticalReceiverMonitor(Long cmcId, SnmpParam snmpParam);

    /**
     * 获取信道用户数
     * 
     * @param cmcPortId
     * @return
     */
    ChannelCmNum getChannelCmNum(Long cmcPortId);

    /**
     * 获取CMC光功率信息
     * 
     * @param paramMap
     * @return
     */
    List<CmcLinkQualityData> getCmcOpticalInfo(Map<String, Object> paramMap);

    /**
     * 获取Cmc光功率信息数目
     * 
     * @return
     */
    int getCmcOpticalNum(Map<String, Object> paramMap);

    /**
     * 　开启Cmc性采集
     */
    void startCmcPerfCollect(Entity entity);

    /**
     * Add by Rod
     * 
     * Use for Topology
     * 
     * @param cmcId
     * @param cmcIndex
     * @param onuIndex
     * @param snmpParam
     * @param entityId
     * @param typeId
     */
    void startCmcPerfMonitorForA(Long cmcId, Long cmcIndex, Long onuIndex, SnmpParam snmpParam, Long entityId,
            Long typeId);

    /**
     * Add by Rod
     * 
     * Use for Topology
     * 
     * @param cmcId
     * @param cmcIndex
     * @param typeId
     * @param snmpParam
     */
    void startCmcPerfMonitorForB(Long cmcId, Long cmcIndex, Long typeId, SnmpParam snmpParam);

    /**
     * Add by Rod
     * 
     * Use for Topology
     * 
     * @param entityId
     * @param snmpParam
     */
    void startCmCpePerfMonitor(Long entityId, SnmpParam snmpParam);

    /**
     * Add by Rod
     * 
     * Use for Topology
     * 
     * @param cmcId
     * @param snmpParam
     */
    void stopCmcPerfMonitorForA(Long cmcId, SnmpParam snmpParam);

    /**
     * Add by Rod
     * 
     * Use for Topology
     * 
     * @param cmcId
     * @param snmpParam
     */
    void stopCmcPerfMonitorForB(Long cmcId, SnmpParam snmpParam);

    /**
     * Add by Rod
     * 
     * Use for Topology
     * 
     * @param entityId
     * @param snmpParam
     */
    void stopCmCpePerfMonitor(Long entityId, SnmpParam snmpParam);

    /**
     * 将该cmc下的所有cm状态改为offline
     * 
     * @param cmcId
     *            该cmc的index
     */
    void changeCmStatusOffine(long cmcId);

    /**
     * 更新8800B的在线状态
     * 
     * @param cmcId
     *            cmcId
     * @param status
     *            状态
     */
    void changeCmc8800BStatus(Long cmcId, Boolean status);

    /**
     * 开启CMC光机温度性能采集
     * 
     * @param cmcId
     * @param cmcIndex
     * @param snmpParam
     */
    void startCmcDorOptTempQuality(Long cmcId, Long cmcIndex, SnmpParam snmpParam, Long entityId, Long typeId);

    /**
     * 关闭CMC光机温度性能采集
     * 
     * @param cmcId
     * @param snmpParam
     */
    void stopCmcDorOptTempQuality(Long cmcId, SnmpParam snmpParam);

    /**
     * 开启CMC 60V电压性能采集
     * 
     * @param cmcId
     * @param cmcIndex
     * @param snmpParam
     */
    void startCmcDorLinePowerQuality(Long cmcId, Long cmcIndex, SnmpParam snmpParam, Long entityId, Long typeId);

    /**
     * 关闭CMC 60V电压性能采集
     * 
     * @param cmcId
     * @param snmpParam
     */
    void stopCmcDorLinePowerQuality(Long cmcId, SnmpParam snmpParam);

    /**
     * 获取光机温度信息top10
     * 
     * @param paramMap
     * @return
     */
    List<CmcOpticalTemp> getCmcOpticalTempInfo(Map<String, Object> paramMap);

    /**
     * 获取光机温度信息条数
     * 
     * @param paramMap
     * @return
     */
    int getCmcOpticalTempNum(Map<String, Object> paramMap);

}