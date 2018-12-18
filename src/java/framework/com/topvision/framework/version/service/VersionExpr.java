/***********************************************************************
 * $Id: VersionExpr.java,v 1.1 2010-1-17 下午06:48:28 kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.framework.version.service;

import java.sql.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.framework.common.DateUtils;

/**
 * @Create Date 2010-1-17 下午06:48:28
 * 
 * @author kelers
 * 
 */
public class VersionExpr implements Comparable<VersionExpr> {
    protected static final Logger logger = LoggerFactory.getLogger(VersionExpr.class);

    private static int compareDate(Date d1, Date d2) {
        d1 = Date.valueOf(DateUtils.MINUTE_FORMAT.format(d1));
        d2 = Date.valueOf(DateUtils.MINUTE_FORMAT.format(d2));
        return d1.compareTo(d2);
    }

    private int[] varray = null;
    private Date build = null;
    private VersionExpr dbVersionExpr;

    private String version;

    private String module;

    public VersionExpr(String version, Date build) throws Exception {
        if (!isValidVersion(version))
            throw new RuntimeException("Input invalid version [" + version + "]. eg.1.1.2");
        String[] vs = version.split("\\.");
        varray = new int[vs.length];
        for (int i = 0; i < vs.length; i++) {
            varray[i] = Integer.parseInt(vs[i]);
        }
        this.version = version;
        this.build = build;
    }

    public VersionExpr(String version, String build) throws Exception {
        this(version, new Date(DateUtils.FULL_FORMAT.parse(build).getTime()));
    }

    /**
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(VersionExpr expr) {
        int[] varray1 = expr.varray;
        int count = Math.max(varray1.length, varray.length);
        for (int i = 0; i < count; i++) {
            if (i >= varray1.length)
                return 1;
            else if (i >= varray.length)
                return -1;
            else if (varray[i] == varray1[i])
                continue;
            else
                return varray[i] - varray1[i];
        }
        return compareDate(build, expr.build);
    }

    /**
     * @return the build
     */
    public Date getBuild() {
        return build;
    }

    /**
     * @return the dbVersionExpr
     */
    public VersionExpr getDbVersionExpr() {
        return dbVersionExpr;
    }

    /**
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    private boolean isValidVersion(String version) {
        String[] vs = version.split("\\.");
        if (vs.length == 0)
            return false;
        boolean match = true;
        for (int i = 0; i < vs.length; i++) {
            match = match || vs[i].matches("\\d+");
        }
        return match || vs.length > 0;
    }

    /**
     * @param dbVersionExpr
     *            the dbVersionExpr to set
     */
    public void setDbVersionExpr(VersionExpr dbVersionExpr) {
        this.dbVersionExpr = dbVersionExpr;
    }

    /**
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("V");
        for (int i = 0; i < varray.length; i++) {
            sb.append(varray[i]).append(".");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("_D").append(DateUtils.MINUTE_FORMAT.format(build));
        return sb.toString();
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getId() {
        StringBuilder sb = new StringBuilder(module);
        sb.append("_V");
        for (int i = 0; i < varray.length; i++) {
            sb.append(varray[i]).append(".");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("_D").append(DateUtils.MINUTE_FORMAT.format(build));
        return sb.toString();
    }
}
