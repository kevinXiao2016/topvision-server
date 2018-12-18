/***********************************************************************
 * $Id: CmcAuthServiceImpl.java,v1.0 2012-10-9 下午02:17:05 $
 * 
 * @author: dosion
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.auth.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.auth.dao.CmcAuthDao;
import com.topvision.ems.cmc.auth.facade.domain.CcmtsAuthManagement;
import com.topvision.ems.cmc.auth.service.CmcAuthService;
import com.topvision.framework.constants.Symbol;
import com.topvision.ems.cmc.exception.SetValueFailException;
import com.topvision.ems.cmc.service.impl.CmcBaseCommonService;

/**
 * @author dosion
 * @created @2012-10-9-下午02:17:05
 * 
 */
@Service("cmcAuthService")
public class CmcAuthServiceImpl extends CmcBaseCommonService implements CmcAuthService {
	@Resource(name = "cmcAuthDao")
	private CmcAuthDao cmcAuthDao;
	/*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.service.CmcAuthService#getCcmtsAuthInfo(java.lang.Long)
     */
    @Override
    public CcmtsAuthManagement getCcmtsAuthInfo(Long cmcId) {
        return cmcAuthDao.getCcmtsAuthInfo(cmcId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.service.CmcAuthService#getCcmtsAuthInfoFromEntity(java.lang.Long)
     */
    @Override
    public CcmtsAuthManagement getCcmtsAuthInfoFromEntity(Long cmcId) {
        CcmtsAuthManagement authMgmt = new CcmtsAuthManagement();
        authMgmt.setCmcId(cmcId);
        authMgmt.setIfIndex(cmcAuthDao.getCmcIndexByCmcId(cmcId));
        // 获取授权信息
        snmpParam = getSnmpParamByEntityId(getEntityIdByCmcId(cmcId));
        authMgmt = getCmcFacade(snmpParam.getIpAddress()).getCmcAuthInfo(snmpParam, authMgmt);
        // 判断是否授权成功
        if (authMgmt.getTopCcmtsAuthApplyStatus() == 9) {
            //授权成功则更新数据库
            cmcAuthDao.updateCmcAuthInfo(authMgmt, cmcId);
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.service.CmcAuthService#setCcmtsAuthInfo(java.lang.Long,
     * com.topvision.ems.cmc.facade.domain.CcmtsAuthManagement)
     */
    @Override
    public Boolean setCcmtsAuthInfo(Long cmcId, CcmtsAuthManagement authMgmt) {
        CcmtsAuthManagement authMgmtAfterModified;
        //初始化数据
        authMgmt.setTopCcmtsAuthApply(1);
        //将ipAddress与fileName转换为URI
        if(authMgmt.getIpAddress() != null && authMgmt.getFileName() != null){
            authMgmt.setTopCcmtsAuthFileURI(Symbol.SLASH + authMgmt.getIpAddress() + Symbol.SLASH + authMgmt.getFileName());
        }
        // 获取全局授权状态
        snmpParam = getSnmpParamByCmcId(cmcId);
        int status = getCmcFacade(snmpParam.getIpAddress()).getCmcAuthStatus(snmpParam);
        if (status == 1) {
            // 若有CC处于授权中则返回授权失败
            return false;
        } else {
            // 若无CC处于授权状态，则进行设置
            authMgmtAfterModified = getCmcFacade(snmpParam.getIpAddress()).setCmcAuthInfo(snmpParam, authMgmt);
        }
        //验证设置结果
        if(authMgmtAfterModified != null){
            boolean isSuccess = true;
            if(!authMgmt.getTopCcmtsAuthApply().equals(authMgmtAfterModified.getTopCcmtsAuthApply())){
                isSuccess = false;
                logger.error("cmc.message.cmc.setTopCcmtsAuthApply");
            }
            if(!authMgmt.getTopCcmtsAuthFileURI().equals(authMgmtAfterModified.getTopCcmtsAuthFileURI())){
                isSuccess = false;
                logger.error("cmc.message.cmc.setTopCcmtsAuthFileURI");
            }
            if(!isSuccess){
                throw new SetValueFailException("set failure"); 
            }
        }
       
        return true;
    }
}
