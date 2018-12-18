/***********************************************************************
 * $Id: BaiduMapDaoImpl.java,v1.0 2015年9月16日 上午10:43:13 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.baidu.dao.mybatis;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.baidu.dao.BaiduMapDao;
import com.topvision.ems.baidu.domain.BaiduEntity;
import com.topvision.ems.google.domain.GoogleEntity;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author loyal
 * @created @2015年9月16日-上午10:43:13
 * 
 */
@Repository("baiduMapDao")
public class BaiduMapDaoImpl extends MyBatisDaoSupport<BaiduEntity> implements BaiduMapDao {
    @Override
    protected String getDomainName() {
        return BaiduEntity.class.getName();
    }

    @Override
    public void insertBaiduEntity(BaiduEntity baiduEntity) {
        getSqlSession().insert(getNameSpace("insertBaiduEntity"), baiduEntity);
    }

    @Override
    public List<BaiduEntity> selectBaiduEntity() {
        return getSqlSession().selectList(getNameSpace("selectBaiduEntity"));
    }

    @Override
    public void updateBaiduEntity(BaiduEntity baiduEntity) {
        getSqlSession().update(getNameSpace("updateBaiduEntity"), baiduEntity);
    }

    @Override
    public void deleteBaiduEntity(Long entityId) {
        getSqlSession().delete(getNameSpace("deleteBaiduEntity"), entityId);
    }

    @Override
    public BaiduEntity selectBaiduEntityByEntityId(Long entityId) {
        return getSqlSession().selectOne(getNameSpace("selectBaiduEntityByEntityId"), entityId);
    }

    @Override
    public List<BaiduEntity> searchEntity(Map<String, Object> queryMap) {
        return getSqlSession().selectList(getNameSpace("searchEntity"), queryMap);
    }

    @Override
    public void batchInsertOrUpdateBaiduMap(List<BaiduEntity> baiduEntities) {
        SqlSession session = getBatchSession();
        try {
            for (BaiduEntity baiduEntity : baiduEntities) {
                session.insert(getNameSpace("batchInsertOrUpdateBaiduEntity"), baiduEntity);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

}
