/***********************************************************************
 * $Id: CmcShareSecretConfig.java,v1.0 2013-7-23 下午1:50:50 $
 * 
 * @author: dosion
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.sharesecret.facade.domain;

import java.io.Serializable;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author dosion
 * @created @2013-7-23-下午1:50:50
 * 
 */
//@Alias("cmcShareSecretConfig")
public class CmcShareSecretConfig implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -974056891555717665L;

    private Long cmcId;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.1", index = true)
    private Long ifIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.10.3.1.1", writable = true, type = "Integer32")
    private Integer sharedSecretEnabled;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.10.3.1.2", writable = true, type = "Integer32")
    private Integer sharedSecretEncrypted;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.10.3.1.3", writable = true, type = "OctetString")
    private String sharedSecretAuthStr;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.10.3.1.4", writable = true, type = "OctetString")
    private String sharedSecretCipherStr;

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Long getIfIndex() {
        return ifIndex;
    }

    public void setIfIndex(Long ifIndex) {
        this.ifIndex = ifIndex;
    }

    public Integer getSharedSecretEnabled() {
        return sharedSecretEnabled;
    }

    public void setSharedSecretEnabled(Integer sharedSecretEnabled) {
        this.sharedSecretEnabled = sharedSecretEnabled;
    }

    public Integer getSharedSecretEncrypted() {
        return sharedSecretEncrypted;
    }

    public void setSharedSecretEncrypted(Integer sharedSecretEncrypted) {
        this.sharedSecretEncrypted = sharedSecretEncrypted;
    }

    public String getSharedSecretAuthStr() {
        return sharedSecretAuthStr;
    }

    public void setSharedSecretAuthStr(String sharedSecretAuthStr) {
        this.sharedSecretAuthStr = sharedSecretAuthStr;
    }

    public String getSharedSecretCipherStr() {
        return sharedSecretCipherStr;
    }

    public void setSharedSecretCipherStr(String sharedSecretCipherStr) {
        this.sharedSecretCipherStr = sharedSecretCipherStr;
    }

}
