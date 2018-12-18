/***********************************************************************
 * $Id: CmcPhyConfig.java,v1.0 2013-4-23 下午3:32:05 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.sni.facade.domain;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author haojie
 * @created @2013-4-23-下午3:32:05
 *
 */
@Alias("cmcPhyConfig")
public class CmcPhyConfig implements AliasesSuperType {
    private static final long serialVersionUID = -864246216241513182L;
    private Long cmcId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.5.5.1.1", index = true)
    private Integer phyIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.5.5.1.2", writable = true, type = "Integer32")
    private Integer topCcmtsSniInt;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.5.5.1.3")
    private Integer topCcmtsSniCurrentMedia;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.5.5.1.4")
    private Integer topCcmtsSniDuplexMod;

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Integer getPhyIndex() {
        return phyIndex;
    }

    public void setPhyIndex(Integer phyIndex) {
        this.phyIndex = phyIndex;
    }

    public Integer getTopCcmtsSniInt() {
        return topCcmtsSniInt;
    }

    public void setTopCcmtsSniInt(Integer topCcmtsSniInt) {
        this.topCcmtsSniInt = topCcmtsSniInt;
    }

    public Integer getTopCcmtsSniCurrentMedia() {
        return topCcmtsSniCurrentMedia;
    }

    public void setTopCcmtsSniCurrentMedia(Integer topCcmtsSniCurrentMedia) {
        this.topCcmtsSniCurrentMedia = topCcmtsSniCurrentMedia;
    }

    public Integer getTopCcmtsSniDuplexMod() {
        return topCcmtsSniDuplexMod;
    }

    public void setTopCcmtsSniDuplexMod(Integer topCcmtsSniDuplexMod) {
        this.topCcmtsSniDuplexMod = topCcmtsSniDuplexMod;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcPhyConfig [cmcId=");
        builder.append(cmcId);
        builder.append(", phyIndex=");
        builder.append(phyIndex);
        builder.append(", topCcmtsSniInt=");
        builder.append(topCcmtsSniInt);
        builder.append(", topCcmtsSniCurrentMedia=");
        builder.append(topCcmtsSniCurrentMedia);
        builder.append(", topCcmtsSniDuplexMod=");
        builder.append(topCcmtsSniDuplexMod);
        builder.append("]");
        return builder.toString();
    }

}
