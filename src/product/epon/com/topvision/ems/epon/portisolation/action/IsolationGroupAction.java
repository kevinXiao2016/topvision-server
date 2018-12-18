/***********************************************************************
 * $Id: PortIsolationGroupConfigAction.java,v1.0 2014-12-18 上午11:28:25 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.portisolation.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.portisolation.domain.PortIsolationGroup;
import com.topvision.ems.epon.portisolation.domain.PortIsolationGrpMember;
import com.topvision.ems.epon.portisolation.service.IsolationGroupConfigService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.domain.UserPreferences;
import com.topvision.platform.service.UserPreferencesService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author flack
 * @created @2014-12-18-上午11:28:25
 *
 */
@Controller("isolationGroupAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class IsolationGroupAction extends BaseAction {
    private static final long serialVersionUID = 4069355945041203660L;
    private Long entityId;
    private Integer groupIndex;
    private String groupDesc;
    private Long portIndex;
    private Entity entity;
    private JSONObject layout;
    private String portsStr;
    private Long typeId;

    @Autowired
    private EntityService entityService;
    @Autowired
    private UserPreferencesService userPreferencesService;
    @Autowired
    private IsolationGroupConfigService isolationGroupConfigService;


    /**
     * 显示隔离组配置页面
     * @return
     */
    public String showIsolataionGroupView() {
        entity = entityService.getEntity(entityId);
        //加载用户视图信息
        layout = this.getUserView();
        return SUCCESS;
    }

    /**
     * 显示添加隔离组页面
     * @return
     */
    public String showAddGroupPage() {
        entity = entityService.getEntity(entityId);
        typeId = entity.getTypeId();
        return SUCCESS;
    }

    /**
     * 添加隔离组
     * @return
     */
    public String addGroup() {
        PortIsolationGroup group = new PortIsolationGroup();
        group.setEntityId(entityId);
        group.setGroupIndex(groupIndex);
        group.setGroupDesc(groupDesc);
        isolationGroupConfigService.addGroup(group);
        return NONE;
    }

    /**
     * 获取隔离组
     * @return
     * @throws IOException 
     */
    public String loadGroupList() throws IOException {
        List<PortIsolationGroup> groupList = isolationGroupConfigService.getGroupList(entityId);
        JSONArray json = JSONArray.fromObject(groupList);
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 显示修改隔离组页面
     * @return
     */
    public String showModifyGroupPage() {
        groupDesc = groupDesc.replace("\"", "&quot;");
        return SUCCESS;
    }

    /**
     * 修改隔离组
     * @return
     */
    public String modifyGroup() {
        PortIsolationGroup group = new PortIsolationGroup();
        group.setEntityId(entityId);
        group.setGroupIndex(groupIndex);
        group.setGroupDesc(groupDesc);
        isolationGroupConfigService.modifyGroup(group);
        return NONE;
    }

    /**
     * 删除隔离组
     * @return
     */
    public String deleteGroup() {
        PortIsolationGroup group = new PortIsolationGroup();
        group.setEntityId(entityId);
        group.setGroupIndex(groupIndex);
        isolationGroupConfigService.deleteGroup(group);
        return NONE;
    }

    /**
     * 加载隔离组成员
     * @return
     * @throws IOException
     */
    public String loadGroupMember() throws IOException {
        List<PortIsolationGrpMember> memberList = isolationGroupConfigService.getGroupMember(entityId, groupIndex);
        JSONArray json = JSONArray.fromObject(memberList);
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 添加隔离组成员
     * @return
     */
    public String addGroupMember() {
        PortIsolationGrpMember member = new PortIsolationGrpMember();
        member.setEntityId(entityId);
        member.setGroupIndex(groupIndex);
        member.setPortIndex(portIndex);
        isolationGroupConfigService.addGroupMemeber(member);
        return NONE;
    }

    /**
     * 删除隔离组成员
     * @return
     */
    public String deleteGroupMember() {
        PortIsolationGrpMember member = new PortIsolationGrpMember();
        member.setEntityId(entityId);
        member.setGroupIndex(groupIndex);
        member.setPortIndex(portIndex);
        isolationGroupConfigService.deleteGroupMember(member);
        return NONE;
    }

    /**
     * 刷新隔离组信息
     * @return
     */
    public String refreshGroupData() {
        isolationGroupConfigService.refreshGroupData(entityId);
        return NONE;
    }

    public String checkIndexUseable() throws IOException {
        PortIsolationGroup group = isolationGroupConfigService.getGroup(entityId, groupIndex);
        JSONObject json = new JSONObject();
        if (group == null) {
            json.put("useable", true);
        } else {
            json.put("useable", false);
        }
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 批量删除隔离组成员
     * @return
     */
    public String batchDeleteGrpMember() {
        isolationGroupConfigService.deleteMemberByPorts(entityId, groupIndex, portsStr);
        return NONE;
    }

    /**
     * 批量添加隔离组成员
     * @return
     */
    public String batchAddGrpMember() {
        String[] portsList = portsStr.split(",");
        PortIsolationGrpMember member = null;
        List<PortIsolationGrpMember> memberList = new ArrayList<PortIsolationGrpMember>();
        for (String portIndex : portsList) {
            member = new PortIsolationGrpMember();
            member.setEntityId(entityId);
            member.setGroupIndex(groupIndex);
            member.setPortIndex(Long.parseLong(portIndex));
            memberList.add(member);
        }
        isolationGroupConfigService.batchAddGroupMember(memberList, entityId);
        return NONE;
    }

    /**
     * 获取用户视图信息
     * @return
     */
    private JSONObject getUserView() {
        JSONObject view = new JSONObject();
        UserContext uc = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        UserPreferences up = new UserPreferences();
        up.setModule("oltFaceplateView");
        up.setUserId(uc.getUserId());
        Properties oltView = userPreferencesService.getModulePreferences(up);
        Map<String, Object> viewMap = new HashMap<String, Object>();
        viewMap.put("rightTopHeight", oltView.get("rightTopHeight"));
        viewMap.put("middleBottomHeight", oltView.get("middleBottomHeight"));
        viewMap.put("leftWidth", oltView.get("leftWidth"));
        viewMap.put("rightWidth", oltView.get("rightWidth"));
        viewMap.put("rightTopOpen", oltView.get("rightTopOpen"));
        viewMap.put("rightBottomOpen", oltView.get("rightBottomOpen"));
        viewMap.put("middleBottomOpen", oltView.get("middleBottomOpen"));
        viewMap.put("layoutToMiddle", oltView.get("layoutToMiddle"));
        viewMap.put("tabBtnSelectedIndex", oltView.get("tabBtnSelectedIndex"));
        view = JSONObject.fromObject(viewMap);
        return view;
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

    public String getGroupDesc() {
        return groupDesc;
    }

    public void setGroupDesc(String groupDesc) {
        this.groupDesc = groupDesc;
    }

    public Long getPortIndex() {
        return portIndex;
    }

    public void setPortIndex(Long portIndex) {
        this.portIndex = portIndex;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public JSONObject getLayout() {
        return layout;
    }

    public void setLayout(JSONObject layout) {
        this.layout = layout;
    }

    public String getPortsStr() {
        return portsStr;
    }

    public void setPortsStr(String portsStr) {
        this.portsStr = portsStr;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

}
