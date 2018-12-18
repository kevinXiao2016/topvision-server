/***********************************************************************
 * $Id: OltPonPortFlowStastic.java,v1.0 2013-5-30 下午3:39:57 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.report.domain;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * 
 * PON端口的属性可能比SNI端口多，比如光属性，为后期考虑
 * 
 * @author Bravin
 * @created @2013-5-30-下午3:39:57
 * 
 */
public class OltPonPortFlowStastic extends OltSniPortFlowStastic implements AliasesSuperType {
    private static final long serialVersionUID = -6005453314024296506L;

}
