/***********************************************************************
 * $Id: OltVlanFacadeTest.java,v1.0 2011-10-27 下午04:33:45 $
 * 
 * @author: Administrator
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.facade;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.topvision.ems.epon.utils.EponUtil;
import com.topvision.ems.epon.vlan.domain.PortVlanAttribute;
import com.topvision.ems.epon.vlan.domain.VlanAggregationRule;
import com.topvision.ems.epon.vlan.domain.VlanAttribute;
import com.topvision.ems.epon.vlan.domain.VlanLlidAggregationRule;
import com.topvision.ems.epon.vlan.domain.VlanLlidQinQRule;
import com.topvision.ems.epon.vlan.domain.VlanLlidTranslationRule;
import com.topvision.ems.epon.vlan.domain.VlanLlidTrunkRule;
import com.topvision.ems.epon.vlan.domain.VlanQinQRule;
import com.topvision.ems.epon.vlan.domain.VlanTranslationRule;
import com.topvision.ems.epon.vlan.domain.VlanTrunkRule;
import com.topvision.ems.epon.vlan.facade.OltVlanFacade;
import com.topvision.framework.EnvironmentConstants;
import com.topvision.framework.snmp.RowStatus;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.domain.EngineServer;
import com.topvision.platform.facade.DubboFacadeFactory;
import com.topvision.platform.facade.FacadeFactory;

/**
 * @author Administrator
 * @created @2011-10-27-下午04:33:45
 * 
 */
public class OltVlanFacadeTest {
    private String ip = "172.10.10.20";
    private String roCommunity = "public";
    private String rwCommunity = "private";
    private String mib = "RFC1213-MIB,NSCRTV-EPONEOC-EPON-MIB,SUMA-EPONEOC-EPON-MIB";

