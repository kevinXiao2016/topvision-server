/***********************************************************************
 * $Id: OnuAuthManageDao.java,v1.0 2015年4月20日 上午11:43:20 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onuauth.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.epon.onuauth.domain.OltOnuAuthStatistics;
import com.topvision.ems.epon.onuauth.domain.OnuAuthFail;
import com.topvision.ems.epon.onuauth.domain.OnuAuthInfo;
import com.topvision.ems.epon.onuauth.domain.PonOnuAuthStatistics;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author loyal
 * @created @2015年4月20日-上午11:43:20
 * 
 */
public interface OnuAuthManageDao extends BaseEntityDao<Object> {
    /**
     * 获取ONU认证列表
     * @param queryMap
     * @return
     */
    List<OnuAuthInfo> selectOnuAuthList(Map<String, Object> queryMap);
    
    /**
     * 获取ONU认证数量
     * @param queryMap
     * @return
     */
    Long selectOnuAuthListCount(Map<String, Object> queryMap);
    
    /**
     * 获取OLT ONU认证信息
     * @param queryMap
     * @return
     */
    List<OltOnuAuthStatistics> selectOltOnuAuthStatistics(Map<String, Object> queryMap);
    
    /**
     * 获取OLT ONU认证数量
     * @param queryMap
     * @return
     */
    Long selectOltOnuAuthStatisticsCount(Map<String, Object> queryMap);
    
    /**
     * 获取OLT PON认证信息
     * @param entityId
     * @return
     */
    List<PonOnuAuthStatistics> selectPonOnuAuthStatistics(Long entityId);
    
    /**
     * 获取ONU认证失败列表
     * @param queryMap
     * @return
     */
    List<OnuAuthFail> selectOnuAuthFailList(Map<String, Object> queryMap);
    
    /**
     * 获取ONU认证失败数量
     * @param queryMap
     * @return
     */
    Long selectOnuAuthFailListCount(Map<String, Object> queryMap);
    
    /**
     * 获取当前ONU认证的ONU Index
     * @param 
     * @return
     */
    List<Long> getOnuAuthIndex(Long entityId, Long ponIndex, Integer authAction);

}
