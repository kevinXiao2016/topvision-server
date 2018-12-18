/***********************************************************************
 * $Id: PonOnuAuthMode.java,v1.0 2016年12月21日 上午11:24:12 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.onuauth.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponIndex;

/**
 * @author Bravin
 * @created @2016年12月21日-上午11:24:12
 *
 */
public class GponOnuAuthMode implements AliasesSuperType {
    private static final long serialVersionUID = 7666395743935398653L;
    private Long entityId;
    private Long ponIndex;
    private Long ponId;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.3.4.1.1", index = true, type = "Integer32")
    private Long ponAuthenDeviceIndex = 1L;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.3.4.1.2", index = true, type = "Integer32")
    private Long ponAuthenCardIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.3.4.1.3", index = true, type = "Integer32")
    private Long ponAuthenPortIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.3.4.1.4", writable = true, type = "Integer32")
    private Integer ponOnuAuthenMode;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.3.4.1.5", writable = true, type = "Integer32")
    private Integer ponAutoFindEnable;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    @Override
    public String toString() {
        return "GponOnuAuthMode [entityId=" + entityId + ", ponIndex=" + ponIndex + ", ponId=" + ponId
                + ", ponAuthenDeviceIndex=" + ponAuthenDeviceIndex + ", ponAuthenCardIndex=" + ponAuthenCardIndex
                + ", ponAuthenPortIndex=" + ponAuthenPortIndex + ", ponOnuAuthenMode=" + ponOnuAuthenMode
                + ", ponAutoFindEnable=" + ponAutoFindEnable + "]";
    }

    public Long getPonId() {
        return ponId;
    }

    public void setPonId(Long ponId) {
        this.ponId = ponId;
    }

    public Long getPonIndex() {
        if (ponIndex == null) {
            return new EponIndex(ponAuthenCardIndex.intValue(), ponAuthenPortIndex.intValue()).getPonIndex();
        }
        return ponIndex;
    }

    public void setPonIndex(Long ponIndex) {
        this.ponIndex = ponIndex;
        this.ponAuthenCardIndex = EponIndex.getSlotNo(ponIndex);
        this.ponAuthenPortIndex = EponIndex.getPonNo(ponIndex);
    }

    public Long getPonAuthenDeviceIndex() {
        return ponAuthenDeviceIndex;
    }

    public void setPonAuthenDeviceIndex(Long ponAuthenDeviceIndex) {
        this.ponAuthenDeviceIndex = ponAuthenDeviceIndex;
    }

    public Long getPonAuthenCardIndex() {
        return ponAuthenCardIndex;
    }

    public void setPonAuthenCardIndex(Long ponAuthenCardIndex) {
        this.ponAuthenCardIndex = ponAuthenCardIndex;
    }

    public Long getPonAuthenPortIndex() {
        return ponAuthenPortIndex;
    }

    public void setPonAuthenPortIndex(Long ponAuthenPortIndex) {
        this.ponAuthenPortIndex = ponAuthenPortIndex;
    }

    public Integer getPonOnuAuthenMode() {
        return ponOnuAuthenMode;
    }

    public void setPonOnuAuthenMode(Integer ponOnuAuthenMode) {
        this.ponOnuAuthenMode = ponOnuAuthenMode;
    }

    public Integer getPonAutoFindEnable() {
        return ponAutoFindEnable;
    }

    public void setPonAutoFindEnable(Integer ponAutoFindEnable) {
        this.ponAutoFindEnable = ponAutoFindEnable;
    }

}
