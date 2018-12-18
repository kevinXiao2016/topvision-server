package com.topvision.ems.epon.olt.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

public class OltSpectrumSwitch implements AliasesSuperType {
    private static final long serialVersionUID = -4594925490762668923L;

    private Long entityId;
    private String entityIp;
    private String oltName;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.16.1.1.0", writable = true, type = "Integer32")
    private Integer collectSwitch;
    private Integer state;// olt的在线状态

    public Long getEntityId() {
        return entityId;
    }

    public String getEntityIp() {
        return entityIp;
    }

    public String getOltName() {
        return oltName;
    }

    public Integer getCollectSwitch() {
        return collectSwitch;
    }

    public Integer getState() {
        return state;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public void setEntityIp(String entityIp) {
        this.entityIp = entityIp;
    }

    public void setOltName(String oltName) {
        this.oltName = oltName;
    }

    public void setCollectSwitch(Integer collectSwitch) {
        this.collectSwitch = collectSwitch;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SpectrumOltSwitch [entityId=");
        builder.append(entityId);
        builder.append(", entityIp=");
        builder.append(entityIp);
        builder.append(", oltName=");
        builder.append(oltName);
        builder.append(", collectSwitch=");
        builder.append(collectSwitch);
        builder.append(", state=");
        builder.append(state);
        builder.append("]");
        return builder.toString();
    }

}
