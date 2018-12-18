/**
 * 
 */
package com.topvision.ems.devicesupport.version.util;

import com.topvision.ems.devicesupport.version.DeviceVersionXmlParser;

/**
 * @author dosion
 * 
 */
public class DeviceFuctionSupport {
    private static DeviceFuctionSupport instance = new DeviceFuctionSupport();
    private final DeviceVersionXmlParser xmlParser;
    
    private DeviceFuctionSupport() {
        xmlParser = DeviceVersionXmlParser.getInstance();
    }

    /**
     * 获取参数指定参数的值
     * 
     * @param typeId
     *            设备类型
     * @param versionName
     *            版本名称,版本名称严格区分大小写
     * @param functionName
     *            功能名称,功能名称严格区分大小写
     * @param paramName
     *            参数名称,参数名称严格区分大小写
     * @return 参数名称对应的值，当不存在时返回null
     */
    public static String getParamValue(Long typeId, String versionName, String functionName, String paramName) {
        if (typeId == null || versionName == null || functionName == null || paramName == null) {
            return null;
        }
        return instance.xmlParser.getParamValue(typeId, versionName, functionName, paramName);
    }

    /**
     * 获取指定的功能是否支持
     * 
     * @param typeId
     *            设备类型
     * @param versionName
     *            版本名称,版本名称严格区分大小写
     * @param functionName
     *            功能名称,功能名称严格区分大小写
     * @return true：支持，false：不支持
     */
    public static boolean isSupportFunction(Long typeId, String versionName, String functionName) {
        if (typeId == null || versionName == null || functionName == null) {
            return false;
        }
        return instance.xmlParser.isSupportFunction(typeId, versionName, functionName);
    }

    /**
     * 比较两个版本的大小
     * 
     * @param srcVersion
     * @param dstVersion
     * @return EQUAL(0): srcVersion = dstVersion LT(-1): srcVersion < dstVersion GT(1): scrVersion >
     *         dstVersion ERROR(-2): 两个版本格式不一致，无法比较
     */
    public static Integer compareVersion(String srcVersion, String dstVersion) {
        return instance.xmlParser.compareVersion(srcVersion, dstVersion);
    }
    
}