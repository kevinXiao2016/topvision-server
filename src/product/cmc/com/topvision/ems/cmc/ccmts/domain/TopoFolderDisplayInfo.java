/***********************************************************************
 * $Id: TopoFolderDisplayInfo.java,v1.0 2014-5-30 下午1:46:10 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ccmts.domain;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author flack
 * @created @2014-5-30-下午1:46:10
 *
 */
public class TopoFolderDisplayInfo implements AliasesSuperType {
    private static final long serialVersionUID = 1788711105692613521L;

    private Long folderId;
    private Long superiorId;
    private String name;
    private Integer totalNum;
    private Integer onlineNum;
    private Integer offineNum;

    public TopoFolderDisplayInfo() {
        super();
    }

    public TopoFolderDisplayInfo(Long folderId, Long superiorId, String name, Integer totalNum,
            Integer onlineNum, Integer offineNum) {
        super();
        this.folderId = folderId;
        this.superiorId = superiorId;
        this.name = name;
        this.totalNum = totalNum;
        this.onlineNum = onlineNum;
        this.offineNum = offineNum;
    }

    public Long getFolderId() {
        return folderId;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    public Long getSuperiorId() {
        return superiorId;
    }

    public void setSuperiorId(Long superiorId) {
        this.superiorId = superiorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

    public Integer getOnlineNum() {
        return onlineNum;
    }

    public void setOnlineNum(Integer onlineNum) {
        this.onlineNum = onlineNum;
    }

    public Integer getOffineNum() {
        return offineNum;
    }

    public void setOffineNum(Integer offineNum) {
        this.offineNum = offineNum;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("TopoFolderDisplayInfo [folderId=");
        builder.append(folderId);
        builder.append(", superiorId=");
        builder.append(superiorId);
        builder.append(", name=");
        builder.append(name);
        builder.append(", superiorName=");
        builder.append(", totalNum=");
        builder.append(totalNum);
        builder.append(", onlineNum=");
        builder.append(onlineNum);
        builder.append(", offineNum=");
        builder.append(offineNum);
        builder.append("]");
        return builder.toString();
    }

}
