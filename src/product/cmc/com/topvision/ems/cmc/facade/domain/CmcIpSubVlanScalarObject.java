package com.topvision.ems.cmc.facade.domain;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

@Alias("cmcIpSubVlanScalarObject")
public class CmcIpSubVlanScalarObject implements AliasesSuperType{
    private static final long serialVersionUID = -102776194745825905L;
    private Long cmcId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.13.1.1.0", writable = true, type = "Integer32")
    private Integer topCcmtsIpSubVlanCfi;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.13.1.2.0", writable = true, type = "Integer32")
    private Integer topCcmtsIpSubVlanTpid;
    private String ipSubVlanTpid;

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Integer getTopCcmtsIpSubVlanCfi() {
        return topCcmtsIpSubVlanCfi;
    }

    public void setTopCcmtsIpSubVlanCfi(Integer topCcmtsIpSubVlanCfi) {
        this.topCcmtsIpSubVlanCfi = topCcmtsIpSubVlanCfi;
    }

    public Integer getTopCcmtsIpSubVlanTpid() {
        if (topCcmtsIpSubVlanTpid == null) {
            topCcmtsIpSubVlanTpid = Integer.parseInt(ipSubVlanTpid, 16);
        }
        return topCcmtsIpSubVlanTpid;
    }

    public void setTopCcmtsIpSubVlanTpid(Integer topCcmtsIpSubVlanTpid) {
        this.topCcmtsIpSubVlanTpid = topCcmtsIpSubVlanTpid;
    }

    public String getIpSubVlanTpid() {
        if (ipSubVlanTpid == null) {
            ipSubVlanTpid = Integer.toHexString(topCcmtsIpSubVlanTpid);
        }
        return ipSubVlanTpid;
    }

    public void setIpSubVlanTpid(String ipSubVlanTpid) {
        this.ipSubVlanTpid = ipSubVlanTpid;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcIpSubVlanScalarObject [cmcId=");
        builder.append(cmcId);
        builder.append(", topCcmtsIpSubVlanCfi=");
        builder.append(topCcmtsIpSubVlanCfi);
        builder.append(", topCcmtsIpSubVlanTpid=");
        builder.append(topCcmtsIpSubVlanTpid);
        builder.append("]");
        return builder.toString();
    }

}
