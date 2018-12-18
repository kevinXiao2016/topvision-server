/***********************************************************************
 * $Id: UniVlanProfileDao.java,v1.0 2013-11-28 上午10:11:53 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.univlanprofile.dao;

import java.util.List;

import com.topvision.ems.epon.univlanprofile.domain.UniVlanBindTable;
import com.topvision.ems.epon.univlanprofile.domain.UniVlanProfileTable;
import com.topvision.ems.epon.univlanprofile.domain.UniVlanRuleTable;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author flack
 * @created @2013-11-28-上午10:11:53
 *
 */
public interface UniVlanProfileDao extends BaseEntityDao<Object> {

    /**
     * 批量插入UNI VLAN 模板
     * 
     * @param uniVlanProfiles
     * @param entityId
     */
    void batchInsertUniVlanProfiles(List<UniVlanProfileTable> uniVlanProfiles, Long entityId);

    /**
     * 获得设备UNI VLAN 模板列表
     * 
     * @param entityId
     */
    List<UniVlanProfileTable> getUniVlanProfiles(Long entityId);

    /**
     * 获得设备UNI VLAN 模板
     * 
     * @param entityId
     * @param profileId
     */
    UniVlanProfileTable getUniVlanProfileById(Long entityId, Integer profileId);

    /**
     * 添加UNI　VLAN 模板
     * @param uniVlanProfile
     */
    void insertUniVlanProfile(UniVlanProfileTable uniVlanProfile);

    /**
     * 更新UNI VLAN 模板
     * 
     * @param uniVlanProfile
     */
    void updateUniVlanProfile(UniVlanProfileTable uniVlanProfile);

    /**
     * 更新模板引用次数
     * @param uniVlanProfile
     */
    void updateProfileRefCnt(UniVlanProfileTable uniVlanProfile);

    /**
     * 删除UNI VLAN 模板
     * 
     * @param uniVlanProfile
     */
    void deleteUniVlanProfile(UniVlanProfileTable uniVlanProfile);

    /**
     * 批量插入UNI VLAN 模板规则
     * 
     * @param uniVlanRules
     * @param entityId
     */
    void batchInsertUniVlanRules(List<UniVlanRuleTable> uniVlanRules, Long entityId);

    /**
     * 获得模板UNI VLAN 模板规则列表
     * 
     * @param entityId
     * @param profileId
     */
    List<UniVlanRuleTable> getProfileVlanRules(Long entityId, Integer profileId);

    /**
     * 获得UNI VLAN 模板规则
     * 
     * @param uniVlanRule
     */
    UniVlanRuleTable getUniVlanRuleById(UniVlanRuleTable uniVlanRule);

    /**
     * 插入UNI VLAN 模板规则
     * 
     * @param uniVlanRule
     */
    void insertUniVlanRule(UniVlanRuleTable uniVlanRule);

    /**
     * 更新UNI VLAN 模板规则
     * 
     * @param uniVlanRule
     */
    void updateUniVlanRule(UniVlanRuleTable uniVlanRule);

    /**
     * 删除UNI VLAN 模板规则
     * 
     * @param uniVlanRule
     */
    void deleteUniVlanRule(UniVlanRuleTable uniVlanRule);

    /**
     * 删除与模板关联和规则
     * @param entityId
     * @param profileId
     */
    void deleteProfileRelRules(Long entityId, Integer profileId);

    /**
     * 批量插入UNI VLAN 绑定规则
     * 
     * @param uniVlanBinds
     * @param entityId
     */
    void batchInsertUniVlanBind(List<UniVlanBindTable> uniVlanBinds, Long entityId);

    /**
     * 获得UNI VLAN 绑定规则
     * 
     * @param uniVlanBind
     */
    UniVlanBindTable getUniVlanBindById(UniVlanBindTable uniVlanBind);

    /**
     * 更新UNI VLAN 绑定规则
     * 
     * @param uniVlanBind
     */
    void updateUniVlanBind(UniVlanBindTable uniVlanBind);

    /**
     * 批量更新UNI VLAN模板绑定规则
     * @param bindList
     */
    void batchUpdateVlanBind(List<UniVlanBindTable> bindList);

    /**
     * 查询模板所绑定的UNI端口列表
     * @param profileIndex
     * @return
     */
    List<Long> queryProfileBindList(Integer profileIndex, Long entityId);

    /**
     * 查询uni口绑定模板信息
     * @param uniId
     * @return
     */
    UniVlanBindTable queryUniBindInfo(UniVlanBindTable bindTable);

    /**
     * 更新uni口pvid
     * @param bindTable
     */
    void updateUniPvid(UniVlanBindTable bindTable);

    /**
     * 更新uni口vlan绑定信息
     * @param bindTable
     */
    void updateUniBindInfo(UniVlanBindTable bindTable);

    /**
     * 查询当前设备模板绑定记录
     * @param entityId
     * @return
     */
    List<UniVlanBindTable> queryEntityBindList(Long entityId);

    /**
     * 更新数据库的模板绑定
     * @param table
     */
    void updateVlanBind(UniVlanBindTable table);
}
