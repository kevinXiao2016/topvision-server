package com.topvision.platform;

import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.framework.EnvironmentConstants;
import com.topvision.framework.common.DateUtils;

/**
 * 加载 WEB-INF/conf/config.properties.
 * 
 * @author niejun
 */
public final class SystemConstants {
    private final static Logger logger = LoggerFactory.getLogger(SystemConstants.class);
    public static int PAGE_SIZE = 25;
    public static String ROOT_REAL_PATH = "/";
    public static String WEB_INF_REAL_PATH = "/WEB-INF";

    /**
     * 由第一次HTTP请求后懒惰设置WEB应用名.
     */
    public static String WEBAPP_NAME = "ems";

    private static final SystemConstants sc = new SystemConstants();
    /**
     * 判断版本是开发版本还是发布版本
     */
    public static boolean isDevelopment;

    /**
     * 用于控制superadmin中的某些危险功能，默认关闭，临时开启使用后应该及时关闭
     */
    public static boolean isSuperMode = false;
    public static String osname = System.getProperty("os.name");

    public static void destroy() {
        sc.systemParam.clear();
    }

    public static SystemConstants getInstance() {
        return sc;
    }

    public static String getStartTime() {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        return DateUtils.FULL_S_FORMAT.format(new Date(runtimeMXBean.getStartTime()));
    }

    public static String getDuration() {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        return DateUtils.getTimeDesInObscure(System.currentTimeMillis() - runtimeMXBean.getStartTime(),
                SystemConstants.getInstance().getStringParam("language", "zh_CN"));
    }

    public static String getDuration(String lang) {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        return DateUtils.getTimeDesInObscure(System.currentTimeMillis() - runtimeMXBean.getStartTime(), lang);
    }

    private int pageSize = 25;

    private Properties systemParam = null;

    private SystemConstants() {
        systemParam = new Properties();
        // 根据版本号判断是否是发布版本，开发版本不检查用户名
        com.topvision.platform.SystemVersion version = new com.topvision.platform.SystemVersion();
        logger.info("SystemConstants.isDevelopment={}", version.getBuildVersion());
        if (version.getBuildNumber().equals("B001")) {
            isDevelopment = true;
        } else {
            isDevelopment = false;
        }
        EnvironmentConstants.putEnv("Version", version.toString());
    }

    public void initialize(InputStream is) throws IOException {
        Properties p = new Properties();
        p.load(is);
        sc.systemParam.putAll(p);
        if (System.getProperty("java.rmi.server.hostname") == null) {
            if ("".equals(sc.getStringParam("java.rmi.server.hostname", ""))) {
                EnvironmentConstants.putEnv("java.rmi.server.hostname", EnvironmentConstants.getHostAddress());
            } else {
                EnvironmentConstants.putEnv("java.rmi.server.hostname",
                        sc.systemParam.getProperty("java.rmi.server.hostname"));
            }
        }
    }

    public Boolean getBooleanParam(String key) {
        return Boolean.parseBoolean(systemParam.getProperty(key));
    }

    public Boolean getBooleanParam(String key, Boolean defaultValue) {
        return Boolean.parseBoolean(systemParam.getProperty(key, String.valueOf(defaultValue)));
    }

    public Integer getIntParam(String key) {
        return Integer.parseInt(systemParam.getProperty(key));
    }

    public Integer getIntParam(String key, Integer defaultValue) {
        return Integer.parseInt(systemParam.getProperty(key, String.valueOf(defaultValue)));
    }

    @Deprecated
    public String getLanguage() {
        return systemParam.getProperty("language", "zh_CN");
    }

    public Long getLongParam(String key) {
        return Long.parseLong(systemParam.getProperty(key));
    }

    public Long getLongParam(String key, Long defaultValue) {
        return Long.parseLong(systemParam.getProperty(key, String.valueOf(defaultValue)));
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public String getResourceName() {
        return systemParam.getProperty("resourceName", "resources");
    }

    public String getStringParam(String key) {
        return systemParam.getProperty(key);
    }

    public String getStringParam(String key, String defaultValue) {
        return systemParam.getProperty(key, defaultValue);
    }

    public Properties getSystemParam() {
        return systemParam;
    }

    public void putParam(String key, String value) {
        systemParam.put(key, value);
    }

    public void putParams(Map<String, String> params) {
        systemParam.putAll(params);
    }

    public void putParams(Properties params) {
        systemParam.putAll(params);
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        PAGE_SIZE = pageSize;
    }

}