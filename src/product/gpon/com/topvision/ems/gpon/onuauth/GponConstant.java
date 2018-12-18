/***********************************************************************
 * $Id: GponConstant.java,v1.0 2016年12月21日 下午4:43:27 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.onuauth;

import com.topvision.platform.zetaframework.var.ZetaValueGetter;

/**
 * @author Bravin
 * @created @2016年12月21日-下午4:43:27
 *
 */
@ZetaValueGetter("GponConstant")
public class GponConstant {
    public static final Integer PORT_TYPE_EPON = 1;
    public static final Integer PORT_TYPE_GPON = 3;

    public static final Integer PON_AUTO_FIND_ENABLED = 1;
    public static final Integer PON_AUTO_FIND_DISABLED = 2;

    public static final Integer GPON_MAX_ONU_AUTH_NUM = 128;
    public static final Integer AUTO_AUTH_MAX_RULE_NUM = 256;

    public static final String GPON_ONU = "G";

    public static final Integer ATUH_SN = 1;
    public static final Integer ATUH_SN_PWD = 2;
    public static final Integer ATUH_LOID = 3;
    public static final Integer ATUH_LOID_PWD = 4;
    public static final Integer ATUH_PWD = 5;
    public static final Integer ATUH_AUTO = 6;
    public static final Integer ATUH_MIX = 7;
}
