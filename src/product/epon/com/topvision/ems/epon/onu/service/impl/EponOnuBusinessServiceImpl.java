package com.topvision.ems.epon.onu.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.onu.dao.EponOnuBusinessDao;
import com.topvision.ems.epon.onu.domain.EponOnuBusinessInfo;
import com.topvision.ems.epon.onu.service.EponOnuBusinessService;
import com.topvision.framework.service.BaseService;

@Service
public class EponOnuBusinessServiceImpl extends BaseService implements EponOnuBusinessService {

    @Autowired
    private EponOnuBusinessDao eponOnuBusinessDao;

    @Override
    public List<EponOnuBusinessInfo> queryEponOnuBusinessList(Map<String, Object> queryMap) {
        return eponOnuBusinessDao.selectEponOnuBusinessList(queryMap);
    }

    @Override
    public Integer queryEponOnuBusinessCount(Map<String, Object> queryMap) {
        return eponOnuBusinessDao.selectEponOnuBusinessCount(queryMap);
    }

}
