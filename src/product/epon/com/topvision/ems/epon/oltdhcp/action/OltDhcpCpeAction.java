/***********************************************************************
 * $Id: OltDhcpCpeAction.java,v1.0 2017年11月24日 上午9:28:45 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.oltdhcp.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltDhcpCpeInfo;
import com.topvision.ems.epon.oltdhcp.service.OltDhcpCpeService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.web.struts2.BaseAction;

import net.sf.json.JSONObject;

/**
 * @author haojie
 * @created @2017年11月24日-上午9:28:45
 *
 */
@Controller("oltDhcpCpeAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OltDhcpCpeAction extends BaseAction {
    private static final long serialVersionUID = 6838728050335809369L;
    private Long entityId;
    private String cpeIpIndex;
    private String cpeMac;
    private Integer cpeVlan;
    private Integer cpePortType;
    private Integer cpeSlot;
    private Integer cpePort;
    private Integer cpeOnu;
    private int start;
    private int limit;
    @Autowired
    private OltDhcpCpeService oltDhcpCpeService;
    @Autowired
    private EntityTypeService entityTypeService;
    @Autowired
    private EntityService entityService;

    /**
     * 跳转CPE列表页面
     * 
     * @return
     */
    public String showOltDhcpCpeList() {
        return SUCCESS;
    }

    /**
     * 获取CPE列表数据
     * 
     * @return
     */
    public String loadOltDhcpCpeList() {
        Map<String, Object> json = new HashMap<String, Object>();
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("entityId", entityId);
        queryMap.put("start", start);
        queryMap.put("limit", limit);
        if (cpeIpIndex != null && !"".equals(cpeIpIndex)) {
            queryMap.put("cpeIpIndex", cpeIpIndex);
        }
        if (cpeMac != null && !"".equals(cpeMac)) {
            queryMap.put("cpeMac", cpeMac);
        }
        if (cpeVlan != null && !"".equals(cpeVlan)) {
            queryMap.put("cpeVlan", cpeVlan);
        }
        if (cpePortType != null && !"".equals(cpePortType)) {
            queryMap.put("cpePortType", cpePortType);
        }
        if (cpeSlot != null && !"".equals(cpeSlot)) {
            Entity entity = entityService.getEntity(entityId);
            if(entityTypeService.isPN8602_GType(entity.getTypeId())){
                queryMap.put("cpeSlot", 0);
            }else{
                queryMap.put("cpeSlot", cpeSlot);
            }
        }
        if (cpePort != null && !"".equals(cpePort)) {
            queryMap.put("cpePort", cpePort);
        }
        if (cpeOnu != null && !"".equals(cpeOnu)) {
            queryMap.put("cpeOnu", cpeOnu);
        }
        List<TopOltDhcpCpeInfo> cpes = oltDhcpCpeService.getOltDhcpCpeInfo(queryMap);
        Long cpeCount = oltDhcpCpeService.getOltDhcpCpeInfoCount(queryMap);
        json.put("data", cpes);
        json.put("cpeCount", cpeCount);
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 刷新CPE列表数据
     * 
     * @return
     */
    public String refreshOltDhcpCpeList() {
        oltDhcpCpeService.refreshOltDhcpCpeInfo(entityId);
        return NONE;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getCpeIpIndex() {
        return cpeIpIndex;
    }

    public void setCpeIpIndex(String cpeIpIndex) {
        this.cpeIpIndex = cpeIpIndex;
    }

    public String getCpeMac() {
        return cpeMac;
    }

    public void setCpeMac(String cpeMac) {
        this.cpeMac = cpeMac;
    }

    public Integer getCpeVlan() {
        return cpeVlan;
    }

    public void setCpeVlan(Integer cpeVlan) {
        this.cpeVlan = cpeVlan;
    }

    public Integer getCpePortType() {
        return cpePortType;
    }

    public void setCpePortType(Integer cpePortType) {
        this.cpePortType = cpePortType;
    }

    public Integer getCpeSlot() {
        return cpeSlot;
    }

    public void setCpeSlot(Integer cpeSlot) {
        this.cpeSlot = cpeSlot;
    }

    public Integer getCpePort() {
        return cpePort;
    }

    public void setCpePort(Integer cpePort) {
        this.cpePort = cpePort;
    }

    public Integer getCpeOnu() {
        return cpeOnu;
    }

    public void setCpeOnu(Integer cpeOnu) {
        this.cpeOnu = cpeOnu;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

}
