/***********************************************************************
 * $Id: OltDhcpRelayDao.java,v1.0 2013-10-25 下午5:47:15 $
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

import com.topvision.ems.epon.dhcp.dao.OltDhcpRelayDao;
import com.topvision.ems.epon.dhcp.domain.DhcpBundle;
import com.topvision.ems.epon.dhcp.domain.DhcpGiaddrConfig;
import com.topvision.ems.epon.dhcp.domain.DhcpIntIpConfig;
import com.topvision.ems.epon.dhcp.domain.DhcpOption60;
import com.topvision.ems.epon.dhcp.domain.DhcpRelayVlanMap;
import com.topvision.ems.epon.dhcp.domain.DhcpServerConfig;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author flack
 * @created @2013-10-25-下午5:47:15
 *
 */
@Repository("oltDhcpRelayDao")
public class OltDhcpRelayDaoImpl extends MyBatisDaoSupport<Object> implements OltDhcpRelayDao {

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.mybatis.MyBatisDaoSupport#getDomainName()
     */
    @Override
    protected String getDomainName() {
        return "com.topvision.ems.epon.dhcp.domain.DhcpRelay";
    }

    /**
     * 获取DHCP Bundle
     * 
     * @return
     */
    @Override
    public DhcpBundle getDhcpRelayBundle(Map<String, Object> map) {
        return getSqlSession().selectOne(getNameSpace("getDhcpBundle"), map);
    }

    /**
     * 获取DHCP Server
     * 
     * @return
     */
    @Override
    public DhcpServerConfig getDhcpRelayServerConfig(Map<String, Object> map) {
        return getSqlSession().selectOne(getNameSpace("getDhcpServer"), map);
    }

    /**
     * 获取DHCP Bundle 列表
     * 
     * @return
     */
    @Override
    public List<DhcpBundle> getDhcpRelayBundleList(Long entityId) {
        return getSqlSession().selectList(getNameSpace("getDhcpBundleList"), entityId);
    }

    /**
     * 删除一条DHCP Bundle
     * 
     * @param map
     */
    @Override
    public void deleteDhcpBundle(Map<String, Object> map) {
        getSqlSession().delete(getNameSpace("deleteDhcpBundle"), map);
    }

    /**
     * 新增一条DHCP Bundle
     * 
     * @param DhcpBundle
     */
    @Override
    public void addDhcpBundle(DhcpBundle cmcDhcpBundle) {
        getSqlSession().insert(getNameSpace("addDhcpBundle"), cmcDhcpBundle);
    }

    /**
     * 修改一条DHCP Bundle
     * 
     * @param DhcpBundle
     */
    @Override
    public void updateDhcpBundle(DhcpBundle cmcDhcpBundle) {
        getSqlSession().update(getNameSpace("updateDhcpBundle"), cmcDhcpBundle);
    }

    /**
     * 获取DHCP Server 列表
     * 
     * @return
     */
    @Override
    public List<DhcpServerConfig> getDhcpRelayServerList(Map<String, Object> map) {
        return getSqlSession().selectList(getNameSpace("getDhcpServerList"), map);
    }

    /**
     * 获取DHCP GiAddr 列表
     * 
     * @return
     */
    @Override
    public List<DhcpGiaddrConfig> getDhcpRelayGiAddrList(Map<String, Object> map) {
        return getSqlSession().selectList(getNameSpace("getDhcpGiAddrList"), map);
    }

    /**
     * 获取DHCP IntIp 列表
     * 
     * @return
     */
    @Override
    public List<DhcpIntIpConfig> getDhcpRelayIntIpList(Long entityId) {
        return getSqlSession().selectList(getNameSpace("getDhcpIntIpList"), entityId);
    }

