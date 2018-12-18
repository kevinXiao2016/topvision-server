/***********************************************************************
 * $Id: FrequencyHoppingDao.java,v1.0 2013-8-2 上午10:44:16 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.frequencyhopping.dao;

import java.util.List;
import java.util.Map;

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
import com.topvision.framework.dao.Dao;

/**
 * @author haojie
 * @created @2013-8-2-上午10:44:16
 * 
 */
public interface FrequencyHoppingDao extends Dao {

    /**
     * 获取CCMTS自动跳频全局配置信息
     * 
     * @param entityId
     * @return
     */
    CcmtsSpectrumGpGlobal queryGpGlobal(Long entityId);

    /**
     * 修改CCMTS自动跳频全局配置
     * 
     * @param ccmtsSpectrumGpGlobal
     */
    void updateGpGlobal(CcmtsSpectrumGpGlobal ccmtsSpectrumGpGlobal);

    /**
     * 插入CCMTS自动跳频全局配置
     * 
     * @param ccmtsSpectrumGpGlobal
     * @param entityId
     */
    void insertOrUpdateCcmtsSpectrumGpGlobal(CcmtsSpectrumGpGlobal ccmtsSpectrumGpGlobal, Long entityId);

    /**
     * 获取跳频组列表信息
     * 
     * @param entityId
     * @return
     */
    List<CcmtsSpectrumGp> queryDeviceGroupList(Long entityId);

    /**
     * 获取跳频组成员频点信息
     * 
     * @param entityId
     * @param groupId
     * @return
     */
    List<CcmtsSpectrumGpFreq> querySpectrumGroupFreqList(Long entityId, Integer groupId);

    /**
     * 获取信道的跳频组信息(备份调制方式和应用的跳频组)
     * 
     * @param entityId
     * @param channelIndex
     * @return
     */
    CcmtsSpectrumGpChnl queryChnlGroupInfo(Long entityId, Long channelIndex);

    /**
     * 更改信道的跳频组信息(备份调制方式和应用的跳频组)
     * 
     * @param groupChnl
     */
    void updateChnlGroupInfo(CcmtsSpectrumGpChnl groupChnl);

    /**
     * 在数据库中插入信道跳频组信息
     * 
     * @param chnlGroup
     */
    void insertChnlGroupInfo(CcmtsSpectrumGpChnl chnlGroup);

    /**
     * 批量插入信道跳频组信息
     * 
     * @param chnlGpList
     */
    void batchInsertChnlGroup(List<CcmtsSpectrumGpChnl> chnlGpList);

    /**
     * 删除信道跳频组信息
     * 
     * @param chnlGroup
     */
    void deleteChnlGroupInfo(CcmtsSpectrumGpChnl chnlGroup);

    /**
     * 获取指定上行信道上所有的跳频历史记录
     * 
     * @param entityId
     * @param chanelIndex
     * @return
     */
    public List<CcmtsSpectrumGpHopHis> queryGroupHopHisList(Long entityId, Long channelIndex);

    /**
     * 清空指定上行信道上所有的跳频历史记录
     * 
     * @param entityId
     * @param chanelIndex
     */
    public void deleteGroupHopHis(Long entityId, Long channelIndex);

    /**
     * 插入上行信道跳频历史记录
     * 
     * @param gpHopHis
     */
    public void insertGroupHopHis(CcmtsSpectrumGpHopHis gpHopHis);

    /**
     * 批量插入上行信道跳频历史记录
     * 
     * @param hopHisList
     */
    public void batchInsertGroupHopHis(List<CcmtsSpectrumGpHopHis> hopHisList, Long entityId, Long channelIndex);

    /**
     * 获取设备上所有的跳频组
     * 
     * @param entityId
     */
    public List<CcmtsSpectrumGp> queryAllDeviceGroup(Long entityId);

    /**
     * 删除设备侧跳频组
     * 
     * @param entityId
     * @param groupId
     */
    void deleteDeviceGroup(Long entityId, Integer groupId);

    /**
     * 批量插入跳频组信息
     * 
     * @param ccmtsSpectrumGpList
     * @param entityId
     */
    void batchInsertDeviceGroup(List<CcmtsSpectrumGp> ccmtsSpectrumGpList, Long entityId);

    /**
     * 批量插入跳频组成员频点信息
     * 
     * @param ccmtsSpectrumGpFreqList
     * @param entityId
     */
    void batchInsertSpectrumGroupFreq(List<CcmtsSpectrumGpFreq> ccmtsSpectrumGpFreqList, Long entityId);

    /**
     * 插入跳频组信息
     * 
     * @param ccmtsSpectrumGp
     */
    void insertDeviceGroup(CcmtsSpectrumGp ccmtsSpectrumGp);

    /**
     * 插入跳频组成员频点信息
     * 
     * @param ccmtsSpectrumGpFreq
     */
    void insertSpectrumGroupFreq(CcmtsSpectrumGpFreq ccmtsSpectrumGpFreq);

    /**
     * 获取单个跳频组信息
     * 
     * @param entityId
     * @param groupId
     * @return
     */
    CcmtsSpectrumGp queryDeviceGroup(Long entityId, Integer groupId);

    /**
     * 修改跳频组
     * 
     * @param ccmtsSpectrumGp
     */
    void updateDeviceGroup(CcmtsSpectrumGp ccmtsSpectrumGp);

    /**
     * 获取自动跳频模板列表信息
     * 
     * @return
     */
    List<CcmtsSpectrumGpTemplate> querySpectrumGpTempLateList(Map<String, Object> map);

