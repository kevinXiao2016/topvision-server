/***********************************************************************
 * $ LabelsUtils.java,v1.0 2012-7-13 16:40:37 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.util.highcharts;

import com.topvision.platform.util.highcharts.domain.Labels;
import com.topvision.platform.util.highcharts.domain.Items;

import java.util.List;
import java.util.ArrayList;

/**
 * @author jay
 * @created @2012-7-13-16:40:37
 */
public class LabelsUtils {
    public static Labels createDefaultLabels(Labels labels, String html, Integer left, Integer top, String color) {
        if (labels == null) {
            labels = new Labels();
        }
        List<Items> items = createItemForLabels(labels.getItems(), html, left, top, color);
        labels.setItems(items);
        return labels; // To change body of created methods use File | Settings | File Templates.
    }

    private static List<Items> createItemForLabels(List<Items> list, String html, Integer left, Integer top,
            String color) {
        if (list == null) {
            list = new ArrayList<Items>();
        }
        Items items = new Items();
        items.setHtml(html);
        Items.Style style = items.new Style();
        style.setLeft(left);
        style.setTop(top);
        style.setColor(color);
        items.setStyle(style);
        list.add(items);
        return list;
    }
}
