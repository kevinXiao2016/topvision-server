/***********************************************************************
 * $Id: OnuAuthManageService.java,v1.0 2015年4月20日 下午12:22:22 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onuauth.service;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.topvision.ems.epon.onuauth.domain.OltOnuAuthStatistics;
import com.topvision.ems.epon.onuauth.domain.OnuAuthFail;
import com.topvision.ems.epon.onuauth.domain.OnuAuthInfo;
import com.topvision.ems.epon.onuauth.domain.PonOnuAuthStatistics;
import com.topvision.framework.service.Service;

/**
 * @author loyal
 * @created @2015年4月20日-下午12:22:22
 * 
 */
public interface OnuAuthManageService extends Service {
    /**
     * 获取ONU认证列表
     * 
     * @param queryMap
     * @return
     */
    List<OnuAuthInfo> getOnuAuthList(Map<String, Object> queryMap);
    
    /**
     * 获取ONU认证数量
     * @param queryMap
     * @return
     */
    Long getOnuAuthListCount(Map<String, Object> queryMap);
    
    /**
     * 获取OLT ONU认证信息
     * @param queryMap
     * @return
     */
    List<OltOnuAuthStatistics> getOltOnuAuthStatistics(Map<String, Object> queryMap);
    
    /**
     * 获取OLT ONU认证数量
     * @param queryMap
     * @return
     */
    Long getOltOnuAuthStatisticsCount(Map<String, Object> queryMap);
    
    /**
     * 获取OLT PON认证信息
     * @param entityId
     * @return
     */
    List<PonOnuAuthStatistics> getPonOnuAuthStatistics(Long entityId);
    
    /**
     * 获取ONU认证失败列表
     * @param queryMap
     * @return
     */
    List<OnuAuthFail> getOnuAuthFailList(Map<String, Object> queryMap);
    
    /**
     * 获取ONU认证失败数量
     * @param queryMap
     * @return
     */
    Long getOnuAuthFailListCount(Map<String, Object> queryMap);
    
    /**
     * 获取当前ONU认证的ONU ID
     * @param 
     * @return
     */
    List<Long> getAuthOnuId(Long entityId, Long ponIndex, Integer authAction);
    
    JSONObject addOnuAuthList(JSONArray addAuthArray);
    
}
