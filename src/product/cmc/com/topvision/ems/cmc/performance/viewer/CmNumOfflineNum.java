/***********************************************************************
 * $Id: CmNumOfflineNum.java,v1.0 2012-7-19 下午04:42:08 $
 * 
 * @author: dosion
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.viewer;

import java.io.Serializable;
import java.util.List;

import com.topvision.framework.constants.Symbol;
import com.topvision.framework.utils.CmcIndexUtils;
import com.topvision.ems.cmc.util.ResourcesUtil;
import com.topvision.ems.performance.domain.ViewerParam;
import com.topvision.platform.util.highcharts.domain.Point;

/**
 * @author dosion
 * @created @2012-7-19-下午04:42:08
 *
 */
public class CmNumOfflineNum  extends AbstractView  implements Serializable {
    private static final long serialVersionUID = -6947840878618117015L;

    public String getViewerType() {
        return "cmNumOffline";    //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getViewerName() {
        return ResourcesUtil.getString("CCMTS.view.cmNumOffline.name");    //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getViewerUnit() {
        return ResourcesUtil.getString("CCMTS.view.cmNumOffline.unit"); //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getYTitle() {
        return ResourcesUtil.getString("CCMTS.view.cmNumOffline.ytitle");    //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getSeriesName(ViewerParam viewerParam) {
        return makePortName(viewerParam.getIndex());
    }

    public Double getYMin() {
        return 0D;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Double getYMax() {
        return 256D;    //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<Point> read(ViewerParam viewerParam) {
        return cmcPerfDao.cmNumOfflineRead(viewerParam);
    }

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
        sb.append(type).append(" ").append(slotNo).append(Symbol.SLASH).append(ponNo).append(Symbol.SLASH).append(cmcNo).append(Symbol.SLASH).append(chNo);
        return sb.toString();
    }
}
