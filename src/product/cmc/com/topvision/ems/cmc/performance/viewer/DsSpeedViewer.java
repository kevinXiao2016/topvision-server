/***********************************************************************
 * $Id: DsSpeedViewer.java,v1.0 2012-7-19 下午03:15:16 $
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
 * @created @2012-7-19-下午03:15:16
 *
 */
public class DsSpeedViewer extends AbstractView {
    /* (non-Javadoc)
     * @see com.topvision.ems.performance.service.Viewer#getViewerType()
     */
    @Override
    public String getViewerType() {
        return "dsSpeed";
    }

    public String getViewerName() {
        return ResourcesUtil.getString("CCMTS.view.dsSpeed.name"); //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getViewerUnit() {
        return ResourcesUtil.getString("CCMTS.view.dsSpeed.unit"); //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getYTitle() {
        return ResourcesUtil.getString("CCMTS.view.dsSpeed.ytitle"); //To change body of implemented methods use File | Settings | File Templates.
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
        return 50D;
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.performance.service.Viewer#read(com.topvision.ems.performance.domain.ViewerParam)
     */
    @Override
    public List<Point> read(ViewerParam viewerParam) {
        return cmcPerfDao.dsSpeedRead(viewerParam);
        /*List<Point> points = cmcPerfDao.dsSpeedRead(viewerParam);
        if(points != null){
            double maxValue = points.get(0).getY();
            for(int i = 1; i < points.size(); i++){
                if(points.get(i).getY() > maxValue){
                    maxValue = points.get(i).getY();
                }
            }
            if(maxValue < 100 * 1024){
                viewerUnit = "bps";
                return points;
            }else if(maxValue < 100 * 1024 * 1024){
                //Kbps
                viewerUnit = "Kbps";
                return points;
            }else{
                //Mbps
                viewerUnit = "Mbps";
                return points;
            }
        }
        else{
            return null;
        }*/
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
