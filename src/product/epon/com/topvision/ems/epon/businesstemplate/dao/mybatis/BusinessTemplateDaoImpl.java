/***********************************************************************
 * $Id: BusinessTemplateDaoImpl.java,v1.0 2015-12-8 下午2:08:23 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.businesstemplate.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.businesstemplate.dao.BusinessTemplateDao;
import com.topvision.ems.epon.businesstemplate.domain.OnuCapability;
import com.topvision.ems.epon.businesstemplate.domain.OnuIgmpProfile;
import com.topvision.ems.epon.businesstemplate.domain.OnuPortVlanProfile;
import com.topvision.ems.epon.businesstemplate.domain.OnuSrvIgmpVlanTrans;
import com.topvision.ems.epon.businesstemplate.domain.OnuSrvProfile;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author lizongtian
 * @created @2015-12-8-下午2:08:23
 *
 */
@Repository("businessTemplateDao")
public class BusinessTemplateDaoImpl extends MyBatisDaoSupport<OnuSrvProfile> implements BusinessTemplateDao {

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.epon.businesstemplate.domain.BusinessTemplate";
    }

    @Override
    public List<OnuSrvProfile> getOnuSrvProfiles(Long entityId) {
        return this.getSqlSession().selectList(getNameSpace("selectOnuSrvProfiles"), entityId);
    }

    @Override
    public List<OnuIgmpProfile> getOnuIgmpProfiles(Long entityId, Integer profileId) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("srvProfileId", profileId);
        return this.getSqlSession().selectList(getNameSpace("selectOnuIgmpProfiles"), paramMap);
    }

    @Override
    public List<OnuPortVlanProfile> getOnuPortVlanProfiles(Long entityId, Integer profileId) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("srvProfileId", profileId);
        return this.getSqlSession().selectList(getNameSpace("selectOnuPortVlanProfiles"), paramMap);
    }

    @Override
    public void deleteOnuSrvProfile(OnuSrvProfile srvProfile) {
        this.getSqlSession().delete(getNameSpace("deleteOnuSrvProfile"), srvProfile);
    }

    @Override
    public void deleteOnuIgmpProfile(OnuIgmpProfile igmpProfile) {
        this.getSqlSession().delete(getNameSpace("deleteOnuIgmpProfile"), igmpProfile);
    }

    @Override
    public void deleteOnuPortVlanProfile(OnuPortVlanProfile portVlanProfile) {
        this.getSqlSession().delete(getNameSpace("deleteOnuPortVlanProfile"), portVlanProfile);
    }

    @Override
    public void updateOnuSrvProfile(OnuSrvProfile srvProfile) {
        this.getSqlSession().update(getNameSpace("updateOnuSrvProfile"), srvProfile);
    }

    @Override
    public void updateOnuIgmpProfile(OnuIgmpProfile igmpProfile) {
        this.getSqlSession().update(getNameSpace("updateOnuIgmpProfile"), igmpProfile);
    }

    @Override
    public void updateOnuPortVlanProfile(OnuPortVlanProfile portVlanProfile) {
        this.getSqlSession().update(getNameSpace("updateOnuPortVlanProfile"), portVlanProfile);
    }

    @Override
    public void addOnuSrvProfile(OnuSrvProfile srvProfile) {
        this.getSqlSession().insert(getNameSpace("insertOnuSrvProfile"), srvProfile);
    }

    @Override
    public void addOnuIgmpProfile(OnuIgmpProfile igmpProfile) {
        this.getSqlSession().insert(getNameSpace("insertOnuIgmpProfile"), igmpProfile);
    }

    @Override
    public void addOnuPortVlanProfile(OnuPortVlanProfile portVlanProfile) {
        this.getSqlSession().insert(getNameSpace("insertOnuPortVlanProfile"), portVlanProfile);
    }

    @Override
    public void batchInsertOnuSrvProfile(Long entityId, List<OnuSrvProfile> srvProfiles) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace() + "deleteOnuSrvProfiles", entityId);
            for (OnuSrvProfile srvProfile : srvProfiles) {
                srvProfile.setEntityId(entityId);
                sqlSession.insert(getNameSpace() + "insertOnuSrvProfile", srvProfile);
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
    public void batchInsertOnuIgmpProfile(Long entityId, List<OnuIgmpProfile> igmpProfiles) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace() + "deleteOnuIgmpProfiles", entityId);
            for (OnuIgmpProfile igmpProfile : igmpProfiles) {
                igmpProfile.setEntityId(entityId);
                sqlSession.insert(getNameSpace() + "insertOnuIgmpProfile", igmpProfile);
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
    public void batchInsertOnuPortVlanProfile(Long entityId, List<OnuPortVlanProfile> portVlanProfiles) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace() + "deleteOnuPortVlanProfiles", entityId);
            for (OnuPortVlanProfile portVlanProfile : portVlanProfiles) {
                portVlanProfile.setEntityId(entityId);
                sqlSession.insert(getNameSpace() + "insertOnuPortVlanProfile", portVlanProfile);
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
    public void batchInsertOnuIgmpVlanTrans(Long entityId, List<OnuSrvIgmpVlanTrans> igmpVlanTrans) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace() + "deleteAllIgmpVlanTrans", entityId);
            for (OnuSrvIgmpVlanTrans onuVlanTrans : igmpVlanTrans) {
                onuVlanTrans.setEntityId(entityId);
                sqlSession.insert(getNameSpace() + "insertOnuIgmpVlanTrans", onuVlanTrans);
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
    public OnuSrvProfile getOnuSrvProfile(Long entityId, Integer profileId) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("profileId", profileId);
        return this.getSqlSession().selectOne(getNameSpace("selectOnuSrvProfile"), paramMap);
    }

    @Override
    public OnuIgmpProfile getOnuIgmpProfile(Long entityId, Integer profileId, Integer igmpPortId) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("profileId", profileId);
        paramMap.put("igmpPortId", igmpPortId);
        return this.getSqlSession().selectOne(getNameSpace("selectOnuIgmpProfile"), paramMap);
    }

    @Override
    public OnuPortVlanProfile getOnuPortVlanProfile(Long entityId, Integer profileId, Integer srvPortId) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("profileId", profileId);
        paramMap.put("srvPortId", srvPortId);
        return this.getSqlSession().selectOne(getNameSpace("selectOnuPortVlanProfile"), paramMap);
    }

    @Override
    public List<OnuCapability> getOnuCapabilitys(Long entityId) {
        return this.getSqlSession().selectList(getNameSpace("selectOnuCapabilitys"), entityId);
    }

    @Override
    public void deleteOnuCapability(OnuCapability capability) {
        this.getSqlSession().delete(getNameSpace("deleteOnuCapability"), capability);
    }

    @Override
    public void addOnuCapability(OnuCapability capability) {
        this.getSqlSession().insert(getNameSpace("insertOnuCapability"), capability);
    }

    @Override
    public void batchInsertOnuCapability(Long entityId, List<OnuCapability> capabilitys) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace() + "deleteOnuCapabilitys", entityId);
            for (OnuCapability capability : capabilitys) {
                capability.setEntityId(entityId);
                sqlSession.insert(getNameSpace() + "insertOnuCapability", capability);
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
    public void updateProfileBindCap(OnuSrvProfile srvProfile) {
        this.getSqlSession().update(getNameSpace("updateProfileBindCap"), srvProfile);
    }

    @Override
    public void deleteOnuSrvIgmpVlanTrans(OnuSrvIgmpVlanTrans igmpVlanTrans) {
        this.getSqlSession().delete(getNameSpace("deleteOnuIgmpVlanTrans"), igmpVlanTrans);
    }

    @Override
    public void addOnuSrvIgmpVlanTrans(OnuSrvIgmpVlanTrans igmpVlanTrans) {
        this.getSqlSession().insert(getNameSpace("insertOnuIgmpVlanTrans"), igmpVlanTrans);
    }

    @Override
    public List<OnuSrvIgmpVlanTrans> loadOnuIgmpVlanTrans(Long entityId, Integer profileId, Integer portId) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("profileId", profileId);
        paramMap.put("portId", portId);
        return this.getSqlSession().selectList(getNameSpace("loadOnuIgmpVlanTrans"), paramMap);
    }

}
