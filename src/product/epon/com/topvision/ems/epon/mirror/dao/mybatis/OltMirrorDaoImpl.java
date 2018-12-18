/***********************************************************************
 * $Id: OltMirrorDaoImpl.java,v1.0 2013-10-25 下午2:03:53 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.mirror.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.mirror.dao.OltMirrorDao;
import com.topvision.ems.epon.mirror.domain.OltSniMirrorConfig;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author flack
 * @created @2013-10-25-下午2:03:53
 *
 */
@Repository("oltMirrorDao")
public class OltMirrorDaoImpl extends MyBatisDaoSupport<Object> implements OltMirrorDao {

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.epon.mirror.domain.OltMirror";
    }

    @Override
    public void batchInsertOltSniMirrorConfig(final List<OltSniMirrorConfig> sniMirrorConfigs, Long entityId) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace() + "deleteAllOltSniMirrorConfig", entityId);
            for (OltSniMirrorConfig sniMirrorConfig : sniMirrorConfigs) {
                sniMirrorConfig.setEntityId(entityId);
                sqlSession.insert(getNameSpace() + "insertOltSniMirrorConfig", sniMirrorConfig);
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
    public void modifyMirrorName(Long entityId, Integer sniMirrorGroupIndex, String sniMirrorGroupName) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("sniMirrorGroupIndex", sniMirrorGroupIndex);
        map.put("sniMirrorGroupName", sniMirrorGroupName);
        getSqlSession().update(getNameSpace() + "modifyMirrorName", map);
    }


    @Override
    public List<OltSniMirrorConfig> getMirrorConfigList(Long entityId) {
        return getSqlSession().selectList(getNameSpace() + "getMirrorConfigList", entityId);
    }

    @Override
    public void updateMirrorPortList(OltSniMirrorConfig oltSniMirrorConfig) {
        getSqlSession().update(getNameSpace() + "updateMirrorPortList", oltSniMirrorConfig);
    }

}
