/***********************************************************************
 * $Id: VersionAction.java,v1.0 2012-11-28 下午3:34:07 $
 * 
 * @author: dengl
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.action;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.framework.version.domain.Version;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.SystemVersion;
import com.topvision.platform.service.VersionService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author dengl
 * @created @2012-11-28-下午3:34:07
 * 
 */
@Controller("versionAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class VersionAction extends BaseAction {
    private static final long serialVersionUID = 1371297882863141950L;
    private static Logger logger = LoggerFactory.getLogger(VersionAction.class);
    private final DateFormat shortDF = new SimpleDateFormat("yyyy-MM-dd");
    @Autowired
    private VersionService versionService;
    private JSONObject versions = new JSONObject();
    private com.topvision.framework.Version version;

    /**
     * 进入显示软件版本号的页面
     * 
     * @return
     */
    public String queryVersion() {
        List<Version> ret = versionService.queryVersion();
        JSONArray jsonArray = new JSONArray();
        for (Version version : ret) {
            JSONObject v = new JSONObject();
            v.put("moduleName", version.getModuleName());
            v.put("versionNum", version.getVersionNum());
            v.put("versionDate", toShortDate(version.getVersionDate()));
            jsonArray.add(v);
        }

        versions.put("versions", jsonArray);

        try {
            version = new SystemVersion();
        } catch (Exception e) {
            logger.error("", e);
        }
        return SUCCESS;
    }

    /**
     * 格式化输出时间
     * 
     * @param date
     * @return
     */
    private String toShortDate(Date date) {
        if (date != null) {
            return shortDF.format(date);
        }

        return null;
    }

    public void setVersionService(VersionService versionService) {
        this.versionService = versionService;
    }

    public JSONObject getVersions() {
        return versions;
    }

    public com.topvision.framework.Version getVersion() {
        return version;
    }
}
