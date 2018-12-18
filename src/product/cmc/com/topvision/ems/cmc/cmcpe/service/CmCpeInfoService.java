package com.topvision.ems.cmc.cmcpe.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.cmc.cmcpe.domain.CmCpeInfo;
import com.topvision.framework.service.Service;

/**
 * cm下cpe查询
 * @author xiaoyue
 * @created @2017年5月20日-下午6:47:14
 */
public interface CmCpeInfoService extends Service {

    /**
     * 加载cm下cpe
     * @param queryMap
     * @return
     */
    List<CmCpeInfo> getCmCpeList(Map<String, Object> queryMap);

    /**
     * 加载符合查询条件的CM列表的总数
     * @param queryMap
     * @return
     */
    Integer getCmNum(Map<String, Object> queryMap);

}
