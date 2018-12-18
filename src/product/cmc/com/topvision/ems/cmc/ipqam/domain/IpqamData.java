/***********************************************************************
 * $Id: IpqamData.java,v1.0 2016年5月3日 下午3:08:59 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ipqam.domain;

import java.io.Serializable;
import java.util.List;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author loyal
 * @created @2016年5月3日-下午3:08:59
 * 
 */
public class IpqamData implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 7275049337790394666L;

    private List<CmcEqamStatus> cmcEqamStatusList;
    private List<CmcEqamProgram> cmcEqamProgramList;
    private List<ProgramIn> programInList;
    private List<ProgramOut> programOutList;

    public List<CmcEqamStatus> getCmcEqamStatusList() {
        return cmcEqamStatusList;
    }

    public void setCmcEqamStatusList(List<CmcEqamStatus> cmcEqamStatusList) {
        this.cmcEqamStatusList = cmcEqamStatusList;
    }

    public List<CmcEqamProgram> getCmcEqamProgramList() {
        return cmcEqamProgramList;
    }

    public void setCmcEqamProgramList(List<CmcEqamProgram> cmcEqamProgramList) {
        this.cmcEqamProgramList = cmcEqamProgramList;
    }

    public List<ProgramIn> getProgramInList() {
        return programInList;
    }

    public void setProgramInList(List<ProgramIn> programInList) {
        this.programInList = programInList;
    }

    public List<ProgramOut> getProgramOutList() {
        return programOutList;
    }

    public void setProgramOutList(List<ProgramOut> programOutList) {
        this.programOutList = programOutList;
    }

}
