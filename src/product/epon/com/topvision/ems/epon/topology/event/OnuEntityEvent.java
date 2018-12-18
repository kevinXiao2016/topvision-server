/***********************************************************************
 * $Id: OnuEntityEvent.java,v1.0 2015-4-22 上午10:14:10 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.topology.event;

import java.util.ArrayList;
import java.util.List;

import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.platform.message.event.EmsEventObject;

/**
 * @author flack
 * @created @2015-4-22-上午10:14:10
 *
 */
public class OnuEntityEvent extends EmsEventObject<OnuEntityListener> {
    private static final long serialVersionUID = -3172378052015986905L;
    public static final String STANDARD_OLT_TOPO = "standard";
    public static final String TOPVISION_OLT_TOPO = "topvision";

    private Long entityId;
    private List<Long> onuIndexList = new ArrayList<>();
    private List<OltOnuAttribute> onuAttributes = new ArrayList<>();
    // only use in delete onu info
    private List<Long> del_onuId = new ArrayList<>();
    private String topoType = STANDARD_OLT_TOPO;

    public OnuEntityEvent(Object source) {
        super(source);
    }
    
    
    public OnuEntityEvent(Object source, String topoType) {
        super(source);
        this.topoType = topoType;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    /**
     * @return the onuIndexList
     */
    public List<Long> getOnuIndexList() {
        return onuIndexList;
    }

    /**
     * @param onuIndexList the onuIndexList to set
     */
    public void setOnuIndexList(List<Long> onuIndexList) {
        this.onuIndexList = onuIndexList;
    }

    /**
     * @return the onuAttributes
     */
    public List<OltOnuAttribute> getOnuAttributes() {
        return onuAttributes;
    }

    /**
     * @param onuAttributes the onuAttributes to set
     */
    public void setOnuAttributes(List<OltOnuAttribute> onuAttributes) {
        this.onuAttributes = onuAttributes;
    }

    /**
     * 
     * 
     * @param onuId
     */
    public void addDelOnuInfo(Long onuId) {
        del_onuId.add(onuId);
    }

    /**
     * @return the del_onuId
     */
    public List<Long> getDel_onuId() {
        return del_onuId;
    }

    /**
     * @param del_onuId the del_onuId to set
     */
    public void setDel_onuId(List<Long> del_onuId) {
        this.del_onuId = del_onuId;
    }

    /**
     * 
     * 
     * @param oltOnuAttribute
     */
    public void addOnuEntity(OltOnuAttribute oltOnuAttribute) {
        onuAttributes.add(oltOnuAttribute);
        onuIndexList.add(oltOnuAttribute.getOnuIndex());
    }

    /**
     * @return the topoType
     */
    public String getTopoType() {
        return topoType;
    }

    /**
     * @param topoType the topoType to set
     */
    public void setTopoType(String topoType) {
        this.topoType = topoType;
    }

}
