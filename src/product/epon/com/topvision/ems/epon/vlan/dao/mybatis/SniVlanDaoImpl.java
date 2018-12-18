/***********************************************************************
 * $Id: SniVlanDaoImpl.java,v1.0 2013-10-25 上午11:45:47 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.vlan.dao.mybatis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.olt.dao.OltPonDao;
import com.topvision.ems.epon.olt.domain.OltSniAttribute;
import com.topvision.ems.epon.vlan.dao.SniVlanDao;
import com.topvision.ems.epon.vlan.domain.OltPortVlanRelation;
import com.topvision.ems.epon.vlan.domain.OltVlanAttribute;
import com.topvision.ems.epon.vlan.domain.PortVlanAttribute;
import com.topvision.ems.epon.vlan.domain.TopOltVlanConfigTable;
import com.topvision.ems.epon.vlan.domain.TopOltVlanVifPriIpTable;
import com.topvision.ems.epon.vlan.domain.TopOltVlanVifSubIpTable;
import com.topvision.ems.epon.vlan.domain.VlanAttribute;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author jay
 * @created @2011-10-25-11:36:51
 * 
 * @Mybatis Modify by Rod @2013-10-19
 */
@Repository("sniVlanDao")
public class SniVlanDaoImpl extends MyBatisDaoSupport<Object> implements SniVlanDao {

    @Autowired
    private OltPonDao oltPonDao;

    @Override
    public void updateOltVlanGlobalInfo(OltVlanAttribute oltVlanAttribute) {
        if ((Integer) getSqlSession().selectOne(getNameSpace() + "selectEntityVlanCount",
                oltVlanAttribute.getEntityId()) == 0) {
            getSqlSession().insert(getNameSpace() + "insertOltVlanGlobalInfo", oltVlanAttribute);
        } else {
            getSqlSession().update(getNameSpace() + "updateOltVlanGlobalInfo", oltVlanAttribute);
        }

    }

