package com.topvision.ems.cmc.spectrum.facade.domain;

import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceDomain;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author bryan
 */
@Alias("cmcSpectrumII")
public class SpectrumII extends OperClass implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 8571259721660333279L;
    private long cmcId;
    private String macAddress;
    private Integer frequencyIndex;
    private String databuffer;
    private String cmcType;
    private Timestamp dt;
    private long x;
    private float y;

    public SpectrumII() {
        super(null, "spectrumIIPerfScheduler", "CC_CCSPECTRUM");
    }

    @Override
    public long getIdentifyKey() {
        return cmcId;
    }

    @Override
    public String[] makeOids() {
        return null;
    }

    @Override
    public PerformanceDomain[] makeObjects() {
        return null;
    }

    @Override
    public boolean isTaskCancle() {
        return false;
    }

    @Override
    public void shutdownTarget(String targetName, Object data) {
    }

    @Override
    public void startUpTarget(String targetName, Object data) {
    }

    @Override
    public void setIdentifyKey(Long identifyKey) {
    }

    public long getCmcId() {
        return cmcId;
    }

    public void setCmcId(long cmcId) {
        this.cmcId = cmcId;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public Integer getFrequencyIndex() {
        return frequencyIndex;
    }

    public void setFrequencyIndex(Integer frequencyIndex) {
        this.frequencyIndex = frequencyIndex;
    }

    public String getDatabuffer() {
        return databuffer;
    }

    public void setDatabuffer(String databuffer) {
        this.databuffer = databuffer;
    }

    public String getCmcType() {
        return cmcType;
    }

    public void setCmcType(String cmcType) {
        this.cmcType = cmcType;
    }

    public Timestamp getDt() {
        return dt;
    }

    public void setDt(Timestamp dt) {
        this.dt = dt;
    }

    public long getX() {
        return x;
    }

    public void setX(long x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcSpectrum [entityId=");
        builder.append(entityId);
        builder.append(", cmcId=");
        builder.append(cmcId);
        builder.append(", macAddress=");
        builder.append(macAddress);
        builder.append(", frequencyIndex=");
        builder.append(frequencyIndex);
        builder.append(", databuffer=");
        builder.append(databuffer);
        builder.append(", cmcType=");
        builder.append(cmcType);
        builder.append(", dt=");
        builder.append(dt);
        builder.append(", x=");
        builder.append(x);
        builder.append(", y=");
        builder.append(y);
        builder.append(", perfService=");
        builder.append(perfService);
        builder.append(", scheduler=");
        builder.append(scheduler);
        builder.append(", category=");
        builder.append(category);
        builder.append("]");
        return builder.toString();
    }
}
