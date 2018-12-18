/***********************************************************************
 * $Id: FrequencyHoppingDaoImpl.java,v1.0 2013-10-16 上午10:59:53 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.frequencyhopping.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.cmc.frequencyhopping.dao.FrequencyHoppingDao;
import com.topvision.ems.cmc.frequencyhopping.domain.CcmtsSpectrumGp;
import com.topvision.ems.cmc.frequencyhopping.domain.CcmtsSpectrumGpChnl;
import com.topvision.ems.cmc.frequencyhopping.domain.CcmtsSpectrumGpFreq;
import com.topvision.ems.cmc.frequencyhopping.domain.CcmtsSpectrumGpGlobal;
import com.topvision.ems.cmc.frequencyhopping.domain.CcmtsSpectrumGpHopHis;
import com.topvision.ems.cmc.frequencyhopping.domain.CcmtsSpectrumGpTemplate;
import com.topvision.ems.cmc.frequencyhopping.domain.EmsCcmtsSpectrumGp;
import com.topvision.ems.cmc.frequencyhopping.domain.EmsCcmtsSpectrumGpFreq;
import com.topvision.ems.cmc.frequencyhopping.domain.SpectrumTempConfigLog;
import com.topvision.ems.cmc.frequencyhopping.domain.SpectrumTempConfigLogDetail;
import com.topvision.ems.cmc.frequencyhopping.domain.SpectrumTempGpRelation;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author haojie
 * @created @2013-10-16-上午10:59:53
 * 
 */
@Repository("frequencyHoppingDao")
public class FrequencyHoppingDaoImpl extends MyBatisDaoSupport<CcmtsSpectrumGp> implements FrequencyHoppingDao {

    @Override
    public CcmtsSpectrumGpGlobal queryGpGlobal(Long entityId) {
        return getSqlSession().selectOne(getNameSpace("getGpGlobal"), entityId);
    }

    @Override
    public void updateGpGlobal(CcmtsSpectrumGpGlobal ccmtsSpectrumGpGlobal) {
        getSqlSession().update(getNameSpace("updateGpGlobal"), ccmtsSpectrumGpGlobal);

    }

    @Override
    public void insertOrUpdateCcmtsSpectrumGpGlobal(CcmtsSpectrumGpGlobal ccmtsSpectrumGpGlobal, Long entityId) {
        getSqlSession().delete(getNameSpace("deleteCcmtsSpectrumGpGlobal"), entityId);
        ccmtsSpectrumGpGlobal.setEntityId(entityId);
        getSqlSession().insert(getNameSpace("insertCcmtsSpectrumGpGlobal"), ccmtsSpectrumGpGlobal);
    }

    @Override
    public List<CcmtsSpectrumGp> queryDeviceGroupList(Long entityId) {
        return getSqlSession().selectList(getNameSpace("getDeviceGroupList"), entityId);
    }

    @Override
    public List<CcmtsSpectrumGpFreq> querySpectrumGroupFreqList(Long entityId, Integer groupId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("groupId", groupId);
        return getSqlSession().selectList(getNameSpace("getSpectrumGroupFreqList"), map);
    }

