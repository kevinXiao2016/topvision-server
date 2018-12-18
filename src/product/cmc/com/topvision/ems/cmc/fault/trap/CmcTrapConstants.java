/***********************************************************************
 * $Id: CmcTrapConstants.java,v1.0 2012-4-10 下午04:10:58 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.fault.trap;

import com.topvision.ems.fault.parser.TrapConstants;
import com.topvision.platform.zetaframework.var.ZetaValueGetter;

/**
 * @author loyal
 * @created @2012-4-10-下午04:10:58
 * 
 */
@ZetaValueGetter("CmcTrapConstants")
public class CmcTrapConstants extends TrapConstants implements java.io.Serializable {
    private static final long serialVersionUID = 5596501438228695872L;
    // 除信道上下线外，其它都是用该节点作为CC告警的标志id
    public final static String CCMTS_DOMAIN_TRAP_TYPE = "1.3.6.1.4.1.4491.2.1.20.0.1.0";
    // CC告警的报文格式使用dev event表，index如下
    public final static String CCMTS_DEVEVENT_INDEX = "1.3.6.1.2.1.69.1.5.8.1.1.0";
    // CC告警的报文格式中用于获取CCMTS mac地址的节点
    public final static String CCMTS_DEVEVENT_TEXT = "1.3.6.1.2.1.69.1.5.8.1.7.0";
    // cc告警的報文格式中用于获取 告警的eventId
    public final static String CCMTS_EVENT_ID = "1.3.6.1.2.1.69.1.5.8.1.6.0";

    // 标示用epon告警节点标示
    // public final static String CCMTS_CHANNEL_LINK_BY_EPON = "1.3.6.1.4.1.17409.2.2.11.1.1.1";
    // linkup,linkdown,coldstart,warmstart等节点标识
    public final static String CCMTS_CHANNEL_COMMON_ALERT = "1.3.6.1.6.3.1.1.5";

    // 处理CC断电告警
    public final static String TRAP_OBJECT_INSTANCE = "1.3.6.1.4.1.17409.2.2.11.1.2.1.0";
    public final static String TRAP_OBJECT_CORRELATIONID = "1.3.6.1.4.1.17409.2.2.11.1.2.2.0";
    public final static String TRAP_OBJECT_ADDITIONALTEXT = "1.3.6.1.4.1.17409.2.2.11.1.2.3.0";
    public final static String TRAP_OBJECT_CODE = "1.3.6.1.4.1.17409.2.2.11.1.2.4.0";
    public final static String TRAP_OBJECT_SEVERITY = "1.3.6.1.4.1.17409.2.2.11.1.2.5.0";
    public final static String TRAP_OBJECT_OCCURTIME = "1.3.6.1.4.1.17409.2.2.11.1.2.6.0";
    public final static String TRAP_OBJECT_SEQUENCENUMBER = "1.3.6.1.4.1.17409.2.2.11.1.2.7.0";
    public final static long CC_CLOSE_DOWNSTREAM = 1011L;
    public final static long CC_OPEN_DOWNSTREAM = 1012L;
    public final static long CC_CLOSE_UPSTREAM = 1013L;
    public final static long CC_OPEN_UPSTREAM = 1014L;

    // 跳频
    public final static long CC_SPECTRUM_GP_HOP = 1036L;
    public final static long CC_SPECTRUM_GP_HOP_RECOVERY = 1035L;
    public final static String SPECTRUM_HOP_RECOVERY = "recovery";

    //CCMTS下线事件ID(网管维护)
    public static final int CMC_OFFLINE = 1016433;
    // CCMTS上线事件ID(网管维护)
    public static final int CMC_ONLINE = 1016434;
    // CCMTS断纤事件ID(网管维护)
    public static final int CMC_FIBER_BREAK = 1016435;
    // CCMTS解绑定事件ID(网管维护)
    public static final int CMC_DELETE = 1016439;
    // CCMTS断电事件ID(网管维护)
    public static final int CMC_POWER_OFF = 101003;

    public static final int CMC_RESET = 101025;
    public static final int CMC_RESTART = 101029;

    public static final int CMC_US_OPEN = 101014;
    public static final int CMC_US_CLOSE = 101013;
    public static final int CMC_US_PARAM = 101015;
    public static final int CMC_DS_OPEN = 101012;
    public static final int CMC_DS_CLOSE = 101011;
    public static final int CMC_DS_PARAM = 101010;
    public static final int CMC_CHANNEL_ONLINE = 102005;
    public static final int CMC_CHANNEL_OFFLINE = 102006;
    
    public static final int CMC_REONLINE = 101002;


 
}
