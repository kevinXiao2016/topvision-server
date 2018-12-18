/***********************************************************************
 * $Id: ProductInVirtual.java,v1.0 2012-2-28 下午04:55:41 $
 * 
 * @author: huqiao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.domain;

import java.io.Serializable;

/**
 * @author huqiao
 * @created @2012-2-28-下午04:55:41
 * 
 */
public class ProductInVirtual implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 7877341994065258287L;
    public Long productId;
    public String productName;
    public String productIp;
    public Long virtualNetId;
    public Long productType;

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
     * @return the virtualNetId
     */
    public Long getVirtualNetId() {
        return virtualNetId;
    }

    /**
     * @param virtualNetId
     *            the virtualNetId to set
     */
    public void setVirtualNetId(Long virtualNetId) {
        this.virtualNetId = virtualNetId;
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

}
