/***********************************************************************
 * $Id: CmcDiscoveryData.java,v1.0 2011-11-13 下午01:47:16 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.topology.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcPort;
import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.cmc.facade.domain.CmCpe;
import com.topvision.ems.cmc.facade.domain.CmStaticIp;
import com.topvision.ems.cmc.facade.domain.CmcDownChannelBaseInfo;
import com.topvision.ems.cmc.facade.domain.DocsIf3CmtsCmUsStatus;
import com.topvision.ems.cmc.sni.facade.domain.CmcPhyConfig;
import com.topvision.ems.cmc.upchannel.facade.domain.CmcUpChannelBaseInfo;
import com.topvision.ems.cmc.upchannel.facade.domain.CmcUpChannelSignalQualityInfo;
import com.topvision.ems.facade.domain.DiscoveryData;
import com.topvision.platform.message.event.CmcEntityInfo;

/**
 * @author Victor
 * @created @2011-11-13-下午01:47:16
 * 
 */
/**
 * @author bryan
 * @created @2011-12-13-下午02:21:37
 * 
 */
public class CmcDiscoveryData extends DiscoveryData {
    private static final long serialVersionUID = 2783843283681255604L;
    public static final Integer CMC_TYPE_A = 1;
    public static final Integer CMC_TYPE_B = 2;

    private Integer dataType;
    private List<Long> cmcIndexs = new ArrayList<Long>();
    private List<CmcAttribute> cmcAttributes;
    private List<CmcUpChannelBaseInfo> cmcUpChannelBaseInfos;
    private List<CmcUpChannelSignalQualityInfo> cmcUpChannelSignalQualityInfos;
    private List<CmcDownChannelBaseInfo> cmcDownChannelBaseInfos;
    private List<CmAttribute> cmAttributes;
    private List<CmcPort> cmcPorts;
    private List<CmCpe> cmCpeList;
    private List<CmStaticIp> cmStaticIpList;
    private List<DocsIf3CmtsCmUsStatus> docsIf3CmtsCmUsStatusList;
    private Map<Long, CmcEntityInfo> cmcEntityInfos;
    private List<CmcPhyConfig> cmcPhyConfigs;

    // 信道采集方式 - For CC8800B
    private Integer indexCollectType;

    public CmcDiscoveryData() {
        super();
    }

    public CmcDiscoveryData(Long entityId) {
        super(entityId);
    }

    /**
     * Base TopoType
     * 
     * @param topoType
     */
    public CmcDiscoveryData(Integer topoType) {
        super(topoType);
    }

    /**
     * Use for B Discovery
     * 
     * 
     * @param cmcIndexs
     */
    public CmcDiscoveryData(List<Long> cmcIndexs, Integer dataType) {
        this.cmcIndexs = cmcIndexs;
        this.dataType = dataType;
    }

    /**
     * Use for A Discovery
     * 
     * @param cmcEntityInfos
     */
    public CmcDiscoveryData(Map<Long, CmcEntityInfo> cmcEntityInfos, List<Long> cmcIndexs) {
        this.cmcEntityInfos = cmcEntityInfos;
        this.cmcIndexs = cmcIndexs;
    }

    /**
     * Use for B Auto Discovery
     * 
     * @param cmcIndexs
     * @param dataType
     * @param topoType
     */
    public CmcDiscoveryData(List<Long> cmcIndexs, Integer dataType, Integer topoType) {
        super(topoType);
        this.cmcIndexs = cmcIndexs;
        this.dataType = dataType;
    }

    /**
     * @return the dataType
     */
    public Integer getDataType() {
        return dataType;
    }

