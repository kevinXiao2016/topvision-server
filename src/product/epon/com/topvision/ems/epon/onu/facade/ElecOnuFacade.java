/***********************************************************************
 * $Id: OnuFacade.java,v1.0 2013-10-25 上午11:22:35 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.facade;

import java.util.List;

import com.topvision.ems.epon.onu.domain.OltOnuComAttribute;
import com.topvision.ems.epon.onu.domain.OltOnuComStatics;
import com.topvision.ems.epon.onu.domain.OltOnuComVlanConfig;
import com.topvision.ems.epon.onu.domain.OltOnuMacMgmt;
import com.topvision.ems.facade.Facade;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author flack
 * @created @2013-10-25-上午11:22:35
 *
 */
@EngineFacade(serviceName = "ElecOnuFacade", beanName = "elecOnuFacade")
public interface ElecOnuFacade extends Facade {

    /**
     * 设置ONU串口VLAN
     * 
     * @param param
     * @param onuComVlan
     */
    void setOnuComVlan(SnmpParam param, Integer onuComVlan);

    /**
     *  设置ONU的管理信息
     * 
     * @param param
     * @param onuIndex
     * @param onuIp
     * @param onuMask
     * @param onuGateway
     */
    void setOnuIpMaskInfo(SnmpParam param, Long onuIndex, String onuIp, String onuMask, String onuGateway);

    /**
     *  设置ONU的串口服务器基本信息
     * 
     * @param param
     * @param attribute
     */
    void setOnuComAttribute(SnmpParam param, OltOnuComAttribute attribute);

    /**
     *  实时获取ONU的串口统计信息
     * 
     * @param param
     * @param onuComIndex
     * @return
     */
    OltOnuComStatics getOnuComStatisc(SnmpParam param, Long onuComIndex);

    /**
     *  清除ONU的串口统计信息
     * 
     * @param param
     * @param onuComIndex
     */
    void cleanOnuComStatisc(SnmpParam param, Long onuComIndex);

    /**
     *  PON口的业务切割
     * 
     * @param param
     * @param mainPonIndex
     * @param backPonIndex
     * @param action
     */
    void switchPonInfo(SnmpParam param, Long srcPonIndex, Long dstPonIndex, Integer action);

    /**
     *  刷新ONU的串口VLAN信息
     * 
     * @param param
     * @return
     */
    OltOnuComVlanConfig getOnuComVlan(SnmpParam param);

    /**
     *  刷新ONU的串口服务器信息
     * 
     * @param param
     * @param onuComIndex
     * @return
     */
    OltOnuComAttribute getOnuComAttribute(SnmpParam param, Long onuComIndex);

    /**
     *  采集ONU串口服务器的信息（拓扑使用）
     * 
     * @param param
     * @return
     */
    List<OltOnuComAttribute> getOltOnuComAttributes(SnmpParam param);

    /**
     * 获取ONU MAC地址管理信息
     * 
     * @param param
     * @param onuIndex
     * @return
     */
    OltOnuMacMgmt getOnuMacMgmt(SnmpParam param, Long onuIndex);

    /**
     * 修改ONU MAC地址管理信息
     * 
     * @param param
     * @param oltOnuMacMgmt
     */
    void setOnuMacMgmt(SnmpParam param, OltOnuMacMgmt oltOnuMacMgmt);

    /**
     * 添加一条ONU MAC地址管理信息
     * 
     * @param param
     * @param oltOnuMacMgmt
     */
    void addOnuMacMgmt(SnmpParam param, OltOnuMacMgmt oltOnuMacMgmt);

}
