/***********************************************************************
 * $Id: CmcTempQualityPerfResult.java,v1.0 2013-9-5 上午09:46:47 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.domain;

import java.io.Serializable;
import java.util.List;

import com.topvision.ems.cmc.performance.facade.CmcTempQualityFor8800B;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceResult;

/**
 * @author Rod John
 * @created @2013-9-5-上午09:46:47
 * 
 */
public class CmcTempQualityPerfResult extends PerformanceResult<OperClass> implements Serializable {
    private static final long serialVersionUID = 5485739969967135082L;
    private List<CmcTempQualityFor8800B> cmcTempQualityFor8800Bs;

    /**
     * @param domain
     */
    public CmcTempQualityPerfResult(OperClass domain) {
        super(domain);
    }

    /**
     * @return the cmcTempQualityFor8800Bs
     */
    public List<CmcTempQualityFor8800B> getCmcTempQualityFor8800Bs() {
        return cmcTempQualityFor8800Bs;
    }

    /**
     * @param cmcTempQualityFor8800Bs
     *            the cmcTempQualityFor8800Bs to set
     */
    public void setCmcTempQualityFor8800Bs(List<CmcTempQualityFor8800B> cmcTempQualityFor8800Bs) {
        this.cmcTempQualityFor8800Bs = cmcTempQualityFor8800Bs;
    }

}
