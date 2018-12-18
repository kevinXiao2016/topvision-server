/***********************************************************************
 * $Id: OnuAuthDao.java,v1.0 2013年10月25日 下午6:04:59 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onuauth.dao;

import java.util.List;

import com.topvision.ems.epon.onu.domain.OltTopOnuProductTable;
import com.topvision.ems.epon.onuauth.domain.OltAuthentication;
import com.topvision.ems.epon.onuauth.domain.OltOnuBlockAuthen;
import com.topvision.ems.epon.onuauth.domain.OltOnuBlockExtAuthen;
import com.topvision.ems.epon.onuauth.domain.OltPonOnuAuthModeTable;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author Bravin
 * @created @2013年10月25日-下午6:04:59
 *
 */
public interface OnuAuthDao extends BaseEntityDao<Object> {
    /**
     * 更新ONU认证策略
     * 
     * @param entityId
     *            设备ID
     * @param policy
     *            ONU认证策略
     */
    void updateOnuAuthPolicy(Long entityId, Integer policy);

    /**
     * 获取认证onuId列表
     * 
     * @param ponId
     *            PONID
     * @return List<Long>
     */
    List<Long> getOnuAuthIdList(Long ponId);

    /**
     * 获取block onuId列表
     * 
     * @param ponId
     *            PONID
     * @return List<Long>
     */
    List<Long> getBlockOnuAuthIdList(Long ponId);

    /**
     * 获取ONU认证列表(通过PON ID)
     * 
     * @param ponId
     *            PON口ID
     * @return List<OltAuthentication>
     */
    List<OltAuthentication> getOnuAuthenPreConfigList(OltAuthentication oltAuthen);

    /**
     * 获取ONU认证列表(通过 SLOT ID)
     * 
     * @param slotId
     *            SLOT ID
     * @return List<OltAuthentication>
     */
    List<OltAuthentication> getOnuAuthenListBySlot(OltAuthentication oltAuthen);

    /**
     * 获取ONU认证列表(通过 entityId)
     * 
     * @param entityId
     *            设备 ID
     * @return List<OltAuthentication>
     */
    List<OltAuthentication> getOnuAuthenListByEntity(Long entityId);

    /**
     * 修改ONU认证规则
     * 
     * @param oltAuthen
     *            认证规则
     */
    void updateOnuAuthenPreConfig(OltAuthentication oltAuthen);

    /**
     * 插入ONU认证规则
     * 
     * @param oltAuthen
     *            认证规则
     */
    void insertOnuAuthenPreConfig(OltAuthentication oltAuthen);

    /**
     * 删除ONU认证规则
     * 
     * @param ponId
     *            PON ID
     * @param onuIndex
     *            ONU 索引号
     */
    void deleteAuthenPreConfig(Long ponId, Long onuIndex);

    /**
     * 删除PON口下ONU认证规则
     * 
     * @param ponId
     *            PON ID
     */
    void deleteAuthenPreConfigByPonId(Long ponId);

    /**
     * 获取ONU认证阻塞列表
     * 
     * @param ponId
     *            OLT PON口ID
     * @return List<OltOnuBlockAuthen>
     */
    List<OltOnuBlockAuthen> getOnuAuthenBlockList(Long ponId);

    /**
     * 获取ONU认证阻塞列表
     * 
     * @param slotId
     *            OLT 板卡ID
     * @return List<OltOnuBlockAuthen>
     */
    List<OltOnuBlockAuthen> getOnuAuthenBlockListBySlot(Long slotId);

    /**
     * 获取ONU认证阻塞列表
     * 
     * @param entityId
     *            OLT 设备ID
     * @return List<OltOnuBlockAuthen>
     */
    List<OltOnuBlockAuthen> getOnuAuthenBlockListByOlt(Long entityId);

    /**
     * 更新ONU认证阻塞列表
     * 
     * @param List
     *            <OltOnuBlockAuthen> ONU认证阻塞列表
     * @param Long
     *            entityId 设备 ID
     */
    void refreshOnuAuthenBlockList(Long entityId, List<OltOnuBlockAuthen> list);

