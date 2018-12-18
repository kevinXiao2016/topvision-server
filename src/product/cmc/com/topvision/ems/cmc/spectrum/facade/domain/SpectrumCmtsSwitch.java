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
@Alias("spectrumCmtsSwitch")
public class SpectrumCmtsSwitch implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 6237988094835812387L;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.16.2.1.1", index = true)
    private PhysAddress fftMacIndex;// MIB用
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.16.2.1.6", writable = true, type = "Integer32")
    private Integer fftMonitorPollingStatus;

    private Long cmcId;
    //网管侧历史频谱开关
    private Integer hisVideoSwitch;
    //网管侧频谱采集开关
    private Integer collectSwitch;

    public PhysAddress getFftMacIndex() {
        return fftMacIndex;
    }

    public void setFftMacIndex(PhysAddress fftMacIndex) {
        this.fftMacIndex = fftMacIndex;
    }

    public Integer getFftMonitorPollingStatus() {
        return fftMonitorPollingStatus;
    }

    public void setFftMonitorPollingStatus(Integer fftMonitorPollingStatus) {
        this.fftMonitorPollingStatus = fftMonitorPollingStatus;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Integer getHisVideoSwitch() {
        return hisVideoSwitch;
    }

    public void setHisVideoSwitch(Integer hisVideoSwitch) {
        this.hisVideoSwitch = hisVideoSwitch;
    }

    public Integer getCollectSwitch() {
        return collectSwitch;
    }

    public void setCollectSwitch(Integer collectSwitch) {
        this.collectSwitch = collectSwitch;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SpectrumCmtsSwitch [fftMacIndex=");
        builder.append(fftMacIndex);
        builder.append(", fftMonitorPollingStatus=");
        builder.append(fftMonitorPollingStatus);
        builder.append(", cmcId=");
        builder.append(cmcId);
        builder.append(", hisVideoSwitch=");
        builder.append(hisVideoSwitch);
        builder.append(", collectSwitch=");
        builder.append(collectSwitch);
        builder.append("]");
        return builder.toString();
    }

}