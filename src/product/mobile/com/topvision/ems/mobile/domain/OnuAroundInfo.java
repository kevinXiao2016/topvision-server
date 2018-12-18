/***********************************************************************
 * $Id: OnuAroundInfo.java,v1.0 2017年7月17日 下午7:07:30 $
 * 
 * @author: ls
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.mobile.domain;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;

import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.platform.zetaframework.var.UnitConfigConstant;

/**
 * @author ls
 * @created @2017年7月17日-下午7:07:30
 *
 */
public class OnuAroundInfo implements Serializable, AliasesSuperType {

    private static final long serialVersionUID = -1908439281923872302L;
    private static final DecimalFormat df = new DecimalFormat("0.0");
    // 位置信息
    private Long onuId;// ONUId
    private Float latitude;// 纬度
    private Float longitude;// 经度
    private String uniqueId;//mac|sn
    private Long typeId;
    // 健康度指标
    private Float onuPonRevPower;// ONU光口接收功率
    private Float onuPonTransPower;// ONU光口发送功率
    private Float oltPonRevPower;// OLTpon口接收功率
    private Float onuCatvOrInfoRxPower;// catv接收功率
    private Float onuCatvOrInfoRfOutVoltage;// catv电平
    private Float onuCatvOrInfoVoltage;// catv电压
    private Float onuCatvOrInfoTemperature;// catv温度
    private Integer onuHealthyFlag;//健康等级，如果没有阈值超标则为0
    private List<String> healthyContent;//超标值
    private Integer healthyTarget;//超标指标
    
    private String onuPonRevPowerForunit;
    private String onuPonTransPowerForunit;
    private String oltPonRevPowerForunit;
    private String catvRxPowerForunit;
    private String catvRfOutVoltageForunit;
    private String catvVoltageForunit;
    private String catvTemperatureForunit; 

    public String getOnuPonRevPowerForunit() {
        if (onuPonRevPower != null) {
            onuPonRevPowerForunit = df.format(Double.parseDouble(onuPonRevPower.toString()));
        } else {
            onuPonRevPowerForunit = "--";
        }
        return onuPonRevPowerForunit;
    }

    public void setOnuPonRevPowerForunit(String onuPonRevPowerForunit) {
        this.onuPonRevPowerForunit = onuPonRevPowerForunit;
    }

    public String getOnuPonTransPowerForunit() {
        if (onuPonTransPower != null) {
            onuPonTransPowerForunit = df.format(Double.parseDouble(onuPonTransPower.toString()));
        } else {
            onuPonTransPowerForunit = "--";
        }
        return onuPonTransPowerForunit;
    }

    public void setOnuPonTransPowerForunit(String onuPonTransPowerForunit) {
        this.onuPonTransPowerForunit = onuPonTransPowerForunit;
    }

    public String getOltPonRevPowerForunit() {
        if (oltPonRevPower != null) {
            oltPonRevPowerForunit = df.format(Double.parseDouble(oltPonRevPower.toString()));
        } else {
            oltPonRevPowerForunit = "--";
        }
        return oltPonRevPowerForunit;
    }

    public void setOltPonRevPowerForunit(String oltPonRevPowerForunit) {
        this.oltPonRevPowerForunit = oltPonRevPowerForunit;
    }

    public String getCatvRxPowerForunit() {
        if (onuCatvOrInfoRxPower != null) {
            catvRxPowerForunit = df.format(Double.parseDouble(onuCatvOrInfoRxPower.toString()) / 10);
        } else {
            catvRxPowerForunit = "--";
        }
        return catvRxPowerForunit;
    }

    public void setCatvRxPowerForunit(String catvRxPowerForunit) {
        this.catvRxPowerForunit = catvRxPowerForunit;
    }

    public String getCatvRfOutVoltageForunit() {
        String powerUnit = (String) UnitConfigConstant.get(UnitConfigConstant.POWER_UNIT);
        if (onuCatvOrInfoRfOutVoltage != null&&onuCatvOrInfoRfOutVoltage>-200) {
            Double rfOutVoltage;
            if (powerUnit.equalsIgnoreCase(UnitConfigConstant.MICRO_VOLT_UNIT)) {
                rfOutVoltage = Double.parseDouble(onuCatvOrInfoRfOutVoltage.toString()) / 10;
            } else {
                rfOutVoltage = UnitConfigConstant.transDBμVToDBmV(Double.parseDouble(onuCatvOrInfoRfOutVoltage.toString()) / 10);
            }
            catvRfOutVoltageForunit =df.format(rfOutVoltage);
        } else {
            catvRfOutVoltageForunit = "--";
        }
        return catvRfOutVoltageForunit;
    }

