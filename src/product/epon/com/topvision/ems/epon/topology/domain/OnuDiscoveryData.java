/***********************************************************************
 * $Id: OnuDiscoveryData.java,v1.0 2015-8-5 上午9:24:15 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.topology.domain;

import java.util.ArrayList;
import java.util.List;

import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.onu.domain.OltOnuCapability;
import com.topvision.ems.epon.onu.domain.OltOnuPonAttribute;
import com.topvision.ems.epon.onu.domain.OltTopOnuCapability;
import com.topvision.ems.epon.onu.domain.OltTopOnuProductTable;
import com.topvision.ems.epon.onu.domain.OltUniAttribute;
import com.topvision.ems.epon.onu.domain.OltUniExtAttribute;
import com.topvision.ems.facade.domain.DiscoveryData;
import com.topvision.ems.gpon.onu.domain.GponOnuCapability;
import com.topvision.ems.gpon.onu.domain.GponUniAttribute;
import com.topvision.ems.gpon.onuvoip.domain.TopGponOnuCapability;

/**
 * @author Rod John
 * @created @2015-8-5-上午9:24:15
 *
 */
public class OnuDiscoveryData extends DiscoveryData {
    private static final long serialVersionUID = 6754811237325812581L;

    private Long entityId;

    // EPON OR GPON
    private String onuEorG = "E";

    private List<Long> onuIndexs = new ArrayList<Long>();
    private List<OltOnuAttribute> oltOnuAttributes;
    private List<OltTopOnuProductTable> oltTopOnuProducts;
    private List<OltTopOnuCapability> oltTopOnuCapabilities;
    private List<OltOnuCapability> oltOnuCapabilities;
    private List<OltOnuPonAttribute> oltOnuPonAttributes;
    private List<OltUniAttribute> oltUniAttributes;
    private List<OltUniExtAttribute> oltUniExtAttributes;

    //GPON 
    private List<GponOnuCapability> gponOnuCapabilities;
    private List<TopGponOnuCapability> topGponOnuCapabilities;

    public OnuDiscoveryData() {
    }

    public OnuDiscoveryData(Long entityId) {
        this.entityId = entityId;
    }

    /**
     * @return the entityId
     */
    public Long getEntityId() {
        return entityId;
    }

    /**
     * @param entityId the entityId to set
     */
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    /**
     * @return the onuEorG
     */
    public String getOnuEorG() {
        return onuEorG;
    }

    /**
     * @param onuEorG the onuEorG to set
     */
    public void setOnuEorG(String onuEorG) {
        this.onuEorG = onuEorG;
    }

    /**
     * @return the onuIndexs
     */
    public List<Long> getOnuIndexs() {
        return onuIndexs;
    }

    /**
     * @param onuIndexs the onuIndexs to set
     */
    public void setOnuIndexs(List<Long> onuIndexs) {
        this.onuIndexs = onuIndexs;
    }

    public void addOnuIndex(Long onuIndex) {
        if (this.onuIndexs == null) {
            this.onuIndexs = new ArrayList<>();
        }
        this.onuIndexs.add(onuIndex);
    }

    /**
     * @return the oltOnuAttributes
     */
    public List<OltOnuAttribute> getOltOnuAttributes() {
        return oltOnuAttributes;
    }

    /**
     * @param oltOnuAttributes the oltOnuAttributes to set
     */
    public void setOltOnuAttributes(List<OltOnuAttribute> oltOnuAttributes) {
        this.oltOnuAttributes = oltOnuAttributes;
    }

    public void addOltOnuAttribute(OltOnuAttribute oltOnuAttribute) {
        if (this.oltOnuAttributes == null) {
            this.oltOnuAttributes = new ArrayList<>();
        }
        this.oltOnuAttributes.add(oltOnuAttribute);
    }

    /**
     * @return the oltTopOnuProducts
     */
    public List<OltTopOnuProductTable> getOltTopOnuProducts() {
        return oltTopOnuProducts;
    }

    /**
     * @param oltTopOnuProducts the oltTopOnuProducts to set
     */
    public void setOltTopOnuProducts(List<OltTopOnuProductTable> oltTopOnuProducts) {
        this.oltTopOnuProducts = oltTopOnuProducts;
    }

    /**
     * @return the oltTopOnuCapabilities
     */
    public List<OltTopOnuCapability> getOltTopOnuCapabilities() {
        return oltTopOnuCapabilities;
    }

    /**
     * @param oltTopOnuCapabilities the oltTopOnuCapabilities to set
     */
    public void setOltTopOnuCapabilities(List<OltTopOnuCapability> oltTopOnuCapabilities) {
        this.oltTopOnuCapabilities = oltTopOnuCapabilities;
    }

    public void addOltTopOnuCapabilities(OltTopOnuCapability oltTopOnuCapability) {
        if (this.oltTopOnuCapabilities == null) {
            this.oltTopOnuCapabilities = new ArrayList<>();
        }
        this.oltTopOnuCapabilities.add(oltTopOnuCapability);
    }

    /**
     * @return the oltOnuCapabilities
     */
    public List<OltOnuCapability> getOltOnuCapabilities() {
        return oltOnuCapabilities;
    }

