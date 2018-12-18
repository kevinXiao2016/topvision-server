/***********************************************************************
 * $Id: OltPerfGraphDao.java,v1.0 2013-8-6 下午03:08:48 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.epon.olt.domain.OltFanAttribute;
import com.topvision.ems.epon.olt.domain.OltPonAttribute;
import com.topvision.ems.epon.olt.domain.OltPortInfo;
import com.topvision.ems.epon.olt.domain.OltSlotAttribute;
import com.topvision.ems.epon.olt.domain.OltSniAttribute;
import com.topvision.ems.epon.onu.domain.OltOnuPonAttribute;
import com.topvision.ems.epon.onu.domain.OltUniAttribute;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.framework.dao.BaseEntityDao;
import com.topvision.platform.util.highcharts.domain.Point;

/**
 * @author lizongtian
 * @created @2013-8-6-下午03:08:48
 *
 */
public interface OltPerfGraphDao extends BaseEntityDao<Entity> {

    /**
     * 加载设备板卡列表
     * 
     * @param entityId
     * @return List<OltSlotAttribute>
     */
    List<OltSlotAttribute> selectOltSlotListByEntityId(Long entityId);

    /**
     * 加载设备风扇列表
     * 
     * @param entityId
     * @return List<OltFanAttribute>
     */
    List<OltFanAttribute> selectOltFanListByEntityId(Long entityId);

    /**
     * 加载设备SNI端口列表
     * 
     * @param entityId
     * @return List<OltSniAttribute>
     */
    List<OltSniAttribute> selectOltSniListByEntityId(Long entityId);

    /**
     * 加载设备PON端口列表
     * 
     * @param entityId
     * @return List<OltPonAttribute>
     */
    List<OltPonAttribute> selectOltPonListByEntityId(Long entityId);

    /**
     * 加载ONU PON端口列表
     * 
     * @param entityId
     * @return List<OltOnuPonAttribute>
     */
    List<OltOnuPonAttribute> selectOltOnuPonListByEntityId(Long entityId);

    /**
     * 加载ONU UNI端口列表
     * 
     * @param entityId
     * @return List<OltUniAttribute>
     */
    List<OltUniAttribute> selectOltUniListByEntityId(Long entityId);

    /**
     * 加载光端口列表
     * 
     * @param entityId
     * @return List<OltPortInfo>
     */
    List<OltPortInfo> selectOltFirberListByEntityId(Long entityId);

    /**
     * 加载板卡CPU利用率性能数据
     * 
     * @param entityId
     * @param slotIndex
     * @param startTime
     * @param endTime
     * @return List<Point>
     */
    List<Point> selectOltCpuUsedPoints(Long entityId, Long slotIndex, String startTime, String endTime);

    /**
     * 加载板卡内存利用率性能数据
     * 
     * @param entityId
     * @param slotIndex
     * @param startTime
     * @param endTime
     * @return List<Point>
     */
    List<Point> selectOltMemUsedPoints(Long entityId, Long slotIndex, String startTime, String endTime);

    /**
     * 加载板卡Flash利用率性能数据
     * 
     * @param entityId
     * @param slotIndex
     * @param startTime
     * @param endTime
     * @return List<Point>
     */
    List<Point> selectOltFlashUsedPoints(Long entityId, Long slotIndex, String startTime, String endTime);

    /**
     * 加载板卡温度性能数据
     * 
     * @param entityId
     * @param slotIndex
     * @param startTime
     * @param endTime
     * @return List<Point>
     */
    List<Point> selectOltBoardTempPoints(Long entityId, Long slotIndex, String startTime, String endTime);

    /**
     * 加载风扇转速性能数据
     * 
     * @param entityId
     * @param slotIndex
     * @param startTime
     * @param endTime
     * @return List<Point>
     */
    List<Point> selectOltFanSpeedPoints(Long entityId, Long slotIndex, String startTime, String endTime);

    /**
     * 加载光口链路质量性能数据
     * 
     * @param entityId
     * @param slotIndex
     * @param perfTarget
     * @param startTime
     * @param endTime
     * @return List<Point>
     */
    List<Point> selectOltOptLinkPerfPoints(Long entityId, Long slotIndex, String perfTarget, String startTime,
            String endTime);

    /**
     * 加载端口流量性能数据
     * 
     * @param entityId
     * @param slotIndex
     * @param perfTarget
     * @param startTime
     * @param endTime
     * @param direction
     * @return List<Point>
     */
    List<Point> selectPortFlowPerfPoints(Long entityId, Long slotIndex, String perfTarget, String startTime,
            String endTime, Integer direction);

    /**
     * 获取所有的UNI列表
     * @param map
     * @return
     */
    List<OltUniAttribute> queryOltUniList(Map<String, Object> map);

    /**
     * 获取UNI数目
     * @param map
     * @return
     */
    int queryOltUniCount(Map<String, Object> map);

    /**
     * 获取所有的ONU PON口列表
     * @param map
     * @return
     */
    List<OltOnuPonAttribute> queryOltOnuPonList(Map<String, Object> map);

    /**
     * 获取所有的ONU PON口数目
     * @param map
     * @return
     */
    int queryOltOntPonCount(Map<String, Object> map);

    /**
     * 加载设备响应延时
     * 
     * @param entityId
     * @param startTime
     * @param endTime
     * @return List<Point>
     */
    List<Point> queryOltRelayPerfPoints(Long entityId, String startTime, String endTime);
}
