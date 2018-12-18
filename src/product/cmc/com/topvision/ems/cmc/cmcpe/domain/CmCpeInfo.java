package com.topvision.ems.cmc.cmcpe.domain;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

@Alias("cmCpeInfo")
public class CmCpeInfo implements AliasesSuperType {

    private static final long serialVersionUID = 432598306196359760L;
    private String cpeIp;
    private String cpeMac;
    private String cmcAlias;
    private String cmcIp;
    private String cmIp;
    private String cmMac;

    public String getCpeIp() {
        return cpeIp;
    }

    public void setCpeIp(String cpeIp) {
        this.cpeIp = cpeIp;
    }

    public String getCpeMac() {
        return cpeMac;
    }

    public void setCpeMac(String cpeMac) {
        this.cpeMac = cpeMac;
    }

    public String getCmcAlias() {
        return cmcAlias;
    }

    public void setCmcAlias(String cmcAlias) {
        this.cmcAlias = cmcAlias;
    }

    public String getCmcIp() {
        return cmcIp;
    }

    public void setCmcIp(String cmcIp) {
        this.cmcIp = cmcIp;
    }

    public String getCmIp() {
        return cmIp;
    }

    public void setCmIp(String cmIp) {
        this.cmIp = cmIp;
    }

    public String getCmMac() {
        return cmMac;
    }

    public void setCmMac(String cmMac) {
        this.cmMac = cmMac;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmCpeInfo [cpeIp=");
        builder.append(cpeIp);
        builder.append(", cpeMac=");
        builder.append(cpeMac);
        builder.append(", cmcAlias=");
        builder.append(cmcAlias);
        builder.append(", cmcIp=");
        builder.append(cmcIp);
        builder.append(", cmIp=");
        builder.append(cmIp);
        builder.append(", cmMac=");
        builder.append(cmMac);
        builder.append("]");
        return builder.toString();
    }

}
