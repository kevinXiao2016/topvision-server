/***********************************************************************
 * $Id: CmcServiceImpl.java,v1.0 2011-10-25 下午04:30:31 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ccmts.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.ccmts.dao.CmcListDao;
import com.topvision.ems.cmc.ccmts.domain.EntityPonRelation;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.ccmts.service.CmcListService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.service.BaseService;

/**
 * 基本功能实现
 * 
 * @author loyal
 * @created @2011-10-25-下午04:30:31
 * 
 */
@Service("cmcListService")
public class CmcListServiceImpl extends BaseService implements CmcListService{
    @Autowired
    CmcListDao cmcListDao;
    @Autowired
    EntityTypeService entityTypeService;
    
    @Override
    public List<EntityPonRelation> getEntityPonInfoList() {
        return cmcListDao.getEntityPonInfoList();
    }
    
    @Override
    public List<CmcAttribute> queryCmc8800BList(Map<String, Object> cmcQueryMap) {
        return cmcListDao.queryCmc8800BList(cmcQueryMap);
    }
    
    @Override
    public Long queryCmc8800BNum(Map<String, Object> cmcQueryMap) {
        return cmcListDao.queryCmc8800BNum(cmcQueryMap);
    }
    
    @Override
    public List<CmcAttribute> queryCmcList(Map<String, Object> cmcQueryMap) {
        return cmcListDao.queryForCmcList(cmcQueryMap);
    }
    
    @Override
    public Long getCmcNum(Map<String, Object> cmcQueryMap) {
        return cmcListDao.getCmcNum(cmcQueryMap);
    }

    @Override
    public Map<Object, Object> getAllOnuIdToCmcNmnameMap(Long entityId) {
        Map<Object, Object> map = cmcListDao.getAllOnuIdToCmcNmnameMap(entityId);
        return map;
    }
}