    /**
     * @param dataType the dataType to set
     */
    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }

    /**
     * @return the indexCollectType
     */
    public Integer getIndexCollectType() {
        return indexCollectType;
    }

    /**
     * @param indexCollectType the indexCollectType to set
     */
    public void setIndexCollectType(Integer indexCollectType) {
        this.indexCollectType = indexCollectType;
    }

    /**
     * @return the cmcIndexs
     */
    public List<Long> getCmcIndexs() {
        return cmcIndexs;
    }

    /**
     * @param cmcIndexs the cmcIndexs to set
     */
    public void setCmcIndexs(List<Long> cmcIndexs) {
        this.cmcIndexs = cmcIndexs;
    }

    /**
     * @return the cmcAttributes
     */
    public List<CmcAttribute> getCmcAttributes() {
        return cmcAttributes;
    }

    /**
     * @param cmcAttributes the cmcAttributes to set
     */
    public void setCmcAttributes(List<CmcAttribute> cmcAttributes) {
        this.cmcAttributes = cmcAttributes;
    }

    public void addCmcAttribute(CmcAttribute cmcAttribute) {
        if (cmcAttributes == null) {
            cmcAttributes = new ArrayList<>();
        }
        cmcAttributes.add(cmcAttribute);
    }

    /**
     * @return the cmAttributes
     */
    public List<CmAttribute> getCmAttributes() {
        return cmAttributes;
    }

    /**
     * @param cmAttributes
     *            the cmAttributes to set
     */
    public void setCmAttributes(List<CmAttribute> cmAttributes) {
        this.cmAttributes = cmAttributes;
    }

    public List<CmCpe> getCmCpeList() {
        return cmCpeList;
    }

    public void setCmCpeList(List<CmCpe> cmCpeList) {
        this.cmCpeList = cmCpeList;
    }

    public List<CmStaticIp> getCmStaticIpList() {
        return cmStaticIpList;
    }

    public void setCmStaticIpList(List<CmStaticIp> cmStaticIpList) {
        this.cmStaticIpList = cmStaticIpList;
    }

    public List<DocsIf3CmtsCmUsStatus> getDocsIf3CmtsCmUsStatusList() {
        return docsIf3CmtsCmUsStatusList;
    }

    public void setDocsIf3CmtsCmUsStatusList(List<DocsIf3CmtsCmUsStatus> docsIf3CmtsCmUsStatusList) {
        this.docsIf3CmtsCmUsStatusList = docsIf3CmtsCmUsStatusList;
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
     * 添加 Cmc 端口基本信息（ifTable）
     * 
     * @param cmcUpChannelBaseInfo
     *            上行信道基本信息
     */
    public void addCmcPort(CmcPort cmcPort) {
        if (cmcPorts == null) {
            cmcPorts = new ArrayList<CmcPort>();
        }
        cmcPorts.add(cmcPort);
    }

    public void addCmcPorts(List<CmcPort> cmcPortList) {
        if (cmcPorts == null) {
            cmcPorts = new ArrayList<CmcPort>();
        }
        cmcPorts.addAll(cmcPortList);
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
     * 
     * 
     * @param cmcUpChannelSignalQualityInfoList
     */
    public void addCmcUpChannelSignalQualityInfos(List<CmcUpChannelSignalQualityInfo> cmcUpChannelSignalQualityInfoList) {
        if (cmcUpChannelSignalQualityInfos == null) {
            cmcUpChannelSignalQualityInfos = new ArrayList<CmcUpChannelSignalQualityInfo>();
        }
        cmcUpChannelSignalQualityInfos.addAll(cmcUpChannelSignalQualityInfoList);
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

    public List<CmcPhyConfig> getCmcPhyConfigs() {
        return cmcPhyConfigs;
    }

    public void setCmcPhyConfigs(List<CmcPhyConfig> cmcPhyConfigs) {
        this.cmcPhyConfigs = cmcPhyConfigs;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "CmcDiscoveryData [dataType=" + dataType + ", cmcIndexs=" + cmcIndexs + ", cmcAttributes="
                + cmcAttributes + ", cmcUpChannelBaseInfos=" + cmcUpChannelBaseInfos
                + ", cmcUpChannelSignalQualityInfos=" + cmcUpChannelSignalQualityInfos + ", cmcDownChannelBaseInfos="
                + cmcDownChannelBaseInfos + ", cmAttributes=" + cmAttributes + ", cmcPorts=" + cmcPorts
                + ", cmCpeList=" + cmCpeList + ", cmStaticIpList=" + cmStaticIpList + ", docsIf3CmtsCmUsStatusList="
                + docsIf3CmtsCmUsStatusList + ", cmcEntityInfos=" + cmcEntityInfos + ", indexCollectType="
                + indexCollectType + "]";
    }

}