    /*
     * 删除ONU认证BLOCK表数据库中的一条记录
     * 
     * @param ponId PON口 ID
     * 
     * @param onuIndex ONU onuIndex
     * 
     */
    void deleteOnuAuthBlock(Long ponId, Long onuIndex);

    void deleteOnuAuthBlockByMac(Long ponId, String mac);

    /**
     * 获取ONU认证MAC列表
     * 
     * @param entityId
     *            设备 ID
     * @return List<OltAuthentication>
     */
    List<OltAuthentication> getOnuAuthMacList(Long entityId);

    /**
     * 获取ONU认证SN列表
     * 
     * @param entityId
     *            设备 ID
     * @return List<OltAuthentication>
     */
    List<OltAuthentication> getOnuAuthSnList(Long entityId);

    /**
     * 刷新Block扩展表
     * 
     * @param onuBlockExtAuthenList
     */
    void refreshOnuAuthenExtBlockList(List<OltOnuBlockExtAuthen> onuBlockExtAuthenList);

    /**
     * 刷新pon口认证模式表
     * 
     * @param entityId
     * @param oltPonOnuAuthModeTables
     */
    void refreshOnuAuthMode(Long entityId, List<OltPonOnuAuthModeTable> oltPonOnuAuthModeTables);

    /**
     * 更新某一个pon口的认证模式
     * 
     * @param oltPonOnuAuthModeTable
     */
    void updateOnuAuthMode(OltPonOnuAuthModeTable oltPonOnuAuthModeTable);

    /**
     * 获取所有pon口的认证模式
     * 
     * @param entityId
     * @return
     */
    List<OltPonOnuAuthModeTable> getOnuAuthMode(Long entityId);
    
    /**
     * 获取指定pon口的认证模式
     * 
     * @param entityId
     * @return
     */
    Integer getPonOnuAuthMode(Long entityId, Long ponIndex);

    /**
     * 刷新pon口认证使能表
     * 
     * @param entityId
     * @param oltPonOnuAutoAuthModeTables
     */
    void refreshOnuAuthEnable(Long entityId, List<OltPonOnuAuthModeTable> oltPonOnuAuthModeTable);

    /**
     * 更新某一个pon口的认证使能
     * 
     * @param oltPonOnuAutoAuthModeTable
     */
    void updateOnuAuthEnable(OltPonOnuAuthModeTable oltPonOnuAuthModeTable);

    /**
     * 获取所有pon口的认证使能状态列表
     * 
     * @param entityId
     * @return
     */
    List<OltPonOnuAuthModeTable> getOnuAuthEnable(Long entityId);

    void updateOltAuthentication(OltAuthentication oltAuthentication);

    void updateOnuPreType(Long entityId, Long onuIndex, String type);

    void updateAllOnuPreType(final List<OltTopOnuProductTable> onuPre);

    /**
     * 批量插入以及更新ONU阻塞表
     * 
     * @param list
     *            ONU阻塞表
     */
    void batchInsertOltOnuBlockAuth(final List<OltOnuBlockAuthen> list);

    /**
     * 批量插入以及更新ONU认证表
     * 
     * @param list
     *            ONU认证表
     */
    void batchInsertOltOnuAuthInfo(final List<OltAuthentication> list);

    /**
     * 删除所有的ONU认证表
     * 
     * @param entityId
     *            设备索引
     */
    void deleteAllOnuAuth(Long entityId);

    /**
     * 插入更新ONU认证规则
     * 
     * @param oltAuthentication
     *            ONU认证规则
     */
    void insertOrUpdateOltAuthentication(List<OltAuthentication> oltAuthentications, Long entityId);

    /**
     * 删除某一个pon口的认证允许的规则
     * 
     * @param entityId
     * @param ponId
     */
    void deletePonAuthRule(Long entityId, Long ponId);

    /**
     *  获得某个PON口的MAC认证拒绝列表
     * @param entityId
     * @param ponId
     * @return
     */
    List<OltAuthentication> loadRejectedMacList(Long entityId, Long ponId);

    /**
     *  通过onuIndex查询ONU认证信息
     * @param entityId
     * @param onuIndex
     * @return
     */
    OltAuthentication selectOltAuthenticationByIndex(Long entityId, Long onuIndex);
}
