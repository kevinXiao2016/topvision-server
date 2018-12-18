package com.topvision.ems.epon.vlan.domain;

import java.io.Serializable;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.annotation.TableProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author haojie
 * @created @2012-4-11-上午11:48:38
 */
@TableProperty(tables = { "default" })
public class TopOltVlanConfigTable implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 8882542634073462133L;
    private Long entityId;
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.7.3.1.1", index = true)
    private Integer vlanIndex;
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.7.3.1.2", writable = true, type = "Integer32")
    private Integer topMcFloodMode;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getVlanIndex() {
        return vlanIndex;
    }

    public void setVlanIndex(Integer vlanIndex) {
        this.vlanIndex = vlanIndex;
    }

    public Integer getTopMcFloodMode() {
        return topMcFloodMode;
    }

    public void setTopMcFloodMode(Integer topMcFloodMode) {
        this.topMcFloodMode = topMcFloodMode;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("VlanAttribute");
        sb.append(", entityId=").append(entityId);
        sb.append(", vlanIndex=").append(vlanIndex);
        sb.append(", topMcFloodMode=").append(topMcFloodMode);
        sb.append('}');
        return sb.toString();
    }
}