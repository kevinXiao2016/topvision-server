/***********************************************************************
 * $Id: OnuAuthFailListService.java,v1.0 2015年4月18日 上午10:28:49 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onuauth.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.epon.onuauth.domain.OnuAuthFail;
import com.topvision.framework.service.Service;

/**
 * @author loyal
 * @created @2015年4月18日-上午10:28:49
 * 
 */
public interface OnuAuthFailListService extends Service {
    /**
     * 获取认证失败列表
     * 
     * @param queryMap
     * @return
     */
    List<OnuAuthFail> getOnuAuthFailList(Map<String, Object> queryMap);

    /**
     * 获取认证失败数量
     * 
     * @param queryMap
     * @return
     */
    Long getOnuAuthFailCount(Map<String, Object> queryMap);

    /**
     * 获取认证失败对象
     * 
     * @param entityId
     *            ,onuIndex
     * @return
     */
    OnuAuthFail getOnuAuthFailObject(Long entityId, Long onuIndex);

}
