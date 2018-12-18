/***********************************************************************
 * $Id: SendConfigEntityObject.java,v1.0 2016年3月23日 下午1:54:06 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author loyal
 * @created @2016年3月23日-下午1:54:06
 * 
 */
public class SendConfigEntityObject {
    private Long ip;
    private List<SendConfigEntity> sendConfigEntitys = new ArrayList<SendConfigEntity>();

    public SendConfigEntityObject(Long ip) {
        super();
        this.ip = ip;
    }

    public Long getIp() {
        return ip;
    }

    public void setIp(Long ip) {
        this.ip = ip;
    }

    public List<SendConfigEntity> getSendConfigEntitys() {
        return sendConfigEntitys;
    }

    public void setSendConfigEntitys(List<SendConfigEntity> sendConfigEntitys) {
        this.sendConfigEntitys = sendConfigEntitys;
    }

    public void addSendConfigEntity(SendConfigEntity sendConfigEntity){
        sendConfigEntitys.add(sendConfigEntity);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SendConfigEntityObject other = (SendConfigEntityObject) obj;
        if (ip == null) {
            if (other.getIp() != null)
                return false;
        } else if (!ip.equals(other.getIp()))
            return false;
        return true;
    }

    public void removeEntity(List<SendConfigEntity> sendConfigEntitys) {
        this.sendConfigEntitys.removeAll(sendConfigEntitys);
    }

    public boolean isEmpty() {
        return this.sendConfigEntitys.isEmpty();
    }

    @Override
    public String toString() {
        return "SendConfigEntityObject{" +
                "ip=" + ip +
                ", sendConfigEntitys=" + sendConfigEntitys +
                '}';
    }
}
