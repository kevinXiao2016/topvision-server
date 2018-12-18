package com.topvision.ems.mobile.action;

import java.io.IOException;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cmc.spectrum.utils.VersionUtil;
import com.topvision.ems.mobile.MobileAndroidVersion;
import com.topvision.ems.mobile.MobileEMSIIAndroidVersion;
import com.topvision.ems.mobile.MobileEMSIIIOSVersion;
import com.topvision.ems.mobile.MobileIOSVersion;
import com.topvision.ems.mobile.MobileMUIAndroidVersion;
import com.topvision.ems.mobile.MobileMUIIOSVersion;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.SystemConstants;
import com.topvision.platform.SystemVersion;

import net.sf.json.JSONObject;

@Controller("mVersionAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MVersionAction extends BaseAction {
    private static final long serialVersionUID = -5646845239796100096L;
    
    /*****************************************android*****************************************/
    public String getAndroidVersion() throws IOException {
        JSONObject json = new JSONObject();
        json.put("versionCode", MobileAndroidVersion.versionCode);
        json.put("versionName", MobileAndroidVersion.versionName);
        json.write(response.getWriter());
        return NONE;
    }
    
    public String getAndroidVersionAndUrl() throws IOException {
        JSONObject json = new JSONObject();
        json.put("versionCode", MobileAndroidVersion.versionCode);
        json.put("versionName", MobileAndroidVersion.versionName);
        if (SystemConstants.isDevelopment) {
            json.put("url", MobileAndroidVersion.getTestUrl(MobileAndroidVersion.versionName));
            json.put("shortUrl", MobileAndroidVersion.getTestUrl(MobileAndroidVersion.versionName));
        } else {
            json.put("url", MobileAndroidVersion.getUrl(MobileAndroidVersion.versionName));
            json.put("shortUrl", MobileAndroidVersion.getUrl(MobileAndroidVersion.versionName));
        }
        json.write(response.getWriter());
        return NONE;
    }

    
    /***************************************ios************************************/
    public String getIOSVersion() throws IOException {
        JSONObject json = new JSONObject();
        json.put("build", MobileIOSVersion.build);
        json.put("version", MobileIOSVersion.version);
        json.write(response.getWriter());
        return NONE;
    }
    
    public String getIOSUrl() throws IOException {
        if (SystemConstants.isDevelopment) {
            writeDataToAjax(MobileIOSVersion.getTestUrl(MobileIOSVersion.version));
        } else {
            writeDataToAjax(MobileIOSVersion.getUrl(MobileIOSVersion.version));
        }
        return NONE;
    }
    
    public String getIOSVersionAndUrl() throws IOException {
        JSONObject json = new JSONObject();
        json.put("build",  MobileIOSVersion.build);
        json.put("version", MobileIOSVersion.version);
        if (SystemConstants.isDevelopment) {
            json.put("url", MobileIOSVersion.getTestUrl(MobileIOSVersion.version));
        } else {
            json.put("url", MobileIOSVersion.getUrl(MobileIOSVersion.version));
        }
        json.write(response.getWriter());
        return NONE;
    }

    public String getEmsVersion() throws IOException {
        JSONObject json = new JSONObject();
        SystemVersion version = new SystemVersion();
        json.put("version", version.getBuildVersion());
        json.put("versionLong", VersionUtil.getVersionLong(version.getBuildVersion()));
        writeDataToAjax(json);
        return NONE;
    }
    
    /****************************************Terminal******************************************/
    public String getMuiAndroidVersionAndUrl() {
        JSONObject json = new JSONObject();
        json.put("version", MobileMUIAndroidVersion.version);
        json.put("url", MobileMUIAndroidVersion.getUrl(MobileMUIAndroidVersion.version));
        json.put("mandatory", MobileMUIAndroidVersion.mandatory);
        writeDataToAjax(json);
        return NONE;
    }
    
    public String getMuiIOSVersionAndUrl() {
        JSONObject json = new JSONObject();
        json.put("version", MobileMUIIOSVersion.version);
        json.put("url", MobileMUIIOSVersion.getUrl(MobileMUIIOSVersion.version));
        json.put("mandatory", MobileMUIAndroidVersion.mandatory);
        writeDataToAjax(json);
        return NONE;
    }

    /****************************************EMSII******************************************/
    public String getEMSIIAndroidVersionAndUrl() {
        JSONObject json = new JSONObject();
        json.put("version", MobileEMSIIAndroidVersion.version);
        json.put("url", MobileEMSIIAndroidVersion.getUrl(MobileEMSIIAndroidVersion.version));
        json.put("mandatory", MobileEMSIIAndroidVersion.mandatory);
        writeDataToAjax(json);
        return NONE;
    }
    
    public String getEMSIIIOSVersionAndUrl() {
        JSONObject json = new JSONObject();
        json.put("version", MobileEMSIIIOSVersion.version);
        json.put("url", MobileEMSIIIOSVersion.getUrl(MobileEMSIIIOSVersion.version));
        json.put("mandatory", MobileEMSIIIOSVersion.mandatory);
        writeDataToAjax(json);
        return NONE;
    }

}
