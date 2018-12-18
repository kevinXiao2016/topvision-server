package com.topvision.ems.cmc.cm.dao.mybatis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.topvision.ems.cmc.cm.dao.CmRefreshDao;
import com.topvision.ems.cmc.domain.CmCmcRelation;
import com.topvision.ems.cmc.domain.CmcPortRelation;
import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * 
 * @author YangYi
 * @created @2013-10-17-上午8:34:29
 * 
 */
@Repository("cmRefreshDao")
public class CmRefreshDaoImpl extends MyBatisDaoSupport<CmCmcRelation> implements CmRefreshDao {
    public void batchRefreshCmAttribute(Long entityId, Long cmcId, List<CmAttribute> cmAttributes) {
        List<CmCmcRelation> oldCmCmcRelations = getSqlSession().selectList(
                getNameSpace("selectCmCmcRelationByCmcIdFirst"), cmcId);
        List<CmAttribute> oldCmAttributesList = getSqlSession().selectList(getNameSpace("selectCmAttributeByCmcId"),
                cmcId);
        List<CmcPortRelation> channelIndexToIdList = getSqlSession().selectList(
                getNameSpace("selectChannelIndexToIdByCmcId"), cmcId);
        Map<String, CmAttribute> oldCmAttributes = new HashMap<String, CmAttribute>();
        for (CmAttribute cmAttribute : oldCmAttributesList) {
            oldCmAttributes.put(cmAttribute.getStatusMacAddress(), cmAttribute);
        }
        Map<Long, Long> channelIndexToId = new HashMap<Long, Long>();
        for (CmcPortRelation cmcPortRelation : channelIndexToIdList) {
            channelIndexToId.put(cmcPortRelation.getChannelIndex(), cmcPortRelation.getCmcPortId());
        }
        List<CmCmcRelation> newCmCmcRelations = new ArrayList<CmCmcRelation>();
        Map<String, CmAttribute> newCmAttributes = new HashMap<String, CmAttribute>();
        for (CmAttribute cmAttribute : cmAttributes) {
            try {
                CmCmcRelation cmCmcRelation = new CmCmcRelation();
                cmCmcRelation.setEntityId(entityId);
                cmCmcRelation.setCmcId(cmcId);
                cmCmcRelation.setCmIndex(cmAttribute.getStatusIndex());
                cmCmcRelation.setUpPortId(channelIndexToId.get(cmAttribute.getStatusUpChannelIfIndex()));
                cmCmcRelation.setDownPortId(channelIndexToId.get(cmAttribute.getStatusDownChannelIfIndex()));
                cmCmcRelation.setMac(cmAttribute.getStatusMacAddress());
                newCmCmcRelations.add(cmCmcRelation);
                newCmAttributes.put(cmAttribute.getStatusMacAddress(), cmAttribute);
            } catch (Exception e) {
                if (logger.isDebugEnabled()) {
                    logger.debug("attr error##############cmmac:" + cmAttribute.getStatusMacAddress() + " cmAttribute:" + cmAttribute,e);
                }
            }
        }
        List<CmCmcRelation> deleteR = new ArrayList<CmCmcRelation>();
        List<CmCmcRelation> insertR = new ArrayList<CmCmcRelation>();
        List<CmCmcRelation> updateR = new ArrayList<CmCmcRelation>();
        List<CmAttribute> insert = new ArrayList<CmAttribute>();
        List<CmAttribute> update = new ArrayList<CmAttribute>();
        for (CmCmcRelation cmCmcRelation : oldCmCmcRelations) {
            if (!newCmCmcRelations.contains(cmCmcRelation)) {
                deleteR.add(cmCmcRelation);
            }
        }
        for (CmCmcRelation cmCmcRelation : newCmCmcRelations) {
            int index = oldCmCmcRelations.indexOf(cmCmcRelation);
            if (index == -1) {
                CmAttribute newCmAttribute = newCmAttributes.get(cmCmcRelation.getMac());
                insert.add(newCmAttribute);
                insertR.add(cmCmcRelation);
            } else {
                CmAttribute oldCmAttribute = oldCmAttributes.get(cmCmcRelation.getMac());
                CmAttribute newCmAttribute = newCmAttributes.get(cmCmcRelation.getMac());
                if (oldCmAttribute == null) {
                    insert.add(newCmAttribute);
                } else {
                    oldCmAttribute.copyBy(newCmAttribute);
                    update.add(oldCmAttribute);
                }
                CmCmcRelation oldcmCmCmcRelation = oldCmCmcRelations.get(index);
                oldcmCmCmcRelation.setCmIndex(cmCmcRelation.getCmIndex());
                oldcmCmCmcRelation.setUpPortId(cmCmcRelation.getUpPortId());
                oldcmCmCmcRelation.setDownPortId(cmCmcRelation.getDownPortId());
                updateR.add(oldcmCmCmcRelation);
            }
        }
        SqlSessionTemplate sst1 = (SqlSessionTemplate) getSqlSession();
        SqlSession session1 = sst1.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
        try {
            for (CmCmcRelation cmCmcRelation : deleteR) {
                session1.delete(getNameSpace("deleteCmCmcRelation"), cmCmcRelation);
            }
            for (CmCmcRelation cmCmcRelation : insertR) {
                session1.insert(getNameSpace("insertCmCmcRelation"), cmCmcRelation);
            }
            for (CmCmcRelation cmCmcRelation : updateR) {
                session1.update(getNameSpace("updateCmCmcRelation"), cmCmcRelation);
            }
            session1.commit();
        } catch (Exception e) {
            logger.debug("CmRefreshDaoImpl.CmCmcRelation",e);
            session1.rollback();
        } finally {
            session1.close();
        }
        List<CmCmcRelation> nowCmCmcRelations = getSqlSession().selectList(getNameSpace("selectCmCmcRelationByCmcId"),
                cmcId);
        Map<String, Long> cmmacToCmId = new HashMap<String, Long>();
        for (CmCmcRelation cmCmcRelation : nowCmCmcRelations) {
            cmmacToCmId.put(cmCmcRelation.getMac(), cmCmcRelation.getCmId());
        }
        for (CmAttribute cmAttribute : insert) {
            cmAttribute.setCmcId(cmcId);
            cmAttribute.setCmId(cmmacToCmId.get(cmAttribute.getStatusMacAddress()));
        }
        for (CmAttribute cmAttribute : update) {
            cmAttribute.setCmcId(cmcId);
            cmAttribute.setCmId(cmmacToCmId.get(cmAttribute.getStatusMacAddress()));
        }
        SqlSessionTemplate sst2 = (SqlSessionTemplate) getSqlSession();
        SqlSession session2 = sst2.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
        try {
            for (CmAttribute cmAttribute : insert) {
                session2.insert(getNameSpace("insertCmAttribute"), cmAttribute);
            }
            for (CmAttribute cmAttribute : update) {
                session2.update(getNameSpace("updateCmAttribute"), cmAttribute);
            }
            session2.commit();
        } catch (Exception e) {
            logger.debug("CmRefreshDaoImpl.CmAttribute",e);
            session2.rollback();
        } finally {
            session2.close();
        }
    }

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.cmc.cm.domain.CmRefresh";
    }

    @Override
    public void refreshCmPortId(Long upPortId, Long downPortId, String mac) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("mac", mac);
        map.put("upPortId", upPortId);
        map.put("downPortId", downPortId);
        getSqlSession().update(getNameSpace("refreshCmPortId"), map);
    }
}
