package com.topvision.ems.epon.onu.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.onu.dao.OnuLinkDao;
import com.topvision.ems.epon.onu.domain.OnuLinkInfo;
import com.topvision.ems.epon.onu.domain.OnuLinkThreshold;
import com.topvision.ems.epon.onu.service.OnuLinkService;
import com.topvision.framework.service.BaseService;

/**
 * 
 * @author CWQ
 * @created @2017年12月21日-下午4:10:12
 *
 */
@Service
public class OnuLinkServiceImpl extends BaseService implements OnuLinkService {

    @Autowired
    private OnuLinkDao onuLinkDao;

    @Override
    public List<OnuLinkInfo> queryOnuLinkList(Map<String, Object> queryMap) {
        return onuLinkDao.selectOnuLinkList(queryMap);
    }

    @Override
    public Integer queryOnuLinkListCount(Map<String, Object> queryMap) {
        return onuLinkDao.selectOnuLinkListCount(queryMap);
    }

    @Override
    public List<OnuLinkThreshold> queryOnuLinkThreshold() {
        return onuLinkDao.selectOnuLinkThreshold();
    }

    @Override
    public OnuLinkInfo getOnuLinkInfo(Long onuId) {
        return onuLinkDao.selectOnuLinkInfo(onuId);
    }

}