    @Override
    public CcmtsSpectrumGpChnl queryChnlGroupInfo(Long entityId, Long channelIndex) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("entityId", entityId);
        param.put("channelIndex", channelIndex);
        return getSqlSession().selectOne(getNameSpace("selectChnlGroup"), param);
    }

    @Override
    public void updateChnlGroupInfo(CcmtsSpectrumGpChnl groupChnl) {
        //add by fanzidong,需要在更新之前格式化MAC地址
        String formattedMac = MacUtils.convertToMaohaoFormat(groupChnl.getChnlCmcMac());
        groupChnl.setChnlCmcMac(formattedMac);
        getSqlSession().update(getNameSpace("updateChnlGroup"), groupChnl);

    }

    @Override
    public void insertChnlGroupInfo(CcmtsSpectrumGpChnl chnlGroup) {
        //add by fanzidong,需要在入库之前格式化MAC地址
        String formattedMac = MacUtils.convertToMaohaoFormat(chnlGroup.getChnlCmcMac());
        chnlGroup.setChnlCmcMac(formattedMac);
        getSqlSession().insert(getNameSpace("insertChnlGroup"), chnlGroup);

    }

    @Override
    public void batchInsertChnlGroup(List<CcmtsSpectrumGpChnl> chnlGpList) {
        SqlSession session = getBatchSession();
        try {
            for (CcmtsSpectrumGpChnl chnlGp : chnlGpList) {
                //add by fanzidong,需要在入库之前格式化MAC地址
                String formattedMac = MacUtils.convertToMaohaoFormat(chnlGp.getChnlCmcMac());
                chnlGp.setChnlCmcMac(formattedMac);
                session.insert(getNameSpace("insertChnlGroup"), chnlGp);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void deleteChnlGroupInfo(CcmtsSpectrumGpChnl chnlGroup) {
        //add by fanzidong,需要在删除之前格式化MAC地址
        String formattedMac = MacUtils.convertToMaohaoFormat(chnlGroup.getChnlCmcMac());
        chnlGroup.setChnlCmcMac(formattedMac);
        getSqlSession().delete(getNameSpace("deleteChnlGroup"), chnlGroup);

    }

    @Override
    public List<CcmtsSpectrumGpHopHis> queryGroupHopHisList(Long entityId, Long channelIndex) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("entityId", entityId);
        param.put("channelIndex", channelIndex);
        return getSqlSession().selectList(getNameSpace("selectHopHis"), param);
    }

    @Override
    public void deleteGroupHopHis(Long entityId, Long channelIndex) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("entityId", entityId);
        param.put("channelIndex", channelIndex);
        getSqlSession().delete(getNameSpace("deleteHopHis"), param);
    }

    @Override
    public void insertGroupHopHis(CcmtsSpectrumGpHopHis gpHopHis) {
        getSqlSession().insert(getNameSpace("insertHopHis"), gpHopHis);

    }

    @Override
    public void batchInsertGroupHopHis(List<CcmtsSpectrumGpHopHis> hopHisList, Long entityId, Long channelIndex) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("entityId", entityId);
        param.put("channelIndex", channelIndex);
        getSqlSession().delete(getNameSpace("deleteHopHis"), param);

        SqlSession session = getBatchSession();
        try {
            for (CcmtsSpectrumGpHopHis hopHis : hopHisList) {
                //add by fanzidong,需要在入库之前对MAC地址进行格式化
                String formatedMac = MacUtils.convertToMaohaoFormat(hopHis.getCmcMac());
                hopHis.setCmcMac(formatedMac);
                session.insert(getNameSpace("insertHopHis"), hopHis);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public List<CcmtsSpectrumGp> queryAllDeviceGroup(Long entityId) {
        return getSqlSession().selectList(getNameSpace("selectAllDeviceGp"), entityId);
    }

    @Override
    public void deleteDeviceGroup(Long entityId, Integer groupId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("groupId", groupId);
        getSqlSession().delete(getNameSpace("deleteDeviceGroup"), map);
    }

    @Override
    public void batchInsertDeviceGroup(List<CcmtsSpectrumGp> ccmtsSpectrumGpList, Long entityId) {
        getSqlSession().delete(getNameSpace("deleteDeviceGroupList"), entityId);
        SqlSession session = getBatchSession();
        try {
            for (CcmtsSpectrumGp ccmtsSpectrumGp : ccmtsSpectrumGpList) {
                ccmtsSpectrumGp.setEntityId(entityId);
                session.insert(getNameSpace("insertDeviceGroup"), ccmtsSpectrumGp);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void batchInsertSpectrumGroupFreq(List<CcmtsSpectrumGpFreq> ccmtsSpectrumGpFreqList, Long entityId) {
        SqlSession session = getBatchSession();
        try {
            for (CcmtsSpectrumGpFreq ccmtsSpectrumGpFreq : ccmtsSpectrumGpFreqList) {
                ccmtsSpectrumGpFreq.setEntityId(entityId);
                session.insert(getNameSpace("insertDeviceGpFreq"), ccmtsSpectrumGpFreq);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void insertDeviceGroup(CcmtsSpectrumGp ccmtsSpectrumGp) {
        getSqlSession().insert(getNameSpace("insertDeviceGroup"), ccmtsSpectrumGp);

    }

    @Override
    public void insertSpectrumGroupFreq(CcmtsSpectrumGpFreq ccmtsSpectrumGpFreq) {
        getSqlSession().insert(getNameSpace("insertDeviceGpFreq"), ccmtsSpectrumGpFreq);

    }

    @Override
    public CcmtsSpectrumGp queryDeviceGroup(Long entityId, Integer groupId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("groupId", groupId);
        return getSqlSession().selectOne(getNameSpace("getDeviceGroup"), map);
    }

    @Override
    public void updateDeviceGroup(CcmtsSpectrumGp ccmtsSpectrumGp) {
        getSqlSession().update(getNameSpace("updateDeviceGroup"), ccmtsSpectrumGp);

    }

    @Override
    public List<CcmtsSpectrumGpTemplate> querySpectrumGpTempLateList(Map<String, Object> map) {
        return getSqlSession().selectList(getNameSpace("selectAllGpTemplate"), map);
    }

    @Override
    public Long insertSpectrumGpTempLate(CcmtsSpectrumGpTemplate gpTemplate) {
        return (long) getSqlSession().insert(getNameSpace("insertGpTemplate"), gpTemplate);
    }

    @Override
    public CcmtsSpectrumGpTemplate querySpectrumGpTempLateById(Long templateId) {
        return getSqlSession().selectOne(getNameSpace("selectGpTemplateById"), templateId);
    }

    @Override
    public void updateSpectrumGpTempLate(CcmtsSpectrumGpTemplate gpTemplate) {
        getSqlSession().update(getNameSpace("updateGpTemplate"), gpTemplate);
    }

    @Override
    public void deleteSpectrumGpTempLate(Long templateId) {
        getSqlSession().delete(getNameSpace("deleteGpTemplate"), templateId);
    }

    @Override
    public List<EmsCcmtsSpectrumGp> queryGlobalSpectrumGpList() {
        return getSqlSession().selectList(getNameSpace("getGlobalSpectrumGpList"));
    }

    @Override
    public void batchDeleteGlobalGroup(List<Long> groupIds) {
        SqlSession session = getBatchSession();
        try {
            for (Long groupId : groupIds) {
                session.delete(getNameSpace("deleteGlobalGroup"), groupId);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public Long insertGlobalSpectrumGp(EmsCcmtsSpectrumGp emsCcmtsSpectrumGp) {
        return (long) getSqlSession().insert(getNameSpace("insertGlobalSpectrumGp"), emsCcmtsSpectrumGp);
    }

    @Override
    public void batchInsertGlobalSpectrumGpFreq(List<EmsCcmtsSpectrumGpFreq> emsCcmtsSpectrumGpFreqList) {
        SqlSession session = getBatchSession();
        try {
            for (EmsCcmtsSpectrumGpFreq emsCcmtsSpectrumGpFreq : emsCcmtsSpectrumGpFreqList) {
                session.insert(getNameSpace("insertGlobalSpectrumGpFreq"), emsCcmtsSpectrumGpFreq);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public EmsCcmtsSpectrumGp queryGlobalSpectrumGpById(Long emsGroupId) {
        return (EmsCcmtsSpectrumGp) getSqlSession().selectList(getNameSpace("getGlobalSpectrumGpById"), emsGroupId);
    }

    @Override
    public List<EmsCcmtsSpectrumGpFreq> queryGlobalSpectrumGpFreqById(Long emsGroupId) {
        return getSqlSession().selectList(getNameSpace("getGlobalSpectrumGpFreqById"), emsGroupId);
    }

    @Override
    public void updateGlobalSpectrumGp(EmsCcmtsSpectrumGp emsCcmtsSpectrumGp) {
        getSqlSession().update(getNameSpace("updateGlobalSpectrumGp"), emsCcmtsSpectrumGp);
    }

    @Override
    public void batchUpdateGlobalSpectrumGpFreq(List<EmsCcmtsSpectrumGpFreq> emsCcmtsSpectrumGpFreqList, Long groupId) {
        getSqlSession().delete(getNameSpace("deleteGlobalSpectrumGpFreqById"), groupId);
        SqlSession session = getBatchSession();
        try {
            for (EmsCcmtsSpectrumGpFreq emsCcmtsSpectrumGpFreq : emsCcmtsSpectrumGpFreqList) {
                session.insert(getNameSpace("insertGlobalSpectrumGpFreq"), emsCcmtsSpectrumGpFreq);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void batchInsertTempGpRelation(List<SpectrumTempGpRelation> relationList) {
        SqlSession session = getBatchSession();
        try {
            for (SpectrumTempGpRelation tempGpRelation : relationList) {
                session.insert(getNameSpace("insetTempGpRelation"), tempGpRelation);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void deleteRelByTemplateId(Long templateId) {
        getSqlSession().delete(getNameSpace("delRelByTemplateId"), templateId);
    }

    @Override
    public List<EmsCcmtsSpectrumGp> selectTempRelationGp(Long tempateId) {
        return getSqlSession().selectList(getNameSpace("selectTempRelationGp"), tempateId);
    }

    @Override
    public Long insertTempConfigLog(SpectrumTempConfigLog configLog) {
        return (long) getSqlSession().insert(getNameSpace("insertConfigLog"), configLog);
    }

    @Override
    public void insertConfigLogDetail(SpectrumTempConfigLogDetail logDetail) {
        getSqlSession().insert(getNameSpace("insertLogDetail"), logDetail);
    }

    @Override
    public void batchInsertLogDetail(List<SpectrumTempConfigLogDetail> detailList) {
        SqlSession session = getBatchSession();
        try {
            for (SpectrumTempConfigLogDetail logDetail : detailList) {
                session.insert(getNameSpace("insertLogDetail"), logDetail);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public List<SpectrumTempConfigLog> queryConfigLogList(Map<String, Object> map) {
        return getSqlSession().selectList(getNameSpace("selectAllConfigLog"), map);
    }

    @Override
    public List<SpectrumTempConfigLogDetail> queryLogDetailList(Map<String, Object> map) {
        return getSqlSession().selectList(getNameSpace("selectDetailByLogId"), map);
    }

    @Override
    public Integer queryLogCount() {
        return getSqlSession().selectOne(getNameSpace("selectTotalLog"));
    }

    @Override
    public Integer queryDetailCount(Long configLogId) {
        return getSqlSession().selectOne(getNameSpace("selectTotalDetail"), configLogId);
    }

    @Override
    public void updateSpectrumTempConfigLogStatus(Long configLogId) {
        getSqlSession().update(getNameSpace("updateSpectrumTempConfigLogStatus"), configLogId);
    }

    @Override
    public Integer queryTempCount() {
        return getSqlSession().selectOne(getNameSpace("selectTempCount"));
    }

    @Override
    public String queryGroupChlNum(Long entityId, Integer groupId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("groupId", groupId);
        return getSqlSession().selectOne(getNameSpace("getGroupChlNum"), map);
    }

    @Override
    public void updateChlGroupToZero(Long entityId, Integer groupId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("groupId", groupId);
        getSqlSession().update(getNameSpace("updateChlGroupToZero"), map);
    }

    @Override
    public List<EmsCcmtsSpectrumGp> queryGlobalSpectrumGpList(Map<String, Object> map) {
        return getSqlSession().selectList(getNameSpace("getGlobalGpList"), map);
    }

    @Override
    public Integer queryGlobalSpectrumGpNum() {
        return getSqlSession().selectOne(getNameSpace("getGlobalSpectrumGpNum"));
    }

    @Override
    public List<EmsCcmtsSpectrumGp> queryAllGlobalGroup() {
        return getSqlSession().selectList(getNameSpace("getAllGlobalGp"));
    }

    @Override
    public void delChnlGpByEntityId(Long entityId) {
        getSqlSession().delete(getNameSpace("delChnlGpByEntityId"), entityId);
    }

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.cmc.frequencyhopping.domain.CcmtsSpectrumGp";
    }

}
