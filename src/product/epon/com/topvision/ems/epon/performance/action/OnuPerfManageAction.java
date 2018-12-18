/***********************************************************************
 * $Id: OnuPerfManageAction.java,v1.0 2015-4-22 下午5:22:49 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.performance.action.PerfTargetManageAction;
import com.topvision.ems.performance.domain.DevicePerfTarget;
import com.topvision.ems.performance.domain.PerfThresholdEntity;
import com.topvision.ems.template.service.EntityTypeService;

/**
 * @author flack
 * @created @2015-4-22-下午5:22:49
 *
 */
@Controller("onuPerfManageAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OnuPerfManageAction extends PerfTargetManageAction {
    private static final long serialVersionUID = 1092190691326103728L;
    @Resource(name = "entityTypeService")
    private EntityTypeService entityTypeService;

    private String templateName;
    private Integer tempRela;
    private List<PerfThresholdEntity> perfThresholdList;

    /**
     * 显示Onu性能指标管理页面
     * @return
     */
    public String showOnuPerfManage() {
        return SUCCESS;
    }

    /**
     * 加载Onu性能指标数据
     * @return
     * @throws IOException 
     */
    public String loadOnuPerfTargetList() throws IOException {
        Long onuType = entityTypeService.getOnuType();
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("entityType", onuType);
        paramsMap.put("parentType", onuType);
        paramsMap.put("start", start);
        paramsMap.put("limit", limit);
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
        List<DevicePerfTarget> deviceTargetList = devicePerfTargetService.getDevicePerfList(paramsMap);
        /*if (deviceTargetList != null) {
            for (DevicePerfTarget target : deviceTargetList) {
                Long type = target.getEntityType();
                if (type > 255) {
                    target.setDisplayName(OnuTypeConvertor.convertTypeName(type.intValue()));
                }
            }
        }*/
        JSONArray targetArray = encodeAllTargetToDevice(deviceTargetList);
        int totalCount = devicePerfTargetService.getDeviceNum(paramsMap);
        JSONObject deviceJson = new JSONObject();
        deviceJson.put("totalCount", totalCount);
        deviceJson.put("data", targetArray);
        deviceJson.write(response.getWriter());
        return NONE;
    }

    /**
     * 加载指定Onu的性能指标数据
     * @return
     * @throws IOException 
     */
    public String loadOnuPerfTarget() throws IOException {
        //获取指定设备的性能指标及对应的全局性能指标
        JSONObject targetData = getDeviceTargetById(entityId);
        writeDataToAjax(targetData);
        return NONE;
    }

    /**
     * 修改指定Onu性能指标数据
     * @return
     */
    public String modifyOnuPerfTarget() {
        //将前台传回来的数据封装为对象列表
        List<DevicePerfTarget> targetList = decodeDataToTargetList(targetData, entityId, entityType,
                entityTypeService.getOnuType(), typeId);
        devicePerfTargetService.modifyDevicePerfTarget(targetList, entityId);
        return NONE;
    }

    /**
     * 将Onu性能指标配置保存为全局
     * @return
     */
    public String saveAsOnuGlobalTarget() {
        //将前台传回来的数据封装为对象列表
        List<DevicePerfTarget> currentTargetList = decodeDataToTargetList(targetData, null, entityType,
                entityTypeService.getOnuType(), null);
        //使用当前的性能指标数据去更新全局性能指标数据
        devicePerfTargetService.modifyGlobalPerfTarget(currentTargetList);
        return NONE;
    }

    /**
     * 将性能指标数据应用到同类型设备
     * @return
     * @throws IOException 
     */
    public String applyCurrentTargetToAllOnu() throws IOException {
        //将前台传回来的数据封装为对象列表
        List<DevicePerfTarget> targetList = decodeDataToTargetList(targetData, entityId, entityType,
                entityTypeService.getOnuType(), null);
        //将当前指标应用到所有的OLt设备,Olt不用区分具体的设备类型
        Map<String, Object> resultMap = devicePerfTargetService.applyTargetToAllDevice(targetList, entityType, null,
                saveGlobalFlag);
        JSONObject result = JSONObject.fromObject(resultMap);
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * 显示Onu性能指标全局配置
     * @return
     */
    public String showOnuGlobalPerfTarget() {
        //查询olt全局性能指标
        onuTargetJson = this.getGlobalTargetByType(entityTypeService.getOnuType());
        //加载是否关联默认阀值模板和是否开启性能告警
        loadBindTempAndPerfAlert(entityTypeService.getOnuType());
        return SUCCESS;
    }

    /**
     * 修改Onu性能指标全局配置
     * @return
     * @throws IOException 
     */
    public String modifyOnuGlobalTarget() throws IOException {
        Long onuType = entityTypeService.getOnuType();
        //将前台传回来的数据封装为对象列表
        List<DevicePerfTarget> globalTargetList = this.decodeDataToTargetList(targetData, null, onuType, onuType, null);
        //修改是否绑定默认阀值模板和是否开启性能告警
        udpateBindTempAndPerfAlert(onuType, isBindDftTemp, isPerfThdOn);
        //使用当前的性能指标数据去更新全局性能指标数据
        devicePerfTargetService.modifyGlobalPerfTarget(globalTargetList);
        //获取指定设备的全局性能指标
        JSONObject globalData = this.getGlobalTargetByType(onuType);
        writeDataToAjax(globalData);
        return NONE;
    }

    /**
     * 显示ONU阈值模板管理页面
     * @return
     */
    public String showOnuPerfTemplate() {
        return SUCCESS;
    }

    /**
     * 加载Onu关联阈值模板列表
     * @return
     * @throws IOException 
     */
    public String loadOnuPerfTemplateList() throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityName", deviceName);
        map.put("entityIp", deviceIp);
        map.put("templateName", templateName);
        map.put("entityType", entityTypeService.getOnuType());
        map.put("tempRela", tempRela);
        map.put("start", start);
        map.put("limit", limit);
        perfThresholdList = perfThresholdService.selectEntityPerfThresholdTmeplate(map);
        Long perfThresholdCount = perfThresholdService.selectPerfTmeplateCount(map);
        JSONObject json = new JSONObject();
        json.put("data", perfThresholdList);
        json.put("rowCount", perfThresholdCount);
        json.write(response.getWriter());
        return NONE;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public Integer getTempRela() {
        return tempRela;
    }

    public void setTempRela(Integer tempRela) {
        this.tempRela = tempRela;
    }

    public List<PerfThresholdEntity> getPerfThresholdList() {
        return perfThresholdList;
    }

    public void setPerfThresholdList(List<PerfThresholdEntity> perfThresholdList) {
        this.perfThresholdList = perfThresholdList;
    }

}
