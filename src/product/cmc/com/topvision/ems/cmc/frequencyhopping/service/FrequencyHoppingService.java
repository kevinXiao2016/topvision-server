/***********************************************************************
 * $Id: CcmtsSpectrumGpService.java,v1.0 2013-8-2 上午10:29:22 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.frequencyhopping.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.cmc.frequencyhopping.domain.CcmtsSpectrumGp;
import com.topvision.ems.cmc.frequencyhopping.domain.CcmtsSpectrumGpChnl;
import com.topvision.ems.cmc.frequencyhopping.domain.CcmtsSpectrumGpGlobal;
import com.topvision.ems.cmc.frequencyhopping.domain.CcmtsSpectrumGpHopHis;
import com.topvision.ems.cmc.frequencyhopping.domain.CcmtsSpectrumGpTemplate;
import com.topvision.ems.cmc.frequencyhopping.domain.EmsCcmtsSpectrumGp;
import com.topvision.ems.cmc.frequencyhopping.domain.SpectrumTempConfigLog;
import com.topvision.ems.cmc.frequencyhopping.domain.SpectrumTempConfigLogDetail;
import com.topvision.framework.service.Service;

/**
 * @author haojie
 * @created @2013-8-2-上午10:29:22
 * 
 */
public interface FrequencyHoppingService extends Service {

    /**
     * 获取CCMTS自动跳频全局配置信息
     * 
     * @param entityId
     * @return
     */
    CcmtsSpectrumGpGlobal getDeviceGpGlobal(Long entityId);

    /**
     * 更新CCMTS自动跳频全局配置
     * 
     * @param ccmtsSpectrumGpGlobal
     */
    void modifyDeviceGpGlobal(CcmtsSpectrumGpGlobal ccmtsSpectrumGpGlobal);

    /**
     * 从设备获取CCMTS自动跳频全局配置信息
     * 
     * @param entityId
     */
    void refreshGpGlobalFromDevice(Long entityId);

    /**
     * 获取自动跳频组列表信息
     * 
     * @param entityId
     * @return
     */
    List<CcmtsSpectrumGp> getDeviceGroupList(Long entityId);

    /**
     * 获取信道跳频历史记录
     * 
     * @param entityId
     * @param channelIndex
     * @return
     */
    public List<CcmtsSpectrumGpHopHis> getGroupHopHisList(Long entityId, Long channelIndex);

    /**
     * 清空信道跳频历史记录
     * 
     * @param chnlGroup
     */
    public void deleteGroupHopHis(CcmtsSpectrumGpChnl chnlGroup);

    /**
     * 从设备上获取信道历史
     * 
     * @param entityId
     * @param chanelIndex
     * @return
     */
    public void refreshGpHopHisFromDevice(CcmtsSpectrumGpHopHis hopHis);

    /**
     * 获取当前设备上的所有跳频组
     * 
     * @param entityId
     * @return
     */
    public List<CcmtsSpectrumGp> getAllDeviceGroup(Long entityId);

    /**
     * 删除设备侧跳频组
     * 
     * @param entityId
     * @param groupId
     */
    void deleteDeviceGroup(Long entityId, Integer groupId);

    /**
     * 刷新设备侧跳频组信息
     * 
     * @param entityId
     */
    void refreshGroupFromDevice(Long entityId);

    /**
     * 新增跳频组
     * 
     * @param ccmtsSpectrumGp
     */
    void addDeviceGroup(CcmtsSpectrumGp ccmtsSpectrumGp);

    /**
     * 获取单个跳频组信息
     * 
     * @param entityId
     * @param groupId
     * @return
     */
    CcmtsSpectrumGp getDeviceGroup(Long entityId, Integer groupId);

    /**
     * 修改单个跳频组
     * 
     * @param ccmtsSpectrumGp
     */
    void modifyDeviceGroup(CcmtsSpectrumGp ccmtsSpectrumGp);

    /**
     * 获取自动跳频模板列表信息
     * 
     * @return
     */
    List<CcmtsSpectrumGpTemplate> getSpectrumGpTempLateList(Map<String, Object> map);

    /**
     * 新增自动跳频模板
     * 
     * @param gpTemplate
     */
    void addSpectrumGpTempLate(CcmtsSpectrumGpTemplate gpTemplate, String selectGroup);

    /**
     * 获取单个自动跳频模板信息
     * 
     * @param templateId
     * @return
     */
    CcmtsSpectrumGpTemplate getSpectrumGpTempLateById(Long templateId);

