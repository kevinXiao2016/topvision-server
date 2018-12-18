/***********************************************************************
 * $Id: OnuIgmpConfigFacade.java,v1.0 2016-6-6 下午4:37:48 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.igmpconfig.facade;

import java.util.List;

import com.topvision.ems.epon.igmpconfig.domain.IgmpOnuConfig;
import com.topvision.ems.epon.igmpconfig.domain.IgmpUniBindCtcProfile;
import com.topvision.ems.epon.igmpconfig.domain.IgmpUniConfig;
import com.topvision.ems.epon.igmpconfig.domain.IgmpUniVlanTrans;
import com.topvision.ems.facade.Facade;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author flack
 * @created @2016-6-6-下午4:37:48
 *
 */
@EngineFacade(serviceName = "onuIgmpConfigFacade", beanName = "onuIgmpConfigFacade")
public interface OnuIgmpConfigFacade extends Facade {

    /**
     * 添加UNI口绑定CTC模板记录
     * @param snmpParam
     * @param bindCtcProfile
     */
    public void createBindCtcProfile(SnmpParam snmpParam, IgmpUniBindCtcProfile bindCtcProfile);

    /**
     * 删除UNI口绑定CTC模板记录
     * @param snmpParam
     * @param bindCtcProfile
     */
    public void destoryBindCtcProfile(SnmpParam snmpParam, IgmpUniBindCtcProfile bindCtcProfile);

    /**
     * 获取ONU IGMP配置
     * @param snmpParam
     * @param onuIndex
     * @return
     */
    public IgmpOnuConfig getIgmpOnuConfig(SnmpParam snmpParam, Long onuIndex);

    /**
     * 修改ONU IGMP配置
     * @param snmpParam
     * @param onuConfig
     */
    public void modifyIgmpOnuConfig(SnmpParam snmpParam, IgmpOnuConfig onuConfig);

    /**
     * 获取UNI口IGMP配置
     * @param snmpParam
     * @param uniIndex
     * @return
     */
    public IgmpUniConfig getIgmpUniConfig(SnmpParam snmpParam, Long uniIndex);

    /**
     * 更新UNI口IGMP配置
     * @param snmpParam
     * @param uniConfig
     */
    public void modifyIgmpUniConfig(SnmpParam snmpParam, IgmpUniConfig uniConfig);

    /**
     * 添加UNI口VLAN转换配置
     * @param snmpParam
     * @param vlanTrans
     */
    public void createUniVlanTrans(SnmpParam snmpParam, IgmpUniVlanTrans vlanTrans);

    /**
     * 获取UNI口VLAN转换配置列表
     * @param snmpParam
     * @return
     */
    public List<IgmpUniVlanTrans> getUniVlanTransList(SnmpParam snmpParam);

    /**
     * 删除UNI口VLAN转换配置
     * @param snmpParam
     * @param vlanTrans
     */
    public void destoryUniVlanTrans(SnmpParam snmpParam, IgmpUniVlanTrans vlanTrans);

}
