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
import com.topvision.ems.cmc.config.facade.domain.CmcEmsConfig;
import com.topvision.ems.cmc.dhcp.facade.domain.CmcDhcpBundle;
import com.topvision.ems.cmc.dhcp.facade.domain.CmcDhcpGiAddr;
import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.cmc.facade.domain.CpeAttribute;
import com.topvision.ems.cmc.facade.domain.DocsDevEvControl;
import com.topvision.ems.cmc.macdomain.facade.domain.MacDomainBaseInfo;
import com.topvision.ems.cmc.topology.domain.CmcDiscoveryData;
import com.topvision.framework.EnvironmentConstants;
import com.topvision.framework.exception.engine.SnmpException;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.snmp.SnmpUtil;
public class CmcConfigServiceImplTest {
    private String ip = "172.16.17.46";
    private String roCommunity = "public";
    private String rwCommunity = "private";
    private String cmcmib = "RFC1213-MIB,DOCS-IF-MIB,DOCS-QOS-MIB,DOCS-SUBMGT-MIB,TOPVISION-CCMTS-MIB,DOCS-CABLE-DEVICE-MIB";
    private String eponmib = "RFC1213-MIB,NSCRTV-EPONEOC-EPON-MIB,SUMA-EPONEOC-EPON-MIB";
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private SnmpExecutorService snmpExecutorService;
    private SnmpUtil snmpUtil;

    private SnmpParam getSnmpParam(String mib) {
        EnvironmentConstants.putEnv(EnvironmentConstants.MIB_HOME, "webapp/WEB-INF/mibs");
        SnmpParam param = new SnmpParam();
        param.setIpAddress(ip);
        param.setCommunity(roCommunity);
        param.setWriteCommunity(rwCommunity);
        param.setMibs(mib);
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
        snmpUtil = new SnmpUtil(getSnmpParam(cmcmib));
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
    public void testmodifyCmcEms() {
        /*
         * List<CmcEmsConfig> cmcEmsConfigs = snmpUtil.getTable( CmcEmsConfig.class, true);
         * System.out.println(cmcEmsConfigs);
         */
        /*
         * List<DocsDevEvControl> docsDevEvControls = snmpUtil.getTable(DocsDevEvControl.class,
         * true); System.out.println(docsDevEvControls);
         */
        List<DocsDevEvControl> docsDevEvControls = snmpUtil.getTable(DocsDevEvControl.class, true);
        System.out.println(docsDevEvControls);
        DocsDevEvControl docsDevEvControl = new DocsDevEvControl();
        docsDevEvControl.setDocsDevEvPriority(2);
        docsDevEvControl.setDocsDevEvReporting("0x608");
        // DocsDevEvControl docsDevEvControlAfter =
        // snmpExecutorService.setData(getSnmpParam(cmcmib), docsDevEvControl);
        // //(getSnmpParam(cmcmib), docsDevEvControl);
        snmpExecutorService.set(getSnmpParam(cmcmib), "1.3.6.1.2.1.69.1.5.7.1.2.1", "0xE8:80");
        // snmpExecutorService.set(getSnmpParam(cmcmib), "1.3.6.1.2.1.69.1.5.7.1.2.1", "0x80");

        // System.out.println(docsDevEvControlAfter);
        /*
         * CmcEmsConfig cmcEmsConfig = new CmcEmsConfig(); cmcEmsConfig.setDocsDevNmAccessIndex(1);
         * cmcEmsConfig.setTopCcmtsNmAccessIp("192.165.53.28");
         * snmpExecutorService.setData(getSnmpParam(cmcmib), cmcEmsConfig);
         */
        // CmcEmsConfig cmcEmsConfigAfterModified = this.modifyCmcEmsConfig(getSnmpParam(cmcmib),
        // cmcEmsConfig);
        // logger.info("set CmcEmsConfig end"+cmcEmsConfigAfterModified);
        // assertNotNull(cmcEmsConfigAfterModified);
    }

    private CmcDiscoveryData discovery(SnmpExecutorService snmpExecutorService, SnmpParam param) {
        return null;
    }

    public CmcEmsConfig modifyCmcEmsConfig(SnmpParam snmpParam, CmcEmsConfig cmcEmsConfig) {
        // TODO Auto-generated method stub
        return snmpExecutorService.setData(snmpParam, cmcEmsConfig);
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

    @After
    public void tearAfterRun() {
        snmpExecutorService.destroy();
    }

    @Test
    public void testOids() {
        List<CmcDhcpBundle> cmcDhcpBundles = snmpUtil.getTable(CmcDhcpBundle.class, true);
        System.out.println(cmcDhcpBundles);
        /* System.out.println(snmpUtil.getNext("1.3.6.1.4.1.32285.11.1.1.2.3.1.5")); */
        /*
         * System.out.println(snmpUtil.getTableWithIndex(new String[] {
         * "1.3.6.1.4.1.32285.11.1.1.2.3.2.1.1", "1.3.6.1.4.1.32285.11.1.1.2.3.2.1.2",
         * "1.3.6.1.4.1.32285.11.1.1.2.3.2.1.3" }));
         */

        List<CmcDhcpGiAddr> cmcDhcpOption60s = snmpUtil.getTable(CmcDhcpGiAddr.class, true);
        System.out.println(cmcDhcpOption60s);
        /*
         * List<OltFileAttribute> cmcDhcpGiAddrs = snmpUtil.getTable(OltFileAttribute.class,true);
         * System.out.println(cmcDhcpGiAddrs);
         */
        /*
         * List<CmcDhcpServerConfig> cmcDhcpServerConfigs =
         * snmpUtil.getTable(CmcDhcpServerConfig.class,true);
         * System.out.println(cmcDhcpServerConfigs);
         */

        /*
         * List<CmcAttribute> cmcDhcpBund = snmpUtil.getTable(CmcAttribute.class,true);
         * System.out.println(cmcDhcpBund);
         */
    }

}