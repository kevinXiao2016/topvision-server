/***********************************************************************
 * $Id: CcmtsSpectrumGroupWorker.java,v1.0 2013-8-16 上午10:55:43 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.frequencyhopping.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;

import com.topvision.ems.cmc.ccmts.dao.CmcDao;
import com.topvision.ems.cmc.ccmts.dao.CmcListDao;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.framework.constants.Symbol;
import com.topvision.ems.cmc.frequencyhopping.dao.FrequencyHoppingDao;
import com.topvision.ems.cmc.frequencyhopping.domain.CcmtsSpectrumGp;
import com.topvision.ems.cmc.frequencyhopping.domain.CcmtsSpectrumGpChnl;
import com.topvision.ems.cmc.frequencyhopping.domain.CcmtsSpectrumGpGlobal;
import com.topvision.ems.cmc.frequencyhopping.domain.CcmtsSpectrumGpTemplate;
import com.topvision.ems.cmc.frequencyhopping.domain.EmsCcmtsSpectrumGp;
import com.topvision.ems.cmc.frequencyhopping.domain.EmsCcmtsSpectrumGpFreq;
import com.topvision.ems.cmc.frequencyhopping.domain.SpectrumTempConfigLogDetail;
import com.topvision.ems.cmc.frequencyhopping.service.FrequencyHoppingService;
import com.topvision.ems.cmc.util.ResourcesUtil;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.message.Message;
import com.topvision.ems.message.MessagePusher;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.template.service.EntityTypeService;

/**
 * @author haojie
 * @created @2013-8-16-上午10:55:43
 * 
 */
public class FrequencyHoppingWorker implements Job {
    private Logger logger = LoggerFactory.getLogger(FrequencyHoppingWorker.class);
    private FrequencyHoppingService frequencyHoppingService;
    private FrequencyHoppingDao frequencyHoppingDao;
    private EntityService entityService;
    private EntityTypeService entityTypeService;
    private CmcDao cmcDao;
    private CmcListDao cmcListDao;
    private final int NO_GROUP_CHANNEL = 0;
    private final int CONFIG_SUCCESS = 1;
    private final int CONFIG_FAIL = 0;
    private MessagePusher pusher;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap jobMap = context.getJobDetail().getJobDataMap();
        Long entityId = (Long) jobMap.get("SpectrumGroup.entityId");
        CcmtsSpectrumGpTemplate template = (CcmtsSpectrumGpTemplate) jobMap.get("SpectrumGroup.template");
        Long configLogId = (Long) jobMap.get("SpectrumGroup.configLogId");
        BeanFactory beanFactory = (BeanFactory) jobMap.get("beanFactory");
        frequencyHoppingDao = (FrequencyHoppingDao) beanFactory.getBean("frequencyHoppingDao");
        frequencyHoppingService = (FrequencyHoppingService) beanFactory.getBean("frequencyHoppingService");
        entityService = (EntityService) beanFactory.getBean("entityService");
        entityTypeService = (EntityTypeService) beanFactory.getBean("entityTypeService");
        cmcDao = (CmcDao) beanFactory.getBean("cmcDao");
        pusher = (MessagePusher) beanFactory.getBean("messagePusher");
        Entity entity = entityService.getEntity(entityId);

        List<SpectrumTempConfigLogDetail> spectrumTempConfigLogDetails = new ArrayList<SpectrumTempConfigLogDetail>();

