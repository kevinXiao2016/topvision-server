/***********************************************************************
 * $Id: OnuAuthFailListServiceImpl.java,v1.0 2015年4月18日 上午10:33:50 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onuauth.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.onuauth.dao.OnuAuthFailListDao;
import com.topvision.ems.epon.onuauth.domain.OnuAuthFail;
import com.topvision.ems.epon.onuauth.service.OnuAuthFailListService;
import com.topvision.framework.service.BaseService;

/**
 * @author loyal
 * @created @2015年4月18日-上午10:33:50
 * 
 */
@Service("onuAuthFailListService")
public class OnuAuthFailListServiceImpl extends BaseService implements OnuAuthFailListService {
    @Autowired
    private OnuAuthFailListDao onuAuthFailListDao;

    @Override
    public List<OnuAuthFail> getOnuAuthFailList(Map<String, Object> queryMap) {
        return onuAuthFailListDao.selectOnuAuthFailList(queryMap);
    }

    @Override
    public Long getOnuAuthFailCount(Map<String, Object> queryMap) {
        return onuAuthFailListDao.selectOnuAuthFailCount(queryMap);
    }

    @Override
    public OnuAuthFail getOnuAuthFailObject(Long entityId, Long onuIndex) {
        return onuAuthFailListDao.getOnuAuthFailObject(entityId, onuIndex);
    }

}
