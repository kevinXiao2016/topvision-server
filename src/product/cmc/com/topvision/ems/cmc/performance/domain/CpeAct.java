/***********************************************************************
 * $ CpeAct.java,v1.0 2013-6-27 17:43:10 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.common.IpUtils;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author jay
 * @created @2013-6-27-17:43:10
 */
@Alias("cpeAct")
public class CpeAct implements Serializable,  AliasesSuperType {
    private static final long serialVersionUID = 7342180206202447869L;
    public static Integer OFFLINE = 1;
    public static Integer ONLINE = 2;
    private Long entityId;
    private Long cmIndex;
    private Long cmmac;
    private Long cpemac;
    private Long cpeip;
    private Integer action;
    private Timestamp time;
    private Timestamp realtime;
    private String cmmacString;
    private String cpemacString;
    private String cpeipString;
    private String realtimeString;
    private Long realtimeLong;

    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

    public Long getCmIndex() {
        return cmIndex;
    }

    public void setCmIndex(Long cmIndex) {
        this.cmIndex = cmIndex;
    }

    public Long getCmmac() {
        return cmmac;
    }

    public void setCmmac(Long cmmac) {
        this.cmmac = cmmac;
        this.cmmacString = new MacUtils(cmmac).toString(MacUtils.MAOHAO).toUpperCase();
    }

    public Long getCpeip() {
        return cpeip;
    }

    public void setCpeip(Long cpeip) {
        this.cpeip = cpeip;
        this.cpeipString = new IpUtils(cpeip).toString();
    }

    public Long getCpemac() {
        return cpemac;
    }

    public void setCpemac(Long cpemac) {
        this.cpemac = cpemac;
        this.cpemacString = new MacUtils(cpemac).toString(MacUtils.MAOHAO).toUpperCase();
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Timestamp getRealtime() {
        return realtime;
    }

    public void setRealtime(Timestamp realtime) {
        this.realtime = realtime;
        this.realtimeLong = realtime.getTime();
        this.realtimeString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(realtimeLong));
    }

    public Long getRealtimeLong() {
        return realtimeLong;
    }

    public void setRealtimeLong(Long realtimeLong) {
        this.realtimeLong = realtimeLong;
        this.realtime = new Timestamp(realtimeLong);
        this.realtimeString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(realtimeLong));
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public Long getTimeLong() {
        return time.getTime();
    }

    public void setTimeLong(Long timeLong) {
        this.time = new Timestamp(timeLong);
    }

    public String getCmmacString() {
        return cmmacString;
    }

    public void setCmmacString(String cmmacString) {
        this.cmmacString = cmmacString;
        this.cmmac = new MacUtils(cmmacString).longValue();
    }

    public String getCpemacString() {
        return cpemacString;
    }

    public void setCpemacString(String cpemacString) {
        this.cpemacString = cpemacString;
        this.cpemac = new MacUtils(cpemacString).longValue();
    }

    public String getCpeipString() {
        return cpeipString;
    }

    public void setCpeipString(String cpeipString) {
        this.cpeipString = cpeipString;
    }

    public String getRealtimeString() {
        return realtimeString;
    }

    public void setRealtimeString(String realtimeString) throws ParseException {
        this.realtimeString = realtimeString;
        this.realtimeLong = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(realtimeString).getTime();
        this.realtime = new Timestamp(realtimeLong);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CpeAct [entityId=");
        builder.append(entityId);
        builder.append(", cmIndex=");
        builder.append(cmIndex);
        builder.append(", cmmac=");
        builder.append(cmmac);
        builder.append(", cpemac=");
        builder.append(cpemac);
        builder.append(", cpeip=");
        builder.append(cpeip);
        builder.append(", action=");
        builder.append(action);
        builder.append(", time=");
        builder.append(time);
        builder.append(", realtime=");
        builder.append(realtime);
        builder.append(", cmmacString=");
        builder.append(cmmacString);
        builder.append(", cpemacString=");
        builder.append(cpemacString);
        builder.append(", cpeipString=");
        builder.append(cpeipString);
        builder.append(", realtimeString=");
        builder.append(realtimeString);
        builder.append(", realtimeLong=");
        builder.append(realtimeLong);
        builder.append("]");
        return builder.toString();
    }
}
