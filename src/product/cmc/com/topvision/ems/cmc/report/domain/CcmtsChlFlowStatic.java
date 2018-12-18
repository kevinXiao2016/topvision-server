/***********************************************************************
 * $Id: CcmtsChlFlowStatic.java,v1.0 2014-3-27 下午2:20:56 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.report.domain;

import com.topvision.ems.report.domain.TopoEntityStastic;

/**
 * @author haojie
 * @created @2014-3-27-下午2:20:56
 * 
 */
public class CcmtsChlFlowStatic extends TopoEntityStastic {
    private Integer chlNum;
    private Integer usage0;
    private Integer usage0to10;
    private Integer usage10to20;
    private Integer usage20to30;
    private Integer usage30to40;
    private Integer usage40to50;
    private Integer usage50to60;
    private Integer usage60to70;
    private Integer usage70to80;
    private Integer usage80to90;
    private Integer usage90to100;

    public Integer getChlNum() {
        return chlNum;
    }

    public void setChlNum(Integer chlNum) {
        this.chlNum = chlNum;
    }

    public Integer getUsage0() {
        return usage0;
    }

    public void setUsage0(Integer usage0) {
        this.usage0 = usage0;
    }

    public Integer getUsage0to10() {
        return usage0to10;
    }

    public void setUsage0to10(Integer usage0to10) {
        this.usage0to10 = usage0to10;
    }

    public Integer getUsage10to20() {
        return usage10to20;
    }

    public void setUsage10to20(Integer usage10to20) {
        this.usage10to20 = usage10to20;
    }

    public Integer getUsage20to30() {
        return usage20to30;
    }

    public void setUsage20to30(Integer usage20to30) {
        this.usage20to30 = usage20to30;
    }

    public Integer getUsage30to40() {
        return usage30to40;
    }

    public void setUsage30to40(Integer usage30to40) {
        this.usage30to40 = usage30to40;
    }

    public Integer getUsage40to50() {
        return usage40to50;
    }

    public void setUsage40to50(Integer usage40to50) {
        this.usage40to50 = usage40to50;
    }

    public Integer getUsage50to60() {
        return usage50to60;
    }

    public void setUsage50to60(Integer usage50to60) {
        this.usage50to60 = usage50to60;
    }

    public Integer getUsage60to70() {
        return usage60to70;
    }

    public void setUsage60to70(Integer usage60to70) {
        this.usage60to70 = usage60to70;
    }

    public Integer getUsage70to80() {
        return usage70to80;
    }

    public void setUsage70to80(Integer usage70to80) {
        this.usage70to80 = usage70to80;
    }

    public Integer getUsage80to90() {
        return usage80to90;
    }

    public void setUsage80to90(Integer usage80to90) {
        this.usage80to90 = usage80to90;
    }

    public Integer getUsage90to100() {
        return usage90to100;
    }

    public void setUsage90to100(Integer usage90to100) {
        this.usage90to100 = usage90to100;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CcmtsChlFlowStatic [chlNum=");
        builder.append(chlNum);
        builder.append(", usage0=");
        builder.append(usage0);
        builder.append(", usage0to10=");
        builder.append(usage0to10);
        builder.append(", usage10to20=");
        builder.append(usage10to20);
        builder.append(", usage20to30=");
        builder.append(usage20to30);
        builder.append(", usage30to40=");
        builder.append(usage30to40);
        builder.append(", usage40to50=");
        builder.append(usage40to50);
        builder.append(", usage50to60=");
        builder.append(usage50to60);
        builder.append(", usage60to70=");
        builder.append(usage60to70);
        builder.append(", usage70to80=");
        builder.append(usage70to80);
        builder.append(", usage80to90=");
        builder.append(usage80to90);
        builder.append(", usage90to100=");
        builder.append(usage90to100);
        builder.append("]");
        return builder.toString();
    }

}
