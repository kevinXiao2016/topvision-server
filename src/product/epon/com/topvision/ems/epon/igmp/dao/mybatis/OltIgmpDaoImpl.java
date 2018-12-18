/***********************************************************************
 * $Id: OltIgmpDao.java,v1.0 2013-10-25 下午4:23:05 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.igmp.dao.mybatis;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.domain.IgmpForwardingSnooping;
import com.topvision.ems.epon.igmp.dao.OltIgmpDao;
import com.topvision.ems.epon.igmp.domain.IgmpControlledMcCdrTable;
import com.topvision.ems.epon.igmp.domain.IgmpControlledMcPreviewIntervalTable;
import com.topvision.ems.epon.igmp.domain.IgmpControlledMulticastPackageTable;
import com.topvision.ems.epon.igmp.domain.IgmpControlledMulticastUserAuthorityTable;
import com.topvision.ems.epon.igmp.domain.IgmpEntityTable;
import com.topvision.ems.epon.igmp.domain.IgmpForwardingTable;
import com.topvision.ems.epon.igmp.domain.IgmpMcOnuTable;
import com.topvision.ems.epon.igmp.domain.IgmpMcOnuVlanTransTable;
import com.topvision.ems.epon.igmp.domain.IgmpMcParamMgmtObjects;
import com.topvision.ems.epon.igmp.domain.IgmpMcSniConfigMgmtObjects;
import com.topvision.ems.epon.igmp.domain.IgmpMcUniConfigTable;
import com.topvision.ems.epon.igmp.domain.IgmpProxyParaTable;
import com.topvision.ems.epon.igmp.domain.TopIgmpForwardingSnooping;
import com.topvision.ems.epon.igmp.domain.TopMcForwardingOnuTable;
import com.topvision.ems.epon.igmp.domain.TopMcForwardingPortTable;
import com.topvision.ems.epon.igmp.domain.TopMcForwardingSlotTable;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author flack
 * @created @2013-10-25-下午4:23:05
 *
 */
@Repository("oltIgmpDao")
public class OltIgmpDaoImpl extends MyBatisDaoSupport<Object> implements OltIgmpDao {

    /**
     * 添加一个组播组
     * 
     * @param igmpControlledMulticastPackageTable
     *            组播组
     */
    @Override
    public void addIgmpMvlan(IgmpControlledMulticastPackageTable igmpControlledMulticastPackageTable) {
        getSqlSession().insert(getNameSpace() + "insertIgmpControlledMulticastPackageTable",
                igmpControlledMulticastPackageTable);
    }

    /**
     * 添加一个频道
     * 
     * @param igmpProxyParaTable
     *            频道
     */
    @Override
    public void addIgmpProxy(IgmpProxyParaTable igmpProxyParaTable) {
        getSqlSession().insert(getNameSpace() + "insertIgmpProxyParaTable", igmpProxyParaTable);
    }

    /**
     * 添加组播VLAN转换规则
     * 
     * @param igmpMcOnuVlanTransTable
     *            组播VLAN转换规则
     */
    @Override
    public void addIgmpVlanTrans(IgmpMcOnuVlanTransTable igmpMcOnuVlanTransTable) {
        getSqlSession().insert(getNameSpace() + "insertIgmpMcOnuVlanTransTable", igmpMcOnuVlanTransTable);
    }

    /**
     * 删除一个组播组
     * 
     * @param igmpControlledMulticastPackageTable
     * 
     */
    @Override
    public void deleteIgmpMvlan(IgmpControlledMulticastPackageTable igmpControlledMulticastPackageTable) {
        getSqlSession().delete(getNameSpace() + "deleteIgmpControlledMulticastPackageTable",
                igmpControlledMulticastPackageTable);
    }

    /**
     * 删除一个频道
     * 
     * @param igmpProxyParaTable
     */
    @Override
    public void deleteIgmpProxy(IgmpProxyParaTable igmpProxyParaTable) {
        getSqlSession().delete(getNameSpace() + "deleteIgmpProxyParaTable", igmpProxyParaTable);
    }

