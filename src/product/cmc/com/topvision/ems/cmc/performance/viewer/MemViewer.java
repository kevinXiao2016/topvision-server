/***********************************************************************
 * $ MemViewer.java,v1.0 2012-7-16 17:47:18 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.viewer;

import java.util.List;

import com.topvision.ems.cmc.util.ResourcesUtil;
import com.topvision.ems.performance.domain.ViewerParam;
import com.topvision.platform.util.highcharts.domain.Point;

/**
 * @author jay
 * @created @2012-7-16-17:47:18
 */
public class MemViewer extends AbstractView {
    public String getViewerType() {
        return "mem"; //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getViewerName() {
        return ResourcesUtil.getString("CCMTS.view.mem.name"); //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getViewerUnit() {
        return ResourcesUtil.getString("CCMTS.view.mem.unit"); //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getYTitle() {
        return ResourcesUtil.getString("CCMTS.view.mem.ytitle"); //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getSeriesName(ViewerParam viewerParam) {
        return ResourcesUtil.getString("type.Mem"); //To change body of implemented methods use File | Settings | File Templates.
    }

    public Double getYMin() {
        return 0D; //To change body of implemented methods use File | Settings | File Templates.
    }

    public Double getYMax() {
        return 100D; //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<Point> read(ViewerParam viewerParam) {
        return cmcPerfDao.memRead(viewerParam); //To change body of implemented methods use File | Settings | File Templates.
    }

    public Point readLast(ViewerParam viewerParam) {
        return null; //To change body of implemented methods use File | Settings | File Templates.
    }
}