        Integer result = CONFIG_FAIL;// 默认配置结果，失败
        if (entityTypeService.isCcmtsWithAgent(entity.getTypeId())) {
            // 解除信道关联跳频组
            for (int i = 0; i < 4; i++) {
                CcmtsSpectrumGpChnl ccmtsSpectrumGpChnl = new CcmtsSpectrumGpChnl();
                ccmtsSpectrumGpChnl.setChnlCmcMac(entity.getMac());
                ccmtsSpectrumGpChnl.setEntityId(entityId);
                ccmtsSpectrumGpChnl.setChnlId(i + 1);
                ccmtsSpectrumGpChnl.setChnlGroupId(NO_GROUP_CHANNEL);
                result = CONFIG_FAIL;
                try {
                    frequencyHoppingService.modifyChnlGroupInfo(ccmtsSpectrumGpChnl);
                    result = CONFIG_SUCCESS;
                } catch (Exception e) {
                    logger.debug("modifyChnlGroupInfo errer", e);
                } finally {
                    // 设置结果存入数据库
                    SpectrumTempConfigLogDetail spectrumTempConfigLogDetail = new SpectrumTempConfigLogDetail();
                    spectrumTempConfigLogDetail.setConfigLogId(configLogId);
                    spectrumTempConfigLogDetail.setTemplateName(template.getTemplateName());
                    spectrumTempConfigLogDetail.setConfigOperation(ResourcesUtil.getString("CMC.GP.releasegroup"));
                    spectrumTempConfigLogDetail.setConfigResult(result);
                    spectrumTempConfigLogDetail.setConfigTime(new Date());
                    int channelId = i + 1;
                    spectrumTempConfigLogDetail.setConfigUnit(entity.getName() + Symbol.MIDDLE_LINE
                            + ResourcesUtil.getString("CHANNEL.channel") + channelId);
                    spectrumTempConfigLogDetails.add(spectrumTempConfigLogDetail);
                }
            }

            // 删除设备跳频组

            // 刷新跳频组信息的目的是为了保证网管数据库和设备上保持一致，如果不一致，则网管数据是没有意义的
            result = CONFIG_FAIL;
            try {
                frequencyHoppingService.refreshGroupFromDevice(entityId);
                result = CONFIG_SUCCESS;
            } catch (Exception e) {
                logger.debug("refreshGroupFromDevice errer", e);
            } finally {
                // 设置结果存入数据库
                SpectrumTempConfigLogDetail spectrumTempConfigLogDetail = new SpectrumTempConfigLogDetail();
                spectrumTempConfigLogDetail.setConfigLogId(configLogId);
                spectrumTempConfigLogDetail.setTemplateName(template.getTemplateName());
                spectrumTempConfigLogDetail.setConfigOperation(ResourcesUtil.getString("CMC.GP.refreshGroup"));
                spectrumTempConfigLogDetail.setConfigResult(result);
                spectrumTempConfigLogDetail.setConfigTime(new Date());
                spectrumTempConfigLogDetail.setConfigUnit(entity.getName());
                spectrumTempConfigLogDetails.add(spectrumTempConfigLogDetail);
            }
            List<CcmtsSpectrumGp> ccmtsSpectrumGps = frequencyHoppingDao.queryDeviceGroupList(entityId);
            if (ccmtsSpectrumGps != null) {
                for (CcmtsSpectrumGp ccmtsSpectrumGp : ccmtsSpectrumGps) {
                    result = CONFIG_FAIL;
                    try {
                        // 这里要确认一下设备上删除跳频组是否连带成员频点信息一并删除
                        frequencyHoppingService.deleteDeviceGroup(entityId, ccmtsSpectrumGp.getGroupId());
                        result = CONFIG_SUCCESS;
                    } catch (Exception e) {
                        logger.debug("deleteDeviceGroup errer", e);
                    } finally {
                        // 设置结果存入数据库
                        SpectrumTempConfigLogDetail spectrumTempConfigLogDetail = new SpectrumTempConfigLogDetail();
                        spectrumTempConfigLogDetail.setConfigLogId(configLogId);
                        spectrumTempConfigLogDetail.setTemplateName(template.getTemplateName());
                        spectrumTempConfigLogDetail.setConfigOperation(ResourcesUtil.getString("CMC.GP.deleteGroup")
                                + ccmtsSpectrumGp.getGroupId());
                        spectrumTempConfigLogDetail.setConfigResult(result);
                        spectrumTempConfigLogDetail.setConfigTime(new Date());
                        spectrumTempConfigLogDetail.setConfigUnit(entity.getName());
                        spectrumTempConfigLogDetails.add(spectrumTempConfigLogDetail);
                    }
                }
            }

            // 下发全局配置
            CcmtsSpectrumGpGlobal ccmtsSpectrumGpGlobal = new CcmtsSpectrumGpGlobal();
            ccmtsSpectrumGpGlobal.setEntityId(entityId);
            ccmtsSpectrumGpGlobal.setGlobalAdminStatus(template.getGlobalAdminStatus());
            ccmtsSpectrumGpGlobal.setHopHisMaxCount(template.getHopHisMaxCount());
            ccmtsSpectrumGpGlobal.setSnrQueryPeriod(template.getSnrQueryPeriod());
            result = CONFIG_FAIL;
            try {
                frequencyHoppingService.modifyDeviceGpGlobal(ccmtsSpectrumGpGlobal);
                result = CONFIG_SUCCESS;
            } catch (Exception e) {
                logger.debug("modifyDeviceGpGlobal errer", e);
            } finally {
                // 设置结果存入数据库
                SpectrumTempConfigLogDetail spectrumTempConfigLogDetail = new SpectrumTempConfigLogDetail();
                spectrumTempConfigLogDetail.setConfigLogId(configLogId);
                spectrumTempConfigLogDetail.setTemplateName(template.getTemplateName());
                spectrumTempConfigLogDetail.setConfigOperation(ResourcesUtil.getString("CMC.GP.issuedglobal"));
                spectrumTempConfigLogDetail.setConfigResult(result);
                spectrumTempConfigLogDetail.setConfigTime(new Date());
                spectrumTempConfigLogDetail.setConfigUnit(entity.getName());
                spectrumTempConfigLogDetails.add(spectrumTempConfigLogDetail);
            }

            // 下发设备跳频组及成员频点信息
            List<EmsCcmtsSpectrumGp> emsCcmtsSpectrumGps = frequencyHoppingService.getTempRelationGp(template
                    .getTempLateId());
            if (emsCcmtsSpectrumGps != null) {
                for (EmsCcmtsSpectrumGp emsCcmtsSpectrumGp : emsCcmtsSpectrumGps) {
                    List<EmsCcmtsSpectrumGpFreq> emsCcmtsSpectrumGpFreqs = frequencyHoppingDao
                            .queryGlobalSpectrumGpFreqById(emsCcmtsSpectrumGp.getEmsGroupId());
                    emsCcmtsSpectrumGp.setEmsCcmtsSpectrumGpFreqList(emsCcmtsSpectrumGpFreqs);
                    result = CONFIG_FAIL;
                    try {
                        frequencyHoppingService.addDeviceGroupFromGlobal(entityId, emsCcmtsSpectrumGp.getEmsGroupId(),
                                emsCcmtsSpectrumGp.getDeviceGroupId());
                        result = CONFIG_SUCCESS;
                    } catch (Exception e) {
                        logger.debug("addDeviceGroupFromGlobal errer", e);
                    } finally {
                        // 设置结果存入数据库
                        SpectrumTempConfigLogDetail spectrumTempConfigLogDetail = new SpectrumTempConfigLogDetail();
                        spectrumTempConfigLogDetail.setConfigLogId(configLogId);
                        spectrumTempConfigLogDetail.setTemplateName(template.getTemplateName());
                        spectrumTempConfigLogDetail.setConfigOperation(ResourcesUtil.getString("CMC.GP.issuedgroup")
                                + emsCcmtsSpectrumGp.getEmsGroupName());
                        spectrumTempConfigLogDetail.setConfigResult(result);
                        spectrumTempConfigLogDetail.setConfigTime(new Date());
                        spectrumTempConfigLogDetail.setConfigUnit(entity.getName());
                        spectrumTempConfigLogDetails.add(spectrumTempConfigLogDetail);
                    }
                }
            }

            // 下发信道关联跳频组和备份调制方式
            for (int i = 0; i < 4; i++) {
                CcmtsSpectrumGpChnl ccmtsSpectrumGpChnl = new CcmtsSpectrumGpChnl();
                ccmtsSpectrumGpChnl.setChnlCmcMac(entity.getMac());
                ccmtsSpectrumGpChnl.setEntityId(entityId);
                ccmtsSpectrumGpChnl.setChnlId(i + 1);
                ccmtsSpectrumGpChnl.setChnlGroupId(template.getGpForUpChannel1());
                ccmtsSpectrumGpChnl.setChnlSecondaryProf(template.getChnlSecondaryProf());
                ccmtsSpectrumGpChnl.setChnlTertiaryProf(template.getChnlTertiaryProf());
                result = CONFIG_FAIL;
                try {
                    frequencyHoppingService.modifyChnlGroupInfo(ccmtsSpectrumGpChnl);
                    result = CONFIG_SUCCESS;
                } catch (Exception e) {
                    logger.debug("modifyChnlGroupInfo errer", e);
                } finally {
                    // 设置结果存入数据库
                    SpectrumTempConfigLogDetail spectrumTempConfigLogDetail = new SpectrumTempConfigLogDetail();
                    spectrumTempConfigLogDetail.setConfigLogId(configLogId);
                    spectrumTempConfigLogDetail.setTemplateName(template.getTemplateName());
                    spectrumTempConfigLogDetail.setConfigOperation(ResourcesUtil.getString("CMC.GP.issuedChlConf"));
                    spectrumTempConfigLogDetail.setConfigResult(result);
                    spectrumTempConfigLogDetail.setConfigTime(new Date());
                    int channelId = i + 1;
                    spectrumTempConfigLogDetail.setConfigUnit(entity.getName() + Symbol.MIDDLE_LINE
                            + ResourcesUtil.getString("CHANNEL.channel") + channelId);
                    spectrumTempConfigLogDetails.add(spectrumTempConfigLogDetail);
                }
            }
        } else if (entityTypeService.isOlt(entity.getTypeId())) {
            Map<String, Object> cmcQueryMap = new HashMap<String, Object>();
            cmcQueryMap.put("entityId", entityId);
            Long ccType = entityTypeService.getCcmtsType();
            cmcQueryMap.put("type", ccType);
            List<CmcAttribute> cmcAttributes = cmcListDao.queryCmcList(cmcQueryMap, 0, Integer.MAX_VALUE);
            // 解除信道关联跳频组
            if (cmcAttributes != null) {
                for (CmcAttribute cmcAttribute : cmcAttributes) {
                    for (int i = 0; i < 4; i++) {
                        CcmtsSpectrumGpChnl ccmtsSpectrumGpChnl = new CcmtsSpectrumGpChnl();
                        ccmtsSpectrumGpChnl.setChnlCmcMac(cmcAttribute.getTopCcmtsSysMacAddr());
                        ccmtsSpectrumGpChnl.setEntityId(entityId);
                        ccmtsSpectrumGpChnl.setChnlId(i + 1);
                        ccmtsSpectrumGpChnl.setChnlGroupId(NO_GROUP_CHANNEL);
                        result = CONFIG_FAIL;
                        try {
                            frequencyHoppingService.modifyChnlGroupInfo(ccmtsSpectrumGpChnl);
                            result = CONFIG_SUCCESS;
                        } catch (Exception e) {
                            logger.debug("modifyChnlGroupInfo", e);
                        } finally {
                            // 设置结果存入数据库
                            SpectrumTempConfigLogDetail spectrumTempConfigLogDetail = new SpectrumTempConfigLogDetail();
                            spectrumTempConfigLogDetail.setConfigLogId(configLogId);
                            spectrumTempConfigLogDetail.setTemplateName(template.getTemplateName());
                            spectrumTempConfigLogDetail.setConfigOperation(ResourcesUtil
                                    .getString("CMC.GP.releasegroup"));
                            spectrumTempConfigLogDetail.setConfigResult(result);
                            spectrumTempConfigLogDetail.setConfigTime(new Date());
                            int channelId = i + 1;
                            spectrumTempConfigLogDetail.setConfigUnit(entity.getName() + Symbol.MIDDLE_LINE
                                    + cmcAttribute.getNmName() + Symbol.MIDDLE_LINE
                                    + ResourcesUtil.getString("CHANNEL.channel") + channelId);
                            spectrumTempConfigLogDetails.add(spectrumTempConfigLogDetail);
                        }
                    }
                }
            }

            // 删除设备跳频组
            // 刷新跳频组信息的目的是为了保证网管数据库和设备上保持一致，如果不一致，则网管数据是没有意义的
            result = CONFIG_FAIL;
            try {
                frequencyHoppingService.refreshGroupFromDevice(entityId);
                result = CONFIG_SUCCESS;
            } catch (Exception e) {
                logger.debug("refreshGroupFromDevice errer", e);
            } finally {
                // 设置结果存入数据库
                SpectrumTempConfigLogDetail spectrumTempConfigLogDetail = new SpectrumTempConfigLogDetail();
                spectrumTempConfigLogDetail.setConfigLogId(configLogId);
                spectrumTempConfigLogDetail.setTemplateName(template.getTemplateName());
                spectrumTempConfigLogDetail.setConfigOperation(ResourcesUtil.getString("CMC.GP.refreshGroup"));
                spectrumTempConfigLogDetail.setConfigResult(result);
                spectrumTempConfigLogDetail.setConfigTime(new Date());
                spectrumTempConfigLogDetail.setConfigUnit(entity.getName());
                spectrumTempConfigLogDetails.add(spectrumTempConfigLogDetail);
            }
            List<CcmtsSpectrumGp> ccmtsSpectrumGps = frequencyHoppingDao.queryDeviceGroupList(entityId);
            if (ccmtsSpectrumGps != null) {
                for (CcmtsSpectrumGp ccmtsSpectrumGp : ccmtsSpectrumGps) {
                    result = CONFIG_FAIL;
                    try {
                        // 这里要确认一下设备上删除跳频组是否连带成员频点信息一并删除
                        frequencyHoppingService.deleteDeviceGroup(entityId, ccmtsSpectrumGp.getGroupId());
                        result = CONFIG_SUCCESS;
                    } catch (Exception e) {
                        logger.debug("deleteDeviceGroup errer", e);
                    } finally {
                        // 设置结果存入数据库
                        SpectrumTempConfigLogDetail spectrumTempConfigLogDetail = new SpectrumTempConfigLogDetail();
                        spectrumTempConfigLogDetail.setConfigLogId(configLogId);
                        spectrumTempConfigLogDetail.setTemplateName(template.getTemplateName());
                        spectrumTempConfigLogDetail.setConfigOperation(ResourcesUtil.getString("CMC.GP.deleteGroup")
                                + ccmtsSpectrumGp.getGroupId());
                        spectrumTempConfigLogDetail.setConfigResult(result);
                        spectrumTempConfigLogDetail.setConfigTime(new Date());
                        spectrumTempConfigLogDetail.setConfigUnit(entity.getName());
                        spectrumTempConfigLogDetails.add(spectrumTempConfigLogDetail);
                    }
                }
            }

            // 下发全局配置
            CcmtsSpectrumGpGlobal ccmtsSpectrumGpGlobal = new CcmtsSpectrumGpGlobal();
            ccmtsSpectrumGpGlobal.setEntityId(entityId);
            ccmtsSpectrumGpGlobal.setGlobalAdminStatus(template.getGlobalAdminStatus());
            ccmtsSpectrumGpGlobal.setHopHisMaxCount(template.getHopHisMaxCount());
            ccmtsSpectrumGpGlobal.setSnrQueryPeriod(template.getSnrQueryPeriod());
            result = CONFIG_FAIL;
            try {
                frequencyHoppingService.modifyDeviceGpGlobal(ccmtsSpectrumGpGlobal);
                result = CONFIG_SUCCESS;
            } catch (Exception e) {
                logger.debug("modifyDeviceGpGlobal errer", e);
            } finally {
                // 设置结果存入数据库
                SpectrumTempConfigLogDetail spectrumTempConfigLogDetail = new SpectrumTempConfigLogDetail();
                spectrumTempConfigLogDetail.setConfigLogId(configLogId);
                spectrumTempConfigLogDetail.setTemplateName(template.getTemplateName());
                spectrumTempConfigLogDetail.setConfigOperation(ResourcesUtil.getString("CMC.GP.issuedglobal"));
                spectrumTempConfigLogDetail.setConfigResult(result);
                spectrumTempConfigLogDetail.setConfigTime(new Date());
                spectrumTempConfigLogDetail.setConfigUnit(entity.getName());
                spectrumTempConfigLogDetails.add(spectrumTempConfigLogDetail);
            }

            // 下发设备跳频组及成员频点信息
            List<EmsCcmtsSpectrumGp> emsCcmtsSpectrumGps = frequencyHoppingService.getTempRelationGp(template
                    .getTempLateId());
            if (emsCcmtsSpectrumGps != null) {
                for (EmsCcmtsSpectrumGp emsCcmtsSpectrumGp : emsCcmtsSpectrumGps) {
                    List<EmsCcmtsSpectrumGpFreq> emsCcmtsSpectrumGpFreqs = frequencyHoppingDao
                            .queryGlobalSpectrumGpFreqById(emsCcmtsSpectrumGp.getEmsGroupId());
                    emsCcmtsSpectrumGp.setEmsCcmtsSpectrumGpFreqList(emsCcmtsSpectrumGpFreqs);
                    result = CONFIG_FAIL;
                    try {
                        frequencyHoppingService.addDeviceGroupFromGlobal(entityId, emsCcmtsSpectrumGp.getEmsGroupId(),
                                emsCcmtsSpectrumGp.getDeviceGroupId());
                        result = CONFIG_SUCCESS;
                    } catch (Exception e) {
                        logger.debug("addDeviceGroupFromGlobal errer", e);
                    } finally {
                        // 设置结果存入数据库
                        SpectrumTempConfigLogDetail spectrumTempConfigLogDetail = new SpectrumTempConfigLogDetail();
                        spectrumTempConfigLogDetail.setConfigLogId(configLogId);
                        spectrumTempConfigLogDetail.setTemplateName(template.getTemplateName());
                        spectrumTempConfigLogDetail.setConfigOperation(ResourcesUtil.getString("CMC.GP.issuedgroup")
                                + emsCcmtsSpectrumGp.getEmsGroupName());
                        spectrumTempConfigLogDetail.setConfigResult(result);
                        spectrumTempConfigLogDetail.setConfigTime(new Date());
                        spectrumTempConfigLogDetail.setConfigUnit(entity.getName());
                        spectrumTempConfigLogDetails.add(spectrumTempConfigLogDetail);
                    }
                }
            }

            // 下发信道关联跳频组和备份调制方式
            if (cmcAttributes != null) {
                for (CmcAttribute cmcAttribute : cmcAttributes) {
                    for (int i = 0; i < 4; i++) {
                        CcmtsSpectrumGpChnl ccmtsSpectrumGpChnl = new CcmtsSpectrumGpChnl();
                        ccmtsSpectrumGpChnl.setChnlCmcMac(cmcAttribute.getTopCcmtsSysMacAddr());
                        ccmtsSpectrumGpChnl.setChnlId(i + 1);
                        ccmtsSpectrumGpChnl.setEntityId(entityId);
                        ccmtsSpectrumGpChnl.setChnlGroupId(template.getGpForUpChannel1());
                        ccmtsSpectrumGpChnl.setChnlSecondaryProf(template.getChnlSecondaryProf());
                        ccmtsSpectrumGpChnl.setChnlTertiaryProf(template.getChnlTertiaryProf());
                        result = CONFIG_FAIL;
                        try {
                            frequencyHoppingService.modifyChnlGroupInfo(ccmtsSpectrumGpChnl);
                            result = CONFIG_SUCCESS;
                        } catch (Exception e) {
                            logger.debug("modifyChnlGroupInfo", e);
                        } finally {
                            // 设置结果存入数据库
                            SpectrumTempConfigLogDetail spectrumTempConfigLogDetail = new SpectrumTempConfigLogDetail();
                            spectrumTempConfigLogDetail.setConfigLogId(configLogId);
                            spectrumTempConfigLogDetail.setTemplateName(template.getTemplateName());
                            spectrumTempConfigLogDetail.setConfigOperation(ResourcesUtil
                                    .getString("CMC.GP.issuedChlConf"));
                            spectrumTempConfigLogDetail.setConfigResult(result);
                            spectrumTempConfigLogDetail.setConfigTime(new Date());
                            int channelId = i + 1;
                            spectrumTempConfigLogDetail.setConfigUnit(entity.getName() + Symbol.MIDDLE_LINE
                                    + cmcAttribute.getNmName() + Symbol.MIDDLE_LINE
                                    + ResourcesUtil.getString("CHANNEL.channel") + channelId);
                            spectrumTempConfigLogDetails.add(spectrumTempConfigLogDetail);
                        }
                    }
                }
            }
        }

