/***********************************************************************
 * CmWebProxyAction.java,v1.0 17-4-24 下午3:16 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.webproxy.action;

import com.topvision.ems.cmc.webproxy.domain.CmWebProxyModule;
import com.topvision.ems.cmc.webproxy.domain.CmWebStatus;
import com.topvision.ems.cmc.webproxy.service.CmWebProxyConfigService;
import com.topvision.ems.cmc.webproxy.service.CmWebProxyHeartbeatService;
import com.topvision.framework.web.struts2.BaseAction;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * @author jay
 * @created 17-4-24 下午3:16
 */
@Controller("cmWebProxyAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CmWebProxyAction extends BaseAction {
    @Autowired
    private CmWebProxyConfigService cmWebProxyConfigService;
    @Autowired
    private CmWebProxyHeartbeatService cmWebProxyHeartbeatService;
    private Integer cmWebJumpModule = CmWebProxyModule.DIRECTJUMP;
    private Long heartbeatId;
    private Long cmId;
    private Long cmcId;
    private String natIp;
    private String manageIp;
    private Integer proxyPort;

    /**
     * 显示CM单机WEB跳转方式设置
     *
     * @return
     */
    public String showCmWebJumpConfig() {
        cmWebJumpModule = cmWebProxyConfigService.loadCmWebJumpModule();
        natIp = cmWebProxyConfigService.loadNatServerIp();
        return SUCCESS;
    }

    /**
     * 设置CM单机WEB跳转方式
     *
     * @return
     */
    public String configCmWebProxy() {
        String result = "";
        try {
            cmWebProxyConfigService.configCmWebProxy(cmWebJumpModule,natIp);
            result = "success";
        } catch (Exception e) {
            logger.debug("configCmWebProxy errer", e);
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * 显示Cm单机web代理页面
     * @return
     */
    public String showCmWebProxy() {
        return SUCCESS;
    }

    /**
     * 添加一个心跳
     * @return
     */
    public String addHeartbeat() {
        CmWebStatus cmWebStatus = cmWebProxyHeartbeatService.addHeartbeat(cmId);
        writeDataToAjax(JSONObject.fromObject(cmWebStatus));
        return NONE;
    }

    /**
     * 获取一个cmc的代理
     * @return
     */
    public String pickCCProxyByCmcId() {
        CmWebStatus cmWebStatus = cmWebProxyHeartbeatService.pickCCProxyByCmcId(heartbeatId, cmId);
        writeDataToAjax(JSONObject.fromObject(cmWebStatus));
        return NONE;
    }

    /**
     * 获取一个cm的端口转换
     * @return
     */
    public String pickPortByCmId() {
    	CmWebStatus cmWebStatus;
    	try {
    		cmWebStatus = cmWebProxyHeartbeatService.pickPortByCmId(heartbeatId, cmId, manageIp, proxyPort);
    	} catch (Exception e) {
    		logger.debug("",e);
    		cmWebStatus = new CmWebStatus();
    	}
        writeDataToAjax(JSONObject.fromObject(cmWebStatus));
        return NONE;
    }

    /**
     * 获取CM代理完整地址
     *
     * @return
     */
    public String loadCmWebProxyUrl() {
        CmWebStatus cmWebStatus = cmWebProxyHeartbeatService.selectPortByCmId(heartbeatId,cmId);
        String natIp = cmWebProxyConfigService.loadNatServerIp();
        String url = "http://" + natIp + ":" + cmWebStatus.getNm3000Port();
        cmWebStatus.setNatIp(natIp);
        cmWebStatus.setUrl(url);
        writeDataToAjax(JSONObject.fromObject(cmWebStatus));
        return NONE;
    }

    /**
     * 获取转换端口创建状态
     *
     * @return
     */
    public String loadCmPortMapByCmId() {
        CmWebStatus cmWebStatus = cmWebProxyHeartbeatService.selectPortByCmId(heartbeatId,cmId);
        writeDataToAjax(JSONObject.fromObject(cmWebStatus));
        return NONE;
    }

    /**
     * 获取cmc代理创建状态
     *
     * @return
     */
    public String loadCCProxyByCmcId() {
        CmWebStatus cmWebStatus = cmWebProxyHeartbeatService.selectCCProxyByCmcId(heartbeatId,cmcId);
        writeDataToAjax(JSONObject.fromObject(cmWebStatus));
        return NONE;
    }

    /**
     * 释放某个客户端的心跳
     *
     * @return
     */
    public String releaseHeartbeat() {
        cmWebProxyHeartbeatService.deleteHeartbeat(heartbeatId);
        return NONE;
    }

    /**
     * 心跳
     *
     * @return
     */
    public String heartbeat() {
        CmWebStatus cmWebStatus = cmWebProxyHeartbeatService.heartbeat(heartbeatId);
        writeDataToAjax(JSONObject.fromObject(cmWebStatus));
        return NONE;
    }

    public Long getHeartbeatId() {
        return heartbeatId;
    }

    public void setHeartbeatId(Long heartbeatId) {
        this.heartbeatId = heartbeatId;
    }

    public Integer getCmWebJumpModule() {
        return cmWebJumpModule;
    }

    public void setCmWebJumpModule(Integer cmWebJumpModule) {
        this.cmWebJumpModule = cmWebJumpModule;
    }

    public Long getCmId() {
        return cmId;
    }

    public void setCmId(Long cmId) {
        this.cmId = cmId;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public String getManageIp() {
        return manageIp;
    }

    public void setManageIp(String manageIp) {
        this.manageIp = manageIp;
    }

    public Integer getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(Integer proxyPort) {
        this.proxyPort = proxyPort;
    }

    public String getNatIp() {
        return natIp;
    }

    public void setNatIp(String natIp) {
        this.natIp = natIp;
    }
}
