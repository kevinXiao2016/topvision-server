/***********************************************************************
 * $Id: FlowEvent.java,v 1.1 Sep 17, 2009 9:59:09 AM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.platform.message.event;

/**
 * @Create Date Sep 17, 2009 9:59:09 AM
 * 
 * @author kelers
 * 
 */
public class FlowEvent extends EmsEventObject<FlowListener> {
    private static final long serialVersionUID = 4851544329380079294L;
    private long linkId;
    private double flow;
    private double rate;
    private double usage;

    /**
     * @param source
     */
    public FlowEvent(Object source) {
        super(source);
    }

    /**
     * @return the flow
     */
    public final double getFlow() {
        return flow;
    }

    /**
     * @return the linkId
     */
    public final long getLinkId() {
        return linkId;
    }

    /**
     * @return the rate
     */
    public final double getRate() {
        return rate;
    }

    /**
     * @return the usage
     */
    public final double getUsage() {
        return usage;
    }

    /**
     * @param flow
     *            the flow to set
     */
    public final void setFlow(double flow) {
        this.flow = flow;
    }

    /**
     * @param linkId
     *            the linkId to set
     */
    public final void setLinkId(long linkId) {
        this.linkId = linkId;
    }

    /**
     * @param rate
     *            the rate to set
     */
    public final void setRate(double rate) {
        this.rate = rate;
    }

    /**
     * @param usage
     *            the usage to set
     */
    public final void setUsage(double usage) {
        this.usage = usage;
    }
}
