/***********************************************************************
 * $Id: CmcDao.java,v1.0 2011-10-25 下午04:31:35 $
 *
 * @author: loyal
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.perf.dao;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.topvision.ems.cmc.ccmts.domain.Cmc;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.domain.CmcCmNumStatic;
import com.topvision.ems.cmc.domain.PortalChannelUtilizationShow;
import com.topvision.ems.cmc.facade.domain.CmcPortMonitorDomain;
import com.topvision.ems.cmc.perf.domain.CmcOpticalTemp;
import com.topvision.ems.cmc.performance.domain.ChannelCmNum;
import com.topvision.ems.cmc.performance.domain.ChannelSpeedStatic;
import com.topvision.ems.cmc.performance.domain.ChannelUtilization;
import com.topvision.ems.cmc.performance.domain.CmcLinkQualityData;
import com.topvision.ems.cmc.performance.domain.CmcServiceQualityPerfResult;
import com.topvision.ems.cmc.performance.domain.CmcSignalQualityPerfResult;
import com.topvision.ems.cmc.performance.domain.SingleNoise;
import com.topvision.ems.cmc.performance.domain.UsBitErrorRate;
import com.topvision.ems.cmc.performance.facade.CmFlap;
import com.topvision.ems.cmc.performance.facade.CmcCmNumber;
import com.topvision.ems.cmc.performance.facade.CmcFlowQuality;
import com.topvision.ems.cmc.performance.facade.CmcLinkQualityFor8800A;
import com.topvision.ems.cmc.performance.facade.CmcServiceQuality;
import com.topvision.ems.cmc.performance.facade.CmcTempQualityFor8800B;
import com.topvision.ems.facade.domain.ScheduleMessage;
import com.topvision.ems.performance.domain.ViewerParam;
import com.topvision.framework.dao.Dao;
import com.topvision.platform.message.event.EntityValueEvent;
import com.topvision.platform.util.highcharts.domain.Point;

/**
 * @author loyal
 * @created @2011-10-25-下午04:31:35
 */
public interface CmcPerfDao extends Dao {
    /**
     * 获取CCMTS设备的上下行通道用户数
     * 
     * @return
     */
    List<ChannelCmNum> getNetworkCcmtsDeviceUsersLoadingTop(Map<String, Object> map);

    /**
     * 获取CCMTS设备的上下行通道用户数
     * 
     * @return
     */
    List<ChannelCmNum> getCcmtsDeviceUsersList(Map<String, Object> map);

    /**
     * 获取CCMTS设备的上下行通道用户数
     * 
     * @return
     */
    Integer getCcmtsDeviceUsersCount(Map<String, Object> map);

    /**
     * 获取CCMTS设备的负载排行.
     * 
     * @param item
     * @return
     */
    List<Cmc> getNetworkCcmtsDeviceLoadingTop(String item);

    /**
     * 更新接口的信噪数据
     * 
     * @param singleNoise
     *            信噪采集结果
     */
    void recordNoise(SingleNoise singleNoise);

    /**
     * 更新信道利用率
     * 
     * @param singleNoise
     *            信噪采集结果
     */
    void recordChannelUtilization(ChannelUtilization channelUtilization);

    /**
     * 更新CCMTS系统信息，并记录CPU、内存利用率
     * 
     * @param cmcAttribute
     */
    void recordCcmtsSystemInfo(CmcAttribute cmcAttribute);

    /**
     * 获取信噪监视器
     * 
     * @param cmcId
     * @return
     */
    Long getSNRMonitorId(Long cmcId);

    /**
     * 根据CMC ID 与通道类型获取通道的ifIndex
     * 
     * @param cmcId
     * @param type
     * @return
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
     * @return
     */
    public List<SingleNoise> getSnrData(Map<String, Object> map, Timestamp timeStart, Timestamp timeEnd);

    /**
     * 获取某个通道最近采集的size指定条数的数据
     * 
     * @param map
     *            查询条件
     * @param size
     *            条数
     * @return
     */
    public List<SingleNoise> getSnrData(Map<String, Object> map, Integer size);

    /**
     * 获取SNR性能采集周期
     * 
     * @param cmcId
     * @return
     */
    public Integer getSnrPeriod(Long cmcId);

    /**
     * 获取某个通道timeStart，timeEnd之间的数据
     * 
     * @param map
     *            查询条件
     * @param timeStart
     *            起始时间
     * @param timeEnd
     *            终止时间
     * @return
     */
    public List<ChannelUtilization> getUtilizationData(Map<String, Object> map, Timestamp timeStart, Timestamp timeEnd);

