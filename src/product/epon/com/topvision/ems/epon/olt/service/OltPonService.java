/***********************************************************************
 * $Id: OltService.java,v1.0 2013-10-25 上午10:25:28 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.olt.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.epon.olt.domain.OltPonAttribute;
import com.topvision.ems.epon.olt.domain.OltPonStormSuppressionEntry;
import com.topvision.ems.epon.olt.domain.TopPonPortRateLimit;
import com.topvision.ems.epon.olt.domain.TopPonPortSpeedEntry;
import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.framework.service.Service;

/**
 * @author flack
 * @created @2013-10-25-上午10:25:28
 *
 */
public interface OltPonService extends Service {
    /**
     * 获取PON口属性
     * 
     * @param ponId
     *            pon口Id
     * @return OltPonAttribute
     */
    OltPonAttribute getPonAttribute(Long ponId);

    /**
     * 设置PON口使能状态
     * 
     * @param entityId
     *            设备ID
     * @param ponId
     *            pon口ID
     * @param adminStatus
     *            PON口使能状态
     */
    void setPonAdminStatus(Long entityId, Long ponId, Integer adminStatus);

    /**
     * 设置PON口隔离使能状态
     * 
     * @param entityId
     *            设备ID
     * @param ponId
     *            pon口ID
     * @param isolationStatus
     *            隔离使能状态号
     */
    void setPonIsolationStatus(Long entityId, Long ponId, Integer isolationStatus);

    /**
     * 设置PON口加密模式
     * 
     * @param entityId
     *            设备ID
     * @param ponId
     *            pon口ID
     * @param encryptMode
     *            加密模式
     * @param exchangeTime
     *            密钥交换时间
     */
    void setPonPortEncryptMode(Long entityId, Long ponId, Integer encryptMode, Integer exchangeTime);

    /**
     * 获取PON口广播风暴参数
     * 
     * @param ponId
     *            PON口ID
     * @return OltPonStormSuppressionEntry
     */
    OltPonStormSuppressionEntry getPonStormSuppression(Long ponId);

    /**
     * 设置PON口MAC最大学习数
     * 
     * @param entityId
     *            设备ID
     * @param ponId
     *            pon口ID
     * @param maxLearnMacNum
     *            最大学习数
     */
    void setPonMaxLearnMacNum(Long entityId, Long ponId, Long maxLearnMacNum);

    /**
     * 设置PON口15分钟性能统计使能状态
     * 
     * @param entityId
     *            设备ID
     * @param ponId
     *            pon口ID
     * @param perfStatus
     *            PON口15分钟性能统计使能状态
     */
    void setPon15MinPerfStatus(Long entityId, Long ponId, Integer perfStatus);

    /**
     * 批量设置PON口15分钟性能统计使能状态
     * 
     * @param entityId
     * @param ponAttributes
     * 
     */
    void updateEntityPon15MinPerfStatus(Long entityId, List<OltPonAttribute> ponAttributes);

    /**
     * 设置PON口24小时性能统计使能状态
     * 
     * @param entityId
     *            设备ID
     * @param ponId
     *            pon口ID
     * @param perfStatus
     *            PON口24小时性能统计使能状态
     */
    void setPon24HourPerfStatus(Long entityId, Long ponId, Integer perfStatus);

    /**
     * PON口的广播风暴抑制
     * 
     * @param oltPonStormSuppressionEntry
     *            PON口广播风暴参数
     */
    void modifyPonStormInfo(OltPonStormSuppressionEntry oltPonStormSuppressionEntry);

    /**
     * 获取PON风暴抑制信息
     * 
     * @param entityId
     */
    void refreshOltPonStormSuppressionEntry(Long entityId);

    /**     * 通过ponId获得ponIndex
     * 
     * @param id
     * @return
     */
    Long getPonIndex(Long id);

    /**
     * 设置PON口上下线速率
     * 
     * @param oltFileAttribute
     * @return
     */
    void setPonPortRateLimit(TopPonPortRateLimit topPonPortRateLimit);

    /**
     * Add by lzt
     * 
     * 获得PON端口工作模式
     * 
     * @param entityId
     * @param ponId
     */
    TopPonPortSpeedEntry getPonPortSpeedMode(Long entityId, Long ponId);

    /**
     * Add by lzt
     * 
     * 修改PON端口工作模式
     * 
     * @param entityId
     * @param ponId
     * @param speedMode
     */
    void modifyPonPortSpeedMode(Long entityId, Long ponId, Integer speedMode);

    /**
     * Add by lzt
     * 
     * 通过PON Index获得pon Id
     * 
     * @param entityId
     * @param ponIndex
     */
    Long getPonIdByIndex(Long entityId, Long ponIndex);

    /**
     * 获取所有PON口索引
     * 
     * @param entityId
     *            设备ID
     * @return
     */
    List<Long> getAllPonIndex(Long entityId);

    /**
     * 获取所有EPON口索引
     * 
     * @param entityId
     *            设备ID
     * @return
     */
    List<Long> getAllEponIndex(Long entityId);

    /**
     * 获取指定PON口下ONU列表
     * 
     * @param ponIndexList
     *            PON口索引列表
     * @param onuType
     *            ONU类型
     * @return List<OltOnuAttribute>
     */
    List<OltOnuAttribute> getOnuAttributeByPonIndexs(Long entityid, List<Long> ponIndexList, Integer onuType);

    /**
     * 查询所有PON端口信息
     * 
     * @return
     */
    List<OltPonAttribute> getPonPortList(Map<String, Object> map);

    /**
     * 更新所有Pon端口15分钟性能统计状态
     * 
     * @return
     */
    void updatePonPort15MinStatus(Long entityId);

    /**
     * 更新某一个Pon端口15分钟性能统计状态
     * @param entityId
     * @param targetIndex
     * @param status
     */
    void updatePon15PerfStatus(Long entityId, Long targetIndex, Integer status);

    /**
     * 拓扑时刷新OLT所有PON口的广播风暴抑制信息
     * @param entityId
     */
    void refreshOltPonStormInfo(Long entityId);

    /**
     * 刷新PON口速率
     * @param entityId
     */
    void refreshOltPonPortSpeed(Long entityId);

    /**
     * 获取OLT设备PON口列表
     * @param entityId
     * @return
     */
    List<OltPonAttribute> getOltPonList(Long entityId);

    /**
     * PON口复位
     * 先去使能,过段时间再使能
     * @param entityId
     * @param ponId
     */
    void resetPon(Long entityId, Long ponId);

    void refreshAllPonStatus(Long entityId);

    OltPonAttribute refreshPonStatus(Long entityId, Long ponIndex);

    OltPonAttribute loadPonPortLmt(Long entityId, Long ponId);
}
