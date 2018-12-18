/***********************************************************************
 * $Id: BaiduMapAction.java,v1.0 2015年9月14日 上午9:11:25 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.baidu.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.baidu.domain.BaiduEntity;
import com.topvision.ems.baidu.service.BaiduMapService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.service.UserPreferencesService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author loyal
 * @created @2015年9月14日-上午9:11:25
 * 
 */
@Controller("baiduMapAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BaiduMapAction extends BaseAction {
    private static final long serialVersionUID = 5179324383045729641L;
    private Long entityId;
    private Long typeId;
    private String location;
    private BaiduEntity baiduEntity;
    protected Byte zoom;
    private double longitud;
    private String longitudStr;
    private String latitudeStr;
    private String zoomStr;
    private double latitude;
    private String entityName;
    private String searchTxt;
    private JSONObject baiduEntityJason;
    private JSONArray baiduEntityListJason = new JSONArray();
    @Autowired
    private BaiduMapService baiduMapService;
    @Autowired
    private EntityService entityService;
    @Autowired
    private UserPreferencesService UserPreferencesService;

    public String showAddEntity() {
        UserContext uc = (UserContext) getSession().get(UserContext.KEY);
        String macRule = uc.getMacDisplayStyle();
        if (UserPreferencesService.getUserPreference(uc.getUserId(), "longitud") != null) {
            longitudStr = UserPreferencesService.getUserPreference(uc.getUserId(), "longitud").getValue();
        }
        if (UserPreferencesService.getUserPreference(uc.getUserId(), "latitude") != null) {
            latitudeStr = UserPreferencesService.getUserPreference(uc.getUserId(), "latitude").getValue();
        }
        if (UserPreferencesService.getUserPreference(uc.getUserId(), "zoom") != null) {
            zoomStr = UserPreferencesService.getUserPreference(uc.getUserId(), "zoom").getValue();
        }
        baiduEntity = baiduMapService.getBaiduEntityByEntityId(entityId);
        if (baiduEntity != null) {
            String formatedMac = MacUtils.convertMacToDisplayFormat(baiduEntity.getMac(), macRule);
            baiduEntity.setMac(formatedMac);
            baiduEntityJason = JSONObject.fromObject(baiduEntity);
            return "modify";
        }
        return SUCCESS;
    }

    public String addEntityToBaiduMap() throws IOException {
        UserContext uc = (UserContext) getSession().get(UserContext.KEY);
        String macRule = uc.getMacDisplayStyle();
        BaiduEntity baiduEntity = new BaiduEntity();
        Entity entity = entityService.getEntity(entityId);
        baiduEntity.setLocation(location);
        baiduEntity.setEntityId(entityId);
        baiduEntity.setTypeId(entity.getTypeId());
        baiduEntity.setZoom(zoom);
        baiduEntity.setLatitude(latitude);
        baiduEntity.setLongitude(longitud);
        baiduEntity = baiduMapService.addBaiduEntity(baiduEntity);
        String formatedMac = MacUtils.convertMacToDisplayFormat(baiduEntity.getMac(), macRule);
        baiduEntity.setMac(formatedMac);
        baiduEntityJason = JSONObject.fromObject(baiduEntity);
        baiduEntityJason.write(response.getWriter());
        return NONE;
    }

    public String getBaiduMapEntity() throws IOException {
        UserContext uc = (UserContext) getSession().get(UserContext.KEY);
        String macRule = uc.getMacDisplayStyle();
        List<BaiduEntity> list = baiduMapService.getBaiduEntity();
        for (BaiduEntity baiduEntity : list) {
            String formatedMac = MacUtils.convertMacToDisplayFormat(baiduEntity.getMac(), macRule);
            baiduEntity.setMac(formatedMac);
        }
        baiduEntityListJason = JSONArray.fromObject(list);
        baiduEntityListJason.write(response.getWriter());
        return NONE;
    }

    public String deleteEntityFromBaiduMap() {
        baiduMapService.deleteBaiduEntity(entityId);
        return NONE;
    }

    public String modifyBaiduEntity() {
        BaiduEntity baiduEntity = new BaiduEntity();
        baiduEntity.setLocation(location);
        baiduEntity.setEntityId(entityId);
        baiduEntity.setZoom(zoom);
        baiduEntity.setLatitude(latitude);
        baiduEntity.setLongitude(longitud);
        baiduMapService.modifyBaiduEntity(baiduEntity);
        return NONE;
    }

    public String searchEntity() throws IOException {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        String macRule = uc.getMacDisplayStyle();
        queryMap.put("queryContent", searchTxt);
        String formatQueryMac = MacUtils.formatQueryMac(searchTxt);
        if (formatQueryMac.indexOf(":") == -1) {
            queryMap.put("queryMacWithoutSplit", formatQueryMac);
        }
        queryMap.put("queryContentMac", formatQueryMac);
        List<BaiduEntity> baiduEntityList = baiduMapService.searchEntity(queryMap);
        for (BaiduEntity baiduEntity : baiduEntityList) {
            String formatedMac = MacUtils.convertMacToDisplayFormat(baiduEntity.getMac(), macRule);
            baiduEntity.setMac(formatedMac);
        }
        baiduEntityListJason = JSONArray.fromObject(baiduEntityList);
        baiduEntityListJason.write(response.getWriter());
        return NONE;
    }

    public String saveBaiduMapCenter() {
        UserContext uc = (UserContext) getSession().get(UserContext.KEY);
        baiduMapService.saveBaiduMapCenter(uc, longitud, latitude, zoom);
        return NONE;
    }

    public String showBaiduMap() {
        UserContext uc = (UserContext) getSession().get(UserContext.KEY);
        if (UserPreferencesService.getUserPreference(uc.getUserId(), "longitud") != null) {
            longitudStr = UserPreferencesService.getUserPreference(uc.getUserId(), "longitud").getValue();
        }
        if (UserPreferencesService.getUserPreference(uc.getUserId(), "latitude") != null) {
            latitudeStr = UserPreferencesService.getUserPreference(uc.getUserId(), "latitude").getValue();
        }
        if (UserPreferencesService.getUserPreference(uc.getUserId(), "zoom") != null) {
            zoomStr = UserPreferencesService.getUserPreference(uc.getUserId(), "zoom").getValue();
        }
        return SUCCESS;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public BaiduEntity getBaiduEntity() {
        return baiduEntity;
    }

    public void setBaiduEntity(BaiduEntity baiduEntity) {
        this.baiduEntity = baiduEntity;
    }

    public JSONObject getBaiduEntityJason() {
        return baiduEntityJason;
    }

    public void setBaiduEntityJason(JSONObject baiduEntityJason) {
        this.baiduEntityJason = baiduEntityJason;
    }

    public Byte getZoom() {
        return zoom;
    }

    public void setZoom(Byte zoom) {
        this.zoom = zoom;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getSearchTxt() {
        return searchTxt;
    }

    public void setSearchTxt(String searchTxt) {
        this.searchTxt = searchTxt;
    }

    public String getLongitudStr() {
        return longitudStr;
    }

    public void setLongitudStr(String longitudStr) {
        this.longitudStr = longitudStr;
    }

    public String getLatitudeStr() {
        return latitudeStr;
    }

    public void setLatitudeStr(String latitudeStr) {
        this.latitudeStr = latitudeStr;
    }

    public String getZoomStr() {
        return zoomStr;
    }

    public void setZoomStr(String zoomStr) {
        this.zoomStr = zoomStr;
    }

}
