/***********************************************************************
 * $Id: OnuService.java,v1.0 2013-10-25 上午11:14:13 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.service;

import java.util.List;

import com.topvision.ems.epon.onu.domain.OltOnuComAttribute;
import com.topvision.ems.epon.onu.domain.OltOnuComStatics;
import com.topvision.ems.epon.onu.domain.OltOnuComVlanConfig;
import com.topvision.ems.epon.onu.domain.OltOnuMacMgmt;
import com.topvision.ems.epon.onu.domain.OltTopOnuCapability;
import com.topvision.framework.service.Service;

/**
 * @author flack
 * @created @2013-10-25-上午11:14:13
 *
 */
public interface ElectricityOnuService extends Service {
    /**
     * 设置ONU串口VLAN
     * 
     * @param param
     * @param onuComVlan
     */
    void setOnuComVlan(Long entityId, Integer onuComVlan);

    /**
     * 设置ONU的管理信息
     * 
     * @param param
     * @param onuIndex
     * @param onuIp
     * @param onuMask
     * @param onuGateway
     */
    void setOnuIpMaskInfo(Long entityId, Long onuIndex, String onuIp, String onuMask, String onuGateway);

    /**
     * 设置ONU的串口服务器基本信息
     * 
     * @param param
     * @param attribute
     */
    void setOnuComAttribute(Long entityId, OltOnuComAttribute attribute);

    /**
     * 实时获取ONU的串口统计信息
     * 
     * @param param
     * @param onuComIndex
     * @return
     */
    OltOnuComStatics getOnuComStatisc(Long entityId, Long onuComIndex);

    /**
     * 清除ONU的串口统计信息
     * 
     * @param param
     * @param onuComIndex
     */
    void cleanOnuComStatisc(Long entityId, Long onuComIndex);

    /**
     * PON口的业务切割
     * 
     * @param param
     * @param mainPonIndex
     * @param backPonIndex
     * @param action
     */
    void switchPonInfo(Long entityId, Long srcPonIndex, Long dstPonIndex);

    /**
     * 从数据库获得ONU的串口VLAN信息
     * 
     * @param param
     * @return
     */
    OltOnuComVlanConfig getOnuComVlan(Long entityId);

    /**
     * 刷新ONU的串口VLAN信息
     * 
     * @param entityId
     * @return
     */
    void refreshOnuComVlan(Long entityId);

    /**
     * 从数据库获取ONU的串口服务器信息
     * 
     * @param param
     * @param onuComIndex
     * @return
     */
    OltOnuComAttribute getOnuComAttribute(Long entityId, Long onuComIndex);

    /**
     * 刷新ONU的串口服务器信息
     * 
     * @param entityId
     * @param onuComIndex
     * @return
     */
    void refreshOnuComAttribute(Long entityId, Long onuComIndex);

    /**
     * 加载可用的PON口割接端口列表
     * 
     * @param entityId
     * @param ponCutOverPortIndex
     * @return
     */
    List<Long> loadPonCutOverPort(Long entityId, Long ponCutOverPortIndex);

    /**
     * 从数据库获取ONU MAC地址管理信息
     * 
     * @param entityId
     * @param onuIndex
     * @return
     */
    OltOnuMacMgmt getOnuMacMgmt(Long entityId, Long onuIndex);

    /**
     * 刷新ONU MAC地址管理信息
     * 
     * @param entityId
     * @param onuIndex
     */
    void refreshOnuMacMgmt(Long entityId, Long onuIndex);

    /**
     * 修改ONU MAC地址管理信息
     * 
     * @param oltOnuMacMgmt
     */
    void setOnuMacMgmt(OltOnuMacMgmt oltOnuMacMgmt);

    /**
     * 从数据库加载电力ONU的带内管理信息
     * 
     * @param entityId
     * @param onuIndex
     * @return
     */
    OltTopOnuCapability getElecOnuCapability(Long entityId, Long onuIndex);

    /**
     * 设置下行报文环回使能
     * 
     * @return
     * @throws Exception
     */
    void setUniDSLoopBackEnable(Long entityId, Long uniId, Integer uniDSLoopBackEnable);

    /**
     * 设置上行utag报文指定优先级
     * 
     * @throws Exception
     */
    void setUniUSUtgPri(Long entityId, Long uniId, Integer uniUSUtgPri);

    /**
     * 修改电力ONU的广播风暴抑制参数
     * 
     * @throws Exception
     */
    void modifyOnuStormOutPacketRate(Long entityId, Long uniId, Long unicastStormOutPacketRate,
            Long multicastStormOutPacketRate, Long broadcastStormOutPacketRate);

    /**
     * 刷新UNI口的上行untag报文的指定优先级
     * 
     * @param entityId
     *            , uniId
     */
    void refreshUniUSUtgPri(Long entityId, Long uniId);

    /**
     * PON口的最大带宽限制
     * 
     * @param ponId
     * @param bandMax
     */
    void modifyPonBandMax(Long ponId, Integer bandMax);

    /**
     * 配置ONU UNI端口环回使能
     * 
     * @param entityId
     * @param uniId
     * @param topUniLoopDetectEnable
     */
    void configUniLoopDetectEnable(Long entityId, Long uniId, int topUniLoopDetectEnable);

}
