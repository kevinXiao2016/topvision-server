/***********************************************************************
 * $Id: OltPonDao.java,v1.0 2013-10-25 上午10:15:11 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.olt.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.topvision.ems.epon.olt.domain.OltPonAttribute;
import com.topvision.ems.epon.olt.domain.OltPonStormSuppressionEntry;
import com.topvision.ems.epon.olt.domain.OltSlotAttribute;
import com.topvision.ems.epon.olt.domain.TopPonPortSpeedEntry;
import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author flack
 * @created @2013-10-25-上午10:15:11
 *
 */
public interface OltPonDao extends BaseEntityDao<Object> {
    /**
     * 更新PON口使能状态
     * 
     * @param ponId
     *            PON口ID
     * @param adminStatus
     *            PON口使能状态
     */
    void updatePonAdminStatus(Long ponId, Integer adminStatus);

    /**
     * 更新PON口隔离使能状态
     * 
     * @param ponId
     *            PON口ID
     * @param adminStatus
     *            PON口隔离使能状态
     */
    void updatePonIsolationStatus(Long ponId, Integer adminStatus);

    /**
     * 更新PON口加密模式
     * 
     * @param ponId
     *            PON口ID
     * @param encryptMode
     *            PON口加密模式
     * @param exchangeTime
     *            密钥交换时间
     */
    void updatePonPortEncryptMode(Long ponId, Integer encryptMode, Integer exchangeTime);

    /**
     * 获取PON口广播风暴参数
     * 
     * @param ponId
     *            PON口ID
     * @return OltPonStormSuppressionEntry
     */
    OltPonStormSuppressionEntry getPonStormSuppression(Long ponId);

    /**
     * 更新PON口MAC最大学习数
     * 
     * @param ponId
     *            PON口ID
     * @param maxLearnMacNum
     *            PON口MAC学习最大数
     */
    void updatePonMaxLearnMacNum(Long ponId, Long maxLearnMacNum);

    /**
     * 更新PON口15分钟性能统计使能状态
     * 
     * @param ponId
     *            PON口ID
     * @param adminStatus
     *            PON口15分钟性能统计使能状态
     */
    void updatePon15MinPerfStatus(Long ponId, Integer adminStatus);

    /**
     * 更新PON口24小时性能统计使能状态
     * 
     * @param ponId
     *            PON口ID
     * @param adminStatus
     *            PON口24小时性能统计使能状态
     */
    void updatePon24HourPerfStatus(Long ponId, Integer adminStatus);

    /**
     * Added by lzt
     * 
     * 更新PON端口工作模式
     * 
     * @param entityId
     * @param ponId
     * @param speedMode
     */
    void modifyPonPortSpeedMode(Long entityId, Long ponId, Integer speedMode);

    /**
     * Added by lzt
     * 
     * 获得PON端口工作模式
     * 
     * @param entityId
     * @param ponId
     */
    TopPonPortSpeedEntry getPonPortSpeedMode(Long entityId, Long ponId);

    /**
     * 获取PON口属性
     * 
     * @param ponId
     *            PON口ID
     * @return OltPonAttribute
     */
    OltPonAttribute getPonAttribute(Long ponId);

    /**
     * 获取PON口索引
     * 
     * @param ponId
     *            PON口ID
     * @return Long
     */
    Long getPonIndex(Long ponId);

    /**
     * 获取所有PON口索引
     * 
     * @param entityId
     *            设备ID
     * @return List<Long>
     */
    List<Long> getAllPonIndex(Long entityId);

    /**
     * 获取所有EPON口索引
     * 
     * @param entityId
     *            设备ID
     * @return List<Long>
     */
    List<Long> getAllEponIndex(Long entityId);

    /**
     * 获得设备上所有PON板
     * 
     * @param entityId
     *            设备ID
     * @return List<OltSlotAttribute>
     */
    List<OltSlotAttribute> getOltPonList(Long entityId);

