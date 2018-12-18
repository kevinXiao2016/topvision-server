/***********************************************************************
 * $Id: TopCcmtsOpRxOutput.java,v1.0 2016年9月13日 下午1:48:29 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.opticalreceiver.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author haojie
 * @created @2016年9月13日-下午1:48:29
 *
 */
public class TopCcmtsOpRxOutput implements AliasesSuperType {
    private static final long serialVersionUID = -834848806715286088L;

    private Long cmcId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.21.5.1.1", index = true)
    private Long outputIndex;

    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.21.5.1.2", writable = true, type = "Integer32")
    private Integer outputControl;// 正向光收模块状态 1: off 2:on

    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.21.5.1.6", writable = true, type = "Integer32")
    private Integer configurationOutputRFlevelatt;// 正向光发射频衰减

    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.21.5.1.7", type = "Integer32")
    private Integer configurationAGCRg;// AGC输入光功率范围,根据光机类型显示固定值

    /**
     * @return the cmcId
     */
    public Long getCmcId() {
        return cmcId;
    }

    /**
     * @param cmcId
     *            the cmcId to set
     */
    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    /**
     * @return the outputIndex
     */
    public Long getOutputIndex() {
        return outputIndex;
    }

    /**
     * @param outputIndex
     *            the outputIndex to set
     */
    public void setOutputIndex(Long outputIndex) {
        this.outputIndex = outputIndex;
    }

    /**
     * @return the outputControl
     */
    public Integer getOutputControl() {
        return outputControl;
    }

    /**
     * @param outputControl
     *            the outputControl to set
     */
    public void setOutputControl(Integer outputControl) {
        this.outputControl = outputControl;
    }

    /**
     * @return the configurationOutputRFlevelatt
     */
    public Integer getConfigurationOutputRFlevelatt() {
        return configurationOutputRFlevelatt;
    }

    /**
     * @param configurationOutputRFlevelatt
     *            the configurationOutputRFlevelatt to set
     */
    public void setConfigurationOutputRFlevelatt(Integer configurationOutputRFlevelatt) {
        this.configurationOutputRFlevelatt = configurationOutputRFlevelatt;
    }

    /**
     * @return the configurationAGCRg
     */
    public Integer getConfigurationAGCRg() {
        return configurationAGCRg;
    }

    /**
     * @param configurationAGCRg
     *            the configurationAGCRg to set
     */
    public void setConfigurationAGCRg(Integer configurationAGCRg) {
        this.configurationAGCRg = configurationAGCRg;
    }

}
