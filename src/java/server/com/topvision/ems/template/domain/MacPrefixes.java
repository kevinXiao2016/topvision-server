/***********************************************************************
 * $Id: MacPrefixes.java,v 1.1 2009-11-8 下午05:57:32 kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.ems.template.domain;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.domain.BaseEntity;

/**
 * @Create Date 2009-11-8 下午05:57:32
 * 
 * @author kelers
 * 
 */
@Alias("macPrefixes")
public class MacPrefixes extends BaseEntity implements AliasesSuperType {
    private static final long serialVersionUID = 9126716337985789454L;
    private String prefix;
    private String corp;
    private String corp_zh;
    private String type;
    private String note;

    /**
     * @return the corp
     */
    public String getCorp() {
        return corp;
    }

    /**
     * @return the corp_zh
     */
    public String getCorp_zh() {
        return corp_zh;
    }

    /**
     * @return the note
     */
    public String getNote() {
        return note;
    }

    /**
     * @return the prefix
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param corp
     *            the corp to set
     */
    public void setCorp(String corp) {
        this.corp = corp;
    }

    /**
     * @param corpZh
     *            the corp_zh to set
     */
    public void setCorp_zh(String corpZh) {
        corp_zh = corpZh;
    }

    /**
     * @param note
     *            the note to set
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * @param prefix
     *            the prefix to set
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(String type) {
        this.type = type;
    }
}
