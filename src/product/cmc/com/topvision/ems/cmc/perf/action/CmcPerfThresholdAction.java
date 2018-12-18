/***********************************************************************
 * $Id: CmcPerfThresholdAction.java,v1.0 2013-9-26 下午5:28:02 $
 * 
 * @author: fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.perf.action;

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
 * @created @2013-9-26-下午5:28:02
 *
 */
@Controller("cmcPerfThresholdAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CmcPerfThresholdAction extends PerfThresholdAction {

    private static final long serialVersionUID = 5069071300798792125L;
    @Autowired
    private EntityTypeService entityTypeService;
    private JSONArray ccmtsSubTypes;
    private Integer tempRela;

    /**
     * CC性能阈值模板
     * @return
     */
    public String showCCPerfTemp() {
        //需要获取所有的CCMTS类型
        ccmtsSubTypes = perfThresholdService.loadEntitySubType(entityTypeService.getCcmtsandcmtsType().intValue());
        return SUCCESS;
    }

    /**
     * 加载CCMTS与绑定的阈值模板的列表
     * @return
     * @throws IOException
     */
    public String loadCmcThresholdTemplate() throws IOException {
        Long ccType = entityTypeService.getCcmtsandcmtsType();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityType", ccType);
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
        if (tempRela != null && tempRela != -1) {
            map.put("tempRela", tempRela);
        }
        map.put("start", start);
        map.put("limit", limit);
        perfThresholdList = perfThresholdService.selectEntityPerfThresholdTmeplate(map);
        Long perfThresholdCount = perfThresholdService.selectPerfTmeplateCount(map);
        //add by fanzidong,需要在展示前格式化MAC地址
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        String macRule = uc.getMacDisplayStyle();
        for (PerfThresholdEntity thresholdEntity : perfThresholdList) {
            String formatedMac = MacUtils.convertMacToDisplayFormat(thresholdEntity.getMac(), macRule);
            thresholdEntity.setMac(formatedMac);
            thresholdEntity.setEntityType(entityTypeService.getEntityNetworkGroupIdByEntityTypeId(
                    thresholdEntity.getTypeId().longValue()).intValue());
        }
        JSONObject json = new JSONObject();
        json.put("data", perfThresholdList);
        json.put("rowCount", perfThresholdCount);
        json.write(response.getWriter());
        return NONE;
    }

    public JSONArray getCcmtsSubTypes() {
        return ccmtsSubTypes;
    }

    public void setCcmtsSubTypes(JSONArray ccmtsSubTypes) {
        this.ccmtsSubTypes = ccmtsSubTypes;
    }

    public Integer getTempRela() {
        return tempRela;
    }

    public void setTempRela(Integer tempRela) {
        this.tempRela = tempRela;
    }

}
