/***********************************************************************
 * $Id: CmtsPerformanceHandle.java,v1.0 2013-9-29 上午9:29:47 $
 * 
 * @author: fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmts.performance.handle;

import javax.annotation.Resource;

import com.topvision.ems.network.domain.Port;
import com.topvision.ems.network.service.PortService;
import com.topvision.ems.performance.handle.PerformanceHandle;

/**
 * @author fanzidong
 * @created @2013-9-29-上午9:29:47
 * 
 */
public abstract class CmtsPerformanceHandle extends PerformanceHandle {

    @Resource(name = "portService")
    private PortService portService;

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

}
