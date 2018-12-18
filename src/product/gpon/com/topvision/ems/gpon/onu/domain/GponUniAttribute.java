/***********************************************************************
 * $Id: GponUniAttribute.java,v1.0 2016年10月27日 上午9:44:47 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.onu.domain;

import java.io.Serializable;

import com.topvision.ems.epon.onu.domain.OltUniAttribute;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponIndex;

/**
 * @author Rod John
 * @created @2016年10月27日-上午9:44:47
 *
 */
public class GponUniAttribute extends OltUniAttribute implements Serializable, AliasesSuperType {

    private static final long serialVersionUID = -95334546148209335L;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.5.1.1.1", index = true)
    private Long ethAttributeDeviceIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.5.1.1.2", index = true)
    private Long ethAttributeCardIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.5.1.1.3", index = true)
    private Long ethAttributePortIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.5.1.1.4", writable = true, type = "Integer32")
    private Integer ethAdminStatus;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.5.1.1.5")
    private Integer ethOperationStatus;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.5.1.1.6", writable = true, type = "Integer32")
    private Integer ethDuplexRate;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.5.1.1.7", writable = true, type = "Integer32")
    private Integer ethPerfStats15minuteEnable;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.5.1.1.8", writable = true, type = "Integer32")
    private Integer ethPerfStats24hourEnable;

    private Long uniIndex;
    private Integer gponOnuUniPvid;
    private Integer gponOnuUniPri;

    /**
     * @return the uniIndex
     */
    public Long getUniIndex() {
        if (uniIndex == null) {
            uniIndex = EponIndex.getUniIndexByMibIndex(ethAttributeDeviceIndex, ethAttributeCardIndex,
                    ethAttributePortIndex);
        }
        return uniIndex;
    }

    /**
     * @param uniIndex
     *            the uniIndex to set
     */
    public void setUniIndex(Long uniIndex) {
        this.uniIndex = uniIndex;
        ethAttributeDeviceIndex = EponIndex.getOnuMibIndexByIndex(uniIndex);
        ethAttributeCardIndex = EponIndex.getOnuCardNo(uniIndex);
        ethAttributePortIndex = EponIndex.getUniNo(uniIndex);
    }

    /**
     * @return the ethAttributeDeviceIndex
     */
    public Long getEthAttributeDeviceIndex() {
        return ethAttributeDeviceIndex;
    }

    /**
     * @param ethAttributeDeviceIndex the ethAttributeDeviceIndex to set
     */
    public void setEthAttributeDeviceIndex(Long ethAttributeDeviceIndex) {
        this.ethAttributeDeviceIndex = ethAttributeDeviceIndex;
    }

    /**
     * @return the ethAttributeCardIndex
     */
    public Long getEthAttributeCardIndex() {
        return ethAttributeCardIndex;
    }

    /**
     * @param ethAttributeCardIndex the ethAttributeCardIndex to set
     */
    public void setEthAttributeCardIndex(Long ethAttributeCardIndex) {
        this.ethAttributeCardIndex = ethAttributeCardIndex;
    }

    /**
     * @return the ethAttributePortIndex
     */
    public Long getEthAttributePortIndex() {
        return ethAttributePortIndex;
    }

    /**
     * @param ethAttributePortIndex the ethAttributePortIndex to set
     */
    public void setEthAttributePortIndex(Long ethAttributePortIndex) {
        this.ethAttributePortIndex = ethAttributePortIndex;
    }

    /**
     * @return the ethAdminStatus
     */
    public Integer getEthAdminStatus() {
        return ethAdminStatus;
    }

    /**
     * @param ethAdminStatus the ethAdminStatus to set
     */
    public void setEthAdminStatus(Integer ethAdminStatus) {
        this.ethAdminStatus = ethAdminStatus;
    }

    /**
     * @return the ethOperationStatus
     */
    public Integer getEthOperationStatus() {
        return ethOperationStatus;
    }

    /**
     * @param ethOperationStatus the ethOperationStatus to set
     */
    public void setEthOperationStatus(Integer ethOperationStatus) {
        this.ethOperationStatus = ethOperationStatus;
    }

    /**
     * @return the ethDuplexRate
     */
    public Integer getEthDuplexRate() {
        return ethDuplexRate;
    }

    /**
     * @param ethDuplexRate the ethDuplexRate to set
     */
    public void setEthDuplexRate(Integer ethDuplexRate) {
        this.ethDuplexRate = ethDuplexRate;
    }

    /**
     * @return the ethPerfStats15minuteEnable
     */
    public Integer getEthPerfStats15minuteEnable() {
        return ethPerfStats15minuteEnable;
    }

    /**
     * @param ethPerfStats15minuteEnable the ethPerfStats15minuteEnable to set
     */
    public void setEthPerfStats15minuteEnable(Integer ethPerfStats15minuteEnable) {
        this.ethPerfStats15minuteEnable = ethPerfStats15minuteEnable;
    }

    /**
     * @return the ethPerfStats24hourEnable
     */
    public Integer getEthPerfStats24hourEnable() {
        return ethPerfStats24hourEnable;
    }

    /**
     * @param ethPerfStats24hourEnable the ethPerfStats24hourEnable to set
     */
    public void setEthPerfStats24hourEnable(Integer ethPerfStats24hourEnable) {
        this.ethPerfStats24hourEnable = ethPerfStats24hourEnable;
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

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "GponUniAttribute [ethAttributeDeviceIndex=" + ethAttributeDeviceIndex + ", ethAttributeCardIndex="
                + ethAttributeCardIndex + ", ethAttributePortIndex=" + ethAttributePortIndex + ", ethAdminStatus="
                + ethAdminStatus + ", ethOperationStatus=" + ethOperationStatus + ", ethDuplexRate=" + ethDuplexRate
                + ", ethPerfStats15minuteEnable=" + ethPerfStats15minuteEnable + ", ethPerfStats24hourEnable="
                + ethPerfStats24hourEnable + "]";
    }

}
