package com.topvision.ems.mobile;


public class MobileMUIAndroidVersion {
    public static final Integer build = 6;
    public static final String version = "1.0.0.5";
    public static final Boolean mandatory = false;

    public static String getTestUrl (String version) {
        return "http://ems.top-vision.cn:8110/terminal/android/test/V" + version + "/terminal.apk";
    }

    public static String getUrl (String version) {
        return "http://ems.top-vision.cn:8110/terminal/android/V" + version + "/terminal.apk";
    }
    
}
