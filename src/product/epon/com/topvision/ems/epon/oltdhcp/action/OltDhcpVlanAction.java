/***********************************************************************
 * $Id: OltDhcpVlanAction.java,v1.0 2017年11月24日 上午9:30:16 $
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

import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltDhcpVLANCfg;
import com.topvision.ems.epon.oltdhcp.service.OltDhcpVlanService;
import com.topvision.framework.web.struts2.BaseAction;

import net.sf.json.JSONObject;

/**
 * @author haojie
 * @created @2017年11月24日-上午9:30:16
 *
 */
@Controller("oltDhcpVlanAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OltDhcpVlanAction extends BaseAction {
    private static final long serialVersionUID = 6337038257750088825L;
    private Long entityId;
    private Integer vlanIndex;
    private Integer vlanMode;
    private Integer relayMode;
    @Autowired
    private OltDhcpVlanService oltDhcpVlanService;

    /**
     * 跳转模式配置页面
     * 
     * @return
     */
    public String showOltDhcpVlanList() {
        return SUCCESS;
    }

    /**
     * 获取模式配置列表数据
     * 
     * @return
     */
    public String loadOltDhcpVlanList() {
        Map<String, Object> json = new HashMap<String, Object>();
        List<TopOltDhcpVLANCfg> vlanCfgs = oltDhcpVlanService.getOltDhcpVlanCfg(entityId);
        json.put("data", vlanCfgs);
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 跳转修改模式配置页面
     * 
     * @return
     */
    public String showModifyOltDhcpVlan() {
        return SUCCESS;
    }

    /**
     * 获取单条模式配置数据
     * 
     * @return
     */
    public String loadOltDhcpVlan() {
        TopOltDhcpVLANCfg vlanCfg = oltDhcpVlanService.getOltDhcpVlanCfg(entityId, vlanIndex);
        writeDataToAjax(vlanCfg);
        return NONE;
    }

    /**
     * 修改模式配置
     * 
     * @return
     */
    public String modifyOltDhcpVlan() {
        TopOltDhcpVLANCfg vlanCfg = new TopOltDhcpVLANCfg();
        vlanCfg.setEntityId(entityId);
        vlanCfg.setTopOltDhcpVLANIndex(vlanIndex);
        vlanCfg.setTopOltDhcpVLANMode(vlanMode);
        vlanCfg.setTopOltDhcpVLANRelayMode(relayMode);
        oltDhcpVlanService.modifyOltDhcpVLANCfg(vlanCfg);
        return NONE;
    }

    /**
     * 刷新模式配置列表数据
     * 
     * @return
     */
    public String refreshOltDhcpVlanList() {
        oltDhcpVlanService.refreshOltDhcpVLANCfg(entityId);
        return NONE;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getVlanIndex() {
        return vlanIndex;
    }

    public void setVlanIndex(Integer vlanIndex) {
        this.vlanIndex = vlanIndex;
    }

    public Integer getVlanMode() {
        return vlanMode;
    }

    public void setVlanMode(Integer vlanMode) {
        this.vlanMode = vlanMode;
    }

    public Integer getRelayMode() {
        return relayMode;
    }

    public void setRelayMode(Integer relayMode) {
        this.relayMode = relayMode;
    }

}
