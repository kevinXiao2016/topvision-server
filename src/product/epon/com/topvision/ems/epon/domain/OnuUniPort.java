/***********************************************************************
 * $Id: OnuPonPort.java,v1.0 2011-9-27 下午04:25:44 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.domain;

import java.io.Serializable;

import com.topvision.framework.utils.EponIndex;
import com.topvision.ems.epon.utils.EponUtil;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.platform.ResourceManager;

/**
 * @author loyal
 * @created @2011-9-27-下午04:25:44
 * 
 */
@SuppressWarnings("rawtypes")
public class OnuUniPort implements Serializable, Comparable, AliasesSuperType {
    private static final long serialVersionUID = 2548314528058075774L;
    private final ResourceManager resourceManager = ResourceManager
            .getResourceManager("com.topvision.ems.epon.resources");
    private Long uniId;
    private Long uniIndex;
    private Integer uniOperationStatus;
    private Integer uniAdminStatus;
    private Integer uniAutoNegotiationEnable;
    private Integer uniAutoNegLocalTechAbility;
    private Integer flowCtrl;
    private Integer uniDSLoopBackEnable;
    private Integer uniUSUtgPri;
    private Long uniRealIndex;
    private String uniAutoNegoString;
    private Integer perfStats15minuteEnable;
    private Integer perfStats24hourEnable;
    private Integer isolationEnable;
    private String autoNegAdvertisedTechAbility;
    private Integer topUniLoopDetectEnable;

    /**
     * @return the uniId
     */
    public Long getUniId() {
        return uniId;
    }

    /**
     * @param uniId
     *            the uniId to set
     */
    public void setUniId(Long uniId) {
        this.uniId = uniId;
    }

    /**
     * @return the uniOperationStatus
     */
    public Integer getUniOperationStatus() {
        return uniOperationStatus;
    }

    /**
     * @param uniOperationStatus
     *            the uniOperationStatus to set
     */
    public void setUniOperationStatus(Integer uniOperationStatus) {
        this.uniOperationStatus = uniOperationStatus;
    }

    /**
     * @return the uniAdminStatus
     */
    public Integer getUniAdminStatus() {
        return uniAdminStatus;
    }

    /**
     * @param uniAdminStatus
     *            the uniAdminStatus to set
     */
    public void setUniAdminStatus(Integer uniAdminStatus) {
        this.uniAdminStatus = uniAdminStatus;
    }

    /**
     * @return the uniAutoNegotiationLocalTechAbility
     */
    public Integer getUniAutoNegLocalTechAbility() {
        return uniAutoNegLocalTechAbility;
    }

    /**
     * @param uniAutoNegLocalTechAbility
     *            the uniAutoNegotiationLocalTechAbility to set
     */
    public void setUniAutoNegLocalTechAbility(Integer uniAutoNegLocalTechAbility) {
        if (uniAutoNegLocalTechAbility != null) {
            this.setUniAutoNegoString(resourceManager.getNotNullString("olt.message.onuUni.AutoNego."
                    + uniAutoNegLocalTechAbility));
            this.uniAutoNegLocalTechAbility = uniAutoNegLocalTechAbility;
        } else {
            this.uniAutoNegLocalTechAbility = 0;
            this.setUniAutoNegoString("none");
        }
    }

    public Integer getFlowCtrl() {
        return flowCtrl;
    }

    public void setFlowCtrl(Integer flowCtrl) {
        this.flowCtrl = flowCtrl;
    }

    public Integer getUniAutoNegotiationEnable() {
        return uniAutoNegotiationEnable;
    }

    public void setUniAutoNegotiationEnable(Integer uniAutoNegotiationEnable) {
        this.uniAutoNegotiationEnable = uniAutoNegotiationEnable;
    }

    public Long getUniRealIndex() {
        return uniRealIndex;
    }

    public void setUniRealIndex(Long uniRealIndex) {
        this.uniRealIndex = uniRealIndex;
    }

    public Long getUniIndex() {
        return uniIndex;
    }

