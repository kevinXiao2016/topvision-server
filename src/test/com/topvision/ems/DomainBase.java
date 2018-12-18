/***********************************************************************
 * $ DomainBase.java,v1.0 2012-4-22 10:21:23 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author jay
 * @created @2012-4-22-10:21:23
 */
public class DomainBase implements Serializable {
    private static final long serialVersionUID = 7342180206202447869L;
    private byte base1;
    private Byte base11;
    private int base2;
    private Integer base21;
    private long base3;
    private Long base31;
    private float base4;
    private Float base41;
    private double base5;
    private Double base51;
    private char base6;
    private boolean base7;
    private Boolean base71;
    private String base8;
    private Timestamp base9;

    public byte getBase1() {
        return base1;
    }

    public void setBase1(byte base1) {
        this.base1 = base1;
    }

    public int getBase2() {
        return base2;
    }

    public void setBase2(int base2) {
        this.base2 = base2;
    }

    public long getBase3() {
        return base3;
    }

    public void setBase3(long base3) {
        this.base3 = base3;
    }

    public float getBase4() {
        return base4;
    }

    public void setBase4(float base4) {
        this.base4 = base4;
    }

    public double getBase5() {
        return base5;
    }

    public void setBase5(double base5) {
        this.base5 = base5;
    }

    public char getBase6() {
        return base6;
    }

    public void setBase6(char base6) {
        this.base6 = base6;
    }

    public boolean isBase7() {
        return base7;
    }

    public void setBase7(boolean base7) {
        this.base7 = base7;
    }

    public String getBase8() {
        return base8;
    }

    public void setBase8(String base8) {
        this.base8 = base8;
    }

    public Byte getBase11() {
        return base11;
    }

    public void setBase11(Byte base11) {
        this.base11 = base11;
    }

    public Integer getBase21() {
        return base21;
    }

    public void setBase21(Integer base21) {
        this.base21 = base21;
    }

    public Long getBase31() {
        return base31;
    }

    public void setBase31(Long base31) {
        this.base31 = base31;
    }

    public Float getBase41() {
        return base41;
    }

    public void setBase41(Float base41) {
        this.base41 = base41;
    }

    public Double getBase51() {
        return base51;
    }

    public void setBase51(Double base51) {
        this.base51 = base51;
    }

    public Boolean getBase71() {
        return base71;
    }

    public void setBase71(Boolean base71) {
        this.base71 = base71;
    }

    public Timestamp getBase9() {
        return base9;
    }

    public void setBase9(Timestamp base9) {
        this.base9 = base9;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("DomainBase");
        sb.append("{base11=").append(base11);
        sb.append(", base1=").append(base1);
        sb.append(", base2=").append(base2);
        sb.append(", base21=").append(base21);
        sb.append(", base3=").append(base3);
        sb.append(", base31=").append(base31);
        sb.append(", base4=").append(base4);
        sb.append(", base41=").append(base41);
        sb.append(", base5=").append(base5);
        sb.append(", base51=").append(base51);
        sb.append(", base6=").append(base6);
        sb.append(", base7=").append(base7);
        sb.append(", base71=").append(base71);
        sb.append(", base8='").append(base8).append('\'');
        sb.append(", base9=").append(base9);
        sb.append('}');
        return sb.toString();
    }
}
