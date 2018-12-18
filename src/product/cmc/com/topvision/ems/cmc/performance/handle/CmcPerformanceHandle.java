/***********************************************************************
 * $Id: CmcPerformanceHandle.java,v1.0 2013-6-16 上午10:20:58 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.handle;

import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.framework.utils.CmcIndexUtils;
import com.topvision.ems.network.domain.Port;
import com.topvision.ems.network.service.PortService;
import com.topvision.ems.performance.handle.PerformanceHandle;
import com.topvision.ems.performance.service.PerformanceStatistics;

/**
 * @author Rod John
 * @created @2013-6-16-上午10:20:58
 * 
 */
public abstract class CmcPerformanceHandle extends PerformanceHandle {
    @Autowired
    protected PortService portService;
    @Autowired
    protected PerformanceStatistics performanceStatisticsCenter;

    /**
     * 获取CMC的告警Source描述
     * 
     * Available Only For Channel
     * 
     * @param ifIndex
     * @return
     */
    public String getCmcSourceString(Long ifIndex) {
        StringBuilder source = new StringBuilder();
        int channelId = CmcIndexUtils.getChannelId(ifIndex).intValue();
        if (channelId == 0) {
            return null;
        }
        if (CmcIndexUtils.getChannelType(ifIndex) == 0) {
            source.append("[U]").append(getString("PerformanceAlert.upStream", "cmc"))
                    .append(CmcIndexUtils.getChannelId(ifIndex));
        } else if (CmcIndexUtils.getChannelType(ifIndex) == 1) {
            source.append("[D]").append(getString("PerformanceAlert.downStream", "cmc"))
                    .append(CmcIndexUtils.getChannelId(ifIndex));
        }
        return source.toString();
    }

    /**
     * 获取cmts的告警Source描述 add by loyal 添加cmts阈值告警支持
     * 
     * @param typeId
     *            , entityId, ifIndex
     * @return
     */
    public String getCmtsChannelSourceString(Long typeId, Long entityId, Long ifIndex) {
        // StringBuilder source = new StringBuilder();
        Port port = portService.getPort(entityId, ifIndex);
        /*
         * int channelType = CmcUtil.getCmtsChannelType(typeId, new Long(port.getIfType()),
         * entityTypeService); if (channelType == 0) {
         * source.append("[U]").append(getString("PerformanceAlert.upStream", "cmc"))
         * .append(CmcIndexUtils.getCmtsUpChannelIndex(typeId, port.getIfDescr(),
         * entityTypeService)); } else if (channelType == 1) {
         * source.append("[D]").append(getString("PerformanceAlert.downStream", "cmc"))
         * .append(CmcIndexUtils.getCmtsDownChannelIndex(typeId, port.getIfDescr(),
         * entityTypeService)); }
         */
        return port.getIfName();
    }

    /**
     * 获取CMTS 上联口告警Source描述
     * 
     * @param ifIndex
     * @return
     */
    public String getCmtsUpLinkSourceString(Long entityId, Long ifIndex) {
        Port port = portService.getPort(entityId, ifIndex);
        return port.getIfDescr();
    }

    public String getCmc8800BSourceString(Long ifIndex) {
        return null;
    }

}
