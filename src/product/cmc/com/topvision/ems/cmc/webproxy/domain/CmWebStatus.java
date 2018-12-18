/***********************************************************************
 * CmWebStatus.java,v1.0 17-5-9 上午10:51 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.webproxy.domain;

import com.topvision.framework.dao.mybatis.AliasesSuperType;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author jay
 * @created 17-5-9 上午10:51
 */
@Alias("cmWebStatus")
public class CmWebStatus implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 8571259721660333279L;
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Long heartbeatId;
    private Long cmId;
    private String natIp;

    private Long cmcId;
    private String cmcIp;
    private Integer cmcPort;
    private Boolean cmcProxyStatus;
    private Boolean loginStatus;

    private String cmIp;
    private Integer nm3000Port;
    private Boolean cmPortMapStatus;

    private Long heartbeatTime;
    private Boolean heartbeatStatus;

    private String url;

    public Long getCmId() {
        return cmId;
    }

    public void setCmId(Long cmId) {
        this.cmId = cmId;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public String getCmIp() {
        return cmIp;
    }

    public void setCmIp(String cmIp) {
        this.cmIp = cmIp;
    }

    public Integer getNm3000Port() {
        return nm3000Port;
    }

    public void setNm3000Port(Integer nm3000Port) {
        this.nm3000Port = nm3000Port;
    }

    public String getCmcIp() {
        return cmcIp;
    }

    public void setCmcIp(String cmcIp) {
        this.cmcIp = cmcIp;
    }

    public Integer getCmcPort() {
        return cmcPort;
    }

    public void setCmcPort(Integer cmcPort) {
        this.cmcPort = cmcPort;
    }

    public Long getHeartbeatTime() {
        return heartbeatTime;
    }

    public void setHeartbeatTime(Long heartbeatTime) {
        this.heartbeatTime = heartbeatTime;
    }

    public Long getHeartbeatId() {
        return heartbeatId;
    }

    public void setHeartbeatId(Long heartbeatId) {
        this.heartbeatId = heartbeatId;
    }

    public Boolean getCmPortMapStatus() {
        return cmPortMapStatus;
    }

    public void setCmPortMapStatus(Boolean cmPortMapStatus) {
        this.cmPortMapStatus = cmPortMapStatus;
    }

    public Boolean getCmcProxyStatus() {
        return cmcProxyStatus;
    }

    public void setCmcProxyStatus(Boolean cmcProxyStatus) {
        this.cmcProxyStatus = cmcProxyStatus;
    }

    public Boolean getHeartbeatStatus() {
        return heartbeatStatus;
    }

    public void setHeartbeatStatus(Boolean heartbeatStatus) {
        this.heartbeatStatus = heartbeatStatus;
    }

    public String getUrl() {
        return "http://" + natIp + ":" + nm3000Port;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNatIp() {
        return natIp;
    }

    public void setNatIp(String natIp) {
        this.natIp = natIp;
    }

    public Boolean getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(Boolean loginStatus) {
        this.loginStatus = loginStatus;
    }

    @Override
    public String toString() {
        return "CmWebStatus{" +
                "heartbeatId=" + heartbeatId +
                ", cmId=" + cmId +
                ", natIp='" + natIp + '\'' +
                ", cmcId=" + cmcId +
                ", cmcIp='" + cmcIp + '\'' +
                ", cmcPort=" + cmcPort +
                ", cmcProxyStatus=" + cmcProxyStatus +
                ", loginStatus=" + loginStatus +
                ", cmIp='" + cmIp + '\'' +
                ", nm3000Port=" + nm3000Port +
                ", cmPortMapStatus=" + cmPortMapStatus +
                ", heartbeatTime=" + sdf.format(new Date(heartbeatTime)) +
                ", heartbeatStatus=" + heartbeatStatus +
                ", url='" + url + '\'' +
                '}';
    }
}
