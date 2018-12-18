/***********************************************************************
 * $Id: PerfTargetManageAction.java,v1.0 2014-3-14 上午11:52:42 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.network.domain.TopoFolder;
import com.topvision.ems.network.service.TopologyService;
import com.topvision.ems.performance.domain.DevicePerfTarget;
import com.topvision.ems.performance.domain.PerfGlobal;
import com.topvision.ems.performance.service.DevicePerfTargetService;
import com.topvision.ems.performance.service.PerfThresholdService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.ResourceManager;

/**
 * @author flack
 * @created @2014-3-14-上午11:52:42
 *
 */
@Controller("perfTargetManageAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PerfTargetManageAction extends BaseAction {
    private static final long serialVersionUID = 8579899877779060732L;

    protected Long entityId;
    protected Long entityType;
    protected Long typeId;
    protected String perfTargetName;
    protected String deviceName;
    protected String deviceIp;
    protected Integer folderId;
    protected Integer collectInterval;
    protected Integer targetEnable;
    protected String[] targetData;
    //标识应用到所有设备时是否保存到全局
    protected Boolean saveGlobalFlag;
    // 是否绑定默认模板
    protected Integer isBindDftTemp;
    // 模板是否开启
    protected Integer isPerfThdOn;
    //为了整合cmts加的父类型
    protected Long parentType;
    //用于批量修改单个指标时接收从前台传回来的所有设备entityId 
    protected String entityIds;

    protected JSONObject oltTargetJson;
    protected JSONObject ccmtsTargetJson;
    protected JSONObject cmtsTargetJson;
    protected JSONObject onuTargetJson;
    @Autowired
    protected DevicePerfTargetService devicePerfTargetService;
    @Autowired
    private TopologyService topologyService;
    @Autowired
    protected PerfThresholdService perfThresholdService;
    @Autowired
    protected EntityTypeService entityTypeService;

    /**
     * 查询具体类型设备的全局性能指标
     * @param entityType
     * @return
     */
    protected JSONObject getGlobalTargetByType(Long entityType) {
        List<DevicePerfTarget> globalTargetList = devicePerfTargetService.getGlobalPerfTargetList(entityType);
        Map<String, JSONObject> targetMap = new LinkedHashMap<String, JSONObject>();
        for (DevicePerfTarget perfTarget : globalTargetList) {
            String groupName = perfTarget.getTargetGroup();
            JSONObject group = targetMap.get(groupName);
            if (group == null) {
                group = new JSONObject();
                targetMap.put(groupName, group);
            }
            group.put(perfTarget.getPerfTargetName(), perfTarget);
        }
        return JSONObject.fromObject(targetMap);
    }

    /**
     * 获取指定设备的性能指标及对应的全局性能指标
     * @param entityId
     * @return
     */
    protected JSONObject getDeviceTargetById(Long entityId) {
        List<DevicePerfTarget> deviceTargetList = devicePerfTargetService.getDeviceGlobalPerfList(entityId);
        Map<String, JSONObject> targetMap = new LinkedHashMap<String, JSONObject>();
        for (DevicePerfTarget perfTarget : deviceTargetList) {
            String groupName = perfTarget.getTargetGroup();
            JSONObject group = targetMap.get(groupName);
            if (group == null) {
                group = new JSONObject();
                targetMap.put(groupName, group);
            }
            group.put(perfTarget.getPerfTargetName(), perfTarget);
        }
        return JSONObject.fromObject(targetMap);
    }

    /**
     * 将查询到的所有性能指标与具体设备相对应
     * @param targetList
     * @return
     */
    protected JSONArray encodeAllTargetToDevice(List<DevicePerfTarget> targetList) {
        Map<Long, JSONObject> map = new LinkedHashMap<Long, JSONObject>();
        for (DevicePerfTarget perfTarget : targetList) {
            Long entityId = perfTarget.getEntityId();
            JSONObject deviceTarget = map.get(entityId);
            if (deviceTarget == null) {
                deviceTarget = new JSONObject();
                map.put(entityId, deviceTarget);
            }
            if (deviceTarget.get("entityId") == null || "".equals(deviceTarget.get("entityId"))) {
                deviceTarget.put("entityId", perfTarget.getEntityId());
                deviceTarget.put("entityType", perfTarget.getEntityType());
                deviceTarget.put("parentType", perfTarget.getParentType());
                deviceTarget.put("deviceName", perfTarget.getDeviceName());
                deviceTarget.put("manageIp", perfTarget.getManageIp());
                deviceTarget.put("typeId", perfTarget.getTypeId());
                deviceTarget.put("location", getDeviceTopoFolderName(entityId));
                deviceTarget.put("displayName", perfTarget.getDisplayName());
                deviceTarget.put("uplinkDevice", perfTarget.getUplinkDevice());
                deviceTarget.put("parentId", perfTarget.getParentId());
                deviceTarget.put("parentName", perfTarget.getParentName());
                deviceTarget.put("parentTypeId", perfTarget.getParentTypeId());

            }
            deviceTarget.put(perfTarget.getPerfTargetName(), perfTarget);
        }
        JSONArray targetArray = new JSONArray();
        for (Entry<Long, JSONObject> target : map.entrySet()) {
            targetArray.add(target.getValue());
        }
        return targetArray;
    }

    /**
     * 将前台传回来的数据封装成性能指标对象数据
     * @param targetArray
     * @param entityId
     * @param entityType
     * @param typeId
     * @return
     */
    protected List<DevicePerfTarget> decodeDataToTargetList(String[] targetArray, Long entityId, Long entityType,
            Long parentType, Long typeId) {
        List<DevicePerfTarget> targetList = new ArrayList<DevicePerfTarget>();
        DevicePerfTarget perfTarget = null;
        for (String data : targetArray) {
            String[] targetData = data.split("#");
            perfTarget = new DevicePerfTarget();
            perfTarget.setEntityId(entityId);
            perfTarget.setEntityType(entityType);
            perfTarget.setParentType(parentType);
            perfTarget.setPerfTargetName(targetData[0]);
            perfTarget.setCollectInterval(Integer.parseInt(targetData[1]));
            perfTarget.setTargetEnable(Integer.parseInt(targetData[2]));
            perfTarget.setTargetGroup(targetData[3]);
            targetList.add(perfTarget);
        }
        return targetList;
    }

    /**
     * 加载所有的地域
     * @return
     * @throws IOException 
     */
    public String loadAllArea() throws IOException {
        List<TopoFolder> folderList = topologyService.loadTopoFolder();
        for (TopoFolder folder : folderList) {
            folder.setName(getResourceManager().getString(folder.getName()));
        }
        JSONArray folderJson = JSONArray.fromObject(folderList);
        writeDataToAjax(folderJson);
        return NONE;
    }

    /**
     * 加载是否关联默认阀值模板和是否开启性能告警
     * @param type
     */
    protected void loadBindTempAndPerfAlert(Long type) {
        PerfGlobal perfGlobal = perfThresholdService.loadPerfGolbalByType(type);
        isBindDftTemp = perfGlobal.getIsRelationWithDefaultTemplate();
        isPerfThdOn = perfGlobal.getIsPerfThreshold();
    }

    /**
     * 修改是否关联默认阀值模板和是否开启性能告警
     * @param type
     * @param isBindDftTemp
     * @param isPerfThdOn
     */
    protected void udpateBindTempAndPerfAlert(Long type, Integer isBindDftTemp, Integer isPerfThdOn) {
        PerfGlobal perfGlobal = new PerfGlobal(type, 1, isBindDftTemp, isPerfThdOn);
        perfThresholdService.modifyPerfGlobal(perfGlobal);
    }

    private ResourceManager getResourceManager() {
        return ResourceManager.getResourceManager("com.topvision.ems.resources.resources");
    }

    private String getDeviceTopoFolderName(Long entityId) {
        List<TopoFolder> folderList = topologyService.getTopoFolderByEntityId(entityId);
        if (folderList == null || folderList.isEmpty()) {
            //设备不在任何地域下
            return getResourceManager().getString("WorkBench.topology0");
        } else {
            StringBuilder nameBulider = new StringBuilder();
            for (TopoFolder folder : folderList) {
                nameBulider.append(",").append(getResourceManager().getString(folder.getName()));
            }
            return nameBulider.substring(1);
        }
    }

    /**
     * 显示单个指标的批量配置页面
     * @return
     */
    public String showBatchConfigTarget() {
        //加载三个不同设备类型的数据
        oltTargetJson = getGlobalTargetByType(entityTypeService.getOltType());
        ccmtsTargetJson = getGlobalTargetByType(entityTypeService.getCcmtsType());
        onuTargetJson = getGlobalTargetByType(entityTypeService.getOnuType());
        return SUCCESS;
    }

    /**
     * 显示所有支持当前性能指标的设备页面
     * @return
     */
    public String showSupportTargetDeviceList() {
        return SUCCESS;
    }

    /**
     * 加载所有支持当前性能指标的设备
     * @return
     * @throws IOException 
     */
    public String loadSupportTargetDevice() throws IOException {
        //根据类型和指标名称查询支持该指标的所有设备
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("entityType", entityType);
        paramsMap.put("parentType", parentType);
        paramsMap.put("perfTargetName", perfTargetName);
        if (deviceName != null && !"".equals(deviceName)) {
            paramsMap.put("deviceName", deviceName);
        }
        if (deviceIp != null && !"".equals(deviceIp)) {
            paramsMap.put("manageIp", deviceIp);
        }
        if (folderId != null && folderId != -1) {
            paramsMap.put("folderId", folderId);
        }
        if (typeId != null && typeId != -1) {
            paramsMap.put("typeId", typeId);
        }
        paramsMap.put("start", start);
        paramsMap.put("limit", limit);
        List<DevicePerfTarget> deviceTargetList = devicePerfTargetService.getPerfTargetDeviceList(paramsMap);
        JSONArray deviceArray = encodeAllTargetToDevice(deviceTargetList);
        int totalCount = devicePerfTargetService.getPerfTargetDeviceNum(paramsMap);
        JSONObject deviceJson = new JSONObject();
        deviceJson.put("totalCount", totalCount);
        deviceJson.put("data", deviceArray);
        writeDataToAjax(deviceJson);
        return NONE;
    }

    /**
     * 批量修改所有的设备的指定性能指标
     * @return
     * @throws IOException
     */
    public String batchModifyDeviceSingleTarget() throws IOException {
        //TODO 设备列表  指标名称   采集周期  开启状态
        DevicePerfTarget newTarget = new DevicePerfTarget();
        newTarget.setPerfTargetName(perfTargetName);
        newTarget.setCollectInterval(collectInterval);
        newTarget.setTargetEnable(targetEnable);
        Map<String, Object> resultMap = devicePerfTargetService.modifyDeviceSingleTarget(newTarget, entityIds);
        JSONObject result = JSONObject.fromObject(resultMap);
        writeDataToAjax(result);
        return NONE;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getEntityType() {
        return entityType;
    }

    public void setEntityType(Long entityType) {
        this.entityType = entityType;
    }

    public String getPerfTargetName() {
        return perfTargetName;
    }

    public void setPerfTargetName(String perfTargetName) {
        this.perfTargetName = perfTargetName;
    }

    public JSONObject getOltTargetJson() {
        return oltTargetJson;
    }

    public void setOltTargetJson(JSONObject oltTargetJson) {
        this.oltTargetJson = oltTargetJson;
    }

    public JSONObject getCcmtsTargetJson() {
        return ccmtsTargetJson;
    }

    public void setCcmtsTargetJson(JSONObject ccmtsTargetJson) {
        this.ccmtsTargetJson = ccmtsTargetJson;
    }

    public JSONObject getCmtsTargetJson() {
        return cmtsTargetJson;
    }

    public void setCmtsTargetJson(JSONObject cmtsTargetJson) {
        this.cmtsTargetJson = cmtsTargetJson;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getDeviceIp() {
        return deviceIp;
    }

    public void setDeviceIp(String deviceIp) {
        this.deviceIp = deviceIp;
    }

    public Integer getFolderId() {
        return folderId;
    }

    public void setFolderId(Integer folderId) {
        this.folderId = folderId;
    }

    public Integer getCollectInterval() {
        return collectInterval;
    }

    public void setCollectInterval(Integer collectInterval) {
        this.collectInterval = collectInterval;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public Integer getTargetEnable() {
        return targetEnable;
    }

    public void setTargetEnable(Integer targetEnable) {
        this.targetEnable = targetEnable;
    }

    public String[] getTargetData() {
        return targetData;
    }

    public void setTargetData(String[] targetData) {
        this.targetData = targetData;
    }

    public Boolean getSaveGlobalFlag() {
        return saveGlobalFlag;
    }

    public void setSaveGlobalFlag(Boolean saveGlobalFlag) {
        this.saveGlobalFlag = saveGlobalFlag;
    }

    public Integer getIsBindDftTemp() {
        return isBindDftTemp;
    }

    public void setIsBindDftTemp(Integer isBindDftTemp) {
        this.isBindDftTemp = isBindDftTemp;
    }

    public Integer getIsPerfThdOn() {
        return isPerfThdOn;
    }

    public void setIsPerfThdOn(Integer isPerfThdOn) {
        this.isPerfThdOn = isPerfThdOn;
    }

    public Long getParentType() {
        return parentType;
    }

    public void setParentType(Long parentType) {
        this.parentType = parentType;
    }

    public String getEntityIds() {
        return entityIds;
    }

    public void setEntityIds(String entityIds) {
        this.entityIds = entityIds;
    }

    public JSONObject getOnuTargetJson() {
        return onuTargetJson;
    }

    public void setOnuTargetJson(JSONObject onuTargetJson) {
        this.onuTargetJson = onuTargetJson;
    }

}
