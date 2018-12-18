/***********************************************************************
 * $Id: UniVlanProfileTable.java,v1.0 2013-11-28 上午10:19:55 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.univlanprofile.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author flack
 * @created @2013-11-28-上午10:19:55
 *
 */
public class UniVlanProfileTable implements AliasesSuperType {
    private static final long serialVersionUID = 4157708561358488182L;

    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.7.1.1.1", index = true)
    private Integer profileId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.7.1.1.2")
    private Integer profileRefCnt;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.7.1.1.3", writable = true, type = "OctetString")
    private String profileName;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.7.1.1.4", writable = true, type = "Integer32")
    private Integer profileMode;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.7.1.1.5", writable = true, type = "Integer32")
    private Integer profileRowstatus;
    /** mib中这个表并没有pvid节点，这个是为了方便一起配置才使用的 */
    private Integer pvid;

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

    public Integer getProfileRefCnt() {
        return profileRefCnt;
    }

    public void setProfileRefCnt(Integer profileRefCnt) {
        this.profileRefCnt = profileRefCnt;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public Integer getProfileMode() {
        return profileMode;
    }

    public void setProfileMode(Integer profileMode) {
        this.profileMode = profileMode;
    }

    public Integer getProfileRowstatus() {
        return profileRowstatus;
    }

    public void setProfileRowstatus(Integer profileRowstatus) {
        this.profileRowstatus = profileRowstatus;
    }

    public Integer getPvid() {
        return pvid;
    }

    public void setPvid(Integer pvid) {
        this.pvid = pvid;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("UniVlanProfileTable [entityId=");
        builder.append(entityId);
        builder.append(", profileId=");
        builder.append(profileId);
        builder.append(", profileRefCnt=");
        builder.append(profileRefCnt);
        builder.append(", profileName=");
        builder.append(profileName);
        builder.append(", profileMode=");
        builder.append(profileMode);
        builder.append(", profileRowstatus=");
        builder.append(profileRowstatus);
        builder.append("]");
        return builder.toString();
    }

}
