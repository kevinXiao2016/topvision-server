/***********************************************************************
 * $ CcmtsGetTopoFolderNum.java,v1.0 2012-3-27 14:17:02 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.ccmts.dao.CmcDao;
import com.topvision.ems.network.domain.TopoFolderNum;
import com.topvision.ems.network.domain.TopoFolderStat;
import com.topvision.ems.network.service.GetTopoFolderNum;
import com.topvision.ems.template.service.EntityTypeService;

/**
 * @author jay
 * @created @2012-3-27-14:17:02
 */
@Service("ccmtsGetTopoFolderNum")
public class CcmtsGetTopoFolderNum implements GetTopoFolderNum {
	@Resource(name = "cmcDao")
	private CmcDao cmcDao;
	@Resource(name = "entityTypeService")
	private EntityTypeService entityTypeService;

    public CmcDao getCmcDao() {
        return cmcDao;
    }

    public void setCmcDao(CmcDao cmcDao) {
        this.cmcDao = cmcDao;
    }

    /**
     * 获取本模块的设备统计结果
     * 
     * @return List<TopoFolderNum>
     * @param topoFolderStats List<TopoFolderStat>
     */
    public Map<Long, List<TopoFolderNum>> getTopoFolder(List<TopoFolderStat> topoFolderStats) {
        Map<Long, List<TopoFolderNum>> re = new HashMap<Long, List<TopoFolderNum>>();
        if (topoFolderStats.size() > 0) {
            for (TopoFolderStat topoFolderStat : topoFolderStats) {
                List<TopoFolderNum> list = new ArrayList<TopoFolderNum>();
                re.put(topoFolderStat.getFolderId(), list);
                List<Long> ccmList = new ArrayList<Long>();
                // 有entity的CC
                Long cmtsType = entityTypeService.getCcmtsandcmtsType();
                List<Long> ccEntityList = cmcDao.getCmcIdByFolder(topoFolderStat.getFolderId(), cmtsType);
                if (ccEntityList != null) {
                    for (Long le : ccEntityList) {
                        ccmList.add(le);
                    }
                }

                // TODO 暂时不统计CM的个数
                // CM
//                Integer cmNum = 0;
//                if (ccmList.size() > 0) {
//                    for (Long cmc : ccmList) {
//                        cmNum += cmcDao.getCmTotleNum(cmc);
//                    }
//                }
                TopoFolderNum cmcTopoFolderNum = new TopoFolderNum();
                cmcTopoFolderNum.setColName("CMTS");
                cmcTopoFolderNum.setNum(ccmList.size());
                list.add(cmcTopoFolderNum);
                // TODO 暂时不统计CM的个数
//                TopoFolderNum cmTopoFolderNum = new TopoFolderNum();
//                cmTopoFolderNum.setColName("CM");
//                cmTopoFolderNum.setNum(cmNum);
//                list.add(cmTopoFolderNum);
            }
        }
        return re;
    }

    public List<String> getColNames() {
        List<String> re = new ArrayList<String>();
        re.add("CMTS");
        // TODO 暂时不统计CM的个数
//        re.add("CM");
        return re;
    }
}
