/***********************************************************************
 * $Id: OltIgmpConfigDaoImpl.java,v1.0 2016-6-6 下午4:20:19 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.igmpconfig.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.igmpconfig.dao.OltIgmpConfigDao;
import com.topvision.ems.epon.igmpconfig.domain.IgmpCascadePort;
import com.topvision.ems.epon.igmpconfig.domain.IgmpControlGroupBindRelation;
import com.topvision.ems.epon.igmpconfig.domain.IgmpCtcParam;
import com.topvision.ems.epon.igmpconfig.domain.IgmpCtcProfile;
import com.topvision.ems.epon.igmpconfig.domain.IgmpCtcProfileGroupRela;
import com.topvision.ems.epon.igmpconfig.domain.IgmpCtcRecord;
import com.topvision.ems.epon.igmpconfig.domain.IgmpGlobalGroup;
import com.topvision.ems.epon.igmpconfig.domain.IgmpGlobalParam;
import com.topvision.ems.epon.igmpconfig.domain.IgmpPortInfo;
import com.topvision.ems.epon.igmpconfig.domain.IgmpSnpStaticFwd;
import com.topvision.ems.epon.igmpconfig.domain.IgmpSnpUplinkPort;
import com.topvision.ems.epon.igmpconfig.domain.IgmpVlanGroup;
import com.topvision.ems.epon.igmpconfig.domain.IgmpVlanInfo;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author flack
 * @created @2016-6-6-下午4:20:19
 *
 */
