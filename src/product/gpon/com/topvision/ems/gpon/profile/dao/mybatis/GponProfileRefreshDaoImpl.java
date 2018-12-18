/***********************************************************************
 * $Id: GponProfileRefreshDaoImpl.java,v1.0 2016年12月17日 下午2:16:08 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.profile.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.gpon.profile.dao.GponProfileRefreshDao;
import com.topvision.ems.gpon.profile.facade.domain.GponDbaProfileInfo;
import com.topvision.ems.gpon.profile.facade.domain.GponLineProfileGem;
import com.topvision.ems.gpon.profile.facade.domain.GponLineProfileGemMap;
import com.topvision.ems.gpon.profile.facade.domain.GponLineProfileInfo;
import com.topvision.ems.gpon.profile.facade.domain.GponLineProfileTcont;
import com.topvision.ems.gpon.profile.facade.domain.GponSrvProfileCfg;
import com.topvision.ems.gpon.profile.facade.domain.GponSrvProfileEthPortConfig;
import com.topvision.ems.gpon.profile.facade.domain.GponSrvProfileInfo;
import com.topvision.ems.gpon.profile.facade.domain.GponSrvProfilePortNumProfile;
import com.topvision.ems.gpon.profile.facade.domain.GponSrvProfilePortVlanAggregation;
import com.topvision.ems.gpon.profile.facade.domain.GponSrvProfilePortVlanCfg;
import com.topvision.ems.gpon.profile.facade.domain.GponSrvProfilePortVlanTranslation;
import com.topvision.ems.gpon.profile.facade.domain.GponSrvProfilePortVlanTrunk;
import com.topvision.ems.gpon.profile.facade.domain.GponTrafficProfileInfo;
import com.topvision.ems.gpon.profile.facade.domain.TopDigitMapProfInfo;
import com.topvision.ems.gpon.profile.facade.domain.TopGponSrvPotsInfo;
import com.topvision.ems.gpon.profile.facade.domain.TopGponSrvProfile;
import com.topvision.ems.gpon.profile.facade.domain.TopSIPAgentProfInfo;
import com.topvision.ems.gpon.profile.facade.domain.TopSIPSrvProfInfo;
import com.topvision.ems.gpon.profile.facade.domain.TopVoipMediaProfInfo;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author haojie
 * @created @2016年12月17日-下午2:16:08
 *
 */
@Repository("gponProfileRefreshDao")
public class GponProfileRefreshDaoImpl extends MyBatisDaoSupport<Object> implements GponProfileRefreshDao {

    @Override
    protected String getDomainName() {
        return "GponProfileRefresh";
    }

