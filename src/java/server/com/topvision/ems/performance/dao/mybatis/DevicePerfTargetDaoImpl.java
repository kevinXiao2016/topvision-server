/***********************************************************************
 * $Id: DevicePerfTargetDaoImpl.java,v1.0 2014-3-12 下午2:19:11 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.performance.dao.DevicePerfTargetDao;
import com.topvision.ems.performance.domain.DevicePerfTarget;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.util.CurrentRequest;

/**
 * @author flack
 * @created @2014-3-12-下午2:19:11
 *
 */
@Repository("devicePerfTargetDao")
public class DevicePerfTargetDaoImpl extends MyBatisDaoSupport<Object> implements DevicePerfTargetDao {

    @Override
    protected String getDomainName() {
        return "devicePerfTarget";
    }

    @Override
    public List<Long> queryDevice(Map<String, Object> paramsMap) {
        if (paramsMap.get("folderId") != null) {
            paramsMap.put("authority", CurrentRequest.getAuthorityViewName((Integer) paramsMap.get("folderId")));
        } else {
            paramsMap.put("authority", CurrentRequest.getUserAuthorityViewName());
        }
        return this.getSqlSession().selectList(getNameSpace("queryDevice"), paramsMap);
    }

    @Override
    public List<DevicePerfTarget> queryDevicePerfList(Map<String, Object> paramsMap) {
        return this.getSqlSession().selectList(getNameSpace("queryDevicePerfList"), paramsMap);
    }

    @Override
    public int queryDeviceNum(Map<String, Object> paramsMap) {
        if (paramsMap.get("folderId") != null) {
            paramsMap.put("authority", CurrentRequest.getAuthorityViewName((Integer) paramsMap.get("folderId")));
        } else {
            paramsMap.put("authority", CurrentRequest.getUserAuthorityViewName());
        }
        return this.getSqlSession().selectOne(getNameSpace("queryDeviceNum"), paramsMap);
    }

    @Override
    public List<DevicePerfTarget> queryAllDevicePerfTarget(Long entityId) {
        return this.getSqlSession().selectList(getNameSpace("queryDevicePerfTarget"), entityId);
    }

    @Override
    public List<DevicePerfTarget> queryGlobalPerfTargetList(Long entityType) {
        return this.getSqlSession().selectList(getNameSpace("queryGlobalPerfTarget"), entityType);
    }

    @Override
    public List<DevicePerfTarget> queryDeviceGlobalPerfList(Long entityId) {
        return this.getSqlSession().selectList(getNameSpace("queryDeviceGlobalPerf"), entityId);
    }

    @Override
    public List<Long> querySupportTargetDevice(Map<String, Object> paramsMap) {
        if (paramsMap.get("folderId") != null) {
            paramsMap.put("authority", CurrentRequest.getAuthorityViewName((Integer) paramsMap.get("folderId")));
        } else {
            paramsMap.put("authority", CurrentRequest.getUserAuthorityViewName());
        }
        return this.getSqlSession().selectList(getNameSpace("querySupportTargetDevice"), paramsMap);
    }

    @Override
    public int querySupportTargetDeviceNum(Map<String, Object> paramsMap) {
        if (paramsMap.get("folderId") != null) {
            paramsMap.put("authority", CurrentRequest.getAuthorityViewName((Integer) paramsMap.get("folderId")));
        } else {
            paramsMap.put("authority", CurrentRequest.getUserAuthorityViewName());
        }
        return this.getSqlSession().selectOne(getNameSpace("querySupportTargetDeviceNum"), paramsMap);
    }

    @Override
    public List<DevicePerfTarget> queryDeviceSupportTarget(Long typeId) {
        return this.getSqlSession().selectList(getNameSpace("queryDeviceSupportTarget"), typeId);
    }

    @Override
    public List<DevicePerfTarget> queryTargetByTypeIdAndGroup(Map<String, Object> paramMap) {
        return this.getSqlSession().selectList(getNameSpace("queryTargetByTypeIdAndGroup"), paramMap);
    }

    @Override
    public List<DevicePerfTarget> queryDevicePerfByType(Map<String, Object> paramMap) {
        return this.getSqlSession().selectList(getNameSpace("queryDevicePerfByType"), paramMap);
    }

