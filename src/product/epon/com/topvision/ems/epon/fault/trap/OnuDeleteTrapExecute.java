/***********************************************************************
 * $Id: OnuDeleteTrapExecute.java,v1.0 2017年1月11日 下午4:24:19 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.fault.trap;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.onu.dao.OnuDao;
import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.onuauth.dao.OnuAuthDao;
import com.topvision.ems.fault.execute.EventExecute;
import com.topvision.ems.fault.execute.TrapExceutor;
import com.topvision.ems.fault.message.TrapEvent;
import com.topvision.ems.network.dao.EntityDao;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.service.BaseService;

/**
 * @author lizongtian
 * @created @2017年1月11日-下午4:24:19
 *
 */
@Service("OnuDeleteTrapExecute")
@EventExecute(code = "event.code.{EponCode.ONU_DELETE},event.code.{EponCode.CCMTS_DELETE}")
public class OnuDeleteTrapExecute extends BaseService implements TrapExceutor {
    @Autowired
    private OnuDao onuDao;
    @Autowired
    private EntityDao entityDao;
    @Autowired
    private OnuAuthDao onuAuthDao;
    @Autowired
    private EntityService entityService;

    @Override
    public void execute(TrapEvent evt) {
        try {
            Thread.sleep(10000);
            Long entityId = evt.getEntityId();
            OltOnuAttribute onu = onuDao.getOnuEntityById(entityId);
            Long onuIndex = onuDao.getOnuIndex(onu.getOnuId());
            if (onu != null) {
                //删除认证规则
                Long ponId = onuDao.getPonIdByOnuIndex(onu.getEntityId(), onuIndex);
                onuAuthDao.deleteAuthenPreConfig(ponId, onuIndex);
                // 解绑定成功后发消息进行删除设备的相关操作
                List<Long> entityIds = new ArrayList<>();
                entityIds.add(onu.getOnuId());
                entityService.removeEntity(entityIds);
            }
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
