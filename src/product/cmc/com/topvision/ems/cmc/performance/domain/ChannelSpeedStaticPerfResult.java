/***********************************************************************
 * $Id: ChannelSpeedStaticPerfResult.java,v1.0 2012-7-15 上午11:22:50 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.domain;

import java.util.ArrayList;
import java.util.List;

import com.topvision.ems.cmc.facade.domain.CmcDownPortMonitorDomain;
import com.topvision.ems.cmc.facade.domain.CmcPortMonitorDomain;
import com.topvision.ems.cmc.facade.domain.CmcUpPortMonitorDomain;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceResult;

/**
 * @author loyal
 * @created @2012-7-15-上午11:22:50
 * 
 */
public class ChannelSpeedStaticPerfResult extends PerformanceResult<OperClass> {
    private static final long serialVersionUID = -6111725726747306725L;
    private Long cmcId;
    private List<CmcPortMonitorDomain> cmcPortPerfs;
    private List<CmcUpPortMonitorDomain> cmcUpPortPerfs;
    private List<CmcDownPortMonitorDomain> cmcDownPortPerfs;
    private long dt;

    /**
     * @param domain
     */
    public ChannelSpeedStaticPerfResult(OperClass domain) {
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
     * @return the cmcPortPerfs
     */
    public List<CmcPortMonitorDomain> getCmcPortPerfs() {
        return cmcPortPerfs;
    }

    /**
     * @param cmcPortPerfs
     *            the cmcPortPerfs to set
     */
    public void setCmcPortPerfs(List<CmcPortMonitorDomain> cmcPortPerfs) {
        this.cmcPortPerfs = cmcPortPerfs;
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
     * 添加 信道速率统计信息
     * 
     * @param cmcPortPerf
     *            信道统计信息
     */
    public void addCmcPortPerf(CmcPortMonitorDomain cmcPortPerf) {
        if (cmcPortPerfs == null) {
            cmcPortPerfs = new ArrayList<CmcPortMonitorDomain>();
        }
        cmcPortPerfs.add(cmcPortPerf);
    }

    public List<CmcUpPortMonitorDomain> getCmcUpPortPerfs() {
        return cmcUpPortPerfs;
    }

    public void setCmcUpPortPerfs(List<CmcUpPortMonitorDomain> cmcUpPortPerfs) {
        this.cmcUpPortPerfs = cmcUpPortPerfs;
    }
    
    public void addCmcUpPortPerf(CmcUpPortMonitorDomain cmcUpPortPerf) {
        if (cmcUpPortPerfs == null) {
            cmcUpPortPerfs = new ArrayList<CmcUpPortMonitorDomain>();
        }
        cmcUpPortPerfs.add(cmcUpPortPerf);
    }
    
    public void addCmcDownPortPerf(CmcDownPortMonitorDomain cmcDownPortPerf) {
        if (cmcDownPortPerfs == null) {
            cmcDownPortPerfs = new ArrayList<CmcDownPortMonitorDomain>();
        }
        cmcDownPortPerfs.add(cmcDownPortPerf);
    }
    
    public List<CmcDownPortMonitorDomain> getCmcDownPortPerfs() {
        return cmcDownPortPerfs;
    }

    public void setCmcDownPortPerfs(List<CmcDownPortMonitorDomain> cmcDownPortPerfs) {
        this.cmcDownPortPerfs = cmcDownPortPerfs;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ChannelSpeedStaticPerfResult [cmcId=");
        builder.append(cmcId);
        builder.append(", cmcPortPerfs=");
        builder.append(cmcPortPerfs);
        builder.append(", cmcUpPortPerfs=");
        builder.append(cmcUpPortPerfs);
        builder.append(", cmcDownPortPerfs=");
        builder.append(cmcDownPortPerfs);
        builder.append(", dt=");
        builder.append(dt);
        builder.append("]");
        return builder.toString();
    }
    
}
