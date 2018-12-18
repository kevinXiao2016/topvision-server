package com.topvision.platform.domain;

import java.sql.Timestamp;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.domain.BaseEntity;
import com.topvision.framework.domain.TreeEntity;

@Alias("role")
public class Role extends BaseEntity implements TreeEntity, com.topvision.framework.dao.mybatis.AliasesSuperType {
    private static final long serialVersionUID = -4374987782252017838L;

    public static int SUPERADMIN_ID = 1;
    public static int ADMINISTRATOR_ID = 2;
    private Long roleId;
    private Integer superiorId;
    private String name;
    private String note;
    private Timestamp createTime = null;

    public Timestamp getCreateTime() {
        return createTime;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.domain.TreeEntity#getId()
     */
    @Override
    public String getId() {
        return String.valueOf(roleId);
    }

    public String getName() {
        return name;
    }

    public String getNote() {
        return note;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.domain.TreeEntity#getParentId()
     */
    @Override
    public String getParentId() {
        return String.valueOf(superiorId);
    }

    @Override
    public String getText() {
        return getName();
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Integer getSuperiorId() {
        return superiorId;
    }

    public void setSuperiorId(Integer superiorId) {
        this.superiorId = superiorId;
    }

    @Override
    public String toString() {
        return name;
    }

}
