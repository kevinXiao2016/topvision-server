/***********************************************************************
 * $Id: OnuAuthService.java,v1.0 2013年10月25日 下午5:59:16 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onuauth.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.onuauth.domain.OltAuthentication;
import com.topvision.ems.epon.onuauth.domain.OltOnuBlockAuthen;
import com.topvision.ems.epon.onuauth.domain.OltPonOnuAuthModeTable;
import com.topvision.framework.service.Service;

/**
 * @author Bravin
 * @created @2013年10月25日-下午5:59:16
 *
 */
public interface OnuAuthService extends Service {
    /**
     * 设置ONU认证策略
     * 
     * @param entityId
     *            设备ID
     * @param policy
     *            ONU认证策略
     */
    void setOnuAuthPolicy(Long entityId, Integer policy);

    /**
     * 获取ONU认证列表(通过PON ID)
     * 
     * @param ponId
     *            PON口ID
     * @param sn
     *            ONU SN
     * @param mac
     *            ONU MAC
     * @return List<OltAuthentication>
     */
    List<OltAuthentication> getOnuAuthenPreConfigList(OltAuthentication oltAuthen);

    /**
     * 获取ONU认证列表(通过SLOT ID)
     * 
     * @param slotId
     *            SLOT口ID
     * @param sn
     *            ONU SN
     * @param mac
     *            ONU MAC
     * @return List<OltAuthentication>
     */
    List<OltAuthentication> getOnuAuthenListBySlot(OltAuthentication oltAuthen);

    /**
     * 获取ONU认证列表(通过ENTITY ID)
     * 
     * @param entity
     *            　Id 设备　ID
     * @return List<OltAuthentication>
     */
    List<OltAuthentication> getOnuAuthenListByEntity(Long entityId);

    /**
     * 获取ONU认证列表(通过MAC)
     * 
     * @param entityId
     *            设备ID
     * @param mac
     *            ONU MAC
     * @return List<OltAuthentication>
     */
    List<OltAuthentication> getOnuAuthenListByMac(Long entityId, String mac);

    /**
     * 获取ONU认证列表(通过SN)
     * 
     * @param entityId
     *            设备ID
     * @param sn
     *            ONU SN
     * @return List<OltAuthentication>
     */
    List<OltAuthentication> getOnuAuthenListBySn(Long entityId, String sn);

    /**
     * 获取PON口下的onuId列表
     * 
     * @param ponId
     *            ponId
     * @return List<Long>
     */
    List<Long> getOnuAuthIdList(Long ponId);

    /**
     * 获取PON口下的blockOnuId列表
     * 
     * @param ponId
     *            ponId
     * @return List<Long>
     */
    List<Long> getBlockOnuAuthIdList(Long ponId);

    /**
     * 添加ONU认证规则
     * 
     * @param oltAuthen
     *            认证规则
     */
    void addOnuAuthenPreConfig(OltAuthentication oltAuthen);

    /**
     * 删除ONU认证规则
     * 
     * @param entityId
     *            设备ID
     * @param onuIndex
     *            ONU索引
     * @param ponId
     *            PON ID
     * @param authDelType
     *            1为MAC,2为SN
     */
    void deleteAuthenPreConfig(Long entityId, Long ponId, Long onuIndex, Integer authDelType);

    /**
     * 获取ONU认证阻塞列表
     * 
     * @param ponId
     *            OLT PON口ID
     * @return List<OltOnuBlockAuthen>
     */
    List<OltOnuBlockAuthen> getOnuAuthenBlockList(Long entityId, Long slotId, Long ponId);

    /**
     * 从设备上刷新ONU认证阻塞列表
     * 
     * @param entityId
     *            设备 ID
     */
    void refreshOnuAuthenBlockList(Long entityId);

    /*
     * 刷新ONU认证表
     * 
     * @param entityId 设备ID
     */
    public void refreshOnuAuthInfo(Long entityId);

    /*
     * 删除ONU认证BLOCK表数据库中的一条记录
     * 
     * @param ponId PON口 ID
     * 
     * @param onuIndex ONU onuIndex
     * 
     */
    void deleteOnuAuthBlock(Long ponId, Long onuIndex);

