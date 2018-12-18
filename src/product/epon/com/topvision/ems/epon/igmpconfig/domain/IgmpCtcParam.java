/***********************************************************************
 * $Id: IgmpCtcParam.java,v1.0 2016-6-7 下午3:53:20 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.igmpconfig.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author flack
 * @created @2016-6-7-下午3:53:20
 * CTC组播配置
 */
public class IgmpCtcParam implements AliasesSuperType {
    private static final long serialVersionUID = -4952969980970385460L;

    private Long entityId;
    //CTC组播功能使能
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.5.1.1.0", writable = true, type = "Integer32")
    private Integer ctcEnable;
    //CDR记录主动上报的间隔
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.5.1.2.0", writable = true, type = "Integer32")
    private Integer cdrInterval;
    //CDR记录主动上报的数量
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.5.1.3.0", writable = true, type = "Integer32")
    private Integer cdrNum;
    //手动上报组播CDR日志到服务器
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.5.1.4.0", writable = true, type = "Integer32")
    private Integer cdrReport;
    //组播预览次数自动清零的时刻
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.5.1.5.0", writable = true, type = "Integer32")
    private Integer autoResetTime;
    //组播预览的标识时间
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.5.1.6.0", writable = true, type = "Integer32")
    private Integer recognitionTime;
    //ONU转发模式
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.5.1.7.0", writable = true, type = "Integer32")
    private Integer onuFwdMode;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getCtcEnable() {
        return ctcEnable;
    }

    public void setCtcEnable(Integer ctcEnable) {
        this.ctcEnable = ctcEnable;
    }

    public Integer getCdrInterval() {
        return cdrInterval;
    }

    public void setCdrInterval(Integer cdrInterval) {
        this.cdrInterval = cdrInterval;
    }

    public Integer getCdrNum() {
        return cdrNum;
    }

    public void setCdrNum(Integer cdrNum) {
        this.cdrNum = cdrNum;
    }

    public Integer getCdrReport() {
        return cdrReport;
    }

    public void setCdrReport(Integer cdrReport) {
        this.cdrReport = cdrReport;
    }

    public Integer getAutoResetTime() {
        return autoResetTime;
    }

    public void setAutoResetTime(Integer autoResetTime) {
        this.autoResetTime = autoResetTime;
    }

    public Integer getRecognitionTime() {
        return recognitionTime;
    }

    public void setRecognitionTime(Integer recognitionTime) {
        this.recognitionTime = recognitionTime;
    }

    public Integer getOnuFwdMode() {
        return onuFwdMode;
    }

    public void setOnuFwdMode(Integer onuFwdMode) {
        this.onuFwdMode = onuFwdMode;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("IgmpCtcParam [entityId=");
        builder.append(entityId);
        builder.append(", ctcEnable=");
        builder.append(ctcEnable);
        builder.append(", cdrInterval=");
        builder.append(cdrInterval);
        builder.append(", cdrNum=");
        builder.append(cdrNum);
        builder.append(", cdrReport=");
        builder.append(cdrReport);
        builder.append(", autoResetTime=");
        builder.append(autoResetTime);
        builder.append(", recognitionTime=");
        builder.append(recognitionTime);
        builder.append(", onuFwdMode=");
        builder.append(onuFwdMode);
        builder.append("]");
        return builder.toString();
    }

}
