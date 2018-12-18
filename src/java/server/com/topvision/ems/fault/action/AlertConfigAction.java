package com.topvision.ems.fault.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.alibaba.dubbo.rpc.cluster.merger.BooleanArrayMerger;
import com.topvision.ems.fault.domain.AlertType;
import com.topvision.ems.fault.domain.EventType;
import com.topvision.ems.fault.domain.Level;
import com.topvision.ems.fault.service.AlertConfirmConfigService;
import com.topvision.ems.fault.service.AlertService;
import com.topvision.ems.fault.service.EventService;
import com.topvision.ems.fault.service.FaultService;
import com.topvision.framework.domain.CheckTreeNode;
import com.topvision.framework.domain.TreeNode;
import com.topvision.framework.utils.EponConstants;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.domain.Action;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.service.UserService;
import com.topvision.platform.domain.User;



import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller("alertConfigAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AlertConfigAction extends BaseAction {
    private static final long serialVersionUID = -9092498528028215229L;
    private final Logger logger = LoggerFactory.getLogger(AlertConfigAction.class);
    @Autowired
    private FaultService faultService;
    @Autowired
    private AlertService alertService;
    @Autowired
    private EventService eventService;
    @Autowired
    private UserService userService;
    @Autowired
    private AlertConfirmConfigService alertConfirmConfigService;
    private List<Action> actions;
    private AlertType alertType;
    private List<Long> actionIds;
    private int alertTypeId;
    private String alarmTimes;
    private boolean smartUpdate;
    private int updateLevel;
    private boolean active;
    private byte level = Level.WARNING_LEVEL;
    private String note;
    private Integer activeNum;
    private String confirmAlertTypeString;
    private String originEventTypeString;
    private Boolean userAlertActionChooseEmail;
    private Boolean userAlertActionChooseMobile;
    // alertConfirmConfig
    private Boolean alertConfirmConfig;
    private Long userId;
    private UserContext uc;
    private User user;

    // load user alert type
    private Boolean loadUserAlertType = false;

    public String getAlertTypeById() throws Exception {
        ResourceManager resourceManager = ResourceManager.getResourceManager("com.topvision.ems.fault.resources");
        JSONObject json = new JSONObject();
        alertType = alertService.getAlertTypeById(alertTypeId);
        actionIds = alertService.getActionsOfAlertType(alertTypeId);
        json.put("typeId", alertType.getTypeId());
        json.put("category", alertType.getCategory());
        json.put("name", resourceManager.getString(alertType.getDisplayName()));
        json.put("note", alertType.getText());
        if (alertType.getNote() == null) {
            alertType.setNote("");
        }
        json.put("alarmDesc", alertType.getNote());// 描述信息
        json.put("level", alertType.getLevelId());
        json.put("active", alertType.getActive());
        json.put("threshold", alertType.getThreshold());
        json.put("updateLevel", alertType.getUpdateLevel());
        json.put("alarmTimes", alertType.getAlertTimes());
        json.put("smartUpdate", alertType.getSmartUpdate());
        JSONArray array = new JSONArray();
        JSONObject temp = null;
        if (actionIds != null) {
            for (int i = 0; i < actionIds.size(); i++) {
                temp = new JSONObject();
                temp.put("actionId", actionIds.get(i));
                array.add(temp);
            }
        }
        json.put("actions", array);
        writeDataToAjax(json);
        return NONE;
    }

    public String loadAlertType() throws Exception {
        ResourceManager resourceManager = ResourceManager.getResourceManager("com.topvision.ems.fault.resources");
        List<AlertType> list = alertService.getAllAlertTypes();
        // 用户告警类型展示
        List<Integer> userAlert = null;
        // 用户告警列表中的告警类型
        Set<Integer> userAlertTypeList = null;
        UserContext uc = null;
        try {
            uc = (UserContext) super.getSession().get(UserContext.KEY);
            if (uc != null) {
                userAlert = userService.getUserAlertTypeId(uc.getUserId());
                if (loadUserAlertType) {
                    userAlertTypeList = alertService.getUserAlertTypeCollection(userAlert);
                }
            }
        } catch (Exception e) {
            logger.error("load user alert error:", e);
        }
        List<TreeNode> treeNodes = new ArrayList<TreeNode>();
        HashMap<String, CheckTreeNode> map = new HashMap<String, CheckTreeNode>(list == null ? 0 : list.size());

        // 遍历，将告警主分类加入jsonAlertType中
        for (AlertType alertType : list) {
            if (alertType.getCategory() != 0) {
                continue;
            }
            if (userAlertTypeList != null && !userAlertTypeList.contains(alertType.getTypeId())) {
                continue;
            }
            CheckTreeNode treeNode = new CheckTreeNode();
            treeNode.setText(resourceManager.getString(alertType.getDisplayName()));
            treeNode.setId(Long.parseLong(alertType.getTypeId().toString()));
            treeNode.setValue(alertType.getTypeId().toString());
            treeNode.setParentId(0l);
            treeNode.setIconCls("folder");
            treeNode.setNeedATag(true);
            treeNode.setChildren(new ArrayList<TreeNode>());
            map.put(treeNode.getId().toString(), treeNode);
            treeNodes.add(treeNode);
        }
        // 再次遍历，将各叶子告警插入相应分类之下
        for (AlertType alertType : list) {
            if (alertType.getCategory() == 0) {
                continue;
            }
            if (alertType.getTypeId().equals(200002) || alertType.getTypeId().equals(12326)) {
                // 移出spectrumAlertClearType和光信号恢复告警
                continue;
            }
            if (userAlertTypeList != null && !userAlertTypeList.contains(alertType.getTypeId())) {
                continue;
            }
            CheckTreeNode treeNode = new CheckTreeNode();
            treeNode.setText(resourceManager.getString(alertType.getDisplayName()));
            treeNode.setId(Long.parseLong(alertType.getTypeId().toString()));
            treeNode.setParentId(Long.parseLong(alertType.getParentId().toString()));
            if (alertType.getCategory() == -50001 || alertType.getCategory() == -50002) {
                treeNode.setIconCls("");
            } else {
                treeNode.setIconCls("level" + alertType.getLevelId() + "Icon");
            }

            //Add by Rod For User Alert
            if (userAlert != null && userAlert.contains(alertType.getTypeId())) {
                treeNode.setChecked(true);
            }

            treeNode.setValue(alertType.getTypeId().toString());
            treeNode.setNeedATag(true);
            treeNode.setChildren(new ArrayList<TreeNode>());
            // map的存在意义是始终保留指向相应节点的引用，这样可以方便的修改其children，快速构建正确的树结构
            map.put(String.valueOf(alertType.getTypeId()), treeNode);
            CheckTreeNode parentNode = map.get(String.valueOf(alertType.getParentId()));
            if(!uc.hasSupportModule("cmc")){
            	if(EponConstants.CC_ALERT_TYPE.contains(alertType.getCategory()) || EponConstants.CC_ALERT_TYPE.contains(alertType.getTypeId())){
            		continue;
            	}
            }
            if (parentNode == null) {
                treeNodes.add(treeNode);
            } else {
                parentNode.getChildren().add(treeNode);
                parentNode.setIconCls("folder");
            }
        }
        net.sf.json.JSONArray jsonAlertType = new net.sf.json.JSONArray();
        CheckTreeNode allType = new CheckTreeNode();
        allType.setText(resourceManager.getString("ALERT.allType"));
        allType.setId(0L);
        allType.setParentId(-1L);
        allType.setIconCls("folder");
        allType.setNeedATag(true);
        allType.setChildren(treeNodes);
        jsonAlertType.add(allType);
        // jsonAlertType.addAll(treeNodes);
        writeDataToAjax(jsonAlertType);
        return NONE;
    }
    
    public String loadConcernAlertTypes() {
        List<Long> concernAlertTypes = alertService.getConcernAlertTypes();
        writeDataToAjax(concernAlertTypes);
        return NONE;
    }

    public String saveAlertType() throws Exception {
        String message;
        try {
            List<EventType> confirmEventTypes = alertConfirmConfigService.getConfirmEventByEventTypeId(alertTypeId);
            List<EventType> originEventTypes = alertConfirmConfigService.getOriginEventByEventTypeId(alertTypeId);
            List<Integer> confirmEventIntegers = new ArrayList<Integer>();
            List<Integer> originEventIntegers = new ArrayList<Integer>();
            for (EventType type : confirmEventTypes) {
                confirmEventIntegers.add(type.getTypeId());
            }
            for (EventType type : originEventTypes) {
                originEventIntegers.add(type.getTypeId());
            }
            // 配置清除事件
            if (!"".equals(confirmAlertTypeString)) {
                String typeIdList[] = confirmAlertTypeString.split(",");
                List<Integer> types1 = new ArrayList<Integer>();
                List<Integer> types2 = new ArrayList<Integer>();
                for (int i = 0; i < typeIdList.length; i++) {
                    types1.add(Integer.parseInt(typeIdList[i]));
                    types2.add(Integer.parseInt(typeIdList[i]));
                }
                types1.removeAll(confirmEventIntegers);
                for (Integer typeId : types1) {
                    alertConfirmConfigService.insertConfirmAlert(alertTypeId, typeId);
                }
                confirmEventIntegers.removeAll(types2);
                for (Integer typeId : confirmEventIntegers) {
                    alertConfirmConfigService.deleteConfirmAlert(alertTypeId, typeId);
                }
            } else {
                for (Integer typeId : confirmEventIntegers) {
                    alertConfirmConfigService.deleteConfirmAlert(alertTypeId, typeId);
                }
            }
            // 配置来源事件
            if (!"".equals(originEventTypeString)) {
                String typeIdList[] = originEventTypeString.split(",");
                List<Integer> types1 = new ArrayList<Integer>();
                List<Integer> types2 = new ArrayList<Integer>();
                for (int i = 0; i < typeIdList.length; i++) {
                    types1.add(Integer.parseInt(typeIdList[i]));
                    types2.add(Integer.parseInt(typeIdList[i]));
                }
                types1.removeAll(originEventIntegers);
                for (Integer typeId : types1) {
                    alertConfirmConfigService.insertOriginAlert(typeId, alertTypeId);
                }
                originEventIntegers.removeAll(types2);
                for (Integer typeId : originEventIntegers) {
                    alertConfirmConfigService.deleteOriginAlert(typeId, alertTypeId);
                }
            } else {
                for (Integer typeId : originEventIntegers) {
                    alertConfirmConfigService.deleteOriginAlert(typeId, alertTypeId);
                }
            }
            alertService.updateAlertType(alertTypeId, level, active, updateLevel, alarmTimes, smartUpdate, actionIds,
                    note);
            eventService.reset();
            message = "success";
        } catch (Exception e) {
            message = e.getMessage();
            logger.debug("saveAlertType error:{}", e);
            message = "error";
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 跳转告警确认全局配置
     *
     * @return
     */
    public String showAlertConfirmConfig() {
        alertConfirmConfig = alertConfirmConfigService.getAlertConfirmConfig();
        return SUCCESS;
    }

    /**
     * 修改告警确认全局配置
     *
     * @return
     * @throws Exception
     */
    public String saveAlertConfirmConfig() throws Exception {
        String message;
        try {
            alertConfirmConfigService.updateAlertConfirmConfig(alertConfirmConfig);
            message = "success";
        } catch (Exception e) {
            logger.error("saveAlertConfirmConfig error:{}", e);
            message = "error";
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 保存用户选择的告警类型
     *
     * @param
     * @return String
     */
    public String saveAlertTypeOfUsers() throws Exception {
        String message;
        try {
            List<EventType> confirmEventTypes = alertConfirmConfigService.getConfirmEventByEventTypeId(alertTypeId);
            List<EventType> originEventTypes = alertConfirmConfigService.getOriginEventByEventTypeId(alertTypeId);
            List<Integer> confirmEventIntegers = new ArrayList<Integer>();
            List<Integer> originEventIntegers = new ArrayList<Integer>();
            for (EventType type : confirmEventTypes) {
                confirmEventIntegers.add(type.getTypeId());
            }
            for (EventType type : originEventTypes) {
                originEventIntegers.add(type.getTypeId());
            }
            // 配置清除事件
            if (!"".equals(confirmAlertTypeString)) {
                String typeIdList[] = confirmAlertTypeString.split(",");
                List<Integer> types1 = new ArrayList<Integer>();
                List<Integer> types2 = new ArrayList<Integer>();
                for (int i = 0; i < typeIdList.length; i++) {
                    types1.add(Integer.parseInt(typeIdList[i]));
                    types2.add(Integer.parseInt(typeIdList[i]));
                }
                types1.removeAll(confirmEventIntegers);
                for (Integer typeId : types1) {
                    alertConfirmConfigService.insertConfirmAlert(alertTypeId, typeId);
                }
                confirmEventIntegers.removeAll(types2);
                for (Integer typeId : confirmEventIntegers) {
                    alertConfirmConfigService.deleteConfirmAlert(alertTypeId, typeId);
                }
            } else {
                for (Integer typeId : confirmEventIntegers) {
                    alertConfirmConfigService.deleteConfirmAlert(alertTypeId, typeId);
                }
            }
            // 配置来源事件
            if (!"".equals(originEventTypeString)) {
                String typeIdList[] = originEventTypeString.split(",");
                List<Integer> types1 = new ArrayList<Integer>();
                List<Integer> types2 = new ArrayList<Integer>();
                for (int i = 0; i < typeIdList.length; i++) {
                    types1.add(Integer.parseInt(typeIdList[i]));
                    types2.add(Integer.parseInt(typeIdList[i]));
                }
                types1.removeAll(originEventIntegers);
                for (Integer typeId : types1) {
                    alertConfirmConfigService.insertOriginAlert(typeId, alertTypeId);
                }
                originEventIntegers.removeAll(types2);
                for (Integer typeId : originEventIntegers) {
                    alertConfirmConfigService.deleteOriginAlert(typeId, alertTypeId);
                }
            } else {
                for (Integer typeId : originEventIntegers) {
                    alertConfirmConfigService.deleteOriginAlert(typeId, alertTypeId);
                }
            }
            List<Integer> userActionList = new ArrayList<Integer>();
            if (userAlertActionChooseEmail == true && userAlertActionChooseMobile == true) {
                userActionList.add(1);
                userActionList.add(2);
            } else if (userAlertActionChooseEmail == true && userAlertActionChooseMobile == false) {
                userActionList.add(1);
            } else if (userAlertActionChooseEmail == false && userAlertActionChooseMobile == true) {
                userActionList.add(2);
            }
            alertService.updateAlertTypeOfUsers(alertTypeId, level, active, updateLevel, alarmTimes, smartUpdate,
                    userId, userActionList, actionIds, note);
            eventService.reset();
            message = "success";
        } catch (Exception e) {
            message = e.getMessage();
            logger.debug("saveAlertType error:{}", e);
            message = "error";
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 返回用户的告警选择
     *
     * @param
     * @return String
     */
    public String getAlertTypeOfUsersById() throws Exception {
        ResourceManager resourceManager = ResourceManager.getResourceManager("com.topvision.ems.fault.resources");
        JSONObject json = new JSONObject();
        alertType = alertService.getAlertTypeById(alertTypeId);
        actionIds = alertService.getActionsOfAlertType(alertTypeId);
        List<Integer> actionChoose = alertService.getUserAlertActions(alertTypeId, userId);
        json.put("typeId", alertType.getTypeId());
        json.put("category", alertType.getCategory());
        json.put("name", resourceManager.getString(alertType.getDisplayName()));
        json.put("note", alertType.getText());
        if (alertType.getNote() == null) {
            alertType.setNote("");
        }
        json.put("alarmDesc", alertType.getNote());// 描述信息
        json.put("level", alertType.getLevelId());
        json.put("active", alertType.getActive());
        json.put("threshold", alertType.getThreshold());
        json.put("updateLevel", alertType.getUpdateLevel());
        json.put("alarmTimes", alertType.getAlertTimes());
        json.put("smartUpdate", alertType.getSmartUpdate());
        json.put("actionChoose", actionChoose);
        JSONArray array = new JSONArray();
        JSONObject temp = null;
        if (actionIds != null) {
            for (int i = 0; i < actionIds.size(); i++) {
                temp = new JSONObject();
                temp.put("actionId", actionIds.get(i));
                array.add(temp);
            }
        }
        json.put("actions", array);
        writeDataToAjax(json);
        return NONE;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<Long> getActionIds() {
        return actionIds;
    }

    public List<Action> getActions() {
        return actions;
    }

    public AlertType getAlertType() {
        return alertType;
    }

    public int getAlertTypeId() {
        return alertTypeId;
    }

    public byte getLevel() {
        return level;
    }

    public boolean isActive() {
        return active;
    }

    public void setActionIds(List<Long> actionIds) {
        this.actionIds = actionIds;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setAlertService(AlertService alertService) {
        this.alertService = alertService;
    }

    public void setAlertType(AlertType alertType) {
        this.alertType = alertType;
    }

    public void setAlertTypeId(int alertTypeId) {
        this.alertTypeId = alertTypeId;
    }

    public void setFaultService(FaultService faultService) {
        this.faultService = faultService;
    }

    public void setLevel(byte level) {
        this.level = level;
    }

    public String showAlertConfig() {
        actions = faultService.getActions();
        uc = (UserContext) super.getSession().get(UserContext.KEY);
        user = userService.getUserEx(uc.getUserId());
        return SUCCESS;
    }

    public Integer getActiveNum() {
        return activeNum;
    }

    public void setActiveNum(Integer activeNum) {
        this.activeNum = activeNum;
    }

    public EventService getEventService() {
        return eventService;
    }

    public void setEventService(EventService eventService) {
        this.eventService = eventService;
    }

    public AlertConfirmConfigService getAlertConfirmConfigService() {
        return alertConfirmConfigService;
    }

    public void setAlertConfirmConfigService(AlertConfirmConfigService alertConfirmConfigService) {
        this.alertConfirmConfigService = alertConfirmConfigService;
    }

    public FaultService getFaultService() {
        return faultService;
    }

    public AlertService getAlertService() {
        return alertService;
    }

    public String getAlarmTimes() {
        return alarmTimes;
    }

    public void setAlarmTimes(String alarmTimes) {
        this.alarmTimes = alarmTimes;
    }

    public boolean isSmartUpdate() {
        return smartUpdate;
    }

    public void setSmartUpdate(boolean smartUpdate) {
        this.smartUpdate = smartUpdate;
    }

    public int getUpdateLevel() {
        return updateLevel;
    }

    public void setUpdateLevel(int updateLevel) {
        this.updateLevel = updateLevel;
    }

    public String getConfirmAlertTypeString() {
        return confirmAlertTypeString;
    }

    public void setConfirmAlertTypeString(String confirmAlertTypeString) {
        this.confirmAlertTypeString = confirmAlertTypeString;
    }

    public String getOriginEventTypeString() {
        return originEventTypeString;
    }

    public void setOriginEventTypeString(String originEventTypeString) {
        this.originEventTypeString = originEventTypeString;
    }

    /**
     * @return the alertConfirmConfig
     */
    public Boolean getAlertConfirmConfig() {
        return alertConfirmConfig;
    }

    /**
     * @param alertConfirmConfig the alertConfirmConfig to set
     */
    public void setAlertConfirmConfig(Boolean alertConfirmConfig) {
        this.alertConfirmConfig = alertConfirmConfig;
    }
 

    /**
     * @return the loadUserAlertType
     */
    public Boolean getLoadUserAlertType() {
        return loadUserAlertType;
    }

    /**
     * @param loadUserAlertType the loadUserAlertType to set
     */
    public void setLoadUserAlertType(Boolean loadUserAlertType) {
        this.loadUserAlertType = loadUserAlertType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public UserContext getUc() {
        return uc;
    }

    public void setUc(UserContext uc) {
        this.uc = uc;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getUserAlertActionChooseEmail() {
        return userAlertActionChooseEmail;
    }

    public void setUserAlertActionChooseEmail(Boolean userAlertActionChooseEmail) {
        this.userAlertActionChooseEmail = userAlertActionChooseEmail;
    }

    public Boolean getUserAlertActionChooseMobile() {
        return userAlertActionChooseMobile;
    }

    public void setUserAlertActionChooseMobile(Boolean userAlertActionChooseMobile) {
        this.userAlertActionChooseMobile = userAlertActionChooseMobile;
    }

}
