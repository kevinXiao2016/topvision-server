/***********************************************************************
 * $Id: QosServiceImpl.java,v1.0 2011-12-8 上午10:58:37 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.qos.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.exception.SetValueFailException;
import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.cmc.qos.dao.CmcQosDao;
import com.topvision.ems.cmc.qos.domain.CmcQosServiceClassInfo;
import com.topvision.ems.cmc.qos.domain.CmcQosServiceFlowInfo;
import com.topvision.ems.cmc.qos.domain.CmcQosServiceFlowStats;
import com.topvision.ems.cmc.qos.facade.domain.CmMacToServiceFlow;
import com.topvision.ems.cmc.qos.facade.domain.CmcQosDynamicServiceStats;
import com.topvision.ems.cmc.qos.facade.domain.CmcQosParamSetInfo;
import com.topvision.ems.cmc.qos.facade.domain.CmcQosPktClassInfo;
import com.topvision.ems.cmc.qos.facade.domain.CmcQosServiceClass;
import com.topvision.ems.cmc.qos.facade.domain.CmcQosServiceFlowAttribute;
import com.topvision.ems.cmc.qos.facade.domain.CmcQosServiceFlowStatus;
import com.topvision.ems.cmc.qos.service.CmcQosService;
import com.topvision.ems.cmc.service.impl.CmcBaseCommonService;
import com.topvision.ems.template.service.EntityTypeService;

/**
 * 服务流功能实现
 * 
 * @author loyal
 * @created @2011-12-8-上午10:58:37
 * 
 */
