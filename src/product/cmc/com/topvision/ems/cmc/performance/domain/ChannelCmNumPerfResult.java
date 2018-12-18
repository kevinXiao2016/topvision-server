/***********************************************************************
 * $Id: DsUserNumPerfResult.java,v1.0 2012-7-11 下午01:43:21 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.domain;

import java.util.ArrayList;
import java.util.List;

import com.topvision.framework.constants.Symbol;
import com.topvision.ems.cmc.facade.domain.ChannelCmNumStatic;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceResult;

/**
 * @author loyal
 * @created @2012-7-11-下午01:43:21
 * 
 */
public class ChannelCmNumPerfResult extends PerformanceResult<OperClass> {
    private static final long serialVersionUID = 7839241277065524824L;
    private Long cmcId;
    private List<ChannelCmNumStatic> channelCmNumStatics;
    private long dt;

    /**
     * @param domain
     */
    public ChannelCmNumPerfResult(OperClass domain) {
        super(domain);
    }

    /**
     * @return the cmcId
     */
    public Long getCmcId() {
        return cmcId;
    }

    /**
     * @param cmcId
     *            the cmcId to set
     */
    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    /**
     * @return the dt
     */
    public long getDt() {
        return dt;
    }

    /**
     * @param dt
     *            the dt to set
     */
    public void setDt(long dt) {
        this.dt = dt;
    }

    /**
     * @return the channelCmNumStatics
     */
    public List<ChannelCmNumStatic> getChannelCmNumStatics() {
        return channelCmNumStatics;
    }

    /**
     * @param channelCmNumStatics
     *            the channelCmNumStatics to set
     */
    public void setChannelCmNumStatics(List<ChannelCmNumStatic> channelCmNumStatics) {
        this.channelCmNumStatics = channelCmNumStatics;
    }

    /**
     * 添加 信道cm统计信息
     * 
     * @param channelCmNumStatic
     *            信道统计信息
     */
    public void addChannelCmNumStatic(ChannelCmNumStatic channelCmNumStatic) {
        if (channelCmNumStatics == null) {
            channelCmNumStatics = new ArrayList<ChannelCmNumStatic>();
        }
        channelCmNumStatics.add(channelCmNumStatic);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ChannelCmNumPerfResult [entityId=");
        builder.append(getEntityId());
        builder.append(", cmcId=");
        builder.append(cmcId);
        builder.append(", channelCmNumStatics=");
        builder.append(channelCmNumStatics);
        builder.append(", dt=");
        builder.append(dt);
        builder.append(Symbol.BRACKET_RIGHT);
        return builder.toString();
    }
}
