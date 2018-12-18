package com.topvision.ems.network.domain;

import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.domain.BaseEntity;

public class FolderUserGroupRela extends BaseEntity implements AliasesSuperType {
    private static final long serialVersionUID = 6486026693481849569L;
    private Long folderId;
    private Long userGroupId;
    private Integer power;
    private Long relaId;

    /**
     * @return the folderId
     */
    public Long getFolderId() {
        return folderId;
    }

    /**
     * @return the power
     */
    public Integer getPower() {
        return power;
    }

    /**
     * @return the relaId
     */
    public Long getRelaId() {
        return relaId;
    }

    /**
     * @return the userGroupId
     */
    public Long getUserGroupId() {
        return userGroupId;
    }

    /**
     * @param folderId
     *            the folderId to set
     */
    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    /**
     * @param power
     *            the power to set
     */
    public void setPower(Integer power) {
        this.power = power;
    }

    /**
     * @param relaId
     *            the relaId to set
     */
    public void setRelaId(Long relaId) {
        this.relaId = relaId;
    }

    /**
     * @param userGroupId
     *            the userGroupId to set
     */
    public void setUserGroupId(Long userGroupId) {
        this.userGroupId = userGroupId;
    }
}
