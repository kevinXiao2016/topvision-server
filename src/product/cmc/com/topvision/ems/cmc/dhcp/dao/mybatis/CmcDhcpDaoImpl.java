/***********************************************************************
 * $Id: CmcDhcpDaoImpl.java,v1.0 2012-2-13 下午04:44:22 $
 * 
 * @author: zhanglongyang
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.dhcp.dao.mybatis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.topvision.ems.cmc.dhcp.dao.CmcDhcpDao;
import com.topvision.ems.cmc.dhcp.facade.domain.CmcDhcpBaseConfig;
import com.topvision.ems.cmc.dhcp.facade.domain.CmcDhcpBundle;
import com.topvision.ems.cmc.dhcp.facade.domain.CmcDhcpGiAddr;
import com.topvision.ems.cmc.dhcp.facade.domain.CmcDhcpIntIp;
import com.topvision.ems.cmc.dhcp.facade.domain.CmcDhcpOption60;
import com.topvision.ems.cmc.dhcp.facade.domain.CmcDhcpPacketVlan;
import com.topvision.ems.cmc.dhcp.facade.domain.CmcDhcpServerConfig;
import com.topvision.ems.cmc.domain.CmcEntity;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * DHCP功能实现
 * 
 * @author zhanglongyang
 * @created @2012-2-13-下午04:44:22
 * 
 */
@Repository("cmcDhcpDao")
public class CmcDhcpDaoImpl extends MyBatisDaoSupport<CmcEntity> implements CmcDhcpDao {
    private Logger logger = LoggerFactory.getLogger(CmcDhcpDaoImpl.class);

    /**
     * 获取DHCP Bundle
     * 
     * @return
     */
    @Override
    public CmcDhcpBundle getCmcDhcpBundle(Map<String, Object> map) {
        return (CmcDhcpBundle) getSqlSession().selectOne(getNameSpace() + "getCmcDhcpBundle", map);
    }

    /**
     * 获取DHCP Server
     * 
     * @return
     */
    @Override
    public CmcDhcpServerConfig getCmcDhcpServerConfig(Map<String, Object> map) {
        return (CmcDhcpServerConfig) getSqlSession().selectOne(getNameSpace() + "getCmcDhcpServer", map);
    }

    /**
     * 获取DHCP GiAddr
     * 
     * @return
     */
    @Override
    public CmcDhcpGiAddr getCmcDhcpGiAddr(Map<String, Object> map) {
        return (CmcDhcpGiAddr) getSqlSession().selectOne(getNameSpace() + "getCmcDhcpGiAddr", map);
    }

    /**
     * 获取DHCP Option60
     * 
     * @return
     */
    @Override
    public CmcDhcpOption60 getCmcDhcpOption60(Map<String, Object> map) {
        return (CmcDhcpOption60) getSqlSession().selectOne(getNameSpace() + "getCmcDhcpOption60", map);
    }

    /**
     * 获取DHCP Bundle 列表
     * 
     * @return
     */
    @Override
    public List<CmcDhcpBundle> getCmcDhcpBundleList(Long entityId) {
        return getSqlSession().selectList(getNameSpace() + "getCmcDhcpBundleList", entityId);
    }

    /**
     * 删除一条DHCP Bundle
     * 
     * @param map
     */
    @Override
    public void deleteDhcpBundle(Map<String, Object> map) {
        getSqlSession().delete(getNameSpace() + "deleteDhcpBundle", map);
    }

    /**
     * 新增一条DHCP Bundle
     * 
     * @param CmcDhcpBundle
     */
    @Override
    public void addDhcpBundle(CmcDhcpBundle cmcDhcpBundle) {
        getSqlSession().insert(getNameSpace() + "addDhcpBundle", cmcDhcpBundle);
    }

    /**
     * 修改一条DHCP Bundle
     * 
     * @param CmcDhcpBundle
     */
    @Override
    public void updateDhcpBundle(CmcDhcpBundle cmcDhcpBundle) {
        getSqlSession().update(getNameSpace() + "updateDhcpBundle", cmcDhcpBundle);
    }

    /**
     * 获取DHCP Server 列表
     * 
     * @return
     */
    @Override
    public List<CmcDhcpServerConfig> getCmcDhcpServerList(Map<String, Object> map) {
        return getSqlSession().selectList(getNameSpace() + "getCmcDhcpServerList", map);
    }

    /**
     * 删除一条DHCP Server
     * 
     * @param map
     */
    @Override
    public void deleteDhcpServer(Map<String, Object> map) {
        getSqlSession().delete(getNameSpace() + "deleteDhcpServer", map);
    }

