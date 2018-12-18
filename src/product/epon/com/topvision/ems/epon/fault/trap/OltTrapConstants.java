/***********************************************************************
 * $Id: OltTrapConstants.java,v1.0 2012-1-18 上午09:49:59 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.fault.trap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.topvision.ems.fault.parser.TrapConstants;

/**
 * @author Victor
 * @created @2012-1-18-上午09:49:59
 * 
 */
public class OltTrapConstants extends TrapConstants implements java.io.Serializable {
    private static final long serialVersionUID = 2006778374199724004L;
    // olt Trap类型，一种是事件，一种是告警
    public final static String OLT_TRAP_TYPE_ALERT = "1.3.6.1.4.1.17409.2.2.11.1.1.1";
    public final static String OLT_TRAP_TYPE_EVENT = "1.3.6.1.4.1.17409.2.2.11.1.1.2";

    // Trap实体各个字段定义
    public final static String TRAP_OBJECT_INSTANCE = "1.3.6.1.4.1.17409.2.2.11.1.2.1.0";
    public final static String TRAP_OBJECT_CORRELATIONID = "1.3.6.1.4.1.17409.2.2.11.1.2.2.0";
    public final static String TRAP_OBJECT_ADDITIONALTEXT = "1.3.6.1.4.1.17409.2.2.11.1.2.3.0";
    public final static String TRAP_OBJECT_CODE = "1.3.6.1.4.1.17409.2.2.11.1.2.4.0";
    public final static String TRAP_OBJECT_SEVERITY = "1.3.6.1.4.1.17409.2.2.11.1.2.5.0";
    public final static String TRAP_OBJECT_OCCURTIME = "1.3.6.1.4.1.17409.2.2.11.1.2.6.0";
    public final static String TRAP_OBJECT_SEQUENCENUMBER = "1.3.6.1.4.1.17409.2.2.11.1.2.7.0";

    // 广电告警等级定义
    public final static byte TRAP_SEVERITY_CRITICAL = 1;
    public final static byte TRAP_SEVERITY_MAJOR = 2;
    public final static byte TRAP_SEVERITY_MINOR = 3;
    public final static byte TRAP_SEVERITY_WARNING = 4;
    public final static byte TRAP_SEVERITY_INFO = 5;

    public final static Map<Integer, Integer> event2Alert = new HashMap<Integer, Integer>();

    // EPON告警ID转换 KEY为CREATE ALERT ID VALUE为CLEAR ALERT ID
    static {
        event2Alert.put(4103, 4104);
        event2Alert.put(4105, 4104);
        event2Alert.put(4107, 4108);
        event2Alert.put(4111, 4112);
        event2Alert.put(4117, 4118);
        event2Alert.put(4119, 4120);
        event2Alert.put(4121, 4122);
        event2Alert.put(4125, 4126);
        event2Alert.put(4130, 4131);
        event2Alert.put(8193, 8194);
        event2Alert.put(12293, 12294);
        event2Alert.put(12305, 12304);
        event2Alert.put(12306, 12304);
        event2Alert.put(12307, 12303);
        event2Alert.put(12308, 12303);
        event2Alert.put(12325, 12326);
        event2Alert.put(12327, 12328);
        event2Alert.put(16393, 16300);
        event2Alert.put(16432, 16431);
        event2Alert.put(16423, 16425);
        event2Alert.put(16424, 16425);
        event2Alert.put(16435, 16434);
        event2Alert.put(20483, 20484);
        event2Alert.put(20485, 20486);
        event2Alert.put(20481, 20482);
        event2Alert.put(16389, 53249);
        event2Alert.put(16390, 53249);
        event2Alert.put(16391, 53250);
        event2Alert.put(16392, 53250);
        event2Alert.put(19, 53251);
        event2Alert.put(21, 53252);
        event2Alert.put(5126, 53253);
        event2Alert.put(5382, 53253);
        event2Alert.put(5128, 53254);
        event2Alert.put(5384, 53254);
        event2Alert.put(5130, 53255);
        event2Alert.put(5386, 53255);
        event2Alert.put(5638, 53256);
        event2Alert.put(5894, 53256);
        event2Alert.put(5640, 53257);
        event2Alert.put(5896, 53257);
        event2Alert.put(5642, 53258);
        event2Alert.put(5898, 53258);
        event2Alert.put(28673, 53259);
        event2Alert.put(4132, 53261);

        // ONU CATV
        event2Alert.put(993, 53400);
        event2Alert.put(994, 53400);
        event2Alert.put(995, 53401);
        event2Alert.put(996, 53401);
        event2Alert.put(997, 53402);
        event2Alert.put(998, 53402);
        event2Alert.put(999, 53403);
        event2Alert.put(1000, 53403);
        event2Alert.put(6145, 53260);
        event2Alert.put(12299, 53262);
        event2Alert.put(16437, 53263);

        // OFA ALARM THRESHOLD
        event2Alert.put(4133, 54400);
        event2Alert.put(4134, 54401);
        event2Alert.put(4135, 54402);
        event2Alert.put(4136, 54403);
        event2Alert.put(4137, 54404);
        event2Alert.put(4138, 54405);
        event2Alert.put(4139, 54406);
    }

