/***********************************************************************
 * $Id: Notice.java,v1.0 2013年9月24日 上午11:20:53 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.billboard.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.ParseException;

import org.apache.ibatis.type.Alias;
import org.quartz.JobKey;
import org.quartz.TriggerKey;

import com.topvision.framework.common.DateUtils;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * 公告 record by @bravin 2013-9-25 11:46：@victor说的,不向特定群体特定人推送，这部分功能由各地的OA系统自行维护
 * 
 * @author Bravin
 * @created @2013年9月24日-上午11:20:53
 *
 */
@Alias("notice")
public class Notice implements Serializable, AliasesSuperType, Comparable<Notice> {
    private static final long serialVersionUID = -5460928667001101307L;

    private Integer noticeId;
    private Long userId;
    /* 显示时显示用户名，动态的获取 */
    private String username;
    private Timestamp createTime;
    private Timestamp startTime;
    private Timestamp deadline;
    private Boolean status;
    private String content;
    private final String GROUP_KEY = "billboard";

    public static final int NORMAL_INFO = 1;
    public static final int SERVER_INFO = 2;
    private int type = NORMAL_INFO;

    public String getCreateTimeString() throws ParseException {
        return DateUtils.parseToString(createTime);
    }

    public String getStartTimeString() throws ParseException {
        return DateUtils.parseToString(startTime);
    }

    public String getDeadlineString() throws ParseException {
        return DateUtils.parseToString(deadline);
    }

    public JobKey getJobKey() {
        return new JobKey("noticeJob-" + noticeId, GROUP_KEY);
    }

    public TriggerKey getTriggerKey() {
        return new TriggerKey("noticeJob-" + noticeId, GROUP_KEY);
    }

    public Integer getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(Integer noticeId) {
        this.noticeId = noticeId;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getDeadline() {
        return deadline;
    }

    public void setDeadline(Timestamp deadline) {
        this.deadline = deadline;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Notice [noticeId=" + noticeId + ", userId=" + userId + ", username=" + username + ", createTime="
                + createTime + ", startTime=" + startTime + ", deadline=" + deadline + ", status=" + status
                + ", content=" + content + ", GROUP_KEY=" + GROUP_KEY + "]";
    }

    @Override
    public int compareTo(Notice o) {
        if (this.startTime.after(o.getStartTime())) {
            return -1;
        } else if (this.startTime.equals(o.getStartTime())) {
            return 0;
        }
        return 1;
    }

}
