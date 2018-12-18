/***********************************************************************
 * $Id: OltPerfService.java,v1.0 2013-10-25 下午3:43:54 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.epon.olt.domain.OltFanAttribute;
import com.topvision.ems.epon.olt.domain.OltPonAttribute;
import com.topvision.ems.epon.olt.domain.OltPortInfo;
import com.topvision.ems.epon.olt.domain.OltSlotAttribute;
import com.topvision.ems.epon.olt.domain.OltSniAttribute;
import com.topvision.ems.epon.onu.domain.OltOnuPonAttribute;
import com.topvision.ems.epon.onu.domain.OltUniAttribute;
import com.topvision.framework.annotation.DynamicDB;
import com.topvision.framework.service.Service;
import com.topvision.platform.util.highcharts.domain.Point;

/**
 * @author flack
 * @created @2013-10-25-下午3:43:54
 *
 */
public interface OltPerfGraphService extends Service {

    /**
     * 获得设备板卡列表
     * 
     * @param entityId    
     * @return List<OltSlotAttribute>
     */
    List<OltSlotAttribute> queryOltSlotListByEntityId(Long entityId);

    /**
     * 获得设备风扇列表
     * 
     * @param entityId    
     * @return List<OltFanAttribute>
     */
    List<OltFanAttribute> queryOltFanListByEntityId(Long entityId);

    /**
     * 获得设备SNI端口列表
     * 
     * @param entityId    
     * @return List<OltSniAttribute>
     */
    List<OltSniAttribute> queryOltSniListByEntityId(Long entityId);

    /**
     * 获得设备PON端口列表
     * 
     * @param entityId    
     * @return List<OltPonAttribute>
     */
    List<OltPonAttribute> queryOltPonListByEntityId(Long entityId);

    /**
     * 获得设备ONU PON端口列表
     * 
     * @param entityId    
     * @return List<OltOnuPonAttribute>
     */
    List<OltOnuPonAttribute> queryOltOnuPonListByEntityId(Long entityId);

    /**
     * 获得设备UNI端口列表
     * 
     * @param entityId    
     * @return List<OltUniAttribute>
     */
    List<OltUniAttribute> queryUniListByEntityId(Long entityId);

    /**
     * 获得设备光端口列表
     * 
     * @param entityId    
     * @return List<OltPortInfo>
     */
    List<OltPortInfo> queryEponPortListByEntityId(Long entityId);

    /**
     * 获得设备服务质量性能指标数据列表
     * 
     * @param entityId 
     * @param slotIndex 
     * @param perfTarget
     * @param startTime
     * @param endTime  
     * @return List<Point>
     */
    @DynamicDB
    List<Point> queryOltServicePerfPoints(Long entityId, Long slotIndex, String perfTarget, String startTime,
            String endTime);

    /**
     * 获得光链路质量性能指标数据列表
     * 
     * @param entityId 
     * @param portIndex 
     * @param perfTarget 
     * @param startTime
     * @param endTime   

     * @return List<Point>
     */
    @DynamicDB
    List<Point> queryOltOptLinkPerfPoints(Long entityId, Long portIndex, String perfTarget, String startTime,
            String endTime);

    /**
     * 获得端口流量性能指标数据列表
     * 
     * @param entityId 
     * @param portIndex 
     * @param perfTarget 
     * @param startTime
     * @param endTime  
     * @param direction   
     * @return List<Point>
     */
    @DynamicDB
    List<Point> queryPortFlowPerfPoints(Long entityId, Long portIndex, String perfTarget, String startTime,
            String endTime, Integer direction);

    /**
     * 获取所有的UNI列表
     * @param map
     * @return
     */
    List<OltUniAttribute> getOltUniList(Map<String, Object> map);

    /**
     * 获取所有的UNI数目
     * @param map
     * @return
     */
    int getOltUniCount(Map<String, Object> map);

    /**
     * 获取所有的ONU PON列表
     * @param map
     * @return
     */
    List<OltOnuPonAttribute> getOltOnuPonList(Map<String, Object> map);

    /**
     * 获取所有的ONU PON数目
     * @param map
     * @return
     */
    int getOltOntPonCount(Map<String, Object> map);

    /**
     * 获得端口流量性能指标数据列表
     * 
     * @param entityId 
     * @param startTime
     * @param endTime  
     * @return List<Point>
     */
    @DynamicDB
    List<Point> queryOltRelayPerfPoints(Long entityId, String startTime, String endTime);
}
