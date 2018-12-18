package com.topvision.ems.mobile.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

public class PageView implements Serializable, AliasesSuperType {

    private static final long serialVersionUID = 3129441559106239269L;

    private String uuid;
    private String url;
    private Long startTime;
    private Long closedTime;
    private String opener;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getStartTime() {
        return startTime;
    }

    public Timestamp getStartTimeStamp() {
        return new Timestamp(startTime);
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getClosedTime() {
        return closedTime;
    }
    
    public Timestamp getClosedTimeStamp() {
        return new Timestamp(closedTime);
    }

    public void setClosedTime(Long closedTime) {
        this.closedTime = closedTime;
    }

    public String getOpener() {
        return opener;
    }

    public void setOpener(String opener) {
        this.opener = opener;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PageView [uuid=");
        builder.append(uuid);
        builder.append(", url=");
        builder.append(url);
        builder.append(", startTime=");
        builder.append(startTime);
        builder.append(", closedTime=");
        builder.append(closedTime);
        builder.append(", opener=");
        builder.append(opener);
        builder.append("]");
        return builder.toString();
    }

}
