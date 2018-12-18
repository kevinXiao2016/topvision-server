package com.topvision.ems.epon.realtime.domain;

import java.io.Serializable;

/**
 * Created by jay on 17-2-23.
 */
public interface OltOnuBaseInfo extends Serializable {
    public Long getOnuDeviceIndex();
    public String getOnuName();
    public String getIdentifyKey();
    public Integer getOperationStatus();
    public Integer getTestDistance();
    public Integer getOnuType();
}
