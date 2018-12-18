/***********************************************************************
 * $Id: CmcOpReceiverRfCfg.java,v1.0 2013-12-2 下午2:11:49 $
 * 
 * @author: dosion
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.optical.facade.domain;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * 光机直流电源信息，一台CCMTS设备会有一条光机直流电源信息
 * 
 * @author dosion
 * @created @2013-12-2-下午2:11:49
 * 
 */
@Alias("cmcOpReceiverDcPower")
public class CmcOpReceiverDcPower implements AliasesSuperType {
    private static final long serialVersionUID = 3784533831663236148L;

    private Long id;
    private Long cmcId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.21.8.1.1", index = true)
    private Integer powerIndex;
    /**
     * 电源电压，单位0.1V
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.21.8.1.2")
    private Integer powerVoltage;
    private Long dt;

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

    public Integer getPowerIndex() {
        return powerIndex;
    }

    public void setPowerIndex(Integer powerIndex) {
        this.powerIndex = powerIndex;
    }

    public Integer getPowerVoltage() {
        return powerVoltage;
    }

    public void setPowerVoltage(Integer powerVoltage) {
        this.powerVoltage = powerVoltage;
    }

    public Long getDt() {
        return dt;
    }

    public void setDt(Long dt) {
        this.dt = dt;
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        if(CmcOpReceiverDcPower.class.isInstance(obj)){
            CmcOpReceiverDcPower compare = (CmcOpReceiverDcPower)obj;
            if(this.cmcId == null || this.powerIndex == null ){
                result = false;
            }else if(this.cmcId.equals(compare.cmcId) && this.powerIndex.equals(compare.powerIndex)){
                result = true;
            }
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcOpReceiverDcPower [id=");
        builder.append(id);
        builder.append(", cmcId=");
        builder.append(cmcId);
        builder.append(", powerIndex=");
        builder.append(powerIndex);
        builder.append(", powerVoltage=");
        builder.append(powerVoltage);
        builder.append(", dt=");
        builder.append(dt);
        builder.append("]");
        return builder.toString();
    }

}
