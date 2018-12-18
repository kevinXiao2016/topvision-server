/***********************************************************************
 * $ CmcCmNum.java,v1.0 14-5-12 上午11:55 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ccmts.facade.domain;

import com.topvision.framework.annotation.SnmpProperty;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jay
 * @created @14-5-12-上午11:55
 */
@Alias("cmcCmNum")
public class CmcCmNum implements Serializable{
    private static final long serialVersionUID = 7342180206202447869L;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.2.3.1.0")
    private Long totalCmNum;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.2.3.2.0")
    private Long onlineCmNum;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.2.3.5.0")
    private Long offlineCmNum;
    @SuppressWarnings("unused")
    private Long docsis3Num;
    private Long snrExceptionNum = 0L;
    private List<Long> snrExceptionList = new ArrayList<>();
    private Long powerExceptionNum = 0L;
    private List<Long> powerExceptionList = new ArrayList<>();

    public Long getTotalCmNum() {
        return totalCmNum;
    }

    public void setTotalCmNum(Long totalCmNum) {
        this.totalCmNum = totalCmNum;
    }

    public Long getOnlineCmNum() {
        return onlineCmNum;
    }

    public void setOnlineCmNum(Long onlineCmNum) {
        this.onlineCmNum = onlineCmNum;
    }

    public Long getOfflineCmNum() {
        return offlineCmNum;
    }

    public void setOfflineCmNum(Long offlineCmNum) {
        this.offlineCmNum = offlineCmNum;
    }

    public Long getDocsis3Num() {
        return totalCmNum;
    }

    public void setDocsis3Num(Long docsis3Num) {
        this.docsis3Num = docsis3Num;
    }

    public Long getSnrExceptionNum() {
        return snrExceptionNum;
    }

    public void setSnrExceptionNum(Long snrExceptionNum) {
        this.snrExceptionNum = snrExceptionNum;
    }

    public Long getPowerExceptionNum() {
        return powerExceptionNum;
    }

    public void setPowerExceptionNum(Long powerExceptionNum) {
        this.powerExceptionNum = powerExceptionNum;
    }

    public void addSnrException(Long statusIndex) {
        snrExceptionNum++;
        snrExceptionList.add(statusIndex);
    }

    public void addPowerException(Long statusIndex) {
        powerExceptionNum++;
        powerExceptionList.add(statusIndex);
    }

    public List<Long> getSnrExceptionList() {
        return snrExceptionList;
    }

    public void setSnrExceptionList(List<Long> snrExceptionList) {
        this.snrExceptionList = snrExceptionList;
    }

    public List<Long> getPowerExceptionList() {
        return powerExceptionList;
    }

    public void setPowerExceptionList(List<Long> powerExceptionList) {
        this.powerExceptionList = powerExceptionList;
    }
}