        // 插入记录详情
        frequencyHoppingDao.batchInsertLogDetail(spectrumTempConfigLogDetails);

        // 更改配置状态
        frequencyHoppingDao.updateSpectrumTempConfigLogStatus(configLogId);

        // 推送消息告诉前端这个任务做完了
        Message message = new Message(Message.MESSAGE_TYPE);
        message.setId("spectrumGroupConfig");
        message.setData(entityId);
        pusher.sendMessage(message);
    }

    public FrequencyHoppingService getFrequencyHoppingService() {
        return frequencyHoppingService;
    }

    public void setFrequencyHoppingService(FrequencyHoppingService frequencyHoppingService) {
        this.frequencyHoppingService = frequencyHoppingService;
    }

    public FrequencyHoppingDao getFrequencyHoppingDao() {
        return frequencyHoppingDao;
    }

    public void setFrequencyHoppingDao(FrequencyHoppingDao frequencyHoppingDao) {
        this.frequencyHoppingDao = frequencyHoppingDao;
    }

    public EntityService getEntityService() {
        return entityService;
    }

    public void setEntityService(EntityService entityService) {
        this.entityService = entityService;
    }

    public CmcDao getCmcDao() {
        return cmcDao;
    }

    public void setCmcDao(CmcDao cmcDao) {
        this.cmcDao = cmcDao;
    }

    public CmcListDao getCmcListDao() {
        return cmcListDao;
    }

    public void setCmcListDao(CmcListDao cmcListDao) {
        this.cmcListDao = cmcListDao;
    }

}
