package com.topvision.ems.network.domain;

import com.topvision.framework.domain.BaseEntity;

public class LinkRealFlow extends BaseEntity {
    private static final long serialVersionUID = 7330894385629098269L;
    private Long totalFlow;
    private Long sendFlow;
    private Long receiveFlow;
    private Long sendLosedPacketNumber;
    private Long receiveLosedPacketNumber;
    private Long sendLosedPacketRate;
    private Long receiveLosedPacketRate;

    /**
     * @return the receiveFlow
     */
    public Long getReceiveFlow() {
        return receiveFlow;
    }

    /**
     * @return the receiveLosedPacketNumber
     */
    public Long getReceiveLosedPacketNumber() {
        return receiveLosedPacketNumber;
    }

    /**
     * @return the receiveLosedPacketRate
     */
    public Long getReceiveLosedPacketRate() {
        return receiveLosedPacketRate;
    }

    /**
     * @return the sendFlow
     */
    public Long getSendFlow() {
        return sendFlow;
    }

    /**
     * @return the sendLosedPacketNumber
     */
    public Long getSendLosedPacketNumber() {
        return sendLosedPacketNumber;
    }

    /**
     * @return the sendLosedPacketRate
     */
    public Long getSendLosedPacketRate() {
        return sendLosedPacketRate;
    }

    /**
     * @return the totalFlow
     */
    public Long getTotalFlow() {
        return totalFlow;
    }

    /**
     * @param receiveFlow
     *            the receiveFlow to set
     */
    public void setReceiveFlow(Long receiveFlow) {
        this.receiveFlow = receiveFlow;
    }

    /**
     * @param receiveLosedPacketNumber
     *            the receiveLosedPacketNumber to set
     */
    public void setReceiveLosedPacketNumber(Long receiveLosedPacketNumber) {
        this.receiveLosedPacketNumber = receiveLosedPacketNumber;
    }

    /**
     * @param receiveLosedPacketRate
     *            the receiveLosedPacketRate to set
     */
    public void setReceiveLosedPacketRate(Long receiveLosedPacketRate) {
        this.receiveLosedPacketRate = receiveLosedPacketRate;
    }

    /**
     * @param sendFlow
     *            the sendFlow to set
     */
    public void setSendFlow(Long sendFlow) {
        this.sendFlow = sendFlow;
    }

    /**
     * @param sendLosedPacketNumber
     *            the sendLosedPacketNumber to set
     */
    public void setSendLosedPacketNumber(Long sendLosedPacketNumber) {
        this.sendLosedPacketNumber = sendLosedPacketNumber;
    }

    /**
     * @param sendLosedPacketRate
     *            the sendLosedPacketRate to set
     */
    public void setSendLosedPacketRate(Long sendLosedPacketRate) {
        this.sendLosedPacketRate = sendLosedPacketRate;
    }

    /**
     * @param totalFlow
     *            the totalFlow to set
     */
    public void setTotalFlow(Long totalFlow) {
        this.totalFlow = totalFlow;
    }
}
