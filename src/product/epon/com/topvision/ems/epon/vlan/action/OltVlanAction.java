/***********************************************************************
 * $Id: SniVlanAction.java,v1.0 2013-10-25 下午1:50:56 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.vlan.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.olt.action.AbstractEponAction;
import com.topvision.ems.epon.olt.domain.OltSlotAttribute;
import com.topvision.ems.epon.olt.service.OltSlotService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.service.EntityService;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.domain.UserPreferences;
import com.topvision.platform.service.SystemPreferencesService;
import com.topvision.platform.service.UserPreferencesService;

import net.sf.json.JSONObject;

/**
 * @author flack
 * @created @2013-10-25-下午1:50:56
 *
 */
@Controller("oltVlanAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OltVlanAction extends AbstractEponAction {
    private static final long serialVersionUID = -1868464203033356012L;

    private Entity entity;
    private String entityName;
    private JSONObject layout;
    @Autowired
    private EntityService entityService;
    @Autowired
    private OltSlotService oltSlotService;
    @Autowired
    private UserPreferencesService userPreferencesService;
    @Autowired
    private SystemPreferencesService systemPreferencesService;
    private String cameraSwitch;
    private JSONObject slotTypeMapping = new JSONObject();

    public String showOltVlanList() {
        Properties properties = systemPreferencesService.getModulePreferences("camera");
        cameraSwitch = properties.getProperty("camera.switch");
        entity = entityService.getEntity(entityId);
        entityName = entity.getDisplayName();
        // add by fanzidong, 获取设备的板卡信息
        try{
            List<OltSlotAttribute> slotList = oltSlotService.getOltSlotList(entityId);
            for(OltSlotAttribute slot : slotList) {
                slotTypeMapping.put(slot.getBmapSlotLogNo(), slot.getTopSysBdPreConfigType());
            }
        } catch(Exception e) {
            logger.debug("cannot get entity slot type info, " + e);
        }
        return SUCCESS;
    }

    /**
     * OLT vlan视图
     * 
     * @return String
     */
    public String showOltVlanView() {
        Properties properties = systemPreferencesService.getModulePreferences("camera");
        cameraSwitch = properties.getProperty("camera.switch");
        entity = entityService.getEntity(entityId);
        //加载用户视图信息
        layout = this.getUserView();
        return SUCCESS;
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

    public EntityService getEntityService() {
        return entityService;
    }

    public void setEntityService(EntityService entityService) {
        this.entityService = entityService;
    }

    public UserPreferencesService getUserPreferencesService() {
        return userPreferencesService;
    }

    public void setUserPreferencesService(UserPreferencesService userPreferencesService) {
        this.userPreferencesService = userPreferencesService;
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

    public String getCameraSwitch() {
        return cameraSwitch;
    }

    public void setCameraSwitch(String cameraSwitch) {
        this.cameraSwitch = cameraSwitch;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public JSONObject getSlotTypeMapping() {
        return slotTypeMapping;
    }

    public void setSlotTypeMapping(JSONObject slotTypeMapping) {
        this.slotTypeMapping = slotTypeMapping;
    }

}
