/**
 * 
 */
package com.topvision.ems.resources.domain;

import java.io.Serializable;

/**
 * @author kelers
 * 
 */
public class Resource implements Serializable {

    private static final long serialVersionUID = -4169599775124140993L;

    public static final int TYPE_DEVICE = 1;

    private int type = TYPE_DEVICE;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
