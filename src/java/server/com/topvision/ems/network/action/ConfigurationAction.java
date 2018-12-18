package com.topvision.ems.network.action;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;

import com.topvision.ems.facade.domain.Config;
import com.topvision.ems.network.service.ConfigurationService;
import com.topvision.framework.web.struts2.BaseAction;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

@Controller("configurationAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ConfigurationAction extends BaseAction {
    private static final long serialVersionUID = 1L;
    @Autowired
    private ConfigurationService configurationService;
    private List<Config> needUploadConfigs = null;
    private List<Config> needDownConfigs = null;
    private List<Config> configs = null;
    private Long entityId = null;

    public List<Config> getNeedDownConfigs() {
        return needDownConfigs;
    }

    public void setNeedDownConfigs(List<Config> needDownConfigs) {
        this.needDownConfigs = needDownConfigs;
    }

    public List<Config> getNeedUploadConfigs() {
        return needUploadConfigs;
    }

    public void setNeedUploadConfigs(List<Config> needUploadConfigs) {
        this.needUploadConfigs = needUploadConfigs;
    }

    public List<Config> getConfigs() {
        return configs;
    }

    public void setConfigs(List<Config> configs) {
        this.configs = configs;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    /**
     * 是否有配置没有上传
     * 
     * @return
     * @throws JSONException
     * @throws IOException
     */
    public String isUpLoadAccess() throws JSONException, IOException {
        boolean access = false;
        if (entityId != null) {
            access = configurationService.isUpLoadAccess(entityId);
        }
        JSONObject json = new JSONObject();
        json.put("access", access);
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 是否有配置没有同步
     * 
     * @return
     * @throws JSONException
     * @throws IOException
     */
    public String isDownLoadAccess() throws JSONException, IOException {
        boolean access = false;
        if (entityId != null) {
            access = configurationService.isDownLoadAccess(entityId);
        }
        JSONObject json = new JSONObject();
        json.put("access", access);
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 获取需要上传的设备配置
     * 
     * @return
     */
    public String showUpLoadConfig() {
        if (entityId != null) {
            return ERROR;
        }
        needUploadConfigs = configurationService.getUpLoadConfig(entityId);
        return SUCCESS;
    }

    /**
     * 获取需要同步的设备配置
     * 
     * @return
     */
    public String showDownLoadConfig() {
        if (entityId != null) {
            return ERROR;
        }
        needDownConfigs = configurationService.getDownLoadConfig(entityId);
        return SUCCESS;
    }

    /**
     * 上传配置
     * 
     * @return
     */
    public String upLoadConfig() throws JSONException, IOException {
        boolean sussess = false;
        if (entityId != null) {
            try {
                configurationService.upLoadConfig(entityId, configs);
                sussess = true;
            } catch (DataAccessException e) {
                sussess = false;
            }
        }
        JSONObject json = new JSONObject();
        json.put("sussess", sussess);
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 同步配置
     * 
     * @return
     */
    public String downLoadConfig() throws JSONException, IOException {
        boolean sussess = false;
        if (entityId != null) {
            try {
                configurationService.downLoadConfig(entityId, configs);
                sussess = true;
            } catch (DataAccessException e) {
                sussess = false;
            }
        }
        JSONObject json = new JSONObject();
        json.put("sussess", sussess);
        writeDataToAjax(json);
        return NONE;
    }
}