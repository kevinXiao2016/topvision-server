/***********************************************************************
 * $Id: OltDaoImpl.java,v1.0 2013-10-25 上午10:06:26 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.olt.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.domain.CcmtsFftMonitorScalar;
import com.topvision.ems.epon.domain.DeviceListItem;
import com.topvision.ems.epon.domain.DeviceLocation;
import com.topvision.ems.epon.domain.EponPort;
import com.topvision.ems.epon.domain.Fan;
import com.topvision.ems.epon.domain.Olt;
import com.topvision.ems.epon.domain.PortView;
import com.topvision.ems.epon.domain.Power;
import com.topvision.ems.epon.domain.Slot;
import com.topvision.ems.epon.olt.dao.OltDao;
import com.topvision.ems.epon.olt.domain.OltAttribute;
import com.topvision.ems.epon.olt.domain.OltMacAddressLearnTable;
import com.topvision.ems.epon.olt.domain.TopOnuGlobalCfgMgmt;
import com.topvision.ems.epon.olt.domain.TopSysFileDirEntry;
import com.topvision.ems.epon.realtime.domain.OltBaseInfo;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.EntityAttribute;
import com.topvision.ems.network.dao.EntityDao;
import com.topvision.ems.network.domain.EntitySnap;
import com.topvision.ems.network.domain.SubDeviceCount;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.util.CurrentRequest;

/**
 * @author flack
 * @created @2013-10-25-上午10:06:26
 * 
 */
@Repository("oltDao")
public class OltDaoImpl extends MyBatisDaoSupport<Object> implements OltDao {