    /**
     * 修改自动跳频模板
     * 
     * @param gpTemplate
     */
    void txModifySpectrumGpTempLate(CcmtsSpectrumGpTemplate gpTemplate, String selectGroup);

    /**
     * 删除自动跳频组模板
     * 
     * @param templateId
     */
    void deleteSpectrumGpTempLate(Long templateId);

    /**
     * 获取全局跳频组列表信息
     * 
     * @return
     */
    List<EmsCcmtsSpectrumGp> getGlobalSpectrumGpList();

    /**
     * 删除全局自动跳频组
     * 
     * @param groupIds
     */
    void deleteGlobalSpectrumGp(List<Long> groupIds);

    /**
     * 新增全局跳频组
     * 
     * @param emsCcmtsSpectrumGp
     */
    void addGlobalSpectrumGp(EmsCcmtsSpectrumGp emsCcmtsSpectrumGp);

    /**
     * 获取自动跳频相关联的全局跳频组
     * 
     * @param templateId
     * @return
     */
    List<EmsCcmtsSpectrumGp> getTempRelationGp(Long templateId);

    /**
     * 获取单个全局跳频组
     * 
     * @param groupId
     * @return
     */
    EmsCcmtsSpectrumGp getGlobalSpectrumGpById(Long emsGroupId);

    /**
     * 修改全局跳频组
     * 
     * @param emsCcmtsSpectrumGp
     */
    void modifyGlobalSpectrumGp(EmsCcmtsSpectrumGp emsCcmtsSpectrumGp);

    /**
     * 保存设备跳频组到网管全局跳频组
     * 
     * @param entityId
     * @param groupIds
     */
    void transGroupFromDeviceToGlobal(Long entityId, List<Long> groupIds);

    /**
     * 下发全局跳频组到设备
     * 
     * @param entityId
     * @param emsGroupId
     * @param groupId
     */
    void addDeviceGroupFromGlobal(Long entityId, Long emsGroupId, Integer groupId);

    /**
     * 执行跳频模板的下发
     */
    void executeSpectrumGroupConfig(List<Long> entityIdList, CcmtsSpectrumGpTemplate template);

    /**
     * 获取信道的跳频组信息(备份调制方式和应用的跳频组)
     * 
     * @param entityId
     * @param channelIndex
     * @return
     */
    public CcmtsSpectrumGpChnl getChnlGroupInfo(Long entityId, Long channelIndex);

    /**
     * 从设备上获取信道跳频组信息
     * 
     * @param chnlGroup
     */
    public void refreshChnlGroupFromDevice(Long cmcId, CcmtsSpectrumGpChnl chnlGroup);

    /**
     * 修改信道跳频组信息并应用到设备
     * 
     * @param chnlGroup
     */
    public void modifyChnlGroupInfo(CcmtsSpectrumGpChnl chnlGroup);

    /**
     * 将信道上的跳频组恢复到静态配置的状态
     * 
     * @param chnlGroup
     */
    public void modifyGpChnlResetToStatic(CcmtsSpectrumGpChnl chnlGroup);

    /**
     * 获取自动跳频模板批量配置日志记录
     * 
     * @return
     */
    List<SpectrumTempConfigLog> getConfigLogList(Map<String, Object> map);

    /**
     * 获得指定的日志记录对应的详细记录
     * 
     * @param configLogId
     * @return
     */
    List<SpectrumTempConfigLogDetail> getLogDetailList(Map<String, Object> map);

    /**
     * 获取总的日志记录数量
     * 
     * @return
     */
    int getLogCount();

    /**
     * 获取总的详细记录数量
     * 
     * @return
     */
    int getDetailCount(Long configLogId);

    /**
     * 获取自动跳频模板总数
     * 
     * @return
     */
    int getTempCount();

    /**
     * 获取某一个信道使用到信道的数目
     * 
     * @param entityId
     * @param groupId
     * @return
     */
    String getGroupChlNum(Long entityId, Integer groupId);

    /**
     * 获取全局跳频组列表
     * 
     * @param map
     * @return
     */
    List<EmsCcmtsSpectrumGp> getGlobalSpectrumGpList(Map<String, Object> map);

    /**
     * 获取全局跳频组总数
     * 
     * @return
     */
    Integer getGlobalSpectrumGpNum();

    /**
     * 获取所有的全局跳频组
     * 
     * @return
     */
    List<EmsCcmtsSpectrumGp> getAllGlobalGroup();

}