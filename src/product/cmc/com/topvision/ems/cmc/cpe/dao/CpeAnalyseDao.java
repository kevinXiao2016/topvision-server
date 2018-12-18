/***********************************************************************
 * $ com.topvision.ems.cmc.dao.CpeAnalyseDao,v1.0 2013-6-21 15:33:22 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.cpe.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.cmc.facade.domain.TopCpeAttribute;
import com.topvision.ems.cmc.performance.domain.CmAct;
import com.topvision.ems.cmc.performance.domain.CmNum;
import com.topvision.ems.cmc.performance.domain.CpeAct;
import com.topvision.ems.cmc.performance.domain.InitialDataCmAction;
import com.topvision.ems.cmc.performance.domain.InitialDataCpeAction;
import com.topvision.framework.common.CollectTimeRange;
import com.topvision.framework.dao.Dao;

/**
 * @author jay
 * @created @2013-6-21-15:33:22
 */
public interface CpeAnalyseDao extends Dao {

    /**
     * 通过时间段查询出所有该时间段CM的原始记录
     * 
     * @param ctr
     *            时间段
     * @return 原始记录结果集
     */
    List<InitialDataCmAction> getInitialDataCmActionByTimeRange(CollectTimeRange ctr);

    /**
     * 通过时间段查询出所有该时间段CPE的原始记录
     * 
     * @param ctr
     *            时间段
     * @return 原始记录结果集
     */
    Map<Long, Map<Long, InitialDataCpeAction>> getInitialDataCpeActionByTimeRange(CollectTimeRange ctr);

    /**
     * 插入全网统计CM数量
     * 
     * @param all
     *            全网CM数量
     */
    void insertAllCmNum(CmNum all);

    /**
     * 插入区域CM数量
     * 
     * @param areaMap
     *            区域CM数量
     */
    void insertAreaCmNum(Map<Long, CmNum> areaMap);

    /**
     * 插入设备CM数量
     * 
     * @param deviceMap
     *            设备CM数量
     */
    void insertDeviceCmNum(Map<Long, CmNum> deviceMap);

    /**
     * 插入PON口CM数量
     * 
     * @param ponMap
     *            PON口下CM数量
     */
    void insertPonCmNum(Map<Long, Map<Long, CmNum>> ponMap);

    /**
     * 插入CMTSCM数量
     * 
     * @param cmtsMap
     */
    void insertCmtsCmNum(Map<Long, Map<Long, CmNum>> cmtsMap);

    /**
     * 插入下行信道CM数量
     * 
     * @param downMap
     *            下行信道CM数量
     */
    void insertDownCmNum(Map<Long, Map<Long, Map<Long, CmNum>>> downMap);

    /**
     * 插入上行信道CM数量
     * 
     * @param upMap
     *            上行信道CM数量
     */
    void insertUpCmNum(Map<Long, Map<Long, Map<Long, CmNum>>> upMap);

    /**
     * 插入设备CM数量
     *
     * @param cmNum
     *            设备CM数量
     */
    void insertDeviceCmNum(CmNum cmNum);

    /**
     * 插入PON口CM数量
     *
     * @param cmNum
     *            PON口下CM数量
     */
    void insertPonCmNum(CmNum cmNum);

    /**
     * 插入CMTSCM数量
     *
     * @param cmNum
     */
    void insertCmtsCmNum(CmNum cmNum);

    /**
     * 插入PortCM数量
     *
     * @param cmNum
     *            PortCM数量
     */
    void insertPortCmNum(CmNum cmNum);

    /**
     * 获得某个CM的最后一次的状态
     * 
     * @param entityId
     * @param cmAttribute
     * @return
     */
    CmAct getCmLastStatus(Long entityId, CmAttribute cmAttribute);

    /**
     * 插入CM行为数据
     * 
     * @param ca
     */
    void insertCmAct(CmAct ca);

    /**
     * 查询上一次的轮询结果
     * 
     * @param entityId
     * @return
     */
    List<CmAct> getCmLastStatusByEntityId(Long entityId);

    /**
     * 获得某个CPE的最后一次的状态
     * 
     * @param entityId
     * @param cpeAttribute
     * @return
     */
    CpeAct getCpeLastStatus(Long entityId, TopCpeAttribute cpeAttribute);

    /**
     * 插入CPE行为数据
     * 
     * @param ca
     */
    void insertCpeAct(CpeAct ca);

    /**
     * 查询上一次的轮询结果
     * 
     * @param entityId
     * @return
     */
    List<CpeAct> getCpeLastStatusByEntityId(Long entityId);

