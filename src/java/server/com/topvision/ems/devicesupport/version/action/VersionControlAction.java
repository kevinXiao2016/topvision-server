/***********************************************************************
 * $Id: VersionControlAction.java,v1.0 2017年10月12日 下午2:05:51 $
 * 
 * @author: vanzand
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.devicesupport.version.action;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSONObject;
import com.topvision.ems.devicesupport.version.service.DeviceVersionService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.web.struts2.BaseAction;

/**
 * @author vanzand
 * @created @2017年10月12日-下午2:05:51
 *
 */
@Controller("versionControlAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class VersionControlAction extends BaseAction {

    private static final long serialVersionUID = -8052720601318143303L;

    @Resource(name = "deviceVersionService")
    private DeviceVersionService deviceVersionService;
    @Autowired
    private EntityService entityService;
    @Autowired
    private EntityTypeService entityTypeService;

    private Long typeId;
    private String version;
    private long entityId = -1;
    private String functionName;

    /**
     * 加载基于设备版本以及网管版本的菜单控制
     * 
     * @return
     * @throws JSONException
     * @throws IOException
     */
    public String loadVersionControl() throws JSONException, IOException {
        JSONObject json = deviceVersionService.getVersionControl(typeId, version);
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 通过EntityId获取设备的版本和typeId信息
     * 
     * @TODO 需要增加通过传递ONUID自动找到olt的id并得到olt的typeid和version的功能
     * @return
     * @throws IOException
     */
    public String loadTypeAndVersion() throws IOException {
        Entity entity = entityService.getEntity(entityId);
        String version = deviceVersionService.getEntityVersion(entityId);
        net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
        jsonObject.put("version", version);
        jsonObject.put("typeId", entity.getTypeId());
        writeDataToAjax(jsonObject);
        return NONE;
    }

    public String loadSupportDependsOnSub() throws IOException {
        List<Entity> subEntities = entityService.getSubEntityByEntityId(entityId);
        boolean support = false;
        for (Entity sub : subEntities) {
            if (!entityTypeService.isCcmts(sub.getTypeId())) {
                continue;
            }
            if (deviceVersionService.isFunctionSupported(sub.getEntityId(), functionName)) {
                support = true;
                break;
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("support", support);
        writeDataToAjax(jsonObject);
        return NONE;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public long getEntityId() {
        return entityId;
    }

    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

}
