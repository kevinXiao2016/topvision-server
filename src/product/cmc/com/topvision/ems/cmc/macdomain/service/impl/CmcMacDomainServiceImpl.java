/***********************************************************************
 * $Id: CmcMacDomainServiceImpl.java,v1.0 2012-2-13 下午02:37:30 $
 * 
 * @author: zhanglongyang
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.macdomain.service.impl;

import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.macdomain.dao.CmcMacDomainDao;
import com.topvision.ems.cmc.macdomain.facade.domain.MacDomainBaseInfo;
import com.topvision.ems.cmc.macdomain.facade.domain.MacDomainStatusInfo;
import com.topvision.ems.cmc.macdomain.service.CmcMacDomainService;
import com.topvision.ems.cmc.service.impl.CmcBaseCommonService;

/**
 * MAC Domain功能实现
 * 
 * @author zhanglongyang
 * @created @2012-2-13-下午02:37:30
 * 
 */
@Service("cmcMacDomainService")
public class CmcMacDomainServiceImpl extends CmcBaseCommonService implements CmcMacDomainService {
    //	@Resource(name = "cmcMacDomainDao")
    private CmcMacDomainDao cmcMacDomainDao;

    /*
     * 
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.service.CmcMacDomainService#getMacDomainStatusInfo(java.lang.Long)
     */
    @Override
    public MacDomainStatusInfo getMacDomainStatusInfo(Long cmcId) {
        // 调用Dao方法，返回MacDomain状态信息
        return cmcMacDomainDao.getMacDomainStatusInfo(cmcId);
    }

    /*
     * 
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.service.CmcMacDomainService#getMacDomainBaseInfo(java.lang.Long)
     */
    @Override
    public MacDomainBaseInfo getMacDomainBaseInfo(Long cmcId) {
        // 调用Dao方法，返回Macdomain基本信息
        return cmcMacDomainDao.getMacDomainBaseInfo(cmcId);
    }

    /*
     * 
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.service.CmcMacDomainService#modifyMacDomainBaseInfo(com.topvision.ems
     * .cmc.facade.domain.MacDomainBaseInfo)
     */
    @Override
    public void modifyMacDomainBaseInfo(MacDomainBaseInfo macDomainBaseInfo) {
        // TODO 1:根据macDomainBaseInfo中cmcId查询出entityId，根据entityId查询snmpParam
        // 2:调用cmcFacade 中方法modifyMacDomainBaseInfo修改mad域基本信息
        // 3:比较返回值与预期值，如果不等，抛出异常
        // 4:更新数据库，判断是否有异常抛出，如有，则抛出异常
        snmpParam = getSnmpParamByCmcId(macDomainBaseInfo.getCmcId());
        MacDomainBaseInfo macDomainBaseInfoAfterModified = getCmcFacade(snmpParam.getIpAddress())
                .modifyMacDomainBaseInfo(snmpParam, macDomainBaseInfo);
        // 更新数据库
        if (macDomainBaseInfoAfterModified != null) {
            cmcMacDomainDao.updateMacDomainBaseInfo(macDomainBaseInfoAfterModified);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.service.CmcMacDomainService#refreshMacDomainInfo(java.lang.Long)
     */
    @Override
    public void refreshMacDomainInfo(Long cmcId) {
        snmpParam = getSnmpParamByCmcId(cmcId);
        MacDomainStatusInfo macDomainStatus;
        Long cmcIndex = cmcMacDomainDao.getMacDomainStatusInfo(cmcId).getCmcIndex();
        macDomainStatus = getCmcFacade(snmpParam.getIpAddress()).getMacDomainStatusInfo(snmpParam, cmcIndex);
        macDomainStatus.setCmcId(cmcId);
        cmcMacDomainDao.insertOrUpdateMacDomainStatusInfo(macDomainStatus);
    }

    public CmcMacDomainDao getCmcMacDomainDao() {
        return cmcMacDomainDao;
    }

    public void setCmcMacDomainDao(CmcMacDomainDao cmcMacDomainDao) {
        this.cmcMacDomainDao = cmcMacDomainDao;
    }

}
