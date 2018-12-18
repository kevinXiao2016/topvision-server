/***********************************************************************
 * $Id: IgmpConstants.java,v1.0 2016年7月7日 上午11:48:22 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.igmpconfig;

import java.util.ArrayList;
import java.util.List;

import com.topvision.framework.utils.EponConstants;
import com.topvision.platform.zetaframework.var.ZetaValueGetter;

/**
 * @author flack
 * @created @2016年7月7日-上午11:48:22
 * IGMP业务相关常量
 */
@ZetaValueGetter("IgmpConstants")
public class IgmpConstants {
    public static final String IGMP_GROUP_CALLBACK_HANDLER = "IGMP.GROUP.CALLBACK";
    public static final int IGMP_PORTTYPE_INVALID = 1;
    public static final int IGMP_PORTTYPE_GE = 3;
    public static final int IGMP_PORTTYPE_XE = 4;
    public static final int IGMP_PORTTYPE_EPON = 5;
    public static final int IGMP_PORTTYPE_GPON = 6;
    public static final int IGMP_PORTTYPE_EPON10G = 7;
    public static final int IGMP_PORTTYPE_UNI = 11;
    public static final int IGMP_DEFAULT_PORTFLAG = 0;
    public static final int IGMP_CDRREPORT_NO = 0;

    public static final int IGMP_MODE_PROXY = 1;
    public static final int IGMP_MODE_ROUTER = 2;
    public static final int IGMP_MODE_DISABLE = 3;
    public static final int IGMP_MODE_SNOOPING = 4;
    //标识IGMP管理中用到的GE口类型
    public static final List<Integer> SNI_GE_TYPES = new ArrayList<Integer>();
    static {
        SNI_GE_TYPES.add(EponConstants.SNI_PORT_TYPE_GEPORT);
        //SNI_GE_TYPES.add(EponConstants.SNI_PORT_TYPE_GEFIBER);
    }
    //标识IGMP管理中用到的XE口类型
    public static final List<Integer> SNI_XE_TYPES = new ArrayList<Integer>();
    static {
        SNI_XE_TYPES.add(EponConstants.SNI_PORT_TYPE_XEPORT);
    }

    public static final int UNCONFIG_FLAG = -1;

    public static final int IGMP_VERSION_V1 = 1;
    public static final int IGMP_VERSION_V2 = 2;
    public static final int IGMP_VERSION_V3 = 3;
    public static final int IGMP_VERSION_V3ONLY = 4;

    public static final int IGMP_JOINMODE_JOIN = 1;

    public static final int SUCCESS = 0;
    public static final int IGMP_GROUP_ADD_ERROR = 1;
    public static final int IP_ADDRESS_ERROR = 2;
    public static final int FORMAT_FAILD = 3;
    public static final Integer GROUPID_OVERFLOW = 4;
    public static final Integer VLANID_OVERFLOW = 5;
    public static final Integer SOURCE_IP_ERROR = 6;
    public static final Integer MULTICAST_IPADDRESS_ERROR = 7;
    public static final Integer DESC_ERROR = 8;
    public static final Integer ALIAS_ERROR = 9;
    public static final Integer MAX_BW_ERROR = 10;
    public static final Integer PREJOIN_ERROR = 11;
    public static final Integer SOURCEIP_NULL_ERROR = 12;
    public static final Integer VLANID_NOT_EXIST = 13;
    public static final Integer V3ONLY_NULL_ERROR = 14;

    public static final Integer PREJOIN = 1;
    public static final Integer NO_PREJOIN = 2;

    public static final Integer BIND_FAILD = -1;
    public static final Integer EXCEL_ERROR = -2;
    public static final Integer OTHER_ERROR = -3;
    public static final String REFRESH_GLOBAL_GROUP = "refresh.global.group";

}
