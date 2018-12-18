/***********************************************************************
 * $Id: CmcDiscoveryDaoImpl.java,v1.0 2011-11-13 上午10:30:56 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.topology.dao.mybatis;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcPort;
import com.topvision.ems.cmc.domain.CmCmcRelation;
import com.topvision.ems.cmc.domain.CmcEntity;
import com.topvision.ems.cmc.domain.CmcPortRelation;
import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.cmc.facade.domain.CmCpe;
import com.topvision.ems.cmc.facade.domain.CmStaticIp;
import com.topvision.ems.cmc.facade.domain.CmcDownChannelBaseInfo;
import com.topvision.ems.cmc.facade.domain.CmcEntityRelation;
import com.topvision.ems.cmc.facade.domain.DocsIf3CmtsCmUsStatus;
import com.topvision.ems.cmc.facade.domain.TopCpeAttribute;
import com.topvision.ems.cmc.sni.facade.domain.CmcPhyConfig;
import com.topvision.ems.cmc.topology.dao.CmcDiscoveryDao;
import com.topvision.ems.cmc.upchannel.facade.domain.CmcUpChannelBaseInfo;
import com.topvision.ems.cmc.upchannel.facade.domain.CmcUpChannelSignalQualityInfo;
import com.topvision.ems.exception.TopvisionDataException;
import com.topvision.ems.network.dao.EntityDao;
import com.topvision.ems.network.domain.Port;
import com.topvision.framework.common.IpUtils;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author loyal
 * @created @2011-11-13-上午10:30:56
 * 
 */
