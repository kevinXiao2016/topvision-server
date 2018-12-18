/***********************************************************************
 * $Id: OperationLog.java,v1.0 2011-11-20 下午04:34:50 $
 * 
 * @author: huqiao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.domain;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.domain.BaseEntity;

/**
 * @author huqiao
 * @created @2011-11-20-下午04:34:50
 * 
 */
@Alias("operationLog")
public class OperationLog extends BaseEntity implements com.topvision.framework.dao.mybatis.AliasesSuperType {
    private static final long serialVersionUID = -9121481254194573416L;

    public static final int SUCCESS = 1;
    public static final int FAILURE = 2;
    private Integer operId;
    private String operName;
    private String clientIpAddress;
    private Integer status;
    private Timestamp operTime;
    private String entityIp;
    private Long entityId;
    private String operAction;
    private String operTimeString;

    public OperationLog() {
        super();
    }

    public OperationLog(String operName, String clientIpAddress, Integer status, Long entityId, String operAction) {
        super();
        this.operName = operName;
        this.clientIpAddress = clientIpAddress;
        this.status = status;
        this.operTime = new Timestamp(System.currentTimeMillis());
        this.entityId = entityId;
        this.operAction = operAction;
    }

    /**
     * @return the operId
     */
    public Integer getOperId() {
        return operId;
    }

    /**
     * @param operId
     *            the operId to set
     */
    public void setOperId(Integer operId) {
        this.operId = operId;
    }

    /**
     * @return the operName
     */
    public String getOperName() {
        return operName;
    }

    /**
     * @param operName
     *            the operName to set
     */
    public void setOperName(String operName) {
        this.operName = operName;
    }

    /**
     * @return the status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @param status
     *            the status to set
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * @return the operTime
     */
    public Timestamp getOperTime() {
        return operTime;
    }

    /**
     * @param operTime
     *            the operTime to set
     */
    public void setOperTime(Timestamp operTime) {
        this.operTime = operTime;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        operTimeString = sdf.format(operTime);
    }

    /**
     * @return the entityId
     */
    public Long getEntityId() {
        return entityId;
    }

    /**
     * @param entityId
     *            the entityId to set
     */
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    /**
     * @return the operAction
     */
    public String getOperAction() {
        return operAction;
    }

    /**
     * @param operAction
     *            the operAction to set
     */
    public void setOperAction(String operAction) {
        this.operAction = operAction;
    }

    /**
     * @return the entityIp
     */
    public String getEntityIp() {
        return entityIp;
    }

    /**
     * @param entityIp
     *            the entityIp to set
     */
    public void setEntityIp(String entityIp) {
        this.entityIp = entityIp;
    }

    /**
     * @return the operTimeString
     */
    public String getOperTimeString() {
        return operTimeString;
    }

    /**
     * @param operTimeString
     *            the operTimeString to set
     */
    public void setOperTimeString(String operTimeString) {
        this.operTimeString = operTimeString;
    }

    /**
     * @return the clientIpAddress
     */
    public String getClientIpAddress() {
        return clientIpAddress;
    }

    /**
     * @param clientIpAddress
     *            the clientIpAddress to set
     */
    public void setClientIpAddress(String clientIpAddress) {
        this.clientIpAddress = clientIpAddress;
    }

}