    /**
     * 删除组播VLAN转换规则
     * 
     * @param igmpMcOnuVlanTransTable
     */
    @Override
    public void deleteIgmpVlanTrans(IgmpMcOnuVlanTransTable igmpMcOnuVlanTransTable) {
        getSqlSession().delete(getNameSpace() + "deleteIgmpMcOnuVlanTransTable", igmpMcOnuVlanTransTable);
    }

    /**
     * 获取呼叫信息记录
     * 
     * @param entityId
     *            设备ID
     * @return <IgmpControlledMcCdrTable>
     */
    @Override
    public List<IgmpControlledMcCdrTable> getIgmpControlledMcCdr(Long entityId) {
        return getSqlSession().selectList(getNameSpace() + "getIgmpControlledMcCdr", entityId);
    }

    /**
     * 获取组播组当前活跃pon口列表
     * 
     * @param entityId
     *            设备ID
     * @return List<IgmpForwardingTable>
     */
    @Override
    public List<IgmpForwardingTable> getIgmpForwardingInfo(Long entityId) {
        return getSqlSession().selectList(getNameSpace() + "getIgmpForwardingInfo", entityId);
    }

    /**
     * 获得IGMP全局属性
     * 
     * @param entityId
     *            设备ID
     * @return
     */
    @Override
    public IgmpEntityTable getIgmpGlobalInfo(Long entityId) {
        return getSqlSession().selectOne(getNameSpace() + "getIgmpGlobalInfo", entityId);
    }

    /**
     * 获取最大组播组数目
     * 
     * @param entityId
     *            设备ID
     * @return
     */
    @Override
    public IgmpMcParamMgmtObjects getIgmpMaxGroupNum(Long entityId) {
        return getSqlSession().selectOne(getNameSpace() + "getIgmpMaxGroupNum", entityId);
    }

