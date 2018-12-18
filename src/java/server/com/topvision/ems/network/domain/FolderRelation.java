package com.topvision.ems.network.domain;

import java.util.ArrayList;
import java.util.List;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

public class FolderRelation implements AliasesSuperType {
    private static final long serialVersionUID = 6365033388943232086L;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getFolderId() {
        return folderId;
    }

    public void setFolderId(long folderId) {
        this.folderId = folderId;
    }

    public long getSuperiorId() {
        return superiorId;
    }

    public void setSuperiorId(long superiorId) {
        this.superiorId = superiorId;
    }

    public List<FolderRelation> getChilds() {
        return childs;
    }

    public void setChilds(List<FolderRelation> childs) {
        this.childs = childs;
    }

    public void add(FolderRelation child) {
        childs.add(child);
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    private long folderId;
    private long superiorId;
    private String name;
    private Integer level;
    private List<FolderRelation> childs = new ArrayList<FolderRelation>();
}
