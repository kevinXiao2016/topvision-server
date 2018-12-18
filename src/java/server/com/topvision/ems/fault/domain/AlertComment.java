package com.topvision.ems.fault.domain;

import com.topvision.framework.domain.BaseEntity;

public class AlertComment extends BaseEntity {
    private static final long serialVersionUID = -4740713253690345854L;
    private Long commentId;
    private String name;
    private String note;

    /**
     * 
     * @return commentId
     */
    public Long getCommentId() {
        return commentId;
    }

    /**
     * 
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @return note
     */
    public String getNote() {
        return note;
    }

    /**
     * 
     * @param commentId
     */
    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    /**
     * 
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @param note
     */
    public void setNote(String note) {
        this.note = note;
    }

}