    /**
     * 获取某个通道最近采集的size指定条数的数据
     * 
     * @param map
     *            查询条件
     * @param size
     *            条数
     * @return
     */
    public List<ChannelUtilization> getUtilizationData(Map<String, Object> map, Integer size);

    /**
     * 获取监视器ID
     * 
     * @param cmcId
     *            CMC ID
     * @param category
     *            监视器类型
     * @return 返回监视器ID
     */
    public Long getMonitorId(Long cmcId, String category);

    /**
     * 查询监视器性能采集周期
     * 
     * @param cmcId
     *            CMC ID
     * @param category
     *            监视器类型
     * @return 返回指定CCMTS性能采集周期
     */
    public Integer getCcmtsSystemPeriod(Long cmcId, String category);

    /**
     * 获取所有通道的Top 10
     */
    public List<PortalChannelUtilizationShow> getNetworkCcmtsDeviceLoadingTop(Map<String, Object> map);

    /**
     * 获取信道CM数监视器
     * 
     * @param cmcId
     * @return
     */
    Long getChannelCmNumMonitorId(Long cmcId);

    /**
     * 更新信道CM统计数据
     * 
     * @param channelCmNum
     *            信道CM统计数据
     */
    void recordChannelCmNum(ChannelCmNum channelCmNum);

    /**
     * 获取信道CM数采集周期
     * 
     * @param cmcId
     * @return
     */
    public Integer getChannelCmPeriod(Long cmcId);

    /**
     * 获取信道误码率采集周期
     * 
     * @param cmcId
     * @return
     */
    public Integer getUsBitErrorRatePeriod(Long cmcId);

    /**
     * 获取信道误码率监视器
     * 
     * @param cmcId
     * @return
     */
    Long getUsBitErrorRateMonitorId(Long cmcId);

    /**
     * 更新信道误码率统计数据
     * 
     * @param usBitErrorRate
     *            信道CM统计数据
     */
    void recordUsBitErrorRate(UsBitErrorRate usBitErrorRate);

    /**
     * 获取信道速率采集周期
     * 
     * @param cmcId
     * @return
     */
    public Integer getChannelSpeedStaticPeriod(Long cmcId);

    /**
     * 获取信道速率监视器
     * 
     * @param cmcId
     * @return
     */
    Long getChannelSpeedStaticMonitorId(Long cmcId);

    /**
     * 获取cm监视器
     * 
     * @param entityId
     * @return
     */
    Long getCmStatusMonitorId(Long entityId);

    /**
     * 更新信道速率统计数据
     * 
     * @param channelSpeedStatic
     *            信道速率统计数据
     */
    void recordChannelSpeedStatic(ChannelSpeedStatic channelSpeedStatic);

    /**
     * 获取信道速率统计数据
     * 
     * @param channelSpeedStatic
     */
    ChannelSpeedStatic getLastChannelSpeedStatic(ChannelSpeedStatic channelSpeedStatic);

    /**
     * 获取cmc信道速率统计数据
     * 
     * @param cmcId
     */
    List<ChannelSpeedStatic> getLastCmcChannelSpeedStaticList(Long cmcId);

    /**
     * 获取绘制信噪历史曲线的坐标点列表
     * 
     * @param viewerParam
     * @return
     */
    List<Point> noiseRead(ViewerParam viewerParam);

    /**
     * 获取绘制CPU历史曲线的坐标点列表
     * 
     * @param viewerParam
     * @return
     */
    List<Point> cpuRead(ViewerParam viewerParam);

    /**
     * 获取绘制MEM历史曲线的坐标点列表
     * 
     * @param viewerParam
     * @return
     */
    List<Point> memRead(ViewerParam viewerParam);

    /**
     * 获取CC设备CM FLAP ins异常次数
     * 
     * @return List<UsBitErrorRate>
     */
    List<CmFlap> getTopPortletFlapInsGrowthLoading(Map<String, Object> paramMap);

    List<CmFlap> getCmFlapIns(Map<String, Object> paramMap);

    Integer getCmFlapInsCount(Map<String, Object> paramMap);

    /**
     * 获取CC设备误码率
     * 
     * @return
     */
    List<UsBitErrorRate> getTopPortletErrorCodesLoading(Map<String, Object> paramMap);

    /**
     * 获取CC设备误码率
     * 
     * @return
     */
    List<UsBitErrorRate> getChannelBerRate(Map<String, Object> paramMap);

