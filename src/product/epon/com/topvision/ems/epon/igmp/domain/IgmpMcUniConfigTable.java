/***********************************************************************
 * $ IgmpMcUniConfigTable.java,v1.0 2011-11-23 10:57:59 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.igmp.domain;

import java.util.List;

import com.topvision.ems.epon.utils.EponUtil;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.annotation.TableProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponIndex;

/**
 * @author jay
 * @created @2011-11-23-10:57:59
 */
@TableProperty(tables = { "default" })
public class IgmpMcUniConfigTable implements AliasesSuperType {
    private static final long serialVersionUID = 7342180206202447869L;
    private Long entityId;

    private Long uniIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.2.2.1.1.1", index = true)
    private Long cardNo;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.2.2.1.1.2", index = true)
    private Long ponNo;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.2.2.1.1.3", index = true)
    private Long onuNo;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.2.2.1.1.4", index = true)
    private Long uniNo;

    /**
     * 0: do not limit other value: use specific value INTEGER (0..64)
     */
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.6.2.2.1.1.5", writable = true, type = "Integer32")
    private Integer topMcUniMaxGroupQuantity;
    /**
     * onu uni mc vlan mode INTEGER { strip(1), translation(2), keep(3) }
     */
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.6.2.2.1.1.7", writable = true, type = "Integer32")
    private Integer topMcUniVlanTransIdx;
    /**
     * vlan translation map index. Valid only when topMcUniVlanMode is translation(2). INTEGER
     * (0..64)
     */
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.6.2.2.1.1.6", writable = true, type = "Integer32")
    private Integer topMcUniVlanMode;
    /**
     * Uni Port allows max 64 multicast vlans. Every two octets compose an integer, which represent
     * an vlan id. OCTET STRING (SIZE (0..128))
     */
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.6.2.2.1.1.8", writable = true, type = "OctetString")
    private String topMcUniVlanList;
    private List<Integer> topMcUniVlanListList;

    public Long getUniIndex() {
        if (uniIndex == null) {
            uniIndex = new EponIndex(cardNo.intValue(), ponNo.intValue(), onuNo.intValue(), 0, uniNo.intValue())
                    .getUniIndex();
        }
        return uniIndex;
    }

    public void setUniIndex(Long uniIndex) {
        this.uniIndex = uniIndex;
        cardNo = EponIndex.getSlotNo(uniIndex);
        ponNo = EponIndex.getPonNo(uniIndex);
        onuNo = EponIndex.getOnuNo(uniIndex);
        uniNo = EponIndex.getUniNo(uniIndex);
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

    public Integer getTopMcUniMaxGroupQuantity() {
        return topMcUniMaxGroupQuantity;
    }

    public void setTopMcUniMaxGroupQuantity(Integer topMcUniMaxGroupQuantity) {
        this.topMcUniMaxGroupQuantity = topMcUniMaxGroupQuantity;
    }

    public String getTopMcUniVlanList() {
        return topMcUniVlanList;
    }

    public void setTopMcUniVlanList(String topMcUniVlanList) {
        this.topMcUniVlanList = topMcUniVlanList;
        topMcUniVlanListList = EponUtil.getTwiceBitValueFromOcterString(topMcUniVlanList);
    }

    /**
     * @return the topMcUniVlanListList
     */
    public List<Integer> getTopMcUniVlanListList() {
        return topMcUniVlanListList;
    }

    /**
     * @param topMcUniVlanListList
     *            the topMcUniVlanListList to set
     */
    public void setTopMcUniVlanListList(List<Integer> topMcUniVlanListList) {
        this.topMcUniVlanListList = topMcUniVlanListList;
        topMcUniVlanList = EponUtil.getOcterStringFromTwoByteValueList(topMcUniVlanListList, 64);
    }

    public Integer getTopMcUniVlanMode() {
        return topMcUniVlanMode;
    }

    public void setTopMcUniVlanMode(Integer topMcUniVlanMode) {
        this.topMcUniVlanMode = topMcUniVlanMode;
    }

    public Integer getTopMcUniVlanTransIdx() {
        return topMcUniVlanTransIdx;
    }

    public void setTopMcUniVlanTransIdx(Integer topMcUniVlanTransIdx) {
        this.topMcUniVlanTransIdx = topMcUniVlanTransIdx;
    }

    public Long getUniNo() {
        return uniNo;
    }

    public void setUniNo(Long uniNo) {
        this.uniNo = uniNo;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("IgmpMcUniConfigTable");
        sb.append("{cardNo=").append(cardNo);
        sb.append(", entityId=").append(entityId);
        sb.append(", uniIndex=").append(uniIndex);
        sb.append(", ponNo=").append(ponNo);
        sb.append(", onuNo=").append(onuNo);
        sb.append(", uniNo=").append(uniNo);
        sb.append(", topMcUniMaxGroupQuantity=").append(topMcUniMaxGroupQuantity);
        sb.append(", topMcUniVlanTransIdx=").append(topMcUniVlanTransIdx);
        sb.append(", topMcUniVlanMode=").append(topMcUniVlanMode);
        sb.append(", topMcUniVlanList='").append(topMcUniVlanList).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
