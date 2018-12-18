package com.topvision.ems.epon.ofa.dao;

import java.util.List;

import com.topvision.ems.epon.ofa.facade.domain.CommonOptiTrans;

public interface CommonOptiTransDao {

    /**
     * 获得CommonOptiTrans
     * 
     * @param entityId
     * @return
     */
    CommonOptiTrans getCommonOptiTransById(Long entityId);

    /**
     * 批量插入或修改CommonOptiTrans
     * 
     * @param commonOptiTrans
     */
    void batchInsertOrUpdateCommonOptiTrans(List<CommonOptiTrans> commonOptiTransList);
    
    /**
     * 插入或修改CommonOptiTrans
     * 
     * @param commonOptiTrans
     */
    void insertOrUpdateCommonOptiTrans(CommonOptiTrans commonOptiTrans);
}
