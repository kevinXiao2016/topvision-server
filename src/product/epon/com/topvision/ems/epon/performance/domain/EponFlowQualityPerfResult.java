/***********************************************************************
 * $Id: EponFlowQualityPerfResult.java,v1.0 2013-8-7 下午04:12:12 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.domain;

import java.io.Serializable;
import java.util.List;

import com.topvision.ems.epon.performance.facade.OltFlowQuality;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceResult;

/**
 * @author Rod John
 * @created @2013-8-7-下午04:12:12
 * 
 */
public class EponFlowQualityPerfResult extends PerformanceResult<OperClass> implements Serializable {

    private static final long serialVersionUID = -4315105163160056384L;
    private List<OltFlowQuality> flowPerfs;

    /**
     * @param domain
     */
    public EponFlowQualityPerfResult(OperClass domain) {
        super(domain);
    }

    /**
     * @return the flowPerfs
     */
    public List<OltFlowQuality> getFlowPerfs() {
        return flowPerfs;
    }

    /**
     * @param flowPerfs
     *            the flowPerfs to set
     */
    public void setFlowPerfs(List<OltFlowQuality> flowPerfs) {
        this.flowPerfs = flowPerfs;
    }

}
