/***********************************************************************
 * $Id: OltControlFacadeImplTest.java,v1.0 2011-10-10 下午04:55:42 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.engine.executor;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.Test;

import com.topvision.ems.epon.olt.domain.OltAttribute;
import com.topvision.ems.epon.olt.domain.OltFileAttribute;
import com.topvision.ems.epon.olt.domain.OltPonStormSuppressionEntry;
import com.topvision.ems.epon.olt.facade.OltFacade;
import com.topvision.ems.epon.olt.facade.OltPonFacade;
import com.topvision.ems.epon.olt.facade.OltSlotFacade;
import com.topvision.ems.epon.olt.facade.OltSniFacade;
import com.topvision.ems.epon.onu.domain.OltOnuCatv;
import com.topvision.ems.epon.onu.domain.OltOnuRstp;
import com.topvision.ems.epon.onu.domain.OltUniPortRateLimit;
import com.topvision.ems.epon.onu.domain.OltUniStormSuppressionEntry;
import com.topvision.ems.epon.onu.facade.OnuFacade;
import com.topvision.ems.epon.onu.facade.UniFacade;
import com.topvision.ems.epon.onuauth.domain.OltAuthenMacInfo;
import com.topvision.ems.epon.onuauth.domain.OltAuthenSnInfo;
import com.topvision.ems.epon.onuauth.domain.OltOnuBlockAuthen;
import com.topvision.ems.epon.onuauth.facade.OnuAuthFacade;
import com.topvision.ems.epon.trunk.domain.OltSniTrunkConfig;
import com.topvision.ems.epon.trunk.facade.OltTrunkFacade;
import com.topvision.framework.EnvironmentConstants;
import com.topvision.framework.snmp.RowStatus;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.utils.EponIndex;
import com.topvision.platform.domain.EngineServer;
import com.topvision.platform.facade.DubboFacadeFactory;
import com.topvision.platform.facade.FacadeFactory;

/**
 * @author Victor
 * @created @2011-10-10-下午04:55:42
 */
public class OltControlFacadeImplTest {
    private String ip = "172.10.10.11";
    private String roCommunity = "public";
    private String rwCommunity = "private";
    private String mib = "RFC1213-MIB,NSCRTV-EPONEOC-EPON-MIB,SUMA-EPONEOC-EPON-MIB";

    private OltFacade getFacade() {
        FacadeFactory facadeFactory = new DubboFacadeFactory();
        EngineServer engine = new EngineServer();
        engine.setIp("localhost");
        engine.setPort(3004);
        return facadeFactory.getFacade(engine, OltFacade.class);
    }

    private OltTrunkFacade getTrunkFacade() {
        FacadeFactory facadeFactory = new DubboFacadeFactory();
        EngineServer engine = new EngineServer();
        engine.setIp("localhost");
        engine.setPort(3004);
        return facadeFactory.getFacade(engine, OltTrunkFacade.class);
    }

    private OltSlotFacade getSlotFacade() {
        FacadeFactory facadeFactory = new DubboFacadeFactory();
        EngineServer engine = new EngineServer();
        engine.setIp("localhost");
        engine.setPort(3004);
        return facadeFactory.getFacade(engine, OltSlotFacade.class);
    }

    private OltSniFacade getSniFacade() {
        FacadeFactory facadeFactory = new DubboFacadeFactory();
        EngineServer engine = new EngineServer();
        engine.setIp("localhost");
        engine.setPort(3004);
        return facadeFactory.getFacade(engine, OltSniFacade.class);
    }

    private OltPonFacade getPonFacade() {
        FacadeFactory facadeFactory = new DubboFacadeFactory();
        EngineServer engine = new EngineServer();
        engine.setIp("localhost");
        engine.setPort(3004);
        return facadeFactory.getFacade(engine, OltPonFacade.class);
    }

    private OnuFacade getOnuFacade() {
        FacadeFactory facadeFactory = new DubboFacadeFactory();
        EngineServer engine = new EngineServer();
        engine.setIp("localhost");
        engine.setPort(3004);
        return facadeFactory.getFacade(engine, OnuFacade.class);
    }

    private UniFacade getUniFacade() {
        FacadeFactory facadeFactory = new DubboFacadeFactory();
        EngineServer engine = new EngineServer();
        engine.setIp("localhost");
        engine.setPort(3004);
        return facadeFactory.getFacade(engine, UniFacade.class);
    }

    private OnuAuthFacade getOnuAuthFacade() {
        FacadeFactory facadeFactory = new DubboFacadeFactory();
        EngineServer engine = new EngineServer();
        engine.setIp("localhost");
        engine.setPort(3004);
        return facadeFactory.getFacade(engine, OnuAuthFacade.class);
    }

