/***********************************************************************
 * $Id: OnuHealthyConstants.java,v1.0 2017年7月20日 上午9:23:50 $
 * 
 * @author: ls
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.mobile.domain;

import java.io.Serializable;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author ls
 * @created @2017年7月20日-上午9:23:50
 *
 */
public class OnuHealthyConstants implements Serializable,AliasesSuperType {
    private static final long serialVersionUID = 1496999477071840951L;
    
    public static final String ONUPRP="ONU_PON_RE_POWER";
    public static final String ONUPTP="ONU_PON_TX_POWER";
    public static final String OLTPRP="OLT_PONLLID_RE_POWER";
    public static final String ONUCRP="ONU_CATV_RX_POWER";
    public static final String ONUPR="ONU_CATV_RF";
    public static final String ONUCV="ONU_CATV_VOLTAGE";
    public static final String ONUCT="ONU_CATV_TEMP";
    public static final String ONUGRP="ONU_GPON_RE_POWER";
    public static final String ONUGTP="ONU_GPON_TX_POWER";
    public static final String OLTGPLLIDRP="OLT_GPONLLID_RE_POWER";

}
