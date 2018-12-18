package com.topvision.ems.cmc.spectrum.facade.domain;

import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceResult;

/**
 * @author bryan
 */
public class SpectrumIIIResult extends PerformanceResult<OperClass> {
    /**
     * @param domain
     */
    public SpectrumIIIResult(OperClass domain) {
        super(domain);
    }

    private static final long serialVersionUID = -6434107944338930410L;
    private Long entityId;
    private Long cmcId;
    private String dataBuffer;
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


    public String getDataBuffer() {
        return dataBuffer;
    }

    public void setDataBuffer(String dataBuffer) {
        this.dataBuffer = dataBuffer;
    }
}