    @Autowired
    private EntityDao entityDao;

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.epon.olt.domain.Olt";
    }

    @Override
    public OltAttribute getOltAttribute(Long entityId) {
        HashMap<String, Long> params = new HashMap<String, Long>();
        Long userId = null;
        try {
            userId = CurrentRequest.getCurrentUser().getUserId();
        } catch (Exception e) {
            logger.debug("CurrentRequest.getCurrentUser().getUserId() fail");
        }
        params.put("entityId", entityId);
        params.put("userId", userId);
        return getSqlSession().selectOne(getNameSpace("getOltAttribute"), params);
    }

    @Override
    public void updateOltBaseInfo(Long entityId, String oltName, Integer rackNum, Integer frameNum) {
        OltAttribute oltBaseInfo = new OltAttribute();
        oltBaseInfo.setEntityId(entityId);
        oltBaseInfo.setOltName(oltName);
        oltBaseInfo.setTopSysOltFrameNum(frameNum);
        oltBaseInfo.setTopSysOltRackNum(rackNum);
        getSqlSession().update(getNameSpace("modifyOltBaseInfo"), oltBaseInfo);
    }

    @Override
    public List<DeviceLocation> getAllDeviceLocation() {
        return getSqlSession().selectList(getNameSpace() + "getAllDeviceLocation");
    }

    @Override
    public void updateOltAttribute(OltAttribute oltAttribute) {
        getSqlSession().update(getNameSpace() + "updateOltAttribute", oltAttribute);
    }

    @Override
    public void updateOltDeviceUpTime(Long entityId, Long deviceUpTime) {
        HashMap<String, Long> params = new HashMap<String, Long>();
        params.put("entityId", entityId);
        params.put("deviceUpTime", deviceUpTime);
        getSqlSession().update(getNameSpace() + "updateOltDeviceUpTime", params);
    }

    @Override
    public List<OltAttribute> getOltList(Map<String, Object> paramsMap) {
        paramsMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace() + "getOltListByParams", paramsMap);
    }

    @Override
    public int getOltListCount(Map<String, Object> paramsMap) {
        paramsMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectOne(getNameSpace() + "getOltListCountByParams", paramsMap);
    }

    @Override
    public void batchInsertOltMacLearnTable(final List<OltMacAddressLearnTable> oltMacLearnTableList,
            final long entityId) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace() + "deleteAllOltMacAddressLearn", entityId);
            for (OltMacAddressLearnTable table : oltMacLearnTableList) {
                // add by fanzidong,入库前需要格式化MAC地址
                String formattedMac = MacUtils.convertToMaohaoFormat(table.getTopSysMacAddr());
                table.setTopSysMacAddr(formattedMac);
                sqlSession.insert(getNameSpace() + "batchInsertOltMacAddressLearnTable", table);
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
    public List<OltMacAddressLearnTable> getOltMacLearnTableList(Long entityId) {
        return getSqlSession().selectList(getNameSpace() + "getOltMacLearnTableList", entityId);
    }

    @Override
    public void addOltDeviceUpTime(Long entityId, Long sysUpTime, Long collectTime) {
        Map<String, Long> paramMap = new HashMap<String, Long>();
        paramMap.put("entityId", entityId);
        paramMap.put("onuId", 0L);
        paramMap.put("deviceUpTime", collectTime - sysUpTime * 10);
        paramMap.put("collectTime", collectTime);
        getSqlSession().delete(getNameSpace() + "insertOltDeviceUpTime", paramMap);
    }

    @Override
    public void cleanOltDeviceUpTime(Long entityId, Long collectTime) {
        Map<String, Long> paramMap = new HashMap<String, Long>();
        paramMap.put("entityId", entityId);
        paramMap.put("onuId", 0L);
        paramMap.put("collectTime", collectTime);
        getSqlSession().delete(getNameSpace() + "cleanOltDeviceUpTime", paramMap);
    }

    @Override
    public String selectOltSoftVersion(Long entityId) {
        return (String) getSqlSession().selectOne(getNameSpace() + "selectOltSoftVersion", entityId);
    }

    @Override
    public EntitySnap getOltCurrentPerformance(Long entityId) {
        return getSqlSession().selectOne(getNameSpace() + "getEntitySnap", entityId);
    }

    @Override
    public void insertOrUpdateOltAttribute(OltAttribute oltAttribute) {
        Map<String, Long> paramMap = new HashMap<String, Long>();
        paramMap.put("entityId", oltAttribute.getEntityId());
        Long userId = null;
        try {
            userId = CurrentRequest.getCurrentUser().getUserId();
        } catch (Exception e) {
            logger.debug("insertOrUpdateOltAttribute CurrentRequest.getCurrentUser().getUserId() fail");
        }
        paramMap.put("userId", userId);
        OltAttribute attribute = getSqlSession().selectOne(getNameSpace() + "getOltAttribute", paramMap);
        if (attribute != null) {
            getSqlSession().update(getNameSpace() + "updateOltAttribute", oltAttribute);
        } else {
            // Integer.parseInt(getSqlMapClientTemplate().insert("user.insert",user).toString());
            getSqlSession().insert(getNameSpace() + "insertOltAttribute", oltAttribute);
        }
        // add by haojie EMS-9983
        if (oltAttribute != null && oltAttribute.getOutbandMac() != null) {
            entityDao.updateEntityMac(oltAttribute.getEntityId(), oltAttribute.getOutbandMac());
        }
    }

    @Override
    public Olt getOltStructure(Long entityId) {
        List<PortView> portViews = getSqlSession().selectList(getNameSpace() + "getPortView", entityId);
        Olt re = new Olt();
        re.setEntityId(entityId);
        Map<Long, Fan> fanMap = new HashMap<Long, Fan>();
        Map<Long, Power> powerMap = new HashMap<Long, Power>();
        Map<Long, Slot> slotMap = new HashMap<Long, Slot>();
        for (PortView portView : portViews) {
            if (portView.getPortType().equalsIgnoreCase("fan")) {
                Fan fan;
                if (fanMap.containsKey(portView.getSlotId())) {
                    fan = fanMap.get(portView.getSlotId());
                } else {
                    fan = new Fan();
                    fan.setFanCardId(portView.getSlotId());
                    fan.setFanCardIndex(portView.getSlotIndex());
                    fanMap.put(portView.getSlotId(), fan);
                }
                if (fan != null) {
                    if (!re.getFanList().contains(fan)) {
                        re.getFanList().add(fan);
                    }
                }
            } else if (portView.getPortType().equalsIgnoreCase("power")) {
                Power power;
                if (powerMap.containsKey(portView.getSlotId())) {
                    power = powerMap.get(portView.getSlotId());
                } else {
                    power = new Power();
                    power.setPowerCardId(portView.getSlotId());
                    power.setPowerCardIndex(portView.getSlotIndex());
                    powerMap.put(portView.getSlotId(), power);
                }
                if (power != null) {
                    if (!re.getPowerList().contains(power)) {
                        re.getPowerList().add(power);
                    }
                }
            } else {
                Slot slot;
                if (slotMap.containsKey(portView.getSlotId())) {
                    slot = slotMap.get(portView.getSlotId());
                } else {
                    slot = new Slot();
                    slot.setSlotId(portView.getSlotId());
                    slot.setSlotIndex(portView.getSlotIndex());
                    slotMap.put(portView.getSlotId(), slot);
                }
                if (slot != null) {
                    if (portView.getPortType().equalsIgnoreCase("pon")
                            || portView.getPortType().equalsIgnoreCase("sni")) {
                        EponPort port = new EponPort();
                        port.setPortId(portView.getPortId());
                        port.setPortIndex(portView.getPortIndex());
                        port.setPortSubType(portView.getPortType());
                        slot.getPortList().add(port);
                    }
                    if (!re.getSlotList().contains(slot)) {
                        re.getSlotList().add(slot);
                    }
                }
            }
        }
        return re;
    }

    @Override
    public Entity getEntity(Long EntityId) {
        return getSqlSession().selectOne(getNameSpace() + "getEntity", EntityId);
    }

    @Override
    public Map<Long, String> getOltRelation(Long entityId) {
        return selectMapByKeyAndValue(getNameSpace() + "getOltRelation", entityId, "OLTINDEX", "oltRelationTable");
    }

    @Override
    public Map<Long, Long> getOltMap(Long entityId) {
        HashMap<Long, Long> oltMap = new HashMap<Long, Long>();
        Map<Object, Object> map = selectMapByKeyAndValue(getNameSpace() + "getOltMap", entityId, "OLTINDEX", "OLTID");
        for (Map.Entry<Object, Object> entry : map.entrySet()) {
            oltMap.put(Long.parseLong(entry.getKey().toString()), Long.parseLong(entry.getValue().toString()));
        }
        return oltMap;
    }

    /*
     * @Override public void deleteRelation(Long index, String relationTable) {
     * getSqlSession().delete(getNameSpace() + "deleteOlt" + relationTable, index); }
     */

    @Override
    public List<Long> getEntityByFolder(Long folderId, Long type) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("folderId", folderId);
        map.put("type", type);
        return getSqlSession().selectList(getNameSpace() + "getEntityByFolder", map);
    }

    @Override
    public List<Long> getIsManagedEntityList(Long type) {
        return getSqlSession().selectList(getNameSpace() + "getIsManagedOltList", type);
    }

    @Override
    public String getEntityDolStatus(Long entityId) {
        return (String) getSqlSession().selectOne(getNameSpace() + "getEntityDolStatus", entityId);
    }

    @Override
    public void updateEntityDolStatus(EntityAttribute entityAttribute) {
        EntityAttribute attribute = (EntityAttribute) getSqlSession().selectOne(getNameSpace() + "getEntityAttribute",
                entityAttribute.getEntityId());
        if (attribute != null) {
            getSqlSession().update(getNameSpace() + "updateEntityAttribute", entityAttribute);
        } else {
            getSqlSession().insert(getNameSpace() + "insertEntityAttribute", entityAttribute);
        }
    }

    @Override
    public List<DeviceListItem> getDeviceListItem(Map<String, Object> map) {
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        if (map.get("sortName").equals("ip")) {
            return getSqlSession().selectList(getNameSpace() + "getDeviceListItemSortIp", map);
        } else {
            return getSqlSession().selectList(getNameSpace() + "getDeviceListItem", map);
        }
    }

    @Override
    public void insertCcmtsFftMonitorScalar(CcmtsFftMonitorScalar ccmtsFftMonitorScalar) {
        getSqlSession().delete(getNameSpace() + "deleteFftMonitorGlbStatus", ccmtsFftMonitorScalar.getEntityId());
        getSqlSession().insert(getNameSpace() + "insertFftMonitorGlbStatus", ccmtsFftMonitorScalar);
    }

    @Override
    public CcmtsFftMonitorScalar queryCcmtsFftGbStatus(Long entityId) {
        return getSqlSession().selectOne(getNameSpace() + "getCcmtsFftGbStatus", entityId);
    }

    @Override
    public void updateCcmtsFftGbStatus(CcmtsFftMonitorScalar ccmtsFftMonitorScalar) {
        getSqlSession().update("updateCcmtsFftGbStatus", ccmtsFftMonitorScalar);
    }

    /*
     * @Override public List<MonitorType> getMonitorTypes() { return null; }
     */

    @Override
    public void updateOltFileDir(List<TopSysFileDirEntry> fileDirList, Long entityId) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace() + "deleteFileDirData", entityId);
            for (TopSysFileDirEntry fileDir : fileDirList) {
                fileDir.setEntityId(entityId);
                sqlSession.insert(getNameSpace() + "insertFileDirData", fileDir);
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
    public TopSysFileDirEntry getOltFileDirEntry(Long entityId, Integer fileType) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("fileDirType", fileType);
        return getSqlSession().selectOne("getOltFileDirAttr", paramMap);
    }

    @Override
    public SubDeviceCount querySubCountInfo(Long entityId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return this.getSqlSession().selectOne(getNameSpace("querySubCountInfo"), map);
    }

    @Override
    public void updateOltBaseInfo(OltBaseInfo baseInfo) {
        this.getSqlSession().update(getNameSpace("updateOltBaseInfo"), baseInfo);
    }

    @Override
    public void updateOltSoftVersion(OltBaseInfo baseInfo) {
        this.getSqlSession().update(getNameSpace("updateOltVersion"), baseInfo);
    }

    @Override
    public void batchInsertTopOnuGlobalCfgMgmt(List<TopOnuGlobalCfgMgmt> onuGlobalCfgMgmts, Long entityId) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace() + "deleteTopOnuGlobalCfgMgmt", entityId);
            for (TopOnuGlobalCfgMgmt config : onuGlobalCfgMgmts) {
                sqlSession.insert(getNameSpace("insertTopOnuGlobalCfgMgmt"), config);
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
    public List<TopOnuGlobalCfgMgmt> getTopOnuGlobalCfgMgmt(Long entityId) {
        return getSqlSession().selectList(getNameSpace("getTopOnuGlobalCfgMgmt"), entityId);
    }

    @Override
    public void updateOltClearTime(Integer oltClearTime, Long entityId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("time", oltClearTime);
        getSqlSession().insert(getNameSpace("insertOrupdateOltCmClearTime"), map);
    }

    @Override
    public List<Long> queryEntityIdOfOlt(){
        return getSqlSession().selectList(getNameSpace("queryEntityIdOfOlt"));
    }
}
