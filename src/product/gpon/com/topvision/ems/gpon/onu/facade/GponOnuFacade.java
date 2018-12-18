package com.topvision.ems.gpon.onu.facade;

import java.util.List;

import com.topvision.ems.facade.Facade;
import com.topvision.ems.gpon.onu.domain.GponOnuCapability;
import com.topvision.ems.gpon.onu.domain.GponOnuInfoSoftware;
import com.topvision.ems.gpon.onu.domain.GponOnuIpHost;
import com.topvision.ems.gpon.onu.domain.GponOnuQualityInfo;
import com.topvision.ems.gpon.onu.domain.GponOnuUniPvid;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

@EngineFacade(serviceName = "GponOnuFacade", beanName = "gponOnuFacade")
public interface GponOnuFacade extends Facade {

    /**
     * GponOnuInfoSoftware Topo
     * 
     * @param snmpParam
     * @return
     */
    List<GponOnuInfoSoftware> getGponOnuSoftwares(SnmpParam snmpParam);

    /**
     * GponOnuInfoSoftware Topo
     * 
     * @param snmpParam
     * @return
     */
    GponOnuInfoSoftware getGponOnuSoftware(SnmpParam snmpParam, GponOnuInfoSoftware software);

    /**
     * GponOnuIpHost Topo
     * 
     * @param snmpParam
     * @return
     */
    List<GponOnuIpHost> getGponOnuIpHosts(SnmpParam snmpParam);

    List<GponOnuIpHost> getGponOnuIpHosts(SnmpParam snmpParam, GponOnuIpHost gponOnuIpHost);

    List<GponOnuCapability> getGponOnuCapabilitys(SnmpParam snmpParam);

    List<GponOnuUniPvid> getGponOnuUniPvidList(SnmpParam snmpParam);

    GponOnuUniPvid getGponOnuUniPvid(SnmpParam snmpParam, GponOnuUniPvid gponOnuUniPvid);

    void addGponOnuIpHost(SnmpParam snmpParam, GponOnuIpHost gponOnuIpHost);

    void modifyGponOnuIpHost(SnmpParam snmpParam, GponOnuIpHost gponOnuIpHost);

    void deleteGponOnuIpHost(SnmpParam snmpParam, GponOnuIpHost gponOnuIpHost);

    /**
     * ONU复位
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param onuIndex
     *            ONU索引
     */
    void resetOnu(SnmpParam snmpParam, Long onuIndex);

    /**
     * ONU激活
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param onuIndex
     *            ONU索引
     */
    void deActiveOnu(SnmpParam snmpParam, Long onuIndex, Integer active);

    /**
     * ONU管理状态
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param onuIndex
     *            ONU索引
     */
    void setOnuAdminStatus(SnmpParam snmpParam, Long onuIndex, Integer status);

    void setOnu15minEnable(SnmpParam snmpParam, Long onuIndex, Integer onu15minEnable);

    void setOnu24hEnable(SnmpParam snmpParam, Long onuIndex, Integer onu24hEnable);

    GponOnuQualityInfo refreshGponOnuQuality(SnmpParam snmpParam, GponOnuQualityInfo gponOnuQualityInfo);

}
