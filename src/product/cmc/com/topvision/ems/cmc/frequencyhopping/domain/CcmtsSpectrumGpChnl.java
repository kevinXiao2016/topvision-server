/***********************************************************************
 * $Id: CcmtsSpectrumGpChnl.java,v1.0 2013-8-2 上午9:45:26 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.frequencyhopping.domain;

import java.io.Serializable;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.domain.PhysAddress;

/**
 * @author haojie
 * @created @2013-8-2-上午9:45:26
 * 
 */
public class CcmtsSpectrumGpChnl implements Serializable {
    private static final long serialVersionUID = -4439206271727034260L;
    // 1 表示清空跳频历史记录
    public static final Integer CHNL_CLEAR_HISTORY = 1;
    // 1 表示恢复信道跳频状态
    public static final Integer CHNL_RESET_TOSTATIC = 1;

    private Long entityId;
    private Long channelIndex;
    private String chnlCmcMac;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.17.2.5.1.1", index = true)
    private PhysAddress cmcMacForDevice;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.17.2.5.1.2", index = true)
    private Integer chnlId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.17.2.5.1.3", writable = true, type = "Integer32")
    private Integer chnlGroupId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.17.2.5.1.4", writable = true, type = "Integer32")
    private Integer chnlSecondaryProf;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.17.2.5.1.5", writable = true, type = "Integer32")
    private Integer chnlTertiaryProf;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.17.2.5.1.23", writable = true, type = "Integer32")
    private Integer chnlResetTostatic;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.17.2.5.1.24", writable = true, type = "Integer32")
    private Integer chnlClearHistory;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getChannelIndex() {
        return channelIndex;
    }

    public void setChannelIndex(Long channelIndex) {
        this.channelIndex = channelIndex;
    }

    public String getChnlCmcMac() {
        return chnlCmcMac;
    }

    public void setChnlCmcMac(String chnlCmcMac) {
        this.chnlCmcMac = chnlCmcMac;
    }

    public Integer getChnlId() {
        return chnlId;
    }

    public void setChnlId(Integer chnlId) {
        this.chnlId = chnlId;
    }

    public Integer getChnlGroupId() {
        return chnlGroupId;
    }

    public void setChnlGroupId(Integer chnlGroupId) {
        this.chnlGroupId = chnlGroupId;
    }

    public Integer getChnlSecondaryProf() {
        return chnlSecondaryProf;
    }

    public void setChnlSecondaryProf(Integer chnlSecondaryProf) {
        this.chnlSecondaryProf = chnlSecondaryProf;
    }

    public Integer getChnlTertiaryProf() {
        return chnlTertiaryProf;
    }

    public void setChnlTertiaryProf(Integer chnlTertiaryProf) {
        this.chnlTertiaryProf = chnlTertiaryProf;
    }

    public Integer getChnlResetTostatic() {
        return chnlResetTostatic;
    }

    public void setChnlResetTostatic(Integer chnlResetTostatic) {
        this.chnlResetTostatic = chnlResetTostatic;
    }

    public Integer getChnlClearHistory() {
        return chnlClearHistory;
    }

    public void setChnlClearHistory(Integer chnlClearHistory) {
        this.chnlClearHistory = chnlClearHistory;
    }

    public PhysAddress getCmcMacForDevice() {
        return cmcMacForDevice;
    }

    public void setCmcMacForDevice(PhysAddress cmcMacForDevice) {
        this.cmcMacForDevice = cmcMacForDevice;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CcmtsSpectrumGpChnl [entityId=");
        builder.append(entityId);
        builder.append(", channelIndex=");
        builder.append(channelIndex);
        builder.append(", chnlCmcMac=");
        builder.append(chnlCmcMac);
        builder.append(", cmcMacForDevice=");
        builder.append(cmcMacForDevice);
        builder.append(", chnlId=");
        builder.append(chnlId);
        builder.append(", chnlGroupId=");
        builder.append(chnlGroupId);
        builder.append(", chnlSecondaryProf=");
        builder.append(chnlSecondaryProf);
        builder.append(", chnlTertiaryProf=");
        builder.append(chnlTertiaryProf);
        builder.append(", chnlResetTostatic=");
        builder.append(chnlResetTostatic);
        builder.append(", chnlClearHistory=");
        builder.append(chnlClearHistory);
        builder.append("]");
        return builder.toString();
    }

}
