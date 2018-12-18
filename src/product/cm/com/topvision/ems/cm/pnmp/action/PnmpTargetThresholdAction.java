/***********************************************************************
 * $Id: PnmpTargetThresholdAction.java,v1.0 2017年8月9日 上午10:00:55 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.pnmp.action;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cm.pnmp.facade.domain.PnmpTargetThreshold;
import com.topvision.ems.cm.pnmp.service.PnmpTargetThresholdService;
import com.topvision.framework.web.struts2.BaseAction;

/**
 * @author lizongtian
 * @created @2017年8月9日-上午10:00:55
 *
 */
@Controller("pnmpTargetThresholdAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PnmpTargetThresholdAction extends BaseAction {

    private static final long serialVersionUID = 6177717946855075760L;

    @Autowired
    private PnmpTargetThresholdService pnmpTargetThresholdService;
    private String thresholds;
    private String level;

    public String showPnmpTargetConfig() {
        return SUCCESS;
    }

    public String loadPnmpTargetConfig() {
        List<PnmpTargetThreshold> thresholdList = pnmpTargetThresholdService.getAllThresholds();
        writeDataToAjax(thresholdList);
        return NONE;
    }

    public String savePnmpTargetConfig() {
        JSONArray jsonArray = JSONArray.fromObject(thresholds);
        @SuppressWarnings("unchecked")
        List<PnmpTargetThreshold> thresholdList = JSONArray.toList(jsonArray, new PnmpTargetThreshold(),
                new JsonConfig());
        pnmpTargetThresholdService.updateThresholds(thresholdList);
        return NONE;
    }

    public String getPnmpTargetLevelThresholds() {
        List<PnmpTargetThreshold> thresholdList = pnmpTargetThresholdService.getLevelThresholds(level);
        writeDataToAjax(thresholdList);
        return NONE;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getThresholds() {
        return thresholds;
    }

    public void setThresholds(String thresholds) {
        this.thresholds = thresholds;
    }

}
