/***********************************************************************
 * $Id: TopPonPortChgObjects.java,v1.0 2012-12-18 下午14:47:59 $
 * 
 * @author: lzt
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.olt.domain;

import com.topvision.framework.utils.EponIndex;
import com.topvision.framework.annotation.SnmpProperty;

/**
 * @author lzt
 * @created @2012-12-18-下午14:47:59
 * 
 */
public class TopPonPortChgObjects {

    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.3.6.1.0", writable = true, type = "Integer32")
    private Long ponPortSrcPonId;
    private Long ponSrcId;
    private Long ponSrcIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.3.6.2.0", writable = true, type = "Integer32")
    private Long ponPortDstPonId;
    private Long ponDstId;
    private Long ponDstIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.3.6.3.0", writable = true, type = "Integer32")
    private Integer ponPortChgAction;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getPonSrcId() {
        return ponSrcId;
    }

    public void setPonSrcId(Long ponSrcId) {
        this.ponSrcId = ponSrcId;
    }

    public Long getPonSrcIndex() {
        return ponSrcIndex;
    }

    public void setPonSrcIndex(Long ponSrcIndex) {
        if(ponPortSrcPonId == null){
            ponPortSrcPonId = EponIndex.getMibPonIndexIndexByPonIndex(ponSrcIndex);
        }
        this.ponSrcIndex = ponSrcIndex;
    }

    public Long getPonPortSrcPonId() {
        return ponPortSrcPonId;
    }

    public void setPonPortSrcPonId(Long ponPortSrcPonId) {
        this.ponPortSrcPonId = ponPortSrcPonId;
    }

    public Long getPonDstId() {
        return ponDstId;
    }

    public void setPonDstId(Long ponDstId) {
        this.ponDstId = ponDstId;
    }

    public Long getPonDstIndex() {
        return ponDstIndex;
    }

    public void setPonDstIndex(Long ponDstIndex) {
        if(ponPortDstPonId == null){
            ponPortDstPonId = EponIndex.getMibPonIndexIndexByPonIndex(ponDstIndex);
        }
        this.ponDstIndex = ponDstIndex;
    }

    public Long getPonPortDstPonId() {
        return ponPortDstPonId;
    }

    public void setPonPortDstPonId(Long ponPortDstPonId) {
        this.ponPortDstPonId = ponPortDstPonId;
    }

    public Integer getPonPortChgAction() {
        return ponPortChgAction;
    }

    public void setPonPortChgAction(Integer ponPortChgAction) {
        this.ponPortChgAction = ponPortChgAction;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("topPonPortChgObjects [entityId=");
        builder.append(entityId);
        builder.append(", ponSrcId=");
        builder.append(ponSrcId);
        builder.append(", ponSrcIndex=");
        builder.append(ponSrcIndex);
        builder.append(", ponPortSrcPonId=");
        builder.append(ponPortSrcPonId);
        builder.append(", ponDstId=");
        builder.append(ponDstId);
        builder.append(", ponDstIndex=");
        builder.append(ponDstIndex);
        builder.append(", ponPortDstPonId=");
        builder.append(ponPortDstPonId);
        builder.append(", ponPortChgAction=");
        builder.append(ponPortChgAction);
        builder.append("]");
        return builder.toString();
    }

}
