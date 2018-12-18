/***********************************************************************
 * $Id: CcmtsSpectrumGpServiceImpl.java,v1.0 2013-8-2 上午10:30:37 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.frequencyhopping.service.impl;

import static org.quartz.DateBuilder.futureDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.quartz.DateBuilder.IntervalUnit;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.facade.domain.CmcEntityRelation;
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
import com.topvision.ems.cmc.frequencyhopping.job.FrequencyHoppingWorker;
import com.topvision.ems.cmc.frequencyhopping.service.FrequencyHoppingService;
import com.topvision.ems.cmc.service.impl.CmcBaseCommonService;
import com.topvision.ems.cmc.util.CmcConstants;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.framework.constants.Symbol;
import com.topvision.framework.domain.PhysAddress;
import com.topvision.framework.utils.CmcIndexUtils;
import com.topvision.platform.service.SchedulerService;
import com.topvision.platform.util.CurrentRequest;

/**
 * @author haojie
 * @created @2013-8-2-上午10:30:37
 * 
 */
@Service("frequencyHoppingService")
public class FrequencyHoppingServiceImpl extends CmcBaseCommonService implements FrequencyHoppingService,
        BeanFactoryAware {
    @Resource(name = "frequencyHoppingDao")
    private FrequencyHoppingDao frequencyHoppingDao;
    @Resource(name = "schedulerService")
    private SchedulerService schedulerService;
    private BeanFactory beanFactory;
    private SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @PreDestroy
    public void destroy() {
        super.destroy();
    }

    @PostConstruct
    public void initialize() {
        super.initialize();
    }

    public FrequencyHoppingDao getFrequencyHoppingDao() {
        return frequencyHoppingDao;
    }

    public void setFrequencyHoppingDao(FrequencyHoppingDao frequencyHoppingDao) {
        this.frequencyHoppingDao = frequencyHoppingDao;
    }

    public SchedulerService getSchedulerService() {
        return schedulerService;
    }

    public void setSchedulerService(SchedulerService schedulerService) {
        this.schedulerService = schedulerService;
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public CcmtsSpectrumGpGlobal getDeviceGpGlobal(Long entityId) {
        return frequencyHoppingDao.queryGpGlobal(entityId);
    }

    @Override
    public void modifyDeviceGpGlobal(CcmtsSpectrumGpGlobal ccmtsSpectrumGpGlobal) {
        snmpParam = getSnmpParamByEntityId(ccmtsSpectrumGpGlobal.getEntityId());
        getFrequencyHoppingFacade(snmpParam.getIpAddress()).setGpGlobalToDevice(snmpParam, ccmtsSpectrumGpGlobal);
        frequencyHoppingDao.updateGpGlobal(ccmtsSpectrumGpGlobal);
    }

    @Override
    public void refreshGpGlobalFromDevice(Long entityId) {
        snmpParam = getSnmpParamByEntityId(entityId);
        CcmtsSpectrumGpGlobal ccmtsSpectrumGpGlobal = getFrequencyHoppingFacade(snmpParam.getIpAddress())
                .getGpGlobalFromDevice(snmpParam);
        if (ccmtsSpectrumGpGlobal != null) {
            ccmtsSpectrumGpGlobal.setEntityId(entityId);
            frequencyHoppingDao.updateGpGlobal(ccmtsSpectrumGpGlobal);
        }
    }

    @Override
    public List<CcmtsSpectrumGp> getDeviceGroupList(Long entityId) {
        List<CcmtsSpectrumGp> ccmtsSpectrumGpList = new ArrayList<CcmtsSpectrumGp>();
        ccmtsSpectrumGpList = frequencyHoppingDao.queryDeviceGroupList(entityId);

        for (int i = 0; i < ccmtsSpectrumGpList.size(); i++) {
            List<CcmtsSpectrumGpFreq> ccmtsSpectrumGpFreqList = new ArrayList<CcmtsSpectrumGpFreq>();
            ccmtsSpectrumGpFreqList = frequencyHoppingDao.querySpectrumGroupFreqList(entityId,
                    ccmtsSpectrumGpList.get(i).getGroupId());
            ccmtsSpectrumGpList.get(i).setCcmtsSpectrumGpFreqList(ccmtsSpectrumGpFreqList);
        }
        return ccmtsSpectrumGpList;
    }

    @Override
    public List<CcmtsSpectrumGpHopHis> getGroupHopHisList(Long entityId, Long channelIndex) {
        return frequencyHoppingDao.queryGroupHopHisList(entityId, channelIndex);
    }

    @Override
    public void deleteGroupHopHis(CcmtsSpectrumGpChnl chnlGroup) {
        // 先设置设备状态为清空信道跳频历史
        snmpParam = this.getSnmpParamByEntityId(chnlGroup.getEntityId());
        this.getFrequencyHoppingFacade(snmpParam.getIpAddress()).clearDeviceGroupHopHis(snmpParam, chnlGroup);
        // 再删除数据库中的信道跳频历史记录
        frequencyHoppingDao.deleteGroupHopHis(chnlGroup.getEntityId(), chnlGroup.getChannelIndex());
    }

    @Override
    public void refreshGpHopHisFromDevice(CcmtsSpectrumGpHopHis hopHis) {
        // 先从设备获取数据
        snmpParam = this.getSnmpParamByEntityId(hopHis.getEntityId());
        List<CcmtsSpectrumGpHopHis> hopHisList = this.getFrequencyHoppingFacade(snmpParam.getIpAddress())
                .getGroupHopHisFromDevice(snmpParam);
        List<CcmtsSpectrumGpHopHis> channelHopHisList = new ArrayList<CcmtsSpectrumGpHopHis>();
        for (CcmtsSpectrumGpHopHis deviceHopHis : hopHisList) {
            if (deviceHopHis.getCmcMac().equals(hopHis.getCmcMac())
                    && deviceHopHis.getChnlId().equals(hopHis.getChnlId())) {
                deviceHopHis.setEntityId(hopHis.getEntityId());
                deviceHopHis.setChannelIndex(hopHis.getChannelIndex());
                channelHopHisList.add(deviceHopHis);
            }
        }
        // 再将获取的数据插入数据库
        frequencyHoppingDao.batchInsertGroupHopHis(channelHopHisList, hopHis.getEntityId(), hopHis.getChannelIndex());
    }

    @Override
    public List<CcmtsSpectrumGp> getAllDeviceGroup(Long entityId) {
        return frequencyHoppingDao.queryAllDeviceGroup(entityId);
    }

    @Override
    public void deleteDeviceGroup(Long entityId, Integer groupId) {
        snmpParam = getSnmpParamByEntityId(entityId);
        CcmtsSpectrumGp ccmtsSpectrumGp = new CcmtsSpectrumGp();
        ccmtsSpectrumGp.setEntityId(entityId);
        ccmtsSpectrumGp.setGroupId(groupId);
        getFrequencyHoppingFacade(snmpParam.getIpAddress()).destoryGroupFromDevice(snmpParam, ccmtsSpectrumGp);
        frequencyHoppingDao.deleteDeviceGroup(entityId, groupId);
        // 删除跳频组的同时，将已经应用了此跳频组信道的关联跳频组ID设置为零
        frequencyHoppingDao.updateChlGroupToZero(entityId, groupId);
    }

    @Override
    public void refreshGroupFromDevice(Long entityId) {
        snmpParam = getSnmpParamByEntityId(entityId);
        // 获取跳频组信息并插入数据库
        List<CcmtsSpectrumGp> ccmtsSpectrumGpList = new ArrayList<CcmtsSpectrumGp>();
        ccmtsSpectrumGpList = getFrequencyHoppingFacade(snmpParam.getIpAddress()).getGroupFromDevice(snmpParam);
        frequencyHoppingDao.batchInsertDeviceGroup(ccmtsSpectrumGpList, entityId);
        // 获取跳频组成员频点信息并插入数据库
        List<CcmtsSpectrumGpFreq> ccmtsSpectrumGpFreqList = new ArrayList<CcmtsSpectrumGpFreq>();
        ccmtsSpectrumGpFreqList = getFrequencyHoppingFacade(snmpParam.getIpAddress()).getGroupFreqFromDevice(snmpParam);
        frequencyHoppingDao.batchInsertSpectrumGroupFreq(ccmtsSpectrumGpFreqList, entityId);
    }

    @Override
    public void addDeviceGroup(CcmtsSpectrumGp ccmtsSpectrumGp) {
        snmpParam = getSnmpParamByEntityId(ccmtsSpectrumGp.getEntityId());
        // 在设备侧新增跳频组并插入数据库
        getFrequencyHoppingFacade(snmpParam.getIpAddress()).createGroupToDevice(snmpParam, ccmtsSpectrumGp);
        frequencyHoppingDao.insertDeviceGroup(ccmtsSpectrumGp);
        // 在设备侧新增跳频组成员频点并插入数据库
        for (CcmtsSpectrumGpFreq ccmtsSpectrumGpFreq : ccmtsSpectrumGp.getCcmtsSpectrumGpFreqList()) {
            getFrequencyHoppingFacade(snmpParam.getIpAddress()).createGroupFreqToDevice(snmpParam, ccmtsSpectrumGpFreq);
            frequencyHoppingDao.insertSpectrumGroupFreq(ccmtsSpectrumGpFreq);
        }
    }

    @Override
    public CcmtsSpectrumGp getDeviceGroup(Long entityId, Integer groupId) {
        CcmtsSpectrumGp ccmtsSpectrumGp = new CcmtsSpectrumGp();
        ccmtsSpectrumGp = frequencyHoppingDao.queryDeviceGroup(entityId, groupId);
        List<CcmtsSpectrumGpFreq> ccmtsSpectrumGpFreqList = frequencyHoppingDao.querySpectrumGroupFreqList(entityId,
                groupId);
        ccmtsSpectrumGp.setCcmtsSpectrumGpFreqList(ccmtsSpectrumGpFreqList);
        return ccmtsSpectrumGp;
    }

    @Override
    public void modifyDeviceGroup(CcmtsSpectrumGp ccmtsSpectrumGp) {
        // 在设备侧修改跳频组成员频点并修改数据库,由于修改成员频点涉及到增删改，故将一个跳频组的成员频点删除了再插入
        deleteDeviceGroup(ccmtsSpectrumGp.getEntityId(), ccmtsSpectrumGp.getGroupId());
        addDeviceGroup(ccmtsSpectrumGp);
    }

    @Override
    public List<CcmtsSpectrumGpTemplate> getSpectrumGpTempLateList(Map<String, Object> map) {
        return frequencyHoppingDao.querySpectrumGpTempLateList(map);
    }

    @Override
    public void addSpectrumGpTempLate(CcmtsSpectrumGpTemplate gpTemplate, String selectGroup) {
        Long tempLateId = frequencyHoppingDao.insertSpectrumGpTempLate(gpTemplate);
        List<SpectrumTempGpRelation> relationList = new ArrayList<SpectrumTempGpRelation>();
        // selectGroup内部结构为"emsGroupId_groupId,emsGroupId_groupId,..."形式
        String[] tempGpList = selectGroup.split(Symbol.COMMA);
        SpectrumTempGpRelation newRelation = null;
        for (String tempGp : tempGpList) {
            String[] ids = tempGp.split(Symbol.UNDERLINE);
            if (!Symbol.EMPTY_STRING.equals(ids[0])) {
                newRelation = new SpectrumTempGpRelation();
                newRelation.setTempLateId(tempLateId);
                newRelation.setEmsGroupId(Long.parseLong(ids[0]));
                newRelation.setGroupId(Integer.parseInt(ids[1]));
                relationList.add(newRelation);
            }
        }
        frequencyHoppingDao.batchInsertTempGpRelation(relationList);
    }

    @Override
    public CcmtsSpectrumGpTemplate getSpectrumGpTempLateById(Long templateId) {
        return frequencyHoppingDao.querySpectrumGpTempLateById(templateId);
    }

    @Override
    public void txModifySpectrumGpTempLate(CcmtsSpectrumGpTemplate gpTemplate, String selectGroup) {
        frequencyHoppingDao.updateSpectrumGpTempLate(gpTemplate);
        // 更新自动跳频模板与全局跳频组关联关系,先删除再添加
        frequencyHoppingDao.deleteRelByTemplateId(gpTemplate.getTempLateId());
        // 添加新的关联关系
        List<SpectrumTempGpRelation> relationList = new ArrayList<SpectrumTempGpRelation>();
        // selectGroup内部结构为"emsGroupId_groupId,emsGroupId_groupId,..."形式
        String[] tempGpList = selectGroup.split(Symbol.COMMA);
        SpectrumTempGpRelation newRelation = null;
        for (String tempGp : tempGpList) {
            String[] ids = tempGp.split(Symbol.UNDERLINE);
            if (!Symbol.EMPTY_STRING.equals(ids[0])) {
                newRelation = new SpectrumTempGpRelation();
                newRelation.setTempLateId(gpTemplate.getTempLateId());
                newRelation.setEmsGroupId(Long.parseLong(ids[0]));
                newRelation.setGroupId(Integer.parseInt(ids[1]));
                relationList.add(newRelation);
            }
        }
        frequencyHoppingDao.batchInsertTempGpRelation(relationList);
    }

    @Override
    public void deleteSpectrumGpTempLate(Long templateId) {
        frequencyHoppingDao.deleteSpectrumGpTempLate(templateId);
    }

    @Override
    public List<EmsCcmtsSpectrumGp> getGlobalSpectrumGpList() {
        return frequencyHoppingDao.queryGlobalSpectrumGpList();
    }

    @Override
    public void deleteGlobalSpectrumGp(List<Long> groupIds) {
        frequencyHoppingDao.batchDeleteGlobalGroup(groupIds);
    }

    @Override
    public List<EmsCcmtsSpectrumGp> getTempRelationGp(Long templateId) {
        return frequencyHoppingDao.selectTempRelationGp(templateId);
    }

    @Override
    public void addGlobalSpectrumGp(EmsCcmtsSpectrumGp emsCcmtsSpectrumGp) {
        Long emsGroupId = frequencyHoppingDao.insertGlobalSpectrumGp(emsCcmtsSpectrumGp);
        for (EmsCcmtsSpectrumGpFreq freq : emsCcmtsSpectrumGp.getEmsCcmtsSpectrumGpFreqList()) {
            freq.setEmsGroupId(emsGroupId);
        }
        frequencyHoppingDao.batchInsertGlobalSpectrumGpFreq(emsCcmtsSpectrumGp.getEmsCcmtsSpectrumGpFreqList());
    }

    @Override
    public EmsCcmtsSpectrumGp getGlobalSpectrumGpById(Long emsGroupId) {
        EmsCcmtsSpectrumGp emsCcmtsSpectrumGp = new EmsCcmtsSpectrumGp();
        emsCcmtsSpectrumGp = frequencyHoppingDao.queryGlobalSpectrumGpById(emsGroupId);

        List<EmsCcmtsSpectrumGpFreq> emsCcmtsSpectrumGpFreqList = new ArrayList<EmsCcmtsSpectrumGpFreq>();
        emsCcmtsSpectrumGpFreqList = frequencyHoppingDao.queryGlobalSpectrumGpFreqById(emsGroupId);

        emsCcmtsSpectrumGp.setEmsCcmtsSpectrumGpFreqList(emsCcmtsSpectrumGpFreqList);
        return emsCcmtsSpectrumGp;
    }

    @Override
    public void modifyGlobalSpectrumGp(EmsCcmtsSpectrumGp emsCcmtsSpectrumGp) {
        frequencyHoppingDao.updateGlobalSpectrumGp(emsCcmtsSpectrumGp);
        frequencyHoppingDao.batchUpdateGlobalSpectrumGpFreq(emsCcmtsSpectrumGp.getEmsCcmtsSpectrumGpFreqList(),
                emsCcmtsSpectrumGp.getEmsGroupId());
    }

    @Override
    public void transGroupFromDeviceToGlobal(Long entityId, List<Long> groupIds) {
        Entity entity = entityDao.selectByPrimaryKey(entityId);
        for (Long groupId : groupIds) {
            // 获取设备跳频组
            CcmtsSpectrumGp ccmtsSpectrumGp = frequencyHoppingDao.queryDeviceGroup(entityId,
                    Integer.parseInt(groupId.toString()));
            // 转换成全局跳频组
            EmsCcmtsSpectrumGp emsCcmtsSpectrumGp = new EmsCcmtsSpectrumGp();

            // 封装自动生成的groupName,规则为设备名+groupId+时间
            StringBuffer sBuffer = new StringBuffer();
            sBuffer.append(entity.getName()).append(" Group ").append(groupId).append(" ")
                    .append(dateformat.format(new Date()));

            emsCcmtsSpectrumGp.setEmsGroupName(sBuffer.toString());
            emsCcmtsSpectrumGp.setAdminStatus(ccmtsSpectrumGp.getAdminStatus());
            emsCcmtsSpectrumGp.setFecThresCorrect1(ccmtsSpectrumGp.getFecThresCorrect1());
            emsCcmtsSpectrumGp.setFecThresCorrect2(ccmtsSpectrumGp.getFecThresCorrect2());
            emsCcmtsSpectrumGp.setFecThresUnCorrect1(ccmtsSpectrumGp.getFecThresUnCorrect1());
            emsCcmtsSpectrumGp.setFecThresUnCorrect2(ccmtsSpectrumGp.getFecThresUnCorrect2());
            emsCcmtsSpectrumGp.setGroupPolicy(ccmtsSpectrumGp.getGroupPolicy());
            emsCcmtsSpectrumGp.setGroupPriority1st(ccmtsSpectrumGp.getGroupPriority1st());
            emsCcmtsSpectrumGp.setGroupPriority2st(ccmtsSpectrumGp.getGroupPriority2st());
            emsCcmtsSpectrumGp.setGroupPriority3st(ccmtsSpectrumGp.getGroupPriority3st());
            emsCcmtsSpectrumGp.setHopPeriod(ccmtsSpectrumGp.getHopPeriod());
            emsCcmtsSpectrumGp.setMaxHopLimit(ccmtsSpectrumGp.getMaxHopLimit());
            emsCcmtsSpectrumGp.setSnrThres1(ccmtsSpectrumGp.getSnrThres1());
            emsCcmtsSpectrumGp.setSnrThres2(ccmtsSpectrumGp.getSnrThres2());

            // 全局跳频组信息插入数据库,返回自动生成的emsgroupId
            Long emsGroupId = frequencyHoppingDao.insertGlobalSpectrumGp(emsCcmtsSpectrumGp);

            // 获取设备跳频组成员频点信息
            List<CcmtsSpectrumGpFreq> ccmtsSpectrumGpFreqList = frequencyHoppingDao.querySpectrumGroupFreqList(
                    entityId, Integer.parseInt(groupId.toString()));
            // 转换成全局跳频组成员频点
            List<EmsCcmtsSpectrumGpFreq> emsCcmtsSpectrumGpFreqList = new ArrayList<EmsCcmtsSpectrumGpFreq>();
            for (CcmtsSpectrumGpFreq ccmtsSpectrumGpFreq : ccmtsSpectrumGpFreqList) {
                EmsCcmtsSpectrumGpFreq emsCcmtsSpectrumGpFreq = new EmsCcmtsSpectrumGpFreq();
                emsCcmtsSpectrumGpFreq.setEmsGroupId(emsGroupId);
                emsCcmtsSpectrumGpFreq.setFreqFrequency(ccmtsSpectrumGpFreq.getFreqFrequency());
                emsCcmtsSpectrumGpFreq.setFreqIndex(ccmtsSpectrumGpFreq.getFreqIndex());
                emsCcmtsSpectrumGpFreq.setFreqMaxWidth(ccmtsSpectrumGpFreq.getFreqMaxWidth());
                emsCcmtsSpectrumGpFreq.setFreqPower(ccmtsSpectrumGpFreq.getFreqPower());
                emsCcmtsSpectrumGpFreqList.add(emsCcmtsSpectrumGpFreq);
            }

            // 全局跳频组成员频点插入数据库
            frequencyHoppingDao.batchInsertGlobalSpectrumGpFreq(emsCcmtsSpectrumGpFreqList);
        }

    }

    @Override
    public void addDeviceGroupFromGlobal(Long entityId, Long emsGroupId, Integer groupId) {
        // 获取全局跳频组
        EmsCcmtsSpectrumGp emsCcmtsSpectrumGp = frequencyHoppingDao.queryGlobalSpectrumGpById(emsGroupId);
        // 转换为设备跳频组
        CcmtsSpectrumGp ccmtsSpectrumGp = new CcmtsSpectrumGp();
        ccmtsSpectrumGp.setAdminStatus(emsCcmtsSpectrumGp.getAdminStatus());
        ccmtsSpectrumGp.setEntityId(entityId);
        ccmtsSpectrumGp.setFecThresCorrect1(emsCcmtsSpectrumGp.getFecThresCorrect1());
        ccmtsSpectrumGp.setFecThresCorrect2(emsCcmtsSpectrumGp.getFecThresCorrect2());
        ccmtsSpectrumGp.setFecThresUnCorrect1(emsCcmtsSpectrumGp.getFecThresUnCorrect1());
        ccmtsSpectrumGp.setFecThresUnCorrect2(emsCcmtsSpectrumGp.getFecThresUnCorrect2());
        ccmtsSpectrumGp.setGroupId(groupId);
        ccmtsSpectrumGp.setGroupPolicy(emsCcmtsSpectrumGp.getGroupPolicy());
        ccmtsSpectrumGp.setGroupPriority1st(emsCcmtsSpectrumGp.getGroupPriority1st());
        ccmtsSpectrumGp.setGroupPriority2st(emsCcmtsSpectrumGp.getGroupPriority2st());
        ccmtsSpectrumGp.setGroupPriority3st(emsCcmtsSpectrumGp.getGroupPriority3st());
        ccmtsSpectrumGp.setHopPeriod(emsCcmtsSpectrumGp.getHopPeriod());
        ccmtsSpectrumGp.setMaxHopLimit(emsCcmtsSpectrumGp.getMaxHopLimit());
        ccmtsSpectrumGp.setSnrThres1(emsCcmtsSpectrumGp.getSnrThres1());
        ccmtsSpectrumGp.setSnrThres2(emsCcmtsSpectrumGp.getSnrThres2());
        // 获取全局跳频组成员频点
        List<EmsCcmtsSpectrumGpFreq> emsCcmtsSpectrumGpFreqList = frequencyHoppingDao
                .queryGlobalSpectrumGpFreqById(emsGroupId);
        // 转换为设备跳频组成员频点
        List<CcmtsSpectrumGpFreq> ccmtsSpectrumGpFreqList = new ArrayList<CcmtsSpectrumGpFreq>();
        for (EmsCcmtsSpectrumGpFreq emsCcmtsSpectrumGpFreq : emsCcmtsSpectrumGpFreqList) {
            CcmtsSpectrumGpFreq ccmtsSpectrumGpFreq = new CcmtsSpectrumGpFreq();
            ccmtsSpectrumGpFreq.setEntityId(entityId);
            ccmtsSpectrumGpFreq.setFreqFrequency(emsCcmtsSpectrumGpFreq.getFreqFrequency());
            ccmtsSpectrumGpFreq.setFreqIndex(emsCcmtsSpectrumGpFreq.getFreqIndex());
            ccmtsSpectrumGpFreq.setFreqMaxWidth(emsCcmtsSpectrumGpFreq.getFreqMaxWidth());
            ccmtsSpectrumGpFreq.setFreqPower(emsCcmtsSpectrumGpFreq.getFreqPower());
            ccmtsSpectrumGpFreq.setGroupId(groupId);
            ccmtsSpectrumGpFreqList.add(ccmtsSpectrumGpFreq);
        }
        ccmtsSpectrumGp.setCcmtsSpectrumGpFreqList(ccmtsSpectrumGpFreqList);
        addDeviceGroup(ccmtsSpectrumGp);
    }

    @Override
    public void executeSpectrumGroupConfig(List<Long> entityIdList, CcmtsSpectrumGpTemplate template) {
        final int QUENE_HEAD_INDEX = 0;
        final int MAX_SUB_QUENE_SIZE = 5;
        int configEntityListSize = entityIdList.size();
        while (!entityIdList.isEmpty()) {
            List<Long> subQuene = new ArrayList<Long>();
            int queneCounter = 0;
            while (!entityIdList.isEmpty() && queneCounter < MAX_SUB_QUENE_SIZE) {
                subQuene.add(entityIdList.remove(QUENE_HEAD_INDEX));
            }
            // 保证第一个参数唯一，考虑多个用户同时下发的情况
            Iterator<Long> it = subQuene.iterator();
            int baseTime = 10;
            int ct = 0;

            // 配置记录插入网管数据库
            SpectrumTempConfigLog spectrumTempConfigLog = new SpectrumTempConfigLog();
            spectrumTempConfigLog.setConfigStatus(0);
            spectrumTempConfigLog.setConfigTime(new Date());
            spectrumTempConfigLog.setDeviceNum(configEntityListSize);
            spectrumTempConfigLog.setTempLateId(template.getTempLateId());
            spectrumTempConfigLog.setTemplateName(template.getTemplateName());
            spectrumTempConfigLog.setUserId(CurrentRequest.getCurrentUser().getUserId());
            spectrumTempConfigLog.setUserName(CurrentRequest.getCurrentUser().getUser().getUserName());
            Long configLogId = frequencyHoppingDao.insertTempConfigLog(spectrumTempConfigLog);

            while (it.hasNext()) {
                try {
                    Long entityId = it.next();
                    String jobAndTriKeyId = "sg-" + entityId;
                    String jobAndTriGroup = "SpectrumGroup";
                    JobKey jobKey = new JobKey(jobAndTriKeyId, jobAndTriGroup);
                    JobDetail job = newJob(FrequencyHoppingWorker.class).withIdentity(jobKey).build();
                    job.getJobDataMap().put("SpectrumGroup.entityId", entityId);
                    job.getJobDataMap().put("SpectrumGroup.template", template);
                    job.getJobDataMap().put("SpectrumGroup.configLogId", configLogId);
                    job.getJobDataMap().put("beanFactory", beanFactory);
                    Trigger trg = newTrigger().withIdentity(jobAndTriKeyId, jobAndTriGroup)
                            .startAt(futureDate(baseTime * ct++, IntervalUnit.SECOND)) // 10秒以后执行
                            .build();
                    schedulerService.scheduleJob(job, trg);
                } catch (SchedulerException e) {
                    logger.debug(e.getMessage(), e);
                } catch (Exception e) {
                    logger.debug(e.getMessage(), e);
                }
            }
        }
    }

    @Override
    public CcmtsSpectrumGpChnl getChnlGroupInfo(Long entityId, Long channelIndex) {
        return frequencyHoppingDao.queryChnlGroupInfo(entityId, channelIndex);
    }

    @Override
    public void refreshChnlGroupFromDevice(Long cmcId, CcmtsSpectrumGpChnl chnlGroup) {
        // 先从设备获取数据
        snmpParam = this.getSnmpParamByEntityId(chnlGroup.getEntityId());
        List<CcmtsSpectrumGpChnl> chnlGpList = this.getFrequencyHoppingFacade(snmpParam.getIpAddress())
                .getChnlGroupFromDevice(snmpParam);
        List<CcmtsSpectrumGpChnl> newGpChnlList = new ArrayList<CcmtsSpectrumGpChnl>();
        if (!chnlGpList.isEmpty()) {
            for (CcmtsSpectrumGpChnl deviceChnlGp : chnlGpList) {
                if (deviceChnlGp.getCmcMacForDevice().toString().equals(chnlGroup.getChnlCmcMac())) {
                    CmcEntityRelation cmcEntityRel = cmcDao.getCmcEntityRelationByCmcId(cmcId);
                    Long channelIndex = CmcIndexUtils.buildChannelIndex(cmcEntityRel.getCmcIndex(),
                            CmcConstants.CHANNEL_TYPE_UP, deviceChnlGp.getChnlId().longValue());
                    deviceChnlGp.setEntityId(chnlGroup.getEntityId());
                    deviceChnlGp.setChnlCmcMac(chnlGroup.getChnlCmcMac());
                    deviceChnlGp.setChannelIndex(channelIndex);
                    newGpChnlList.add(deviceChnlGp);
                }
            }
        }
        // 获取数据成功后先删除原来的信道跳频组
        frequencyHoppingDao.deleteChnlGroupInfo(chnlGroup);
        // 再添加新的信道跳频组
        frequencyHoppingDao.batchInsertChnlGroup(newGpChnlList);
    }

    @Override
    public void modifyChnlGroupInfo(CcmtsSpectrumGpChnl chnlGroup) {
        chnlGroup.setCmcMacForDevice(new PhysAddress(chnlGroup.getChnlCmcMac()));
        // 先更新设备上信道跳频组信息
        snmpParam = this.getSnmpParamByEntityId(chnlGroup.getEntityId());
        this.getFrequencyHoppingFacade(snmpParam.getIpAddress()).setChnlGroupToDevice(snmpParam, chnlGroup);
        // 再更新数据库
        frequencyHoppingDao.updateChnlGroupInfo(chnlGroup);
    }

    @Override
    public void modifyGpChnlResetToStatic(CcmtsSpectrumGpChnl chnlGroup) {
        snmpParam = this.getSnmpParamByEntityId(chnlGroup.getEntityId());
        this.getFrequencyHoppingFacade(snmpParam.getIpAddress()).setChnlGroupToDevice(snmpParam, chnlGroup);
    }

    @Override
    public List<SpectrumTempConfigLog> getConfigLogList(Map<String, Object> map) {
        return frequencyHoppingDao.queryConfigLogList(map);
    }

    @Override
    public List<SpectrumTempConfigLogDetail> getLogDetailList(Map<String, Object> map) {
        return frequencyHoppingDao.queryLogDetailList(map);
    }

    @Override
    public int getLogCount() {
        return frequencyHoppingDao.queryLogCount();
    }

    @Override
    public int getDetailCount(Long configLogId) {
        return frequencyHoppingDao.queryDetailCount(configLogId);
    }

    @Override
    public int getTempCount() {
        return frequencyHoppingDao.queryTempCount();
    }

    @Override
    public String getGroupChlNum(Long entityId, Integer groupId) {
        return frequencyHoppingDao.queryGroupChlNum(entityId, groupId);
    }

    @Override
    public List<EmsCcmtsSpectrumGp> getGlobalSpectrumGpList(Map<String, Object> map) {
        return frequencyHoppingDao.queryGlobalSpectrumGpList(map);
    }

    @Override
    public Integer getGlobalSpectrumGpNum() {
        return frequencyHoppingDao.queryGlobalSpectrumGpNum();
    }

    @Override
    public List<EmsCcmtsSpectrumGp> getAllGlobalGroup() {
        return frequencyHoppingDao.queryAllGlobalGroup();
    }

    @SuppressWarnings("unused")
    private void refreshChnlGroup(Long entityId) {
        snmpParam = this.getSnmpParamByEntityId(entityId);
        Long cmcIndex = cmcDao.getCmcIndexByCmcId(entityId);
        List<CcmtsSpectrumGpChnl> chnlGpList = getFrequencyHoppingFacade(snmpParam.getIpAddress())
                .getChnlGroupFromDevice(snmpParam);
        if (chnlGpList != null) {
            try {
                for (CcmtsSpectrumGpChnl chnlGroup : chnlGpList) {
                    Long channelIndex = CmcIndexUtils.buildChannelIndex(cmcIndex, CmcConstants.CHANNEL_TYPE_UP,
                            chnlGroup.getChnlId().longValue());
                    chnlGroup.setEntityId(entityId);
                    chnlGroup.setChannelIndex(channelIndex);
                    chnlGroup.setChnlCmcMac(chnlGroup.getCmcMacForDevice().toString());
                }
                frequencyHoppingDao.delChnlGpByEntityId(entityId);
                frequencyHoppingDao.batchInsertChnlGroup(chnlGpList);
            } catch (Exception e) {
                logger.error("Syn CcmtsSpectrumGpChnlList error ", e);
            }
        }
    }

    @SuppressWarnings("unused")
    private void refreshGpHopHis(Long entityId) {
        snmpParam = this.getSnmpParamByEntityId(entityId);
        Long cmcIndex = cmcDao.getCmcIndexByCmcId(entityId);
        List<CcmtsSpectrumGpHopHis> gpHopHisList = getFrequencyHoppingFacade(snmpParam.getIpAddress())
                .getGroupHopHisFromDevice(snmpParam);
        if (gpHopHisList != null) {
            try {
                Long channelIndex = null;
                if (!gpHopHisList.isEmpty()) {
                    channelIndex = CmcIndexUtils.buildChannelIndex(cmcIndex, CmcConstants.CHANNEL_TYPE_UP, gpHopHisList
                            .get(0).getChnlId().longValue());
                }
                frequencyHoppingDao.batchInsertGroupHopHis(gpHopHisList, entityId, channelIndex);
            } catch (Exception e) {
                logger.error("Syn CcmtsSpectrumGpChnlList error ", e);
            }
        }
    }

}