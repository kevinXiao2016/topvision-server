package com.topvision.ems.fault.domain;

public class AlertTypeStat extends AlertType {
    private static final long serialVersionUID = 5656080007682237802L;
    private Integer count;
    private String percentageStr;

    /**
     * 
     * @return count
     */
    public Integer getCount() {
        return count;
    }

    /**
     * 
     * @return percentageStr
     */
    public String getPercentageStr() {
        return percentageStr;
    }

    /**
     * 
     * @param count
     */
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
     * 
     * @param percentageStr
     */
    public void setPercentageStr(String percentageStr) {
        this.percentageStr = percentageStr;
    }

}
