/***********************************************************************
 * $Id: CmcAclDaoImpl.java,v1.0 2013-5-3 下午01:40:57 $
 * 
 * @author: lzs
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.downchannel.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.topvision.ems.cmc.downchannel.domain.*;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.cmc.domain.CmcDownChannelBaseShowInfo;
import com.topvision.ems.cmc.downchannel.dao.CmcDownChannelDao;
import com.topvision.ems.cmc.facade.domain.CmcDownChannelBaseInfo;
import com.topvision.ems.cmc.facade.domain.CmcDownChannelStaticInfo;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author lzs
 * @created @2013-5-3-下午01:40:57
 * 
 */
@Repository("cmcDownChannelDao")
public class CmcDownChannelDaoImpl extends MyBatisDaoSupport<Object> implements CmcDownChannelDao {
    @Override
    protected String getDomainName() {
        return "com.topvision.ems.cmc.downchannel.domain.CmcDownChannel";
    }

    @Override
    public List<CmcDownChannelBaseShowInfo> getDownChannelBaseShowInfoList(Long cmcId) {
        return getSqlSession().selectList(getNameSpace() + "getDownChannelBaseShowInfoList", cmcId);
    }

    @Override
    public CmcDownChannelBaseShowInfo getDownChannelBaseShowInfo(Long cmcPortId) {
        return (CmcDownChannelBaseShowInfo) getSqlSession().selectOne(getNameSpace() + "getDownChannelBaseShowInfo",
                cmcPortId);
    }

    @Override
    public void updateDownChannelBaseInfo(CmcDownChannelBaseInfo cmcDownChannelBaseInfo) {

        getSqlSession().update(getNameSpace() + "updateDownChannelBaseInfo", cmcDownChannelBaseInfo);
    }

    @Override
    public List<CmcDownChannelStaticInfo> getDownChannelStaticInfoList(Long cmcId) {
        return getSqlSession().selectList(getNameSpace() + "getDownChannelStaticInfoList", cmcId);
    }

    @Override
    public List<CmcDownChannelBaseShowInfo> getDownChannelListByPortId(Long cmcPortId) {
        return getSqlSession().selectList(getNameSpace() + "getDownChannelListByPortId", cmcPortId);
    }

    @Override
    public Long getDownChannelAdminStatusUpNum(Long cmcId) {
        return (Long) getSqlSession().selectOne(getNameSpace() + "getDownChannelAdminStatusUpNum", cmcId);
    }

    @Override
    public List<CmcDSIpqamBaseInfo> getDownChannelIPQAMInfoList(Long cmcId) {
        return getSqlSession().selectList(getNameSpace() + "getDSIPQAMInfoListByCmcId", cmcId);
    }

    @Override
    public CmcDSIpqamBaseInfo getDownChannelIPQAMInfo(Long cmcPortId) {
        return (CmcDSIpqamBaseInfo) getSqlSession().selectOne(getNameSpace() + "getDSIPQAMInfoByCmcPortId", cmcPortId);
    }

