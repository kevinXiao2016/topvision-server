/***********************************************************************
 * $ TrendAttribute.java,v1.0 2011-11-21 16:06:48 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author jay
 * @created @2011-11-21-16:06:48
 */
public class PerfTrendAttribute implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 7342180206202447869L;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Long value;
    private Timestamp facadeTime;
    private String facadeTimeStr;

    public Timestamp getFacadeTime() {
        return facadeTime;
    }

    public void setFacadeTime(Timestamp facadeTime) {
        this.facadeTime = facadeTime;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public String getFacadeTimeStr() {
        facadeTimeStr = sdf.format(facadeTime);
        return facadeTimeStr;
    }

    public void setFacadeTimeStr(String facadeTimeStr) {
        try {
            facadeTime = new Timestamp(sdf.parse(facadeTimeStr).getTime());
        } catch (ParseException e) {
        }
        this.facadeTimeStr = facadeTimeStr;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("PerfTrendAttribute");
        sb.append("{facadeTime=").append(facadeTime);
        sb.append(", value=").append(value);
        sb.append('}');
        return sb.toString();
    }
}
