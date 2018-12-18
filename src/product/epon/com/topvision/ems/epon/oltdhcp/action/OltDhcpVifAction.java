/***********************************************************************
 * $Id: OltDhcpVifAction.java,v1.0 2017年11月24日 上午9:30:03 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.oltdhcp.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.logicinterface.domain.InterfaceIpV4Config;
import com.topvision.ems.epon.logicinterface.domain.LogicInterface;
import com.topvision.ems.epon.logicinterface.service.LogicInterfaceService;
import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltDhcpVifCfg;
import com.topvision.ems.epon.oltdhcp.service.OltDhcpVifService;
import com.topvision.framework.web.struts2.BaseAction;

import net.sf.json.JSONObject;

/**
 * @author haojie
 * @created @2017年11月24日-上午9:30:03
 *
 */
@Controller("oltDhcpVifAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OltDhcpVifAction extends BaseAction {
    private static final long serialVersionUID = 770732580879024822L;
    private Long entityId;
    private Integer vifIndex;
    private String opt60StrIndex;
    private String vifAgentAddr;
    private Integer vifServerGroup;
    private Integer interfaceIndex;
    private String type;
    @Autowired
    private OltDhcpVifService oltDhcpVifService;
    @Autowired
    private LogicInterfaceService logicInterfaceService;

    /**
     * 跳转RELAY规则列表页面
     * 
     * @return
     */
    public String showOltDhcpVifList() {
        return SUCCESS;
    }

    /**
     * 获取RELAY规则配置列表数据
     * 
     * @return
     */
    public String loadOltDhcpVifList() {
        Map<String, Object> json = new HashMap<String, Object>();
        List<TopOltDhcpVifCfg> vifCfgs = oltDhcpVifService.getOltDhcpVifCfg(entityId);
        json.put("data", vifCfgs);
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 跳转新增RELAY规则配置页面
     * 
     * @return
     */
    public String showAddOltDhcpVif() {
        return SUCCESS;
    }

    /**
     * 获取单条模式配置数据
     * 
     * @return
     */
    public String loadOltDhcpVif() {
        TopOltDhcpVifCfg vifCfg = oltDhcpVifService.getOltDhcpVifCfg(entityId, vifIndex, opt60StrIndex);
        writeDataToAjax(vifCfg);
        return NONE;
    }

    /**
     * 新增RELAY规则配置
     * 
     * @return
     */
    public String addOltDhcpVif() {
        TopOltDhcpVifCfg vifCfg = new TopOltDhcpVifCfg();
        vifCfg.setEntityId(entityId);
        vifCfg.setTopOltDhcpVifIndex(vifIndex);
        vifCfg.setTopOltDhcpVifOpt60StrIndex(opt60StrIndex);
        vifCfg.setTopOltDhcpVifAgentAddr(vifAgentAddr);
        if (vifServerGroup != 0) {
            vifCfg.setTopOltDhcpVifServerGroup(vifServerGroup);
        }
        oltDhcpVifService.addOltDhcpVifCfg(vifCfg);
        return NONE;
    }

    /**
     * 跳转修改RELAY规则配置页面
     * 
     * @return
     */
    public String showModifyOltDhcpVif() {
        return SUCCESS;
    }

    /**
     * 修改RELAY规则配置
     * 
     * @return
     */
    public String modifyOltDhcpVif() {
        TopOltDhcpVifCfg vifCfg = new TopOltDhcpVifCfg();
        vifCfg.setEntityId(entityId);
        vifCfg.setTopOltDhcpVifIndex(vifIndex);
        vifCfg.setTopOltDhcpVifOpt60StrIndex(opt60StrIndex);
        vifCfg.setTopOltDhcpVifAgentAddr(vifAgentAddr);
        vifCfg.setTopOltDhcpVifServerGroup(vifServerGroup);
        oltDhcpVifService.modifyOltDhcpVifCfg(vifCfg);
        return NONE;
    }

    /**
     * 删除RELAY规则配置
     * 
     * @return
     */
    public String deleteOltDhcpVif() {
        oltDhcpVifService.deleteOltDhcpVifCfg(entityId, vifIndex, opt60StrIndex);
        return NONE;
    }

    /**
     * 刷新RELAY规则列表数据
     * 
     * @return
     */
    public String refreshOltDhcpVifList() {
        logicInterfaceService.refreshLogicInterface(entityId);
        logicInterfaceService.refreshLogicInterfaceIpConfig(entityId);
        oltDhcpVifService.refreshOltDhcpVifCfg(entityId);
        return NONE;
    }

    /**
     * 获取VLAN虚接口数据，用于下拉框
     * 
     * @return
     */
    public String loadOltVifList() {
        Map<String, Object> json = new HashMap<String, Object>();
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("entityId", entityId);
        param.put("interfaceType", 1);
        List<LogicInterface> logicInterfaceList = oltDhcpVifService.getOltLogicInterfaceByType(param);
        json.put("data", logicInterfaceList);
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 获取虚接口IP，用于配置agent IP地址
     * 
     * @return
     */
    public String loadOltVifIpList() {
        Map<String, Object> json = new HashMap<String, Object>();
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("entityId", entityId);
        param.put("ipV4ConfigIndex", interfaceIndex);
        List<InterfaceIpV4Config> interfaceIpV4List = logicInterfaceService.getInterfaceIpList(param);
        json.put("data", interfaceIpV4List);
        writeDataToAjax(json);
        return NONE;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getVifIndex() {
        return vifIndex;
    }

    public void setVifIndex(Integer vifIndex) {
        this.vifIndex = vifIndex;
    }

    public String getOpt60StrIndex() {
        return opt60StrIndex;
    }

    public void setOpt60StrIndex(String opt60StrIndex) {
        this.opt60StrIndex = opt60StrIndex;
    }

    public String getVifAgentAddr() {
        return vifAgentAddr;
    }

    public void setVifAgentAddr(String vifAgentAddr) {
        this.vifAgentAddr = vifAgentAddr;
    }

    public Integer getVifServerGroup() {
        return vifServerGroup;
    }

    public void setVifServerGroup(Integer vifServerGroup) {
        this.vifServerGroup = vifServerGroup;
    }

    public Integer getInterfaceIndex() {
        return interfaceIndex;
    }

    public void setInterfaceIndex(Integer interfaceIndex) {
        this.interfaceIndex = interfaceIndex;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
