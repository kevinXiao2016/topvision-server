/***********************************************************************
 * $Id: OltSubOpticalInfo.java,v1.0 2014年7月23日 上午11:39:10 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.realtime.domain;

import java.io.Serializable;

import com.topvision.framework.annotation.SnmpProperty;

/**
 * @author Bravin
 * @created @2014年7月23日-上午11:39:10
 *
 */
public class OltSubOpticalInfo implements Serializable {
    private static final long serialVersionUID = 7806446748679383048L;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.12.2.1.1", type = "Integer32", index = true)
    private Long cardIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.12.2.1.2", type = "Integer32", index = true)
    private Integer portIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.12.2.1.3", type = "Integer32", index = true)
    private Integer onuIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.12.2.1.4", type = "Integer32")
    private Integer recvOpticalPower;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.12.2.1.5", type = "Integer32")
    private Integer transOpticalPower;

    public Long getCardIndex() {
        return cardIndex;
    }

    public void setCardIndex(Long cardIndex) {
        this.cardIndex = cardIndex;
    }

    public Integer getPortIndex() {
        return portIndex;
    }

    public void setPortIndex(Integer portIndex) {
        this.portIndex = portIndex;
    }

    public Integer getOnuIndex() {
        return onuIndex;
    }

    public void setOnuIndex(Integer onuIndex) {
        this.onuIndex = onuIndex;
    }

    public Integer getRecvOpticalPower() {
        return recvOpticalPower;
    }

    public void setRecvOpticalPower(Integer recvOpticalPower) {
        this.recvOpticalPower = recvOpticalPower;
    }

    public Integer getTransOpticalPower() {
        return transOpticalPower;
    }

    public void setTransOpticalPower(Integer transOpticalPower) {
        this.transOpticalPower = transOpticalPower;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OltSubOpticalInfo [cardIndex=");
        builder.append(cardIndex);
        builder.append(", portIndex=");
        builder.append(portIndex);
        builder.append(", onuIndex=");
        builder.append(onuIndex);
        builder.append(", recvOpticalPower=");
        builder.append(recvOpticalPower);
        builder.append(", transOpticalPower=");
        builder.append(transOpticalPower);
        builder.append("]");
        return builder.toString();
    }

}
