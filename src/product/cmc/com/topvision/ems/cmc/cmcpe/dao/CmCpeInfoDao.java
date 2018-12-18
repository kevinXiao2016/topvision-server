package com.topvision.ems.cmc.cmcpe.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.cmc.cmcpe.domain.CmCpeInfo;
import com.topvision.framework.dao.Dao;

/**
 * CM下cpe查询dao层
 * @author xiaoyue
 * @created @2017年5月20日-下午6:52:06
 *
 */
public interface CmCpeInfoDao extends Dao {

    /**
     * 查询Cm下符合条件的cpe
     * @param queryMap
     * @return
     */
    List<CmCpeInfo> selectCmCpeList(Map<String, Object> queryMap);

    /**
     * 查询符合条件的CPE数量
     * @param queryMap
     * @return 
     */
    Integer selectCmCpeListNum(Map<String, Object> queryMap);

}
