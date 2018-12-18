/**
 * 
 */
package com.topvision.ems.facade.domain;

import java.io.Serializable;

/**
 * @author kelers
 * 
 */
public class EntityTemplate implements Serializable {

    private static final long serialVersionUID = -3031369747146072911L;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
