/***********************************************************************
 * $Id: OltDhcpStaticIpAction.java,v1.0 2017年11月24日 上午9:29:35 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.oltdhcp.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.exception.AddStaticIpException;
import com.topvision.ems.epon.olt.domain.OltSlotAttribute;
import com.topvision.ems.epon.olt.service.OltSlotService;
import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltDhcpGlobalObjects;
import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltDhcpStaticIp;
import com.topvision.ems.epon.oltdhcp.service.OltDhcpBaseService;
import com.topvision.ems.epon.oltdhcp.service.OltDhcpStaticIpService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.domain.IpsAddress;
import com.topvision.framework.utils.EponIndex;
import com.topvision.framework.web.struts2.BaseAction;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author haojie
 * @created @2017年11月24日-上午9:29:35
 *
 */
@Controller("oltDhcpStaticIpAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OltDhcpStaticIpAction extends BaseAction {
    private static final long serialVersionUID = 2823307179736936040L;
    private Long entityId;
    private String ipIndex;
    private String maskIndex;
    private Integer staticIpSlot;
    private Integer staticIpPort;
    private Integer staticIpOnu;
    private Integer sourceVerifyEnable;
    private int start;
    private int limit;
    @Autowired
    private OltDhcpStaticIpService oltDhcpStaticIpService;
    @Autowired
    private OltSlotService oltSlotService;
    @Autowired
    private OltDhcpBaseService oltDhcpBaseService;
    @Autowired
    private EntityTypeService entityTypeService;
    @Autowired
    private EntityService entityService;

    /**
     * 跳转静态IP列表页面
     * 
     * @return
     */
    public String showOltDhcpStaticIpList() {
        TopOltDhcpGlobalObjects global = oltDhcpBaseService.getOltDhcpBaseCfg(entityId);
        sourceVerifyEnable = global.getTopOltDhcpSourceVerifyEnable();
        return SUCCESS;
    }

    /**
     * 获取静态IP列表数据
     * 
     * @return
     */
    public String loadOltDhcpStaticIpList() {
        Map<String, Object> json = new HashMap<String, Object>();
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("entityId", entityId);
        queryMap.put("start", start);
        queryMap.put("limit", limit);
        if (staticIpSlot != null && !"".equals(staticIpSlot)) {
            Entity entity = entityService.getEntity(entityId);
            if(entityTypeService.isPN8602_GType(entity.getTypeId())){
                queryMap.put("staticIpSlot", 0);
            }else{
                queryMap.put("staticIpSlot", staticIpSlot);
            }
        }
        if (staticIpPort != null && !"".equals(staticIpPort)) {
            queryMap.put("staticIpPort", staticIpPort);
        }
        if (staticIpOnu != null && !"".equals(staticIpOnu)) {
            queryMap.put("staticIpOnu", staticIpOnu);
        }
        
        List<TopOltDhcpStaticIp> ips = oltDhcpStaticIpService.getOltDhcpStaticIp(queryMap);
        Long ipCount = oltDhcpStaticIpService.getOltDhcpStaticIpCount(queryMap);
        json.put("data", ips);
        json.put("ipCount", ipCount);
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 新增静态IP配置
     * 
     * @return
     */
    public String addOltDhcpStaticIp() {
        JSONObject json = new JSONObject();
        TopOltDhcpStaticIp ip = new TopOltDhcpStaticIp();
        ip.setEntityId(entityId);
        ip.setTopOltDhcpStaticIpVrfNameIndex("");
        ip.setTopOltDhcpStaticIpIndex(new IpsAddress(ipIndex));
        ip.setTopOltDhcpStaticIpMaskIndex(new IpsAddress(maskIndex));
        Entity entity = entityService.getEntity(entityId);
        //8602G设备slot下0
        if(entityTypeService.isPN8602_GType(entity.getTypeId())){
            ip.setTopOltDhcpStaticIpSlot(0);
        }else{
            ip.setTopOltDhcpStaticIpSlot(staticIpSlot);
        }
        ip.setTopOltDhcpStaticIpPort(staticIpPort);
        ip.setTopOltDhcpStaticIpOnu(staticIpOnu);
        try {
            oltDhcpStaticIpService.addOltDhcpStaticIp(ip);
            json.put("msg", "SUCCESS");
        } catch (AddStaticIpException e) {
            json.put("msg", e.getMessage());
        }
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 删除静态IP配置
     * 
     * @return
     */
    public String deleteOltDhcpStaticIp() {
        oltDhcpStaticIpService.deleteOltDhcpStaticIp(entityId, ipIndex, maskIndex);
        return NONE;
    }

    /**
     * 刷新静态IP列表数据
     * 
     * @return
     */
    public String refreshOltDhcpStaticIpList() {
        oltDhcpStaticIpService.refreshOltDhcpStaticIp(entityId);
        return NONE;
    }

    /**
     * 加载OLT下的槽位号
     * 
     * @return
     */
    public String loadOltSlotIdList() {
        List<OltSlotAttribute> eponSlotList = oltSlotService.getOltEponSlotList(entityId);
        List<Long> slots = new ArrayList<>();
        for (OltSlotAttribute attr : eponSlotList) {
            slots.add(attr.getSlotNo());
        }
        writeDataToAjax(slots);
        return NONE;
    }

    /**
     * 加载OLT下某槽位的ponId
     * 
     * @return
     */
    public String loadOltSlotPonIdList() {
        Map<String, Object> map = new HashMap<>();
        map.put("entityId", entityId);
        map.put("slotNo", staticIpSlot);
        List<Long> ponIndexs = oltDhcpStaticIpService.getOltSlotPonIndexList(map);
        List<Long> ponIds = new ArrayList<>();
        for (Long index : ponIndexs) {
            ponIds.add(EponIndex.getPonNo(index));
        }
        writeDataToAjax(ponIds);
        return NONE;
    }

    /**
     * 加载OLT下某槽位某端口下的ONU
     * 
     * @return
     */
    public String loadOltSlotPonOnuIdList() {
        Map<String, Object> map = new HashMap<>();
        map.put("entityId", entityId);
        map.put("slotNo", staticIpSlot);
        map.put("ponNo", staticIpPort);
        List<Long> indexList = oltDhcpStaticIpService.getOltSlotPonOnuIndexList(map);
        List<Long> onuIds = new ArrayList<>();
        for (Long index : indexList) {
            onuIds.add(EponIndex.getOnuNo(index));
        }
        writeDataToAjax(onuIds);
        return NONE;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getIpIndex() {
        return ipIndex;
    }

    public void setIpIndex(String ipIndex) {
        this.ipIndex = ipIndex;
    }

    public String getMaskIndex() {
        return maskIndex;
    }

    public void setMaskIndex(String maskIndex) {
        this.maskIndex = maskIndex;
    }

    public Integer getStaticIpSlot() {
        return staticIpSlot;
    }

    public void setStaticIpSlot(Integer staticIpSlot) {
        this.staticIpSlot = staticIpSlot;
    }

    public Integer getStaticIpPort() {
        return staticIpPort;
    }

    public void setStaticIpPort(Integer staticIpPort) {
        this.staticIpPort = staticIpPort;
    }

    public Integer getStaticIpOnu() {
        return staticIpOnu;
    }

    public void setStaticIpOnu(Integer staticIpOnu) {
        this.staticIpOnu = staticIpOnu;
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

    public Integer getSourceVerifyEnable() {
        return sourceVerifyEnable;
    }

    public void setSourceVerifyEnable(Integer sourceVerifyEnable) {
        this.sourceVerifyEnable = sourceVerifyEnable;
    }

}
