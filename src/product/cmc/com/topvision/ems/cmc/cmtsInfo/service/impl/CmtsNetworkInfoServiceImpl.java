/***********************************************************************
 * $Id: CmtsNetworkInfoServiceImpl.java,v1.0 2017年8月2日 下午1:49:59 $
 * 
 * @author: ls
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.cmtsInfo.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.cmtsInfo.dao.CmtsInfoListDao;
import com.topvision.ems.cmc.cmtsInfo.domain.CmOutPowerNotInRange;
import com.topvision.ems.cmc.cmtsInfo.domain.CmRePowerNotInRange;
import com.topvision.ems.cmc.cmtsInfo.domain.CmtsInfoNotInRange;
import com.topvision.ems.cmc.cmtsInfo.domain.CmtsInfoThreshold;
import com.topvision.ems.cmc.cmtsInfo.domain.CmtsNetworkInfo;
import com.topvision.ems.cmc.cmtsInfo.domain.DownSnrAvgNotInRange;
import com.topvision.ems.cmc.cmtsInfo.domain.UpSnrNotInRange;
import com.topvision.ems.cmc.cmtsInfo.service.CmtsNetworkInfoService;
import com.topvision.framework.service.BaseService;

/**
 * @author ls
 * @created @2017年8月2日-下午1:49:59
 *
 */
@Service("cmtsNetworkInfoService")
public class CmtsNetworkInfoServiceImpl extends BaseService implements CmtsNetworkInfoService {
    @Autowired
    CmtsInfoListDao cmtsInfoListDao;
    
    @Override
    public List<CmtsNetworkInfo> queryCmtsInfoList(Map<String, Object> cmcQueryMap) {        
        return cmtsInfoListDao.queryForCmtsInfoList(cmcQueryMap);
    }
    
    @Override
    public List<CmtsInfoNotInRange> queryCmtsNotInRange(Map<String, Object> cmcQueryMap) {
        return cmtsInfoListDao.queryCmtsNotInRange(cmcQueryMap);
    }

    @Override
    public List<CmOutPowerNotInRange> queryCmOutPowerNotInRange(Map<String, Object> cmcQueryMap) {
        return cmtsInfoListDao.queryCmOutPowerNotInRange(cmcQueryMap);
    }

    @Override
    public List<CmRePowerNotInRange> queryCmRePowerNotInRange(Map<String, Object> cmcQueryMap) {
        return cmtsInfoListDao.queryCmRePowerNotInRange(cmcQueryMap);
    }

    @Override
    public List<UpSnrNotInRange> queryUpSnrNotInRange(Map<String, Object> cmcQueryMap) {
        return cmtsInfoListDao.queryUpSnrNotInRange(cmcQueryMap);
    }

    @Override
    public List<DownSnrAvgNotInRange> queryDownSnrAvgNotInRange(Map<String, Object> cmcQueryMap) {
        return cmtsInfoListDao.queryDownSnrAvgNotInRange(cmcQueryMap);
    }

    @Override
    public void modifyLocalThreshold(Map<String, Object> cmcQueryMap) {
        cmtsInfoListDao.saveLocalThreshold(cmcQueryMap);
    }

    @Override
    public CmtsInfoThreshold getLocalThreshold() {
        return cmtsInfoListDao.getLocalThreshold();
    }

    @Override
    public Long getCmcNum(Map<String, Object> cmcQueryMap) {
        return cmtsInfoListDao.getCmcNum(cmcQueryMap);
    }

}