    /**
     * 在某个时间之后是否所有监视器都已经被采集
     * 
     * @param endTime
     * @return
     */
    boolean isAllCmMonitorHasCollect(Long endTime);

    /**
     * 获取所有CM监视器最后一次的采集时间
     * 
     * @return
     */
    Long getCmMonitorMaxCollectTime();

    /**
     * 删除上下线行为数据
     * 
     * @param actionDataSavePolicy
     *            距离现在往前多少时间 单位毫秒
     */
    void deleteActionData(Long actionDataSavePolicy);

    /**
     * 删除原始数据
     * 
     * @param initialDataSavePolicy
     *            距离现在往前多少时间 单位毫秒
     */
    void deleteInitialData(Long initialDataSavePolicy);

    /**
     * 删除统计数据
     * 
     * @param statisticDataSavePolicy
     *            距离现在往前多少时间 单位毫秒
     */
    void deleteStatisticData(Long statisticDataSavePolicy);

    /**
     * 存入设备cm数统计采集数据到last表
     * 
     * @param lastCmNum
     */
    void insertDeviceCmNumLast(Map<Long, CmNum> lastCmNum);

    /**
     * 存入pon口cm数统计采集数据到last表
     * 
     * @param ponMap
     */
    void insertPonCmNumLast(Map<Long, Map<Long, CmNum>> ponMap);

    /**
     * 存入cmts cm数统计采集数据到last表
     * 
     * @param cmtsMap
     */
    void insertCmtsCmNumLast(Map<Long, Map<Long, CmNum>> cmtsMap);

    /**
     * 存入信道cm数统计采集数据到last表
     * 
     * @param downMap
     * @param upMap
     */
    void insertPortCmNumLast(Map<Long, Map<Long, Map<Long, CmNum>>> downMap,
            Map<Long, Map<Long, Map<Long, CmNum>>> upMap);

    /**
     * 查询出所有设备的最后一次CmNum
     * 
     * @return Map<Long, CmNum>
     */
    Map<Long, CmNum> selectAllDeviceLastCmNum();

    /**
     * 删除一台entity下的所有Device级别的CmNum
     * @param entityId
     */
    void deleteDeviceCmNumLast(Long entityId);

    /**
     * 删除一台entity下的所有Cmts级别的CmNum
     * @param entityId
     */
    void deleteCmtsCmNumLast(Long entityId);

    /**
     * 删除一台entity下的所有Pon级别的CmNum
     * @param entityId
     */
    void deletePonLastCmNum(Long entityId);

    /**
     * 删除一台entity下的所有Port级别的CmNum
     * @param entityId
     */
    void deletePortCmNumLast(Long entityId);

    /**
     * 删除一个Device的CmNum
     * @param cmNum
     */
    void deleteDeviceCmNumLast(CmNum cmNum);

    /**
     * 删除一个Cmts的CmNum
     * @param cmNum
     */
    void deleteCmtsCmNumLast(CmNum cmNum);

    /**
     * 删除一个Pon的CmNum
     * @param cmNum
     */
    void deletePonLastCmNum(CmNum cmNum);

    /**
     * 删除一个Port的CmNum
     * @param cmNum
     */
    void deletePortCmNumLast(CmNum cmNum);

    /**
     * 更新一个Device的CmNum
     * @param cmNum
     */
    void updateDeviceCmNumLast(CmNum cmNum);

    /**
     * 更新一个Pon的CmNum
     * @param cmNum
     */
    void updatePonCmNumLast(CmNum cmNum);

    /**
     * 更新一个Cmts的CmNum
     * @param cmNum
     */
    void updateCmtsCmNumLast(CmNum cmNum);

    /**
     * 更新一个Port的CmNum
     * @param cmNum
     */
    void updatePortCmNumLast(CmNum cmNum);

    /**
     * static cpe number by cm
     */
    void staticCpeNum();

    void deleteCmHistoryData(Long cmHistorySavePolicy);

    /**
     * 根据
     * @param ponNum
     */
    void refreshPonLastCmNum(CmNum ponNum);

    /**
     * 查询一个entity下所有小C的CmNum
     * @param entityId
     * @return
     */
    List<CmNum> selectCmtsCmNumByEntityId(Long entityId);

    /**
     * 根据Device下的Pon口CmNum计算Device下的CmNum
     * @param entityId
     */
    void insertDeviceCmNumLastByPonNum(Long entityId);
}
