/***********************************************************************
 * $Id: CmcFlowQualityPerfResult.java,v1.0 2013-8-13 下午02:55:02 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.domain;

import java.util.List;

import com.topvision.ems.cmc.performance.facade.CmcFlowQuality;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceResult;

/**
 * @author Rod John
 * @created @2013-8-13-下午02:55:02
 * 
 */
public class CmcFlowQualityPerfResult extends PerformanceResult<OperClass> {
    private static final long serialVersionUID = -1709943208636160197L;
    private List<CmcFlowQuality> flowQualities;

    /**
     * @param domain
     */
    public CmcFlowQualityPerfResult(OperClass domain) {
        super(domain);
    }

    /**
     * @return the flowQualities
     */
    public List<CmcFlowQuality> getFlowQualities() {
        return flowQualities;
    }

    /**
     * @param flowQualities
     *            the flowQualities to set
     */
    public void setFlowQualities(List<CmcFlowQuality> flowQualities) {
        this.flowQualities = flowQualities;
    }

}
