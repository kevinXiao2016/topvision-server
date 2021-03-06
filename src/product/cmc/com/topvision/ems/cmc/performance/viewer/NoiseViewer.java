/***********************************************************************
 * $ NoiseViewer.java,v1.0 2012-7-15 17:30:16 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.viewer;

import java.util.List;

import com.topvision.framework.constants.Symbol;
import com.topvision.framework.utils.CmcIndexUtils;
import com.topvision.ems.cmc.util.ResourcesUtil;
import com.topvision.ems.performance.domain.ViewerParam;
import com.topvision.platform.util.highcharts.domain.Point;

/**
 * @author jay
 * @created @2012-7-15-17:30:16
 */
public class NoiseViewer extends AbstractView {
    public String getViewerType() {
        return "noise"; //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getViewerName() {
        return ResourcesUtil.getString("CCMTS.view.noice.name"); //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getViewerUnit() {
        return ResourcesUtil.getString("CCMTS.view.noice.unit"); //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getYTitle() {
        return ResourcesUtil.getString("CCMTS.view.noice.ytitle"); //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getSeriesName(ViewerParam viewerParam) {
        return makePortName(viewerParam.getIndex()); //To change body of implemented methods use File | Settings | File Templates.
    }

    public Double getYMin() {
        return 0D; //To change body of implemented methods use File | Settings | File Templates.
    }

    public Double getYMax() {
        return 50D; //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<Point> read(ViewerParam viewerParam) {
        return cmcPerfDao.noiseRead(viewerParam);
    }

    public Point readLast(ViewerParam viewerParam) {
        return null; //To change body of implemented methods use File | Settings | File Templates.
    }

    private String makePortName(Long channelIndex) {
        String type = CmcIndexUtils.getChannelType(channelIndex) == 0 ? "US" : "DS";
        Long slotNo = CmcIndexUtils.getSlotNo(channelIndex);
        Long ponNo = CmcIndexUtils.getPonNo(channelIndex);
        Long cmcNo = CmcIndexUtils.getCmcId(channelIndex);
        Long chNo = CmcIndexUtils.getChannelId(channelIndex);
        StringBuilder sb = new StringBuilder();
        sb.append(type).append(" ").append(slotNo).append(Symbol.SLASH).append(ponNo).append(Symbol.SLASH).append(cmcNo).append(Symbol.SLASH)
                .append(chNo);
        return sb.toString();
    }
}
