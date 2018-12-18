/***********************************************************************
 * $Id: EponBatchTargetExpression.java,v1.0 2013年12月2日 下午2:31:34 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.batchdeploy.domain;

import java.util.List;

/**
 * @author Bravin
 * @created @2013年12月2日-下午2:31:34
 *
 */
public class Expression {
    public static final int LV_SLOT = 1;
    public static final int LV_PORT = 2;
    public static final int LV_LLID = 3;
    public static final int LV_UNI = 4;
    private int level;
    private String expression;
    private List<Integer> slots;
    private List<Integer> ports;
    private List<Integer> llids;
    private List<Integer> unis;

    public Expression() {

    }
    public Expression(String expression){
        this.expression = expression;
    }
    public int getLevel() {
        return level;
    }
    public void setLevel(int level) {
        this.level = level;
    }
    public String getExpression() {
        return expression;
    }
    public void setExpression(String expression) {
        this.expression = expression;
    }

    public List<Integer> getSlots() {
        return slots;
    }

    public void setSlots(List<Integer> slots) {
        this.slots = slots;
    }

    public List<Integer> getPorts() {
        return ports;
    }

    public void setPorts(List<Integer> ports) {
        this.ports = ports;
    }

    public List<Integer> getLlids() {
        return llids;
    }

    public void setLlids(List<Integer> llids) {
        this.llids = llids;
    }

    public List<Integer> getUnis() {
        return unis;
    }

    public void setUnis(List<Integer> unis) {
        this.unis = unis;
    }

    @Override
    public String toString() {
        return "Expression [level=" + level + ", expression=" + expression + ", slots=" + slots + ", ports=" + ports
                + ", llids=" + llids + ", unis=" + unis + "]";
    }

}
