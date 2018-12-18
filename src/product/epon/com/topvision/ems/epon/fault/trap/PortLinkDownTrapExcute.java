/***********************************************************************
 * $Id: PortLinkDownTrapExcute.java,v1.0 2017年1月11日 下午2:34:42 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.fault.trap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.fault.helper.PortConvertHelper;
import com.topvision.ems.epon.olt.service.OltSniService;
import com.topvision.ems.fault.execute.EventExecute;
import com.topvision.ems.fault.execute.TrapExceutor;
import com.topvision.ems.fault.message.TrapEvent;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.utils.EponIndex;

/**
 * @author lizongtian
 * @created @2017年1月11日-下午2:34:42
 *
 */
@Service("PortLinkDownTrapExcute")
@EventExecute(code = "event.code.{EponCode.PORT_LINK_DOWN}")
public class PortLinkDownTrapExcute extends BaseService implements TrapExceutor {
    @Autowired
    private OltSniService oltSniService;
    @Autowired
    private PortConvertHelper portConvertHelper;

    @Override
    public void execute(TrapEvent evt) {
        Long entityId = evt.getEntityId();
        String source = evt.getSource();
        Long sniIndex = 0L;
        if (source != null) {
            Integer slot = Integer.parseInt(source.split("/")[0]);
            // 如果slot号没在oltTrapParser中转换，就需要判断是否主控，进而转换
            if (slot != 0) {
                slot = portConvertHelper.convertSlot(entityId, slot);
            }
            Integer port = Integer.parseInt(source.split("/")[1]);
            sniIndex = EponIndex.getSniIndex(slot, port);
        }
        Long sniId = oltSniService.getSniIdByIndex(sniIndex, entityId);
        oltSniService.updateSniPortStatus(entityId, sniId);
    }
}