    @Override
    public List<DevicePerfTarget> queryGroupTargetByType(String groupName, Long entityType) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityType", entityType);
        paramMap.put("targetGroup", groupName);
        return this.getSqlSession().selectList(getNameSpace("queryGroupTargetByType"), paramMap);
    }

    @Override
    public DevicePerfTarget queryGlobalTargetByType(String targetName, Long entityType) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityType", entityType);
        paramMap.put("perfTargetName", targetName);
        return this.getSqlSession().selectOne(getNameSpace("queryGlobalTargetByType"), paramMap);
    }

    @Override
    public List<DevicePerfTarget> queryDeviceSingleTarget(Map<String, Object> paramMap) {
        return this.getSqlSession().selectList(getNameSpace("queryDeviceSingleTarget"), paramMap);
    }

    @Override
    public void batchUdpateDevicePerfByType(List<DevicePerfTarget> targetList) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (DevicePerfTarget perfTarget : targetList) {
                sqlSession.update(getNameSpace("udpateDevicePerfByType"), perfTarget);
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }

    }

    @Override
    public void batchUpdateDevicePerfByTypeId(List<DevicePerfTarget> targetList) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (DevicePerfTarget perfTarget : targetList) {
                sqlSession.update(getNameSpace("updateDevicePerfByTypeId"), perfTarget);
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }

    }

    @Override
    public void updateDevicePerfTarget(DevicePerfTarget perfTarget) {
        this.getSqlSession().update(getNameSpace("updateDevicePerfTarget"), perfTarget);
    }

    @Override
    public void batchUpdateDevicePerf(List<DevicePerfTarget> perfTargetList) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (DevicePerfTarget perfTarget : perfTargetList) {
                sqlSession.update(getNameSpace("updateDevicePerfTarget"), perfTarget);
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public void batchUpdateDeviceTarget(List<DevicePerfTarget> perfTargetList) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (DevicePerfTarget perfTarget : perfTargetList) {
                sqlSession.update(getNameSpace("updateDevicePerfTarget"), perfTarget);
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public void updateGLobalPerfTarget(DevicePerfTarget perfTarget) {
        this.getSqlSession().update(getNameSpace("updateGlobalPerfTarget"), perfTarget);

    }

    @Override
    public void batchUpdateGlobalPerf(List<DevicePerfTarget> perfTargetList) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (DevicePerfTarget perfTarget : perfTargetList) {
                sqlSession.update(getNameSpace("updateGlobalPerfTarget"), perfTarget);
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }

    }

    @Override
    public void insertDevicePerfData(DevicePerfTarget perfTarget) {
        this.getSqlSession().insert(getNameSpace("insertDevicePerfData"), perfTarget);

    }

    @Override
    public void insertPerfTarget(DevicePerfTarget perfTarget) {
        this.getSqlSession().insert(getNameSpace("insertDevicePerfTarget"), perfTarget);

    }

    @Override
    public void insertOrUpdatePerfTarget(DevicePerfTarget perfTarget) {
        int existCount = querySpecialTargetCount(perfTarget.getEntityId(), perfTarget.getPerfTargetName());
        if (existCount != 0) {
            this.getSqlSession().update(getNameSpace("updateDevicePerfTarget"), perfTarget);
        } else {
            this.getSqlSession().insert(getNameSpace("insertDevicePerfTarget"), perfTarget);
        }
    }

    @Override
    public void batchInsertDevicePerf(Long entityId, List<DevicePerfTarget> perfTargetList) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (DevicePerfTarget perfTarget : perfTargetList) {
                perfTarget.setEntityId(entityId);
                sqlSession.insert(getNameSpace("insertDevicePerfData"), perfTarget);
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public DevicePerfTarget queryTargetByTypeIdAndName(Long typeId, String targetName) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("typeId", typeId);
        paramMap.put("targetName", targetName);
        return this.getSqlSession().selectOne(getNameSpace("queryTargetByTypeIdAndName"), paramMap);
    }

    @Override
    public Boolean isPerfTargetDisabled(Long entityId, String perfTargetName) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("perfTargetName", perfTargetName);
        Integer targetEnable = this.getSqlSession().selectOne(getNameSpace("getTargetEnableStatus"), paramMap);
        // 只有数据库中明确有，且是关闭的，才是不能开启的
        if (targetEnable != null && !targetEnable.equals(1)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Integer queryColIntervalByIdAndName(Long entityId, String targetName) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("targetName", targetName);
        return (Integer) this.getSqlSession().selectOne(getNameSpace("queryColIntervalByIdAndName"), paramMap);
    }

    @Override
    public void deleteTargetCollectTime(Long entityId, String perfTargetName) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("perfTargetName", perfTargetName);
        this.getSqlSession().delete(getNameSpace("deleteTargetCollectTime"), paramMap);
    }

    @Override
    public int querySpecialTargetCount(Long entityId, String perfTargetName) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("perfTargetName", perfTargetName);
        return this.getSqlSession().selectOne(getNameSpace("querySpecialTargetCount"), paramMap);
    }

    @Override
    public String getManageIpById(Long entityId) {
        return this.getSqlSession().selectOne(getNameSpace("getManageIpById"), entityId);
    }

}
