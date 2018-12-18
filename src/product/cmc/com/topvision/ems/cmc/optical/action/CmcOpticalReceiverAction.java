/***********************************************************************
 * $Id: CmcOpticalReceiverAction.java,v1.0 2013-12-2 下午2:26:35 $
 * 
 * @author: dosion
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.optical.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.ems.cmc.optical.domain.CmcOpReceiverCfg;
import com.topvision.ems.cmc.optical.domain.CmcOpReceiverStatus;
import com.topvision.ems.cmc.optical.service.CmcOpticalReceiverService;
import com.topvision.framework.web.struts2.BaseAction;

import net.sf.json.JSONObject;

/**
 * @author dosion
 * @created @2013-12-2-下午2:26:35
 * 
 */
@Controller("cmcOpticalReceiverAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CmcOpticalReceiverAction extends BaseAction {
    private static final long serialVersionUID = 5382613039053968916L;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource(name = "cmcOpticalReceiverService")
    private CmcOpticalReceiverService cmcOpservice;
    private Long cmcId;
    private JSONObject opCfgJson = new JSONObject();
    private CmcOpReceiverCfg cmcOpReceiverCfg;
    @Resource(name = "cmcService")
    private CmcService cmcService;
    private Byte outputControl;
    private Integer outputRFlevelatt;
    private Integer outputAGCOrigin;
    private Integer outputIndex;
    private CmcAttribute cmcAttribute;

    public String showOpticalReciver() {
    	cmcAttribute = cmcService.getCmcAttributeByCmcId(cmcId);
        return SUCCESS;
    }

    /**
     * 加载光机状态数据
     * 
     * @return 以JSON格式返回前台光机状态信息
     */
    public String loadOpticalReceiverStatus() {
        CmcOpReceiverStatus status = cmcOpservice.getOpticalReceiverStatus(cmcId);
        JSONObject json = JSONObject.fromObject(status);
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 显示光机配置页面
     * 
     * @return 返回光机配置页面
     */
    public String showOpticalReceiverCfgView() {
        cmcOpReceiverCfg = cmcOpservice.getOpticalReceiverCfg(cmcId);
        opCfgJson = JSONObject.fromObject(cmcOpReceiverCfg);
        return SUCCESS;
    }

    /**
     * 修改光机配置
     * 
     * @return 以字符串的形式返回光机配置结果
     */
    public String modifyOpticalReceiverCfg() {
        String result = null;
        cmcOpReceiverCfg = new CmcOpReceiverCfg();
        cmcOpReceiverCfg.setCmcId(cmcId);
        cmcOpReceiverCfg.setOutputControl(outputControl);
        cmcOpReceiverCfg.setOutputAGCOrigin(outputAGCOrigin);
        cmcOpReceiverCfg.setOutputRFlevelatt(outputRFlevelatt);
        cmcOpReceiverCfg.setOutputIndex(outputIndex);
        try {
            cmcOpservice.modifyOpReceiverCfg(cmcId, cmcOpReceiverCfg);
            result = "success";
        } catch (Exception e) {
            logger.error("", e);
            result = "failure";
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * 从设备获取光机配置信息
     * 
     * @return 以JSON格式返回刷新结果，如果成功则返回刷新后的光机状态
     */
    public String refreshOpticalReceiverInfo() {
        Map<String, Object> map = new HashMap<String, Object>();
        String result = null;
        try {
            cmcOpservice.refreshOpReceiverInfo(cmcId);
            CmcOpReceiverStatus status = cmcOpservice.getOpticalReceiverStatus(cmcId);
            map.put("receiverStatus", status);
            result = "success";
        } catch (Exception e) {
            result = "failure";
            logger.error("", e);
        }
        map.put("result", result);
        writeDataToAjax(JSONObject.fromObject(map));
        return NONE;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public JSONObject getOpCfgJson() {
        return opCfgJson;
    }

    public void setOpCfgJson(JSONObject opCfgJson) {
        this.opCfgJson = opCfgJson;
    }

    public CmcOpReceiverCfg getCmcOpReceiverCfg() {
        return cmcOpReceiverCfg;
    }

    public void setCmcOpReceiverCfg(CmcOpReceiverCfg cmcOpReceiverCfg) {
        this.cmcOpReceiverCfg = cmcOpReceiverCfg;
    }

    public Byte getOutputControl() {
        return outputControl;
    }

    public void setOutputControl(Byte outputControl) {
        this.outputControl = outputControl;
    }

    public Integer getOutputRFlevelatt() {
        return outputRFlevelatt;
    }

    public void setOutputRFlevelatt(Integer outputRFlevelatt) {
        this.outputRFlevelatt = outputRFlevelatt;
    }

    public Integer getOutputAGCOrigin() {
        return outputAGCOrigin;
    }

    public void setOutputAGCOrigin(Integer outputAGCOrigin) {
        this.outputAGCOrigin = outputAGCOrigin;
    }

    public Integer getOutputIndex() {
        return outputIndex;
    }

    public void setOutputIndex(Integer outputIndex) {
        this.outputIndex = outputIndex;
    }

	public CmcAttribute getCmcAttribute() {
		return cmcAttribute;
	}

	public void setCmcAttribute(CmcAttribute cmcAttribute) {
		this.cmcAttribute = cmcAttribute;
	}

}
