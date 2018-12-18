/***********************************************************************
 * $Id: LogicInterfaceAction.java,v1.0 2016年10月14日 上午10:01:26 $
 * 
 * @author: lzt
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.logicinterface.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.logicinterface.domain.InterfaceIpV4Config;
import com.topvision.ems.epon.logicinterface.domain.LogicInterface;
import com.topvision.ems.epon.logicinterface.service.LogicInterfaceService;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.util.CurrentRequest;

/**
 * @author lzt
 * @created @2016年10月14日-上午10:01:26
 *
 */
@Controller("logicInterfaceAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LogicInterfaceAction extends BaseAction{
    private static final long serialVersionUID = -7793720615302522885L;

    @Autowired
    private LogicInterfaceService logicInterfaceService;

    private Long entityId;
    private Integer interfaceIndex;
    private Integer interfaceId;
    private Integer interfaceType;
    private String interfaceDesc;
    private Integer interfaceAdminStatus;
    private Integer interfaceOperateStatus;
    private String ipV4Addr;
    private String ipV4NetMask;
    private Integer ipV4AddrType;
    private Integer ipV4ConfigIndex;
    private String name;
    private List<Integer> interfaceIds;
    private LogicInterface logicInterface;

    public String showLogicInterfaceView() {
        return SUCCESS;
    }

    public String loadLogicInterfaceList() throws IOException {
        UserContext uc = CurrentRequest.getCurrentUser();
        String displayRule = uc.getMacDisplayStyle();
        Map<String, Object> param = new HashMap<String, Object>();
        JSONObject json = new JSONObject();
        param.put("entityId", entityId);
        param.put("name", name);
        if (interfaceAdminStatus != null && interfaceAdminStatus != -1) {
            param.put("interfaceAdminStatus", interfaceAdminStatus);
        }
        if (interfaceOperateStatus != null && interfaceOperateStatus != -1) {
            param.put("interfaceOperateStatus", interfaceOperateStatus);
        }
        param.put("start", start);
        param.put("limit", limit);
        param.put("interfaceType", interfaceType);
        param.put("sortName", this.getSort());
        param.put("sortDir", this.getDir());
        List<LogicInterface> logicInterfaceList = logicInterfaceService.getOltLogicInterfaceByType(param);
        for (LogicInterface logicInterface : logicInterfaceList) {
            String formatedMac = MacUtils.convertMacToDisplayFormat(logicInterface.getInterfaceMac(), displayRule);
            logicInterface.setInterfaceMac(formatedMac);
        }
        int totalCount = logicInterfaceService.getOltLogicInterfaceByTypeCount(param);
        json.put("rowCount", totalCount);
        json.put("data", logicInterfaceList);
        writeDataToAjax(json);
        return NONE;
    }

    public String addLogicInterfaceView() {
        return SUCCESS;
    }

    public String addLogicInterface() {
        LogicInterface logicInterface = new LogicInterface();
        logicInterface.setEntityId(entityId);
        logicInterface.setInterfaceType(interfaceType);
        logicInterface.setInterfaceId(interfaceId);
        logicInterface.setInterfaceAdminStatus(interfaceAdminStatus);
        if ("".equals(interfaceDesc)) {
            interfaceDesc = null;
        }
        logicInterface.setInterfaceDesc(interfaceDesc);
        logicInterfaceService.addLogicInterface(logicInterface);
        return NONE;
    }

    public String deleteLogicInterface() {
        logicInterfaceService.deleteLogicInterface(entityId, interfaceType, interfaceId);
        return NONE;
    }

    public String deleteLogicInterfaceList() {
        for (int i = 0; i < interfaceIds.size(); i++) {
            logicInterfaceService.deleteLogicInterface(entityId, interfaceType, interfaceIds.get(i));
        }
        return NONE;
    }

    public String editLogicInterfaceView() {
        logicInterface = logicInterfaceService.getOltLogicInterface(entityId, interfaceType, interfaceId);
        return SUCCESS;
    }

    public String modifyLogicInterface() {
        LogicInterface logicInterface = new LogicInterface();
        logicInterface.setEntityId(entityId);
        logicInterface.setInterfaceType(interfaceType);
        logicInterface.setInterfaceId(interfaceId);
        if (interfaceType != LogicInterface.LOOPBACK_TYPE) {
            logicInterface.setInterfaceAdminStatus(interfaceAdminStatus);
        }
        logicInterface.setInterfaceDesc(interfaceDesc);
        logicInterfaceService.updateLogicInterface(logicInterface);
        return NONE;
    }

    public String refreshLogicInterface() {
        logicInterfaceService.refreshLogicInterface(entityId);
        return NONE;
    }

    public String showInterfaceIpV4ConfigView() {
        UserContext uc = CurrentRequest.getCurrentUser();
        String displayRule = uc.getMacDisplayStyle();
        logicInterface = logicInterfaceService.getOltLogicInterface(entityId, interfaceType, interfaceId);
        if (logicInterface != null && logicInterface.getInterfaceMac() != null) {
            logicInterface.setInterfaceMac(MacUtils.convertMacToDisplayFormat(logicInterface.getInterfaceMac(),
                    displayRule));
        }
        return SUCCESS;
    }

    public String loadInterfaceIpV4Config() throws IOException {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("entityId", entityId);
        param.put("ipV4ConfigIndex", ipV4ConfigIndex);
        param.put("sortName", this.getSort());
        param.put("sortDir", this.getDir());
        JSONObject json = new JSONObject();
        List<InterfaceIpV4Config> interfaceIpV4List = logicInterfaceService.getInterfaceIpList(param);
        json.put("data", interfaceIpV4List);
        writeDataToAjax(json);
        return NONE;
    }

    public String addInterfaceIpV4View() {
        return SUCCESS;
    }

    public String addInterfaceIpV4() {
        InterfaceIpV4Config interfaceIpV4 = new InterfaceIpV4Config();
        interfaceIpV4.setEntityId(entityId);
        interfaceIpV4.setIpV4ConfigIndex(ipV4ConfigIndex);
        interfaceIpV4.setIpV4Addr(ipV4Addr);
        interfaceIpV4.setIpV4NetMask(ipV4NetMask);
        interfaceIpV4.setIpV4AddrType(ipV4AddrType);
        logicInterfaceService.addInterfaceIpV4Config(interfaceIpV4);
        return NONE;
    }

    public String deleteInterfaceIpV4() {
        logicInterfaceService.deleteInterfaceIpV4Config(entityId, ipV4ConfigIndex, ipV4Addr);
        return NONE;
    }

    public String refreshInterfaceIpV4Config() {
        logicInterfaceService.refreshLogicInterfaceIpConfig(entityId);
        return NONE;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getInterfaceIndex() {
        return interfaceIndex;
    }

    public void setInterfaceIndex(Integer interfaceIndex) {
        this.interfaceIndex = interfaceIndex;
    }

    public Integer getInterfaceId() {
        return interfaceId;
    }

    public void setInterfaceId(Integer interfaceId) {
        this.interfaceId = interfaceId;
    }

    public Integer getInterfaceType() {
        return interfaceType;
    }

    public void setInterfaceType(Integer interfaceType) {
        this.interfaceType = interfaceType;
    }

    public String getIpV4Addr() {
        return ipV4Addr;
    }

    public void setIpV4Addr(String ipV4Addr) {
        this.ipV4Addr = ipV4Addr;
    }

    public String getIpV4NetMask() {
        return ipV4NetMask;
    }

    public void setIpV4NetMask(String ipV4NetMask) {
        this.ipV4NetMask = ipV4NetMask;
    }

    public String getInterfaceDesc() {
        return interfaceDesc;
    }

    public void setInterfaceDesc(String interfaceDesc) {
        this.interfaceDesc = interfaceDesc;
    }

    public Integer getInterfaceAdminStatus() {
        return interfaceAdminStatus;
    }

    public void setInterfaceAdminStatus(Integer interfaceAdminStatus) {
        this.interfaceAdminStatus = interfaceAdminStatus;
    }

    public Integer getIpV4AddrType() {
        return ipV4AddrType;
    }

    public void setIpV4AddrType(Integer ipV4AddrType) {
        this.ipV4AddrType = ipV4AddrType;
    }

    public Integer getIpV4ConfigIndex() {
        return ipV4ConfigIndex;
    }

    public void setIpV4ConfigIndex(Integer ipV4ConfigIndex) {
        this.ipV4ConfigIndex = ipV4ConfigIndex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getInterfaceOperateStatus() {
        return interfaceOperateStatus;
    }

    public void setInterfaceOperateStatus(Integer interfaceOperateStatus) {
        this.interfaceOperateStatus = interfaceOperateStatus;
    }

    public LogicInterface getLogicInterface() {
        return logicInterface;
    }

    public void setLogicInterface(LogicInterface logicInterface) {
        this.logicInterface = logicInterface;
    }

    public List<Integer> getInterfaceIds() {
        return interfaceIds;
    }

    public void setInterfaceIds(List<Integer> interfaceIds) {
        this.interfaceIds = interfaceIds;
    }

}
