/***********************************************************************
 * $ EponGetTopoFolderNum.java,v1.0 2012-3-27 14:16:07 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.topology.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.olt.dao.OltDao;
import com.topvision.ems.epon.onu.dao.OnuDao;
import com.topvision.ems.network.domain.TopoFolderNum;
import com.topvision.ems.network.domain.TopoFolderStat;
import com.topvision.ems.network.service.GetTopoFolderNum;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.service.BaseService;

/**
 * @author jay
 * @created @2012-3-27-14:16:07
 */
@Service("eponGetTopoFolderNum")
public class EponGetTopoFolderNum extends BaseService implements GetTopoFolderNum {
    @Autowired
    private OltDao oltDao;
    @Autowired
    private OnuDao onuDao;
    @Autowired
    private EntityTypeService entityTypeService;

    /**
     * 获取本模块的设备统计结果
     * 
     * @return List<TopoFolderNum>
     * @param topoFolderStats
     */
    @Override
    public Map<Long, List<TopoFolderNum>> getTopoFolder(List<TopoFolderStat> topoFolderStats) {
        Map<Long, List<TopoFolderNum>> re = new HashMap<Long, List<TopoFolderNum>>();
        if (topoFolderStats.size() > 0) {
            for (TopoFolderStat topoFolderStat : topoFolderStats) {
                List<TopoFolderNum> list = new ArrayList<TopoFolderNum>();
                re.put(topoFolderStat.getFolderId(), list);
                Long oltType = entityTypeService.getOltType();
                @SuppressWarnings("deprecation")
                List<Long> oltList = oltDao.getEntityByFolder(topoFolderStat.getFolderId(), oltType);
                Long onuType = entityTypeService.getOnuType();
                List<Long> onuList = onuDao.getOnuIdByFolder(onuType, topoFolderStat.getFolderId());
                TopoFolderNum oltTopoFolderNum = new TopoFolderNum();
                oltTopoFolderNum.setColName("OLT");
                oltTopoFolderNum.setNum(oltList.size());
                list.add(oltTopoFolderNum);
                TopoFolderNum onuTopoFolderNum = new TopoFolderNum();
                onuTopoFolderNum.setColName("ONU");
                onuTopoFolderNum.setNum(onuList.size());
                list.add(onuTopoFolderNum);
            }
        }
        return re;
    }

    @Override
    public List<String> getColNames() {
        List<String> re = new ArrayList<String>();
        re.add("OLT");
        re.add("ONU");
        return re;
    }
}
