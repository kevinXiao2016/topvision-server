/***********************************************************************
 * $Id: EntityType.java,v 1.1 Jul 23, 2008 9:41:45 AM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.ems.facade.domain;

import java.io.ByteArrayInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.ibatis.type.Alias;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.domain.TreeEntity;

/**
 * @Create Date Jul 23, 2008 9:41:45 AM
 * 
 * @author kelers
 * 
 */
@Alias("entityType")
public class EntityType implements AliasesSuperType, TreeEntity {
    private static final long serialVersionUID = -2730869915541383418L;

    public static final long UNKNOWN_TYPE = Entity.UNKNOWN_TYPE;

    private long typeId;
    private String name = null;
    private String displayName = null;
    private String icon16 = null;
    private String icon32 = null;
    private String icon48 = null;
    private String icon64 = null;
    private String remark = null;
    private String sysObjectID = null;
    private Integer standard;
    private String discoveryBean;
    private byte[] properties = null;
    private byte[] parentProps = null;
    private long corpId = 1;
    private String logicPane;
    private String physicPane;
    /* 所属资产分类 */
    private long categoryId;
    // 默认管理方式
    private String driverId;
    private int entityCount = 0;
    // 所属模块（产品类型），epon,cmc,wlan etc.
    private String module;
    private boolean disabled = false;

    private boolean licenseSupport = false;

    //Add by Victor@20141018增加entity树形展示支持
    @Override
    public String getId() {
        return name;
    }

    @Override
    public String getParentId() {
        return name.equals(module) ? null : module;
    }

    @Override
    public String getText() {
        return displayName;
    }

    /**
     * @return the entityCount
     */
    public int getEntityCount() {
        return entityCount;
    }

    /**
     * @param entityCount
     *            the entityCount to set
     */
    public void setEntityCount(int entityCount) {
        this.entityCount = entityCount;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public long getCorpId() {
        return corpId;
    }

    /**
     * @return the displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    public String getDriverId() {
        return driverId;
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
     * @return the icon16
     */
    public String getIcon16() {
        return icon16;
    }

    /**
     * @return the icon32
     */
    public String getIcon32() {
        return icon32;
    }

    /**
     * @return the icon48
     */
    public String getIcon48() {
        return icon48;
    }

    /**
     * @return the icon64
     */
    public String getIcon64() {
        return icon64;
    }

    public String getLogicPane() {
        return logicPane;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the parentProps
     */
    public byte[] getParentProps() {
        return parentProps;
    }

    public String getPhysicPane() {
        return physicPane;
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
     * @return the typeId
     */
    public long getTypeId() {
        return typeId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

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

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    /**
     * @param icon16
     *            the icon16 to set
     */
    public void setIcon16(String icon16) {
        this.icon16 = icon16;
    }

    /**
     * @param icon32
     *            the icon32 to set
     */
    public void setIcon32(String icon32) {
        this.icon32 = icon32;
    }

    /**
     * @param icon48
     *            the icon48 to set
     */
    public void setIcon48(String icon48) {
        this.icon48 = icon48;
    }

    /**
     * @param icon64
     *            the icon64 to set
     */
    public void setIcon64(String icon64) {
        this.icon64 = icon64;
    }

    public void setLogicPane(String logicPane) {
        this.logicPane = logicPane;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param parentProps
     *            the parentProps to set
     */
    public void setParentProps(byte[] parentProps) {
        this.parentProps = parentProps;
    }

    public void setPhysicPane(String physicPane) {
        this.physicPane = physicPane;
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
     * @param typeId
     *            the typeId to set
     */
    public void setTypeId(long typeId) {
        this.typeId = typeId;
    }

    /**
     * @return the standard
     */
    public Integer getStandard() {
        return standard;
    }

    /**
     * @param standard the standard to set
     */
    public void setStandard(Integer standard) {
        this.standard = standard;
    }

    /**
     * @return the discoveryBean
     */
    public String getDiscoveryBean() {
        return discoveryBean;
    }

    /**
     * @param discoveryBean the discoveryBean to set
     */
    public void setDiscoveryBean(String discoveryBean) {
        this.discoveryBean = discoveryBean;
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
        toString.append("),typeId=").append(typeId);
        return toString.toString();
    }

    /**
     * @return the module
     */
    public String getModule() {
        return module;
    }

    /**
     * @param module
     *            the module to set
     */
    public void setModule(String module) {
        this.module = module;
    }

    /**
     * @return the licenseSupport
     */
    public boolean isLicenseSupport() {
        return licenseSupport;
    }

    /**
     * @param licenseSupport the licenseSupport to set
     */
    public void setLicenseSupport(boolean licenseSupport) {
        this.licenseSupport = licenseSupport;
    }

    /**
     * @return the disabled
     */
    public boolean isDisabled() {
        return disabled;
    }

    /**
     * @param disabled the disabled to set
     */
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
}
