/***********************************************************************
 * $Id: CmcTempQualityFor8800B.java,v1.0 2013-9-4 上午11:43:33 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.facade;

import java.io.Serializable;
import java.sql.Timestamp;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author Rod John
 * @created @2013-9-4-上午11:43:33
 * 
 */
@Alias("cmcTempQualityFor8800B")
public class CmcTempQualityFor8800B implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 3343398851919377076L;
    private Long entityId;
    private Long cmcId;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.1", index = true)
    private Long cmcIndex;// 是表的ifIndex，Mac域
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.19.1.1")
    private Long usModuleTemp;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.19.1.2")
    private Long dsModuleTemp;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.19.1.3")
    private Long outsideTemp;//MAC模块温度
    //@SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.19.1.4")
    private Long insideTemp;//MAC芯片温度

    private Timestamp collectTime;

    /**
     * @return the entityId
     */
    public Long getEntityId() {
        return entityId;
    }

    /**
     * @param entityId
     *            the entityId to set
     */
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

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
     * @return the cmcIndex
     */
    public Long getCmcIndex() {
        return cmcIndex;
    }

    /**
     * @param cmcIndex
     *            the cmcIndex to set
     */
    public void setCmcIndex(Long cmcIndex) {
        this.cmcIndex = cmcIndex;
    }

    public Long getUsModuleTemp() {
        return usModuleTemp;
    }

    public void setUsModuleTemp(Long usModuleTemp) {
        this.usModuleTemp = usModuleTemp;
    }

    public Long getDsModuleTemp() {
        return dsModuleTemp;
    }

    public void setDsModuleTemp(Long dsModuleTemp) {
        this.dsModuleTemp = dsModuleTemp;
    }

    public Long getOutsideTemp() {
        return outsideTemp;
    }

    public void setOutsideTemp(Long outsideTemp) {
        this.outsideTemp = outsideTemp;
    }

    public Long getInsideTemp() {
        return insideTemp;
    }

    public void setInsideTemp(Long insideTemp) {
        this.insideTemp = insideTemp;
    }

    /**
     * @return the collectTime
     */
    public Timestamp getCollectTime() {
        return collectTime;
    }

    /**
     * @param collectTime
     *            the collectTime to set
     */
    public void setCollectTime(Timestamp collectTime) {
        this.collectTime = collectTime;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcTempQualityFor8800B [entityId=");
        builder.append(entityId);
        builder.append(", cmcId=");
        builder.append(cmcId);
        builder.append(", cmcIndex=");
        builder.append(cmcIndex);
        builder.append(", usModuleTemp=");
        builder.append(usModuleTemp);
        builder.append(", dsModuleTemp=");
        builder.append(dsModuleTemp);
        builder.append(", outsideTemp=");
        builder.append(outsideTemp);
        builder.append(", insideTemp=");
        builder.append(insideTemp);
        builder.append(", collectTime=");
        builder.append(collectTime);
        builder.append("]");
        return builder.toString();
    }

}
