/***********************************************************************
 * $Id: OltService.java,v1.0 2013-10-25 上午10:25:28 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.olt.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.topvision.ems.epon.domain.EponBoardStatistics;
import com.topvision.ems.epon.olt.domain.OltFanAttribute;
import com.topvision.ems.epon.olt.domain.OltPonAttribute;
import com.topvision.ems.epon.olt.domain.OltSlotAttribute;
import com.topvision.ems.epon.olt.domain.OltSniAttribute;
import com.topvision.ems.epon.onu.domain.OltUniAttribute;
import com.topvision.framework.service.Service;

/**
 * @author flack
 * @created @2013-10-25-上午10:25:28
 *
 */
public interface OltSlotService extends Service {
    /**
     * 设置板卡预配置类型
     * 
     * @param entityId
     *            设备ID
     * @param slotId
     *            槽位ID
     * @param boardPreConfigType
     *            预配置类型
     */
    void setOltBdPreConfigType(Long entityId, Long slotId, Integer boardPreConfigType);

    /**
     * 刷新槽位信息
     * 
     * @param entityId
     *            设备ID
     * @param slotId
     *            槽位ID
     */
    void refreshBoardInfo(Long entityId, Long slotId);

    /**
     * 板卡复位
     * 
     * @param entityId
     *            设备ID
     * @param boardId
     *            槽位ID
     */
    void resetOltBoard(Long entityId, Long boardId);

    /**
     * 设置风扇转速
     * 
     * @param entityId
     *            设备ID
     * @param fanCardId
     *            风扇板卡ID
     * @param oltFanSpeedControlLevel
     *            风扇转速等级
     */
    void setOltFanSpeedControl(Long entityId, Long fanCardId, Integer oltFanSpeedControlLevel);

    /**
     * 获取OLT设备槽位列表
     * 
     * @param entityId
     *            设备ID
     * @return List<OltSlotAttribute>
     */
    List<OltSlotAttribute> getOltSlotList(Long entityId);

    /**
     * 获取OLT设备PON板卡列表
     * 
     * @param entityId
     *            设备ID
     * @return List<OltSlotAttribute>
     */
    List<OltSlotAttribute> getOltPonSlotList(Long entityId);

    /**
     * 获取OLT设备EPON板卡列表
     * 
     * @param entityId
     *            设备ID
     * @return List<OltSlotAttribute>
     */
    List<OltSlotAttribute> getOltEponSlotList(Long entityId);

    /**
     * 获取OLT设备GPON板卡列表
     * 
     * @param entityId
     *            设备ID
     * @return List<OltSlotAttribute>
     */
    List<OltSlotAttribute> getOltGponSlotList(Long entityId);

    /**
     * 获取单个槽位上的PON列表
     * 
     * @param slotId
     *            槽位ID
     * @return List<OltPonAttribute>
     */
    List<OltPonAttribute> getSlotPonList(Long slotId);

    /**
     * 获取单个槽位上的SNI列表
     * 
     * @param slotId
     *            槽位ID
     * @return List<OltSniAttribute>
     */
    List<OltSniAttribute> getSlotSniList(Long slotId);

    /**
     * 获取ONU上的UNI列表
     * 
     * @param onuId
     *            ONUID
     * @return List<OltUniAttribute>
     */
    List<OltUniAttribute> getSlotUniList(Long onuId);

    /**
     * 获得主备控板的slot id
     * 
     * @param entityId
     * @return
     */
    List<Integer> getMpuaBoardList(Long entityId);

    /**
     * 刷新板卡温度
     * 
     * @param entityId
     * @param slotId
     */
    Integer refreshBdTemperature(Long entityId, Long slotId);

    /**
     * 设置板卡状态
     * 
     * @param entityId
     * @param slotId
     * @param stauts
     */
    void setBoardAdminStatus(Long entityId, Long slotId, Integer stauts);

    /**
     * 获取风扇转速
     * 
     * @param entityId
     */
    Integer getFanRealSpeed(Long entityId, Long fanCardId);

    /**
     * 查询所有板卡信息
     * 
     * @return
     */
    List<EponBoardStatistics> getBoardList(Map<String, Object> map);

    /**
     * 修改板卡温度探测使能
     * 
     * @param entityId
     * @param slotId
     * @param slotBdTempDetectEnable
     */
    void updateSlotBdTempDetectEnable(Long entityId, Long slotId, Integer slotBdTempDetectEnable);

    /**
     * 开启设备所有板卡温度探测使能
     * 
     * @param entityId
     */
    void updataEntitySlotTempDetectEnable(List<OltSlotAttribute> slots);

    /**
     * 获取风扇属性
     * 
     * @param fanCardId
     *            风扇板卡ID
     * @return OltFanAttribute
     */
    OltFanAttribute getFanAttribute(Long fanCardId);

    /**
     * 获得板卡slotNo
     * 
     * @param entityId
     * @param slotIndex
     */
    Long getSlotNoByIndex(Long slotIndex, Long entityId);

    void refreshBoardLampStatus(Long entityId, Long phySLotNo, Long slotIndex);

    /**
     * 删除板卡
     * @param entityId
     * @param slotId
     * @param boardPreConfigType
     */
    void deleteOltSlot(Long entityId, Long slotId, Integer boardPreConfigType);

    /**
     * 插入或更改版本属性,以及设备版本
     * @param slots
     * @param oltMap
     */
    void batchInsertOrUpdateSlotAttribute(List<OltSlotAttribute> slots, HashMap<Long, Long> oltMap);

    boolean slotIsControlBooard(Long entityId, Integer slotNo);

    void refreshBoardAlarmStatus(Long entityId, Long slotIndex);

    boolean slotIsGponBoard(Long entityId, Integer slotNo);

    /**
     * 获取板卡实际类型
     * @param entityId
     * @param slotNo
     * @return 
     */
    Integer getOltSlotActualType(Long entityId, Integer slotNo);

}
