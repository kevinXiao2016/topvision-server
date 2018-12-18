/***********************************************************************
 * $Id: OltRstpDaoImpl.java,v1.0 2013-10-25 下午5:34:51 $
 * 
 * @author: huqiao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.rstp.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.olt.domain.OltSniAttribute;
import com.topvision.ems.epon.rstp.dao.OltRstpDao;
import com.topvision.ems.epon.rstp.domain.OltStpGlobalConfig;
import com.topvision.ems.epon.rstp.domain.OltStpPortConfig;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author huqiao
 * @created @2011-12-1-下午02:55:48
 * 
 * @Mybatis Modify by Rod @2013-10-25
 */
@Repository("oltRstpDao")
public class OltRstpDaoImpl extends MyBatisDaoSupport<Object> implements OltRstpDao {

    @Override
    public void batchInsertOltStpPortConfig(Long entityId, final List<OltStpPortConfig> stpPortConfigs) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace() + "deleteAllOltStpPortConfig", entityId);
            for (OltStpPortConfig stpPortConfig : stpPortConfigs) {
                // 通过构造一个sni属性对象，获得sni对应的sniId
                OltSniAttribute oltSniAttribute = new OltSniAttribute();
                oltSniAttribute.setEntityId(stpPortConfig.getEntityId());
                oltSniAttribute.setSniIndex(stpPortConfig.getSniIndex());
                stpPortConfig.setSniId((Long) sqlSession.selectOne("com.topvision.ems.epon.olt.domain.OltSni.getSniId",
                        oltSniAttribute));
                sqlSession.insert(getNameSpace() + "insertOltStpPortConfig", stpPortConfig);
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
    public void batchInsertOltStpGlobalConfig(Long entityId, OltStpGlobalConfig stpGlobalConfig) {
        int globalCount = getSqlSession().selectOne(getNameSpace() + "getCountForStpGlobalConfig", entityId);
        if (globalCount > 0) {
            getSqlSession().update(getNameSpace() + "updateOltStpGlobalConfig", stpGlobalConfig);
        } else if (globalCount == 0) {
            getSqlSession().insert(getNameSpace() + "insertOltStpGlobalConfig", stpGlobalConfig);
        }
    }

    @Override
    public OltStpGlobalConfig getOltStpGlobalConfig(Long entityId) {
        return getSqlSession().selectOne(getNameSpace() + "getOltStpGlobalConfig", entityId);
    }

    @Override
    public OltStpPortConfig getOltStpPortConfig(Long entityId, Long sniId) {
        Map<String, Long> paramMap = new HashMap<String, Long>();
        paramMap.put("entityId", entityId);
        paramMap.put("sniId", sniId);
        return getSqlSession().selectOne(getNameSpace() + "getOltStpPortConfigBySniId", paramMap);
    }

    @Override
    public void updateOltStpGlobalConfig(OltStpGlobalConfig globalConfig) {
        getSqlSession().update(getNameSpace() + "updateOltStpGlobalConfig", globalConfig);
    }

    @Override
    public void updateOltStpGlobalEnable(Long entityId, Integer globalEnable) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("globalEnable", globalEnable);
        getSqlSession().update(getNameSpace() + "updateOltStpGlobalEnable", paramMap);
    }

    @Override
    public void updateOltStpPortEnable(Long entityId, Long portId, Integer portEnable) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("sniId", portId);
        paramMap.put("stpPortEnabled", portEnable);
        getSqlSession().update(getNameSpace() + "updateOltStpPortEnable", paramMap);
    }

    @Override
    public void updateOltStpPortProtocolMigration(Long entityId, Long portId, Integer status) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("sniId", portId);
        paramMap.put("stpPortRstpProtocolMigration", status);
        getSqlSession().update(getNameSpace() + "updateOltStpPortMigration", paramMap);
    }

    @Override
    public void updateOltStpPortConfig(Long entityId, OltStpPortConfig portConfig) {
        getSqlSession().update(getNameSpace() + "updateOltStpPortConfig", portConfig);
    }

    @Override
    public List<Long> getStpEnablePortList(Long entityId) {
        return getSqlSession().selectList(getNameSpace() + "getStpEnablePortList", entityId);
    }

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.epon.rstp.domain.OltStp";
    }

}
