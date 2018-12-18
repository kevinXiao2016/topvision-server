/***********************************************************************
 * $Id: AclListTable.java,v1.0 2013年10月25日 下午5:45:46 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.acl.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.annotation.TableProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.domain.BaseEntity;

/**
 * @author Bravin
 * @created @2013年10月25日-下午5:45:46
 *
 */
@TableProperty(tables = { "default" })
public class AclListTable extends BaseEntity implements AliasesSuperType {
    private static final long serialVersionUID = -2799491947840724388L;

    private Long entityId;
    /**
     * ACL List Index
     */
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.3.2.2.1.1", index = true)
    private Integer topAclListIndex;

    /**
     * ACL Description OCTET STRING (SIZE (1..63))
     */
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.3.2.2.1.2", writable = true, type = "OctetString")
    private String topAclDescription;
    /**
     * ACL rule quantity. INTEGER (0..16)
     */
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.3.2.2.1.3")
    private Integer topAclRuleNum;
    /**
     * priority of ACL list index INTEGER (1..16)
     */
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.3.2.2.1.4", writable = true, type = "Integer32")
    private Integer topAclRulePriority;
    /**
     * Add/Del/Set INTEGER { active(1), notInService(2), notReady(3), createAndGo(4),
     * createAndWait(5), destroy(6) }
     */
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.3.2.2.1.5", writable = true, type = "Integer32")
    private Integer topRuleRowStatus;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getTopAclDescription() {
        return topAclDescription;
    }

    public void setTopAclDescription(String topAclDescription) {
        this.topAclDescription = topAclDescription;
    }

    public Integer getTopAclListIndex() {
        return topAclListIndex;
    }

    public void setTopAclListIndex(Integer topAclListIndex) {
        this.topAclListIndex = topAclListIndex;
    }

    public Integer getTopAclRuleNum() {
        return topAclRuleNum;
    }

    public void setTopAclRuleNum(Integer topAclRuleNum) {
        this.topAclRuleNum = topAclRuleNum;
    }

    public Integer getTopAclRulePriority() {
        return topAclRulePriority;
    }

    public void setTopAclRulePriority(Integer topAclRulePriority) {
        this.topAclRulePriority = topAclRulePriority;
    }

    public Integer getTopRuleRowStatus() {
        return topRuleRowStatus;
    }

    public void setTopRuleRowStatus(Integer topRuleRowStatus) {
        this.topRuleRowStatus = topRuleRowStatus;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("AclListTable");
        sb.append("{entityId=").append(entityId);
        sb.append(", topAclListIndex=").append(topAclListIndex);
        sb.append(", topAclDescription='").append(topAclDescription).append('\'');
        sb.append(", topAclRuleNum=").append(topAclRuleNum);
        sb.append(", topAclRulePriority=").append(topAclRulePriority);
        sb.append(", topRuleRowStatus=").append(topRuleRowStatus);
        sb.append('}');
        return sb.toString();
    }

}
