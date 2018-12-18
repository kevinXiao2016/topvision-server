/***********************************************************************
 * $Id: Formula.java,v 1.1 Nov 1, 2008 2:52:55 PM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.ems.facade.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Create Date Nov 1, 2008 2:52:55 PM
 * 
 * @author kelers
 * 
 */
public class Formula implements Serializable {
    private static final long serialVersionUID = 3653394889800392384L;
    private String name;
    private String type;
    private String formula;
    private String condition;
    private String desc;
    private String mibs;

    public Formula() {
    }

    public Formula(String f) {
        this.formula = f;
    }

    /**
     * @return the condition
     */
    public String getCondition() {
        return condition;
    }

    /**
     * @return the desc
     */
    public String getDesc() {
        return desc;
    }

    /**
     * @return the formula
     */
    public String getFormula() {
        return formula;
    }

    /**
     * @return the mibs
     */
    public String getMibs() {
        return mibs;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the formula
     */
    public String[] getOids() {
        String[] ss = formula.split("([()+-]|/|\\*)");
        List<String> oids = new ArrayList<String>();
        for (String s : ss) {
            if (s == null || s.trim().length() <= 10) {
                continue;
            }
            s = s.trim();
            if (!oids.contains(s)) {
                oids.add(s);
            }
        }
        return oids.toArray(new String[oids.size()]);
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param condition
     *            the condition to set
     */
    public void setCondition(String condition) {
        this.condition = condition;
    }

    /**
     * @param desc
     *            the desc to set
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * @param formula
     *            the formula to set
     */
    public void setFormula(String formula) {
        this.formula = formula;
    }

    /**
     * @param mibs
     *            the mibs to set
     */
    public void setMibs(String mibs) {
        this.mibs = mibs;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        StringBuilder data = new StringBuilder();
        data.append("name:");
        data.append(name);
        data.append(";formula:");
        data.append(formula);
        data.append(";condition:");
        data.append(condition);
        return data.toString();
    }
}
