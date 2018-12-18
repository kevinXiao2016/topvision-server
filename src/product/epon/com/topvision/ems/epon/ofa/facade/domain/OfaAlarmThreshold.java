package com.topvision.ems.epon.ofa.facade.domain;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * OFA告警阈值表目
 * 
 * @author w1992wishes
 * @created @2017年10月13日-上午9:48:23
 *
 */
@Alias("ofaAlarmThreshold")
public class OfaAlarmThreshold implements AliasesSuperType {

    private static final long serialVersionUID = 5319384445382781787L;

    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.9.2.1.1", index = true)
    private Integer alarmThresholdIndex = 1;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.9.2.1.2", writable = true, type = "Integer32")
    private Integer inputAlarmUp;// OFA输入光功率告警阈值上限
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.9.2.1.3", writable = true, type = "Integer32")
    private Integer inputAlarmLow;// OFA 输入光功率告警阈值下限
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.9.2.1.4", writable = true, type = "Integer32")
    private Integer outputAlarmUp;// OFA输出光功率告警阈值上限
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.9.2.1.5", writable = true, type = "Integer32")
    private Integer outputAlarmLow;// OFA 输出光功率告警阈值下限
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.9.2.1.6", writable = true, type = "Integer32")
    private Integer pump1BiasAlarmUp;// OFA泵浦1偏置电流告警阈值上限
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.9.2.1.7", writable = true, type = "Integer32")
    private Integer pump1BiasAlarmLow;// OFA泵浦1偏置电流告警阈值下限
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.9.2.1.8", writable = true, type = "Integer32")
    private Integer pump1TempAlarmUp; // OFA泵浦1温度告警阈值上限
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.9.2.1.9", writable = true, type = "Integer32")
    private Integer pump1TempAlarmLow; // OFA泵浦1温度告警阈值下限
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.9.2.1.10", writable = true, type = "Integer32")
    private Integer pump1TecAlarmUp; // OFA泵浦1温控电流告警阈值上限
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.9.2.1.11", writable = true, type = "Integer32")
    private Integer pump1TecAlarmLow; // OFA泵浦1温控电流告警阈值下限
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.9.2.1.12", writable = true, type = "Integer32")
    private Integer pump2BiasAlarmUp; // OFA泵浦2偏置电流告警阈值上限
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.9.2.1.13", writable = true, type = "Integer32")
    private Integer pump2BiasAlarmLow; // OFA泵浦2偏置电流告警阈值下限
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.9.2.1.14", writable = true, type = "Integer32")
    private Integer pump2TempAlarmUp; // OFA泵浦2温度告警阈值上限
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.9.2.1.15", writable = true, type = "Integer32")
    private Integer pump2TempAlarmLow; // OFA泵浦2温度告警阈值下限
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.9.2.1.16", writable = true, type = "Integer32")
    private Integer voltage5AlarmUp; // OFA 5V电源电压告警阈值上限
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.9.2.1.17", writable = true, type = "Integer32")
    private Integer voltage5AlarmLow; // OFA 5V电源电压告警阈值下限
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.9.2.1.18", writable = true, type = "Integer32")
    private Integer voltage12AlarmUp; // OFA 12V电源电压告警阈值上限
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.9.2.1.19", writable = true, type = "Integer32")
    private Integer voltage12AlarmLow; // OFA 12V电源电压告警阈值下限

    public OfaAlarmThreshold() {
    }

    public Long getEntityId() {
        return entityId;
    }

    public Integer getAlarmThresholdIndex() {
        return alarmThresholdIndex;
    }

    public Integer getInputAlarmUp() {
        return inputAlarmUp;
    }

    public Integer getInputAlarmLow() {
        return inputAlarmLow;
    }

    public Integer getOutputAlarmUp() {
        return outputAlarmUp;
    }

    public Integer getOutputAlarmLow() {
        return outputAlarmLow;
    }

    public Integer getPump1BiasAlarmUp() {
        return pump1BiasAlarmUp;
    }

    public Integer getPump1BiasAlarmLow() {
        return pump1BiasAlarmLow;
    }

    public Integer getPump1TempAlarmUp() {
        return pump1TempAlarmUp;
    }

    public Integer getPump1TempAlarmLow() {
        return pump1TempAlarmLow;
    }

    public Integer getPump1TecAlarmUp() {
        return pump1TecAlarmUp;
    }

    public Integer getPump1TecAlarmLow() {
        return pump1TecAlarmLow;
    }

    public Integer getPump2BiasAlarmUp() {
        return pump2BiasAlarmUp;
    }

    public Integer getPump2BiasAlarmLow() {
        return pump2BiasAlarmLow;
    }

    public Integer getPump2TempAlarmUp() {
        return pump2TempAlarmUp;
    }

    public Integer getPump2TempAlarmLow() {
        return pump2TempAlarmLow;
    }

    public Integer getVoltage5AlarmUp() {
        return voltage5AlarmUp;
    }

    public Integer getVoltage5AlarmLow() {
        return voltage5AlarmLow;
    }