    /**
     * 获取CC设备误码率数目
     * 
     * @return
     */
    Integer getChannelBerRateCount(Map<String, Object> paramMap);

    /**
     * 获取CC设备误码率
     * 
     * @return
     */
    UsBitErrorRate getErrorCodesByPortId(Long cmcId, Long portIndex, String targetName);

    /**
     * 获取CC信噪比
     * 
     * @return
     */
    List<SingleNoise> getTopLowNoiseLoading(Map<String, Object> paramMap);

    /**
     * 获取绘制CCER历史曲线的坐标点列表
     * 
     * @param viewerParam
     * @return
     */
    List<Point> bitErrorRateRead(ViewerParam viewerParam);

    /**
     * 获取绘制UCER历史曲线的坐标点列表
     * 
     * @param viewerParam
     * @return
     */
    List<Point> unBitErrorRateRead(ViewerParam viewerParam);

    /**
     * 获取绘制通道上CM总数历史曲线坐标点列表
     * 
     * @param viewerParam
     * @return
     */
    List<Point> cmNumTotalRead(ViewerParam viewerParam);

    /**
     * 获取绘制通道上在线CM数历史曲线坐标点列表
     * 
     * @param viewerParam
     * @return
     */
    List<Point> cmNumOnlineRead(ViewerParam viewerParam);

    /**
     * 获取绘制通道上Active CM数历史曲线坐标点列表
     * 
     * @param viewerParam
     * @return
     */
    List<Point> cmNumActiveRead(ViewerParam viewerParam);

    /**
     * 获取绘制通道上未注册CM数历史曲线坐标点列表
     * 
     * @param viewerParam
     * @return
     */
    List<Point> cmNumUnRegisteredRead(ViewerParam viewerParam);

    /**
     * 获取绘制通道上离线CM数历史曲线坐标点列表
     * 
     * @param viewerParam
     * @return
     */
    List<Point> cmNumOfflineRead(ViewerParam viewerParam);

    /**
     * 获取绘制通道上注册CM数历史曲线坐标点列表
     * 
     * @param viewerParam
     * @return
     */
    List<Point> cmNumRegisteredRead(ViewerParam viewerParam);

    /**
     * 获取绘制信道利用率曲线的坐标点列表
     * 
     * @param viewerParam
     * @return
     */
    List<Point> channelUtilizationRead(ViewerParam viewerParam);

    /**
     * 获取绘制上行信道速率曲线的坐标点列表
     * 
     * @param viewerParam
     * @return
     */
    List<Point> usSpeedRead(ViewerParam viewerParam);

    /**
     * 获取绘制下行信道速率曲线的坐标点列表
     * 
     * @param viewerParam
     * @return
     */
    List<Point> dsSpeedRead(ViewerParam viewerParam);

    /**
     * 通过cmcId获取cc用户数
     * 
     * @param cmcId
     * @return CmcCmNumStatic
     */
    public CmcCmNumStatic getCmcCmNumStatic(Long cmcId);

    /**
     * 更新CC8800B快照信息
     * 
     * @param event
     */
    void updateCmc8800BSnap(EntityValueEvent event);

    /**
     * 将该cmc下的所有cm状态改为offline
     * 
     * @param cmcId
     *            该cmc的index
     */
    void changeCmStatusOffine(long cmcId);

    /**
     * 通过cmcId查询是否存在该cmc
     * 
     * @param cmcId
     *            cmcId
     * @return true 存在 false 不存在
     */
    boolean isExistCmc(Long cmcId);

    /**
     * 通过cmcId找到拓扑图上对应的EntityId
     * 
     * @param cmcId
     * @param type
     * @return long
     */
    Long getTopoEntityIdByCmcId(long cmcId, Long type);

    /**
     * 更新CC通道的速率
     * 
     * @param cmcId
     *            指定CC的cmcId
     * @param ifIndex
     *            指定CC的ifIndex
     * @param ifSpeed
     *            需要更新的值
     */
    void updateIfSpeed(Long cmcId, Long ifIndex, Long ifSpeed);

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
    List<SingleNoise> selectNoiseRate(String name, Long deviceType, String sort, String dir, int start, int limit);

    /**
     * 查询CCMTS的上行信道的信噪比记录数
     * 
     * @return
     */
    Long selectNoiseRateCount(String name, Long deviceType);

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
    List<PortalChannelUtilizationShow> selectChannelUsed(String name, Long deviceType, String sort, String dir,
            int start, int limit);

