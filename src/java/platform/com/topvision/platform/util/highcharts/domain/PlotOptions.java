/***********************************************************************
 * $ PlotOptions.java,v1.0 2012-7-12 14:27:05 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.util.highcharts.domain;

import java.io.Serializable;

/**
 * @author jay
 * @created @2012-7-12-14:27:05
 */
public class PlotOptions implements Serializable {
    private static final long serialVersionUID = 7342180206202447869L;
    private Area area;
    private Areaspline areaspline;
    private Bar bar;
    private Column column;
    private Line line;
    private Pie pie;
    private Series series;
    private Scatter scatter;
    private Spline spline;

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public Areaspline getAreaspline() {
        return areaspline;
    }

    public void setAreaspline(Areaspline areaspline) {
        this.areaspline = areaspline;
    }

    public Bar getBar() {
        return bar;
    }

    public void setBar(Bar bar) {
        this.bar = bar;
    }

    public Column getColumn() {
        return column;
    }

    public void setColumn(Column column) {
        this.column = column;
    }

    public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public Pie getPie() {
        return pie;
    }

    public void setPie(Pie pie) {
        this.pie = pie;
    }

    public Scatter getScatter() {
        return scatter;
    }

    public void setScatter(Scatter scatter) {
        this.scatter = scatter;
    }

    public Series getSeries() {
        return series;
    }

    public void setSeries(Series series) {
        this.series = series;
    }

    public Spline getSpline() {
        return spline;
    }

    public void setSpline(Spline spline) {
        this.spline = spline;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("PlotOptions");
        sb.append("{area=").append(area);
        sb.append(", areaspline=").append(areaspline);
        sb.append(", bar=").append(bar);
        sb.append(", column=").append(column);
        sb.append(", line=").append(line);
        sb.append(", pie=").append(pie);
        sb.append(", series=").append(series);
        sb.append(", scatter=").append(scatter);
        sb.append(", spline=").append(spline);
        sb.append('}');
        return sb.toString();
    }

    public class Area {
    }

    public class Areaspline {

    }

    public class Bar {

    }

    public class Column {

    }

    public class Line {
        private Integer lineWidth;
        private Marker marker;
        private Boolean shadow;
        private States states;

        public Integer getLineWidth() {
            return lineWidth;
        }

        public void setLineWidth(Integer lineWidth) {
            this.lineWidth = lineWidth;
        }

        public Marker getMarker() {
            return marker;
        }

        public void setMarker(Marker marker) {
            this.marker = marker;
        }

        public Boolean getShadow() {
            return shadow;
        }

        public void setShadow(Boolean shadow) {
            this.shadow = shadow;
        }

        public States getStates() {
            return states;
        }

        public void setStates(States states) {
            this.states = states;
        }
    }

    public class Pie {

    }

    public class Series {

    }

    public class Scatter {

    }

    public class Spline {

    }

    public class Marker {
        private Boolean enabled;
        private States states;

        public Boolean getEnabled() {
            return enabled;
        }

        public void setEnabled(Boolean enabled) {
            this.enabled = enabled;
        }

        public States getStates() {
            return states;
        }

        public void setStates(States states) {
            this.states = states;
        }
    }

    public class States {
        private Hover hover;

        public Hover getHover() {
            return hover;
        }

        public void setHover(Hover hover) {
            this.hover = hover;
        }
    }

    public class Hover {
        private Boolean enabled;
        private Integer lineWidth;
        private Integer radius;

        public Integer getLineWidth() {
            return lineWidth;
        }

        public void setLineWidth(Integer lineWidth) {
            this.lineWidth = lineWidth;
        }

        public Integer getRadius() {
            return radius;
        }

        public void setRadius(Integer radius) {
            this.radius = radius;
        }

        public Boolean getEnabled() {
            return enabled;
        }

        public void setEnabled(Boolean enabled) {
            this.enabled = enabled;
        }
    }
}
