package com.topvision.ems.network.action;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.framework.web.struts2.BaseAction;

@Controller("entityPreferencesAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EntityPreferencesAction extends BaseAction {
    private static final long serialVersionUID = 5123167450019299920L;
    private static Logger logger = LoggerFactory.getLogger(EntityPreferencesAction.class);
    private long entityId;
    private long pingIntervalOfNormal = 300000;
    private long flowIntervalOfNormal = 300000;
    private long systemIntervalOfNormal = 300000;
    private long pingIntervalOfError = 60000;

    /**
     * 保存轮询间隔.
     * 
     * @return
     */
    public String savePollingInterval() {
        return NONE;
    }

    /**
     * 显示轮询间隔.
     * 
     * @return
     */
    public String showPollingInterval() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("entityId", String.valueOf(entityId));
        map.put("category", "ping");
        logger.info("showPollingInterval SUCCESS.");
        return SUCCESS;
    }

    public long getEntityId() {
        return entityId;
    }

    public long getFlowIntervalOfNormal() {
        return flowIntervalOfNormal;
    }

    public long getPingIntervalOfError() {
        return pingIntervalOfError;
    }

    public long getPingIntervalOfNormal() {
        return pingIntervalOfNormal;
    }

    public long getSystemIntervalOfNormal() {
        return systemIntervalOfNormal;
    }

    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }

    public void setFlowIntervalOfNormal(long flowIntervalOfNormal) {
        this.flowIntervalOfNormal = flowIntervalOfNormal;
    }

    public void setPingIntervalOfError(long pingIntervalOfError) {
        this.pingIntervalOfError = pingIntervalOfError;
    }

    public void setPingIntervalOfNormal(long pingIntervalOfNormal) {
        this.pingIntervalOfNormal = pingIntervalOfNormal;
    }

    public void setSystemIntervalOfNormal(long systemIntervalOfNormal) {
        this.systemIntervalOfNormal = systemIntervalOfNormal;
    }

}
