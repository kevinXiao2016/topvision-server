/***********************************************************************
 * $Id: CmtsPerfThresholdAction.java,v1.0 2013-9-27 下午2:15:08 $
 * 
 * @author: fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmts.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.performance.action.PerfThresholdAction;
import com.topvision.ems.performance.domain.PerfThresholdEntity;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.common.MacUtils;
import com.topvision.platform.domain.UserContext;

/**
 * @author fanzidong
 * @created @2013-9-27-下午2:15:08
 *
 */
@Controller("cmtsPerfThresholdAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CmtsPerfThresholdAction extends PerfThresholdAction {

    private static final long serialVersionUID = 7446605234459554599L;
    @Autowired
    private EntityTypeService entityTypeService;
    private JSONArray cmtsSubTypes;

    /**
     * 跳转至CMTS性能阈值模板页面
     * @return
     */
    public String showCmtsPerfTemplate() {
        //需要获取所有的CCMTS类型
        Long cmtsType = entityTypeService.getCmtsType();
        setCmtsSubTypes(perfThresholdService.loadEntitySubType(cmtsType.intValue()));
        return SUCCESS;
    }

    /**
     * 加载CMTS与绑定的阈值模板的列表
     * @return
     * @throws IOException
     */
    public String loadCmtsThresholdTemplate() throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityType", entityTypeService.getCmtsType());
        map.put("entityName", entityName);
        map.put("entityIp", entityIp);
        map.put("templateName", templateName);
        if (mac != null && !"".equals(mac)) {
            String formatQueryMac = MacUtils.formatQueryMac(mac);
            if (formatQueryMac.indexOf(":") == -1) {
                map.put("queryMacWithoutSplit", formatQueryMac);
            }
            map.put("mac", formatQueryMac);
        }
        map.put("typeId", typeId);
        map.put("start", start);
        map.put("limit", limit);
        perfThresholdList = perfThresholdService.selectEntityPerfThresholdTmeplate(map);
        //add by fanzidong,需要在展示前格式化MAC地址
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        String macRule = uc.getMacDisplayStyle();
        for (PerfThresholdEntity thresholdEntity : perfThresholdList) {
            String formatedMac = MacUtils.convertMacToDisplayFormat(thresholdEntity.getMac(), macRule);
            thresholdEntity.setMac(formatedMac);
        }
        JSONObject json = new JSONObject();
        json.put("data", perfThresholdList);
        json.put("rowCount", perfThresholdList.size());
        json.write(response.getWriter());
        return NONE;
    }

    public JSONArray getCmtsSubTypes() {
        return cmtsSubTypes;
    }

    public void setCmtsSubTypes(JSONArray cmtsSubTypes) {
        this.cmtsSubTypes = cmtsSubTypes;
    }

}