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
 * 交流电源信息 ,一台CCMTS设备会有多条交流电源信息
 * 
 * @author dosion
 * @created @2013-12-2-下午2:11:49
 * 
 */
@Alias("cmcOpReceiverAcPower")
public class CmcOpReceiverAcPower implements AliasesSuperType {
    private static final long serialVersionUID = 3784533831663236148L;

    private Long id;
    private Long cmcId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.21.13.1.1", index = true)
    private Integer powerIndex;
    /**
     * 线路电源电压，单位为1VAC
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.21.13.1.2")
    private Integer powerVoltage1;
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

    public Integer getPowerVoltage1() {
        return powerVoltage1;
    }

    public void setPowerVoltage1(Integer powerVoltage1) {
        this.powerVoltage1 = powerVoltage1;
    }

    public Long getDt() {
        return dt;
    }

    public void setDt(Long dt) {
        this.dt = dt;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcOpReceiverAcPower [id=");
        builder.append(id);
        builder.append(", cmcId=");
        builder.append(cmcId);
        builder.append(", powerIndex=");
        builder.append(powerIndex);
        builder.append(", powerVoltage1=");
        builder.append(powerVoltage1);
        builder.append(", dt=");
        builder.append(dt);
        builder.append("]");
        return builder.toString();
    }

}
