/***********************************************************************
 * $Id: OltControlFacade.java,v1.0 2013-10-25 上午10:42:05 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.olt.facade;

import java.util.List;

import com.topvision.ems.epon.config.domain.OltControlFileCommand;
import com.topvision.ems.epon.domain.CcmtsFftMonitorScalar;
import com.topvision.ems.epon.olt.domain.OltAttribute;
import com.topvision.ems.epon.olt.domain.OltClearCmOnTime;
import com.topvision.ems.epon.olt.domain.OltFileAttribute;
import com.topvision.ems.epon.olt.domain.OltMacAddressLearnTable;
import com.topvision.ems.epon.olt.domain.TopOnuGlobalCfgMgmt;
import com.topvision.ems.epon.olt.domain.TopSysFileDirEntry;
import com.topvision.ems.facade.Facade;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author flack
 * @created @2013-10-25-上午10:42:05
 * 
 */
@EngineFacade(serviceName = "OltFacade", beanName = "oltFacade")
public interface OltFacade extends Facade {

    /**
     * OLT设备属性修改
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param oltAttribute
     *            OLT属性
     * @return OltAttribute
     */
    @Deprecated
    OltAttribute modifyOltAttribute(SnmpParam snmpParam, OltAttribute oltAttribute);

    /**
     * OLT Trap参数设置
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param manageAddress
     *            管理地址
     * @param community
     *            共同体名
     */
    void setOltTrapConfig(SnmpParam snmpParam, String manageAddress, String community);

    /**
     * 修改OLT设备信息
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param oltAttribute
     *            OLT属性
     * @return OltAttribute
     */
    OltAttribute modifyOltBaseInfo(SnmpParam snmpParam, OltAttribute oltAttribute);

    /**
     * 修改OLT设备带内网管配置
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param oltAttribute
     *            设备属性
     * @return OltAttribute
     */
    OltAttribute modifyInBandConfig(SnmpParam snmpParam, OltAttribute oltAttribute);

    /**
     * 修改OLT设备带外网管配置
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param oltAttribute
     *            设备属性
     * @return OltAttribute
     */
    OltAttribute modifyOutBandConfig(SnmpParam snmpParam, OltAttribute oltAttribute);

    /**
     * 修改OLT设备SNMP配置
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param oltAttribute
     *            设备属性
     * @return OltAttribute
     */
    OltAttribute modifyOltSnmpConfig(SnmpParam snmpParam, OltAttribute oltAttribute);

    /**
     * OLT设备复位
     * 
     * @param snmpParam
     *            网管SNMP参数
     */
    void resetOlt(SnmpParam snmpParam);

    /**
     * 获取OLT系统时间
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @return Long
     */
    Long getOltSysTime(SnmpParam snmpParam);

    /**
     * OLT系统校时
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param sysTime
     *            OLT系统时间
     */
    void sysTiming(SnmpParam snmpParam, Long sysTime);

    /**
     * OLT主备倒换
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param slotIndex
     *            主控板索引
     */
    void switchoverOlt(SnmpParam snmpParam, Long slotIndex);

    /**
     * 备用主控板同步
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param syncAction
     *            同步类型（syncApp/syncConfig）
     */
    void syncSlaveBoard(SnmpParam snmpParam, Integer syncAction);

    /**
     * OLT恢复设备出厂配置
     * 
     * @param snmpParam
     *            网管SNMP参数
     */
    void restoreOlt(SnmpParam snmpParam);

    /**
     * 修改OLT设备信息
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param oltAttribute
     *            OLT属性
     * @return OltAttribute
     */
    @Deprecated
    OltAttribute modifyOltFacility(SnmpParam snmpParam, OltAttribute oltAttribute);

    /**
     * 获取OLT文件
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @return List<OltFile>
     */
    List<OltFileAttribute> getOltFilePath(SnmpParam snmpParam);

    /**
     * 获取OLT文件传输状态
     * 
     * @param snmpParam
     * @return
     */
    Integer getOltFileTransferStatus(SnmpParam snmpParam);

    /**
     * 删除OLT文件
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param oltFileAttribute
     *            OLT文件属性
     * @return String
     */
    void deleteOltFile(SnmpParam snmpParam, OltFileAttribute oltFileAttribute);

    /**
     * 下载/上传OLT文件
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param oltControlFileCommand
     *            OLT文件传输参数
     * 
     */
    void contorlOltFile(SnmpParam snmpParam, OltControlFileCommand oltControlFileCommand);

    String getValueByOid(SnmpParam snmpParam, String oid);

    /**
     * 
     * @param <T>
     *            表示此为泛型接口
     * @param t
     * @return
     */
    <T> T getDomainInfoLine(SnmpParam snmpParam, T t);

    /**
     * 
     * @param <T>
     * @param snmpParam
     * @param t
     * @return
     */
    <T> List<T> getDomainInfoList(SnmpParam snmpParam, Class<T> T);

    /**
     * 获得设备同步状态信息
     * 
     * @param snmpParam
     * @return
     */
    Integer getOltSyncSlaveBoardStatus(SnmpParam snmpParam);

    /**
     * 下发设备MacLearn表项同步命令
     * 
     * @param snmpParam
     */
    void execOltMacLearnSyncAction(SnmpParam snmpParam);

    /**
     * 从设备刷新Olt Mac Learn表项
     * 
     * @param snmpParam
     * @return
     */
    List<OltMacAddressLearnTable> refreshOltMacLearnTable(SnmpParam snmpParam);

    /**
     * 获得CMC的设备类型
     * 
     * @Add by Rod For C-A AND C-B
     * 
     * @param cmcIndex
     * @param snmpParam
     * @return
     */
    String getCmcEntityType(Long cmcIndex, SnmpParam snmpParam);

    /**
     * 修改CCMTS频谱全局开关
     * 
     * @param snmpParam
     * @param ccmtsFftMonitorScalar
     */
    void modifyCcmtsFftGbStatus(SnmpParam snmpParam, CcmtsFftMonitorScalar ccmtsFftMonitorScalar);

    /**
     * 刷新OLT文件系统路径
     * 
     * @param snmpParam
     */
    List<TopSysFileDirEntry> refreshFileDir(SnmpParam snmpParam);

    /**
     * 获取MTK ONU全局WLAN WAN CATV开关参数
     * 
     * @param snmpParam
     * @return
     */
    List<TopOnuGlobalCfgMgmt> getOnuGlobalCfgMgmt(SnmpParam snmpParam);

    /**
     * 修改MTK ONU全局WLAN WAN CATV开关参数
     * 
     * @param snmpParam
     * @param topOnuGlobalCfgMgmt
     */
    void modifyOnuGlobalCfgMgmt(SnmpParam snmpParam, TopOnuGlobalCfgMgmt topOnuGlobalCfgMgmt);

    /**
     * 获取OLT离线cm清除时间
     * 
     * @param
     * @return OltClearCmOnTime
     */
    OltClearCmOnTime getCmcClearTime(SnmpParam snmpParam);

    /**
     * 获取OLT频谱开关
     * 
     * @param snmpParam
     * @return
     */
    boolean getOltSwitchStatus(SnmpParam snmpParam);

    /**
     * 开启OLT频谱开关
     * 
     * @param snmpParam
     */
    void startSpectrumSwitchOlt(SnmpParam snmpParam);

}
