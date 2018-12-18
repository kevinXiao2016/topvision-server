package com.topvision.ems.mobile;


public class MobileEMSIIIOSVersion {
    public static final Integer build = 15;
    public static final String version = "2.0.2.1";
    public static final Boolean mandatory = false;

    public static String getTestUrl (String version) {
        return "https://ems.top-vision.cn:8111/emsii/ios/test/V"+version+"/EMSII.html";

    }

    public static String getUrl (String version) {
        return "https://ems.top-vision.cn:8111/emsii/ios/V"+version+"/EMSII.html";

    }
}
