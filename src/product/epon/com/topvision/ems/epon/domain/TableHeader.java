package com.topvision.ems.epon.domain;

public class TableHeader {

    private String header;
    private String dataIndex;
    private String align;
    private Boolean menuDisabled = false;

    /**
     * @return the header
     */
    public String getHeader() {
        return header;
    }

    /**
     * @param header
     *            the header to set
     */
    public void setHeader(String header) {
        this.header = header;
    }

    /**
     * @return the dataIndex
     */
    public String getDataIndex() {
        if (this.dataIndex.endsWith("InOctets")) {
            return this.dataIndex.replace("InOctets", "InFlow");
        } else if (this.dataIndex.endsWith("OutOctets")) {
            return this.dataIndex.replace("OutOctets", "OutFlow");
        }
        return dataIndex;
    }

    /**
     * @param dataIndex
     *            the dataIndex to set
     */
    public void setDataIndex(String dataIndex) {
        this.dataIndex = dataIndex;
    }

    /**
     * @return the align
     */
    public String getAlign() {
        return align;
    }

    /**
     * @param align
     *            the align to set
     */
    public void setAlign(String align) {
        this.align = align;
    }

    public Boolean getMenuDisabled() {
        return menuDisabled;
    }

    public void setMenuDisabled(Boolean menuDisabled) {
        this.menuDisabled = menuDisabled;
    }

}
