package com.topvision.ems.cmc.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.topvision.framework.constants.Symbol;
import com.topvision.platform.ResourceManager;

/**
 * 增加一个资源工具类，内部将各个需要的资源文件路径放到一个map中，然后给定一个简称作为模块名称。
 * 
 * @author lzs
 * @created @2012-9-26-下午03:54:46
 * 
 */
public class ResourcesUtil {

    /**
     * 模块简称：CMC
     */
    public static String MODEL_CMC = "cmc";
    public static String MODEL_EMS = "ems";
    public static String MODEL_NETWORK = "network";
    public static String MODEL_MOBILE = "mobile";

    private static Map<String, String> modelMap = new HashMap<String, String>();
    /**
     * 如需要增加资源文件路径，必须采用如下方式增加。
     */
    static {
        modelMap.put(MODEL_CMC, "com.topvision.ems.cmc.resources");
        modelMap.put(MODEL_EMS, "com.topvision.ems.resources.resources");
        modelMap.put(MODEL_NETWORK, "com.topvision.ems.network.resources");
        modelMap.put(MODEL_MOBILE, "com.topvision.ems.mobile.resources");
    }

    /**
     * 这个方法内部自动获取当前用户的 语言。
     * 
     * @param model
     *            模块名，尽量使用这个类的静态常量，也可以使用资源文件的全路径
     * @param key
     *            资源文件中的Key
     * @return 如果资源文件中有，则返回对应的值，否则返回 key
     */
    public static String getString(String model, String key) {
        String value = null;
        /**
         * 遍历所有model
         */
        if (model == null) {
            Collection<String> paths = modelMap.values();
            for (String path : paths) {
                ResourceManager resources = ResourceManager.getResourceManager(path);
                value = resources.getValue(key, null);
                if (value != null) {
                    break;
                }
            }
            if (value == null) {
                value = key;
            }
        } else {
            String resourcesPath = modelMap.get(model);

            if (StringUtils.isEmpty(resourcesPath)) {
                resourcesPath = model;
            }

            ResourceManager resources = ResourceManager.getResourceManager(resourcesPath);
            value = resources.getNotNullString(key);
        }

        return value;
    }

    /**
     * 遍历所有model,直到有值，若都没值，则返回key 默认约束：所有model不会存在重复的key
     * 
     * @param key
     * @return
     */
    public static String getString(String key) {
        return getString(null, key);
    }

    /**
     * 遍历所有model,直到有值，若都没值，则返回key 默认约束：所有model不会存在重复的key 将{i}中替换为数组中内容
     * 
     * @param key
     * @return
     */
    public static String getString(String key, String... strings) {
        String v = getString(key);
        if (v == null) {
            v = key;
        } else {
            for (int i = 0; i < strings.length; i++) {
                v = v.replace(Symbol.BRACE_LEFT + i + Symbol.BRACE_RIGHT, strings[i]);
            }
        }
        return v;
    }
}
