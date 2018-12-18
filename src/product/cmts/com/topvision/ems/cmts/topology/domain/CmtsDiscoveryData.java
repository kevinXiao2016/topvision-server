/***********************************************************************
 * $Id: CmtsDiscoveryData.java,v1.0 2013-7-20 下午02:15:30 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmts.topology.domain;

import java.util.List;
import java.util.Map;

import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcPort;
import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.cmc.facade.domain.CmCpe;
import com.topvision.ems.cmc.facade.domain.CmStaticIp;
import com.topvision.ems.cmc.facade.domain.CmcDownChannelBaseInfo;
import com.topvision.ems.cmc.facade.domain.DocsIf3CmtsCmUsStatus;
import com.topvision.ems.cmc.upchannel.facade.domain.CmcUpChannelBaseInfo;
import com.topvision.ems.cmc.upchannel.facade.domain.CmcUpChannelSignalQualityInfo;
import com.topvision.ems.facade.domain.CpuAndMemData;
import com.topvision.ems.facade.domain.DiscoveryData;
import com.topvision.platform.message.event.CmcEntityInfo;

/**
 * @author Rod John
 * @created @2013-7-20-下午02:15:30
 * 
 */
public class CmtsDiscoveryData extends DiscoveryData {
    private static final long serialVersionUID = -4741514852532484450L;

    private CmcAttribute cmcAttribute;
    private List<CmcUpChannelBaseInfo> cmcUpChannelBaseInfos;
    private List<CmcUpChannelSignalQualityInfo> cmcUpChannelSignalQualityInfos;
    private List<CmcDownChannelBaseInfo> cmcDownChannelBaseInfos;
    private List<CmAttribute> cmAttributes;
    private List<CmcPort> cmcPorts;
    private List<CmCpe> cmCpeList;
    private List<CmStaticIp> cmStaticIpList;
    private List<DocsIf3CmtsCmUsStatus> docsIf3CmtsCmUsStatusList;
    private Map<Long, CmcEntityInfo> cmcEntityInfos;
    private CpuAndMemData cpuAndMemData;

    /**
     * Default TopoType = ALL TOPO
     * 
     */
    public CmtsDiscoveryData() {
        super();
    }

    /**
     * Base TopoType
     * 
     * @param topoType
     */
    public CmtsDiscoveryData(Integer topoType) {
        super(topoType);
    }

    /**
     * 
     * 
     * @param cpuAndMemData
     */
    public CmtsDiscoveryData(CpuAndMemData cpuAndMemData) {
        this.cpuAndMemData = cpuAndMemData;
    }

    /**
     * @return the cmcAttributes
     */
    public CmcAttribute getCmcAttribute() {
        return cmcAttribute;
    }

    /**
     * @param cmcAttributes the cmcAttributes to set
     */
    public void setCmcAttribute(CmcAttribute cmcAttribute) {
        this.cmcAttribute = cmcAttribute;
    }

    /**
     * @return the cmcUpChannelBaseInfos
     */
    public List<CmcUpChannelBaseInfo> getCmcUpChannelBaseInfos() {
        return cmcUpChannelBaseInfos;
    }

    /**
     * @param cmcUpChannelBaseInfos the cmcUpChannelBaseInfos to set
     */
    public void setCmcUpChannelBaseInfos(List<CmcUpChannelBaseInfo> cmcUpChannelBaseInfos) {
        this.cmcUpChannelBaseInfos = cmcUpChannelBaseInfos;
    }

    /**
     * @return the cmcUpChannelSignalQualityInfos
     */
    public List<CmcUpChannelSignalQualityInfo> getCmcUpChannelSignalQualityInfos() {
        return cmcUpChannelSignalQualityInfos;
    }

    /**
     * @param cmcUpChannelSignalQualityInfos the cmcUpChannelSignalQualityInfos to set
     */
    public void setCmcUpChannelSignalQualityInfos(List<CmcUpChannelSignalQualityInfo> cmcUpChannelSignalQualityInfos) {
        this.cmcUpChannelSignalQualityInfos = cmcUpChannelSignalQualityInfos;
    }

    /**
     * @return the cmcDownChannelBaseInfos
     */
    public List<CmcDownChannelBaseInfo> getCmcDownChannelBaseInfos() {
        return cmcDownChannelBaseInfos;
    }

