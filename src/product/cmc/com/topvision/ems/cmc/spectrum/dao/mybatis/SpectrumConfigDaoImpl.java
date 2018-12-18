/***********************************************************************
 * $Id: SpectrumConfigDaoImpl.java,v1.0 2014-1-14 下午4:37:17 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.spectrum.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.cmc.spectrum.dao.SpectrumConfigDao;
import com.topvision.ems.cmc.spectrum.domain.CmtsSpectrumConfig;
import com.topvision.ems.cmc.spectrum.facade.domain.SpectrumOltSwitch;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.util.CurrentRequest;

/**
 * @author haojie
 * @created @2014-1-14-下午4:37:17
 * 
 */
@Repository("spectrumConfigDao")
public class SpectrumConfigDaoImpl extends MyBatisDaoSupport<Entity> implements SpectrumConfigDao {

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.cmc.spectrum.domain.SpectrumConfig";
    }

    @Override
    public Boolean getCmtsSwitchStatus(Long cmcId) {
        Integer status = getSqlSession().selectOne(getNameSpace("getCmtsSwitchStatus"), cmcId);
        if (status != null && status == 1) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<CmtsSpectrumConfig> getCmtsSpectrumConfig(Map<String, Object> map) {
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace("getCmtsSpectrumConfig"), map);
    }

    public Long getCmtsSpectrumConfigCount(Map<String, Object> map) {
        return getSqlSession().selectOne(getNameSpace("getCmtsSpectrumConfigCount"), map);
    }

    @Override
    public void startSpectrumSwitchCmts(List<Long> cmcIds) {
        SqlSession session = getBatchSession();
        try {
            for (Long cmcId : cmcIds) {
                CmtsSpectrumConfig config = getSqlSession().selectOne(getNameSpace("getCmtsSwitchById"), cmcId);
                if (config != null) {
                    session.update(getNameSpace("startSpectrumSwitchCmts"), cmcId);
                } else {
                    session.insert(getNameSpace("insertSpectrumSwitchCmts"), cmcId);
                }
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void stopSpectrumSwitchCmts(List<Long> cmcIds) {
        SqlSession session = getBatchSession();
        try {
            for (Long cmcId : cmcIds) {
                session.update(getNameSpace("stopSpectrumSwitchCmts"), cmcId);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public List<SpectrumOltSwitch> getOltSpectrumConfig(Map<String, Object> map) {
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace("getOltSpectrumConfig"), map);
    }

    @Override
    public Long getOltSpectrumConfigCount(Map<String, Object> map) {
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectOne(getNameSpace("getOltSpectrumConfigCount"), map);
    }

    @Override
    public void updateSpectrumSwitchOlt(Long entityId, Integer status) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("status", status);
        getSqlSession().update(getNameSpace("updateSpectrumSwitchOlt"), map);
    }

    @Override
    public Integer getSpectrumSwitchOlt(Long entityId) {
        return getSqlSession().selectOne(getNameSpace("getSpectrumSwitchOlt"), entityId);
    }

    @Override
    public Boolean getOltSwitchStatus(Long cmcId) {
        Integer status = getSqlSession().selectOne(getNameSpace("getOltSwitchStatus"), cmcId);
        return (status != null &&status == 1) ? true : false;
    }

}
