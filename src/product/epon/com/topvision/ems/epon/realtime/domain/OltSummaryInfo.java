/***********************************************************************
 * $Id: OltSummaryInfo.java,v1.0 2014-8-22 上午10:57:33 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.realtime.domain;

import java.util.List;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author flack
 * @created @2014-8-22-上午10:57:33
 *
 */
public class OltSummaryInfo implements AliasesSuperType {
    private static final long serialVersionUID = -2616531276647818489L;

    private OltBaseInfo baseInfo;
    private List<OltPonTotalInfo> ponTotalList;
    private List<OltSubTotalInfo> subTotalList;
    private List<OltSniInfo> sniList;
    private OltCmTotalInfo cmTotal;

    public OltBaseInfo getBaseInfo() {
        return baseInfo;
    }

    public void setBaseInfo(OltBaseInfo baseInfo) {
        this.baseInfo = baseInfo;
    }

    public List<OltPonTotalInfo> getPonTotalList() {
        return ponTotalList;
    }

    public void setPonTotalList(List<OltPonTotalInfo> ponTotalList) {
        this.ponTotalList = ponTotalList;
    }

    public List<OltSubTotalInfo> getSubTotalList() {
        return subTotalList;
    }

    public void setSubTotalList(List<OltSubTotalInfo> subTotalList) {
        this.subTotalList = subTotalList;
    }

    public OltCmTotalInfo getCmTotal() {
        return cmTotal;
    }

    public void setCmTotal(OltCmTotalInfo cmTotal) {
        this.cmTotal = cmTotal;
    }

    public List<OltSniInfo> getSniList() {
        return sniList;
    }

    public void setSniList(List<OltSniInfo> sniList) {
        this.sniList = sniList;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OltSummaryInfo [baseInfo=");
        builder.append(baseInfo);
        builder.append(", ponTotalList=");
        builder.append(ponTotalList);
        builder.append(", subTotalList=");
        builder.append(subTotalList);
        builder.append(", cmTotal=");
        builder.append(cmTotal);
        builder.append("]");
        return builder.toString();
    }

}
