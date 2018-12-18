/***********************************************************************
 * $Id: OltDhcpPortAction.java,v1.0 2017年11月24日 上午9:29:20 $
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

import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltDhcpPortAttribute;
import com.topvision.ems.epon.oltdhcp.service.OltDhcpPortService;
import com.topvision.framework.web.struts2.BaseAction;

import net.sf.json.JSONObject;

/**
 * @author haojie
 * @created @2017年11月24日-上午9:29:20
 *
 */
@Controller("oltDhcpPortAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OltDhcpPortAction extends BaseAction {
    private static final long serialVersionUID = -5713270273847150456L;
    public static final Integer PORTTRANS = 1;
    public static final Integer PORTTRUST = 2;
    
    private Long entityId;
    private Integer portProtIndex;
    private Integer portTypeIndex;
    private Integer slotIndex;
    private Integer portIndex;
    private Integer portCascade;
    private Integer portTrans;
    private Integer portTrust;
    @Autowired
    private OltDhcpPortService oltDhcpPortService;

    /**
     * 跳转端口配置列表页面
     * 
     * @return
     */
    public String showPortAttributeList() {
        return SUCCESS;
    }

    /**
     * 获取端口配置列表数据
     * 
     * @return
     */
    public String loadOltDhcpPortList() {
        Map<String, Object> json = new HashMap<String, Object>();
        List<TopOltDhcpPortAttribute> ports = oltDhcpPortService.getPortAttribute(entityId, portProtIndex);
        json.put("data", ports);
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 跳转修改端口配置页面
     * 
     * @return
     */
    public String showModifyOltDhcpPort() {
        return SUCCESS;
    }

    /**
     * 获取单条端口配置数据
     * 
     * @return
     */
    public String loadOltDhcpPort() {
        TopOltDhcpPortAttribute port = oltDhcpPortService.getPortAttribute(entityId, portProtIndex, portTypeIndex,
                slotIndex, portIndex);
        writeDataToAjax(port);
        return NONE;
    }

    /**
     * 修改端口配置
     * 
     * @return
     */
    public String modifyOltDhcpPort() {
        TopOltDhcpPortAttribute port = new TopOltDhcpPortAttribute();
        port.setEntityId(entityId);
        port.setTopOltDhcpPortProtIndex(portProtIndex);
        port.setTopOltDhcpPortTypeIndex(portTypeIndex);
        port.setTopOltDhcpSlotIndex(slotIndex);
        port.setTopOltDhcpPortIndex(portIndex);
        port.setTopOltDhcpPortCascade(portCascade);
        if (portCascade == PORTTRANS) {
            port.setTopOltDhcpPortTrans(portTrans);
        } else if (portCascade == PORTTRUST) {
            port.setTopOltDhcpPortTrust(portTrust);
        }
        oltDhcpPortService.modifyPortAttribute(port);
        return NONE;
    }

    /**
     * 刷新端口配置列表数据
     * 
     * @return
     */
    public String refreshOltDhcpPortList() {
        oltDhcpPortService.refreshPortAttribute(entityId, portProtIndex);
        return NONE;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getPortProtIndex() {
        return portProtIndex;
    }

    public void setPortProtIndex(Integer portProtIndex) {
        this.portProtIndex = portProtIndex;
    }

    public Integer getPortTypeIndex() {
        return portTypeIndex;
    }

    public void setPortTypeIndex(Integer portTypeIndex) {
        this.portTypeIndex = portTypeIndex;
    }

    public Integer getSlotIndex() {
        return slotIndex;
    }

    public void setSlotIndex(Integer slotIndex) {
        this.slotIndex = slotIndex;
    }

    public Integer getPortIndex() {
        return portIndex;
    }

    public void setPortIndex(Integer portIndex) {
        this.portIndex = portIndex;
    }

    public Integer getPortCascade() {
        return portCascade;
    }

    public void setPortCascade(Integer portCascade) {
        this.portCascade = portCascade;
    }

    public Integer getPortTrans() {
        return portTrans;
    }

    public void setPortTrans(Integer portTrans) {
        this.portTrans = portTrans;
    }

    public Integer getPortTrust() {
        return portTrust;
    }

    public void setPortTrust(Integer portTrust) {
        this.portTrust = portTrust;
    }

}
