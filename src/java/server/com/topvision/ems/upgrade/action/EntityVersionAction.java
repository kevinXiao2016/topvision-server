/***********************************************************************
 * $Id: EntityVersionAction.java,v1.0 2014年9月23日 下午3:11:57 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.upgrade.action;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.facade.domain.EntityType;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.ems.upgrade.domain.EntityVersion;
import com.topvision.ems.upgrade.service.EntityVersionService;
import com.topvision.framework.web.struts2.BaseAction;

/**
 * @author loyal
 * @created @2014年9月23日-下午3:11:57
 * 
 */
@Controller("entityVersionAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EntityVersionAction extends BaseAction {
    private static final long serialVersionUID = -3583526711489844347L;
    private Logger logger = LoggerFactory.getLogger(EntityVersionAction.class);
    @Autowired
    private EntityVersionService entityVersionService;
    @Autowired
    private EntityTypeService entityTypeService;
    private String versionName;
    private Long typeId;
    private JSONArray supportTypes;
    private String chooseFileName;

    public String showEntityVersionList() {
        // 获取所有CCMTS设备类型
        List<EntityType> ccmtslist = entityTypeService.loadSubType(entityTypeService.getCcmtsType());
        List<EntityType> onulist = entityTypeService.loadSubType(entityTypeService.getOnuType());
        List<EntityType> list = new ArrayList<EntityType>();
        list.addAll(ccmtslist);
        list.addAll(onulist);
        supportTypes = JSONArray.fromObject(list);
        return SUCCESS;
    }

    public String getEntityVersion() {
        EntityVersion entityVersion = entityVersionService.getEntityVersion(versionName);
        writeDataToAjax(JSONObject.fromObject(entityVersion));
        return NONE;
    }

    public String getEntityVersionList() {
        List<EntityVersion> list = entityVersionService.getEntityVersionList(versionName, typeId);
        Map<String, Object> map = new HashMap<>();
        map.put("data", list);
        map.put("rowCount", list.size());
        writeDataToAjax(JSONObject.fromObject(map));
        return NONE;
    }

    public String getEntityVersionProperty() {
        try {
            writeDataToAjax(entityVersionService.getEntityVersionProperty(versionName));
        } catch (Exception e) {
            logger.error("", e);

        }
        return NONE;
    }

    public String deleteEntityVersion() {
        List<Long> jobIds = entityVersionService.deleteEntityVersion(versionName);
        writeDataToAjax(JSONArray.fromObject(jobIds));
        return NONE;
    }

    public String upLoadFile() {
        String tempName = "" + System.currentTimeMillis();
        // 上传文件
        String tempDir = entityVersionService.getTempDir();
        MultiPartRequestWrapper multiWrapper = (MultiPartRequestWrapper) ServletActionContext.getRequest();
        Enumeration<String> fileParameterNames = multiWrapper.getFileParameterNames();
        while (fileParameterNames != null && fileParameterNames.hasMoreElements()) {
            String inputName = fileParameterNames.nextElement();
            String[] contentType = multiWrapper.getContentTypes(inputName);
            if (isNonEmpty(contentType)) {
                String[] fileName = multiWrapper.getFileNames(inputName);
                if (isNonEmpty(fileName)) {
                    File[] files = multiWrapper.getFiles(inputName);
                    if (files != null && files.length > 0) {
                        File tmpFile = files[0];
                        File tf = new File(tempDir + tempName + ".gz.tar");
                        tmpFile.renameTo(tf);
                        break;
                    }
                }
            }
        }
        // 上传文件完成后添加版本
        Map<String, String> result = new HashMap<>();

        try {
            entityVersionService.addEntityVersion(tempName);
            result.put("state", "success");
        } catch (Exception e) {
            result.put("state", "fail");
            result.put("message", e.getMessage());
            logger.debug("", e);
        }
        writeDataToAjax(JSONObject.fromObject(result));
        return NONE;
    }

    /**
     * 读取文件信息的时候判断数组是否有可用对象
     * 
     * @param objArray
     *            数组列表
     * @return String
     */
    private boolean isNonEmpty(Object[] objArray) {
        boolean result = false;
        for (int index = 0; index < objArray.length && !result; index++) {
            if (objArray[index] != null) {
                result = true;
            }
        }
        return result;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public JSONArray getSupportTypes() {
        return supportTypes;
    }

    public void setSupportTypes(JSONArray supportTypes) {
        this.supportTypes = supportTypes;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getChooseFileName() {
        return chooseFileName;
    }

    public void setChooseFileName(String chooseFileName) {
        this.chooseFileName = chooseFileName;
    }

}
