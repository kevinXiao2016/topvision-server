/***********************************************************************
 * $Id: AclPortACLListTable.java,v1.0 2013年10月25日 下午5:46:26 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.acl.domain;

import com.topvision.framework.utils.EponIndex;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.annotation.TableProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.domain.BaseEntity;

/**
 * @author Bravin
 * @created @2013年10月25日-下午5:46:26
 *
 */
@TableProperty(tables = { "default" })
public class AclPortACLListTable extends BaseEntity implements AliasesSuperType {
    private static final long serialVersionUID = 7872916005025137083L;
    private Long entityId;
    /**
     * 设备索引号
     */
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.3.2.1.1.1", index = true)
    private Long deviceIndex;
    private Long portIndex;

    /**
     * ACL RuleList Index
     */
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.3.2.1.1.2", index = true)
    private Integer topPortAclListIndex;
    /**
     * direction INTEGER { ingress(0), egress(1) }
     */
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.3.2.1.1.3", index = true)
    private Integer topAclPortDirection;

    /**
     * Add/Del/Set INTEGER { active(1), notInService(2), notReady(3), createAndGo(4),
     * createAndWait(5), destroy(6) }
     */
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.3.2.1.1.4", writable = true, type = "Integer32")
    private Integer topAclRowStatus;

    public Long getPortIndex() {
        return portIndex;
    }

    public void setPortIndex(Long portIndex) {
        this.portIndex = portIndex;
        if (EponIndex.getUniNo(portIndex) != 0) {
            deviceIndex = EponIndex.getAclDeviceIndexByUniIndex(portIndex);
        } else {
            deviceIndex = EponIndex.getAclDeviceIndexByPonIndex(portIndex);
        }
    }

    public Long getDeviceIndex() {
        return deviceIndex;
    }

    public void setDeviceIndex(Long deviceIndex) {
        this.deviceIndex = deviceIndex;
        portIndex = EponIndex.getAclPortIndexByMibDeviceIndex(deviceIndex);
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getTopAclPortDirection() {
        return topAclPortDirection;
    }

    public void setTopAclPortDirection(Integer topAclPortDirection) {
        this.topAclPortDirection = topAclPortDirection;
    }

    public Integer getTopAclRowStatus() {
        return topAclRowStatus;
    }

    public void setTopAclRowStatus(Integer topAclRowStatus) {
        this.topAclRowStatus = topAclRowStatus;
    }

    public Integer getTopPortAclListIndex() {
        return topPortAclListIndex;
    }

    public void setTopPortAclListIndex(Integer topPortAclListIndex) {
        this.topPortAclListIndex = topPortAclListIndex;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("AclPortACLListTable");
        sb.append("{deviceIndex=").append(deviceIndex);
        sb.append(", entityId=").append(entityId);
        sb.append(", topPortAclListIndex=").append(topPortAclListIndex);
        sb.append(", topAclPortDirection=").append(topAclPortDirection);
        sb.append(", topAclRowStatus=").append(topAclRowStatus);
        sb.append('}');
        return sb.toString();
    }
}
