/***********************************************************************
 * $Id: CmcDiscoveryServiceImplTest.java,v1.0 2011-11-13 下午02:05:27 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.service.impl;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.interceptor.annotations.After;
import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.cmc.facade.domain.CpeAttribute;
import com.topvision.ems.cmc.macdomain.facade.domain.MacDomainBaseInfo;
import com.topvision.ems.cmc.qos.facade.domain.CmcQosServiceFlowAttribute;
import com.topvision.ems.cmc.topology.domain.CmcDiscoveryData;
import com.topvision.ems.cmc.vlan.facade.domain.CmcVlanPrimaryInterface;
import com.topvision.framework.EnvironmentConstants;
import com.topvision.framework.exception.engine.SnmpException;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.snmp.SnmpUtil;

/**
 * @author Victor
 * @created @2011-11-13-下午02:05:27
 * 
 */
public class CmcDiscoveryServiceImplTest {
    private String ip = "172.16.17.27";
    private String roCommunity = "public";
    private String rwCommunity = "private";
    private String cmcmib = "RFC1213-MIB,DOCS-IF-MIB,DOCS-QOS-MIB,DOCS-SUBMGT-MIB,TOPVISION-CCMTS-MIB";
    private String eponmib = "RFC1213-MIB,NSCRTV-EPONEOC-EPON-MIB,SUMA-EPONEOC-EPON-MIB";
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private SnmpExecutorService snmpExecutorService;

    private SnmpParam getSnmpParam(String mib) {
        EnvironmentConstants.putEnv(EnvironmentConstants.MIB_HOME, "webapp/WEB-INF/mibs");
        SnmpParam param = new SnmpParam();
        param.setIpAddress(ip);
        param.setCommunity(roCommunity);
        param.setWriteCommunity(rwCommunity);
        param.setMibs(mib);
        param.setTimeout(10000);
        param.setRetry((byte) 3);
        return param;
    }

    @Before
    public void setUpBefore() {
        // 注意 mib是否写正确了，否则在下面出错
        snmpExecutorService = new SnmpExecutorService();
        snmpExecutorService.setCorePoolSize(Integer.valueOf(32));
        snmpExecutorService.setMaximumPoolSize(Integer.valueOf(255));
        snmpExecutorService.setKeepAliveTime(Integer.valueOf(32));
        snmpExecutorService.setQueueSize(Integer.valueOf(255));
        snmpExecutorService.initialize();// 对getTableLine,getData,getTable必不可少，不然就会出错
    }

    @Test
    public void refreshContactedCmList() {

        List<CmAttribute> cmList = snmpExecutorService.getTable(getSnmpParam(cmcmib), CmAttribute.class);
        System.out.println("打印：" + cmList.size());
        System.out.println("打印：" + cmList.get(0));
    }

    @Test
    public void testUpdatePort() {
        // logger.debug("godo {}",this.getSnmpParam());
    }

    /**
     * Test method for
     * {@link com.topvision.ems.cmc.discovery.service.impl.CmcDiscoveryServiceImpl#discovery(com.topvision.framework.snmp.SnmpParam)}
     * .
     */
    @Test
    public void testDiscoverySnmpParam() {
        // CmcDiscoveryServiceImpl cmc = new CmcDiscoveryServiceImpl();
        logger.info("data discovery begin");
        // good 发现成功，但是。。由于中间某项 modulationprofile有问题，造成中断(中断是由于mib问题)
        CmcDiscoveryData cmcDiscoveryData = this.discovery(snmpExecutorService, getSnmpParam(cmcmib));
        logger.info("data discovery end" + cmcDiscoveryData);
        assertNotNull(cmcDiscoveryData);
        // assertNotNull(cmc.discovery(getSnmpParam()));

    }

    @Test
    public void testRefreshInfo() {
        List<CmcQosServiceFlowAttribute> cmcQosServiceFlowAttribute1 = snmpExecutorService.getTable(
                getSnmpParam(cmcmib), CmcQosServiceFlowAttribute.class);
        System.out.println("测试：" + cmcQosServiceFlowAttribute1.get(1));
        assertNotNull(cmcQosServiceFlowAttribute1);
    }

    private CmcDiscoveryData discovery(SnmpExecutorService snmpExecutorService, SnmpParam param) {
        return null;
    }

    /**
     * Test method for
     * {@link com.topvision.ems.cmc.discovery.service.impl.CmcDiscoveryServiceImpl#updateEntity(com.topvision.ems.facade.domain.Entity, com.topvision.ems.cmc.facade.domain.CmcDiscoveryData)}
     * .
     */
    @Test
    public void testUpdateEntityEntityCmcDiscoveryData() {
    }