    // 记录需要处理的OLT上报的ONU上下线相关事件和告警ID
    public static final List<Integer> onuParseEvent = new ArrayList<Integer>();

    static {
        onuParseEvent.add(16433);
        onuParseEvent.add(116433);
        onuParseEvent.add(16435);
        onuParseEvent.add(321);
        onuParseEvent.add(116435);
        onuParseEvent.add(16434);
        onuParseEvent.add(116434);
        onuParseEvent.add(310);
        onuParseEvent.add(2);
        onuParseEvent.add(16393);
        onuParseEvent.add(16439);
        onuParseEvent.add(116439);
        onuParseEvent.add(258);
        onuParseEvent.add(13002);
        onuParseEvent.add(61698);
        onuParseEvent.add(1003);
        onuParseEvent.add(16437);
        onuParseEvent.add(322);
        onuParseEvent.add(1027);
        onuParseEvent.add(101027);
        onuParseEvent.add(1025);
        onuParseEvent.add(101025);
    }

    public static final List<Integer> onuCatvEvents = new ArrayList<>();

    static {
        onuCatvEvents.add(993);
        onuCatvEvents.add(994);
        onuCatvEvents.add(995);
        onuCatvEvents.add(996);
        onuCatvEvents.add(997);
        onuCatvEvents.add(998);
        onuCatvEvents.add(999);
        onuCatvEvents.add(1000);
    }

    // CCMTS断电事件
    public static final int CCMTS_LINK_LOSE = 1003;
    // ONU断电告警
    public static final int ONU_PWR_OFF = 16393;
    // ONU删除事件
    public static final int ONU_DELETE = 16439;
    // CMTS删除事件
    public static final int CMTS_DELETE = 1016439;
    // ONU下线事件
    public static final int ONU_OFFLINE = 16433;
    // ONU断纤事件
    public static final int ONU_FIBER_BREAK = 16435;
    // ONU上线事件
    public static final int ONU_ONLINE = 16434;

    // GPON ONU下线事件
    public static final int GPON_ONU_OFFLINE = 1027;
    // GPON ONU上线事件
    public static final int GPON_ONU_ONLINE = 1025;

    // 记录ONU下线相关事件和告警ID
    // 主要包括ONU下线、断纤、断电、解绑定(删除)、解注册
    // CMTS断电,经过转换的CMTS下线和CMTS断纤
    public static final List<Integer> ONU_EVENT_OFFLINE = new ArrayList<Integer>();

    static {
        ONU_EVENT_OFFLINE.add(16433);
        ONU_EVENT_OFFLINE.add(16435);
        ONU_EVENT_OFFLINE.add(16393);
        ONU_EVENT_OFFLINE.add(258);
        ONU_EVENT_OFFLINE.add(101003);
        ONU_EVENT_OFFLINE.add(1016433);
        ONU_EVENT_OFFLINE.add(1016435);
    }

    // 记录ONU下线相关事件和告警ID,包括ONU上线和转换后的CMTS上线
    public static final List<Integer> ONU_EVENT_ONLINE = new ArrayList<Integer>();

    static {
        ONU_EVENT_ONLINE.add(16434);
        ONU_EVENT_ONLINE.add(1016434);
    }
}
