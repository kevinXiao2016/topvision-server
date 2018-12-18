/**
 * 
 */
package com.topvision.ems.epon.domain;

import java.util.Date;

/**
 * @author kelers
 * 
 */
public class TimeSegment {

    private Date startTime;
    private Date endTime;
    private long timeLength;

    public TimeSegment(Date startTime, Date endTime) {
        setStartTime(startTime);
        setEndTime(endTime);
        timeLength = endTime.getTime() - startTime.getTime();
    }

    public Date getEndTime() {
        return endTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public long getTimeLength() {
        return timeLength;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public void setTimeLength(long timeLength) {
        this.timeLength = timeLength;
    }

}
