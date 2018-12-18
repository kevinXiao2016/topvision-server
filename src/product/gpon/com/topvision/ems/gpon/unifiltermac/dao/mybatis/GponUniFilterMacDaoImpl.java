/***********************************************************************
 * $Id: GponUniFilterMacDaoImpl.java,v1.0 2016年12月24日 下午5:50:06 $
 * 
 * @author: jay
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.unifiltermac.dao.mybatis;

import com.topvision.ems.gpon.unifiltermac.dao.GponUniFilterMacDao;
import com.topvision.ems.gpon.unifiltermac.facade.domain.GponUniFilterMac;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jay
 * @created @2016年12月24日-下午5:50:06
 *
 */
@Repository("gponUniFilterMacDao")
public class GponUniFilterMacDaoImpl extends MyBatisDaoSupport<Object> implements GponUniFilterMacDao {
    @Override
    protected String getDomainName() {
        return "GponUniFilterMac";
    }

    @Override
    public List<GponUniFilterMac> loadGponUniFilterMacList(Long uniId) {
        return getSqlSession().selectList(getNameSpace("loadGponUniFilterMacList"), uniId);
    }

    @Override
    public void addGponUniFilterMac(GponUniFilterMac gponUniFilterMac) {
        getSqlSession().insert(getNameSpace("addGponUniFilterMac"), gponUniFilterMac);
    }

    @Override
    public void deleteGponUniFilterMac(GponUniFilterMac gponUniFilterMac) {
        getSqlSession().delete(getNameSpace("deleteGponUniFilterMac"), gponUniFilterMac);
    }

    @Override
    public void insertGponUniFilterMacForEntity(Long entityId,List<GponUniFilterMac> gponUniFilterMacs) {
        SqlSession session = getBatchSession();
        try {
            session.delete(getNameSpace("deleteGponUniFilterMacForEntity"), entityId);
            for (GponUniFilterMac gponUniFilterMac : gponUniFilterMacs) {
                session.insert(getNameSpace("addGponUniFilterMac"), gponUniFilterMac);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void insertGponUniFilterMacForUni(Long uniId,List<GponUniFilterMac> gponUniFilterMacs) {
        SqlSession session = getBatchSession();
        try {
            session.delete(getNameSpace("deleteGponUniFilterMacForUni"), uniId);
            for (GponUniFilterMac gponUniFilterMac : gponUniFilterMacs) {
                session.insert(getNameSpace("addGponUniFilterMac"), gponUniFilterMac);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public GponUniFilterMac loadGponUniFilterMac(Long uniId, String mac) {
        Map<String,String> map = new HashMap<>();
        map.put("uniId","" + uniId);
        map.put("mac",mac);
        return getSqlSession().selectOne(getNameSpace("loadGponUniFilterMac"), map);
    }
}
