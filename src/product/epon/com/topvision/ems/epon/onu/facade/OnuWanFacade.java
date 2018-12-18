/***********************************************************************
 * $Id: OnuWanFacade.java,v1.0 2016年6月28日 上午10:27:56 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.facade;

import java.util.List;

import com.topvision.ems.epon.onu.domain.OnuWanBind;
import com.topvision.ems.epon.onu.domain.OnuWanConfig;
import com.topvision.ems.epon.onu.domain.OnuWanConnect;
import com.topvision.ems.epon.onu.domain.OnuWanConnectStatus;
import com.topvision.ems.epon.onu.domain.OnuWanSsid;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author haojie
 * @created @2016年6月28日-上午10:27:56
 *
 */
@EngineFacade(serviceName = "OnuWanFacade", beanName = "onuWanFacade")
public interface OnuWanFacade {

    /**
     * 获取一个OLT下所有ONU WAN配置
     * 
     * @param snmpParam
     * @return
     */
    List<OnuWanConfig> getOnuWanConfig(SnmpParam snmpParam);

    /**
     * 获取单个ONU WAN配置
     * 
     * @param snmpParam
     * @param onuIndex
     * @return
     */
    OnuWanConfig getOnuWanConfig(SnmpParam snmpParam, Long onuIndex);

    /**
     * 获取一个OLT下所有ONU WAN SSID列表
     * 
     * @param snmpParam
     * @return
     */
    List<OnuWanSsid> getOnuWanSsids(SnmpParam snmpParam);

    /**
     * 获取单个ONU WAN SSID列表
     * 
     * @param snmpParam
     * @param onuIndex
     * @return
     */
    List<OnuWanSsid> getOnuWanSsids(SnmpParam snmpParam, Long onuIndex);

    /**
     * 获取一个OLT下所有ONU WAN 连接列表
     * 
     * @param snmpParam
     * @return
     */
    List<OnuWanConnect> getOnuWanConnects(SnmpParam snmpParam);

    /**
     * 获取单个ONU下 WAN 连接列表
     * 
     * @param snmpParam
     * @param onuIndex
     * @return
     */
    List<OnuWanConnect> getOnuWanConnects(SnmpParam snmpParam, Long onuIndex);

    /**
     * 获取一个OLT下所有ONU WAN STATUS列表
     * 
     * @param snmpParam
     * @return
     */
    List<OnuWanConnectStatus> getOnuWanConnectStatus(SnmpParam snmpParam);

    /**
     * 获取单个ONU下 WAN STATUS列表
     * 
     * @param snmpParam
     * @param onuIndex
     * @return
     */
    List<OnuWanConnectStatus> getOnuWanConnectStatus(SnmpParam snmpParam, Long onuIndex);

    /**
     * 对WIFI ONU进行恢复出厂设置
     * 
     * @param snmpParam
     * @param onuIndex
     */
    void restoreWifiOnu(SnmpParam snmpParam, Long onuIndex);

    /**
     * 修改ONU WAN配置信息
     * 
     * @param snmpParam
     * @param onuWanConfig
     */
    OnuWanConfig modifyOnuWanConfig(SnmpParam snmpParam, OnuWanConfig onuWanConfig);

    /**
     * 清除ONU WAN连接信息
     * 
     * @param snmpParam
     * @param onuIndex
     */
    void clearWanConnect(SnmpParam snmpParam, Long onuIndex);

    /**
     * 新增SSID
     * 
     * @param snmpParam
     * @param onuWanSsid
     */
    void addOnuWanSsid(SnmpParam snmpParam, OnuWanSsid onuWanSsid);

    /**
     * 修改SSID
     * 
     * @param snmpParam
     * @param onuWanSsid
     */
    void modifyOnuWanSsid(SnmpParam snmpParam, OnuWanSsid onuWanSsid);

    /**
     * 删除SSID
     * 
     * @param snmpParam
     * @param onuWanSsid
     */
    void deleteOnuWanSsid(SnmpParam snmpParam, OnuWanSsid onuWanSsid);

    /**
     * 新增ONU WAN CONNECT
     * 
     * @param snmpParam
     * @param onuWanSsid
     */
    OnuWanConnect addOnuWanConnect(SnmpParam snmpParam, OnuWanConnect onuWanConnect);

    /**
     * 修改ONU WAN CONNECT
     * 
     * @param snmpParam
     * @param onuWanSsid
     */
    void modifyOnuWanConnect(SnmpParam snmpParam, OnuWanConnect onuWanConnect);

    /**
     * 删除ONU WAN CONNECT
     * 
     * @param snmpParam
     * @param onuWanSsid
     */
    void deleteOnuWanConnect(SnmpParam snmpParam, OnuWanConnect onuWanConnect);

    /**
     * 修改WAN 绑定信息
     * 
     * @param snmpParam
     * @param onuIndex
     * @param connectId
     * @param bindInterface
     */
    void modifyBindInterface(SnmpParam snmpParam, Long onuIndex, Integer connectId, String bindInterface);

    /**
     * 修改wan连接serviceType
     * 
     * @param snmpParam
     * @param onuWanBind
     */
    void modifyServieMode(SnmpParam snmpParam, OnuWanBind onuWanBind);
}