    /**
     * @param cmcDownChannelBaseInfos the cmcDownChannelBaseInfos to set
     */
    public void setCmcDownChannelBaseInfos(List<CmcDownChannelBaseInfo> cmcDownChannelBaseInfos) {
        this.cmcDownChannelBaseInfos = cmcDownChannelBaseInfos;
    }

    /**
     * @return the cmAttributes
     */
    public List<CmAttribute> getCmAttributes() {
        return cmAttributes;
    }

    /**
     * @param cmAttributes the cmAttributes to set
     */
    public void setCmAttributes(List<CmAttribute> cmAttributes) {
        this.cmAttributes = cmAttributes;
    }

    /**
     * @return the cmcPorts
     */
    public List<CmcPort> getCmcPorts() {
        return cmcPorts;
    }

    /**
     * @param cmcPorts the cmcPorts to set
     */
    public void setCmcPorts(List<CmcPort> cmcPorts) {
        this.cmcPorts = cmcPorts;
    }

    /**
     * @return the cmCpeList
     */
    public List<CmCpe> getCmCpeList() {
        return cmCpeList;
    }

    /**
     * @param cmCpeList the cmCpeList to set
     */
    public void setCmCpeList(List<CmCpe> cmCpeList) {
        this.cmCpeList = cmCpeList;
    }

    /**
     * @return the cmStaticIpList
     */
    public List<CmStaticIp> getCmStaticIpList() {
        return cmStaticIpList;
    }

    /**
     * @param cmStaticIpList the cmStaticIpList to set
     */
    public void setCmStaticIpList(List<CmStaticIp> cmStaticIpList) {
        this.cmStaticIpList = cmStaticIpList;
    }

    /**
     * @return the docsIf3CmtsCmUsStatusList
     */
    public List<DocsIf3CmtsCmUsStatus> getDocsIf3CmtsCmUsStatusList() {
        return docsIf3CmtsCmUsStatusList;
    }

    /**
     * @param docsIf3CmtsCmUsStatusList the docsIf3CmtsCmUsStatusList to set
     */
    public void setDocsIf3CmtsCmUsStatusList(List<DocsIf3CmtsCmUsStatus> docsIf3CmtsCmUsStatusList) {
        this.docsIf3CmtsCmUsStatusList = docsIf3CmtsCmUsStatusList;
    }

    /**
     * @return the cmcEntityInfos
     */
    public Map<Long, CmcEntityInfo> getCmcEntityInfos() {
        return cmcEntityInfos;
    }

    /**
     * @param cmcEntityInfos the cmcEntityInfos to set
     */
    public void setCmcEntityInfos(Map<Long, CmcEntityInfo> cmcEntityInfos) {
        this.cmcEntityInfos = cmcEntityInfos;
    }

    public CpuAndMemData getCpuAndMemData() {
        return cpuAndMemData;
    }

    public void setCpuAndMemData(CpuAndMemData cpuAndMemData) {
        this.cpuAndMemData = cpuAndMemData;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmtsDiscoveryData [cmcAttribute=");
        builder.append(cmcAttribute);
        builder.append(", cmcUpChannelBaseInfos=");
        builder.append(cmcUpChannelBaseInfos);
        builder.append(", cmcUpChannelSignalQualityInfos=");
        builder.append(cmcUpChannelSignalQualityInfos);
        builder.append(", cmcDownChannelBaseInfos=");
        builder.append(cmcDownChannelBaseInfos);
        builder.append(", cmAttributes=");
        builder.append(cmAttributes);
        builder.append(", cmcPorts=");
        builder.append(cmcPorts);
        builder.append(", cmCpeList=");
        builder.append(cmCpeList);
        builder.append(", cmStaticIpList=");
        builder.append(cmStaticIpList);
        builder.append(", docsIf3CmtsCmUsStatusList=");
        builder.append(docsIf3CmtsCmUsStatusList);
        builder.append(", cmcEntityInfos=");
        builder.append(cmcEntityInfos);
        builder.append(", cpuAndMemData=");
        builder.append(cpuAndMemData);
        builder.append("]");
        return builder.toString();
    }

}
