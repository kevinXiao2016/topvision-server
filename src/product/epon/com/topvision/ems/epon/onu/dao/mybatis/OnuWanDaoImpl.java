/***********************************************************************
 * $Id: OnuWanDaoImpl.java,v1.0 2016年5月30日 下午4:05:49 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.onu.dao.OnuWanDao;
import com.topvision.ems.epon.onu.domain.OnuWanConfig;
import com.topvision.ems.epon.onu.domain.OnuWanConnect;
import com.topvision.ems.epon.onu.domain.OnuWanConnectStatus;
import com.topvision.ems.epon.onu.domain.OnuWanSsid;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author loyal
 * @created @2016年5月30日-下午4:05:49
 * 
 */
@Repository("onuWanDao")
public class OnuWanDaoImpl extends MyBatisDaoSupport<Object> implements OnuWanDao {

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.epon.onu.dao.OnuWanDao";
    }

    @Override
    public OnuWanConfig getOnuWanConfig(Long onuId) {
        return getSqlSession().selectOne(getNameSpace() + "getOnuWanConfig", onuId);
    }

    @Override
    public List<OnuWanSsid> getOnuWanSsid(Long onuId) {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("onuId", onuId);
        return getSqlSession().selectList(getNameSpace() + "getOnuWanSsid", queryMap);
    }

    @Override
    public OnuWanSsid getOnuWanSsid(Long onuId, Integer ssid) {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("onuId", onuId);
        queryMap.put("ssid", ssid);
        return getSqlSession().selectOne(getNameSpace() + "getOnuWanSsid", queryMap);
    }

    @Override
    public List<OnuWanConnect> getOnuWanConnect(Long onuId) {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("onuId", onuId);
        return getSqlSession().selectList(getNameSpace() + "getOnuWanConnect", queryMap);
    }

    @Override
    public OnuWanConnect getOnuWanConnect(Long onuId, Integer connectId) {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("onuId", onuId);
        queryMap.put("connectId", connectId);
        return getSqlSession().selectOne(getNameSpace() + "getOnuWanConnect", queryMap);
    }

    @Override
    public List<OnuWanConnectStatus> getOnuWanConnectStatus(Long onuId) {
        return getSqlSession().selectList(getNameSpace() + "getOnuWanConnectStatus", onuId);
    }

    @Override
    public void insertOnuWanConfig(OnuWanConfig onuWanConfig) {
        getSqlSession().insert(getNameSpace() + "insertOnuWanConfig", onuWanConfig);
    }

    @Override
    public void insertOnuWanSsid(OnuWanSsid onuWanSsid) {
        getSqlSession().insert(getNameSpace() + "insertOnuWanSsid", onuWanSsid);
    }

    @Override
    public void insertOnuWanConnect(OnuWanConnect onuWanConnect) {
        getSqlSession().insert(getNameSpace() + "insertOnuWanConnect", onuWanConnect);
    }

    @Override
    public void insertOnuWanConnectStatus(OnuWanConnectStatus onuWanConnectStatus) {
        getSqlSession().insert(getNameSpace() + "insertOnuWanConnectStatus", onuWanConnectStatus);
    }

    @Override
    public void deleteOnuWanSsid(Long onuId, Integer ssid) {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("onuId", onuId);
        queryMap.put("ssid", ssid);
        getSqlSession().delete(getNameSpace() + "deleteOnuWanSsid", queryMap);
    }

    @Override
    public void deleteOnuWanConnect(Long onuId, Integer connectId) {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("onuId", onuId);
        queryMap.put("connectId", connectId);
        getSqlSession().delete(getNameSpace() + "deleteOnuWanConnect", queryMap);
    }

    @Override
    public void updateOnuWanConfig(OnuWanConfig onuWanConfig) {
        getSqlSession().update(getNameSpace() + "updateOnuWanConfig", onuWanConfig);
    }

    @Override
    public void updateOnuWanSsid(OnuWanSsid onuWanSsid) {
        getSqlSession().update(getNameSpace() + "updateOnuWanSsid", onuWanSsid);
    }

    @Override
    public void updateWanSsidAndName(OnuWanSsid onuWanSsid) {
        getSqlSession().update(getNameSpace() + "updateWanSsidAndName", onuWanSsid);
    }

    @Override
    public void updateOnuWanConnect(OnuWanConnect onuWanConnect) {
        getSqlSession().update(getNameSpace() + "updateOnuWanConnect", onuWanConnect);
    }

    @Override
    public void updateOnuWanPassord(OnuWanConnect onuWanConnect) {
        getSqlSession().update(getNameSpace() + "updateOnuWanPassord", onuWanConnect);
    }

    @Override
    public String loadBindInterface(Long onuId, Integer connectId) {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("onuId", onuId);
        queryMap.put("connectId", connectId);
        return getSqlSession().selectOne(getNameSpace("loadBindInterface"), queryMap);
    }

    @Override
    public void saveBindInterface(Long onuId, Integer connectId, String bindInterface) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("onuId", onuId);
        map.put("connectId", connectId);
        map.put("bindInterface", bindInterface);
        getSqlSession().update(getNameSpace("saveBindInterface"), map);
    }

    @Override
    public List<String> loadBindInterface(Long onuId) {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("onuId", onuId);
        return getSqlSession().selectList(getNameSpace("loadBindInterfaceByOnuId"), queryMap);
    }

    private Long getOnuIdByIndex(Long entityId, Long onuIndex) {
        Map<String, Long> map = new HashMap<String, Long>();
        map.put("entityId", entityId);
        map.put("onuIndex", onuIndex);
        return getSqlSession().selectOne(getNameSpace("getOnuIdByIndex"), map);
    }

    @Override
    public void batchInsertOrUpdateOnuWanConfig(Long entityId, List<OnuWanConfig> onuWanConfigList) {
        SqlSession session = getBatchSession();
        try {
            for (OnuWanConfig onuWanConfig : onuWanConfigList) {
                Long onuId = getOnuIdByIndex(entityId, onuWanConfig.getOnuIndex());
                onuWanConfig.setOnuId(onuId);
                session.insert(getNameSpace("insertOrUpdateOnuWanConfig"), onuWanConfig);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void batchInsertOrUpdateOnuWanConfig(Long entityId, Long onuIndex, OnuWanConfig onuWanConfig) {
        SqlSession session = getBatchSession();
        try {
            Long onuId = getOnuIdByIndex(entityId, onuIndex);
            onuWanConfig.setOnuId(onuId);
            session.insert(getNameSpace("insertOrUpdateOnuWanConfig"), onuWanConfig);
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void batchInsertOrUpdateOnuWanSsid(Long entityId, Long onuIndex, List<OnuWanSsid> onuWanSsids) {
        SqlSession session = getBatchSession();
        try {
            Long onuId = getOnuIdByIndex(entityId, onuIndex);
            session.delete(getNameSpace("deleteOnuWanSsidByOnuId"), onuId);
            for (OnuWanSsid onuWanSsid : onuWanSsids) {
                onuWanSsid.setOnuId(onuId);
                session.insert(getNameSpace("insertOnuWanSsid"), onuWanSsid);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void batchInsertOrUpdateOnuWanSsid(Long entityId, List<OnuWanSsid> onuWanSsids) {
        SqlSession session = getBatchSession();
        try {
            session.delete(getNameSpace("deleteOnuWanSsidByOltId"), entityId);
            for (OnuWanSsid onuWanSsid : onuWanSsids) {
                Long onuId = getOnuIdByIndex(entityId, onuWanSsid.getOnuIndex());
                onuWanSsid.setOnuId(onuId);
                session.insert(getNameSpace("insertOnuWanSsid"), onuWanSsid);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void batchInsertOrUpdateOnuWanConnect(Long entityId, Long onuIndex, List<OnuWanConnect> onuWanConnects) {
        SqlSession session = getBatchSession();
        try {
            Long onuId = getOnuIdByIndex(entityId, onuIndex);
            session.delete(getNameSpace("deleteOnuWanConnectsByOnuId"), onuId);
            for (OnuWanConnect onuWanConnect : onuWanConnects) {
                onuWanConnect.setOnuId(onuId);
                session.insert(getNameSpace("insertOnuWanConnect"), onuWanConnect);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void batchInsertOrUpdateOnuWanConnect(Long entityId, List<OnuWanConnect> onuWanConnects) {
        SqlSession session = getBatchSession();
        try {
            session.delete(getNameSpace("deleteOnuWanConnectsByOltId"), entityId);
            for (OnuWanConnect onuWanConnect : onuWanConnects) {
                Long onuId = getOnuIdByIndex(entityId, onuWanConnect.getOnuIndex());
                onuWanConnect.setOnuId(onuId);
                session.insert(getNameSpace("insertOnuWanConnect"), onuWanConnect);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void batchInsertOrUpdateOnuWanConnectStatus(Long entityId, Long onuIndex,
            List<OnuWanConnectStatus> onuWanConnectStatus) {
        SqlSession session = getBatchSession();
        try {
            Long onuId = getOnuIdByIndex(entityId, onuIndex);
            session.delete(getNameSpace("deleteOnuWanConnectStatusByOnuId"), onuId);
            for (OnuWanConnectStatus status : onuWanConnectStatus) {
                status.setOnuId(onuId);
                session.insert(getNameSpace("insertOnuWanConnectStatus"), status);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void batchInsertOrUpdateOnuWanConnectStatus(Long entityId, List<OnuWanConnectStatus> onuWanConnectStatus) {
        SqlSession session = getBatchSession();
        try {
            session.delete(getNameSpace("deleteOnuWanConnectStatusByOltId"), entityId);
            for (OnuWanConnectStatus status : onuWanConnectStatus) {
                Long onuId = getOnuIdByIndex(entityId, status.getOnuIndex());
                status.setOnuId(onuId);
                session.insert(getNameSpace("insertOnuWanConnectStatus"), status);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void deleteOnuWanConnectsByOnuId(Long onuId) {
        getSqlSession().delete(getNameSpace("deleteOnuWanConnectsByOnuId"), onuId);
    }

    @Override
    public List<String> loadAlreadyBindInterface(Long onuId, Integer connectId) {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("onuId", onuId);
        queryMap.put("connectId", connectId);
        return getSqlSession().selectList(getNameSpace("loadAlreadyBindInterface"), queryMap);
    }

}
