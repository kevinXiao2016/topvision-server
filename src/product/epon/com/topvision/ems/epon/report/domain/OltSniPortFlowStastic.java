/***********************************************************************
 * $Id: OltSniPortFlowStastic.java,v1.0 2013-5-28 下午4:01:50 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.report.domain;

import java.text.NumberFormat;

import com.topvision.ems.report.domain.TopoEntityStastic;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author Bravin
 * @created @2013-5-28-下午4:01:50
 * 
 */
public class OltSniPortFlowStastic extends TopoEntityStastic implements AliasesSuperType {
    private static final long serialVersionUID = -1079602405699748301L;

    private Long maxFlow;
    private Double maxSendFlow = 0.0;
    private Double maxRecvFlow = 0.0;
    private String maxSendFlowString;
    private String maxRecvFlowString;
    private Integer currentLinkedPortCount;
    private Integer range20 = 0;
    private Integer range40 = 0;
    private Integer range60 = 0;
    private Integer range80 = 0;
    private Integer range100 = 0;
    private static final NumberFormat formater = NumberFormat.getInstance();
    static {
        // 设置小数点后面最多2位
        formater.setMaximumFractionDigits(2);
    }

    /**
     * @return the maxFlow
     */
    public Long getMaxFlow() {
        return maxFlow;
    }

    /**
     * @param maxFlow
     *            the maxFlow to set
     */
    public void setMaxFlow(Long maxFlow) {
        this.maxFlow = maxFlow;
    }

    /**
     * @return the maxSendFlow
     */
    public Double getMaxSendFlow() {
        return maxSendFlow;
    }

    /**
     * @param maxSendFlow
     *            the maxSendFlow to set
     */
    public void setMaxSendFlow(Double maxSendFlow) {
        if (maxSendFlow != null) {
            this.maxSendFlowString = formater.format(maxSendFlow);
        } else {
            this.maxSendFlowString = "";
        }
        this.maxSendFlow = maxSendFlow;
    }

    /**
     * @return the maxRecvFlow
     */
    public Double getMaxRecvFlow() {
        return maxRecvFlow;
    }

    /**
     * @param maxRecvFlow
     *            the maxRecvFlow to set
     */
    public void setMaxRecvFlow(Double maxRecvFlow) {
        if (maxRecvFlow != null) {
            this.maxRecvFlowString = formater.format(maxRecvFlow);
        } else {
            this.maxRecvFlowString = "";
        }
        this.maxRecvFlow = maxRecvFlow;
    }

    /**
     * @return the currentLinkedPortCount
     */
    public Integer getCurrentLinkedPortCount() {
        return currentLinkedPortCount;
    }

    /**
     * @param currentLinkedPortCount
     *            the currentLinkedPortCount to set
     */
    public void setCurrentLinkedPortCount(Integer currentLinkedPortCount) {
        this.currentLinkedPortCount = currentLinkedPortCount;
    }

    /**
     * @return the range20
     */
    public Integer getRange20() {
        return range20;
    }

    /**
     * @param range20
     *            the range20 to set
     */
    public void setRange20(Integer range20) {
        this.range20 = range20;
    }

    /**
     * @return the range40
     */
    public Integer getRange40() {
        return range40;
    }

    /**
     * @param range40
     *            the range40 to set
     */
    public void setRange40(Integer range40) {
        this.range40 = range40;
    }

    /**
     * @return the range60
     */
    public Integer getRange60() {
        return range60;
    }

    /**
     * @param range60
     *            the range60 to set
     */
    public void setRange60(Integer range60) {
        this.range60 = range60;
    }

    /**
     * @return the range80
     */
    public Integer getRange80() {
        return range80;
    }

    /**
     * @param range80
     *            the range80 to set
     */
    public void setRange80(Integer range80) {
        this.range80 = range80;
    }

    /**
     * @return the range100
     */
    public Integer getRange100() {
        return range100;
    }

    /**
     * @param range100
     *            the range100 to set
     */
    public void setRange100(Integer range100) {
        this.range100 = range100;
    }

    /**
     * @return the maxSendFlowString
     */
    public String getMaxSendFlowString() {
        return maxSendFlowString;
    }

    /**
     * @param maxSendFlowString
     *            the maxSendFlowString to set
     */
    public void setMaxSendFlowString(String maxSendFlowString) {
        this.maxSendFlowString = maxSendFlowString;
    }

    /**
     * @return the maxRecvFlowString
     */
    public String getMaxRecvFlowString() {
        return maxRecvFlowString;
    }

    /**
     * @param maxRecvFlowString
     *            the maxRecvFlowString to set
     */
    public void setMaxRecvFlowString(String maxRecvFlowString) {
        this.maxRecvFlowString = maxRecvFlowString;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OltSniPortFlowStastic [maxFlow=");
        builder.append(maxFlow);
        builder.append(", maxSendFlow=");
        builder.append(maxSendFlow);
        builder.append(", maxRecvFlow=");
        builder.append(maxRecvFlow);
        builder.append(", maxSendFlowString=");
        builder.append(maxSendFlowString);
        builder.append(", maxRecvFlowString=");
        builder.append(maxRecvFlowString);
        builder.append(", currentLinkedPortCount=");
        builder.append(currentLinkedPortCount);
        builder.append(", range20=");
        builder.append(range20);
        builder.append(", range40=");
        builder.append(range40);
        builder.append(", range60=");
        builder.append(range60);
        builder.append(", range80=");
        builder.append(range80);
        builder.append(", range100=");
        builder.append(range100);
        builder.append("]");
        return builder.toString();
    }

}
