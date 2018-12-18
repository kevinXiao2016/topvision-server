/***********************************************************************
 * $Id: CmcService.java,v1.0 2011-10-25 下午04:29:45 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ccmts.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.cmc.ccmts.domain.Cmc;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcBfsxSnapInfo;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcDevSoftware;
import com.topvision.ems.cmc.downchannel.domain.CmcFpgaSpecification;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.domain.SubDeviceCount;
import com.topvision.framework.service.Service;
import com.topvision.framework.snmp.SnmpParam;

/**
 * 基本功能
 * 
 * @author loyal
 * @created @2011-10-25-下午04:29:45
 * 
 */
public interface CmcService extends Service {
    /**
     * 通过cmcId查询entityId
     * 
     * @param cmcId
     *            CMC ID
     * @return entityId entityId
     */
    Long getEntityIdByCmcId(Long cmcId);

    public SnmpParam getSnmpParamByCmcId(Long cmcId);

    public SnmpParam getSnmpParamByEntityId(Long entityId);

    public <T> T getCmcFacade(String ip, Class<T> clazz);

    /**
     * 获取cmc属性
     * 
     * @param cmcId
     *            CMC ID
     * @return cmc属性 CMC属性
     */
    CmcAttribute getCmcAttributeByCmcId(Long cmcId);

    /**
     * 获取CC版本，若为8800A则返回olt版本，若为8800B或8800D则返回dol版本
     * @param cmcId
     * @return
     */
    public String obtainCcmtsVersion(Long cmcId);

    /**
     * 通过entityId获取cmcId
     * 
     * @param entityId Long
     * @return Long
     */
    Long getCmcIdByEntityId(Long entityId);

    /**
     * 获取cmctype
     * 
     * @param cmcId Long
     * @return Integer
     */
    Integer getCmcTypeByCmcId(Long cmcId);

    /**
     * 获取cmcIndex
     * 
     * @param cmcId Long
     * @return Long
     */
    Long getCmcIndexByCmcId(Long cmcId);

    /**
     * 获取cmc设备清单
     * 
     * @param map Map<String, Object>
     * @return List<CmcAttribute>
     */
    List<CmcAttribute> getDeviceListItem(Map<String, Object> map);

    /**
     * 根据onuId获取cmcId
     * 
     * @param onuId Long
     * @return Long
     */
    Long getCmcIdByOnuId(Long onuId);

    /**
     * 获取未关联的CC
     * 
     * @return HashMap<Long, String>
     */
    Map<Long, String> getCmcNotRelated();

    /**
     * 通过ONUID查询该ONU下的所有CMC设备
     * 
     * @param onuId Long
     * @return List<Cmc>
     */
    List<Cmc> getCmcList(Long onuId);

    /**
     * 刷新指定的CC
     * 
     * @param entityId Long
     * @param cmcId Long
     * @param cmcType Long
     */
    void refreshCC(Long entityId, Long cmcId, Integer cmcType);

    /**
     * 获取product snmp参数
     * 
     * @param cmcId Long
     * @param cmcType Long
     */
    void discoveryCmcAgain(Long cmcId, Long cmcType);

    /**
     * 重启不带Agent的CMC
     * 
     * @param cmcId Long
     */
    void resetCmcWithoutAgent(Long cmcId);

    /**
     * 获取CMC升级进度
     * 
     * @param cmcId Long
     * @return Integer
     */
    Integer getCmcUpdateRecord(Long cmcId);

    /**
     * 升级CC
     * 
     * @param cmcDevSoftware CmcDevSoftware
     * @return CmcDevSoftware
     */
    CmcDevSoftware updateCmc(CmcDevSoftware cmcDevSoftware);

    /**
     * 获取CMTS升级结果
     * @param cmcId
     * @return
     */
    public Integer getCmcUpdateStatus(Long cmcId);

    /**
     * 将CMC关联到某个ONU
     * 
     * @param onuId Long
     * @param cmcId Long
     */
    void relateCmcToOnu(Long onuId, Long cmcId);

    /**
     * 解绑定指定CC
     * 
     * @param cmcId Long
     */
    void cmcNoMacBind(Long cmcId);

    /**
     * 获取onu id
     * 
     * @param cmcId Long
     * @return Long
     */
    Long getOnuIdByCmcId(Long cmcId);

    /**
     * 重命名CC
     * 
     * 只更新数据库中，网管赋予的设备别名
     * @param entityId Long
     * @param cmcId Long
     * @param ccName String
     */
    void renameCc(Long entityId, Long cmcId, String ccName);

    /**
     * 判断cmc是否在tupo图上
     * @param cmcId Long
     * @param folderId Long
     * @return boolean
     */
    boolean isCmcExistsInTopo(Long cmcId, Long folderId);

    /**
     * 从onu视图上移到topo图
     * @param cmcId Long
     * @param entity Entity
     * @return Long
     */
    Long moveToTopoFromOnuView(Long cmcId, Entity entity);

    /**
     * 获取Dol或8800B上信道利用率采集周期
     * 
     * @param entityId Long
     * @param cmcId Long
     * @return Integer
     */
    Integer getCmtsChannelUtilizationInterval(Long entityId, Long cmcId);

    /**
     * 设置Dol上信道利用率采集周期
     * 
     * @param entityId Long
     * @param peroid Long
     * @param snmpParam SnmpParam
     */
    void setCmtsChannelUtilizationInterval(Long entityId, Long peroid, SnmpParam snmpParam);

    /**
     * 获取当前olt设备下所有cmcId
     * @param entityId
     * @return
     */
    List<Long> getCmcIdsByEntityId(Long entityId);

    /**
     * 更改8800a设备名后对应修改onu表中onuName，保证一致性
     * @param onuId
     * @param onuName
     */
    void modifyOnuName(Long onuId, String onuName);

    /**
     * 通过olt entityId获取cmcId列表
     * 
     * @param entityId Long
     * @return List<Long>
     */
    List<Long> getCmcIdListByEntityId(Long entityId);

    /**
     * 通过cmcId和cc类型删除添加到拓扑图的设备
     * @param type cc类型
     */
    void deleteTopoEntityByEntityIdAndType(Long type);

    /**
     * 获取B型设备FPGA对功能的支持信息
     * @param cmcId
     * @return
     */
    CmcFpgaSpecification getFpgaSpecificationById(Long cmcId);

    /**
     * 获取CMC统计信息
     * @param entityId
     * @return
     */
    SubDeviceCount getSubCountInfo(Long entityId);

    /**
     * 刷新类A型设备
     * @param upperDeviceId
     * @param cmcId
     * @return
     */
    CmcBfsxSnapInfo refreshCC8800A(Long upperDeviceId, Long cmcId, Long cmcIndex);

    /**
     * 解析出CMTS的软件版本
     * 
     * @param cmc
     */
    void analyseSofewareVersion(CmcAttribute cmc);

    /**
     * 刷新CC的网络基本信息
     * @param cmcId
     */
    void refreshCmcSystemIpInfo(Long cmcId);

    /**
     * 更新entity表中cmc mac
     * @param onuId
     * @param topCcmtsSysMacAddr
     */
    void modifyCmcMac(Long onuId, String topCcmtsSysMacAddr);

    void updateCmcStatus(Long cmcId, Integer status);

    List<CmcAttribute> getContainOptDorCmcList();
}
