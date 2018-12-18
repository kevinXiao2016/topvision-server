/**
 *
 */
package com.topvision.platform.domain;

import com.topvision.framework.domain.BaseEntity;

/**
 * @author niejun
 */
public class Dashboard extends BaseEntity {

    private static final long serialVersionUID = 2837257455490228557L;

    private String displayName;
    private String name;
    private String icon;

    public String getDisplayName() {
        return displayName;
    }

    public String getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setName(String name) {
        this.name = name;
    }

}
