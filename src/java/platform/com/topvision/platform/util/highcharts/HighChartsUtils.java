/***********************************************************************
 * $ HighChartsUtils.java,v1.0 2012-7-13 8:54:33 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.util.highcharts;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.DefaultValueProcessor;

import com.topvision.exception.service.JsonException;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.util.highcharts.domain.Chart;
import com.topvision.platform.util.highcharts.domain.Credits;
import com.topvision.platform.util.highcharts.domain.Global;
import com.topvision.platform.util.highcharts.domain.Highcharts;
import com.topvision.platform.util.highcharts.domain.Legend;
import com.topvision.platform.util.highcharts.domain.PlotOptions;
import com.topvision.platform.util.highcharts.domain.Subtitle;
import com.topvision.platform.util.highcharts.domain.Title;
import com.topvision.platform.util.highcharts.domain.Tooltip;
import com.topvision.platform.util.highcharts.domain.Xaxis;
import com.topvision.platform.util.highcharts.domain.Yaxis;

/**
 * @author jay
 * @created @2012-7-13-8:54:33
 */
public class HighChartsUtils {
    /**
     * 创建一个没有曲线的图形框架
     * 
     * @param chartId
     *            页面上div的id
     * @param titleString
     *            主标题
     * @param yTitle
     *            y轴的名称
     * @param width
     *            曲线图的宽
     * @param height
     *            曲线图的高
     * @return Highcharts
     */
    public static Highcharts createDefaultLineXdateTimeChart(String chartId, String titleString, String yTitle,
            Integer width, Integer height) {
        Highcharts highcharts = new Highcharts();
        Chart chart = ChartUtils.createDefaultChart(chartId, "line", width, height);
        highcharts.setChart(chart);
        if (titleString != null) {
            Title title = TitleUtils.createDefaultTitle(titleString);
            highcharts.setTitle(title);
        }
        Subtitle subtitle = SubtitleUtils.createDefaultTitle(getResourceString("HighChartsUtils.moveToSelectViewTimeRange","com.topvision.ems.resources.resources"));
        subtitle.setY(40);
        highcharts.setSubtitle(subtitle);
        List<Xaxis> xaxis = XaxisUtils.createDefaultXaxisArray(null, "datetime");
        highcharts.setxAxis(xaxis);
        if (yTitle == null) {
            yTitle = "";
        }
        List<Yaxis> yaxis = YaxisUtils.createDefaultYaxisArray(null, yTitle);
        highcharts.setyAxis(yaxis);
        Global global = GlobalUtils.createDefaultGlobal();
        highcharts.setGlobal(global);
        Tooltip tooltip = TooltipUtils.createDefaultTooltip();
        highcharts.setTooltip(tooltip);
        Legend legend = LegendUtils.createDefaultLegend();
        highcharts.setLegend(legend);
        PlotOptions plotOptions = PlotOptionsUtils.createDefaultPlotOptions();
        highcharts.setPlotOptions(plotOptions);
        Credits credits = CreditsUtils.createDefaultCredits();
        highcharts.setCredits(credits);
        return highcharts;
    }

    public static JSONArray toJSONArray(Object o) {
        if (!(o instanceof Collection)) {
            throw new JsonException("object is not a collection");
        }
        JsonConfig jsonConfig = getJsonConfig();
        JSONArray jsonArray = JSONArray.fromObject(o, jsonConfig);
        checkArray(jsonArray);
        return jsonArray;
    }

    private static JsonConfig getJsonConfig() {
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.registerDefaultValueProcessor(Integer.class, new DefaultValueProcessor() {
            @SuppressWarnings("rawtypes")
            public Object getDefaultValue(Class type) {
                return JSONNull.getInstance();
            }
        });
        jsonConfig.registerDefaultValueProcessor(Double.class, new DefaultValueProcessor() {
            @SuppressWarnings("rawtypes")
            public Object getDefaultValue(Class type) {
                return JSONNull.getInstance();
            }
        });
        jsonConfig.registerDefaultValueProcessor(String.class, new DefaultValueProcessor() {
            @SuppressWarnings("rawtypes")
            public Object getDefaultValue(Class type) {
                return JSONNull.getInstance();
            }
        });
        jsonConfig.registerDefaultValueProcessor(Boolean.class, new DefaultValueProcessor() {
            @SuppressWarnings("rawtypes")
            public Object getDefaultValue(Class type) {
                return JSONNull.getInstance();
            }
        });
        return jsonConfig;
    }

    public static JSONObject toJSONObject(Object o) {
        if (o instanceof Collection) {
            throw new JsonException("object is a collection");
        }

        JsonConfig jsonConfig = getJsonConfig();
        JSONObject jsonObject = JSONObject.fromObject(o, jsonConfig);
        filterNullSeg(jsonObject);
        return jsonObject;
    }

    public static String toJSON(Object o) {
        JsonConfig jsonConfig = getJsonConfig();
        String json;
        if (o instanceof Collection) {
            JSONArray jsonArray = JSONArray.fromObject(o, jsonConfig);
            checkArray(jsonArray);
            json = jsonArray.toString();
        } else {
            JSONObject jsonObject = JSONObject.fromObject(o, jsonConfig);
            filterNullSeg(jsonObject);
            json = jsonObject.toString();
        }
        return json;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static void filterNullSeg(JSONObject json) {
        Set set = new HashSet();
        for (Object o : json.keySet()) {
            if (json.containsKey(o)) {
                Object v = json.get(o);
                if (v instanceof JSONArray) {
                    JSONArray jsonArray = (JSONArray) v;
                    if (jsonArray.size() == 0) {
                        set.add(o);
                    } else {
                        for (Object o1 : jsonArray) {
                            checkArray(o1);
                        }
                    }
                }
                if (v instanceof JSONNull) {
                    set.add(o);
                }
                if (v instanceof JSONObject) {
                    JSONObject jsonObject = (JSONObject) v;
                    filterNullSeg(jsonObject);
                }
            }
        }
        for (Object o : set) {
            json.remove(o);
        }
    }

    private static void checkArray(Object o) {
        if (o instanceof JSONArray) {
            JSONArray jsonArray = (JSONArray) o;
            for (Object o1 : jsonArray) {
                checkArray(o1);
            }
        } else if (o instanceof JSONObject) {
            JSONObject jsonObject = (JSONObject) o;
            filterNullSeg(jsonObject);
        }
    }

    /*
     * key：properties文件的keymodule：资源文件
     */
    protected static String getResourceString(String key, String module) {
        try {
            ResourceManager resourceManager = ResourceManager.getResourceManager(module);
            return resourceManager.getNotNullString(key);
        } catch (Exception e) {
            return key;
        }
    }
}
