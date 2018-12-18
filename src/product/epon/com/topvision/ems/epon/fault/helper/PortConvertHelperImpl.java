package com.topvision.ems.epon.fault.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.olt.service.OltSlotService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.service.BaseService;

/**
 * 上联口 转换辅助类
 * 
 * @author w1992wishes
 * @created @2018年1月16日-下午4:22:57
 *
 */
@Service("portConvertHelper")
public class PortConvertHelperImpl extends BaseService implements PortConvertHelper {

    @Autowired
    private EntityService entityService;
    @Autowired
    private EntityTypeService entityTypeService;
    @Autowired
    private OltSlotService oltSlotService;

    public Integer convertPort(Long entityId, Integer port) {
        Entity entity = entityService.getEntity(entityId);
        if (entity == null) {
            return port;
        }
        if ((entityTypeService.isPN8602_EFType(entity.getTypeId()) || entityTypeService.isPN8602_EType(entity
                .getTypeId())) && port < 16) {
            port = port + 16;
        }
        return port;
    }

    public Integer convertSlot(Long entityId, Integer slot) {
        boolean controlBoard = oltSlotService.slotIsControlBooard(entityId, slot);
        if (controlBoard) {
            slot = 0;
        }
        return slot;
    }

}
