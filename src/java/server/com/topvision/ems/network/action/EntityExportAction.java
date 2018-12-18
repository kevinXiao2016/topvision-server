/***********************************************************************
 * $Id: EntityExportAction.java,v1.0 2013-11-1 上午8:33:13 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.action;

import java.io.UnsupportedEncodingException;

import javax.annotation.Resource;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.network.service.EntityExportService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.ResourceManager;

/**
 * @author loyal
 * @created @2013-11-1-上午8:33:13
 * 
 */
@Controller("entityExportAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EntityExportAction extends BaseAction {
    private static final long serialVersionUID = -3850408970279980157L;
    @Resource(name = "entityExportService")
    private EntityExportService entityExportService;
    @Resource(name = "entityTypeService")
    private EntityTypeService entityTypeService;
    private String regionId;
    private String ipString;
    private String macString;
    private String typeIdString;
    private int start;
    private int limit;

    /**
     * 查询设备
     * 
     * @return
     */
    /*
     * public String loadEntity() { Map<String, Object> json = new HashMap<String, Object>();
     * Map<String, Object> map = new HashMap<String, Object>(); UserContext uc = (UserContext)
     * super.getSession().get(UserContext.KEY); String macRule = uc.getMacDisplayStyle(); if
     * (regionId != null && !"".equals(regionId.trim())) { map.put("regionId", regionId); } if
     * (ipString != null && !"".equals(ipString.trim())) { String[] ips = ipString.split("-");
     * map.put("startIp", ips[0]); map.put("endIp", ips[1]); map.put("ipString", ipString); } if
     * (macString != null && !"".equals(macString.trim())) { String[] macs = macString.split("-");
     * map.put("startMac", MacUtils.convertToMaohaoFormat(macs[0])); map.put("endMac",
     * MacUtils.convertToMaohaoFormat(macs[1])); map.put("macString", macString); } if (typeIdString
     * != null && !"".equals(typeIdString.trim())) { map.put("typeIdString", typeIdString); }
     * List<Entity> entityList = entityExportService.getEntity(map, start, limit); Long entityNum =
     * entityExportService.getEntityNum(map); for (int i = 0; i < entityList.size(); i++) { Long
     * entityId = entityList.get(i).getEntityId(); List<String> folderList = new
     * ArrayList<String>(); folderList = entityExportService.getEntityFolder(entityId);
     * StringBuilder location = new StringBuilder(); int size = folderList.size(); for (int j = 0; j
     * < size; j++) { if (folderList.get(j) != null) { if (j == (size - 1)) {
     * location.append(getResourceManager().getString(folderList.get(j))); } else {
     * location.append(getResourceManager().getString(folderList.get(j))).append(","); } } }
     * entityList.get(i).setLocation(location.toString());
     * entityList.get(i).setMac(MacUtils.convertMacToDisplayFormat(entityList.get(i).getMac(),
     * macRule)); } json.put("data", entityList); json.put("rowCount", entityNum);
     * writeDataToAjax(JSONObject.fromObject(json)); return NONE; }
     */

    /**
     * 导出设备到EXCEL
     * 
     * @return
     * @throws UnsupportedEncodingException
     *//*
       * public String exportEntityToExcel() throws UnsupportedEncodingException { UserContext uc =
       * (UserContext) super.getSession().get(UserContext.KEY); String macRule =
       * uc.getMacDisplayStyle(); // 查询条件 Map<String, Object> map = new HashMap<String, Object>();
       * if (regionId != null && !"".equals(regionId.trim())) { map.put("regionId", regionId); } if
       * (ipString != null && !"".equals(ipString.trim())) { String[] ips = ipString.split("-");
       * map.put("startIp", ips[0]); map.put("endIp", ips[1]); map.put("ipString", ipString); } if
       * (macString != null && !"".equals(macString.trim())) { String[] macs = macString.split("-");
       * map.put("startMac", MacUtils.convertToMaohaoFormat(macs[0])); map.put("endMac",
       * MacUtils.convertToMaohaoFormat(macs[1])); map.put("macString", macString); } if
       * (typeIdString != null && !"".equals(typeIdString.trim())) { map.put("typeIdString",
       * typeIdString); } List<Entity> entityList = entityExportService.getEntity(map, start,
       * Integer.MAX_VALUE); for (int i = 0; i < entityList.size(); i++) { Long entityId =
       * entityList.get(i).getEntityId(); List<String> folderList = new ArrayList<String>();
       * folderList = entityExportService.getEntityFolder(entityId); StringBuilder location = new
       * StringBuilder(); int size = folderList.size(); for (int j = 0; j < size; j++) { if
       * (folderList.get(j) != null) { if (j == (size - 1)) {
       * location.append(getResourceManager().getString(folderList.get(j))); } else {
       * location.append(getResourceManager().getString(folderList.get(j))).append(","); } } }
       * entityList.get(i).setLocation(location.toString());
       * entityList.get(i).setMac(MacUtils.convertMacToDisplayFormat(entityList.get(i).getMac(),
       * macRule)); } entityExportService.exportEntityToExcel(entityList); return NONE; }
       */

    private ResourceManager getResourceManager() {
        return ResourceManager.getResourceManager("com.topvision.ems.resources.resources");
    }

    public EntityExportService getEntityExportService() {
        return entityExportService;
    }

    public void setEntityExportService(EntityExportService entityExportService) {
        this.entityExportService = entityExportService;
    }

    public EntityTypeService getEntityTypeService() {
        return entityTypeService;
    }

    public void setEntityTypeService(EntityTypeService entityTypeService) {
        this.entityTypeService = entityTypeService;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getIpString() {
        return ipString;
    }

    public void setIpString(String ipString) {
        this.ipString = ipString;
    }

    public String getMacString() {
        return macString;
    }

    public void setMacString(String macString) {
        this.macString = macString;
    }

    public String getTypeIdString() {
        return typeIdString;
    }

    public void setTypeIdString(String typeIdString) {
        this.typeIdString = typeIdString;
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
