/***********************************************************************
 * $Id: IpqamServiceImpl.java,v1.0 2016年5月3日 下午2:45:37 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ipqam.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.ipqam.dao.IpqamDao;
import com.topvision.ems.cmc.ipqam.domain.Ipqam;
import com.topvision.ems.cmc.ipqam.domain.Program;
import com.topvision.ems.cmc.ipqam.service.IpqamService;

/**
 * @author loyal
 * @created @2016年5月3日-下午2:45:37
 * 
 */
@Service("ipqamService")
public class IpqamServiceImpl implements IpqamService {
    @Autowired
    private IpqamDao ipqamDao;

    @Override
    public List<Ipqam> getEqamList(Long cmcId) {
        return ipqamDao.selectEqamList(cmcId);
    }

    @Override
    public List<Program> getProgramList(Map<String, Object> queryMap) {
        return ipqamDao.selectProgramList(queryMap);
    }

    @Override
    public Long getProgramListCount(Long cmcId) {
        return ipqamDao.selectProgramListCount(cmcId);
    }

    @Override
    public List<Program> getOltEqamList(Map<String, Object> queryMap) {
        return ipqamDao.selectOltEqamList(queryMap);
    }

    @Override
    public Long getOltProgramListCount(Map<String, Object> queryMap) {
        return ipqamDao.selectOltEqamListCount(queryMap);
    }

    @Override
    public boolean loadEqamSupportUnderOlt(Long entityId) {
        // 获取entityId下联CCMTS有多少支持EQAM
        Integer count = ipqamDao.selectEqamSupportCountUnderOlt(entityId);
        return count != null && count != 0;
    }

    @Override
    public boolean loadEqamSupport(Long entityId) {
        Integer supportState = ipqamDao.selectEqamSupport(entityId);
        return supportState != null && supportState == 1;
    }

}
