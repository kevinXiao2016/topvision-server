/***********************************************************************
 * $Id: OltSlotDao.java,v1.0 2013-10-25 上午10:14:15 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.olt.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.topvision.ems.epon.domain.EponBoardStatistics;
import com.topvision.ems.epon.olt.domain.OltFanAttribute;
import com.topvision.ems.epon.olt.domain.OltFanStatus;
import com.topvision.ems.epon.olt.domain.OltPonAttribute;
import com.topvision.ems.epon.olt.domain.OltPowerAttribute;
import com.topvision.ems.epon.olt.domain.OltPowerStatus;
import com.topvision.ems.epon.olt.domain.OltSlotAttribute;
import com.topvision.ems.epon.olt.domain.OltSlotMapTable;
import com.topvision.ems.epon.olt.domain.OltSlotStatus;
import com.topvision.ems.epon.olt.domain.OltSniAttribute;
import com.topvision.ems.epon.onu.domain.OltUniAttribute;
import com.topvision.framework.dao.BaseEntityDao;
import com.topvision.nbi.tl1.api.domain.BoardInfo;

/**
 * @author flack
 * @created @2013-10-25-上午10:14:15
 *
 */
public interface OltSlotDao extends BaseEntityDao<Object> {

    /**
     * 获取槽位属性
     * 
     * @param slotId
     *            槽位ID
     * @return
     */
    OltSlotAttribute getSlotAttribute(Long slotId);

    /**
     * 获取电源属性
     * 
     * @param powerCardId
     *            电源槽位ID
     * @return
     */
    OltPowerAttribute getPowerAttribute(Long powerCardId);

    /**
     * 获取风扇属性
     * 
     * @param fanCardId
     *            风扇槽位ID
     * @return
     */
    OltFanAttribute getFanAttribute(Long fanCardId);

    /**
     * 更新风扇转速等级
     * 
     * @param fanCardId
     *            风扇槽位ID
     * @return fanSpeedControlLevel 风扇风速等级
     */
    void updateFanSpeedControlLevel(Long fanCardId, Integer fanSpeedControlLevel);

    /**
     * 更新风扇转速
     * 
     * @param fanCardId
     * @param fanStatus
     */
    void updateFanSpeedControl(OltFanStatus fanStatus);

    /**
     * 更新板卡状态（Index方式）
     * 
     * @param entityId
     * @param slotStatus
     */
    void updateSlotStatusBySlotIndex(Long entityId, OltSlotStatus slotStatus);

    /**
     * @param entityId
     * @param bAdminStatus
     * @param slotIndex
     */
    void updateSlotInstalled(Long entityId, Integer bPresenceStatus, Integer topSysBdActualType, Long slotIndex);

    /**
     * 得到风扇当前转速
     * 
     * @param entityId
     * @return
     */
    OltFanStatus getFanRealSpeed(Long entityId);

    /**
     * 查询所有板卡信息
     * 
     * @return
     */
    List<EponBoardStatistics> getBoardList(Map<String, Object> map);

    /**
     * 
     * 获得板卡slotNo号
     * 
     * @param entityId
     * @param slotId
     */
    Long getSlotNoById(Long entityId, Long slotId);

    /**
     * 获得设备上所有槽位
     * 
     * @param entityId
     *            设备ID
     * @return List<OltSlotAttribute>
     */
    List<OltSlotAttribute> getOltSlotList(Long entityId);

    /**
     * 获取SLOT状态信息
     * 
     * @param slotId
     *            槽位ID
     * @return OltSlotStatus
     */
    OltSlotStatus getOltSlotStatus(Long slotId);

    /**
     * 获得某个pon板的pon口列表
     * 
     * @param slotId
     * @return
     */
    List<OltPonAttribute> getSlotPonList(Long slotId);

    /**
     * 获得某个sni板的sni口列表
     * 
     * @param slotId
     * @return
     */
    List<OltSniAttribute> getSlotSniList(Long slotId);

    /**
     * 获得某个uni板的uni口列表
     * 
     * @param onuId
     * @return
     */
    List<OltUniAttribute> getSlotUniList(Long onuId);

    /**
     * 获取槽位索引
     * 
     * @param slotId
     *            槽位ID
     * @return Long
     */
    Long getSlotIndex(Long slotId);

    /**
     * 获取风扇索引
     * 
     * @param fanCardId
     *            风扇ID
     * @return Long
     */
    Long getFanCardIndex(Long fanCardId);

    /**
     * 记录板卡预配置记录
     * 
     * @param slotId
     * @param entityId
     * @param collectPreConfigType
     */
    void recordOltSlotCollect(Long slotId, Long entityId, Integer collectPreConfigType);

    /**
     * 删除槽位关系
     * 
     * @param slotId
     *            槽位ID
     */
    void deleteOltSlot(Long slotId, Long entityId, Integer preConfigType);

    /**
     * 获取风扇状态
     * 
     * @param fanCardId
     *            风扇ID
     */
    OltFanStatus getFanStatus(Long fanCardId);

