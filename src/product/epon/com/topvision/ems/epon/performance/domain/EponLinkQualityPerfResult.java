/***********************************************************************
 * $Id: EponLinkQualityPerfResult.java,v1.0 2013-7-19 上午09:15:59 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.topvision.ems.epon.performance.facade.OltPonOptInfoPerf;
import com.topvision.ems.epon.performance.facade.OltSniOptInfoPerf;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceResult;

/**
 * @author Rod John
 * @created @2013-7-19-上午09:15:59
 * 
 */
public class EponLinkQualityPerfResult extends PerformanceResult<OperClass> implements Serializable {

    private static final long serialVersionUID = 1930464895894414894L;
    private List<OltSniOptInfoPerf> sniOptInfoPerfs;
    private List<OltPonOptInfoPerf> ponOptInfoPerfs;
    private List<EponLinkQualityData> linkQualityDatas = new ArrayList<EponLinkQualityData>();

    /**
     * @param domain
     */
    public EponLinkQualityPerfResult(OperClass domain) {
        super(domain);
    }

    /**
     * @return the sniOptInfoPerfs
     */
    public List<OltSniOptInfoPerf> getSniOptInfoPerfs() {
        return sniOptInfoPerfs;
    }

    /**
     * @param sniOptInfoPerfs
     *            the sniOptInfoPerfs to set
     */
    public void setSniOptInfoPerfs(List<OltSniOptInfoPerf> sniOptInfoPerfs) {
        if (sniOptInfoPerfs != null) {
            for (OltSniOptInfoPerf perf : sniOptInfoPerfs) {
                EponLinkQualityData data = new EponLinkQualityData(perf);
                linkQualityDatas.add(data);
            }
        }
        this.sniOptInfoPerfs = sniOptInfoPerfs;
    }

    /**
     * @return the ponOptInfoPerfs
     */
    public List<OltPonOptInfoPerf> getPonOptInfoPerfs() {
        return ponOptInfoPerfs;
    }

    /**
     * @param ponOptInfoPerfs
     *            the ponOptInfoPerfs to set
     */
    public void setPonOptInfoPerfs(List<OltPonOptInfoPerf> ponOptInfoPerfs) {
        if (ponOptInfoPerfs != null) {
            for (OltPonOptInfoPerf perf : ponOptInfoPerfs) {
                EponLinkQualityData data = new EponLinkQualityData(perf);
                linkQualityDatas.add(data);
            }
        }
        this.ponOptInfoPerfs = ponOptInfoPerfs;
    }

    /**
     * @return the linkQualityDatas
     */
    public List<EponLinkQualityData> getLinkQualityDatas() {
        return linkQualityDatas;
    }

    /**
     * @param linkQualityDatas
     *            the linkQualityDatas to set
     */
    public void setLinkQualityDatas(List<EponLinkQualityData> linkQualityDatas) {
        this.linkQualityDatas = linkQualityDatas;
    }

}
