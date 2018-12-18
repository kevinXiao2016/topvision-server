package com.topvision.ems.cmc.acl.service;

import java.util.List;

import com.topvision.ems.cmc.acl.facade.domain.CmcAclDefAction;
import com.topvision.ems.cmc.acl.facade.domain.CmcAclInfo;

/**
 * CC 设备对ACL的功能管理
 * 目前只有CC8800B支持ACL功能
 * @author lzs
 * @created @2013-4-20-下午04:01:41
 *
 */
public interface CmcAclService {

    /**
     * 从数据库中查询一个ACL的信息
     * @param aclID
     * @return
     */
    public CmcAclInfo getSingleAclInfo(Long cmcId,Integer aclID);

    /**
     * 创建一个新的ACL
     * @param aclID
     * @return
     */
    public CmcAclInfo addSingleAclInfo(Long cmcId,CmcAclInfo aclInfo);

    /**
     * 修改一个已有的ACL，当ACL已经安装到某个放置点则不允许修改
     * @param aclInfo
     * @return
     */
    public CmcAclInfo modifySingleAclInfo(Long cmcId,CmcAclInfo aclInfo);

    /**
     * 删除一个已有的ACL，当ACL已经安装到某个放置点则不允许删除
     * @param aclID
     * @return
     */
    public boolean deleteSingleAclInfo(Long cmcId,Integer aclID);

    /**
     * 查询一台设备一个放置点的默认规则
     * 1: uplinkIngress(1) 
     * 2: uplinkEgress(2) 
     * 3: cableEgress(3) 
     * 4: cableIngress(4) 
     * @return
     */
    public CmcAclDefAction getAclPositionDefInfo(Long cmcId,Integer posion);
    



    /**
     * 设置一个CC设备一个放置点的默认动作
     * @param cmcID
     * @param positionID
     * @param isEnable ture 允许通过，false，丢弃报文
     * @return
     */
    public boolean enablePositionDefAct(Long cmcID,Integer positionID,boolean isEnable);
    
    /**
     * 从设备刷新一台CC的所有ACL数据并更新数据库
     * @param cmcId
     * @return
     */
    public boolean refreshAllAclInfo(Long cmcId);
    
    /**
     * 从数据库中查询一个放置点的所有ACL
     * @param cmcId
     * @param position
     * @return
     */
    public List<CmcAclInfo> getOnePositionAclList(Long cmcId,Integer position);
    /**
     * 创建ACL的时候，给用户提供一个推荐的ID，来源：从数据库中查询所有已有ACL ID，
     * 遍历取空值或者最大值，如果没有合法ID，则返回null
     * @param cmcId
     * @return
     */
    public Integer getOneNewAclId(Long cmcId);

}
