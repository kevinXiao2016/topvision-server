/***********************************************************************
 * $Id: PonCmNumOnlineView.java,v1.0 2012-7-19 下午04:20:55 $
 * 
 * @author: dosion
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.viewer;

import com.topvision.framework.constants.Symbol;
import com.topvision.ems.cmc.util.ResourcesUtil;
import com.topvision.ems.performance.domain.ViewerParam;
import com.topvision.framework.utils.EponIndex;
import com.topvision.platform.util.highcharts.domain.Point;

import java.io.Serializable;
import java.util.List;

/**
 * @author dosion
 * @created @2012-7-19-下午04:20:55
 *
 */
public class PonCmNumOnlineView extends AbstractView implements Serializable {
    private static final long serialVersionUID = -7830576529994927680L;

    public String getViewerType() {
        return "ponCmNumOnline";    //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getViewerName() {
        return ResourcesUtil.getString("CCMTS.view.cmNumOnline.name");    //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getViewerUnit() {
        return ResourcesUtil.getString("CCMTS.view.cmNumOnline.unit"); //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getYTitle() {
        return ResourcesUtil.getString("CCMTS.view.cmNumOnline.ytitle");    //To change body of implemented methods use File | Settings | File Templates.
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
        return cmcPerfDao.ponCmNumOnlineRead(viewerParam);
    }

    public Point readLast(ViewerParam viewerParam) {
        return null;
    }

    private String makePortName(Long ifIndex) {
        Long slotNo = EponIndex.getSlotNo(ifIndex);
        Long ponNo = EponIndex.getPonNo(ifIndex);
        StringBuilder sb = new StringBuilder();
        sb.append(slotNo).append(Symbol.SLASH).append(ponNo);
        return sb.toString();
    }
}
