/***********************************************************************
 * $Id: TopOnuLaserEntry.java,v1.0 2017年6月17日 上午9:36:36 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponIndex;

/**
 * @author lizongtian
 * @created @2017年6月17日-上午9:36:36
 *
 */
public class TopOnuLaserEntry implements AliasesSuperType {

    private static final long serialVersionUID = 4651794082559306185L;

    private Long onuId;
    private Long onuIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.8.1.2.1.1", index = true)
    private Integer laserCardIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.8.1.2.1.2", index = true)
    private Integer laserPortIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.8.1.2.1.3", index = true)
    private Integer laserOnuIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.8.1.2.1.4", writable = true, type = "Integer32")
    private Integer laserSwitch;

    public Long getOnuId() {
        return onuId;
    }

    public void setOnuId(Long onuId) {
        this.onuId = onuId;
    }

    public Long getOnuIndex() {
        if (onuIndex == null) {
            onuIndex = new EponIndex(laserCardIndex, laserPortIndex, laserOnuIndex).getOnuIndex();
        }
        return onuIndex;
    }

    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
        laserCardIndex = EponIndex.getSlotNo(onuIndex).intValue();
        laserPortIndex = EponIndex.getPonNo(onuIndex).intValue();
        laserOnuIndex = EponIndex.getOnuNo(onuIndex).intValue();
    }

    public Integer getLaserCardIndex() {
        return laserCardIndex;
    }

    public void setLaserCardIndex(Integer laserCardIndex) {
        this.laserCardIndex = laserCardIndex;
    }

    public Integer getLaserPortIndex() {
        return laserPortIndex;
    }

    public void setLaserPortIndex(Integer laserPortIndex) {
        this.laserPortIndex = laserPortIndex;
    }

    public Integer getLaserOnuIndex() {
        return laserOnuIndex;
    }

    public void setLaserOnuIndex(Integer laserOnuIndex) {
        this.laserOnuIndex = laserOnuIndex;
    }

    public Integer getLaserSwitch() {
        return laserSwitch;
    }

    public void setLaserSwitch(Integer laserSwitch) {
        this.laserSwitch = laserSwitch;
    }

}
