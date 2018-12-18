/***********************************************************************
 * $Id: SpectrumDiscoveryDaoImpl.java,v1.0 2014-3-5 上午10:30:13 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.spectrum.dao.mybatis;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.cmc.spectrum.dao.SpectrumDiscoveryDao;
import com.topvision.ems.cmc.spectrum.facade.domain.SpectrumCfg;
import com.topvision.ems.cmc.spectrum.facade.domain.SpectrumCmtsSwitch;
import com.topvision.ems.cmc.spectrum.facade.domain.SpectrumOltSwitch;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author haojie
 * @created @2014-3-5-上午10:30:13
 *
 */
@Repository("spectrumDiscoveryDao")
public class SpectrumDiscoveryDaoImpl extends MyBatisDaoSupport<SpectrumCfg> implements SpectrumDiscoveryDao {

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.cmc.spectrum.domain.SpectrumDiscovery";
    }

    @Override
    public void inserSpectrumCfg(Long entityId, List<SpectrumCfg> spectrumCfgs) {
        SqlSession session = getBatchSession();
        try {
            for (SpectrumCfg spectrumCfg : spectrumCfgs) {
                //CMTS配置
                Long cmcId = session.selectOne(getNameSpace("getCmcIdByMac"), spectrumCfg.getFftMonitorCmcMacIndex());
                spectrumCfg.setCmcId(cmcId);
                SpectrumCfg cfg = session.selectOne(getNameSpace("getSpectrumCfgByCmcId"), cmcId);
                if (cfg != null) {
                    session.update(getNameSpace("updateSpectrumCfg"), spectrumCfg);
                } else {
                    session.insert(getNameSpace("insertSpectrumCfg"), spectrumCfg);
                }
                //网管侧CMTS开关配置，插入默认值
                SpectrumCmtsSwitch cs = session.selectOne(getNameSpace("getSpectrumCmtsSwitch"), cmcId);
                if (cs == null) {
                    session.insert(getNameSpace("insertSpectrumCmtsSwitch"), cmcId);
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
    public void inserSpectrumCfgForA(Long cmcId, List<SpectrumCfg> spectrumCfgs) {
        SqlSession session = getBatchSession();
        try {
            for (SpectrumCfg spectrumCfg : spectrumCfgs) {
                Long id = session.selectOne(getNameSpace("getCmcIdByMac"), spectrumCfg.getFftMonitorCmcMacIndex());
                if (!id.equals(cmcId)) {
                    continue;
                }
                spectrumCfg.setCmcId(cmcId);
                SpectrumCfg cfg = session.selectOne(getNameSpace("getSpectrumCfgByCmcId"), cmcId);
                if (cfg != null) {
                    session.update(getNameSpace("updateSpectrumCfg"), spectrumCfg);
                } else {
                    session.insert(getNameSpace("insertSpectrumCfg"), spectrumCfg);
                }

                //网管侧CMTS开关配置，插入默认值
                SpectrumCmtsSwitch cs = session.selectOne(getNameSpace("getSpectrumCmtsSwitch"), cmcId);
                if (cs == null) {
                    session.insert(getNameSpace("insertSpectrumCmtsSwitch"), cmcId);
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
    public void insertSpectrumOltSwitch(SpectrumOltSwitch spectrumOltSwitch) {
        SpectrumOltSwitch os = getSqlSession().selectOne(getNameSpace("getSpectrumOltSwitch"),
                spectrumOltSwitch.getEntityId());
        if (os != null) {
            getSqlSession().update(getNameSpace("updateSpectrumOltSwitch"), spectrumOltSwitch);
        } else {
            getSqlSession().insert(getNameSpace("insertSpectrumOltSwitch"), spectrumOltSwitch);
        }
    }

    @Override
    public void insertSpectrumCmtsSwitch(Long entityId) {
        SpectrumCmtsSwitch cs = getSqlSession().selectOne(getNameSpace("getSpectrumCmtsSwitch"), entityId);
        if (cs == null) {
            getSqlSession().insert(getNameSpace("insertSpectrumCmtsSwitch"), entityId);
        }
    }
}
