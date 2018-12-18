/***********************************************************************
 * $Id: OnuDaoImpl.java,v1.0 2013-10-25 上午11:07:48 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.onu.dao.ElectricityOnuDao;
import com.topvision.ems.epon.onu.domain.OltOnuComAttribute;
import com.topvision.ems.epon.onu.domain.OltOnuComVlanConfig;
import com.topvision.ems.epon.onu.domain.OltOnuMacMgmt;
import com.topvision.ems.epon.onu.domain.OltTopOnuCapability;
import com.topvision.framework.utils.EponIndex;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author flack
 * @created @2013-10-25-上午11:07:48
 *
 */
@Repository("electricityOnuDao")
public class ElectricityOnuDaoImpl extends MyBatisDaoSupport<Object> implements ElectricityOnuDao {

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.epon.onu.domain.ElectricityOnu";
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.dao.OltElecOnuDao#updateOnuComVlan(java.lang.Long, int)
     */
    @Override
    public void updateOnuComVlan(Long entityId, int vlan) {
        OltOnuComVlanConfig comVlanConfig = new OltOnuComVlanConfig();
        comVlanConfig.setEntityId(entityId);
        comVlanConfig.setOnuComVlan(vlan);
        getSqlSession().update(getNameSpace("updateOnuComVlan"), comVlanConfig);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.dao.OltElecOnuDao#batchInsertOnuComVlan(java.lang.Long, int)
     */
    @Override
    public void batchInsertOnuComVlan(Long entityId, int vlan) {
        if (getComVlanConfig(entityId) != null) {
            updateOnuComVlan(entityId, vlan);
        } else {
            OltOnuComVlanConfig comVlanConfig = new OltOnuComVlanConfig();
            comVlanConfig.setEntityId(entityId);
            comVlanConfig.setOnuComVlan(vlan);
            getSqlSession().update(getNameSpace("insertOnuComVlan"), comVlanConfig);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.dao.OltElecOnuDao#updateOnuIpMaskInfo(java.lang.Long,
     * java.lang.Long, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void updateOnuIpMaskInfo(Long entityId, Long onuIndex, String onuIp, String onuMask, String onuGateway) {
        OltTopOnuCapability capability = new OltTopOnuCapability();
        capability.setEntityId(entityId);
        capability.setOnuIndex(onuIndex);
        capability.setTopOnuMgmtIp(onuIp);
        capability.setTopOnuNetMask(onuMask);
        capability.setTopOnuGateway(onuGateway);
        getSqlSession().update(getNameSpace("updateOnuIpMaskInfo"), capability);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.dao.OltElecOnuDao#updateOnuComAttribute(java.lang.Long,
     * com.topvision.ems.epon.facade.domain.OltOnuComAttribute)
     */
    @Override
    public void updateOnuComAttribute(Long entityId, OltOnuComAttribute attribute) {
        attribute.setEntityId(entityId);
        getSqlSession().update(getNameSpace("updateOnuComAttribute"), attribute);
    }

    private HashMap<Long, Long> getOnuComMap(Long entityId) {
        HashMap<Long, Long> oltMap = new HashMap<Long, Long>();
        /*Map<Object, Object> map = getSqlMapClientTemplate().queryForMap(getNameSpace() + "getOnuComMap", entityId,
                "OLTINDEX", "OLTID");
        for (Map.Entry<Object, Object> entry : map.entrySet()) {
            oltMap.put(Long.parseLong(entry.getKey().toString()), Long.parseLong(entry.getValue().toString()));
        }*/
        return oltMap;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.dao.OltElecOnuDao#batchInsertOnuComAttribute(java.lang.Long,
     * com.topvision.ems.epon.facade.domain.OltOnuComAttribute)
     */
    @Override
    public void batchInsertOnuComAttribute(Long entityId, final List<OltOnuComAttribute> attributes) {
        // 此处的集合只包括ONU与ONUCOM的信息 参考设备结构数据采集方式
        final HashMap<Long, Long> oltMap = getOnuComMap(entityId);
        for (OltOnuComAttribute onuComAttribute : attributes) {
            if (oltMap.containsKey(onuComAttribute.getOnuComIndex())) {
                getSqlSession().update(getNameSpace("updateOnuComAttributeAll"), onuComAttribute);
            } else {
                onuComAttribute.setOnuId(oltMap.get(EponIndex.getOnuIndex(onuComAttribute.getOnuComIndex())));
                getSqlSession().insert(getNameSpace("insertOnuComRelation"), onuComAttribute);
                onuComAttribute.setOnuComId(onuComAttribute.getOnuComId());
                getSqlSession().insert(getNameSpace("insertEntity"), onuComAttribute);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.dao.OltElecOnuDao#getComVlanConfig(java.lang.Long)
     */
    @Override
    public OltOnuComVlanConfig getComVlanConfig(Long entityId) {
        return getSqlSession().selectOne(getNameSpace("getComVlanConfig"), entityId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.dao.OltElecOnuDao#getComVlamAttribute(java.lang.Long,
     * java.lang.Long)
     */
    @Override
    public OltOnuComAttribute getComVlamAttribute(Long entityId, Long onuComIndex) {
        Map<String, Long> map = new HashMap<String, Long>();
        map.put("entityId", entityId);
        map.put("onuComIndex", onuComIndex);
        return getSqlSession().selectOne(getNameSpace("getComAttribute"), map);
    }

    @Override
    public List<Long> loadPonCutOverPort(Long entityId, Long ponCutOverPortIndex) {
        Map<String, Long> map = new HashMap<String, Long>();
        map.put("entityId", entityId);
        map.put("ponIndex", ponCutOverPortIndex);
        return getSqlSession().selectList(getNameSpace("loadPonCutOverPort"), map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.dao.OltElecOnuDao#getOnuMacMgmt(java.lang.Long,
     * java.lang.Long)
     */
    @Override
    public OltOnuMacMgmt getOnuMacMgmt(Long entityId, Long onuIndex) {
        Map<String, Long> map = new HashMap<String, Long>();
        map.put("entityId", entityId);
        map.put("onuIndex", onuIndex);
        return getSqlSession().selectOne(getNameSpace("getOnuMacMgmt"), map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.dao.OltElecOnuDao#updateOnuMacMgmt(com.topvision
     * .ems.epon.facade.domain.OltOnuMacMgmt)
     */
    @Override
    public void updateOnuMacMgmt(OltOnuMacMgmt oltOnuMacMgmt) {
        getSqlSession().update(getNameSpace("updateOnuMacMgmt"), oltOnuMacMgmt);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.dao.OltElecOnuDao#insertOnuMacMgmt
     * (com.topvision.ems.epon.facade.domain.OltOnuMacMgmt)
     */
    @Override
    public void insertOnuMacMgmt(OltOnuMacMgmt oltOnuMacMgmt) {
        getSqlSession().insert(getNameSpace("insertOnuMacMgmt"), oltOnuMacMgmt);
    }

    @Override
    public OltTopOnuCapability getElecOnuCapability(Long entityId, Long onuIndex) {
        Map<String, Long> map = new HashMap<String, Long>();
        map.put("entityId", entityId);
        map.put("onuIndex", onuIndex);
        return getSqlSession().selectOne(getNameSpace("queryElecOnuCapability"), map);
    }
}
