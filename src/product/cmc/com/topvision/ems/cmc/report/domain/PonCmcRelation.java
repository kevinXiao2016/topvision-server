/***********************************************************************
 * $Id: PonCmcRelation.java,v1.0 2013-9-25 下午12:08:27 $
 * 
 * @author: fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.report.domain;

import java.util.List;

/**
 * @author fanzidong
 * @created @2013-9-25-下午12:08:27
 * 
 */
public class PonCmcRelation {
    private Long ponIndex;
    private String ponIndexStr;
    private List<CmRealTimeUserStaticReport> reportDatas;

    public PonCmcRelation() {
        super();
    }

    public PonCmcRelation(Long ponIndex, String ponIndexStr) {
        super();
        this.ponIndex = ponIndex;
        this.ponIndexStr = ponIndexStr;
    }

    public PonCmcRelation(Long ponIndex, String ponIndexStr, List<CmRealTimeUserStaticReport> reportDatas) {
        super();
        this.ponIndex = ponIndex;
        this.ponIndexStr = ponIndexStr;
        this.reportDatas = reportDatas;
    }

    public Long getPonIndex() {
        return ponIndex;
    }

    public void setPonIndex(Long ponIndex) {
        this.ponIndex = ponIndex;
    }

    public String getPonIndexStr() {
        return ponIndexStr;
    }

    public void setPonIndexStr(String ponIndexStr) {
        this.ponIndexStr = ponIndexStr;
    }

    public List<CmRealTimeUserStaticReport> getReportDatas() {
        return reportDatas;
    }

    public void setReportDatas(List<CmRealTimeUserStaticReport> reportDatas) {
        this.reportDatas = reportDatas;
    }

    @Override
    public String toString() {
        return "PonCmcRelation [ponIndex=" + ponIndex + ", ponIndexStr=" + ponIndexStr + ", reportDatas=" + reportDatas
                + "]";
    }

}
