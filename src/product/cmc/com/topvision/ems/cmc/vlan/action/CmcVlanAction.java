/***********************************************************************
 * $Id: CmcVlanAction.java,v1.0 2013-4-23 下午02:45:41 $
 * 
 * @author: lzt
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.vlan.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cmc.facade.domain.CmcIpSubVlanCfgEntry;
import com.topvision.ems.cmc.facade.domain.CmcIpSubVlanScalarObject;
import com.topvision.ems.cmc.vlan.facade.domain.CmcVlanConfigEntry;
import com.topvision.ems.cmc.vlan.service.CmcVlanService;
import com.topvision.framework.web.struts2.BaseAction;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author lzt
 * @created @2013-4-23 下午02:45:41
 * 
 */
@Controller("cmcVlanAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CmcVlanAction extends BaseAction {

    private static final long serialVersionUID = -3663869265178242261L;

    @Resource(name = "cmcVlanService")
    private CmcVlanService cmcVlanService;
    private CmcIpSubVlanScalarObject cmcIpSubVlanScalar;
    private CmcIpSubVlanCfgEntry cmcIpSubVlanCfgEntry;
    private List<CmcIpSubVlanCfgEntry> cmcIpSubVlanCfgEntries;
    private List<CmcVlanConfigEntry> cmcVlanConfigEntries;

    private Long cmcId;
    private Integer topCcmtsIpSubVlanCfi;
    private String ipSubVlanTpid;
    private String topCmcIpSubVlanIp;
    private String topCmcIpSubVlanIpMask;
    private Integer topCmcIpSubVlanVlanId;
    private Integer topCmcIpSubVlanPri;

    private Integer topCcmtsVlanIndex;
    private Integer ipType;
    private String cmcVlanIp;
    private String cmcVlanMask;

    private Integer dhcpAlloc;
    private String option60;
    private String dhcpAllocIpAddr;
    private String dhcpAllocIpMask;

    private Integer priIpExist;
    private Integer secVidIndex;

    /**
     * 显示子网VLAN 配置界面
     * 
     * @return
     */
    public String showCcmtsIpSubVlan() {
        cmcIpSubVlanScalar = cmcVlanService.getCmcIpSubVlanScalarById(cmcId);
        return SUCCESS;
    }

    /**
     * 更新子网VLAN 全局配置
     * 
     * @return
     */
    public String modifyCmcIpSubVlanScalar() {
        cmcVlanService.modifyCmcIpSubVlanScalar(cmcId, topCcmtsIpSubVlanCfi, ipSubVlanTpid);
        return NONE;
    }

    /**
     * 更新子网VLAN IP列表
     * 
     * @return
     */
    public String loadCmcIpSubVlanCfg() throws IOException {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("sort", sort);
        queryMap.put("dir", dir);
        queryMap.put("cmcId", cmcId);
        cmcIpSubVlanCfgEntries = cmcVlanService.getCmcIpSubVlanCfgList(queryMap);
        writeDataToAjax(JSONArray.fromObject(cmcIpSubVlanCfgEntries));
        return NONE;
    }

    /**
     * 新增子网VLAN IP
     * 
     * @return
     */
    public String addCmcIpSubVlanCfg() {
        cmcVlanService.addCmcIpSubVlanCfg(cmcId, topCmcIpSubVlanIp, topCmcIpSubVlanIpMask, topCmcIpSubVlanVlanId,
                topCmcIpSubVlanPri);
        return NONE;
    }

    /**
     * 修改子网VLAN 优先级
     * 
     * @return
     */
    public String modifyCmcIpSubVlanCfg() {
        cmcVlanService.modifyCmcIpSubVlanCfg(cmcId, topCmcIpSubVlanIp, topCmcIpSubVlanIpMask, topCmcIpSubVlanPri);
        return NONE;
    }

    /**
     * 删除子网VLAN IP
     * 
     * @return
     */
    public String deleteCmcIpSubVlanCfg() {
        cmcVlanService.deleteCmcIpSubVlanCfg(cmcId, topCmcIpSubVlanIp, topCmcIpSubVlanIpMask);
        return NONE;
    }

    /**
     * 刷新子网VLAN 配置
     * 
     * @return
     */
    public String refreshCmcIpSubVlanCfg() {
        cmcVlanService.refreshCmcIpSubVlanCfg(cmcId);
        cmcIpSubVlanScalar = cmcVlanService.getCmcIpSubVlanScalarById(cmcId);
        writeDataToAjax(JSONObject.fromObject(cmcIpSubVlanScalar));
        return NONE;
    }

    /**
     * 展示设备VLAN 配置列表
     * 
     * @return
     */
    public String showCcmtsVlanJsp() {
        return SUCCESS;
    }

    /**
     * 加载设备VLAN 配置列表
     * 
     * @return
     */
    public String loadCmcVlanList() throws IOException {
        Map<String, Object> json = new HashMap<String, Object>();
        cmcVlanConfigEntries = cmcVlanService.getCmcVlanList(cmcId);
        json.put("totalProperty", cmcVlanConfigEntries.size());
        json.put("data", cmcVlanConfigEntries);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 显示新增VLAN 配置页面
     * 
     * @return
     */
    public String showAddCmcVlanJsp() {
        return SUCCESS;
    }

    /**
     * 新增VLAN 配置
     * 
     * @return
     * @throws IOException
     */
    public String addCmcVlan() throws IOException {
        CmcVlanConfigEntry cmcVlanEntry = cmcVlanService.getCmcVlanCfgById(cmcId, topCcmtsVlanIndex);
        if (cmcVlanEntry != null) {
            writeDataToAjax("vlanExist");
        } else {
            cmcVlanService.addCmcVlan(cmcId, topCcmtsVlanIndex);
            writeDataToAjax("success");
        }
        return NONE;
    }

    /**
     * 删除VLAN 配置
     * 
     * @return
     */
    public String deleteCmcVlan() {
        cmcVlanService.deleteCmcVlan(cmcId, topCcmtsVlanIndex);
        return NONE;
    }

    /**
     * 显示VLAN 虚接口IP配置界面
     * 
     * @return
     */
    public String showAddCmcVlanIp() {
        return SUCCESS;
    }

    /**
     * 新增VLAN 虚接口IP配置
     * 
     * @return
     * @throws IOException
     */
    public String addCmcVlanIp() throws IOException {
        if (!cmcVlanService.checkCmcVlanIpExist(cmcId, cmcVlanIp, cmcVlanMask)) {
            if (ipType == 1) {
                cmcVlanService.addCmcVlanPriIp(cmcId, topCcmtsVlanIndex, cmcVlanIp, cmcVlanMask);
            } else {
                cmcVlanService.addCmcVlanSecIp(cmcId, topCcmtsVlanIndex, cmcVlanIp, cmcVlanMask);
            }
        } else {
            writeDataToAjax("vlanIpExist");
        }
        return NONE;
    }

    public String showModifyCmcVlanIp() {
        return SUCCESS;
    }

    /**
     * 删除VLAN 虚接口IP配置
     * 
     * @return
     */
    public String deleteCmcVlanIp() {
        if (ipType == 1) {
            cmcVlanService.deleteCmcVlanPriIp(cmcId, topCcmtsVlanIndex);
            cmcVlanService.updateCmcVlanPriIpDhcpCfg(cmcId, topCcmtsVlanIndex, dhcpAlloc, option60);
        } else {
            cmcVlanService.deleteCmcVlanSecIp(cmcId, topCcmtsVlanIndex, secVidIndex);
        }
        return NONE;
    }

    /**
     * 更新VLAN 主IP Dhcp配置
     * 
     * @return
     * @throws IOException
     */
    public String modifyVlanPriIpDhcpCfg() throws IOException {

        if (dhcpAlloc == 1) {
            cmcVlanService.updateCmcVlanPriIpDhcpCfg(cmcId, topCcmtsVlanIndex, dhcpAlloc, option60);
            // cmcVlanService.deleteAllCmcVlanSubIpByVlan(cmcId, topCcmtsVlanIndex);
        } else {
            // CmcVlanConfigEntry cmcVlanConfigEntry = cmcVlanService.getCmcVlanCfgById(cmcId,
            // topCcmtsVlanIndex);
            if (!cmcVlanService.checkCmcVlanIpExist(cmcId, cmcVlanIp, cmcVlanMask)) {
                cmcVlanService.updateCmcVlanPriIpDhcpCfg(cmcId, topCcmtsVlanIndex, dhcpAlloc, null);
                cmcVlanService.modifyCmcVlanPriIp(cmcId, topCcmtsVlanIndex, cmcVlanIp, cmcVlanMask);
            } else {
                writeDataToAjax("vlanIpExist");
            }
        }
        return NONE;
    }

    /**
     * 更新VLAN 从IP 配置
     * 
     * @return
     * @throws IOException
     */
    public String modifyVlanSubIp() throws IOException {
        if (!cmcVlanService.checkCmcVlanIpExist(cmcId, cmcVlanIp, cmcVlanMask)) {
            cmcVlanService.modifyCmcVlanSecIp(cmcId, topCcmtsVlanIndex, secVidIndex, cmcVlanIp, cmcVlanMask);
        } else {
            writeDataToAjax("vlanIpExist");
        }
        return NONE;
    }

    /**
     * 刷新VLAN 虚接口IP配置
     * 
     * @return
     */
    public String refreshCmcVlan() {
        cmcVlanService.refreshCmcVlanConfigFromDevice(cmcId);
        return NONE;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public CmcIpSubVlanScalarObject getCmcIpSubVlanScalar() {
        return cmcIpSubVlanScalar;
    }

    public void setCmcIpSubVlanScalar(CmcIpSubVlanScalarObject cmcIpSubVlanScalar) {
        this.cmcIpSubVlanScalar = cmcIpSubVlanScalar;
    }

    public CmcVlanService getCmcVlanService() {
        return cmcVlanService;
    }

    public void setCmcVlanService(CmcVlanService cmcVlanService) {
        this.cmcVlanService = cmcVlanService;
    }

    public Integer getTopCcmtsIpSubVlanCfi() {
        return topCcmtsIpSubVlanCfi;
    }

    public void setTopCcmtsIpSubVlanCfi(Integer topCcmtsIpSubVlanCfi) {
        this.topCcmtsIpSubVlanCfi = topCcmtsIpSubVlanCfi;
    }

    public String getIpSubVlanTpid() {
        return ipSubVlanTpid;
    }

    public void setIpSubVlanTpid(String ipSubVlanTpid) {
        this.ipSubVlanTpid = ipSubVlanTpid;
    }

    public CmcIpSubVlanCfgEntry getCmcIpSubVlanCfgEntry() {
        return cmcIpSubVlanCfgEntry;
    }

    public void setCmcIpSubVlanCfgEntry(CmcIpSubVlanCfgEntry cmcIpSubVlanCfgEntry) {
        this.cmcIpSubVlanCfgEntry = cmcIpSubVlanCfgEntry;
    }

    public String getTopCmcIpSubVlanIp() {
        return topCmcIpSubVlanIp;
    }

    public void setTopCmcIpSubVlanIp(String topCmcIpSubVlanIp) {
        this.topCmcIpSubVlanIp = topCmcIpSubVlanIp;
    }

    public String getTopCmcIpSubVlanIpMask() {
        return topCmcIpSubVlanIpMask;
    }

    public void setTopCmcIpSubVlanIpMask(String topCmcIpSubVlanIpMask) {
        this.topCmcIpSubVlanIpMask = topCmcIpSubVlanIpMask;
    }

    public Integer getTopCmcIpSubVlanVlanId() {
        return topCmcIpSubVlanVlanId;
    }

    public void setTopCmcIpSubVlanVlanId(Integer topCmcIpSubVlanVlanId) {
        this.topCmcIpSubVlanVlanId = topCmcIpSubVlanVlanId;
    }

    public Integer getTopCmcIpSubVlanPri() {
        return topCmcIpSubVlanPri;
    }

    public void setTopCmcIpSubVlanPri(Integer topCmcIpSubVlanPri) {
        this.topCmcIpSubVlanPri = topCmcIpSubVlanPri;
    }

    public List<CmcIpSubVlanCfgEntry> getCmcIpSubVlanCfgEntries() {
        return cmcIpSubVlanCfgEntries;
    }

    public void setCmcIpSubVlanCfgEntries(List<CmcIpSubVlanCfgEntry> cmcIpSubVlanCfgEntries) {
        this.cmcIpSubVlanCfgEntries = cmcIpSubVlanCfgEntries;
    }

    public List<CmcVlanConfigEntry> getCmcVlanConfigEntries() {
        return cmcVlanConfigEntries;
    }

    public void setCmcVlanConfigEntries(List<CmcVlanConfigEntry> cmcVlanConfigEntries) {
        this.cmcVlanConfigEntries = cmcVlanConfigEntries;
    }

    public Integer getTopCcmtsVlanIndex() {
        return topCcmtsVlanIndex;
    }

    public void setTopCcmtsVlanIndex(Integer topCcmtsVlanIndex) {
        this.topCcmtsVlanIndex = topCcmtsVlanIndex;
    }

    public Integer getIpType() {
        return ipType;
    }

    public void setIpType(Integer ipType) {
        this.ipType = ipType;
    }

    public String getCmcVlanIp() {
        return cmcVlanIp;
    }

    public void setCmcVlanIp(String cmcVlanIp) {
        this.cmcVlanIp = cmcVlanIp;
    }

    public String getCmcVlanMask() {
        return cmcVlanMask;
    }

    public void setCmcVlanMask(String cmcVlanMask) {
        this.cmcVlanMask = cmcVlanMask;
    }

    public Integer getDhcpAlloc() {
        return dhcpAlloc;
    }

    public void setDhcpAlloc(Integer dhcpAlloc) {
        this.dhcpAlloc = dhcpAlloc;
    }

    public String getOption60() {
        return option60;
    }

    public void setOption60(String option60) {
        this.option60 = option60;
    }

    public String getDhcpAllocIpAddr() {
        return dhcpAllocIpAddr;
    }

    public void setDhcpAllocIpAddr(String dhcpAllocIpAddr) {
        this.dhcpAllocIpAddr = dhcpAllocIpAddr;
    }

    public String getDhcpAllocIpMask() {
        return dhcpAllocIpMask;
    }

    public void setDhcpAllocIpMask(String dhcpAllocIpMask) {
        this.dhcpAllocIpMask = dhcpAllocIpMask;
    }

    public Integer getPriIpExist() {
        return priIpExist;
    }

    public void setPriIpExist(Integer priIpExist) {
        this.priIpExist = priIpExist;
    }

    public Integer getSecVidIndex() {
        return secVidIndex;
    }

    public void setSecVidIndex(Integer secVidIndex) {
        this.secVidIndex = secVidIndex;
    }

}
