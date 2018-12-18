package com.topvision.ems.resources.service;

import java.util.List;

import com.topvision.ems.resources.domain.EntityCategoryStat;
import com.topvision.ems.resources.domain.EntityStastic;
import com.topvision.ems.resources.domain.EntityStat;
import com.topvision.framework.service.Service;

public interface ResourcesService extends Service {

    /**
     * 按大类统计设备数量.
     * 
     * @return
     * @throws Exception
     */
    EntityStat statEntity() throws Exception;

    /**
     * 按小类统计设备数量.
     * 
     * @return
     * @throws Exception
     */
    EntityCategoryStat statEntityCategory() throws Exception;

    /**
     * 统计SNMP设备的数量.
     * 
     * @return
     * @throws Exception
     */
    EntityStat statSnmpEntity() throws Exception;

    /**
     * 按小类统计SNMP设备数量.
     * 
     * @return
     * @throws Exception
     */
    EntityCategoryStat statSnmpEntityCategory() throws Exception;

    List<EntityStastic> getEntityStasticByState();

}
