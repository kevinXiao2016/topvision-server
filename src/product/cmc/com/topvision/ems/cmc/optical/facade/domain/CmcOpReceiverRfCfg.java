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
 * 光机RF配置信息，一台CCMTS设备会有一条RF配置信息
 * 
 * @author dosion
 * @created @2013-12-2-下午2:11:49
 * 
 */
@Alias("cmcOpReceiverRfCfg")
public class CmcOpReceiverRfCfg implements AliasesSuperType {
    private static final long serialVersionUID = 3784533831663236148L;

    /**
     * 数据库主键ID
     */
    private Long id;
    private Long cmcId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.21.5.1.1", index = true)
    private Integer outputIndex;
    /**
     * 控制下行光接收机输出的开关
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.21.5.1.2", writable = true, type = "Integer32")
    private Byte outputControl;
    /**
     * 控制下行光接收机输出增益的类型
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.21.5.1.3", writable = true, type = "Integer32")
    private Byte outputGainType;
    /**
     * 下行光接收机输出RF电平
     */
    // @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.21.5.1.4")
    private Integer outputLevel;
    /**
     * 下行光接收机光路AGC起始功率设置
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.21.5.1.5", writable = true, type = "Integer32")
    private Integer outputAGCOrigin;
    /**
     * 下行光接收机中RF输出电平衰减量设置
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.21.5.1.6", writable = true, type = "Integer32")
    private Integer outputRFlevelatt;
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

    public Integer getOutputIndex() {
        return outputIndex;
    }

    public void setOutputIndex(Integer outputIndex) {
        this.outputIndex = outputIndex;
    }

    public Byte getOutputControl() {
        return outputControl;
    }

    public void setOutputControl(Byte outputControl) {
        this.outputControl = outputControl;
    }

    public Byte getOutputGainType() {
        return outputGainType;
    }

    public void setOutputGainType(Byte outputGainType) {
        this.outputGainType = outputGainType;
    }

    public Integer getOutputLevel() {
        return outputLevel;
    }

    public void setOutputLevel(Integer outputLevel) {
        this.outputLevel = outputLevel;
    }

    public Integer getOutputAGCOrigin() {
        return outputAGCOrigin;
    }

    public void setOutputAGCOrigin(Integer outputAGCOrigin) {
        this.outputAGCOrigin = outputAGCOrigin;
    }

    public Integer getOutputRFlevelatt() {
        return outputRFlevelatt;
    }

    public void setOutputRFlevelatt(Integer outputRFlevelatt) {
        this.outputRFlevelatt = outputRFlevelatt;
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
        builder.append("CmcOpReceiverRfCfg [id=");
        builder.append(id);
        builder.append(", cmcId=");
        builder.append(cmcId);
        builder.append(", outputIndex=");
        builder.append(outputIndex);
        builder.append(", outputControl=");
        builder.append(outputControl);
        builder.append(", outputGainType=");
        builder.append(outputGainType);
        builder.append(", outputLevel=");
        builder.append(outputLevel);
        builder.append(", outputAGCOrigin=");
        builder.append(outputAGCOrigin);
        builder.append(", outputRFlevelatt=");
        builder.append(outputRFlevelatt);
        builder.append(", dt=");
        builder.append(dt);
        builder.append("]");
        return builder.toString();
    }

}
