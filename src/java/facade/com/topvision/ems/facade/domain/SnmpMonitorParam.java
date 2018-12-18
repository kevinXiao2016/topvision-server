/***********************************************************************
 * $Id: SnmpMonitorParam.java,v 1.1 May 30, 2008 4:52:02 PM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.ems.facade.domain;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.topvision.framework.snmp.SnmpParam;

/**
 * @Create Date May 30, 2008 4:52:02 PM
 * 
 * @author kelers
 * 
 */
public class SnmpMonitorParam extends AbstractMonitorParam {
    private static final long serialVersionUID = 4799540327397842726L;
    private SnmpParam snmpParam = null;
    private Map<String, FormulaBinding> bindings = null;

    /**
     * default constructor.
     */
    public SnmpMonitorParam() {
        bindings = new HashMap<String, FormulaBinding>();
    }

    /**
     * 
     * @param binding
     */
    public void addBinding(FormulaBinding binding) {
        bindings.put(binding.getName(), binding);
    }

    public void addBinding(String name, String oid, byte type, Formula formula) {
        addBinding(name, oid, type, formula, null);
    }

    /**
     * 
     * @param name
     * @param oid
     * @param type
     * @param formula
     * @param desc
     */
    public void addBinding(String name, String oid, byte type, Formula formula, String desc) {
        FormulaBinding binding = new FormulaBinding(name);
        binding.setDesc(desc);
        binding.setOid(oid);
        binding.setType(type);
        binding.setFormula(formula);
        bindings.put(name, binding);
    }

    public void addBinding(String name, String[] cols, byte type, Formula formula) {
        addBinding(name, cols, type, formula, null);
    }

    /**
     * 
     * @param name
     * @param cols
     * @param type
     * @param formula
     * @param desc
     */
    public void addBinding(String name, String[] cols, byte type, Formula formula, String desc) {
        FormulaBinding binding = new FormulaBinding(name);
        binding.setDesc(desc);
        binding.setOids(cols);
        binding.setType(type);
        binding.setFormula(formula);
        bindings.put(name, binding);
    }

    /**
     * clear bindings
     */
    public void clearBindings() {
        bindings.clear();
    }

    /**
     * @param name
     * @return
     */
    public boolean containBindings(String name) {
        return bindings.containsKey(name);
    }

    /**
     * @return the binding
     */
    public FormulaBinding getBinding(String name) {
        return bindings.get(name);
    }

    /**
     * @return the bindings
     */
    public Map<String, FormulaBinding> getBindings() {
        return bindings;
    }

    /**
     * @return the snmpParam
     */
    public SnmpParam getSnmpParam() {
        return snmpParam;
    }

    /**
     * @return the bindings
     */
    public Iterator<FormulaBinding> iteratorBindings() {
        return bindings.values().iterator();
    }

    /**
     * @param bindings
     *            the bindings to set
     */
    public void setBindings(Map<String, FormulaBinding> map) {
        this.bindings.putAll(map);
    }

    /**
     * @param snmpParam
     *            the snmpParam to set
     */
    public void setSnmpParam(SnmpParam snmpParam) {
        this.snmpParam = snmpParam;
    }
}
