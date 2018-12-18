/***********************************************************************
 * $Id: OnuOfflineTrapExecute.java,v1.0 2017年1月11日 下午4:22:29 $
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
import com.topvision.ems.epon.onu.domain.OnuEventLogEntry;
import com.topvision.ems.epon.onu.service.OnuOnOffRecordCollectService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.fault.execute.EventExecute;
import com.topvision.ems.fault.execute.TrapExceutor;
import com.topvision.ems.fault.message.TrapEvent;
import com.topvision.ems.network.dao.EntityDao;
import com.topvision.ems.network.domain.EntitySnap;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.utils.EponConstants;

/**
 * @author lizongtian
 * @created @2017年1月11日-下午4:22:29 处理下线、断纤、断电
 *
 */
@Service("OnuOfflineTrapExecute")
@EventExecute(code = "event.code.{EponCode.ONU_OFFLINE},event.code.{EponCode.ONU_FIBER_BREAK},event.code.{EponCode.ONU_PWR_DOWN}")
public class OnuOfflineTrapExecute extends BaseService implements TrapExceutor {
    @Autowired
    private OnuDao onuDao;
    @Autowired
    private EntityDao entityDao;
    @Autowired
    private EntityService entityService;
    @Autowired
    private OnuOnOffRecordCollectService onuOnOffRecordCollectService;

    @Override
    public void execute(TrapEvent evt) {
        Long index = evt.getDeviceIndex();
        Entity entity = entityDao.getEntityByIp(evt.getTrap().getAddress());
        OltOnuAttribute onu = onuDao.getOnuAttributeByIndex(entity.getEntityId(), index);
        if (onu != null) {
            // onu.setOnuOperationStatus(EponConstants.ONU_STATUS_DOWN);
            // onuDao.insertOrUpdateOltOnuAttribute(onu);
            onuDao.updateOnuOperationStatus(onu.getOnuId(), EponConstants.ONU_STATUS_DOWN);

            // EMS-14957 更新上下线记录
            OnuEventLogEntry onuEventLogEntry = new OnuEventLogEntry();
            onuEventLogEntry.setEntityId(onu.getEntityId());
            onuEventLogEntry.setOnuIndex(onu.getOnuIndex());
            try {
                onuOnOffRecordCollectService.refreshOnOffRecords(onuEventLogEntry);
            } catch (Exception e) {
                logger.error("refresh onu onoff records failure", e);
            }

            // 更新entitysnap表中onu的在线状态
            EntitySnap onuSnap = new EntitySnap();
            onuSnap.setEntityId(onu.getOnuId());
            onuSnap.setState(false);
            entityDao.updateOnuEntitySnap(onuSnap);
        }
    }

}
