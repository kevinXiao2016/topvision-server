/***********************************************************************
 * $Id: OnuUpgradeTrapExecute.java,v1.0 2017年1月9日 上午10:53:26 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.fault.trap;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.onu.dao.OnuDao;
import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.onu.domain.OltTopOnuCapability;
import com.topvision.ems.epon.onu.facade.OnuFacade;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.fault.execute.EventExecute;
import com.topvision.ems.fault.execute.TrapExceutor;
import com.topvision.ems.fault.message.TrapEvent;
import com.topvision.ems.network.dao.EntityDao;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.facade.FacadeFactory;

/**
 * @author lizongtian
 * @created @2017年1月9日-上午10:53:26
 *
 */
@Service("OnuUpgradeTrapExecute")
@EventExecute(code = "event.code.{EponCode.ONU_UPGRADE_OK},event.code.{EponCode.ONU_AUTO_UPGRADE}")
public class OnuUpgradeTrapExecute extends BaseService implements TrapExceutor {
    @Autowired
    private EntityService entityService;
    @Autowired
    private EntityDao entityDao;
    @Autowired
    private OnuDao onuDao;
    @Autowired
    private FacadeFactory facadeFactory;

    @Override
    public void execute(TrapEvent evt) {
        Entity entity = entityDao.getEntityByIp(evt.getTrap().getAddress());
        Long entityId = entity.getEntityId();
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        try {
            Thread.sleep(60000);
            List<OltOnuAttribute> onuAttributes = facadeFactory.getFacade(evt.getTrap().getAddress(), OnuFacade.class)
                    .getOnuAttributes(snmpParam);
            List<OltTopOnuCapability> onuCapabilities = facadeFactory.getFacade(evt.getTrap().getAddress(),
                    OnuFacade.class).getOnuListAttribute(snmpParam);
            onuDao.batchUpdateOnuSoftVersion(entityId, onuAttributes);
            onuDao.batchUpdateOnuHardVersion(entityId, onuCapabilities);
        } catch (InterruptedException e) {
            logger.error("", e);
        }
    }
}
