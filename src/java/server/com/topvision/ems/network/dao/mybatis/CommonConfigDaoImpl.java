/***********************************************************************
 * $Id: CommonConfigDaoImpl.java,v1.0 2014年7月22日 下午7:34:33 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.network.dao.CommonConfigDao;
import com.topvision.ems.network.domain.SendConfigEntity;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author loyal
 * @created @2014年7月22日-下午7:34:33
 *
 */
@Repository("commonConfigDao")
public class CommonConfigDaoImpl extends MyBatisDaoSupport<SendConfigEntity> implements CommonConfigDao {
    @Override
    protected String getDomainName() {
        return SendConfigEntity.class.getName();
    }

    @Override
    public List<String> getCommonConfigs(Long type) {
        return getSqlSession().selectList(getNameSpace("getCommonConfigs"), type);
    }
    
    @Override
    public List<String> getCommonConfigs(Long type, Long folderId){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("type", type);
        map.put("folderId", folderId);
        return getSqlSession().selectList(getNameSpace("getCommonConfigsWithFolder"), map);
    }
    
    @Override
    public void clearCommonConfig(Long type) {
        getSqlSession().delete(getNameSpace("clearCommonConfig"), type);
    }
    
    @Override
    public void clearCommonConfig(Long type, Long folderId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("type", type);
        map.put("folderId", folderId);
        getSqlSession().delete(getNameSpace("clearCommonConfigWithFolder"), map);
    }

    @Override
    public void addCommonConfig(String config, Long type) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("config", config);
        map.put("type", type);
        getSqlSession().insert(getNameSpace("addCommonConfig"), map);
    }
    
    @Override
    public void addCommonConfig(String config, Long type, Long folderId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("config", config);
        map.put("type", type);
        map.put("folderId", folderId);
        getSqlSession().insert(getNameSpace("addCommonConfigWithFolder"), map);
    }

    @Override
    public void addCommonConfig(final List<String> configs, final Long type) {
        exeBatch(new BatchExecutor() {
            @Override
            public void exe(SqlSession session) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("type", type);
                for (int i = 0; i < configs.size(); i++) {
                    map.remove("config");
                    map.put("config", configs.get(i));
                    session.insert(getNameSpace("addCommonConfig"), map);
                }
            }
        });
    }
    
    @Override
    public void addCommonConfig(final List<String> configs, final Long type, final Long folderId) {
        exeBatch(new BatchExecutor() {
            @Override
            public void exe(SqlSession session) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("type", type);
                map.put("folderId", folderId);
                for (int i = 0; i < configs.size(); i++) {
                    map.remove("config");
                    map.put("config", configs.get(i));
                    session.insert(getNameSpace("addCommonConfigWithFolder"), map);
                }
            }
        });
    }

}
