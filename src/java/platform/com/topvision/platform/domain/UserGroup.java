package com.topvision.platform.domain;

import java.sql.Timestamp;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.domain.BaseEntity;

@Alias("userGroup")
public class UserGroup extends BaseEntity implements com.topvision.framework.dao.mybatis.AliasesSuperType {
    private static final long serialVersionUID = 5090832397825884440L;

    /**
     * 超级默认组.
     */
    public static final long DEFAULT_SUPER_GROUP = 1;
    private long userGroupId;
    private String name;
    private String description;
    private Timestamp createTime;

    public Timestamp getCreateTime() {
        return createTime;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public long getUserGroupId() {
        return userGroupId;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUserGroupId(long userGroupId) {
        this.userGroupId = userGroupId;
    }

    @Override
    public String toString() {
        return "UserGroup{" + "createTime=" + createTime + ", userGroupId=" + userGroupId + ", name='" + name + '\''
                + ", description='" + description + '\'' + '}';
    }
}
