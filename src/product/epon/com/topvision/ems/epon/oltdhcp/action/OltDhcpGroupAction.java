/***********************************************************************
 * $Id: OltDhcpGroupAction.java,v1.0 2017年11月24日 上午9:29:07 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.oltdhcp.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltDhcpServerGroup;
import com.topvision.ems.epon.oltdhcp.service.OltDhcpGroupService;
import com.topvision.framework.web.struts2.BaseAction;

import net.sf.json.JSONObject;

/**
 * @author haojie
 * @created @2017年11月24日-上午9:29:07
 *
 */
@Controller("oltDhcpGroupAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OltDhcpGroupAction extends BaseAction {
    private static final long serialVersionUID = 6901564219067188375L;
    private Long entityId;
    private Integer groupIndex;
    private String serverIpList;
    private String type;// 跳转模式，新增/修改
    private String availableGroupId;
    private String serverIpsDisplay;
    @Autowired
    private OltDhcpGroupService oltDhcpGroupService;

    /**
     * 获取服务器组列表数据
     * 
     * @return
     */
    public String loadOltDhcpGroupList() {
        Map<String, Object> json = new HashMap<String, Object>();
        List<TopOltDhcpServerGroup> groups = oltDhcpGroupService.getOltDhcpServerGroup(entityId);
        json.put("data", groups);
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 跳转新增服务器组页面
     * 
     * @return
     */
    public String showAddOltDhcpGroup() {
        List<TopOltDhcpServerGroup> groups = oltDhcpGroupService.getOltDhcpServerGroup(entityId);
        List<Integer> ids = new ArrayList<Integer>();
        for (int i = 0; i < 20; i++) {
            ids.add(i + 1);
        }
        for (int i = 0; i < groups.size(); i++) {
            ids.remove(groups.get(i).getTopOltDhcpServerGroupIndex());
        }
        availableGroupId = StringUtils.join(ids.toArray(), ",");
        return SUCCESS;
    }

    /**
     * 新增服务器组
     * 
     * @return
     */
    public String addOltDhcpGroup() {
        TopOltDhcpServerGroup group = new TopOltDhcpServerGroup();
        group.setEntityId(entityId);
        group.setTopOltDhcpServerGroupIndex(groupIndex);
        //group.setTopOltDhcpServerIpList(serverIpList);
        group.setServerIpsDisplay(serverIpsDisplay);
        oltDhcpGroupService.addOltDhcpServerGroup(group);
        return NONE;
    }

    /**
     * 跳转修改服务器组页面
     * 
     * @return
     */
    public String showModifyOltDhcpGroup() {
        return SUCCESS;
    }

    /**
     * 获取单条服务器组数据
     * 
     * @return
     */
    public String loadOltDhcpGroup() {
        TopOltDhcpServerGroup group = oltDhcpGroupService.getOltDhcpServerGroup(entityId, groupIndex);
        writeDataToAjax(group);
        return NONE;
    }

    /**
     * 修改服务器组
     * 
     * @return
     */
    public String modifyOltDhcpGroup() {
        TopOltDhcpServerGroup group = new TopOltDhcpServerGroup();
        group.setEntityId(entityId);
        group.setTopOltDhcpServerGroupIndex(groupIndex);
        //group.setTopOltDhcpServerIpList(serverIpList);
        group.setServerIpsDisplay(serverIpsDisplay);
        oltDhcpGroupService.modifyOltDhcpServerGroup(group);
        return NONE;
    }

    /**
     * 删除服务器组
     * 
     * @return
     */
    public String deleteOltDhcpGroup() {
        oltDhcpGroupService.deleteOltDhcpServerGroup(entityId, groupIndex);
        return NONE;
    }

    /**
     * 刷新服务器组列表数据
     * 
     * @return
     */
    public String refreshOltDhcpGroupList() {
        oltDhcpGroupService.refreshOltDhcpServerGroup(entityId);
        return NONE;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getGroupIndex() {
        return groupIndex;
    }

    public void setGroupIndex(Integer groupIndex) {
        this.groupIndex = groupIndex;
    }

    public String getServerIpList() {
        return serverIpList;
    }

    public void setServerIpList(String serverIpList) {
        this.serverIpList = serverIpList;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAvailableGroupId() {
        return availableGroupId;
    }

    public void setAvailableGroupId(String availableGroupId) {
        this.availableGroupId = availableGroupId;
    }

    public String getServerIpsDisplay() {
        return serverIpsDisplay;
    }

    public void setServerIpsDisplay(String serverIpsDisplay) {
        this.serverIpsDisplay = serverIpsDisplay;
    }

}
