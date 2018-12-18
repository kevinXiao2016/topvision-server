/***********************************************************************
 * $Id: CmcOpReceiverRfPort.java,v1.0 2013-12-18 上午11:43:29 $
 * 
 * @author: dosion
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.optical.facade.domain;

import java.sql.Timestamp;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author dosion
 * @created @2013-12-18-上午11:43:29
 * 
 */
@Alias("cmcOpReceiverRfPort")
public class CmcOpReceiverRfPort implements AliasesSuperType {
    private static final long serialVersionUID = -8593957534534813690L;
    private Long id;
    private Long cmcId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.21.11.1.1", index = true)
    private Integer rfPortIndex;
    /**
     * 射频输出电平，一个光机包括4路输出，当取值为-1时为表示光机不支持该参数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.21.11.1.2")
    private Integer rfOutputLevel;
    private Timestamp collectTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Integer getRfPortIndex() {
        return rfPortIndex;
    }

    public void setRfPortIndex(Integer rfPortIndex) {
        this.rfPortIndex = rfPortIndex;
    }

    public Integer getRfOutputLevel() {
        return rfOutputLevel;
    }

    public void setRfOutputLevel(Integer rfOutputLevel) {
        this.rfOutputLevel = rfOutputLevel;
    }

    public Timestamp getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Timestamp collectTime) {
        this.collectTime = collectTime;
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        if(CmcOpReceiverRfPort.class.isInstance(obj)){
            CmcOpReceiverRfPort compare = (CmcOpReceiverRfPort)obj;
            if(this.cmcId == null || this.rfPortIndex == null ){
                result = false;
            }else if(this.cmcId.equals(compare.cmcId) && this.rfPortIndex.equals(compare.rfPortIndex)){
                result = true;
            }
        }
        return result;
    }

}