    /**
     * @param oltOnuCapabilities the oltOnuCapabilities to set
     */
    public void setOltOnuCapabilities(List<OltOnuCapability> oltOnuCapabilities) {
        this.oltOnuCapabilities = oltOnuCapabilities;
    }

    public void addOltOnuCapabilities(OltOnuCapability oltOnuCapability) {
        if (this.oltOnuCapabilities == null) {
            this.oltOnuCapabilities = new ArrayList<>();
        }
        this.oltOnuCapabilities.add(oltOnuCapability);
    }

    /**
     * @return the oltOnuPonAttributes
     */
    public List<OltOnuPonAttribute> getOltOnuPonAttributes() {
        return oltOnuPonAttributes;
    }

    /**
     * @param oltOnuPonAttributes the oltOnuPonAttributes to set
     */
    public void setOltOnuPonAttributes(List<OltOnuPonAttribute> oltOnuPonAttributes) {
        this.oltOnuPonAttributes = oltOnuPonAttributes;
    }

    public void addOltOnuPonAttributes(OltOnuPonAttribute oltOnuPonAttribute) {
        if (this.oltOnuPonAttributes == null) {
            this.oltOnuPonAttributes = new ArrayList<>();
        }
        this.oltOnuPonAttributes.add(oltOnuPonAttribute);
    }

    /**
     * @return the oltUniAttributes
     */
    public List<OltUniAttribute> getOltUniAttributes() {
        return oltUniAttributes;
    }

    /**
     * @param oltUniAttributes the oltUniAttributes to set
     */
    public void setOltUniAttributes(List<OltUniAttribute> oltUniAttributes) {
        this.oltUniAttributes = oltUniAttributes;
    }

    /**
     * Add EPON Uni
     * 
     * @param oltUniAttribute
     */
    public void addEponUniAttributes(List<OltUniAttribute> oltUniAttribute) {
        if (this.oltUniAttributes == null) {
            this.oltUniAttributes = new ArrayList<>();
        }
        this.oltUniAttributes.addAll(oltUniAttribute);
    }

    /**
     * Add GPON Uni
     * 
     * @param gponUniAttribute
     */
    public void addGponUniAttributes(List<GponUniAttribute> gponUniAttribute) {
        if (this.oltUniAttributes == null) {
            this.oltUniAttributes = new ArrayList<>();
        }
        this.oltUniAttributes.addAll(gponUniAttribute);
    }


    /**
     * @return the oltUniExtAttributes
     */
    public List<OltUniExtAttribute> getOltUniExtAttributes() {
        return oltUniExtAttributes;
    }

    /**
     * @param oltUniExtAttributes the oltUniExtAttributes to set
     */
    public void setOltUniExtAttributes(List<OltUniExtAttribute> oltUniExtAttributes) {
        this.oltUniExtAttributes = oltUniExtAttributes;
    }

    /**
     * @return the gponOnuCapabilities
     */
    public List<GponOnuCapability> getGponOnuCapabilities() {
        return gponOnuCapabilities;
    }

    /**
     * @param gponOnuCapabilities the gponOnuCapabilities to set
     */
    public void setGponOnuCapabilities(List<GponOnuCapability> gponOnuCapabilities) {
        this.gponOnuCapabilities = gponOnuCapabilities;
    }

    /**
     * Add GPON Onu capability 
     * 
     * @param gponUniAttribute
     */
    public void addGponOnuCapability(GponOnuCapability gponUniAttribute) {
        if (this.gponOnuCapabilities == null) {
            this.gponOnuCapabilities = new ArrayList<>();
        }
        this.gponOnuCapabilities.add(gponUniAttribute);
    }
    
    public List<TopGponOnuCapability> getTopGponOnuCapabilities() {
        return topGponOnuCapabilities;
    }

    public void setTopGponOnuCapabilities(List<TopGponOnuCapability> topGponOnuCapabilities) {
        this.topGponOnuCapabilities = topGponOnuCapabilities;
    }

    /**
     * Add topGponOnuCapability 
     * 
     * @param gponUniAttribute
     */
    public void addTopGponOnuCapability(TopGponOnuCapability topGponOnuCapability) {
        if (this.topGponOnuCapabilities == null) {
            this.topGponOnuCapabilities = new ArrayList<>();
        }
        this.topGponOnuCapabilities.add(topGponOnuCapability);
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OnuDiscoveryData [entityId=");
        builder.append(entityId);
        builder.append(", onuIndexs=");
        builder.append(onuIndexs);
        builder.append(", oltOnuAttributes=");
        builder.append(oltOnuAttributes);
        builder.append(", oltTopOnuProducts=");
        builder.append(oltTopOnuProducts);
        builder.append(", oltTopOnuCapabilities=");
        builder.append(oltTopOnuCapabilities);
        builder.append(", oltOnuCapabilities=");
        builder.append(oltOnuCapabilities);
        builder.append(", oltOnuPonAttributes=");
        builder.append(oltOnuPonAttributes);
        builder.append(", oltUniAttributes=");
        builder.append(oltUniAttributes);
        builder.append(", oltUniExtAttributes=");
        builder.append(oltUniExtAttributes);
        builder.append("]");
        return builder.toString();
    }

}
