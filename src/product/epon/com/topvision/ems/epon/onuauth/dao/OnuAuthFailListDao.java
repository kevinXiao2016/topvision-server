/***********************************************************************
 * $Id: OnuAuthFailDao.java,v1.0 2015年4月18日 上午10:36:11 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onuauth.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.epon.onuauth.domain.OnuAuthFail;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author loyal
 * @created @2015年4月18日-上午10:36:11
 * 
 */
public interface OnuAuthFailListDao extends BaseEntityDao<Object> {
    /**
     * 获取认证失败列表
     * 
     * @param queryMap
     * @return
     */
    List<OnuAuthFail> selectOnuAuthFailList(Map<String, Object> queryMap);

    /**
     * 获取认证失败ONU数量
     * 
     * @param queryMap
     * @return
     */
    Long selectOnuAuthFailCount(Map<String, Object> queryMap);

    OnuAuthFail getOnuAuthFailObject(Long entityId, Long onuIndex);

}
