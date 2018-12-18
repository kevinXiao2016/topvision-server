/***********************************************************************
 * $Id: UniLinkDownTrapExecute.java,v1.0 2017年1月11日 下午4:24:36 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.fault.trap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.onu.dao.UniDao;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.fault.execute.EventExecute;
import com.topvision.ems.fault.execute.TrapExceutor;
import com.topvision.ems.fault.message.TrapEvent;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.utils.EponConstants;
import com.topvision.framework.utils.EponIndex;

/**
 * @author lizongtian
 * @created @2017年1月11日-下午4:24:36
 *
 */
@Service("UniLinkDownTrapExecute")
@EventExecute(code = "event.code.{EponCode.ONU_UNI_LINKDOWN}")
public class UniLinkDownTrapExecute extends BaseService implements TrapExceutor {
    @Autowired
    private UniDao uniDao;
    @Autowired
    private EntityService entityService;

    @Override
    public void execute(TrapEvent evt) {
        Long entityId = evt.getEntityId();
        String source = evt.getSource();
        Long uniIndex = 0L;
        if (source != null) {
            Integer slotNo = Integer.parseInt(source.split(":")[1].split("/")[0]);
            Integer ponNo = Integer.parseInt(source.split(":")[1].split("/")[1]);
            Integer onuNo = Integer.parseInt(source.split(":")[2].split("/")[0]);
            Integer uniNo = Integer.parseInt(source.split(":")[2].split("/")[1]);
            uniIndex = EponIndex.getUniIndex(slotNo, ponNo, onuNo, uniNo);
        }
        Entity entity = entityService.getEntity(entityId);
        Long uniId = uniDao.getUniIdByIndex(entity.getParentId(), uniIndex);
        uniDao.updateUniOperaStatus(uniId, EponConstants.PORT_STATUS_DOWN);
    }

}
