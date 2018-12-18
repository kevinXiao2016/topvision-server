/***********************************************************************
 * $Id: DeviceResetTrapExecute.java,v1.0 2017年1月9日 上午10:30:37 $
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
 * @created @2017年1月9日-上午10:30:37
 *
 */
@Service("DeviceResetTrapExecute")
@EventExecute(code = "event.code.{EponCode.DEVICE_RESET}")
public class DeviceResetTrapExecute extends BaseService implements TrapExceutor {
    @Autowired
    private EntityDao entityDao;
    @Autowired
    private OnuDao onuDao;

    @Override
    public void execute(TrapEvent evt) {
        //更新OLT的状态和在线时长
        Entity entity = entityDao.getEntityByIp(evt.getTrap().getAddress());
        EntitySnap snap = new EntitySnap();
        snap.setEntityId(entity.getEntityId());
        snap.setDelay(-1);
        snap.setSysUpTime("-1");
        snap.setState(false);
        entityDao.updateEntityState(snap);
        //更新下级设备状态
        List<Long> onuIds = onuDao.getOnuIdList(entity.getEntityId());
        for (Long onuId : onuIds) {
            onuDao.updateOnuOperationStatus(onuId, EponConstants.ONU_STATUS_DOWN);
            // 更新entitysnap表中onu的在线状态
            EntitySnap onuSnap = new EntitySnap();
            onuSnap.setEntityId(onuId);
            onuSnap.setState(false);
            entityDao.updateOnuEntitySnap(onuSnap);
        }
    }
}
