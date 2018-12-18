/***********************************************************************
 * $Id: UpgradeRecordDaoImpl.java,v1.0 2014年9月23日 下午3:24:36 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.upgrade.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.upgrade.dao.UpgradeRecordDao;
import com.topvision.ems.upgrade.domain.UpgradeRecord;
import com.topvision.ems.upgrade.domain.UpgradeRecordQueryParam;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author loyal
 * @created @2014年9月23日-下午3:24:36
 * 
 */
@Repository("upgradeRecordDao")
public class UpgradeRecordDaoImpl extends MyBatisDaoSupport<UpgradeRecord> implements UpgradeRecordDao {

    @Override
    public List<UpgradeRecord> getUpgradeRecord(UpgradeRecordQueryParam upgradeRecordQueryParam) {
        return getSqlSession().selectList(getNameSpace("getUpgradeRecord"), upgradeRecordQueryParam);
    }

    @Override
    public Long getUpgradeRecordNum(UpgradeRecordQueryParam upgradeRecordQueryParam) {
        return getSqlSession().selectOne(getNameSpace("getUpgradeRecordNum"), upgradeRecordQueryParam);
    }

    @Override
    public void deleteUpgradeRecord(List<Long> recordIdList) {
        SqlSession session = getBatchSession();
        try {
            for (int i = 0; i < recordIdList.size(); i++) {
                session.delete("addTelnetLogin", recordIdList.get(i));
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
    public Long insertUpgradeRecord(UpgradeRecord upgradeRecord) {
        getSqlSession().insert(getNameSpace("insertUpgradeRecord"), upgradeRecord);
        return upgradeRecord.getRecordId();
    }

    @Override
    public Long selectCmcEntityIdByIndexAndEntityId(Long index, Long entityId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("index", index);
        map.put("entityId", entityId);
        return getSqlSession().selectOne("selectCmcEntityIdByIndexAndEntityId", map);
    }

    @Override
    public String selectCmcVersionByEntityId(Long entityId) {
        return getSqlSession().selectOne(getNameSpace("selectCmcVersionByEntityId"), entityId);
    }

    @Override
    public String selectOnuVersionByEntityId(Long entityId) {
        return getSqlSession().selectOne(getNameSpace("selectOnuVersionByEntityId"), entityId);
    }

    @Override
    public String selectUplinkEntityName(Long entityId) {
        return getSqlSession().selectOne(getNameSpace("selectUplinkEntityName"), entityId);
    }
    
    @Override
    public Long selectOnuEntityIdByIndexAndEntityId(Long index, Long entityId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("index", index);
        map.put("entityId", entityId);
        return getSqlSession().selectOne(getNameSpace("selectOnuEntityIdByIndexAndEntityId"), map);
    }

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.upgrade.domain.UpgradeRecord";
    }

}
