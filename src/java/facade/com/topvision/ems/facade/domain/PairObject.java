/***********************************************************************
 * $Id: PairObject.java,v 1.1 Mar 20, 2009 11:29:15 PM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.ems.facade.domain;

import java.io.Serializable;

/**
 * @Create Date Mar 20, 2009 11:29:15 PM
 * 
 * @author kelers
 * 
 */
public class PairObject<M, N> implements Serializable {
    private static final long serialVersionUID = -7798601838342221858L;
    private M first;
    private N second;

    public PairObject(M m, N n) {
        first = m;
        second = n;
    }

    /**
     * @return the first
     */
    public M getFirst() {
        return first;
    }

    /**
     * @return the second
     */
    public N getSecond() {
        return second;
    }

    /**
     * @param first
     *            the first to set
     */
    public void setFirst(M first) {
        this.first = first;
    }

    /**
     * @param second
     *            the second to set
     */
    public void setSecond(N second) {
        this.second = second;
    }
}