    public void setCatvRfOutVoltageForunit(String catvRfOutVoltageForunit) {
        this.catvRfOutVoltageForunit = catvRfOutVoltageForunit;
    }

    public String getCatvVoltageForunit() {
        if (onuCatvOrInfoVoltage != null) {
            catvVoltageForunit = df.format(Double.parseDouble(onuCatvOrInfoVoltage.toString()) / 10);
        } else {
            catvVoltageForunit = "--";
        }
        return catvVoltageForunit;
    }

    public void setCatvVoltageForunit(String catvVoltageForunit) {
        this.catvVoltageForunit = catvVoltageForunit;
    }

    public String getCatvTemperatureForunit() {        
        String tempUnit = (String) UnitConfigConstant.get(UnitConfigConstant.TEMP_UNIT);
        if (onuCatvOrInfoTemperature != null) {
            Double temperature;
            if (tempUnit.equalsIgnoreCase(UnitConfigConstant.CENTI_TEMP_UNIT)) {
                temperature = Double.parseDouble(onuCatvOrInfoTemperature.toString()) / 10;
            } else {
                temperature = UnitConfigConstant.transCentiToF(Double.parseDouble(onuCatvOrInfoTemperature.toString()) / 10);
            }
            catvTemperatureForunit =df.format(temperature);
        } else {
            catvTemperatureForunit = "--";
        }
        return catvTemperatureForunit;
    }

    public void setCatvTemperatureForunit(String catvTemperatureForunit) {
        this.catvTemperatureForunit = catvTemperatureForunit;
    }

    public Long getOnuId() {
        return onuId;
    }

    public void setOnuId(Long onuId) {
        this.onuId = onuId;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public Float getOnuPonRevPower() {
        return onuPonRevPower;
    }

    public void setOnuPonRevPower(Float onuPonRevPower) {
        this.onuPonRevPower = onuPonRevPower;
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

    public void setOltPonRevPower(Float oltPonRevPower) {
        this.oltPonRevPower = oltPonRevPower;
    }

    public Float getOnuCatvOrInfoRxPower() {
        return onuCatvOrInfoRxPower;
    }

    public void setOnuCatvOrInfoRxPower(Float onuCatvOrInfoRxPower) {
        this.onuCatvOrInfoRxPower = onuCatvOrInfoRxPower;
    }

    public Float getOnuCatvOrInfoRfOutVoltage() {
        return onuCatvOrInfoRfOutVoltage;
    }

    public void setOnuCatvOrInfoRfOutVoltage(Float onuCatvOrInfoRfOutVoltage) {
        this.onuCatvOrInfoRfOutVoltage = onuCatvOrInfoRfOutVoltage;
    }

    public Float getOnuCatvOrInfoVoltage() {
        return onuCatvOrInfoVoltage;
    }

    public void setOnuCatvOrInfoVoltage(Float onuCatvOrInfoVoltage) {
        this.onuCatvOrInfoVoltage = onuCatvOrInfoVoltage;
    }

    public Float getOnuCatvOrInfoTemperature() {
        return onuCatvOrInfoTemperature;
    }

    public void setOnuCatvOrInfoTemperature(Float onuCatvOrInfoTemperature) {
        this.onuCatvOrInfoTemperature = onuCatvOrInfoTemperature;
    }

    public Integer getOnuHealthyFlag() {
        return onuHealthyFlag;
    }

    public void setOnuHealthyFlag(Integer onuHealthyFlag) {
        this.onuHealthyFlag = onuHealthyFlag;
    }

    public List<String> getHealthyContent() {
        return healthyContent;
    }

    public void setHealthyContent(List<String> healthyContent) {
        this.healthyContent = healthyContent;
    }

    public Integer getHealthyTarget() {
        return healthyTarget;
    }

    public void setHealthyTarget(Integer healthyTarget) {
        this.healthyTarget = healthyTarget;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

}
