/***********************************************************************
 * $Id: GponOnuUniPvid.java,v1.0 2016年12月24日 下午2:48:01 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.onu.domain;

import java.io.Serializable;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponIndex;

/**
 * @author lizongtian
 * @created @2016年12月24日-下午2:48:01
 *
 */
public class GponOnuUniPvid implements Serializable, AliasesSuperType {

    private static final long serialVersionUID = -998970201919437554L;
    private Long entityId;
    private Long uniIndex;
    private Long uniId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.5.2.1.1.1", index = true)
    private Long gponOnuCardIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.5.2.1.1.2", index = true)
    private Long gponOnuPonIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.5.2.1.1.3", index = true)
    private Long gponOnuIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.5.2.1.1.4", index = true)
    private Long gponOnuUniIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.5.2.1.1.5", writable = true, type = "Integer32")
    private Integer gponOnuUniPvid;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.5.2.1.1.6", writable = true, type = "Integer32")
    private Integer gponOnuUniPri;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getUniIndex() {
        if (uniIndex == null) {
            uniIndex = EponIndex.getUniIndex(gponOnuCardIndex, gponOnuPonIndex, gponOnuIndex, gponOnuUniIndex);
        }
        return uniIndex;
    }

    public void setUniIndex(Long uniIndex) {
        this.uniIndex = uniIndex;
        if (uniIndex != null) {
            gponOnuCardIndex = EponIndex.getSlotNo(uniIndex);
            gponOnuPonIndex = EponIndex.getPonNo(uniIndex);
            gponOnuIndex = EponIndex.getOnuNo(uniIndex);
            gponOnuUniIndex = EponIndex.getUniNo(uniIndex);
        }
    }

    public Long getGponOnuCardIndex() {
        return gponOnuCardIndex;
    }

    public void setGponOnuCardIndex(Long gponOnuCardIndex) {
        this.gponOnuCardIndex = gponOnuCardIndex;
    }

    public Long getGponOnuPonIndex() {
        return gponOnuPonIndex;
    }

    public void setGponOnuPonIndex(Long gponOnuPonIndex) {
        this.gponOnuPonIndex = gponOnuPonIndex;
    }

    public Long getGponOnuIndex() {
        return gponOnuIndex;
    }

    public void setGponOnuIndex(Long gponOnuIndex) {
        this.gponOnuIndex = gponOnuIndex;
    }

    public Long getGponOnuUniIndex() {
        return gponOnuUniIndex;
    }

    public void setGponOnuUniIndex(Long gponOnuUniIndex) {
        this.gponOnuUniIndex = gponOnuUniIndex;
    }

    public Integer getGponOnuUniPvid() {
        return gponOnuUniPvid;
    }

    public void setGponOnuUniPvid(Integer gponOnuUniPvid) {
        this.gponOnuUniPvid = gponOnuUniPvid;
    }

    public Integer getGponOnuUniPri() {
        return gponOnuUniPri;
    }

    public void setGponOnuUniPri(Integer gponOnuUniPri) {
        this.gponOnuUniPri = gponOnuUniPri;
    }

    public Long getUniId() {
        return uniId;
    }

    public void setUniId(Long uniId) {
        this.uniId = uniId;
    }

}
