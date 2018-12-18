/***********************************************************************
 * $Id: CmcAclDaoImpl.java,v1.0 2013-5-3 下午01:40:57 $
 * 
 * @author: lzs
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ccmts.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.cmc.ccmts.dao.CmcListDao;
import com.topvision.ems.cmc.ccmts.domain.EntityPonRelation;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.util.CurrentRequest;

/**
 * @author lzs
 * @created @2013-5-3-下午01:40:57
 *
 */
@Repository("cmcListDao")
public class CmcListDaoImpl extends MyBatisDaoSupport<CmcAttribute> implements CmcListDao {
    @Override
    public List<EntityPonRelation> getEntityPonInfoList() {
        HashMap<String, Object> authorityHashMap = new HashMap<String, Object>();
        authorityHashMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace() + "getEntityPonInfoList", authorityHashMap);
    }

    @Override
    public List<CmcAttribute> queryCmc8800BList(Map<String, Object> cmcQueryMap) {
        cmcQueryMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        /*//add by fanzidong,需要在查询前格式化MAC地址
        String cmcMac = (String) cmcQueryMap.get("cmcMac");
        cmcMac = MacUtils.formatQueryMac(cmcMac);
        cmcQueryMap.put("cmcMac", cmcMac);
        */
        return getSqlSession().selectList(getNameSpace() + "queryCmc8800BList", cmcQueryMap);
    }

    @Override
    public Long queryCmc8800BNum(Map<String, Object> cmcQueryMap) {
        /*
        //add by fanzidong,需要在查询前格式化MAC地址
        String cmcMac = (String) cmcQueryMap.get("cmcMac");
        cmcMac = MacUtils.formatQueryMac(cmcMac);
        cmcQueryMap.put("cmcMac", cmcMac);
        */
        cmcQueryMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectOne(getNameSpace() + "queryCmc8800BNum", cmcQueryMap);
    }

    @Override
    public List<CmcAttribute> queryCmcList(Map<String, Object> cmcQueryMap, int start, int limit) {
        cmcQueryMap.put("start", start);
        cmcQueryMap.put("limit", limit);
        cmcQueryMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        //add by fanzidong,需要在查询前格式化MAC地址
        String cmcMac = (String) cmcQueryMap.get("cmcMac");
        cmcMac = MacUtils.formatQueryMac(cmcMac);
        cmcQueryMap.put("cmcMac", cmcMac);
        return getSqlSession().selectList(getNameSpace() + "queryCmcList", cmcQueryMap);
    }

    @Override
    public List<CmcAttribute> queryForCmcList(Map<String, Object> cmcQueryMap) {
        cmcQueryMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        Long userId = null;
        try {
            userId = CurrentRequest.getCurrentUser().getUserId();
        } catch (Exception e) {
            logger.debug("CurrentRequest.getCurrentUser().getUserId() fail");
        }
        cmcQueryMap.put("userId", userId);
        return getSqlSession().selectList(getNameSpace() + "queryForCmcList", cmcQueryMap);
    }

    public Long getCmcNum(Map<String, Object> cmcQueryMap) {
        cmcQueryMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        Long userId = null;
        try {
            userId = CurrentRequest.getCurrentUser().getUserId();
        } catch (Exception e) {
            logger.debug("CurrentRequest.getCurrentUser().getUserId() fail");
        }
        cmcQueryMap.put("userId", userId);
        /*
        //add by fanzidong,需要在查询前格式化MAC地址
        String cmcMac = (String) cmcQueryMap.get("cmcMac");
        cmcMac = MacUtils.formatQueryMac(cmcMac);
        cmcQueryMap.put("cmcMac", cmcMac);
        */
        return (Long) getSqlSession().selectOne(getNameSpace() + "queryCmcListCount", cmcQueryMap);
    }

    @Override
    public Map<Object, Object> getAllOnuIdToCmcNmnameMap(Long entityId) {
        //TODO 与原方法的参数不一致,需要测试
        return (HashMap<Object, Object>) getSqlSession().selectMap(getNameSpace() + "getAllOnuIdToCmcNmnameMap",
                entityId, "onuid");
    }

    @Override
    public List<CmcAttribute> getAllCmcEntityList() {
        return getSqlSession().selectList(getNameSpace() + "getAllCmcEntityList");
    }

    @Override
    protected String getDomainName() {
        return CmcAttribute.class.getName();
    }

}
