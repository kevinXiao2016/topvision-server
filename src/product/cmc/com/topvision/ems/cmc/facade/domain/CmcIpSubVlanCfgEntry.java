package com.topvision.ems.cmc.facade.domain;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.domain.IpsAddress;

@Alias("cmcIpSubVlanCfgEntry")
public class CmcIpSubVlanCfgEntry implements AliasesSuperType{
    private static final long serialVersionUID = -2254024733019086126L;
    private Long cmcId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.13.2.1.1", index = true)
    private Integer topCcmtsIpSubVlanIfIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.13.2.1.2", index = true)
    private IpsAddress topCcmtsIpIndex;
    private String topCcmtsIpSubVlanIpIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.13.2.1.3", index = true)
    private IpsAddress topCcmtsMaskIndex;
    private String topCcmtsIpSubVlanIpMaskIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.13.2.1.4", writable = true, type = "Integer32")
    private Integer topCcmtsIpSubVlanVlanId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.13.2.1.5", writable = true, type = "Integer32")
    private Integer topCcmtsIpSubVlanPri;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.13.2.1.6", writable = true, type = "Integer32")
    private Integer topCcmtsIpSubVlanRowStatus;

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Integer getTopCcmtsIpSubVlanIfIndex() {
        return topCcmtsIpSubVlanIfIndex;
    }

    public void setTopCcmtsIpSubVlanIfIndex(Integer topCcmtsIpSubVlanIfIndex) {
        this.topCcmtsIpSubVlanIfIndex = topCcmtsIpSubVlanIfIndex;
    }

    public String getTopCcmtsIpSubVlanIpIndex() {
        if (topCcmtsIpSubVlanIpIndex == null) {
            topCcmtsIpSubVlanIpIndex = topCcmtsIpIndex.toString();
        }
        return topCcmtsIpSubVlanIpIndex;
    }

    public void setTopCcmtsIpSubVlanIpIndex(String topCcmtsIpSubVlanIpIndex) {
        this.topCcmtsIpSubVlanIpIndex = topCcmtsIpSubVlanIpIndex;
    }

    public String getTopCcmtsIpSubVlanIpMaskIndex() {
        if (topCcmtsIpSubVlanIpMaskIndex == null) {
            topCcmtsIpSubVlanIpMaskIndex = topCcmtsMaskIndex.toString();
        }
        return topCcmtsIpSubVlanIpMaskIndex;
    }

    public void setTopCcmtsIpSubVlanIpMaskIndex(String topCcmtsIpSubVlanIpMaskIndex) {
        this.topCcmtsIpSubVlanIpMaskIndex = topCcmtsIpSubVlanIpMaskIndex;
    }

    public Integer getTopCcmtsIpSubVlanVlanId() {
        return topCcmtsIpSubVlanVlanId;
    }

    public void setTopCcmtsIpSubVlanVlanId(Integer topCcmtsIpSubVlanVlanId) {
        this.topCcmtsIpSubVlanVlanId = topCcmtsIpSubVlanVlanId;
    }

    public Integer getTopCcmtsIpSubVlanPri() {
        return topCcmtsIpSubVlanPri;
    }

    public void setTopCcmtsIpSubVlanPri(Integer topCcmtsIpSubVlanPri) {
        this.topCcmtsIpSubVlanPri = topCcmtsIpSubVlanPri;
    }

    public Integer getTopCcmtsIpSubVlanRowStatus() {
        return topCcmtsIpSubVlanRowStatus;
    }

    public void setTopCcmtsIpSubVlanRowStatus(Integer topCcmtsIpSubVlanRowStatus) {
        this.topCcmtsIpSubVlanRowStatus = topCcmtsIpSubVlanRowStatus;
    }

    public IpsAddress getTopCcmtsIpIndex() {
        topCcmtsIpIndex = new IpsAddress(topCcmtsIpSubVlanIpIndex);
        return topCcmtsIpIndex;
    }

    public void setTopCcmtsIpIndex(IpsAddress topCcmtsIpIndex) {
        this.topCcmtsIpIndex = topCcmtsIpIndex;
    }

    public IpsAddress getTopCcmtsMaskIndex() {
        topCcmtsMaskIndex = new IpsAddress(topCcmtsIpSubVlanIpMaskIndex);
        return topCcmtsMaskIndex;
    }

    public void setTopCcmtsMaskIndex(IpsAddress topCcmtsMaskIndex) {
        this.topCcmtsMaskIndex = topCcmtsMaskIndex;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcIpSubVlanCfgEntry [cmcId=");
        builder.append(cmcId);
        builder.append(", topCcmtsIpSubVlanIfIndex=");
        builder.append(topCcmtsIpSubVlanIfIndex);
        builder.append(", topCcmtsIpSubVlanIpIndex=");
        builder.append(topCcmtsIpSubVlanIpIndex);
        builder.append(", topCcmtsIpSubVlanIpMaskIndex=");
        builder.append(topCcmtsIpSubVlanIpMaskIndex);
        builder.append(", topCcmtsIpSubVlanVlanId=");
        builder.append(topCcmtsIpSubVlanVlanId);
        builder.append(", topCcmtsIpSubVlanPri=");
        builder.append(topCcmtsIpSubVlanPri);
        builder.append(", topCcmtsIpSubVlanRowStatus=");
        builder.append(topCcmtsIpSubVlanRowStatus);
        builder.append("]");
        return builder.toString();
    }

}
