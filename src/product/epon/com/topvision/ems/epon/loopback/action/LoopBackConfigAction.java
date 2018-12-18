/***********************************************************************
 * $Id: LoopBackConfigAction.java,v1.0 2013-11-16 上午11:55:20 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.loopback.action;

import java.io.IOException;
import java.util.List;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.loopback.domain.LoopbackConfigTable;
import com.topvision.ems.epon.loopback.domain.LoopbackSubIpTable;
import com.topvision.ems.epon.loopback.service.LoopBackConfigService;
import com.topvision.framework.web.struts2.BaseAction;

/**
 * @author flack
 * @created @2013-11-16-上午11:55:20
 *
 */
@Controller("loopBackConfigAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LoopBackConfigAction extends BaseAction {
    private static final long serialVersionUID = -587005669121337145L;

    private Long entityId;
    private Integer interfaceIndex;
    private Integer subIpIndex;
    private String ipAddress;
    private String maskAddress;
    @Autowired
    private LoopBackConfigService loopBackConfigService;

    /**
     * 显示环回接口配置页面
     * @return
     */
    public String showLoopBackConfig() {
        return SUCCESS;
    }

    /**
     * 加载环回接口列表
     * @return
     * @throws IOException 
     */
    public String loadLBInterfaceList() throws IOException {
        List<LoopbackConfigTable> loopBackList = loopBackConfigService.getLBInterfaceList(entityId);
        JSONArray array = JSONArray.fromObject(loopBackList);
        array.write(response.getWriter());
        return NONE;
    }

    /**
     * 添加环回接口
     * @return
     */
    public String addLBInterface() {
        LoopbackConfigTable loopBack = new LoopbackConfigTable();
        loopBack.setEntityId(entityId);
        loopBack.setLoopbackIndex(interfaceIndex);
        loopBack.setLoopbackPriIpAddr(ipAddress);
        loopBack.setLoopbackPriMask(maskAddress);
        loopBackConfigService.addLoopBackInterface(loopBack);
        return NONE;
    }

    /**
     * 删除环回接口
     * @return
     */
    public String deleteLBInterface() {
        LoopbackConfigTable loopBack = new LoopbackConfigTable();
        loopBack.setEntityId(entityId);
        loopBack.setLoopbackIndex(interfaceIndex);
        loopBackConfigService.deleteLoopBackInterface(loopBack);
        return NONE;
    }

    /**
     * 修改环回接口
     * @return
     */
    public String modifyLoopBack() {
        LoopbackConfigTable loopBack = new LoopbackConfigTable();
        loopBack.setEntityId(entityId);
        loopBack.setLoopbackIndex(interfaceIndex);
        loopBack.setLoopbackPriIpAddr(ipAddress);
        loopBack.setLoopbackPriMask(maskAddress);
        loopBackConfigService.modifyLoopBackInterface(loopBack);
        return NONE;
    }

    /**
     * 从设备刷新环回接口
     * @return
     */
    public String refreshLoopBackInterface() {
        loopBackConfigService.refreshLoopBackConfig(entityId);
        return NONE;
    }

    /**
     * 显示环回接口子Ip配置页面
     * @return
     */
    public String showLBSubIpConfig() {
        return SUCCESS;
    }

    /**
     * 加载子Ip列表
     * @return
     * @throws IOException 
     */
    public String loadLBSubIpList() throws IOException {
        List<LoopbackSubIpTable> subIpList = loopBackConfigService.getLBSubIpList(entityId, interfaceIndex);
        JSONArray array = JSONArray.fromObject(subIpList);
        array.write(response.getWriter());
        return NONE;
    }

    /**
     * 添加环回接口子Ip
     * @return
     */
    public String addLBSubIp() {
        LoopbackSubIpTable subIpTable = new LoopbackSubIpTable();
        subIpTable.setEntityId(entityId);
        subIpTable.setLoopbackSubIpIndex(interfaceIndex);
        subIpTable.setLoopbackSubIpSeqIndex(subIpIndex);
        subIpTable.setLoopbackSubIpAddr(ipAddress);
        subIpTable.setLoopbackSubMask(maskAddress);
        loopBackConfigService.addLBSubIp(subIpTable);
        return NONE;
    }

    /**
     * 删除环回接口子Ip
     * @return
     */
    public String deleteLBSubIp() {
        LoopbackSubIpTable subIpTable = new LoopbackSubIpTable();
        subIpTable.setEntityId(entityId);
        subIpTable.setLoopbackSubIpIndex(interfaceIndex);
        subIpTable.setLoopbackSubIpSeqIndex(subIpIndex);
        loopBackConfigService.deleteLBSubIp(subIpTable);
        return NONE;
    }

    /**
     * 修改子IP
     * @return
     */
    public String modifyLBSubIp() {
        LoopbackSubIpTable subIpTable = new LoopbackSubIpTable();
        subIpTable.setEntityId(entityId);
        subIpTable.setLoopbackSubIpIndex(interfaceIndex);
        subIpTable.setLoopbackSubIpSeqIndex(subIpIndex);
        subIpTable.setLoopbackSubIpAddr(ipAddress);
        subIpTable.setLoopbackSubMask(maskAddress);
        loopBackConfigService.modifyLBSubIp(subIpTable);
        return NONE;
    }

    /**
     * 从设备刷新环回接口子IP
     * @return
     */
    public String refreshLBSubIp() {
        loopBackConfigService.refreshLoopBackSub(entityId);
        return NONE;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getMaskAddress() {
        return maskAddress;
    }

    public void setMaskAddress(String maskAddress) {
        this.maskAddress = maskAddress;
    }

    public Integer getInterfaceIndex() {
        return interfaceIndex;
    }

    public void setInterfaceIndex(Integer interfaceIndex) {
        this.interfaceIndex = interfaceIndex;
    }

    public Integer getSubIpIndex() {
        return subIpIndex;
    }

    public void setSubIpIndex(Integer subIpIndex) {
        this.subIpIndex = subIpIndex;
    }

}
