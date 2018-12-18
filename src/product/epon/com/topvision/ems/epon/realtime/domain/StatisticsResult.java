/***********************************************************************
 * $Id: StatisticsResult.java,v1.0 2014-8-23 下午4:13:06 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.realtime.domain;

import java.util.List;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author flack
 * @created @2014-8-23-下午4:13:06
 *
 */
public class StatisticsResult implements AliasesSuperType {
    private static final long serialVersionUID = -5886879710085400922L;

    private List<OpticalStatsticsInfo> recvStasticsList;
    private List<OpticalStatsticsInfo> transStasticsList;
    private int recvErrorNum = 0;
    private int transErrorNum = 0;

    public List<OpticalStatsticsInfo> getRecvStasticsList() {
        return recvStasticsList;
    }

    public void setRecvStasticsList(List<OpticalStatsticsInfo> recvStasticsList) {
        this.recvStasticsList = recvStasticsList;
    }

    public List<OpticalStatsticsInfo> getTransStasticsList() {
        return transStasticsList;
    }

    public void setTransStasticsList(List<OpticalStatsticsInfo> transStasticsList) {
        this.transStasticsList = transStasticsList;
    }

    public int getRecvErrorNum() {
        return recvErrorNum;
    }

    public void setRecvErrorNum(int recvErrorNum) {
        this.recvErrorNum = recvErrorNum;
    }

    public int getTransErrorNum() {
        return transErrorNum;
    }

    public void setTransErrorNum(int transErrorNum) {
        this.transErrorNum = transErrorNum;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("StatisticsResult [recvStasticsList=");
        builder.append(recvStasticsList);
        builder.append(", transStasticsList=");
        builder.append(transStasticsList);
        builder.append(", recvErrorNum=");
        builder.append(recvErrorNum);
        builder.append(", transErrorNum=");
        builder.append(transErrorNum);
        builder.append("]");
        return builder.toString();
    }

}
