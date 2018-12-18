/***********************************************************************
 * $Id: CmSignalTargetThresholdAction.java,v1.0 2017年8月9日 上午10:01:04 $
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

import com.topvision.ems.cm.pnmp.facade.domain.CmTargetThreshold;
import com.topvision.ems.cm.pnmp.service.CmSignalTargetThresholdService;
import com.topvision.framework.web.struts2.BaseAction;

/**
 * @author lizongtian
 * @created @2017年8月9日-上午10:01:04
 *
 */
@Controller("cmSignalTargetThresholdAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CmSignalTargetThresholdAction extends BaseAction {

    private static final long serialVersionUID = 2899167205911381202L;

    @Autowired
    private CmSignalTargetThresholdService cmSignalTargetThresholdService;
    private String thresholds;
    private String level;

    public String showCmTargetConfig() {
        return SUCCESS;
    }

    public String loadCmSignalTargetThresholds() {
        List<CmTargetThreshold> thresholdList = cmSignalTargetThresholdService.getAllThresholds();
        writeDataToAjax(thresholdList);
        return NONE;
    }

    public String saveCmTargetConfig() {
        JSONArray jsonArray = JSONArray.fromObject(thresholds);
        @SuppressWarnings("unchecked")
        List<CmTargetThreshold> thresholdList = JSONArray.toList(jsonArray, new CmTargetThreshold(), new JsonConfig());
        cmSignalTargetThresholdService.updateThresholds(thresholdList);
        return NONE;
    }

    public String loadCmTargetLevelThresholds() {
        List<CmTargetThreshold> thresholdList = cmSignalTargetThresholdService.selectLevelThresholds(level);
        writeDataToAjax(thresholdList);
        return NONE;
    }

    public String getThresholds() {
        return thresholds;
    }

    public void setThresholds(String thresholds) {
        this.thresholds = thresholds;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

}