    /**
     * 获取电源状态
     * 
     * @param powerCardId
     *            电源ID
     */
    OltPowerStatus getPowerStatus(Long powerCardId);

    /**
     * 批量插入以及更新Power属性
     * 
     * @param list
     *            电源属性列表
     * @param oltMap
     *            oltMap
     * @param entityId
     */
    void batchInsertOltPowerAttribute(Long entityId, List<OltPowerAttribute> list);

    /**
     * 批量插入以及更新Fan属性
     * 
     * @param list
     *            风扇属性列表
     * @param oltMap
     *            oltMap
     * @param entityId
     */
    void batchInsertOltFanAttribute(Long entityId, List<OltFanAttribute> list);

    /**
     * 批量插入以及更新Fan状态
     * 
     * @param list
     *            风扇属性列表
     * 
     */
    void batchInsertOltFanStatus(final List<OltFanStatus> list);

    /**
     * 批量插入以及更新Slo状态
     * 
     * @param list
     *            槽位属性列表
     * 
     */
    void batchInsertOltSlotStatus(final List<OltSlotStatus> list);

    /**
     * 批量插入以及更新Power状态
     * 
     * @param list
     *            电源属性列表
     * 
     */
    void batchInsertOltPowerStatus(final List<OltPowerStatus> list);

    /**
     * 获得主备控板的slot id
     * 
     * @param entityId
     * @return
     */
    List<Integer> getMpuaBoardList(Long entityId);

    /**
     * 修改板卡温度探测使能
     * 
     * @param slotId
     * @param slotBdTempDetectEnable
     */
    void updateSlotBdTempDetectEnable(Long slotId, Integer slotBdTempDetectEnable);

    /**
     * 修改板卡温度
     * 
     * @param slotId
     * @param slotBdTemperature
     */
    void updateBdTemperature(Long slotId, Integer temperature);

    /**
     * 刷新设备的电源属性
     * 
     * @param entityId
     * @param powerAttributes
     * @param powerStatus
     */
    void updateOltPower(Long entityId, List<OltPowerAttribute> powerAttributes, List<OltPowerStatus> powerStatus);

    /**
     * 刷新设备的风扇属性
     * 
     * @param entityId
     * @param fanAttributes
     * @param fanStatus
     */
    void updateOltFan(Long entityId, List<OltFanAttribute> fanAttributes, List<OltFanStatus> fanStatus);

    /**
     * 修改板卡服务状态
     * 
     * @param entityId
     * @param slotIndex
     * @param operationStatus
     */
    void updateOltBoardOperationStatus(Long entityId, Long slotIndex, Integer operationStatus);

    /**
     * 批量插入以及更新Slot属性
     * 
     * @param list
     *            槽位属性列表
     * @param oltMap
     *            oltMap
     */
    void batchInsertSlotAttribute(final List<OltSlotAttribute> list, HashMap<Long, Long> oltMap);

    /**
     * 更新板卡信息
     * 
     * @param entityId
     * @param slotAttribute
     */
    void updateSlotAttribute(Long entityId, OltSlotAttribute slotAttribute);

    /**
     * 更新板卡状态
     * 
     * @param slotId
     * @param status
     */
    void updateSlotStatus(Long slotId, Integer status);

    /**
     * 
     * 
     * @param entityId
     * @return
     */
    List<Long> getV152MpuSlotIndex(Long entityId);

    /**
     * 
     * 
     * @param entityId
     * @param V152MpuSlotIndex
     */
    void deleteV152MpuSlot(Long entityId, List<Long> V152MpuSlotIndex);

    /**
     * @param slotId
     * @param bAttribute
     * @param slotNo
     */
    void updateMasterOrSlaveStatus(Long slotId, Integer bAttribute, Long slotNo);

    /**
     * @param slotIndex
     * @param entityId
     */
    Long getSlotNoByIndex(Long slotIndex, Long entityId);

    /**
     * getMasterSlotIndex
     * 
     * @param entityId
     * @return
     */
    Long getMasterSlotNo(Long entityId);

    /**
     * 批量插入板卡映射关系
     * @param slotMapList
     */
    void batchInsertSlotMap(List<OltSlotMapTable> slotMapList, Long entityId);

    /**
     * 查询板卡逻辑槽位
     * @param paramsMap
     * @return
     */
    int querySlotLogNo(Map<String, Object> paramsMap);

    void updateBoardLampStatus(Long slotId, Integer lampStatus);

    Long getSlotIdByPhyNo(Long entityId, Long slotNo);

    List<Long> getEntityIdBySlotId(Long slotId);

    /**
     * @param entityId
     * @return
     */
    List<OltSlotAttribute> selectSlotList(Long entityId);

    Long getSlotIdByIndex(Long entityId, Long slotIndex);

    void updateBoardAlarmStatus(Long slotId, Integer bAlarmStatus);

    Integer querySlotPreType(Map<String, Object> paramsMap);

    List<BoardInfo> selectSlotListForTl1(long entityId);

    Integer querySlotActualType(HashMap<String, Object> paramsMap);

}
