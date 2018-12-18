/***********************************************************************
 * $Id: VirtualProduct.java,v1.0 2012-2-24 上午11:24:45 $
 * 
 * @author: huqiao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.domain;

import java.sql.Timestamp;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author huqiao
 * @created @2012-2-24-上午11:24:45
 * 
 */
public class VirtualProduct implements AliasesSuperType {

    /**
     * 
     */
    private static final long serialVersionUID = -710100381190663240L;
    private Long virtualNetId;
    private Long productId;
    private Long productType;
    private String productName;
    private String productIp;
    private Long productIpLong;
    private Timestamp productCreateTime;

    /**
     * @return the virtualNetId
     */
    public Long getVirtualNetId() {
        return virtualNetId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("VirtualProduct [virtualNetId=");
        builder.append(virtualNetId);
        builder.append(", productId=");
        builder.append(productId);
        builder.append(", productType=");
        builder.append(productType);
        builder.append(", productIp=");
        builder.append(productIp);
        builder.append(", productIpLong=");
        builder.append(productIpLong);
        builder.append(", productCreateTime=");
        builder.append(productCreateTime);
        builder.append("]");
        return builder.toString();
    }

    /**
     * @param virtualNetId
     *            the virtualNetId to set
     */
    public void setVirtualNetId(Long virtualNetId) {
        this.virtualNetId = virtualNetId;
    }

    /**
     * @return the productId
     */
    public Long getProductId() {
        return productId;
    }

    /**
     * @param productId
     *            the productId to set
     */
    public void setProductId(Long productId) {
        this.productId = productId;
    }

    /**
     * @return the productType
     */
    public Long getProductType() {
        return productType;
    }

    /**
     * @param productType
     *            the productType to set
     */
    public void setProductType(Long productType) {
        this.productType = productType;
    }

    /**
     * @return the productIp
     */
    public String getProductIp() {
        return productIp;
    }

    /**
     * @param productIp
     *            the productIp to set
     */
    public void setProductIp(String productIp) {
        this.productIp = productIp;
    }

    /**
     * @return the productIpLong
     */
    public Long getProductIpLong() {
        return productIpLong;
    }

    /**
     * @param productIpLong
     *            the productIpLong to set
     */
    public void setProductIpLong(Long productIpLong) {
        this.productIpLong = productIpLong;
    }

    /**
     * @return the productCreateTime
     */
    public Timestamp getProductCreateTime() {
        return productCreateTime;
    }

    /**
     * @param productCreateTime
     *            the productCreateTime to set
     */
    public void setProductCreateTime(Timestamp productCreateTime) {
        this.productCreateTime = productCreateTime;
    }

    /**
     * @return the productName
     */
    public String getProductName() {
        return productName;
    }

    /**
     * @param productName
     *            the productName to set
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

}
