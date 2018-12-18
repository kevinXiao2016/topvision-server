/***********************************************************************
 * $Id: OltFanAttribute.java,v1.0 2011-9-26 上午09:20:42 $
 * 
 * @author: zhanglongyang
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.olt.domain;

import java.io.Serializable;

import com.topvision.framework.utils.EponIndex;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.annotation.TableProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * 风扇属性
 * 
 * @author zhanglongyang
 * 
 */
@TableProperty(tables = { "default", "fan" })
public class OltFanAttribute implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -1133851854809839463L;
    public static final Integer STARTNO_INTEGER = 19;

    private Long entityId;
    private Long fanCardId;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.1.5.1.1.1", index = true)
    private Long deviceNo = 1L;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.1.5.1.1.2", index = true)
    private Long fanNo;
    @SnmpProperty(table = "fan", oid = "1.3.6.1.4.1.32285.11.2.3.1.5.1.1.1", index = true)
    private Long topSysFanCardNo;
    @SnmpProperty(table = "fan", oid = "1.3.6.1.4.1.32285.11.2.3.1.5.1.1.2", index = true)
    private Long topSysFanNo = 1L;
    private Long fanCardIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.1.5.1.1.5")
    private String fanCardName;
    @SnmpProperty(table = "fan", oid = "1.3.6.1.4.1.32285.11.2.3.1.5.1.1.3", writable = true, type = "Integer32")
    private Integer topSysFanSpeedControl;

    /**
     * @return the entityId
     */
    public Long getEntityId() {
        return entityId;
    }

    /**
     * @param entityId
     *            the entityId to set
     */
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    /**
     * @return the fanCardId
     */
    public Long getFanCardId() {
        return fanCardId;
    }

    /**
     * @param fanCardId
     *            the fanCardId to set
     */
    public void setFanCardId(Long fanCardId) {
        this.fanCardId = fanCardId;
    }

    /**
     * @return the fanCardIndex
     */
    public Long getFanCardIndex() {
        fanCardIndex = new EponIndex(fanNo.intValue()).getSlotIndex();
        return fanCardIndex;
    }

    /**
     * @param fanCardIndex
     *            the fanCardIndex to set
     */
    public void setFanCardIndex(Long fanCardIndex) {
        this.fanCardIndex = fanCardIndex;
        fanNo = topSysFanCardNo = EponIndex.getSlotNo(fanCardIndex);
    }

    /**
     * @return the fanCardName
     */
    public String getFanCardName() {
        return fanCardName;
    }

    /**
     * @param fanCardName
     *            the fanCardName to set
     */
    public void setFanCardName(String fanCardName) {
        this.fanCardName = fanCardName;
    }

    /**
     * @return the topSysFanSpeedControl
     */
    public Integer getTopSysFanSpeedControl() {
        return topSysFanSpeedControl;
    }

    /**
     * @param topSysFanSpeedControl
     *            the topSysFanSpeedControl to set
     */
    public void setTopSysFanSpeedControl(Integer topSysFanSpeedControl) {
        this.topSysFanSpeedControl = topSysFanSpeedControl;
    }

    public Long getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(Long deviceNo) {
        this.deviceNo = deviceNo;
    }

    public Long getFanNo() {
        return fanNo;
    }

    public void setFanNo(Long fanNo) {
        this.fanNo = fanNo;
    }

    public Long getTopSysFanCardNo() {
        return topSysFanCardNo;
    }

    public void setTopSysFanCardNo(Long topSysFanCardNo) {
        this.topSysFanCardNo = topSysFanCardNo;
    }

    public Long getTopSysFanNo() {
        return topSysFanNo;
    }

    public void setTopSysFanNo(Long topSysFanNo) {
        this.topSysFanNo = topSysFanNo;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("OltFanAttribute");
        sb.append("{deviceNo=").append(deviceNo);
        sb.append(", entityId=").append(entityId);
        sb.append(", fanCardId=").append(fanCardId);
        sb.append(", fanNo=").append(fanNo);
        sb.append(", topSysFanCardNo=").append(topSysFanCardNo);
        sb.append(", topSysFanNo=").append(topSysFanNo);
        sb.append(", fanCardIndex=").append(fanCardIndex);
        sb.append(", fanCardName='").append(fanCardName).append('\'');
        sb.append(", topSysFanSpeedControl=").append(topSysFanSpeedControl);
        sb.append('}');
        return sb.toString();
    }
}
