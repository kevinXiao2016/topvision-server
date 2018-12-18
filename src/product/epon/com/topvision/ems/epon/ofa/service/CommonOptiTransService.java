package com.topvision.ems.epon.ofa.service;

import com.topvision.ems.epon.ofa.facade.domain.CommonOptiTrans;

public interface CommonOptiTransService {

    /**
     * 获取CommonOptiTrans
     * 
     * @param entityId
     * @return
     */
    CommonOptiTrans getCommonOptiTrans(Long entityId);

    /**
     * 修改CommonOptiTrans信息
     * 
     * @param topOFAAlarmThresholdEntry
     */
    void modifyCommonOptiTrans(CommonOptiTrans commonOptiTrans, Long entityId);

    /**
     * 刷新CommonOptiTrans
     * 
     * @param entityId
     * @return
     */
    void refreshCommonOptiTrans(Long entityId);
}