    /**
     * 获取DHCP GiAddr 列表
     * 
     * @return
     */
    @Override
    public List<CmcDhcpGiAddr> getCmcDhcpGiAddrList(Map<String, Object> map) {
        return getSqlSession().selectList(getNameSpace() + "getCmcDhcpGiAddrList", map);
    }

    /**
     * 删除一条DHCP GiAddr
     * 
     * @param map
     */
    @Override
    public void deleteDhcpGiAddr(Map<String, Object> map) {
        getSqlSession().delete(getNameSpace() + "deleteDhcpGiAddr", map);
    }

    /**
     * 获取DHCP IntIp 列表
     * 
     * @return
     */
    @Override
    public List<CmcDhcpIntIp> getCmcDhcpIntIpList(Long entityId) {
        return getSqlSession().selectList(getNameSpace() + "getCmcDhcpIntIpList", entityId);
    }

    /**
     * 删除一条DHCP IntIp
     * 
     * @param cmcId
     *            , index
     */
    @Override
    public void deleteDhcpIntIp(Long entityId, String bundleInterface, String ip) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("topCcmtsDhcpBundleInterface", bundleInterface);
        paramMap.put("ip", ip);
        getSqlSession().delete(getNameSpace() + "deleteDhcpIntIp", paramMap);
    }

    /**
     * 新增一条DHCP Server
     * 
     * @param CmcDhcpServerConfig
     */
    @Override
    public void addDhcpServer(CmcDhcpServerConfig cmcDhcpServerConfig) {
        getSqlSession().insert(getNameSpace() + "addDhcpServer", cmcDhcpServerConfig);
    }

    /**
     * 修改一条DHCP Server
     * 
     * @param CmcDhcpServerConfig
     */
    @Override
    public void updateDhcpServer(CmcDhcpServerConfig cmcDhcpServerConfig) {
        getSqlSession().update(getNameSpace() + "updateDhcpServer", cmcDhcpServerConfig);
    }

    /**
     * 新增一条DHCP GiAddr
     * 
     * @param CmcDhcpGiAddr
     */
    @Override
    public void addDhcpGiAddr(CmcDhcpGiAddr cmcDhcpGiAddr) {
        getSqlSession().insert(getNameSpace() + "addDhcpGiAddr", cmcDhcpGiAddr);
    }

    /**
     * 修改一条DHCP GiAddr
     * 
     * @param CmcDhcpGiAddr
     */
    @Override
    public void updateDhcpGiAddr(CmcDhcpGiAddr cmcDhcpGiAddr) {
        getSqlSession().update(getNameSpace() + "updateDhcpGiAddr", cmcDhcpGiAddr);
    }

    /**
     * 新增一条DHCP IntIp
     * 
     * @param CmcDhcpIntIp
     */
    @Override
    public void addDhcpIntIp(CmcDhcpIntIp cmcDhcpIntIp) {
        getSqlSession().insert(getNameSpace() + "addDhcpIntIp", cmcDhcpIntIp);
    }

    /**
     * 修改一条DHCP IntIp
     * 
     * @param CmcDhcpIntIp
     */
    @Override
    public void updateDhcpIntIp(CmcDhcpIntIp cmcDhcpIntIp) {
        getSqlSession().update(getNameSpace() + "updateDhcpIntIp", cmcDhcpIntIp);
    }

    /**
     * 获取DHCP index列表
     * 
     * @param cmcId
     *            flag
     */
    @Override
    public List<Integer> getCmcDhcpIndexList(Long cmcId, String bundle, Integer type, Integer flag) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("cmcId", cmcId);
        paramMap.put("bundle", bundle);
        paramMap.put("dhcpType", type);
        if (flag == 1) {
            return getSqlSession().selectList(getNameSpace() + "getCmcDhcpServerIndexList", paramMap);
        } else if (flag == 2) {
            return getSqlSession().selectList(getNameSpace() + "getCmcDhcpGiAddrIndexList", paramMap);
        } else if (flag == 3) {
            return getSqlSession().selectList(getNameSpace() + "getCmcDhcpOption60IndexList", paramMap);
        } else if (flag == 4) {
            return getSqlSession().selectList(getNameSpace() + "getCmcDhcpIntIpIndexList", paramMap);
        } else {
            return (List<Integer>) new ArrayList<Integer>();
        }
    }

    /**
     * 获取DHCP ifIndex列表
     * 
     * @param cmcId
     */
    @Override
    public List<Long> getCmcDhcpIfIndex(Long cmcId) {
        return getSqlSession().selectList(getNameSpace() + "getCmcDhcpIntIpIfIndexList", cmcId);
    }

    /**
     * 获取DHCP Option60 列表
     * 
     * @return
     */
    @Override
    public List<CmcDhcpOption60> getCmcDhcpOption60List(Map<String, Object> map) {
        return getSqlSession().selectList(getNameSpace() + "getCmcDhcpOption60List", map);
    }

    /**
     * 删除一条DHCP Option60
     * 
     * @param map
     */
    @Override
    public void deleteDhcpOption60(Map<String, Object> map) {
        getSqlSession().delete(getNameSpace() + "deleteDhcpOption60", map);
    }

    /**
     * 新增一条DHCP Option60
     * 
     * @param CmcDhcpServerConfig
     */
    @Override
    public void addDhcpOption60(CmcDhcpOption60 cmcDhcpOption60) {
        getSqlSession().insert(getNameSpace() + "addDhcpOption60", cmcDhcpOption60);
    }

    /**
     * 修改一条DHCP Option60
     * 
     * @param CmcDhcpServerConfig
     */
    @Override
    public void updateDhcpOption60(CmcDhcpOption60 cmcDhcpOption60) {
        getSqlSession().update(getNameSpace() + "updateDhcpOption60", cmcDhcpOption60);
    }

    @Override
    public Long getCmcDeviceStyleByCmcId(Long cmcId) {
        return (Long) getSqlSession().selectOne(getNameSpace() + "getCmcDeviceStyleByCmcId", cmcId);
    }

    @Override
    public String getDomainName() {
        return "com.topvision.ems.cmc.dhcp.domain.CmcDhcp";
    }

    @Override
    public void batchInsertDhcpGiaddr(final Long entityId, final List<CmcDhcpGiAddr> cmcDhcpGiAdrList) {

        SqlSession sqlSession = getBatchSession();
        try {

            for (CmcDhcpGiAddr giAddr : cmcDhcpGiAdrList) {
                giAddr.setEntityId(entityId);
                sqlSession.insert(getNameSpace() + "addDhcpGiAddr", giAddr);

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
    public void batchUpdateDhcpGiaddr(final Long entityId, final List<CmcDhcpGiAddr> cmcDhcpGiAdrList) {
        SqlSession sqlSession = getBatchSession();
        try {

            for (CmcDhcpGiAddr giAddr : cmcDhcpGiAdrList) {
                giAddr.setEntityId(entityId);
                sqlSession.insert(getNameSpace() + "updateDhcpGiAddr", giAddr);

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
    public void batchDeleteDhcpGiAddr(final Long entityId, final List<CmcDhcpGiAddr> cmcDhcpGiAdrList) {

        SqlSession sqlSession = getBatchSession();
        try {
            for (CmcDhcpGiAddr giAddr : cmcDhcpGiAdrList) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("entityId", entityId);
                map.put("giAddrId", giAddr.getGiAddrId());
                sqlSession.insert(getNameSpace() + "deleteDhcpGiAddr", map);
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
    public void batchInsertDhcpOption60(final Long entityId, final List<CmcDhcpOption60> cmcDhcpOption60List) {

        SqlSession sqlSession = getBatchSession();
        try {
            for (CmcDhcpOption60 option60 : cmcDhcpOption60List) {
                option60.setEntityId(entityId);
                sqlSession.insert(getNameSpace() + "addDhcpOption60", option60);
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
    public void batchUpdateDhcpOption60(final Long entityId, final List<CmcDhcpOption60> cmcDhcpOption60List) {

        SqlSession sqlSession = getBatchSession();
        try {
            for (CmcDhcpOption60 option60 : cmcDhcpOption60List) {
                option60.setEntityId(entityId);
                sqlSession.insert(getNameSpace() + "updateDhcpOption60", option60);
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
    public void batchDeleteDhcpOption60(final Long entityId, final List<CmcDhcpOption60> cmcDhcpOption60List) {

        SqlSession sqlSession = getBatchSession();
        try {
            for (CmcDhcpOption60 option60 : cmcDhcpOption60List) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("entityId", entityId);
                map.put("option60Id", option60.getOption60Id());
                sqlSession.insert(getNameSpace() + "deleteDhcpOption60", map);
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
    public void batchInsertDhcpServer(final Long entityId, final List<CmcDhcpServerConfig> cmcDhcpServerConfigList) {

        SqlSession sqlSession = getBatchSession();
        try {
            for (CmcDhcpServerConfig server : cmcDhcpServerConfigList) {
                server.setEntityId(entityId);
                sqlSession.insert(getNameSpace() + "addDhcpServer", server);
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
    public void batchUpdateDhcpServer(final Long entityId, final List<CmcDhcpServerConfig> cmcDhcpServerConfigList) {

        SqlSession sqlSession = getBatchSession();
        try {
            for (CmcDhcpServerConfig server : cmcDhcpServerConfigList) {
                server.setEntityId(entityId);
                sqlSession.insert(getNameSpace() + "updateDhcpServer", server);
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
    public void batchDeleteDhcpServer(final Long entityId, final List<CmcDhcpServerConfig> cmcDhcpServerConfigList) {

        SqlSession sqlSession = getBatchSession();
        try {
            for (CmcDhcpServerConfig server : cmcDhcpServerConfigList) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("entityId", entityId);
                map.put("helperId", server.getHelperId());
                sqlSession.insert(getNameSpace() + "deleteDhcpServer", map);
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
    public List<CmcDhcpIntIp> getCmcDhcpIntIpListByMap(Map<String, Object> map) {
        return getSqlSession().selectList(getNameSpace() + "getCmcDhcpIntIpListByMap", map);
    }

    @Override
    public CmcDhcpPacketVlan selectDhcpPacketVlan(Long entityId, String bundleInterface, Integer devicetype) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("topCcmtsDhcpBundleInterface", bundleInterface);
        map.put("deviceType", devicetype);
        return (CmcDhcpPacketVlan) getSqlSession().selectOne(getNameSpace() + "getCmcDhcpPacketVlanList", map);
    }

    @Override
    public List<CmcDhcpPacketVlan> selectDhcpPacketVlanList(Long entityId, String bundleInterface) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("topCcmtsDhcpBundleInterface", bundleInterface);
        return getSqlSession().selectList(getNameSpace() + "getCmcDhcpPacketVlanList", map);
    }

    @Override
    public void insertDhcpPacketVlan(Long entityId, CmcDhcpPacketVlan cmcDhcpPacketVlan) {
        cmcDhcpPacketVlan.setEntityId(entityId);
        getSqlSession().insert(getNameSpace() + "insertCmcDhcpPacketVlan", cmcDhcpPacketVlan);
    }

    @Override
    public void updateDhcpPacketVlan(Long entityId, CmcDhcpPacketVlan cmcDhcpPacketVlan) {
        cmcDhcpPacketVlan.setEntityId(entityId);
        getSqlSession().update(getNameSpace() + "updateDhcpPacketVlan", cmcDhcpPacketVlan);
    }

    @Override
    public void deleteDhcpPacketVlan(Long entityId, String bundleInterface, Integer deviceType) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("topCcmtsDhcpBundleInterface", bundleInterface);
        map.put("topCcmtsDhcpDeviceType", deviceType);
        getSqlSession().delete(getNameSpace() + "deleteDhcpPacketVlan", map);
    }

    @Override
    public CmcDhcpGiAddr selectDhcpGiAddr(Long entityId, String bundleInterface, Integer deviceType) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("topCcmtsDhcpBundleInterface", bundleInterface);
        map.put("deviceType", deviceType);
        return (CmcDhcpGiAddr) getSqlSession().selectOne(getNameSpace() + "getCmcDhcpGiAddrByMap", map);
    }

    @Override
    public List<Integer> selectDhcpServerIndex(Long entityId, String bundleInterface, Integer deviceType) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("topCcmtsDhcpBundleInterface", bundleInterface);
        map.put("deviceType", deviceType);

        List<Integer> selectOne = getSqlSession().selectList(getNameSpace() + "getCmcDhcpServerIndex", map);
        return selectOne;
    }

    @Override
    public List<Integer> selectDhcpOption60Index(Long entityId, String bundleInterface, Integer deviceType) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("topCcmtsDhcpBundleInterface", bundleInterface);
        map.put("deviceType", deviceType);

        List<Integer> selectOne = getSqlSession().selectList(getNameSpace() + "getCmcDhcpOption60Index", map);
        return selectOne;
    }

    @Override
    public void insertDhcpOption60(Long entityId, CmcDhcpOption60 option60) {
        option60.setEntityId(entityId);
        getSqlSession().insert(getNameSpace() + "addDhcpOption60", option60);
    }

    @Override
    public void insertDhcpServer(Long entityId, CmcDhcpServerConfig server) {
        server.setEntityId(entityId);
        getSqlSession().insert(getNameSpace() + "addDhcpServer", server);
    }

    @Override
    public void batchInsertCcDhcpBundleConfig(final List<CmcDhcpBundle> dhcpDhcpBundles, final Long cmcId,
            final Long entityId) {
        getSqlSession().delete(getNameSpace() + "deleteAllCmcDhcpBundle", entityId);
        SqlSession session = getBatchSession();
        try {
            for (CmcDhcpBundle config : dhcpDhcpBundles) {
                config.setCmcId(cmcId);
                config.setEntityId(entityId);
                try {
                    getSqlSession().insert(getNameSpace() + "batchInsertCmcDhcpBundle", config);
                } catch (Exception e) {
                    logger.debug("", e);
                }
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
    public void batchInsertCcDhcpServerConfig(final List<CmcDhcpServerConfig> dhcpServerConfigs, final Long cmcId,
            final Long entityId) {
        getSqlSession().delete(getNameSpace() + "deleteAllCmcDhcpServer", entityId);
        SqlSession session = getBatchSession();
        try {
            for (CmcDhcpServerConfig config : dhcpServerConfigs) {
                config.setCmcId(cmcId);
                config.setEntityId(entityId);
                try {
                    getSqlSession().insert(getNameSpace() + "batchInsertCmcDhcpServerConfig", config);
                } catch (Exception e) {
                    logger.debug("", e);
                }
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
    public void batchInsertCcDhcpGiaddrConfig(final List<CmcDhcpGiAddr> giAddrs, final Long cmcId, final Long entityId) {
        getSqlSession().delete(getNameSpace() + "deleteAllCmcDhcpGiServer", entityId);
        SqlSession session = getBatchSession();
        try {
            for (CmcDhcpGiAddr config : giAddrs) {
                config.setCmcId(cmcId);
                config.setEntityId(entityId);
                try {
                    getSqlSession().insert(getNameSpace() + "batchInsertCmcDhcpGiServerConfig", config);
                } catch (Exception e) {
                    logger.debug("", e);
                }
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
    public void batchInsertCcDhcpOption60Config(final List<CmcDhcpOption60> dhcpOption60s, final Long cmcId,
            final Long entityId) {
        getSqlSession().delete(getNameSpace() + "deleteAllCmcDhcpOption60", entityId);
        SqlSession session = getBatchSession();
        try {
            for (CmcDhcpOption60 config : dhcpOption60s) {
                config.setCmcId(cmcId);
                config.setEntityId(entityId);
                try {
                    getSqlSession().insert(getNameSpace() + "batchInsertCmcDhcpOption60", config);
                } catch (Exception e) {
                    logger.debug("", e);
                }
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
    public void batchInsertCcDhcpIntIpConfig(final List<CmcDhcpIntIp> intIps, final Long entityId) {
        getSqlSession().delete(getNameSpace() + "deleteAllCmcDhcpIntIp", entityId);
        SqlSession session = getBatchSession();
        try {
            for (CmcDhcpIntIp config : intIps) {
                config.setEntityId(entityId);
                getSqlSession().insert(getNameSpace() + "batchInsertCmcDhcpIntIpTable", config);
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
    public void batchInsertCcDhcpPacketVlanConfig(final List<CmcDhcpPacketVlan> packetVlan, final Long entityId) {
        SqlSession session = getBatchSession();
        try {
            for (CmcDhcpPacketVlan config : packetVlan) {
                config.setEntityId(entityId);
                getSqlSession().insert(getNameSpace() + "batchInsertCmcDhcpPacketVlan", config);
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
    public void batchInsertOrUpdateCC8800BCmcDhcpBaseConfig(final CmcDhcpBaseConfig cmcDhcpBaseConfig,
            final Long entityId) {
        SqlSession session = getBatchSession();
        try {
            cmcDhcpBaseConfig.setEntityId(entityId);
            if (getSqlSession().selectOne(getNameSpace() + "getCmcDhcpBaseConfigByEntityId", entityId) != null) {
                getSqlSession().update(getNameSpace() + "updateCmcDhcpBaseConfigByEntityId", cmcDhcpBaseConfig);
            } else {
                session.insert(getNameSpace() + "insertCmcDhcpBaseConfig", cmcDhcpBaseConfig);
            }
            session.commit();
        } catch (Exception e) {
            logger.error("", e);
            session.rollback();
        } finally {
            session.close();
        }
    }
}
