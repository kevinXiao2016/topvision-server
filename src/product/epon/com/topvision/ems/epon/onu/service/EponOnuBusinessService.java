package com.topvision.ems.epon.onu.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.epon.onu.domain.EponOnuBusinessInfo;
import com.topvision.framework.service.Service;

public interface EponOnuBusinessService extends Service {

    /**
     * 获取Epon业务信息
     * 
     * @param queryMap
     * @return
     */
    List<EponOnuBusinessInfo> queryEponOnuBusinessList(Map<String, Object> queryMap);

    /**
     * 获取Epon业务信息数量
     * 
     * @param queryMap
     * @return
     */
    Integer queryEponOnuBusinessCount(Map<String, Object> queryMap);

}
