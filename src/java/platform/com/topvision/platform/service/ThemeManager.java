/**
 *
 */
package com.topvision.platform.service;

import java.awt.Color;
import java.awt.Paint;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import com.topvision.framework.common.ColorUtils;

/**
 * 加载 WEB-INF/conf/theme.properties. 为报表等具有图形展示的模块提供动态的颜色变化配置.
 * 
 * @author niejun
 */
public class ThemeManager {
    private static ThemeManager theme = new ThemeManager();

    private Properties systemParam = null;
    private Map<String, Color> colorMapping = new Hashtable<String, Color>();

    private ThemeManager() {
        systemParam = new Properties();
    }

    /**
     * 清除参数
     */
    public static void destroy() {
        theme.systemParam.clear();
    }

    /**
     * 获得某个参数的BOOLEAN值
     * 
     * @param key
     * @param dftValue
     * @return
     */
    public static Boolean getBoolean(String key, Boolean dftValue) {
        String v = theme.systemParam.getProperty(key);
        if (v == null) {
            return dftValue;
        } else {
            return "true".equalsIgnoreCase(v);
        }
    }

    /**
     * 获得某个参数的Color值
     * 
     * @param key
     * @param dftValue
     * @return
     */
    public static Color getColor(String key, Color dftValue) {
        String v = theme.systemParam.getProperty(key);
        if (v == null) {
            return dftValue;
        } else {
            Color c = theme.colorMapping.get(key);
            if (c == null) {
                c = ColorUtils.decode(v);
                theme.colorMapping.put(key, c);
            }
            return c;
        }
    }

    /**
     * 获得某个参数的Paint对象
     * 
     * @param key
     * @param dftValue
     * @return
     */
    public static Paint getColor(String key, Paint dftValue) {
        String v = theme.systemParam.getProperty(key);
        if (v == null) {
            return dftValue;
        } else {
            Color c = theme.colorMapping.get(key);
            if (c == null) {
                c = ColorUtils.decode(v);
                theme.colorMapping.put(key, c);
            }
            return c;
        }
    }

    /**
     * 获得默认主题
     * 
     * @return
     */
    public static String getDefaultTheme() {
        String v = theme.systemParam.getProperty("default.style");
        return v == null ? "default" : v;
    }

    /**
     * 主题实例
     * 
     * @return ThemeManager
     */
    public static ThemeManager getInstance() {
        return theme;
    }

    /**
     * 初始化
     * 
     * @param is
     * @throws IOException
     */
    public static void initialize(InputStream is) throws IOException {
        theme.systemParam.load(is);
    }
}
