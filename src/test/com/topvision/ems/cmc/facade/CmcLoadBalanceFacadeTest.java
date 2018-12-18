/***********************************************************************
 * $Id: CmcLoadBalanceFacadeTest.java,v1.0 2013-4-28 下午1:15:38 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.facade;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.topvision.ems.cmc.loadbalance.domain.CmcLoadBalanceGroup;
import com.topvision.ems.cmc.loadbalance.facade.CmcLoadBalanceFacade;
import com.topvision.ems.cmc.loadbalance.facade.domain.CmcLoadBalCfg;
import com.topvision.ems.cmc.loadbalance.facade.domain.CmcLoadBalExcludeCm;
import com.topvision.ems.cmc.loadbalance.service.CmcLoadBalanceService;
import com.topvision.framework.EnvironmentConstants;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.facade.FacadeFactory;

/**
 * @author loyal
 * @created @2013-4-28-下午1:15:38
 *
 */
public class CmcLoadBalanceFacadeTest{
    protected static ApplicationContext ctx = null;
    protected static FacadeFactory facadeFactory;
    private String ip = "172.16.34.111";
    private String roCommunity = "public";
    private String mib = "TOPVISION-CCMTS-MIB, DOCS-LOADBALANCING-MIB";
    private SnmpParam snmpParam = new SnmpParam();
    private Long cmcIndex = 142671872l;// 测试数据index
    private CmcLoadBalanceService cmcLoadBalanceService;
    private CmcLoadBalanceFacade cmcLoadBalanceFacade;

    @Before
    public void setUp() {
        //需要修改ac-test.xml文件中工程文件夹路径 
        String[] configs = { "classpath:META-INF/spring/ac-test.xml",
                "classpath:META-INF/spring/applicationContext-facade.xml",
                "classpath*:com/**/ec-cmcfacade-test.xml" };
        ctx = new ClassPathXmlApplicationContext(configs);
        facadeFactory = (FacadeFactory) ctx.getBean("facadeFactory");
        EnvironmentConstants.putEnv(EnvironmentConstants.MIB_HOME, "webapp/WEB-INF/mibs");
        cmcLoadBalanceService = (CmcLoadBalanceService) ctx.getBean("cmcLoadBalanceService");
        cmcLoadBalanceFacade = (CmcLoadBalanceFacade) ctx.getBean("cmcLoadBalanceFacade");
        snmpParam = getSnmpParam();
    }

    @Test
    public void testSetConfig() throws SQLException {
    	//test ok 
        CmcLoadBalCfg cmcLoadBalCfg = new CmcLoadBalCfg();
        cmcLoadBalCfg.setCmcId(30000000000l);
        cmcLoadBalCfg.setTopLoadBalConfigCmcIndex(cmcIndex);
        cmcLoadBalCfg.setTopLoadBalConfigEnable(1);
        cmcLoadBalCfg.setTopLoadBalConfigDbcInitAtdma(1);//初始化技术
        cmcLoadBalCfg.setTopLoadBalConfigDbcInitScdma(1);
        cmcLoadBalCfg.setTopLoadBalConfigDccInitAtdma(3);
        cmcLoadBalCfg.setTopLoadBalConfigDccInitScdma(1);
        cmcLoadBalCfg.setTopLoadBalConfigDiffThresh(1000l);//差值门限
        cmcLoadBalCfg.setTopLoadBalConfigInterval(1000l);//移动时间间隔
        cmcLoadBalCfg.setTopLoadBalConfigMaxMoves(50l);//最大移动CM数
        cmcLoadBalCfg.setTopLoadBalConfigMethod(1);//方式 : unitli,moderm
        cmcLoadBalCfg.setTopLoadBalConfigNumPeriod(1000);//周期加权
        cmcLoadBalCfg.setTopLoadBalConfigPeriod(1000l);//周期
        cmcLoadBalCfg.setTopLoadBalConfigTriggerThresh(1000l);//阈值
        cmcLoadBalanceService.modifyLoadBalanceGlobalCfg(cmcLoadBalCfg);
    }
    
    @Test
    public void testAddCmcLoadBalExcludeCmTable() throws SQLException {
        CmcLoadBalExcludeCm cmcLoadBalExcludeCm = new CmcLoadBalExcludeCm();
        cmcLoadBalExcludeCm.setCmcId(30000000000l);
        cmcLoadBalExcludeCm.setIfIndex(cmcIndex);
        cmcLoadBalExcludeCm.setTopLoadBalExcludeCmMacRang("00:00:00:00:00:29 00:00:00:00:00:30");
        cmcLoadBalanceService.addLoadBalanceExcMacRange(cmcLoadBalExcludeCm);
    }
    
    @Test
    public void testAddLoadBalGroupTable() throws SQLException {
        cmcLoadBalanceFacade.addLoadBalGroupTable(snmpParam, 142672128l);
    }
    
    @Test
    public void testAddLoadBalGroup() throws SQLException {
    	CmcLoadBalanceGroup cmcLoadBalanceGroup = new CmcLoadBalanceGroup();
    	cmcLoadBalanceGroup.setCmcId(30000000000l);
    	cmcLoadBalanceService.addLoadBalanceGroup(cmcLoadBalanceGroup);
    }
    
    @Test
    public void testAddLoadBalanceExcMacRange() throws SQLException {
    	CmcLoadBalExcludeCm cmcLoadBalExcludeCm = new CmcLoadBalExcludeCm();
    	cmcLoadBalExcludeCm.setCmcId(30000000000l);
    	cmcLoadBalExcludeCm.setTopLoadBalExcludeCmMacRang("00:00:00:00:00:12 00:00:00:00:00:25");
    	cmcLoadBalanceService.addLoadBalanceExcMacRange(cmcLoadBalExcludeCm);
    }
    
    @Test
    public void testModifyCmcLoadBalTopPolicy() throws SQLException {
    	//test OK
        cmcLoadBalanceFacade.modifyCmcLoadBalTopPolicy(snmpParam, 142671872l, 2l);
    }

    private SnmpParam getSnmpParam() {
        EnvironmentConstants.putEnv(EnvironmentConstants.MIB_HOME, "webapp/WEB-INF/mibs");
        SnmpParam snmpParam = new SnmpParam();
        snmpParam.setIpAddress(ip);
        snmpParam.setCommunity(roCommunity);
        snmpParam.setMibs(mib);
        return snmpParam;
    }
}
