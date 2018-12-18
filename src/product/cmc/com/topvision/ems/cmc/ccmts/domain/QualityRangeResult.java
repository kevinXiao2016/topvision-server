/***********************************************************************
 * $ QualityRange.java,v1.0 14-5-11 下午12:45 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ccmts.domain;

import java.util.ArrayList;
import java.util.List;

import com.topvision.platform.zetaframework.var.UnitConfigConstant;

/**
 * @author jay
 * @created @14-5-11-下午12:45
 */
public class QualityRangeResult {
    private List<QualityRange> usTxPowerQualityRange = new ArrayList<>();
    private List<QualityRange> usSnrQualityRange = new ArrayList<>();
    private List<QualityRange> dsRxPowerQualityRange = new ArrayList<>();
    private List<QualityRange> dsSnrQualityRange = new ArrayList<>();
    //还未实现
    private List<QualityRange> usPowerAttenuateQualityRange = new ArrayList<>();
    //还未实现
    private List<QualityRange> dsPowerAttenuateQualityRange = new ArrayList<>();

    private boolean remoteQuery = true;

    public QualityRangeResult() {
        //usTxPower
        QualityRange usTxPowerNA = new QualityRange();
        usTxPowerNA.setIsNa(true);
        usTxPowerNA.setRangeLevel(QualityRange.R);
        usTxPowerQualityRange.add(usTxPowerNA);
        QualityRange usTxPower1 = new QualityRange();
        usTxPower1.setStart(QualityRange.START);
        usTxPower1.setEnd((int) UnitConfigConstant.parsePowerValue(30));
        usTxPower1.setRangeLevel(QualityRange.R);
        usTxPowerQualityRange.add(usTxPower1);
        QualityRange usTxPower2 = usTxPower1.nextRange((int) UnitConfigConstant.parsePowerValue(38), QualityRange.Y);
        usTxPowerQualityRange.add(usTxPower2);
        QualityRange usTxPower3 = usTxPower2.nextRange((int) UnitConfigConstant.parsePowerValue(42),QualityRange.G);
        usTxPowerQualityRange.add(usTxPower3);
        QualityRange usTxPower4 = usTxPower3.nextRange((int) UnitConfigConstant.parsePowerValue(45),QualityRange.G);
        usTxPowerQualityRange.add(usTxPower4);
        QualityRange usTxPower5 = usTxPower4.nextRange((int) UnitConfigConstant.parsePowerValue(48),QualityRange.G);
        usTxPowerQualityRange.add(usTxPower5);
        QualityRange usTxPower6 = usTxPower5.nextRange((int) UnitConfigConstant.parsePowerValue(52),QualityRange.Y);
        usTxPowerQualityRange.add(usTxPower6);
        QualityRange usTxPower7 = usTxPower6.nextRange(QualityRange.END,QualityRange.R);
        usTxPowerQualityRange.add(usTxPower7);
        //usSnr
        QualityRange usSnrNA = new QualityRange();
        usSnrNA.setIsNa(true);
        usSnrNA.setRangeLevel(QualityRange.R);
        usSnrQualityRange.add(usSnrNA);
        QualityRange usSnr1 = new QualityRange();
        usSnr1.setStart(QualityRange.START);
        usSnr1.setEnd(20);
        usSnr1.setRangeLevel(QualityRange.R);
        usSnrQualityRange.add(usSnr1);
        QualityRange usSnr2 = usSnr1.nextRange(22,QualityRange.R);
        usSnrQualityRange.add(usSnr2);
        QualityRange usSnr3 = usSnr2.nextRange(25,QualityRange.Y);
        usSnrQualityRange.add(usSnr3);
        QualityRange usSnr4 = usSnr3.nextRange(28,QualityRange.Y);
        usSnrQualityRange.add(usSnr4);
        QualityRange usSnr5 = usSnr4.nextRange(30,QualityRange.G);
        usSnrQualityRange.add(usSnr5);
        QualityRange usSnr6 = usSnr5.nextRange(34,QualityRange.G);
        usSnrQualityRange.add(usSnr6);
        QualityRange usSnr7 = usSnr6.nextRange(QualityRange.END,QualityRange.G);
        usSnrQualityRange.add(usSnr7);
        //dsRxPower
        QualityRange dsRxPowerNA = new QualityRange();
        dsRxPowerNA.setIsNa(true);
        dsRxPowerNA.setRangeLevel(QualityRange.R);
        dsRxPowerQualityRange.add(dsRxPowerNA);
        QualityRange dsRxPower1 = new QualityRange();
        dsRxPower1.setStart(QualityRange.START);
        dsRxPower1.setEnd((int) UnitConfigConstant.parsePowerValue(-15));
        dsRxPower1.setRangeLevel(QualityRange.R);
        dsRxPowerQualityRange.add(dsRxPower1);
        QualityRange dsRxPower2 = dsRxPower1.nextRange((int) UnitConfigConstant.parsePowerValue(-10), QualityRange.Y);
        dsRxPowerQualityRange.add(dsRxPower2);
        QualityRange dsRxPower3 = dsRxPower2.nextRange((int) UnitConfigConstant.parsePowerValue(-3), QualityRange.G);
        dsRxPowerQualityRange.add(dsRxPower3);
        QualityRange dsRxPower4 = dsRxPower3.nextRange((int) UnitConfigConstant.parsePowerValue(3), QualityRange.G);
        dsRxPowerQualityRange.add(dsRxPower4);
        QualityRange dsRxPower5 = dsRxPower4.nextRange((int) UnitConfigConstant.parsePowerValue(10), QualityRange.G);
        dsRxPowerQualityRange.add(dsRxPower5);
        QualityRange dsRxPower6 = dsRxPower5.nextRange((int) UnitConfigConstant.parsePowerValue(15), QualityRange.Y);
        dsRxPowerQualityRange.add(dsRxPower6);
        QualityRange dsRxPower7 = dsRxPower6.nextRange(QualityRange.END,QualityRange.R);
        dsRxPowerQualityRange.add(dsRxPower7);
        //dsSnr
        QualityRange dsSnrNA = new QualityRange();
        dsSnrNA.setIsNa(true);
        dsSnrNA.setRangeLevel(QualityRange.R);
        dsSnrQualityRange.add(dsSnrNA);
        QualityRange dsSnr1 = new QualityRange();
        dsSnr1.setStart(QualityRange.START);
        dsSnr1.setEnd(20);
        dsSnr1.setRangeLevel(QualityRange.R);
        dsSnrQualityRange.add(dsSnr1);
        QualityRange dsSnr2 = dsSnr1.nextRange(22,QualityRange.R);
        dsSnrQualityRange.add(dsSnr2);
        QualityRange dsSnr3 = dsSnr2.nextRange(25,QualityRange.Y);
        dsSnrQualityRange.add(dsSnr3);
        QualityRange dsSnr4 = dsSnr3.nextRange(28,QualityRange.Y);
        dsSnrQualityRange.add(dsSnr4);
        QualityRange dsSnr5 = dsSnr4.nextRange(30,QualityRange.G);
        dsSnrQualityRange.add(dsSnr5);
        QualityRange dsSnr6 = dsSnr5.nextRange(34,QualityRange.G);
        dsSnrQualityRange.add(dsSnr6);
        QualityRange dsSnr7 = dsSnr6.nextRange(QualityRange.END,QualityRange.G);
        dsSnrQualityRange.add(dsSnr7);
    }