    /**
     * 删除一条DHCP IntIp
     * 
     * @param cmcId
     *            , index
     */
    @Override
    public void deleteDhcpRelayIntIp(Long entityId, String bundleInterface, String ip) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("topCcmtsDhcpBundleInterface", bundleInterface);
        paramMap.put("ip", ip);
        getSqlSession().delete(getNameSpace("deleteDhcpIntIp"), paramMap);
    }

    /**
     * 新增一条DHCP Server
     * 
     * @param CmcDhcpServerConfig
     */
    @Override
    public void addDhcpServer(DhcpServerConfig cmcDhcpServerConfig) {
        getSqlSession().insert(getNameSpace("addDhcpServer"), cmcDhcpServerConfig);
    }

    /**
     * 新增一条DHCP IntIp
     * 
     * @param CmcDhcpIntIp
     */
    @Override
    public void addDhcpRelayIntIp(DhcpIntIpConfig cmcDhcpIntIp) {
        getSqlSession().insert(getNameSpace("addDhcpIntIp"), cmcDhcpIntIp);
    }

    /**
     * 修改一条DHCP IntIp
     * 
     * @param CmcDhcpIntIp
     */
    @Override
    public void updateDhcpRelayIntIp(DhcpIntIpConfig cmcDhcpIntIp) {
        getSqlSession().update(getNameSpace("updateDhcpIntIp"), cmcDhcpIntIp);
    }

    /**
     * 获取DHCP Option60 列表
     * 
     * @return
     */
    @Override
    public List<DhcpOption60> getDhcpRelayOption60List(Map<String, Object> map) {
        return getSqlSession().selectList(getNameSpace("getDhcpOption60List"), map);
    }

    /**
     * 新增一条DHCP Option60
     * 
     * @param CmcDhcpServerConfig
     */
    @Override
    public void addDhcpRelayOption60(DhcpOption60 cmcDhcpOption60) {
        getSqlSession().insert(getNameSpace("addDhcpOption60"), cmcDhcpOption60);
    }

    /**
     * 获取DHCP Option60
     * 
     * @return
     */
    @Override
    public DhcpOption60 getDhcpRelayOption60(Map<String, Object> map) {
        return getSqlSession().selectOne(getNameSpace("getDhcpOption60"), map);
    }

    @Override
    public void batchInsertDhcpRelayGiaddr(final Long entityId, final List<DhcpGiaddrConfig> cmcDhcpGiAdrList) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (DhcpGiaddrConfig giAddr : cmcDhcpGiAdrList) {
                giAddr.setEntityId(entityId);
                try {
                    sqlSession.insert(getNameSpace("addDhcpGiAddr"), giAddr);
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
    public void batchUpdateDhcpRelayGiaddr(final Long entityId, final List<DhcpGiaddrConfig> cmcDhcpGiAdrList) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (DhcpGiaddrConfig giAddr : cmcDhcpGiAdrList) {
                giAddr.setEntityId(entityId);
                try {
                    sqlSession.update(getNameSpace("updateDhcpGiAddr"), giAddr);
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
    public void batchDeleteDhcpRelayGiAddr(final Long entityId, final List<DhcpGiaddrConfig> cmcDhcpGiAdrList) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (DhcpGiaddrConfig giAddr : cmcDhcpGiAdrList) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("entityId", entityId);
                map.put("giAddrId", giAddr.getGiAddrId());
                try {
                    sqlSession.delete(getNameSpace("deleteDhcpGiAddr"), map);
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
    public void batchInsertDhcpRelayOption60(final Long entityId, final List<DhcpOption60> cmcDhcpOption60List) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (DhcpOption60 option60 : cmcDhcpOption60List) {
                option60.setEntityId(entityId);
                try {
                    sqlSession.insert(getNameSpace("addDhcpOption60"), option60);
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
    public void batchUpdateDhcpRelayOption60(final Long entityId, final List<DhcpOption60> cmcDhcpOption60List) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (DhcpOption60 option60 : cmcDhcpOption60List) {
                option60.setEntityId(entityId);
                try {
                    sqlSession.insert(getNameSpace("updateDhcpOption60"), option60);
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
    public void batchDeleteDhcpRelayOption60(final Long entityId, final List<DhcpOption60> cmcDhcpOption60List) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (DhcpOption60 option60 : cmcDhcpOption60List) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("entityId", entityId);
                map.put("option60Id", option60.getOption60Id());
                try {
                    sqlSession.insert(getNameSpace("deleteDhcpOption60"), map);
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
    public void batchInsertDhcpRelayServer(final Long entityId, final List<DhcpServerConfig> cmcDhcpServerConfigList) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (DhcpServerConfig server : cmcDhcpServerConfigList) {
                server.setEntityId(entityId);
                try {
                    sqlSession.insert(getNameSpace("addDhcpServer"), server);
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
    public void batchUpdateDhcpRelayServer(final Long entityId, final List<DhcpServerConfig> cmcDhcpServerConfigList) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (DhcpServerConfig server : cmcDhcpServerConfigList) {
                server.setEntityId(entityId);
                try {
                    sqlSession.insert(getNameSpace("updateDhcpServer"), server);
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
    public void batchDeleteDhcpRelayServer(final Long entityId, final List<DhcpServerConfig> cmcDhcpServerConfigList) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (DhcpServerConfig server : cmcDhcpServerConfigList) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("entityId", entityId);
                map.put("helperId", server.getHelperId());
                try {
                    sqlSession.insert(getNameSpace("deleteDhcpServer"), map);
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
    public List<DhcpIntIpConfig> getDhcpRelayIntIpListByMap(Map<String, Object> map) {
        return getSqlSession().selectList(getNameSpace("getDhcpIntIpListByMap"), map);
    }

    @Override
    public DhcpGiaddrConfig selectDhcpRelayGiAddr(Long entityId, String bundleInterface, Integer deviceType) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("topCcmtsDhcpBundleInterface", bundleInterface);
        map.put("deviceType", deviceType);
        return getSqlSession().selectOne(getNameSpace("getDhcpGiAddrByMap"), map);
    }

    @Override
    public List<Integer> selectDhcpRelayServerIndex(Long entityId, String bundleInterface, Integer deviceType,
            String deviceTypeStr) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("topCcmtsDhcpBundleInterface", bundleInterface);
        if (deviceType != null) {
            map.put("deviceType", deviceType);
        }
        if (deviceTypeStr != null) {
            map.put("deviceTypeStr", deviceTypeStr);
        }
        List<Integer> queryForObject = getSqlSession().selectList(getNameSpace("getDhcpServerIndex"), map);
        return queryForObject;
    }

    @Override
    public List<Integer> selectDhcpRelayOption60Index(Long entityId, String bundleInterface, Integer deviceType,
            String deviceTypeStr) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("topCcmtsDhcpBundleInterface", bundleInterface);
        if (deviceType != null) {
            map.put("deviceType", deviceType);
        }
        if (deviceTypeStr != null) {
            map.put("deviceTypeStr", deviceTypeStr);
        }
        List<Integer> queryForObject = getSqlSession().selectList(getNameSpace("getDhcpOption60Index"), map);
        return queryForObject;
    }

    @Override
    public void insertDhcpRelayOption60(Long entityId, DhcpOption60 option60) {
        option60.setEntityId(entityId);
        getSqlSession().insert(getNameSpace("addDhcpOption60"), option60);
    }

    @Override
    public void insertDhcpRelayServer(Long entityId, DhcpServerConfig server) {
        server.setEntityId(entityId);
        getSqlSession().insert(getNameSpace("addDhcpServer"), server);
    }

    @Override
    public void batchInsertDhcpRelayBundleConfig(final List<DhcpBundle> dhcpDhcpBundles, final Long entityId) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace("deleteAllDhcpBundle"), entityId);
            for (DhcpBundle config : dhcpDhcpBundles) {
                config.setEntityId(entityId);
                try {
                    sqlSession.insert(getNameSpace("addDhcpBundle"), config);
                } catch (Exception e) {
                    logger.debug("", e);
                }
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
    public void batchInsertDhcpRelayServerConfig(final List<DhcpServerConfig> dhcpServerConfigs, final Long entityId) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace("deleteAllDhcpServer"), entityId);
            for (DhcpServerConfig config : dhcpServerConfigs) {
                config.setEntityId(entityId);
                try {
                    sqlSession.insert(getNameSpace("addDhcpServer"), config);
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
    public void batchInsertDhcpRelayGiaddrConfig(final List<DhcpGiaddrConfig> giAddrs, final Long entityId) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace("deleteAllDhcpGiaddrConfig"), entityId);
            for (DhcpGiaddrConfig config : giAddrs) {
                config.setEntityId(entityId);
                try {
                    sqlSession.insert(getNameSpace("addDhcpGiAddr"), config);
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
    public void batchInsertDhcpRelayOption60Config(final List<DhcpOption60> dhcpOption60s, final Long entityId) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace("deleteAllDhcpOption60"), entityId);
            for (DhcpOption60 config : dhcpOption60s) {
                config.setEntityId(entityId);
                try {
                    sqlSession.insert(getNameSpace("addDhcpOption60"), config);
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
    public void batchInsertDhcpRelayIntIpConfig(final List<DhcpIntIpConfig> intIps, final Long entityId) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace("deleteAllDhcpIntIp"), entityId);
            for (DhcpIntIpConfig config : intIps) {
                config.setEntityId(entityId);
                sqlSession.insert(getNameSpace("addDhcpIntIp"), config);
            }
            sqlSession.commit();
        } catch (Exception e) {
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public List<String> selectDeviceTypes(Long entityId, String bundleInterface) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("topCcmtsDhcpBundleInterface", bundleInterface);
        return getSqlSession().selectList(getNameSpace("getDeviceTypes"), map);
    }

    @Override
    public void updateDhcpRelayVlanMap(Long entityId, DhcpRelayVlanMap dhcpRelayVlanMap) {
        dhcpRelayVlanMap.setEntityId(entityId);
        getSqlSession().update(getNameSpace("updateDhcpRelayVlanMap"), dhcpRelayVlanMap);
    }

    @Override
    public void batchUpdateDhcpRelayVlanMap(final Long entityId, final List<DhcpRelayVlanMap> dhcpRelayVlanMap) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (DhcpRelayVlanMap config : dhcpRelayVlanMap) {
                config.setEntityId(entityId);
                sqlSession.update(getNameSpace("updateDhcpRelayVlanMap"), config);
            }
            sqlSession.commit();
        } catch (Exception e) {
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public Integer selectDhcpRelaySwitch(Long entityId) {
        return getSqlSession().selectOne(getNameSpace("getDhcpRelaySwitch"), entityId);
    }

    @Override
    public void updateDhcpRelaySwitch(Long entityId, Integer relaySwitch) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("topCcmtsDhcpRelaySwitch", relaySwitch);
        getSqlSession().update(getNameSpace("updateDhcpRelaySwitch"), map);
    }

    @Override
    public void insertDhcpRelaySwitch(Long entityId, Integer relaySwitch) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("topCcmtsDhcpRelaySwitch", relaySwitch);
        getSqlSession().insert(getNameSpace("addDhcpRelaySwitch"), map);
    }

}
