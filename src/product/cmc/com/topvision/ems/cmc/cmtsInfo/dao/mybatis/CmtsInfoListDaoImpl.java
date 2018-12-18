/***********************************************************************
 * $Id: CmtsInfoListDaoImpl.java,v1.0 2017年8月2日 下午1:57:05 $
 * 
 * @author: ls
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.cmtsInfo.dao.mybatis;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.cmc.cmtsInfo.dao.CmtsInfoListDao;
import com.topvision.ems.cmc.cmtsInfo.domain.CmOutPowerNotInRange;
import com.topvision.ems.cmc.cmtsInfo.domain.CmRePowerNotInRange;
import com.topvision.ems.cmc.cmtsInfo.domain.CmtsInfoNotInRange;
import com.topvision.ems.cmc.cmtsInfo.domain.CmtsInfoThreshold;
import com.topvision.ems.cmc.cmtsInfo.domain.CmtsNetworkInfo;
import com.topvision.ems.cmc.cmtsInfo.domain.DownSnrAvgNotInRange;
import com.topvision.ems.cmc.cmtsInfo.domain.UpSnrNotInRange;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.util.CurrentRequest;

/**
 * @author ls
 * @created @2017年8月2日-下午1:57:05
 *
 */
@Repository("cmtsInfoListDao")
public class CmtsInfoListDaoImpl extends MyBatisDaoSupport<CmtsNetworkInfo> implements CmtsInfoListDao{

    @Override
    public List<CmtsNetworkInfo> queryForCmtsInfoList(Map<String, Object> cmcQueryMap) {
        cmcQueryMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        Long userId = null;
        try {
            userId = CurrentRequest.getCurrentUser().getUserId();
        } catch (Exception e) {
            logger.debug("CurrentRequest.getCurrentUser().getUserId() fail");
        }
        cmcQueryMap.put("userId", userId);
        return getSqlSession().selectList(getNameSpace() + "queryForCmtsInfoList2", cmcQueryMap);
    }

    @Override
    protected String getDomainName() {
        return CmtsNetworkInfo.class.getName();
    }

    @Override
    public List<CmtsInfoNotInRange> queryCmtsNotInRange(Map<String, Object> cmcQueryMap) {
        return getSqlSession().selectList(getNameSpace() + "queryForCmtsInfoNotInRange", cmcQueryMap);
    }

    @Override
    public List<CmOutPowerNotInRange> queryCmOutPowerNotInRange(Map<String, Object> cmcQueryMap) {
        return getSqlSession().selectList(getNameSpace() + "queryCmOutPowerNotInRange", cmcQueryMap);
    }

    @Override
    public List<CmRePowerNotInRange> queryCmRePowerNotInRange(Map<String, Object> cmcQueryMap) {
        return getSqlSession().selectList(getNameSpace() + "queryCmRePowerNotInRange", cmcQueryMap);
    }

    @Override
    public List<UpSnrNotInRange> queryUpSnrNotInRange(Map<String, Object> cmcQueryMap) {
        return getSqlSession().selectList(getNameSpace() + "queryUpSnrNotInRange", cmcQueryMap);
    }

    @Override
    public List<DownSnrAvgNotInRange> queryDownSnrAvgNotInRange(Map<String, Object> cmcQueryMap) {
        return getSqlSession().selectList(getNameSpace() + "queryDownSnrAvgNotInRange", cmcQueryMap);
    }

    @Override
    public void saveLocalThreshold(Map<String, Object> cmcQueryMap) {
        getSqlSession().update(getNameSpace("updateLocalThreshold"), cmcQueryMap);
    }

    @Override
    public CmtsInfoThreshold getLocalThreshold() {
        return getSqlSession().selectOne(getNameSpace("selectLocalThreshold"));
    }

    @Override
    public Long getCmcNum(Map<String, Object> cmcQueryMap) {
        return (Long) getSqlSession().selectOne(getNameSpace() + "queryCmcInfoListCount", cmcQueryMap);
    }

}
