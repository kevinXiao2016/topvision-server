/***********************************************************************
 * $Id: OltPonDaoImpl.java,v1.0 2013-10-25 上午10:16:09 $
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
import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.olt.dao.OltSniDao;
import com.topvision.ems.epon.olt.domain.OltSniAttribute;
import com.topvision.ems.epon.olt.domain.OltSniMacAddress;
import com.topvision.ems.epon.olt.domain.OltSniRedirect;
import com.topvision.ems.epon.olt.domain.OltSniStormSuppressionEntry;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.framework.utils.EponConstants;
import com.topvision.framework.utils.EponIndex;
import com.topvision.platform.util.CurrentRequest;

/**
 * @author flack
 * @created @2013-10-25-上午10:16:09
 *
 */
@Repository("oltSniDao")
public class OltSniDaoImpl extends MyBatisDaoSupport<Object> implements OltSniDao {

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.epon.olt.domain.OltSni";
    }

    @Override
    public void updateSniOperationStatus(Long sniId, Integer operationStatus) {
        Map<Object, Object> param = new HashMap<Object, Object>();
        param.put("sniId", sniId);
        param.put("sniOperationStatus", operationStatus);
        getSqlSession().update(getNameSpace() + "updateSniOperationStatus", param);
    }

    @Override
    public Long getSniIdByIndex(Long sniIndex, Long entityId) {
        Map<Object, Object> param = new HashMap<Object, Object>();
        param.put("sniIndex", sniIndex);
        param.put("entityId", entityId);
        return (Long) getSqlSession().selectOne(getNameSpace() + "getSniIdByIndex", param);
    }

    @Override
    public Long getSniIndex(Long sniId) {
        return getSqlSession().selectOne(getNameSpace() + "getSniIndex", sniId);
    }

    @Override
    public OltSniAttribute getSniAttribute(Long sniId) {
        return getSqlSession().selectOne(getNameSpace() + "getSniAttribute", sniId);
    }

    @Override
    public OltSniAttribute getSniAttribute(Long entityId, Long sniIndex) {
        Map<String, Long> param = new HashMap<String, Long>();
        param.put("entityId", entityId);
        param.put("sniIndex", sniIndex);
        return getSqlSession().selectOne(getNameSpace() + "getSniAttributeByIndex", param);
    }

    @Override
    public List<OltSniAttribute> getSniAttrList(Long entityId, List<Long> sniIndexList) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("entityId", entityId);
        param.put("indexList", sniIndexList);
        return getSqlSession().selectList(getNameSpace("getSniAttrList"), param);
    }

    @Override
    public void updateSniPortName(Long sniId, String name) {
        OltSniAttribute sniAttribute = new OltSniAttribute();
        sniAttribute.setSniId(sniId);
        sniAttribute.setSniPortName(name);
        getSqlSession().update(getNameSpace() + "updateSniPortName", sniAttribute);
    }

    @Override
    public void updateSniAdminStatus(Long sniId, Integer adminStatus) {
        OltSniAttribute sniAttribute = new OltSniAttribute();
        sniAttribute.setSniId(sniId);
        sniAttribute.setSniAdminStatus(adminStatus);
        getSqlSession().update(getNameSpace() + "updateSniAdminStatus", sniAttribute);
    }

    @Override
    public void updateSniIsolationStatus(Long sniId, Integer isolationStatus) {
        OltSniAttribute sniAttribute = new OltSniAttribute();
        sniAttribute.setSniId(sniId);
        sniAttribute.setSniIsolationEnable(isolationStatus);
        getSqlSession().update(getNameSpace() + "updateSniIsolationStatus", sniAttribute);
    }

    @Override
    public void updateSniFlowControl(OltSniAttribute sniAttribute) {
        getSqlSession().update(getNameSpace() + "updateSniFlowControl", sniAttribute);
    }

    @Override
    public void updateSniAutoNegotiationMode(Long sniId, Integer sniAutoNegotiationMode) {
        OltSniAttribute sniAttribute = new OltSniAttribute();
        sniAttribute.setSniId(sniId);
        sniAttribute.setSniAutoNegotiationMode(sniAutoNegotiationMode);
        getSqlSession().update(getNameSpace() + "updateSniAutoNegotiationMode", sniAttribute);
    }

    @Override
    public List<OltSniMacAddress> getSniMacAddress(Long sniId) {
        return getSqlSession().selectList(getNameSpace() + "getSniMacAddress", sniId);
    }

    @Override
    public void insertSniMacAddress(Long entityId, OltSniMacAddress sniMacAddress) {
        sniMacAddress.setEntityId(entityId);
        // add by fanzidong,需要在入库前格式化MAC地址
        String formattedMac = MacUtils.convertToMaohaoFormat(sniMacAddress.getSniMacAddrIndex());
        sniMacAddress.setSniMacAddrIndex(formattedMac);
        getSqlSession().insert(getNameSpace() + "insertOltSniMacAddress", sniMacAddress);
    }

    @Override
    public void deleteSniMacAddress(Long entityId, OltSniMacAddress sniMacAddress) {
        sniMacAddress.setEntityId(entityId);
        // add by fanzidong,需要在查询前格式化MAC地址
        String formattedMac = MacUtils.convertToMaohaoFormat(sniMacAddress.getSniMacAddrIndex());
        sniMacAddress.setSniMacAddrIndex(formattedMac);
        getSqlSession().delete(getNameSpace() + "deleteOltSniMacAddress", sniMacAddress);
    }

    @Override
    public void updateSniMacAddressAgingTime(Long entityId, Integer sniAddressAgingTime, Integer topSysArpAgingTime) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("sniAddressAgingTime", sniAddressAgingTime);
        paramMap.put("topSysArpAgingTime", topSysArpAgingTime);
        getSqlSession().update(getNameSpace() + "updateSniMacAddressAgingTime", paramMap);
    }

    @Override
    public void updateSniMacAddrLearnMaxNum(Long sniId, Long sniMacAddrLeranMaxNum) {
        Map<String, Long> paramMap = new HashMap<String, Long>();
        paramMap.put("sniId", sniId);
        paramMap.put("sniMacAddrLeranMaxNum", sniMacAddrLeranMaxNum);
        getSqlSession().update(getNameSpace() + "updateSniMacAddrLearnMaxNum", paramMap);
    }

    @Override
    public OltSniStormSuppressionEntry getSniStormSuppression(Long sniId) {
        return getSqlSession().selectOne(getNameSpace() + "getSniStormSuppression", sniId);
    }

    @Override
    public void updateSniStormSuppression(OltSniStormSuppressionEntry oltSniStormSuppressionEntry) {
        getSqlSession().update(getNameSpace() + "updateSniStormSuppression", oltSniStormSuppressionEntry);
    }

    @Override
    public void insertSniStormSuppression(OltSniStormSuppressionEntry oltSniStormSuppressionEntry) {
        getSqlSession().insert(getNameSpace() + "insertOltSniStormInfo", oltSniStormSuppressionEntry);
    }

    @Override
    public void updateSni15MinPerfStatus(Long sniId, Integer sniPerfStats15minuteEnable) {
        OltSniAttribute sniAttribute = new OltSniAttribute();
        sniAttribute.setSniId(sniId);
        sniAttribute.setSniPerfStats15minuteEnable(sniPerfStats15minuteEnable);
        getSqlSession().update(getNameSpace() + "updateSni15MinPerfStatus", sniAttribute);
    }

    @Override
    public List<OltSniRedirect> getSniRedirect(Long entityId) {
        return getSqlSession().selectList(getNameSpace() + "getSniRedirect", entityId);
    }

    @Override
    public List<OltSniRedirect> getAvailableSniRedirect(Long entityId) {
        return getSqlSession().selectList(getNameSpace() + "getAvailableSniRedirect", entityId);
    }

    @Override
    public List<OltSniRedirect> queryAllSniRedirect(Long entityId) {
        return getSqlSession().selectList(getNameSpace() + "getAllSniRedirect", entityId);
    }

    @Override
    public void addSniRedirect(OltSniRedirect oltSniRedirect) {
        getSqlSession().insert(getNameSpace() + "insertOltSniRedirect", oltSniRedirect);
    }

    @Override
    public void deleteSniRedirect(Long sniId) {
        getSqlSession().delete(getNameSpace() + "deleteSniRedirect", sniId);
    }

    @Override
    public void updateSni24HourPerfStatus(Long sniId, Integer sniPerfStats24hourEnable) {
        OltSniAttribute sniAttribute = new OltSniAttribute();
        sniAttribute.setSniId(sniId);
        sniAttribute.setSniPerfStats24hourEnable(sniPerfStats24hourEnable);
        getSqlSession().update(getNameSpace() + "updateSni24HourPerfStatus", sniAttribute);
    }

    @Override
    public void batchInsertOltSniStormSuppressionEntry(
            final List<OltSniStormSuppressionEntry> sniStormSuppressionEntries, Long entityId) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (OltSniStormSuppressionEntry stormSuppressionEntry : sniStormSuppressionEntries) {
                // 通过构造一个sni属性对象，获得sni对应的sniId
                OltSniAttribute oltSniAttribute = new OltSniAttribute();
                oltSniAttribute.setEntityId(stormSuppressionEntry.getEntityId());
                oltSniAttribute.setSniIndex(stormSuppressionEntry.getSniIndex());
                Long sniId = (Long) sqlSession.selectOne(getNameSpace() + "getSniId", oltSniAttribute);
                if (sniId != null) {
                    stormSuppressionEntry.setSniId(sniId);
                    sqlSession.delete(getNameSpace() + "deleteSniStormInfo", stormSuppressionEntry.getSniId());
                    sqlSession.insert(getNameSpace() + "insertOltSniStormInfo", stormSuppressionEntry);
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
    public void batchInsertOltSniRedirect(final List<OltSniRedirect> sniRedirects, Long entityId) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace() + "deleteAllOltSniRedirect", entityId);
            for (OltSniRedirect sniRedirect : sniRedirects) {
                // 通过构造一个sni属性对象，获得sni对应的sniId
                OltSniAttribute oltSniAttribute = new OltSniAttribute();
                oltSniAttribute.setEntityId(sniRedirect.getEntityId());
                oltSniAttribute.setSniIndex(sniRedirect.getSrcIndex());
                sniRedirect.setTopSniRedirectGroupSrcPortId((Long) sqlSession.selectOne(getNameSpace() + "getSniId",
                        oltSniAttribute));
                oltSniAttribute.setSniIndex(sniRedirect.getDstIndex());
                sniRedirect.setTopSniRedirectGroupDstPortId((Long) sqlSession.selectOne(getNameSpace() + "getSniId",
                        oltSniAttribute));
                sqlSession.insert(getNameSpace() + "insertOltSniRedirect", sniRedirect);
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
    public void batchInsertOltSniMacAddress(final List<OltSniMacAddress> macAddresses, Long entityId) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace() + "deleteAllOltSniMacAddress", entityId);
            if (macAddresses != null && macAddresses.size() > 0) {
                OltSniAttribute sniAttribute = new OltSniAttribute();
                for (OltSniMacAddress sniMacAddress : macAddresses) {
                    sniAttribute.setEntityId(sniMacAddress.getEntityId());
                    sniAttribute.setSniIndex(sniMacAddress.getSniIndex());
                    sniMacAddress.setSniId((Long) sqlSession.selectOne(getNameSpace() + "getSniId", sniAttribute));
                    // add by fanzidong,需要在入库前格式化MAC地址
                    String formattedMac = MacUtils.convertToMaohaoFormat(sniMacAddress.getSniMacAddrIndex());
                    sniMacAddress.setSniMacAddrIndex(formattedMac);
                    sqlSession.insert(getNameSpace() + "insertOltSniMacAddress", sniMacAddress);
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
    public void updateOltSniAttribute(OltSniAttribute sniAttribute) {
        getSqlSession().update(getNameSpace() + "updateOltSniAttribute", sniAttribute);
    }

    @Override
    public void batchInsertSniAttribute(Long entityId, final List<OltSniAttribute> list,
            final HashMap<Long, Long> oltMap) {
        Map<Long, Integer> slotMap = getSlotIdAndTypeMap(entityId);
        for (OltSniAttribute oltSniAttribute : list) {
            if (oltMap.containsKey(oltSniAttribute.getSniIndex())) {
                Long slotId = oltMap.get(EponIndex.getSlotIndex(oltSniAttribute.getSniIndex()));
                String displayName = handleSniDisplayName(slotMap.get(slotId), oltSniAttribute.getSniIndex(),
                        oltSniAttribute.getTopSniAttrPortType());
                oltSniAttribute.setSniDisplayName(displayName);
                getSqlSession().update(getNameSpace() + "updateOltSniAttribute", oltSniAttribute);
            } else {
                if (oltMap.containsKey(EponIndex.getSlotIndex(oltSniAttribute.getSniIndex()))) {
                    Long slotId = oltMap.get(EponIndex.getSlotIndex(oltSniAttribute.getSniIndex()));
                    oltSniAttribute.setSlotId(slotId);
                    getSqlSession().insert(getNameSpace() + "insertOltSniRelation", oltSniAttribute);
                    oltSniAttribute.setSniId(oltSniAttribute.getSniId());
                    String displayName = handleSniDisplayName(slotMap.get(slotId), oltSniAttribute.getSniIndex(),
                            oltSniAttribute.getTopSniAttrPortType());
                    oltSniAttribute.setSniDisplayName(displayName);
                    getSqlSession().insert(getNameSpace() + "insertOltSniAttribute", oltSniAttribute);
                    oltMap.put(oltSniAttribute.getSniIndex(), oltSniAttribute.getSniId());
                }
            }
        }
    }

    /**
     * 获取SlotId与SlotType的对应关系
     * 
     * @param entityId
     * @return
     */
    private Map<Long, Integer> getSlotIdAndTypeMap(Long entityId) {
        Map<Long, Integer> slotMap = new HashMap<Long, Integer>();
        Map<Object, Object> idAndTypeMap = selectMapByKeyAndValue(getNameSpace("getSlotIdAndTypeMap"), entityId,
                "slotId", "slotType");
        for (Map.Entry<Object, Object> entry : idAndTypeMap.entrySet()) {
            slotMap.put(Long.parseLong(entry.getKey().toString()), Integer.parseInt(entry.getValue().toString()));
        }
        return slotMap;
    }

    /**
     * 处理SNI端口展示名称
     * 
     * @param slotType
     * @param sniIndex
     * @param sniPortType
     * @return
     */
    private String handleSniDisplayName(Integer slotType, Long sniIndex, Integer sniPortType) {
        // 如果板卡类型是XGUC则需要对端口名称进行特殊处理
        if (EponConstants.BOARD_PRECONFIG_XGUC.equals(slotType)
                || EponConstants.BOARD_PRECONFIG_MPU_XGUC.equals(slotType)
                || EponConstants.BOARD_PRECONFIG_MEUA.equals(slotType)
                || EponConstants.BOARD_PRECONFIG_MEUB.equals(slotType)
                || EponConstants.BOARD_PRECONFIG_MEFA.equals(slotType)
                || EponConstants.BOARD_PRECONFIG_MEFB.equals(slotType)
                || EponConstants.BOARD_PRECONFIG_MEFC.equals(slotType)
                || EponConstants.BOARD_PRECONFIG_MEFD.equals(slotType)
                || EponConstants.BOARD_PRECONFIG_MGUA.equals(slotType)
                || EponConstants.BOARD_PRECONFIG_MGUB.equals(slotType)) {
            // 对于 XGUC的XE口处理为 "XE2/1"形式 , 约定前两个是XE端口
            // 对于 XGUC的GE口处理为 "GE2/1"形式, 约定后四个是GE端口
            StringBuilder sniNameBuilder = new StringBuilder();
            Long slotNo = EponIndex.getSlotNo(sniIndex);
            Long gePortNo = EponIndex.getSniNo(sniIndex) > 16 ? (EponIndex.getSniNo(sniIndex) - 16) : EponIndex
                    .getSniNo(sniIndex);
            if (EponConstants.BOARD_PRECONFIG_MEUB.equals(slotType)) {
                sniNameBuilder.append("GE").append(slotNo).append("/").append(gePortNo);
            } else if (EponConstants.BOARD_PRECONFIG_MGUA.equals(slotType)
                    || EponConstants.BOARD_PRECONFIG_MGUB.equals(slotType)) {
                sniNameBuilder.append("XE").append(slotNo).append("/").append(gePortNo);
            } else {
                if (gePortNo < 3) {
                    sniNameBuilder.append("XE").append(slotNo).append("/").append(gePortNo);
                } else {
                    // 特殊处理 ： 对GE的端口号进行进行减２的特殊处理
                    sniNameBuilder.append("GE").append(slotNo).append("/").append(gePortNo - 2);
                }
            }
            return sniNameBuilder.toString();
        } else {
            // 对于其他板卡的端口,根据端口类型返回对应的形如"GE3/1"表示形式
            return EponConstants.SNI_TYPE_MAP.get(sniPortType) + EponIndex.getPortStringByIndex(sniIndex);
        }
    }

    @Override
    public List<OltSniAttribute> getSniPortList(Map<String, Object> map) {
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace() + "getSniPortList", map);
    }

    @Override
    public List<OltSniAttribute> availableSniListForTrunkGroupBySniAttribute(OltSniAttribute oltSniAttr) {
        return getSqlSession()
                .selectList(getNameSpace() + "getAvailableSniListForTrunkGroupBySniAttribute", oltSniAttr);
    }

    @Override
    public List<OltSniAttribute> availableSniListForTrunkGroupByEntityId(Long entityId) {
        return getSqlSession().selectList(getNameSpace() + "getAllSniList", entityId);
    }

    @Override
    public void batchUpdateSni15MinStatus(List<OltSniAttribute> oltSniAttrs) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (OltSniAttribute sniAttr : oltSniAttrs) {
                sniAttr.setSniId((Long) sqlSession.selectOne(getNameSpace() + "getSniId", sniAttr));
                sqlSession.update(getNameSpace() + "updateSni15MinPerfStatus", sniAttr);
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
    public String querySniDisplayName(Long entityId, Long sniIndex) {
        Map<String, Long> paramMap = new HashMap<String, Long>();
        paramMap.put("entityId", entityId);
        paramMap.put("sniIndex", sniIndex);
        return getSqlSession().selectOne(getNameSpace("querySniDisplayName"), paramMap);
    }

    @Override
    public void updateSniIngressRateAndEgressRate(Long sniId, Integer topSniAttrIngressRate,
            Integer topSniAttrEgressRate) {
        OltSniAttribute sniAttribute = new OltSniAttribute();
        sniAttribute.setSniId(sniId);
        sniAttribute.setTopSniAttrIngressRate(topSniAttrIngressRate);
        sniAttribute.setTopSniAttrEgressRate(topSniAttrEgressRate);
        getSqlSession().update(getNameSpace("updateSniIngressRateAndEgressRate"), sniAttribute);
    }

}
