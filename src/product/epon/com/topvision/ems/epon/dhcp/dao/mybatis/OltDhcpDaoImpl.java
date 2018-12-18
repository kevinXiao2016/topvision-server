/***********************************************************************
 * $Id: OltDhcpDaoImpl.java,v1.0 2013-10-25 下午5:46:37 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.dhcp.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.dhcp.dao.OltDhcpDao;
import com.topvision.ems.epon.dhcp.domain.OltDhcpBaseConfig;
import com.topvision.ems.epon.dhcp.domain.OltDhcpGiaddrConfig;
import com.topvision.ems.epon.dhcp.domain.OltDhcpIpMacStatic;
import com.topvision.ems.epon.dhcp.domain.OltDhcpServerConfig;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author flack
 * @created @2013-10-25-下午5:46:37
 *
 */
@Repository("oltDhcpDao")
public class OltDhcpDaoImpl extends MyBatisDaoSupport<Object> implements OltDhcpDao {

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.epon.dhcp.domain.Dhcp";
    }

    @Override
    public OltDhcpBaseConfig getDhcpBaseConfig(Long entityId) {
        return getSqlSession().selectOne(getNameSpace("getDhcpBaseConfig"),
                entityId);
    }

    @Override
    public List<OltDhcpServerConfig> getDhcpServerConfigs(Long entityId) {
        return getSqlSession().selectList(getNameSpace("getDhcpServerConfig"), entityId);
    }

    @Override
    public void deleteDhcpServerRule(Long entityId, Integer dhcpServerIndex) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("dhcpServerIndex", dhcpServerIndex);
        getSqlSession().delete(getNameSpace("deleteDhcpServerRule"), paramMap);
    }

    @Override
    public void insertDhcpServerRule(OltDhcpServerConfig dhcpServerConfig) {
        getSqlSession().insert(getNameSpace("insertDhcpServerRule"), dhcpServerConfig);
    }

    @Override
    public void modifyDhcpBaseConfig(OltDhcpBaseConfig baseConfig) {
        getSqlSession().update(getNameSpace() + "updateDhcpBaseConfig", baseConfig);
    }

    @Override
    public List<OltDhcpGiaddrConfig> getDhcpGiaddrConfigs(Long entityId) {
        return getSqlSession().selectOne(getNameSpace("getDhcpGiaddrConfig"), entityId);
    }

    @Override
    public void deleteDhcpGiaddrRule(Long entityId, Integer dhcpGiaddrIndex) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("topOltDHCPGiaddrIndex", dhcpGiaddrIndex);
        getSqlSession().delete(getNameSpace("deleteDhcpGiaddrRule"), paramMap);
    }

    @Override
    public void insertDhcpGiaddrRule(OltDhcpGiaddrConfig giaddrConfig) {
        getSqlSession().insert(getNameSpace("insertDhcpGiaddrRule"), giaddrConfig);
    }

    @Override
    public List<OltDhcpIpMacStatic> getDhcpIpMacStatics(Long entityId) {
        return getSqlSession().selectList(("getDhcpIpMacStatic"), entityId);
    }

    @Override
    public void deleteDhcpIpMacStatic(Long entityId, Integer topOltDHCPIpMacIdx) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("topOltDHCPIpMacIdx", topOltDHCPIpMacIdx);
        getSqlSession().delete(getNameSpace("deleteDhcpIpMacStatic"), paramMap);
    }

    @Override
    public void insertDhcpIpMacStatic(OltDhcpIpMacStatic dhcpIpMacStatic) {
        getSqlSession().insert(getNameSpace("insertDhcpIpMacStatic"), dhcpIpMacStatic);
    }

    @Override
    public void batchInsertDhcpServerConfig(Long entityId, final List<OltDhcpServerConfig> serverConfigs) {
        SqlSession sqlSession = getBatchSession();
        sqlSession.delete(getNameSpace("deleteAllDhcpServerRule"), entityId);
        try {
            for (OltDhcpServerConfig serverConfig : serverConfigs) {
                try {
                    sqlSession.insert(getNameSpace("insertDhcpServerRule"), serverConfig);
                } catch (Exception e) {
                    logger.debug("", e);
                }
            }
            sqlSession.commit();
        } catch (Exception e) {
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public void batchInsertDhcpGiaddrConfig(Long entityId, final List<OltDhcpGiaddrConfig> giaddrConfigs) {
        SqlSession sqlSession = getBatchSession();
        sqlSession.delete(getNameSpace("deleteAllDhcpGiaddrRule"), entityId);
        try {
            for (OltDhcpGiaddrConfig giaddr : giaddrConfigs) {
                try {
                    sqlSession.insert(getNameSpace("insertDhcpGiaddrRule"), giaddr);
                } catch (Exception e) {
                    logger.debug("", e);
                }
            }
            sqlSession.commit();
        } catch (Exception e) {
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public void batchInsertDhcpIpMacStatic(Long entityId, final List<OltDhcpIpMacStatic> ipMacStatics) {
        SqlSession sqlSession = getBatchSession();
        sqlSession.delete(getNameSpace("deleteAllDhcpIpMacStatic"), entityId);
        try {
            for (OltDhcpIpMacStatic macStatic : ipMacStatics) {
                macStatic.setEntityId(entityId);
                try {
                    sqlSession.insert(getNameSpace("insertDhcpIpMacStatic"), macStatic);
                } catch (Exception e) {
                    logger.debug("", e);
                }
            }
            sqlSession.commit();
        } catch (Exception e) {
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public void batchInsertDhcpBaseConfig(Long entityId, OltDhcpBaseConfig baseConfig) {
        if (getDhcpBaseConfig(entityId) != null) {
            modifyDhcpBaseConfig(baseConfig);
        } else {
            insertDhcpBaseConfig(baseConfig);
        }
    }

    @Override
    public void insertDhcpBaseConfig(OltDhcpBaseConfig baseConfig) {
        getSqlSession().insert(getNameSpace() + "insertDhcpBaseConfig", baseConfig);
    }

    @Override
    public Integer getMacIndexForIpMacStatic(Long entityId) {
        return getSqlSession().selectOne(getNameSpace("getMaxIndexForIpMacStatic"),
                entityId);
    }

    @Override
    public Integer getCountForIpMacStatic(Long entityId, Long topOltDHCPIpAddrLong, Long topOltDHCPMacAddrLong) {
        Map<String, Long> paramMap = new HashMap<String, Long>();
        paramMap.put("entityId", entityId);
        paramMap.put("topOltDHCPIpAddr", topOltDHCPIpAddrLong);
        paramMap.put("topOltDHCPMacAddr", topOltDHCPMacAddrLong);
        return getSqlSession().selectOne(getNameSpace("getCountForIpMacStatic"), paramMap);
    }

}
