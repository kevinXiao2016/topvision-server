/***********************************************************************
 * $Id: OltSlotMapTable.java,v1.0 2014-9-25 上午11:08:10 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.olt.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author flack
 * @created @2014-9-25-上午11:08:10
 * 维护板卡物理槽位与逻辑槽位的映射关系 
 */
public class OltSlotMapTable implements AliasesSuperType {
    private static final long serialVersionUID = 3275336341050654552L;

    private Long entityId;
    //板卡物理槽位
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.1.3.2.1.1", index = true, type = "Integer32")
    private Integer slotPhyNo;
    //板卡逻辑槽位
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.1.3.2.1.2", type = "Integer32")
    private Integer slotLogNo;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getSlotPhyNo() {
        return slotPhyNo;
    }

    public void setSlotPhyNo(Integer slotPhyNo) {
        this.slotPhyNo = slotPhyNo;
    }

    public Integer getSlotLogNo() {
        return slotLogNo;
    }

    public void setSlotLogNo(Integer slotLogNo) {
        this.slotLogNo = slotLogNo;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OltSlotMapTable [entityId=");
        builder.append(entityId);
        builder.append(", slotPhyNo=");
        builder.append(slotPhyNo);
        builder.append(", slotLogNo=");
        builder.append(slotLogNo);
        builder.append("]");
        return builder.toString();
    }

}