    /**
     * 获取ONU IGMP信息
     * 
     * @param entityId
     *            设备ID
     * @param onuIndex
     *            ONU INDEX
     * @return IgmpMcOnuTable
     */
    @Override
    public IgmpMcOnuTable getIgmpMcOnuInfo(Long entityId, Long onuIndex) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("entityId", "" + entityId);
        map.put("onuIndex", "" + onuIndex);
        return getSqlSession().selectOne(getNameSpace() + "getIgmpMcOnuInfo", map);
    }

    /**
     * 获取所有ONU IGMP信息
     * 
     * @param entityId
     *            设备ID
     * @return List<IgmpMcOnuTable>
     */
    @Override
    public List<IgmpMcOnuTable> getIgmpMcOnuInfo(Long entityId) {
        return getSqlSession().selectList(getNameSpace() + "getIgmpMcOnuInfoList", entityId);
    }

    /**
     * 获取UNI口的IGMP信息
     * 
     * @param entityId
     *            设备ID
     * @param uniIndex
     *            UNI INDEX
     * @return List<IgmpMcUniConfigTable>
     */
    @Override
    public IgmpMcUniConfigTable getIgmpMcUniConfig(Long entityId, Long uniIndex) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("entityId", "" + entityId);
        map.put("uniIndex", "" + uniIndex);
        return getSqlSession().selectOne(getNameSpace() + "getIgmpMcUniConfig", map);
    }

    /**
     * 获取组播组列表
     * 
     * @param entityId
     *            设备ID
     * @return List<IgmpControlledMulticastPackageTable>
     */
    @Override
    public List<IgmpControlledMulticastPackageTable> getIgmpMvlanInfo(Long entityId) {
        return getSqlSession().selectList(getNameSpace() + "getIgmpMvlanInfo", entityId);
    }

    /**
     * 获取组播组频道LIST
     * 
     * @param entityId
     *            设备ID
     * @param cmIndex
     *            组播组INDEX
     * @return IgmpControlledMulticastPackageTable
     */
    @Override
    public IgmpControlledMulticastPackageTable getIgmpControlledMulticastPackageTable(Long entityId, Integer cmIndex) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("entityId", "" + entityId);
        map.put("cmIndex", "" + cmIndex);
        return getSqlSession().selectOne(getNameSpace() + "getIgmpControlledMulticastPackageTable", map);
    }

    /**
     * 获取 PON口IGMP信息
     * 
     * @param entityId
     *            设备ID
     * @param portIndex
     *            PON口INDEX
     * @return IgmpControlledMulticastUserAuthorityTable
     */
    @Override
    public IgmpControlledMulticastUserAuthorityTable getIgmpControlledMulticastUserAuthorityTable(Long entityId,
            Long portIndex) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("entityId", "" + entityId);
        map.put("portIndex", "" + portIndex);
        return getSqlSession().selectOne(getNameSpace() + "getIgmpControlledMulticastUserAuthorityTable", map);
    }

    /**
     * 获取组播组频道列表
     * 
     * @param entityId
     *            设备ID
     * @return List<IgmpProxyParaTable>
     */
    @Override
    public List<IgmpProxyParaTable> getIgmpProxyInfo(Long entityId) {
        return getSqlSession().selectList(getNameSpace() + "getIgmpProxyInfo", entityId);
    }

    /**
     * 获取SNI口的IGMP全局属性
     * 
     * @param entityId
     *            设备ID
     * @return
     */
    @Override
    public IgmpMcSniConfigMgmtObjects getIgmpSniConfig(Long entityId) {
        return getSqlSession().selectOne(getNameSpace() + "getIgmpSniConfig", entityId);
    }

    /**
     * 获取组播VLAN转换列表
     * 
     * @param entityId
     *            设备ID
     * @return List<IgmpMcOnuVlanTransTable>
     */
    @Override
    public List<IgmpMcOnuVlanTransTable> getIgmpVlanTrans(Long entityId) {
        return getSqlSession().selectList(getNameSpace() + "getIgmpVlanTrans", entityId);
    }

    /**
     * 修改IGMP全局属性
     * 
     * @param igmpEntityTable
     *            IGMP全局配置
     */
    @Override
    public void modifyIgmpGlobalInfo(IgmpEntityTable igmpEntityTable) {
        getSqlSession().update(getNameSpace() + "updateIgmpGlobalInfo", igmpEntityTable);
    }

    /**
     * 修改最大组播组数
     * 
     * @param igmpMcParamMgmtObjects
     */
    @Override
    public void modifyIgmpMaxGroupNum(IgmpMcParamMgmtObjects igmpMcParamMgmtObjects) {
        getSqlSession().update(getNameSpace() + "updateIgmpMaxGroupNum", igmpMcParamMgmtObjects);
    }

    /**
     * 修改ONU IGMP信息
     * 
     * @param igmpMcOnuTable
     *            ONU组播组信息
     */
    @Override
    public void modifyIgmpMcOnuInfo(IgmpMcOnuTable igmpMcOnuTable) {
        getSqlSession().update(getNameSpace() + "updateIgmpMcOnuInfo", igmpMcOnuTable);
    }

    /**
     * 修改UNI口的IGMP信息
     * 
     * @param igmpMcUniConfigTable
     *            UNI口IGMP信息
     */
    @Override
    public void modifyIgmpMcUniConfig(IgmpMcUniConfigTable igmpMcUniConfigTable) {
        getSqlSession().update(getNameSpace() + "updateIgmpMcUniConfig", igmpMcUniConfigTable);
    }

    /**
     * 修改组播组信息
     * 
     * @param igmpControlledMulticastPackageTable
     *            组播组信息
     */
    @Override
    public void modifyIgmpMvlanInfo(IgmpControlledMulticastPackageTable igmpControlledMulticastPackageTable) {
        getSqlSession().update(getNameSpace() + "updateIgmpMvlanInfo", igmpControlledMulticastPackageTable);
    }

    /**
     * 修改频道
     * 
     * @param igmpProxyParaTable
     *            频道
     */
    @Override
    public void modifyIgmpProxyInfo(IgmpProxyParaTable igmpProxyParaTable) {
        getSqlSession().update(getNameSpace() + "updateIgmpProxyInfo", igmpProxyParaTable);
    }

    /**
     * 修改IGMP SNI口全局属性
     * 
     * @param igmpMcSniConfigMgmtObjects
     */
    @Override
    public void modifyIgmpSniConfig(IgmpMcSniConfigMgmtObjects igmpMcSniConfigMgmtObjects) {
        getSqlSession().update(getNameSpace() + "updateIgmpSniConfig", igmpMcSniConfigMgmtObjects);
    }

    /**
     * 修改组播VLAN转换规则
     * 
     * @param igmpMcOnuVlanTransTable
     *            组播VLAN转换规则
     */
    @Override
    public void modifyIgmpVlanTrans(IgmpMcOnuVlanTransTable igmpMcOnuVlanTransTable) {
        getSqlSession().update(getNameSpace() + "updateIgmpVlanTrans", igmpMcOnuVlanTransTable);
    }

    /**
     * 新增UNI口 IGMP信息
     * 
     * @param igmpControlledMulticastUserAuthorityTable
     * 
     */
    @Override
    public void addMulticastUserAuthorityList(
            IgmpControlledMulticastUserAuthorityTable igmpControlledMulticastUserAuthorityTable) {
        getSqlSession().insert(getNameSpace() + "insertIgmpControlledMulticastUserAuthorityTable",
                igmpControlledMulticastUserAuthorityTable);
    }

    /**
     * 新增UNI口可控组播配置信息
     * 
     * @param igmpMcUniConfig
     * 
     */
    @Override
    public void addMcUniConfig(IgmpMcUniConfigTable igmpMcUniConfig) {
        getSqlSession().insert(getNameSpace() + "insertIgmpMcUniConfigTable", igmpMcUniConfig);
    }

    /**
     * 修改UNI口 IGMP信息
     * 
     * @param igmpControlledMulticastUserAuthorityTable
     * 
     */
    @Override
    public void modifyMulticastUserAuthorityList(
            IgmpControlledMulticastUserAuthorityTable igmpControlledMulticastUserAuthorityTable) {
        getSqlSession().update(getNameSpace() + "modifyMulticastUserAuthorityList",
                igmpControlledMulticastUserAuthorityTable);
    }

    /**
     * 删除UNI口可控组播配置信息
     * 
     * @param igmpMcUniConfig
     * 
     */
    @Override
    public void deleteMcUniConfig(IgmpMcUniConfigTable igmpMcUniConfig) {
        getSqlSession().delete(getNameSpace() + "deleteIgmpMcUniConfig", igmpMcUniConfig);
    }

    /**
     * 删除UNI口 IGMP信息
     * 
     * @param igmpControlledMulticastUserAuthorityTable
     * 
     */
    @Override
    public void deleteMulticastUserAuthorityList(
            IgmpControlledMulticastUserAuthorityTable igmpControlledMulticastUserAuthorityTable) {
        getSqlSession().delete(getNameSpace() + "deleteIgmpControlledMulticastUserAuthorityTable",
                igmpControlledMulticastUserAuthorityTable);
    }

    /**
     * 修改频道的组播组vlan ID
     * 
     * @param igmpControlledMulticastPackageTable
     */
    @Override
    public void modifyMulticastPackage(IgmpControlledMulticastPackageTable igmpControlledMulticastPackageTable) {
        getSqlSession().update(getNameSpace() + "updateMulticastPackage", igmpControlledMulticastPackageTable);
    }

    /**
     * 保存呼叫信息记录表
     * 
     * @param igmpControlledMcCdrTables
     */
    @Override
    public void saveIgmpControlledMcCdrTable(Long entityId,
            final List<IgmpControlledMcCdrTable> igmpControlledMcCdrTables) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace() + "deleteIgmpControlledMcCdrTableAll", entityId);
            for (IgmpControlledMcCdrTable igmpControlledMcCdrTable : igmpControlledMcCdrTables) {
                sqlSession.insert(getNameSpace() + "insertIgmpControlledMcCdrTable", igmpControlledMcCdrTable);
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 保存可控组播业务包表
     * 
     * @param igmpControlledMulticastPackageTables
     * 
     */
    @Override
    public void saveIgmpControlledMulticastPackageTable(Long entityId,
            final List<IgmpControlledMulticastPackageTable> igmpControlledMulticastPackageTables) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace() + "deleteIgmpControlledMulticastPackageTableAll", entityId);
            for (IgmpControlledMulticastPackageTable igmpControlledMulticastPackageTable : igmpControlledMulticastPackageTables) {
                sqlSession.insert(getNameSpace() + "insertIgmpControlledMulticastPackageTable",
                        igmpControlledMulticastPackageTable);
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 保存可控组播用户权限表
     * 
     * @param igmpControlledMulticastUserAuthorityTables
     * 
     */
    @Override
    public void saveIgmpControlledMulticastUserAuthorityTable(Long entityId,
            final List<IgmpControlledMulticastUserAuthorityTable> igmpControlledMulticastUserAuthorityTables) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace() + "deleteIgmpControlledMulticastUserAuthorityTableAll", entityId);
            for (IgmpControlledMulticastUserAuthorityTable igmpControlledMulticastUserAuthorityTable : igmpControlledMulticastUserAuthorityTables) {
                sqlSession.insert(getNameSpace() + "insertIgmpControlledMulticastUserAuthorityTable",
                        igmpControlledMulticastUserAuthorityTable);
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 保存IGMP实体表
     * 
     * @param igmpEntityTables
     */
    @Override
    public void saveIgmpEntityTable(Long entityId, final List<IgmpEntityTable> igmpEntityTables) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace() + "deleteIgmpEntityTableAll", entityId);
            for (IgmpEntityTable igmpEntityTable : igmpEntityTables) {
                sqlSession.insert(getNameSpace() + "insertIgmpEntityTable", igmpEntityTable);
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 保存活跃PON口表
     * 
     * @param igmpForwardingTables
     */
    @Override
    public void saveIgmpForwardingTable(Long entityId, final List<IgmpForwardingTable> igmpForwardingTables) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace() + "deleteIgmpForwardingTableAll", entityId);
            for (IgmpForwardingTable igmpForwardingTable : igmpForwardingTables) {
                sqlSession.insert(getNameSpace() + "insertIgmpForwardingTable", igmpForwardingTable);
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 保存 Onu IGMP参数表
     * 
     * @param igmpMcOnuTables
     */
    @Override
    public void saveIgmpMcOnuTable(Long entityId, final List<IgmpMcOnuTable> igmpMcOnuTables) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace() + "deleteIgmpMcOnuTableAll", entityId);
            for (IgmpMcOnuTable igmpMcOnuTable : igmpMcOnuTables) {
                sqlSession.insert(getNameSpace() + "insertIgmpMcOnuTable", igmpMcOnuTable);
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 保存组播组转换规则表
     * 
     * @param igmpMcOnuVlanTransTables
     */
    @Override
    public void saveIgmpMcOnuVlanTransTable(Long entityId, final List<IgmpMcOnuVlanTransTable> igmpMcOnuVlanTransTables) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace() + "deleteIgmpMcOnuVlanTransTableAll", entityId);
            for (IgmpMcOnuVlanTransTable igmpMcOnuVlanTransTable : igmpMcOnuVlanTransTables) {
                sqlSession.insert(getNameSpace() + "insertIgmpMcOnuVlanTransTable", igmpMcOnuVlanTransTable);
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 保存最大组播组数
     * 
     * @param entityId
     * @param igmpMcParamMgmtObject
     */
    @Override
    public void saveIgmpMcParamMgmtObjects(Long entityId, IgmpMcParamMgmtObjects igmpMcParamMgmtObject) {
        // TODO 此处最好的办法是判断是否更新 而不是先删除后添加
        getSqlSession().delete(getNameSpace() + "deleteIgmpMcParamMgmtObjectsAll", entityId);
        getSqlSession().insert(getNameSpace() + "insertIgmpMcParamMgmtObjects", igmpMcParamMgmtObject);

    }

    /**
     * 保存Sni口组播组属性
     * 
     * @param entityId
     * @param igmpMcSniConfigMgmtObject
     */
    @Override
    public void saveIgmpMcSniConfigMgmtObjects(Long entityId, IgmpMcSniConfigMgmtObjects igmpMcSniConfigMgmtObject) {
        getSqlSession().delete(getNameSpace() + "deleteIgmpMcSniConfigMgmtObjectsAll", entityId);
        getSqlSession().insert(getNameSpace() + "insertIgmpMcSniConfigMgmtObjects", igmpMcSniConfigMgmtObject);
    }

    /**
     * 保存Uni口组播组属性
     * 
     * @param igmpMcUniConfigTables
     */
    @Override
    public void saveIgmpMcUniConfigTable(Long entityId, final List<IgmpMcUniConfigTable> igmpMcUniConfigTables) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace() + "deleteIgmpMcUniConfigTableAll", entityId);
            for (IgmpMcUniConfigTable igmpMcUniConfigTable : igmpMcUniConfigTables) {
                sqlSession.insert(getNameSpace() + "insertIgmpMcUniConfigTable", igmpMcUniConfigTable);
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 保存频道表
     * 
     * @param igmpProxyParaTables
     */
    @Override
    public void saveIgmpProxyParaTable(Long entityId, final List<IgmpProxyParaTable> igmpProxyParaTables) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace() + "deleteIgmpProxyParaTableAll", entityId);
            for (IgmpProxyParaTable igmpProxyParaTable : igmpProxyParaTables) {
                sqlSession.insert(getNameSpace() + "insertIgmpProxyParaTable", igmpProxyParaTable);
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.dao.OltIgmpDao#getTopMcForwardingSlot(java.lang.Long,
     * java.lang.Integer)
     */
    @Override
    public TopMcForwardingSlotTable getTopMcForwardingSlot(Long entityId, Integer proxyId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("proxyId", proxyId);
        return (TopMcForwardingSlotTable) getSqlSession().selectOne(getNameSpace() + "getTopMcForwardingSlot", map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.dao.OltIgmpDao#getTopMcForwardingPort(java.lang.Long,
     * java.lang.Integer, java.lang.Integer)
     */
    @Override
    public TopMcForwardingPortTable getTopMcForwardingPort(Long entityId, Integer proxyId, Integer slotNo) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("proxyId", proxyId);
        map.put("slotNo", slotNo);
        return (TopMcForwardingPortTable) getSqlSession().selectOne(getNameSpace() + "getTopMcForwardingPort", map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.dao.OltIgmpDao#getTopMcForwardingPort(java.lang.Long,
     * java.lang.Integer, java.lang.Integer, java.lang.Integer)
     */
    @Override
    public TopMcForwardingOnuTable getTopMcForwardingOnu(Long entityId, Integer proxyId, Integer slotNo,
            Integer ponPortNo) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("proxyId", proxyId);
        map.put("slotNo", slotNo);
        map.put("ponPortNo", ponPortNo);
        return (TopMcForwardingOnuTable) getSqlSession().selectOne(getNameSpace() + "getTopMcForwardingOnu", map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.dao.OltIgmpDao#batchInsertTopMcForwardingSlotTables(java.util.List,
     * java.lang.Long)
     */
    @Override
    public void batchInsertTopMcForwardingSlotTables(final List<TopMcForwardingSlotTable> list, Long entityId) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace() + "deleteAllForwardingSlot", entityId);
            for (TopMcForwardingSlotTable forwardingSlot : list) {
                sqlSession.insert(getNameSpace() + "insertForwardingSlot", forwardingSlot);
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.dao.OltIgmpDao#batchInsertTopMcForwardingPortTables(java.util.List,
     * java.lang.Long)
     */
    @Override
    public void batchInsertTopMcForwardingPortTables(final List<TopMcForwardingPortTable> list, Long entityId) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace() + "deleteAllForwardingPort", entityId);
            for (TopMcForwardingPortTable forwardingPort : list) {
                sqlSession.insert(getNameSpace() + "insertForwardingPort", forwardingPort);
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.dao.OltIgmpDao#batchInsertTopMcForwardingOnuTables(java.util.List,
     * java.lang.Long)
     */
    @Override
    public void batchInsertTopMcForwardingOnuTables(final List<TopMcForwardingOnuTable> list, Long entityId) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace() + "deleteAllForwardingOnu", entityId);
            for (TopMcForwardingOnuTable forwardingOnu : list) {
                sqlSession.insert(getNameSpace() + "insertForwardingOnu", forwardingOnu);
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.dao.OltIgmpDao#getForwardingSlot(java.lang.Long)
     */
    @Override
    public List<TopMcForwardingSlotTable> getForwardingSlot(Long entityId) {
        return getSqlSession().selectList(getNameSpace() + "getForwardingSlot", entityId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.dao.OltIgmpDao#getForwardingPort(java.lang.Long)
     */
    @Override
    public List<TopMcForwardingPortTable> getForwardingPort(Long entityId) {
        return getSqlSession().selectList(getNameSpace() + "getForwardingPort", entityId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.dao.OltIgmpDao#saveIgmpControlledMcPreviewIntervalTable(java.lang.
     * Long, java.util.List)
     */
    @Override
    public void saveIgmpControlledMcPreviewIntervalTable(Long entityId,
            final List<IgmpControlledMcPreviewIntervalTable> igmpControlledMcPreviewIntervalTables) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (IgmpControlledMcPreviewIntervalTable igmpControlledMcPreviewIntervalTable : igmpControlledMcPreviewIntervalTables) {
                sqlSession.update(getNameSpace() + "updateIgmpControlledMulticastPackageTable",
                        igmpControlledMcPreviewIntervalTable);
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.dao.OltIgmpDao#getIgmpControlledMcCdr(java.lang.Long, int, int)
     */
    @Override
    public List<IgmpControlledMcCdrTable> getIgmpControlledMcCdr(Long entityId, int start, int limit) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("start", start);
        map.put("limit", limit);
        return getSqlSession().selectList(getNameSpace() + "getIgmpControlledMcCdrLimit", map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.dao.OltIgmpDao#loadIgmpSnoopingData(java.lang.Long,
     * java.lang.Long, java.lang.Integer)
     */
    @Override
    public List<IgmpForwardingSnooping> loadIgmpSnoopingData(Long entityId, Long portIndex, Integer vid) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("portIndex", portIndex);
        map.put("vid", vid);
        return getSqlSession().selectList(getNameSpace() + "loadIgmpSnoopingData", map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.dao.OltIgmpDao#batchInsertTopIgmpForwardingSnooping(java.util.List,
     * java.lang.Long)
     */
    @Override
    public void batchInsertTopIgmpForwardingSnooping(final List<TopIgmpForwardingSnooping> list, final Long entityId) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace() + "deleteAllSnooping", entityId);
            if (list != null) {
                for (TopIgmpForwardingSnooping topSnooping : list) {
                    for (Long portIndex : topSnooping.getPortList()) {
                        IgmpForwardingSnooping snooping = new IgmpForwardingSnooping();
                        snooping.setEntityId(entityId);
                        snooping.setVid(getVid(entityId, topSnooping.getMvlanId(), topSnooping.getIp()));
                        snooping.setPortIndex(portIndex);
                        sqlSession.insert(getNameSpace() + "batchInsertTopIgmpForwardingSnooping", snooping);
                    }
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

    private Integer getVid(Long entityId, Integer mvlan, String ip) throws SQLException {
        Map<String, String> map = new HashMap<String, String>();
        map.put("mvlan", mvlan.toString());
        map.put("ip", ip);
        map.put("entityId", entityId.toString());
        return getSqlSession().selectOne(getNameSpace() + "getVidByMvlanIp", map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.mybatis.MyBatisDaoSupport#getDomainName()
     */
    @Override
    protected String getDomainName() {
        return "com.topvision.ems.epon.igmp.domain.OltIgmp";
    }

}
