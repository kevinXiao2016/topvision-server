/***********************************************************************
 * $Id: PonUsedInfo.java,v1.0 2014-8-29 上午11:01:14 $
 * 
 * @author: lzt
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.domain;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author lzt
 * @created @2014-8-29 上午11:01:14
 *
 */
public class PonUsedInfo implements AliasesSuperType {

    private static final long serialVersionUID = -1817523403911748752L;
    private Integer slotTotalNum;
    private Integer ponUpNum;
    private Integer unusedPonNum;
    private Integer unusedSlotNum;

    public Integer getSlotTotalNum() {
        return slotTotalNum;
    }

    public void setSlotTotalNum(Integer slotTotalNum) {
        this.slotTotalNum = slotTotalNum;
    }

    public Integer getPonUpNum() {
        return ponUpNum;
    }

    public void setPonUpNum(Integer ponUpNum) {
        this.ponUpNum = ponUpNum;
    }

    public Integer getUnusedPonNum() {
        return unusedPonNum;
    }

    public void setUnusedPonNum(Integer unusedPonNum) {
        this.unusedPonNum = unusedPonNum;
    }

    public Integer getUnusedSlotNum() {
        return unusedSlotNum;
    }

    public void setUnusedSlotNum(Integer unusedSlotNum) {
        this.unusedSlotNum = unusedSlotNum;
    }

    @Override
    public String toString() {
        return "PonUsedInfo [slotTotalNum=" + slotTotalNum + ", ponUpNum=" + ponUpNum + ", unusedPonNum="
                + unusedPonNum + ", unusedSlotNum=" + unusedSlotNum + "]";
    }

}