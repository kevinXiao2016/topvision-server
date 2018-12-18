/***********************************************************************
 * $Id: OnuWanService.java,v1.0 2016年5月30日 下午4:03:34 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.service;

import java.util.List;

import com.topvision.ems.epon.onu.domain.OnuWanConfig;
import com.topvision.ems.epon.onu.domain.OnuWanConnect;
import com.topvision.ems.epon.onu.domain.OnuWanConnectStatus;
import com.topvision.ems.epon.onu.domain.OnuWanSsid;
import com.topvision.framework.service.Service;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author loyal
 * @created @2016年5月30日-下午4:03:34
 * 
 */
public interface OnuWanService extends Service {

    /**
     * 获取ONU WAN配置
     * 
     * @param onuId
     * @return
     */
    OnuWanConfig getOnuWanConfig(Long onuId);

    List<OnuWanSsid> getOnuWanSsid(Long onuId);

    OnuWanSsid getOnuWanSsid(Long onuId, Integer ssid);

    List<OnuWanConnect> getOnuWanConnect(Long onuId);

    List<OnuWanConnectStatus> getOnuWanConnectStatus(Long onuId);

    void insertOnuWanConfig(OnuWanConfig onuWanConfig);

    void insertOnuWanSsid(OnuWanSsid onuWanSsid);

    void insertOnuWanConnect(OnuWanConnect onuWanConnect);

    void insertOnuWanConnectStatus(OnuWanConnectStatus onuWanConnectStatus);

    void deleteOnuWanConnect(Long entityId, Long onuId, Integer connectId);

    void updateOnuWanConfig(OnuWanConfig onuWanConfig);

    void updateOnuWanSsid(OnuWanSsid onuWanSsid);

    /**
     * 修改ssid名称密码
     * 
     * @param onuWanSsid
     */
    void updateWifiPassword(OnuWanSsid onuWanSsid);

    void updateOnuWanConnect(OnuWanConnect onuWanConnect);

    /**
     * 修改Wan PPPoE 密码
     * 
     * @param onuWanConnect
     */
    void updateWanPPPoEPassord(OnuWanConnect onuWanConnect);

    List<Integer> loadBindInterface(Long onuId, Integer connectId);

    List<Integer> loadBindInterface(Long onuId);

    void saveBindInterface(Long entityId, Long onuId, Integer connectId, List<Integer> bindInterface);

    /**
     * 保存WLAN配置
     * 
     * @param onuWanConfig
     */
    void saveWLANConfig(OnuWanConfig onuWanConfig);

    /**
     * 刷新一个olt下onuwanconfig
     * 
     * @param entityId
     */
    void refreshOnuWanConfig(Long entityId);

    /**
     * 刷新单个onu wanconfig
     * 
     * @param entityId
     * @param onuIndex
     */
    void refreshOnuWanConfig(Long entityId, Long onuIndex);

    /**
     * 刷新一个olt onuwanssid
     * 
     * @param entityId
     */
    void refreshOnuWanSsid(Long entityId);

    /**
     * 刷新单个onu wanssid
     * 
     * @param entityId
     * @param onuIndex
     */
    void refreshOnuWanSsid(Long entityId, Long onuIndex);

    /**
     * 刷新单个onu wanssid
     * 
     * @param snmpParam
     * @param entityId
     * @param onuIndex
     */
    void refreshOnuWanSsid(SnmpParam snmpParam, Long entityId, Long onuIndex);

    /**
     * 刷新一个olt下 onuwanconnnect
     * 
     * @param entityId
     */
    void refreshOnuWanConnect(Long entityId);

    /**
     * 刷新单个onu wan connect
     * 
     * @param entityId
     * @param onuIndex
     */
    void refreshWanConnection(Long entityId, Long onuIndex);

    /**
     * 刷新单个onu wan connect
     * 
     * @param snmpParam
     * @param entityId
     * @param onuId
     * @param onuIndex
     */
    void refreshWanConnection(SnmpParam snmpParam, Long entityId, Long onuId, Long onuIndex);

    /**
     * 刷新一个olt下 onuwanconnnectstatus
     * 
     * @param entityId
     */
    void refreshOnuWanConnectStatus(Long entityId);

    /**
     * 刷新单个onu wanconnnectstatus
     * 
     * @param entityId
     * @param onuIndex
     */
    void refreshOnuWanConnectStatus(Long entityId, Long onuIndex);

    /**
     * 删除ssid
     * 
     * @param onuId
     * @param onuIndex
     * @param entityId
     * @param ssid
     */
    void deleteOnuWanSsid(Long onuId, Long onuIndex, Long entityId, Integer ssid);

    /**
     * 清除WAN连接
     * 
     * @param onuId
     * @param entityId
     */
    void clearWanConnection(Long onuId, Long entityId);

    /**
     * 读取一个ONU的一个WAN连接信息
     * 
     * @param onuId
     * @param connectId
     * @return
     */
    OnuWanConnect loadWanConnection(Long onuId, Integer connectId);

    /**
     * ONU恢复出厂设置
     * 
     * @param onuId
     * @param entityId
     */
    void restoreOnu(Long onuId, Long entityId);

    /**
     * 获取已经绑定了的端口
     * 
     * @param onuId
     * @param connectId
     * @return
     */
    List<Integer> loadAlreadyBandInterface(Long onuId, Integer connectId);
}
