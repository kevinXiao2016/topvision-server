package com.topvision.ems.cmc.spectrum.facade.domain;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.domain.PhysAddress;

/**
 * 设备频谱采集配置信息 另： 频谱开关对A、B设备不一样 对B设备来讲，每个CC都有一个采集开关 对A设备来讲，除了每台CC有一个单独开关外，还有一个基于OLT的整体开关
 *
 * @author bryan
 */
@Alias("spectrumCfg")
public class SpectrumCfg implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 6237988094835812387L;
    public static final Integer STOP = 0;
    public static final Integer START = 1;
    public static final Integer FREQINTERVAL = 20000;
    private Long cmcId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.16.2.1.1", index = true)
    private PhysAddress fftMacIndex;// MIB用
    private String fftMonitorCmcMacIndex;// 数据库用
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.16.2.1.2", writable = true, type = "Integer32")
    private Integer fftMonitorTimeInterval;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.16.2.1.3", writable = true, type = "Integer32")
    private Integer fftMonitorFreqInterval;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.16.2.1.4")
    private Integer fftMonitorFreqStart;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.16.2.1.5")
    private Integer fftMonitorFreqEnd;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.16.2.1.6", writable = true, type = "Integer32")
    private Integer fftMonitorPollingStatus;

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public String getFftMonitorCmcMacIndex() {
        if (fftMacIndex != null) {
            fftMonitorCmcMacIndex = fftMacIndex.toString();
        }
        return fftMonitorCmcMacIndex;
    }

    public void setFftMonitorCmcMacIndex(String fftMonitorCmcMacIndex) {
        this.fftMonitorCmcMacIndex = fftMonitorCmcMacIndex;
    }

    public Integer getFftMonitorTimeInterval() {
        return fftMonitorTimeInterval;
    }

    public PhysAddress getFftMacIndex() {
        return fftMacIndex;
    }

    public void setFftMacIndex(PhysAddress fftMacIndex) {
        this.fftMacIndex = fftMacIndex;
    }

    public void setFftMonitorTimeInterval(Integer fftMonitorTimeInterval) {
        this.fftMonitorTimeInterval = fftMonitorTimeInterval;
    }

    public Integer getFftMonitorFreqInterval() {
        return fftMonitorFreqInterval;
    }

    public void setFftMonitorFreqInterval(Integer fftMonitorFreqInterval) {
        this.fftMonitorFreqInterval = fftMonitorFreqInterval;
    }

    public Integer getFftMonitorFreqStart() {
        return fftMonitorFreqStart;
    }

    public void setFftMonitorFreqStart(Integer fftMonitorFreqStart) {
        this.fftMonitorFreqStart = fftMonitorFreqStart;
    }

    public Integer getFftMonitorFreqEnd() {
        return fftMonitorFreqEnd;
    }

    public void setFftMonitorFreqEnd(Integer fftMonitorFreqEnd) {
        this.fftMonitorFreqEnd = fftMonitorFreqEnd;
    }

    public Integer getFftMonitorPollingStatus() {
        return fftMonitorPollingStatus;
    }

    public void setFftMonitorPollingStatus(Integer fftMonitorPollingStatus) {
        this.fftMonitorPollingStatus = fftMonitorPollingStatus;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SpectrumCfg [cmcId=");
        builder.append(cmcId);
        builder.append(", fftMacIndex=");
        builder.append(fftMacIndex);
        builder.append(", fftMonitorCmcMacIndex=");
        builder.append(fftMonitorCmcMacIndex);
        builder.append(", fftMonitorTimeInterval=");
        builder.append(fftMonitorTimeInterval);
        builder.append(", fftMonitorFreqInterval=");
        builder.append(fftMonitorFreqInterval);
        builder.append(", fftMonitorFreqStart=");
        builder.append(fftMonitorFreqStart);
        builder.append(", fftMonitorFreqEnd=");
        builder.append(fftMonitorFreqEnd);
        builder.append(", fftMonitorPollingStatus=");
        builder.append(fftMonitorPollingStatus);
        builder.append("]");
        return builder.toString();
    }

}
