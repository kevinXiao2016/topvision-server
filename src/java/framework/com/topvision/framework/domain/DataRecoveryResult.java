/***********************************************************************
 * $Id: DataRecoveryResult.java,v1.0 2012-8-7 下午08:07:34 $
 * 
 * @author: RodJohn
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.domain;


/**
 * @author RodJohn
 * @created @2012-8-7-下午08:07:34
 *
 */
public class DataRecoveryResult extends BaseEntity {
    private static final long serialVersionUID = 1263592810329583145L;
    private String errorInfo;
    private int[] recoveryResult;
    /**
     * @return the errorInfo
     */
    public String getErrorInfo() {
        return errorInfo;
    }
    /**
     * @param errorInfo the errorInfo to set
     */
    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }
    /**
     * @return the recoveryResult
     */
    public int[] getRecoveryResult() {
        return recoveryResult;
    }
    /**
     * @param recoveryResult the recoveryResult to set
     */
    public void setRecoveryResult(int[] recoveryResult) {
        this.recoveryResult = recoveryResult;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("DataRecoveryResult [errorInfo=");
        builder.append(errorInfo);
        builder.append(", recoveryResult=");
        builder.append(recoveryResult);
        builder.append("]");
        return builder.toString();
    }
 
    
}
