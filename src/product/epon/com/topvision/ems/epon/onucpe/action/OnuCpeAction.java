/**
 * 
 */
package com.topvision.ems.epon.onucpe.action;

import java.util.List;
import java.util.Properties;

import net.sf.json.JSONObject;

import org.apache.tools.ant.types.resources.MultiRootFileSet.SetType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.onu.service.OnuService;
import com.topvision.ems.epon.onucpe.domain.OnuCpeConfig;
import com.topvision.ems.epon.onucpe.domain.OnuUniCpe;
import com.topvision.ems.epon.onucpe.service.OnuCpeService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.service.SystemPreferencesService;

/**
 * @author vanzand
 *
 */
@Controller("onuCpeAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OnuCpeAction extends BaseAction {
    private static final long serialVersionUID = 7859020625655500124L;
    @Autowired
    private OnuCpeService onuCpeService;
    @Autowired
    private EntityService entityService;
    @Autowired
    private OnuService onuService;
    @Autowired
    private SystemPreferencesService systemPreferencesService;
    private Entity entity;
    private Long entityId;
    private Long onuId;
    private Long oltId;
    private Entity onu;
    private Integer onuCpeStatus;
    private Integer onuCpeInterval;
    private Long onuIndex;
    private String cameraSwitch;
    private OltOnuAttribute onuAttribute;

    /*
     * 获取UNI CPE列表
     */
    public String loadOnuUniCpeList() {
        List<OnuUniCpe> list = onuCpeService.loadOnuUniCpeList(onuId);
        for (OnuUniCpe onuUniCpe : list) {
        	if (onuUniCpe.getType().equals(1)) {
				onuUniCpe.setRenderType("static");
			}
        	else if (onuUniCpe.getType().equals(2)) {
				onuUniCpe.setRenderType("dynamic");
			}
		}
        writeDataToAjax(list);
        return NONE;
    }

    /**
     * 根据ONUINDEX获取ONU的UNI CPE列表
     * 
     * @return
     * @throws Exception
     */
    public String loadOnuUniCpeListByIndex() throws Exception {
        Long onuId = onuService.getOnuIdByIndex(entityId, onuIndex);
        onuCpeService.refreshOnuUniCpe(onuId);
        List<OnuUniCpe> list = onuCpeService.loadOnuUniCpeList(onuId);
        writeDataToAjax(list);
        return NONE;
    }

    /*
     * 获取UNI CPE列表
     */
    public String loadOltUniCpeList() {
        List<OnuUniCpe> list = onuCpeService.loadOltUniCpeList(oltId, start, limit);
        int count = onuCpeService.loadOltUniCpeListCount(oltId);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data", list);
        jsonObject.put("rowCount", count);
        writeDataToAjax(jsonObject);
        return NONE;
    }

    /**
     * 实时刷新ONU CPE
     * 
     * @return
     */
    public String refreshOnuUniCpe() {
        onuCpeService.refreshOnuUniCpe(onuId);
        return NONE;
    }

    /**
     * onu cpe采集全局配置
     * 
     * @return
     */
    public String modifyOnuCpeConfig() {
        OnuCpeConfig config = new OnuCpeConfig();
        config.setOnuCpeInterval(onuCpeInterval * 1000);
        config.setOnuCpeStatus(onuCpeStatus);
        onuCpeService.updateOnuCpeConfig(config);
        return NONE;
    }

    /**
     * 展示ONU CPE列表
     * 
     * @return
     */
    public String showOnuCpeList() {
        onu = entityService.getEntity(onuId);
        onuAttribute = onuService.getOnuAttribute(onuId);
        return SUCCESS;
    }

    /**
     * 展示OLT CPE列表
     * 
     * @return
     */
    public String showOltCpeList() {
        Properties properties = systemPreferencesService.getModulePreferences("camera");
        cameraSwitch = properties.getProperty("camera.switch");
        entity = entityService.getEntity(entityId);
        return SUCCESS;
    }

    public Long getOnuId() {
        return onuId;
    }

    public void setOnuId(Long onuId) {
        this.onuId = onuId;
    }

    public Entity getOnu() {
        return onu;
    }

    public void setOnu(Entity onu) {
        this.onu = onu;
    }

    public Integer getOnuCpeStatus() {
        return onuCpeStatus;
    }

    public void setOnuCpeStatus(Integer onuCpeStatus) {
        this.onuCpeStatus = onuCpeStatus;
    }

    public Integer getOnuCpeInterval() {
        return onuCpeInterval;
    }

    public void setOnuCpeInterval(Integer onuCpeInterval) {
        this.onuCpeInterval = onuCpeInterval;
    }

    public Long getOltId() {
        return oltId;
    }

    public void setOltId(Long oltId) {
        this.oltId = oltId;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getOnuIndex() {
        return onuIndex;
    }

    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
    }

    /**
     * @return the onuAttribute
     */
    public OltOnuAttribute getOnuAttribute() {
        return onuAttribute;
    }

    /**
     * @param onuAttribute
     *            the onuAttribute to set
     */
    public void setOnuAttribute(OltOnuAttribute onuAttribute) {
        this.onuAttribute = onuAttribute;
    }

    public String getCameraSwitch() {
        return cameraSwitch;
    }

    public void setCameraSwitch(String cameraSwitch) {
        this.cameraSwitch = cameraSwitch;
    }

}
