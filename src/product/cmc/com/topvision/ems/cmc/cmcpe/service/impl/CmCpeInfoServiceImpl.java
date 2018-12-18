package com.topvision.ems.cmc.cmcpe.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.cmcpe.dao.CmCpeInfoDao;
import com.topvision.ems.cmc.cmcpe.domain.CmCpeInfo;
import com.topvision.ems.cmc.cmcpe.service.CmCpeInfoService;
import com.topvision.framework.service.BaseService;
@Service("cmCpeInfoService")
public class CmCpeInfoServiceImpl extends BaseService implements CmCpeInfoService {
    
    @Autowired
    private CmCpeInfoDao cmCpeInfoDao;
    
    @Override
    public List<CmCpeInfo> getCmCpeList(Map<String, Object> queryMap) {
        return cmCpeInfoDao.selectCmCpeList(queryMap);
    }

    @Override
    public Integer getCmNum(Map<String, Object> queryMap) {
        return cmCpeInfoDao.selectCmCpeListNum(queryMap);
    }
    


}
