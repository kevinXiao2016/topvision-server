/***********************************************************************
 * $Id: SnmpUtilTest.java,v1.0 2011-10-9 下午04:45:33 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.ems.epon.fault.domain.OltTrapConfig;
import com.topvision.ems.epon.olt.domain.OltFileAttribute;
import com.topvision.ems.epon.olt.domain.OltSlotAttribute;
import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.gpon.profile.facade.domain.GponTrafficProfileInfo;
import com.topvision.framework.EnvironmentConstants;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.snmp.SnmpUtil;

/**
 * @author Victor
 * @created @2011-10-9-下午04:45:33
 * 
 */
public class SnmpUtilTest {
    public static final Pattern oidPattern = Pattern.compile("[.[0-9]++]++");
    private static Logger logger = LoggerFactory.getLogger(SnmpUtilTest.class);
    private static SnmpUtil snmpUtil;

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        String ip = "172.17.10.213";
        String roCommunity = "public";
        String rwCommunity = "private";
        String mib = "RFC1213-MIB,NSCRTV-EPONEOC-EPON-MIB,NSCRTV-EPONEOC-GPON-MIB,SUMA-EPONEOC-EPON-MIB,SNMP-VIEW-BASED-ACM-MIB,SNMP-USER-BASED-SM-MIB,SNMP-TARGET-MIB,SNMP-NOTIFICATION-MIB,TOPVISION-CCMTS-MIB,DOCS-IF3-MIB";
        EnvironmentConstants.putEnv(EnvironmentConstants.MIB_HOME, "webapp/WEB-INF/mibs");
        SnmpParam param = new SnmpParam();
        param.setIpAddress(ip);
        param.setCommunity(roCommunity);
        param.setWriteCommunity(rwCommunity);
        param.setMibs(mib);
        snmpUtil = new SnmpUtil(param);
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        snmpUtil.releaseResources();
    }

    @Test
    public void testGetTable() {
        System.out.println("{{URL}}".replaceAll("\\{\\{URL\\}\\}", "ab"));
        List<String> list = new ArrayList<>();
        long t1 = System.currentTimeMillis();
        //List<?> onuCapabilities = snmpUtil.getTable(GponTrafficProfileInfo.class, true, list);
        //System.out.println(onuCapabilities);
        /* List<OltTopOnuProductTable> attributes = snmpUtil.getTable(OltTopOnuProductTable.class, true, list);*/
        /*CmcTrapServer trap = new CmcTrapServer();
        trap.setTopCcmtsTrapServerIndex(2);
        trap.setTopCcmtsTrapServerIpAddr("172.17.21.168");
        trap.setTopCcmtsTrapServerStatus(2);
        snmpUtil.set(trap);*/
        /*  System.out.println(System.currentTimeMillis()-t1);
          List<CameraFilterTable> trapServerList = snmpUtil.getTable(CameraFilterTable.class, true);
          System.out.println(trapServerList);*/

        /* com.topvision.ems.Version emsVersion = new com.topvision.ems.Version();
         System.out.println(emsVersion.getBuildVersion());

         System.out.println(DateUtils.FULL_S_FORMAT.format(new Date()));
         OltAuthenSnInfo snInfo = new OltAuthenSnInfo();
         List<OltAuthenSnInfo> snInfos = new ArrayList<OltAuthenSnInfo>();
        // snInfos = snmpUtil.getTable(OltAuthenSnInfo.class, true);
         System.out.println(snInfos.size());*/

        /*
         * VacmSnmpV3View view = new VacmSnmpV3View(); view.setSnmpViewName("view2");
         * view.setSnmpViewSubtreeOid("1.0.6"); view.setSnmpViewMask("010");
         * view.setSnmpViewMode(1); view.setSnmpViewStorageType(1); view.setSnmpViewStatus(4);
         * snmpUtil.set(view);
         */

        // OltSlotAttribute slot = new OltSlotAttribute();
        // slot.setTopSysBdSlotNo(2L);
        // slot.setTopSysBdActualType(1);
        // slot.setTopSysBdPreConfigType(1);
        // slot = snmpUtil.set(slot);
        // logger.debug(slot.toString());
    }

    public void testOnuTable() {
        List<OltOnuAttribute> onus = snmpUtil.getTable(OltOnuAttribute.class, true);
        assertNotNull(onus);
        logger.debug("testOnuTable:{}", onus);
    }

    public void testTrap() {
        List<OltTrapConfig> traps = snmpUtil.getTable(OltTrapConfig.class, true);
        assertNotNull(traps);
        assertEquals(1, traps.size());
        logger.debug(traps.toString());
    }

    public void testGetTableLines() {
        OltFileAttribute fileAttribute = new OltFileAttribute();
        String filePath = "/tffs0/";
        String fileNameOid = filePath.length() + ".";
        for (char a : filePath.toCharArray()) {
            fileNameOid += (int) a + ".";
        }
        String fileName = "1.rar";
        String filePathOid = fileName.length() + ".";
        for (char a : fileName.toCharArray()) {
            filePathOid += (int) a + ".";
        }
        logger.debug(fileNameOid);
        logger.debug(filePathOid);
        fileAttribute.setFilePath(fileNameOid.substring(0, fileNameOid.length() - 1));
        fileAttribute.setFileName(filePathOid.substring(0, filePathOid.length() - 1));
        logger.info(fileAttribute.toString());
        fileAttribute = snmpUtil.getTableLine(fileAttribute);
        logger.debug(fileAttribute.getFileSize().toString());
    }

    public void testGet() {
        Long entityId = 3000000000L;
        List<String> list = EponUtil.getFileNameFromEntityId(entityId);
        for (String s : list) {
            System.out.println(s);
        }
        /*
         * OltAttribute attr = snmpUtil.get(OltAttribute.class); String s =
         * snmpUtil.get("1.3.6.1.4.1.17409.2.3.1.2.1.1.5.1"); // 把启动时长换算为服务器的时刻，以便界面显示启动时长
         * logger.info(attr.getOltDeviceUpTime()+""); logger.info(s); List<EponEventLog> list =
         * snmpUtil.getTable(EponEventLog.class, true); for(EponEventLog log : list){
         * System.out.println(System.currentTimeMillis()); logger.info(log.toString()); }
         */
        OltSlotAttribute slotAttribute = new OltSlotAttribute();
        slotAttribute.setDeviceNo(1l);
        slotAttribute.setSlotNo(3l);
        slotAttribute = snmpUtil.getTableLine(slotAttribute);
        System.out.println(slotAttribute.getSlotIndex());
    }
}