@Service("cmcQosService")
public class CmcQosServiceImpl extends CmcBaseCommonService implements CmcQosService {
    @Resource(name = "cmcQosDao")
    private CmcQosDao cmcQosDao;
    @Autowired
    private EntityTypeService entityTypeService;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.service.CmcQosService#getQosUpDynamicServiceStatsInfo(java.lang.Long)
     */
    @Override
    public CmcQosDynamicServiceStats getQosUpDynamicServiceStatsInfo(Long cmcId) {
        // 获取指定CMC上的上 行动态服务流统计信息
        return cmcQosDao.getQosUpDynamicServiceStatsInfo(cmcId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.service.CmcQosService#getQosDownDynamicServiceStatsInfo(java.lang.Long)
     */
    @Override
    public CmcQosDynamicServiceStats getQosDownDynamicServiceStatsInfo(Long cmcId) {
        // 获取指定CMC上的下行动态服务流统计信息
        return cmcQosDao.getQosDownDynamicServiceStatsInfo(cmcId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.service.CmcQosService#getUpQosServiceFlowStatsInfo(java.util.Map)
     */
    @Override
    public CmcQosServiceFlowStats getUpQosServiceFlowStatsInfo(Map<String, Object> map) {
        // 获取指定CMC上的服务流数量统计
        return cmcQosDao.getUpQosServiceFlowStatsInfo(map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.service.CmcQosService#getDownQosServiceFlowStatsInfo(java.util.Map)
     */
    public CmcQosServiceFlowStats getDownQosServiceFlowStatsInfo(Map<String, Object> map) {
        // 获取指定CMC上的服务流数量统计
        return cmcQosDao.getDownQosServiceFlowStatsInfo(map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.service.CmcQosService#getCmcQosServiceFlowListInfoWithCondition(java
     * .util.Map, java.lang.Integer, java.lang.Integer)
     */
    @Override
    public List<CmcQosServiceFlowInfo> getCmcQosServiceFlowListInfoWithCondition(Map<String, String> map,
            Integer start, Integer limit) {
        return cmcQosDao.getCmcQosServiceFlowListInfoWithCondition(map, start, limit);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.service.CmcQosService#getCmcQosServiceFlowListNumWithCondition(java
     * .util.Map)
     */
    @Override
    public Integer getCmcQosServiceFlowListNumWithCondition(Map<String, String> map) {
        // 调用Dao中方法获得过滤的服务流信息
        return cmcQosDao.getCmcQosServiceFlowListNumWithCondition(map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.service.CmcQosService#getQosParamSetInfo(java.lang.Long)
     */
    @Override
    public List<CmcQosParamSetInfo> getQosParamSetInfo(Long sId) {
        // 从数据库中取cmcId和sId指定的服务流参数集信息
        return cmcQosDao.getQosParamSetInfo(sId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.service.CmcQosService#getQosPktClassInfo(java.lang.Long)
     */
    @Override
    public List<CmcQosPktClassInfo> getQosPktClassInfo(Long sId) {
        // 从数据库中取cmcId和sId指定包分类器信息
        return cmcQosDao.getQosPktClassInfo(sId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.service.CmcQosService#getServiceFlowConnectedCm(java.lang.Long)
     */
    @Override
    public CmAttribute getServiceFlowConnectedCm(Long sId) {
        return cmcQosDao.getServiceFlowConnectedCm(sId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.service.CmcService#getQosServiceClassList(java.lang.Long)
     */
    @Override
    public List<CmcQosServiceClassInfo> getQosServiceClassList(Long cmcId) {
        // 调用Dao层方法
        Long entityId = getEntityIdByCmcId(cmcId);
        return cmcQosDao.getQosServiceClassList(entityId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.service.CmcService#getQosServiceClassInfo(java.util.Map)
     */
    @Override
    public CmcQosServiceClass getQosServiceClassInfo(Long scId) {
        // 调用Dao层方法
        return cmcQosDao.getQosServiceClassInfo(scId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.service.CmcService#createOrModifyServiceClassInfo(com.topvision.ems
     * .cmc.facade.domain.CmcQosServiceClass)
     */
    @Override
    public Boolean createOrModifyServiceClassInfo(CmcQosServiceClass serviceClass, Long cmcId) {
        // 1.根据cmcId查询出entityId，根据entityId查询snmpParam
        Long entityId = getEntityIdByCmcId(cmcId);
        serviceClass.setEntityId(entityId);
        snmpParam = getSnmpParamByEntityId(entityId);
        // 2.调用cmcFacade 中方法 createOrModifyServiceClassInfo修改相应信息
        CmcQosServiceClass cmcQosServiceClassAfterModify = getCmcFacade(snmpParam.getIpAddress())
                .createOrModifyServiceClassInfo(snmpParam, serviceClass);
        // 3.与返回值比较，如果不相等，抛出异常
        boolean isSuccess = true;
        if (cmcQosServiceClassAfterModify != null
                && !cmcQosServiceClassAfterModify.getClassName().equals(serviceClass.getClassName())) {
            isSuccess = false;
            logger.error("cmc.message.cmc.setClassName");
        }
        /*
         * if (cmcQosServiceClassAfterModify != null &&
         * !cmcQosServiceClassAfterModify.getClassStatus().equals( serviceClass.getClassStatus())) {
         * sBuilder.append(getString("cmc.message.cmc.setClassStatus")); }
         */
        if (cmcQosServiceClassAfterModify != null
                && !cmcQosServiceClassAfterModify.getClassPriority().equals(serviceClass.getClassPriority())) {
            isSuccess = false;
            logger.error("cmc.message.cmc.setClassPriority");
        }
        if (cmcQosServiceClassAfterModify != null
                && !cmcQosServiceClassAfterModify.getClassMaxTrafficRate()
                        .equals(serviceClass.getClassMaxTrafficRate())) {
            isSuccess = false;
            logger.error("cmc.message.cmc.setClassMaxTrafficRate");
        }
        if (cmcQosServiceClassAfterModify != null
                && !cmcQosServiceClassAfterModify.getClassMaxTrafficBurst().equals(
                        serviceClass.getClassMaxTrafficBurst())) {
            isSuccess = false;
            logger.error("cmc.message.cmc.setClassMaxTrafficBurst");
        }
        if (cmcQosServiceClassAfterModify != null
                && !cmcQosServiceClassAfterModify.getClassMinReservedRate().equals(
                        serviceClass.getClassMinReservedRate())) {
            isSuccess = false;
            logger.error("cmc.message.cmc.setClassMinReservedRate");
        }
        if (cmcQosServiceClassAfterModify != null
                && !cmcQosServiceClassAfterModify.getClassMinReservedPkt()
                        .equals(serviceClass.getClassMinReservedPkt())) {
            isSuccess = false;
            logger.error("cmc.message.cmc.setClassMinReservedPkt");
        }
        if (cmcQosServiceClassAfterModify != null
                && !cmcQosServiceClassAfterModify.getClassMaxConcatBurst()
                        .equals(serviceClass.getClassMaxConcatBurst())) {
            isSuccess = false;
            logger.error("cmc.message.cmc.setClassName");
        }
        if (cmcQosServiceClassAfterModify != null
                && !cmcQosServiceClassAfterModify.getClassNomPollInterval().equals(
                        serviceClass.getClassNomPollInterval())) {
            isSuccess = false;
            logger.error("cmc.message.cmc.setClassNomPollInterval");
        }
        if (cmcQosServiceClassAfterModify != null
                && !cmcQosServiceClassAfterModify.getClassTolPollJitter().equals(serviceClass.getClassTolPollJitter())) {
            isSuccess = false;
            logger.error("cmc.message.cmc.setClassTolPollJitter");
        }
        if (cmcQosServiceClassAfterModify != null
                && !cmcQosServiceClassAfterModify.getClassUnsolicitGrantSize().equals(
                        serviceClass.getClassUnsolicitGrantSize())) {
            isSuccess = false;
            logger.error("cmc.message.cmc.setClassName");
        }
        if (cmcQosServiceClassAfterModify != null
                && !cmcQosServiceClassAfterModify.getClassNomGrantInterval().equals(
                        serviceClass.getClassNomGrantInterval())) {
            isSuccess = false;
            logger.error("cmc.message.cmc.setClassNomGrantInterval");
        }
        if (cmcQosServiceClassAfterModify != null
                && !cmcQosServiceClassAfterModify.getClassTolGrantJitter()
                        .equals(serviceClass.getClassTolGrantJitter())) {
            isSuccess = false;
            logger.error("cmc.message.cmc.setClassTolGrantJitter");
        }
        if (cmcQosServiceClassAfterModify != null
                && !cmcQosServiceClassAfterModify.getClassGrantsPerInterval().equals(
                        serviceClass.getClassGrantsPerInterval())) {
            isSuccess = false;
            logger.error("cmc.message.cmc.setClassGrantsPerInterval");
        }
        if (cmcQosServiceClassAfterModify != null
                && !cmcQosServiceClassAfterModify.getClassMaxLatency().equals(serviceClass.getClassMaxLatency())) {
            isSuccess = false;
            logger.error("cmc.message.cmc.setClassMaxLatency");
        }
        if (cmcQosServiceClassAfterModify != null
                && !cmcQosServiceClassAfterModify.getClassActiveTimeout().equals(serviceClass.getClassActiveTimeout())) {
            isSuccess = false;
            logger.error("cmc.message.cmc.setClassActiveTimeout");
        }
        if (cmcQosServiceClassAfterModify != null
                && !cmcQosServiceClassAfterModify.getClassAdmittedTimeout().equals(
                        serviceClass.getClassAdmittedTimeout())) {
            isSuccess = false;
            logger.error("cmc.message.cmc.setClassAdmittedTimeout");
        }
        if (cmcQosServiceClassAfterModify != null
                && !cmcQosServiceClassAfterModify.getClassSchedulingType()
                        .equals(serviceClass.getClassSchedulingType())) {
            isSuccess = false;
            logger.error("cmc.message.cmc.setClassSchedulingType");
        }
        if (cmcQosServiceClassAfterModify != null
                && !cmcQosServiceClassAfterModify.getClassRequestPolicy().equals(serviceClass.getClassRequestPolicy())) {
            isSuccess = false;
            logger.error("cmc.message.cmc.setClassRequestPolicy");
        }
        if (cmcQosServiceClassAfterModify != null
                && !cmcQosServiceClassAfterModify.getClassTosAndMask().equals(serviceClass.getClassTosAndMask())) {
            isSuccess = false;
            logger.error("cmc.message.cmc.setClassName");
        }
        if (cmcQosServiceClassAfterModify != null
                && !cmcQosServiceClassAfterModify.getClassTosOrMask().equals(serviceClass.getClassTosOrMask())) {
            isSuccess = false;
            logger.error("cmc.message.cmc.setClassTosOrMask");
        }
        if (cmcQosServiceClassAfterModify != null
                && !cmcQosServiceClassAfterModify.getClassDirection().equals(serviceClass.getClassDirection())) {
            isSuccess = false;
            logger.error("cmc.message.cmc.setClassDirection");
        }
        if (!isSuccess) {
            throw new SetValueFailException("set failure");
        }

        // 4.更新数据库
        cmcQosDao.insertOrUpdateServiceClass(serviceClass);
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.service.CmcService#deleteServiceClassInfo(java.lang.Long)
     */
    @Override
    public Boolean deleteServiceClassInfo(Long scId, Long cmcId) {
        // 1.根据cmcId查询出entityId，根据entityId查询snmpParam
        Long entityId = getEntityIdByCmcId(cmcId);
        snmpParam = getSnmpParamByEntityId(entityId);
        // 2.根据scId查找出所要删除的Service class
        CmcQosServiceClass cmcQosServiceClass = cmcQosDao.getQosServiceClassInfo(scId);
        cmcQosServiceClass.setClassStatus(6);
        // 3.调用cmcFacade 中方法 ？？？ 删除相应信息
        CmcQosServiceClass cmcQosServiceClassAfterDelete = getCmcFacade(snmpParam.getIpAddress())
                .deleteServiceClassInfo(snmpParam, cmcQosServiceClass);
        // 4.与返回值比较，如果不相等，抛出异常
        if (cmcQosServiceClassAfterDelete != null) {

        }
        // 5.更新数据库
        cmcQosDao.deleteServiceClass(scId);
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.service.CmcQosService#getEntityIdByCmcId(java.lang.Long)
     */
    @Override
    public Long getEntityIdByCmcId(Long cmcId) {
        return cmcQosDao.getEntityIdByCmcId(cmcId);
    }

    public CmcQosDao getCmcQosDao() {
        return cmcQosDao;
    }

    public void setCmcQosDao(CmcQosDao cmcQosDao) {
        this.cmcQosDao = cmcQosDao;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.service.CmcQosService#refreshServiceFlowBaseInfo(java.lang.Long)
     */
    @Override
    public List<CmcQosServiceFlowAttribute> refreshServiceFlowBaseInfo(Long cmcId) {
        // 1.根据cmcId查询出entityId，根据entityId查询snmpParam
        CmcAttribute cmcAttribute = cmcQosDao.getCmcAttributeByCmcId(cmcId);
        long entityId = getEntityIdByCmcId(cmcId);
        List<CmcQosServiceFlowAttribute> cmcQosServiceFlowAttributeList = null;
        if (entityTypeService.isCcmtsWithoutAgent(cmcAttribute.getCmcDeviceStyle())) {
            snmpParam = getSnmpParamByEntityId(entityId);
            cmcQosServiceFlowAttributeList = getCmcFacade(snmpParam.getIpAddress()).refreshServiceFlowBaseInfo(
                    snmpParam);
            for (CmcQosServiceFlowAttribute csa : cmcQosServiceFlowAttributeList) {
                //临时设置，在更新数据库时会修改
                csa.setCmcId(cmcId);
            }
            if (cmcQosServiceFlowAttributeList.size() != 0) {
                cmcQosDao.batchInsertCmcQosServiceFlowAttribute(cmcQosServiceFlowAttributeList, entityId);
            } else {
                List<Long> cmcIdList = cmcQosDao.getCmcIdByOlt(entityId);
                for (Long cmcIdTemp : cmcIdList) {
                    cmcQosDao.deleteCmcQosServiceFlowRelation(cmcIdTemp);
                }
            }
        } else if (entityTypeService.isCcmtsWithAgent(cmcAttribute.getCmcDeviceStyle())) {
            snmpParam = getSnmpParamByCmcId(cmcId);
            cmcQosServiceFlowAttributeList = getCmcFacade(snmpParam.getIpAddress()).refreshServiceFlowBaseInfo(
                    snmpParam);
            for (CmcQosServiceFlowAttribute csa : cmcQosServiceFlowAttributeList) {
                csa.setCmcId(cmcId);
            }
            if (cmcQosServiceFlowAttributeList.size() != 0) {
                cmcQosDao.batchInsertCmcQosServiceFlowAttribute8800B(cmcQosServiceFlowAttributeList, cmcId);
            } else {
                cmcQosDao.deleteCmcQosServiceFlowRelation(cmcId);
            }
        }
        return cmcQosServiceFlowAttributeList;
    }

    @Override
    public List<CmcQosServiceFlowAttribute> refreshServiceFlowBaseInfoOnCC(Long cmcId, Long cmcIndex) {
        CmcAttribute cmcAttribute = cmcQosDao.getCmcAttributeByCmcId(cmcId);
        long entityId = getEntityIdByCmcId(cmcId);
        List<CmcQosServiceFlowAttribute> cmcQosServiceFlowAttributeList = null;
        if (entityTypeService.isCcmtsWithoutAgent(cmcAttribute.getCmcDeviceStyle())) {
            snmpParam = getSnmpParamByEntityId(entityId);
            cmcQosServiceFlowAttributeList = getCmcFacade(snmpParam.getIpAddress()).refreshServiceFlowBaseInfoOnCC(
                    snmpParam, cmcIndex);
            List<Long> dbSid = cmcQosDao.getDBSIdListByCmcId(cmcId);
            if (cmcQosServiceFlowAttributeList.size() != 0) {
                cmcQosDao.batchInsertOrUpdateQosServiceFlowAttrOnCC(cmcQosServiceFlowAttributeList, cmcId, dbSid);
            } else {
                //cmcDiscoveryDao.deleteCmcQosServiceFlowRelation(cmcId);
            }
        } else if (entityTypeService.isCcmtsWithAgent(cmcAttribute.getCmcDeviceStyle())) {
            snmpParam = getSnmpParamByCmcId(cmcId);
            cmcQosServiceFlowAttributeList = getCmcFacade(snmpParam.getIpAddress()).refreshServiceFlowBaseInfo(
                    snmpParam);
            for (CmcQosServiceFlowAttribute csa : cmcQosServiceFlowAttributeList) {
                csa.setCmcId(cmcId);
            }
            if (cmcQosServiceFlowAttributeList.size() != 0) {
                cmcQosDao.batchInsertCmcQosServiceFlowAttribute8800B(cmcQosServiceFlowAttributeList, cmcId);
            } else {
                cmcQosDao.deleteCmcQosServiceFlowRelation(cmcId);
            }
        }
        return cmcQosServiceFlowAttributeList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.service.CmcQosService#refreshServiceFlowStatus(java.lang.Long)
     */
    @Override
    public List<CmcQosServiceFlowStatus> refreshServiceFlowStatus(Long cmcId) {
        // 1.根据cmcId查询出entityId，根据entityId查询snmpParam
        Long entityId = getEntityIdByCmcId(cmcId);
        snmpParam = getSnmpParamByEntityId(entityId);
        // 2.调用cmcFacade 中方法 refreshServiceFlowStatusInfo刷新相应信息
        List<CmcQosServiceFlowStatus> cmcQosServiceFlowStatusList = getCmcFacade(snmpParam.getIpAddress())
                .refreshServiceFlowStatusInfo(snmpParam);
        cmcQosDao.batchInsertCmcQosServiceFlowStatus(cmcQosServiceFlowStatusList, cmcId);
        return cmcQosServiceFlowStatusList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.service.CmcQosService#refreshServiceFlowPktClassInfos(java.lang.Long)
     */
    @Override
    public List<CmcQosPktClassInfo> refreshServiceFlowPktClassInfos(Long cmcId) {
        CmcAttribute cmcAttribute = cmcQosDao.getCmcAttributeByCmcId(cmcId);
        long entityId = getEntityIdByCmcId(cmcId);
        List<CmcQosPktClassInfo> cmcQosPktClassInfoList = null;
        if (entityTypeService.isCcmtsWithoutAgent(cmcAttribute.getCmcDeviceStyle())) {
            snmpParam = getSnmpParamByEntityId(entityId);
            // 2.调用cmcFacade 中方法 refreshServiceFlowPktClassInfos刷新相应信息
            cmcQosPktClassInfoList = getCmcFacade(snmpParam.getIpAddress()).refreshServiceFlowPktClassInfos(snmpParam);
            cmcQosDao.batchInsertCmcQosPktClassInfo(cmcQosPktClassInfoList, entityId);
        } else if (entityTypeService.isCcmtsWithAgent(cmcAttribute.getCmcDeviceStyle())) {
            snmpParam = getSnmpParamByCmcId(cmcId);
            cmcQosPktClassInfoList = getCmcFacade(snmpParam.getIpAddress()).refreshServiceFlowPktClassInfos(snmpParam);
            cmcQosDao.batchInsertCmcQosPktClassInfo8800B(cmcQosPktClassInfoList, cmcId);
        }
        return cmcQosPktClassInfoList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.service.CmcQosService#refreshServiceFlowParamSetInfos(java.lang.Long)
     */
    @Override
    public List<CmcQosParamSetInfo> refreshServiceFlowParamSetInfos(Long cmcId) {
        CmcAttribute cmcAttribute = cmcQosDao.getCmcAttributeByCmcId(cmcId);
        long entityId = getEntityIdByCmcId(cmcId);
        List<CmcQosParamSetInfo> cmcQosParamSetInfoList = null;
        if (entityTypeService.isCcmtsWithoutAgent(cmcAttribute.getCmcDeviceStyle())) {
            snmpParam = getSnmpParamByEntityId(entityId);
            // 2.调用cmcFacade 中方法 refreshServiceFlowPktClassInfos刷新相应信息
            cmcQosParamSetInfoList = getCmcFacade(snmpParam.getIpAddress()).refreshServiceFlowParamSetInfos(snmpParam);
            cmcQosDao.batchInsertCmcQosParamSetInfo(cmcQosParamSetInfoList, entityId);
        } else if (entityTypeService.isCcmtsWithAgent(cmcAttribute.getCmcDeviceStyle())) {
            snmpParam = getSnmpParamByCmcId(cmcId);
            cmcQosParamSetInfoList = getCmcFacade(snmpParam.getIpAddress()).refreshServiceFlowParamSetInfos(snmpParam);
            cmcQosDao.batchInsertCmcQosParamSetInfo8800B(cmcQosParamSetInfoList, cmcId);
        }
        return cmcQosParamSetInfoList;
    }

    @Override
    public List<CmcQosParamSetInfo> refreshServiceFlowParamSetOnCC(Long cmcId, Long cmcIndex) {
        CmcAttribute cmcAttribute = cmcQosDao.getCmcAttributeByCmcId(cmcId);
        long entityId = getEntityIdByCmcId(cmcId);
        List<CmcQosParamSetInfo> cmcQosParamSetInfoList = null;
        if (entityTypeService.isCcmtsWithoutAgent(cmcAttribute.getCmcDeviceStyle())) {
            snmpParam = getSnmpParamByEntityId(entityId);
            List<Long> serviceFlowIdListInDb = cmcQosDao.getServiceFlowIdListByCmcId(cmcId);
            List<Long> sIdListInDb = cmcQosDao.getDBSIdListByCmcId(cmcId);
            cmcQosParamSetInfoList = getCmcFacade(snmpParam.getIpAddress()).refreshServiceFlowParamSetOnCC(snmpParam,
                    cmcIndex, serviceFlowIdListInDb);
            if (cmcQosParamSetInfoList.size() > 0) {
                cmcQosDao.batchInsertOrUpdateCmcQosParamSetInfoOnCC(cmcQosParamSetInfoList, cmcId, sIdListInDb);
            } else {
                cmcQosDao.deleteServiceFlowParmsetRelationByCmcId(cmcId);
            }

        } else if (entityTypeService.isCcmtsWithAgent(cmcAttribute.getCmcDeviceStyle())) {
            snmpParam = getSnmpParamByCmcId(cmcId);
            cmcQosParamSetInfoList = getCmcFacade(snmpParam.getIpAddress()).refreshServiceFlowParamSetInfos(snmpParam);
            cmcQosDao.batchInsertCmcQosParamSetInfo8800B(cmcQosParamSetInfoList, cmcId);
        }
        return cmcQosParamSetInfoList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.service.CmcQosService#refreshCmMacToServiceFlows(java.lang.Long)
     */
    @Override
    public List<CmMacToServiceFlow> refreshCmMacToServiceFlows(Long cmcId) {
        CmcAttribute cmcAttribute = cmcQosDao.getCmcAttributeByCmcId(cmcId);
        long entityId = getEntityIdByCmcId(cmcId);
        List<CmMacToServiceFlow> cmMacToServiceFlowList = null;
        if (entityTypeService.isCcmtsWithoutAgent(cmcAttribute.getCmcDeviceStyle())) {
            snmpParam = getSnmpParamByEntityId(entityId);
            // 2.调用cmcFacade 中方法 refreshServiceFlowPktClassInfos刷新相应信息
            cmMacToServiceFlowList = getCmcFacade(snmpParam.getIpAddress()).refreshCmMacToServiceFlows(snmpParam);
            cmcQosDao.batchInsertCmMacToServiceFlow(cmMacToServiceFlowList, entityId);
        } else if (entityTypeService.isCcmtsWithAgent(cmcAttribute.getCmcDeviceStyle())) {
            snmpParam = getSnmpParamByCmcId(cmcId);
            cmMacToServiceFlowList = getCmcFacade(snmpParam.getIpAddress()).refreshCmMacToServiceFlows(snmpParam);
            cmcQosDao.batchInsertCC8800BCmMacToServiceFlow(cmMacToServiceFlowList, cmcId, entityId);
        }
        return cmMacToServiceFlowList;
    }

}
