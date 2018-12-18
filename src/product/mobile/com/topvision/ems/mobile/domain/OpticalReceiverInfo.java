package com.topvision.ems.mobile.domain;

public class OpticalReceiverInfo {
    private Integer receiverPowerA;// 接收光功率A
    private Integer receiverPowerB;// 接收光功率B
    private Integer switchControl;// 光输入切换
    private Integer switchThres;// 转换门限
    private Integer dcPower1;// 直流电源(5V)
    private Integer dcPower2;// 直流电源(12V)
    private Integer dcPower3;// 直流电源(24V)
    private Integer acPower;// 交流电源
    private Integer outputControl;// 下行链路开关
    private Integer rfOutput1; // 射频输出1
    private Integer rfOutput2; // 射频输出2
    private Integer rfOutput3; // 射频输出3
    private Integer rfOutput4; // 射频输出4
    private Integer outputGainType;// 增益类型
    private Integer outputAGCOrigin; // AGC起控光功率
    private Integer outputRFlevelatt; // 射频衰减量
    private Integer channelNum;// 载波频道数

    public Integer getReceiverPowerA() {
        return receiverPowerA;
    }

    public Integer getReceiverPowerB() {
        return receiverPowerB;
    }

    public Integer getSwitchControl() {
        return switchControl;
    }

    public Integer getSwitchThres() {
        return switchThres;
    }

    public Integer getDcPower1() {
        return dcPower1;
    }

    public Integer getDcPower2() {
        return dcPower2;
    }

    public Integer getDcPower3() {
        return dcPower3;
    }

    public Integer getAcPower() {
        return acPower;
    }

    public Integer getOutputControl() {
        return outputControl;
    }

    public Integer getRfOutput1() {
        return rfOutput1;
    }

    public Integer getRfOutput2() {
        return rfOutput2;
    }

    public Integer getRfOutput3() {
        return rfOutput3;
    }

    public Integer getRfOutput4() {
        return rfOutput4;
    }

    public Integer getOutputGainType() {
        return outputGainType;
    }

    public Integer getOutputAGCOrigin() {
        return outputAGCOrigin;
    }

    public Integer getOutputRFlevelatt() {
        return outputRFlevelatt;
    }

    public Integer getChannelNum() {
        return channelNum;
    }

    public void setReceiverPowerA(Integer receiverPowerA) {
        this.receiverPowerA = receiverPowerA;
    }

    public void setReceiverPowerB(Integer receiverPowerB) {
        this.receiverPowerB = receiverPowerB;
    }

    public void setSwitchControl(Integer switchControl) {
        this.switchControl = switchControl;
    }

    public void setSwitchThres(Integer switchThres) {
        this.switchThres = switchThres;
    }

    public void setDcPower1(Integer dcPower1) {
        this.dcPower1 = dcPower1;
    }

    public void setDcPower2(Integer dcPower2) {
        this.dcPower2 = dcPower2;
    }

    public void setDcPower3(Integer dcPower3) {
        this.dcPower3 = dcPower3;
    }

    public void setAcPower(Integer acPower) {
        this.acPower = acPower;
    }

    public void setOutputControl(Integer outputControl) {
        this.outputControl = outputControl;
    }

    public void setRfOutput1(Integer rfOutput1) {
        this.rfOutput1 = rfOutput1;
    }

    public void setRfOutput2(Integer rfOutput2) {
        this.rfOutput2 = rfOutput2;
    }

    public void setRfOutput3(Integer rfOutput3) {
        this.rfOutput3 = rfOutput3;
    }

    public void setRfOutput4(Integer rfOutput4) {
        this.rfOutput4 = rfOutput4;
    }

    public void setOutputGainType(Integer outputGainType) {
        this.outputGainType = outputGainType;
    }

    public void setOutputAGCOrigin(Integer outputAGCOrigin) {
        this.outputAGCOrigin = outputAGCOrigin;
    }

    public void setOutputRFlevelatt(Integer outputRFlevelatt) {
        this.outputRFlevelatt = outputRFlevelatt;
    }

    public void setChannelNum(Integer channelNum) {
        this.channelNum = channelNum;
    }

}
