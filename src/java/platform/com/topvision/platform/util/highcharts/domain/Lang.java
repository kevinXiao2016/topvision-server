/***********************************************************************
 * $ Lang.java,v1.0 2012-7-12 14:25:29 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.util.highcharts.domain;

import java.util.List;
import java.io.Serializable;

/**
 * @author jay
 * @created @2012-7-12-14:25:29
 */
public class Lang implements Serializable {
    private static final long serialVersionUID = 7342180206202447869L;
    /**
     * The default decimal point used in the Highcharts.numberFormat method unless otherwise
     * specified in the function arguments. Defaults to ".".
     */
    private String decimalPoint;
    /**
     * Exporting module only. The text for the PNG download menu item. Defaults to
     * "Download PNG image".
     */
    private String downloadPNG;
    /**
     * Exporting module only. The text for the JPEG download menu item. Defaults to
     * "Download JPEG image".
     */
    private String downloadJPEG;
    /**
     * Exporting module only. The text for the PDF download menu item. Defaults to
     * "Download PDF document".
     */
    private String downloadPDF;
    /**
     * Exporting module only. The text for the SVG download menu item. Defaults to
     * "Download SVG vector image".
     */
    private String downloadSVG;
    /**
     * Exporting module only. The tooltip text for the export button. Defaults to
     * "Export to raster or vector image".
     */
    private String exportButtonTitle;
    /**
     * The loading text that appears when the chart is set into the loading state following a call
     * to chart.showLoading. Defaults to Loading....
     */
    private String loading;
    /**
     * An array containing the months names. Corresponds to the %B format in
     * Highcharts.dateFormat(). Defaults to ['January', 'February', 'March', 'April', 'May', 'June',
     * 'July', 'August', 'September', 'October', 'November', 'December'].
     */
    private List<String> months;
    /**
     * An array containing the months names in abbreviated form. Corresponds to the %b format in
     * Highcharts.dateFormat(). Defaults to ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug',
     * 'Sep', 'Oct', 'Nov', 'Dec']
     */
    private List<String> shortMonths;
    /**
     * Exporting module only. The tooltip text for the print button. Defaults to "Print the chart".
     */
    private String printButtonTitle;
    /**
     * The text for the label appearing when a chart is zoomed. Defaults to Reset zoom.
     */
    private String resetZoom;
    /**
     * The tooltip title for the label appearing when a chart is zoomed. Defaults to Reset zoom
     * level 1:1.
     */
    private String resetZoomTitle;
    /**
     * The default thousands separator used in the Highcharts.numberFormat method unless otherwise
     * specified in the function arguments. Defaults to ","
     */
    private String thousandsSep;
    /**
     * An array containing the weekday names. Defaults to ['Sunday', 'Monday', 'Tuesday',
     * 'Wednesday', 'Thursday', 'Friday', 'Saturday'].
     */
    private List<String> weekdays;

    public String getDecimalPoint() {
        return decimalPoint;
    }

    public void setDecimalPoint(String decimalPoint) {
        this.decimalPoint = decimalPoint;
    }

    public String getDownloadJPEG() {
        return downloadJPEG;
    }

    public void setDownloadJPEG(String downloadJPEG) {
        this.downloadJPEG = downloadJPEG;
    }

    public String getDownloadPDF() {
        return downloadPDF;
    }

    public void setDownloadPDF(String downloadPDF) {
        this.downloadPDF = downloadPDF;
    }

    public String getDownloadPNG() {
        return downloadPNG;
    }

    public void setDownloadPNG(String downloadPNG) {
        this.downloadPNG = downloadPNG;
    }

    public String getDownloadSVG() {
        return downloadSVG;
    }

    public void setDownloadSVG(String downloadSVG) {
        this.downloadSVG = downloadSVG;
    }

    public String getExportButtonTitle() {
        return exportButtonTitle;
    }

    public void setExportButtonTitle(String exportButtonTitle) {
        this.exportButtonTitle = exportButtonTitle;
    }

    public String getLoading() {
        return loading;
    }

    public void setLoading(String loading) {
        this.loading = loading;
    }

    public List<String> getMonths() {
        return months;
    }

    public void setMonths(List<String> months) {
        this.months = months;
    }

    public String getPrintButtonTitle() {
        return printButtonTitle;
    }

    public void setPrintButtonTitle(String printButtonTitle) {
        this.printButtonTitle = printButtonTitle;
    }

    public String getResetZoom() {
        return resetZoom;
    }

    public void setResetZoom(String resetZoom) {
        this.resetZoom = resetZoom;
    }

    public String getResetZoomTitle() {
        return resetZoomTitle;
    }

    public void setResetZoomTitle(String resetZoomTitle) {
        this.resetZoomTitle = resetZoomTitle;
    }

    public List<String> getShortMonths() {
        return shortMonths;
    }

    public void setShortMonths(List<String> shortMonths) {
        this.shortMonths = shortMonths;
    }

    public String getThousandsSep() {
        return thousandsSep;
    }

    public void setThousandsSep(String thousandsSep) {
        this.thousandsSep = thousandsSep;
    }

    public List<String> getWeekdays() {
        return weekdays;
    }

    public void setWeekdays(List<String> weekdays) {
        this.weekdays = weekdays;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Lang");
        sb.append("{decimalPoint='").append(decimalPoint).append('\'');
        sb.append(", downloadPNG='").append(downloadPNG).append('\'');
        sb.append(", downloadJPEG='").append(downloadJPEG).append('\'');
        sb.append(", downloadPDF='").append(downloadPDF).append('\'');
        sb.append(", downloadSVG='").append(downloadSVG).append('\'');
        sb.append(", exportButtonTitle='").append(exportButtonTitle).append('\'');
        sb.append(", loading='").append(loading).append('\'');
        sb.append(", months=").append(months);
        sb.append(", shortMonths=").append(shortMonths);
        sb.append(", printButtonTitle='").append(printButtonTitle).append('\'');
        sb.append(", resetZoom='").append(resetZoom).append('\'');
        sb.append(", resetZoomTitle='").append(resetZoomTitle).append('\'');
        sb.append(", thousandsSep='").append(thousandsSep).append('\'');
        sb.append(", weekdays=").append(weekdays);
        sb.append('}');
        return sb.toString();
    }
}
