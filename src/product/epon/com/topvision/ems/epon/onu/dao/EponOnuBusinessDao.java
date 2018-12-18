package com.topvision.ems.epon.onu.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.epon.onu.domain.EponOnuBusinessInfo;

/**
 * epon 业务
 * 
 * @author w1992wishes
 * @created @2017年12月22日-下午4:38:16
 *
 */
public interface EponOnuBusinessDao {

    /**
     * 获取Epon业务信息
     * 
     * @param queryMap
     * @return
     */
    List<EponOnuBusinessInfo> selectEponOnuBusinessList(Map<String, Object> queryMap);

    /**
     * 获取Epon业务信息数量
     * 
     * @param queryMap
     * @return
     */
    Integer selectEponOnuBusinessCount(Map<String, Object> queryMap);
}