    private SnmpParam getSnmpParam() {
        EnvironmentConstants.putEnv(EnvironmentConstants.MIB_HOME, "webapp/WEB-INF/mibs");
        SnmpParam param = new SnmpParam();
        param.setIpAddress(ip);
        param.setCommunity(roCommunity);
        param.setWriteCommunity(rwCommunity);
        param.setMibs(mib);
        return param;
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltControlFacadeImpl#modifyOltAttribute(com.topvision.framework.snmp.SnmpParam, com.topvision.ems.epon.facade.domain.OltAttribute)}
     * .
     */
    @Test
    public void testModifyOltAttribute() {
        OltAttribute oltAttribute = new OltAttribute();
        oltAttribute.setOltName("8602");
        getFacade().modifyOltAttribute(getSnmpParam(), oltAttribute);
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltControlFacadeImpl#setOltTrapConfig(com.topvision.framework.snmp.SnmpParam, java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public void testSetOltTrapConfig() {
        // TODO 无法行创建
        getFacade().setOltTrapConfig(getSnmpParam(), "172.10.10.253", "public");
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltControlFacadeImpl#modifyOltBaseInfo(com.topvision.framework.snmp.SnmpParam, com.topvision.ems.epon.facade.domain.OltAttribute)}
     * .
     */
    @Test
    public void testModifyOltBaseInfo() {
        OltAttribute oltAttribute = new OltAttribute();
        oltAttribute.setOltName(oltAttribute.getOltName());
        oltAttribute.setSysContact("jay");
        oltAttribute.setSysLocation("topvision");
        oltAttribute.setTopSysOltRackNum(3);
        oltAttribute.setTopSysOltFrameNum(20);
        getFacade().modifyOltAttribute(getSnmpParam(), oltAttribute);
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltControlFacadeImpl#modifyInBandConfig(com.topvision.framework.snmp.SnmpParam, com.topvision.ems.epon.facade.domain.OltAttribute)}
     * .
     */
    @Test
    public void testModifyInBandConfig() {
        OltAttribute inbandInfo = new OltAttribute();
        inbandInfo.setInbandIp("192.168.10.11");
        inbandInfo.setInbandMask("255.255.255.0");
        inbandInfo.setInbandGateway("192.168.10.1");
        inbandInfo.setTopSysInBandMaxBw(600);
        getFacade().modifyOltAttribute(getSnmpParam(), inbandInfo);
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltControlFacadeImpl#modifyOutBandConfig(com.topvision.framework.snmp.SnmpParam, com.topvision.ems.epon.facade.domain.OltAttribute)}
     * .
     */
    @Test
    public void testModifyOutBandConfig() {
        OltAttribute outbandInfo = new OltAttribute();
        outbandInfo.setOutbandIp("172.10.10.11");
        outbandInfo.setOutbandMask("255.255.255.0");
        outbandInfo.setOutbandGateway("172.12.255.0");
        getFacade().modifyOltAttribute(getSnmpParam(), outbandInfo);
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltControlFacadeImpl#modifyOltSnmpConfig(com.topvision.framework.snmp.SnmpParam, com.topvision.ems.epon.facade.domain.OltAttribute)}
     * .
     */
    @Test
    public void testModifyOltSnmpConfig() {
        // TODO 该方法不要测试 测试该方法会导致整个设备snmp无法访问
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltControlFacadeImpl#resetOlt(com.topvision.framework.snmp.SnmpParam)}
     * .
     */
    @Test
    public void testResetOlt() {
        getFacade().resetOlt(getSnmpParam());
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltControlFacadeImpl#getOltSysTime(com.topvision.framework.snmp.SnmpParam)}
     * .
     */
    @Test
    public void testGetOltSysTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Long time = getFacade().getOltSysTime(getSnmpParam());
        System.out.println("sdf.format(new Date(time)) = " + sdf.format(new Date(time)));
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltControlFacadeImpl#sysTiming(com.topvision.framework.snmp.SnmpParam, java.lang.Long)}
     * .
     */
    @Test
    public void testSysTiming() {
        testGetOltSysTime();
        getFacade().sysTiming(getSnmpParam(), System.currentTimeMillis() - 60 * 60 * 1000L);
        testGetOltSysTime();
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltControlFacadeImpl#switchoverOlt(com.topvision.framework.snmp.SnmpParam, java.lang.Long)}
     * .
     */
    @Test
    public void testSwitchoverOlt() {
        // 保证9、10号槽位已经插入，并且10号槽位是主用主控，该测试可以进行主控卡的主备切换 TODO snmpanget有问题
        getFacade().switchoverOlt(getSnmpParam(), new EponIndex(9).getSlotIndex());
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltControlFacadeImpl#restoreOlt(com.topvision.framework.snmp.SnmpParam)}
     * .
     */
    @Test
    public void testRestoreOlt() {
        getFacade().restoreOlt(getSnmpParam());
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltControlFacadeImpl#saveOltConfig(com.topvision.framework.snmp.SnmpParam)}
     * .
     */
    @Test
    public void testSaveOltConfig() {
        // getFacade().saveOltConfig(getSnmpParam());
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltControlFacadeImpl#modifyOltFacility(com.topvision.framework.snmp.SnmpParam, com.topvision.ems.epon.facade.domain.OltAttribute)}
     * .
     */
    @Test
    public void testModifyOltFacility() {
        // TODO 好像没用
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltControlFacadeImpl#setSlotPreConfigType(com.topvision.framework.snmp.SnmpParam, java.lang.Long, java.lang.Integer)}
     * .
     */
    @Test
    public void testSetSlotPreConfigType() {
        // 将5号槽为设置为epua
        getSlotFacade().setSlotPreConfigType(getSnmpParam(), new EponIndex(5).getSlotIndex(), 3);
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltControlFacadeImpl#resetOltBoard(com.topvision.framework.snmp.SnmpParam, java.lang.Long)}
     * .
     */
    @Test
    public void testResetOltBoard() {
        // reset2号槽位
        getSlotFacade().resetOltBoard(getSnmpParam(), new EponIndex(2).getSlotIndex());
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltControlFacadeImpl#setOltFanSpeedControl(com.topvision.framework.snmp.SnmpParam, java.lang.Long, java.lang.Integer)}
     * .
     */
    @Test
    public void testSetOltFanSpeedControl() {
        // 设置19号槽位上的风扇转速等级为highSpeed
        getSlotFacade().setOltFanSpeedControl(getSnmpParam(), new EponIndex(19).getSlotIndex(), 4);
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltControlFacadeImpl#setSniName(com.topvision.framework.snmp.SnmpParam, java.lang.Long, java.lang.String)}
     * .
     */
    @Test
    public void testSetSniName() {
        // 设置1号槽1号口名字设置成sniB port 1/1
        getSniFacade().setSniName(getSnmpParam(), new EponIndex(1, 1).getSniIndex(), "sniB port 1/1");
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltControlFacadeImpl#setSniAdminStatus(com.topvision.framework.snmp.SnmpParam, java.lang.Long, java.lang.Integer)}
     * .
     */
    @Test
    public void testSetSniAdminStatus() {
        // 设置1号槽1号口名字设置成sniB port 1/1
        getSniFacade().setSniAdminStatus(getSnmpParam(), new EponIndex(1, 1).getSniIndex(), 1);
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltControlFacadeImpl#setSniIsolationStatus(com.topvision.framework.snmp.SnmpParam, java.lang.Long, java.lang.Integer)}
     * .
     */
    @Test
    public void testSetSniIsolationStatus() {
        // 设置1号槽1号口名字设置成sniB port 1/1
        getSniFacade().setSniIsolationStatus(getSnmpParam(), new EponIndex(1, 1).getSniIndex(), 2);
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltControlFacadeImpl#setSniFlowControl(com.topvision.framework.snmp.SnmpParam, java.lang.Long, java.lang.Integer, java.lang.Integer, java.lang.Integer)}
     * .
     */
    @Test
    public void testSetSniFlowControl() {
        // TODO 设置1槽1口流控模式为enable EgressRate 200 IngressRate200 程序是通的 snmpagent有问题
        getSniFacade().setSniFlowControl(getSnmpParam(), new EponIndex(1, 1).getSniIndex(), 200, 200);
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltControlFacadeImpl#setSniAutoNegotiationMode(com.topvision.framework.snmp.SnmpParam, java.lang.Long, java.lang.Integer)}
     * .
     */
    @Test
    public void testSetSniAutoNegotiationMode() {
        // 设置sniAutoNegotiationMode为half-10(2) TODO 设置不成功 需要再次确认 程序是通的 snmpagent有问题
        getSniFacade().setSniAutoNegotiationMode(getSnmpParam(), new EponIndex(1, 1).getSniIndex(), 2);
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltControlFacadeImpl#redirectSni(com.topvision.framework.snmp.SnmpParam, java.lang.Long, java.lang.Integer, java.lang.String, java.lang.Integer)}
     * .
     */
    @Test
    public void testRedirectSni() {
        // TODO 该方法未实现 不做测试
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltControlFacadeImpl#setSniMaxLearnMacNum(com.topvision.framework.snmp.SnmpParam, java.lang.Long, java.lang.Long)}
     * .
     */
    @Test
    public void testSetSniMaxLearnMacNum() {
        // 设置SniMaxLearnMacNum为100 TODO 设置不成功 需要再次确认 Read only error
        getSniFacade().setSniMaxLearnMacNum(getSnmpParam(), new EponIndex(1, 1).getSniIndex(), 100L);
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltControlFacadeImpl#setSni15MinPerfStatus(com.topvision.framework.snmp.SnmpParam, java.lang.Long, java.lang.Integer)}
     * .
     */
    @Test
    public void testSetSni15MinPerfStatus() {
        // 设置sni口15分钟性能使能为true
        getSniFacade().setSni15MinPerfStatus(getSnmpParam(), new EponIndex(1, 1).getSniIndex(), 1);
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltControlFacadeImpl#setSni24HourPerfStatus(com.topvision.framework.snmp.SnmpParam, java.lang.Long, java.lang.Integer)}
     * .
     */
    @Test
    public void testSetSni24HourPerfStatus() {
        // 设置sni口24小时性能使能为true
        getSniFacade().setSni24HourPerfStatus(getSnmpParam(), new EponIndex(1, 1).getSniIndex(), 1);
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltControlFacadeImpl#setPonAdminStatus(com.topvision.framework.snmp.SnmpParam, java.lang.Long, java.lang.Integer)}
     * .
     */
    @Test
    public void testSetPonAdminStatus() {
        // 设置PON口AdminStatus为2 3/1
        getPonFacade().setPonAdminStatus(getSnmpParam(), new EponIndex(3, 1).getPonIndex(), 2);
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltControlFacadeImpl#setPonIsolationStatus(com.topvision.framework.snmp.SnmpParam, java.lang.Long, java.lang.Integer)}
     * .
     */
    @Test
    public void testSetPonIsolationStatus() {
        // 设置PON口PonIsolationStatus为1 3/1
        getPonFacade().setPonIsolationStatus(getSnmpParam(), new EponIndex(3, 1).getPonIndex(), 1);
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltControlFacadeImpl#setPonPortEncryptMode(com.topvision.framework.snmp.SnmpParam, java.lang.Long, java.lang.Integer, java.lang.Integer)}
     * .
     */
    @Test
    public void testSetPonPortEncryptMode() {
        // 设置PON口ponPortEncryptMode为2 3/1
        getPonFacade().setPonPortEncryptMode(getSnmpParam(), new EponIndex(3, 1).getPonIndex(), 2, 60);
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltControlFacadeImpl#setPonMaxLearnMacNum(com.topvision.framework.snmp.SnmpParam, java.lang.Long, java.lang.Long)}
     * .
     */
    @Test
    public void testSetPonMaxLearnMacNum() {
        // 设置PON口PonMaxLearnMacNum为1000 3/1
        getPonFacade().setPonMaxLearnMacNum(getSnmpParam(), new EponIndex(3, 1).getPonIndex(), 1000L);
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltControlFacadeImpl#setPon15MinPerfStatus(com.topvision.framework.snmp.SnmpParam, java.lang.Long, java.lang.Integer)}
     * .
     */
    @Test
    public void testSetPon15MinPerfStatus() {
        // 设置PON口Pon15MinPerfStatus为2 3/1
        getPonFacade().setPon15MinPerfStatus(getSnmpParam(), new EponIndex(3, 1).getPonIndex(), 2);
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltControlFacadeImpl#setPon24HourPerfStatus(com.topvision.framework.snmp.SnmpParam, java.lang.Long, java.lang.Integer)}
     * .
     */
    @Test
    public void testSetPon24HourPerfStatus() {
        // 设置PON口Pon24HourPerfStatus为2 3/1
        getPonFacade().setPon24HourPerfStatus(getSnmpParam(), new EponIndex(3, 1).getPonIndex(), 2);
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltControlFacadeImpl#setOnuAdminStatus(com.topvision.framework.snmp.SnmpParam, java.lang.Long, java.lang.Integer)}
     * .
     */
    @Test
    public void testSetOnuAdminStatus() {
        // 设置OnuAdminStatus为2 3/1/1
        Long onuIndex = new EponIndex(3, 1, 1).getOnuIndex();
        // 确认计算出来的onuDeviceIndex是否和复查的onuDeviceIndex一致
        System.out.println("onuMibIndex = " + EponIndex.getOnuMibIndexByIndex(onuIndex));
        getOnuFacade().setOnuAdminStatus(getSnmpParam(), onuIndex, 2);
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltControlFacadeImpl#modifyOnuName(com.topvision.framework.snmp.SnmpParam, java.lang.Long, java.lang.String)}
     * .
     */
    @Test
    public void testModifyOnuName() {
        // 设置OnuAdminStatus为x1 onu 3/1/1
        Long onuIndex = new EponIndex(3, 1, 1).getOnuIndex();
        // 确认计算出来的onuDeviceIndex是否和复查的onuDeviceIndex一致
        System.out.println("onuMibIndex = " + EponIndex.getOnuMibIndexByIndex(onuIndex));
        getOnuFacade().modifyOnuName(getSnmpParam(), onuIndex, "xx onu");
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltControlFacadeImpl#resetOnu(com.topvision.framework.snmp.SnmpParam, java.lang.Long)}
     * .
     */
    @Test
    public void testResetOnu() {
        // ResetOnu 3/1/1
        Long onuIndex = new EponIndex(3, 1, 1).getOnuIndex();
        // 确认计算出来的onuDeviceIndex是否和复查的onuDeviceIndex一致
        System.out.println("onuMibIndex = " + EponIndex.getOnuMibIndexByIndex(onuIndex));
        getOnuFacade().resetOnu(getSnmpParam(), onuIndex);
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltControlFacadeImpl#setOnuAuthenPolicy(com.topvision.framework.snmp.SnmpParam, java.lang.Integer)}
     * .
     */
    @Test
    public void testSetOnuAuthenPolicy() {
        // 设置onuAuthenticationPolicy为1
        getOnuAuthFacade().setOnuAuthenPolicy(getSnmpParam(), 1);
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltControlFacadeImpl#addOnuMacAuthen(com.topvision.framework.snmp.SnmpParam, com.topvision.ems.epon.facade.domain.OltAuthentication)}
     * .
     */
    @Test
    public void testAddOnuMacAuthen() {
        // 测试添加ONU的MAC认证表
        OltAuthenMacInfo oltAuthenMacInfo = new OltAuthenMacInfo();
        oltAuthenMacInfo.setOnuMibIndex(16974089L);
        oltAuthenMacInfo.setAuthType(2);
        oltAuthenMacInfo.setAuthAction(1);
        // oltAuthenMacInfo.setOnuAuthenMacAddress(new String(new byte[] { 0x02, 0x02, 0x02, 0x02,
        // 0x02, 0x02 }));
        oltAuthenMacInfo.setOnuAuthenMacAddress("01:09:09:09:09:02");
        oltAuthenMacInfo.setOnuAuthenRowStatus(RowStatus.CREATE_AND_GO);
        getOnuAuthFacade().addOnuMacAuthen(getSnmpParam(), oltAuthenMacInfo);
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltControlFacadeImpl#addOnuSnAuthen(com.topvision.framework.snmp.SnmpParam, com.topvision.ems.epon.facade.domain.OltAuthentication)}
     * .
     */
    @Test
    public void testAddOnuSnAuthen() {
        // 测试添加ONU的SN认证表
        OltAuthenSnInfo oltAuthenSnInfo = new OltAuthenSnInfo();
        oltAuthenSnInfo.setTopOnuAuthLogicSnCardIndex(3);
        oltAuthenSnInfo.setTopOnuAuthLogicSnPonIndex(2);
        oltAuthenSnInfo.setTopOnuAuthLogicSnOnuIndex(1);
        oltAuthenSnInfo.setTopOnuAuthLogicSn("test2");
        // oltAuthenSnInfo.setTopOnuAuthLogicSnMode(0);
        oltAuthenSnInfo.setTopOnuAuthLogicSnAction(1);
        oltAuthenSnInfo.setTopOnuAuthLogicSnRowStatus(RowStatus.CREATE_AND_GO);
        getOnuAuthFacade().addOnuSnAuthen(getSnmpParam(), oltAuthenSnInfo);
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltControlFacadeImpl#modifyOnuMacAuthen(com.topvision.framework.snmp.SnmpParam, com.topvision.ems.epon.facade.domain.OltAuthentication)}
     * .
     */
    @Test
    public void testModifyOnuMacAuthen() {
        // TODO 该方法未实现 不做测试 目前只支持增加和删除，故不能修改
        OltAuthenMacInfo oltAuthenMacInfo = new OltAuthenMacInfo();
        oltAuthenMacInfo.setOnuIndex(1030109L);
        oltAuthenMacInfo.setAuthType(2);
        oltAuthenMacInfo.setAuthAction(2);
        oltAuthenMacInfo.setOnuAuthenMacAddress("08:09:09:09:09:01");
        oltAuthenMacInfo.setOnuAuthenRowStatus(RowStatus.CREATE_AND_WAIT);
        getOnuAuthFacade().modifyOnuMacAuthen(getSnmpParam(), oltAuthenMacInfo);
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltControlFacadeImpl#modifyOnuSnAuthen(com.topvision.framework.snmp.SnmpParam, com.topvision.ems.epon.facade.domain.OltAuthentication)}
     * .
     */
    @Test
    public void testModifyOnuSnAuthen() {
        OltAuthenSnInfo oltAuthenSnInfo = new OltAuthenSnInfo();
        oltAuthenSnInfo.setTopOnuAuthLogicSnCardIndex(3);
        oltAuthenSnInfo.setTopOnuAuthLogicSnPonIndex(1);
        oltAuthenSnInfo.setTopOnuAuthLogicSnOnuIndex(7);
        oltAuthenSnInfo.setTopOnuAuthLogicSn("test2");
        // oltAuthenSnInfo.setTopOnuAuthLogicSnMode(0);
        oltAuthenSnInfo.setTopOnuAuthLogicSnAction(1);
        // oltAuthenSnInfo.setTopOnuAuthLogicSnRowStatus(RowStatus.CREATE_AND_GO);
        getOnuAuthFacade().modifyOnuSnAuthen(getSnmpParam(), oltAuthenSnInfo);
        // TODO 该方法未实现 不做测试
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltControlFacadeImpl#deleteOnuMacAuthen(com.topvision.framework.snmp.SnmpParam, java.lang.Long)}
     * .
     */
    @Test
    public void testDeleteOnuMacAuthen() {
        // TODO 该方法未实现 不做测试
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltControlFacadeImpl#deleteOnuSnAuthen(com.topvision.framework.snmp.SnmpParam, java.lang.Long)}
     * .
     */
    @Test
    public void testDeleteOnuSnAuthen() {
        // TODO 该方法未实现 不做测试
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltControlFacadeImpl#getOnuAuthenPreConfigList(java.lang.Long, java.lang.Long)}
     * .
     */
    @Test
    public void testGetOnuAuthenPreConfigList() {
        // TODO 该方法未实现 不做测试

    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltControlFacadeImpl#deregisterOnu(com.topvision.framework.snmp.SnmpParam, java.lang.Long)}
     * .
     */
    @Test
    public void testDeregisterOnu() {
        // DeregisterOnu 3/1/1
        Long onuIndex = new EponIndex(3, 1, 1).getOnuIndex();
        // 确认计算出来的onuDeviceIndex是否和复查的onuDeviceIndex一致
        System.out.println("onuMibIndex = " + EponIndex.getOnuMibIndexByIndex(onuIndex));
        getOnuFacade().deregisterOnu(getSnmpParam(), onuIndex);
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltControlFacadeImpl#setUniAdminStatus(com.topvision.framework.snmp.SnmpParam, java.lang.Long, java.lang.Integer)}
     * .
     */
    @Test
    public void testSetUniAdminStatus() {
        // SetUniAdminStatus为1 3/1/1/1
        Long uniIndex = new EponIndex(3, 1, 1, 1, 1).getUniIndex();
        // 确认计算出来的onuDeviceIndex是否和复查的onuDeviceIndex一致
        System.out.println("onuMibIndex = " + EponIndex.getOnuMibIndexByIndex(uniIndex));
        System.out.println("OnuCardIndex = " + EponIndex.getOnuCardNo(uniIndex));
        System.out.println("UniNo = " + EponIndex.getUniNo(uniIndex));
        //getUniFacade().setUniAdminStatus(getSnmpParam(), uniIndex, 1);
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltControlFacadeImpl#setUniAutoNegotiationStatus(com.topvision.framework.snmp.SnmpParam, java.lang.Long, java.lang.Integer)}
     * .
     */
    @Test
    public void testSetUniAutoNegotiationStatus() {
        // UniAutoNegotiationStatus 为 1 3/1/1/1
        Long uniIndex = new EponIndex(3, 1, 1, 1, 1).getUniIndex();
        // 确认计算出来的onuDeviceIndex是否和复查的onuDeviceIndex一致
        System.out.println("onuMibIndex = " + EponIndex.getOnuMibIndexByIndex(uniIndex));
        System.out.println("OnuCardIndex = " + EponIndex.getOnuCardNo(uniIndex));
        System.out.println("UniNo = " + EponIndex.getUniNo(uniIndex));
        getUniFacade().setUniAutoNegotiationStatus(getSnmpParam(), uniIndex, 1);
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltControlFacadeImpl#getOltFilePath(com.topvision.framework.snmp.SnmpParam)}
     * .
     */
    @Test
    public void testGetOltFilePath() {
        // TODO 该方法未实现 不做测试
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltControlFacadeImpl#deleteOltFile(com.topvision.framework.snmp.SnmpParam, java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public void testDeleteOltFile() {
        // TODO 该方法未实现 不做测试
        OltFileAttribute file = new OltFileAttribute();
        file.setFileName("minacore1ds/;.d;");
        file.setFilePath("/tffs0/");
        file.setFileManagementAction(2);
        getFacade().deleteOltFile(getSnmpParam(), file);
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltControlFacadeImpl#contorlOltFile(com.topvision.framework.snmp.SnmpParam, java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public void testContorlOltFile() {
        // TODO 该方法未实现 不做测试
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltControlFacadeImpl#getOltSaveStatus(com.topvision.framework.snmp.SnmpParam)}
     * .
     */
    @Test
    public void testGetOltSaveStatus() {
        // GetOltSaveStatus
        // Integer status = getFacade().getOltSaveStatus(getSnmpParam());
        //System.out.println("status = " + status);
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltControlFacadeImpl#setOnuVoipEnable(com.topvision.framework.snmp.SnmpParam, java.lang.Long, java.lang.Integer)}
     * .
     */
    @Test
    public void testSetOnuVoipEnable() {
        // SetOnuVoipEnable 1 3/1/1 TODO mib未实现该节点
        Long onuIndex = new EponIndex(3, 1, 1).getOnuIndex();
        // 确认计算出来的onuDeviceIndex是否和复查的onuDeviceIndex一致
        System.out.println("onuMibIndex = " + EponIndex.getOnuMibIndexByIndex(onuIndex));
        getOnuFacade().setOnuVoipEnable(getSnmpParam(), onuIndex, 1);
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltControlFacadeImpl#setOnuTemperatureDetectEnable(com.topvision.framework.snmp.SnmpParam, java.lang.Long, java.lang.Integer)}
     * .
     */
    @Test
    public void testSetOnuTemperatureDetectEnable() {
        // SetOnuTemperatureDetectEnable 1 3/1/1 TODO 设置不成功 再次确认 snmpagent的问题
        Long onuIndex = new EponIndex(3, 1, 1).getOnuIndex();
        // 确认计算出来的onuDeviceIndex是否和复查的onuDeviceIndex一致
        System.out.println("onuMibIndex = " + EponIndex.getOnuMibIndexByIndex(onuIndex));
        getOnuFacade().setOnuTemperatureDetectEnable(getSnmpParam(), onuIndex, 1);
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltControlFacadeImpl#setOnuFecEnable(com.topvision.framework.snmp.SnmpParam, java.lang.Long, java.lang.Integer)}
     * .
     */
    @Test
    public void testSetOnuFecEnable() {
        // SetOnuFecEnable 1 3/1/1
        Long onuIndex = new EponIndex(3, 1, 1).getOnuIndex();
        // 确认计算出来的onuDeviceIndex是否和复查的onuDeviceIndex一致
        System.out.println("onuMibIndex = " + EponIndex.getOnuMibIndexByIndex(onuIndex));
        getOnuFacade().setOnuFecEnable(getSnmpParam(), onuIndex, 1);
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltControlFacadeImpl#setOnu15minEnable(com.topvision.framework.snmp.SnmpParam, java.lang.Long, java.lang.Integer)}
     * .
     */
    @Test
    public void testSetOnu15minEnable() {
        // SetOnu15minEnable 1 3/1/1 TODO 设置不成功 SET: Error: Undo failed error
        Long onuIndex = new EponIndex(3, 1, 2).getOnuIndex();
        // 确认计算出来的onuDeviceIndex是否和复查的onuDeviceIndex一致
        System.out.println("onuMibIndex = " + EponIndex.getOnuMibIndexByIndex(onuIndex));
        getOnuFacade().setOnu15minEnable(getSnmpParam(), onuIndex, 1);
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltControlFacadeImpl#setOnu24hEnable(com.topvision.framework.snmp.SnmpParam, java.lang.Long, java.lang.Integer)}
     * .
     */
    @Test
    public void testSetOnu24hEnable() {
        // SetOnu24hEnable 1 3/1/1 TODO 设置不成功 SET: Error: Undo failed error
        Long onuIndex = new EponIndex(3, 1, 1).getOnuIndex();
        // 确认计算出来的onuDeviceIndex是否和复查的onuDeviceIndex一致
        System.out.println("onuMibIndex = " + EponIndex.getOnuMibIndexByIndex(onuIndex));
        getOnuFacade().setOnu24hEnable(getSnmpParam(), onuIndex, 1);
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltControlFacadeImpl#setOnuCatvEnable(com.topvision.framework.snmp.SnmpParam, java.lang.Long, java.lang.Integer)}
     * .
     */
    @Test
    public void testSetOnuCatvEnable() {
        // SetOnuCatvEnable 1 3/1/1 TODO mib未实现该节点
        Long onuIndex = new EponIndex(3, 1, 1).getOnuIndex();
        // 确认计算出来的onuDeviceIndex是否和复查的onuDeviceIndex一致
        System.out.println("onuMibIndex = " + EponIndex.getOnuMibIndexByIndex(onuIndex));
        getOnuFacade().setOnuCatvEnable(getSnmpParam(), onuIndex, 1);
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltControlFacadeImpl#setOnuMacMaxNum(com.topvision.framework.snmp.SnmpParam, java.lang.Long, java.lang.Integer)}
     * .
     */
    @Test
    public void testSetOnuMacMaxNum() {
        // SetOnuCatvEnable 1 3/1/1 TODO mib未实现该节点
        Long onuIndex = new EponIndex(3, 1, 1).getOnuIndex();
        // 确认计算出来的onuDeviceIndex是否和复查的onuDeviceIndex一致
        System.out.println("onuMibIndex = " + EponIndex.getOnuMibIndexByIndex(onuIndex));
        getOnuFacade().setOnuCatvEnable(getSnmpParam(), onuIndex, 1);
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltControlFacadeImpl#setOnuRstpBridgeMode(com.topvision.framework.snmp.SnmpParam, java.lang.Long, java.lang.Integer)}
     * .
     */
    @Test
    public void testSetOnuRstpBridgeMode() {
        // SetOnuRstpBridgeMode 1 3/1/1
        Long onuIndex = new EponIndex(3, 1, 1).getOnuIndex();
        // 确认计算出来的onuDeviceIndex是否和复查的onuDeviceIndex一致
        System.out.println("onuMibIndex = " + EponIndex.getOnuMibIndexByIndex(onuIndex));
        getOnuFacade().setOnuRstpBridgeMode(getSnmpParam(), onuIndex, 1);
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltControlFacadeImpl#setUniIsolationEnable(com.topvision.framework.snmp.SnmpParam, java.lang.Long, java.lang.Integer)}
     * .
     */
    @Test
    public void testSetUniIsolationEnable() {
        // SetUniIsolationEnable 1 3/1/1
        Long uniIndex = new EponIndex(3, 1, 1, 1, 1).getUniIndex();
        // 确认计算出来的onuDeviceIndex是否和复查的onuDeviceIndex一致
        System.out.println("onuMibIndex = " + EponIndex.getOnuMibIndexByIndex(uniIndex));
        System.out.println("OnuCardIndex = " + EponIndex.getOnuCardNo(uniIndex));
        System.out.println("UniNo = " + EponIndex.getUniNo(uniIndex));
        getUniFacade().setUniIsolationEnable(getSnmpParam(), uniIndex, 1);
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltControlFacadeImpl#setUniFlowCtrlEnable(com.topvision.framework.snmp.SnmpParam, java.lang.Long, java.lang.Integer)}
     * .
     */
    @Test
    public void testSetUniFlowCtrlEnable() {
        // SetUniFlowCtrlEnable 1 3/1/1
        Long uniIndex = new EponIndex(3, 1, 1, 1, 1).getUniIndex();
        // 确认计算出来的onuDeviceIndex是否和复查的onuDeviceIndex一致
        System.out.println("onuMibIndex = " + EponIndex.getOnuMibIndexByIndex(uniIndex));
        System.out.println("OnuCardIndex = " + EponIndex.getOnuCardNo(uniIndex));
        System.out.println("UniNo = " + EponIndex.getUniNo(uniIndex));
        getUniFacade().setUniFlowCtrlEnable(getSnmpParam(), uniIndex, 1);
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltControlFacadeImpl#setUni15minEnable(com.topvision.framework.snmp.SnmpParam, java.lang.Long, java.lang.Integer)}
     * .
     */
    @Test
    public void testSetUni15minEnable() {
        // SetUni15minEnable 1 3/1/1
        Long uniIndex = new EponIndex(3, 1, 1, 1, 1).getUniIndex();
        // 确认计算出来的onuDeviceIndex是否和复查的onuDeviceIndex一致
        System.out.println("onuMibIndex = " + EponIndex.getOnuMibIndexByIndex(uniIndex));
        System.out.println("OnuCardIndex = " + EponIndex.getOnuCardNo(uniIndex));
        System.out.println("UniNo = " + EponIndex.getUniNo(uniIndex));
        getUniFacade().setUni15minEnable(getSnmpParam(), uniIndex, 1);
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltControlFacadeImpl#setUni24hEnable(com.topvision.framework.snmp.SnmpParam, java.lang.Long, java.lang.Integer)}
     * .
     */
    @Test
    public void testSetUni24hEnable() {
        // SetUni24hEnable 1 3/1/1
        Long uniIndex = new EponIndex(3, 1, 1, 1, 1).getUniIndex();
        // 确认计算出来的onuDeviceIndex是否和复查的onuDeviceIndex一致
        System.out.println("onuMibIndex = " + EponIndex.getOnuMibIndexByIndex(uniIndex));
        System.out.println("OnuCardIndex = " + EponIndex.getOnuCardNo(uniIndex));
        System.out.println("UniNo = " + EponIndex.getUniNo(uniIndex));
        getUniFacade().setUni24hEnable(getSnmpParam(), uniIndex, 1);
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltControlFacadeImpl#modifyUniStormInfo(com.topvision.framework.snmp.SnmpParam, java.lang.Object)}
     * .
     */
    @Test
    public void testModifyUniStormInfo() {
        OltUniStormSuppressionEntry oltUniStormSuppressionEntry = new OltUniStormSuppressionEntry();
        // oltUniStormSuppressionEntry.setUniBroadcastStormSuppressionCardIndex(16974081l);
        // oltUniStormSuppressionEntry.setUniBroadcastStormSuppressionPortIndex(1l);
        // oltUniStormSuppressionEntry.setMulticastStormOutPacketRate(1111);
        getUniFacade().modifyUniStormInfo(getSnmpParam(), oltUniStormSuppressionEntry);
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltControlFacadeImpl#modifyUniRateLimitInfo(com.topvision.framework.snmp.SnmpParam, java.lang.Object)}
     * .
     */
    @Test
    public void testModifyUniRateLimitInfo() {
        OltUniPortRateLimit oltUniPortRateLimit = new OltUniPortRateLimit();
        oltUniPortRateLimit.setUniPortRateLimitDeviceIndex(16974081l);
        oltUniPortRateLimit.setUniPortRateLimitCardIndex(1l);
        oltUniPortRateLimit.setUniPortRateLimitPortIndex(3l);
        oltUniPortRateLimit.setUniPortInCBS(199);
        oltUniPortRateLimit.setUniPortInRateLimitEnable(1);
        getUniFacade().modifyUniRateLimitInfo(getSnmpParam(), oltUniPortRateLimit);
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltControlFacadeImpl#restartUniAutoNego(com.topvision.framework.snmp.SnmpParam, java.lang.Long)}
     * .
     */
    @Test
    public void testRestartUniAutoNego() {
        // RestartUniAutoNego 1 3/1/1
        Long uniIndex = new EponIndex(3, 1, 1, 1, 1).getUniIndex();
        // 确认计算出来的onuDeviceIndex是否和复查的onuDeviceIndex一致
        System.out.println("onuMibIndex = " + EponIndex.getOnuMibIndexByIndex(uniIndex));
        System.out.println("OnuCardIndex = " + EponIndex.getOnuCardNo(uniIndex));
        System.out.println("UniNo = " + EponIndex.getUniNo(uniIndex));
        getUniFacade().restartUniAutoNego(getSnmpParam(), uniIndex);
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltControlFacadeImpl#setUniAutoNegoEnable(com.topvision.framework.snmp.SnmpParam, java.lang.Long, java.lang.Integer)}
     * .
     */
    @Test
    public void testSetUniAutoNegoEnable() {
        // SetUniAutoNegoEnable 2 3/1/1
        Long uniIndex = new EponIndex(3, 1, 1, 1, 1).getUniIndex();
        // 确认计算出来的onuDeviceIndex是否和复查的onuDeviceIndex一致
        System.out.println("onuMibIndex = " + EponIndex.getOnuMibIndexByIndex(uniIndex));
        System.out.println("OnuCardIndex = " + EponIndex.getOnuCardNo(uniIndex));
        System.out.println("UniNo = " + EponIndex.getUniNo(uniIndex));
        getUniFacade().setUniAutoNegoEnable(getSnmpParam(), uniIndex, 2);
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.engine.executor.OltControlFacadeImpl#getUniAutoNegoStatus(com.topvision.framework.snmp.SnmpParam, java.lang.Long)}
     * .
     */
    @Test
    public void testGetUniAutoNegoStatus() {
        // GetUniAutoNegoStatus 3/1/1 TODO 位图处理的问题还没解决
        Long uniIndex = new EponIndex(3, 1, 1, 1, 1).getUniIndex();
        // 确认计算出来的onuDeviceIndex是否和复查的onuDeviceIndex一致
        System.out.println("onuMibIndex = " + EponIndex.getOnuMibIndexByIndex(uniIndex));
        System.out.println("OnuCardIndex = " + EponIndex.getOnuCardNo(uniIndex));
        System.out.println("UniNo = " + EponIndex.getUniNo(uniIndex));
        Integer status = getUniFacade().getUniAutoNegoStatus(getSnmpParam(), uniIndex);
        System.out.println("status = " + status);
    }

    @Test
    public void testGetOltFileTransferStatus() throws Exception {
        // TODO 该方法未实现 不做测试
        System.out.print(getFacade().getOltFileTransferStatus(getSnmpParam()));
    }

    @Test
    public void testGetAuthenMacInfos() {
        // 测试取得ONU的MAC认证表
        List<OltAuthenMacInfo> oltAuthenMacInfos = getOnuAuthFacade().getAuthenMacInfos(getSnmpParam());
        for (OltAuthenMacInfo olt : oltAuthenMacInfos) {
            System.out.println(olt.getOnuAuthenMacAddress());
            System.out.println(olt.getAuthAction());
            System.out.println(olt.getOnuIndex());
        }
    }

    @Test
    public void testGetAuthenSnInfos() {
        // 测试取得ONU的SN认证表
        List<OltAuthenSnInfo> oltAuthenSnInfos = getOnuAuthFacade().getAuthenSnInfos(getSnmpParam());
        for (OltAuthenSnInfo olt : oltAuthenSnInfos) {
            System.out.println(olt.getTopOnuAuthLogicSnCardIndex());
            System.out.println(olt.getTopOnuAuthLogicSnPonIndex());
            System.out.println(olt.getTopOnuAuthLogicSnOnuIndex());
            System.out.println(olt.getTopOnuAuthLogicSn());
            System.out.println(olt.getTopOnuAuthPassword());
            System.out.println(olt.getTopOnuAuthLogicSnMode());
            System.out.println(olt.getTopOnuAuthLogicSnAction());
        }
    }

    @Test
    public void testGetOltPonStormSuppressionEntries() {
        // 测试PON口广播风暴抑制信息
        List<OltPonStormSuppressionEntry> oltPonStormSuppressionEntries = getPonFacade()
                .getOltPonStormSuppressionEntries(getSnmpParam());
        System.out.println("size:" + oltPonStormSuppressionEntries.size());
        for (OltPonStormSuppressionEntry olt : oltPonStormSuppressionEntries) {
            System.out.println(olt.getBsDeviceIndex());
            System.out.println(olt.getBsCardIndex());
            System.out.println(olt.getBsPortIndex());
            System.out.println(olt.getUnicastStormEnable());
            System.out.println(olt.getUnicastStormInPacketRate());
            System.out.println(olt.getUnicastStormOutPacketRate());
            System.out.println(olt.getMulticastStormEnable());
            System.out.println(olt.getMulticastStormInPacketRate());
            System.out.println(olt.getMulticastStormOutPacketRate());
            System.out.println(olt.getBroadcastStormEnable());
            System.out.println(olt.getBroadcastStormInPacketRate());
            System.out.println(olt.getBroadcastStormOutPacketRate());
        }
    }

    /**
     * 
     * 
     */
    @Test
    public void testSetPonStormInfo() {
        // 设备PON口广播风暴抑制信息
        OltPonStormSuppressionEntry oltPonStormSuppressionEntry = new OltPonStormSuppressionEntry();
        oltPonStormSuppressionEntry.setBsCardIndex(3l);
        oltPonStormSuppressionEntry.setBsDeviceIndex(1);
        oltPonStormSuppressionEntry.setBsPortIndex(1);
        oltPonStormSuppressionEntry.setBroadcastStormOutPacketRate(1000);
        getPonFacade().setPonStormInfo(getSnmpParam(), oltPonStormSuppressionEntry);

    }

    /**
     * 
     * 
     */
    @Test
    public void testGetBlockAuthens() {
        // 获得设备ONU阻塞信息
        List<OltOnuBlockAuthen> oltOnuBlockAuthens = getOnuAuthFacade().getBlockAuthens(getSnmpParam());
        for (OltOnuBlockAuthen oltOnuBlockAuthen : oltOnuBlockAuthens) {
            System.out.println(oltOnuBlockAuthen.getMacAddress());
            System.out.println(oltOnuBlockAuthen.getOnuMibIndex());
            System.out.println(oltOnuBlockAuthen.getAuthTime());
        }
    }

    @Test
    public void testGetOnuRstp() {
        List<OltOnuRstp> list = getOnuFacade().getOltOnuRstp(getSnmpParam());
        for (OltOnuRstp oltOnuRstp : list) {
            System.out.println(oltOnuRstp.getTopOnuRstpCardNo());
            System.out.println(oltOnuRstp.getTopOnuRstpPonNo());
            System.out.println(oltOnuRstp.getTopOnuRstpOnuNo());
            System.out.println(oltOnuRstp.getRstpBridgeMode());
        }
    }

    @Test
    public void testGetOnuCatv() {
        List<OltOnuCatv> list = getOnuFacade().getOltOnuCatv(getSnmpParam());
        for (OltOnuCatv oltOnuCatv : list) {
            System.out.println(oltOnuCatv.getTopOnuCatvCardNo());
            System.out.println(oltOnuCatv.getTopOnuCatvPonNo());
            System.out.println(oltOnuCatv.getTopOnuCatvOnuNo());
            System.out.println(oltOnuCatv.getOnuCatvEnable());
        }
    }

    @Test
    public void testAddSniTrunk() {
        OltSniTrunkConfig sniTrunkConfig = new OltSniTrunkConfig();
        sniTrunkConfig.setEntityId(getSnmpParam().getEntityId());
        sniTrunkConfig.setSniTrunkGroupConfigIndex(200);
        sniTrunkConfig.setSniTrunkGroupConfigMember("01:02:03:00:01:02:02:00");
        sniTrunkConfig.setSniTrunkGroupConfigName("trunk2");
        sniTrunkConfig.setSniTrunkGroupConfigPolicy(2);
        getTrunkFacade().addSniTrunkConfig(getSnmpParam(), sniTrunkConfig);
    }

    @Test
    public void testAddMacInfo() {
        OltAuthenMacInfo oltAuthenMacInfo = new OltAuthenMacInfo();
        oltAuthenMacInfo.setAuthAction(1);
        oltAuthenMacInfo.setAuthType(1);
        oltAuthenMacInfo.setOnuAuthenMacAddress("09:09:09:09:09:81");
        oltAuthenMacInfo.setOnuIndex(12918521856l);
        getOnuAuthFacade().addOnuMacAuthen(getSnmpParam(), oltAuthenMacInfo);
    }
}
