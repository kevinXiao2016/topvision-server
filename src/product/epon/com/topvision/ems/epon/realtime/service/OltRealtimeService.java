/***********************************************************************
 * $Id: OltRealtimeService.java,v1.0 2014-7-12 上午9:47:45 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.realtime.service;

import java.util.List;

import com.topvision.ems.epon.realtime.domain.ChannelCmNumInfo;
import com.topvision.ems.epon.realtime.domain.OltBaseInfo;
import com.topvision.ems.epon.realtime.domain.OltCmTotalInfo;
import com.topvision.ems.epon.realtime.domain.OltCurrentCmcInfo;
import com.topvision.ems.epon.realtime.domain.OltPonInfo;
import com.topvision.ems.epon.realtime.domain.OltPonTotalInfo;
import com.topvision.ems.epon.realtime.domain.OltPortSpeedInfo;
import com.topvision.ems.epon.realtime.domain.OltSlotInfo;
import com.topvision.ems.epon.realtime.domain.OltSniInfo;
import com.topvision.ems.epon.realtime.domain.OltSubDeviceInfo;
import com.topvision.ems.epon.realtime.domain.OltSubTotalInfo;
import com.topvision.ems.epon.realtime.domain.OltSummaryInfo;
import com.topvision.ems.epon.realtime.domain.StatisticsResult;
import com.topvision.framework.service.Service;

/**
 * @author flack
 * @created @2014-7-12-上午9:47:45
 *
 */
public interface OltRealtimeService extends Service {

    /**
     * 获取Olt设备基本信息
     * @param entityId
     * @return
     */
    OltBaseInfo getOltBaseInfo(Long entityId);

    /**
     * 获取Olt板卡信息
     * @param entityId
     * @return
     */
    List<OltSlotInfo> getOltSlotInfo(Long entityId);

    /**
     * 获取Pon口统计信息
     * @param entityId
     * @return
     */
    List<OltPonTotalInfo> getPonTotalInfo(Long entityId);

    /**
     * 获取Sni口统计信息
     * @param entityId
     * @return
     */
    List<OltSniInfo> getSniTotalInfo(Long entityId);

    /**
     * 获取OLT SNI口信息
     * @param entityId
     * @return
     */
    List<OltSniInfo> getOltSniInfo(Long entityId);

    /**
     * 获取下级设备统计信息
     * @param entityId
     * @return
     */
    List<OltSubTotalInfo> getSubTotalInfo(Long entityId);

    /**
     * 获取Pon口详细信息
     * @param entityId
     * @return
     */
    List<OltPonInfo> getOltPonInfo(Long entityId, boolean onlineFlag);

    /**
     * 获取下级设备详细信息
     * @param entityId
     * @return
     */
    List<OltSubDeviceInfo> getOltSubInfo(Long entityId, boolean onlineFlag);

    /**
     * 从设备获取下级设备信息
     * @param entityId
     * @return
     */
    List<OltSubDeviceInfo> getSubInfoList(Long entityId);

    /**
     * 获取OLT下的CM统计信息
     * @param entityId
     * @return
     */
    OltCmTotalInfo getCmTotalInfo(Long entityId);

    /**
     * 获取端口的速率信息
     * @return
     */
    OltPortSpeedInfo getPortSpeedInfo(OltPortSpeedInfo speedInfo);

    /**
     * 获取光功率阈值
     * @param entityId
     * @return
     */
    StatisticsResult getOpticalThreshold(Long entityId);

    /**
     * 获取Olt下A/C-A信息
     * @param snmpParam
     * @return
     */
    List<OltCurrentCmcInfo> getOltCurrentCmcInfo(Long entityId);

    /**
     *  获取Olt概要信息
     * @param entityId
     * @return
     */
    OltSummaryInfo getOltSummaryInfo(Long entityId);

    /**
     * 从设备获取基于信道统计的Cm数量
     * @param snmpParam
     * @return
     */
    List<ChannelCmNumInfo> getChannelCmNum(Long entityId);

}
