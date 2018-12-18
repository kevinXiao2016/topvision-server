package com.topvision.ems.epon.onu.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.onu.dao.OnuTagDao;
import com.topvision.ems.epon.onu.domain.OnuTag;
import com.topvision.ems.epon.onu.service.OnuTagService;
import com.topvision.framework.service.BaseService;

/**
 * 
 * @author w1992wishes
 * @created @2017年12月21日-下午2:00:23
 *
 */
@Service("onuTagService")
public class OnuTagServiceImpl extends BaseService implements OnuTagService {

    @Autowired
    private OnuTagDao onuTagDao;

    @Override
    public List<OnuTag> getOnuTags(Map<String, Object> queryMap) {
        return onuTagDao.selectOnuTags(queryMap);
    }

    @Override
    public Integer getOnuTagsCount() {
        return onuTagDao.selectOnuTagsCount();
    }

    @Override
    public void createOnuTag(OnuTag onuTag) {
        onuTagDao.insertEntity(onuTag);
    }

    @Override
    public void modifyOnuTag(OnuTag onuTag) {
        onuTagDao.updateEntity(onuTag);
    }

    @Override
    public void deleteOnuTag(OnuTag onuTag) {
        onuTagDao.deleteByPrimaryKey(onuTag);
    }

}