    /**
     * 查询CCMTS的信道的利用率记录数
     * 
     * @return
     */
    Long selectChannelUsedCount(String name, Long deviceType);

    /**
     * 更新端口状态
     * 
     * @param cmcId
     *            cmcId
     * @param cmcPortMonitorDomain
     *            端口状态
     */
    void updateCcmtsPortStatus(Long cmcId, CmcPortMonitorDomain cmcPortMonitorDomain);

    /**
     * 更新端口状态
     * 
     * @param cmcId
     * @param flowQuality
     */
    void updateCcmtsPortStatus(Long cmcId, List<CmcFlowQuality> flowQuality);

    /**
     * 获取Cpe监视器
     * 
     * @param entityId
     * @return
     */
    Long getCpeStatusMonitorId(Long entityId);

    /**
     * 获得Cmc当前模块的性能处理
     * 
     * @param entityId
     * @param category
     * @return
     */
    List<Integer> getCmcPerformanceMonitorId(Long cmcId, String category);

    /**
     * 更新CMC服务质量性能数据
     * 
     * @param entityId
     * @param targetName
     * @param perfs
     */
    void insertCmcServiceQuality(Long cmcId, String targetName, CmcServiceQualityPerfResult result);

    /**
     * 更新CMC信号质量性能数据
     * 
     * @param cmcSingleQualities
     */
    void insertCmcSignalQuality(Long cmcId, String targetName, CmcSignalQualityPerfResult result);

    /**
     * 更新CMC链路质量性能数据(8800B)
     * 
     * @param cmcLinkQualityPerfs
     */
    void insertCmcLinkQuality(List<CmcLinkQualityData> cmcLinkQualityPerfs);

    /**
     * 更新下行信道CM数量性能数据
     * 
     * @param cmNumbers
     */
    void insertCmcCmNumberQuality(List<CmcCmNumber> cmNumbers);

    /**
     * 更新CMC流量性能数据
     * 
     * @param flowQualities
     */
    void insertCmcFlowQuality(List<CmcFlowQuality> flowQualities);

    /**
     * 更新8800B的模块温度性能数据
     * 
     * @param tempQualityFor8800Bs
     */
    void insertCmcTempQuality(List<CmcTempQualityFor8800B> tempQualityFor8800Bs);

    /**
     * 更新CMC的在线状态,只更新状态
     * 
     * @param cmcServiceQuality
     */
    void updateCmcStatus(Long cmcId, Integer status);

    /**
     * 更新CMC设备系统信息,包括cpu,mem,status等
     * 
     * @param cmcServiceQuality
     */
    void updateCmcSysInfo(CmcServiceQuality cmcServiceQuality);

    /**
     * 同步光功率数据
     * 
     * @param cmcLinkQualityFor8800A
     */
    void syncOnuPonOptical(CmcLinkQualityFor8800A cmcLinkQualityFor8800A);

    /**
     * 读取PON口CM总CM数
     * 
     * @param viewerParam
     * @return
     */
    List<Point> ponCmNumTotalRead(ViewerParam viewerParam);

    /**
     * 读取pon口在线CM数
     * 
     * @param viewerParam
     * @return
     */
    List<Point> ponCmNumOnlineRead(ViewerParam viewerParam);

    /**
     * 读取PON口正注册CM数
     * 
     * @param viewerParam
     * @return
     */
    List<Point> ponCmNumActiveRead(ViewerParam viewerParam);

    /**
     * 读取pon口下线CM数
     * 
     * @param viewerParam
     * @return
     */
    List<Point> ponCmNumOfflineRead(ViewerParam viewerParam);

    /**
     * 获取频谱监视器
     *
     * @param cmcId
     * @return
     */
    Long getSpectrumMonitorId(Long cmcId);

    /**
     * 获取所有频谱监视器的MONITER
     * 
     * @return
     */
    @SuppressWarnings("rawtypes")
    List<ScheduleMessage> getAllSpectrumMonitorId();

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
    List<CmcLinkQualityData> queryCmcOpticalInfo(Map<String, Object> paramMap);

    /**
     * 获取CMC光功率数目
     * 
     * @return
     */
    int queryCmcOpticalNum(Map<String, Object> paramMap);

    /**
     * 获取光机温度信息TopN
     * 
     * @param paramMap
     * @return
     */
    List<CmcOpticalTemp> queryCmcOpticalTempInfo(Map<String, Object> paramMap);

    /**
     * 获取光机温度信息条数
     * 
     * @param paramMap
     * @return
     */
    int queryCmcOpticalTempNum(Map<String, Object> paramMap);
}