/***********************************************************************
 * $Id: CcmtsSpectrumGpTemplate.java,v1.0 2013-8-2 下午1:27:30 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.frequencyhopping.domain;

import java.io.Serializable;

/**
 * @author haojie
 * @created @2013-8-2-下午1:27:30
 * 
 */
public class CcmtsSpectrumGpTemplate implements Serializable {
    private static final long serialVersionUID = 839990617949411413L;

    private Long tempLateId;
    private String templateName;
    private Integer globalAdminStatus;
    private Integer snrQueryPeriod;
    private Integer hopHisMaxCount;
    private Integer chnlSecondaryProf;
    private Integer chnlTertiaryProf;
    private Integer gpForUpChannel1;
    private Integer gpForUpChannel2;
    private Integer gpForUpChannel3;
    private Integer gpForUpChannel4;

    public Long getTempLateId() {
        return tempLateId;
    }

    public void setTempLateId(Long tempLateId) {
        this.tempLateId = tempLateId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public Integer getGlobalAdminStatus() {
        return globalAdminStatus;
    }

    public void setGlobalAdminStatus(Integer globalAdminStatus) {
        this.globalAdminStatus = globalAdminStatus;
    }

    public Integer getSnrQueryPeriod() {
        return snrQueryPeriod;
    }

    public void setSnrQueryPeriod(Integer snrQueryPeriod) {
        this.snrQueryPeriod = snrQueryPeriod;
    }

    public Integer getHopHisMaxCount() {
        return hopHisMaxCount;
    }

    public void setHopHisMaxCount(Integer hopHisMaxCount) {
        this.hopHisMaxCount = hopHisMaxCount;
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

    public Integer getGpForUpChannel1() {
        return gpForUpChannel1;
    }

    public void setGpForUpChannel1(Integer gpForUpChannel1) {
        this.gpForUpChannel1 = gpForUpChannel1;
    }

    public Integer getGpForUpChannel2() {
        return gpForUpChannel2;
    }

    public void setGpForUpChannel2(Integer gpForUpChannel2) {
        this.gpForUpChannel2 = gpForUpChannel2;
    }

    public Integer getGpForUpChannel3() {
        return gpForUpChannel3;
    }

    public void setGpForUpChannel3(Integer gpForUpChannel3) {
        this.gpForUpChannel3 = gpForUpChannel3;
    }

    public Integer getGpForUpChannel4() {
        return gpForUpChannel4;
    }

    public void setGpForUpChannel4(Integer gpForUpChannel4) {
        this.gpForUpChannel4 = gpForUpChannel4;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CcmtsSpectrumGpTemplate [tempLateId=");
        builder.append(tempLateId);
        builder.append(", templateName=");
        builder.append(templateName);
        builder.append(", globalAdminStatus=");
        builder.append(globalAdminStatus);
        builder.append(", snrQueryPeriod=");
        builder.append(snrQueryPeriod);
        builder.append(", hopHisMaxCount=");
        builder.append(hopHisMaxCount);
        builder.append(", chnlSecondaryProf=");
        builder.append(chnlSecondaryProf);
        builder.append(", chnlTertiaryProf=");
        builder.append(chnlTertiaryProf);
        builder.append(", gpForUpChannel1=");
        builder.append(gpForUpChannel1);
        builder.append(", gpForUpChannel2=");
        builder.append(gpForUpChannel2);
        builder.append(", gpForUpChannel3=");
        builder.append(gpForUpChannel3);
        builder.append(", gpForUpChannel4=");
        builder.append(gpForUpChannel4);
        builder.append("]");
        return builder.toString();
    }

}
