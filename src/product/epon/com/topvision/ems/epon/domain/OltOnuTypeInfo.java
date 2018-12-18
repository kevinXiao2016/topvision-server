/***********************************************************************
 * $Id: OltOnuTypeInfo.java,v1.0 2011-12-21 下午02:05:33 $
 * 
 * @author: huqiao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.domain;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author huqiao
 * @created @2011-12-21-下午02:05:33
 * 
 */
@Alias(value = "onuTypeInfo")
public class OltOnuTypeInfo implements Serializable, AliasesSuperType {

    private static final long serialVersionUID = 3580753014604641266L;

    private String OnuTypeName;
    private Integer OnuTypeId;
    private Integer GePortNum;
    private Integer FePortNum;
    private Integer OnuPonNum;
    private String OnuDesc;
    private String OnuIcon;

    /**
     * @return the onuTypeName
     */
    public String getOnuTypeName() {
        return OnuTypeName;
    }

    /**
     * @param onuTypeName
     *            the onuTypeName to set
     */
    public void setOnuTypeName(String onuTypeName) {
        OnuTypeName = onuTypeName;
    }

    /**
     * @return the onuTypeId
     */
    public Integer getOnuTypeId() {
        return OnuTypeId;
    }

    /**
     * @param onuTypeId
     *            the onuTypeId to set
     */
    public void setOnuTypeId(Integer onuTypeId) {
        OnuTypeId = onuTypeId;
    }

    /**
     * @return the gePortNum
     */
    public Integer getGePortNum() {
        return GePortNum;
    }

    /**
     * @param gePortNum
     *            the gePortNum to set
     */
    public void setGePortNum(Integer gePortNum) {
        GePortNum = gePortNum;
    }

    /**
     * @return the fePortNum
     */
    public Integer getFePortNum() {
        return FePortNum;
    }

    /**
     * @param fePortNum
     *            the fePortNum to set
     */
    public void setFePortNum(Integer fePortNum) {
        FePortNum = fePortNum;
    }

    /**
     * @return the onuPonNum
     */
    public Integer getOnuPonNum() {
        return OnuPonNum;
    }

    /**
     * @param onuPonNum
     *            the onuPonNum to set
     */
    public void setOnuPonNum(Integer onuPonNum) {
        OnuPonNum = onuPonNum;
    }

    /**
     * @return the onuDesc
     */
    public String getOnuDesc() {
        return OnuDesc;
    }

    /**
     * @param onuDesc
     *            the onuDesc to set
     */
    public void setOnuDesc(String onuDesc) {
        OnuDesc = onuDesc;
    }

    /**
     * @return the onuIcon
     */
    public String getOnuIcon() {
        return OnuIcon;
    }

    /**
     * @param onuIcon
     *            the onuIcon to set
     */
    public void setOnuIcon(String onuIcon) {
        OnuIcon = onuIcon;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OltOnuTypeInfo [OnuTypeName=");
        builder.append(OnuTypeName);
        builder.append(", OnuTypeId=");
        builder.append(OnuTypeId);
        builder.append(", GePortNum=");
        builder.append(GePortNum);
        builder.append(", FePortNum=");
        builder.append(FePortNum);
        builder.append(", OnuPonNum=");
        builder.append(OnuPonNum);
        builder.append(", OnuDesc=");
        builder.append(OnuDesc);
        builder.append(", OnuIcon=");
        builder.append(OnuIcon);
        builder.append("]");
        return builder.toString();
    }

}
