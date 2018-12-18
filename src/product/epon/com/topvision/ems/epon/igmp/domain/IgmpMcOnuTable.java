/***********************************************************************
 * $ IgmpMcOnuTable.java,v1.0 2011-11-23 10:39:11 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.igmp.domain;

import com.topvision.framework.utils.EponIndex;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.annotation.TableProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author jay
 * @created @2011-11-23-10:39:11
 */
@TableProperty(tables = { "default" })
public class IgmpMcOnuTable implements AliasesSuperType {
    private static final long serialVersionUID = 7342180206202447869L;
    private Long entityId;
    private Long onuIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.2.1.1.1.1", index = true)
    private Long cardNo;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.2.1.1.1.2", index = true)
    private Long ponNo;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.2.1.1.1.3", index = true)
    private Long onuNo;

    /**
     * igmp mode, depend on igmpMode in igmpEntityEntry INTEGER { ctc(1), snooping(2), disable(3) }
     */
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.6.2.1.1.1.4", writable = true, type = "Integer32")
    private Integer topMcOnuMode;
    /**
     * onu fast leave INTEGER { enable(1), disable(2) }
     */
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.6.2.1.1.1.5", writable = true, type = "Integer32")
    private Integer topMcOnuFastLeave;

    public Long getOnuIndex() {
        if (onuIndex == null) {
            onuIndex = new EponIndex(cardNo.intValue(), ponNo.intValue(), onuNo.intValue()).getOnuIndex();
        }
        return onuIndex;
    }

    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
        cardNo = EponIndex.getSlotNo(onuIndex);
        ponNo = EponIndex.getPonNo(onuIndex);
        onuNo = EponIndex.getOnuNo(onuIndex);
    }

    public Long getCardNo() {
        return cardNo;
    }

    public void setCardNo(Long cardNo) {
        this.cardNo = cardNo;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getOnuNo() {
        return onuNo;
    }

    public void setOnuNo(Long onuNo) {
        this.onuNo = onuNo;
    }

    public Long getPonNo() {
        return ponNo;
    }

    public void setPonNo(Long ponNo) {
        this.ponNo = ponNo;
    }

    public Integer getTopMcOnuFastLeave() {
        return topMcOnuFastLeave;
    }

    public void setTopMcOnuFastLeave(Integer topMcOnuFastLeave) {
        this.topMcOnuFastLeave = topMcOnuFastLeave;
    }

    public Integer getTopMcOnuMode() {
        return topMcOnuMode;
    }

    public void setTopMcOnuMode(Integer topMcOnuMode) {
        this.topMcOnuMode = topMcOnuMode;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("IgmpMcOnuTable");
        sb.append("{cardNo=").append(cardNo);
        sb.append(", entityId=").append(entityId);
        sb.append(", onuIndex=").append(onuIndex);
        sb.append(", ponNo=").append(ponNo);
        sb.append(", onuNo=").append(onuNo);
        sb.append(", topMcOnuMode=").append(topMcOnuMode);
        sb.append(", topMcOnuFastLeave=").append(topMcOnuFastLeave);
        sb.append('}');
        return sb.toString();
    }
}