    public List<QualityRange> getUsTxPowerQualityRange() {
        return usTxPowerQualityRange;
    }

    public void setUsTxPowerQualityRange(List<QualityRange> usTxPowerQualityRange) {
        this.usTxPowerQualityRange = usTxPowerQualityRange;
    }

    public List<QualityRange> getUsSnrQualityRange() {
        return usSnrQualityRange;
    }

    public void setUsSnrQualityRange(List<QualityRange> usSnrQualityRange) {
        this.usSnrQualityRange = usSnrQualityRange;
    }

    public List<QualityRange> getDsRxPowerQualityRange() {
        return dsRxPowerQualityRange;
    }

    public void setDsRxPowerQualityRange(List<QualityRange> dsRxPowerQualityRange) {
        this.dsRxPowerQualityRange = dsRxPowerQualityRange;
    }

    public List<QualityRange> getDsSnrQualityRange() {
        return dsSnrQualityRange;
    }

    public void setDsSnrQualityRange(List<QualityRange> dsSnrQualityRange) {
        this.dsSnrQualityRange = dsSnrQualityRange;
    }

    public List<QualityRange> getUsPowerAttenuateQualityRange() {
        return usPowerAttenuateQualityRange;
    }

    public void setUsPowerAttenuateQualityRange(List<QualityRange> usPowerAttenuateQualityRange) {
        this.usPowerAttenuateQualityRange = usPowerAttenuateQualityRange;
    }

    public List<QualityRange> getDsPowerAttenuateQualityRange() {
        return dsPowerAttenuateQualityRange;
    }

    public void setDsPowerAttenuateQualityRange(List<QualityRange> dsPowerAttenuateQualityRange) {
        this.dsPowerAttenuateQualityRange = dsPowerAttenuateQualityRange;
    }

    public boolean isRemoteQuery() {
        return remoteQuery;
    }

    public void setRemoteQuery(boolean remoteQuery) {
        this.remoteQuery = remoteQuery;
    }
}
