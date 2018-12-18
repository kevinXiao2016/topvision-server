/***********************************************************************
 * $Id: ChannelUtilizationViewer.java,v1.0 2012-7-19 下午02:52:11 $
 * 
 * @author: loyal
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
 * @author loyal
 * @created @2012-7-19-下午02:52:11
 *
 */
public class ChannelUtilizationViewer extends AbstractView {
    /* (non-Javadoc)
     * @see com.topvision.ems.performance.service.Viewer#getViewerType()
     */
    @Override
    public String getViewerType() {
        return "channelUtilization";
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.performance.service.Viewer#getViewerName()
     */
    public String getViewerName() {
        return ResourcesUtil.getString("CCMTS.view.channelUtilization.name"); //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getViewerUnit() {
        return ResourcesUtil.getString("CCMTS.view.channelUtilization.unit"); //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getYTitle() {
        return ResourcesUtil.getString("CCMTS.view.channelUtilization.ytitle"); //To change body of implemented methods use File | Settings | File Templates.
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.performance.service.Viewer#getSeriesName(com.topvision.ems.performance.domain.ViewerParam)
     */
    @Override
    public String getSeriesName(ViewerParam viewerParam) {
        return makePortName(viewerParam.getIndex());
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.performance.service.Viewer#getYMin()
     */
    @Override
    public Double getYMin() {
        return 0D;
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.performance.service.Viewer#getYMax()
     */
    @Override
    public Double getYMax() {
        return 100D;
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.performance.service.Viewer#read(com.topvision.ems.performance.domain.ViewerParam)
     */
    @Override
    public List<Point> read(ViewerParam viewerParam) {
        return cmcPerfDao.channelUtilizationRead(viewerParam);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.performance.service.Viewer#readLast(com.topvision.ems.performance.domain.ViewerParam)
     */
    @Override
    public Point readLast(ViewerParam viewerParam) {
        return null;
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