@Repository("oltIgmpConfigDao")
public class OltIgmpConfigDaoImpl extends MyBatisDaoSupport<Object> implements OltIgmpConfigDao {

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.epon.igmpconfig.domain.IgmpGlobalParam";
    }

    @Override
    public void insertOrUpdateGlobalParam(IgmpGlobalParam globalParam) {
        getSqlSession().insert(getNameSpace("insertOrUpdateGlobalParam"), globalParam);
    }

    @Override
    public IgmpGlobalParam queryGloablParam(Long entityId) {
        return getSqlSession().selectOne(getNameSpace("queryGloablParam"), entityId);
    }

    @Override
    public Integer queryIgmpMode(Long entityId) {
        return getSqlSession().selectOne(getNameSpace("queryIgmpMode"), entityId);
    }

    @Override
    public void updateIgmpMode(Long entityId, Integer igmpMode) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("igmpMode", igmpMode);
        getSqlSession().update(getNameSpace("updateIgmpMode"), paramMap);
    }

    @Override
    public void updateGlobalParam(IgmpGlobalParam globalParam) {
        getSqlSession().update(getNameSpace("updateGlobalParam"), globalParam);
    }

    @Override
    public void insertCascadePort(IgmpCascadePort cascadePort) {
        getSqlSession().insert(getNameSpace("insertCascadePort"), cascadePort);
    }

    @Override
    public void batchInsertCascadePort(Long entityId, List<IgmpCascadePort> cascadePortList) {
        SqlSession sqlSession = getBatchSession();
        try {
            // 删除该设备下所有级联端口
            sqlSession.delete(getNameSpace("deleteAllCascadePort"), entityId);
            for (IgmpCascadePort cascadePort : cascadePortList) {
                cascadePort.setEntityId(entityId);
                sqlSession.insert(getNameSpace("insertCascadePort"), cascadePort);
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
    public List<IgmpCascadePort> queryCascadePortList(Long entityId) {
        return getSqlSession().selectList(getNameSpace("queryCascadePortList"), entityId);
    }

    @Override
    public void deleteCascadePort(IgmpCascadePort cascadePort) {
        getSqlSession().delete(getNameSpace("deleteCascadePort"), cascadePort);
    }

    @Override
    public void batchDeleteCascadePort(List<IgmpCascadePort> cascadePorts) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (IgmpCascadePort igmpCascadePort : cascadePorts) {
                sqlSession.delete(getNameSpace("deleteCascadePort"), igmpCascadePort);
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
    public void insertOrUpdateSnpUpLinkPort(IgmpSnpUplinkPort uplinkPort) {
        getSqlSession().insert(getNameSpace("insertOrUpdateSnpUpLinkPort"), uplinkPort);
    }

    @Override
    public void batchInsertSnpUplinkPort(List<IgmpSnpUplinkPort> uplinkPortList) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (IgmpSnpUplinkPort uplinkPort : uplinkPortList) {
                sqlSession.insert(getNameSpace("insertSnpUpLinkPort"), uplinkPort);
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
    public IgmpSnpUplinkPort querySnpUplinkPort(Long entityId) {
        return getSqlSession().selectOne(getNameSpace("querySnpUplinkPort"), entityId);
    }

    @Override
    public void updateSnpUplinkPort(IgmpSnpUplinkPort uplinkPort) {
        getSqlSession().update(getNameSpace("updateSnpUplinkPort"), uplinkPort);
    }

    @Override
    public void insertSnpStaticFwd(IgmpSnpStaticFwd staticFwd) {
        getSqlSession().insert(getNameSpace("insertSnpStaticFwd"), staticFwd);
    }

    @Override
    public void batchInsertSnpStaticFwd(Long entityId, List<IgmpSnpStaticFwd> staticFwdList) {
        SqlSession sqlSession = getBatchSession();
        try {
            // 删除该设备对应的静态加入配置
            sqlSession.delete(getNameSpace("deleteAllSnpStaticFwd"), entityId);
            for (IgmpSnpStaticFwd staticFwd : staticFwdList) {
                staticFwd.setEntityId(entityId);
                sqlSession.insert(getNameSpace("insertSnpStaticFwd"), staticFwd);
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
    public List<IgmpSnpStaticFwd> querySnpStaticFwdList(Map<String, Object> paramMap) {
        return getSqlSession().selectList(getNameSpace("querySnpStaticFwdList"), paramMap);
    }

    @Override
    public Integer querySnpStaticFwdNum(Map<String, Object> paramMap) {
        return getSqlSession().selectOne(getNameSpace("querySnpStaticFwdNum"), paramMap);
    }

    @Override
    public List<IgmpSnpStaticFwd> queryAllStaticFwdList(Long entityId) {
        return getSqlSession().selectList(getNameSpace("queryAllStaticFwdList"), entityId);
    }

    @Override
    public void deleteSnpStaticFwd(IgmpSnpStaticFwd staticFwd) {
        getSqlSession().delete(getNameSpace("deleteSnpStaticFwd"), staticFwd);
    }

    @Override
    public void batchDeleteSnpStaticFwd(List<IgmpSnpStaticFwd> staticFwds) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (IgmpSnpStaticFwd staticFwd : staticFwds) {
                sqlSession.delete(getNameSpace("deleteSnpStaticFwd"), staticFwd);
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
    public void insertVlanInfo(IgmpVlanInfo vlanInfo) {
        getSqlSession().insert(getNameSpace("insertVlanInfo"), vlanInfo);
    }

    @Override
    public void batchInsertVlanInfo(Long entityId, List<IgmpVlanInfo> vlanInfoList) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace("deleteAllVlanInfo"), entityId);
            for (IgmpVlanInfo vlanInfo : vlanInfoList) {
                vlanInfo.setEntityId(entityId);
                sqlSession.insert(getNameSpace("insertVlanInfo"), vlanInfo);
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
    public List<IgmpVlanInfo> queryVlanInfoList(Long entityId) {
        return getSqlSession().selectList(getNameSpace("queryVlanInfoList"), entityId);
    }

    @Override
    public void updateVlanInfo(IgmpVlanInfo vlanInfo) {
        getSqlSession().insert(getNameSpace("updateVlanInfo"), vlanInfo);
    }

    @Override
    public void deleteVlanInfo(Long entityId, Integer vlanId) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("vlanId", vlanId);
        getSqlSession().delete(getNameSpace("deleteVlanInfo"), paramMap);
    }

    @Override
    public void batchDeleteVlanInfo(Long entityId, List<Integer> vlanIds) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (Integer vlanId : vlanIds) {
                Map<String, Object> paramMap = new HashMap<String, Object>();
                paramMap.put("entityId", entityId);
                paramMap.put("vlanId", vlanId);
                sqlSession.delete(getNameSpace("deleteVlanInfo"), paramMap);
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
    public void insertVlanGroup(IgmpVlanGroup groupInfo) {
        getSqlSession().insert(getNameSpace("insertVlanGroup"), groupInfo);
    }

    @Override
    public void batchInsertVlanGroup(Long entityId, List<IgmpVlanGroup> groupList) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace("deleteAllVlanGroup"), entityId);
            for (IgmpVlanGroup groupInfo : groupList) {
                groupInfo.setEntityId(entityId);
                sqlSession.insert(getNameSpace("insertVlanGroup"), groupInfo);
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
    public List<IgmpVlanGroup> queryVlanGroupList(Long entityId, Integer vlanId) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("vlanId", vlanId);
        return getSqlSession().selectList(getNameSpace("queryVlanGroupList"), paramMap);
    }

    @Override
    public List<IgmpVlanGroup> queryAllVlanGroup(Long entityId) {
        return getSqlSession().selectList(getNameSpace("queryAllVlanGroup"), entityId);
    }

    @Override
    public List<IgmpVlanGroup> queryVlanGroupList(Map<String, Object> paramMap) {
        return getSqlSession().selectList(getNameSpace("queryGroupWithPage"), paramMap);
    }

    @Override
    public Integer queryVlanGroupNum(Map<String, Object> paramMap) {
        return getSqlSession().selectOne(getNameSpace("queryGroupNum"), paramMap);
    }

    @Override
    public List<Integer> queryGroupIdList(Long entityId) {
        return getSqlSession().selectList(getNameSpace("queryGroupIdList"), entityId);
    }

    @Override
    public List<IgmpVlanGroup> queryWithSrcGroup(Long entityId) {
        return getSqlSession().selectList(getNameSpace("queryWithSrcGroup"), entityId);
    }

    @Override
    public List<IgmpVlanGroup> queryWithoutSrcGroup(Long entityId) {
        return getSqlSession().selectList(getNameSpace("queryWithoutSrcGroup"), entityId);
    }

    @Override
    public void updateVlanGroup(IgmpVlanGroup groupInfo) {
        getSqlSession().update(getNameSpace("updateVlanGroup"), groupInfo);
    }

    @Override
    public void updateGroupPreJoin(IgmpVlanGroup groupInfo) {
        getSqlSession().update(getNameSpace("updateGroupPreJoin"), groupInfo);
    }

    @Override
    public void deleteVlanGroup(Long entityId, Integer groulId) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("groupId", groulId);
        getSqlSession().delete(getNameSpace("deleteVlanGroup"), paramMap);
    }

    @Override
    public void batchDeleteVlanGroup(List<IgmpVlanGroup> igmpVlanGroups) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (IgmpVlanGroup igmpVlanGroup : igmpVlanGroups) {
                Map<String, Object> paramMap = new HashMap<String, Object>();
                paramMap.put("entityId", igmpVlanGroup.getEntityId());
                paramMap.put("groupId", igmpVlanGroup.getGroupId());
                sqlSession.delete(getNameSpace("deleteVlanGroup"), paramMap);
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
    public void batchInsertGlobalGroup(Long entityId, List<IgmpGlobalGroup> groupList) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace("deleteAllGlobalGroup"), entityId);
            for (IgmpGlobalGroup groupInfo : groupList) {
                groupInfo.setEntityId(entityId);
                sqlSession.insert(getNameSpace("insertGlobalGroup"), groupInfo);
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
    public List<IgmpGlobalGroup> queryGlobalGroupList(Map<String, Object> paramMap) {
        return getSqlSession().selectList(getNameSpace("queryGlobalGroupList"), paramMap);
    }

    @Override
    public Integer queryGlobalGroupNum(Map<String, Object> paramMap) {
        return getSqlSession().selectOne(getNameSpace("queryGlobalGroupNum"), paramMap);
    }

    @Override
    public void insertOrUpdateCtcParam(IgmpCtcParam ctcParam) {
        getSqlSession().insert(getNameSpace("insertOrUpdateCtcParam"), ctcParam);
    }

    @Override
    public IgmpCtcParam queryCtcParam(Long entityId) {
        return getSqlSession().selectOne(getNameSpace("queryCtcParam"), entityId);
    }

    @Override
    public void updateCtcParam(IgmpCtcParam ctcParam) {
        getSqlSession().update(getNameSpace("updateCtcParam"), ctcParam);
    }

    @Override
    public void insertCtcProfile(IgmpCtcProfile ctcProfile) {
        getSqlSession().insert(getNameSpace("insertCtcProfile"), ctcProfile);
    }

    @Override
    public void batchInsertCtcProfile(Long entityId, List<IgmpCtcProfile> profileList) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace("deleteAllCtcProfile"), entityId);
            for (IgmpCtcProfile ctcProfile : profileList) {
                ctcProfile.setEntityId(entityId);
                sqlSession.insert(getNameSpace("insertCtcProfile"), ctcProfile);
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
    public IgmpCtcProfile queryCtcProfile(Long entityId, Integer profileId) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("profileId", profileId);
        return getSqlSession().selectOne(getNameSpace("queryCtcProfile"), paramMap);
    }

    @Override
    public List<IgmpCtcProfile> queryCtcProfileList(Map<String, Object> paramMap) {
        return getSqlSession().selectList(getNameSpace("queryCtcProfileList"), paramMap);
    }

    @Override
    public Integer queryCtcProfileNum(Map<String, Object> paramMap) {
        return getSqlSession().selectOne(getNameSpace("queryCtcProfileNum"), paramMap);
    }

    @Override
    public List<IgmpCtcProfile> queryAllCtcProfile(Long entityId) {
        return getSqlSession().selectList(getNameSpace("queryAllCtcProfile"), entityId);
    }

    @Override
    public List<Integer> queryProfileIdList(Long entityId) {
        return getSqlSession().selectList(getNameSpace("queryProfileIdList"), entityId);
    }

    @Override
    public void updateCtcProfile(IgmpCtcProfile ctcProfile) {
        getSqlSession().update(getNameSpace("updateCtcProfile"), ctcProfile);
    }

    @Override
    public void deleteCtcProfile(Long entityId, Integer profileId) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("profileId", profileId);
        getSqlSession().delete(getNameSpace("deleteCtcProfile"), paramMap);
    }

    @Override
    public void batchDeleteCtcProfile(Long entityId, List<Integer> profileIds) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (Integer profileId : profileIds) {
                Map<String, Object> paramMap = new HashMap<String, Object>();
                paramMap.put("entityId", entityId);
                paramMap.put("profileId", profileId);
                sqlSession.delete(getNameSpace("deleteCtcProfile"), paramMap);
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
    public void insertProfileGroupRela(IgmpCtcProfileGroupRela profileGroup) {
        getSqlSession().insert(getNameSpace("insertProfileGroupRela"), profileGroup);
    }

    @Override
    public void batchInsertProfileGroupRela(Long entityId, List<IgmpCtcProfileGroupRela> profileGroupList) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace("deleteAllProfileGroupRela"), entityId);
            for (IgmpCtcProfileGroupRela profileGroup : profileGroupList) {
                profileGroup.setEntityId(entityId);
                sqlSession.insert(getNameSpace("insertProfileGroupRela"), profileGroup);
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
    public List<IgmpControlGroupBindRelation> queryProfileGroupRelaList(Long entityId, Integer profileId) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("profileId", profileId);
        return getSqlSession().selectList(getNameSpace("queryProfileGroupRelaList"), paramMap);
    }

    @Override
    public List<IgmpCtcProfileGroupRela> queryAllProfileGroupRela(Long entityId) {
        return getSqlSession().selectList(getNameSpace("queryAllProfileGroupRela"), entityId);
    }

    @Override
    public List<IgmpCtcProfileGroupRela> queryWithSrcGroupRela(Long entityId) {
        return getSqlSession().selectList(getNameSpace("queryWithSrcGroupRela"), entityId);
    }

    @Override
    public List<IgmpCtcProfileGroupRela> queryWithoutSrcGroupRela(Long entityId) {
        return getSqlSession().selectList(getNameSpace("queryWithoutSrcGroupRela"), entityId);
    }

    @Override
    public void deleteProfileGroupRela(IgmpCtcProfileGroupRela profileGroup) {
        getSqlSession().delete(getNameSpace("deleteProfileGroupRela"), profileGroup);
    }

    @Override
    public void insertCtcRecord(IgmpCtcRecord ctcRecord) {
        getSqlSession().insert(getNameSpace("insertCtcRecord"), ctcRecord);
    }

    @Override
    public void batchInserCtcRecord(Long entityId, List<IgmpCtcRecord> recordList) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace("deleteAllCtcRecord"), entityId);
            for (IgmpCtcRecord igmpRecord : recordList) {
                igmpRecord.setEntityId(entityId);
                sqlSession.insert(getNameSpace("insertCtcRecord"), igmpRecord);
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
    public List<IgmpCtcRecord> queryCtcRecordList(Map<String, Object> paramsMap) {
        return getSqlSession().selectList(getNameSpace("queryCtcRecordList"), paramsMap);
    }

    @Override
    public Integer queryCtcRecordNum(Map<String, Object> paramsMap) {
        return getSqlSession().selectOne(getNameSpace("queryCtcRecordNum"), paramsMap);
    }

    @Override
    public List<IgmpPortInfo> querySniPortByType(Long entityId, List<Integer> typeList) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("typeList", typeList);
        return getSqlSession().selectList(getNameSpace("querySniPortByType"), paramMap);
    }

    @Override
    public List<IgmpPortInfo> querySniPort(Long entityId) {
        return getSqlSession().selectList(getNameSpace("querySniPort"), entityId);
    }

    @Override
    public List<IgmpPortInfo> querySniAggList(Long entityId) {
        return getSqlSession().selectList(getNameSpace("querySniAggList"), entityId);
    }

    @Override
    public List<IgmpPortInfo> queryPonPortByType(Long entityId, Integer portType) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("portType", portType);
        return getSqlSession().selectList(getNameSpace("queryPonPortByType"), paramMap);
    }

    @Override
    public List<IgmpCascadePort> queryCascadePortByType(Long entityId, Integer portType) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("portType", portType);
        return getSqlSession().selectList(getNameSpace("queryCascadePortByType"), paramMap);
    }

    @Override
    public void insertOrUpdateGroupName(IgmpVlanGroup groupInfo) {
        getSqlSession().update(getNameSpace("insertOrUpdateGroupName"), groupInfo);
    }

    @Override
    public void deleteGroupName(IgmpVlanGroup groupInfo) {
        getSqlSession().delete(getNameSpace("deleteGroupName"), groupInfo);
    }

    @Override
    public void batchDeleteGroupName(List<IgmpVlanGroup> groupInfos) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (IgmpVlanGroup igmpVlanGroup : groupInfos) {
                sqlSession.delete(getNameSpace("deleteGroupName"), igmpVlanGroup);
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
    public void insertProfileName(IgmpCtcProfile ctcProfile) {
        getSqlSession().insert(getNameSpace("insertProfileName"), ctcProfile);
    }

    @Override
    public void insertOrUpdateProfileName(IgmpCtcProfile ctcProfile) {
        getSqlSession().insert(getNameSpace("insertOrUpdateProfileName"), ctcProfile);
    }

    @Override
    public void deleteProfileName(Long entityId, Integer profileId) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("profileId", profileId);
        getSqlSession().delete(getNameSpace("deleteProfileName"), paramMap);
    }

    @Override
    public void batchDeleteProfileName(Long entityId, List<Integer> profileIds) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (Integer profileId : profileIds) {
                Map<String, Object> paramMap = new HashMap<String, Object>();
                paramMap.put("entityId", entityId);
                paramMap.put("profileId", profileId);
                sqlSession.delete(getNameSpace("deleteProfileName"), paramMap);
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }
}