    private OltVlanFacade getVlanFacade() {
        FacadeFactory facadeFactory = new DubboFacadeFactory();
        EngineServer engine = new EngineServer();
        engine.setIp("localhost");
        engine.setPort(3004);
        return facadeFactory.getFacade(engine, OltVlanFacade.class);
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
     * {@link com.topvision.ems.epon.facade.OltVlanFacade#getOltVlanAttributes(com.topvision.framework.snmp.SnmpParam)}
     * .
     */
    @Test
    public void testGetOltVlanAttributes() {
        /*
         * //List<OltVlanAttribute> attributes =
         * getVlanFacade().getOltVlanAttributes(getSnmpParam()); for (OltVlanAttribute attribute :
         * attributes) { System.out.println(attribute.getDeviceNo());
         * System.out.println(attribute.getMaxSupportVlans());
         * System.out.println(attribute.getMaxVlanId());
         * System.out.println(attribute.getCreatedVlanNumber()); }
         */
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.facade.OltVlanFacade#getSniVlanAttributes(com.topvision.framework.snmp.SnmpParam)}
     * .
     */
    @Test
    public void testGetSniVlanAttributes() {
        List<VlanAttribute> attributes = getVlanFacade().getSniVlanAttributes(getSnmpParam());
        for (VlanAttribute attribute : attributes) {
            System.out.println(attribute.getDeviceNo());
            System.out.println(attribute.getVlanIndex());
            System.out.println(attribute.getOltVlanName());
            System.out.println(attribute.getTaggedPort());
            System.out.println(EponUtil.getMibStringFromSniPortList(EponUtil.getSniPortIndexFromMib(attribute
                    .getTaggedPort())));
            System.out.println(attribute.getUntaggedPort());
        }
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.facade.OltVlanFacade#getVlanAggregationRules(com.topvision.framework.snmp.SnmpParam)}
     * .
     */
    @Test
    public void testGetVlanAggregationRules() {
        List<VlanAggregationRule> attributes = getVlanFacade().getVlanAggregationRules(getSnmpParam());
        for (VlanAggregationRule attribute : attributes) {
            // System.out.println(attribute.getDeviceNo());
            // System.out.println(attribute.getPortAggregationVidIndex());
            System.out.println(attribute.getAggregationVidList());
            List<Integer> list = new ArrayList<Integer>();
            list = EponUtil.getVlanListFromMib(attribute.getAggregationVidList());
            for (int i : list) {
                System.out.println("vlan " + i);
            }
            System.out.println(list.size());
            // System.out.println(EponUtil.getVlanBitMapFormList(list));
            // System.out.println(attribute.getPortNo());
            // System.out.println(attribute.getUntaggedPort());
        }
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.facade.OltVlanFacade#getVlanTranslationRules(com.topvision.framework.snmp.SnmpParam)}
     * .
     */
    @Test
    public void testGetVlanTranslationRules() {
        List<VlanTranslationRule> attributes = getVlanFacade().getVlanTranslationRules(getSnmpParam());
        for (VlanTranslationRule attribute : attributes) {
            // System.out.println(attribute.getDeviceNo());
            System.out.println(attribute.getTranslationNewVid());
            System.out.println(attribute.getVlanIndex());
            System.out.println(attribute.getPortNo());
            // System.out.println(attribute.getUntaggedPort());
        }
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.facade.OltVlanFacade#getVlanTrunkRules(com.topvision.framework.snmp.SnmpParam)}
     * .
     */
    @Test
    public void testGetVlanTrunkRules() {
        List<VlanTrunkRule> attributes = getVlanFacade().getVlanTrunkRules(getSnmpParam());
        for (VlanTrunkRule attribute : attributes) {
            // System.out.println(attribute.getDeviceNo());
            System.out.println(attribute.getTrunkVidList());
            // System.out.println(attribute.getVlanIndex());
            System.out.println(attribute.getPortNo());
            // System.out.println(attribute.getUntaggedPort());
        }
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.facade.OltVlanFacade#getVlanQinQRules(com.topvision.framework.snmp.SnmpParam)}
     * .
     */
    @Test
    public void testGetVlanQinQRules() {
        List<VlanQinQRule> attributes = getVlanFacade().getVlanQinQRules(getSnmpParam());
        for (VlanQinQRule attribute : attributes) {
            // System.out.println(attribute.getDeviceNo());
            System.out.println(attribute.getPqSVlanId());
            System.out.println(attribute.getPqStartVlanId());
            System.out.println(attribute.getPqEndVlanId());
            System.out.println(attribute.getPqSTagCosDetermine());
            System.out.println(attribute.getPqSTagCosNewValue());
            // System.out.println(attribute.getUntaggedPort());
        }
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.facade.OltVlanFacade#getVlanLlidTranslationRules(com.topvision.framework.snmp.SnmpParam)}
     * .
     */
    @Test
    public void testGetVlanLlidTranslationRules() {
        List<VlanLlidTranslationRule> attributes = getVlanFacade().getVlanLlidTranslationRules(getSnmpParam());
        for (VlanLlidTranslationRule attribute : attributes) {
            System.out.println(attribute.getOnuMac());
            System.out.println(attribute.getTopLlidTransVidIdx());
            System.out.println(attribute.getSlotNo());
            System.out.println(attribute.getPortNo());
            // System.out.println(attribute.getUntaggedPort());
        }
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.facade.OltVlanFacade#getVlanLlidTrunkRules(com.topvision.framework.snmp.SnmpParam)}
     * .
     */
    @Test
    public void testGetVlanLlidTrunkRules() {
        List<VlanLlidTrunkRule> attributes = getVlanFacade().getVlanLlidTrunkRules(getSnmpParam());
        for (VlanLlidTrunkRule attribute : attributes) {
            System.out.println(attribute.getOnuMac());
            System.out.println(attribute.getLlidVlanTrunkVidBmp());
            System.out.println(attribute.getSlotNo());
            System.out.println(attribute.getPortNo());
            // System.out.println(attribute.getUntaggedPort());
        }
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.facade.OltVlanFacade#getVlanLlidAggregationRules(com.topvision.framework.snmp.SnmpParam)}
     * .
     */
    @Test
    public void testGetVlanLlidAggregationRules() {
        List<VlanLlidAggregationRule> attributes = getVlanFacade().getVlanLlidAggregationRules(getSnmpParam());
        for (VlanLlidAggregationRule attribute : attributes) {
            System.out.println(attribute.getOnuMac());
            System.out.println(attribute.getLlidVlanBeforeAggVidList());
            System.out.println(attribute.getLlidVlanAfterAggVid());
            System.out.println(attribute.getSlotNo());
            System.out.println(attribute.getPortNo());
            // System.out.println(attribute.getUntaggedPort());
        }
    }

    /**
     * Test method for
     * {@link com.topvision.ems.epon.facade.OltVlanFacade#getvlanLlidQinQRules(com.topvision.framework.snmp.SnmpParam)}
     * .
     */
    @Test
    public void testGetvlanLlidQinQRules() {
        List<VlanLlidQinQRule> attributes = getVlanFacade().getvlanLlidQinQRules(getSnmpParam());
        for (VlanLlidQinQRule attribute : attributes) {
            System.out.println(attribute.getOnuMac());
            // System.out.println(attribute.get);
            // System.out.println(attribute.getLlidVlanAfterAggVid());
            // System.out.println(attribute.getSlotNo());
            System.out.println(attribute.getPortNo());
            // System.out.println(attribute.getUntaggedPort());
        }
    }

    @Test
    public void testAddTransRule() {
        VlanTranslationRule vlanTranslationRule1 = new VlanTranslationRule();
        // vlanTranslationRule1.setPortId(8L);
        // vlanTranslationRule1.setEntityId(30000000000L);
        vlanTranslationRule1.setPortIndex(17196646400l);
        vlanTranslationRule1.setTranslationNewVid(11);
        vlanTranslationRule1.setVlanIndex(12);
        vlanTranslationRule1.setTranslationRowStatus(RowStatus.CREATE_AND_GO);
        getVlanFacade().addTransRule(getSnmpParam(), vlanTranslationRule1);
    }

    @Test
    public void testModifyVlanName() {
        VlanAttribute vlanAttribute = new VlanAttribute();
        vlanAttribute.setDeviceNo(1l);
        vlanAttribute.setVlanIndex(1);
        vlanAttribute.setOltVlanName("hello");
        getVlanFacade().modifyVlanName(getSnmpParam(), vlanAttribute);

    }

    @Test
    public void testAddOltVlan() {
        VlanAttribute vlanAttribute = new VlanAttribute();
        vlanAttribute.setVlanIndex(102);
        vlanAttribute.setOltVlanName("hello 102");
        vlanAttribute.setOltVlanRowStatus(RowStatus.CREATE_AND_GO);
        getVlanFacade().addOltVlan(getSnmpParam(), vlanAttribute);
    }

    @Test
    public void testDeleteOltVlan() {
        VlanAttribute vlanAttribute = new VlanAttribute();
        vlanAttribute.setVlanIndex(10);
        vlanAttribute.setOltVlanRowStatus(RowStatus.DESTORY);
        getVlanFacade().deleteOltVlan(getSnmpParam(), vlanAttribute);

    }

    @Test
    public void testModifyPortVlanAttributes() {
        PortVlanAttribute portVlanAttribute = new PortVlanAttribute();
        portVlanAttribute.setDeviceIndex(16843008l);
        portVlanAttribute.setPortNo(0l);
        portVlanAttribute.setSlotNo(1l);
        portVlanAttribute.setVlanTagPriority(5);
        portVlanAttribute.setVlanPVid(100);
        getVlanFacade().modifyPortVlanAttributes(getSnmpParam(), portVlanAttribute);

    }

    @Test
    public void testAddTrunkRule() {
        VlanTrunkRule vlanTrunkRule = new VlanTrunkRule();
        vlanTrunkRule.setPortIndex(12901744898l);
        List<Integer> list = new ArrayList<Integer>();
        vlanTrunkRule.setTrunkVidListAfterSwitch(list);
        System.out.println(vlanTrunkRule.getTrunkVidList());
        getVlanFacade().modifyTrunkRule(getSnmpParam(), vlanTrunkRule);
        List<VlanTrunkRule> attributes = getVlanFacade().getVlanTrunkRules(getSnmpParam());
    }

    @Test
    public void testDeleteTransRule() {
        VlanTranslationRule vlanTranslationRule = new VlanTranslationRule();
        vlanTranslationRule.setEntityId(30000000000L);
        vlanTranslationRule.setDeviceIndex(16974080l);
        vlanTranslationRule.setSlotNo(1l);
        vlanTranslationRule.setPortNo(0l);
        System.out.println(vlanTranslationRule.getSlotNo());
        System.out.println(vlanTranslationRule.getPortNo());
        getVlanFacade().deleteTransRule(getSnmpParam(), vlanTranslationRule);
    }

    @Test
    public void testDeleteTrunkRule() {
        VlanTrunkRule trunkRule = new VlanTrunkRule();
        trunkRule.setDeviceIndex(16777217l);
        trunkRule.setSlotNo(1l);
        trunkRule.setPortNo(1l);
        getVlanFacade().deleteTrunkRule(getSnmpParam(), trunkRule);
    }

    @Test
    public void testDeleteTopTrunkRule() {
        VlanLlidTrunkRule vlanLlidTrunkRule = new VlanLlidTrunkRule();
        vlanLlidTrunkRule.setSlotNo(3l);
        vlanLlidTrunkRule.setPortNo(1l);
        //vlanLlidTrunkRule.setOnuMac("1.2.3.4.5.6");
        getVlanFacade().deleteLlidTrunkRule(getSnmpParam(), vlanLlidTrunkRule);
    }

    @Test
    public void testAddAggRule() {
        VlanAggregationRule aggregationRule = new VlanAggregationRule();
        aggregationRule.setPortIndex(12901744898L);
        aggregationRule.setPortAggregationVidIndex(22);
        List<Integer> list = new ArrayList<Integer>();
        list.add(10);
        list.add(11);
        list.add(12);
        list.add(13);
        list.add(14);
        list.add(15);
        aggregationRule.setAggregationVidListAfterSwitch(list);
        getVlanFacade().addSvlanAggrRule(getSnmpParam(), aggregationRule);
    }

    @Test
    public void testAddTopAggRule() {
        VlanLlidAggregationRule rule = new VlanLlidAggregationRule();
        rule.setLlidVlanAfterAggVid(4);
        rule.setLlidVlanAggCosMode(1);
        rule.setLlidVlanAggNewCos(0);
        List<Integer> list = new ArrayList<Integer>();
        list.add(100);
        list.add(101);
        list.add(102);
        list.add(103);
        list.add(104);
        rule.setLlidVlanBeforeAggVidListAfterSwitch(list);
        rule.setLlidVlanAggNewTpid(8100);
        byte[] bs = { 1, 2, 3, 4, 5, 6 };
        String mac = new String(bs);
        System.out.println(mac);
        //rule.setOnuMac("1.2.3.4.5.6");
        rule.setSlotNo(3l);
        rule.setPortNo(1l);
        getVlanFacade().addLlidSvlanAggrRule(getSnmpParam(), rule);

    }

    @Test
    public void testDeleteTopQinQ() {
        VlanLlidQinQRule rule = new VlanLlidQinQRule();
        rule.setSlotNo(0l);
        rule.setPortNo(1l);
        //rule.setOnuMac("0.0.0.0.0.0");
        rule.setTopLqVlanStartCVid(23);
        rule.setTopLqVlanEndCVid(32);
        getVlanFacade().deleteLlidQinQRule(getSnmpParam(), rule);

    }

}
