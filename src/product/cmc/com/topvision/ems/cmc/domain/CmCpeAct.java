/***********************************************************************
 * $Id: CmCpeAct.java,v1.0 2013-7-17 下午1:23:16 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.domain;

import java.util.ArrayList;

import com.topvision.ems.cmc.performance.domain.CpeAct;

/**
 * @author loyal
 * @created @2013-7-17-下午1:23:16
 *
 */
public class CmCpeAct {
    private String cpeMacString;
    private ArrayList<CpeAct> cpeActs = new ArrayList<CpeAct>();
       
    public String getCpeMacString() {
        return cpeMacString;
    }
    public void setCpeMacString(String cpeMacString) {
        this.cpeMacString = cpeMacString;
    }
    public ArrayList<CpeAct> getCpeActs() {
        return cpeActs;
    }
    public void setCpeActs(ArrayList<CpeAct> cpeActs) {
        this.cpeActs = cpeActs;
    }
    
    public void addCpeAct(CpeAct cpeAct){
        this.cpeActs.add(cpeAct);
    }
}
