/***********************************************************************
 * $ Xaxis.java,v1.0 2012-7-12 14:33:27 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.util.highcharts.domain;

import java.util.List;

/**
 * @author jay
 * @created @2012-7-12-14:33:27
 */
public class Xaxis {
    /**
     * Whether to allow decimals in this axis' ticks. When counting integers, like persons or hits
     * on a web page, decimals must be avoided in the axis tick labels. Defaults to true.
     */
    private Boolean allowDecimals;
    /**
     * When using an alternate grid color, a band is painted across the plot area between every
     * other grid line. Minor grid lines do not affect it. Defaults to null.
     */
    private String alternateGridColor;
    /**
     * If categories are present for the xAxis, names are used instead of numbers for that axis.
     * Example: categories: ['Apples', 'Bananas', 'Oranges'] Defaults to [].
     */
    private List<String> categories;
    /**
     * Whether to force the axis to end on a tick. Use this option with the maxPadding option to
     * control the axis end. Defaults to false
     */
    private Boolean endOnTick;
    /**
     * Color of the grid lines extending the ticks across the plot area. Defaults to "#C0C0C0".
     */
    private String gridLineColor;
    /**
     * The dash or dot style of the grid lines. For possible values, see this demonstration.
     * Defaults to Solid.
     */
    private String gridLineDashStyle;
    /**
     * The width of the grid lines extending the ticks across the plot area. Defaults to 0.
     */
    private String gridLineWidth;
    /**
     * An id for the axis. This can be used after render time to get a pointer to the axis object
     * through chart.get(). Defaults to null.
     */
    private String id;
    /**
     * Configuration object for the axis labels, usually displaying the number for each tick.
     */
    private Labels labels;
    /**
     * The color of the line marking the axis itself. Defaults to "#C0D0E0".
     */
    private String lineColor;
    /**
     * The width of the line marking the axis itself. Defaults to 1
     */
    private Integer lineWidth;
    /**
     * Index of another axis that this axis is linked to. When an axis is linked to a master axis,
     * it will take the same extremes as the master, but as assigned by min or max or by
     * setExtremes. It can be used to show additional info, or to ease reading the chart by
     * duplicating the scales. Defaults to null.
     */
    private Integer linkedTo;
    /**
     * The maximum value of the axis. If null, the max value is automatically calculated. If the
     * endOnTick option is true, the max value might be rounded up. The actual maximum value is also
     * influenced by chart.alignTicks. Defaults to null.
     */
    private Double max;
    /**
     * Padding of the max value relative to the length of the axis. A padding of 0.05 will make a
     * 100px axis 5px longer. This is useful when you don't want the highest data value to appear on
     * the edge of the plot area. When the axis' max option is set or a max extreme is set using
     * axis.setExtremes(), the maxPadding will be ignored. Defaults to 0.01.
     */
    private Integer maxPadding;
    /**
     * The minimum value of the axis. If null the min value is automatically calculated. If the
     * startOnTick option is true, the min value might be rounded down. Defaults to null.
     */
    private Double min;
    /**
     * Color of the minor, secondary grid lines. Defaults to #E0E0E0.
     */
    private String minorGridLineColor;
    /**
     * The dash or dot style of the minor grid lines. For possible values, see this demonstration.
     * Defaults to Solid
     */
    private String minorGridLineDashStyle;
    /**
     * Width of the minor, secondary grid lines. Defaults to 1.
     */
    private Integer minorGridLineWidth;
    /**
     * Color for the minor tick marks. Defaults to #A0A0A0.
     */
    private String minorTickColor;
    /**
     * Tick interval in scale units for the minor ticks. On a linear axis, if "auto", the minor tick
     * interval is calculated as a fifth of the tickInterval. If null, minor ticks are not shown.
     * 
     * On logarithmic axes, the unit is the power of the value. For example, setting the
     * minorTickInterval to 1 puts one tick on each of 0.1, 1, 10, 100 etc. Setting the
     * minorTickInterval to 0.1 produces 9 ticks between 1 and 10, 10 and 100 etc. A
     * minorTickInterval of "auto" on a log axis results in a best guess, attempting to enter
     * approximately 5 minor ticks between each major tick.
     * 
     * Defaults to null.
     */
    private Integer minorTickInterval;
    /**
     * The pixel length of the minor tick marks. Defaults to 2.
     */
    private Integer minorTickLength;
    /**
     * The position of the minor tick marks relative to the axis line. Can be one of inside and
     * outside. Defaults to outside.
     */
    private String minorTickPosition;
    /**
     * The pixel width of the minor tick mark. Defaults to 0.
     */
    private Integer minorTickWidth;
    /**
     * Padding of the min value relative to the length of the axis. A padding of 0.05 will make a
     * 100px axis 5px longer. This is useful when you don't want the lowest data value to appear on
     * the edge of the plot area. When the axis' min option is set or a min extreme is set using
     * axis.setExtremes(), the minPadding will be ignored. Defaults to 0.01.
     */
    private Integer minPadding;
    /**
     * The minimum range to display on this axis. The entire axis will not be allowed to span over a
     * smaller interval than this. For example, for a datetime axis the main unit is milliseconds.
     * If minRange is set to 3600000, you can't zoom in more than to one hour.
     * 
     * The default minRange for the x axis is five times the smallest interval between any of the
     * data points.
     * 
     * On a logarithmic axis, the unit for the minimum range is the power. So a minRange of 1 means
     * that the axis can be zoomed to 10-100, 100-1000, 1000-10000 etc.
     */
    private Integer minRange;
    /**
     * The distance in pixels from the plot area to the axis line. A positive offset moves the axis
     * with it's line, labels and ticks away from the plot area. This is typically used when two or
     * more axes are displayed on the same side of the plot. Defaults to 0.
     */
    private Integer offset;
    /**
     * Whether to display the axis on the opposite side of the normal. The normal is on the left
     * side for vertical axes and bottom for horizontal, so the opposite sides will be right and top
     * respectively. This is typically used with dual or multiple axes. Defaults to false.
     */
    private Boolean opposite;
    /**
     * An array of configuration objects for plot bands colouring parts of the plot area background.
     * Defaults to null.
     */
    private List<PlotBands> plotBands;
    private List<PlotLines> plotLines;
    /**
     * Whether to reverse the axis so that the highest number is closest to origo. If the chart is
     * inverted, the x axis is reversed by default. Defaults to false
     */
    private Boolean reversed;
    /**
     * Whether to show the first tick label. Defaults to true.
     */
    private Boolean showFirstLabel;
    /**
     * Whether to show the last tick label. Defaults to false.
     */
    private Boolean showLastLabel;
    /**
     * For datetime axes, this decides where to put the tick between weeks. 0 = Sunday, 1 = Monday.
     * Defaults to 1.
     */
    private Integer startOfWeek;
    /**
     * Whether to force the axis to start on a tick. Use this option with the maxPadding option to
     * control the axis start. Defaults to false.
     */
    private Boolean startOnTick;
    /**
     * Color for the main tick marks. Defaults to #C0D0E0
     */
    private String tickColor;
    /**
     * The interval of the tick marks in axis units. When null, the tick interval is computed to
     * approximately follow the tickPixelInterval on linear and datetime axes. On categorized axes,
     * a null tickInterval will default to 1, one category. Note that datetime axes are based on
     * milliseconds, so for example an interval of one day is expressed as 24 * 3600 * 1000.
     * 
     * On logarithmic axes, the tickInterval is based on powers, so a tickInterval of 1 means one
     * tick on each of 0.1, 1, 10, 100 etc. A tickInterval of 2 means a tick of 0.1, 10, 1000 etc. A
     * tickInterval of 0.2 puts a tick on 0.1, 0.2, 0.4, 0.6, 0.8, 1, 2, 4, 6, 8, 10, 20, 40 etc.
     * 
     * Defaults to null.
     */
    private Integer tickInterval;
    /**
     * The pixel length of the main tick marks. Defaults to 5.
     */
    private Integer tickLength;
    /**
     * For categorized axes only. If "on" the tick mark is placed in the center of the category, if
     * "between" the tick mark is placed between categories. Defaults to "between".
     */
    private String tickmarkPlacement;
    /**
     * If tickInterval is null this option sets the approximate pixel interval of the tick marks.
     * Not applicable to categorized axis. Defaults to 72 for the Y axis and 100 for the X axis.
     */
    private Integer tickPixelInterval;
    /**
     * The position of the major tick marks relative to the axis line. Can be one of inside and
     * outside. Defaults to "outside".
     */
    private String tickPosition;
    /**
     * The pixel width of the major tick marks. Defaults to 1.
     */
    private Integer tickWidth;
    /**
     * Configuration object for the axis title.
     */
    private Title title;
    /**
     * The type of axis. Can be one of "linear", "logarithmic" or "datetime". In a datetime axis,
     * the numbers are given in milliseconds, and tick marks are placed on appropriate values like
     * full hours or days. Defaults to "linear".
     */
    private String type;

