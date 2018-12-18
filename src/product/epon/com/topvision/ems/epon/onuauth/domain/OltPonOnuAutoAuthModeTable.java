package com.topvision.ems.epon.onuauth.domain;

import java.io.Serializable;

import com.topvision.framework.utils.EponIndex;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author jay
 * @created @2012-3-27-15:30:39
 */
public class OltPonOnuAutoAuthModeTable implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -2003283591030498396L;
    private Long entityId;
    private Long ponId;
    private Long ponIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.3.4.1.1", index = true)
    private Long topPonOnuAutoAuthModeCardIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.3.4.1.2", index = true)
    private Long topPonOnuAutoAuthModePonIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.3.4.1.3", writable = true, type = "Integer32")
    private Integer topPonOnuAutoAuthEnable;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getPonId() {
        return ponId;
    }

    public void setPonId(Long ponId) {
        this.ponId = ponId;
    }

    public Long getTopPonOnuAutoAuthModeCardIndex() {
        return topPonOnuAutoAuthModeCardIndex;
    }

    public void setTopPonOnuAutoAuthModeCardIndex(Long topPonOnuAutoAuthModeCardIndex) {
        this.topPonOnuAutoAuthModeCardIndex = topPonOnuAutoAuthModeCardIndex;
    }

    public Long getTopPonOnuAutoAuthModePonIndex() {
        return topPonOnuAutoAuthModePonIndex;
    }

    public void setTopPonOnuAutoAuthModePonIndex(Long topPonOnuAutoAuthModePonIndex) {
        this.topPonOnuAutoAuthModePonIndex = topPonOnuAutoAuthModePonIndex;
    }

    public Integer getTopPonOnuAutoAuthEnable() {
        return topPonOnuAutoAuthEnable;
    }

    public void setTopPonOnuAutoAuthEnable(Integer topPonOnuAutoAuthEnable) {
        this.topPonOnuAutoAuthEnable = topPonOnuAutoAuthEnable;
    }

    public Long getPonIndex() {
        ponIndex = EponIndex.getPonIndex(topPonOnuAutoAuthModeCardIndex.intValue(),
                topPonOnuAutoAuthModePonIndex.intValue());
        return ponIndex;
    }

    public void setPonIndex(Long ponIndex) {
        this.ponIndex = ponIndex;
        topPonOnuAutoAuthModeCardIndex = EponIndex.getSlotNo(ponIndex);
        topPonOnuAutoAuthModePonIndex = EponIndex.getPonNo(ponIndex);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("OltPonOnuAuthModeTable");
        sb.append("{entityId=").append(entityId);
        sb.append(", topPonOnuAutoAuthModeCardIndex=").append(topPonOnuAutoAuthModeCardIndex);
        sb.append(", topPonOnuAutoAuthModePonIndex='").append(topPonOnuAutoAuthModePonIndex).append('\'');
        sb.append(", topPonOnuAutoAuthEnable='").append(topPonOnuAutoAuthEnable).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
