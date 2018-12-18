/***********************************************************************
 * $Id: CmtsFlowQualityPerfResult.java,v1.0 2014-4-17 上午10:04:17 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmts.performance.domain;

import java.util.ArrayList;
import java.util.List;

import com.topvision.ems.cmc.performance.facade.CmtsFlowQuality;
import com.topvision.ems.cmc.performance.facade.CmtsXFlowQuality;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceResult;

/**
 * @author Rod John
 * @created @2014-4-17-上午10:04:17
 * 
 */
public class CmtsFlowQualityPerfResult extends PerformanceResult<OperClass> {
    private static final long serialVersionUID = 7752093168176083497L;

    public static final String SAMPLE_COLLECT = "Sample_Collect";
    public static final String INTERVAL_COLLECT = "Interval_Collect";

    private List<CmtsFlowQuality> cmtsFlowQualities = new ArrayList<>();
    private String collectType;

    /**
     * @param domain
     */
    public CmtsFlowQualityPerfResult(OperClass domain) {
        super(domain);
    }

    /**
     * @return the cmtsFlowQualities
     */
    public List<CmtsFlowQuality> getCmtsFlowQualities() {
        return cmtsFlowQualities;
    }

    /**
     * @param cmtsFlowQualities
     *            the cmtsFlowQualities to set
     */
    public void setCmtsFlowQualities(List<CmtsFlowQuality> cmtsFlowQualities) {
        this.cmtsFlowQualities = cmtsFlowQualities;
    }

    public void addCmtsXFlowQualites(List<CmtsXFlowQuality> cmtsXFlowQualities) {
        for (CmtsXFlowQuality xFlowQuality : cmtsXFlowQualities) {
            this.cmtsFlowQualities.add(new CmtsFlowQuality(xFlowQuality));
        }
    }

    public void addCmtsFlowQualites(List<CmtsFlowQuality> cmtsFlowQualities) {
        this.cmtsFlowQualities.addAll(cmtsFlowQualities);
    }

    public String getCollectType() {
        return collectType;
    }

    public void setCollectType(String collectType) {
        this.collectType = collectType;
    }

}
