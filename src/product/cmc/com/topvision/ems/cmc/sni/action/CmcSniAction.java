/***********************************************************************
 * $Id: CmcSniAction.java,v1.0 2013-4-23 下午4:48:16 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.sni.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cmc.sni.facade.domain.CmcPhyConfig;
import com.topvision.ems.cmc.sni.facade.domain.CmcRateLimit;
import com.topvision.ems.cmc.sni.facade.domain.CmcSniConfig;
import com.topvision.ems.cmc.sni.service.CmcSniService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.web.struts2.BaseAction;

/**
 * @author haojie
 * @created @2013-4-23-下午4:48:16
 * 
 */
@Controller("cmcSniAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CmcSniAction extends BaseAction {
    private static final long serialVersionUID = 197640135422728382L;
    private Logger logger = LoggerFactory.getLogger(CmcSniAction.class);
    @Resource(name = "cmcSniService")
    private CmcSniService cmcSniService;
    @Resource(name = "entityService")
    private EntityService entityService;
    private CmcRateLimit cmcRateLimit;
    private Long cmcId;

    private Integer arpLimit;
    private Integer uniLimit;
    private Integer udpLimit;
    private Integer dhcpLimit;
    private Integer icmpLimit;
    private Integer igmpLimit;
    private Long typeId;

    private JSONArray cmcPhyConfigListJson;

    private Integer sniPhy1Config;
    private Integer sniPhy2Config;

    private Integer loopbackStatus;

    /**
     * 显示CPU端口限速页面
     * 
     * @return String
     */
    public String showCpuRateLimit() {
        cmcRateLimit = cmcSniService.getCmcRateLimit(cmcId);
        return SUCCESS;
    }

    /**
     * 修改CPU端口限速
     * 
     * @return String
     */
    public String modifyCmcCpuPortRateLimit() {
        String message;
        try {
            cmcSniService.modifyCmcCpuPortRateLimit(cmcId, arpLimit, uniLimit, udpLimit, dhcpLimit);
            message = "success";
        } catch (Exception e) {
            message = e.getMessage();
            logger.debug("", e);
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 显示SNI端口限速页面
     * 
     * @return String
     */
    public String showSniRateLimit() {
        cmcRateLimit = cmcSniService.getCmcRateLimit(cmcId);
        return SUCCESS;
    }

    /**
     * 修改上联口限速
     * 
     * @return String
     */
    public String modifyCmcSniRateLimit() {
        String message;
        try {
            cmcSniService.modifyCmcSniRateLimit(cmcId, cmcRateLimit);
            message = "success";
        } catch (Exception e) {
            message = e.getMessage();
            logger.debug("", e);
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 显示PHY信息配置页面（3C）
     * 
     * @return String
     */
    public String showPhyConfigFor3c() {
        return SUCCESS;
    }

    /**
     * 显示PHY信息配置页面（4C）
     * 
     * @return String
     */
    public String showPhyConfigFor4c() {
        List<CmcPhyConfig> cmcPhyConfigList = cmcSniService.getCmcPhyConfigList(cmcId);
        typeId = entityService.getEntity(cmcId).getTypeId();
        cmcPhyConfigListJson = JSONArray.fromObject(cmcPhyConfigList);
        return SUCCESS;
    }

    public String refreshPhyConfigFor4c() {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            List<CmcPhyConfig> list = cmcSniService.refreshCmcPhyConfig(cmcId);
            map.put("message", "success");
            map.put("data", list);
        } catch (Exception e) {
            map.put("message", "failure");
            logger.error("", e);
        }
        writeDataToAjax(JSONObject.fromObject(map));
        return NONE;
    }

    /**
     * 修改上联口PHY信息
     * 
     * @return String
     */
    public String modifyCmcSniPhyConfig() {
        String message;
        try {
            if (cmcSniService.modifyCmcSniPhyConfig(cmcId, sniPhy1Config, sniPhy2Config)) {
                message = "success";
            } else {
                message = "fail";
            }
        } catch (Exception e) {
            message = e.getMessage();
            logger.debug("", e);
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 获取上联口环回使能
     * 
     * @return String
     */
    public String loadSniUplinkLoopbackStatus() {
        Map<String, Object> json = new HashMap<String, Object>();
        CmcSniConfig cmcSniConfig = cmcSniService.getCmcSniConfig(cmcId);
        json.put("cmcSniConfig", cmcSniConfig);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 设置上联口环回使能
     * 
     * @return String
     */
    public String modifySniLoopbackStatus() {
        String message;
        try {
            cmcSniService.modifySniLoopbackStatus(cmcId, loopbackStatus);
            message = "success";
        } catch (Exception e) {
            message = e.getMessage();
            logger.debug("", e);
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 显示风暴抑制配置页面
     * 
     * @return
     */
    public String showStormLimitConfig() {
        cmcRateLimit = cmcSniService.getCmcRateLimit(cmcId);
        return SUCCESS;
    }

    public String modifyStormLimitConfig() {
        String message;
        try {
            cmcSniService.modifyStormLimitConfig(cmcId, cmcRateLimit);
            message = "success";
        } catch (Exception e) {
            message = e.getMessage();
            logger.debug("", e);
        }
        writeDataToAjax(message);
        return NONE;
    }

    public CmcSniService getCmcSniService() {
        return cmcSniService;
    }

    public void setCmcSniService(CmcSniService cmcSniService) {
        this.cmcSniService = cmcSniService;
    }

    public CmcRateLimit getCmcRateLimit() {
        return cmcRateLimit;
    }

    public void setCmcRateLimit(CmcRateLimit cmcRateLimit) {
        this.cmcRateLimit = cmcRateLimit;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Integer getArpLimit() {
        return arpLimit;
    }

    public void setArpLimit(Integer arpLimit) {
        this.arpLimit = arpLimit;
    }

    public Integer getUniLimit() {
        return uniLimit;
    }

    public void setUniLimit(Integer uniLimit) {
        this.uniLimit = uniLimit;
    }

    public Integer getUdpLimit() {
        return udpLimit;
    }

    public void setUdpLimit(Integer udpLimit) {
        this.udpLimit = udpLimit;
    }

    public Integer getDhcpLimit() {
        return dhcpLimit;
    }

    public void setDhcpLimit(Integer dhcpLimit) {
        this.dhcpLimit = dhcpLimit;
    }

    public Integer getIcmpLimit() {
        return icmpLimit;
    }

    public void setIcmpLimit(Integer icmpLimit) {
        this.icmpLimit = icmpLimit;
    }

    public Integer getIgmpLimit() {
        return igmpLimit;
    }

    public void setIgmpLimit(Integer igmpLimit) {
        this.igmpLimit = igmpLimit;
    }

    public JSONArray getCmcPhyConfigListJson() {
        return cmcPhyConfigListJson;
    }

    public void setCmcPhyConfigListJson(JSONArray cmcPhyConfigListJson) {
        this.cmcPhyConfigListJson = cmcPhyConfigListJson;
    }

    public Integer getSniPhy1Config() {
        return sniPhy1Config;
    }

    public void setSniPhy1Config(Integer sniPhy1Config) {
        this.sniPhy1Config = sniPhy1Config;
    }

    public Integer getSniPhy2Config() {
        return sniPhy2Config;
    }

    public void setSniPhy2Config(Integer sniPhy2Config) {
        this.sniPhy2Config = sniPhy2Config;
    }

    public Integer getLoopbackStatus() {
        return loopbackStatus;
    }

    public void setLoopbackStatus(Integer loopbackStatus) {
        this.loopbackStatus = loopbackStatus;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }
}
