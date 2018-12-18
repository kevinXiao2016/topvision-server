/***********************************************************************
 * $Id: CmRealtimeNum.java,v1.0 2014年5月11日 下午1:05:28 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ccmts.domain;

import java.io.Serializable;

/**
 * @author loyal
 * @created @2014年5月11日-下午1:05:28
 *
 */
public class CmRealtimeNum implements Serializable{
    private static final long serialVersionUID = -2545378605652443345L;
    private Integer allNum;
    private Integer onlineNum;
    private Integer offlineNum;
    private Integer docsis3Num;
    private Integer snrExceptionNum;
    private Integer initNum;
    private Integer cpeOnlineNum;
    public Integer getAllNum() {
        return allNum;
    }
    public void setAllNum(Integer allNum) {
        this.allNum = allNum;
    }
    public Integer getOnlineNum() {
        return onlineNum;
    }
    public void setOnlineNum(Integer onlineNum) {
        this.onlineNum = onlineNum;
    }
    public Integer getOfflineNum() {
        return offlineNum;
    }
    public void setOfflineNum(Integer offlineNum) {
        this.offlineNum = offlineNum;
    }
    public Integer getDocsis3Num() {
        return docsis3Num;
    }
    public void setDocsis3Num(Integer docsis3Num) {
        this.docsis3Num = docsis3Num;
    }
    public Integer getSnrExceptionNum() {
        return snrExceptionNum;
    }
    public void setSnrExceptionNum(Integer snrExceptionNum) {
        this.snrExceptionNum = snrExceptionNum;
    }
    public Integer getInitNum() {
        return initNum;
    }
    public void setInitNum(Integer initNum) {
        this.initNum = initNum;
    }
    public Integer getCpeOnlineNum() {
        return cpeOnlineNum;
    }
    public void setCpeOnlineNum(Integer cpeOnlineNum) {
        this.cpeOnlineNum = cpeOnlineNum;
    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmRealtimeNum [allNum=");
        builder.append(allNum);
        builder.append(", onlineNum=");
        builder.append(onlineNum);
        builder.append(", offlineNum=");
        builder.append(offlineNum);
        builder.append(", docsis3Num=");
        builder.append(docsis3Num);
        builder.append(", snrExceptionNum=");
        builder.append(snrExceptionNum);
        builder.append(", initNum=");
        builder.append(initNum);
        builder.append(", cpeOnlineNum=");
        builder.append(cpeOnlineNum);
        builder.append("]");
        return builder.toString();
    }
    
}
