package com.topvision.ems.mobile.domain;

import com.topvision.ems.cmc.cm.domain.CcmtsLocation;
import com.topvision.ems.cmc.cm.domain.OltLocation;

/**
 * Created by jay on 14-6-21.
 */
public class Location {
    private Long entityId;
    private Long cmcId;
    private Long typeId;
    private String typeName;
    private boolean hasOlt = false;
    private OltLocation oltLocation;
    private CcmtsLocation ccmtsLocation;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public OltLocation getOltLocation() {
        return oltLocation;
    }

    public void setOltLocation(OltLocation oltLocation) {
        this.oltLocation = oltLocation;
    }

    public CcmtsLocation getCcmtsLocation() {
        return ccmtsLocation;
    }

    public void setCcmtsLocation(CcmtsLocation ccmtsLocation) {
        this.ccmtsLocation = ccmtsLocation;
    }

    public boolean isHasOlt() {
        return hasOlt;
    }

    public void setHasOlt(boolean hasOlt) {
        this.hasOlt = hasOlt;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
