/***********************************************************************
 * $Id: CommandSendDaoImpl.java,v1.0 2014年7月17日 下午4:10:08 $
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

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.dao.CommandSendDao;
import com.topvision.ems.network.domain.SendConfigEntity;
import com.topvision.ems.network.domain.SendConfigResult;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.util.CurrentRequest;

/**
 * @author loyal
 * @created @2014年7月17日-下午4:10:08
 * 
 */
@Repository("commandSendDao")
public class CommandSendDaoImpl extends MyBatisDaoSupport<SendConfigEntity> implements CommandSendDao {

    @Override
    public List<Entity> selectEntityList(Map<String, Object> map, int start, int limit) {
        map.put("start", start);
        map.put("limit", limit);
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace("selectEntityList"), map);
    }

    @Override
    public Long selectEntityListNum(Map<String, Object> map) {
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectOne(getNameSpace("selectEntityListNum"), map);
    }

    @Override
    public List<SendConfigEntity> getUncompleteEntitys() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace("getUncompleteEntitys"), map);
    }

    @Override
    public List<SendConfigEntity> getFailedEntitys() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace("selectFailedEntitys"), map);
    }

    @Override
    public List<SendConfigEntity> getUnstartEntitys() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace("selectUnstartEntitys"), map);
    }

    @Override
    public void updateSendConfigEntity(SendConfigResult sendConfigResult) {
        getSqlSession().update("updateSendConfigEntity", sendConfigResult);
    }

    @Override
    public void insertSendConfigEntity(List<Long> entityIdList) {
        SqlSession session = getBatchSession();
        try {
            for (int i = 0; i < entityIdList.size(); i++) {
                getSqlSession().delete("deleteSendConfigEntity", entityIdList.get(i));
                getSqlSession().insert("insertSendConfigEntity", entityIdList.get(i));
            }
            session.commit();
        } catch (Exception e) {
            logger.error("", e);
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void deleteSendConfigEntity(List<Long> entityIdList) {
        SqlSession session = getBatchSession();
        try {
            for (int i = 0; i < entityIdList.size(); i++) {
                getSqlSession().delete("deleteSendConfigEntity", entityIdList.get(i));
            }
            session.commit();
        } catch (Exception e) {
            logger.error("", e);
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public List<SendConfigEntity> selectCommandSendEntityList(Map<String, Object> map, int start, int limit) {
        map.put("start", start);
        map.put("limit", limit);
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace("selectCommandSendEntityList"), map);
    }

    @Override
    public Long selectCommandSendEntityListNum(Map<String, Object> map) {
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectOne(getNameSpace("selectCommandSendEntityListNum"), map);
    }

    @Override
    public SendConfigEntity selectCommandSendEntityByEntityId(Long entityId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        map.put("entityId", entityId);
        return getSqlSession().selectOne(getNameSpace("selectCommandSendEntityByEntityId"), map);
    }

    @Override
    public List<String> selectEntityFolder(Long entityId) {
        return getSqlSession().selectList(getNameSpace("selectEntityFolder"), entityId);
    }

    @Override
    public String selectSendConfigResult(Long entityId) {
        return getSqlSession().selectOne(getNameSpace("selectSendConfigResult"), entityId);
    }

    @Override
    public void updateCommandSendStatue(List<SendConfigEntity> sendConfigEntitys, Integer status) {
        SqlSession batchSession = getBatchSession();
        try {
            for (SendConfigEntity sendConfigEntity : sendConfigEntitys) {
                if (sendConfigEntity.getState() == SendConfigEntity.CONFIGERROR) {
                    continue;
                }
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("entityId", sendConfigEntity.getEntityId());
                map.put("state", status);
                batchSession.update(getNameSpace() + "updateCommandSendStatue", map);
            }
            batchSession.commit();
        } catch (Exception e) {
            batchSession.rollback();
        } finally {
            batchSession.close();
        }
    }

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.network.domain.SendConfigEntity";
    }

}
