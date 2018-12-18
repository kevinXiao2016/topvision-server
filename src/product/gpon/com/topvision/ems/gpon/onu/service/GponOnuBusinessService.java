package com.topvision.ems.gpon.onu.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.gpon.onu.domain.GponOnuBusinessInfo;

/**
 * 
 * @author CWQ
 * @created @2017年12月25日-下午2:32:20
 *
 */
public interface GponOnuBusinessService {

    /**
     * 获取Gpon业务信息
     * 
     * @param queryMap
     * @return
     */
    List<GponOnuBusinessInfo> queryGponOnuBusinessList(Map<String, Object> queryMap);

    /**
     * 获取Gpon业务信息数量
     * 
     * @param queryMap
     * @return
     */
    Integer queryGponOnuBusinessCount(Map<String, Object> queryMap);
}