    void deleteOnuAuthBlockByMac(Long entityId, Long ponIndex, String mac);

    /**
     * 获取ONU认证的MAC列表
     * 
     * @param entityId
     *            设备 ID
     * @return List<OltAuthentication>
     */
    List<OltAuthentication> getOnuAuthMacList(Long entityId);

    /**
     * 获取ONU认证的SN列表
     * 
     * @param entityId
     *            设备 ID
     * @return List<OltAuthentication>
     */
    List<OltAuthentication> getOnuAuthSnList(Long entityId);

    /**
     * 刷新设备的所有PON的ONU认证模式，并更新到数据库
     * 
     * @param entityId
     *            设备ID
     */
    void refreshOnuAuthMode(Long entityId);

    /**
     * 修改某个PON口的认证模式
     * 
     * @param entityId
     *            设备ID
     * @param ponIndex
     *            pon口index
     * @param mode
     *            认证模式
     */
    void modifyOnuAuthMode(Long entityId, Long ponIndex, Integer mode);

    /**
     * 获得设备所有的PON口的ONU认证模式
     * 
     * @param entityId
     *            设备ID
     * @return List<OltPonOnuAuthModeTable>
     */
    List<OltPonOnuAuthModeTable> getOnuAuthMode(Long entityId);

    /**
     * 获取ONU预配置类型
     * 
     * @param ponId
     * @return
     */
    List<OltOnuAttribute> getOnuPreTypeByPonId(Long ponId);

    /**
     * 设置ONU预配置类型
     * 
     * @param entityId
     * @param onuIndex
     * @param type
     */
    void modifyOnuPreType(Long entityId, Long onuIndex, String type);

    /**
     * 刷新设备的所有PON的ONU认证使能，并更新到数据库
     * 
     * @param entityId
     *            设备ID
     */
    void refreshOnuAuthEnable(Long entityId);

    void refreshOnuAuthPreType(Long entityId);

    /**
     * 修改某个PON口的认证模式
     * 
     * @param entityId
     *            设备ID
     * @param ponId
     *            pon口id
     * @param status
     *            认证状态
     */
    void modifyOnuAuthEnable(Long entityId, Long ponId, Integer status);

    /**
     * 获得设备所有的PON口的ONU认证模式
     * 
     * @param entityId
     *            设备ID
     * @return List<OltPonOnuAutoAuthModeTable>
     */
    List<OltPonOnuAuthModeTable> getOnuAuthEnable(Long entityId);

    /**
     * 获得设备指定PON口的ONU认证模式
     * 
     * @param entityId
     *            设备ID
     * @return
     */
    Integer getPonOnuAuthMode(Long entityId, Long ponIndex);

    /**
     * 替换某条认证规则中的mac，并将该ONU所涉及的所有表项中的所有MAC进行相应的修改：认证表oltauthentication、 属性表oltonuattribute。
     * 
     * @param entityId
     *            设备ID
     * @param onuIndex
     *            ONU INDEX
     * @param mac
     *            替换的MAC地址
     * @throws Exception
     */
    void onuAuthMacInstead(Long entityId, Long onuIndex, String mac) throws Exception;

    /**
     * 替换某条认证规则中的sn及密码，并将该ONU所涉及的所有表项中的所有SN进行相应的修改：认证表oltauthentication。
     * 
     * @param entityId
     *            设备ID
     * @param onuIndex
     *            ONU INDEX
     * @param sn
     *            sn
     * @param password
     *            密码
     * @throws Exception
     */
    void onuAuthSnInstead(Long entityId, Long onuIndex, String sn, String password) throws Exception;

    /**
     * 获得某个PON口的MAC认证拒绝列表
     * @param entityId
     * @param ponId
     * @return
     */
    List<OltAuthentication> loadRejectedMacList(Long entityId, Long ponId);

    /**
     * 通过onuIndex查询ONU认证信息
     * @param entityId
     * @param onuIndex
     * @return
     */
    OltAuthentication getOltAuthenticationByIndex(Long entityId, Long onuIndex);

    /**
     * 关系缓存
     * @return Map<String, String> getOnuLevelCache()
     */
    Map<String, Integer> getOnuLevelCache();

}
