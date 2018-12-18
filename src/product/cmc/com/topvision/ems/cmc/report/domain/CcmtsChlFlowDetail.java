/***********************************************************************
 * $Id: CcmtsChlFlowDetail.java,v1.0 2014-3-28 下午2:19:37 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.report.domain;

import java.text.DecimalFormat;
import java.util.List;

import com.topvision.ems.report.domain.TopoEntityStastic;

/**
 * @author haojie
 * @created @2014-3-28-下午2:19:37
 *
 */
public class CcmtsChlFlowDetail extends TopoEntityStastic {
    private static final DecimalFormat df = new DecimalFormat("##0.00");
    private Long cmcEntityId;//上联OLT的ID
    private String oltName;//上联OLT的名称
    private Double usageAvg;//信道流量利用率平均值
    private String usageAvgString;
    private Integer maxRegUserNum;//域注册用户最大值
    private Integer maxUserNum;//域用户总数最大值
    private List<CcmtsChlFlowInfo> CcmtsChlFlowInfos;//信道的基本信息，包括宽口号、调制方式、信道流量最大值、下行流量利用率最大值

    public Long getCmcEntityId() {
        return cmcEntityId;
    }

    public void setCmcEntityId(Long cmcEntityId) {
        this.cmcEntityId = cmcEntityId;
    }

    public String getOltName() {
        return oltName;
    }

    public void setOltName(String oltName) {
        this.oltName = oltName;
    }

    public Double getUsageAvg() {
        return usageAvg;
    }

    public void setUsageAvg(Double usageAvg) {
        this.usageAvg = usageAvg;
        if (usageAvg != null) {
            usageAvgString = df.format(usageAvg);
        }
    }

    public Integer getMaxRegUserNum() {
        if (maxRegUserNum != null) {
            return maxRegUserNum;
        } else {
            return 0;
        }
    }

    public void setMaxRegUserNum(Integer maxRegUserNum) {
        this.maxRegUserNum = maxRegUserNum;
    }

    public Integer getMaxUserNum() {
        if (maxUserNum != null) {
            return maxUserNum;
        } else {
            return 0;
        }
    }

    public void setMaxUserNum(Integer maxUserNum) {
        this.maxUserNum = maxUserNum;
    }

    public List<CcmtsChlFlowInfo> getCcmtsChlFlowInfos() {
        return CcmtsChlFlowInfos;
    }

    public void setCcmtsChlFlowInfos(List<CcmtsChlFlowInfo> ccmtsChlFlowInfos) {
        CcmtsChlFlowInfos = ccmtsChlFlowInfos;
    }

    public String getUsageAvgString() {
        return usageAvgString;
    }

    public void setUsageAvgString(String usageAvgString) {
        this.usageAvgString = usageAvgString;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CcmtsChlFlowDetail [cmcEntityId=");
        builder.append(cmcEntityId);
        builder.append(", oltName=");
        builder.append(oltName);
        builder.append(", usageAvg=");
        builder.append(usageAvg);
        builder.append(", usageAvgString=");
        builder.append(usageAvgString);
        builder.append(", maxRegUserNum=");
        builder.append(maxRegUserNum);
        builder.append(", maxUserNum=");
        builder.append(maxUserNum);
        builder.append(", CcmtsChlFlowInfos=");
        builder.append(CcmtsChlFlowInfos);
        builder.append("]");
        return builder.toString();
    }

}