@Repository("cmcDiscoveryDao")
public class CmcDiscoveryDaoImpl extends MyBatisDaoSupport<CmcEntity> implements CmcDiscoveryDao {
    private final Logger logger = LoggerFactory.getLogger(CmcDiscoveryDaoImpl.class);
    @Autowired
    private EntityDao entityDao;

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.ibatis.IbatisDaoSupport#getNameSpace()
     */
    @Override
    public String getDomainName() {
        return "com.topvision.ems.cmc.discovery.domain.CmcDiscovery";
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.discovery.dao.CmcDiscoveryDao#syncCmcEntityInfo(java.lang.Long,
     * java.util.List, java.util.List)
     */
    @Override
    public void syncCmcEntityInfo(Long entityId, List<CmcEntityRelation> cmcEntityRelations,
            List<CmcAttribute> cmcAttributes) {
        if (cmcEntityRelations.size() != cmcAttributes.size()) {
            throw new TopvisionDataException("CmcEntityRelation Data Error");
        }
        List<CmcEntityRelation> db_relation = getSqlSession().selectList(getNameSpace() + "getCmcEntityRelationByOlt",
                entityId);
        SqlSession batchSession = getBatchSession();
        try {
            for (int i = 0; i < cmcEntityRelations.size(); i++) {
                CmcEntityRelation cmcEntityRelation = cmcEntityRelations.get(i);
                CmcAttribute cmcAttribute = cmcAttributes.get(i);
                if (db_relation.contains(cmcEntityRelation)) {
                    batchSession.update(getNameSpace() + "updateCmcEntityRelation", cmcEntityRelation);
                    batchSession.update(getNameSpace() + "updateCmcAttribute", cmcAttribute);
                    if (cmcAttribute.getCmcDeviceStyle() != null) {
                        batchSession.update(getNameSpace() + "updateEntityTypeByCmcAttribute", cmcAttribute);
                    }
                } else {
                    batchSession.insert(getNameSpace() + "insertCmcEntityRelation", cmcEntityRelation);
                    batchSession.insert(getNameSpace() + "insertCmcAttribute", cmcAttribute);
                }

                // 更新别名记录中的别名
                String historyAlias = null;
                historyAlias = entityDao.selectHistoryAlias(cmcAttribute.getEntityId(),
                        cmcAttribute.getTopCcmtsSysMacAddr(), cmcAttribute.getCmcIndex().toString(), null);
                if (historyAlias != null) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("cmcId", cmcAttribute.getCmcId());
                    map.put("name", historyAlias);
                    batchSession.update(getNameSpace() + "updateCmcName", map);
                }
            }
            batchSession.commit();
        } catch (Exception e) {
            logger.error("syncCmcEntityInfo Error:", e);
            batchSession.rollback();
        } finally {
            batchSession.close();
        }

    }

    @Override
    public Map<String, Long> getChannelMap(Long entityId) {
        HashMap<String, Long> channelMap = new HashMap<>();
        Map<Object, Object> map = selectMapByKeyAndValue(getNameSpace() + "getChannelMap", entityId,
                "cmcId_channelIndex", "cmcPortId");
        for (Map.Entry<Object, Object> entry : map.entrySet()) {
            channelMap.put(entry.getKey().toString(), Long.parseLong(entry.getValue().toString()));
        }
        return channelMap;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.discovery.dao.CmcDiscoveryDao#syncCmcChannelBaseInfo(java.util.List,
     * java.util.List, java.util.List, java.lang.Long)
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void syncCmcChannelBaseInfo(List<CmcUpChannelBaseInfo> cmcUpChannelBaseInfos,
            List<CmcUpChannelSignalQualityInfo> cmcUpChannelSignalQualityInfos,
            List<CmcDownChannelBaseInfo> cmcDownChannelBaseInfos, List<CmcPort> cmcPorts, Long entityId) {

        Map<String, Long> channelMap = getChannelMap(entityId);

        SqlSession batchSession = getBatchSession();
        try {
            if (cmcUpChannelBaseInfos != null) {
                for (CmcUpChannelBaseInfo cmcUpChannelBaseInfo : cmcUpChannelBaseInfos) {
                    Long cmcId = cmcUpChannelBaseInfo.getCmcId();
                    Long channelIndex = cmcUpChannelBaseInfo.getChannelIndex();
                    String id_index_key = cmcId + "_" + channelIndex;
                    Long cmcPortId = channelMap.get(id_index_key);
                    if (cmcPortId != null) {
                        cmcUpChannelBaseInfo.setCmcPortId(cmcPortId);
                        // batchSession.update(getNameSpace() + "updateCmcUpChannelBaseInfo",
                        // cmcUpChannelBaseInfo);
                        batchSession.update(getNameSpace() + "syncCmcUpChannelBaseInfo", cmcUpChannelBaseInfo);
                    } else {
                        // CmcPortRelation
                        CmcPortRelation cmcPortRelation = new CmcPortRelation();
                        cmcPortRelation.setCmcId(cmcUpChannelBaseInfo.getCmcId());
                        cmcPortRelation.setChannelIndex(cmcUpChannelBaseInfo.getChannelIndex());
                        cmcPortRelation.setChannelType(0);
                        // 需要使用getSqlSession 获取自增主键
                        getSqlSession().insert(getNameSpace() + "insertCmcPortRelation", cmcPortRelation);
                        channelMap.put(id_index_key, cmcPortRelation.getCmcPortId());
                        // CmcUpChannel
                        cmcUpChannelBaseInfo.setCmcPortId(cmcPortRelation.getCmcPortId());
                        batchSession.insert(getNameSpace() + "insertCmcUpChannelBaseInfo", cmcUpChannelBaseInfo);
                    }
                }
            }
            if (cmcDownChannelBaseInfos != null) {
                for (CmcDownChannelBaseInfo cmcDownChannelBaseInfo : cmcDownChannelBaseInfos) {
                    Long cmcId = cmcDownChannelBaseInfo.getCmcId();
                    Long channelIndex = cmcDownChannelBaseInfo.getChannelIndex();
                    String id_index_key = cmcId + "_" + channelIndex;
                    Long cmcPortId = channelMap.get(id_index_key);
                    if (cmcPortId != null) {
                        cmcDownChannelBaseInfo.setCmcPortId(cmcPortId);
                        // batchSession.update(getNameSpace() + "updateCmcDownChannelBaseInfo",
                        // cmcDownChannelBaseInfo);
                        batchSession.update(getNameSpace() + "syncCmcDownChannelBaseInfo", cmcDownChannelBaseInfo);
                    } else {
                        // CmcPortRelation
                        CmcPortRelation cmcPortRelation = new CmcPortRelation();
                        cmcPortRelation.setCmcId(cmcDownChannelBaseInfo.getCmcId());
                        cmcPortRelation.setChannelIndex(cmcDownChannelBaseInfo.getChannelIndex());
                        cmcPortRelation.setChannelType(1);
                        getSqlSession().insert(getNameSpace() + "insertCmcPortRelation", cmcPortRelation);
                        channelMap.put(id_index_key, cmcPortRelation.getCmcPortId());
                        // CmcDownChannel
                        cmcDownChannelBaseInfo.setCmcPortId(cmcPortRelation.getCmcPortId());
                        batchSession.insert(getNameSpace() + "insertCmcDownChannelBaseInfo", cmcDownChannelBaseInfo);
                    }
                }
            }
            if (cmcPorts != null) {
                for (CmcPort cmcPort : cmcPorts) {
                    Long cmcId = cmcPort.getCmcId();
                    Long channelIndex = cmcPort.getIfIndex();
                    String id_index_key = cmcId + "_" + channelIndex;
                    Long cmcPortId = channelMap.get(id_index_key);
                    cmcPort.setCmcPortId(cmcPortId);
                    batchSession.insert(getNameSpace() + "syncCmcPortInfo", cmcPort);
                }
            }
            if (cmcUpChannelSignalQualityInfos != null) {
                for (CmcUpChannelSignalQualityInfo cmcUpChannelSignalQualityInfo : cmcUpChannelSignalQualityInfos) {
                    Long cmcId = cmcUpChannelSignalQualityInfo.getCmcId();
                    Long channelIndex = cmcUpChannelSignalQualityInfo.getChannelIndex();
                    String id_index_key = cmcId + "_" + channelIndex;
                    Long cmcPortId = channelMap.get(id_index_key);
                    if (cmcUpChannelSignalQualityInfo.getDocsIfSigQSignalNoise() != null
                            && cmcUpChannelSignalQualityInfo.getDocsIfSigQSignalNoise() < 0) {
                        cmcUpChannelSignalQualityInfo.setDocsIfSigQSignalNoise(0l);
                    }
                    cmcUpChannelSignalQualityInfo.setCmcPortId(cmcPortId);
                    batchSession.update(getNameSpace() + "syncCmcUpChannelSignalQualityInfo",
                            cmcUpChannelSignalQualityInfo);
                }
            }
            batchSession.commit();
        } catch (Exception e) {
            logger.error("syncCmcChannelBaseInfo Error:", e);
            batchSession.rollback();
            throw e;
        } finally {
            batchSession.close();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcDiscoveryDao#batchInsertCmcUpChannelSignalQualityInfo(java.util
     * .List)
     */
    @Override
    public void batchInsertCmcUpChannelSignalQualityInfo(
            final List<CmcUpChannelSignalQualityInfo> cmcUpChannelSignalQualityInfoList, final Long cmcId) {
        SqlSession session = getBatchSession();
        try {
            for (CmcUpChannelSignalQualityInfo cmcUpChannelSignalQualityInfo : cmcUpChannelSignalQualityInfoList) {
                Map<String, Long> map = new HashMap<String, Long>();
                map.put("cmcId", cmcId);
                map.put("channelIndex", cmcUpChannelSignalQualityInfo.getChannelIndex());
                cmcUpChannelSignalQualityInfo.setCmcId(cmcId);
                cmcUpChannelSignalQualityInfo.setCmcPortId(getCmcPortIdByIfIndexAndCmcId(map));
                if (cmcUpChannelSignalQualityInfo.getDocsIfSigQSignalNoise() < 0) {
                    cmcUpChannelSignalQualityInfo.setDocsIfSigQSignalNoise(0l);
                }
                if (((Integer) getSqlSession().selectOne(getNameSpace() + "getCmcUpChannelSignalQualityInfo",
                        cmcUpChannelSignalQualityInfo)) != 0) {
                    getSqlSession().update(getNameSpace() + "updateCmcUpChannelSignalQualityInfo",
                            cmcUpChannelSignalQualityInfo);
                } else {
                    getSqlSession().insert(getNameSpace() + "insertCmcUpChannelSignalQualityInfo",
                            cmcUpChannelSignalQualityInfo);
                }
                if (((Integer) getSqlSession().selectOne(getNameSpace() + "getPerfCmcUpChannelSignalQualityInfo",
                        cmcUpChannelSignalQualityInfo)) != 0) {
                    getSqlSession().update(getNameSpace() + "updatePerfCmcUpChannelSignalQualityInfo",
                            cmcUpChannelSignalQualityInfo);
                } else {
                    getSqlSession().insert(getNameSpace() + "insertPerfCmcUpChannelSignalQualityInfo",
                            cmcUpChannelSignalQualityInfo);
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

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcDiscoveryDao#batchInsertCmcDownChannelBaseInfo(java.util.List)
     */
    @Override
    public void batchInsertCmcDownChannelBaseInfo(final List<CmcDownChannelBaseInfo> cmcDownChannelBaseInfoList,
            final Long cmcId) {
        SqlSession session = null;
        List<CmcDownChannelBaseInfo> insertList = new ArrayList<CmcDownChannelBaseInfo>();
        for (CmcDownChannelBaseInfo cmcDownChannelBaseInfo : cmcDownChannelBaseInfoList) {
            cmcDownChannelBaseInfo.setCmcId(cmcId);
            if (((Integer) getSqlSession().selectOne(getNameSpace() + "getCmcDownChannelBaseInfo",
                    cmcDownChannelBaseInfo)) != 0) {
                getSqlSession().update(getNameSpace() + "updateCmcDownChannelBaseInfo", cmcDownChannelBaseInfo);
            } else {
                insertList.add(cmcDownChannelBaseInfo);
            }
        }
        try {
            session = getBatchSession();
            for (CmcDownChannelBaseInfo cmcDownChannelBaseInfo : insertList) {
                cmcDownChannelBaseInfo.setCmcId(cmcId);
                CmcPortRelation cmcPortRelation = new CmcPortRelation();
                cmcPortRelation.setCmcId(cmcDownChannelBaseInfo.getCmcId());
                cmcPortRelation.setChannelIndex(cmcDownChannelBaseInfo.getChannelIndex());
                cmcPortRelation.setChannelType(1);
                getSqlSession().insert(getNameSpace() + "insertCmcPortRelation", cmcPortRelation);
                Long cmcPortId = cmcPortRelation.getCmcPortId();
                cmcDownChannelBaseInfo.setCmcPortId(cmcPortId);
            }
            session.commit();
        } catch (Exception e) {
            logger.error("", e);
            session.rollback();
        } finally {
            session.close();
        }
        try {
            session = getBatchSession();
            for (CmcDownChannelBaseInfo cmcDownChannelBaseInfo : insertList) {
                session.insert(getNameSpace() + "insertCmcDownChannelBaseInfo", cmcDownChannelBaseInfo);
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
    public void batchInsertCmcPortInfo(final List<CmcPort> cmcPortList, final Long cmcId) {
        SqlSession session = getBatchSession();
        try {
            for (CmcPort cmcPort : cmcPortList) {
                Map<String, Long> map = new HashMap<String, Long>();
                map.put("cmcId", cmcId);
                /*
                 * ifIndex中增加了两个上联口：值固定为1，2. 包括一个mac domain. 剩下的才是上下行信道。 这里只能处理上下行信道，其他ifIndex的值都过滤掉
                 * modify by lzs at 20130508
                 */
                Integer ifType = cmcPort.getIfType();
                // if
                // (CmcIndexUtils.getCmcIndexFromChannelIndex(cmcPort.getIfIndex()).longValue()
                // != cmcPort
                // .getIfIndex().longValue()) {
                // 处理index是mac domain情况
                if (CmcPort.IF_TYPE_CABLE_DOWNSTREAM.equals(ifType) || CmcPort.IF_TYPE_CABLE_UPSTREAM.equals(ifType)) {
                    map.put("channelIndex", cmcPort.getIfIndex());
                    cmcPort.setCmcId(cmcId);
                    cmcPort.setCmcPortId(getCmcPortIdByIfIndexAndCmcId(map));
                    // Add by Victor@20160728解决几个表不一致，导致所有数据不能插入的问题
                    if (cmcPort.getCmcPortId() == null) {
                        continue;
                    }
                    CmcPort cmcPortDb = getSqlSession().selectOne(getNameSpace() + "getCmcPort", cmcPort);
                    if (cmcPortDb != null) {
                        if (cmcPortDb.getIfName() != null && !"".equals(cmcPortDb.getIfAlias())) {
                            // 如果ifname有值，则不更新
                            cmcPort.setIfName(cmcPortDb.getIfName());
                        } else {
                            // 为了解决大C的ifname第一次发现没有插入，或者是此功能之前的版本ifname没采集的情况，更新的时候如果ifname为空，则插入
                            if (cmcPort.getIfAlias() != null && !"".equals(cmcPort.getIfAlias())) {
                                cmcPort.setIfName(cmcPort.getIfAlias());
                            }
                        }
                        session.update(getNameSpace() + "updateCmcPort", cmcPort);
                    } else {
                        // 第一次拓扑发现的时候，如果大C端口的ifAlias不为空，就存ifAlias，如果ifAlias空，就存ifName
                        if (cmcPort.getIfAlias() != null && !"".equals(cmcPort.getIfAlias())) {
                            cmcPort.setIfName(cmcPort.getIfAlias());
                        }
                        session.insert(getNameSpace() + "insertCmcPort", cmcPort);
                    }
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

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.dao.CmcDiscoveryDao#batchInsertCmcChannelInfo(java.util.List,
     * java.lang.Long)
     */
    @Override
    public void batchInsertCmcChannelInfoForArris(final List<CmcPort> cmcPortList, final Long cmcId) {
        SqlSession session = getBatchSession();
        try {
            for (CmcPort cmcPort : cmcPortList) {
                Map<String, Long> map = new HashMap<String, Long>();
                map.put("cmcId", cmcId);
                /*
                 * ifIndex中增加了两个上联口：值固定为1，2. 包括一个mac domain. 剩下的才是上下行信道。 这里只能处理上下行信道，其他ifIndex的值都过滤掉
                 * modify by lzs at 20130508
                 */
                Integer ifType = cmcPort.getIfType();
                // if
                // (CmcIndexUtils.getCmcIndexFromChannelIndex(cmcPort.getIfIndex()).longValue()
                // != cmcPort
                // .getIfIndex().longValue()) {
                // 处理index是mac domain情况
                // Modify by Victor@20160728对于大CMTS 129和205都认为是上行信道
                if (CmcPort.IF_TYPE_CABLE_DOWNSTREAM_CHANNEL.equals(ifType)
                        || CmcPort.IF_TYPE_CABLE_UPSTREAM_CHANNEL.equals(ifType)) {
                    map.put("channelIndex", cmcPort.getIfIndex());
                    cmcPort.setCmcId(cmcId);
                    cmcPort.setCmcPortId(getCmcPortIdByIfIndexAndCmcId(map));
                    // Add by Victor@20160728解决几个表不一致，导致所有数据不能插入的问题
                    if (cmcPort.getCmcPortId() == null) {
                        continue;
                    }
                    CmcPort portDb = getSqlSession().selectOne(getNameSpace() + "getCmcPort", cmcPort);
                    if (portDb != null) {
                        if (portDb.getIfName() != null && !"".equals(portDb.getIfName())) {
                            cmcPort.setIfName(portDb.getIfName());
                        } else {
                            if (cmcPort.getIfAlias() != null && !"".equals(cmcPort.getIfAlias())) {
                                cmcPort.setIfName(cmcPort.getIfAlias());
                            }
                        }
                        getSqlSession().update(getNameSpace() + "updateCmcPort", cmcPort);
                    } else {
                        if (cmcPort.getIfAlias() != null && !"".equals(cmcPort.getIfAlias())) {
                            cmcPort.setIfName(cmcPort.getIfAlias());
                        }
                        getSqlSession().insert(getNameSpace() + "insertCmcPort", cmcPort);
                    }
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

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.dao.CmcDiscoveryDao#batchInsertCmcChannelInfo(java.util.List,
     * java.lang.Long)
     */
    @Override
    public void batchInsertCmcChannelInfoForUbr(final List<CmcPort> cmcPortList, final Long cmcId) {
        SqlSession session = getBatchSession();
        try {
            for (CmcPort cmcPort : cmcPortList) {
                Map<String, Long> map = new HashMap<String, Long>();
                map.put("cmcId", cmcId);
                Integer ifType = cmcPort.getIfType();

                /*
                 * GET NEXT : ifType.1289 -> 1 GET NEXT : ifType.1290 -> 1 GET NEXT : ifType.1291 ->
                 * 1 GET NEXT : ifType.1292 -> 1 GET NEXT : ifType.1293 -> 1 GET NEXT : ifType.1294
                 * -> 1 GET NEXT : ifType.1295 -> 1 GET NEXT : ifType.1296 -> 1 GET NEXT :
                 * ifType.1337 -> 129 GET NEXT : ifType.1338 -> 129 GET NEXT : ifType.1339 -> 129
                 * GET NEXT : ifType.1340 -> 129 GET NEXT : ifType.1361 -> 129 GET NEXT :
                 * ifType.1362 -> 129 GET NEXT : ifType.1363 -> 129 GET NEXT : ifType.1364 -> 129
                 */
                if (CmcPort.IF_TYPE_OTHER.equals(ifType) || CmcPort.IF_TYPE_CABLE_DOWNSTREAM.equals(ifType)
                        || CmcPort.IF_TYPE_CABLE_UPSTREAM.equals(ifType)) {
                    map.put("channelIndex", cmcPort.getIfIndex());
                    cmcPort.setCmcId(cmcId);
                    cmcPort.setCmcPortId(getCmcPortIdByIfIndexAndCmcId(map));
                    // Add by Victor@20160728解决几个表不一致，导致所有数据不能插入的问题
                    if (cmcPort.getCmcPortId() == null) {
                        continue;
                    }
                    CmcPort portDb = getSqlSession().selectOne(getNameSpace() + "getCmcPort", cmcPort);
                    if (portDb != null) {
                        if (portDb.getIfName() != null && !"".equals(portDb.getIfName())) {
                            cmcPort.setIfName(portDb.getIfName());
                        } else {
                            if (cmcPort.getIfAlias() != null && !"".equals(cmcPort.getIfAlias())) {
                                cmcPort.setIfName(cmcPort.getIfAlias());
                            }
                        }
                        getSqlSession().update(getNameSpace() + "updateCmcPort", cmcPort);
                    } else {
                        if (cmcPort.getIfAlias() != null && !"".equals(cmcPort.getIfAlias())) {
                            cmcPort.setIfName(cmcPort.getIfAlias());
                        }
                        getSqlSession().insert(getNameSpace() + "insertCmcPort", cmcPort);
                    }
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

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.dao.CmcDiscoveryDao#batchInsertCmcChannelInfo(java.util.List,
     * java.lang.Long)
     */
    @Override
    public void batchInsertCmcChannelInfo(final List<CmcPort> cmcPortList, final Long cmcId) {
        SqlSession session = getBatchSession();
        try {
            for (CmcPort cmcPort : cmcPortList) {
                Map<String, Long> map = new HashMap<String, Long>();
                map.put("cmcId", cmcId);
                /*
                 * ifIndex中增加了两个上联口：值固定为1，2. 包括一个mac domain. 剩下的才是上下行信道。 这里只能处理上下行信道，其他ifIndex的值都过滤掉
                 * modify by lzs at 20130508
                 */
                Integer ifType = cmcPort.getIfType();
                // if
                // (CmcIndexUtils.getCmcIndexFromChannelIndex(cmcPort.getIfIndex()).longValue()
                // != cmcPort
                // .getIfIndex().longValue()) {
                // 处理index是mac domain情况
                // Modify by Victor@20160728对于大CMTS 129和205都认为是上行信道
                if (CmcPort.IF_TYPE_CABLE_DOWNSTREAM_CHANNEL.equals(ifType)
                        || CmcPort.IF_TYPE_CABLE_UPSTREAM_CHANNEL.equals(ifType)
                        || CmcPort.IF_TYPE_CABLE_UPSTREAM.equals(ifType)) {
                    map.put("channelIndex", cmcPort.getIfIndex());
                    cmcPort.setCmcId(cmcId);
                    cmcPort.setCmcPortId(getCmcPortIdByIfIndexAndCmcId(map));
                    // Add by Victor@20160728解决几个表不一致，导致所有数据不能插入的问题
                    if (cmcPort.getCmcPortId() == null) {
                        continue;
                    }
                    CmcPort portDb = getSqlSession().selectOne(getNameSpace() + "getCmcPort", cmcPort);
                    if (portDb != null) {
                        if (portDb.getIfName() != null && !"".equals(portDb.getIfName())) {
                            cmcPort.setIfName(portDb.getIfName());
                        } else {
                            if (cmcPort.getIfAlias() != null && !"".equals(cmcPort.getIfAlias())) {
                                cmcPort.setIfName(cmcPort.getIfAlias());
                            }
                        }
                        getSqlSession().update(getNameSpace() + "updateCmcPort", cmcPort);
                    } else {
                        if (cmcPort.getIfAlias() != null && !"".equals(cmcPort.getIfAlias())) {
                            cmcPort.setIfName(cmcPort.getIfAlias());
                        }
                        getSqlSession().insert(getNameSpace() + "insertCmcPort", cmcPort);
                    }
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
    @Deprecated
    public void batchInsertCmAttribute8800b(final List<CmAttribute> cmAttributeList, final Long cmcId,
            final Long entityId) {
        SqlSession session = getBatchSession();
        try {
            getSqlSession().delete(getNameSpace() + "delCmCmcRelationByEntityId", entityId);
            for (CmAttribute cmAttribute : cmAttributeList) {
                Map<String, Long> map = new HashMap<String, Long>();
                cmAttribute.setCmcId(cmcId);
                CmCmcRelation cmCmcRelation = new CmCmcRelation();
                cmCmcRelation.setEntityId(entityId);
                cmCmcRelation.setCmIndex(cmAttribute.getStatusIndex());
                cmCmcRelation.setCmcId(cmcId);
                cmCmcRelation.setMac(cmAttribute.getStatusMacAddress());
                map.put("cmcId", cmcId);
                map.put("channelIndex", cmAttribute.getStatusUpChannelIfIndex());
                cmCmcRelation.setUpPortId(getCmcPortIdByIfIndexAndCmcId(map));
                map.remove("ifIndex");
                map.put("channelIndex", cmAttribute.getStatusDownChannelIfIndex());
                cmCmcRelation.setDownPortId(getCmcPortIdByIfIndexAndCmcId(map));
                // add by fanzidong,入库之前需要格式化MAC地址
                String formatedMac = MacUtils.convertToMaohaoFormat(cmCmcRelation.getMac());
                cmCmcRelation.setMac(formatedMac);
                getSqlSession().insert(getNameSpace() + "insertCmCmcRelation", cmCmcRelation);
                cmAttribute.setCmId(cmCmcRelation.getCmId());
                session.insert(getNameSpace() + "insertCmAttribute", cmAttribute);
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
    public Long getCmcIdByCmcIndexAndEntityId(Map<String, Long> map) {
        return (Long) getSqlSession().selectOne(getNameSpace() + "getCmcIdByCmcIndexAndEntityId", map);
    }

    @Override
    public List<Long> getCmcIdByOlt(Long entityId) {
        return getSqlSession().selectList(getNameSpace() + "getCmcIdByOlt", entityId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.dao.CmcDiscoveryDao#getCmcPortIdByIfIndex(java.lang.Long)
     */
    @Override
    public Long getCmcPortIdByIfIndexAndCmcId(Map<String, Long> map) {
        return (Long) getSqlSession().selectOne(getNameSpace() + "getCmcPortIdByIfIndexAndCmcId", map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.dao.CmcDiscoveryDao#getCmIdByCmcIdAndIfIndex(java.util.Map)
     */
    @Override
    public Long getCmIdByMac(Long entityId, Long mac) {
        CmCmcRelation cmCmcRelation = new CmCmcRelation();
        cmCmcRelation.setEntityId(entityId);
        cmCmcRelation.setMaclong(mac);
        return (Long) getSqlSession().selectOne(getNameSpace() + "getCmIdByCmMac", cmCmcRelation);
    }

    @Override
    public void updateEntityMac(long entityId, String mac) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("mac", mac);
        getSqlSession().update(getNameSpace() + "updateEntityMac", map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.dao.CmcDiscoveryDao#deleteCmcOfflinCm(long)
     */
    @Override
    public void deleteCmcOfflineCm(long cmcId) {
        getSqlSession().delete(getNameSpace() + "deleteOfflineCmcCmRelationByCmc", cmcId);
        getSqlSession().delete(getNameSpace() + "deleteOfflineCmAttributeByCmc", cmcId);
    }

    @Override
    public List<Long> queryForCmIdListByCmcId(Long cmcId) {
        return getSqlSession().selectList(getNameSpace() + "queryForCmIdListByCmcId", cmcId);
    }

    @Override
    public void deleteCmByCmId(Long cmId) {
        getSqlSession().delete(getNameSpace() + "deleteCmByCmId", cmId);
    }

    @Override
    public void updateCmcAttributeToOnuAttribute(CmcAttribute cmcAttribute) {
        getSqlSession().update(getNameSpace() + "updateCmcAttributeToOnuAttribute", cmcAttribute);
    }

    @Override
    public void batchInsertOrUpdateCmCpe(final List<CmCpe> cmCpeList, final Long entityId) {
        getSqlSession().delete(getNameSpace() + "deteleCpeByEntityId", entityId);
        SqlSession session = getBatchSession();
        try {
            for (CmCpe cmCpe : cmCpeList) {
                String cpeMac = cmCpe.getTopCmCpeMacAddress().toString();
                Long cmIndex = cmCpe.getTopCmCpeCmStatusIndex();
                // 通过cm的inedex获取cmId

                CmCmcRelation cmCmcRelation = new CmCmcRelation();
                cmCmcRelation.setEntityId(entityId);
                cmCmcRelation.setCmIndex(cmCpe.getTopCmCpeCmStatusIndex());
                cmCmcRelation.setMac(cmCpe.getTopCmCpeToCmMacAddr());

                Map<String, Long> map = new HashMap<String, Long>();
                map.put("cmIndex", cmIndex);
                map.put("entityId", entityId);
                Long cmId = null;
                try {
                    cmId = ((Long) getSqlSession().selectOne(getNameSpace() + "getCmIdByCmMac", cmCmcRelation));
                } catch (DataAccessException e) {
                    logger.debug("Param:" + cmCmcRelation, e);
                }
                if (cmId != null) {
                    cmCpe.setCmId(cmId);
                    cmCpe.setTopCmCpeMacAddressString(cmCpe.getTopCmCpeMacAddress().toString());
                    cmCpe.setEntityId(entityId);
                    // add by fanzidong,在入库之前需要格式化MAC地址
                    String formatedCmCpeMac = MacUtils.convertToMaohaoFormat(cmCpe.getTopCmCpeMacAddressString());
                    cmCpe.setTopCmCpeMacAddressString(formatedCmCpeMac);
                    String formatedCmCpeToCmMac = MacUtils.convertToMaohaoFormat(cmCpe.getTopCmCpeToCmMacAddr());
                    cmCpe.setTopCmCpeToCmMacAddr(formatedCmCpeToCmMac);
                    cpeMac = MacUtils.convertToMaohaoFormat(cpeMac);

                    session.delete(getNameSpace() + "deleteCmCpeByCpeMac", cpeMac);
                    session.insert(getNameSpace() + "insertCmCpe", cmCpe);
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
    public void batchInsertOrUpdateCmStaticIp(final List<CmStaticIp> cmStaticIpList, final Long entityId) {
        getSqlSession().delete(getNameSpace() + "deteleCmStaticIpByEntityId", entityId);
        SqlSession session = getBatchSession();
        try {
            for (CmStaticIp cmStaticIp : cmStaticIpList) {
                Long cmIndex = cmStaticIp.getDocsIfCmtsCmStatusIndex();
                // 通过cm的inedex获取cmId
                Map<String, Long> map = new HashMap<String, Long>();
                map.put("cmIndex", cmIndex);
                map.put("entityId", entityId);
                Long cmId = ((Long) getSqlSession().selectOne(getNameSpace() + "getCmIdByCmIndex", map));
                if (cmId != null) {
                    cmStaticIp.setCmId(cmId);
                }
                cmStaticIp.setEntityId(entityId);

                session.insert(getNameSpace() + "insertCmStaticIp", cmStaticIp);
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
    public void batchInsertOrUpdateDocsIf3CmtsCmUsStatus(final List<DocsIf3CmtsCmUsStatus> docsIf3CmtsCmUsStatusList,
            final Long cmcId) {
        SqlSession session = getBatchSession();
        try {
            for (DocsIf3CmtsCmUsStatus docsIf3CmtsCmUsStatus : docsIf3CmtsCmUsStatusList) {
                Long cmIndex = docsIf3CmtsCmUsStatus.getCmRegStatusId();
                // 通过cm的inedex获取cmId
                Map<String, Long> map = new HashMap<String, Long>();
                map.put("cmIndex", cmIndex);
                map.put("entityId", cmcId);
                Long cmId = ((Long) getSqlSession().selectOne(getNameSpace() + "getCmIdByCmIndex", map));
                session.delete(getNameSpace() + "deleteUsStatusByCmId", cmId);
            }
            session.commit();
            for (DocsIf3CmtsCmUsStatus docsIf3CmtsCmUsStatus : docsIf3CmtsCmUsStatusList) {
                Long cmIndex = docsIf3CmtsCmUsStatus.getCmRegStatusId();
                // 通过cm的inedex获取cmId
                Map<String, Long> map = new HashMap<String, Long>();
                map.put("cmIndex", cmIndex);
                map.put("entityId", cmcId);
                Long cmId = ((Long) getSqlSession().selectOne(getNameSpace() + "getCmIdByCmIndex", map));
                if (cmId != null) {
                    docsIf3CmtsCmUsStatus.setCmId(cmId);
                }
                session.insert(getNameSpace() + "insertDocsIf3CmtsCmUsStatus", docsIf3CmtsCmUsStatus);
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
    public void batchInsertOrUpdateDocsIf3CmtsCmUsStatusFor8800A(
            final List<DocsIf3CmtsCmUsStatus> docsIf3CmtsCmUsStatusList, final Long cmcId) {
        SqlSession session = getBatchSession();
        try {
            for (DocsIf3CmtsCmUsStatus docsIf3CmtsCmUsStatus : docsIf3CmtsCmUsStatusList) {
                Long cmIndex = docsIf3CmtsCmUsStatus.getCmRegStatusId();
                // 通过cm的inedex获取cmId
                Map<String, Long> map = new HashMap<String, Long>();
                map.put("cmIndex", cmIndex);
                map.put("cmcId", cmcId);
                Long cmId = ((Long) getSqlSession().selectOne(getNameSpace() + "getCmIdByMapFor8800A", map));
                session.delete(getNameSpace() + "deleteUsStatusByCmId", cmId);
            }
            session.commit();
            for (DocsIf3CmtsCmUsStatus docsIf3CmtsCmUsStatus : docsIf3CmtsCmUsStatusList) {
                Long cmIndex = docsIf3CmtsCmUsStatus.getCmRegStatusId();
                // 通过cm的inedex获取cmId
                Map<String, Long> map = new HashMap<String, Long>();
                map.put("cmIndex", cmIndex);
                map.put("cmcId", cmcId);
                Long cmId = ((Long) getSqlSession().selectOne(getNameSpace() + "getCmIdByMapFor8800A", map));
                if (cmId != null) {
                    docsIf3CmtsCmUsStatus.setCmId(cmId);
                }
                session.insert(getNameSpace() + "insertDocsIf3CmtsCmUsStatus", docsIf3CmtsCmUsStatus);
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
    public void batchInsertInitialDataCmAction(final long dt, final List<CmAttribute> cmAttributes,
            final Long entityId) {
        SqlSession session = getBatchSession();
        try {
            for (CmAttribute cmAttribute : cmAttributes) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("entityId", entityId);
                map.put("upChannelIfIndex", cmAttribute.getStatusUpChannelIfIndex());
                map.put("downChannelIfIndex", cmAttribute.getStatusDownChannelIfIndex());
                map.put("cmIndex", cmAttribute.getStatusIndex());
                map.put("cmmac", new MacUtils(cmAttribute.getStatusMacAddress()).longValue());
                long ip = 0;
                try {
                    ip = new IpUtils(cmAttribute.getStatusInetAddress()).longValue();
                } catch (Exception e) {
                    try {
                        ip = new IpUtils(cmAttribute.getStatusIpAddress()).longValue();
                    } catch (Exception e1) {
                        logger.debug("getStatusIpAddress", e1);
                    }
                    logger.debug("getStatusInetAddress", e);
                }
                map.put("cmip", ip);
                map.put("state", cmAttribute.getStatusValue());
                map.put("realtime", new Timestamp(dt));
                session.insert(getNameSpace() + "insertInitialDataCmAction", map);
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
    public void batchInsertOrUpdateCpeAttribute(final long dt, final List<TopCpeAttribute> attributes,
            final Long entityId) {
        SqlSession session = getBatchSession();
        try {
            for (TopCpeAttribute cmAttribute : attributes) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("entityId", entityId);
                map.put("cmmac", cmAttribute.getTopCmCpeToCmMacAddrLong());
                map.put("cpemac", cmAttribute.getTopCmCpeMacAddressLong());
                map.put("cpeip", cmAttribute.getTopCmCpeIpAddressLong());
                map.put("cpetype", cmAttribute.getTopCmCpeType());
                map.put("realtime", new Timestamp(dt));
                session.insert(getNameSpace() + "insertInitialDataCpeAction", map);
            }
            session.commit();
        } catch (Exception e) {
            logger.error("", e);
            session.rollback();
        } finally {
            session.close();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.dao.CmcDiscoveryDao#batchInsertCmtsPorts(java.lang.Long,
     * java.util.List)
     */
    public void batchInsertCmtsPorts(final Long cmcId, final List<CmcPort> cmtsPorts) {
        SqlSession session = getBatchSession();
        try {
            // getSqlSession().delete(getNameSpace() + "deleteCmtsPort", cmcId);
            for (CmcPort cmtsPort : cmtsPorts) {
                cmtsPort.setCmcId(cmcId);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("entityId", cmcId);
                map.put("ifIndex", cmtsPort.getIfIndex());
                Port portDb = getSqlSession().selectOne(getNameSpace() + "selectCmtsPortByEntityIdAndIfIndex", map);
                if (portDb != null) {
                    if (portDb.getIfName() != null && !"".equals(portDb.getIfName())) {
                        cmtsPort.setIfName(portDb.getIfName());
                    } else {
                        if (cmtsPort.getIfAlias() != null && !"".equals(cmtsPort.getIfAlias())) {
                            cmtsPort.setIfName(cmtsPort.getIfAlias());
                        }
                    }
                    getSqlSession().update(getNameSpace() + "updateCmtsPort", cmtsPort);
                } else {
                    if (cmtsPort.getIfAlias() != null && !"".equals(cmtsPort.getIfAlias())) {
                        cmtsPort.setIfName(cmtsPort.getIfAlias());
                    }
                    getSqlSession().insert(getNameSpace() + "insertCmtsPort", cmtsPort);
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

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.discovery.dao.CmcDiscoveryDao#batchInsertCmcUpChannelBaseInfo(java.util
     * .List, java.lang.Long)
     */
    @Override
    public void batchInsertCmcUpChannelBaseInfo(List<CmcUpChannelBaseInfo> cmcUpChannelBaseInfoList, Long cmcId) {
        SqlSession session = null;
        List<CmcUpChannelBaseInfo> insertList = new ArrayList<CmcUpChannelBaseInfo>();
        for (CmcUpChannelBaseInfo cmcUpChannelBaseInfo : cmcUpChannelBaseInfoList) {
            cmcUpChannelBaseInfo.setCmcId(cmcId);
            if (((Integer) getSqlSession().selectOne(getNameSpace() + "getUpChannelBaseInfo",
                    cmcUpChannelBaseInfo)) != 0) {
                getSqlSession().update(getNameSpace() + "updateCmcUpChannelBaseInfo", cmcUpChannelBaseInfo);
            } else {
                insertList.add(cmcUpChannelBaseInfo);
            }
        }
        try {
            session = getBatchSession();
            for (CmcUpChannelBaseInfo cmcUpChannelBaseInfo : insertList) {
                cmcUpChannelBaseInfo.setCmcId(cmcId);
                CmcPortRelation cmcPortRelation = new CmcPortRelation();
                cmcPortRelation.setCmcId(cmcUpChannelBaseInfo.getCmcId());
                cmcPortRelation.setChannelIndex(cmcUpChannelBaseInfo.getChannelIndex());
                cmcPortRelation.setChannelType(0);
                getSqlSession().insert(getNameSpace() + "insertCmcPortRelation", cmcPortRelation);
                cmcUpChannelBaseInfo.setCmcPortId(cmcPortRelation.getCmcPortId());
            }
            session.commit();
        } catch (Exception e) {
            logger.error("", e);
            session.rollback();
        } finally {
            session.close();
        }
        try {
            session = getBatchSession();
            for (CmcUpChannelBaseInfo cmcUpChannelBaseInfo : insertList) {
                session.insert(getNameSpace() + "insertCmcUpChannelBaseInfo", cmcUpChannelBaseInfo);
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
    public void batchInsertCmcPhyConfigs(List<CmcPhyConfig> cmcPhyConfigs, Long entityId) {
        SqlSession session = getBatchSession();
        try {
            for (CmcPhyConfig cmcPhyConfig : cmcPhyConfigs) {
                cmcPhyConfig.setCmcId(entityId);
                if (((Long) session.selectOne(getNameSpace() + "getCmcPhyConfig", cmcPhyConfig)) != null) {
                    session.update(getNameSpace() + "updateCmcPhyConfig", cmcPhyConfig);
                } else {
                    session.insert(getNameSpace() + "insertCmcPhyConfig", cmcPhyConfig);
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