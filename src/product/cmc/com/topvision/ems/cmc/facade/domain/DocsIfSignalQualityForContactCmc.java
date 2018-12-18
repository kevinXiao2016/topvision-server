/***********************************************************************
 * $Id: DocsIfSignalQuality.java,v1.0 2013-4-28 上午10:31:53 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.facade.domain;

import java.io.Serializable;
import java.text.DecimalFormat;

import com.topvision.framework.annotation.SnmpProperty;

/**
 * @author haojie
 * @created @2013-4-28-上午10:31:53
 * 
 */
public class DocsIfSignalQualityForContactCmc implements Serializable, Comparable<DocsIfSignalQualityForContactCmc> {
    private static final long serialVersionUID = -6073906532878063277L;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.1", index = true)
    private Long ifIndex;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.4.1.5")
    private Long docsIfSigQSignalNoise;
    private Long downChanelId;

    private final static DecimalFormat df = new DecimalFormat("0.0");
    private String docsIfSigQSignalNoiseForUnit;

    public Long getIfIndex() {
        return ifIndex;
    }

    public void setIfIndex(Long ifIndex) {
        this.ifIndex = ifIndex;
    }

    public Long getDocsIfSigQSignalNoise() {
        return docsIfSigQSignalNoise;
    }

    public void setDocsIfSigQSignalNoise(Long docsIfSigQSignalNoise) {
        this.docsIfSigQSignalNoise = docsIfSigQSignalNoise;
    }

    public Long getDownChanelId() {
        return downChanelId;
    }

    public void setDownChanelId(Long downChanelId) {
        this.downChanelId = downChanelId;
    }

    public String getDocsIfSigQSignalNoiseForUnit() {
        if (this.getDocsIfSigQSignalNoise() != null) {
            double NoiseForUnit = docsIfSigQSignalNoise;
            docsIfSigQSignalNoiseForUnit = df.format(NoiseForUnit / 10) /* + " dB" */;
        }
        return docsIfSigQSignalNoiseForUnit;
    }

    public void setDocsIfSigQSignalNoiseForUnit(String docsIfSigQSignalNoiseForUnit) {
        this.docsIfSigQSignalNoiseForUnit = docsIfSigQSignalNoiseForUnit;
    }

    @Override
    public int compareTo(DocsIfSignalQualityForContactCmc another) {
        return (int) (this.downChanelId - another.downChanelId);
    }

}
