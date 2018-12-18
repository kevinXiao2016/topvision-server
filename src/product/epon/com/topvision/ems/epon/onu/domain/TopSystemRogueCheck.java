package com.topvision.ems.epon.onu.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

public class TopSystemRogueCheck implements AliasesSuperType {

    private static final long serialVersionUID = 7487751091926543525L;

    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.1.13.1.0", writable = true, type = "Integer32")
    private Integer systemRogueCheck;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getSystemRogueCheck() {
        return systemRogueCheck;
    }

    public void setSystemRogueCheck(Integer systemRogueCheck) {
        this.systemRogueCheck = systemRogueCheck;
    }

}
