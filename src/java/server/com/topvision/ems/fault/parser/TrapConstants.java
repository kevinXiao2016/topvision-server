/***********************************************************************
 * $Id: TrapConstants.java,v1.0 2012-1-18 上午09:58:50 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.fault.parser;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Victor
 * @created @2012-1-18-上午09:58:50
 * 
 */
public class TrapConstants {
    public final static String TRAP_TYPE_OID = "1.3.6.1.6.3.1.1.4.1.0";
    public final static String TRAP_IFINDEX_OID = "1.3.6.1.2.1.2.2.1.1.0";
    public final static String TRAP_COLDSTART = "1.3.6.1.6.3.1.1.5.1.0";
    public final static String TRAP_LINKUP = "1.3.6.1.6.3.1.1.5.4.0";
    public final static String TRAP_LINKDOWN = "1.3.6.1.6.3.1.1.5.3.0";
    public final static String TRAP_SYSUPTIME = "1.3.6.1.2.1.1.3.0";
    public final static String TRAP_IFINDEX = "1.3.6.1.2.1.2.2.1.1";
    public final static String AP_DAEMON_TRAP_OID = "1.3.6.1.4.1.32285.11.4.100";
    public final static String AP_DAEMON_TRAP_VALUE = "1.3.6.1.4.1.32285.11.4.100.1.1";
    //标识CC告警
    public final static String CCMTS_TRAP_TYPE = "1.3.6.1.4.1.4491.2.1.20.0.1.0";
    //cc告警的報文格式中用于获取 告警的eventId
    public final static String CCMTS_TRAP_CODE = "1.3.6.1.2.1.69.1.5.8.1.6.0";
    //CC告警的报文格式中用于获取CCMTS mac地址的节点
    public final static String CCMTS_MAC_NODE = "1.3.6.1.2.1.69.1.5.8.1.7.0";

    public final static List<Integer> CCMTS_ALETR_LIST = new ArrayList<Integer>();
    static {
        CCMTS_ALETR_LIST.add(1016433);
        CCMTS_ALETR_LIST.add(1016435);
        CCMTS_ALETR_LIST.add(101003);
    }
}
