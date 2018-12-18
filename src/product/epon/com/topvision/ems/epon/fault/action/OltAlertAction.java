/***********************************************************************
 * $Id: OltAlertAction.java,v1.0 2013-10-26 上午09:48:20 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.fault.action;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.domain.OltAlertInstance;
import com.topvision.ems.epon.domain.OltAlertTop;
import com.topvision.ems.epon.exception.TrapConfigInconsistentValueException;
import com.topvision.ems.epon.fault.domain.OltTopAlarmCodeMask;
import com.topvision.ems.epon.fault.domain.OltTopAlarmInstanceMask;
import com.topvision.ems.epon.fault.domain.OltTrapConfig;
import com.topvision.ems.epon.fault.service.OltAlertService;
import com.topvision.ems.epon.olt.action.AbstractEponAction;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.fault.domain.AlertType;
import com.topvision.ems.fault.domain.Level;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.annotation.OperationLogProperty;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.domain.TreeNode;
import com.topvision.framework.web.util.HttpResponseUtils;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.domain.OperationLog;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.service.SystemPreferencesService;

/**
 * @author lizongtian
 * @created @2013-10-26-上午09:48:20
 * 
 */
@Controller("oltAlertAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OltAlertAction extends AbstractEponAction {
    private static final long serialVersionUID = -3580066864663195837L;
    private final Logger logger = LoggerFactory.getLogger(OltAlertAction.class);
    @Autowired
    private EntityService entityService;
    @Autowired
    private OltAlertService oltAlertService;
    @Autowired
    private SystemPreferencesService systemPreferencesService;
    private Entity entity;
    private String ip;
    private Long codeMaskIndex;
    private Integer codeMaskEnable;
    private Long instanceMaskIndex;
    private Integer instanceMaskEnable;
    private String trapServerIp;
    private String trapNameString;
    private Integer trapPort;
    private String trapCommunity;
    private Integer type = 1;
    private String cameraSwitch;
    private Long alertId;

    /**
     * 显示OLT告警屏蔽
     * 
     * @return
     */
    public String showOltAlertMask() {
        logger.debug("showOltAlertMask");
        return SUCCESS;
    }

    /**
     * 获取OLT告警类型屏蔽列表
     * 
     * @return
     * @throws Exception
     */
    public String loadOltAlertCodeMask() throws Exception {
        List<OltTopAlarmCodeMask> codeMaskList = oltAlertService.getOltAlertCodeMask(entityId);
        for (OltTopAlarmCodeMask codeMask : codeMaskList) {
            codeMask.setAlarmName(getString(codeMask.getAlarmName(), "fault"));
        }
        logger.debug("loadOltAlertCodeMask:{}", codeMaskList);
        writeDataToAjax(JSONArray.fromObject(codeMaskList));
        return NONE;
    }

    /**
     * 获取可用的告警类型列表
     * 
     * @return
     * @throws Exception
     */
    public String loadOltAlertAvailableType() throws Exception {
        List<AlertType> alertTypeList = oltAlertService.getOltAlertAvailableType(entityId);
        for (AlertType alertType : alertTypeList) {
            alertType.setDisplayName(getString(alertType.getDisplayName(), "fault"));
        }
        logger.debug("loadOltAlertAvailableType:{}", alertTypeList);
        writeDataToAjax(JSONArray.fromObject(alertTypeList));
        return NONE;
    }

    /**
     * 添加OLT告警类型屏蔽规则
     * 
     * @return
     * @throws Exception
     */
    @OperationLogProperty(actionName = "oltAlertAction", operationName = "addOltAlertCodeMask")
    public String addOltAlertCodeMask() throws Exception {
        OltTopAlarmCodeMask codeMask = new OltTopAlarmCodeMask();
        codeMask.setEntityId(entityId);
        codeMask.setTopAlarmCodeMaskIndex(codeMaskIndex);
        codeMask.setTopAlarmCodeMaskEnable(codeMaskEnable);
        try {
            oltAlertService.addOltAlertCodeMask(codeMask);
            operationResult = OperationLog.SUCCESS;
        } catch (Exception e) {
            logger.debug("addOltAlertCodeMask error: {}", e);
            operationResult = OperationLog.FAILURE;
            // 添加失败，返回错误信息给前台。
            writeDataToAjax("failure");
        }
        return NONE;
    }

    /**
     * 删除OLT告警类型屏蔽规则
     * 
     * @return
     * @throws Exception
     */
    @OperationLogProperty(actionName = "oltAlertAction", operationName = "deleteOltAlertCodeMask")
    public String deleteOltAlertCodeMask() throws Exception {
        String msg = "success";
        try {
            oltAlertService.deleteOltAlertCodeMask(entityId, codeMaskIndex);
            operationResult = OperationLog.SUCCESS;
        } catch (Exception e) {
            msg = "fail";
            logger.error("deleteOltAlertCodeMask error: {}", e);
            operationResult = OperationLog.FAILURE;
        }
        writeDataToAjax(msg);
        return NONE;
    }

    /**
     * 获取OLT告警实体屏蔽列表
     * 
     * @return
     * @throws Exception
     */
    public String loadOltAlertInstanceMask() throws Exception {
        List<OltTopAlarmInstanceMask> instanceMaskList = oltAlertService.getOltAlertInstanceMask(entityId);
        logger.debug("loadOltAlertInstanceMask:{}", instanceMaskList);
        writeDataToAjax(JSONArray.fromObject(instanceMaskList));
        return NONE;
    }

    /**
     * 获取可以进行告警屏蔽的实体
     * 
     * @return
     * @throws Exception
     */
    public String loadOltAlertAvailableInstance() throws Exception {
        List<OltAlertInstance> instanceList = oltAlertService.getOltAlertAvailableInstance(entityId);
        logger.debug("loadOltAlertAvailableInstance:{}", instanceList);
        writeDataToAjax(JSONArray.fromObject(instanceList));
        return NONE;
    }

    /**
     * 添加OLT告警实体屏蔽规则
     * 
     * @return
     * @throws Exception
     */
    @OperationLogProperty(actionName = "oltAlertAction", operationName = "addOltAlertInstanceMask")
    public String addOltAlertInstanceMask() throws Exception {
        OltTopAlarmInstanceMask instanceMask = new OltTopAlarmInstanceMask();
        instanceMask.setEntityId(entityId);
        instanceMask.setTopAlarmInstanceMaskIndex5Byte(instanceMaskIndex);
        instanceMask.setTopAlarmInstanceMaskEnable(instanceMaskEnable);
        try {
            oltAlertService.addOltAlertInstanceMask(instanceMask);
            operationResult = OperationLog.SUCCESS;
        } catch (Exception e) {
            logger.debug("addOltAlertInstanceMask error: {}", e);
            operationResult = OperationLog.FAILURE;
            // 添加失败，返回错误信息给前台。
            writeDataToAjax("failure");
        }
        return NONE;
    }

    /**
     * 删除OLT告警实体屏蔽规则
     * 
     * @return
     * @throws Exception
     */
    @OperationLogProperty(actionName = "oltAlertAction", operationName = "deleteOltAlertInstanceMask")
    public String deleteOltAlertInstanceMask() throws Exception {
        String msg = "success";
        try {
            oltAlertService.deleteOltAlertInstanceMask(entityId, instanceMaskIndex);
            operationResult = OperationLog.SUCCESS;
        } catch (Exception e) {
            msg = "fail";
            logger.error("deleteOltAlertInstanceMask error: {}", e);
            operationResult = OperationLog.FAILURE;
        }
        writeDataToAjax(msg);
        return NONE;
    }

    /**
     * 从设备刷新告警屏蔽信息
     * 
     * @return
     * @throws Exception
     */
    public String refreshMaskDataFromFacility() throws Exception {
        String msg = "success";
        try {
            oltAlertService.refreshMaskDataFromFacility(entityId);
        } catch (Exception e) {
            msg = "fail";
            logger.error("refreshMaskDataFromFacility error: {}", e);
        }
        writeDataToAjax(msg);
        return NONE;
    }

    /**
     * 显示OLT Trap服务器管理页面
     * 
     * @return
     */
    public String showOltTrapServerManagement() {
        return SUCCESS;
    }

    /**
     * 获取OLT Trap服务器数据
     * 
     * @return
     * @throws Exception
     */
    public String loadOltTrapConfig() throws Exception {
        List<OltTrapConfig> oltTrapConfigList = oltAlertService.getOltTrapConfig(entityId);
        logger.debug("loadOltTrapServerData:{}", oltTrapConfigList);
        writeDataToAjax(JSONArray.fromObject(oltTrapConfigList));
        return NONE;
    }

    /**
     * 添加Trap服务器配置
     * 
     * @return
     * @throws Exception
     */
    @OperationLogProperty(actionName = "oltAlertAction", operationName = "addOltTrapConfig")
    public String addOltTrapConfig() throws Exception {
        Map<String, String> message = new HashMap<String, String>();
        OltTrapConfig oltTrapConfig = new OltTrapConfig();
        oltTrapConfig.setEntityId(entityId);
        oltTrapConfig.setEponManagementAddrName(trapNameString);
        oltTrapConfig.setAddrTAddress(trapServerIp);
        oltTrapConfig.setAddrTPort(trapPort);
        oltTrapConfig.setEponManagementAddrCommunity(trapCommunity);
        oltAlertService.refreshTrapConfigFromFacility(entityId);
        List<OltTrapConfig> oltTrapConfigList = oltAlertService.getOltTrapConfig(entityId);
        boolean tmpFlag = true;
        for (OltTrapConfig aTrap : oltTrapConfigList) {
            if (aTrap.getAddrTAddress().equals(trapServerIp) && aTrap.getAddrTPort().equals(trapPort)) {
                tmpFlag = false;
            }
        }
        if (tmpFlag) {
            try {
                oltAlertService.addOltTrapConfig(oltTrapConfig);
                operationResult = OperationLog.SUCCESS;
            } catch (TrapConfigInconsistentValueException tcive) {
                message.put("message", tcive.getMessage());
            } catch (Exception e) {
                message.put("message", e.getMessage());
                logger.info("addOltTrapConfig error: {}", e);
                operationResult = OperationLog.FAILURE;
            }
        } else {
            message.put("message", "repeat");
        }
        writeDataToAjax(JSONObject.fromObject(message));
        return NONE;
    }

    /**
     * 删除Trap服务器配置
     * 
     * @return
     * @throws Exception
     */
    @OperationLogProperty(actionName = "oltAlertAction", operationName = "deleteOltTrapConfig")
    public String deleteOltTrapConfig() throws Exception {
        String msg = "success";
        try {
            oltAlertService.deleteOltTrapConfig(entityId, trapNameString);
            operationResult = OperationLog.SUCCESS;
        } catch (Exception e) {
            msg = "fail";
            logger.error("deleteOltTrapConfig error: {}", e);
            operationResult = OperationLog.FAILURE;
        }
        writeDataToAjax(msg);
        return NONE;
    }

    /**
     * 从设备刷新Trap配置信息
     * 
     * @return
     * @throws Exception
     */
    public String refreshTrapConfigFromFacility() throws Exception {
        String msg = "success";
        try {
            oltAlertService.refreshTrapConfigFromFacility(entityId);

        } catch (Exception e) {
            msg = "fail";
            logger.error("update trap failed:{}", e);
        }
        writeDataToAjax(msg);
        return NONE;
    }

    /**
     * 获取查询条件中数据
     * 
     * @return
     * @throws Exception
     */
    public String loadAlertQueryInitData() throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        // 可用设备IP
        List<String> ipList = oltAlertService.getAvailableEntityIp();
        logger.debug("loadAlertQueryInitData ipList:{}", ipList);
        // 告警等级
        List<Level> severity = oltAlertService.getAlertSeverity();
        for (Level level : severity) {
            level.setName(getString(level.getName(), "fault"));
        }
        logger.debug("loadAlertQueryInitData severity:{}", severity);

        // 告警类型
        List<AlertType> alertType = oltAlertService.getAlertType();
        for (AlertType type : alertType) {
            type.setDisplayName(getString(type.getDisplayName(), "fault"));
            type.setName(getString(type.getName(), "fault"));
        }
        logger.debug("loadAlertQueryInitData alertType:{}", alertType);

        result.put("ip", ipList);
        result.put("severity", severity);
        result.put("type", alertType);
        writeDataToAjax(JSONArray.fromObject(result));
        return NONE;
    }

    /**
     * 获取所有的Olt告警类型,并组装成树结构,在Olt告警查看页面选择告警类型时使用 注意此方法中使用 org.json包里的Json对象更方便处理问题
     * 
     * @author flackyang
     * @since 2013-11-11
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public String getAllOltAlertType() throws Exception {
        List<AlertType> list = oltAlertService.getAlertType();

        List<TreeNode> treeNodeList = new ArrayList<TreeNode>();

        // 构建告警非叶子节点的map
        HashMap<String, TreeNode> map = new HashMap<String, TreeNode>(list == null ? 0 : list.size());
        AlertType type = null;
        TreeNode treeNode = null;
        for (int i = 0; list != null && i < list.size(); i++) {
            type = list.get(i);
            if (type.getCategory() != 0) {
                continue;
            }
            treeNode = new TreeNode();
            treeNode.setName(getString(type.getDisplayName(), "fault"));
            treeNode.setValue(String.valueOf(type.getTypeId()));
            treeNode.setChildren(new ArrayList<TreeNode>());
            treeNodeList.add(treeNode);
            map.put(String.valueOf(type.getTypeId()), treeNode);
        }

        TreeNode parent;
        for (int i = 0; list != null && i < list.size(); i++) {
            type = list.get(i);
            if (type.getCategory() == 0) {
                continue;
            }
            if (type.getTypeId().equals(200002) || type.getTypeId().equals(12326)) {
                // 移出spectrumAlertClearType和光信号恢复告警
                continue;
            }
            treeNode = new TreeNode();
            treeNode.setName(getString(type.getDisplayName(), "fault"));
            treeNode.setValue(String.valueOf(type.getTypeId()));
            treeNode.setChildren(new ArrayList<TreeNode>());
            map.put(String.valueOf(type.getTypeId()), treeNode);
            parent = map.get(String.valueOf(type.getParentId()));
            if (parent == null) {
                treeNodeList.add(treeNode);
            } else {
                parent.getChildren().add(treeNode);
            }
        }
        writeDataToAjax(treeNodeList);
        return NONE;
    }

    /**
     * 获取OLT设备告警top 10
     * 
     * @return
     * @throws Exception
     */
    public String getOltDeviceAlertTop() throws Exception {
        HttpResponseUtils.setXMLResponse(ServletActionContext.getResponse());
        Writer writer = ServletActionContext.getResponse().getWriter();

        ResourceManager resourceManager = ResourceManager.getResourceManager("com.topvision.ems.resources.resources");

        writer.write("<table width=100% class=portletTable><tr><th width=80px style=\"text-align:left\">"
                + resourceManager.getString("COMMON.entity") + resourceManager.getString("WorkBench.Ranking")
                + "</th><th>" + resourceManager.getString("Config.oltConfigFileImported.deviceName") + "</th><Th>"
                + resourceManager.getString("COMMON.ip")
                + "</th><th style=\"text-align:right\"><img src=\"../images/desc.gif\" border=0 align=absmiddle hspace=5>"
                + resourceManager.getString("WorkBench.AlertNum") + "</th></tr>");
        try {
            List<OltAlertTop> responses = oltAlertService.getOltDeviceAlertTop();
            int size = responses == null ? 0 : responses.size();
            OltAlertTop oltAlertTop = null;
            for (int i = 0; i < size; i++) {
                oltAlertTop = responses.get(i);
                writer.write("<tr><Td><div class=topCls");
                writer.write(String.valueOf(i + 1));
                writer.write("></div></Td>");
                writer.write("<Td><a class=my-link href=\"javascript:showEntitySnap(");
                writer.write(String.valueOf(oltAlertTop.getEntityId()));
                writer.write(",'");
                writer.write(oltAlertTop.getIp());
                writer.write("');\">");
                writer.write(oltAlertTop.getOltName());
                writer.write("</a></Td>");
                writer.write("<Td>");
                writer.write(oltAlertTop.getIp());
                writer.write("</Td>");
                writer.write("</a></Td>");
                writer.write("<Td align=right>");
                writer.write("" + oltAlertTop.getHappenTimes());
                writer.write("</Td>");
                writer.write("</tr>");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            writer.write("</table>");
            writer.flush();
        }
        return NONE;
    }

    @Override
    protected String getString(String key, String module) {
        module = String.format("com.topvision.ems.%s.resources", module);
        try {
            ResourceManager resourceManager = ResourceManager.getResourceManager(module);
            return resourceManager.getNotNullString(key);
        } catch (Exception e) {
            return key;
        }
    }

    /**
     * Trap设置
     * 
     * @return String
     * @throws Exception
     */
    @OperationLogProperty(actionName = "oltAlertAction", operationName = "setOltTrapConfig")
    public String setOltTrapConfig() throws Exception {
        Map<String, String> message = new HashMap<String, String>();
        try {
            oltAlertService.setOltTrapConfig(entityId);
        } catch (TrapConfigInconsistentValueException tcive) {
            message.put("message", getString(tcive.getMessage(), "epon"));
            if (logger.isDebugEnabled()) {
                logger.debug("", tcive);
            }
        }
        writeDataToAjax(JSONObject.fromObject(message));
        return NONE;
    }

    /**
     * 显示告警信息
     * 
     * @return String
     */
    public String showOltAlert() {
        Properties properties = systemPreferencesService.getModulePreferences("camera");
        cameraSwitch = properties.getProperty("camera.switch");
        if (entityId != null && entityId > 0) {
            entity = entityService.getEntity(entityId);
            ip = entity.getIp();
        } else {
            entity = entityService.getEntityByIp(ip);
            if (entity != null) {
                // add by fanzidong,需要在展示前格式化MAC地址
                UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
                String macRule = uc.getMacDisplayStyle();
                String formatedMac = MacUtils.convertMacToDisplayFormat(entity.getMac(), macRule);
                entity.setMac(formatedMac);
            }
            entityId = entity.getEntityId();
        }
        return SUCCESS;
    }

    /**
     * 显示历史告警
     * 
     * @return
     */
    public String showOltAlertHistory() {
        Properties properties = systemPreferencesService.getModulePreferences("camera");
        cameraSwitch = properties.getProperty("camera.switch");
        if (entityId != null && entityId > 0) {
            entity = entityService.getEntity(entityId);
            ip = entity.getIp();
        } else {
            entity = entityService.getEntityByIp(ip);
            // add by fanzidong,需要在展示前格式化MAC地址
            UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
            String macRule = uc.getMacDisplayStyle();
            String formatedMac = MacUtils.convertMacToDisplayFormat(entity.getMac(), macRule);
            entity.setMac(formatedMac);
            entityId = entity.getEntityId();
        }
        return SUCCESS;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Long getCodeMaskIndex() {
        return codeMaskIndex;
    }

    public void setCodeMaskIndex(Long codeMaskIndex) {
        this.codeMaskIndex = codeMaskIndex;
    }

    public Integer getCodeMaskEnable() {
        return codeMaskEnable;
    }

    public void setCodeMaskEnable(Integer codeMaskEnable) {
        this.codeMaskEnable = codeMaskEnable;
    }

    public Long getInstanceMaskIndex() {
        return instanceMaskIndex;
    }

    public void setInstanceMaskIndex(Long instanceMaskIndex) {
        this.instanceMaskIndex = instanceMaskIndex;
    }

    public Integer getInstanceMaskEnable() {
        return instanceMaskEnable;
    }

    public void setInstanceMaskEnable(Integer instanceMaskEnable) {
        this.instanceMaskEnable = instanceMaskEnable;
    }

    public String getTrapServerIp() {
        return trapServerIp;
    }

    public void setTrapServerIp(String trapServerIp) {
        this.trapServerIp = trapServerIp;
    }

    public String getTrapNameString() {
        return trapNameString;
    }

    public void setTrapNameString(String trapNameString) {
        this.trapNameString = trapNameString;
    }

    public Integer getTrapPort() {
        return trapPort;
    }

    public void setTrapPort(Integer trapPort) {
        this.trapPort = trapPort;
    }

    public String getTrapCommunity() {
        return trapCommunity;
    }

    public void setTrapCommunity(String trapCommunity) {
        this.trapCommunity = trapCommunity;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getCameraSwitch() {
        return cameraSwitch;
    }

    public void setCameraSwitch(String cameraSwitch) {
        this.cameraSwitch = cameraSwitch;
    }

    public Long getAlertId() {
        return alertId;
    }

    public void setAlertId(Long alertId) {
        this.alertId = alertId;
    }

}
