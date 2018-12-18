package com.topvision.ems.mobile;


public class MobileEMSIIAndroidVersion {
    public static final Integer build = 15;
    public static final String version = "2.0.2.1";
    public static final Boolean mandatory = false;

    public static String getTestUrl (String version) {
        return "http://ems.top-vision.cn:8110/emsii/android/test/V" + version + "/EMSII.apk";
    }

    public static String getUrl (String version) {
        return "http://ems.top-vision.cn:8110/emsii/android/V" + version + "/EMSII.apk";
    }
    
}