    @Test
    public void testSnmpCaijiData() {
        Long cmcIndex = 142671872L;
        // 方式一
        SnmpUtil snmpUtil = new SnmpUtil(getSnmpParam(cmcmib));
        // 方式二 snmpExecutorService
        // OltAttribute oltAttribute = snmpExecutorService.getData(getSnmpParam(eponmib),
        // OltAttribute.class);
        /*
         * CmcUpChannelBaseInfo channelBaseInfo = new CmcUpChannelBaseInfo();
         * List<CmcUpChannelBaseInfo> cmcUpChannelBaseInfos =
         * snmpUtil.getTableLines(channelBaseInfo, 1, Integer.MAX_VALUE);
         */
        MacDomainBaseInfo macDomainBaseInfo = new MacDomainBaseInfo();
        macDomainBaseInfo.setCmcIndex(cmcIndex);
        // snmpExecutorService.getData始终无法取到值,使用下面的try catch操作来进行
        // macDomainBaseInfo =snmpExecutorService.getData(getSnmpParam(cmcmib),
        // MacDomainBaseInfo.class);//snmpExecutorService.getTableLine(getSnmpParam(),
        // macDomainBaseInfo);//snmpUtil.getTableLine(macDomainBaseInfo);

        List<CmAttribute> cmAttributes = snmpExecutorService.getTable(getSnmpParam(cmcmib), CmAttribute.class);
        logger.info("cmAttributes:" + cmAttributes.size());
        // 取值仍然错误，跟测试数据有关系，测试数据有两条--不应该，因为测试数据就是两台设备
        List<MacDomainBaseInfo> macDomainBaseInfos = null;
        try {
            // macDomainBaseInfo = snmpExecutorService.getData(getSnmpParam(cmcmib),
            // MacDomainBaseInfo.class);//应该是唯一时使用，但是有两个cc即有两个mac
            macDomainBaseInfos = snmpExecutorService.getTable(getSnmpParam(cmcmib), MacDomainBaseInfo.class);// 由于上面那句出错，造成这句结果出错
        } catch (SnmpException e) {
            // 这里在macDomain中取值错误是由于在设置docsIfCmtsMacStorageType时设置了
            // docsIfCmtsMacStorageTypeString，但是该值并不存在
            // docsIfCmtsMacStorageTypeString这种值。为保证不出差，应先将String注释掉
            logger.error("error", e);
        }
        logger.info("macBaseInfo" + macDomainBaseInfo.toString());
        if (macDomainBaseInfos != null) {
            logger.info("" + macDomainBaseInfos.size());
            logger.info("" + macDomainBaseInfos.toString());
        }
        assertNotNull(macDomainBaseInfo);
    }

    @Test
    public void testCpeInfo() {
        List<CpeAttribute> cpeAttributes = snmpExecutorService.getTable(getSnmpParam(cmcmib), CpeAttribute.class);
        assertNotNull(cpeAttributes);
    }

    @Test
    public void testVlan() {
        SnmpUtil snmpUtil = new SnmpUtil(getSnmpParam(cmcmib));
        CmcVlanPrimaryInterface cmcVlanPrimaryInterface = snmpUtil.get(CmcVlanPrimaryInterface.class);
        if (cmcVlanPrimaryInterface != null) {
        }
        /*
         * String oid = "1.3.6.1.4.1.32285.11.1.1.2.7.3.1.2.1"; snmpUtil.set(oid, "172.16.17.214");
         * CmcVlanPrimaryIp cmcVlanPrimaryIp = new CmcVlanPrimaryIp();
         * cmcVlanPrimaryIp.setTopCcmtsVifPriIpVlanId(1);
         * cmcVlanPrimaryIp.setTopCcmtsVifPriIpAddr("172.16.17.214");
         * cmcVlanPrimaryIp.setTopCcmtsVifPriIpMask("255.255.255.0");
         * snmpUtil.set(cmcVlanPrimaryIp);
         */
        // List<CmcVlanPrimaryIp> cmcVlanPrimaryIps =
        // snmpUtil.getTable(CmcVlanPrimaryIp.class,true);
        // List<CmcDhcpBundle> cmcDhcpBundle = snmpUtil.getTable(CmcDhcpBundle.class,true);
        // List<CmAttribute> cmAttribute = snmpUtil.getTable(CmAttribute.class,true);
        // snmpExecutorService.getTable(getSnmpParam(cmcmib),CmcVlanPrimaryIp.class);
        // List<CmcQosServiceFlowStatus> cmcQosServiceFlowStatusList =
        // snmpExecutorService.getTable(getSnmpParam(cmcmib),CmcQosServiceFlowStatus.class);
        // }catch (Exception e) {
        // TODO: handle exception
        // e.printStackTrace();
        // }
    }

    @After
    public void tearAfterRun() {
        snmpExecutorService.destroy();
    }

}
