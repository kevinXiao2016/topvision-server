/***********************************************************************
 * $Id: EponServiceQualityPertResult.java,v1.0 2013-7-19 上午09:13:22 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.domain;

import java.io.Serializable;
import java.util.List;

import com.topvision.ems.epon.performance.facade.OltFanPerf;
import com.topvision.ems.epon.performance.facade.OltServiceQualityPerf;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceResult;

/**
 * @author Rod John
 * @created @2013-7-19-上午09:13:22
 * 
 */
public class EponServiceQualityPertResult extends PerformanceResult<OperClass> implements Serializable {

    private static final long serialVersionUID = 824907733559488665L;
    private List<OltServiceQualityPerf> perfs;
    private List<OltFanPerf> fanSpeedPerfs;

    /**
     * @param domain
     */
    public EponServiceQualityPertResult(OperClass domain) {
        super(domain);
    }

    /**
     * @return the perfs
     */
    public List<OltServiceQualityPerf> getPerfs() {
        return perfs;
    }

    /**
     * @param perfs
     *            the perfs to set
     */
    public void setPerfs(List<OltServiceQualityPerf> perfs) {
        this.perfs = perfs;
    }

    /**
     * @return the fanSpeedPerfs
     */
    public List<OltFanPerf> getFanSpeedPerfs() {
        return fanSpeedPerfs;
    }

    /**
     * @param fanSpeedPerfs
     *            the fanSpeedPerfs to set
     */
    public void setFanSpeedPerfs(List<OltFanPerf> fanSpeedPerfs) {
        this.fanSpeedPerfs = fanSpeedPerfs;
    }

}
