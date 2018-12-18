/***********************************************************************
 * $Id: PonOpticalRemoveTrapExecute.java,v1.0 2017年1月11日 下午4:20:50 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.fault.trap;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.fault.helper.PortConvertHelper;
import com.topvision.ems.epon.olt.dao.OltPonDao;
import com.topvision.ems.epon.olt.dao.OltSniDao;
import com.topvision.ems.epon.olt.service.OltPonService;
import com.topvision.ems.epon.olt.service.OltSniService;
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
 * @created @2017年1月11日-下午4:20:50
 *
 */
@Service("PonOpticalRemoveTrapExecute")
@EventExecute(code = "event.code.{EponCode.PON_OPTICAL_REMOVE}")
public class PonOpticalRemoveTrapExecute extends BaseService implements TrapExceutor {

    @Autowired
    private OltPonService oltPonService;
    @Autowired
    private OltSniService oltSniService;
    @Autowired
    private OnuService onuService;
    @Autowired
    private OltPonDao oltPonDao;
    @Autowired
    private OltSniDao oltSniDao;
    @Autowired
    private OnuDao onuDao;
    @Autowired
    private EntityDao entityDao;
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
            oltPonDao.updatePonOperationStatus(ponId, EponConstants.PORT_STATUS_DOWN);
            List<OltOnuAttribute> onuList = onuService.getOnuListByPonId(ponId);
            for (OltOnuAttribute onu : onuList) {
                onuDao.updateOnuOperationStatus(onu.getOnuId(), EponConstants.ONU_STATUS_DOWN);
                // 更新entitysnap表中onu的在线状态
                EntitySnap onuSnap = new EntitySnap();
                onuSnap.setEntityId(onu.getOnuId());
                onuSnap.setState(false);
                entityDao.updateOnuEntitySnap(onuSnap);
            }
        } else {
            // 上联口走这这段
            Long sniId = oltSniService.getSniIdByIndex(portIndex, entityId);
            if (sniId != null) {
                oltSniDao.updateSniOperationStatus(sniId, EponConstants.ONU_STATUS_DOWN);
            }
        }
    }
}
