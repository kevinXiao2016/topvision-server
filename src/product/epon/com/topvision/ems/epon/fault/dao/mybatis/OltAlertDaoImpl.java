/***********************************************************************
 * $Id: OltAlertDaoImpl.java,v1.0 2013-10-26 上午09:54:41 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.fault.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.domain.OltAlertInstance;
import com.topvision.ems.epon.domain.OltAlertTop;
import com.topvision.ems.epon.fault.dao.OltAlertDao;
import com.topvision.ems.epon.fault.domain.OltTopAlarmCodeMask;
import com.topvision.ems.epon.fault.domain.OltTopAlarmInstanceMask;
import com.topvision.ems.epon.fault.domain.OltTrapConfig;
import com.topvision.ems.fault.domain.AlertType;
import com.topvision.ems.fault.domain.Level;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author lizongtian
 * @created @2013-10-26-上午09:54:41
 *
 */
@Repository("oltAlertDao")
public class OltAlertDaoImpl extends MyBatisDaoSupport<Object> implements OltAlertDao {

    @Override
    public List<OltTopAlarmCodeMask> getOltAlertCodeMask(Long entityId, Integer type) {
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("entityId", entityId);
        paramsMap.put("type", type);
        return getSqlSession().selectList(getNameSpace("getOltAlertCodeMask"), paramsMap);
    }

    @Override
    public List<AlertType> getOltAlertAvailableType(Long entityId, Integer type) {
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("entityId", entityId);
        paramsMap.put("type", type);
        return getSqlSession().selectList(getNameSpace("getOltAlertAvailableType"), paramsMap);
    }

    @Override
    public void insertOltAlertCodeMask(OltTopAlarmCodeMask oltTopAlarmCodeMask) {
        getSqlSession().insert(getNameSpace("insertOltAlertCodeMask"), oltTopAlarmCodeMask);
    }

    @Override
    public void deleteOltAlertCodeMask(Long entityId, Long codeMaskIndex) {
        Map<String, Long> paramMap = new HashMap<String, Long>();
        paramMap.put("entityId", entityId);
        paramMap.put("codeMaskIndex", codeMaskIndex);
        getSqlSession().delete(getNameSpace("deleteOltAlertCodeMask"), paramMap);
    }

    @Override
    public List<OltTopAlarmInstanceMask> getOltAlertInstanceMask(Long entityId) {
        return getSqlSession().selectList(getNameSpace("getOltAlertInstanceMask"), entityId);
    }

    @Override
    public List<OltAlertInstance> getOltAlertAvailableInstance(Long entityId) {
        return getSqlSession().selectList(getNameSpace("getOltAlertAvailableInstance"), entityId);
    }

    @Override
    public void insertOltAlertInstanceMask(OltTopAlarmInstanceMask oltTopAlarmInstanceMask) {
        getSqlSession().insert(getNameSpace("insertOltAlertInstanceMask"), oltTopAlarmInstanceMask);
    }

    @Override
    public void deleteOltAlertInstanceMask(Long entityId, Long instanceMaskIndex) {
        Map<String, Long> paramMap = new HashMap<String, Long>();
        paramMap.put("entityId", entityId);
        paramMap.put("instanceMaskIndex", instanceMaskIndex);
        getSqlSession().delete(getNameSpace("deleteOltAlertInstanceMask"), paramMap);
    }

    @Override
    public List<OltTrapConfig> getOltTrapConfig(Long entityId) {
        return getSqlSession().selectList(getNameSpace("getOltTrapConfig"), entityId);
    }

    @Override
    public void insertOltTrapConfig(OltTrapConfig oltTrapConfig) {
        getSqlSession().insert(getNameSpace("insertOltTrapConfig"), oltTrapConfig);
    }

    @Override
    public void insertOrUpdateOltTrapConfig(OltTrapConfig oltTrapConfig) {
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("entityId", oltTrapConfig.getEntityId());
        paramsMap.put("eponManagementAddrName", oltTrapConfig.getEponManagementAddrName());
        OltTrapConfig trapConfig = getSqlSession().selectOne(getNameSpace("getOltTrapByPrimaryKey"), paramsMap);
        if (trapConfig != null) {
            // update
            getSqlSession().update(getNameSpace("updateOltTrapConfig"), oltTrapConfig);
        } else {
            // insert
            getSqlSession().insert(getNameSpace("insertOltTrapConfig"), oltTrapConfig);
        }
    }

    @Override
    public void deleteOltTrapConfig(OltTrapConfig oltTrapConfig) {
        getSqlSession().delete(getNameSpace("deleteOltTrapConfig"), oltTrapConfig);
    }

    @Override
    public List<String> getAvailableEntityIp() {
        return getSqlSession().selectList(getNameSpace("getAvailableEntityIp"));
    }

    @Override
    public List<Level> getAlertSeverity() {
        return getSqlSession().selectList(getNameSpace("getAlertSeverity"));
    }

    @Override
    public List<AlertType> getAlertType() {
        return getSqlSession().selectList(getNameSpace("getAlertType"));
    }

    @Override
    public void batchInsertOltAlertCodeMask(Long entityId, final List<OltTopAlarmCodeMask> oltTopAlarmCodeMaskList) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace("deleteAllAlertCodeMask"), entityId);
            for (OltTopAlarmCodeMask oltTopAlarmCodeMask : oltTopAlarmCodeMaskList) {
                sqlSession.insert(getNameSpace("insertOltAlertCodeMask"), oltTopAlarmCodeMask);
            }
            sqlSession.commit();
        } catch (Exception e) {
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public void batchInsertOltAlertInstanceMask(Long entityId,
            final List<OltTopAlarmInstanceMask> oltTopAlarmInstanceMaskList) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace("deleteAllAlertInstanceMask"), entityId);
            for (OltTopAlarmInstanceMask oltTopAlarmInstanceMask : oltTopAlarmInstanceMaskList) {
                sqlSession.insert(getNameSpace("insertOltAlertInstanceMask"), oltTopAlarmInstanceMask);
            }
            sqlSession.commit();
        } catch (Exception e) {
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public void batchInsertOltTrapConfig(Long entityId, final List<OltTrapConfig> oltTrapConfigList) {
        getSqlSession().delete(getNameSpace("deleteAllTrapConfig"), entityId);
        for (OltTrapConfig oltTrapConfig : oltTrapConfigList) {
            insertOrUpdateOltTrapConfig(oltTrapConfig);
        }
    }

    @Override
    public List<OltAlertTop> getOltDeviceAlertTop() {
        return getSqlSession().selectList(getNameSpace("getOltDeviceAlertTop"));
    }
    
    @Override
    protected String getDomainName() {
        return "com.topvision.ems.epon.fault.domain.OltAlert";
    }
}
