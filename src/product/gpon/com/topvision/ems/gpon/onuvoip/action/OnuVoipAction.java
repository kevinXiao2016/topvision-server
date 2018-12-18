/***********************************************************************
 * $Id: OnuVoipAction.java,v1.0 2017年5月4日 上午11:20:29 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.onuvoip.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.exception.SetValueConflictException;
import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.onu.service.OnuService;
import com.topvision.ems.gpon.onuvoip.domain.TopGponOnuPots;
import com.topvision.ems.gpon.onuvoip.domain.TopSIPPstnUser;
import com.topvision.ems.gpon.onuvoip.domain.TopVoIPLineStatus;
import com.topvision.ems.gpon.onuvoip.service.OnuVoipService;
import com.topvision.ems.gpon.profile.facade.domain.TopDigitMapProfInfo;
import com.topvision.ems.gpon.profile.facade.domain.TopSIPSrvProfInfo;
import com.topvision.ems.gpon.profile.service.TopDigitMapProfService;
import com.topvision.ems.gpon.profile.service.TopSIPSrvProfService;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.ResourceManager;

import net.sf.json.JSONObject;

/**
 * @author haojie
 * @created @2017年5月4日-上午11:20:29
 *
 */
@Controller("onuVoipAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OnuVoipAction extends BaseAction {
    private static final long serialVersionUID = -6505987781833957081L;
    @Autowired
    private OnuVoipService onuVoipService;
    @Autowired
    private OnuService onuService;
    @Autowired
    private TopSIPSrvProfService topSIPSrvProfService;
    @Autowired
    private TopDigitMapProfService digitMapProfService;

    private TopSIPPstnUser topSIPPstnUser;
    private JSONObject topSIPPstnUserJson;
    private Long onuId;
    private Long entityId;
    private String topSIPPstnUserTelno;
    private String topSIPPstnUserName;
    private String topSIPPstnUserPwd;

    private Integer topSIPPstnUserPotsIdx;// POTS端口编号
    private Integer topSIPPstnUserSipsrvId;
    private Integer topSIPPstnUserDigitmapId;
    private Integer topSIPPstnUserForwardType;
    private String topSIPPstnUserTransferNum;
    private Integer topSIPPstnUserForwardTime;
    private Integer potsAdminStatus;// POTS口使能(管理状态)

    private TopVoIPLineStatus topVoIPLineStatus;
    private JSONObject topVoIPLineStatusJson;

    /**
     * 打开用户配置页面
     * 
     * @return
     */
    public String showSIPPstnUser() {
        return SUCCESS;
    }

    /**
     * 刷新SIP用户配置信息
     * 
     * @return
     */
    public String refreshSipPstnUser() {
        onuVoipService.refreshTopSIPPstnUserByOnuId(onuId);
        return NONE;
    }

    /**
     * 获取线路状态信息
     * 
     * @return
     */
    public String loadVoIPLineStatus() {
        topVoIPLineStatus = onuVoipService.getTopVoIPLineStatus(onuId);
        topVoIPLineStatusJson = JSONObject.fromObject(topVoIPLineStatus);
        writeDataToAjax(topVoIPLineStatusJson);
        return NONE;
    }

    /**
     * 刷新VOIP线路状态信息
     * 
     * @return
     */
    public String refreshVoIPLineStatus() {
        onuVoipService.refreshTopVoIPLineStatusByOnuId(onuId);
        return NONE;
    }

    /**
     * 获取POST口列表
     * 
     * @return
     */
    public String loadGponOnuPotsList() {
        List<TopGponOnuPots> topGponOnuPots = onuVoipService.loadGponOnuPotsList(onuId);
        writeDataToAjax(topGponOnuPots);
        return NONE;
    }

    /**
     * 从设备获取POTS数据
     * 
     * @return
     */
    public String refreshGponOnuPotsInfo() {
        onuVoipService.refreshGponOnuPotsInfo(onuId);
        return NONE;
    }

    /**
     * 设置POTS口使能（管理状态）
     * 
     * @return
     */
    public String setOnuPotsAdminStatus() {
        Map<String, String> message = new HashMap<String, String>();
        try {
            onuVoipService.setOnuPotsAdminStatus(onuId, topSIPPstnUserPotsIdx, potsAdminStatus);
        } catch (SetValueConflictException svce) {
            message.put("message", getString(svce.getMessage(), "gpon"));
            //operationResult = OperationLog.FAILURE;
            logger.debug("reset pots error:{}", svce);
        }
        writeDataToAjax(JSONObject.fromObject(message));
        return NONE;
    }

    /**
     * 打开POTS 配置窗口
     * 
     * @return
     */
    public String showPotsConfigView() {
        // 1、获取sip用户信息
        HashMap<String, Object> map = new HashMap<>();
        map.put("onuId", onuId);
        map.put("topSIPPstnUserPotsIdx", topSIPPstnUserPotsIdx);
        topSIPPstnUser = onuVoipService.loadGponOnuPotsConfig(map);
        // 2、获取已存在业务数据模板
        List<TopSIPSrvProfInfo> topSIPSrvProfInfos = topSIPSrvProfService.loadTopSIPSrvProfInfoList(entityId);
        // 3、获取已存在数图模板
        List<TopDigitMapProfInfo> digitMapProfInfos = digitMapProfService.loadTopDigitMapProfInfoList(entityId);
        // 4、封装topSIPPstnUser
        topSIPPstnUser.setTopSIPSrvProfInfos(topSIPSrvProfInfos);
        topSIPPstnUser.setDigitMapProfInfos(digitMapProfInfos);
        return SUCCESS;
    }

    /**
     * 修改POTS口用户配置
     * 
     * @return
     */
    public String modifyGponOnuPotsConfig() {
        OltOnuAttribute onu = onuService.getOnuAttribute(onuId);
        TopSIPPstnUser user = new TopSIPPstnUser();
        user.setEntityId(onu.getEntityId());
        user.setOnuId(onuId);
        user.setOnuIndex(onu.getOnuIndex());
        user.setTopSIPPstnUserPotsIdx(topSIPPstnUserPotsIdx);
        user.setTopSIPPstnUserTelno(topSIPPstnUserTelno);
        user.setTopSIPPstnUserName(topSIPPstnUserName);
        user.setTopSIPPstnUserPwd(topSIPPstnUserPwd);
        user.setTopSIPPstnUserSipsrvId(topSIPPstnUserSipsrvId);
        user.setTopSIPPstnUserDigitmapId(topSIPPstnUserDigitmapId);
        user.setTopSIPPstnUserForwardType(topSIPPstnUserForwardType);
        user.setTopSIPPstnUserTransferNum(topSIPPstnUserTransferNum);
        user.setTopSIPPstnUserForwardTime(topSIPPstnUserForwardTime);
        onuVoipService.modifyTopSIPPstnUser(user);
        return NONE;
    }

    protected String getString(String key, String module) {
        module = String.format("com.topvision.ems.%s.resources", module);
        try {
            ResourceManager resourceManager = ResourceManager.getResourceManager(module);
            return resourceManager.getNotNullString(key);
        } catch (Exception e) {
            return key;
        }
    }

    public TopSIPPstnUser getTopSIPPstnUser() {
        return topSIPPstnUser;
    }

    public void setTopSIPPstnUser(TopSIPPstnUser topSIPPstnUser) {
        this.topSIPPstnUser = topSIPPstnUser;
    }

    public JSONObject getTopSIPPstnUserJson() {
        return topSIPPstnUserJson;
    }

    public void setTopSIPPstnUserJson(JSONObject topSIPPstnUserJson) {
        this.topSIPPstnUserJson = topSIPPstnUserJson;
    }

    public Long getOnuId() {
        return onuId;
    }

    public void setOnuId(Long onuId) {
        this.onuId = onuId;
    }

    public String getTopSIPPstnUserTelno() {
        return topSIPPstnUserTelno;
    }

    public void setTopSIPPstnUserTelno(String topSIPPstnUserTelno) {
        this.topSIPPstnUserTelno = topSIPPstnUserTelno;
    }

    public String getTopSIPPstnUserName() {
        return topSIPPstnUserName;
    }

    public void setTopSIPPstnUserName(String topSIPPstnUserName) {
        this.topSIPPstnUserName = topSIPPstnUserName;
    }

    public String getTopSIPPstnUserPwd() {
        return topSIPPstnUserPwd;
    }

    public void setTopSIPPstnUserPwd(String topSIPPstnUserPwd) {
        this.topSIPPstnUserPwd = topSIPPstnUserPwd;
    }

    public TopVoIPLineStatus getTopVoIPLineStatus() {
        return topVoIPLineStatus;
    }

    public void setTopVoIPLineStatus(TopVoIPLineStatus topVoIPLineStatus) {
        this.topVoIPLineStatus = topVoIPLineStatus;
    }

    public JSONObject getTopVoIPLineStatusJson() {
        return topVoIPLineStatusJson;
    }

    public void setTopVoIPLineStatusJson(JSONObject topVoIPLineStatusJson) {
        this.topVoIPLineStatusJson = topVoIPLineStatusJson;
    }

    public Integer getTopSIPPstnUserPotsIdx() {
        return topSIPPstnUserPotsIdx;
    }

    public void setTopSIPPstnUserPotsIdx(Integer topSIPPstnUserPotsIdx) {
        this.topSIPPstnUserPotsIdx = topSIPPstnUserPotsIdx;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getTopSIPPstnUserSipsrvId() {
        return topSIPPstnUserSipsrvId;
    }

    public void setTopSIPPstnUserSipsrvId(Integer topSIPPstnUserSipsrvId) {
        this.topSIPPstnUserSipsrvId = topSIPPstnUserSipsrvId;
    }

    public Integer getTopSIPPstnUserDigitmapId() {
        return topSIPPstnUserDigitmapId;
    }

    public void setTopSIPPstnUserDigitmapId(Integer topSIPPstnUserDigitmapId) {
        this.topSIPPstnUserDigitmapId = topSIPPstnUserDigitmapId;
    }

    public Integer getTopSIPPstnUserForwardType() {
        return topSIPPstnUserForwardType;
    }

    public void setTopSIPPstnUserForwardType(Integer topSIPPstnUserForwardType) {
        this.topSIPPstnUserForwardType = topSIPPstnUserForwardType;
    }

    public Integer getTopSIPPstnUserForwardTime() {
        return topSIPPstnUserForwardTime;
    }

    public void setTopSIPPstnUserForwardTime(Integer topSIPPstnUserForwardTime) {
        this.topSIPPstnUserForwardTime = topSIPPstnUserForwardTime;
    }

    public String getTopSIPPstnUserTransferNum() {
        return topSIPPstnUserTransferNum;
    }

    public void setTopSIPPstnUserTransferNum(String topSIPPstnUserTransferNum) {
        this.topSIPPstnUserTransferNum = topSIPPstnUserTransferNum;
    }

    public Integer getPotsAdminStatus() {
        return potsAdminStatus;
    }

    public void setPotsAdminStatus(Integer potsAdminStatus) {
        this.potsAdminStatus = potsAdminStatus;
    }

}
