package com.topvision.ems.gpon.onu.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.gpon.onu.domain.GponOnuAttribute;
import com.topvision.ems.gpon.onu.domain.GponOnuBusinessInfo;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * 
 * @author CWQ
 * @created @2017年12月25日-下午2:30:59
 *
 */
public interface GponOnuBusinessDao extends BaseEntityDao<GponOnuAttribute> {

    /**
     * 获取Gpon业务信息
     * 
     * @param queryMap
     * @return
     */
    List<GponOnuBusinessInfo> selectGponOnuBusinessList(Map<String, Object> queryMap);

    /**
     * 获取Gpon业务信息数量
     * 
     * @param queryMap
     * @return
     */
    Integer selectGponOnuBusinessCount(Map<String, Object> queryMap);

}
