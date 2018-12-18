/***********************************************************************
 * $Id: OltConfigInfoService.java,v1.0 2013-10-26 上午9:39:31 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.config.service;

import java.util.List;
import java.util.Set;

import com.topvision.ems.epon.domain.OltVlanInterface;
import com.topvision.ems.epon.domain.Room;
import com.topvision.ems.epon.olt.domain.OltAttribute;
import com.topvision.ems.epon.vlan.domain.VlanAttribute;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.framework.service.Service;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author flack
 * @created @2013-10-26-上午9:39:31
 *
 */
public interface OltConfigInfoService extends Service {
    /**
     * 获取所有机房列表信息
     * 
     * @return Set<Room>
     */
    Set<Room> getLocationList();

    /**
     * 修改OLT设备信息
     * 
     * @param entity
     *            设备实体
     * @param oltAttribute
     * @param param
     */
    void modifyOltBaseInfo(Entity entity, OltAttribute oltAttribute, Integer modifyOltBaseInfo, SnmpParam param);

    /**
     * 更新DB中OLT设备IP
     * 
     * @param entity
     *            设备实例
     */
    void updateOltEntityIp(Entity entity);

    /**
     * 保存基本信息
     * 
     * @param baseInfo
     * @param entity
     */
    void saveBasicConfig(OltAttribute baseInfo, Entity entity);

    /**
     * 修改OLT设备带内网管配置
     * 
     * @param oltAttribute
     * @param param
     */
    void modifyInBandConfig(OltAttribute oltAttribute, SnmpParam param);

    /**
     * 修改OLT设备带外网管配置
     * 
     * @param oltAttribute
     * @param param
     */
    void modifyOutBandConfig(OltAttribute oltAttribute, SnmpParam param);

    /**
     * 修改OLT设备SNMP配置
     * 
     * @param oltAttribute
     * @param param
     */
    void modifyOltSnmpConfig(OltAttribute oltAttribute, SnmpParam param);

    /**
     * 更新网管SNMP参数
     * 
     * @param snmpParam
     *            网管SNMP参数
     */
    void updateEmsSnmpparam(SnmpParam snmpParam);

    /**
     * Add by lzt
     * 
     * 修改设备SNMPV2C config
     * 
     * @param oltAttribute
     * @param param
     */
    void modifySnmpV2CConfig(OltAttribute oltAttribute, SnmpParam param);

    /**
     * 获得VLAN 列表
     * 
     * @param entityId
     * @return
     */
    List<VlanAttribute> getAvailableVlanIndexList(Long entityId);

    /**
     * 获得VLAN虚接口列表
     * 
     * @param entityId
     * @return
     */
    List<OltVlanInterface> getVlanInterfaceList(Long entityId);

    /**
     * 保存带内信息
     * 
     * @param entityId
     * @param ipMaskInfo
     * @param param
     */
    void modifyInBandConfig(Long entityId, OltAttribute ipMaskInfo, SnmpParam param);

    /**
     * 更改entityAddress
     * 
     * @param entityId
     * @param ip
     * @param oldIp
     */
    void updateEntityIpAddress(Long entityId, String ip, String oldIp);

    /**
     * 保存网关配置
     * 
     * @param entityId
     * @param gate
     * @param param
     */
    void saveGate(Long entityId, OltAttribute gate, SnmpParam param);

    /**
     * 保存带内参数
     * 
     * @param inbandInfo
     * @param param
     */
    void modifyInBandParamInfo(OltAttribute inbandInfo, SnmpParam param);

    /**
     * 保存带外参数
     * 
     * @param ipMaskInfo
     * @param param
     */
    void updateOutbandParamInfo(OltAttribute ipMaskInfo, SnmpParam param);

    /**
     * 刷新Olt的配置信息
     * @param entityId
     */
    void refreshOltConfigInfo(Long entityId);
}
