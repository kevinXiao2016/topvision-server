package com.topvision.ems.epon.fault.trap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.olt.dao.OltPonDao;
import com.topvision.ems.epon.olt.service.OltPonService;
import com.topvision.ems.epon.olt.service.OltSlotService;
import com.topvision.ems.fault.execute.EventExecute;
import com.topvision.ems.fault.execute.TrapExceutor;
import com.topvision.ems.fault.message.TrapEvent;
import com.topvision.ems.network.dao.EntityDao;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.utils.EponConstants;
import com.topvision.framework.utils.EponIndex;

@Service("PonPortDisableOkTrapExecute")
@EventExecute(code = "event.code.{EponCode.PORT_PON_DISABLE_OK}")
public class PonPortDisableOkTrapExecute extends BaseService implements TrapExceutor {
    @Autowired
    private OltPonService oltPonService;
    @Autowired
    private OltPonDao oltPonDao;
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
        oltPonDao.updatePonAdminStatus(ponId, EponConstants.PORT_STATUS_UP);
    }

}
