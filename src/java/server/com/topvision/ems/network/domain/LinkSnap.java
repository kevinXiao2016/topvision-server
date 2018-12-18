package com.topvision.ems.network.domain;

import java.util.Date;

public class LinkSnap extends LinkEx {
    private static final long serialVersionUID = 8587121012260327817L;
    private Date snapTime;
    private Long snapTimeMillis;
    private Double flow;
    private Double usage;
    private Double rate;

    /**
     * @return the flow
     */
    public Double getFlow() {
        return flow;
    }

    /**
     * @return the rate
     */
    public Double getRate() {
        return rate;
    }

    /**
     * @return the snapTime
     */
    public Date getSnapTime() {
        return snapTime;
    }

    /**
     * @return the snapTimeMillis
     */
    public Long getSnapTimeMillis() {
        return snapTimeMillis;
    }

    /**
     * @return the usage
     */
    public Double getUsage() {
        return usage;
    }

    /**
     * @param flow
     *            the flow to set
     */
    public void setFlow(Double flow) {
        this.flow = flow;
    }

    /**
     * @param rate
     *            the rate to set
     */
    public void setRate(Double rate) {
        this.rate = rate;
    }

    /**
     * @param snapTime
     *            the snapTime to set
     */
    public void setSnapTime(Date snapTime) {
        this.snapTime = snapTime;
    }

    /**
     * @param snapTimeMillis
     *            the snapTimeMillis to set
     */
    public void setSnapTimeMillis(Long snapTimeMillis) {
        this.snapTimeMillis = snapTimeMillis;
    }

    /**
     * @param usage
     *            the usage to set
     */
    public void setUsage(Double usage) {
        this.usage = usage;
    }
}
