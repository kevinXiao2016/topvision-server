/***********************************************************************
 * $Id: GponOnuServiceImpl.java,v1.0 2016年12月19日 下午1:13:11 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.onuauth.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.olt.dao.OltPonDao;
import com.topvision.ems.epon.onu.dao.OnuDao;
import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.facade.domain.DiscoveryData;
import com.topvision.ems.gpon.onuauth.GponConstant;
import com.topvision.ems.gpon.onuauth.dao.GponOnuAuthDao;
import com.topvision.ems.gpon.onuauth.domain.GponAutoAuthConfig;
import com.topvision.ems.gpon.onuauth.domain.GponOnuAuthConfig;
import com.topvision.ems.gpon.onuauth.domain.GponOnuAuthMode;
import com.topvision.ems.gpon.onuauth.domain.GponOnuAutoFind;
import com.topvision.ems.gpon.onuauth.exception.AutoAuthRuleFullException;
import com.topvision.ems.gpon.onuauth.exception.GponOnuAuthFullException;
import com.topvision.ems.gpon.onuauth.facade.GponOnuAuthFacade;
import com.topvision.ems.gpon.onuauth.service.GponOnuAuthService;
import com.topvision.ems.gpon.utils.GponIndex;
import com.topvision.ems.network.service.DiscoveryService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.utils.EponIndex;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.message.event.SynchronizedEvent;
import com.topvision.platform.message.event.SynchronizedListener;
import com.topvision.platform.message.service.MessageService;

/**
 * @author Bravin
 * @created @2016年12月19日-下午1:13:11
 *
 */
@Service
public class GponOnuAuthServiceImpl extends BaseService implements GponOnuAuthService, SynchronizedListener {
	@Autowired
	private MessageService messageService;
	@Autowired
	private FacadeFactory facadeFactory;
	@Autowired
	private EntityService entityService;
	@Autowired
	private GponOnuAuthDao gponOnuAuthDao;
	@Autowired
	private DiscoveryService<DiscoveryData> discoveryService;
	@Autowired
	private OltPonDao oltPonDao;
	@Autowired
	private OnuDao onuDao;

	@Override
	@PreDestroy
	public void destroy() {
		super.destroy();
		messageService.removeListener(SynchronizedListener.class, this);
	}

