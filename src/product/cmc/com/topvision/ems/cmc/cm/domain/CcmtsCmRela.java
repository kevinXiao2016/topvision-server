/***********************************************************************
 * $Id: CcmtsCmRela.java,v1.0 2013-11-2 下午1:26:40 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.cm.domain;

import java.text.DecimalFormat;

import com.topvision.platform.zetaframework.var.UnitConfigConstant;

/**
 * @author haojie
 * @created @2013-11-2-下午1:26:40
 * 
 */
public class CcmtsCmRela {
    private Long ccmtsOutPower;// CCMTS(CMTS)侧的发送电平,CmcDownChannelBaseShowInfo-docsIfDownChannelPower
    private String ccmtsOutPowerForunit;// 用于前台展示，带单位
    private String cmInPower;// CM侧的接收电平,单独做表了
    private String cmInPowerForunit;//
    private String downAttenuation; // 下行衰减

    private String cmOutPower;// CM侧的发送电平,单独做表了
    private String cmOutPowerForunit;
    private Long ccmtsInPower;// CCMTS(CMTS)侧的接收电平,cmattribute中的接收电平,1.3.6.1.2.1.10.127.1.3.3.1.6
    private String ccmtsInPowerForunit;
    private String upAttenuation;// 上行衰减

    DecimalFormat df = new DecimalFormat("#.00");

    public Long getCcmtsOutPower() {
        return ccmtsOutPower;
    }

    public void setCcmtsOutPower(Long ccmtsOutPower) {
        this.ccmtsOutPower = ccmtsOutPower;
    }

    public Long getCcmtsInPower() {
        return ccmtsInPower;
    }

    public void setCcmtsInPower(Long ccmtsInPower) {
        this.ccmtsInPower = ccmtsInPower;
    }

    public String getCmInPower() {
        return cmInPower;
    }

    public void setCmInPower(String cmInPower) {
        this.cmInPower = cmInPower;
    }

    public String getCmOutPower() {
        return cmOutPower;
    }

    public void setCmOutPower(String cmOutPower) {
        this.cmOutPower = cmOutPower;
    }

    public String getDownAttenuation() {
        if (ccmtsOutPower != null && cmInPower != null) {
            Double value = Double.parseDouble(ccmtsOutPower.toString()) / 10 - Double.parseDouble(cmInPower);
            this.downAttenuation = df.format(UnitConfigConstant.parsePowerValue(value)) + " "
                    + UnitConfigConstant.get("elecLevelUnit");
        }
        return downAttenuation;
    }

    public void setDownAttenuation(String downAttenuation) {
        this.downAttenuation = downAttenuation;
    }

    public String getUpAttenuation() {
        if (cmOutPower != null && ccmtsInPower != null) {
            Double value = Double.parseDouble(cmOutPower) - Double.parseDouble(ccmtsInPower.toString()) / 10;
            this.upAttenuation = df.format(UnitConfigConstant.parsePowerValue(value)) + " "
                    + UnitConfigConstant.get("elecLevelUnit");
        }
        return upAttenuation;
    }

    public void setUpAttenuation(String upAttenuation) {
        this.upAttenuation = upAttenuation;
    }

    public String getCcmtsOutPowerForunit() {
        if (ccmtsOutPower != null) {
            double powerValue = UnitConfigConstant.parsePowerValue((double) ccmtsOutPower / 10);
            this.ccmtsOutPowerForunit = df.format(powerValue) + " " + UnitConfigConstant.get("elecLevelUnit");
        }
        return ccmtsOutPowerForunit;
    }

    public void setCcmtsOutPowerForunit(String ccmtsOutPowerForunit) {
        this.ccmtsOutPowerForunit = ccmtsOutPowerForunit;
    }

    public String getCmInPowerForunit() {
        if (cmInPower != null) {
            double powerValue = UnitConfigConstant.parsePowerValue(Double.valueOf(cmInPower));
            this.cmInPowerForunit = df.format(powerValue) + " " + UnitConfigConstant.get("elecLevelUnit");
        }
        return cmInPowerForunit;
    }

    public void setCmInPowerForunit(String cmInPowerForunit) {
        this.cmInPowerForunit = cmInPowerForunit;
    }

    public String getCmOutPowerForunit() {
        if (cmOutPower != null) {
            double powerValue = UnitConfigConstant.parsePowerValue(Double.valueOf(cmOutPower));
            this.cmOutPowerForunit = df.format(powerValue) + " " + UnitConfigConstant.get("elecLevelUnit");
        }
        return cmOutPowerForunit;
    }

    public void setCmOutPowerForunit(String cmOutPowerForunit) {
        this.cmOutPowerForunit = cmOutPowerForunit;
    }

    public String getCcmtsInPowerForunit() {
        if (ccmtsInPower != null) {
            double powerValue = UnitConfigConstant.parsePowerValue((double) ccmtsInPower / 10);
            this.ccmtsInPowerForunit = df.format(powerValue) + " " + UnitConfigConstant.get("elecLevelUnit");
        }
        return ccmtsInPowerForunit;
    }

    public void setCcmtsInPowerForunit(String ccmtsInPowerForunit) {
        this.ccmtsInPowerForunit = ccmtsInPowerForunit;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CcmtsCmRela [ccmtsOutPower=");
        builder.append(ccmtsOutPower);
        builder.append(", ccmtsOutPowerForunit=");
        builder.append(ccmtsOutPowerForunit);
        builder.append(", cmInPower=");
        builder.append(cmInPower);
        builder.append(", cmInPowerForunit=");
        builder.append(cmInPowerForunit);
        builder.append(", downAttenuation=");
        builder.append(downAttenuation);
        builder.append(", cmOutPower=");
        builder.append(cmOutPower);
        builder.append(", cmOutPowerForunit=");
        builder.append(cmOutPowerForunit);
        builder.append(", ccmtsInPower=");
        builder.append(ccmtsInPower);
        builder.append(", ccmtsInPowerForunit=");
        builder.append(ccmtsInPowerForunit);
        builder.append(", upAttenuation=");
        builder.append(upAttenuation);
        builder.append("]");
        return builder.toString();
    }

}
