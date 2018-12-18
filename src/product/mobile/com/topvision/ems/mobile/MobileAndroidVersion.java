package com.topvision.ems.mobile;


public class MobileAndroidVersion {
    public static final Integer versionCode = 24;
    public static final String versionName = "V1.0.21.1";

    public static String getTestUrl(String version) {
        return "http://ems.top-vision.cn:8110/android/test/" + version + "/nm3k.apk";
    }

    public static String getUrl(String version) {
        return "http://ems.top-vision.cn:8110/android/" + version + "/nm3k.apk";
    }
}
