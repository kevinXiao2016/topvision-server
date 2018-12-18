/***********************************************************************
 * $Id: GponSrvProfilePortVlanCfg.java,v1.0 2016年10月25日 上午8:29:49 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.profile.facade.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author Bravin
 * @created @2016年10月25日-上午8:29:49
 *
 */
public class GponSrvProfilePortVlanCfg implements AliasesSuperType {
    private static final long serialVersionUID = -6714012301332069879L;

    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.3.5.1.1.1", index = true, type = "Integer32")
    private Integer gponSrvProfilePortVlanProfileIndex;// 1-1024
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.3.5.1.1.2", index = true, type = "Integer32")
    private Integer gponSrvProfilePortVlanPortTypeIndex;// 0:eth,1:wlan,2:catv,3:POTS,4:VEIP
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.3.5.1.1.3", index = true, type = "Integer32")
    private Integer gponSrvProfilePortVlanPortIdIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.3.5.1.1.4", writable = true, type = "Integer32")
    private Integer gponSrvProfilePortVlanPvid;// 0-4094 0:unconcern
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.3.5.1.1.5", writable = true, type = "Integer32")
    private Integer gponSrvProfilePortVlanPvidPri;// 0-7 必须和Pvid一起配置，不能单独配置
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.3.5.1.1.6", writable = true, type = "Integer32")
    private Integer gponSrvProfilePortVlanMode;// 0-4 0:transparent 1:tag 2:translate 3:aggregation
                                               // 4:trunk

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getGponSrvProfilePortVlanProfileIndex() {
        return gponSrvProfilePortVlanProfileIndex;
    }

    public void setGponSrvProfilePortVlanProfileIndex(Integer gponSrvProfilePortVlanProfileIndex) {
        this.gponSrvProfilePortVlanProfileIndex = gponSrvProfilePortVlanProfileIndex;
    }

    public Integer getGponSrvProfilePortVlanPortTypeIndex() {
        return gponSrvProfilePortVlanPortTypeIndex;
    }

    public void setGponSrvProfilePortVlanPortTypeIndex(Integer gponSrvProfilePortVlanPortTypeIndex) {
        this.gponSrvProfilePortVlanPortTypeIndex = gponSrvProfilePortVlanPortTypeIndex;
    }

    public Integer getGponSrvProfilePortVlanPortIdIndex() {
        return gponSrvProfilePortVlanPortIdIndex;
    }

    public void setGponSrvProfilePortVlanPortIdIndex(Integer gponSrvProfilePortVlanPortIdIndex) {
        this.gponSrvProfilePortVlanPortIdIndex = gponSrvProfilePortVlanPortIdIndex;
    }

    public Integer getGponSrvProfilePortVlanPvid() {
        return gponSrvProfilePortVlanPvid;
    }

    public void setGponSrvProfilePortVlanPvid(Integer gponSrvProfilePortVlanPvid) {
        this.gponSrvProfilePortVlanPvid = gponSrvProfilePortVlanPvid;
    }

    public Integer getGponSrvProfilePortVlanPvidPri() {
        return gponSrvProfilePortVlanPvidPri;
    }

    public void setGponSrvProfilePortVlanPvidPri(Integer gponSrvProfilePortVlanPvidPri) {
        this.gponSrvProfilePortVlanPvidPri = gponSrvProfilePortVlanPvidPri;
    }

    public Integer getGponSrvProfilePortVlanMode() {
        return gponSrvProfilePortVlanMode;
    }

    public void setGponSrvProfilePortVlanMode(Integer gponSrvProfilePortVlanMode) {
        this.gponSrvProfilePortVlanMode = gponSrvProfilePortVlanMode;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("GponSrvProfilePortVlanCfg [entityId=");
        builder.append(entityId);
        builder.append(", gponSrvProfilePortVlanProfileIndex=");
        builder.append(gponSrvProfilePortVlanProfileIndex);
        builder.append(", gponSrvProfilePortVlanPortTypeIndex=");
        builder.append(gponSrvProfilePortVlanPortTypeIndex);
        builder.append(", gponSrvProfilePortVlanPortIdIndex=");
        builder.append(gponSrvProfilePortVlanPortIdIndex);
        builder.append(", gponSrvProfilePortVlanPvid=");
        builder.append(gponSrvProfilePortVlanPvid);
        builder.append(", gponSrvProfilePortVlanPvidPri=");
        builder.append(gponSrvProfilePortVlanPvidPri);
        builder.append(", gponSrvProfilePortVlanMode=");
        builder.append(gponSrvProfilePortVlanMode);
        builder.append("]");
        return builder.toString();
    }

}
