/***********************************************************************
 * $Id: OnuIllegalRegTrapExecute.java,v1.0 2017年6月30日 上午10:56:08 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.fault.trap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.onu.dao.OnuDao;
import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.onu.service.RogueOnuService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.fault.execute.EventExecute;
import com.topvision.ems.fault.execute.TrapExceutor;
import com.topvision.ems.fault.message.TrapEvent;
import com.topvision.ems.network.dao.EntityDao;
import com.topvision.ems.network.domain.EntitySnap;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.utils.EponConstants;

/**
 * @author lizongtian
 * @created @2017年6月30日-上午10:56:08
 *
 */
@Service("RogueOnuTrapExecute")
@EventExecute(code = "event.code.{EponCode.ONU_ROGUE}")
public class RogueOnuTrapExecute extends BaseService implements TrapExceutor {
    @Autowired
    private RogueOnuService rogueOnuService;
    @Autowired
    private OnuDao onuDao;
    @Autowired
    private EntityDao entityDao;

    @Override
    public void execute(TrapEvent evt) {
        Long index = evt.getDeviceIndex();
        Entity entity = entityDao.getEntityByIp(evt.getTrap().getAddress());
        OltOnuAttribute onu = onuDao.getOnuAttributeByIndex(entity.getEntityId(), index);
        if (onu != null) {
            // onu.setOnuOperationStatus(EponConstants.ONU_STATUS_DOWN);
            // onuDao.insertOrUpdateOltOnuAttribute(onu);
            onuDao.updateOnuOperationStatus(onu.getOnuId(), EponConstants.ONU_STATUS_DOWN);
            // 更新entitysnap表中onu的在线状态
            EntitySnap onuSnap = new EntitySnap();
            onuSnap.setEntityId(onu.getOnuId());
            onuSnap.setState(false);
            entityDao.updateOnuEntitySnap(onuSnap);
        }
        try {
            rogueOnuService.refreshPonPortRogueEntry(entity.getEntityId());
        } catch (Exception e) {
            logger.info("trap ONU_ROGUE refreshPonPortRogueEntry", e);
        }
        try {
            rogueOnuService.refreshOnuLaserSwitch(entity.getEntityId(), onu.getOnuId());
        } catch (Exception e) {
            logger.info("trap ONU_ROGUE refreshOnuLaserSwitch", e);
        }

    }
}
