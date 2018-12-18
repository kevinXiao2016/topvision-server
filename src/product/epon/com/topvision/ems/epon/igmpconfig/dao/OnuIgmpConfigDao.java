/***********************************************************************
 * $Id: OnuIgmpConfigDao.java,v1.0 2016-6-6 下午4:18:44 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.igmpconfig.dao;

import java.util.List;

import com.topvision.ems.epon.igmpconfig.domain.IgmpOnuConfig;
import com.topvision.ems.epon.igmpconfig.domain.IgmpPortInfo;
import com.topvision.ems.epon.igmpconfig.domain.IgmpUniBindCtcProfile;
import com.topvision.ems.epon.igmpconfig.domain.IgmpUniConfig;
import com.topvision.ems.epon.igmpconfig.domain.IgmpUniVlanTrans;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author flack
 * @created @2016-6-6-下午4:18:44
 *
 */
public interface OnuIgmpConfigDao extends BaseEntityDao<Object> {

    /**
     * 插入UNI口绑定CTC模板记录
     * @param bindCtcProfile
     */
    public void insertBindCtcProfile(IgmpUniBindCtcProfile bindCtcProfile);

    /**
     * 批量插入UNI口绑定CTC模板记录
     * @param entityId
     * @param bindProfileList
     */
    public void batchInsertBindProfile(Long entityId, List<IgmpUniBindCtcProfile> bindProfileList);

    /**
     * 查询UNI口绑定CTC模板列表
     * @param entityId
     * @param uniIndex
     * @return
     */
    public List<IgmpUniBindCtcProfile> queryBindCtcProfileList(Long entityId, Long uniIndex);

    /**
     * 查询所有被UNI端口绑定的带有组播组的模板信息
     * @param entityId
     */
    public List<IgmpUniBindCtcProfile> queryHasGroupBindProfile(Long entityId);

    /**
     * 查询所有绑定了包含带源Ip的组播组的模板UNI绑定信息
     * @param entityId
     */
    public List<IgmpUniBindCtcProfile> queryWithSrcGroupBindProfile(Long entityId);

    /**
     * 查询所有绑定了包含不带源Ip的组播组的模板UNI绑定信息
     * @param entityId
     */
    public List<IgmpUniBindCtcProfile> queryWithoutSrcGroupBindProfile(Long entityId);

    /**
     * 删除UNI口绑定CTC模板记录
     * @param bindProfile
     */
    public void deleteBindCtcProfile(IgmpUniBindCtcProfile bindProfile);

    /**
     * 插入ONU IGMP配置
     * @param onuConfig
     */
    public void insertIgmpOnuConfig(IgmpOnuConfig onuConfig);

    /**
     * 批量插入ONU IGMP配置
     * @param entityId
     * @param onuConfigList
     */
    public void batchInsertOnuConfig(Long entityId, List<IgmpOnuConfig> onuConfigList);

    /**
     * 获取ONU IGMP配置
     * @param entityId
     * @param onuIndex
     * @return
     */
    public IgmpOnuConfig queryIgmpOnuConfig(Long entityId, Long onuIndex);

    /**
     * 修改ONU IGMP配置
     * @param onuConfig
     */
    public void updateIgmpOnuConfig(IgmpOnuConfig onuConfig);

    /**
     * 插入UNI口IGMP配置
     * @param uniConfig
     */
    public void insertIgmpUniConfig(IgmpUniConfig uniConfig);

    /**
     * 批量插入UNI口IGMP配置
     * @param entityId
     * @param uniConfigList
     */
    public void batchInsertUniConfig(Long entityId, List<IgmpUniConfig> uniConfigList);

    /**
     * 获取UNI口IGMP配置
     * @param entityId
     * @param uniIndex
     * @return
     */
    public IgmpUniConfig queryIgmpUniConfig(Long entityId, Long uniIndex);

    /**
     * 更新UNI口IGMP配置
     * @param uniConfig
     */
    public void updateIgmpUniConfig(IgmpUniConfig uniConfig);

    /**
     * 插入UNI口VLAN转换配置
     * @param vlanTrans
     */
    public void insertUniVlanTrans(IgmpUniVlanTrans vlanTrans);

    /**
     * 批量插入UNI口VLAN转换列表
     * @param entityId
     * @param vlanTransList
     */
    public void batchInsertVlanTrans(Long entityId, List<IgmpUniVlanTrans> vlanTransList);

    /**
     * 获取UNI口VLAN转换配置列表
     * @param entityId
     * @param uniIndex
     * @return
     */
    public List<IgmpUniVlanTrans> queryUniVlanTransList(Long entityId, Long uniIndex);

    /**
     * 删除UNI口VLAN转换配置
     * @param vlanTrans
     */
    public void deleteUniVlanTrans(IgmpUniVlanTrans vlanTrans);

    /**
     * 获取指定ONU设备的UNI端口信息
     * @param onuId
     * @return
     */
    public List<IgmpPortInfo> queryUniPortList(Long onuId);

    /**
     * 插入或者更新ONU IGMP配置
     * @param onuConfig
     */
    public void insertOrUpdateOnuConfig(IgmpOnuConfig onuConfig);

    /**
     * 插入或者更新UNI IGMP配置
     * @param uniConfig
     */
    public void insertOrUpdateUniConfig(IgmpUniConfig uniConfig);
}
