/***********************************************************************
 * $Id: CmcCmStatusPerfResult.java,v1.0 2013-8-12 下午01:50:26 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.domain;

import java.io.Serializable;
import java.util.List;

import com.topvision.ems.cmc.performance.facade.CmcCmNumber;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceResult;

/**
 * @author Rod John
 * @created @2013-8-12-下午01:50:26
 * 
 */
public class CmcCmNumberPerfResult extends PerformanceResult<OperClass> implements Serializable {
    private static final long serialVersionUID = 6557898085586242131L;
    private List<CmcCmNumber> cmcCmNumbers;

    /**
     * @param domain
     */
    public CmcCmNumberPerfResult(OperClass domain) {
        super(domain);
    }

    /**
     * @return the cmcCmNumbers
     */
    public List<CmcCmNumber> getCmcCmNumbers() {
        return cmcCmNumbers;
    }

    /**
     * @param cmcCmNumbers
     *            the cmcCmNumbers to set
     */
    public void setCmcCmNumbers(List<CmcCmNumber> cmcCmNumbers) {
        this.cmcCmNumbers = cmcCmNumbers;
    }

}
