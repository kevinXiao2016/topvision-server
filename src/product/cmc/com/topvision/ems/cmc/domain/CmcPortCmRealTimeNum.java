/***********************************************************************
 * $Id: CmcPortCmRealTimeNum.java,v1.0 2013-7-9 下午5:32:54 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author loyal
 * @created @2013-7-9-下午5:32:54
 *
 */
public class CmcPortCmRealTimeNum {
    private Long cmcIndex;
    private String cmcIndexString;
    private List<CmcCmReatimeNum> cmcCmReatimeNumList = new ArrayList<CmcCmReatimeNum>();
    public Long getCmcIndex() {
        return cmcIndex;
    }
    public void setCmcIndex(Long cmcIndex) {
        this.cmcIndex = cmcIndex;
    }
    public String getCmcIndexString() {
        return cmcIndexString;
    }
    public void setCmcIndexString(String cmcIndexString) {
        this.cmcIndexString = cmcIndexString;
    }
    public List<CmcCmReatimeNum> getCmcCmReatimeNumList() {
        return cmcCmReatimeNumList;
    }
    public void setCmcCmReatimeNumList(List<CmcCmReatimeNum> cmcCmReatimeNumList) {
        this.cmcCmReatimeNumList = cmcCmReatimeNumList;
    }
    public void addCmcCmReatimeNum(CmcCmReatimeNum cmcCmReatimeNum){
        this.cmcCmReatimeNumList.add(cmcCmReatimeNum);
    }
    public void sort(){
        if(this.cmcCmReatimeNumList != null && this.cmcCmReatimeNumList.size() > 0){
            Collections.sort(this.cmcCmReatimeNumList, new Comparator<CmcCmReatimeNum>() {
                public int compare(CmcCmReatimeNum arg0, CmcCmReatimeNum arg1) {
                    return arg0.getChannelId().compareTo(arg1.getChannelId());
                }
            });
        }
    }
}
