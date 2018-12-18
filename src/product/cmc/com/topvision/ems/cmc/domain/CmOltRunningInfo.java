package com.topvision.ems.cmc.domain;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.utils.CmcIndexUtils;
/***********************************************************************
 * $Id: CmOltRunningInfo.java,v1.0 2013-7-19 下午3:47:14 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author loyal
 * @created @2013-7-19-下午3:47:14
 *
 */
@Alias("cmOltRunningInfo")
public class CmOltRunningInfo implements AliasesSuperType{
    private static final long serialVersionUID = -8025045670421649725L;
    private Long entityId;
    private String ponIdString;
    private Long onuPonId;
    private Long ponIndex;
    private Long cmcId;
    private Long cmcIndex;
    private Long cmId;
    private Float txPower;
    private Float rxPower;
    public Long getEntityId() {
        return entityId;
    }
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }
    public String getPonIdString() {
		return ponIdString;
	}
	public void setPonIdString(String ponIdString) {
		this.ponIdString = ponIdString;
	}
	public Long getPonIndex() {
        return ponIndex;
    }
    public void setPonIndex(Long ponIndex) {
        this.ponIndex = ponIndex;
    }
    public Long getCmcId() {
        return cmcId;
    }
    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }
    public Long getCmId() {
        return cmId;
    }
    public void setCmId(Long cmId) {
        this.cmId = cmId;
    }
    public Float getTxPower() {
        return txPower;
    }
    public void setTxPower(Float txPower) {
        this.txPower = txPower;
    }
    public Float getRxPower() {
        return rxPower;
    }
    public void setRxPower(Float rxPower) {
        this.rxPower = rxPower;
    }
    public Long getCmcIndex() {
        return cmcIndex;
    }
    public void setCmcIndex(Long cmcIndex) {
        this.cmcIndex = cmcIndex;
        this.ponIdString = CmcIndexUtils.getSlotNo(cmcIndex) + "/" + CmcIndexUtils.getPonNo(cmcIndex);
    }
    public Long getOnuPonId() {
        return onuPonId;
    }
    public void setOnuPonId(Long onuPonId) {
        this.onuPonId = onuPonId;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmOltRunningInfo [entityId=");
        builder.append(entityId);
        builder.append(", ponIdString=");
        builder.append(ponIdString);
        builder.append(", onuPonId=");
        builder.append(onuPonId);
        builder.append(", ponIndex=");
        builder.append(ponIndex);
        builder.append(", cmcId=");
        builder.append(cmcId);
        builder.append(", cmcIndex=");
        builder.append(cmcIndex);
        builder.append(", cmId=");
        builder.append(cmId);
        builder.append(", txPower=");
        builder.append(txPower);
        builder.append(", rxPower=");
        builder.append(rxPower);
        builder.append("]");
        return builder.toString();
    }
}
