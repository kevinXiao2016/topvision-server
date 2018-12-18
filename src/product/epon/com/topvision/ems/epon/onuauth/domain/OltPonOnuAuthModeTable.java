/***********************************************************************
 * $ OltPonOnuAuthModeTable.java,v1.0 2012-3-27 15:30:39 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onuauth.domain;

import java.io.Serializable;

import com.topvision.framework.utils.EponIndex;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author jay
 * @created @2012-3-27-15:30:39
 */
public class OltPonOnuAuthModeTable implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 7342180206202447869L;
    private Long entityId;
    private Long ponId;
    private Long ponIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.3.3.1.1", index = true)
    private Long topPonOnuAuthModeCardIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.3.3.1.2", index = true)
    private Long topPonOnuAuthModePonIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.3.3.1.3", writable = true, type = "Integer32")
    private Integer topPonOnuAuthMode;// 1:auto 2:mac 3:mix 4:sn 5:sn+pw

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getTopPonOnuAuthModeCardIndex() {
        return topPonOnuAuthModeCardIndex;
    }

    public void setTopPonOnuAuthModeCardIndex(Long topPonOnuAuthModeCardIndex) {
        this.topPonOnuAuthModeCardIndex = topPonOnuAuthModeCardIndex;
    }

    public Integer getTopPonOnuAuthMode() {
        return topPonOnuAuthMode;
    }

    public void setTopPonOnuAuthMode(Integer topPonOnuAuthMode) {
        this.topPonOnuAuthMode = topPonOnuAuthMode;
    }

    public Long getTopPonOnuAuthModePonIndex() {
        return topPonOnuAuthModePonIndex;
    }

    public void setTopPonOnuAuthModePonIndex(Long topPonOnuAuthModePonIndex) {
        this.topPonOnuAuthModePonIndex = topPonOnuAuthModePonIndex;
    }

    public Long getPonId() {
        return ponId;
    }

    public void setPonId(Long ponId) {
        this.ponId = ponId;
    }

    public Long getPonIndex() {
        ponIndex = EponIndex.getPonIndex(topPonOnuAuthModeCardIndex.intValue(), topPonOnuAuthModePonIndex.intValue());
        return ponIndex;
    }

    public void setPonIndex(Long ponIndex) {
        this.ponIndex = ponIndex;
        topPonOnuAuthModeCardIndex = EponIndex.getSlotNo(ponIndex);
        topPonOnuAuthModePonIndex = EponIndex.getPonNo(ponIndex);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("OltPonOnuAuthModeTable");
        sb.append("{entityId=").append(entityId);
        sb.append(", topPonOnuAuthModeCardIndex=").append(topPonOnuAuthModeCardIndex);
        sb.append(", topPonOnuAuthModePonIndex='").append(topPonOnuAuthModePonIndex).append('\'');
        sb.append(", topPonOnuAuthMode='").append(topPonOnuAuthMode).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
