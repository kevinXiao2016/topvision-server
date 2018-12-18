/***********************************************************************
 * $Id: GponOnuAction.java,v1.0 2016年12月20日 下午1:20:02 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.onu.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.onu.service.OnuService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.gpon.onu.domain.GponOnuCapability;
import com.topvision.ems.gpon.onu.domain.GponOnuIpHost;
import com.topvision.ems.gpon.onu.domain.GponUniAttribute;
import com.topvision.ems.gpon.onu.service.GponOnuService;
import com.topvision.ems.gpon.onuauth.GponConstant;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.util.CurrentRequest;

import net.sf.json.JSONObject;

/**
 * @author lizongtian
 * @created @2016年12月20日-下午1:20:02
 *
 */
@Controller("gponOnuAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GponOnuAction extends BaseAction {

    private static final long serialVersionUID = -4159159489930663086L;

    @Autowired
    private GponOnuService gponOnuService;
    @Autowired
    private EntityService entityService;
    @Autowired
    private OnuService onuService;

    private Long entityId;
    private Long onuId;
    private Long onuIndex;
    private Integer onuIpHostIndex;
    private Integer onuHostMode;
    private String hostIpAddr;
    private String hostMask;
    private String hostGateway;
    private String priDns;
    private String secondDns;
    private Integer hostVlanPri;
    private Integer hostVlanId;
    private GponOnuIpHost onuIpHost;
    private List<Integer> usedHostIpIndex;
    private String selectValue;
    private List<Integer> onuIpHostIndexList;
    private Long uniId;
    private Integer uniNo;
    private Integer gponOnuUniPri;
    private Integer gponOnuUniPvid;

    private GponOnuCapability gponOnuCapability;
    private Entity onu;
    private OltOnuAttribute onuAttribute;
    private GponUniAttribute gponUniAttribute;

    public String loadGponOnuIpHostList() throws IOException {
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("onuId", onuId);
        paramMap.put("onuHostMode", onuHostMode);
        paramMap.put("selectValue", selectValue);
        paramMap.put("hostVlanPri", hostVlanPri);
        paramMap.put("hostVlanId", hostVlanId);
        paramMap.put("sort", sort);
        List<GponOnuIpHost> ipHostList = gponOnuService.loadGponOnuIpHost(paramMap);
        JSONObject json = new JSONObject();
        //需要在展示前格式化MAC地址
        UserContext uc = CurrentRequest.getCurrentUser();
        String displayRule = uc.getMacDisplayStyle();
        for (GponOnuIpHost onuIpHost : ipHostList) {
            String formatedMac = MacUtils.convertMacToDisplayFormat(onuIpHost.getOnuIpHostMacAddress(), displayRule);
            onuIpHost.setOnuIpHostMacAddress(formatedMac);
        }
        json.put("data", ipHostList);
        json.write(response.getWriter());
        return NONE;
    }

    public String showAddOnuIpHostView() {
        usedHostIpIndex = gponOnuService.usedHostIpIndex(onuId);
        return SUCCESS;
    }

    public String addOnuIpHost() {
        GponOnuIpHost onuIpHost = new GponOnuIpHost();
        onuIpHost.setEntityId(entityId);
        onuIpHost.setOnuId(onuId);
        onuIpHost.setOnuIndex(onuIndex);
        onuIpHost.setOnuIpHostIndex(onuIpHostIndex);
        onuIpHost.setOnuIpHostAddressConfigMode(onuHostMode);
        onuIpHost.setOnuIpHostAddress(hostIpAddr);
        onuIpHost.setOnuIpHostSubnetMask(hostMask);
        onuIpHost.setOnuIpHostGateway(hostGateway);
        onuIpHost.setOnuIpHostPrimaryDNS(priDns);
        onuIpHost.setOnuIpHostSecondaryDNS(secondDns);
        onuIpHost.setOnuIpHostVlanPVid(hostVlanId);
        onuIpHost.setOnuIpHostVlanTagPriority(hostVlanPri);
        gponOnuService.addGponOnuIpHost(onuIpHost);
        return NONE;
    }

    public String deleteOnuIpHost() {
        GponOnuIpHost onuIpHost = new GponOnuIpHost();
        onuIpHost.setEntityId(entityId);
        onuIpHost.setOnuId(onuId);
        onuIpHost.setOnuIndex(onuIndex);
        onuIpHost.setOnuIpHostIndex(onuIpHostIndex);
        gponOnuService.delGponOnuIpHost(onuIpHost);
        return NONE;
    }

    public String batchDelOnuIpHost() {
        GponOnuIpHost onuIpHost = new GponOnuIpHost();
        onuIpHost.setEntityId(entityId);
        onuIpHost.setOnuId(onuId);
        onuIpHost.setOnuIndex(onuIndex);
        for (Integer ipHostIndex : onuIpHostIndexList) {
            onuIpHost.setOnuIpHostIndex(ipHostIndex);
            gponOnuService.delGponOnuIpHost(onuIpHost);
        }
        return NONE;
    }

    public String refreshOnuIpHost() {
        gponOnuService.refreshOnuIpHost(entityId);
        return NONE;
    }

    public String showGponOnuCapability() {
        gponOnuCapability = gponOnuService.queryForGponOnuCapability(onuId);
        return SUCCESS;
    }

    public String showOnuUniList() {
        onu = entityService.getEntity(onuId);
        onuAttribute = onuService.getOnuAttribute(onuId);
        if(onuAttribute.getOnuEorG().equals(GponConstant.GPON_ONU)){
            gponOnuCapability=gponOnuService.queryForGponOnuCapability(onuId);
        }
        return SUCCESS;
    }

    public String loadGponOnuUniList() {
        List<GponUniAttribute> unilist = gponOnuService.loadGponOnuUniList(onuId);
        writeDataToAjax(unilist);
        return NONE;
    }

    public String showGponOnuPotsList() {
        onu = entityService.getEntity(onuId);
        onuAttribute = onuService.getOnuAttribute(onuId);
        if(onuAttribute.getOnuEorG().equals(GponConstant.GPON_ONU)){
            gponOnuCapability=gponOnuService.queryForGponOnuCapability(onuId);
        }
        return SUCCESS;
    }
    
    public String showOnuCpeList() {
        onu = entityService.getEntity(onuId);
        onuAttribute = onuService.getOnuAttribute(onuId);
        if(onuAttribute.getOnuEorG().equals(GponConstant.GPON_ONU)){
            gponOnuCapability=gponOnuService.queryForGponOnuCapability(onuId);
        }
        return SUCCESS;
    }

    public String showUniVlanView() {
        gponUniAttribute = gponOnuService.loadUniVlanConfig(uniId);
        return SUCCESS;
    }

    public String setUniVlanConfig() {
        gponOnuService.setUniVlanConfig(entityId, uniId, gponOnuUniPri, gponOnuUniPvid);
        return NONE;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getOnuId() {
        return onuId;
    }

    public void setOnuId(Long onuId) {
        this.onuId = onuId;
    }

    public Long getOnuIndex() {
        return onuIndex;
    }

    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
    }

    public Integer getOnuIpHostIndex() {
        return onuIpHostIndex;
    }

    public void setOnuIpHostIndex(Integer onuIpHostIndex) {
        this.onuIpHostIndex = onuIpHostIndex;
    }

    public Integer getOnuHostMode() {
        return onuHostMode;
    }

    public void setOnuHostMode(Integer onuHostMode) {
        this.onuHostMode = onuHostMode;
    }

    public String getPriDns() {
        return priDns;
    }

    public void setPriDns(String priDns) {
        this.priDns = priDns;
    }

    public String getSecondDns() {
        return secondDns;
    }

    public void setSecondDns(String secondDns) {
        this.secondDns = secondDns;
    }

    public String getHostIpAddr() {
        return hostIpAddr;
    }

    public void setHostIpAddr(String hostIpAddr) {
        this.hostIpAddr = hostIpAddr;
    }

    public String getHostMask() {
        return hostMask;
    }

    public void setHostMask(String hostMask) {
        this.hostMask = hostMask;
    }

    public String getHostGateway() {
        return hostGateway;
    }

    public void setHostGateway(String hostGateway) {
        this.hostGateway = hostGateway;
    }

    public Integer getHostVlanPri() {
        return hostVlanPri;
    }

    public void setHostVlanPri(Integer hostVlanPri) {
        this.hostVlanPri = hostVlanPri;
    }

    public Integer getHostVlanId() {
        return hostVlanId;
    }

    public void setHostVlanId(Integer hostVlanId) {
        this.hostVlanId = hostVlanId;
    }

    public GponOnuIpHost getOnuIpHost() {
        return onuIpHost;
    }

    public void setOnuIpHost(GponOnuIpHost onuIpHost) {
        this.onuIpHost = onuIpHost;
    }

    public List<Integer> getUsedHostIpIndex() {
        return usedHostIpIndex;
    }

    public void setUsedHostIpIndex(List<Integer> usedHostIpIndex) {
        this.usedHostIpIndex = usedHostIpIndex;
    }

    public String getSelectValue() {
        return selectValue;
    }

    public void setSelectValue(String selectValue) {
        this.selectValue = selectValue;
    }

    public List<Integer> getOnuIpHostIndexList() {
        return onuIpHostIndexList;
    }

    public void setOnuIpHostIndexList(List<Integer> onuIpHostIndexList) {
        this.onuIpHostIndexList = onuIpHostIndexList;
    }

    public GponOnuCapability getGponOnuCapability() {
        return gponOnuCapability;
    }

    public void setGponOnuCapability(GponOnuCapability gponOnuCapability) {
        this.gponOnuCapability = gponOnuCapability;
    }

    public Entity getOnu() {
        return onu;
    }

    public void setOnu(Entity onu) {
        this.onu = onu;
    }

    public OltOnuAttribute getOnuAttribute() {
        return onuAttribute;
    }

    public void setOnuAttribute(OltOnuAttribute onuAttribute) {
        this.onuAttribute = onuAttribute;
    }

    public Long getUniId() {
        return uniId;
    }

    public void setUniId(Long uniId) {
        this.uniId = uniId;
    }

    public Integer getGponOnuUniPri() {
        return gponOnuUniPri;
    }

    public void setGponOnuUniPri(Integer gponOnuUniPri) {
        this.gponOnuUniPri = gponOnuUniPri;
    }

    public Integer getGponOnuUniPvid() {
        return gponOnuUniPvid;
    }

    public void setGponOnuUniPvid(Integer gponOnuUniPvid) {
        this.gponOnuUniPvid = gponOnuUniPvid;
    }

    public GponUniAttribute getGponUniAttribute() {
        return gponUniAttribute;
    }

    public void setGponUniAttribute(GponUniAttribute gponUniAttribute) {
        this.gponUniAttribute = gponUniAttribute;
    }

    public Integer getUniNo() {
        return uniNo;
    }

    public void setUniNo(Integer uniNo) {
        this.uniNo = uniNo;
    }

}
