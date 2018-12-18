/***********************************************************************
 * $ OltOnuBlockExtAuthen.java,v1.0 2012-3-27 15:24:12 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onuauth.domain;

import com.topvision.framework.utils.EponIndex;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author jay
 * @created @2012-3-27-15:24:12
 */
public class OltOnuBlockExtAuthen implements AliasesSuperType {
    private static final long serialVersionUID = 7342180206202447869L;
    private Long entityId;
    private Long ponId;
    private Long onuIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.2.2.1.1", index = true)
    private Long topOnuAuthBlockExtOnuId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.2.2.1.2", writable = true, type = "OctetString")
    private String topOnuAuthBlockedExtLogicSn;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.2.2.1.3", writable = true, type = "OctetString")
    private String topOnuAuthBlockedExtPwd;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getOnuIndex() {
        return onuIndex;
    }

    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
        topOnuAuthBlockExtOnuId = EponIndex.getOnuMibIndexByIndex(onuIndex);
    }

    public Long getPonId() {
        return ponId;
    }

    public void setPonId(Long ponId) {
        this.ponId = ponId;
    }

    public String getTopOnuAuthBlockedExtLogicSn() {
        return topOnuAuthBlockedExtLogicSn;
    }

    public void setTopOnuAuthBlockedExtLogicSn(String topOnuAuthBlockedExtLogicSn) {
        this.topOnuAuthBlockedExtLogicSn = topOnuAuthBlockedExtLogicSn;
    }

    public String getTopOnuAuthBlockedExtPwd() {
        return topOnuAuthBlockedExtPwd;
    }

    public void setTopOnuAuthBlockedExtPwd(String topOnuAuthBlockedExtPwd) {
        this.topOnuAuthBlockedExtPwd = topOnuAuthBlockedExtPwd;
    }

    public Long getTopOnuAuthBlockExtOnuId() {
        return topOnuAuthBlockExtOnuId;
    }

    public void setTopOnuAuthBlockExtOnuId(Long topOnuAuthBlockExtOnuId) {
        this.topOnuAuthBlockExtOnuId = topOnuAuthBlockExtOnuId;
        onuIndex = EponIndex.getOnuIndexByMibIndex(topOnuAuthBlockExtOnuId);
    }
}
