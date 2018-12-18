package com.topvision.ems.epon.ofa.facade.domain;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * OFA基本信息
 * 
 * @author CWQ
 * @created @2017年10月13日-下午4:22:52
 *
 */
@Alias("ofaBasicInfo")
public class OfaBasicInfo implements AliasesSuperType {

    private static final long serialVersionUID = 5319384445382781787L;

    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.9.1.1.1", index = true)
    private Long deviceIndex = 1L;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.9.1.1.2")
    private String platSN;// 平台序列号
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.9.1.1.3")
    private String hWVer;// 硬件版本号
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.9.1.1.4")
    private String sWVer;// 软件版本号
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.9.1.1.5")
    private String moduleType;// 模块型号
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.9.1.1.6")
    private Integer inputPower;// 输入光功率
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.9.1.1.7")
    private Integer outputPower;// 输出光功率
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.9.1.1.8")
    private Integer pump1BiasCurr;// 泵浦1偏置电流
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.9.1.1.9")
    private Integer pump1Temp;// 泵浦1温度
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.9.1.1.10")
    private Integer pump1Tec;// 泵浦1温控电流
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.9.1.1.11")
    private Integer pump2BiasCurr;// 泵浦2偏置电流
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.9.1.1.12")
    private Integer pump2Temp;// 泵浦2温度
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.9.1.1.13")
    private Integer pump2Tec;// 泵浦2温控电流
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.9.1.1.14")
    private Integer voltage5v;// 5V OFA电源电压
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.9.1.1.15")
    private Integer voltage12v;// 12V OFA电源电压
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.9.1.1.16")
    private Integer systemTemp;// 系统温度
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.9.1.1.17", writable = true, type = "Integer32")
    private Integer outputAtt;// 输出衰减

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getDeviceIndex() {
        return deviceIndex;
    }

    public void setDeviceIndex(Long deviceIndex) {
        this.deviceIndex = deviceIndex;
    }

    public String getPlatSN() {
        return platSN;
    }

    public void setPlatSN(String platSN) {
        this.platSN = platSN;
    }

    public String gethWVer() {
        return hWVer;
    }

    public void sethWVer(String hWVer) {
        this.hWVer = hWVer;
    }

    public String getsWVer() {
        return sWVer;
    }

    public void setsWVer(String sWVer) {
        this.sWVer = sWVer;
    }

    public String getModuleType() {
        return moduleType;
    }

    public void setModuleType(String moduleType) {
        this.moduleType = moduleType;
    }

    public Integer getInputPower() {
        return inputPower;
    }

    public void setInputPower(Integer inputPower) {
        this.inputPower = inputPower;
    }

    public Integer getOutputPower() {
        return outputPower;
    }

    public void setOutputPower(Integer outputPower) {
        this.outputPower = outputPower;
    }

    public Integer getPump1BiasCurr() {
        return pump1BiasCurr;
    }

    public void setPump1BiasCurr(Integer pump1BiasCurr) {
        this.pump1BiasCurr = pump1BiasCurr;
    }

    public Integer getPump1Temp() {
        return pump1Temp;
    }

    public void setPump1Temp(Integer pump1Temp) {
        this.pump1Temp = pump1Temp;
    }

    public Integer getPump1Tec() {
        return pump1Tec;
    }

    public void setPump1Tec(Integer pump1Tec) {
        this.pump1Tec = pump1Tec;
    }

    public Integer getPump2BiasCurr() {
        return pump2BiasCurr;
    }

    public void setPump2BiasCurr(Integer pump2BiasCurr) {
        this.pump2BiasCurr = pump2BiasCurr;
    }

    public Integer getPump2Temp() {
        return pump2Temp;
    }

    public void setPump2Temp(Integer pump2Temp) {
        this.pump2Temp = pump2Temp;
    }

    public Integer getPump2Tec() {
        return pump2Tec;
    }

    public void setPump2Tec(Integer pump2Tec) {
        this.pump2Tec = pump2Tec;
    }

    public Integer getVoltage5v() {
        return voltage5v;
    }

    public void setVoltage5v(Integer voltage5v) {
        this.voltage5v = voltage5v;
    }

    public Integer getVoltage12v() {
        return voltage12v;
    }

    public void setVoltage12v(Integer voltage12v) {
        this.voltage12v = voltage12v;
    }

    public Integer getSystemTemp() {
        return systemTemp;
    }

    public void setSystemTemp(Integer systemTemp) {
        this.systemTemp = systemTemp;
    }

    public Integer getOutputAtt() {
        return outputAtt;
    }

    public void setOutputAtt(Integer outputAtt) {
        this.outputAtt = outputAtt;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("TopOFABasicInfoEntry [entityId=");
        builder.append(entityId);
        builder.append(", deviceIndex=");
        builder.append(deviceIndex);
        builder.append(", platSN=");
        builder.append(platSN);
        builder.append(", hWVer=");
        builder.append(hWVer);
        builder.append(", sWVer=");
        builder.append(sWVer);
        builder.append(", moduleType=");
        builder.append(moduleType);
        builder.append(", inputPower=");
        builder.append(inputPower);
        builder.append(", outputPower=");
        builder.append(outputPower);
        builder.append(", pump1BiasCurr=");
        builder.append(pump1BiasCurr);
        builder.append(", pump1Temp=");
        builder.append(pump1Temp);
        builder.append(", pump1Tec=");
        builder.append(pump1Tec);
        builder.append(", pump2BiasCurr=");
        builder.append(pump2BiasCurr);
        builder.append(", pump2Temp=");
        builder.append(pump2Temp);
        builder.append(", pump2Tec=");
        builder.append(pump2Tec);
        builder.append(", voltage5v=");
        builder.append(voltage5v);
        builder.append(", voltage12v=");
        builder.append(voltage12v);
        builder.append(", systemTemp=");
        builder.append(systemTemp);
        builder.append(", outputAtt=");
        builder.append(outputAtt);
        return builder.toString();
    }
}
