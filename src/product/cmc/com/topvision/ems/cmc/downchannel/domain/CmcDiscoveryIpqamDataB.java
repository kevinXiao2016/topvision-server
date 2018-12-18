/***********************************************************************
 * $Id: CmcIpqamDiscoveryData.java,v1.0 2013-11-19 下午02:36:24 $
 * 
 * @author:bryan
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.downchannel.domain;

import java.util.List;

import com.topvision.ems.facade.domain.DiscoveryData;

/**
 * @author bryan
 * @created @2013-11-19-下午02:36:24
 *
 */
public class CmcDiscoveryIpqamDataB extends DiscoveryData {
    /**
     * 
     */
    private static final long serialVersionUID = 8151516078224853692L;
    private List<CmcDSIpqamBaseInfo> cmcDSIpqamBaseInfos;
    private List<CmcDSIpqamStatusInfo> cmcDSIpqamStatusInfos;
    private List<CmcDSIpqamMappings> cmcDSIpqamMappings;
    private List<CmcDSIpqamISInfo> cmcDSIpqamISInfos;
    private List<CmcDSIpqamOSInfo> cmcDSIpqamOSInfos;
    private CmcIpqamInfo cmcIpqamInfo;
    
    /**
     * @return the cmcDSIpqamBaseInfos
     */
    public List<CmcDSIpqamBaseInfo> getCmcDSIpqamBaseInfos() {
        return cmcDSIpqamBaseInfos;
    }
    /**
     * @param cmcDSIpqamBaseInfos the cmcDSIpqamBaseInfos to set
     */
    public void setCmcDSIpqamBaseInfos(List<CmcDSIpqamBaseInfo> cmcDSIpqamBaseInfos) {
        this.cmcDSIpqamBaseInfos = cmcDSIpqamBaseInfos;
    }
    /**
     * @return the cmcDSIpqamStatusInfos
     */
    public List<CmcDSIpqamStatusInfo> getCmcDSIpqamStatusInfos() {
        return cmcDSIpqamStatusInfos;
    }
    /**
     * @param cmcDSIpqamStatusInfos the cmcDSIpqamStatusInfos to set
     */
    public void setCmcDSIpqamStatusInfos(List<CmcDSIpqamStatusInfo> cmcDSIpqamStatusInfos) {
        this.cmcDSIpqamStatusInfos = cmcDSIpqamStatusInfos;
    }
    /**
     * @return the cmcDSIpqamMappings
     */
    public List<CmcDSIpqamMappings> getCmcDSIpqamMappings() {
        return cmcDSIpqamMappings;
    }
    /**
     * @param cmcDSIpqamMappings the cmcDSIpqamMappings to set
     */
    public void setCmcDSIpqamMappings(List<CmcDSIpqamMappings> cmcDSIpqamMappings) {
        this.cmcDSIpqamMappings = cmcDSIpqamMappings;
    }
    /**
     * @return the cmcDSIpqamISInfos
     */
    public List<CmcDSIpqamISInfo> getCmcDSIpqamISInfos() {
        return cmcDSIpqamISInfos;
    }
    /**
     * @param cmcDSIpqamISInfos the cmcDSIpqamISInfos to set
     */
    public void setCmcDSIpqamISInfos(List<CmcDSIpqamISInfo> cmcDSIpqamISInfos) {
        this.cmcDSIpqamISInfos = cmcDSIpqamISInfos;
    }
    /**
     * @return the cmcDSIpqamOSInfos
     */
    public List<CmcDSIpqamOSInfo> getCmcDSIpqamOSInfos() {
        return cmcDSIpqamOSInfos;
    }
    /**
     * @param cmcDSIpqamOSInfos the cmcDSIpqamOSInfos to set
     */
    public void setCmcDSIpqamOSInfos(List<CmcDSIpqamOSInfo> cmcDSIpqamOSInfos) {
        this.cmcDSIpqamOSInfos = cmcDSIpqamOSInfos;
    }
    /**
     * @return the cmcIpqamInfo
     */
    public CmcIpqamInfo getCmcIpqamInfo() {
        return cmcIpqamInfo;
    }
    /**
     * @param cmcIpqamInfo the cmcIpqamInfo to set
     */
    public void setCmcIpqamInfo(CmcIpqamInfo cmcIpqamInfo) {
        this.cmcIpqamInfo = cmcIpqamInfo;
    }
    

}
