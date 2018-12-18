/***********************************************************************
 * $Id: EntityVerison.java,v1.0 2014年9月23日 下午3:07:33 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.upgrade.domain;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author loyal
 * @created @2014年9月23日-下午3:07:33
 * 
 */
@Alias("entityVersion")
public class EntityVersion implements AliasesSuperType {
    private static final long serialVersionUID = -4506771951603544708L;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private String versionName;
    private Timestamp createTime;
    @SuppressWarnings("unused")
    private String createTimeStr;
    private String propertyFileName;
    private String typeDisplayNames;
    private List<String> subType = new ArrayList<>();
    private String subTypeString;
    private String transferType;// FTP - 类A ; TFTP- 类B

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
        createTimeStr = sdf.format(createTime);
    }

    public String getPropertyFileName() {
        return propertyFileName;
    }

    public void setPropertyFileName(String propertyFileName) {
        this.propertyFileName = propertyFileName;
    }

    public String getTypeDisplayNames() {
        return typeDisplayNames;
    }

    public void setTypeDisplayNames(String typeDisplayNames) {
        this.typeDisplayNames = typeDisplayNames;
    }

    public List<String> getSubType() {
        return subType;
    }

    public void setSubType(List<String> subType) {
        this.subType = subType;
    }

    public String getSubTypeString() {
        return subTypeString;
    }

    public void setSubTypeString(String subTypeString) {
        this.subTypeString = subTypeString;
    }

    public String getCreateTimeStr() {
        return sdf.format(createTime);
    }

    public String getTransferType() {
        return transferType;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("EntityVerison [versionName=");
        builder.append(versionName);
        builder.append(", createTime=");
        builder.append(createTime);
        builder.append(", propertyFileName=");
        builder.append(propertyFileName);
        builder.append(", typeDisplayNames=");
        builder.append(typeDisplayNames);
        builder.append("]");
        return builder.toString();
    }

}
