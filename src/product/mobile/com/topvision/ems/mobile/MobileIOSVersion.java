package com.topvision.ems.mobile;


public class MobileIOSVersion {
    public static final Integer build = 14;
    public static final String version = "1.0.2.6";

    public static String getTestUrl (String version) {
        return "https://ems.top-vision.cn:8111/ios/test/V"+version+"/EMS.html";

    }

    public static String getUrl (String version) {
        return "https://ems.top-vision.cn:8111/ios/V"+version+"/EMS.html";

    }
}
