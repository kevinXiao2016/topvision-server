/***********************************************************************
 * $Id: CmcAclDao.java,v1.0 2013-5-3 下午01:25:02 $
 * 
 * @author: lzs
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.acl.dao;

import java.util.List;

import com.topvision.ems.cmc.acl.facade.domain.CmcAclDefAction;
import com.topvision.ems.cmc.acl.facade.domain.CmcAclInfo;
import com.topvision.ems.cmc.domain.CmcEntity;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author lzs
 * @created @2013-5-3-下午01:25:02
 *
 */
public interface CmcAclDao extends BaseEntityDao<CmcEntity> {
  
    /**
     * 删除所有放置点默认动作，再插入新的
     * @param entityId
     * @param aclDefActList
     */
    void refreshAllPositionDefAct(Long entityId, List<CmcAclDefAction> aclDefActList);
    /**
     * 修改一个放置点的默认动作
     * @param entityId
     * @param defAct
     */
    void modifyOnePositionDefAct(Long entityId,CmcAclDefAction defAct);
    /**
     * 查询一个放置点的默认动作
     * @param entityId
     * @param position
     * @return
     */
    CmcAclDefAction getOnePositionDefAct(Long entityId,Integer position);
    
    
    
    /**
     * 删除所有ACL，插入新的
     * @param entityId
     * @param allAclList
     */
    void refreshAllAclList(Long entityId, List<CmcAclInfo> allAclList);
    /**
     * 更新一个ACL，实现方式：删除一个，插入新的
     * @param entityId
     * @param aclInfo
     */
    void refreshOneAclInfo(Long entityId,CmcAclInfo aclInfo);
    /**
     * 删除一个ACL
     * @param entityId
     * @param aclId
     */
    void deleteOneAclInfo(Long entityId,Integer aclId);
    /**
     * 查询一个放置点所有的ACL列表
     * @param entityId
     * @param position
     * @return
     */
    List<CmcAclInfo> getOnePositionAclList(Long entityId,Integer position);
    /**
     * 查询一个ACL
     * @param entityId
     * @param aclId
     * @return
     */
    CmcAclInfo getOneAclInfo(Long entityId,Integer aclId);
    
    /**
     * 查询所有ACL列表
     * @param entityId
     * @return
     */
    List<CmcAclInfo> getAllAclList(Long entityId);
}
