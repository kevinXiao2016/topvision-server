/***********************************************************************
 * $Id: PerfTargetAction.java,v1.0 2013-7-31 下午03:39:02 $
 * 
 * @author: fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.topvision.ems.performance.domain.PerfTarget;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.util.highcharts.domain.Point;

/**
 * @author fanzidong
 * @created @2013-7-31-下午03:39:02
 * @modify @2013-9-6 by fanzidong
 * 
 */
public class PerfTargetUtil {

    public static List<Point> samplePoints(List<Point> points) {
        if (points.size() <= 2304) {
            return points;
        } else {
            List<Point> sampledPonits = new ArrayList<Point>();
            int interval = points.size() / 2304 + 1;
            for (int i = 0; i < points.size(); i += interval) {
                sampledPonits.add(points.get(i));
            }
            // 加上最后一个点
            sampledPonits.add(points.get(points.size() - 1));
            return sampledPonits;
        }
    }

    public static List<Point> samplePoints(List<Point> points, Integer maxNum) {
        if(maxNum==null){
            samplePoints(points);
        }
        if (points.size() <= maxNum) {
            return points;
        } else {
            List<Point> sampledPonits = new ArrayList<Point>();
            int interval = points.size() / maxNum + 1;
            for (int i = 0; i < points.size(); i += interval) {
                sampledPonits.add(points.get(i));
            }
            // 加上最后一个点
            sampledPonits.add(points.get(points.size() - 1));
            return sampledPonits;
        }
    }

    public static String getString(String key, String module) {
        module = String.format("com.topvision.ems.%s.resources", module);
        try {
            ResourceManager resourceManager = ResourceManager.getResourceManager(module);
            return resourceManager.getNotNullString(key);
        } catch (Exception e) {
            return key;
        }
    }

    /**
     * 将一个java对象转换为hashmap的形式
     * 
     * @param object
     * @return
     */
    public static HashMap<String, Object> transfer_object_to_hashMap(Object object) {
        HashMap<String, Object> data = new HashMap<String, Object>();
        JSONObject jsonObject = JSONObject.fromObject(object);
        Iterator<?> it = jsonObject.keys();
        while (it.hasNext()) {
            String key = String.valueOf(it.next());
            Object value = jsonObject.get(key);
            if (value != null && value != "") {
                data.put(key, value);
            }
        }
        return data;
    }

    public static JSONObject groupPerfThresholdTarget(List<PerfTarget> perfTargetList) {
        Map<String, List<PerfTarget>> groupedPerfTargets = new HashMap<String, List<PerfTarget>>();
        for (PerfTarget perfTarget : perfTargetList) {
            perfTarget.setTargetDisplayName(PerfTargetUtil.getString(perfTarget.getTargetDisplayName(), "performance"));
            String groupName = PerfTargetUtil.getString(perfTarget.getTargetGroup(), "performance");
            if (groupedPerfTargets.containsKey(groupName)) {
                List<PerfTarget> pts = groupedPerfTargets.get(groupName);
                pts.add(perfTarget);
                groupedPerfTargets.put(groupName, pts);
            } else {
                List<PerfTarget> pts = new ArrayList<PerfTarget>();
                pts.add(perfTarget);
                groupedPerfTargets.put(groupName, pts);
            }
        }
        return JSONObject.fromObject(groupedPerfTargets);
    }

}
