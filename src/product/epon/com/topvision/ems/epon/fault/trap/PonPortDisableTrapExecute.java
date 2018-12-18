/***********************************************************************
 * $Id: PonPortDisableTrapExecute.java,v1.0 2017年1月11日 下午4:02:52 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.fault.trap;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.olt.dao.OltPonDao;
import com.topvision.ems.epon.olt.service.OltPonService;
import com.topvision.ems.epon.olt.service.OltSlotService;
import com.topvision.ems.epon.onu.dao.OnuDao;
import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.onu.service.OnuService;
import com.topvision.ems.fault.execute.EventExecute;
import com.topvision.ems.fault.execute.TrapExceutor;
import com.topvision.ems.fault.message.TrapEvent;
import com.topvision.ems.network.dao.EntityDao;
import com.topvision.ems.network.domain.EntitySnap;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.utils.EponConstants;
import com.topvision.framework.utils.EponIndex;

/**
 * @author lizongtian
 * @created @2017年1月11日-下午4:02:52
 *
 */
@Service("PonPortDisableTrapExecute")
@EventExecute(code = "event.code.{EponCode.PORT_PON_DISABLE}")
public class PonPortDisableTrapExecute extends BaseService implements TrapExceutor {
    @Autowired
    private OltPonService oltPonService;
    @Autowired
    private OnuService onuService;
    @Autowired
    private OltPonDao oltPonDao;
    @Autowired
    private OnuDao onuDao;
    @Autowired
    private EntityDao entityDao;
    @Autowired
    private OltSlotService oltSlotService;

    @Override
    public void execute(TrapEvent evt) {
        Long entityId = evt.getEntityId();
        String source = evt.getSource();
        Long ponIndex = 0L;
        if (source != null) {
            Integer slot = Integer.parseInt(source.split("/")[0]);
            if (!slot.equals(0)) {
                boolean controlBoard = oltSlotService.slotIsControlBooard(entityId, slot);
                if (controlBoard) {
                    slot = 0;
                }
            }
            Integer port = Integer.parseInt(source.split("/")[1]);
            ponIndex = EponIndex.getPonIndex(slot, port);
        }
        Long ponId = oltPonService.getPonIdByIndex(entityId, ponIndex);
        if (evt.getIsClear() != null && evt.getIsClear()) {
            oltPonDao.updatePonAdminStatus(ponId, EponConstants.PORT_STATUS_UP);
            oltPonDao.updatePonOperationStatus(ponId, EponConstants.PORT_STATUS_UP);
        } else {
            oltPonDao.updatePonAdminStatus(ponId, EponConstants.PORT_STATUS_DOWN);
            oltPonDao.updatePonOperationStatus(ponId, EponConstants.PORT_STATUS_DOWN);
        }
        List<OltOnuAttribute> onuList = onuService.getOnuListByPonId(ponId);
        for (OltOnuAttribute onu : onuList) {
            onuDao.updateOnuOperationStatus(onu.getOnuId(), EponConstants.ONU_STATUS_DOWN);
            // 更新entitysnap表中onu的在线状态
            EntitySnap onuSnap = new EntitySnap();
            onuSnap.setEntityId(onu.getOnuId());
            onuSnap.setState(false);
            entityDao.updateOnuEntitySnap(onuSnap);
        }
    }
}
