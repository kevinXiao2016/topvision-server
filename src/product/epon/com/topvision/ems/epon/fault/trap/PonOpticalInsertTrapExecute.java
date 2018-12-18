/***********************************************************************
 * $Id: PonOpticalInsertTrapExecute.java,v1.0 2017年1月11日 下午4:21:13 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.fault.trap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.fault.helper.PortConvertHelper;
import com.topvision.ems.epon.olt.dao.OltPonDao;
import com.topvision.ems.epon.olt.service.OltPonService;
import com.topvision.ems.epon.olt.service.OltSniService;
import com.topvision.ems.fault.execute.EventExecute;
import com.topvision.ems.fault.execute.TrapExceutor;
import com.topvision.ems.fault.message.TrapEvent;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.utils.EponIndex;

/**
 * @author lizongtian
 * @created @2017年1月11日-下午4:21:13
 *
 */
@Service("PonOpticalInsertTrapExecute")
@EventExecute(code = "event.code.{EponCode.PON_OPTICAL_INSERT}")
public class PonOpticalInsertTrapExecute extends BaseService implements TrapExceutor {

    @Autowired
    private OltPonService oltPonService;
    @Autowired
    private OltSniService oltSniService;
    @Autowired
    private OltPonDao oltPonDao;
    @Autowired
    private PortConvertHelper portConvertHelper;

    @Override
    public void execute(TrapEvent evt) {
        Long entityId = evt.getEntityId();
        String source = evt.getSource();
        Long portIndex = 0L;
        Integer slot = null;
        Integer port = null;
        if (source != null) {
            slot = Integer.parseInt(source.split("/")[0]);
            if (!slot.equals(0)) {
                // 判断是否是主控，如果是主控，将slot设为0
                slot = portConvertHelper.convertSlot(entityId, slot);
            }
            port = Integer.parseInt(source.split("/")[1]);
            portIndex = EponIndex.getPonIndex(slot, port);
        }
        Long ponId = oltPonService.getPonIdByIndex(entityId, portIndex);

        if (ponId != null) {
            // 不是上联口走这段
            oltPonService.refreshPonStatus(entityId, portIndex);
        } else {
            Long sniId = oltSniService.getSniIdByIndex(portIndex, entityId);
            if (sniId != null) {
                oltSniService.updateSniPortStatus(entityId, sniId);
            }
        }

    }

}
