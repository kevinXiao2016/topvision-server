/***********************************************************************
 * $ CmAct.java,v1.0 2013-6-27 14:05:10 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.utils.CmcIndexUtils;
import com.topvision.framework.common.IpUtils;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author jay
 * @created @2013-6-27-14:05:10
 */
@Alias("cmAct")
public class CmAct implements Serializable, AliasesSuperType {
    public static Integer OFFLINE = 1;
    public static Integer ONLINE = 2;
    private static final long serialVersionUID = 7342180206202447869L;
    private Long entityId;
    private String entityIp;
    private Long cmIndex;
    private String cmcIndexString;
    private Long cmmac;
    private Long cmip;
    private Integer action;
    private Timestamp time;
    private Timestamp realtime;
    private Long realtimeLong;
    private String realtimeString;
    private String cmmacString;
    private String cmipString;

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
        this.cmcIndexString = CmcIndexUtils.getSlotNo(cmIndex) + "/" + CmcIndexUtils.getPonNo(cmIndex) + "/" + CmcIndexUtils.getCmcId(cmIndex);
    }

    public Long getCmip() {
        return cmip;
    }

    public void setCmip(Long cmip) {
        this.cmip = cmip;
        this.cmipString = new IpUtils(cmip).toString();
    }

    public Long getCmmac() {
        return cmmac;
    }

    public Long getCmMac() {
        return cmmac;
    }

    public void setCmmac(Long cmmac) {
        this.cmmac = cmmac;
        this.cmmacString = new MacUtils(cmmac).toString(MacUtils.MAOHAO).toUpperCase();
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
    }

    public String getCmipString() {
        return cmipString;
    }

    public void setCmipString(String cmipString) {
        this.cmipString = cmipString;
    }

    public String getRealtimeString() {
        return realtimeString;
    }

    public void setRealtimeString(String realtimeString) {
        this.realtimeString = realtimeString;
    }

    public String getEntityIp() {
        return entityIp;
    }

    public void setEntityIp(String entityIp) {
        this.entityIp = entityIp;
    }

    public String getCmcIndexString() {
        return cmcIndexString;
    }

    public void setCmcIndexString(String cmcIndexString) {
        this.cmcIndexString = cmcIndexString;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("CmAct");
        sb.append("{action=").append(action);
        sb.append(", entityId=").append(entityId);
        sb.append(", cmIndex=").append(cmIndex);
        sb.append(", cmmac=").append(cmmac);
        sb.append(", cmip=").append(cmip);
        sb.append(", time=").append(time);
        sb.append(", realtime=").append(realtime);
        sb.append('}');
        return sb.toString();
    }
    
    public CmAct copyFrom() {
    	CmAct copy = new CmAct();
        copy.setEntityId(entityId);
        copy.setEntityIp(entityIp);
        copy.setCmIndex(cmIndex);
        copy.setCmcIndexString(cmcIndexString);
        copy.setCmmac(cmmac);
        copy.setCmip(cmip);
        copy.setAction(action);
        copy.setRealtimeLong(realtimeLong);
        copy.setTimeLong(getTimeLong());
        return copy;
    }
}
