/***********************************************************************
 * $Id: OltDhcpBaseAction.java,v1.0 2017年11月24日 上午9:19:50 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.oltdhcp.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltDhcpGlobalObjects;
import com.topvision.ems.epon.oltdhcp.service.OltDhcpBaseService;
import com.topvision.framework.web.struts2.BaseAction;

import net.sf.json.JSONObject;

/**
 * @author haojie
 * @created @2017年11月24日-上午9:19:50
 *
 */
@Controller("oltDhcpBaseAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OltDhcpBaseAction extends BaseAction {
    private static final long serialVersionUID = 1117382756583538917L;
    private Long entityId;
    private Integer topOltDhcpEnable;

    private Integer opt82Enable;
    private Integer opt82Policy;
    private String opt82Format;

    private Integer sourceVerifyEnable;

    private Integer pppoEPlusEnable;
    private Integer pppoEPlusPolicy;
    private String pppoEPlusFormat;
    @Autowired
    private OltDhcpBaseService oltDhcpBaseService;

    /**
     * 跳转olt dhcp
     * 
     * @return
     */
    public String showOltDhcp() {
        TopOltDhcpGlobalObjects global = oltDhcpBaseService.getOltDhcpBaseCfg(entityId);
        topOltDhcpEnable = global.getTopOltDhcpEnable();
        return SUCCESS;
    }

    /**
     * 跳转olt ppoe
     * 
     * @return
     */
    public String showOltPppoe() {
        TopOltDhcpGlobalObjects global = oltDhcpBaseService.getOltDhcpBaseCfg(entityId);
        pppoEPlusEnable = global.getTopOltPPPoEPlusEnable();
        return SUCCESS;
    }

    /**
     * 跳转Option82配置页面
     * 
     * @return
     */
    public String showOltDhcpOption82Cfg() {
        return SUCCESS;
    }

    /**
     * 跳转PPPoE配置页面
     * 
     * @return
     */
    public String showOltDhcpPppoeCfg() {
        return SUCCESS;
    }

    /**
     * 获取单条模式配置数据
     * 
     * @return
     */
    public String loadOltDhcpGlobalCfg() {
        TopOltDhcpGlobalObjects global = oltDhcpBaseService.getOltDhcpBaseCfg(entityId);
        writeDataToAjax(global);
        return NONE;
    }

    /**
     * 修改全局DHCP开关
     * 
     * @return
     */
    public String modifyOltDhcpEnable() {
        oltDhcpBaseService.moidfyDhcpEnable(entityId, topOltDhcpEnable);
        return NONE;
    }

    /**
     * 修改Option82配置
     * 
     * @return
     */
    public String modifyOltDhcpOption82Cfg() {
        oltDhcpBaseService.modifyOption82Cfg(entityId, opt82Enable, opt82Policy, opt82Format);
        return NONE;
    }

    /**
     * 修改全局PPPoE开关
     * 
     * @return
     */
    public String modifyOltPppoeEnable() {
        oltDhcpBaseService.modifyPppoeEnable(entityId, pppoEPlusEnable);
        return NONE;
    }

    /**
     * 修改PPPoE配置
     * 
     * @return
     */
    public String modifyOltPppoeCfg() {
        oltDhcpBaseService.modifyPppoeCfg(entityId, pppoEPlusPolicy, pppoEPlusFormat);
        return NONE;
    }

    /**
     * 修改防静态IP开关
     * 
     * @return
     */
    public String modifySourceVerifyEnable() {
        oltDhcpBaseService.modifyOltDhcpSourceVerifyEnable(entityId, sourceVerifyEnable);
        return NONE;
    }

    /**
     * 刷新Option82配置信息
     * 
     * @return
     */
    public String refreshOltDhcpOption82Cfg() {
        oltDhcpBaseService.refrshOption82Cfg(entityId);
        return NONE;
    }

    /**
     * 同步DHCP数据
     * 
     * @return
     */
    public String refreshOltDhcpData() {
        oltDhcpBaseService.refreshDhcpData(entityId);
        return NONE;
    }

    /**
     * 同步PPPoE数据
     * 
     * @return
     */
    public String refreshOltPppoeData() {
        oltDhcpBaseService.refreshPppoeData(entityId);
        return NONE;
    }

    /**
     * 刷新PPPoE配置信息
     * 
     * @return
     */
    public String refreshOltPppoeCfg() {
        oltDhcpBaseService.refreshPppoeCfg(entityId);
        return NONE;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getTopOltDhcpEnable() {
        return topOltDhcpEnable;
    }

    public void setTopOltDhcpEnable(Integer topOltDhcpEnable) {
        this.topOltDhcpEnable = topOltDhcpEnable;
    }

    public Integer getOpt82Enable() {
        return opt82Enable;
    }

    public void setOpt82Enable(Integer opt82Enable) {
        this.opt82Enable = opt82Enable;
    }

    public Integer getOpt82Policy() {
        return opt82Policy;
    }

    public void setOpt82Policy(Integer opt82Policy) {
        this.opt82Policy = opt82Policy;
    }

    public String getOpt82Format() {
        return opt82Format;
    }

    public void setOpt82Format(String opt82Format) {
        this.opt82Format = opt82Format;
    }

    public Integer getSourceVerifyEnable() {
        return sourceVerifyEnable;
    }

    public void setSourceVerifyEnable(Integer sourceVerifyEnable) {
        this.sourceVerifyEnable = sourceVerifyEnable;
    }

    public Integer getPppoEPlusEnable() {
        return pppoEPlusEnable;
    }

    public void setPppoEPlusEnable(Integer pppoEPlusEnable) {
        this.pppoEPlusEnable = pppoEPlusEnable;
    }

    public Integer getPppoEPlusPolicy() {
        return pppoEPlusPolicy;
    }

    public void setPppoEPlusPolicy(Integer pppoEPlusPolicy) {
        this.pppoEPlusPolicy = pppoEPlusPolicy;
    }

    public String getPppoEPlusFormat() {
        return pppoEPlusFormat;
    }

    public void setPppoEPlusFormat(String pppoEPlusFormat) {
        this.pppoEPlusFormat = pppoEPlusFormat;
    }

}
