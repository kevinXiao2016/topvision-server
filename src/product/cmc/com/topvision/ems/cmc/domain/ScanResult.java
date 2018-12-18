/***********************************************************************
 * $Id: CmActions.java,v1.0 2013-2-24 12:06:07 $
 *
 * @author: huangdongsheng
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.domain;

import java.util.List;

/**
 * 扫描文件结果
 * 
 * @author huangdongsheng
 * 
 */
public class ScanResult<T> {
    private List<T> domainList;
    private List<Integer> errorList;

    /**
     * @return the domainList
     */
    public List<T> getDomainList() {
        return domainList;
    }

    /**
     * @param domainList
     *            the domainList to set
     */
    public void setDomainList(List<T> domainList) {
        this.domainList = domainList;
    }

    /**
     * @return the errorList
     */
    public List<Integer> getErrorList() {
        return errorList;
    }

    /**
     * @param errorList
     *            the errorList to set
     */
    public void setErrorList(List<Integer> errorList) {
        this.errorList = errorList;
    }

}
