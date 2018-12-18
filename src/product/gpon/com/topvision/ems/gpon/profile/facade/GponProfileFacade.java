/***********************************************************************
 * $Id: GponProfilleFacade.java,v1.0 2016年10月25日 下午12:12:27 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.profile.facade;

import java.util.List;

import com.topvision.ems.facade.Facade;
import com.topvision.ems.gpon.profile.facade.domain.GponLineProfileGem;
import com.topvision.ems.gpon.profile.facade.domain.GponLineProfileGemMap;
import com.topvision.ems.gpon.profile.facade.domain.GponLineProfileTcont;
import com.topvision.ems.gpon.profile.facade.domain.GponSrvProfileCfg;
import com.topvision.ems.gpon.profile.facade.domain.GponSrvProfileEthPortConfig;
import com.topvision.ems.gpon.profile.facade.domain.GponSrvProfilePortNumProfile;
import com.topvision.ems.gpon.profile.facade.domain.GponSrvProfilePortVlanCfg;
import com.topvision.ems.gpon.profile.facade.domain.TopGponSrvPotsInfo;
import com.topvision.ems.gpon.profile.facade.domain.TopGponSrvProfile;
import com.topvision.ems.gpon.profile.facade.domain.TopSIPAgentProfInfo;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author Bravin
 * @created @2016年10月25日-下午12:12:27
 *
 */
@EngineFacade(serviceName = "GponProfilleFacade", beanName = "gponProfilleFacade")
public interface GponProfileFacade extends Facade {

    /**
     * 拓扑模板表通用接口
     * 
     * @param snmpParam
     * @param clazz
     * @return
     */
    <T> List<T> getGponProfileTable(SnmpParam snmpParam, Class<T> clazz);

    /**
     * 新增/修改/删除模板通用接口
     * 
     * @param snmpParam
     * @param object
     */
    <T> void setGponProfileTable(SnmpParam snmpParam, T object);

    /**
     * 获取某一业务模板下T-CONT列表
     * 
     * @param snmpParam
     * @param tcont
     * @return
     */
    List<GponLineProfileTcont> getTcontInProfile(SnmpParam snmpParam, GponLineProfileTcont tcont);

    /**
     * 获取某一业务模板下GEM列表
     * 
     * @param snmpParam
     * @param gem
     * @return
     */
    List<GponLineProfileGem> getGemInProfile(SnmpParam snmpParam, GponLineProfileGem gem);

    /**
     * 获取某一GEM下GEM映射列表
     * 
     * @param snmpParam
     * @param gemMap
     * @return
     */
    List<GponLineProfileGemMap> getGemMapInProfile(SnmpParam snmpParam, GponLineProfileGemMap gemMap);

    /**
     * 获取某一业务模板下以太口配置列表
     * 
     * @param snmpParam
     * @param ethPortConfig
     * @return
     */
    List<GponSrvProfileEthPortConfig> getEthPortConfigInProfile(SnmpParam snmpParam,
            GponSrvProfileEthPortConfig ethPortConfig);

    /**
     * 获取某一个业务模板下端口VLAN配置列表
     * 
     * @param snmpParam
     * @param portVlanCfg
     * @return
     */
    List<GponSrvProfilePortVlanCfg> getPortVlanCfgInProfile(SnmpParam snmpParam, GponSrvProfilePortVlanCfg portVlanCfg);

    /**
     * 获取单个端口VLAN配置
     * 
     * @param snmpParam
     * @param portVlanCfg
     */
    GponSrvProfilePortVlanCfg getPortVlanCfg(SnmpParam snmpParam, GponSrvProfilePortVlanCfg portVlanCfg);

    /**
     * 返回索引范围内的表数据 startIndex - endIndex
     * 
     * @param <T>
     * @param snmpParam
     * @param object
     * @param startIndex
     * @param endIndex
     * @return
     */

    <T> List<T> getGponTableRangeLines(SnmpParam snmpParam, T object, Long startIndex, Long endIndex);

    /**
     * 获得指定行的表数据
     * 
     * @param snmpParam
     * @param object
     * @return
     */
    <T> T getGponTableLine(SnmpParam snmpParam, T object);

    /**
     * 获取业务模板配置信息
     * 
     * @param snmpParam
     * @param cfg
     * @return
     */
    GponSrvProfileCfg getGponSrvProfileCfg(SnmpParam snmpParam, GponSrvProfileCfg cfg);

    /**
     * 获取业务模板端口配置信息
     * 
     * @param snmpParam
     * @param portNum
     * @return
     */
    GponSrvProfilePortNumProfile getPortNumProfile(SnmpParam snmpParam, GponSrvProfilePortNumProfile portNum);

    /**
     * 获取业务模板的VOIP配置信息
     * @param snmpParam
     * @param cfg
     * @return
     */
    TopGponSrvProfile getTopGponSrvProfile(SnmpParam snmpParam, TopGponSrvProfile cfg);

    /**
     * 获取SIP代理模板信息
     * @param snmpParam
     * @param cfg
     * @return
     */
    TopSIPAgentProfInfo getTopSIPAgentProfInfo(SnmpParam snmpParam, TopSIPAgentProfInfo cfg);

    /**
     * 获取一个业务模板下的POTS口配置列表
     * @param snmpParam
     * @param pots
     * @return
     */
    List<TopGponSrvPotsInfo> getTopGponSrvPotsInfo(SnmpParam snmpParam, TopGponSrvPotsInfo pots);

}
