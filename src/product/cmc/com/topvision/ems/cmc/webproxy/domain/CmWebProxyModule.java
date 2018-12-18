/***********************************************************************
 * CmWebProxyModule.java,v1.0 17-4-24 下午3:05 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.webproxy.domain;

import com.topvision.framework.dao.mybatis.AliasesSuperType;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;

/**
 * @author jay
 * @created 17-4-24 下午3:05
 */
@Alias("cmWebProxyConfig")
public class CmWebProxyModule implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 8571259721660333279L;

    public static Integer DIRECTJUMP = 1;
    public static Integer PROXYJUMP = 2;
}
