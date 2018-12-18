/***********************************************************************
 * $Id: Cmc_bRouteAction.java,v1.0 2013-8-6 下午4:40:27 $
 * 
 * @author: dosion
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.route.action;

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

import com.topvision.ems.cmc.config.service.CmcConfigService;
import com.topvision.ems.cmc.facade.domain.CmcSystemIpInfo;
import com.topvision.ems.cmc.route.facade.domain.CmcRoute;
import com.topvision.ems.cmc.route.service.CmcRouteService;
import com.topvision.ems.cmc.util.CmcConstants;
import com.topvision.ems.cmc.vlan.facade.domain.CmcVifSubIpEntry;
import com.topvision.ems.cmc.vlan.facade.domain.CmcVlanConfigEntry;
import com.topvision.ems.cmc.vlan.service.CmcVlanService;
import com.topvision.framework.exception.engine.SnmpNoResponseException;
import com.topvision.framework.snmp.RowStatus;
import com.topvision.framework.web.struts2.BaseAction;

/**
 * @author dosion
 * @created @2013-8-6-下午4:40:27
 * 
 */
@Controller("cmcRouteAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CmcRouteAction extends BaseAction {
    private static final long serialVersionUID = -5347086165354662731L;
    private final Logger logger = LoggerFactory.getLogger(CmcRouteAction.class);
    public static final String DO_SUCCESS = "success";
    public static final String DO_FAIL = "fail";
    @Resource(name = "cmcRouteService")
    private CmcRouteService cmcRouteService;
    @Resource(name = "cmcVlanService")
    private CmcVlanService cmcVlanService;
    @Resource(name = "cmcConfigService")
    private CmcConfigService cmcConfigService;
    private Long entityId;
    private Long cmcId;
    private CmcRoute route;
    private Integer index;
    private String topCcmtsRouteDstIp;
    private String topCcmtsRouteIpMask;
    private String topCcmtsRouteNexthop;
    private JSONArray vlanList;
    private Integer action;

    /**
     * 显示静态路由页面
     * 
     * @return
     */

    public String showRouteViewList() {
        return SUCCESS;
    }

    public String showModifyRouteView() {
        List<CmcVlanConfigEntry> ipList = cmcVlanService.getCmcVlanList(cmcId);
        CmcSystemIpInfo systemIpInfo = cmcConfigService.getCmcIpInfo(entityId);
        CmcVlanConfigEntry cmcVlan = new CmcVlanConfigEntry();
        //IP类型为非主IP
        cmcVlan.setPriTag(CmcConstants.PRI_TAG_IS);
        cmcVlan.setCmcId(cmcId);
        cmcVlan.setTopCcmtsVlanIndex(0);
        cmcVlan.setIpAddr(systemIpInfo.getTopCcmtsEthIpAddr());
        cmcVlan.setIpMask(systemIpInfo.getTopCcmtsEthIpMask());
        ipList.add(cmcVlan);
        List<CmcVifSubIpEntry> subIpList = cmcVlanService.getCmcVlanListFromVlan0(entityId);
        for (CmcVifSubIpEntry subIpEntry : subIpList) {
            CmcVlanConfigEntry cmcVlanEntry = new CmcVlanConfigEntry();
            //IP类型为非主IP
            cmcVlanEntry.setPriTag(CmcConstants.PRI_TAG_NO);
            cmcVlanEntry.setCmcId(cmcId);
            cmcVlanEntry.setSecVidIndex(subIpEntry.getTopCcmtsVifSubIpSeqIdx());
            cmcVlanEntry.setTopCcmtsVlanIndex(0);
            cmcVlanEntry.setIpAddr(subIpEntry.getTopCcmtsVifSubIpAddr());
            cmcVlanEntry.setIpMask(subIpEntry.getTopCcmtsVifSubIpMask());
            ipList.add(cmcVlanEntry);
        }
        vlanList = JSONArray.fromObject(ipList);
        return SUCCESS;
    }

    /**
     * 获取静态路由数据
     * 
     * @return
     */
    public String getRouteViewData() {
        Map<String, Object> json = new HashMap<String, Object>();
        List<CmcRoute> list;
        String result;
        try {
            list = cmcRouteService.getRouteConfigData(entityId);
            result = DO_SUCCESS;
        } catch (Exception e) {
            list = null;
            result = DO_FAIL;
            logger.error("", e);
        }
        json.put("data", list);
        json.put("result", result);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 增加静态路由配置
     * 
     * @return
     */
    public String addRouteConfig() {
        String message;
        try {
            route.setTopCcmtsRouteStatus(RowStatus.CREATE_AND_GO);
            cmcRouteService.modifyRouteConfig(entityId, route);
            message = DO_SUCCESS;
        } catch (Exception e) {
            message = DO_FAIL;
            logger.error("", e);
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 修改静态路由配置
     * 
     * @return
     */
    public String modifyRouteconfig() {
        String message;
        try {
            cmcRouteService.modifyRouteConfig(entityId, route);
            message = DO_SUCCESS;
        } catch (Exception e) {
            message = DO_FAIL;
            logger.error("", e);
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 删除静态路由配置
     * 
     * @return
     */
    public String deleteRouteConfig() {
        String message = null;
        try {
            route.setTopCcmtsRouteStatus(RowStatus.DESTORY);
            cmcRouteService.modifyRouteConfig(entityId, route);
            message = DO_SUCCESS;
        } catch(SnmpNoResponseException e){
            message = "SnmpNoResponse";
            logger.error("", e);
        } catch (Exception e) {
            message = DO_FAIL;
            logger.error("", e);
        }
        writeDataToAjax(message);
        return NONE;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public CmcRouteService getcmcRouteService() {
        return cmcRouteService;
    }

    public void setcmcRouteService(CmcRouteService cmcRouteService) {
        this.cmcRouteService = cmcRouteService;
    }

    public CmcRoute getRoute() {
        return route;
    }

    public void setRoute(CmcRoute route) {
        this.route = route;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getTopCcmtsRouteDstIp() {
        return topCcmtsRouteDstIp;
    }

    public void setTopCcmtsRouteDstIp(String topCcmtsRouteDstIp) {
        this.topCcmtsRouteDstIp = topCcmtsRouteDstIp;
    }

    public String getTopCcmtsRouteIpMask() {
        return topCcmtsRouteIpMask;
    }

    public void setTopCcmtsRouteIpMask(String topCcmtsRouteIpMask) {
        this.topCcmtsRouteIpMask = topCcmtsRouteIpMask;
    }

    public String getTopCcmtsRouteNexthop() {
        return topCcmtsRouteNexthop;
    }

    public void setTopCcmtsRouteNexthop(String topCcmtsRouteNexthop) {
        this.topCcmtsRouteNexthop = topCcmtsRouteNexthop;
    }

    public CmcVlanService getCmcVlanService() {
        return cmcVlanService;
    }

    public void setCmcVlanService(CmcVlanService cmcVlanService) {
        this.cmcVlanService = cmcVlanService;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public JSONArray getVlanList() {
        return vlanList;
    }

    public void setVlanList(JSONArray vlanList) {
        this.vlanList = vlanList;
    }

    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

    public CmcConfigService getCmcConfigService() {
        return cmcConfigService;
    }

    public void setCmcConfigService(CmcConfigService cmcConfigService) {
        this.cmcConfigService = cmcConfigService;
    }

}
