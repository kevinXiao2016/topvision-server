/***********************************************************************
 * $Id: UpgradeParamAction.java,v1.0 2014年9月23日 下午2:39:48 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.upgrade.action;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.upgrade.domain.UpgradeGlobalParam;
import com.topvision.ems.upgrade.service.UpgradeParamService;
import com.topvision.framework.web.struts2.BaseAction;

/**
 * @author loyal
 * @created @2014年9月23日-下午2:39:48
 * 
 */
@Controller("upgradeParamAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UpgradeParamAction extends BaseAction {
    private static final long serialVersionUID = 8584087381912359899L;
    @Resource(name = "upgradeParamService")
    private UpgradeParamService upgradeParamService;
    private UpgradeGlobalParam upgradeGlobalParam;
    private Long retryTimes;
    private Long retryInterval;
    private Long writeConfig;
    private final Logger logger = LoggerFactory.getLogger(UpgradeParamAction.class);

    public String showUpgradeParam() {
        upgradeGlobalParam = upgradeParamService.getUpgradeGlobalParam();
        return SUCCESS;
    }

    public String modifyUpgradeParam() {
        String message = "success";
        try {
            UpgradeGlobalParam upgradeGlobalParam = new UpgradeGlobalParam();
            upgradeGlobalParam.setRetryTimes(retryTimes);
            upgradeGlobalParam.setRetryInterval(retryInterval);
            upgradeGlobalParam.setWriteConfig(writeConfig);
            upgradeParamService.modifyUpgradeGlobalParam(upgradeGlobalParam);
        } catch (Exception ex) {
            message = "fail";
            logger.debug("modifyParamConfig fail", ex);
        }
        writeDataToAjax(message);
        return NONE;
    }

    public UpgradeGlobalParam getUpgradeGlobalParam() {
        return upgradeGlobalParam;
    }

    public void setUpgradeGlobalParam(UpgradeGlobalParam upgradeGlobalParam) {
        this.upgradeGlobalParam = upgradeGlobalParam;
    }

    public Long getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(Long retryTimes) {
        this.retryTimes = retryTimes;
    }

    public Long getRetryInterval() {
        return retryInterval;
    }

    public void setRetryInterval(Long retryInterval) {
        this.retryInterval = retryInterval;
    }

    public Long getWriteConfig() {
        return writeConfig;
    }

    public void setWriteConfig(Long writeConfig) {
        this.writeConfig = writeConfig;
    }

}
