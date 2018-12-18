package com.topvision.ems.cmc.spectrum.facade.domain;

import java.util.List;

import com.topvision.ems.cmc.spectrum.facade.domain.SpectrumData;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceResult;

/**
 * @author bryan
 */
public class SpectrumResult extends PerformanceResult<OperClass> {
    /**
     * @param domain
     */
    public SpectrumResult(OperClass domain) {
        super(domain);
    }

    private static final long serialVersionUID = -6434107944338930410L;
    private Long entityId;
    private Long cmcId;
    private List<SpectrumData> spectrumData;
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

    public List<SpectrumData> getSpectrumData() {
        return spectrumData;
    }

    public void setSpectrumData(List<SpectrumData> spectrumData) {
        this.spectrumData = spectrumData;
    }

}
