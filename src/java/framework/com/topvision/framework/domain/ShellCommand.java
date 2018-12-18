/**
 * 
 */
package com.topvision.framework.domain;

/**
 * @author niejun
 * 
 */
public class ShellCommand extends BaseEntity {
    private static final long serialVersionUID = 7929015565692659749L;

    private String name;

    private String help;

    public String getHelp() {
        return help;
    }

    public String getName() {
        return name;
    }

    public void setHelp(String help) {
        this.help = help;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toStirng() {
        return name + "" + help;
    }

}
