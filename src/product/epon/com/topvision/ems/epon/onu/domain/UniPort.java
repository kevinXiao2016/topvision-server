/***********************************************************************
 * $Id: UniPort.java;v1.0 2015年4月22日 上午9:36:34 $
 * 
 * @author: Administrator
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.domain;

import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponIndex;

/**
 * @author Administrator
 * @created @2015年4月22日-上午9:36:34
 *
 */
public class UniPort implements AliasesSuperType {
    private static final long serialVersionUID = 4486762145085059175L;
    private Long uniId;
    private Integer uniOperationStatus;
    private Integer uniAutoNegotiationEnable;
    private Integer flowCtrl;
    private Integer uniAdminStatus;
    private Integer perfStats15minuteEnable;
    private Integer bindPvid;
    private Integer profileMode;
    private Long uniIndex;
    private Long uniNo;
    private Long entityId;
    private Integer vlanMode;

    public Long getUniId() {
        return uniId;
    }

    public void setUniId(Long uniId) {
        this.uniId = uniId;
    }

    public Integer getUniOperationStatus() {
        return uniOperationStatus;
    }

    public void setUniOperationStatus(Integer uniOperationStatus) {
        this.uniOperationStatus = uniOperationStatus;
    }

    public Integer getUniAutoNegotiationEnable() {
        return uniAutoNegotiationEnable;
    }

    public void setUniAutoNegotiationEnable(Integer uniAutoNegotiationEnable) {
        this.uniAutoNegotiationEnable = uniAutoNegotiationEnable;
    }

    public Integer getFlowCtrl() {
        return flowCtrl;
    }

    public void setFlowCtrl(Integer flowCtrl) {
        this.flowCtrl = flowCtrl;
    }

    public Integer getUniAdminStatus() {
        return uniAdminStatus;
    }

    public void setUniAdminStatus(Integer uniAdminStatus) {
        this.uniAdminStatus = uniAdminStatus;
    }

    public Integer getPerfStats15minuteEnable() {
        return perfStats15minuteEnable;
    }

    public void setPerfStats15minuteEnable(Integer perfStats15minuteEnable) {
        this.perfStats15minuteEnable = perfStats15minuteEnable;
    }

    public Integer getBindPvid() {
        return bindPvid;
    }

    public void setBindPvid(Integer bindPvid) {
        this.bindPvid = bindPvid;
    }

    public Integer getProfileMode() {
        return profileMode;
    }

    public void setProfileMode(Integer profileMode) {
        this.profileMode = profileMode;
    }

    public Long getUniNo() {
        return uniNo;
    }

    public void setUniNo(Long uniNo) {
        this.uniNo = uniNo;
    }

    public Long getUniIndex() {
        return uniIndex;
    }

    public void setUniIndex(Long uniIndex) {
        this.uniNo = EponIndex.getUniNo(uniIndex);
        this.uniIndex = uniIndex;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getVlanMode() {
        return vlanMode;
    }

    public void setVlanMode(Integer vlanMode) {
        this.vlanMode = vlanMode;
    }

    @Override
    public String toString() {
        return "UniPort [uniId=" + uniId + ", uniOperationStatus=" + uniOperationStatus + ", uniAutoNegotiationEnable="
                + uniAutoNegotiationEnable + ", flowCtrl=" + flowCtrl + ", uniAdminStatus=" + uniAdminStatus
                + ", perfStats15minuteEnable=" + perfStats15minuteEnable + ", bindPvid=" + bindPvid + ", profileMode="
                + profileMode + ", uniIndex=" + uniIndex + ", uniNo=" + uniNo + ", entityId=" + entityId + "]";
    }

}
