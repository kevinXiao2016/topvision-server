/***********************************************************************
 * $Id: OnuVoipFacade.java,v1.0 2017年5月4日 上午11:33:50 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.onuvoip.facade;

import java.util.List;

import com.topvision.ems.gpon.onuvoip.domain.TopOnuIfPotsInfo;
import com.topvision.ems.gpon.onuvoip.domain.TopSIPPstnUser;
import com.topvision.ems.gpon.onuvoip.domain.TopVoIPLineStatus;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author haojie
 * @created @2017年5月4日-上午11:33:50
 *
 */
@EngineFacade(serviceName = "OnuVoipFacade", beanName = "onuVoipFacade")
public interface OnuVoipFacade {

    /**
     * 获取ONU下POTS口VOIP线路状态
     * 
     * @param snmpParam
     * @param onuIndex
     * @return
     */
    List<TopVoIPLineStatus> getTopVoIPLineStatus(SnmpParam snmpParam, Long onuIndex);

    /**
     * 获取ONU下POTS口VOIP用户配置
     * 
     * @param snmpParam
     * @param onuIndex
     * @return
     */
    List<TopSIPPstnUser> getTopSIPPstnUser(SnmpParam snmpParam, Long onuIndex);

    /**
     * 获取OLT下所有ONU的POTS的VOIP线路状态
     * 
     * @param snmpParam
     * @return
     */
    List<TopVoIPLineStatus> getTopVoIPLineStatus(SnmpParam snmpParam);

    /**
     * 获取OLT下所有ONU的POTS的VOIP用户信息
     * 
     * @param snmpParam
     * @return
     */
    List<TopSIPPstnUser> getTopSIPPstnUser(SnmpParam snmpParam);

    /**
     * 修改pots口voip用户配置信息
     * @param snmpParam
     * @param topSIPPstnUser
     */
    void modifyTopSIPPstnUser(SnmpParam snmpParam, TopSIPPstnUser topSIPPstnUser);

    /**
     * 获取OLT下pots口状态
     * @param snmpParam
     * @return
     */
    List<TopOnuIfPotsInfo> getTopOnuIfPotsInfo(SnmpParam snmpParam);

    /**
     * 获取一个onu pots口状态
     * @param snmpParam
     * @param onuIndex
     * @return
     */
    List<TopOnuIfPotsInfo> getTopOnuIfPotsInfo(SnmpParam snmpParam, Long onuIndex);

    /**
     * 设置一个onu pots口状态
     * @param snmpParam
     * @param onuIfPotsInfo
     */
    void setOnuPotsAdminStatus(SnmpParam snmpParam, TopOnuIfPotsInfo onuIfPotsInfo);

}
