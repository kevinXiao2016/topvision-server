/***********************************************************************
 * $Id: VirtualNetEvent.java,v1.0 2012-2-23 下午01:15:05 $
 * 
 * @author: zhanglongyang
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.message;

import com.topvision.ems.network.domain.ProductInVirtual;
import com.topvision.ems.network.domain.ProductSnmpParam;
import com.topvision.platform.message.event.EmsEventObject;

/**
 * @author zhanglongyang
 * @created @2012-2-23-下午01:15:05
 * 
 */
public class VirtualNetEvent extends EmsEventObject<VirtualNetListener> {
    private static final long serialVersionUID = -658599199635401228L;
    private ProductSnmpParam productSnmpParam;
    private ProductInVirtual productInVirtual;
    private String virtualNetEntityIp;
    private Long virtualNetEntityType;

    public VirtualNetEvent(Object source) {
        super(source);
    }

    /**
     * @return the productSnmpParam
     */
    public ProductSnmpParam getProductSnmpParam() {
        return productSnmpParam;
    }

    /**
     * @param productSnmpParam
     *            the productSnmpParam to set
     */
    public void setProductSnmpParam(ProductSnmpParam productSnmpParam) {
        this.productSnmpParam = productSnmpParam;
    }

    /**
     * @return the virtualNetEntityIp
     */
    public String getVirtualNetEntityIp() {
        return virtualNetEntityIp;
    }

    /**
     * @param virtualNetEntityIp
     *            the virtualNetEntityIp to set
     */
    public void setVirtualNetEntityIp(String virtualNetEntityIp) {
        this.virtualNetEntityIp = virtualNetEntityIp;
    }

    /**
     * @return the virtualNetEntityType
     */
    public Long getVirtualNetEntityType() {
        return virtualNetEntityType;
    }

    /**
     * @param virtualNetEntityType
     *            the virtualNetEntityType to set
     */
    public void setVirtualNetEntityType(Long virtualNetEntityType) {
        this.virtualNetEntityType = virtualNetEntityType;
    }

    /**
     * @return the productInVirtual
     */
    public ProductInVirtual getProductInVirtual() {
        return productInVirtual;
    }

    /**
     * @param productInVirtual
     *            the productInVirtual to set
     */
    public void setProductInVirtual(ProductInVirtual productInVirtual) {
        this.productInVirtual = productInVirtual;
    }

}
