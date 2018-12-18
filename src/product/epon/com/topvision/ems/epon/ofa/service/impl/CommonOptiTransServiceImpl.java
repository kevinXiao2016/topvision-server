package com.topvision.ems.epon.ofa.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.ofa.dao.CommonOptiTransDao;
import com.topvision.ems.epon.ofa.facade.CommonOptiTransFacade;
import com.topvision.ems.epon.ofa.facade.domain.CommonOptiTrans;
import com.topvision.ems.epon.ofa.service.CommonOptiTransService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.message.event.SynchronizedEvent;
import com.topvision.platform.message.event.SynchronizedListener;
import com.topvision.platform.message.service.MessageService;

/**
 * 
 * @author CWQ
 * @created @2017年10月14日-下午5:10:46
 *
 */
@Service("commonOptiTransService")
public class CommonOptiTransServiceImpl extends BaseService implements CommonOptiTransService, SynchronizedListener {

    @Autowired
    private CommonOptiTransDao commonOptiTransDeviceDao;
    @Autowired
    private EntityService entityService;
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private MessageService messageService;

    @Override
    public CommonOptiTrans getCommonOptiTrans(Long entityId) {
        return commonOptiTransDeviceDao.getCommonOptiTransById(entityId);
    }

    @Override
    public void modifyCommonOptiTrans(CommonOptiTrans commonOptiTrans, Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        CommonOptiTrans newCommonOptiTrans = getCommonOptiTransDeviceFacade(
                snmpParam.getIpAddress()).modifyCommonOptiTrans(snmpParam, commonOptiTrans);
        newCommonOptiTrans.setEntityId(entityId);
        commonOptiTransDeviceDao.insertOrUpdateCommonOptiTrans(newCommonOptiTrans);
    }

    public CommonOptiTransFacade getCommonOptiTransDeviceFacade(String ip) {
        return facadeFactory.getFacade(ip, CommonOptiTransFacade.class);
    }

    @Override
    public void refreshCommonOptiTrans(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<CommonOptiTrans> commonOptiTransList = getCommonOptiTransDeviceFacade(snmpParam.getIpAddress())
                .getCommonOptiTrans(snmpParam);
        for(CommonOptiTrans commonOptiTrans:commonOptiTransList){
        	commonOptiTrans.setEntityId(entityId);
        }
        commonOptiTransDeviceDao.batchInsertOrUpdateCommonOptiTrans(commonOptiTransList);
    }

    @Override
    public void insertEntityStates(SynchronizedEvent event) {
        try {
            refreshCommonOptiTrans(event.getEntityId());
            logger.info("refreshCommonOptiTrans finish");
        } catch (Exception e) {
            logger.error("refreshCommonOptiTrans wrong", e);
        }
    }

	@Override
	public void updateEntityStates(SynchronizedEvent event) {
	}

	@PostConstruct
    @Override
    public void initialize() {
        super.initialize();
        messageService.addListener(SynchronizedListener.class, this);
    }
	
	@PreDestroy
    @Override
    public void destroy() {
        super.destroy();
        messageService.removeListener(SynchronizedListener.class, this);
    }
}
