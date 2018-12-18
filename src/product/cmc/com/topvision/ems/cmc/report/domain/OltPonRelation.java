/***********************************************************************
 * $Id: OltPonRelation.java,v1.0 2013-9-25 下午12:06:39 $
 * 
 * @author: fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.report.domain;

import java.util.Map;

/**
 * @author fanzidong
 * @created @2013-9-25-下午12:06:39
 * 
 */
public class OltPonRelation {
    private String oltIp;
    private String oltName;
    private Map<String, PonCmcRelation> ponCmcRelations;
    private Integer rowspan;

    public OltPonRelation() {
        super();
    }

    public OltPonRelation(String oltIp, String oltName, Integer rowspan) {
        super();
        this.oltIp = oltIp;
        this.oltName = oltName;
        this.rowspan = rowspan;
    }

    public OltPonRelation(String oltIp, String oltName, Map<String, PonCmcRelation> ponCmcRelations, Integer rowspan) {
        super();
        this.oltIp = oltIp;
        this.oltName = oltName;
        this.ponCmcRelations = ponCmcRelations;
        this.rowspan = rowspan;
    }

    public String getOltIp() {
        return oltIp;
    }

    public void setOltIp(String oltIp) {
        this.oltIp = oltIp;
    }

    public String getOltName() {
        return oltName;
    }

    public void setOltName(String oltName) {
        this.oltName = oltName;
    }

    public Map<String, PonCmcRelation> getPonCmcRelations() {
        return ponCmcRelations;
    }

    public void setPonCmcRelations(Map<String, PonCmcRelation> ponCmcRelations) {
        this.ponCmcRelations = ponCmcRelations;
    }

    public Integer getRowspan() {
        return rowspan;
    }

    public void setRowspan(Integer rowspan) {
        this.rowspan = rowspan;
    }

}
