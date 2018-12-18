/***********************************************************************
 * $ EponCmListAction.java,v1.0 2014-2-25 18:09:51 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.cm.action;

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

import com.topvision.ems.epon.cm.service.EponCmListService;
import com.topvision.ems.epon.domain.EponCmNumStatic;
import com.topvision.ems.epon.performance.domain.CmtsCmNum;
import com.topvision.ems.epon.performance.domain.PonCmNum;
import com.topvision.framework.web.struts2.BaseAction;

/**
 * @author jay
 * @created @2014-2-25-18:09:51
 */
@Controller("eponCmListAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EponCmListAction extends BaseAction {
    private static final long serialVersionUID = 7342180206202447869L;
    @Resource(name = "eponCmListService")
    private EponCmListService eponCmListService;
    private Long entityId;
    private Long cmcId;

    /**
     * 展示CM列表页面
     * 
     * @return
     * @throws IOException
     */
    public String loadPonAttrByCmcId() throws IOException {
        JSONObject ponObj = eponCmListService.loadPonAttrByCmcId(cmcId);
        ponObj.write(response.getWriter());
        // writeTextToAjax(""+ponObj.getLong("ponId"));
        return NONE;
    }

    /**
     * 加载OLT下的PON口列表
     *
     * @return
     */
    public String loadPonOfOlt() {
        JSONArray ponOfOlt = eponCmListService.loadPonOfOlt(entityId);
        writeDataToAjax(ponOfOlt);
        return NONE;
    }

    /**
     * 刷新CM信息
     *
     * @return String
     */
    public String refreshCmInfo() {
        //TODO 需要考虑该方法位置
        EponCmNumStatic eponCmNumStatic = eponCmListService.getEponCmNumStatic(entityId);
        if (eponCmNumStatic == null) {
            eponCmNumStatic = new EponCmNumStatic();
            eponCmNumStatic.setEntityId(entityId);
            eponCmNumStatic.setCmNumActive(0);
            eponCmNumStatic.setCmNumOffline(0);
            eponCmNumStatic.setCmNumOnline(0);
            eponCmNumStatic.setCmNumTotal(0);
        }
        writeDataToAjax(JSONObject.fromObject(eponCmNumStatic));
        return NONE;
    }

    /**
     * 刷新CM信息
     *
     * @return String
     */
    public String getPonCmNum() {
        Map<String, Object> json = new HashMap<String, Object>();
        List<PonCmNum> ponCmNumList = eponCmListService.getPonCmNumList(entityId);
        json.put("data", ponCmNumList);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 刷新CM信息
     *
     * @return String
     */
    public String getCmtsCmNum() {
        Map<String, Object> json = new HashMap<String, Object>();
        List<CmtsCmNum> cmtsCmNumList = eponCmListService.getCmtsCmNumList(entityId);
        json.put("data", cmtsCmNumList);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

}
