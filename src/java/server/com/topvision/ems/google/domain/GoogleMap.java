/**
 * 
 */
package com.topvision.ems.google.domain;

import com.topvision.framework.domain.BaseEntity;

/**
 * @author niejun
 * 
 */
public class GoogleMap extends BaseEntity {

    private static final long serialVersionUID = -6803147371184378288L;

    private String key = null;

    private String lang = null;

    public String getKey() {
        return key;
    }

    public String getLang() {
        return lang;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

}
