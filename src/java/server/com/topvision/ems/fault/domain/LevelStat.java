package com.topvision.ems.fault.domain;

public class LevelStat extends Level {
    private static final long serialVersionUID = 1655676005063830171L;
    private Long count;
    private Long total;
    private Double percentage;
    private String percentageStr;

    /**
     * 
     * @return count
     */
    public Long getCount() {
        return count;
    }

    /**
     * 
     * @return percentage
     */
    public Double getPercentage() {
        return percentage;
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
     * @return total
     */
    public Long getTotal() {
        return total;
    }

    /**
     * 
     * @param count
     */
    public void setCount(Long count) {
        this.count = count;
    }

    /**
     * 
     * @param percentage
     */
    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    /**
     * 
     * @param percentageStr
     */
    public void setPercentageStr(String percentageStr) {
        this.percentageStr = percentageStr;
    }

    /**
     * 
     * @param total
     */
    public void setTotal(Long total) {
        this.total = total;
    }

}
