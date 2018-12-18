/***********************************************************************
 * $Id: UniVlanProfileService.java,v1.0 2013-11-28 上午10:09:58 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.univlanprofile.service;

import java.util.List;

import com.topvision.ems.epon.univlanprofile.domain.UniVlanBindTable;
import com.topvision.ems.epon.univlanprofile.domain.UniVlanProfileTable;
import com.topvision.ems.epon.univlanprofile.domain.UniVlanRuleTable;
import com.topvision.framework.service.Service;

/**
 * @author flack
 * @created @2013-11-28-上午10:09:58
 *
 */
public interface UniVlanProfileService extends Service {
    /**
     * 获取UNI VLAN模板列表
     * @param entityId
     * @return
     */
    List<UniVlanProfileTable> getProfileList(Long entityId);

    /**
     * 获取指定的UniVlanProfileTable信息
     * @param entityId
     * @param profileId
     * @return
     */
    UniVlanProfileTable getUniVlanProfile(Long entityId, Integer profileId);

    /**
     * 添加UNI VLAN模板
     * @param profile
     */
    void addProfile(UniVlanProfileTable profile);

    /**
     * 更新UNI VLAN 模板
     * @param profile
     */
    void updateProfile(UniVlanProfileTable profile, boolean modeChange);

    /**
     * 删除UNI　VLAN模板
     * @param profile
     */
    void deleteProfile(UniVlanProfileTable profile);

    /**
     * 刷新UNI　VlAN模板数据
     * @param entityId
     */
    void refreshProfileData(Long entityId);

    /**
     * 从设备刷新UNI VLAN 模板相关数据
     * @param entityId
     */
    void refreshProfileAndRule(Long entityId);

    /**
     * 获取规则列表
     * @param entityId
     * @param profileId
     * @return
     */
    List<UniVlanRuleTable> getRuleList(Long entityId, Integer profileId);

    /**
     * 添加规则
     * @param rule
     */
    void addRule(UniVlanRuleTable rule);

    /**
     * 删除规则
     * @param rule
     */
    void deleteRule(UniVlanRuleTable rule);

    /**
     * 刷新规则数据
     * @param entityId
     */
    void refreshRuleData(Long entityId);

    /**
     * 刷新OLT下所有UNI端口vlan配置
     * @param entityId
     */
    void refreshUniVlanData(Long entityId);

    /**
     * 刷新单个ONU UNI端口vlan配置
     * @param entityId
     */
    void refreshUniVlanData(Long entityId, Long onuIndex);

    /**
     * 更新模板绑定
     * @param bindList
     * @param profile
     */
    void updateProfileBind(List<UniVlanBindTable> bindList, UniVlanProfileTable profile);

    /**
     * 更新模板解绑定
     * @param bindList
     */
    void updateUnBind(List<UniVlanBindTable> bindList);

    /**
     * 获取模板绑定的UNI端口列表
     * @param profileIndex
     * @return
     */
    List<Long> getProfileBindList(Integer profileIndex, Long entityId);

    /**
     * 获取uni端口的模板绑定信息
     * @param uniId
     * @return
     */
    UniVlanBindTable getUniBindInfo(Long uniId, Long entityId);

    /**
     * 更新uni口的pvid
     * @param bindTable
     */
    void updateUniPvid(UniVlanBindTable bindTable);

    /**
     * 刷指定UNI口的vlan信息
     * @param entityId
     * @param uniIndex
     */
    void refreshUniBindInfo(Long entityId, Long uniIndex);

    /**
     * 查询指定设备的模板绑定记录
     * @param entityId
     * @return
     */
    List<UniVlanBindTable> getEntityBindList(Long entityId);

    /**
     * 替换uni口的模板绑定
     * @param uniBindInfo
     */
    void replaceBindProfile(UniVlanBindTable uniBindInfo);

    /**
    * 解除UNI口模板的绑定
    * @param entityId
    * @param uniIndex
    */
    void unBindUniProfile(UniVlanBindTable bindTable);

    /**
     * 获取uni端口的绑定VLAN信息
     * @param entityId
     * @param uniIndex
     * @return
     */
    UniVlanBindTable getUniVlanBindInfo(Long entityId, Long uniIndex);

    /**
     * 从设备获取UNI端口VLAN信息
     * 为确保业务一致，会先获取模板和规则数据
     * @param entityId
     * @param uniIndex
     */
    void refreshUniVlanInfo(Long entityId, Long uniIndex);

}