    public Boolean getAllowDecimals() {
        return allowDecimals;
    }

    public void setAllowDecimals(Boolean allowDecimals) {
        this.allowDecimals = allowDecimals;
    }

    public String getAlternateGridColor() {
        return alternateGridColor;
    }

    public void setAlternateGridColor(String alternateGridColor) {
        this.alternateGridColor = alternateGridColor;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public Boolean getEndOnTick() {
        return endOnTick;
    }

    public void setEndOnTick(Boolean endOnTick) {
        this.endOnTick = endOnTick;
    }

    public String getGridLineColor() {
        return gridLineColor;
    }

    public void setGridLineColor(String gridLineColor) {
        this.gridLineColor = gridLineColor;
    }

    public String getGridLineDashStyle() {
        return gridLineDashStyle;
    }

    public void setGridLineDashStyle(String gridLineDashStyle) {
        this.gridLineDashStyle = gridLineDashStyle;
    }

    public String getGridLineWidth() {
        return gridLineWidth;
    }

    public void setGridLineWidth(String gridLineWidth) {
        this.gridLineWidth = gridLineWidth;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Labels getLabels() {
        return labels;
    }

    public void setLabels(Labels labels) {
        this.labels = labels;
    }

    public String getLineColor() {
        return lineColor;
    }

    public void setLineColor(String lineColor) {
        this.lineColor = lineColor;
    }

    public Integer getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(Integer lineWidth) {
        this.lineWidth = lineWidth;
    }

    public Integer getLinkedTo() {
        return linkedTo;
    }

    public void setLinkedTo(Integer linkedTo) {
        this.linkedTo = linkedTo;
    }

    public Double getMax() {
        return max;
    }

    public void setMax(Double max) {
        this.max = max;
    }

    public Integer getMaxPadding() {
        return maxPadding;
    }

    public void setMaxPadding(Integer maxPadding) {
        this.maxPadding = maxPadding;
    }

    public Double getMin() {
        return min;
    }

    public void setMin(Double min) {
        this.min = min;
    }

    public String getMinorGridLineColor() {
        return minorGridLineColor;
    }

    public void setMinorGridLineColor(String minorGridLineColor) {
        this.minorGridLineColor = minorGridLineColor;
    }

    public String getMinorGridLineDashStyle() {
        return minorGridLineDashStyle;
    }

    public void setMinorGridLineDashStyle(String minorGridLineDashStyle) {
        this.minorGridLineDashStyle = minorGridLineDashStyle;
    }

    public Integer getMinorGridLineWidth() {
        return minorGridLineWidth;
    }

    public void setMinorGridLineWidth(Integer minorGridLineWidth) {
        this.minorGridLineWidth = minorGridLineWidth;
    }

    public String getMinorTickColor() {
        return minorTickColor;
    }

    public void setMinorTickColor(String minorTickColor) {
        this.minorTickColor = minorTickColor;
    }

    public Integer getMinorTickInterval() {
        return minorTickInterval;
    }

    public void setMinorTickInterval(Integer minorTickInterval) {
        this.minorTickInterval = minorTickInterval;
    }

    public Integer getMinorTickLength() {
        return minorTickLength;
    }

    public void setMinorTickLength(Integer minorTickLength) {
        this.minorTickLength = minorTickLength;
    }

    public String getMinorTickPosition() {
        return minorTickPosition;
    }

    public void setMinorTickPosition(String minorTickPosition) {
        this.minorTickPosition = minorTickPosition;
    }

    public Integer getMinorTickWidth() {
        return minorTickWidth;
    }

    public void setMinorTickWidth(Integer minorTickWidth) {
        this.minorTickWidth = minorTickWidth;
    }

    public Integer getMinPadding() {
        return minPadding;
    }

    public void setMinPadding(Integer minPadding) {
        this.minPadding = minPadding;
    }

    public Integer getMinRange() {
        return minRange;
    }

    public void setMinRange(Integer minRange) {
        this.minRange = minRange;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Boolean getOpposite() {
        return opposite;
    }

    public void setOpposite(Boolean opposite) {
        this.opposite = opposite;
    }

    public Boolean getReversed() {
        return reversed;
    }

    public void setReversed(Boolean reversed) {
        this.reversed = reversed;
    }

    public Boolean getShowFirstLabel() {
        return showFirstLabel;
    }

    public void setShowFirstLabel(Boolean showFirstLabel) {
        this.showFirstLabel = showFirstLabel;
    }

    public Boolean getShowLastLabel() {
        return showLastLabel;
    }

    public void setShowLastLabel(Boolean showLastLabel) {
        this.showLastLabel = showLastLabel;
    }

    public Integer getStartOfWeek() {
        return startOfWeek;
    }

    public void setStartOfWeek(Integer startOfWeek) {
        this.startOfWeek = startOfWeek;
    }

    public Boolean getStartOnTick() {
        return startOnTick;
    }

    public void setStartOnTick(Boolean startOnTick) {
        this.startOnTick = startOnTick;
    }

    public String getTickColor() {
        return tickColor;
    }

    public void setTickColor(String tickColor) {
        this.tickColor = tickColor;
    }

    public Integer getTickLength() {
        return tickLength;
    }

    public void setTickLength(Integer tickLength) {
        this.tickLength = tickLength;
    }

    public String getTickmarkPlacement() {
        return tickmarkPlacement;
    }

    public void setTickmarkPlacement(String tickmarkPlacement) {
        this.tickmarkPlacement = tickmarkPlacement;
    }

    public Integer getTickPixelInterval() {
        return tickPixelInterval;
    }

    public void setTickPixelInterval(Integer tickPixelInterval) {
        this.tickPixelInterval = tickPixelInterval;
    }

    public String getTickPosition() {
        return tickPosition;
    }

    public void setTickPosition(String tickPosition) {
        this.tickPosition = tickPosition;
    }

    public Integer getTickWidth() {
        return tickWidth;
    }

    public void setTickWidth(Integer tickWidth) {
        this.tickWidth = tickWidth;
    }

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<PlotBands> getPlotBands() {
        return plotBands;
    }

    public void setPlotBands(List<PlotBands> plotBands) {
        this.plotBands = plotBands;
    }

    public List<PlotLines> getPlotLines() {
        return plotLines;
    }

    public void setPlotLines(List<PlotLines> plotLines) {
        this.plotLines = plotLines;
    }

    public Integer getTickInterval() {
        return tickInterval;
    }

    public void setTickInterval(Integer tickInterval) {
        this.tickInterval = tickInterval;
    }

    class Labels {
        /**
         * What part of the string the given position is anchored to. Can be one of "left", "center"
         * or "right". In inverted charts, x axis label alignment and y axis alignment are swapped.
         * Defaults to "center".
         */
        private String align;
        /**
         * Enable or disable the axis labels. Defaults to true
         */
        private Boolean enabled;
        /**
         * How to handle overflowing labels on horizontal axis. Can be undefined or "justify". If
         * "justify", labels will not render outside the plot area. If there is room to move it, it
         * will be aligned to the edge, else it will be removed. Defaults to undefined.
         */
        private String overflow;
        /**
         * Rotation of the labels in degrees. Defaults to 0.
         */
        private Integer rotation;
        /**
         * Horizontal axes only. The number of lines to spread the labels over to make room or
         * tighter labels. Defaults to null.
         */
        private Integer staggerLines;
        /**
         * To show only every n'th label on the axis, set the step to n. Setting the step to 2 shows
         * every other label. Defaults to null
         */
        private Integer step;
        /**
         * The x position offset of the label relative to the tick position on the axis. Defaults to
         * 0.
         */
        private Integer x;
        /**
         * The y position offset of the label relative to the tick position on the axis. Defaults to
         * 0.
         */
        private Integer y;

        public String getAlign() {
            return align;
        }

        public void setAlign(String align) {
            this.align = align;
        }

        public Boolean getEnabled() {
            return enabled;
        }

        public void setEnabled(Boolean enabled) {
            this.enabled = enabled;
        }

        public String getOverflow() {
            return overflow;
        }

        public void setOverflow(String overflow) {
            this.overflow = overflow;
        }

        public Integer getRotation() {
            return rotation;
        }

        public void setRotation(Integer rotation) {
            this.rotation = rotation;
        }

        public Integer getStaggerLines() {
            return staggerLines;
        }

        public void setStaggerLines(Integer staggerLines) {
            this.staggerLines = staggerLines;
        }

        public Integer getStep() {
            return step;
        }

        public void setStep(Integer step) {
            this.step = step;
        }

        public Integer getX() {
            return x;
        }

        public void setX(Integer x) {
            this.x = x;
        }

        public Integer getY() {
            return y;
        }

        public void setY(Integer y) {
            this.y = y;
        }
    }

    class Title {
        /**
         * Alignment of the title relative to the axis values. Possible values are "low", "middle"
         * or "high". Defaults to "middle".
         */
        private String align;
        /**
         * The pixel distance between the axis labels or line and the title. Defaults to 0 for
         * horizontal axes, 10 for vertical
         */
        private Integer margin;
        /**
         * The distance of the axis title from the axis line. By default, this distance is computed
         * from the offset width of the labels, the labels' distance from the axis and the title's
         * margin. However when the offset option is set, it overrides all this. Defaults to
         * undefined.
         */
        private Integer offset;
        /**
         * The rotation of the text in degrees. 0 is horizontal, 270 is vertical reading from bottom
         * to top. Defaults to 0.
         */
        private Integer rotation;
        /**
         * The actual text of the axis title. It can contain basic HTML text markup like <b>, <i>
         * and spans with style. Defaults to null.
         */
        private String text;

        public String getAlign() {
            return align;
        }

        public void setAlign(String align) {
            this.align = align;
        }

        public Integer getMargin() {
            return margin;
        }

        public void setMargin(Integer margin) {
            this.margin = margin;
        }

        public Integer getOffset() {
            return offset;
        }

        public void setOffset(Integer offset) {
            this.offset = offset;
        }

        public Integer getRotation() {
            return rotation;
        }

        public void setRotation(Integer rotation) {
            this.rotation = rotation;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    class PlotBands {
        /**
         * The color of the plot band. Defaults to null.
         */
        private String color;
        /**
         * The start position of the plot band in axis units. Defaults to null
         */
        private Integer from;
        /**
         * The end position of the plot band in axis units. Defaults to null.
         */
        private Integer to;
        /**
         * The z index of the plot band within the chart. Defaults to null.
         */
        private Integer zIndex;
        /**
         * An id used for identifying the plot band in Axis.removePlotBand. Defaults to null.
         */
        private String id;
        /**
         * Text labels for the plot bands.
         */
        private Label label;

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public Integer getFrom() {
            return from;
        }

        public void setFrom(Integer from) {
            this.from = from;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Label getLabel() {
            return label;
        }

        public void setLabel(Label label) {
            this.label = label;
        }

        public Integer getTo() {
            return to;
        }

        public void setTo(Integer to) {
            this.to = to;
        }

        public Integer getZIndex() {
            return zIndex;
        }

        public void setZIndex(Integer zIndex) {
            this.zIndex = zIndex;
        }

        class Label {
            /**
             * Horizontal alignment of the label. Can be one of "left", "center" or "right".
             * Defaults to "center".
             */
            private String align;
            /**
             * Vertical alignment of the label relative to the plot band. Can be one of "top",
             * "middle" or "bottom". Defaults to "top". Try it: Vertically centered label
             */
            private String verticalAlign;
            /**
             * Rotation of the text label in degrees Defaults to 0.
             */
            private Integer rotation;
            /**
             * Horizontal position relative the alignment. Default varies by orientation.
             */
            private Integer x;
            /**
             * Vertical position of the text baseline relative to the alignment. Default varies by
             * orientation.
             */
            private Integer y;
            /**
             * The string text itself. A subset of HTML is supported.
             */

            private String text;
            /**
             * The text alignment for the label. While align determines where the texts anchor point
             * is placed within the plot band, textAlign determines how the text is aligned against
             * its anchor point. Possible values are "left", "center" and "right". Defaults to the
             * same as the align option.
             */
            private String textAlign;

            public String getAlign() {
                return align;
            }

            public void setAlign(String align) {
                this.align = align;
            }

            public Integer getRotation() {
                return rotation;
            }

            public void setRotation(Integer rotation) {
                this.rotation = rotation;
            }

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }

            public String getTextAlign() {
                return textAlign;
            }

            public void setTextAlign(String textAlign) {
                this.textAlign = textAlign;
            }

            public String getVerticalAlign() {
                return verticalAlign;
            }

            public void setVerticalAlign(String verticalAlign) {
                this.verticalAlign = verticalAlign;
            }

            public Integer getX() {
                return x;
            }

            public void setX(Integer x) {
                this.x = x;
            }

            public Integer getY() {
                return y;
            }

            public void setY(Integer y) {
                this.y = y;
            }
        }

    }

    class PlotLines {

        /**
         * The color of the line. Defaults to null.
         */
        private String color;
        /**
         * The dashing or dot style for the plot line. For possible values see this overview.
         * Defaults to Solid
         */
        private String dashStyle;
        /**
         * An id used for identifying the plot line in Axis.removePlotLine. Defaults to null.
         */
        private Integer id;
        /**
         * Text labels for the plot lines.
         */
        private Label label;
        /**
         * The position of the line in axis units. Defaults to null.
         */
        private Integer value;
        /**
         * The width or thickness of the plot line. Defaults to null.
         */
        private Integer width;
        /**
         * The z index of the plot line within the chart. Defaults to null.
         */
        private Integer zIndex;

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getDashStyle() {
            return dashStyle;
        }

        public void setDashStyle(String dashStyle) {
            this.dashStyle = dashStyle;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Label getLabel() {
            return label;
        }

        public void setLabel(Label label) {
            this.label = label;
        }

        public Integer getValue() {
            return value;
        }

        public void setValue(Integer value) {
            this.value = value;
        }

        public Integer getWidth() {
            return width;
        }

        public void setWidth(Integer width) {
            this.width = width;
        }

        public Integer getZIndex() {
            return zIndex;
        }

        public void setZIndex(Integer zIndex) {
            this.zIndex = zIndex;
        }

        class Label {
            /**
             * Horizontal alignment of the label. Can be one of "left", "center" or "right".
             * Defaults to "center".
             */
            private String align;
            /**
             * Vertical alignment of the label relative to the plot band. Can be one of "top",
             * "middle" or "bottom". Defaults to "top". Try it: Vertically centered label
             */
            private String verticalAlign;
            /**
             * Rotation of the text label in degrees Defaults to 0.
             */
            private Integer rotation;
            /**
             * Horizontal position relative the alignment. Default varies by orientation.
             */
            private Integer x;
            /**
             * Vertical position of the text baseline relative to the alignment. Default varies by
             * orientation.
             */
            private Integer y;
            /**
             * The text alignment for the label. While align determines where the texts anchor point
             * is placed within the plot band, textAlign determines how the text is aligned against
             * its anchor point. Possible values are "left", "center" and "right". Defaults to the
             * same as the align option.
             */
            private String textAlign;

            public String getAlign() {
                return align;
            }

            public void setAlign(String align) {
                this.align = align;
            }

            public Integer getRotation() {
                return rotation;
            }

            public void setRotation(Integer rotation) {
                this.rotation = rotation;
            }

            public String getTextAlign() {
                return textAlign;
            }

            public void setTextAlign(String textAlign) {
                this.textAlign = textAlign;
            }

            public String getVerticalAlign() {
                return verticalAlign;
            }

            public void setVerticalAlign(String verticalAlign) {
                this.verticalAlign = verticalAlign;
            }

            public Integer getX() {
                return x;
            }

            public void setX(Integer x) {
                this.x = x;
            }

            public Integer getY() {
                return y;
            }

            public void setY(Integer y) {
                this.y = y;
            }
        }
    }
}
