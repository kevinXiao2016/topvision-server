/***********************************************************************
 * $Id: CmcServiceQualityPerfResult.java,v1.0 2013-8-8 下午03:05:08 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.domain;

import java.io.Serializable;

import com.topvision.ems.cmc.performance.facade.CmcServiceQuality;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceResult;

/**
 * @author Rod John
 * @created @2013-8-8-下午03:05:08
 * 
 */
public class CmcServiceQualityPerfResult extends PerformanceResult<OperClass> implements Serializable {
    private static final long serialVersionUID = -170789426948607173L;
    private CmcServiceQuality perf;

    /**
     * @param domain
     */
    public CmcServiceQualityPerfResult(OperClass domain) {
        super(domain);
    }

    /**
     * @return the perf
     */
    public CmcServiceQuality getPerf() {
        return perf;
    }

    /**
     * @param perfs
     *            the perfs to set
     */
    public void setPerf(CmcServiceQuality perf) {
        this.perf = perf;
    }

}
