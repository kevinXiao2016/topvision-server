/**
 * 
 */
package com.topvision.ems.google.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.google.domain.GoogleEntity;
import com.topvision.ems.google.service.GoogleEntityService;
import com.topvision.ems.network.domain.Link;
import com.topvision.framework.common.MacUtils;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.domain.UserContext;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

/**
 * @author niejun
 * 
 */
@Controller("networkGoogleMapAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class NetworkGoogleMapAction extends GoogleMapAction {
    private static final long serialVersionUID = -5599073888359292444L;
    private static final Logger logger = LoggerFactory.getLogger(NetworkGoogleMapAction.class);
    @Autowired
    private GoogleEntityService googleEntityService;
    private Long entityId = new Long(0);
    private List<GoogleEntity> entities = null;
    private String search = null;
    private GoogleEntity googleEntity;
    private boolean fixed;
    private String location;
    private String latitudes;
    private String longitudes;
    private String mapTypeId;

    public String deleteGoogleEntity() throws Exception {
        googleEntityService.deleteGoogleEntity(entityId);
        return NONE;
    }

    /**
     * 查询地理位置
     * 
     * @return
     * @throws IOException
     */
    public String queryForGeoLocationFromLatLng() throws IOException {
        net.sf.json.JSONObject json = new net.sf.json.JSONObject();
        String geo = googleEntityService.queryForGeoLocationFromLatLng(latitude, longitude);
        json.put("geo", getResourceString(geo, "com.topvision.ems.resources.resources"));
        writeDataToAjax(json);
        return NONE;
    }

    public String queryForLatLngFromGeoLocation() {
        @SuppressWarnings("unused")
        String geo = googleEntityService.queryForLatLngFromGeoLocation();
        return NONE;
    }

    /*
     * key：properties文件的keymodule：资源文件
     */
    protected String getResourceString(String key, String module) {
        try {
            ResourceManager resourceManager = ResourceManager.getResourceManager(module);
            return resourceManager.getNotNullString(key);
        } catch (Exception e) {
            return key;
        }
    }

    /**
     * 加载将要在GOOGLE地图上展示的设备
     * 
     * @return
     * @throws Exception
     */
    public String loadGoogleEntities() throws Exception {
        JSONArray jarray = new JSONArray();
        try {
            //Modify by Rod
            entities = googleEntityService.getAllGoogleEntity(new HashMap<String, String>());
            Map<Long, JSONObject> jsons = new HashMap<Long, JSONObject>(entities.size());
            for (GoogleEntity entity : entities) {
                JSONObject json = new JSONObject();
                json.put("type", "entity");
                json.put("entityType", entity.getTypeId());
                json.put("typeId", entity.getTypeId());
                json.put("entityId", entity.getEntityId());
                json.put("latitude", entity.getLatitude());
                json.put("longitude", entity.getLongitude());
                json.put("parentId", entity.getParentId());
                json.put("mac", entity.getMac());
                String icon48 = entity.getIcon48();
                if (icon48.substring(0, 8).equals("/images/")) {
                    icon48 = icon48.substring(8);
                }
                json.put("icon48", icon48);
                json.put("ip", entity.getIp());
                json.put("url", entity.getUrl());
                json.put("name", entity.getName());
                json.put("fixed", entity.isFixed());
                json.put("zoom", entity.getZoom());
                json.put("maxZoom", entity.getMaxZoom());
                json.put("minZoom", entity.getMinZoom());
                json.put("location", entity.getLocation());
                jarray.add(json);
                jsons.put(entity.getEntityId(), json);
            }
            List<Link> links = googleEntityService.getAllLinks();
            for (int i = 0; links != null && i < links.size(); i++) {
                Link link = links.get(i);
                JSONObject json = new JSONObject();
                json.put("type", "link");
                json.put("entityType", "");
                json.put("linkId", link.getLinkId());
                json.put("name", link.getName());
                json.put("srcLongitude", jsons.get(link.getSrcEntityId()).getDouble("longitude"));
                json.put("srcLatitude", jsons.get(link.getSrcEntityId()).getDouble("latitude"));
                json.put("destLongitude", jsons.get(link.getDestEntityId()).getDouble("longitude"));
                json.put("destLatitude", jsons.get(link.getDestEntityId()).getDouble("latitude"));
                jarray.add(json);
            }
        } catch (Exception ex) {
            logger.debug("Test Ping Monitor.", ex);
        } finally {
            writeDataToAjax(jarray);
        }
        return NONE;
    }

    /**
     * 设备位置改动
     * 
     * @return
     * @throws Exception
     */
    public String save2GoogleMap() throws Exception {
        GoogleEntity googleEntity = new GoogleEntity();
        googleEntity.setEntityId(entityId);
        googleEntity.setLatitude(latitude);
        googleEntity.setLongitude(longitude);
        googleEntity.setZoom(zoom);
        googleEntity.setMaxZoom((byte) (zoom + 3));
        googleEntity.setMinZoom((byte) (zoom - 3));
        googleEntity.setLocation(location);
        googleEntityService.insertOrUpdateGoogleEntity(googleEntity);
        return NONE;
    }

    /**
     * 查询所有能加入到google地图的设备
     * 
     * @return
     * @throws JSONException
     * @throws IOException
     */
    public String queryAvaibleDevice() throws JSONException, IOException {
        net.sf.json.JSONObject json = new net.sf.json.JSONObject();
        List<Entity> list = googleEntityService.queryAvaibleDevice();
        //add by fanzidong,需要在展示前格式化MAC地址
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        String macRule = uc.getMacDisplayStyle();
        for (Entity entity : list) {
            String formatedMac = MacUtils.convertMacToDisplayFormat(entity.getMac(), macRule);
            entity.setMac(formatedMac);
        }
        json.put("list", list);
        writeDataToAjax(json);
        return NONE;
    }

    public String searchGoogleEntity() throws Exception {
        JSONArray jarray = new JSONArray();
        try {
            Map<String, String> map = new HashMap<String, String>();
            if (search != null && search.trim().length() > 0) {
                map.put("filter", search);
                entities = googleEntityService.getAllGoogleEntity(map);
            } else {
                entities = googleEntityService.getAllGoogleEntity(map);
            }
            for (GoogleEntity entity : entities) {
                JSONObject json = new JSONObject();
                json.put("entityId", entity.getEntityId());
                json.put("latitude", entity.getLatitude());
                json.put("longitude", entity.getLongitude());
                json.put("icon48", entity.getIcon48());
                json.put("ip", entity.getIp());
                json.put("name", entity.getName());
                json.put("zoom", entity.getZoom());
                json.put("url", entity.getUrl());
                json.put("location", entity.getLocation());
                jarray.add(json);
            }
        } catch (Exception ex) {
            logger.debug("Test Ping Monitor.", ex);
        } finally {
            writeDataToAjax(jarray);
        }
        return NONE;
    }

    /**
     * 固定位置
     * 
     * @return
     */
    public String fixlocation() {
        googleEntityService.fixlocation(entityId, fixed);
        return NONE;
    }

    @Override
    public String showGoogleMapPreview() {
        return SUCCESS;
    }

    public String showEntityProperty() {
        googleEntity = googleEntityService.getGoogleEntityById(entityId);
        return SUCCESS;
    }

    public void setEntities(List<GoogleEntity> entities) {
        this.entities = entities;
    }

    @Override
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    @Override
    public void setGoogleEntityService(GoogleEntityService googleEntityService) {
        this.googleEntityService = googleEntityService;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public List<GoogleEntity> getEntities() {
        return entities;
    }

    @Override
    public Long getEntityId() {
        return entityId;
    }

    @Override
    public GoogleEntityService getGoogleEntityService() {
        return googleEntityService;
    }

    public String getSearch() {
        return search;
    }

    public GoogleEntity getGoogleEntity() {
        return googleEntity;
    }

    public void setGoogleEntity(GoogleEntity googleEntity) {
        this.googleEntity = googleEntity;
    }

    public boolean isFixed() {
        return fixed;
    }

    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLatitudes() {
        return latitudes;
    }

    public void setLatitudes(String latitudes) {
        this.latitude = Double.parseDouble(latitudes);
    }

    public String getLongitudes() {
        return longitudes;
    }

    public void setLongitudes(String longitudes) {
        this.longitude = Double.parseDouble(longitudes);
    }

    public String getMapTypeId() {
        return mapTypeId;
    }

    public void setMapTypeId(String mapTypeId) {
        this.mapTypeId = mapTypeId;
    }
 
}
