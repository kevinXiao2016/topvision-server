/***********************************************************************
 * $Id: TopoEntityStastic.java,v1.0 2013-5-28 上午10:27:39 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.report.domain;

import com.topvision.platform.zetaframework.util.ZetaUtil;

/**
 * @author Bravin
 * @created @2013-5-28-上午10:27:39
 * 
 */
public class TopoEntityStastic {
    private Long folderId;
    private String folderName;
    private Long entityId;
    private String entityIp;
    private String entityName;
    private Integer typeId;
    private String typeName;

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        if (folderName == null) {
            this.folderName = "";
        } else {
            this.folderName = ZetaUtil.getStaticString(folderName, "resources");
        }
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getEntityIp() {
        return entityIp;
    }

    public void setEntityIp(String entityIp) {
        this.entityIp = entityIp;
    }

    public Long getFolderId() {
        return folderId;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("TopoEntityStastic [folderId=");
        builder.append(folderId);
        builder.append(", folderName=");
        builder.append(folderName);
        builder.append(", entityId=");
        builder.append(entityId);
        builder.append(", entityIp=");
        builder.append(entityIp);
        builder.append(", entityName=");
        builder.append(entityName);
        builder.append(", typeId=");
        builder.append(typeId);
        builder.append(", typeName=");
        builder.append(typeName);
        builder.append("]");
        return builder.toString();
    }

}