	@Override
	@PostConstruct
	public void initialize() {
		super.initialize();
		messageService.addListener(SynchronizedListener.class, this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.topvision.platform.message.event.SynchronizedListener#
	 * insertEntityStates(com.topvision
	 * .platform.message.event.SynchronizedEvent)
	 */
	@Override
	public void insertEntityStates(SynchronizedEvent event) {
		refreshGponOnuAuth(event.getEntityId());
	}

	/**
	 * @param entityId
	 */
	private void refreshGponOnuAuthMode(long entityId) {
		SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
		GponOnuAuthFacade facade = facadeFactory.getFacade(snmpParam.getIpAddress(), GponOnuAuthFacade.class);
		List<GponOnuAuthMode> list = facade.refershGponOnuAuthModeList(snmpParam, GponOnuAuthMode.class);
		gponOnuAuthDao.batchInsertOrUpdateOnuAuthMode(entityId, list);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.topvision.platform.message.event.SynchronizedListener#
	 * updateEntityStates(com.topvision
	 * .platform.message.event.SynchronizedEvent)
	 */
	@Override
	public void updateEntityStates(SynchronizedEvent event) {
	}

	/**
	 * @param entityId
	 */
	private void refreshGponOnuAutoAuthConfig(long entityId) {
		SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
		GponOnuAuthFacade facade = facadeFactory.getFacade(snmpParam.getIpAddress(), GponOnuAuthFacade.class);
		List<GponAutoAuthConfig> list = facade.refershGponOnuAutoAuthList(snmpParam, GponAutoAuthConfig.class);
		gponOnuAuthDao.batchInsertOrUpdateOnuAutoAuth(entityId, list);
	}

	/**
	 * @param entityId
	 */
	private void refreshGponOnuFindConfig(long entityId) {
		SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
		GponOnuAuthFacade facade = facadeFactory.getFacade(snmpParam.getIpAddress(), GponOnuAuthFacade.class);
		List<GponOnuAutoFind> list = facade.refershGponOnuAutoFindList(snmpParam, GponOnuAutoFind.class);
		gponOnuAuthDao.batchInsertOrUpdateOnuAutoFind(entityId, list);
	}

	/**
	 * @param entityId
	 */
	private void refreshGponOnuAuthConfig(long entityId) {
		SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
		GponOnuAuthFacade facade = facadeFactory.getFacade(snmpParam.getIpAddress(), GponOnuAuthFacade.class);
		List<GponOnuAuthConfig> list = facade.refershGponOnuAuthConfigList(snmpParam, GponOnuAuthConfig.class);
		gponOnuAuthDao.batchInsertOrUpdateOnuAuthConfig(entityId, list);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.topvision.ems.gpon.onuauth.service.GponOnuAuthService#loadPonAuthMode
	 * (java.lang.Long)
	 */
	@Override
	public List<GponOnuAuthMode> loadPonAuthMode(Long entityId) {
		return gponOnuAuthDao.selectPonAuthMode(entityId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.topvision.ems.gpon.onuauth.service.GponOnuAuthService#
	 * loadOnuAuthConfigList(java.lang .Long, java.lang.Long)
	 */
	@Override
	public List<GponOnuAuthConfig> loadOnuAuthConfigList(Long entityId, Long ponIndex) {
		return gponOnuAuthDao.selectOnuAuthConfigList(entityId, ponIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.topvision.ems.gpon.onuauth.service.GponOnuAuthService#
	 * loadOnuAutoFindList(java.lang.Long, java.lang.Long)
	 */
	@Override
	public List<GponOnuAutoFind> loadAutoFindOnuList(Long entityId, Long ponIndex) {
		return gponOnuAuthDao.selectAutoFindOnuList(entityId, ponIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.topvision.ems.gpon.onuauth.service.GponOnuAuthService#
	 * loadOnuAutoAuthConfigList(java. lang.Long, java.lang.Long)
	 */
	@Override
	public List<GponAutoAuthConfig> loadAutoAuthConfigList(Long entityId) {
		return gponOnuAuthDao.selectAutoAuthConfigList(entityId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.topvision.ems.gpon.onuauth.service.GponOnuAuthService#
	 * refreshGponOnuAuth(java.lang.Long)
	 */
	@Override
	public void refreshGponOnuAuth(Long entityId) {
		try {
			refreshGponOnuAuthConfig(entityId);
		} catch (Exception e) {
			logger.error("refresh gpon onu authentication faild:{}", e);
		}
		try {
			refreshGponOnuAutoAuthConfig(entityId);
		} catch (Exception e) {
			logger.error("refresh gpon onu auto authentication faild:{}", e);
		}
		try {
			refreshGponOnuFindConfig(entityId);
		} catch (Exception e) {
			logger.error("refresh gpon onu auto find faild:{}", e);
		}
		try {
			refreshGponOnuAuthMode(entityId);
		} catch (Exception e) {
			logger.error("refresh gpon onu auth mode faild:{}", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.topvision.ems.gpon.onuauth.service.GponOnuAuthService#addGponOnuAuth(
	 * com.topvision.ems .gpon.onuauth.domain.GponOnuAuthConfig)
	 */
	@Override
	public void addGponOnuAuth(GponOnuAuthConfig gponOnuAuthConfig, Long onuNo) {
		long entityId = gponOnuAuthConfig.getEntityId();
		SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
		GponOnuAuthFacade facade = facadeFactory.getFacade(snmpParam.getIpAddress(), GponOnuAuthFacade.class);
		// // 自动分配authId
		// List<GponOnuAuthConfig> list = gponOnuAuthDao
		// .selectOnuAuthConfigList(entityId, gponOnuAuthConfig.getPonIndex());
		// Long availableAuthId = 1L;
		// loop: for (; availableAuthId < GponConstant.GPON_MAX_ONU_AUTH_NUM;
		// availableAuthId++) {
		// for (GponOnuAuthConfig config : list) {
		// //由于添加ONU并非频繁操作功能,所以对于AUTHID每次循环的时候都算一次，否则可以缓存每个config的authid减少运算次数
		// Long authId =
		// GponIndex.getOnuNoFromMibIndex(config.getAuthenOnuId());
		// if (authId.equals(availableAuthId)) {
		// continue loop;
		// }
		// }
		// break loop;
		// }
		if (onuNo > GponConstant.GPON_MAX_ONU_AUTH_NUM) {
			throw new GponOnuAuthFullException();
		}
		String sn = gponOnuAuthConfig.getSn();
		String autoFindSn = gponOnuAuthConfig.getSn();
		if (sn == null || "".equals(sn)) {
			gponOnuAuthConfig.setSn(null);
		} else {
			sn = sn.replaceAll(":", "");
			gponOnuAuthConfig.setSn(GponIndex.getMibStringFromGponSn(sn));
		}
		if ("".equals(gponOnuAuthConfig.getPassword())) {
			gponOnuAuthConfig.setPassword(null);
		}
		if ("".equals(gponOnuAuthConfig.getLoidPassword())) {
			gponOnuAuthConfig.setLoidPassword(null);
		}
		if ("".equals(gponOnuAuthConfig.getLoid())) {
			gponOnuAuthConfig.setLoid(null);
		}
		Long ponIndex = gponOnuAuthConfig.getPonIndex();
		Long slotNo = EponIndex.getSlotNo(ponIndex);
		Long ponNo = EponIndex.getPonNo(ponIndex);
		gponOnuAuthConfig.setAuthenOnuId(GponIndex.getOnuAuthId(1L, slotNo, ponNo, onuNo));
		if (GponConstant.ATUH_LOID.equals(gponOnuAuthConfig.getAuthMode())
				|| GponConstant.ATUH_LOID_PWD.equals(gponOnuAuthConfig.getAuthMode())) {
			gponOnuAuthConfig.setSn(null);
			gponOnuAuthConfig.setPassword(null);
		}
		facade.addGponOnuAuth(snmpParam, gponOnuAuthConfig);
		gponOnuAuthDao.insertGponOnuAuth(gponOnuAuthConfig);
		gponOnuAuthDao.deleteGponAutoAuthBySn(entityId, autoFindSn);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.topvision.ems.gpon.onuauth.service.GponOnuAuthService#
	 * modifyOnuAuthMode(com.topvision .ems.gpon.onuauth.domain.GponOnuAuthMode)
	 */
	@Override
	public void modifyOnuAuthMode(GponOnuAuthMode gponOnuAuthMode) {
		SnmpParam snmpParam = entityService.getSnmpParamByEntity(gponOnuAuthMode.getEntityId());
		GponOnuAuthFacade facade = facadeFactory.getFacade(snmpParam.getIpAddress(), GponOnuAuthFacade.class);
		facade.modifyOnuAuthMode(snmpParam, gponOnuAuthMode);
		refreshGponOnuAuth(gponOnuAuthMode.getEntityId());
		// EMS-14320，GPON切换认证模式同EPON相同，不去重新拓扑
		// discoveryService.refresh(gponOnuAuthMode.getEntityId());
		// 切换认证模式会将该端口下ONU全部删除
		Long ponId = oltPonDao.getPonIdByPonIndex(gponOnuAuthMode.getEntityId(), gponOnuAuthMode.getPonIndex());
		List<Long> list = onuDao.getOnuIdByPonId(gponOnuAuthMode.getEntityId(), ponId);
		if (list != null) {
			entityService.removeEntity(list);
			/*
			 * for (Long onuId : list) { onuAuthDao.deleteOnuAllInfo(onuId); }
			 */
		}
	}

	@Override
	public void modifySingleOnuAuthMode(GponOnuAuthMode gponOnuAuthMode) {
		try {
			SnmpParam snmpParam = entityService.getSnmpParamByEntity(gponOnuAuthMode.getEntityId());
			GponOnuAuthFacade facade = facadeFactory.getFacade(snmpParam.getIpAddress(), GponOnuAuthFacade.class);
			facade.modifyOnuAuthMode(snmpParam, gponOnuAuthMode);
		} catch (Exception e) {
			logger.info("modifySingleOnuAuthMode fail {}", e.getMessage());
		}
	}

	@Override
	public String batchModifyGponOnuAuthMode(Long entityId, List<Long> ponIndexs,
			List<GponOnuAuthMode> gponOnuAuthModes) {
		SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
		String result = "success";
		for (GponOnuAuthMode gponOnuAuthMode : gponOnuAuthModes) {
			try {
				GponOnuAuthFacade facade = facadeFactory.getFacade(snmpParam.getIpAddress(), GponOnuAuthFacade.class);
				facade.modifyOnuAuthMode(snmpParam, gponOnuAuthMode);
			} catch (Exception e) {
				// 删除失败将ponIndex从集合中移出
				ponIndexs.remove(gponOnuAuthMode.getPonIndex());
				logger.info("modifyOnuAuthMode fail {}", e.getMessage());
				result = "error";
			}
		}
		refreshGponOnuAuth(entityId);
		if (ponIndexs != null && ponIndexs.size() != 0) {
			// 切换认证模式会将该端口下ONU全部删除
			List<Long> ponIds = oltPonDao.getPonIdsByPonIndexs(entityId, ponIndexs);
			List<Long> list = onuDao.getOnuIdsByPonIds(entityId, ponIds);
			if (list != null) {
				entityService.removeEntity(list);
				/*
				 * for (Long onuId : list) { onuAuthDao.deleteOnuAllInfo(onuId);
				 * }
				 */
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.topvision.ems.gpon.onuauth.service.GponOnuAuthService#addGponAutoAuth
	 * (com.topvision.ems .gpon.onuauth.domain.GponAutoAuthConfig)
	 */
	@Override
	public void addGponAutoAuth(GponAutoAuthConfig gponAutoAuthConfig) {
		Long entityId = gponAutoAuthConfig.getEntityId();
		SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
		GponOnuAuthFacade facade = facadeFactory.getFacade(snmpParam.getIpAddress(), GponOnuAuthFacade.class);
		// 自动分
		List<GponAutoAuthConfig> list = gponOnuAuthDao.selectAutoAuthConfigList(entityId);
		Integer availableAuthId = 1;

		loop: for (; availableAuthId < GponConstant.AUTO_AUTH_MAX_RULE_NUM; availableAuthId++) {
			for (GponAutoAuthConfig config : list) {
				if (availableAuthId.equals(config.getAuthIndex())) {
					continue loop;
				}
			}
			break loop;
		}
		if (availableAuthId.equals(GponConstant.AUTO_AUTH_MAX_RULE_NUM)) {
			throw new AutoAuthRuleFullException();
		}
		gponAutoAuthConfig.setAuthIndex(availableAuthId);
		facade.addGponAutoAuthConfig(snmpParam, gponAutoAuthConfig);
		gponOnuAuthDao.insertGponAutoAuth(gponAutoAuthConfig);
	}

	@Override
	public void updateGponAutoAuth(GponAutoAuthConfig gponAutoAuthConfig) {
		Long entityId = gponAutoAuthConfig.getEntityId();
		SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
		GponOnuAuthFacade facade = facadeFactory.getFacade(snmpParam.getIpAddress(), GponOnuAuthFacade.class);
		GponAutoAuthConfig gponAutoAuthForDel = new GponAutoAuthConfig();
		gponAutoAuthForDel.setEntityId(gponAutoAuthConfig.getEntityId());
		gponAutoAuthForDel.setAuthIndex(gponAutoAuthConfig.getAuthIndex());
		facade.deleteGponAutoAuth(snmpParam, gponAutoAuthForDel);
		facade.addGponAutoAuthConfig(snmpParam, gponAutoAuthConfig);
		gponOnuAuthDao.updateGponAutoAuth(gponAutoAuthConfig);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.topvision.ems.gpon.onuauth.service.GponOnuAuthService#
	 * deleteGponOnuAuth(com.topvision
	 * .ems.gpon.onuauth.domain.GponOnuAuthConfig)
	 */
	@Override
	public void deleteGponOnuAuth(GponOnuAuthConfig gponOnuAuthConfig) {
		Long entityId = gponOnuAuthConfig.getEntityId();
		Long onuId = null;
		SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
		if (gponOnuAuthConfig.getAuthenOnuId() == null) {
			onuId = gponOnuAuthConfig.getOnuId();
			OltOnuAttribute onuAttr = onuDao.getOnuEntityById(onuId);
			GponOnuAuthConfig authConfig = gponOnuAuthDao.selectOnuAuthConfig(entityId,
					onuAttr.getOnuUniqueIdentification());
			if (authConfig != null) {
				gponOnuAuthConfig.setAuthenOnuId(authConfig.getAuthenOnuId());
			}
		} else {
			String sn = gponOnuAuthDao.selectOnuAuthConfigSn(entityId, gponOnuAuthConfig.getAuthenOnuId());
			Long onuIndex = EponIndex.getOnuIndexByMibIndex(gponOnuAuthConfig.getAuthenOnuId());
			onuId = onuDao.getOnuIdByIndex(entityId, onuIndex);
			// OltOnuAttribute onuAttr =
			// onuDao.getOnuAttributeByUniqueId(entityId, sn);
			// if (onuAttr != null) {
			// onuId = onuAttr.getOnuId();
			// }
		}
		GponOnuAuthFacade facade = facadeFactory.getFacade(snmpParam.getIpAddress(), GponOnuAuthFacade.class);
		facade.deleteGponOnuAuth(snmpParam, gponOnuAuthConfig);
		gponOnuAuthDao.deleteGponOnuAuth(gponOnuAuthConfig);
		// EMS 14320
		// discoveryService.refresh(gponOnuAuthConfig.getEntityId());
		// 同时删除所有的ONU相关表项
		List<Long> list = new ArrayList<>();
		if (onuId != null) {
			list.add(onuId);
			entityService.removeEntity(list);
			// onuAuthDao.deleteOnuAllInfo(onuId);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.topvision.ems.gpon.onuauth.service.GponOnuAuthService#
	 * queryGponOnuAutoFind(java.util.Map)
	 */
	@Override
	public List<GponOnuAutoFind> queryGponOnuAutoFind(Map<String, Object> conditions) {
		return gponOnuAuthDao.queryGponOnuAutoFindList(conditions);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.topvision.ems.gpon.onuauth.service.GponOnuAuthService#
	 * queryGponOnuAutoFindCount(java. util.Map)
	 */
	@Override
	public int queryGponOnuAutoFindCount(Map<String, Object> conditions) {
		return gponOnuAuthDao.queryGponOnuAutoFindCount(conditions);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.topvision.ems.gpon.onuauth.service.GponOnuAuthService#
	 * queryGponOnuAutoFind(java.lang. Long, java.lang.Long)
	 */
	@Override
	public GponOnuAutoFind queryGponOnuAutoFind(Long entityId, Long onuIndex) {
		return gponOnuAuthDao.queryGponOnuAutoFind(entityId, onuIndex);
	}

	@Override
	public GponOnuAutoFind selectGponOnuAutoFind(Long entityId, Long onuIndex) {
		return gponOnuAuthDao.selectGponOnuAutoFind(entityId, onuIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.topvision.ems.gpon.onuauth.service.GponOnuAuthService#
	 * deleteGponAutoAuth(com.topvision
	 * .ems.gpon.onuauth.domain.GponAutoAuthConfig)
	 */
	@Override
	public void deleteGponAutoAuth(GponAutoAuthConfig gponAutoAuthConfig) {
		SnmpParam snmpParam = entityService.getSnmpParamByEntity(gponAutoAuthConfig.getEntityId());
		GponOnuAuthFacade facade = facadeFactory.getFacade(snmpParam.getIpAddress(), GponOnuAuthFacade.class);
		facade.deleteGponAutoAuth(snmpParam, gponAutoAuthConfig);
		gponOnuAuthDao.deleteGponAutoAuth(gponAutoAuthConfig);
	}

	@Override
	public GponAutoAuthConfig queryAutoAuthConfig(GponAutoAuthConfig gponAutoAuthConfig) {
		return gponOnuAuthDao.queryAutoAuthConfig(gponAutoAuthConfig);
	}

	@Override
	public List<Long> getOnuAuthIdList(Long ponId) {
		List<Long> newList = gponOnuAuthDao.getOnuAuthIdList(ponId);
		List<Long> onuNoList = new ArrayList<Long>();
		for (Long aNewList : newList) {
			onuNoList.add(GponIndex.getOnuNoFromMibIndex(aNewList));
		}
		return onuNoList;
	}

	@Override
	public List<GponOnuAutoFind> loadAutoFindOnuList(Map<String, Object> conditions) {
		return gponOnuAuthDao.selectAutoFindOnuList(conditions);
	}

	@Override
	public Long selectPonId(long entityId, Long ponIndex) {
		return gponOnuAuthDao.selectPonId(entityId, ponIndex);
	}

	@Override
	public Integer getAutoFindOnuNum(Map<String, Object> conditions) {
		return gponOnuAuthDao.getAutoFindOnuNum(conditions);
	}
}
