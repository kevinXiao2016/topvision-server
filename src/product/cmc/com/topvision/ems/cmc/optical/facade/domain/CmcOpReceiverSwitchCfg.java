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
 * @author dosion
 * @created @2013-12-2-下午2:11:49
 * 
 */
@Alias("cmcOpReceiverSwitchCfg")
public class CmcOpReceiverSwitchCfg implements AliasesSuperType {
    private static final long serialVersionUID = 3784533831663236148L;

    private Long id;
    private Long cmcId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.21.10.1.1", index = true)
    private Integer switchIndex;
    /**
     * 报告AB开关的状态
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.21.10.1.2")
    private Byte switchState;
    /**
     * 光接收机的AB开关控制
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.21.10.1.3")
    private Byte switchControl;
    /**
     * 下行光接收机中A/B开关转换门限，单位为1dBm
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.21.10.1.4")
    private Integer switchThres;
    
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

    public Integer getSwitchIndex() {
        return switchIndex;
    }

    public void setSwitchIndex(Integer switchIndex) {
        this.switchIndex = switchIndex;
    }

    public Byte getSwitchState() {
        return switchState;
    }

    public void setSwitchState(Byte switchState) {
        this.switchState = switchState;
    }

    public Byte getSwitchControl() {
        return switchControl;
    }

    public void setSwitchControl(Byte switchControl) {
        this.switchControl = switchControl;
    }

    public Integer getSwitchThres() {
        return switchThres;
    }

    public void setSwitchThres(Integer switchThres) {
        this.switchThres = switchThres;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcOpReceiverSwitchCfg [id=");
        builder.append(id);
        builder.append(", cmcId=");
        builder.append(cmcId);
        builder.append(", switchIndex=");
        builder.append(switchIndex);
        builder.append(", switchState=");
        builder.append(switchState);
        builder.append(", switchControl=");
        builder.append(switchControl);
        builder.append(", switchThres=");
        builder.append(switchThres);
        builder.append("]");
        return builder.toString();
    }
}
