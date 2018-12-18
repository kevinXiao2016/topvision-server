/***********************************************************************
 * $Id: PerfTargetDaoImpl.java,v1.0 2013-8-1 上午09:38:05 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.performance.dao.PerfTargetDao;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.util.CurrentRequest;

/**
 * @author lizongtian
 * @created @2013-8-1-上午09:38:05
 * 
 * 
 */
@Repository("perfTargetDao")
public class PerfTargetDaoImpl extends MyBatisDaoSupport<Object> implements PerfTargetDao {

    @Override
    public List<String> getPerfTargetGroupsByDeviceType(Long entityId) {
        return getSqlSession().selectList(getNameSpace("getPerfTargetGroupsByDeviceType"), entityId);
    }

    @Override
    public List<String> getOnuPerfTargetGroupsByDeviceType(Long entityType) {
        return getSqlSession().selectList(getNameSpace("getOnuPerfTargetGroupsByDeviceType"), entityType);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.dao.PerfTargetDao#getPerfTargetNameList(java.lang.Integer)
     */
    @Override
    public List<String> getPerfTargetNameList(Long entityType) {
        return getSqlSession().selectList(getNameSpace("getPerfTargetNameList"), entityType);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.dao.PerfTargetDao#getPerfNamesByGroup(java.lang.String)
     */
    @Override
    public List<String> getPerfNamesByGroup(String groupName, Long entityId) {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("groupName", groupName);
        queryMap.put("entityId", entityId);
        return getSqlSession().selectList(getNameSpace("getPerfNamesByGroup"), queryMap);
    }

    @Override
    public List<String> getOnuPerfNamesByGroup(String groupName, Long entityType) {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("groupName", groupName);
        queryMap.put("entityType", entityType);
        return getSqlSession().selectList(getNameSpace("getOnuPerfNamesByGroup"), queryMap);
    }

    @Override
    public List<Long> getEntityIdList(Integer type) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        map.put("type", type);
        return getSqlSession().selectList(getNameSpace("getEntityIdList"), map);
    }

    @Override
    public List<Entity> loadDevicesByMap(Map<String, Object> queryMap) {
        queryMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace("loadDevicesByMap"), queryMap);
    }

    @Override
    public Integer loadDevicesNumByMap(Map<String, Object> queryMap) {
        return getSqlSession().selectOne(getNameSpace("loadDevicesNumByMap"), queryMap);
    }

    @Override
    public String selectPerfTargetCategory(Long typeId, String perfTarget) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("typeId", typeId);
        map.put("perfTarget", perfTarget);
        return getSqlSession().selectOne(getNameSpace("selectPerfTargetCategory"), map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.mybatis.MyBatisDaoSupport#getDomainName()
     */
    @Override
    protected String getDomainName() {
        return "com.topvision.ems.performance.domain.PerfTarget";
    }

}