    public Integer getVoltage12AlarmUp() {
        return voltage12AlarmUp;
    }

    public Integer getVoltage12AlarmLow() {
        return voltage12AlarmLow;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public void setAlarmThresholdIndex(Integer alarmThresholdIndex) {
        this.alarmThresholdIndex = alarmThresholdIndex;
    }

    public void setInputAlarmUp(Integer inputAlarmUp) {
        this.inputAlarmUp = inputAlarmUp;
    }

    public void setInputAlarmLow(Integer inputAlarmLow) {
        this.inputAlarmLow = inputAlarmLow;
    }

    public void setOutputAlarmUp(Integer outputAlarmUp) {
        this.outputAlarmUp = outputAlarmUp;
    }

    public void setOutputAlarmLow(Integer outputAlarmLow) {
        this.outputAlarmLow = outputAlarmLow;
    }

    public void setPump1BiasAlarmUp(Integer pump1BiasAlarmUp) {
        this.pump1BiasAlarmUp = pump1BiasAlarmUp;
    }

    public void setPump1BiasAlarmLow(Integer pump1BiasAlarmLow) {
        this.pump1BiasAlarmLow = pump1BiasAlarmLow;
    }

    public void setPump1TempAlarmUp(Integer pump1TempAlarmUp) {
        this.pump1TempAlarmUp = pump1TempAlarmUp;
    }

    public void setPump1TempAlarmLow(Integer pump1TempAlarmLow) {
        this.pump1TempAlarmLow = pump1TempAlarmLow;
    }

    public void setPump1TecAlarmUp(Integer pump1TecAlarmUp) {
        this.pump1TecAlarmUp = pump1TecAlarmUp;
    }

    public void setPump1TecAlarmLow(Integer pump1TecAlarmLow) {
        this.pump1TecAlarmLow = pump1TecAlarmLow;
    }

    public void setPump2BiasAlarmUp(Integer pump2BiasAlarmUp) {
        this.pump2BiasAlarmUp = pump2BiasAlarmUp;
    }

    public void setPump2BiasAlarmLow(Integer pump2BiasAlarmLow) {
        this.pump2BiasAlarmLow = pump2BiasAlarmLow;
    }

    public void setPump2TempAlarmUp(Integer pump2TempAlarmUp) {
        this.pump2TempAlarmUp = pump2TempAlarmUp;
    }

    public void setPump2TempAlarmLow(Integer pump2TempAlarmLow) {
        this.pump2TempAlarmLow = pump2TempAlarmLow;
    }

    public void setVoltage5AlarmUp(Integer voltage5AlarmUp) {
        this.voltage5AlarmUp = voltage5AlarmUp;
    }

    public void setVoltage5AlarmLow(Integer voltage5AlarmLow) {
        this.voltage5AlarmLow = voltage5AlarmLow;
    }

    public void setVoltage12AlarmUp(Integer voltage12AlarmUp) {
        this.voltage12AlarmUp = voltage12AlarmUp;
    }

    public void setVoltage12AlarmLow(Integer voltage12AlarmLow) {
        this.voltage12AlarmLow = voltage12AlarmLow;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OfaAlarmThreshold [entityId=");
        builder.append(entityId);
        builder.append(", alarmThresholdIndex=");
        builder.append(alarmThresholdIndex);
        builder.append(", inputAlarmUp=");
        builder.append(inputAlarmUp);
        builder.append(", inputAlarmLow=");
        builder.append(inputAlarmLow);
        builder.append(", outputAlarmUp=");
        builder.append(outputAlarmUp);
        builder.append(", outputAlarmLow=");
        builder.append(outputAlarmLow);
        builder.append(", pump1BiasAlarmUp=");
        builder.append(pump1BiasAlarmUp);
        builder.append(", pump1BiasAlarmLow=");
        builder.append(pump1BiasAlarmLow);
        builder.append(", pump1TempAlarmUp=");
        builder.append(pump1TempAlarmUp);
        builder.append(", pump1TempAlarmLow=");
        builder.append(pump1TempAlarmLow);
        builder.append(", pump1TecAlarmUp=");
        builder.append(pump1TecAlarmUp);
        builder.append(", pump1TecAlarmLow=");
        builder.append(pump1TecAlarmLow);
        builder.append(", pump2BiasAlarmUp=");
        builder.append(pump2BiasAlarmUp);
        builder.append(", pump2BiasAlarmLow=");
        builder.append(pump2BiasAlarmLow);
        builder.append(", pump2TempAlarmUp=");
        builder.append(pump2TempAlarmUp);
        builder.append(", pump2TempAlarmLow=");
        builder.append(pump2TempAlarmLow);
        builder.append(", voltage5AlarmUp=");
        builder.append(voltage5AlarmUp);
        builder.append(", voltage5AlarmLow=");
        builder.append(voltage5AlarmLow);
        builder.append(", voltage12AlarmUp=");
        builder.append(voltage12AlarmUp);
        builder.append(", voltage12AlarmLow=");
        builder.append(voltage12AlarmLow);
        return builder.toString();
    }
}
