/***********************************************************************
 * $Id: EntityCorp.java,v 1.1 Jul 23, 2008 3:43:20 PM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.ems.template.domain;

import java.io.ByteArrayInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.ibatis.type.Alias;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.domain.BaseEntity;

/**
 * @Create Date Jul 23, 2008 3:43:20 PM
 * 
 * @author kelers
 * 
 */
@Alias("entityCorp")
public class EntityCorp extends BaseEntity implements AliasesSuperType {
    private static final long serialVersionUID = 2243391917228962682L;

    private long corpId;
    private String sysObjectID = null;
    private String name = null;
    private String displayName = null;
    private byte[] properties = null;
    private String remark = null;

    /**
     * @return the corpId
     */
    public long getCorpId() {
        return corpId;
    }

    /**
     * @return the displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * 
     * @param tag
     * @return
     */
    public NodeList getElementsByTagName(String tag) {
        if (properties == null || properties.length == 0) {
            return null;
        }
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new ByteArrayInputStream(properties));
            document.getDocumentElement().normalize();
            return document.getElementsByTagName(tag);
        } catch (Exception ex) {
        }
        return null;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the properties
     */
    public byte[] getProperties() {
        return properties;
    }

    /**
     * @return the remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @return the sysObjectID
     */
    public String getSysObjectID() {
        return sysObjectID;
    }

    /**
     * @param corpId
     *            the corpId to set
     */
    public void setCorpId(long corpId) {
        this.corpId = corpId;
    }

    /**
     * @param displayName
     *            the displayName to set
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param properties
     *            the properties to set
     */
    public void setProperties(byte[] properties) {
        this.properties = properties;
    }

    /**
     * @param remark
     *            the remark to set
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * @param sysObjectID
     *            the sysObjectID to set
     */
    public void setSysObjectID(String sysObjectID) {
        this.sysObjectID = sysObjectID;
    }

    /**
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder toString = new StringBuilder();
        toString.append(name).append("(").append(displayName);
        toString.append("),corpId=").append(corpId);
        return toString.toString();
    }
}
