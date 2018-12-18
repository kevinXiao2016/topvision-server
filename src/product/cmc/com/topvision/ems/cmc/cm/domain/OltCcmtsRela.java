/***********************************************************************
 * $Id: OltCcmtsRela.java,v1.0 2013-11-2 下午1:25:56 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.cm.domain;

import java.text.DecimalFormat;

/**
 * @author haojie
 * @created @2013-11-2-下午1:25:56
 * 
 */
public class OltCcmtsRela {
    private Double ponOutPower; // PON发送功率
    private String ponOutPowerForunit;
    private Double onuPonInPower;// ONU PON接收功率
    private String onuPonInPowerForunit;
    private String downAttenuation;// 下行衰减

    private Double onuPonOutPower;// ONU PON发送功率
    private String onuPonOutPowerForunit;
    private Double ponInPower; // PON接收功率
    private String ponInPowerForunit;
    private String upAttenuation; // 上行衰减

    DecimalFormat df = new DecimalFormat("#.00");

    public Double getPonOutPower() {
        return ponOutPower;
    }

    public void setPonOutPower(Double ponOutPower) {
        this.ponOutPower = Double.valueOf(df.format(ponOutPower));
    }

    public Double getOnuPonInPower() {
        return onuPonInPower;
    }

    public void setOnuPonInPower(Double onuPonInPower) {
        this.onuPonInPower = Double.valueOf(df.format(onuPonInPower));
    }

    public Double getOnuPonOutPower() {
        return onuPonOutPower;
    }

    public void setOnuPonOutPower(Double onuPonOutPower) {
        this.onuPonOutPower = Double.valueOf(df.format(onuPonOutPower));
    }

    public Double getPonInPower() {
        return ponInPower;
    }

    public void setPonInPower(Double ponInPower) {
        this.ponInPower = Double.valueOf(df.format(ponInPower));
    }

    public String getDownAttenuation() {
        if (ponOutPower != null && onuPonInPower != null) {
            Double value = ponOutPower - onuPonInPower;
            downAttenuation = df.format(value) + " dBm";
        }
        return downAttenuation;
    }

    public void setDownAttenuation(String downAttenuation) {
        this.downAttenuation = downAttenuation;
    }

    public String getUpAttenuation() {
        if (onuPonOutPower != null && ponInPower != null) {
            Double value = onuPonOutPower - ponInPower;
            upAttenuation = df.format(value) + " dBm";
        }
        return upAttenuation;
    }

    public void setUpAttenuation(String upAttenuation) {
        this.upAttenuation = upAttenuation;
    }

    public String getPonOutPowerForunit() {
        if (ponOutPower != null) {
            ponOutPowerForunit = df.format(ponOutPower) + " dBm";
        }
        return ponOutPowerForunit;
    }

    public void setPonOutPowerForunit(String ponOutPowerForunit) {
        this.ponOutPowerForunit = ponOutPowerForunit;
    }

    public String getOnuPonInPowerForunit() {
        if (onuPonInPower != null) {
            onuPonInPowerForunit = df.format(onuPonInPower) + " dBm";
        }
        return onuPonInPowerForunit;
    }

    public void setOnuPonInPowerForunit(String onuPonInPowerForunit) {
        this.onuPonInPowerForunit = onuPonInPowerForunit;
    }

    public String getOnuPonOutPowerForunit() {
        if (onuPonOutPower != null) {
            onuPonOutPowerForunit = df.format(onuPonOutPower) + " dBm";
        }
        return onuPonOutPowerForunit;
    }

    public void setOnuPonOutPowerForunit(String onuPonOutPowerForunit) {
        this.onuPonOutPowerForunit = onuPonOutPowerForunit;
    }

    public String getPonInPowerForunit() {
        if (ponInPower != null) {
            ponInPowerForunit = df.format(ponInPower) + " dBm";
        }
        return ponInPowerForunit;
    }

    public void setPonInPowerForunit(String ponInPowerForunit) {
        this.ponInPowerForunit = ponInPowerForunit;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OltCcmtsRela [ponOutPower=");
        builder.append(ponOutPower);
        builder.append(", ponOutPowerForunit=");
        builder.append(ponOutPowerForunit);
        builder.append(", onuPonInPower=");
        builder.append(onuPonInPower);
        builder.append(", onuPonInPowerForunit=");
        builder.append(onuPonInPowerForunit);
        builder.append(", downAttenuation=");
        builder.append(downAttenuation);
        builder.append(", onuPonOutPower=");
        builder.append(onuPonOutPower);
        builder.append(", onuPonOutPowerForunit=");
        builder.append(onuPonOutPowerForunit);
        builder.append(", ponInPower=");
        builder.append(ponInPower);
        builder.append(", ponInPowerForunit=");
        builder.append(ponInPowerForunit);
        builder.append(", upAttenuation=");
        builder.append(upAttenuation);
        builder.append("]");
        return builder.toString();
    }

}
