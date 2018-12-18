/***********************************************************************
 * $Id: CmcOpticalReveiverInfoView.java,v1.0 2013-12-16 下午4:05:09 $
 * 
 * @author: dosion
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.viewer;

import java.util.List;

import com.topvision.ems.cmc.optical.dao.CmcOpticalReceiverDao;
import com.topvision.ems.cmc.util.ResourcesUtil;
import com.topvision.ems.performance.domain.ViewerParam;
import com.topvision.platform.util.highcharts.domain.Point;

/**
 * @author dosion
 * @created @2013-12-16-下午4:05:09
 * 
 */
public class CmcOpticalReveiverInfoView extends AbstractView {
    private CmcOpticalReceiverDao cmcOpticalReceiverDao;

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.service.Viewer#getViewerType()
     */
    @Override
    public String getViewerType() {
        return "opticalReceiver";
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.service.Viewer#getViewerName()
     */
    @Override
    public String getViewerName() {
        return ResourcesUtil.getString("CCMTS.view.optical.name");
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.service.Viewer#getViewerUnit()
     */
    @Override
    public String getViewerUnit() {
        return ResourcesUtil.getString("CCMTS.view.optical.unit");
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.service.Viewer#getYTitle()
     */
    @Override
    public String getYTitle() {
        return ResourcesUtil.getString("CCMTS.view.optical.ytitle");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.performance.service.Viewer#getSeriesName(com.topvision.ems.performance.
     * domain.ViewerParam)
     */
    @Override
    public String getSeriesName(ViewerParam viewerParam) {
        return makePortName(viewerParam.getIndex());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.service.Viewer#getYMin()
     */
    @Override
    public Double getYMin() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.service.Viewer#getYMax()
     */
    @Override
    public Double getYMax() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.service.Viewer#read(com.topvision.ems.performance.domain.
     * ViewerParam)
     */
    @Override
    public List<Point> read(ViewerParam viewerParam) {
        return cmcOpticalReceiverDao.selectOpReceiverInputInfoHis(viewerParam.getCmcId(), viewerParam.getIndex(),
                viewerParam.getStartTime().toString(), viewerParam.getEndTime().toString());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.performance.service.Viewer#readLast(com.topvision.ems.performance.domain
     * .ViewerParam)
     */
    @Override
    public Point readLast(ViewerParam viewerParam) {
        return null;
    }

    public CmcOpticalReceiverDao getCmcOpticalReceiverDao() {
        return cmcOpticalReceiverDao;
    }

    public void setCmcOpticalReceiverDao(CmcOpticalReceiverDao cmcOpticalReceiverDao) {
        this.cmcOpticalReceiverDao = cmcOpticalReceiverDao;
    }

    private String makePortName(Long channelIndex) {
        Long mask = 7L;
        Long id = mask & channelIndex / 2;
        char c = 'A';
        if (id == 1) {
            c = 'A';
        } else {
            c = 'B';
        }
        return ResourcesUtil.getString("CMC.optical.receiverPower") + c;

    }

}
