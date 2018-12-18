package com.topvision.ems.network.domain;

import java.io.Serializable;

public class FolderCategory implements Serializable {
    private static final long serialVersionUID = -2397726671422698510L;
    public static final int CLASS_GRAPH = 0;
    public static final int CLASS_NETWORK = 10;
    public static final int CLASS_BUSINESS = 20;
    public static final int CLASS_MACHINEROOM = 30;

    private Integer categoryId = CLASS_GRAPH;
    private String name;
    private String note;

    /**
     * @return the categoryId
     */
    public Integer getCategoryId() {
        return categoryId;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the note
     */
    public String getNote() {
        return note;
    }

    /**
     * @param categoryId
     *            the categoryId to set
     */
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param note
     *            the note to set
     */
    public void setNote(String note) {
        this.note = note;
    }
}
