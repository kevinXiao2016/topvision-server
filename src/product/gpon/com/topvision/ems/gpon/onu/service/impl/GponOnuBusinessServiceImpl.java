package com.topvision.ems.gpon.onu.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.gpon.onu.dao.GponOnuBusinessDao;
import com.topvision.ems.gpon.onu.domain.GponOnuBusinessInfo;
import com.topvision.ems.gpon.onu.service.GponOnuBusinessService;

/**
 * 
 * @author CWQ
 * @created @2017年12月25日-下午2:32:35
 *
 */
@Service
public class GponOnuBusinessServiceImpl implements GponOnuBusinessService {

    @Autowired
    private GponOnuBusinessDao gponOnuBusinessDao;

    @Override
    public List<GponOnuBusinessInfo> queryGponOnuBusinessList(Map<String, Object> queryMap) {
        return gponOnuBusinessDao.selectGponOnuBusinessList(queryMap);
    }

    @Override
    public Integer queryGponOnuBusinessCount(Map<String, Object> queryMap) {
        return gponOnuBusinessDao.selectGponOnuBusinessCount(queryMap);
    }

}
