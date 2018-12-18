/***********************************************************************
 * $Id: CmcBaseConfigServiceImpl.java,v1.0 2013-11-1 下午2:48:07 $
 * 
 * @author: dosion
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ccmts.service.impl;

import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.ccmts.service.CmcBaseConfigService;
import com.topvision.ems.cmc.exception.SaveConfigException;
import com.topvision.ems.cmc.facade.domain.CmcSystemBasicInfo;
import com.topvision.ems.cmc.service.impl.CmcBaseCommonService;
import com.topvision.ems.cmc.util.CmcConstants;

/**
 * @author dosion
 * @created @2013-11-1-下午2:48:07
 *
 */
@Service("cmcBaseConfigService")
public class CmcBaseConfigServiceImpl extends CmcBaseCommonService implements CmcBaseConfigService {

	@Override
	public void modifyCcmtsBasicInfo(Long entityId, Long cmcId, String cmcName, String ccSysLocation,
			String ccSysContact) {
		CmcSystemBasicInfo cmcSystemBasicInfo = new CmcSystemBasicInfo();
		cmcSystemBasicInfo.setIfIndex(cmcDao.getCmcIndexByCmcId(cmcId));
		cmcSystemBasicInfo.setCmcId(cmcId);
		cmcSystemBasicInfo.setTopCcmtsSysName(cmcName);
		cmcSystemBasicInfo.setEntityId(entityId);
		cmcSystemBasicInfo.setTopCcmtsSysContact(ccSysContact);
		cmcSystemBasicInfo.setTopCcmtsSysLocation(ccSysLocation);
		snmpParam = getSnmpParamByEntityId(entityId);
		getCmcFacade(snmpParam.getIpAddress()).modifyCcmtsBasicInfo(snmpParam, cmcSystemBasicInfo);
		cmcDao.updateCcmtsBasicInfo(cmcSystemBasicInfo);
	}

	@Override
	public void saveConfig(Long cmcId) {
		snmpParam = getSnmpParamByCmcId(cmcId);
		Integer topSysCmcSaveStatus = getCmcFacade(snmpParam.getIpAddress()).getCmcSaveStatus(snmpParam);
		if (topSysCmcSaveStatus == null) {
			throw new SaveConfigException(getString("cmc.message.connection"));
		} else {
			if (!CmcConstants.CMC_SAVE_CONFIG_SAVEDORREADY.equals(topSysCmcSaveStatus)
					&& !CmcConstants.CMC_SAVE_CONFIG_SAVEDFAIL.equals(topSysCmcSaveStatus)) {
				throw new SaveConfigException(getString("cmc.message.saveConfig"));
			} else {
				// 发送facade保存操作
				getCmcFacade(snmpParam.getIpAddress()).saveCmcConfig(snmpParam);
			}
		}
	}

	@Override
	public void clearConfig(Long cmcId) {
		snmpParam = getSnmpParamByCmcId(cmcId);
		getCmcFacade(snmpParam.getIpAddress()).clearConfig(snmpParam);
	}
}