    /**
     * 获得设备上所有EPON板
     * 
     * @param entityId
     *            设备ID
     * @return List<OltSlotAttribute>
     */
    List<OltSlotAttribute> getOltEponList(Long entityId);

    /**
     * 获得设备上所有GPON板
     * 
     * @param entityId
     *            设备ID
     * @return List<OltSlotAttribute>
     */
    List<OltSlotAttribute> getOltGponList(Long entityId);

    /**
     * 插入更新PON口基本信息
     * 
     * @param oltPonAttribute
     *            PON口属性
     */
    void insertOrUpdateOltPonAttribute(OltPonAttribute oltPonAttribute);

    /**
     * 更新PON口广播风暴抑制
     * 
     * @param oltPonStormSuppressionEntry
     *            PON口广播风暴参数
     */
    void updatePonStormInfo(OltPonStormSuppressionEntry oltPonStormSuppressionEntry);

    /**
     * 插入PON口广播风暴抑制
     * 
     * @param oltPonStormSuppressionEntry
     *            PON口广播风暴参数
     */
    void insertPonStormInfo(OltPonStormSuppressionEntry oltPonStormSuppressionEntry);

    /**
     * 批量插入以及更新PON口广播风暴抑制
     * 
     * @param list
     *            PON口广播风暴抑制
     */
    void batchInsertOltPonStormInfo(final List<OltPonStormSuppressionEntry> list);

    /**
     * 通过entityId和ponIndex获得ponId
     * 
     * @param entityId
     * @param ponIndex
     * @return
     */
    Long getPonIdByPonIndex(Long entityId, Long ponIndex);
    
    /**
     * 通过entityId和ponIndexs获得ponIds
     * 
     * @param entityId
     * @param ponIndexs
     * @return
     */
    List<Long> getPonIdsByPonIndexs(Long entityId, List<Long> ponIndexs);

    /**
     * PON口的最大带宽限制
     * 
     * @param ponId
     * @param bandMax
     */
    void updatePonBandMax(Long ponId, Integer bandMax);

    /**
     * PON口的上下行速率
     * 
     * @param ponId
     * @param upRatelimit
     * @param downRatelimit
     */
    void updatePonRateLimit(Long ponId, Integer upRatelimit, Integer downRatelimit);

    /**
     * 批量插入以及更新Pon属性
     * 
     * @param list
     *            PON口属性列表
     * @param oltMap
     *            oltMap
     */
    void batchInsertPonAttribute(final List<OltPonAttribute> list, HashMap<Long, Long> oltMap);

    /**
     * 批量插入以及更新PON口工作模式
     * 
     * @param list
     *            PON口速率
     */
    void batchInsertOltPonSpeed(final List<TopPonPortSpeedEntry> list, final Long entityId);

    /**
     * 查询所有PON端口信息
     * 
     * @return
     */
    List<OltPonAttribute> getPonPortList(Map<String, Object> map);

    /**
     * 获取指定PON口下ONU列表
     * 
     * @param ponIndexList
     *            PON口索引列表
     * @param onuType
     *            ONU类型
     * @return List<OltOnuAttribute>
     */
    List<OltOnuAttribute> getOnuAttributeByPonIndexs(Long entityId, List<Long> ponIndexList, Integer onuType);

    /**
     * 批量更新PON端口15min性能统计使能状态
     * 
     * @param oltPonAttrs
     */
    void batchUpdatePon15MinStatus(List<OltPonAttribute> oltPonAttrs);

    /**
     * 更新pon口操作状态
     * 
     * @param ponId
     * @param operationStatus
     */
    void updatePonOperationStatus(Long ponId, Integer ponOperationStatus);

    /**
     * 获得OLT PON端口列表
     * 
     * @param oltPonAttrs
     */
    List<OltPonAttribute> getPonListByEntityId(Long entityId);

    Integer getPonPortType(Long entityId, Long ponIndex);

}