    @Override
    public void batchInsertOltVlanConfig(final List<VlanAttribute> list, Long entityId) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace() + "deleteAllOltVlanConfig", entityId);
            sqlSession.delete(getNameSpace() + "deleteAllOltPortVlanRelation", entityId);
            for (VlanAttribute vlanAttribute : list) {
                sqlSession.insert(getNameSpace() + "insertOltVlanConfig", vlanAttribute);
                syncOltPortVlanRelation(sqlSession, vlanAttribute);
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
     * 获取SNI VLAN基本属性
     * 
     * @param entityId
     *            设备id
     * @param sniIndex
     *            sni口Index
     * @return PortVlanAttribute
     */
    @Override
    public PortVlanAttribute getSniPortVlanAttribute(Long entityId, Long sniIndex) {
        Map<String, Long> param = new HashMap<String, Long>();
        param.put("entityId", entityId);
        param.put("sniIndex", sniIndex);
        return getSqlSession().selectOne(getNameSpace() + "getSniPortAttributeByMap", param);
    }

    @Override
    public void updateSniPortAttribute(PortVlanAttribute portVlanAttribute) {
        getSqlSession().update(getNameSpace() + "updateSniPortAttribute", portVlanAttribute);
    }

    @Override
    public void modifyVlanName(Long entityId, Integer vlanIndex, String oltVlanName, Integer topMcFloodMode) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("vlanIndex", vlanIndex);
        map.put("oltVlanName", oltVlanName);
        map.put("topMcFloodMode", topMcFloodMode);
        getSqlSession().update(getNameSpace() + "modifyVlanName", map);
    }

    @Override
    public OltVlanAttribute getOltVlanGlobalInfo(Long entityId) {
        return getSqlSession().selectOne(getNameSpace() + "getOltVlanGlobalInfo", entityId);
    }

    @Override
    public List<VlanAttribute> getOltVlanConfigList(Long entityId) {
        return getSqlSession().selectList(getNameSpace() + "getOltVlanConfigList", entityId);
    }

    @Override
    public void addOltVlan(VlanAttribute vlanAttribute) {
        getSqlSession().insert(getNameSpace() + "addOltVlan", vlanAttribute);
    }

    @Override
    public void deleteOltVlan(Long entityId, Integer vlanIndex) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("vlanIndex", vlanIndex);
        getSqlSession().delete(getNameSpace() + "deleteOltVlan", map);
        getSqlSession().delete(getNameSpace("deleteOltPortVlanRelation"), map);
    }

    @Override
    public void updateTagStatus(VlanAttribute vlanAttribute) {
        SqlSession session = getBatchSession();
        session.update(getNameSpace() + "updateTagStatus", vlanAttribute);
        session.delete(getNameSpace("deletePortVlanRelation"), vlanAttribute);
        syncOltPortVlanRelation(session, vlanAttribute);
        session.commit();
    }

    /**
     * 同步更新VLAN-PORT关系表
     * @param session
     * @param vlanAttribute
     */
    private void syncOltPortVlanRelation(SqlSession session, VlanAttribute vlanAttribute) {
        List<Long> taggedPortIndexList = vlanAttribute.getTaggedPortIndexList();
        for (Long index : taggedPortIndexList) {
            OltPortVlanRelation relation = new OltPortVlanRelation(vlanAttribute.getEntityId(), index,
                    vlanAttribute.getVlanIndex(), OltPortVlanRelation.TAG_PORT);
            session.insert(getNameSpace() + "insertOltPortVlanRelation", relation);
        }
        List<Long> untaggedPortIndexList = vlanAttribute.getUntaggedPortIndexList();
        for (Long index : untaggedPortIndexList) {
            OltPortVlanRelation relation = new OltPortVlanRelation(vlanAttribute.getEntityId(), index,
                    vlanAttribute.getVlanIndex(), OltPortVlanRelation.UNTAG_PORT);
            session.insert(getNameSpace() + "insertOltPortVlanRelation", relation);
        }
    }

    @Override
    public VlanAttribute getOltVlanConfig(Long entityId, Integer vlanIndex) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("vlanIndex", vlanIndex);
        return getSqlSession().selectOne(getNameSpace() + "getOltVlanConfig", map);
    }

    @Override
    public void batchInsertOltPortVlan(final List<PortVlanAttribute> list, Long entityId) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace() + "deleteAllOltPortVlan", entityId);
            for (PortVlanAttribute portVlanAttribute : list) {
                OltSniAttribute sniAttribute = new OltSniAttribute();
                sniAttribute.setEntityId(entityId);
                sniAttribute.setSniIndex(portVlanAttribute.getPortIndex());
                // 脏数据处理
                Long sniId = (Long) sqlSession.selectOne(getNameSpace() + "getSniId", sniAttribute);
                Long ponId = null;
                Long portId = null;
                if (sniId == null) {
                    ponId = oltPonDao.getPonIdByPonIndex(portVlanAttribute.getEntityId(),
                            portVlanAttribute.getPortIndex());
                    if (ponId == null) {
                        continue;
                    } else {
                        portId = ponId;
                    }
                } else {
                    portId = sniId;
                }
                portVlanAttribute.setPortId(portId);
                sqlSession.insert(getNameSpace() + "insertOltPortVlan", portVlanAttribute);
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
        // 对sni端口vlan刷新完成后，将带内管理端口存入数据库
        //@lizongtian 2016-09-20 10:28:13这个更新去掉
        //updateInBandPortList(list, inBandVlanId, entityId);
    }

    public void updateInBandPortList(final List<PortVlanAttribute> list, Integer inBandVlanId, Long entityId) {
        List<Long> inBandPorts = new ArrayList<Long>();
        for (PortVlanAttribute portVlanAttribute : list) {
            if (portVlanAttribute.getVlanPVid().equals(inBandVlanId)) {
                inBandPorts.add(portVlanAttribute.getPortIndex());
            }
        }
        String ports = null;
        if (inBandPorts.size() > 0) {
            StringBuilder bu = new StringBuilder();
            for (Long l : inBandPorts) {
                bu.append(",").append(l.toString());
            }
            ports = bu.toString().substring(1);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("entityId", entityId);
            map.put("ports", ports);
            getSqlSession().update("com.topvision.ems.epon.olt.domain.Olt.updateInBandPorts", map);
        }
    }

    @Override
    public void setVlanPriIp(TopOltVlanVifPriIpTable vlanVifPriIp) {
        getSqlSession().insert(getNameSpace() + "setVlanPriIp", vlanVifPriIp);
    }

    @Override
    public void modifyVlanVifPriIp(TopOltVlanVifPriIpTable vlanVifPriIp) {
        getSqlSession().update(getNameSpace() + "modifyVlanVifPriIp", vlanVifPriIp);
    }

    @Override
    public void deleteVlanVif(TopOltVlanVifPriIpTable vlanVifPriIp) {
        getSqlSession().delete(getNameSpace() + "deleteVlanVif", vlanVifPriIp);
    }

    @Override
    public void addVlanVifSubIp(TopOltVlanVifSubIpTable newObject) {
        getSqlSession().insert(getNameSpace() + "addVlanVifSubIp", newObject);
    }

    @Override
    public void modifyVlanVifSubIp(TopOltVlanVifSubIpTable vlanVifSubIp) {
        getSqlSession().update(getNameSpace() + "modifyVlanVifSubIp", vlanVifSubIp);
    }

    @Override
    public void deleteVlanVifSubIp(TopOltVlanVifSubIpTable vlanVifSubIp) {
        getSqlSession().delete(getNameSpace() + "deleteVlanVifSubIp", vlanVifSubIp);
    }

    @Override
    public TopOltVlanVifPriIpTable getVlanVifPriIp(Long entityId, Integer vlanIndex) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("vlanIndex", vlanIndex);
        return getSqlSession().selectOne(getNameSpace() + "getVlanVifPriIp", map);
    }

    @Override
    public List<TopOltVlanVifPriIpTable> getVlanVifPriIpList(Long entityId) {
        return getSqlSession().selectList(getNameSpace() + "getVlanVifPriIpList", entityId);
    }

    @Override
    public List<TopOltVlanVifSubIpTable> getVlanVifSubIp(Long entityId, Integer vlanIndex) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("vlanIndex", vlanIndex);
        return getSqlSession().selectList(getNameSpace() + "getVlanVifSubIp", map);
    }

    @Override
    public void deleteVlanVifSubIps(Long entityId, Integer vlanIndex) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("vlanIndex", vlanIndex);
        getSqlSession().delete(getNameSpace() + "deleteVlanVifSubIps", map);
    }

    @Override
    public void batchInsertVlanVifPriIp(final List<TopOltVlanVifPriIpTable> list, Long entityId) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace() + "deleteAllVlanVifPriIp", entityId);
            for (TopOltVlanVifPriIpTable vlanVifPriIp : list) {
                sqlSession.insert(getNameSpace() + "insertVlanVifPriIp", vlanVifPriIp);
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
    public void batchInsertVlanVifSubIp(final List<TopOltVlanVifSubIpTable> list, Long entityId) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace() + "deleteAllVlanVifSubIp", entityId);
            for (TopOltVlanVifSubIpTable vlanVifSubIp : list) {
                sqlSession.insert(getNameSpace() + "insertVlanVifSubIp", vlanVifSubIp);
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
    public void batchInsertTopMcFloodMode(final List<TopOltVlanConfigTable> list, Long entityId) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (TopOltVlanConfigTable topMcFloodMode : list) {
                sqlSession.update(getNameSpace() + "insertTopMcFloodMode", topMcFloodMode);
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
    protected String getDomainName() {
        return "com.topvision.ems.epon.vlan.domain.SniVlan";
    }

    @Override
    public List<Integer> queryUsedSubIpIndex(Long entityId, Integer vlanIndex) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("vlanIndex", vlanIndex);
        return this.getSqlSession().selectList(getNameSpace("getUsedSubIpIndex"), map);
    }

}
