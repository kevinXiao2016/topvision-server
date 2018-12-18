/***********************************************************************
 * $Id: BoardTempTrapExecute.java,v1.0 2017年1月4日 下午2:48:08 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.fault.trap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.olt.service.OltSlotService;
import com.topvision.ems.fault.execute.EventExecute;
import com.topvision.ems.fault.execute.TrapExceutor;
import com.topvision.ems.fault.message.TrapEvent;
import com.topvision.ems.network.dao.EntityDao;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.utils.EponIndex;

/**
 * @author lizongtian
 * @created @2017年1月4日-下午2:48:08
 *
 */
@Service("BoardTempTrapExecute")
@EventExecute(code = "event.code.{EponCode.BD_TEMP_HIGH},event.code.{EponCode.BD_TEMP_LOW},event.code.{EponCode.BD_TEMP_OK}")
public class BoardTempTrapExecute extends BaseService implements TrapExceutor {
    @Autowired
    private OltSlotService oltSlotService;
    @Autowired
    private EntityDao entityDao;

    @Override
    public void execute(TrapEvent evt) {
        Long entityId = evt.getEntityId();
        Long slotIndex = EponIndex.getSlotIndex(Integer.parseInt(evt.getSource().split(":")[1], 16));
        oltSlotService.refreshBoardLampStatus(entityId, Long.parseLong(evt.getSource().split(":")[1], 16), slotIndex);
        oltSlotService.refreshBoardAlarmStatus(entityId, slotIndex);
    }

}
