/***********************************************************************
 * $Id: CmServiceTypeServiceImpl.java,v1.0 2016年11月3日 上午10:29:14 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.cm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.cm.dao.CmServiceTypeDao;
import com.topvision.ems.cmc.cm.domain.CmFileNameChangeLog;
import com.topvision.ems.cmc.cm.domain.CmServiceType;
import com.topvision.ems.cmc.cm.service.CmServiceTypeService;
import com.topvision.ems.cmc.service.impl.CmcBaseCommonService;

/**
 * @author haojie
 * @created @2016年11月3日-上午10:29:14
 *
 */
@Service("cmServiceTypeService")
public class CmServiceTypeServiceImpl extends CmcBaseCommonService implements CmServiceTypeService {
    @Autowired
    private CmServiceTypeDao cmServiceTypeDao;

    @Override
    public List<CmServiceType> getCmServiceTypeList() {
        return cmServiceTypeDao.getCmServiceTypeList();
    }

    @Override
    public void addCmServiceType(CmServiceType cmServiceType) {
        cmServiceTypeDao.insertCmServiceType(cmServiceType);
    }

    @Override
    public CmServiceType getCmServiceTypeById(String fileName) {
        return cmServiceTypeDao.getCmServiceTypeById(fileName);
    }

    @Override
    public void modifyCmServiceType(CmServiceType cmServiceType) {
        cmServiceTypeDao.updateCmServiceType(cmServiceType);
    }

    @Override
    public void deleteCmServiceType(String fileName) {
        cmServiceTypeDao.deleteCmServiceType(fileName);
    }

    @Override
    public void addCmServiceTypes(List<CmServiceType> cmServiceTypes, Boolean deleteStatus) {
        if (deleteStatus) {
            cmServiceTypeDao.deleteCmServiceType();
        }
        cmServiceTypeDao.batchInsertOrUpdateCmServiceType(cmServiceTypes);
    }

    @Override
    public List<CmFileNameChangeLog> getlogs(Long cmId) {
        return cmServiceTypeDao.getlogs(cmId);
    }

}
