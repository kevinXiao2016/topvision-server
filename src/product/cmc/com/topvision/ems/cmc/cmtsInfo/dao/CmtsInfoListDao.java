package com.topvision.ems.cmc.cmtsInfo.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.cmc.cmtsInfo.domain.CmOutPowerNotInRange;
import com.topvision.ems.cmc.cmtsInfo.domain.CmRePowerNotInRange;
import com.topvision.ems.cmc.cmtsInfo.domain.CmtsInfoNotInRange;
import com.topvision.ems.cmc.cmtsInfo.domain.CmtsInfoThreshold;
import com.topvision.ems.cmc.cmtsInfo.domain.CmtsNetworkInfo;
import com.topvision.ems.cmc.cmtsInfo.domain.DownSnrAvgNotInRange;
import com.topvision.ems.cmc.cmtsInfo.domain.UpSnrNotInRange;
import com.topvision.framework.dao.BaseEntityDao;

public interface CmtsInfoListDao extends BaseEntityDao<CmtsNetworkInfo> {
    /**
     * 查询cmc列表
     * 
     * @param cmcQueryMap
     *            olt设备id
     * @param start
     *            pon口id
     * @param limit
     *            cmc设备mac地址
     * @return List<CmcAttribute> cmc列表信息
     */
    List<CmtsNetworkInfo> queryForCmtsInfoList(Map<String, Object> cmcQueryMap);

    List<CmtsInfoNotInRange> queryCmtsNotInRange(Map<String, Object> cmcQueryMap);

    List<CmOutPowerNotInRange> queryCmOutPowerNotInRange(Map<String, Object> cmcQueryMap);

    List<CmRePowerNotInRange> queryCmRePowerNotInRange(Map<String, Object> cmcQueryMap);

    List<UpSnrNotInRange> queryUpSnrNotInRange(Map<String, Object> cmcQueryMap);

    List<DownSnrAvgNotInRange> queryDownSnrAvgNotInRange(Map<String, Object> cmcQueryMap);

    /**
     * 保存合格指标区间
     * @param
     * @return void
     */
    void saveLocalThreshold(Map<String, Object> cmcQueryMap);

    /**
     * 获取指标合格区间
     * @param
     * @return CmtsInfoThreshold
     */
    CmtsInfoThreshold getLocalThreshold();

    /**
     * 列表行数
     * @param cmcQueryMap 
     * @param
     * @return Long
     */
    Long getCmcNum(Map<String, Object> cmcQueryMap);
}
