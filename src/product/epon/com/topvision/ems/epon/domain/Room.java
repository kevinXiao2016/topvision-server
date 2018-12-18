/***********************************************************************
 * $Id: Room.java,v1.0 2011-9-26 下午01:15:57 $
 * 
 * @author: zhanglongyang
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.domain;

import java.io.Serializable;
import java.util.List;

/**
 * 机房信息
 * 
 * @author zhanglongyang
 * 
 */
public class Room implements Serializable {
    private static final long serialVersionUID = -1168776896637161515L;
    // 机房名（英文）
    private String enName;
    // 机房名（中文）
    private String cnName;
    private List<Vertex> vertexList;
    private List<Rack> rackList;
    private Vertex doorPosition;
    private Integer roomWidth;
    private Integer roomHeight;
    private Integer rightWidth;
    private Integer roomMargin;
    private Integer rowMargin;
    private Integer maxRow;
    private Integer maxCol;
    private String rackIcon;

    /**
     * @return the enName
     */
    public String getEnName() {
        return enName;
    }

    /**
     * @param enName
     *            the enName to set
     */
    public void setEnName(String enName) {
        this.enName = enName;
    }

    /**
     * @return the cnName
     */
    public String getCnName() {
        return cnName;
    }

    /**
     * @param cnName
     *            the cnName to set
     */
    public void setCnName(String cnName) {
        this.cnName = cnName;
    }

    /**
     * @return the vertexList
     */
    public List<Vertex> getVertexList() {
        return vertexList;
    }

    /**
     * @param vertexList
     *            the vertexList to set
     */
    public void setVertexList(List<Vertex> vertexList) {
        this.vertexList = vertexList;
    }

    /**
     * @return the rackList
     */
    public List<Rack> getRackList() {
        return rackList;
    }

    /**
     * @param rackList
     *            the rackList to set
     */
    public void setRackList(List<Rack> rackList) {
        this.rackList = rackList;
    }

    /**
     * @return the doorPosition
     */
    public Vertex getDoorPosition() {
        return doorPosition;
    }

    public Integer getMaxRow() {
        return maxRow;
    }

    public void setMaxRow(Integer maxRow) {
        this.maxRow = maxRow;
    }

    public Integer getMaxCol() {
        return maxCol;
    }

    public void setMaxCol(Integer maxCol) {
        this.maxCol = maxCol;
    }

    /**
     * @param doorPosition
     *            the doorPosition to set
     */
    public void setDoorPosition(Vertex doorPosition) {
        this.doorPosition = doorPosition;
    }

    public Integer getRoomHeight() {
        return roomHeight;
    }

    public void setRoomHeight(Integer roomHeight) {
        this.roomHeight = roomHeight;
    }

    public Integer getRoomWidth() {
        return roomWidth;
    }

    public void setRoomWidth(Integer roomWidth) {
        this.roomWidth = roomWidth;
    }

    public String getRackIcon() {
        return rackIcon;
    }

    public void setRackIcon(String rackIcon) {
        this.rackIcon = rackIcon;
    }

    public Integer getRightWidth() {
        return rightWidth;
    }

    public void setRightWidth(Integer rightWidth) {
        this.rightWidth = rightWidth;
    }

    public Integer getRowMargin() {
        return rowMargin;
    }

    public void setRowMargin(Integer rowMargin) {
        this.rowMargin = rowMargin;
    }

    public Integer getRoomMargin() {
        return roomMargin;
    }

    public void setRoomMargin(Integer roomMargin) {
        this.roomMargin = roomMargin;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Room");
        sb.append("{cnName='").append(cnName).append('\'');
        sb.append(", enName='").append(enName).append('\'');
        sb.append(", vertexList=").append(vertexList);
        sb.append(", rackList=").append(rackList);
        sb.append(", doorPosition=").append(doorPosition);
        sb.append(", roomWidth=").append(roomWidth);
        sb.append(", roomHeight=").append(roomHeight);
        sb.append(", rightWidth=").append(rightWidth);
        sb.append(", roomMargin=").append(roomMargin);
        sb.append(", rowMargin=").append(rowMargin);
        sb.append(", maxRow=").append(maxRow);
        sb.append(", maxCol=").append(maxCol);
        sb.append(", rackIcon='").append(rackIcon).append('\'');
        sb.append('}');
        return sb.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object arg0) {
        if (this.enName.equals(((Room) arg0).enName) || this.cnName.equals(((Room) arg0).cnName)) {
            return true;
        } else {
            return false;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return 0;
    }

}
