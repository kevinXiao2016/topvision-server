/***********************************************************************
 * $Id: OltOnuCapability.java,v1.0 2011-9-26 上午09:18:16 $
 * 
 * @author: zhanglongyang
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.domain;

import java.io.Serializable;
import java.util.List;

import com.topvision.ems.epon.utils.EponUtil;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponIndex;

/**
 * Onu能力
 * 
 * @author zhanglongyang
 * 
 */
public class OltOnuCapability implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -4682843933096808561L;
    private Long entityId;
    private Long onuId;
    private Long ponId;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.3.1.1", index = true)
    private Long onuMibIndex;
    private Long onuIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.3.1.2")
    private Long onuGePortNum;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.3.1.3")
    private String onuGePortBitmap;
    private List<Integer> onuGePortList;
    private String onuGePortListStr;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.3.1.4")
    private Long onuFePortNum;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.3.1.5")
    private String onuFePortBitmap;
    private List<Integer> onuFePortList;
    private String onuFePortListStr;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.3.1.6")
    private Long onuQueueNumUplink;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.3.1.7")
    private Long onuMaxQueueNumUplink;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.3.1.8")
    private Long onuQueueNumDownlink;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.3.1.9")
    private Long onuMaxQueueNumDownlink;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.3.1.10", writable = true, type = "Integer32")
    private Integer onuFecEnable;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.3.1.11")
    private Long onuEncryptMode;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.3.1.12")
    private Long onuEncryptKeyExchangeTime;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getOnuEncryptKeyExchangeTime() {
        return onuEncryptKeyExchangeTime;
    }

    public void setOnuEncryptKeyExchangeTime(Long onuEncryptKeyExchangeTime) {
        this.onuEncryptKeyExchangeTime = onuEncryptKeyExchangeTime;
    }

    public Long getOnuEncryptMode() {
        return onuEncryptMode;
    }

    public void setOnuEncryptMode(Long onuEncryptMode) {
        this.onuEncryptMode = onuEncryptMode;
    }

    public Integer getOnuFecEnable() {
        return onuFecEnable;
    }

    public void setOnuFecEnable(Integer onuFecEnable) {
        this.onuFecEnable = onuFecEnable;
    }

    public String getOnuFePortBitmap() {
        return onuFePortBitmap;
    }

    public void setOnuFePortBitmap(String onuFePortBitmap) {
        this.onuFePortBitmap = onuFePortBitmap;
        onuFePortList = EponUtil.getOnuPortListFromMibBitMap(onuFePortBitmap);
        StringBuilder result = new StringBuilder();
        boolean flag = false;
        for (Integer string : onuFePortList) {
            if (flag) {
                result.append(",");
            } else {
                flag = true;
            }
            result.append(string);
        }
        onuFePortListStr = result.toString();
    }

    public Long getOnuFePortNum() {
        return onuFePortNum;
    }

    public void setOnuFePortNum(Long onuFePortNum) {
        this.onuFePortNum = onuFePortNum;
    }

    public String getOnuGePortBitmap() {
        return onuGePortBitmap;
    }

    public void setOnuGePortBitmap(String onuGePortBitmap) {
        this.onuGePortBitmap = onuGePortBitmap;
        onuGePortList = EponUtil.getOnuPortListFromMibBitMap(onuGePortBitmap);
        StringBuilder result = new StringBuilder();
        boolean flag = false;
        for (Integer string : onuGePortList) {
            if (flag) {
                result.append(",");
            } else {
                flag = true;
            }
            result.append(string);
        }
        onuGePortListStr = result.toString();
    }

    public Long getOnuGePortNum() {
        return onuGePortNum;
    }

    public void setOnuGePortNum(Long onuGePortNum) {
        this.onuGePortNum = onuGePortNum;
    }

    public Long getOnuId() {
        return onuId;
    }

    public void setOnuId(Long onuId) {
        this.onuId = onuId;
    }

    /**
     * @return the onuIndex
     */
    public Long getOnuIndex() {
        return onuIndex;
    }

    /**
     * @param onuIndex
     *            the onuIndex to set
     */
    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
        onuMibIndex = EponIndex.getOnuMibIndexByIndex(onuIndex);
    }

    public Long getOnuMaxQueueNumDownlink() {
        return onuMaxQueueNumDownlink;
    }

    public void setOnuMaxQueueNumDownlink(Long onuMaxQueueNumDownlink) {
        this.onuMaxQueueNumDownlink = onuMaxQueueNumDownlink;
    }

    public Long getOnuMaxQueueNumUplink() {
        return onuMaxQueueNumUplink;
    }

    public void setOnuMaxQueueNumUplink(Long onuMaxQueueNumUplink) {
        this.onuMaxQueueNumUplink = onuMaxQueueNumUplink;
    }

    public Long getOnuQueueNumDownlink() {
        return onuQueueNumDownlink;
    }

    public void setOnuQueueNumDownlink(Long onuQueueNumDownlink) {
        this.onuQueueNumDownlink = onuQueueNumDownlink;
    }

    public Long getOnuQueueNumUplink() {
        return onuQueueNumUplink;
    }

    public void setOnuQueueNumUplink(Long onuQueueNumUplink) {
        this.onuQueueNumUplink = onuQueueNumUplink;
    }

    public Long getPonId() {
        return ponId;
    }

    public void setPonId(Long ponId) {
        this.ponId = ponId;
    }

    /**
     * @return the onuMibIndex
     */
    public Long getOnuMibIndex() {
        return onuMibIndex;
    }

    /**
     * @param onuMibIndex
     *            the onuMibIndex to set
     */
    public void setOnuMibIndex(Long onuMibIndex) {
        this.onuMibIndex = onuMibIndex;
        onuIndex = EponIndex.getOnuIndexByMibIndex(onuMibIndex);
    }

    /**
     * @return the onuGePortList
     */
    public List<Integer> getOnuGePortList() {
        return onuGePortList;
    }

    /**
     * @param onuGePortList
     *            the onuGePortList to set
     */
    public void setOnuGePortList(List<Integer> onuGePortList) {
        this.onuGePortList = onuGePortList;
    }

    /**
     * @return the onuFePortList
     */
    public List<Integer> getOnuFePortList() {
        return onuFePortList;
    }

    /**
     * @param onuFePortList
     *            the onuFePortList to set
     */
    public void setOnuFePortList(List<Integer> onuFePortList) {
        this.onuFePortList = onuFePortList;
    }

    public String getOnuGePortListStr() {
        return onuGePortListStr;
    }

    public void setOnuGePortListStr(String onuGePortListStr) {
        this.onuGePortListStr = onuGePortListStr;
    }

    public String getOnuFePortListStr() {
        return onuFePortListStr;
    }

    public void setOnuFePortListStr(String onuFePortListStr) {
        this.onuFePortListStr = onuFePortListStr;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("OltOnuCapability");
        sb.append("{entityId=").append(entityId);
        sb.append(", onuId=").append(onuId);
        sb.append(", ponId=").append(ponId);
        sb.append(", onuMibIndex=").append(onuMibIndex);
        sb.append(", onuIndex=").append(onuIndex);
        sb.append(", onuGePortNum=").append(onuGePortNum);
        sb.append(", onuGePortBitmap='").append(onuGePortBitmap).append('\'');
        sb.append(", onuFePortNum=").append(onuFePortNum);
        sb.append(", onuFePortBitmap='").append(onuFePortBitmap).append('\'');
        sb.append(", onuQueueNumUplink=").append(onuQueueNumUplink);
        sb.append(", onuMaxQueueNumUplink=").append(onuMaxQueueNumUplink);
        sb.append(", onuQueueNumDownlink=").append(onuQueueNumDownlink);
        sb.append(", onuMaxQueueNumDownlink=").append(onuMaxQueueNumDownlink);
        sb.append(", onuFecEnable=").append(onuFecEnable);
        sb.append(", onuEncryptMode=").append(onuEncryptMode);
        sb.append(", onuEncryptKeyExchangeTime=").append(onuEncryptKeyExchangeTime);
        sb.append('}');
        return sb.toString();
    }
}
