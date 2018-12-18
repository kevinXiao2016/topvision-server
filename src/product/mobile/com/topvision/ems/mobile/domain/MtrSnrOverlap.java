package com.topvision.ems.mobile.domain;

import java.sql.Timestamp;

public class MtrSnrOverlap {
    private Double mtr; 
    private Double upSnr; 
    private Timestamp xTime;
    public Double getMtr() {
        return mtr;
    }
    public void setMtr(Double mtr) {
        this.mtr = mtr;
    }
    public Double getUpSnr() {
        return upSnr;
    }
    public void setUpSnr(Double upSnr) {
        this.upSnr = upSnr;
    }
    public Timestamp getxTime() {
        return xTime;
    }
    public void setxTime(Timestamp xTime) {
        this.xTime = xTime;
    } 
}