    @Override
    public void insertGemProfiles(List<GponLineProfileGem> profiles, Long entityId) {
        SqlSession session = getBatchSession();
        try {
            session.delete(getNameSpace("deleteGponLineProfileGems"), entityId);
            for (GponLineProfileGem profile : profiles) {
                profile.setEntityId(entityId);
                session.insert(getNameSpace("insertGponLineProfileGem"), profile);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void insertDBAProfiles(List<GponDbaProfileInfo> profiles, Long entityId) {
        SqlSession session = getBatchSession();
        try {
            session.delete(getNameSpace("deleteGponDbaProfileInfos"), entityId);
            for (GponDbaProfileInfo profile : profiles) {
                profile.setEntityId(entityId);
                session.insert(getNameSpace("insertGponDbaProfileInfo"), profile);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void insertGemMapProfiles(List<GponLineProfileGemMap> profiles, Long entityId) {
        SqlSession session = getBatchSession();
        try {
            session.delete(getNameSpace("deleteGponLineProfileGemMaps"), entityId);
            for (GponLineProfileGemMap profile : profiles) {
                profile.setEntityId(entityId);
                session.insert(getNameSpace("insertGponLineProfileGemMap"), profile);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void insertPortVlanProfiles(List<GponSrvProfilePortVlanCfg> profiles, Long entityId) {
        SqlSession session = getBatchSession();
        try {
            session.delete(getNameSpace("deleteGponSrvProfilePortVlanCfgs"), entityId);
            for (GponSrvProfilePortVlanCfg profile : profiles) {
                profile.setEntityId(entityId);
                session.insert(getNameSpace("insertGponSrvProfilePortVlanCfg"), profile);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void insertTrafficProfiles(List<GponTrafficProfileInfo> profiles, Long entityId) {
        SqlSession session = getBatchSession();
        try {
            session.delete(getNameSpace("deleteGponTrafficProfileInfos"), entityId);
            for (GponTrafficProfileInfo profile : profiles) {
                profile.setEntityId(entityId);
                session.insert(getNameSpace("insertGponTrafficProfileInfo"), profile);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void insertVlanTranslationProfiles(List<GponSrvProfilePortVlanTranslation> profiles, Long entityId) {
        SqlSession session = getBatchSession();
        try {
            session.delete(getNameSpace("deleteGponSrvProfilePortVlanTranslations"), entityId);
            for (GponSrvProfilePortVlanTranslation profile : profiles) {
                profile.setEntityId(entityId);
                session.insert(getNameSpace("insertGponSrvProfilePortVlanTranslation"), profile);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void insertVlanTrunkProfiles(List<GponSrvProfilePortVlanTrunk> profiles, Long entityId) {
        SqlSession session = getBatchSession();
        try {
            session.delete(getNameSpace("deleteGponSrvProfilePortVlanTrunks"), entityId);
            for (GponSrvProfilePortVlanTrunk profile : profiles) {
                profile.setEntityId(entityId);
                session.insert(getNameSpace("insertGponSrvProfilePortVlanTrunk"), profile);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void insertVlanAggrProfiles(List<GponSrvProfilePortVlanAggregation> profiles, Long entityId) {
        SqlSession session = getBatchSession();
        try {
            session.delete(getNameSpace("deleteGponSrvProfilePortVlanAggregations"), entityId);
            for (GponSrvProfilePortVlanAggregation profile : profiles) {
                profile.setEntityId(entityId);
                session.insert(getNameSpace("insertGponSrvProfilePortVlanAggregation"), profile);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void insertLineProfiles(List<GponLineProfileInfo> profiles, Long entityId) {
        SqlSession session = getBatchSession();
        try {
            session.delete(getNameSpace("deleteGponLineProfileInfos"), entityId);
            for (GponLineProfileInfo profile : profiles) {
                profile.setEntityId(entityId);
                session.insert(getNameSpace("insertGponLineProfileInfo"), profile);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void insertTcontProfiles(List<GponLineProfileTcont> profiles, Long entityId) {
        SqlSession session = getBatchSession();
        try {
            session.delete(getNameSpace("deleteGponLineProfileTconts"), entityId);
            for (GponLineProfileTcont profile : profiles) {
                profile.setEntityId(entityId);
                session.insert(getNameSpace("insertGponLineProfileTcont"), profile);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void insertSrvEthProfiles(List<GponSrvProfileEthPortConfig> profiles, Long entityId) {
        SqlSession session = getBatchSession();
        try {
            session.delete(getNameSpace("deleteGponSrvProfileEthPortConfigs"), entityId);
            for (GponSrvProfileEthPortConfig profile : profiles) {
                profile.setEntityId(entityId);
                session.insert(getNameSpace("insertGponSrvProfileEthPortConfig"), profile);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void insertSrvPortNumProfiles(List<GponSrvProfilePortNumProfile> profiles, Long entityId) {
        SqlSession session = getBatchSession();
        try {
            session.delete(getNameSpace("deleteGponSrvProfilePortNumProfiles"), entityId);
            for (GponSrvProfilePortNumProfile profile : profiles) {
                profile.setEntityId(entityId);
                session.insert(getNameSpace("insertGponSrvProfilePortNumProfile"), profile);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void insertSrvProfileInfos(List<GponSrvProfileInfo> profiles, Long entityId) {
        SqlSession session = getBatchSession();
        try {
            session.delete(getNameSpace("deleteGponSrvProfileInfos"), entityId);
            for (GponSrvProfileInfo profile : profiles) {
                profile.setEntityId(entityId);
                session.insert(getNameSpace("insertGponSrvProfileInfo"), profile);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void insertSrvProfileCfgs(List<GponSrvProfileCfg> profiles, Long entityId) {
        SqlSession session = getBatchSession();
        try {
            session.delete(getNameSpace("deleteGponSrvProfileCfgs"), entityId);
            for (GponSrvProfileCfg profile : profiles) {
                profile.setEntityId(entityId);
                session.insert(getNameSpace("insertGponSrvProfileCfg"), profile);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void insertTcontProfiles(List<GponLineProfileTcont> tconts, Long entityId, Integer profileIndex) {
        SqlSession session = getBatchSession();
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("entityId", entityId);
            map.put("profileIndex", profileIndex);
            session.delete(getNameSpace("deleteTcontsInProfile"), map);
            for (GponLineProfileTcont profile : tconts) {
                profile.setEntityId(entityId);
                session.insert(getNameSpace("insertGponLineProfileTcont"), profile);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void insertGemProfiles(List<GponLineProfileGem> gems, Long entityId, Integer profileIndex) {
        SqlSession session = getBatchSession();
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("entityId", entityId);
            map.put("profileIndex", profileIndex);
            session.delete(getNameSpace("deleteGemsInProfile"), map);
            for (GponLineProfileGem profile : gems) {
                profile.setEntityId(entityId);
                session.insert(getNameSpace("insertGponLineProfileGem"), profile);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void insertGemMapProfiles(List<GponLineProfileGemMap> gemMaps, Long entityId, Integer profileIndex,
            Integer gemIndex) {
        SqlSession session = getBatchSession();
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("entityId", entityId);
            map.put("profileIndex", profileIndex);
            map.put("gemIndex", gemIndex);
            session.delete(getNameSpace("deleteGemMapsInGem"), map);
            for (GponLineProfileGemMap profile : gemMaps) {
                profile.setEntityId(entityId);
                session.insert(getNameSpace("insertGponLineProfileGemMap"), profile);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void insertEthPortConfigs(List<GponSrvProfileEthPortConfig> ethPortConfigs, Long entityId,
            Integer profileIndex) {
        SqlSession session = getBatchSession();
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("entityId", entityId);
            map.put("profileIndex", profileIndex);
            session.delete(getNameSpace("deleteEthPortConfigInProfile"), map);
            for (GponSrvProfileEthPortConfig profile : ethPortConfigs) {
                profile.setEntityId(entityId);
                session.insert(getNameSpace("insertGponSrvProfileEthPortConfig"), profile);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void insertPortVlanCfgs(List<GponSrvProfilePortVlanCfg> portVlanCfgs, Long entityId, Integer profileIndex) {
        SqlSession session = getBatchSession();
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("entityId", entityId);
            map.put("profileIndex", profileIndex);
            session.delete(getNameSpace("deletePortVlanCfgInProfile"), map);
            for (GponSrvProfilePortVlanCfg profile : portVlanCfgs) {
                profile.setEntityId(entityId);
                session.insert(getNameSpace("insertGponSrvProfilePortVlanCfg"), profile);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void insertTopGponSrvProfiles(List<TopGponSrvProfile> topGponSrvProfile, Long entityId) {
        SqlSession session = getBatchSession();
        try {
            session.delete(getNameSpace("deleteTopGponSrvProfiles"), entityId);
            for (TopGponSrvProfile profile : topGponSrvProfile) {
                profile.setEntityId(entityId);
                session.insert(getNameSpace("insertTopGponSrvProfile"), profile);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void insertTopSIPAgentProfInfos(List<TopSIPAgentProfInfo> topSIPAgentProfInfo, Long entityId) {
        SqlSession session = getBatchSession();
        try {
            session.delete(getNameSpace("deleteTopSIPAgentProfInfos"), entityId);
            for (TopSIPAgentProfInfo profile : topSIPAgentProfInfo) {
                profile.setEntityId(entityId);
                session.insert(getNameSpace("insertTopSIPAgentProfInfo"), profile);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void insertTopGponSrvPotsInfos(List<TopGponSrvPotsInfo> potsTable, Long entityId) {
        SqlSession session = getBatchSession();
        try {
            session.delete(getNameSpace("deleteTopGponSrvPotsInfos"), entityId);
            for (TopGponSrvPotsInfo profile : potsTable) {
                profile.setEntityId(entityId);
                session.insert(getNameSpace("insertTopGponSrvPotsInfo"), profile);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void insertTopVoipMediaProfInfos(List<TopVoipMediaProfInfo> mediaTable, Long entityId) {
        SqlSession session = getBatchSession();
        try {
            session.delete(getNameSpace("deleteTopVoipMediaProfInfos"), entityId);
            for (TopVoipMediaProfInfo profile : mediaTable) {
                profile.setEntityId(entityId);
                session.insert(getNameSpace("insertTopVoipMediaProfInfo"), profile);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void insertTopSIPSrvProfInfos(List<TopSIPSrvProfInfo> sipSrvTable, Long entityId) {
        SqlSession session = getBatchSession();
        try {
            session.delete(getNameSpace("deleteTopSIPSrvProfInfos"), entityId);
            for (TopSIPSrvProfInfo profile : sipSrvTable) {
                profile.setEntityId(entityId);
                session.insert(getNameSpace("insertTopSIPSrvProfInfo"), profile);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void insertTopDigitMapProfInfos(List<TopDigitMapProfInfo> digitMapTable, Long entityId) {
        SqlSession session = getBatchSession();
        try {
            session.delete(getNameSpace("deleteTopDigitMapProfInfos"), entityId);
            for (TopDigitMapProfInfo profile : digitMapTable) {
                profile.setEntityId(entityId);
                session.insert(getNameSpace("insertTopDigitMapProfInfo"), profile);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void insertTopGponSrvPotsInfos(List<TopGponSrvPotsInfo> potsList, Long entityId, Integer profileIndex) {
        SqlSession session = getBatchSession();
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("entityId", entityId);
            map.put("profileIndex", profileIndex);
            session.delete(getNameSpace("deletePotsInProfile"), map);
            for (TopGponSrvPotsInfo profile : potsList) {
                profile.setEntityId(entityId);
                session.insert(getNameSpace("insertTopGponSrvPotsInfo"), profile);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void insertGponSrvProfileCfg(GponSrvProfileCfg gponSrvProfileCfg) {
        getSqlSession().insert(getNameSpace("insertGponSrvProfileCfg"), gponSrvProfileCfg);
    }

    @Override
    public void insertGponSrvProfilePortNumProfile(GponSrvProfilePortNumProfile gponSrvProfilePortNumProfile) {
        getSqlSession().insert(getNameSpace("insertGponSrvProfilePortNumProfile"), gponSrvProfilePortNumProfile);
    }

    @Override
    public void insertTopGponSrvProfile(TopGponSrvProfile cfg) {
        getSqlSession().insert(getNameSpace("insertTopGponSrvProfile"), cfg);
    }

}
