/***********************************************************************
 * $ Labels.java,v1.0 2012-7-12 14:25:13 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.util.highcharts.domain;

import java.util.List;
import java.io.Serializable;

/**
 * @author jay
 * @created @2012-7-12-14:25:13 A HTML label that can be positioined anywhere in the chart area
 */
public class Labels implements Serializable {
    private static final long serialVersionUID = 7342180206202447869L;
    private List<Items> items;

    public List<Items> getItems() {
        return items;
    }

    public void setItems(List<Items> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Labels");
        sb.append("{items=").append(items);
        sb.append('}');
        return sb.toString();
    }
}
