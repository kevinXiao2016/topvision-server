/***********************************************************************
 * $Id: CmcSnrReportDetail.java,v1.0 2013-6-8 上午11:13:49 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.report.domain;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.topvision.framework.utils.CmcIndexUtils;

/**
 * @author haojie
 * @created @2013-6-8-上午11:13:49
 * 
 */
public class CmcSnrReportDetail {
    private Long entityId;
    private Long cmcId;
    private String entityName;
    private String cmcName;
    private Long ifindex;
    private String channelName;
    private Integer noise;
    private String noiseString;
    private Date dt;
    private String dtString;
    private Long lowTimes;
    private static final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * @return the entityName
     */
    public String getEntityName() {
        return entityName;
    }

    /**
     * @param entityName
     *            the entityName to set
     */
    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public String getCmcName() {
        return cmcName;
    }

    public void setCmcName(String cmcName) {
        this.cmcName = cmcName;
    }

    public Long getIfindex() {
        return ifindex;
    }

    public void setIfindex(Long ifindex) {
        this.ifindex = ifindex;
    }

    public Integer getNoise() {
        return noise;
    }

    public void setNoise(Integer noise) {
        this.noise = noise;
    }

    public Date getDt() {
        return dt;
    }

    public void setDt(Date dt) {
        this.dt = dt;
    }

    public String getNoiseString() {
        String str = "";
        Float l = 0f;
        if (this.getNoise() > -1) {
            l = (float) this.getNoise() / 10;
        } else {
            l = 0f;
        }
        noiseString = l.toString();
        return noiseString + str;
    }

    public void setNoiseString(String noiseString) {
        this.noiseString = noiseString;
    }

    public String getChannelName() {
        channelName = "";
        if (this.ifindex != null) {
            channelName = makePortName(this.ifindex);
        }
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    private String makePortName(Long channelIndex) {
        String type = CmcIndexUtils.getChannelType(channelIndex) == 0 ? "US" : "DS";
        Long slotNo = CmcIndexUtils.getSlotNo(channelIndex);
        Long ponNo = CmcIndexUtils.getPonNo(channelIndex);
        Long cmcNo = CmcIndexUtils.getCmcId(channelIndex);
        Long chNo = CmcIndexUtils.getChannelId(channelIndex);
        StringBuilder sb = new StringBuilder();
        sb.append(type).append(" ").append(slotNo).append("/").append(ponNo).append("/").append(cmcNo).append("/")
                .append(chNo);
        return sb.toString();
    }

    public String getDtString() {
        dtString = "";
        if (this.dt != null) {
            dtString = formatter.format(dt);
        }
        return dtString;
    }

    public void setDtString(String dtString) {
        this.dtString = dtString;
    }

    public Long getLowTimes() {
        return lowTimes;
    }

    public void setLowTimes(Long lowTimes) {
        this.lowTimes = lowTimes;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcSnrReportDetail [entityId=");
        builder.append(entityId);
        builder.append(", cmcId=");
        builder.append(cmcId);
        builder.append(", entityName=");
        builder.append(entityName);
        builder.append(", cmcName=");
        builder.append(cmcName);
        builder.append(", ifindex=");
        builder.append(ifindex);
        builder.append(", channelName=");
        builder.append(channelName);
        builder.append(", noise=");
        builder.append(noise);
        builder.append(", noiseString=");
        builder.append(noiseString);
        builder.append(", dt=");
        builder.append(dt);
        builder.append(", dtString=");
        builder.append(dtString);
        builder.append(", lowTimes=");
        builder.append(lowTimes);
        builder.append("]");
        return builder.toString();
    }

}
