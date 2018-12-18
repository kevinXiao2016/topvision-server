package com.topvision.ems.cmc.performance.viewer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.topvision.framework.constants.Symbol;
import com.topvision.framework.utils.CmcIndexUtils;
import com.topvision.ems.cmc.util.ResourcesUtil;
import com.topvision.ems.performance.domain.ViewerParam;
import com.topvision.platform.util.highcharts.domain.Point;

public class UnBitErrorRateView extends AbstractView implements Serializable {
    private static final long serialVersionUID = -9078047591524040448L;

    public String getViewerType() {
        return "unBitErrorRate"; //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getViewerName() {
        return ResourcesUtil.getString("CCMTS.view.unBitErroRate.name"); //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getViewerUnit() {
        return ResourcesUtil.getString("CCMTS.view.unBitErroRate.unit"); //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getYTitle() {
        return ResourcesUtil.getString("CCMTS.view.unBitErroRate.ytitle"); //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getSeriesName(ViewerParam viewerParam) {
        return makePortName(viewerParam.getIndex());
    }

    public Double getYMin() {
        return 0D; //To change body of implemented methods use File | Settings | File Templates.
    }

    public Double getYMax() {
        return 100D; //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<Point> read(ViewerParam viewerParam) {
        List<Point> points = cmcPerfDao.unBitErrorRateRead(viewerParam);
        List<Point> pList = new ArrayList<Point>();
        for (Point point : points) {
            if (point.getY() >= 0) {
                pList.add(point);
            }
        }
        return pList;
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
        sb.append(type).append(" ").append(slotNo).append(Symbol.SLASH).append(ponNo).append(Symbol.SLASH)
                .append(cmcNo).append(Symbol.SLASH).append(chNo);
        return sb.toString();
    }

}
