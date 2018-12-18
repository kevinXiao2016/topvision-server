/***********************************************************************
 * $Id: GponDaoImpl.java,v1.0 2016年10月17日 下午4:07:12 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.onu.dao.mybatis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.onu.dao.OnuDao;
import com.topvision.ems.epon.onu.dao.UniDao;
import com.topvision.ems.gpon.onu.dao.GponOnuDao;
import com.topvision.ems.gpon.onu.domain.GponOnuAttribute;
import com.topvision.ems.gpon.onu.domain.GponOnuCapability;
import com.topvision.ems.gpon.onu.domain.GponOnuInfoSoftware;
import com.topvision.ems.gpon.onu.domain.GponOnuIpHost;
import com.topvision.ems.gpon.onu.domain.GponOnuUniPvid;
import com.topvision.ems.gpon.onu.domain.GponUniAttribute;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author Rod John
 * @created @2016年10月17日-下午4:07:12
 *
 */
@Repository("gponOnuDao")
public class GponOnuDaoImpl extends MyBatisDaoSupport<GponOnuAttribute> implements GponOnuDao {
    @Autowired
    private OnuDao onuDao;
    @Autowired
    private UniDao uniDao;

    /* (non-Javadoc)
     * @see com.topvision.ems.gpon.onu.dao.GponOnuDao#syncGponOnuIpHosts(java.lang.Long, java.util.List)
     */
    @Override
    public void syncGponOnuIpHosts(Long entityId, List<GponOnuIpHost> gponOnuIpHosts) {
        List<Long> onuIdList = new ArrayList<Long>();
        for (GponOnuIpHost gponOnuIpHost : gponOnuIpHosts) {
            gponOnuIpHost.setEntityId(entityId);
            Long onuId = onuDao.getOnuIdByIndex(gponOnuIpHost.getEntityId(), gponOnuIpHost.getOnuIndex());
            gponOnuIpHost.setOnuId(onuId);
            if (!onuIdList.contains(onuId)) {
                onuIdList.add(onuId);
            }
        }
        SqlSession sqlSession = getBatchSession();
        try {
            for (Long onuId : onuIdList) {
                sqlSession.delete(getNameSpace() + "delGponOnuIpHostByOnuId", onuId);
            }
            for (GponOnuIpHost gponOnuIpHost : gponOnuIpHosts) {
                sqlSession.insert(getNameSpace() + "syncGponOnuIpHosts", gponOnuIpHost);
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }

    }

    /* (non-Javadoc)
     * @see com.topvision.ems.gpon.onu.dao.GponOnuDao#syncGponSoftware(java.lang.Long, java.util.List)
     */
    @Override
    public void syncGponSoftware(Long entityId, List<GponOnuInfoSoftware> gponOnuInfoSoftwares) {
        for (GponOnuInfoSoftware gponOnuInfoSoftware : gponOnuInfoSoftwares) {
            gponOnuInfoSoftware.setEntityId(entityId);
            gponOnuInfoSoftware.setOnuId(onuDao.getOnuIdByIndex(gponOnuInfoSoftware.getEntityId(),
                    gponOnuInfoSoftware.getOnuIndex()));
        }
        SqlSession sqlSession = getBatchSession();
        try {
            for (GponOnuInfoSoftware gponOnuInfoSoftware : gponOnuInfoSoftwares) {
                sqlSession.insert(getNameSpace() + "syncGponSoftware", gponOnuInfoSoftware);
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
    public void syncGponOnuUniPvid(Long entityId, List<GponOnuUniPvid> gponOnuUniPvidList) {
        for (GponOnuUniPvid gponOnuUniPvid : gponOnuUniPvidList) {
            gponOnuUniPvid.setEntityId(entityId);
            gponOnuUniPvid.setUniId(uniDao.getUniIdByIndex(entityId, gponOnuUniPvid.getUniIndex()));
        }
        SqlSession sqlSession = getBatchSession();
        try {
            for (GponOnuUniPvid gponOnuUniPvid : gponOnuUniPvidList) {
                sqlSession.insert(getNameSpace() + "syncGponOnuUniPvid", gponOnuUniPvid);
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    /* (non-Javadoc)
     * @see com.topvision.framework.dao.mybatis.MyBatisDaoSupport#getDomainName()
     */
    @Override
    protected String getDomainName() {
        return "com.topvision.ems.gpon.onu.dao.GponOnuDao";
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.gpon.onu.dao.GponOnuDao#syncGponOnuCapability(java.util.List, java.util.HashMap)
     */
    @Override
    public void syncGponOnuCapability(List<GponOnuCapability> gponOnuCapabilities, HashMap<Long, Long> onuMap) {
        SqlSession session = getBatchSession();
        try {
            for (GponOnuCapability gponOnuCapability : gponOnuCapabilities) {
                if (onuMap.containsKey(gponOnuCapability.getOnuIndex())) {
                    gponOnuCapability.setOnuId(onuMap.get(gponOnuCapability.getOnuIndex()));
                    session.update(getNameSpace() + "updateGponOnuCapability", gponOnuCapability);
                }
            }
            session.commit();
        } catch (Exception e) {
            logger.error("", e);
            session.rollback();
        } finally {
            session.close();
        }
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.gpon.onu.dao.GponOnuDao#queryForGponOnuCapability(long)
     */
    @Override
    public GponOnuCapability queryForGponOnuCapability(long onuId) {
        return getSqlSession().selectOne(getNameSpace("queryForGponOnuCapability"), onuId);
    }

    @Override
    public GponOnuInfoSoftware queryForGponOnuSoftware(Long onuId) {
        return getSqlSession().selectOne(getNameSpace("queryForGponOnuSoftware"), onuId);
    }

    @Override
    public List<GponOnuIpHost> loadGponOnuIpHost(HashMap<String, Object> map) {
        return getSqlSession().selectList(getNameSpace("loadGponOnuIpHost"), map);
    }

    @Override
    public void addGponOnuIpHost(GponOnuIpHost gponOnuIpHost) {
        getSqlSession().insert(getNameSpace("syncGponOnuIpHosts"), gponOnuIpHost);
    }

    @Override
    public GponOnuIpHost getGponOnuIpHost(Long onuId, Integer onuIpHostIndex) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("onuId", onuId);
        map.put("onuIpHostIndex", onuIpHostIndex);
        return getSqlSession().selectOne(getNameSpace("getGponOnuIpHost"), map);
    }

    @Override
    public void modifyGponOnuIpHost(GponOnuIpHost gponOnuIpHost) {
        getSqlSession().update(getNameSpace("syncGponOnuIpHosts"), gponOnuIpHost);
    }

    @Override
    public void delGponOnuIpHost(Long onuId, Integer onuIpHostIndex) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("onuId", onuId);
        map.put("onuIpHostIndex", onuIpHostIndex);
        getSqlSession().delete(getNameSpace("delGponOnuIpHost"), map);
    }

    @Override
    public void delGponOnuIpHostByOnuId(Long onuId) {
        getSqlSession().delete(getNameSpace("delGponOnuIpHostByOnuId"), onuId);
    }

    @Override
    public List<Integer> usedHostIpIndex(Long onuId) {
        return getSqlSession().selectList(getNameSpace("getUsedHostIpIndex"), onuId);
    }

    @Override
    public List<GponUniAttribute> loadGponOnuUniList(Long onuId) {
        return getSqlSession().selectList(getNameSpace("loadGponOnuUniList"), onuId);
    }

    @Override
    public GponUniAttribute loadUniVlanConfig(Long uniId) {
        return getSqlSession().selectOne(getNameSpace("loadUniVlanConfig"), uniId);
    }

    @Override
    public void setUniVlanConfig(Long uniId, Integer gponOnuUniPri, Integer gponOnuUniPvid) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("uniId", uniId);
        map.put("gponOnuUniPri", gponOnuUniPri);
        map.put("gponOnuUniPvid", gponOnuUniPvid);
        getSqlSession().update(getNameSpace("updateUniVlanConfig"), map);
    }
}
