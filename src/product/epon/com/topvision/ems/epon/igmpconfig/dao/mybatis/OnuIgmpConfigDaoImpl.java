/***********************************************************************
 * $Id: OnuIgmpConfigDaoImpl.java,v1.0 2016-6-6 下午4:21:15 $
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

import com.topvision.ems.epon.igmpconfig.dao.OnuIgmpConfigDao;
import com.topvision.ems.epon.igmpconfig.domain.IgmpOnuConfig;
import com.topvision.ems.epon.igmpconfig.domain.IgmpPortInfo;
import com.topvision.ems.epon.igmpconfig.domain.IgmpUniBindCtcProfile;
import com.topvision.ems.epon.igmpconfig.domain.IgmpUniConfig;
import com.topvision.ems.epon.igmpconfig.domain.IgmpUniVlanTrans;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author flack
 * @created @2016-6-6-下午4:21:15
 *
 */
@Repository("onuIgmpConfigDao")
public class OnuIgmpConfigDaoImpl extends MyBatisDaoSupport<Object> implements OnuIgmpConfigDao {

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.epon.igmpconfig.domain.IgmpOnuConfig";
    }

    @Override
    public void insertBindCtcProfile(IgmpUniBindCtcProfile bindCtcProfile) {
        getSqlSession().insert(getNameSpace("insertBindCtcProfile"), bindCtcProfile);
    }

    @Override
    public void batchInsertBindProfile(Long entityId, List<IgmpUniBindCtcProfile> bindProfileList) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace("deleteAllBindCtcProfile"), entityId);
            for (IgmpUniBindCtcProfile bindProfile : bindProfileList) {
                bindProfile.setEntityId(entityId);
                sqlSession.insert(getNameSpace("insertBindCtcProfile"), bindProfile);
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
    public List<IgmpUniBindCtcProfile> queryBindCtcProfileList(Long entityId, Long uniIndex) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("uniIndex", uniIndex);
        return getSqlSession().selectList(getNameSpace("queryBindCtcProfileList"), paramMap);
    }

    @Override
    public List<IgmpUniBindCtcProfile> queryHasGroupBindProfile(Long entityId) {
        return getSqlSession().selectList(getNameSpace("queryHasGroupBindProfile"), entityId);
    }

    @Override
    public List<IgmpUniBindCtcProfile> queryWithSrcGroupBindProfile(Long entityId) {
        return getSqlSession().selectList(getNameSpace("queryWithSrcGroupBindProfile"), entityId);
    }

    @Override
    public List<IgmpUniBindCtcProfile> queryWithoutSrcGroupBindProfile(Long entityId) {
        return getSqlSession().selectList(getNameSpace("queryWithoutSrcGroupBindProfile"), entityId);
    }

    @Override
    public void deleteBindCtcProfile(IgmpUniBindCtcProfile bindProfile) {
        getSqlSession().delete(getNameSpace("deleteBindCtcProfile"), bindProfile);
    }


    @Override
    public void insertIgmpOnuConfig(IgmpOnuConfig onuConfig) {
        getSqlSession().insert(getNameSpace("insertIgmpOnuConfig"), onuConfig);
    }

    @Override
    public void batchInsertOnuConfig(Long entityId, List<IgmpOnuConfig> onuConfigList) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace("deleteAllOnuConfig"), entityId);
            for (IgmpOnuConfig onuConfig : onuConfigList) {
                onuConfig.setEntityId(entityId);
                sqlSession.insert(getNameSpace("insertIgmpOnuConfig"), onuConfig);
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
    public IgmpOnuConfig queryIgmpOnuConfig(Long entityId, Long onuIndex) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("onuIndex", onuIndex);
        return getSqlSession().selectOne(getNameSpace("queryIgmpOnuConfig"), paramMap);
    }

    @Override
    public void updateIgmpOnuConfig(IgmpOnuConfig onuConfig) {
        getSqlSession().update(getNameSpace("updateIgmpOnuConfig"), onuConfig);
    }

    @Override
    public void insertIgmpUniConfig(IgmpUniConfig uniConfig) {
        getSqlSession().insert(getNameSpace("insertIgmpUniConfig"), uniConfig);
    }

    @Override
    public void batchInsertUniConfig(Long entityId, List<IgmpUniConfig> uniConfigList) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace("deleteAllUniConfig"), entityId);
            for (IgmpUniConfig uniConfig : uniConfigList) {
                uniConfig.setEntityId(entityId);
                sqlSession.insert(getNameSpace("insertIgmpUniConfig"), uniConfig);
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
    public IgmpUniConfig queryIgmpUniConfig(Long entityId, Long uniIndex) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("uniIndex", uniIndex);
        return getSqlSession().selectOne(getNameSpace("queryIgmpUniConfig"), paramMap);
    }

    @Override
    public void updateIgmpUniConfig(IgmpUniConfig uniConfig) {
        getSqlSession().update(getNameSpace("updateIgmpUniConfig"), uniConfig);
    }

    @Override
    public void insertUniVlanTrans(IgmpUniVlanTrans vlanTrans) {
        getSqlSession().insert(getNameSpace("insertUniVlanTrans"), vlanTrans);
    }

    @Override
    public void batchInsertVlanTrans(Long entityId, List<IgmpUniVlanTrans> vlanTransList) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace("deleteAllUniVlanTrans"), entityId);
            for (IgmpUniVlanTrans vlanTrans : vlanTransList) {
                vlanTrans.setEntityId(entityId);
                sqlSession.insert(getNameSpace("insertUniVlanTrans"), vlanTrans);
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
    public List<IgmpUniVlanTrans> queryUniVlanTransList(Long entityId, Long uniIndex) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("uniIndex", uniIndex);
        return getSqlSession().selectList(getNameSpace("queryUniVlanTransList"), paramMap);
    }

    @Override
    public void deleteUniVlanTrans(IgmpUniVlanTrans vlanTrans) {
        getSqlSession().delete(getNameSpace("deleteUniVlanTrans"), vlanTrans);
    }

    @Override
    public List<IgmpPortInfo> queryUniPortList(Long onuId) {
        return getSqlSession().selectList(getNameSpace("queryUniPortList"), onuId);
    }

    @Override
    public void insertOrUpdateOnuConfig(IgmpOnuConfig onuConfig) {
        getSqlSession().insert(getNameSpace("insertOrUpdateOnuConfig"), onuConfig);
    }

    @Override
    public void insertOrUpdateUniConfig(IgmpUniConfig uniConfig) {
        getSqlSession().insert(getNameSpace("insertOrUpdateUniConfig"), uniConfig);
    }

}
