/**
 * 
 */
package com.topvision.ems.google.action;

import java.math.BigDecimal;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.google.domain.GoogleEntity;
import com.topvision.ems.google.service.GoogleEntityService;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.service.SystemPreferencesService;
import com.topvision.platform.util.GoogleConstants;

/**
 * @author niejun
 * 
 */
@Controller("googleMapAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GoogleMapAction extends BaseAction {
    private static final long serialVersionUID = 6569768399006621446L;
    @Autowired
    protected SystemPreferencesService systemPreferencesService;
    @Autowired
    private GoogleEntityService googleEntityService;
    protected Double latitude = new Double(37);
    protected Double longitude = new Double(107);
    protected Byte zoom = 4;
    private Long entityId;
    private String module = null;
    private String country = null;

    public String add2GoogleMap() throws Exception {
        GoogleEntity googleEntity = googleEntityService.getGoogleEntityById(entityId);
        if (googleEntity != null && googleEntity.getLatitude() > 0 && googleEntity.getLongitude() > 0
                && googleEntity.getZoom() > 0) {
            latitude = googleEntity.getLatitude();
            longitude = googleEntity.getLongitude();
            zoom = googleEntity.getZoom();
            return SUCCESS;
        } else {
            Properties props = systemPreferencesService.getModulePreferences(GoogleConstants.GOOGLE_LOC_MODULE);
            if (props != null) {
                String key = null;
                key = props.getProperty("google.latitude");
                if (key != null) {
                    latitude = Double.parseDouble(key);
                }
                key = props.getProperty("google.longitude");
                if (key != null) {
                    longitude = Double.parseDouble(key);
                }
                key = props.getProperty("google.zoom");
                if (key != null) {
                    zoom = Byte.parseByte(key);
                }
            }
            return SUCCESS;
        }
    }

    public String saveGoogleLocation() throws Exception {
        systemPreferencesService.saveGoogleLocation(latitude, longitude, zoom);
        return NONE;
    }

    public String jumpToMap() throws Exception {
        return SUCCESS;
    }

    public String saveGoogleMapKey() throws Exception {
        return NONE;
    }

    /**
     * 展示GOOGLE地图
     * 
     * @return
     * @throws Exception
     */
    public String showGoogleMap() throws Exception {
        Properties props = systemPreferencesService.getModulePreferences(GoogleConstants.GOOGLE_LOC_MODULE);
        if (props != null) {
            String key = null;
            key = props.getProperty("google.latitude");
            if (key != null) {
                latitude = Double.parseDouble(key);
            }
            key = props.getProperty("google.longitude");
            if (key != null) {
                longitude = Double.parseDouble(key);
            }
            key = props.getProperty("google.zoom");
            if (key != null) {
                zoom = Byte.parseByte(key);
            }
        }
        return "google";
    }

    public String showGoogleMapPreview() throws Exception {
        Properties props = systemPreferencesService.getModulePreferences(GoogleConstants.GOOGLE_LOC_MODULE);
        if (props != null) {
            String key = null;
            key = props.getProperty("google.latitude");
            if (key != null) {
                latitude = Double.parseDouble(key);
            }
            key = props.getProperty("google.longitude");
            if (key != null) {
                longitude = Double.parseDouble(key);
            }
            key = props.getProperty("google.zoom");
            if (key != null) {
                zoom = Byte.parseByte(key);
            }
        }
        return SUCCESS;
    }

    public String showPopGoogleMapKey() throws Exception {
        return SUCCESS;
    }

    public GoogleEntityService getGoogleEntityService() {
        return googleEntityService;
    }

    public void setGoogleEntityService(GoogleEntityService googleEntityService) {
        this.googleEntityService = googleEntityService;
    }

    public String getCountry() {
        return country;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getModule() {
        return module;
    }

    public Byte getZoom() {
        return zoom;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude.doubleValue();
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude.doubleValue();
    }

    public void setModule(String module) {
        this.module = module;
    }

    public void setSystemPreferencesService(SystemPreferencesService systemPreferencesService) {
        this.systemPreferencesService = systemPreferencesService;
    }

    public void setZoom(Byte zoom) {
        this.zoom = zoom;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }
}