/***********************************************************************
 * $Id: OnuIgmpConfigService.java,v1.0 2016-6-6 下午4:14:08 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.igmpconfig.service;

import java.util.List;

import com.topvision.ems.epon.igmpconfig.domain.IgmpOnuConfig;
import com.topvision.ems.epon.igmpconfig.domain.IgmpPortInfo;
import com.topvision.ems.epon.igmpconfig.domain.IgmpUniBindCtcProfile;
import com.topvision.ems.epon.igmpconfig.domain.IgmpUniConfig;
import com.topvision.ems.epon.igmpconfig.domain.IgmpUniVlanTrans;
import com.topvision.framework.service.Service;

/**
 * @author flack
 * @created @2016-6-6-下午4:14:08
 *
 */
public interface OnuIgmpConfigService extends Service {

    /**
     * 添加UNI口绑定CTC模板记录
     * @param bindCtcProfile
     */
    public void addBindCtcProfile(IgmpUniBindCtcProfile bindCtcProfile);

    /**
     * 查询UNI口绑定CTC模板列表
     * @param entityId
     * @param uniIndex
     * @return
     */
    public List<IgmpUniBindCtcProfile> getBindCtcProfileList(Long entityId, Long uniIndex);

    /**
     * 查询所有被UNI端口绑定的带有组播组的模板信息
     * @param entityId
     */
    public List<IgmpUniBindCtcProfile> getHasGroupBindProfile(Long entityId);

    /**
     * 删除UNI口绑定CTC模板记录
     * @param bindProfile 
     */
    public void deleteBindCtcProfile(IgmpUniBindCtcProfile bindProfile);

    /**
     * 刷新UNI端口绑定CTC模板
     * @param entityId
     */
    public void refreshUniBindProfile(Long entityId);

    /**
     * 添加ONU IGMP配置
     * @param onuConfig
     */
    public void addIgmpOnuConfig(IgmpOnuConfig onuConfig);

    /**
     * 获取ONU IGMP配置
     * @param entityId
     * @param onuIndex
     * @return
     */
    public IgmpOnuConfig getIgmpOnuConfig(Long entityId, Long onuIndex);

    /**
     * 修改ONU IGMP配置
     * @param onuConfig
     */
    public void modifyIgmpOnuConfig(IgmpOnuConfig onuConfig);

    /**
     * 刷新ONU IGMP配置
     * @param entityId
     * @param onuIndex
     */
    public void refreshIgmpOnuConfig(Long entityId, Long onuIndex);

    /**
     * 添加UNI口IGMP配置
     * @param uniConfig
     */
    public void addIgmpUniConfig(IgmpUniConfig uniConfig);

    /**
     * 获取UNI口IGMP配置
     * @param entityId
     * @param uniIndex
     * @return
     */
    public IgmpUniConfig getIgmpUniConfig(Long entityId, Long uniIndex);

    /**
     * 更新UNI口IGMP配置
     * @param uniConfig
     */
    public void modifyIgmpUniConfig(IgmpUniConfig uniConfig);

    /**
     * 刷新UNI IGMP配置
     * @param entityId
     * @param uniIndex
     */
    public void refreshIgmpUniConfig(Long entityId, Long uniIndex);

    /**
     * 添加UNI口VLAN转换配置
     * @param vlanTrans
     */
    public void addUniVlanTrans(IgmpUniVlanTrans vlanTrans);

    /**
     * 获取UNI口VLAN转换配置列表
     * @param entityId
     * @param uniIndex
     * @return
     */
    public List<IgmpUniVlanTrans> getUniVlanTransList(Long entityId, Long uniIndex);

    /**
     * 删除UNI口VLAN转换配置
     * @param vlanTrans TODO
     */
    public void deleteUniVlanTrans(IgmpUniVlanTrans vlanTrans);

    /**
     * 刷新UNI端口VLAN转化配置
     * @param entityId
     */
    public void refreshUniVlanTrans(Long entityId);

    /**
     * 获取指定ONU设备的UNI端口信息
     * @param onuId
     * @return
     */
    public List<IgmpPortInfo> getUniPortList(Long onuId);

}
