/***********************************************************************
 * $Id: OltTrunkDaoImpl.java,v1.0 2013-10-25 下午3:16:35 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.trunk.dao.mybatis;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.trunk.dao.OltTrunkDao;
import com.topvision.ems.epon.trunk.domain.OltSniTrunkConfig;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author flack
 * @created @2013-10-25-下午3:16:35
 *
 */
@Repository("oltTrunkDao")
public class OltTrunkDaoImpl extends MyBatisDaoSupport<Object> implements OltTrunkDao {

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.epon.trunk.domain.OltTrunk";
    }

    @Override
    public void batchInsertOltSniTrunkConfig(final List<OltSniTrunkConfig> sniTrunkConfigs, final Long entityId) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace() + "deleteAllOltSniTrunkConfig", entityId);
            for (OltSniTrunkConfig sniTrunkConfig : sniTrunkConfigs) {
                sniTrunkConfig.setEntityId(entityId);
                sqlSession.insert(getNameSpace() + "insertOltSniTrunkConfig", sniTrunkConfig);
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
    public List<OltSniTrunkConfig> getTrunkConfigList(Long entityId) {
        return getSqlSession().selectList(getNameSpace() + "getTrunkConfigList", entityId);
    }

    @Override
    public void deleteSniTrunkConfig(OltSniTrunkConfig oltSniTrunkConfig) {
        getSqlSession().delete(getNameSpace() + "deleteOltSniTrunkConfig", oltSniTrunkConfig);
    }

    @Override
    public void insertSniTrunkConfig(OltSniTrunkConfig newOltSniTrunkConfig) {
        getSqlSession().insert(getNameSpace() + "insertOltSniTrunkConfig", newOltSniTrunkConfig);
    }

    @Override
    public void updateSniTrunkConfig(OltSniTrunkConfig oltSniTrunkConfig) {
        getSqlSession().update(getNameSpace() + "updateOltSniTrunk", oltSniTrunkConfig);
    }

}
