package com.topvision.ems.cmc.performance.domain;

import java.io.Serializable;

import com.topvision.ems.cmc.performance.facade.CmcDorOptTempQuality;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceResult;

public class CmcDorOptTempQualityPerfResult extends PerformanceResult<OperClass> implements Serializable {

    private static final long serialVersionUID = 3896906116005054858L;
    private CmcDorOptTempQuality perf;

    public CmcDorOptTempQualityPerfResult(OperClass domain) {
        super(domain);
    }

    public CmcDorOptTempQuality getPerf() {
        return perf;
    }

    public void setPerf(CmcDorOptTempQuality perf) {
        this.perf = perf;
    }

}
