/***********************************************************************
 * $Id: OltPortInfoAction.java,v1.0 2016-4-12 上午11:23:05 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.portinfo.action;

import java.util.List;
import java.util.Properties;

import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.DefaultValueProcessor;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.portinfo.domain.OltPortOpticalInfo;
import com.topvision.ems.epon.portinfo.service.OltPortInfoService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.service.SystemPreferencesService;

/**
 * @author flack
 * @created @2016-4-12-上午11:23:05
 *
 */
@Controller("oltPortInfoAction")
public class OltPortInfoAction extends BaseAction {
    private static final long serialVersionUID = 3650404671150753313L;

    private Entity entity;
    private Long entityId;
    private Long portId;
    private Long portIndex;
    private Integer perfStats;
    private String jConnectedId;

    @Autowired
    private EntityService entityService;
    @Autowired
    private OltPortInfoService oltPortInfoService;
    @Autowired
    private SystemPreferencesService systemPreferencesService;
    private String cameraSwitch;

    /**
     * 展示OLT端口信息页面
     * @return
     */
    public String showOltPortInfo() {
        Properties properties = systemPreferencesService.getModulePreferences("camera");
        cameraSwitch = properties.getProperty("camera.switch");
        entity = entityService.getEntity(entityId);
        return SUCCESS;
    }

    /**
     * 加载SNI口光信息列表
     * @return
     * @throws Exception
     */
    public String loadSniOpticalInfoList() throws Exception {
        List<OltPortOpticalInfo> sniInfoList = oltPortInfoService.getSniOpticalInfoList(entityId);
        JSONArray sniArray = JSONArray.fromObject(sniInfoList, getJsonConfig());
        writeDataToAjax(sniArray);
        return NONE;
    }

    /**
     * 加载PON口光信息列表
     * @return
     * @throws Exception
     */
    public String loadPonOpticalInfoList() throws Exception {
        List<OltPortOpticalInfo> ponInfoList = oltPortInfoService.getPonOpticalInfoList(entityId);
        JSONArray ponArray = JSONArray.fromObject(ponInfoList, getJsonConfig());
        writeDataToAjax(ponArray);
        return NONE;
    }

    /**
     * 刷新SNI口光功率信息
     * @return
     * @throws Exception 
     */
    public String refreshSniOpticalInfo() throws Exception {
        OltPortOpticalInfo sniPortOptical = oltPortInfoService.refreshSniOpticalInfo(entityId, portIndex, portId,
                perfStats);
        JSONObject sniJson = JSONObject.fromObject(sniPortOptical, getJsonConfig());
        writeDataToAjax(sniJson);
        return NONE;
    }

    /**
     * 刷新所有SNI口光功率信息
     * @return
     */
    public String refreshAllSniOptical() {
        String seesionId = ServletActionContext.getRequest().getSession().getId();
        oltPortInfoService.refreshAllSniOptical(entityId, jConnectedId, seesionId);
        return NONE;
    }

    /**
     * 刷新PON口光功率信息
     * @return
     * @throws Exception 
     */
    public String refreshPonOpticalInfo() throws Exception {
        OltPortOpticalInfo ponPortOptical = oltPortInfoService.refreshPonOpticalInfo(entityId, portIndex, portId,
                perfStats);
        JSONObject ponJson = JSONObject.fromObject(ponPortOptical, getJsonConfig());
        writeDataToAjax(ponJson);
        return NONE;
    }

    /**
     * 刷新所有PON口光功率信息
     * @return
     */
    public String refreshAllPonOptical() {
        String seesionId = ServletActionContext.getRequest().getSession().getId();
        oltPortInfoService.refreshAllPonOptical(entityId, jConnectedId, seesionId);
        return NONE;
    }

    private static JsonConfig getJsonConfig() {
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.registerDefaultValueProcessor(Integer.class, new DefaultValueProcessor() {
            @SuppressWarnings("rawtypes")
            public Object getDefaultValue(Class type) {
                return JSONNull.getInstance();
            }
        });
        jsonConfig.registerDefaultValueProcessor(Double.class, new DefaultValueProcessor() {
            @SuppressWarnings("rawtypes")
            public Object getDefaultValue(Class type) {
                return JSONNull.getInstance();
            }
        });
        jsonConfig.registerDefaultValueProcessor(Long.class, new DefaultValueProcessor() {
            @SuppressWarnings("rawtypes")
            public Object getDefaultValue(Class type) {
                return JSONNull.getInstance();
            }
        });
        jsonConfig.registerDefaultValueProcessor(Boolean.class, new DefaultValueProcessor() {
            @SuppressWarnings("rawtypes")
            public Object getDefaultValue(Class type) {
                return JSONNull.getInstance();
            }
        });
        jsonConfig.registerDefaultValueProcessor(String.class, new DefaultValueProcessor() {
            @SuppressWarnings("rawtypes")
            public Object getDefaultValue(Class type) {
                return JSONNull.getInstance();
            }
        });
        return jsonConfig;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public Long getPortId() {
        return portId;
    }

    public void setPortId(Long portId) {
        this.portId = portId;
    }

    public Long getPortIndex() {
        return portIndex;
    }

    public void setPortIndex(Long portIndex) {
        this.portIndex = portIndex;
    }

    public Integer getPerfStats() {
        return perfStats;
    }

    public void setPerfStats(Integer perfStats) {
        this.perfStats = perfStats;
    }

    public String getjConnectedId() {
        return jConnectedId;
    }

    public void setjConnectedId(String jConnectedId) {
        this.jConnectedId = jConnectedId;
    }

    public String getCameraSwitch() {
        return cameraSwitch;
    }

    public void setCameraSwitch(String cameraSwitch) {
        this.cameraSwitch = cameraSwitch;
    }

}