    public void setUniIndex(Long uniIndex) {
        setUniRealIndex(EponIndex.getUniNo(uniIndex));
        this.uniIndex = uniIndex;
    }

    public String getUniAutoNegoString() {
        return uniAutoNegoString;
    }

    public void setUniAutoNegoString(String uniAutoNegoString) {
        this.uniAutoNegoString = uniAutoNegoString;
    }

    public Integer getPerfStats15minuteEnable() {
        return perfStats15minuteEnable;
    }

    public void setPerfStats15minuteEnable(Integer perfStats15minuteEnable) {
        this.perfStats15minuteEnable = perfStats15minuteEnable;
    }

    public Integer getPerfStats24hourEnable() {
        return perfStats24hourEnable;
    }

    public void setPerfStats24hourEnable(Integer perfStats24hourEnable) {
        this.perfStats24hourEnable = perfStats24hourEnable;
    }

    public Integer getIsolationEnable() {
        return isolationEnable;
    }

    public void setIsolationEnable(Integer isolationEnable) {
        this.isolationEnable = isolationEnable;
    }

    public String getAutoNegAdvertisedTechAbility() {
        return autoNegAdvertisedTechAbility;
    }

    public Integer getUniDSLoopBackEnable() {
        return uniDSLoopBackEnable;
    }

    public void setUniDSLoopBackEnable(Integer uniDSLoopBackEnable) {
        this.uniDSLoopBackEnable = uniDSLoopBackEnable;
    }

    public Integer getUniUSUtgPri() {
        return uniUSUtgPri;
    }

    public void setUniUSUtgPri(Integer uniUSUtgPri) {
        this.uniUSUtgPri = uniUSUtgPri;
    }

    public void setAutoNegAdvertisedTechAbility(String autoNegAdvertisedTechAbility) {
        if (autoNegAdvertisedTechAbility != null) {
            this.autoNegAdvertisedTechAbility = resourceManager.getNotNullString("olt.message.onuUni.AutoNego."
                    + EponUtil.getSymbolInfoFromMibByteStartFromZero(autoNegAdvertisedTechAbility).toString());
            if (autoNegAdvertisedTechAbility != null && !autoNegAdvertisedTechAbility.equalsIgnoreCase("")) {
                uniAutoNegoString = autoNegAdvertisedTechAbility;
            } else {
                uniAutoNegoString = "-1";
            }
        } else {
            uniAutoNegoString = "-1";
        }
    }

    public Integer getTopUniLoopDetectEnable() {
        return topUniLoopDetectEnable;
    }

    public void setTopUniLoopDetectEnable(Integer topUniLoopDetectEnable) {
        this.topUniLoopDetectEnable = topUniLoopDetectEnable;
    }

    @Override
    public String toString() {
        return "OnuUniPort [resourceManager=" + resourceManager + ", uniId=" + uniId + ", uniIndex=" + uniIndex
                + ", uniOperationStatus=" + uniOperationStatus + ", uniAdminStatus=" + uniAdminStatus
                + ", uniAutoNegotiationEnable=" + uniAutoNegotiationEnable + ", uniAutoNegLocalTechAbility="
                + uniAutoNegLocalTechAbility + ", flowCtrl=" + flowCtrl + ", uniDSLoopBackEnable="
                + uniDSLoopBackEnable + ", uniUSUtgPri=" + uniUSUtgPri + ", uniRealIndex=" + uniRealIndex
                + ", uniAutoNegoString=" + uniAutoNegoString + ", perfStats15minuteEnable=" + perfStats15minuteEnable
                + ", perfStats24hourEnable=" + perfStats24hourEnable + ", isolationEnable=" + isolationEnable
                + ", autoNegAdvertisedTechAbility=" + autoNegAdvertisedTechAbility + ", topUniLoopDetectEnable="
                + topUniLoopDetectEnable + "]";
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Object o) {
        OnuUniPort uniPort = (OnuUniPort) o;
        if (this.getUniIndex() > uniPort.getUniIndex()) {
            return 1;
        } else if (this.getUniIndex().equals(uniPort.getUniIndex())) {
            return 0;
        } else {
            return -1;
        }
    }
}
