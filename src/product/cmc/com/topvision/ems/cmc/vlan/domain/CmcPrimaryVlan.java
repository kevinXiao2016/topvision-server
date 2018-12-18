package com.topvision.ems.cmc.vlan.domain;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.constants.Symbol;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author bryan
 * @created @2012-8-28-下午07:49:57
 * 
 */
@Alias("cmcPrimaryVlan")
public class CmcPrimaryVlan implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 2995382088692229893L;
    private Long entityId;
    private Long cmcId;
    private Integer productType;
    private Long vlanId;
    private String vlanName;
    private String priIpAddr;
    private String defaultRoute;
    private String priIpMask;
    private String communityRO;
    private String communityRW;
    private String taggedPort;
    private String untaggedPort;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Integer getProductType() {
        return productType;
    }

    public void setProductType(Integer productType) {
        this.productType = productType;
    }

    public Long getVlanId() {
        return vlanId;
    }

    public void setVlanId(Long vlanId) {
        this.vlanId = vlanId;
    }

    public String getVlanName() {
        return vlanName;
    }

    public void setVlanName(String vlanName) {
        this.vlanName = vlanName;
    }

    public String getPriIpAddr() {
        return priIpAddr;
    }

    public void setPriIpAddr(String priIpAddr) {
        this.priIpAddr = priIpAddr;
    }

    public String getDefaultRoute() {
        return defaultRoute;
    }

    public void setDefaultRoute(String defaultRoute) {
        this.defaultRoute = defaultRoute;
    }

    public String getPriIpMask() {
        return priIpMask;
    }

    public void setPriIpMask(String priIpMask) {
        this.priIpMask = priIpMask;
    }

    public String getCommunityRO() {
        return communityRO;
    }

    public void setCommunityRO(String communityRO) {
        this.communityRO = communityRO;
    }

    public String getCommunityRW() {
        return communityRW;
    }

    public void setCommunityRW(String communityRW) {
        this.communityRW = communityRW;
    }

    public String getTaggedPort() {
        return taggedPort;
    }

    public void setTaggedPort(String taggedPort) {
        this.taggedPort = taggedPort;
    }

    public String getUntaggedPort() {
        return untaggedPort;
    }

    public void setUntaggedPort(String untaggedPort) {
        this.untaggedPort = untaggedPort;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcPrimaryVlan [entityId=");
        builder.append(entityId);
        builder.append(", cmcId=");
        builder.append(cmcId);
        builder.append(", productType=");
        builder.append(productType);
        builder.append(", vlanId=");
        builder.append(vlanId);
        builder.append(", vlanName=");
        builder.append(vlanName);
        builder.append(", priIpAddr=");
        builder.append(priIpAddr);
        builder.append(", defaultRoute=");
        builder.append(defaultRoute);
        builder.append(", priIpMask=");
        builder.append(priIpMask);
        builder.append(", communityRO=");
        builder.append(communityRO);
        builder.append(", communityRW=");
        builder.append(communityRW);
        builder.append(", taggedPort=");
        builder.append(taggedPort);
        builder.append(", untaggedPort=");
        builder.append(untaggedPort);
        builder.append(Symbol.BRACKET_RIGHT);
        return builder.toString();
    }

}
