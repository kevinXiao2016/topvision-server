/***********************************************************************
 * $Id: CmcLinkQualityPerfResult.java,v1.0 2013-8-8 下午08:18:15 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.topvision.ems.cmc.performance.facade.CmcLinkQuality;
import com.topvision.ems.cmc.performance.facade.CmcLinkQualityFor8800A;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceResult;

/**
 * @author Rod John
 * @created @2013-8-8-下午08:18:15
 * 
 */
public class CmcLinkQualityPerfResult extends PerformanceResult<OperClass> implements Serializable {
    private static final long serialVersionUID = -239212335355535708L;
    private List<CmcLinkQuality> perfs;
    private CmcLinkQualityFor8800A cmcLinkQualityFor8800A;
    private List<CmcLinkQualityData> cmcLinkQualityDatas = new ArrayList<CmcLinkQualityData>();

    /**
     * @param domain
     */
    public CmcLinkQualityPerfResult(OperClass domain) {
        super(domain);
    }

    /**
     * @return the perfs
     */
    public List<CmcLinkQuality> getPerfs() {
        return perfs;
    }

    /**
     * @param perfs
     *            the perfs to set
     */
    public void setPerfs(List<CmcLinkQuality> perfs) {
        if (perfs != null) {
            for (CmcLinkQuality perf : perfs) {
                CmcLinkQualityData data = new CmcLinkQualityData(perf);
                cmcLinkQualityDatas.add(data);
            }
        }
        this.perfs = perfs;
    }

    public CmcLinkQualityFor8800A getCmcLinkQualityFor8800A() {
        return cmcLinkQualityFor8800A;
    }

    public void setCmcLinkQualityFor8800A(CmcLinkQualityFor8800A cmcLinkQualityFor8800A) {
        if (cmcLinkQualityFor8800A != null) {
            CmcLinkQualityData data = new CmcLinkQualityData(cmcLinkQualityFor8800A);
            cmcLinkQualityDatas.add(data);
        }
        this.cmcLinkQualityFor8800A = cmcLinkQualityFor8800A;
    }

    public List<CmcLinkQualityData> getCmcLinkQualityDatas() {
        return cmcLinkQualityDatas;
    }

    public void setCmcLinkQualityDatas(List<CmcLinkQualityData> cmcLinkQualityDatas) {
        this.cmcLinkQualityDatas = cmcLinkQualityDatas;
    }

}
