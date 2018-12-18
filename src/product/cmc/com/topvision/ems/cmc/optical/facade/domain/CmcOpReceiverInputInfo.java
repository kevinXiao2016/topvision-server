/***********************************************************************
 * $Id: CmcOpReceiverRfCfg.java,v1.0 2013-12-2 下午2:11:49 $
 * 
 * @author: dosion
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.optical.facade.domain;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * 光机接收光功率信息
 * 
 * @author dosion
 * @created @2013-12-2-下午2:11:49
 * 
 */
@Alias("cmcOpReceiverInputInfo")
public class CmcOpReceiverInputInfo implements AliasesSuperType {
    private static final long serialVersionUID = 3784533831663236148L;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 数据库存储主键ID
     */
    private Long id;
    private Long cmcId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.21.3.1.1", index = true)
    private Integer inputIndex;
    /**
     * 接收光功率, 单位0.1dBm
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.21.3.1.2")
    private Integer inputPower;
    /**
     * 采集时间
     */
    private Long dt;
    private Timestamp collectTime;
    private String collectTimeStr;

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

    public Integer getInputIndex() {
        return inputIndex;
    }

    public void setInputIndex(Integer inputIndex) {
        this.inputIndex = inputIndex;
    }

    public Integer getInputPower() {
        return inputPower;
    }

    public void setInputPower(Integer inputPower) {
        this.inputPower = inputPower;
    }

    public Long getDt() {
        return dt;
    }

    public void setDt(Long dt) {
        this.dt = dt;
        this.collectTime = new Timestamp(dt);
        this.collectTimeStr = dateFormat.format(collectTime);
    }

    public Timestamp getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Timestamp collectTime) {
        this.collectTime = collectTime;
        this.collectTimeStr = dateFormat.format(collectTime);
        this.dt = collectTime.getTime();
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        if (CmcOpReceiverInputInfo.class.isInstance(obj)) {
            CmcOpReceiverInputInfo compare = (CmcOpReceiverInputInfo) obj;
            if (this.cmcId == null || this.inputIndex == null) {
                result = false;
            } else if (this.cmcId.equals(compare.cmcId) && this.inputIndex.equals(compare.inputIndex)) {
                result = true;
            }
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcOpReceiverInputInfo [id=");
        builder.append(id);
        builder.append(", cmcId=");
        builder.append(cmcId);
        builder.append(", inputIndex=");
        builder.append(inputIndex);
        builder.append(", inputPower=");
        builder.append(inputPower);
        builder.append(", dt=");
        builder.append(dt);
        builder.append("]");
        return builder.toString();
    }

    public String getCollectTimeStr() {
        return collectTimeStr;
    }

    public void setCollectTimeStr(String collectTimeStr) {
        this.collectTimeStr = collectTimeStr;
    }
}
