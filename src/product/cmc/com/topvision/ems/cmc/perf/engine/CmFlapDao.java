package com.topvision.ems.cmc.perf.engine;

import java.util.List;

import com.topvision.ems.cmc.performance.domain.CMFlapHis;

public interface CmFlapDao {

    /**
     * 插入历史表
     * 
     * @param allFlap
     * @param entityId
     */
    public void batchInsertCmFlapHis(List<CMFlapHis> allFlap, Long entityId);

}
