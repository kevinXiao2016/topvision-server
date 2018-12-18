/***********************************************************************
 * $Id: SnmpProperty.java,v 1.1 Mar 11, 2009 11:12:28 PM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Create Date Mar 11, 2009 11:12:28 PM
 * 
 * @author kelers
 * 
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface SnmpProperty {
    /**
     * 表格中index序号，在写mib table时必须设置所有index的这个属性
     * 
     * @return
     */
    boolean index() default false;

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

    /**
     * 在type是OctetString时有效，对返回true的字段不进行acsii码转换
     */
    boolean isHex() default false;

    /**
     * 此变量对应OID是否有写能力
     * 
     */
    boolean writable() default false;

    /**
     * 如果一个Bean中有多个table，需要对不同的table设置不同的值
     * 
     */
    String table() default "default";
}
