package com.topvision.platform.domain;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.domain.BaseEntity;

@Alias("department")
public class Department extends BaseEntity implements com.topvision.framework.dao.mybatis.AliasesSuperType {
    private static final long serialVersionUID = 4183625053053974751L;

    private String name;
    private long departmentId;
    private long superiorId;
    private String note;

    public long getDepartmentId() {
        return departmentId;
    }

    public String getName() {
        return name;
    }

    public String getNote() {
        return note;
    }

    public long getSuperiorId() {
        return superiorId;
    }

    public void setDepartmentId(long departmentId) {
        this.departmentId = departmentId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setSuperiorId(long superiorId) {
        this.superiorId = superiorId;
    }

    @Override
    public String toString() {
        return "Department{" + "departmentId=" + departmentId + ", name='" + name + '\'' + ", superiorId=" + superiorId
                + ", note='" + note + '\'' + '}';
    }
}
