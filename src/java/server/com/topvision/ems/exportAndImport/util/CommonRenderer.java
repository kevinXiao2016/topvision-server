/***********************************************************************
 * $Id: CommonRenderer.java,v1.0 2017年2月8日 下午4:48:53 $
 * 
 * @author: vanzand
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.exportAndImport.util;

/**
 * 通用的renderer
 * 
 * @author vanzand
 * @created @2017年2月8日-下午4:48:53
 */
public class CommonRenderer {
    public static final String RENDER_MAC = "renderMac";
    public static final String RENDER_DOUBLE = "renderDouble"; // 将double数据格式化
    public static final String RENDER_TEMPERATURE = "renderTemperature";
    public static final String RENDER_PERCENT = "renderPercent";
    public static final String RENDER_PERCENT_100 = "percentRendererIn100";
    
    public static String renderValue(String render, String value) {
        if (render == null || render.equals("")) {
            return value;
        }
        String ret = null;
        try {
            switch (render) {
            case RENDER_PERCENT:
                // 转换百分比
                double per_value = Double.valueOf(value).doubleValue() * 100;
                ret = String.format("%.2f", per_value) + "%";
                break;
            case RENDER_PERCENT_100:
                // 转换百分比
                double per_value_100 = Double.valueOf(value).doubleValue();
                ret = String.format("%.2f", per_value_100) + "%";
                break;
            default:
                ret = value;
            }
        } catch (Exception e) {
            ret = value;
        }
        return ret;
    }
}
