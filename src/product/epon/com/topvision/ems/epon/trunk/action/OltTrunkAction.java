/***********************************************************************
 * $Id: OltTrunkAction.java,v1.0 2013-10-25 下午3:20:37 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.trunk.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.exception.SetValueConflictException;
import com.topvision.ems.epon.exception.SniTrunkConfigException;
import com.topvision.ems.epon.olt.action.AbstractEponAction;
import com.topvision.ems.epon.olt.domain.OltSniAttribute;
import com.topvision.ems.epon.olt.service.OltBfsxService;
import com.topvision.ems.epon.olt.service.OltSniService;
import com.topvision.ems.epon.trunk.domain.OltSniTrunkConfig;
import com.topvision.ems.epon.trunk.service.OltTrunkService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.annotation.OperationLogProperty;
import com.topvision.framework.exception.engine.SnmpException;
import com.topvision.platform.domain.OperationLog;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.domain.UserPreferences;
import com.topvision.platform.service.SystemPreferencesService;
import com.topvision.platform.service.UserPreferencesService;

/**
 * @author flack
 * @created @2013-10-25-下午3:20:37
 *
 */
@Controller("oltTrunkAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OltTrunkAction extends AbstractEponAction {
    private static final long serialVersionUID = 2139862495404257139L;
    private final Logger logger = LoggerFactory.getLogger(OltTrunkAction.class);
    private Entity entity;
    private String oltSoftVersion;
    private JSONObject layout;
    private String sniTrunkGroupConfigName;
    private Integer sniTrunkGroupConfigIndex;// TODO 验证到底是用index还是string
    private String sniTrunkGroupConfigGroup;
    private Integer sniTrunkGroupConfigPolicy;
    private Long sniId;
    @Autowired
    private EntityService entityService;
    @Autowired
    private UserPreferencesService userPreferencesService;
    @Autowired
    private OltTrunkService oltTrunkService;
    @Autowired
    private SystemPreferencesService systemPreferencesService;
    @Autowired
    private OltSniService oltSniService;
    @Autowired
    private OltBfsxService oltBfsxService;

    private String cameraSwitch;

    /**
     * 显示SNITrunk组管理
     * 
     * @return String
     */
    public String showSniTrunkGroupMgmt() {
        entity = entityService.getEntity(entityId);
        return SUCCESS;
    }

    /**
     * OLT Trunk视图
     * 
     * @return String
     */
    public String showOltTrunkView() {
        Properties properties = systemPreferencesService.getModulePreferences("camera");
        cameraSwitch = properties.getProperty("camera.switch");
        oltSoftVersion = oltService.getOltSoftVersion(entityId);
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

    /**
     * 跳转到添加trunk组页面
     * 
     * @return String
     */
    public String jumpTrunkGroupPage() {
        return SUCCESS;
    }

    /**
     * Trunk视图中获取Trunk列表数据
     * 
     * @return String
     * @throws java.io.IOException
     */
    public String loadTrunkConfigList() throws IOException {
        Map<String, Object> TrunkJson = new HashMap<String, Object>();
        List<OltSniTrunkConfig> oltTrunkConfigList = oltTrunkService.getTrunkConfigList(entityId);
        handleTrunkPortName(entityId, oltTrunkConfigList);
        TrunkJson.put("data", oltTrunkConfigList);
        writeDataToAjax(JSONObject.fromObject(TrunkJson));
        return NONE;
    }

    /**
     * 处理TRUNK组中SNI端口名称
     * @param entityId
     * @param trunkList
     */
    private void handleTrunkPortName(Long entityId, List<OltSniTrunkConfig> trunkList) {
        for (OltSniTrunkConfig trunkConfig : trunkList) {
            List<String> sniNameList = new ArrayList<String>();
            for (Long sniIndex : trunkConfig.getSniTrunkGroupConfigGroup()) {
                OltSniAttribute sniAttribue = oltSniService.getSniAttribute(entityId, sniIndex);
                sniNameList.add(sniAttribue.getSniDisplayName());
            }
            trunkConfig.setTrunkPortNameList(sniNameList);
        }
    }

    /**
     * 得到TRUNK组的可用端口列表
     * 
     * @return String
     * @throws Exception
     */
    public String getAvailblePortTrunkList() throws Exception {
        Map<String, Object> groupJson = new HashMap<String, Object>();
        List<OltSniAttribute> sniList = new ArrayList<OltSniAttribute>();
        try {
            sniList = oltTrunkService.availableSniList(entityId, sniTrunkGroupConfigIndex, null);
        } catch (SniTrunkConfigException e) {
            logger.debug("getAvailblePortTrunkList error: {}", e);
            operationResult = OperationLog.FAILURE;
        }
        groupJson.put("data", sniList);
        writeDataToAjax(JSONObject.fromObject(groupJson));
        return NONE;
    }

    /**
     * 加载所有满足能加入trunk组功能的SNI端口列表
     * 
     * @return String
     * @throws java.io.IOException
     */
    public String loadAvailbleListFirst() throws IOException {
        Map<String, Object> avaJson = new HashMap<String, Object>();
        List<OltSniAttribute> avaLst = new ArrayList<OltSniAttribute>();
        try {
            avaLst = oltTrunkService.availableSniList(entityId, null, null);
        } catch (SniTrunkConfigException e) {
            logger.debug("loadAvailbleListFirst error: {}", e);
            operationResult = OperationLog.FAILURE;
        }
        avaJson.put("data", avaLst);
        writeDataToAjax(JSONObject.fromObject(avaJson));
        return NONE;
    }

    /**
     * 当有一个端口准备加入到trunk组时，更新可加入trunk组功能的端口列表
     * 
     * @return String
     * @throws java.io.IOException
     */
    public String loadAvailbleListWithSni() throws IOException {
        List<OltSniAttribute> avaLst = oltTrunkService.availableSniList(entityId, null, sniId);
        JSONArray.fromObject(avaLst).write(response.getWriter());
        return NONE;
    }

    /**
     * 新建一个Trunk组
     * 
     * @return String
     * @throws Exception
     */
    @OperationLogProperty(actionName = "oltTrunkAction", operationName = "createTrunkGroup")
    public String createTrunkGroup() throws Exception {
        String result = "success";
        OltSniTrunkConfig group = new OltSniTrunkConfig();
        group.setEntityId(entityId);
        group.setSniTrunkGroupConfigName(sniTrunkGroupConfigName);
        String[] groupMember = sniTrunkGroupConfigGroup.split("@");
        List<Long> groupMemberLong = new ArrayList<Long>();
        for (String member : groupMember) {
            groupMemberLong.add(Long.parseLong(member));
        }
        group.setSniTrunkGroupConfigGroup(groupMemberLong);
        group.setSniTrunkGroupConfigPolicy(sniTrunkGroupConfigPolicy);
        try {
            oltTrunkService.addSniTrunkConfig(group);
            oltBfsxService.bfsxOltTrunk(entityId);
            operationResult = OperationLog.SUCCESS;
        } catch (SniTrunkConfigException e) {
            logger.debug("createTrunkGroup  is error:{}", e);
            result = "SniTrunkConfigException:" + e.getMessage();
            operationResult = OperationLog.FAILURE;
        } catch (SnmpException e) {
            logger.debug("createTrunkGroup SNMP error: {}", e);
            result = "SNMP error:" + e.getMessage();
            operationResult = OperationLog.FAILURE;
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * 删除一个trunk组
     * 
     * @return String
     * @throws Exception
     */
    @OperationLogProperty(actionName = "oltTrunkAction", operationName = "dropTrunkGroup")
    public String dropTrunkGroup() throws Exception {
        String result = "success";
        try {
            oltTrunkService.deleteSniTrunkConfig(entityId, sniTrunkGroupConfigIndex);
            operationResult = OperationLog.SUCCESS;
        } catch (SnmpException se) {
            logger.debug("deleteSniTrunkConfig error:{}", se);
            operationResult = OperationLog.FAILURE;
            result = "error";
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * 修改Trunk组，比如添加成员，删除成员，修改名称等
     * 
     * @return String
     * @throws Exception
     */
    @OperationLogProperty(actionName = "oltTrunkAction", operationName = "modifyTrunkGroup")
    public String modifyTrunkGroup() throws Exception {
        String result = "success";
        OltSniTrunkConfig group = new OltSniTrunkConfig();
        group.setEntityId(entityId);
        if (!"".equals(sniTrunkGroupConfigName)) {
            group.setSniTrunkGroupConfigName(sniTrunkGroupConfigName);
        }
        String[] groupMember = sniTrunkGroupConfigGroup.split("@");
        List<Long> groupMemberLong = new ArrayList<Long>();
        for (String member : groupMember) {
            groupMemberLong.add(Long.parseLong(member));
        }
        group.setSniTrunkGroupConfigGroup(groupMemberLong);
        group.setSniTrunkGroupConfigPolicy(sniTrunkGroupConfigPolicy);
        group.setSniTrunkGroupConfigIndex(sniTrunkGroupConfigIndex);
        try {
            oltTrunkService.modifySniTrunkConfig(group);
            oltBfsxService.bfsxOltTrunk(entityId);
            operationResult = OperationLog.SUCCESS;
        } catch (SniTrunkConfigException e) {
            result = "error";
            operationResult = OperationLog.FAILURE;
            logger.debug("modifyTrunkGroup  is error:{}", e);
        } catch (SetValueConflictException e) {
            result = "error";
            operationResult = OperationLog.FAILURE;
            logger.debug("modifyTrunkGroup  is error:{}", e);
        }
        writeDataToAjax(result);
        return NONE;
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

    public String getSniTrunkGroupConfigName() {
        return sniTrunkGroupConfigName;
    }

    public void setSniTrunkGroupConfigName(String sniTrunkGroupConfigName) {
        this.sniTrunkGroupConfigName = sniTrunkGroupConfigName;
    }

    public Integer getSniTrunkGroupConfigIndex() {
        return sniTrunkGroupConfigIndex;
    }

    public void setSniTrunkGroupConfigIndex(Integer sniTrunkGroupConfigIndex) {
        this.sniTrunkGroupConfigIndex = sniTrunkGroupConfigIndex;
    }

    public String getSniTrunkGroupConfigGroup() {
        return sniTrunkGroupConfigGroup;
    }

    public void setSniTrunkGroupConfigGroup(String sniTrunkGroupConfigGroup) {
        this.sniTrunkGroupConfigGroup = sniTrunkGroupConfigGroup;
    }

    public Integer getSniTrunkGroupConfigPolicy() {
        return sniTrunkGroupConfigPolicy;
    }

    public void setSniTrunkGroupConfigPolicy(Integer sniTrunkGroupConfigPolicy) {
        this.sniTrunkGroupConfigPolicy = sniTrunkGroupConfigPolicy;
    }

    public Long getSniId() {
        return sniId;
    }

    public void setSniId(Long sniId) {
        this.sniId = sniId;
    }

    public String getCameraSwitch() {
        return cameraSwitch;
    }

    public void setCameraSwitch(String cameraSwitch) {
        this.cameraSwitch = cameraSwitch;
    }

}
