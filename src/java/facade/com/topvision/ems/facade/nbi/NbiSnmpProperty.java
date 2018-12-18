/***********************************************************************
 * $Id: NbiSnmpProperty.java,v1.0 2016年3月14日 上午11:29:29 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.facade.nbi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Bravin
 * @created @2016年3月14日-上午11:29:29
 *
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface NbiSnmpProperty {

    /**
     * 对应变量的OID eg. .1.3.6.1.2.1.1.2.0
     * 
     * @return
     */
    String oid();

    /**
     * 变量对应OID的mib节点类型，在写mib库时需要用 ，只读mib节点可以忽略此属性
     * 
     * @return SNMP协议支持的数据类型OctetString, IpAddress, Counter32, TimeTicks
     */
    String type() default "OctetString";
}
