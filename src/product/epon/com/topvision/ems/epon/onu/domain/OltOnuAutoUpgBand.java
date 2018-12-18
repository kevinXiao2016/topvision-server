/***********************************************************************
 * $ OltOnuAutoUpgBand.java,v1.0 2012-9-19 16:30:28 $
 *
 * @author: yq
 *
 ***********************************************************************/
package com.topvision.ems.epon.onu.domain;

import java.io.Serializable;

import com.topvision.framework.utils.EponIndex;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author yq
 * @created @2012-9-19-16:30:28
 */
public class OltOnuAutoUpgBand implements Serializable, AliasesSuperType {
	private static final long serialVersionUID = 1418410469711282210L;
	private Long entityId;
	private Long ponId;
	private Long ponIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.7.2.1.1", index = true)
    private Integer slotNo;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.7.2.1.2", index = true)
    private Integer ponNo;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.7.2.1.3", index = true)
    private Integer onuNo;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.7.2.1.4", index = true)
    private Integer profileId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.7.2.1.5", type = "Integer32")
    private Integer installStat;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.7.2.1.6", writable = true, type = "Integer32")
    private Integer topOnuAutoUpgInstallAction;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.7.2.1.7", writable = true, type = "Integer32")
    private Integer topOnuAutoUpgInstallRowStatus;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getProfileId() {
        return profileId;
    }

    public void setProfileId(Integer profileId) {
        this.profileId = profileId;
    }

    public Integer getSlotNo() {
        return slotNo;
    }

    public void setSlotNo(Integer slotNo) {
        this.slotNo = slotNo;
    }

    public Integer getPonNo() {
        return ponNo;
    }

    public void setPonNo(Integer ponNo) {
        this.ponNo = ponNo;
    }

    public Integer getOnuNo() {
    	if(onuNo != null){
    		return onuNo;
    	}else{
    		return 0;
    	}
    }

    public void setOnuNo(Integer onuNo) {
    	if(onuNo != null){
    		this.onuNo = onuNo;
    	}else{
    		this.onuNo = 0;
    	}
    }

    public Integer getInstallStat() {
        return installStat;
    }

    public void setInstallStat(Integer installStat) {
        this.installStat = installStat;
    }

    public Integer getTopOnuAutoUpgInstallAction() {
    	return topOnuAutoUpgInstallAction;
    }

    public void setTopOnuAutoUpgInstallAction(Integer topOnuAutoUpgInstallAction) {
   		this.topOnuAutoUpgInstallAction = topOnuAutoUpgInstallAction;
    }

    public Integer getTopOnuAutoUpgInstallRowStatus() {
        return topOnuAutoUpgInstallRowStatus;
    }

    public void setTopOnuAutoUpgInstallRowStatus(Integer topOnuAutoUpgInstallRowStatus) {
        this.topOnuAutoUpgInstallRowStatus = topOnuAutoUpgInstallRowStatus;
    }

    public Long getPonId() {
        return ponId;
    }

    public void setPonId(Long ponId) {
        this.ponId = ponId;
    }

    public Long getPonIndex() {
        return ponIndex;
    }

    public void setPonIndex(Long ponIndex) {
        this.ponIndex = ponIndex;
        if (ponIndex != null) {
            this.slotNo = EponIndex.getSlotNo(ponIndex).intValue();
            this.ponNo = EponIndex.getPonNo(ponIndex).intValue();
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("OltOnuAutoUpgBand");
        sb.append("{entityId=").append(entityId);
        sb.append(", profileId=").append(profileId);
        sb.append('}');
        return sb.toString();
    }
}
