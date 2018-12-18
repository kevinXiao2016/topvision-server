package com.topvision.ems.epon.onu.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponConstants;

/**
 * 
 * @author CWQ
 * @created @2017年12月21日-下午3:25:41
 *
 */
public class OnuLinkInfo extends OnuCommonInfo implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 6379849069184519319L;

    private String onuRunTime;// 在线时长
    private String location;// 位置
    private Integer laserSwitch;// 激光器开关
    private Float onuPonRevPower;// 光接收功率
    private Float minOnuPonRevPower;// 最低光接收功率
    private String minPowerTimeStr;// 最低光接收功率对应的时间（有多个，取24小时内的最新值）
    private Float onuPonTransPower;// 光发送功率
    private Float oltPonRevPower;// OLT PON接收功率
    private Double oltPonTransPower;// OLT PON发送功率(此数据表中没有找到)
    private Float onuCatvOrInfoRxPower;// CATV光接收功率
    private Float minCatvRevPower;// CATV最低光接收功率
    private String minCatvTimeStr;// 最低光接收功率对应的时间（有多个，取24小时内的最新值）

    private Timestamp changeTime;
    private Long onuTimeSinceLastRegister;
    private Timestamp lastDeregisterTime;
    private String typeName;
    private Integer onuTestDistance;
    private Integer onuDeactive;

    public String getOnuRunTime() {
        return onuRunTime;
    }

    public void setOnuRunTime(String onuRunTime) {
        this.onuRunTime = onuRunTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getLaserSwitch() {
        return laserSwitch;
    }

    public void setLaserSwitch(Integer laserSwitch) {
        this.laserSwitch = laserSwitch;
    }

    public Float getOnuPonRevPower() {
        return onuPonRevPower;
    }

    public void setOnuPonRevPower(Float onuPonRevPower) {
        this.onuPonRevPower = onuPonRevPower;
    }

    public Float getMinOnuPonRevPower() {
        return minOnuPonRevPower;
    }

    public void setMinOnuPonRevPower(Float minOnuPonRevPower) {
        this.minOnuPonRevPower = minOnuPonRevPower;
    }

    public String getMinPowerTimeStr() {
        return minPowerTimeStr;
    }

    public void setMinPowerTimeStr(String minPowerTimeStr) {
        this.minPowerTimeStr = minPowerTimeStr;
    }

    public Float getOnuPonTransPower() {
        return onuPonTransPower;
    }

    public void setOnuPonTransPower(Float onuPonTransPower) {
        this.onuPonTransPower = onuPonTransPower;
    }

    public Float getOltPonRevPower() {
        return oltPonRevPower;
    }

    public Double getOltPonTransPower() {
        return oltPonTransPower;
    }

    public void setOltPonTransPower(Double oltPonTransPower) {
        if (EponConstants.TX_POWER != oltPonTransPower) {
            this.oltPonTransPower = oltPonTransPower;
            if (oltPonTransPower != null && oltPonTransPower > 0) {
                this.oltPonTransPower = 10 * Math.log10(oltPonTransPower) - 40;
            } else {
                this.oltPonTransPower = 0.0;
            }
        } else {
            this.oltPonTransPower = null;
        }
    }

    public Float getOnuCatvOrInfoRxPower() {
        return onuCatvOrInfoRxPower;
    }

    public void setOnuCatvOrInfoRxPower(Float onuCatvOrInfoRxPower) {
        this.onuCatvOrInfoRxPower = onuCatvOrInfoRxPower;
    }

    public Float getMinCatvRevPower() {
        return minCatvRevPower;
    }

    public void setMinCatvRevPower(Float minCatvRevPower) {
        this.minCatvRevPower = minCatvRevPower;
    }

    public String getMinCatvTimeStr() {
        return minCatvTimeStr;
    }

    public void setMinCatvTimeStr(String minCatvTimeStr) {
        this.minCatvTimeStr = minCatvTimeStr;
    }

    public Timestamp getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(Timestamp changeTime) {
        this.changeTime = changeTime;
    }

    public Long getOnuTimeSinceLastRegister() {
        return onuTimeSinceLastRegister;
    }

    public void setOnuTimeSinceLastRegister(Long onuTimeSinceLastRegister) {
        this.onuTimeSinceLastRegister = onuTimeSinceLastRegister;
    }

    public Timestamp getLastDeregisterTime() {
        return lastDeregisterTime;
    }

    public void setLastDeregisterTime(Timestamp lastDeregisterTime) {
        this.lastDeregisterTime = lastDeregisterTime;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Integer getOnuTestDistance() {
        return onuTestDistance;
    }

    public void setOnuTestDistance(Integer onuTestDistance) {
        this.onuTestDistance = onuTestDistance;
    }

    public void setOltPonRevPower(Float oltPonRevPower) {
        this.oltPonRevPower = oltPonRevPower;
    }

    public Integer getOnuDeactive() {
		return onuDeactive;
	}

	public void setOnuDeactive(Integer onuDeactive) {
		this.onuDeactive = onuDeactive;
	}

	@Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OnuLinkInfo [onuId=");
        builder.append(onuId);
        builder.append(", onuIndex=");
        builder.append(onuIndex);
        builder.append(", entityId=");
        builder.append(entityId);
        builder.append(", entityIp=");
        builder.append(entityIp);
        builder.append(", name=");
        builder.append(name);
        builder.append(", onuUniqueIdentification=");
        builder.append(onuUniqueIdentification);
        builder.append(", onuOperationStatus=");
        builder.append(onuOperationStatus);
        builder.append(", onuRunTime=");
        builder.append(onuRunTime);
        builder.append(", onuTestDistance=");
        builder.append(onuTestDistance);
        builder.append(", manageName=");
        builder.append(manageName);
        builder.append(", location=");
        builder.append(location);
        builder.append(", onuPonRevPower=");
        builder.append(onuPonRevPower);
        builder.append(", onuPonTransPower=");
        builder.append(onuPonTransPower);
        builder.append(", oltPonRevPower=");
        builder.append(oltPonRevPower);
        builder.append(", oltPonTransPower=");
        builder.append(oltPonTransPower);

        builder.append(", onuCatvOrInfoRxPower=");
        builder.append(onuCatvOrInfoRxPower);
        builder.append(", minCatvRevPower=");
        builder.append(minCatvRevPower);
        builder.append(", minCatvTimeStr=");
        builder.append(minCatvTimeStr);

        builder.append(", location=");
        builder.append(location);
        builder.append(", onuPreType=");
        builder.append(onuPreType);
        builder.append(", typeName=");
        builder.append(typeName);
        builder.append(", onuMac=");
        builder.append(onuMac);
        builder.append(", typeId=");
        builder.append(typeId);
        builder.append(", onuEorG=");
        builder.append(onuEorG);
        builder.append(", tagName=");
        builder.append(tagName);
        builder.append(", tagId=");
        builder.append(tagId);
        builder.append(", onuTimeSinceLastRegister=");
        builder.append(onuTimeSinceLastRegister);
        builder.append(", lastDeregisterTime=");
        builder.append(lastDeregisterTime);
        builder.append(", changeTime=");
        builder.append(changeTime);
        builder.append(", onuDeactive=");
        builder.append(onuDeactive);
        builder.append("]");
        return builder.toString();
    }
}
