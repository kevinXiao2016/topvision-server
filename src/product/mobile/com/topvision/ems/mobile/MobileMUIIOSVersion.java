package com.topvision.ems.mobile;


public class MobileMUIIOSVersion {
    public static final Integer build = 6;
    public static final String version = "1.0.0.5";
    public static final Boolean mandatory = false;

    public static String getTestUrl (String version) {
        return "https://ems.top-vision.cn:8111/terminal/ios/test/V"+version+"/terminal.html";

    }

    public static String getUrl (String version) {
        return "https://ems.top-vision.cn:8111/terminal/ios/V"+version+"/terminal.html";

    }
}
