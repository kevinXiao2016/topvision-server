/***********************************************************************
 * $Id: CmcConfigDaoImpl.java,v1.0 2012-2-13 下午04:42:24 $
 *
 * @author: zhanglongyang
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.config.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.cmc.config.dao.CmcConfigDao;
import com.topvision.ems.cmc.config.facade.domain.CmcEmsConfig;
import com.topvision.ems.cmc.config.facade.domain.CmcSnmpCommunityTable;
import com.topvision.ems.cmc.config.facade.domain.CmcSysConfig;
import com.topvision.ems.cmc.dhcp.facade.domain.CmcDhcpBaseConfig;
import com.topvision.ems.cmc.domain.CmcEntity;
import com.topvision.ems.cmc.facade.domain.CmcSystemBasicInfo;
import com.topvision.ems.cmc.facade.domain.CmcSystemIpInfo;
import com.topvision.ems.cmc.facade.domain.DocsDevEvControl;
import com.topvision.ems.cmc.sni.facade.domain.CcmtsSniObject;
import com.topvision.ems.cmc.vlan.domain.CmcPrimaryVlan;
import com.topvision.ems.cmc.vlan.facade.domain.CmcVlanPrimaryInterface;
import com.topvision.ems.cmc.vlan.facade.domain.CmcVlanPrimaryIp;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * 配置功能实现
 * 
 * @author zhanglongyang
 * @created @2012-2-13-下午04:42:24
 */
@Repository("cmcConfigDao")
public class CmcConfigDaoImpl extends MyBatisDaoSupport<CmcEntity> implements CmcConfigDao {

    public String getDomainName() {
        return "com.topvision.ems.cmc.config.facade.domain.CmcConfig";
    }

    /**
     * 获得CMC的基本配置属性
     * 
     * @param cmcId
     *            设备ID
     * @return
     */
    @Override
    public CmcSystemBasicInfo getCmcBasicInfo(Long cmcId) {
        return (CmcSystemBasicInfo) getSqlSession().selectOne(getNameSpace() + "getCmcBasicInfo", cmcId);
    }

    /**
     * 获得CMC的IP配置属性
     * 
     * @param cmcId
     *            设备ID
     * @return
     */
    @Override
    public CmcSystemIpInfo getCmcIpInfo(Long cmcId) {
        return (CmcSystemIpInfo) getSqlSession().selectOne(getNameSpace() + "getCmcIpInfo", cmcId);
    }

    /**
     * 获得CMC的EMS配置属性
     * 
     * @param cmcId
     *            设备ID
     * @return
     */
    @Override
    public List<CmcEmsConfig> getCmcEmsList(Long cmcId) {
        return getSqlSession().selectList(getNameSpace() + "getCmcEmsList", cmcId);
    }

