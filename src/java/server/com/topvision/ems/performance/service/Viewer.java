/***********************************************************************
 * $ com.topvision.ems.performance.action.Viewer,v1.0 2012-7-15 15:58:41 $
 *
 * @author: Administrator
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.service;

import com.topvision.platform.util.highcharts.domain.Point;
import com.topvision.ems.performance.domain.ViewerParam;

import java.util.List;

/**
 * @author Administrator
 * @created @2012-7-15-15:58:41
 */
public interface Viewer {
    public String getViewerType();

    public String getViewerName();

    public String getViewerUnit();

    public String getYTitle();

    public String getSeriesName(ViewerParam viewerParam);

    public Double getYMin();

    public Double getYMax();

    public List<Point> read(ViewerParam viewerParam);

    public Point readLast(ViewerParam viewerParam);
}
