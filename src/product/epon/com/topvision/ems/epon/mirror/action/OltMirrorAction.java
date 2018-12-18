/***********************************************************************
 * $Id: OltMirrorAction.java,v1.0 2013-10-25 下午2:11:43 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.mirror.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.exception.SetValueConflictException;
import com.topvision.ems.epon.mirror.domain.OltSniMirrorConfig;
import com.topvision.ems.epon.mirror.service.OltMirrorService;
import com.topvision.ems.epon.olt.action.AbstractEponAction;
import com.topvision.ems.epon.olt.domain.OltSniAttribute;
import com.topvision.ems.epon.olt.service.OltSniService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.annotation.OperationLogProperty;
import com.topvision.platform.domain.OperationLog;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.domain.UserPreferences;
import com.topvision.platform.service.SystemPreferencesService;
import com.topvision.platform.service.UserPreferencesService;

/**
 * @author flack
 * @created @2013-10-25-下午2:11:43
 *
 */
@Controller("oltMirrorAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OltMirrorAction extends AbstractEponAction {
    private static final long serialVersionUID = 5652078185395387725L;
    private final Logger logger = LoggerFactory.getLogger(OltMirrorAction.class);
    @Autowired
    private EntityService entityService;
    @Autowired
    private UserPreferencesService userPreferencesService;
    @Autowired
    private OltMirrorService oltMirrorService;
    @Autowired
    private SystemPreferencesService systemPreferencesService;
    @Autowired
    private OltSniService oltSniService;

    private String cameraSwitch;
    private Entity entity;
    private Integer sniMirrorGroupIndex;
    private String sniMirrorGroupName;
    private String sniMirrorGroupDstPortIndexList;
    private String sniMirrorGroupSrcInPortIndexList;
    private String sniMirrorGroupSrcOutPortIndexList;
    private String oltSoftVersion;
    private JSONObject layout;

    /**
    * 显示SNI镜像组管理
    * 
    * @return String
    */
    public String showSniMirrorMgmt() {
        entity = entityService.getEntity(entityId);
        return SUCCESS;
    }

    /**
     * MIRROR视图中修改MIRROR名称
     * 
     * @return String
     * @throws Exception
     */
    @OperationLogProperty(actionName = "oltMirrorAction", operationName = "modifyMirrorName")
    public String modifyMirrorName() throws Exception {
        String result;
        try {
            oltMirrorService.modifyMirrorName(entityId, sniMirrorGroupIndex, sniMirrorGroupName);
            result = "success";
            operationResult = OperationLog.SUCCESS;
        } catch (SetValueConflictException sce) {
            result = getString(sce.getMessage(), "epon");
            operationResult = OperationLog.FAILURE;
            logger.debug("mirrorView modifyMirrorName error:{}", sce);
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * MIRROR视图中更新目的端口、入流量端口、出流量端口列表
     * 
     * @return String
     * @throws Exception
     */
    @OperationLogProperty(actionName = "oltMirrorAction", operationName = "updateMirrorPortList")
    public String updateMirrorPortList() throws Exception {
        String result;
        List<Long> dscIndexList = new ArrayList<Long>();
        if (!sniMirrorGroupDstPortIndexList.equals("") && sniMirrorGroupDstPortIndexList.length() > 0) {
            for (String index : sniMirrorGroupDstPortIndexList.split(",")) {
                dscIndexList.add(Long.parseLong(index));
            }
        }
        List<Long> srcOutIndexList = new ArrayList<Long>();
        if (!sniMirrorGroupSrcOutPortIndexList.equals("") && sniMirrorGroupSrcOutPortIndexList.length() > 0) {
            for (String index : sniMirrorGroupSrcOutPortIndexList.split(",")) {
                srcOutIndexList.add(Long.parseLong(index));
            }
        }
        List<Long> srcInIndexList = new ArrayList<Long>();
        if (!sniMirrorGroupSrcInPortIndexList.equals("") && sniMirrorGroupSrcInPortIndexList.length() > 0) {
            for (String index : sniMirrorGroupSrcInPortIndexList.split(",")) {
                srcInIndexList.add(Long.parseLong(index));
            }
        }
        try {
            oltMirrorService.updateMirrorPortList(entityId, sniMirrorGroupIndex, srcOutIndexList, srcInIndexList,
                    dscIndexList);
            result = "success";
            operationResult = OperationLog.SUCCESS;
        } catch (SetValueConflictException sce) {
            result = getString(sce.getMessage(), "epon");
            operationResult = OperationLog.FAILURE;
            logger.debug("mirrorView updateMirrorPortList error:{}", sce);
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * OLT mirror视图
     * 
     * @return String
     */
    public String showOltMirrorView() {
        Properties properties = systemPreferencesService.getModulePreferences("camera");
        cameraSwitch = properties.getProperty("camera.switch");
        entity = entityService.getEntity(entityId);
        oltSoftVersion = oltService.getOltSoftVersion(entityId);
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

    /**
     * MIRROR视图中获取MIRROR列表数据
     * 
     * @return String
     * @throws Exception
     * @throws java.io.IOException
     */
    public String loadMirrorConfigList() throws IOException {
        Map<String, Object> mirrorJson = new HashMap<String, Object>();
        List<OltSniMirrorConfig> oltMirrorConfigList = oltMirrorService.getMirrorConfigList(entityId);
        handleMirrorDestPortName(entityId, oltMirrorConfigList);
        mirrorJson.put("data", oltMirrorConfigList);
        writeDataToAjax(JSONObject.fromObject(mirrorJson));
        return NONE;
    }

    /**
     * 处理Mirror配置中目的端口名称
     * @param entityId
     * @param mirrorList
     */
    private void handleMirrorDestPortName(Long entityId, List<OltSniMirrorConfig> mirrorList) {
        for (OltSniMirrorConfig mirrorConfig : mirrorList) {
            if (!mirrorConfig.getSniMirrorGroupDstPortIndexList().isEmpty()) {
                Long destSniIndex = mirrorConfig.getSniMirrorGroupDstPortIndexList().get(0);
                //处理默认目的端口为0的情况
                if (destSniIndex > 0L) {
                    OltSniAttribute sniAttribue = oltSniService.getSniAttribute(entityId, destSniIndex);
                    mirrorConfig.setDestPortName(sniAttribue.getSniDisplayName());
                }
            }
        }
    }

    /**
     * 设置镜像组名称页面跳转
     * 
     * @return String String
     */
    public String showMirrorNameJsp() {
        return SUCCESS;
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

    public Integer getSniMirrorGroupIndex() {
        return sniMirrorGroupIndex;
    }

    public void setSniMirrorGroupIndex(Integer sniMirrorGroupIndex) {
        this.sniMirrorGroupIndex = sniMirrorGroupIndex;
    }

    public String getSniMirrorGroupName() {
        return sniMirrorGroupName;
    }

    public void setSniMirrorGroupName(String sniMirrorGroupName) {
        this.sniMirrorGroupName = sniMirrorGroupName;
    }

    public String getSniMirrorGroupDstPortIndexList() {
        return sniMirrorGroupDstPortIndexList;
    }

    public void setSniMirrorGroupDstPortIndexList(String sniMirrorGroupDstPortIndexList) {
        this.sniMirrorGroupDstPortIndexList = sniMirrorGroupDstPortIndexList;
    }

    public String getSniMirrorGroupSrcInPortIndexList() {
        return sniMirrorGroupSrcInPortIndexList;
    }

    public void setSniMirrorGroupSrcInPortIndexList(String sniMirrorGroupSrcInPortIndexList) {
        this.sniMirrorGroupSrcInPortIndexList = sniMirrorGroupSrcInPortIndexList;
    }

    public String getSniMirrorGroupSrcOutPortIndexList() {
        return sniMirrorGroupSrcOutPortIndexList;
    }

    public void setSniMirrorGroupSrcOutPortIndexList(String sniMirrorGroupSrcOutPortIndexList) {
        this.sniMirrorGroupSrcOutPortIndexList = sniMirrorGroupSrcOutPortIndexList;
    }

    public String getOltSoftVersion() {
        return oltSoftVersion;
    }

    public void setOltSoftVersion(String oltSoftVersion) {
        this.oltSoftVersion = oltSoftVersion;
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

}
