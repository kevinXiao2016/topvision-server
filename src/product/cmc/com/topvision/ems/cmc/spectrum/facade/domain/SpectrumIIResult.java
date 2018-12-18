package com.topvision.ems.cmc.spectrum.facade.domain;

import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceResult;

/**
 * @author bryan
 */
public class SpectrumIIResult extends PerformanceResult<OperClass> {
    /**
     * @param domain
     */
    public SpectrumIIResult(OperClass domain) {
        super(domain);
    }

    private static final long serialVersionUID = -6434107944338930410L;
    private Long entityId;
    private Long cmcId;
    private SpectrumIIData spectrumIIData;
    private long dt;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public long getDt() {
        return dt;
    }

    public void setDt(long dt) {
        this.dt = dt;
    }

    public SpectrumIIData getSpectrumIIData() {
        return spectrumIIData;
    }

    public void setSpectrumIIData(SpectrumIIData spectrumIIData) {
        this.spectrumIIData = spectrumIIData;
    }
}
