/***********************************************************************
 * $Id: OnuAuthFacade.java,v1.0 2013年10月25日 下午6:06:11 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onuauth.facade;

import java.util.List;

import com.topvision.ems.epon.onu.domain.OltOnuAuthModify;
import com.topvision.ems.epon.onuauth.domain.OltAuthenMacInfo;
import com.topvision.ems.epon.onuauth.domain.OltAuthenSnInfo;
import com.topvision.ems.epon.onuauth.domain.OltAuthentication;
import com.topvision.ems.epon.onuauth.domain.OltOnuBlockAuthen;
import com.topvision.ems.epon.onuauth.domain.OltPonOnuAuthModeTable;
import com.topvision.ems.facade.Facade;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author Bravin
 * @created @2013年10月25日-下午6:06:11
 *
 */
@EngineFacade(serviceName = "OnuAuthFacade", beanName = "onuAuthFacade")
public interface OnuAuthFacade extends Facade {

    /**
     * 设置ONU认证策略
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param policy
     *            认证策略
     * @return Integer
     */
    Integer setOnuAuthenPolicy(SnmpParam snmpParam, Integer policy);

    /**
     * 增加ONU MAC认证规则
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param oltAuthenMacInfo
     *            ONU认证规则
     * @return OltAuthenMacInfo
     */
    OltAuthenMacInfo addOnuMacAuthen(SnmpParam snmpParam, OltAuthenMacInfo oltAuthenMacInfo);

    /**
     * 增加ONU SN认证规则
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param oltAuthenSnInfo
     *            ONU认证规则
     * @return OltAuthenSnInfo
     */
    OltAuthenSnInfo addOnuSnAuthen(SnmpParam snmpParam, OltAuthenSnInfo oltAuthenSnInfo);

    /**
     * 修改ONU MAC认证规则
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param oltAuthenMacInfo
     *            ONU认证规则
     * @return OltAuthenMacInfo
     */
    OltAuthenMacInfo modifyOnuMacAuthen(SnmpParam snmpParam, OltAuthenMacInfo oltAuthenMacInfo);

    /**
     * 修改ONU SN认证规则
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param oltAuthenSnInfo
     *            ONU认证规则
     * @return OltAuthenSnInfo
     */
    OltAuthenSnInfo modifyOnuSnAuthen(SnmpParam snmpParam, OltAuthenSnInfo oltAuthenSnInfo);

    /**
     * 删除ONU MAC认证规则
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param onuIndex
     *            ONU索引
     * @return OltAuthenMacInfo
     */
    OltAuthenMacInfo deleteOnuMacAuthen(SnmpParam snmpParam, Long onuIndex);

    /**
     * 删除ONU SN认证规则
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param onuIndex
     *            ONU索引
     * @return OltAuthenSnInfo
     */
    OltAuthenSnInfo deleteOnuSnAuthen(SnmpParam snmpParam, Long onuIndex);

    /**
     * 获取ONU认证列表
     * 
     * @param entityId
     *            设备ID
     * @param ponId
     *            PON口ID
     * @return List<OltAuthentication>
     */
    @Deprecated
    List<OltAuthentication> getOnuAuthenPreConfigList(Long entityId, Long ponId);

    /**
     * 获得ONU MAC认证列表
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @return List<OltAuthenMacInfo>
     */
    List<OltAuthenMacInfo> getAuthenMacInfos(SnmpParam snmpParam);

    /**
     * 获得ONU SN认证列表
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @return List<OltAuthenSnInfo>
     */
    List<OltAuthenSnInfo> getAuthenSnInfos(SnmpParam snmpParam);

    /**
     * 获得ONU阻塞表信息
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @return List<OltOnuBlockAuthen>
     */
    List<OltOnuBlockAuthen> getBlockAuthens(SnmpParam snmpParam);

    void setOnuAuthMode(SnmpParam snmpParam, OltPonOnuAuthModeTable oltPonOnuAuthModeTable);

    void onuAuthInstead(SnmpParam snmpParam, OltOnuAuthModify oltOnuAuthModify);
}
