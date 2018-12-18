/***********************************************************************
 * $ Series.java,v1.0 2012-7-12 14:27:38 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.util.highcharts.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @author jay
 * @created @2012-7-12-14:27:38
 */
public class Series implements Serializable {
    private static final long serialVersionUID = 7342180206202447869L;
    private String name;
    private String unit;
    private String color;
    private String type;
    private Double max;
    private Double min;
    private Double avg;
    private Double cur;
    private Integer xAxis;
    private Integer yAxis;
    private List<List<Double>> data;

    public Double getAvg() {
        return avg;
    }

    public void setAvg(Double avg) {
        this.avg = avg;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Double getCur() {
        return cur;
    }

    public void setCur(Double cur) {
        this.cur = cur;
    }

    public List<List<Double>> getData() {
        return data;
    }

    public void setData(List<List<Double>> data) {
        this.data = data;
    }

    public Double getMax() {
        return max;
    }

    public void setMax(Double max) {
        this.max = max;
    }

    public Double getMin() {
        return min;
    }

    public void setMin(Double min) {
        this.min = min;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getXAxis() {
        return xAxis;
    }

    public void setXAxis(Integer xAxis) {
        this.xAxis = xAxis;
    }

    public Integer getYAxis() {
        return yAxis;
    }

    public void setYAxis(Integer yAxis) {
        this.yAxis = yAxis;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Series");
        sb.append("{avg=").append(avg);
        sb.append(", name='").append(name).append('\'');
        sb.append(", unit='").append(unit).append('\'');
        sb.append(", color='").append(color).append('\'');
        sb.append(", type='").append(type).append('\'');
        sb.append(", max=").append(max);
        sb.append(", min=").append(min);
        sb.append(", cur=").append(cur);
        sb.append(", xAxis=").append(xAxis);
        sb.append(", yAxis=").append(yAxis);
        sb.append(", data=").append(data);
        sb.append('}');
        return sb.toString();
    }
}