    /**
     * 修改基本配置信息
     * 
     * @param cmcId
     *            name location contact
     */
    @Override
    public void updateCmcConfigBasicInfo(Long cmcId, String cmcName, String location, String cmcContact) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("cmcId", cmcId);
        paramMap.put("cmcName", cmcName);
        paramMap.put("location", location);
        paramMap.put("cmcContact", cmcContact);
        getSqlSession().update(getNameSpace() + "updateCmcConfigBasicInfo", paramMap);
    }

    /**
     * 修改IP配置信息
     * 
     * @param ipInfo
     */
    @Override
    public void updateCmcConfigIpInfo(CmcSystemIpInfo ipInfo) {
        getSqlSession().update(getNameSpace() + "updateCmcConfigIpInfo", ipInfo);
    }

    @Override
    public void updateCmcConfigGateway(CmcSystemIpInfo ipInfo) {
        getSqlSession().update(getNameSpace() + "updateCmcConfigGateway", ipInfo);
    }

    @Override
    public void modifyPhysicalInterfacePreferredMode(Long cmcEntityId, Integer physicalInterfaceMode) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("cmcEntityId", "" + cmcEntityId);
        map.put("topCcmtsSniEthInt", "" + physicalInterfaceMode);
        getSqlSession().update(getNameSpace() + "modifyPhysicalInterfacePreferredMode", map);
    }

    @Override
    public void modifyMainTransmissionMode(Long cmcEntityId, Integer transmissionMode) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("cmcEntityId", "" + cmcEntityId);
        map.put("topCcmtsSniMainInt", "" + transmissionMode);
        getSqlSession().update(getNameSpace() + "modifyMainTransmissionMode", map);
    }

    @Override
    public void modifyBackupTransmissionMode(Long cmcEntityId, Integer transmissionMode) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("cmcEntityId", "" + cmcEntityId);
        map.put("topCcmtsSniBackupInt", "" + transmissionMode);
        getSqlSession().update(getNameSpace() + "modifyMainTransmissionMode", map);
    }

    @Override
    public void insertCmcEmsConfig(CmcEmsConfig cmcEmsConfig) {
        getSqlSession().insert(getNameSpace() + "insertCmcEmsConfig", cmcEmsConfig);
    }

    @Override
    public void updateCmcEmsConfig(CmcEmsConfig cmcEmsConfig) {
        getSqlSession().update(getNameSpace() + "modifyCmcEmsConfig", cmcEmsConfig);
    }

    @Override
    public void insertDevEvControl(DocsDevEvControl docsDevEvControl) {
        getSqlSession().insert(getNameSpace() + "insertDevEvControl", docsDevEvControl);
    }

    @Override
    public void updateDevEvControl(DocsDevEvControl docsDevEvControl) {
        getSqlSession().update(getNameSpace() + "modifyDevEvControl", docsDevEvControl);
    }

    @Override
    public CmcVlanPrimaryInterface getCC8800BVlanPriInterface(Long cmcId) {
        return (CmcVlanPrimaryInterface) getSqlSession().selectOne(getNameSpace() + "getCmcVlanPriInterfaceByCmcId",
                cmcId);
    }

    @Override
    public List<CmcVlanPrimaryIp> getCC8800BVlanPriIpList(Long cmcId) {
        return getSqlSession().selectList(getNameSpace() + "getCmcVlanPrimaryIpByCmcId", cmcId);
    }

    @Override
    public CmcDhcpBaseConfig getCC8800BCmcDhcpBaseConfig(Long entityId) {
        return (CmcDhcpBaseConfig) getSqlSession().selectOne(getNameSpace() + "getCmcDhcpBaseConfigByEntityId",
                entityId);
    }

    @Override
    public CmcPrimaryVlan getCC8800BCcPrimaryVlanAsSnmp(Long cmcId, Integer vlanId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cmcId", cmcId);
        map.put("vlanId", vlanId);
        return (CmcPrimaryVlan) getSqlSession().selectOne(
                getNameSpace() + "getCC8800BCcPrimaryVlanAsSnmpByCmcIdAndVlanId", map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcConfigDao#getCC8800BSniConfig(java.lang.
     * Long)
     */
    @Override
    public CcmtsSniObject getCC8800BSniConfig(Long cmcId) {
        return (CcmtsSniObject) getSqlSession().selectOne(getNameSpace() + "getCC8800BSniConfig", cmcId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcConfigDao#CC8800BSniConfig(com.topvision
     * .ems.cmc.facade.domain .CcmtsSniObject)
     */
    @Override
    public void updateCC8800BSniConfig(CcmtsSniObject sni) {
        getSqlSession().update(getNameSpace() + "updateCC8800BSniConfig", sni);
    }

    @Override
    public CmcVlanPrimaryIp getCC8800BVlanPriIpByCmcIdAndVId(Long cmcId, Integer vlanId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cmcId", cmcId);
        map.put("vlanId", vlanId);
        return (CmcVlanPrimaryIp) getSqlSession().selectOne(getNameSpace() + "getCC8800BVlanPriIpByCmcIdAndVId", map);
    }

    @Override
    public void updateCC8800BCcPrimaryVlan(CmcVlanPrimaryIp cmcVlanPrimaryIp) {
        getSqlSession().update(getNameSpace() + "updateCC8800BCcPrimaryVlan", cmcVlanPrimaryIp);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcConfigDao#getCmcSnmpCommunityTable(java.
     * lang.Long)
     */
    @Override
    public CmcSnmpCommunityTable getCmcSnmpCommunityTable(Long entityId) {
        return (CmcSnmpCommunityTable) getSqlSession().selectOne(getNameSpace() + "getCmcSnmpCommunityTable", entityId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcConfigDao#updateCC8800BSnmpInfo(com.topvision
     * .ems.cmc.facade.domain.CmcSnmpCommunityTable)
     */
    @Override
    public void updateCC8800BSnmpInfo(CmcSnmpCommunityTable snmpCommunityTable) {
        getSqlSession().update(getNameSpace() + "updateCC8800BSnmpInfo", snmpCommunityTable);
    }

    @Override
    public CmcSysConfig selectCmcSysConfig(Long cmcId) {
        return (CmcSysConfig) getSqlSession().selectOne(getNameSpace() + "selectCmcSysConfig", cmcId);
    }

    @Override
    public void insertCmcSysConfig(Long cmcId, CmcSysConfig cmcSysConfig) {
        getSqlSession().delete(getNameSpace() + "deleteCmcSysConfig", cmcId);
        cmcSysConfig.setCmcId(cmcId);
        getSqlSession().insert(getNameSpace() + "insertCmcSysConfig", cmcSysConfig);
    }

    @Override
    public void batchInsertOrUpdateCC8800BVlanPriInterface(final CmcVlanPrimaryInterface cmcVlanPrimaryInterface,
            final Long cmcId) {
        SqlSession session = getBatchSession();
        try {
            cmcVlanPrimaryInterface.setCmcId(cmcId);
            if ((getSqlSession().selectOne(getNameSpace() + "getCmcVlanPriInterfaceByCmcId", cmcId)) != null) {
                getSqlSession().update(getNameSpace() + "updateCmcVlanPriInterfaceByCmcId", cmcVlanPrimaryInterface);
            } else {
                session.insert(getNameSpace() + "insertCmcVlanPriInterface", cmcVlanPrimaryInterface);
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
    public void batchInsertOrUpdateCC8800BVlanPriIpList(final List<CmcVlanPrimaryIp> cmcVlanPrimaryIpList,
            final Long cmcId) {
        SqlSession session = getBatchSession();
        try {
            for (CmcVlanPrimaryIp cmcVlanPrimaryIp : cmcVlanPrimaryIpList) {
                cmcVlanPrimaryIp.setCmcId(cmcId);
                if ((getSqlSession().selectOne(getNameSpace() + "getCmcVlanPrimaryIpByCmcId", cmcId)) != null) {
                    getSqlSession().delete(getNameSpace() + "deleteCmcVlanPrimaryIpByCmcId", cmcId);
                    session.insert(getNameSpace() + "insertCmcVlanPrimaryIp", cmcVlanPrimaryIp);

                } else {
                    session.insert(getNameSpace() + "insertCmcVlanPrimaryIp", cmcVlanPrimaryIp);
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
}
