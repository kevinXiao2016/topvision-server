/***********************************************************************
 * $Id: CmcSingleQualityResult.java,v1.0 2013-8-10 下午01:52:52 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.domain;

import java.io.Serializable;
import java.util.List;

import com.topvision.ems.cmc.performance.facade.CmcSignalQuality;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceResult;

/**
 * @author Rod John
 * @created @2013-8-10-下午01:52:52
 * 
 */
public class CmcSignalQualityPerfResult extends PerformanceResult<OperClass> implements Serializable {
    private static final long serialVersionUID = -170789426948607173L;
    private List<CmcSignalQuality> perfs;

    /**
     * @param domain
     */
    public CmcSignalQualityPerfResult(OperClass domain) {
        super(domain);
    }

    /**
     * @return the perfs
     */
    public List<CmcSignalQuality> getPerfs() {
        return perfs;
    }

    /**
     * @param perfs
     *            the perfs to set
     */
    public void setPerfs(List<CmcSignalQuality> perfs) {
        this.perfs = perfs;
    }

}
