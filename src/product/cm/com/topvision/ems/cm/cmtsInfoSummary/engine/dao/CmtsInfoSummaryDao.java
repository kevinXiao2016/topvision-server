package com.topvision.ems.cm.cmtsInfoSummary.engine.dao;

import java.util.List;

import com.topvision.ems.cm.cmtsInfoSummary.domain.CmMainChannel;
import com.topvision.ems.cm.cmtsInfoSummary.domain.CmtsInfoStatistics;
import com.topvision.ems.cmc.cmtsInfo.domain.CmtsInfoThreshold;

public interface CmtsInfoSummaryDao {
    //提取合格区间
    CmtsInfoThreshold selectLocalThreshold();

    /**
     * 插入数据到历史表
     * @param
     * @return void
     */
    void insertCmtsInfoSummary(List<CmtsInfoStatistics> cmtsInfoStatistics);

    /**
     * 插入数据到最近表
     * @param
     * @return void
     */
    void updateCmtsInfoSummaryLst(List<CmtsInfoStatistics> cmtsInfoStatistics);

    /**
     * 查找cm上下行主信道
     * @param
     * @return CmMainChannel
     */
    List<CmMainChannel> selectCmAttr();

}