    @Override
    public Integer getDownChannelIPQAMListSize(Long cmcId) {
        return (Integer) getSqlSession().selectOne(getNameSpace() + "getDownChannelIPQAMListSize", cmcId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.downchannel.dao.CmcDownChannelDao#showIpqamOutputStreamInfoList(java
     * .lang.Long)
     */
    @Override
    public List<CmcDSIpqamOSInfo> showIpqamOutputStreamInfoList(Long cmcId) {
        return getSqlSession().selectList(getNameSpace() + "queryIpqamOutputStreamInfoListByCmcId", cmcId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.downchannel.dao.CmcDownChannelDao#queryCCIpqamOutPutStatusList(java
     * .lang.Long)
     */
    @Override
    public List<CmcDSIpqamStatusInfo> queryCCIpqamOutPutStatusList(Long cmcId) {
        return getSqlSession().selectList(getNameSpace() + "queryCCIpqamOutPutStatusListByCmcId", cmcId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.downchannel.dao.CmcDownChannelDao#queryIpqamStreamMappingsList(java
     * .lang.Long)
     */
    @Override
    public List<CmcDSIpqamMappings> queryIpqamStreamMappingsList(Long cmcId) {
        return getSqlSession().selectList(getNameSpace() + "queryIpqamStreamMappingsListByCmcId", cmcId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.downchannel.dao.CmcDownChannelDao#queryIpqamStreamMappingsById(java
     * .lang.Long)
     */
    @Override
    public CmcDSIpqamMappings queryIpqamStreamMappingsById(Long mappingId) {
        return (CmcDSIpqamMappings) getSqlSession().selectOne(getNameSpace() + "queryIpqamStreamMappingsById",
                mappingId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.downchannel.dao.CmcDownChannelDao#showIpqamInputStreamInfoList(java
     * .lang.Long)
     */
    @Override
    public List<CmcDSIpqamISInfo> showIpqamInputStreamInfoList(Long cmcId) {
        return getSqlSession().selectList(getNameSpace() + "queryIpqamInputStreamInfoListByCmcId", cmcId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.downchannel.dao.CmcDownChannelDao#getDownChannelOnListByCmcId(java.
     * lang.Long)
     */
    @Override
    public List<CmcDownChannelBaseShowInfo> getDownChannelOnListByCmcId(Long cmcId) {
        return getSqlSession().selectList(getNameSpace() + "getDownChannelOnListByCmcId", cmcId);
    }

    @Override
    public Long getPortId(Long cmcId, Long channelIndex) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cmcId", cmcId);
        map.put("channelIndex", channelIndex);
        return getSqlSession().selectOne(getNameSpace("getPortId"), map);
    }

    @Override
    public void batchInsertCC8800BDSIPQAMBaseList(final List<CmcDSIpqamBaseInfo> cc8800bHttpDownChannelIPQAMs,
            final Long cmcId) {
        SqlSession session = getBatchSession();
        try {
            session.delete(getNameSpace() + "deleteCC8800BIpqamBase", cmcId);
            for (CmcDSIpqamBaseInfo ipqam : cc8800bHttpDownChannelIPQAMs) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("cmcId", cmcId);
                map.put("docsIfDownChannelId", ipqam.getDocsIfDownChannelId());
                Long portId = (Long) session.selectOne(getNameSpace() + "queryCmcPortIdByCmcIdAndChannelId",
                        map);

                if (portId != null && portId > 0) {
                    ipqam.setCmcId(cmcId);
                    ipqam.setCmcPortId(portId);
                    session.insert(getNameSpace() + "insertCC8800BIpqamBase", ipqam);
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
    public void batchInsertCC8800BDSIPQAMStatusList(final List<CmcDSIpqamStatusInfo> cc8800bHttpDSIpqamStatusInfos,
            final Long cmcId) {
        SqlSession session = getBatchSession();
        try {
            session.delete(getNameSpace() + "deleteCC8800BIpqamStatus", cmcId);
            for (CmcDSIpqamStatusInfo ipqam : cc8800bHttpDSIpqamStatusInfos) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("cmcId", cmcId);
                map.put("docsIfDownChannelId", ipqam.getIpqamOutputQAMChannel());
                Long portId = (Long) getSqlSession().selectOne(getNameSpace() + "queryCmcPortIdByCmcIdAndChannelId",
                        map);

                if (portId != null && portId > 0) {
                    ipqam.setCmcId(cmcId);
                    ipqam.setCmcPortId(portId);
                    session.insert(getNameSpace() + "insertCC8800BIpqamStatus", ipqam);
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
    public void batchInsertCC8800BDSIPQAMMappingsList(final List<CmcDSIpqamMappings> cc8800bHttpDSIpqamMappings,
            final Long cmcId) {
        SqlSession session = getBatchSession();
        try {
            session.delete(getNameSpace() + "deleteCC8800BIpqamMappings", cmcId);
            for (CmcDSIpqamMappings ipqam : cc8800bHttpDSIpqamMappings) {
                ipqam.setCmcId(cmcId);
                session.insert(getNameSpace() + "insertCC8800BIpqamMappings", ipqam);
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
    public void batchInsertCC8800BDSIPQAMISInfoList(final List<CmcDSIpqamISInfo> cc8800bHttpIpQamISInfos,
            final Long cmcId) {
        SqlSession session = getBatchSession();
        try {
            session.delete(getNameSpace() + "deleteCC8800BIpqamIsInfo", cmcId);
            for (CmcDSIpqamISInfo ipqam : cc8800bHttpIpQamISInfos) {
                ipqam.setCmcId(cmcId);
                session.insert(getNameSpace() + "insertCC8800BIpqamIsInfo", ipqam);
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
    public void batchInsertCC8800BDSIPQAMOsInfoList(final List<CmcDSIpqamOSInfo> cc8800bHttpIpQamOSInfos,
            final Long cmcId) {
        SqlSession session = getBatchSession();
        try {
            session.delete(getNameSpace() + "deleteCC8800BIpqamOsInfo", cmcId);
            for (CmcDSIpqamOSInfo ipqam : cc8800bHttpIpQamOSInfos) {
                ipqam.setCmcId(cmcId);
                session.insert(getNameSpace() + "insertCC8800BIpqamOsInfo", ipqam);
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
    public void insertorUpdateCC8800BIPQAMIpInfo(CmcIpqamInfo cmcIpqamInfo, Long cmcId) {
        SqlSession sqlSession = getSqlSession();
        cmcIpqamInfo.setCmcId(cmcId);
        CmcIpqamInfo cmcInfo = sqlSession.selectOne(getNameSpace() + "selectIpqamIpInfo", cmcId);
        if (cmcInfo == null) {
            sqlSession.insert(getNameSpace() + "insertIqamIPInfo", cmcIpqamInfo);
        } else {
            sqlSession.update(getNameSpace() + "updateIpqamIPInfo", cmcIpqamInfo);
        }
    }

    @Override
    public CmcIpqamInfo queryforCC8800BIPQAMIpInfo(Long cmcId) {
        SqlSession sqlSession = getSqlSession();
        CmcIpqamInfo cmcInfo = sqlSession.selectOne(getNameSpace() + "selectIpqamIpInfo", cmcId);
        return cmcInfo;
    }

    @Override
    public void batchInsertCmcDownChannelStaticInfo(final List<CmcDownChannelStaticInfo> cmcDownChannelStaticInfoList,
            final Long cmcId) {
        SqlSession session = getBatchSession();
        try {
            for (CmcDownChannelStaticInfo cmcDownChannelStaticInfo : cmcDownChannelStaticInfoList) {
                Map<String, Long> map = new HashMap<String, Long>();
                map.put("cmcId", cmcId);
                map.put("channelIndex", cmcDownChannelStaticInfo.getChannelIndex());
                Long cmcPortId = (Long) getSqlSession().selectOne(getNameSpace() + "getCmcPortIdByIfIndexAndCmcId", map);;
                cmcDownChannelStaticInfo.setCmcId(cmcId);
                cmcDownChannelStaticInfo.setCmcPortId(cmcPortId);
                if (((Integer) getSqlSession().selectOne(getNameSpace() + "getCmcDownChannelStaticInfo",
                        cmcDownChannelStaticInfo)) != 0) {
                    session.update(getNameSpace() + "updateCmcDownChannelStaticInfo", cmcDownChannelStaticInfo);
                } else {
                    session.insert(getNameSpace() + "insertCmcDownChannelStaticInfo", cmcDownChannelStaticInfo);
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
    public void batchRefreshCmcTxPowerLimit(List<TxPowerLimit> list, long entityId, Long cmcIndex) {
        Map<String,Long> map = new HashMap<>();
        map.put("entityId",entityId);
        map.put("cmcIndex",cmcIndex);
        getSqlSession().delete(getNameSpace() + "deleteCmcTxPowerLimit", map);
        SqlSession session = getBatchSession();
        try {
            for (TxPowerLimit txPowerLimit : list) {
                txPowerLimit.setEntityId(entityId);
                session.insert(getNameSpace() + "insertCmcTxPowerLimit", txPowerLimit);
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
    public List<TxPowerLimit> getTxPowerLimit(Long entityId,Long cmcIndex) {
        Map<String,Long> map = new HashMap<>();
        map.put("entityId",entityId);
        map.put("cmcIndex",cmcIndex);
        List<TxPowerLimit> txPowerLimits = getSqlSession().selectList(getNameSpace() + "getTxPowerLimit", map);
        return txPowerLimits;
    }

}
