/***********************************************************************
 * $Id: UniVlanBindTable.java,v1.0 2013-11-28 上午10:20:54 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.univlanprofile.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponIndex;

/**
 * @author flack
 * @created @2013-11-28-上午10:20:54
 *
 */
public class UniVlanBindTable implements AliasesSuperType {
    private static final long serialVersionUID = -8851097723968232424L;
    private Long entityId;
    private Long uniId;
    private Long uniIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.7.3.1.1", index = true)
    private Integer bindSlotIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.7.3.1.2", index = true)
    private Integer bindPonIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.7.3.1.3", index = true)
    private Integer bindOnuIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.7.3.1.4", index = true)
    private Integer bindUniIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.7.3.1.5", writable = true, type = "Integer32")
    private Integer bindPvid;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.7.3.1.6", writable = true, type = "Integer32")
    private Integer bindProfileId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.7.3.1.7", writable = false, type = "Integer32")
    private Integer bindProfAttr;

    private String portString;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getBindSlotIndex() {
        return bindSlotIndex;
    }

    public void setBindSlotIndex(Integer bindSlotIndex) {
        this.bindSlotIndex = bindSlotIndex;
    }

    public Integer getBindPonIndex() {
        return bindPonIndex;
    }

    public void setBindPonIndex(Integer bindPonIndex) {
        this.bindPonIndex = bindPonIndex;
    }

    public Integer getBindOnuIndex() {
        return bindOnuIndex;
    }

    public void setBindOnuIndex(Integer bindOnuIndex) {
        this.bindOnuIndex = bindOnuIndex;
    }

    public Integer getBindUniIndex() {
        return bindUniIndex;
    }

    public void setBindUniIndex(Integer bindUniIndex) {
        this.bindUniIndex = bindUniIndex;
    }

    public Integer getBindPvid() {
        return bindPvid;
    }

    public void setBindPvid(Integer bindPvid) {
        this.bindPvid = bindPvid;
    }

    public Integer getBindProfileId() {
        return bindProfileId;
    }

    public void setBindProfileId(Integer bindProfileId) {
        this.bindProfileId = bindProfileId;
    }

    public Long getUniIndex() {
        if (uniIndex == null) {
            uniIndex = new EponIndex(bindSlotIndex, bindPonIndex, bindOnuIndex, 0, bindUniIndex).getUniIndex();
        }
        return uniIndex;
    }

    public void setUniIndex(Long uniIndex) {
        this.uniIndex = uniIndex;
        if (uniIndex != null) {
            bindSlotIndex = EponIndex.getSlotNo(uniIndex).intValue();
            bindPonIndex = EponIndex.getPonNo(uniIndex).intValue();
            bindOnuIndex = EponIndex.getOnuNo(uniIndex).intValue();
            bindUniIndex = EponIndex.getUniNo(uniIndex).intValue();
            portString = EponIndex.getStringFromIndex(uniIndex);
        }
    }

    public Long getUniId() {
        return uniId;
    }

    public void setUniId(Long uniId) {
        this.uniId = uniId;
    }

    public String getPortString() {
        return portString;
    }

    public void setPortString(String portString) {
        this.portString = portString;
    }

    public Integer getBindProfAttr() {
        //兼容原版本无法获取该值时,默认设置为离散配置
        //TODO 因为在采集为null时不会调用set方法,所以在get方法做默认设置
        if (bindProfAttr == null) {
            return 2;
        } else {
            return bindProfAttr;
        }
    }

    public void setBindProfAttr(Integer bindProfAttr) {
        this.bindProfAttr = bindProfAttr;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("UniVlanBindTable [entityId=");
        builder.append(entityId);
        builder.append(", uniId=");
        builder.append(uniId);
        builder.append(", uniIndex=");
        builder.append(uniIndex);
        builder.append(", bindSlotIndex=");
        builder.append(bindSlotIndex);
        builder.append(", bindPonIndex=");
        builder.append(bindPonIndex);
        builder.append(", bindOnuIndex=");
        builder.append(bindOnuIndex);
        builder.append(", bindUniIndex=");
        builder.append(bindUniIndex);
        builder.append(", bindPvid=");
        builder.append(bindPvid);
        builder.append(", bindProfileId=");
        builder.append(bindProfileId);
        builder.append(", bindProfAttr=");
        builder.append(bindProfAttr);
        builder.append(", portString=");
        builder.append(portString);
        builder.append("]");
        return builder.toString();
    }

}
