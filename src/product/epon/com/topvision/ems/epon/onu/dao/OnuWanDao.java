/***********************************************************************
 * $Id: OnuWanDao.java,v1.0 2016年5月30日 下午4:05:19 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.dao;

import java.util.List;

import com.topvision.ems.epon.onu.domain.OnuWanConfig;
import com.topvision.ems.epon.onu.domain.OnuWanConnect;
import com.topvision.ems.epon.onu.domain.OnuWanConnectStatus;
import com.topvision.ems.epon.onu.domain.OnuWanSsid;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author loyal
 * @created @2016年5月30日-下午4:05:19
 * 
 */
public interface OnuWanDao extends BaseEntityDao<Object> {
    public OnuWanConfig getOnuWanConfig(Long onuId);

    public List<OnuWanSsid> getOnuWanSsid(Long onuId);

    public OnuWanSsid getOnuWanSsid(Long onuId, Integer ssid);

    public List<OnuWanConnect> getOnuWanConnect(Long onuId);

    public List<OnuWanConnectStatus> getOnuWanConnectStatus(Long onuId);

    public void insertOnuWanConfig(OnuWanConfig onuWanConfig);

    public void insertOnuWanSsid(OnuWanSsid onuWanSsid);

    public void insertOnuWanConnect(OnuWanConnect onuWanConnect);

    public void insertOnuWanConnectStatus(OnuWanConnectStatus onuWanConnectStatus);

    public void deleteOnuWanSsid(Long onuId, Integer ssid);

    public void deleteOnuWanConnect(Long onuId, Integer connectId);

    public void deleteOnuWanConnectsByOnuId(Long onuId);

    public void updateOnuWanConfig(OnuWanConfig onuWanConfig);

    public void updateOnuWanSsid(OnuWanSsid onuWanSsid);

    /**
     * 修改ssid名称和密码
     * 
     * @param onuWanSsid
     */
    public void updateWanSsidAndName(OnuWanSsid onuWanSsid);

    public void updateOnuWanConnect(OnuWanConnect onuWanConnect);

    /**
     * 修改WAN 密码
     * 
     * @param onuWanConnect
     */
    public void updateOnuWanPassord(OnuWanConnect onuWanConnect);

    public String loadBindInterface(Long onuId, Integer connectId);

    public void saveBindInterface(Long onuId, Integer connectId, String bindInterface);

    public List<String> loadBindInterface(Long onuId);

    /**
     * 插入或者更新一个OLT下所有ONU WAN配置信息
     * 
     * @param entityId
     * @param onuWanConfigList
     */
    public void batchInsertOrUpdateOnuWanConfig(Long entityId, List<OnuWanConfig> onuWanConfigList);

    /**
     * 插入或者更新单个ONU WAN配置信息
     * 
     * @param entityId
     * @param onuIndex
     * @param onuWanConfig
     */
    public void batchInsertOrUpdateOnuWanConfig(Long entityId, Long onuIndex, OnuWanConfig onuWanConfig);

    /**
     * 插入或者更新ONU的SSID信息(单个ONU)
     * 
     * @param entityId
     * @param object
     * @param onuWanSsids
     */
    public void batchInsertOrUpdateOnuWanSsid(Long entityId, Long onuIndex, List<OnuWanSsid> onuWanSsids);

    /**
     * 插入或者更新ONU的SSID信息(一个OLT下所有ONU)
     * 
     * @param entityId
     * @param onuWanSsids
     */
    public void batchInsertOrUpdateOnuWanSsid(Long entityId, List<OnuWanSsid> onuWanSsids);

    /**
     * 插入或者更新ONU的WAN连接信息(一个OLT下所有ONU)
     * 
     * @param entityId
     * @param onuWanConnects
     */
    public void batchInsertOrUpdateOnuWanConnect(Long entityId, List<OnuWanConnect> onuWanConnects);

    /**
     * 插入或者更新ONU的WAN连接信息(单个ONU)
     * 
     * @param entityId
     * @param onuIndex
     * @param onuWanConnects
     */
    public void batchInsertOrUpdateOnuWanConnect(Long entityId, Long onuIndex, List<OnuWanConnect> onuWanConnects);

    /**
     * 插入或者更新ONU的WAN STATUS信息(一个OLT下所有ONU)
     * 
     * @param entityId
     * @param onuWanConnectStatus
     */
    public void batchInsertOrUpdateOnuWanConnectStatus(Long entityId, List<OnuWanConnectStatus> onuWanConnectStatus);

    /**
     * 插入或者更新ONU的WAN STATUS信息(单个ONU)
     * 
     * @param entityId
     * @param onuIndex
     * @param onuWanConnectStatus
     */
    public void batchInsertOrUpdateOnuWanConnectStatus(Long entityId, Long onuIndex,
            List<OnuWanConnectStatus> onuWanConnectStatus);

    /**
     * 获取一个ONU下一个WAN连接信息
     * 
     * @param onuId
     * @param connectId
     * @return
     */
    public OnuWanConnect getOnuWanConnect(Long onuId, Integer connectId);

    /**
     * 获取已经绑定的端口
     * 
     * @param onuId
     * @param connectId
     * @return
     */
    public List<String> loadAlreadyBindInterface(Long onuId, Integer connectId);

}
