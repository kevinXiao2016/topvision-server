package com.topvision.ems.cmc.performance.domain;

import java.io.Serializable;

import com.topvision.ems.cmc.performance.facade.CmcDorLinePowerQuality;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceResult;

public class CmcDorLinePowerQualityPerfResult extends PerformanceResult<OperClass> implements Serializable {

    private static final long serialVersionUID = 8563263583627575014L;
    private CmcDorLinePowerQuality perf;

    public CmcDorLinePowerQualityPerfResult(OperClass domain) {
        super(domain);
    }

    public CmcDorLinePowerQuality getPerf() {
        return perf;
    }

    public void setPerf(CmcDorLinePowerQuality perf) {
        this.perf = perf;
    }

}