    /**
     * 新增自动跳频模板
     * 
     * @param gpTemplate
     */
    Long insertSpectrumGpTempLate(CcmtsSpectrumGpTemplate gpTemplate);

    /**
     * 获取指定的自动跳频模板信息
     * 
     * @param templateId
     * @return
     */
    CcmtsSpectrumGpTemplate querySpectrumGpTempLateById(Long templateId);

    /**
     * 更新自动跳频模板信息
     * 
     * @param gpTemplate
     */
    void updateSpectrumGpTempLate(CcmtsSpectrumGpTemplate gpTemplate);

    /**
     * 删除指定的跳频模板信息
     * 
     * @param templateId
     */
    void deleteSpectrumGpTempLate(Long templateId);

    /**
     * 获取全局跳频组列表信息
     * 
     * @return
     */
    List<EmsCcmtsSpectrumGp> queryGlobalSpectrumGpList();

    /**
     * 批量删除全局跳频组
     * 
     * @param groupIds
     */
    void batchDeleteGlobalGroup(List<Long> groupIds);

    /**
     * 插入全局跳频组
     * 
     * @param emsCcmtsSpectrumGp
     */
    Long insertGlobalSpectrumGp(EmsCcmtsSpectrumGp emsCcmtsSpectrumGp);

    /**
     * 插入全局跳频组成员频点
     * 
     * @param emsCcmtsSpectrumGpFreqList
     */
    void batchInsertGlobalSpectrumGpFreq(List<EmsCcmtsSpectrumGpFreq> emsCcmtsSpectrumGpFreqList);

    /**
     * 获取单个全局跳频组
     * 
     * @param groupId
     * @return
     */
    EmsCcmtsSpectrumGp queryGlobalSpectrumGpById(Long emsGroupId);

    /**
     * 获取单个全局跳频组成员频点
     * 
     * @param groupId
     * @return
     */
    List<EmsCcmtsSpectrumGpFreq> queryGlobalSpectrumGpFreqById(Long emsGroupId);

    /**
     * 修改全局跳频组
     * 
     * @param emsCcmtsSpectrumGp
     */
    void updateGlobalSpectrumGp(EmsCcmtsSpectrumGp emsCcmtsSpectrumGp);

    /**
     * 修改全局跳频组成员频点
     * 
     * @param emsCcmtsSpectrumGpFreqList
     * @param groupId
     */
    void batchUpdateGlobalSpectrumGpFreq(List<EmsCcmtsSpectrumGpFreq> emsCcmtsSpectrumGpFreqList, Long groupId);

    /**
     * 批量插入自动跳频模板与全局跳频组关联
     * 
     * @param relationList
     */
    void batchInsertTempGpRelation(List<SpectrumTempGpRelation> relationList);

    /**
     * 根据templateId删除自动跳频模板与全局跳频组关联
     * 
     * @param templateId
     */
    void deleteRelByTemplateId(Long templateId);

    /**
     * 查询与自动跳频模板相关联的
     * 
     * @param tempateId
     * @return
     */
    List<EmsCcmtsSpectrumGp> selectTempRelationGp(Long tempateId);

    /**
     * 插入自动跳频模板批量配置日志记录
     * 
     * @param configLog
     */
    Long insertTempConfigLog(SpectrumTempConfigLog configLog);

    /**
     * 插入自动跳频模板批量配置详细记录
     * 
     * @param logDetail
     */
    void insertConfigLogDetail(SpectrumTempConfigLogDetail logDetail);

    /**
     * 批量插入自动跳频模板配置详细记录
     * 
     * @param detailList
     */
    void batchInsertLogDetail(List<SpectrumTempConfigLogDetail> detailList);

    /**
     * 查询所有的自动跳频模板批量配置日志记录
     * 
     * @return
     */
    List<SpectrumTempConfigLog> queryConfigLogList(Map<String, Object> map);

    /**
     * 查询指定日志记录对应的所有详细记录
     * 
     * @param configLogId
     * @return
     */
    List<SpectrumTempConfigLogDetail> queryLogDetailList(Map<String, Object> map);

    /**
     * 获取总的日志记录数量
     * 
     * @return
     */
    public Integer queryLogCount();

    /**
     * 获取总的详细记录数量
     * 
     * @return
     */
    public Integer queryDetailCount(Long configLogId);

    /**
     * 更改配置状态
     * 
     * @param tempLateId
     */
    void updateSpectrumTempConfigLogStatus(Long configLogId);

    /**
     * 查询自动跳频模板总数
     * 
     * @return
     */
    public Integer queryTempCount();

    /**
     * 获取某一个信道使用到信道的数目
     * 
     * @param entityId
     * @param groupId
     * @return
     */
    String queryGroupChlNum(Long entityId, Integer groupId);

    /**
     * 删除跳频组的同时，将已经应用了此跳频组信道的关联跳频组ID设置为零
     * 
     * @param entityId
     * @param groupId
     */
    void updateChlGroupToZero(Long entityId, Integer groupId);

    /**
     * 获取全局跳频组列表
     * 
     * @param map
     * @return
     */
    List<EmsCcmtsSpectrumGp> queryGlobalSpectrumGpList(Map<String, Object> map);

    /**
     * 获取全局跳频组总数
     * 
     * @return
     */
    Integer queryGlobalSpectrumGpNum();

    /**
     * 获取所有的全局跳频组
     * 
     * @return
     */
    List<EmsCcmtsSpectrumGp> queryAllGlobalGroup();

    /**
     * 通过entityId来删除信道跳频组关联信息
     * 
     * @param entityId
     */
    void delChnlGpByEntityId(Long entityId);
}
