/***********************************************************************
 * $Id: CmUpgradeDaoImpl.java,v1.0 2016年12月5日 下午6:41:35 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.upgrade.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.cm.upgrade.dao.CmUpgradeDao;
import com.topvision.ems.cm.upgrade.domain.CmUpgradeConfig;
import com.topvision.ems.cm.upgrade.domain.CmcUpgradeInfo;
import com.topvision.ems.cm.upgrade.facade.domain.TopCcmtsCmSwVersionTable;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.util.CurrentRequest;

/**
 * @author Rod John
 * @created @2016年12月5日-下午6:41:35
 *
 */
@Repository("cmUpgradeDao")
public class CmUpgradeDaoImpl extends MyBatisDaoSupport<CmUpgradeConfig> implements CmUpgradeDao {

    /* (non-Javadoc)
     * @see com.topvision.ems.cm.upgrade.dao.CmUpgradeDao#insertCmUpgradeConfig(com.topvision.ems.cm.upgrade.domain.CmUpgradeConfig)
     */
    @Override
    public void insertCmUpgradeConfig(CmUpgradeConfig cmUpgradeConfig) {
        getSqlSession().insert(getNameSpace() + "insertCmUpgradeConfig", cmUpgradeConfig);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.cm.upgrade.dao.CmUpgradeDao#modifyCmUpgradeConfig(com.topvision.ems.cm.upgrade.domain.CmUpgradeConfig)
     */
    @Override
    public void modifyCmUpgradeConfig(CmUpgradeConfig cmUpgradeConfig) {
        getSqlSession().insert(getNameSpace() + "modifyCmUpgradeConfig", cmUpgradeConfig);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.cm.upgrade.dao.CmUpgradeDao#deleteCmUpgradeConfig(java.lang.Integer)
     */
    @Override
    public void deleteCmUpgradeConfig(Integer id) {
        getSqlSession().insert(getNameSpace() + "deleteCmUpgradeConfig", id);
    }

    /* (non-Javadoc)
     * @see com.topvision.framework.dao.mybatis.MyBatisDaoSupport#getDomainName()
     */
    @Override
    protected String getDomainName() {
        return "com.topvision.ems.cm.upgrade.dao.CmUpgradeDao";
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.cm.upgrade.dao.CmUpgradeDao#loadCmUpgradeConfig()
     */
    @Override
    public List<CmUpgradeConfig> loadCmUpgradeConfig() {
        return getSqlSession().selectList(getNameSpace() + "selectCmUpgradeConfig");
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.cm.upgrade.dao.CmUpgradeDao#syncCmModulSoftversion(java.util.Map)
     */
    @Override
    public void syncCmModulSoftversion(Map<Long, List<TopCcmtsCmSwVersionTable>> version) {
        SqlSession session = getBatchSession();
        try {
            for (Entry<Long, List<TopCcmtsCmSwVersionTable>> entry : version.entrySet()) {
                Long entityId = entry.getKey();
                List<TopCcmtsCmSwVersionTable> list = entry.getValue();
                session.delete(getNameSpace() + "deleteCmModulSoftversion", entityId);
                for (TopCcmtsCmSwVersionTable cmVersion : list) {
                    cmVersion.setEntityId(entityId);
                    session.insert(getNameSpace() + "insertCmModulSoftversion", cmVersion);
                }
                session.commit();
            }
        } catch (Exception e) {
            logger.error("syncCmModulSoftversion error:", e);
            session.rollback();
        } finally {
            session.close();
        }
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.cm.upgrade.dao.CmUpgradeDao#selectModulList()
     */
    @Override
    public List<String> selectModulList() {
        return getSqlSession().selectList(getNameSpace() + "selectModulList");
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.cm.upgrade.dao.CmUpgradeDao#selectCmcUpgradeEntityInfo(java.util.Map)
     */
    @Override
    public List<CmcUpgradeInfo> selectCmcUpgradeEntityInfo(Map<String, Object> param) {
        param.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace() + "selectCmcUpgradeEntityInfo", param);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.cm.upgrade.dao.CmUpgradeDao#selectCmModulSoftversion(java.lang.Long, java.lang.Long)
     */
    @Override
    public TopCcmtsCmSwVersionTable selectCmModulSoftversion(Long entityId, Long statusIndex) {
        Map<String, Long> params = new HashMap<>();
        params.put("entityId", entityId);
        params.put("statusIndex", statusIndex);
        return getSqlSession().selectOne(getNameSpace() + "selectCmModulSoftversion", params);

    }

    /* (non-Javadoc)
     * @see com.topvision.ems.cm.upgrade.dao.CmUpgradeDao#getCmSwVersionInfo(java.lang.Long)
     */
    @Override
    public List<TopCcmtsCmSwVersionTable> getCmSwVersionInfo(Long entityId) {
        return getSqlSession().selectList(getNameSpace() + "getCmSwVersionInfo", entityId);
    }

}
